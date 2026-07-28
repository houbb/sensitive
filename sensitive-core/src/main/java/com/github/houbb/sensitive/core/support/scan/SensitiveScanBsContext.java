package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 敏感信息扫描上下文管理器
 * <p>
 * 统一管理 log4j2/logback 的配置，基于 chars-scan 的 CharsScanBs 实现
 * 
 * @author dh
 * @since 1.9.0
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

            try {
                Properties config = loadConfig();
                System.out.println("[DEBUG] 配置文件加载完成，配置项数量: " + config.size());

                // 构建扫描策略工厂
                ICharsScanFactory scanFactory = buildScanFactory(config);
                System.out.println("[DEBUG] 扫描策略工厂: " + scanFactory.getClass().getName());

                // 暂时不传入替换策略工厂和哈希策略，看看是否能正常工作
                // ICharsReplaceFactory replaceFactory = buildReplaceFactory(config);
                // System.out.println("[DEBUG] 替换策略工厂: " + replaceFactory.getClass().getName());
                // ICharsReplaceHash replaceHash = buildReplaceHash(config);
                // System.out.println("[DEBUG] 哈希策略: " + replaceHash.getClass().getName());

                // 构建核心实现
                ICharsCore charsCore = buildCharsCore(config);
                System.out.println("[DEBUG] 核心实现: " + charsCore.getClass().getName());

                // 构建前缀字符集合
                Set<Character> prefixCharSet = buildPrefixCharSet(config);
                System.out.println("[DEBUG] 前缀字符集合大小: " + prefixCharSet.size());

                // 构建白名单
                ITrieTree whiteListTrie = buildWhiteListTrie(config);

                // 构建引导类
                charsScanBs = CharsScanBs.newInstance()
                        .charsScanFactory(scanFactory)
                        // .charsReplaceFactory(replaceFactory)
                        // .charsReplaceHash(replaceHash)
                        .charsCore(charsCore)
                        .prefixCharSet(prefixCharSet)
                        .whiteListTrie(whiteListTrie)
                        .scanStartIndex(getInt(config, "scanStartIndex", 0))
                        .init();

                System.out.println("[DEBUG] CharsScanBs 初始化成功");
            } catch (Exception e) {
                // 初始化失败，使用默认配置
                System.err.println("[ERROR] 初始化失败: " + e.getMessage());
                e.printStackTrace();
                charsScanBs = CharsScanBs.newInstance().init();
            }

            initialized = true;
        }
    }

    /**
     * 执行扫描和替换
     * 
     * @param text 文本
     * @return 脱敏后的结果
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
     * 
     * @return CharsScanBs 实例
     */
    public static CharsScanBs getCharsScanBs() {
        init();
        return charsScanBs;
    }

    /**
     * 重新加载配置
     */
    public static void reload() {
        initialized = false;
        charsScanBs = null;
        init();
    }

    // ==================== 构建方法 ====================

    private static ICharsScanFactory buildScanFactory(Properties config) {
        String scanListStr = config.getProperty(CONFIG_KEY_PREFIX + "scanList", "1,2,3,4,5,9");
        List<String> scanTypes = Arrays.stream(scanListStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        System.out.println("[DEBUG] 扫描类型列表: " + scanTypes);

        // 检查是否有自定义扫描策略
        Map<String, ICharsScan> customScans = loadCustomScans(config, scanTypes);

        if (customScans.isEmpty()) {
            // 使用默认工厂，需要传入 scanTypes 参数！
            return new com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory(scanTypes);
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
        String prefixStr = config.getProperty(CONFIG_KEY_PREFIX + "prefix", ":：,，'\"'\"()+()（）");

        System.out.println("[DEBUG] 前缀字符串: " + prefixStr);
        System.out.println("[DEBUG] 前缀字符串长度: " + prefixStr.length());

        Set<Character> prefixCharSet = new HashSet<>();
        for (char c : prefixStr.toCharArray()) {
            prefixCharSet.add(c);
        }

        System.out.println("[DEBUG] 前缀字符集合内容: " + prefixCharSet);

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

}