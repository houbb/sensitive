package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsCore;
import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.constant.CharsScanConfigConst;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 根据配置构建 {@link CharsScanBs}。
 *
 * <p>配置项与 {@code CharsScanBs} 的构建方法一一对应。用户既可以替换完整组件，
 * 也可以通过类列表向默认扫描/替换工厂追加单个实现。</p>
 *
 * @author dh
 * @since 1.9.0
 */
public final class SensitiveScanBsBuilder {

    private SensitiveScanBsBuilder() {
    }

    public static final String KEY_CHARS_SCAN_FACTORY_CLASS =
            "chars.scan.charsScanFactory.class";
    public static final String KEY_CHARS_REPLACE_FACTORY_CLASS =
            "chars.scan.charsReplaceFactory.class";
    public static final String KEY_CHARS_CORE_CLASS =
            "chars.scan.charsCore.class";
    public static final String KEY_CHARS_REPLACE_HASH_CLASS =
            "chars.scan.charsReplaceHash.class";
    public static final String KEY_WHITE_LIST_TRIE_CLASS =
            "chars.scan.whiteListTrie.class";

    public static final String KEY_BUILT_IN_SCAN_TYPES =
            "chars.scan.builtIn.scanTypes";
    public static final String KEY_BUILT_IN_REPLACE_TYPES =
            "chars.scan.builtIn.replaceTypes";
    public static final String KEY_DEFAULT_REPLACE_TYPE =
            "chars.scan.builtIn.defaultReplaceType";
    public static final String KEY_CUSTOM_SCAN_CLASSES =
            "chars.scan.custom.scans";
    public static final String KEY_CUSTOM_REPLACE_CLASSES =
            "chars.scan.custom.replaces";
    public static final String KEY_CUSTOM_OVERRIDE =
            "chars.scan.custom.override";

    public static final String KEY_CHARS_CORE =
            "chars.scan.charsCore";
    public static final String KEY_CHARS_CORE_THREAD_SIZE =
            "chars.scan.charsCore.threadSize";
    public static final String KEY_CHARS_REPLACE_HASH =
            "chars.scan.charsReplaceHash";
    public static final String KEY_SHOW_HASH =
            "chars.scan.showHash";
    public static final String KEY_PREFIX_CHAR_SET =
            "chars.scan.prefixCharSet";
    public static final String KEY_ESCAPE_PREFIX_CHAR_SET =
            "chars.scan.escapePrefixCharSet";
    public static final String KEY_WHITE_LIST =
            "chars.scan.whiteList";
    public static final String KEY_SCAN_START_INDEX =
            "chars.scan.scanStartIndex";

    private static final String LEGACY_SCAN_TYPES = "chars.scan.scanList";
    private static final String LEGACY_REPLACE_TYPES = "chars.scan.replaceList";
    private static final String LEGACY_DEFAULT_REPLACE = "chars.scan.defaultReplace";
    private static final String LEGACY_CORE = "chars.scan.core";
    private static final String LEGACY_REPLACE_HASH = "chars.scan.replaceHash";
    private static final String LEGACY_PREFIX = "chars.scan.prefix";

    public static final String DEFAULT_BUILT_IN_SCAN_TYPES = "1,2,3,4,5,9";
    public static final String DEFAULT_BUILT_IN_REPLACE_TYPES = "1,2,3,4,5,9";
    public static final String DEFAULT_REPLACE_TYPE = "12";

