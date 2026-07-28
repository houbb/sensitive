# 基于 CharsScanBs 的最佳扩展方案

## chars-scan 扩展机制分析

### 核心架构

```
CharsScanBs (引导类)
├── charsScanFactory (扫描策略工厂) - 控制识别规则
├── charsReplaceFactory (替换策略工厂) - 控制脱敏逻辑
├── charsReplaceHash (哈希策略) - 控制哈希显示
├── prefixCharSet (前缀字符集合) - 控制触发条件
├── whiteListTrie (白名单前缀树) - 控制跳过规则
└── charsCore (核心实现) - 控制并发安全等
```

### 关键扩展点

1. **扫描策略工厂** (`ICharsScanFactory`)
   - 通过 `SimpleCharsScanFactory` 指定使用的策略
   - 示例：`new SimpleCharsScanFactory(Arrays.asList("1", "2", "3", "4", "5"))`

2. **替换策略工厂** (`ICharsReplaceFactory`)
   - 可以自定义替换逻辑
   - 支持 `SingletonCharsReplaceFactory` 管理策略实例

3. **哈希策略** (`ICharsReplaceHash`)
   - `CharsReplaceHashes.md5()` - 显示哈希
   - `CharsReplaceHashes.none()` - 不显示哈希

## 最佳扩展方案

### 1. 统一配置文件设计

**chars-scan-config.properties**

```properties
# ================== 核心配置 ==================
# 扫描策略列表（1=手机号,2=身份证,3=银行卡,4=邮箱,5=中国人名,...）
chars.scan.scanList=1,2,3,4,5,9

# 替换策略列表（对应扫描策略）
chars.scan.replaceList=1,2,3,4,5,9

# 默认替换策略（未匹配时使用）
chars.scan.defaultReplace=12

# 哈希策略（md5/none）
chars.scan.replaceHash=md5

# 是否显示哈希值
chars.scan.showHash=true

# 前缀字符集合（触发识别的条件）
chars.scan.prefix=:：,，'"'"+()（）

# 白名单（跳过脱敏的文本）
chars.scan.whiteList=

# ================== 高级配置 ==================
# 扫描起始位置（默认0）
chars.scan.scanStartIndex=0

# 核心实现（defaults/concurrency/threadLocal）
chars.scan.core=defaults

# ================== 自定义策略配置（v1.8.0+） ==================
# 手机号策略自定义实现类
chars.scan.custom.scan.1.class=com.example.MyPhoneScan
chars.scan.custom.replace.1.class=com.example.MyPhoneReplace

# 中国人名策略自定义实现类
chars.scan.custom.scan.5.class=com.example.MyChineseNameScan
chars.scan.custom.replace.5.class=com.example.MyChineseNameReplace

# 自定义策略是否覆盖内置策略（true=覆盖，false=共存）
chars.scan.custom.override=true
```

### 2. 统一上下文管理器

**SensitiveScanBsContext.java**

