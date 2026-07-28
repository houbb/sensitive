# 自定义策略配置增强方案

## 架构设计

### 核心思路
**包装 chars-scan**，在不修改外部库的情况下，通过配置文件支持自定义策略。

### 设计原则
1. **最小侵入**：不修改 chars-scan 库
2. **向后兼容**：默认使用 chars-scan 内置策略
3. **灵活配置**：支持配置文件指定自定义策略
4. **易于扩展**：支持所有内置策略的自定义覆盖

## 配置格式设计

### chars-scan-config.properties

```properties
# ================== chars-scan 原生配置 ==================
chars.scan.prefix=:：,，'"'"+()（）
chars.scan.scanList=1,2,3,4,9
chars.scan.replaceList=1,2,3,4,9
chars.scan.defaultReplace=12
chars.scan.replaceHash=md5
chars.scan.whiteList=""
chars.scan.showHash=true

# ================== 自定义策略配置（v1.8.0+） ==================
# 策略标识：1=手机号，2=身份证，3=银行卡，4=邮箱，5=中国人名，...

# 手机号策略（标识=1）
# 支持国际手机号（00开头）
chars.scan.strategy.1.class=com.github.houbb.sensitive.core.strategy.custom.InternationalPhoneStrategy

# 中国人名策略（标识=5）
# 支持新疆名字（4+字）
chars.scan.strategy.5.class=com.github.houbb.sensitive.core.strategy.custom.UyghurNameStrategy

# 身份证策略（标识=2）
# 用户自定义实现
chars.scan.strategy.2.class=com.example.MyIdCardStrategy

# 扫描增强配置
# 是否启用自定义策略（默认 true）
chars.scan.custom.enabled=true

# 自定义策略优先级（higher=自定义优先，native=内置优先，默认 higher）
chars.scan.custom.priority=higher
```

## 核心实现

### 1. 自定义策略接口

```java
/**
 * 自定义脱敏策略接口
 * @since 1.8.0
 */
public interface ICustomSensitiveStrategy {
    
    /**
     * 策略标识（1=手机号，2=身份证，3=银行卡，...）
     * @return 策略标识
     */
    int strategyId();
    
    /**
     * 正则匹配模式（用于识别敏感信息）
     * @return 正则表达式
     */
    String regexPattern();
    
    /**
     * 执行脱敏
     * @param original 原始字符串
     * @return 脱敏后的字符串
     */
    String desensitize(String original);
    
    /**
     * 是否启用哈希（默认跟随全局配置）
     * @return 是否启用哈希
     */
    default boolean enableHash() {
        return SensitiveConfig.showHash();
    }
}
```

### 2. 策略加载器

```java
/**
 * 自定义策略加载器
 * @since 1.8.0
 */
public final class CustomStrategyLoader {
    
    private static final Map<Integer, ICustomSensitiveStrategy> STRATEGY_MAP = new HashMap<>();
    
    private static volatile boolean initialized = false;
    
    static {
        loadStrategies();
    }
    
    /**
     * 加载自定义策略
     */
    private static void loadStrategies() {
        if (initialized) {
            return;
        }
        
        synchronized (CustomStrategyLoader.class) {
            if (initialized) {
                return;
            }
            
            try {
                Properties config = loadConfig();
                boolean enabled = Boolean.parseBoolean(
                    config.getProperty("chars.scan.custom.enabled", "true"));
                
                if (!enabled) {
                    initialized = true;
                    return;
                }
                
                // 加载策略 1-13
                for (int i = 1; i <= 13; i++) {
                    String className = config.getProperty("chars.scan.strategy." + i + ".class");
                    if (className != null) {
                        ICustomSensitiveStrategy strategy = loadStrategy(className);
                        if (strategy != null) {
                            STRATEGY_MAP.put(i, strategy);
                        }
                    }
                }
                
                initialized = true;
            } catch (Exception e) {
                // 加载失败，使用默认策略
            }
        }
    }
    
    /**
     * 加载策略实例（反射）
     */
    private static ICustomSensitiveStrategy loadStrategy(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (ICustomSensitiveStrategy) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取策略
     */
    public static ICustomSensitiveStrategy getStrategy(int strategyId) {
        return STRATEGY_MAP.get(strategyId);
    }
    
    /**
     * 是否存在自定义策略
     */
    public static boolean hasCustomStrategy(int strategyId) {
        return STRATEGY_MAP.containsKey(strategyId);
    }
}
```