    private static final Set<String> BUILT_IN_SCAN_TYPES =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "m1", "m2", "m3"
            )));
    private static final Set<String> BUILT_IN_REPLACE_TYPES =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                    "11", "12", "13"
            )));

    /**
     * 构建并初始化扫描器。
     *
     * @param config 配置；允许为空
     * @return 初始化后的扫描器
     */
    public static CharsScanBs build(Properties config) {
        Properties actualConfig = config == null ? new Properties() : config;

        return CharsScanBs.newInstance()
                .charsCore(buildCharsCore(actualConfig))
                .charsScanFactory(buildCharsScanFactory(actualConfig))
                .charsReplaceFactory(buildCharsReplaceFactory(actualConfig))
                .charsReplaceHash(buildCharsReplaceHash(actualConfig))
                .prefixCharSet(buildPrefixCharSet(actualConfig))
                .escapePrefixCharSet(buildEscapePrefixCharSet(actualConfig))
                .whiteListTrie(buildWhiteListTrie(actualConfig))
                .scanStartIndex(getInt(
                        actualConfig,
                        KEY_SCAN_START_INDEX,
                        CharsScanConfigConst.DEFAULT_START_INDEX))
                .init();
    }

    private static ICharsScanFactory buildCharsScanFactory(Properties config) {
        ICharsScanFactory configuredFactory = newConfiguredInstance(
                config, KEY_CHARS_SCAN_FACTORY_CLASS, ICharsScanFactory.class);
        if (configuredFactory != null) {
            return configuredFactory;
        }

        List<ICharsScan> customScans = loadConfiguredInstances(
                config, KEY_CUSTOM_SCAN_CLASSES, ICharsScan.class);
        customScans.addAll(loadLegacyCustomScans(config));

        Map<String, ICharsScan> customScanMap = indexScans(customScans);
        List<String> configuredTypes = split(getProperty(
                config, KEY_BUILT_IN_SCAN_TYPES, LEGACY_SCAN_TYPES,
                DEFAULT_BUILT_IN_SCAN_TYPES));
        List<String> builtInTypes = new ArrayList<String>();
        for (String type : configuredTypes) {
            if (isBuiltInScanType(type)) {
                builtInTypes.add(type);
            } else if (!customScanMap.containsKey(type)) {
                throw new IllegalArgumentException(
                        "Unsupported built-in scan type: " + type
                                + ". Configure the implementation with "
                                + KEY_CUSTOM_SCAN_CLASSES + '.');
            }
        }

        ICharsScanFactory builtInFactory = CharsScans.defaults(builtInTypes);
        if (customScanMap.isEmpty()) {
            return builtInFactory;
        }

        return new CustomCharsScanFactory(
                builtInFactory, customScanMap.values(),
                getBoolean(config, KEY_CUSTOM_OVERRIDE, true));
    }

    private static ICharsReplaceFactory buildCharsReplaceFactory(Properties config) {
        ICharsReplaceFactory configuredFactory = newConfiguredInstance(
                config, KEY_CHARS_REPLACE_FACTORY_CLASS, ICharsReplaceFactory.class);
        if (configuredFactory != null) {
            return configuredFactory;
        }

        List<ICharsReplace> customReplaces = loadConfiguredInstances(
                config, KEY_CUSTOM_REPLACE_CLASSES, ICharsReplace.class);
        customReplaces.addAll(loadLegacyCustomReplaces(config));

        Map<String, ICharsReplace> customReplaceMap = indexReplaces(customReplaces);
        List<String> configuredTypes = split(getProperty(
                config, KEY_BUILT_IN_REPLACE_TYPES, LEGACY_REPLACE_TYPES,
                DEFAULT_BUILT_IN_REPLACE_TYPES));
        List<String> builtInTypes = new ArrayList<String>();
        for (String type : configuredTypes) {
            if (BUILT_IN_REPLACE_TYPES.contains(type)) {
                builtInTypes.add(type);
            } else if (!customReplaceMap.containsKey(type)) {
                throw new IllegalArgumentException(
                        "Unsupported built-in replace type: " + type
                                + ". Configure the implementation with "
                                + KEY_CUSTOM_REPLACE_CLASSES + '.');
            }
        }

        String defaultReplaceType = getProperty(
                config, KEY_DEFAULT_REPLACE_TYPE, LEGACY_DEFAULT_REPLACE,
                DEFAULT_REPLACE_TYPE).trim();
        boolean customOverride =
                getBoolean(config, KEY_CUSTOM_OVERRIDE, true);
        ICharsReplace configuredCustomDefault =
                customReplaceMap.get(defaultReplaceType);
        if (!BUILT_IN_REPLACE_TYPES.contains(defaultReplaceType)
                && configuredCustomDefault == null) {
            throw new IllegalArgumentException(
                    "Unsupported default replace type: " + defaultReplaceType
                            + ". Configure the implementation with "
                            + KEY_CUSTOM_REPLACE_CLASSES + '.');
        }
        ICharsReplace customDefaultReplace =
                configuredCustomDefault != null
                        && (customOverride
                        || !BUILT_IN_REPLACE_TYPES.contains(defaultReplaceType))
                        ? configuredCustomDefault
                        : null;

        String builtInDefaultReplaceType =
                BUILT_IN_REPLACE_TYPES.contains(defaultReplaceType)
                        ? defaultReplaceType
                        : DEFAULT_REPLACE_TYPE;
        ICharsReplaceFactory builtInFactory =
                CharsReplaces.defaultsReplaceFactory(
                        builtInTypes, builtInDefaultReplaceType);
        if (customReplaceMap.isEmpty()) {
            return builtInFactory;
        }

        return new CustomCharsReplaceFactory(
                builtInFactory, builtInTypes, customReplaceMap.values(),
                customDefaultReplace,
                customOverride);
    }

    private static ICharsCore buildCharsCore(Properties config) {
        ICharsCore configuredCore = newConfiguredInstance(
                config, KEY_CHARS_CORE_CLASS, ICharsCore.class);
        if (configuredCore != null) {
            return configuredCore;
        }

        String type = getProperty(
                config, KEY_CHARS_CORE, LEGACY_CORE, "defaults").trim();
        if ("concurrency".equalsIgnoreCase(type)) {
            int threadSize = getInt(config, KEY_CHARS_CORE_THREAD_SIZE, 10);
            return CharsCores.concurrency(threadSize);
        }
        if ("threadLocal".equalsIgnoreCase(type)) {
            return CharsCores.threadLocal();
        }
        if ("common".equalsIgnoreCase(type) || "defaults".equalsIgnoreCase(type)) {
            return CharsCores.defaults();
        }

        throw new IllegalArgumentException("Unsupported chars core: " + type);
    }

    private static ICharsReplaceHash buildCharsReplaceHash(Properties config) {
        if (!getBoolean(config, KEY_SHOW_HASH, true)) {
            return CharsReplaceHashes.none();
        }

        ICharsReplaceHash configuredHash = newConfiguredInstance(
                config, KEY_CHARS_REPLACE_HASH_CLASS, ICharsReplaceHash.class);
        if (configuredHash != null) {
            return configuredHash;
        }

        String type = getProperty(
                config, KEY_CHARS_REPLACE_HASH, LEGACY_REPLACE_HASH, "md5").trim();
        return CharsReplaceHashes.newInstance(type);
    }

    private static Set<Character> buildPrefixCharSet(Properties config) {
        String prefix = getProperty(
                config, KEY_PREFIX_CHAR_SET, LEGACY_PREFIX,
                CharsScanConfigConst.DEFAULT_PREFIX);
        return InnerCharUtil.getCharSet(prefix);
    }

    private static Set<Character> buildEscapePrefixCharSet(Properties config) {
        String escapePrefix = getProperty(
                config, KEY_ESCAPE_PREFIX_CHAR_SET, null,
                CharsScanConfigConst.DEFAULT_ESCAPE_PREFIX);
        return InnerCharUtil.getEscapeCharSet(escapePrefix);
    }

    private static ITrieTree buildWhiteListTrie(Properties config) {
        ITrieTree trieTree = newConfiguredInstance(
                config, KEY_WHITE_LIST_TRIE_CLASS, ITrieTree.class);
        if (trieTree == null) {
            trieTree = TrieTrees.node();
        }

        for (String word : split(getProperty(config, KEY_WHITE_LIST, null, ""))) {
            trieTree.insert(word);
        }
        return trieTree;
    }

    private static List<ICharsScan> loadLegacyCustomScans(Properties config) {
        List<ICharsScan> result = new ArrayList<ICharsScan>();
        for (String type : split(getProperty(
                config, KEY_BUILT_IN_SCAN_TYPES, LEGACY_SCAN_TYPES,
                DEFAULT_BUILT_IN_SCAN_TYPES))) {
            String className = trimToNull(
                    config.getProperty("chars.scan.custom.scan." + type + ".class"));
            if (className != null) {
                result.add(newInstance(className, ICharsScan.class));
            }
        }
        return result;
    }

    private static List<ICharsReplace> loadLegacyCustomReplaces(Properties config) {
        List<ICharsReplace> result = new ArrayList<ICharsReplace>();
        for (String type : split(getProperty(
                config, KEY_BUILT_IN_REPLACE_TYPES, LEGACY_REPLACE_TYPES,
                DEFAULT_BUILT_IN_REPLACE_TYPES))) {
            String className = trimToNull(
                    config.getProperty("chars.scan.custom.replace." + type + ".class"));
            if (className != null) {
                result.add(newInstance(className, ICharsReplace.class));
            }
        }
        return result;
    }

    private static boolean isBuiltInScanType(String type) {
        String baseType = type;
        int parameterIndex = type.indexOf(':');
        if (parameterIndex >= 0) {
            baseType = type.substring(0, parameterIndex);
        }
        return BUILT_IN_SCAN_TYPES.contains(baseType);
    }

    private static Map<String, ICharsScan> indexScans(Collection<ICharsScan> scans) {
        Map<String, ICharsScan> result = new LinkedHashMap<String, ICharsScan>();
        for (ICharsScan scan : scans) {
            String type = trimToNull(scan.getScanType());
            if (type == null) {
                throw new IllegalArgumentException(
                        scan.getClass().getName() + " returned an empty scan type");
            }
            if (result.put(type, scan) != null) {
                throw new IllegalArgumentException("Duplicate custom scan type: " + type);
            }
        }
        return result;
    }

    private static Map<String, ICharsReplace> indexReplaces(
            Collection<ICharsReplace> replaces) {
        Map<String, ICharsReplace> result =
                new LinkedHashMap<String, ICharsReplace>();
        for (ICharsReplace replace : replaces) {
            String type = trimToNull(replace.getScanType());
            if (type == null) {
                throw new IllegalArgumentException(
                        replace.getClass().getName() + " returned an empty scan type");
            }
            if (result.put(type, replace) != null) {
                throw new IllegalArgumentException("Duplicate custom replace type: " + type);
            }
        }
        return result;
    }

    private static <T> T newConfiguredInstance(
            Properties config, String key, Class<T> expectedType) {
        String className = trimToNull(config.getProperty(key));
        return className == null ? null : newInstance(className, expectedType);
    }

    private static <T> List<T> loadConfiguredInstances(
            Properties config, String key, Class<T> expectedType) {
        String classNames = config.getProperty(key, "");
        List<T> result = new ArrayList<T>();
        for (String className : split(classNames)) {
            result.add(newInstance(className, expectedType));
        }
        return result;
    }

    private static <T> T newInstance(String className, Class<T> expectedType) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = SensitiveScanBsBuilder.class.getClassLoader();
            }

            Class<?> implementationClass =
                    Class.forName(className, true, classLoader);
            if (!expectedType.isAssignableFrom(implementationClass)) {
                throw new IllegalArgumentException(
                        className + " must implement " + expectedType.getName());
            }

            Object instance = implementationClass.getConstructor().newInstance();
            return expectedType.cast(instance);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot create " + expectedType.getSimpleName()
                            + " from " + className, e);
        }
    }

    private static String getProperty(
            Properties config, String key, String legacyKey, String defaultValue) {
        if (config.containsKey(key)) {
            return config.getProperty(key, "");
        }
        if (legacyKey != null && config.containsKey(legacyKey)) {
            return config.getProperty(legacyKey, "");
        }
        return defaultValue;
    }

    private static boolean getBoolean(
            Properties config, String key, boolean defaultValue) {
        String value = trimToNull(config.getProperty(key));
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    private static int getInt(Properties config, String key, int defaultValue) {
        String value = trimToNull(config.getProperty(key));
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static List<String> split(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<String>();
        }

        List<String> result = new ArrayList<String>();
        for (String item : value.split(",")) {
            String normalized = trimToNull(item);
            if (normalized != null) {
                result.add(normalized);
            }
        }
        return result;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