```java
package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.replace.factory.CharsReplaceFactory;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 敏感信息扫描上下文管理器
 * 统一管理 log4j2/logback 的配置
 * @since 1.8.0
 */
public final class SensitiveScanBsContext {
    
    private SensitiveScanBsContext() {
    }
    
    private static volatile CharsScanBs charsScanBs;
    private static volatile boolean initialized = false;
    
    private static final String CONFIG_FILE_NAME = "chars-scan-config.properties";
    private static final String CONFIG_KEY_PREFIX = "chars.scan.";
    
    /**
     * 初始化上下文
     */
    public static void init() {
        if (initialized) {
            return;
        }
        
        synchronized (SensitiveScanBsContext.class) {
            if (initialized) {
                return;
            }
            
            Properties config = loadConfig();
            
            // 构建扫描策略工厂
            ICharsScanFactory scanFactory = buildScanFactory(config);
            
            // 构建替换策略工厂
            ICharsReplaceFactory replaceFactory = buildReplaceFactory(config);
            
            // 构建哈希策略
            ICharsReplaceHash replaceHash = buildReplaceHash(config);
            
            // 构建核心实现
            ICharsCore charsCore = buildCharsCore(config);
            
            // 构建前缀字符集合
            Set<Character> prefixCharSet = buildPrefixCharSet(config);
            
            // 构建白名单
            ITrieTree whiteListTrie = buildWhiteListTrie(config);
            
            // 构建引导类
            charsScanBs = CharsScanBs.newInstance()
                    .charsScanFactory(scanFactory)
                    .charsReplaceFactory(replaceFactory)
                    .charsReplaceHash(replaceHash)
                    .charsCore(charsCore)
                    .prefixCharSet(prefixCharSet)
                    .whiteListTrie(whiteListTrie)
                    .scanStartIndex(getInt(config, "scanStartIndex", 0))
                    .init();
            
            initialized = true;
        }
    }
    
    /**
     * 执行扫描和替换
     */
    public static String scanAndReplace(String text) {
        init();
        
        if (charsScanBs == null) {
            return text;
        }
        
        try {
            return charsScanBs.scanAndReplace(text);
        } catch (Exception e) {
            return text;
        }
    }
    
    /**
     * 获取 CharsScanBs 实例
     */
    public static CharsScanBs getCharsScanBs() {
        init();
        return charsScanBs;
    }
    
    // ==================== 构建方法 ====================
    
    private static ICharsScanFactory buildScanFactory(Properties config) {
        String scanListStr = config.getProperty(CONFIG_KEY_PREFIX + "scanList", "1,2,3,4,5,9");
        List<String> scanTypes = Arrays.stream(scanListStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        
        // 检查是否有自定义扫描策略
        Map<String, ICharsScan> customScans = loadCustomScans(config, scanTypes);
        
        if (customScans.isEmpty()) {
            // 使用默认工厂
            return new SimpleCharsScanFactory(scanTypes);
        } else {
            // 使用自定义工厂
            return new CustomCharsScanFactory(scanTypes, customScans, config);
        }
    }
    
    private static ICharsReplaceFactory buildReplaceFactory(Properties config) {
        String replaceListStr = config.getProperty(CONFIG_KEY_PREFIX + "replaceList", "1,2,3,4,5,9");
        List<String> replaceTypes = Arrays.stream(replaceListStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        
        // 检查是否有自定义替换策略
        Map<String, ICharsReplace> customReplaces = loadCustomReplaces(config, replaceTypes);
        
        if (customReplaces.isEmpty()) {
            // 使用默认工厂
            return CharsReplaces.defaults();
        } else {
            // 使用自定义工厂
            return new CustomCharsReplaceFactory(replaceTypes, customReplaces, config);
        }
    }
    
    private static ICharsReplaceHash buildReplaceHash(Properties config) {
        String showHash = config.getProperty(CONFIG_KEY_PREFIX + "showHash", "true");
        
        if ("false".equalsIgnoreCase(showHash)) {
            return CharsReplaceHashes.none();
        }
        
        String replaceHash = config.getProperty(CONFIG_KEY_PREFIX + "replaceHash", "md5");
        
        if ("md5".equalsIgnoreCase(replaceHash)) {
            return CharsReplaceHashes.md5();
        } else {
            return CharsReplaceHashes.none();
        }
    }
    
    private static ICharsCore buildCharsCore(Properties config) {
        String coreType = config.getProperty(CONFIG_KEY_PREFIX + "core", "defaults");
        
        switch (coreType.toLowerCase()) {
            case "concurrency":
                return CharsCores.concurrency();
            case "threadlocal":
                return CharsCores.threadLocal();
            default:
                return CharsCores.defaults();
        }
    }
    
    private static Set<Character> buildPrefixCharSet(Properties config) {
        String prefixStr = config.getProperty(CONFIG_KEY_PREFIX + "prefix", ":：,，'"'"+()（）");
        
        Set<Character> prefixCharSet = new HashSet<>();
        for (char c : prefixStr.toCharArray()) {
            prefixCharSet.add(c);
        }
        
        return prefixCharSet;
    }
    
    private static ITrieTree buildWhiteListTrie(Properties config) {
        String whiteListStr = config.getProperty(CONFIG_KEY_PREFIX + "whiteList", "");
        
        ITrieTree trieTree = TrieTrees.node();
        
        if (StringUtil.isNotEmpty(whiteListStr)) {
            String[] whiteList = whiteListStr.split(",");
            for (String word : whiteList) {
                if (StringUtil.isNotEmpty(word.trim())) {
                    trieTree.insert(word.trim());
                }
            }
        }
        
        return trieTree;
    }
    
    // ==================== 自定义策略加载 ====================
    
    private static Map<String, ICharsScan> loadCustomScans(Properties config, List<String> scanTypes) {
        Map<String, ICharsScan> customScans = new HashMap<>();
        
        for (String scanType : scanTypes) {
            String className = config.getProperty(CONFIG_KEY_PREFIX + "custom.scan." + scanType + ".class");
            if (StringUtil.isNotEmpty(className)) {
                try {
                    Class<?> clazz = Class.forName(className.trim());
                    ICharsScan scan = (ICharsScan) clazz.getDeclaredConstructor().newInstance();
                    customScans.put(scanType, scan);
                } catch (Exception e) {
                    // 加载失败，使用默认策略
                }
            }
        }
        
        return customScans;
    }
    
    private static Map<String, ICharsReplace> loadCustomReplaces(Properties config, List<String> replaceTypes) {
        Map<String, ICharsReplace> customReplaces = new HashMap<>();
        
        for (String replaceType : replaceTypes) {
            String className = config.getProperty(CONFIG_KEY_PREFIX + "custom.replace." + replaceType + ".class");
            if (StringUtil.isNotEmpty(className)) {
                try {
                    Class<?> clazz = Class.forName(className.trim());
                    ICharsReplace replace = (ICharsReplace) clazz.getDeclaredConstructor().newInstance();
                    customReplaces.put(replaceType, replace);
                } catch (Exception e) {
                    // 加载失败，使用默认策略
                }
            }
        }
        
        return customReplaces;
    }
    
    // ==================== 配置加载 ====================
    
    private static Properties loadConfig() {
        Properties config = new Properties();
        try (InputStream inputStream = SensitiveScanBsContext.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE_NAME)) {
            if (inputStream != null) {
                config.load(inputStream);
            }
        } catch (IOException e) {
            // 配置文件不存在或加载失败，使用默认值
        }
        return config;
    }
    
    private static int getInt(Properties config, String key, int defaultValue) {
        String value = config.getProperty(CONFIG_KEY_PREFIX + key);
        if (StringUtil.isNotEmpty(value)) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * 重新加载配置
     */
    public static void reload() {
        initialized = false;
        init();
    }
}
```