### 3. 增强的扫描处理器

```java
/**
 * 增强的敏感信息扫描处理器
 * 包装 chars-scan，支持自定义策略
 * @since 1.8.0
 */
public final class EnhancedSensitiveScanner {
    
    /**
     * 扫描并替换敏感信息
     */
    public static String scanAndReplace(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 1. 先使用 chars-scan 原生扫描
        String result = CharsScanPropertyHelper.scanAndReplace(text);
        
        // 2. 应用自定义策略进行二次处理
        result = applyCustomStrategies(result, text);
        
        return result;
    }
    
    /**
     * 应用自定义策略
     */
    private static String applyCustomStrategies(String result, String original) {
        Properties config = SensitiveConfig.getConfigProperties();
        String priority = config.getProperty("chars.scan.custom.priority", "higher");
        
        if ("higher".equals(priority)) {
            // 自定义策略优先：先应用自定义，再使用 chars-scan
            return applyCustomFirst(original);
        } else {
            // chars-scan 优先：chars-scan 结果作为基础，再用自定义策略补充
            return applyNativeFirst(result);
        }
    }
    
    /**
     * 自定义策略优先
     */
    private static String applyCustomFirst(String text) {
        String result = text;
        
        // 应用自定义策略
        for (Map.Entry<Integer, ICustomSensitiveStrategy> entry : 
             CustomStrategyLoader.getStrategies().entrySet()) {
            
            ICustomSensitiveStrategy strategy = entry.getValue();
            Pattern pattern = Pattern.compile(strategy.regexPattern());
            Matcher matcher = pattern.matcher(result);
            
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String original = matcher.group();
                String desensitized = strategy.desensitize(original);
                
                // 添加哈希
                if (strategy.enableHash()) {
                    String hash = Hashes.md5().hash(original);
                    desensitized = desensitized + "|" + hash;
                }
                
                matcher.appendReplacement(sb, Matcher.quoteReplacement(desensitized));
            }
            matcher.appendTail(sb);
            result = sb.toString();
        }
        
        // 最后应用 chars-scan 内置策略（跳过已自定义的）
        result = applyNativeWithSkip(result);
        
        return result;
    }
    
    /**
     * chars-scan 优先
     */
    private static String applyNativeFirst(String text) {
        String result = text;
        
        // 对于自定义策略，只处理 chars-scan 未识别的文本
        for (Map.Entry<Integer, ICustomSensitiveStrategy> entry : 
             CustomStrategyLoader.getStrategies().entrySet()) {
            
            ICustomSensitiveStrategy strategy = entry.getValue();
            Pattern pattern = Pattern.compile(strategy.regexPattern());
            Matcher matcher = pattern.matcher(result);
            
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String original = matcher.group();
                
                // 只处理 chars-scan 未处理的文本（不包含*的）
                if (!original.contains("*")) {
                    String desensitized = strategy.desensitize(original);
                    
                    if (strategy.enableHash()) {
                        String hash = Hashes.md5().hash(original);
                        desensitized = desensitized + "|" + hash;
                    }
                    
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(desensitized));
                } else {
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(original));
                }
            }
            matcher.appendTail(sb);
            result = sb.toString();
        }
        
        return result;
    }
}
```

### 4. 内置常用自定义策略

```java
/**
 * 国际手机号策略
 * 支持 00 开头的国际手机号
 * @since 1.8.0
 */
public class InternationalPhoneStrategy implements ICustomSensitiveStrategy {
    
    @Override
    public int strategyId() {
        return 1; // 手机号策略
    }
    
    @Override
    public String regexPattern() {
        // 支持：国际号码（00开头）+ 标准国内号码（1开头）
        return "00\\d{10,15}|1[3-9]\\d{9}";
    }
    
    @Override
    public String desensitize(String original) {
        if (original == null) return null;
        
        if (original.startsWith("00")) {
            // 国际号码：008613912345678 -> 0086****5678
            if (original.length() >= 8) {
                return original.substring(0, 4) + "****" + 
                       original.substring(original.length() - 4);
            }
        } else if (original.startsWith("1") && original.length() == 11) {
            // 国内号码：13912345678 -> 139****5678
            return original.substring(0, 3) + "****" + original.substring(7);
        }
        
        return original;
    }
}

/**
 * 新疆名字策略
 * 支持长中文姓名（4+字）
 * @since 1.8.0
 */
public class UyghurNameStrategy implements ICustomSensitiveStrategy {
    
    @Override
    public int strategyId() {
        return 5; // 中国人名策略
    }
    
    @Override
    public String regexPattern() {
        // 匹配 2-10 个汉字
        return "[\\u4e00-\\u9fa5]{2,10}";
    }
    
    @Override
    public String desensitize(String original) {
        if (original == null) return null;
        
        int len = original.length();
        
        if (len <= 1) {
            return original;
        } else if (len <= 3) {
            // 标准：张三 -> 张*
            return original.charAt(0) + "*";
        } else {
            // 长名字：阿里木江·买买提 -> 阿里木*******
            return original.substring(0, 3) + 
                   original.substring(3).replaceAll(".", "*");
        }
    }
}
```

## 日志插件集成

### 修改 SensitivePatternLayout

```java
@Override
public String toSerializable(LogEvent event) {
    StringBuilder stringBuilder = new StringBuilder();
    for(PatternFormatter formatter : patternFormatterList) {
        formatter.format(event, stringBuilder);
    }
    String text = stringBuilder.toString();
    
    try {
        // 使用增强的扫描处理器
        return EnhancedSensitiveScanner.scanAndReplace(text);
    } catch (Exception e) {
        return text;
    }
}
```

## 使用示例

### 1. 配置文件

```properties
# chars-scan-config.properties

# 启用自定义策略
chars.scan.custom.enabled=true
chars.scan.custom.priority=higher

# 国际手机号
chars.scan.strategy.1.class=com.github.houbb.sensitive.core.strategy.custom.InternationalPhoneStrategy

# 新疆名字
chars.scan.strategy.5.class=com.github.houbb.sensitive.core.strategy.custom.UyghurNameStrategy
```

### 2. 自定义策略

```java
public class MyCustomPhoneStrategy implements ICustomSensitiveStrategy {
    
    @Override
    public int strategyId() {
        return 1;
    }
    
    @Override
    public String regexPattern() {
        return "我的自定义正则";
    }
    
    @Override
    public String desensitize(String original) {
        // 我的自定义逻辑
        return "****";
    }
}
```

### 3. 配置使用

```properties
chars.scan.strategy.1.class=com.example.MyCustomPhoneStrategy
```

## 实施步骤

### Phase 1: 核心框架（必须）
1. 创建 `ICustomSensitiveStrategy` 接口
2. 实现 `CustomStrategyLoader` 加载器
3. 实现 `EnhancedSensitiveScanner` 处理器
4. 修改日志插件，使用增强处理器

### Phase 2: 内置策略（推荐）
1. 实现 `InternationalPhoneStrategy`
2. 实现 `UyghurNameStrategy`
3. 提供其他常用策略

### Phase 3: 文档和测试
1. 编写使用文档
2. 编写单元测试
3. 提供示例代码

## 优势

1. **零侵入**：不修改 chars-scan 库
2. **向后兼容**：默认行为不变
3. **灵活配置**：支持配置文件指定策略
4. **易于扩展**：只需实现接口并配置
5. **优先级控制**：支持自定义优先或内置优先

## 注意事项

1. **性能**：自定义策略会增加正则匹配开销
2. **冲突**：注意自定义正则与内置策略的冲突
3. **测试**：务必测试自定义策略的正确性
4. **文档**：提供详细的配置说明和示例