### 3. 自定义策略工厂

**CustomCharsScanFactory.java**

```java
package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;

import java.util.*;

/**
 * 自定义扫描策略工厂
 * 支持用户自定义策略覆盖内置策略
 * @since 1.8.0
 */
public class CustomCharsScanFactory implements ICharsScanFactory {
    
    private final SimpleCharsScanFactory defaultFactory;
    private final Map<String, ICharsScan> customScans;
    private final boolean override;
    
    public CustomCharsScanFactory(List<String> scanTypes, 
                                  Map<String, ICharsScan> customScans,
                                  Properties config) {
        this.defaultFactory = new SimpleCharsScanFactory(scanTypes);
        this.customScans = customScans;
        this.override = Boolean.parseBoolean(
            config.getProperty("chars.scan.custom.override", "true"));
    }
    
    @Override
    public Map<String, ICharsScan> getCharsScanMap() {
        Map<String, ICharsScan> scanMap = new HashMap<>();
        
        if (override) {
            // 自定义策略优先：先加载内置，再用自定义覆盖
            scanMap.putAll(defaultFactory.getCharsScanMap());
            scanMap.putAll(customScans);
        } else {
            // 内置策略优先：先加载自定义，再用内置覆盖
            scanMap.putAll(customScans);
            scanMap.putAll(defaultFactory.getCharsScanMap());
        }
        
        return scanMap;
    }
}
```

### 4. 日志插件集成

**SensitivePatternLayout.java (log4j2)**

```java
@Override
public String toSerializable(LogEvent event) {
    StringBuilder stringBuilder = new StringBuilder();
    for(PatternFormatter formatter : patternFormatterList) {
        formatter.format(event, stringBuilder);
    }
    String text = stringBuilder.toString();
    
    try {
        // 使用统一上下文
        return SensitiveScanBsContext.scanAndReplace(text);
    } catch (Exception e) {
        return text;
    }
}
```

**SensitiveLogbackLayout.java (logback)**

```java
@Override
public String doLayout(ILoggingEvent event) {
    String text = event.getFormattedMessage();
    
    try {
        // 使用统一上下文
        return SensitiveScanBsContext.scanAndReplace(text);
    } catch (Exception e) {
        return text;
    }
}
```

### 5. 自定义策略示例

**国际手机号扫描策略**

```java
package com.example.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.scan.AbstractConditionCharScan;

/**
 * 国际手机号扫描策略
 * 支持 00 开头的国际号码
 */
public class InternationalPhoneScan extends AbstractConditionCharScan {
    
    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return Character.isDigit(c);
    }
    
    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, CharsScanContext context) {
        StringBuilder buffer = getBuffer();
        int bufferLen = buffer.length();
        
        // 支持 11 位国内号码 + 10-16 位国际号码（00开头）
        if (bufferLen >= 11 && bufferLen <= 16) {
            String phone = buffer.toString();
            return phone.startsWith("00") || phone.startsWith("1");
        }
        
        return false;
    }
    
    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PHONE.getScanType(); // 使用手机号类型
    }
    
    @Override
    public int getPriority() {
        return CharsScanTypeEnum.PHONE.getPriority();
    }
}
```

**国际手机号替换策略**

```java
package com.example.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.replace.AbstractRangeCharReplace;

/**
 * 国际手机号替换策略
 */
public class InternationalPhoneReplace extends AbstractRangeCharReplace {
    
    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PHONE.getScanType();
    }
    
    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem item) {
        String phone = new String(chars, item.getStartIndex(), itemLen);
        
        if (phone.startsWith("00")) {
            // 国际号码：008613912345678 -> 0086****5678
            return item.getStartIndex() + 4;
        } else {
            // 国内号码：13912345678 -> 139****5678
            return item.getStartIndex() + 3;
        }
    }
    
    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem item) {
        return item.getEndIndex() - 4;
    }
}
```

## 配置使用示例

### chars-scan-config.properties

```properties
# 基础配置
chars.scan.scanList=1,2,3,4,5,9
chars.scan.replaceList=1,2,3,4,5,9
chars.scan.showHash=true

# 自定义国际手机号策略
chars.scan.custom.scan.1.class=com.example.scan.InternationalPhoneScan
chars.scan.custom.replace.1.class=com.example.replace.InternationalPhoneReplace
chars.scan.custom.override=true
```

## 方案优势

### 1. 统一配置管理
- log4j2 和 logback 共享同一套配置
- 配置集中管理，避免重复

### 2. 高扩展性
- 支持自定义扫描策略（识别规则）
- 支持自定义替换策略（脱敏逻辑）
- 支持覆盖内置策略

### 3. 零侵入
- 不修改 chars-scan 库
- 利用 chars-scan 的扩展机制

### 4. 性能优化
- 单例模式管理 CharsScanBs
- 支持多种核心实现（defaults/concurrency/threadLocal）

### 5. 易于使用
- 配置文件驱动
- 反射加载自定义策略
- 无需修改代码