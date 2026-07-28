package com.github.houbb.sensitive.core.support.config;

import com.github.houbb.heaven.util.lang.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 脱敏全局配置
 * <p>
 * 配置优先级（从高到低）：
 * 1. 系统属性：-Dsensitive.showHash=false
 * 2. 配置文件：chars-scan-config.properties 中的 chars.scan.showHash=false
 * 3. 默认值：showHash=true
 *
 * @author dh
 * @since 1.8.0
 */
public final class SensitiveConfig {

    private SensitiveConfig() {
    }

    /**
     * 配置文件名称（使用 chars-scan 的配置文件）
     */
    private static final String CONFIG_FILE_NAME = "chars-scan-config.properties";

    /**
     * 配置键前缀（使用 chars-scan 的前缀）
     */
    private static final String CONFIG_KEY_PREFIX = "chars.scan.";

    /**
     * 系统属性前缀
     */
    private static final String SYSTEM_PROPERTY_PREFIX = "sensitive.";

    /**
     * showHash 配置键
     */
    private static final String KEY_SHOW_HASH = "showHash";

    /**
     * 默认显示哈希值（保持向后兼容）
     */
    private static final boolean DEFAULT_SHOW_HASH = true;

    /**
     * 配置属性缓存
     */
    private static Properties configProperties;

    /**
     * 是否已打印过配置信息
     */
    private static volatile boolean configDumped = false;

    static {
        loadConfig();
    }

    /**
     * 加载配置
     */
    private static void loadConfig() {
        configProperties = new Properties();
        try (InputStream inputStream = SensitiveConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (inputStream != null) {
                configProperties.load(inputStream);
            }
        } catch (IOException e) {
            // 配置文件不存在或加载失败，使用默认值
        }
    }

    /**
     * 是否显示哈希值
     * @return 是否显示哈希值
     */
    public static boolean showHash() {
        boolean value = getBoolean(KEY_SHOW_HASH, DEFAULT_SHOW_HASH);
        
        // 首次使用时打印配置信息
        dumpConfigOnce();
        
        return value;
    }

    /**
     * 首次使用时打印配置信息（防止递归）
     */
    private static void dumpConfigOnce() {
        if (!configDumped) {
            synchronized (SensitiveConfig.class) {
                if (!configDumped) {
                    System.out.println(buildConfigDump());
                    configDumped = true;
                }
            }
        }
    }

    /**
     * 输出配置信息，方便排查问题
     * @return 配置信息字符串
     */
    public static String dumpConfig() {
        return buildConfigDump();
    }

    /**
     * 构建配置信息字符串
     * @return 配置信息字符串
     */
    private static String buildConfigDump() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== Sensitive Configuration ==========\n");
        
        // showHash 配置
        String key = KEY_SHOW_HASH;
        boolean effectiveValue = getBoolean(KEY_SHOW_HASH, DEFAULT_SHOW_HASH);
        String source = getConfigSource(key);
        
        sb.append(String.format("%-20s = %-10s (source: %s)\n", 
            CONFIG_KEY_PREFIX + key, effectiveValue, source));
        
        sb.append("=============================================");
        return sb.toString();
    }

    /**
     * 获取布尔配置值
     * <p>
     * 优先级：系统属性 > 配置文件 > 默认值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    private static boolean getBoolean(String key, boolean defaultValue) {
        // 1. 优先使用系统属性（sensitive.showHash）
        String systemKey = SYSTEM_PROPERTY_PREFIX + key;
        String systemValue = System.getProperty(systemKey);
        if (StringUtil.isNotEmpty(systemValue)) {
            return Boolean.parseBoolean(systemValue.trim());
        }

        // 2. 使用配置文件（chars.scan.showHash）
        if (configProperties != null) {
            String fileKey = CONFIG_KEY_PREFIX + key;
            String fileValue = configProperties.getProperty(fileKey);
            if (StringUtil.isNotEmpty(fileValue)) {
                return Boolean.parseBoolean(fileValue.trim());
            }
        }

        // 3. 返回默认值
        return defaultValue;
    }

    /**
     * 获取配置值的来源
     * @param key 配置键
     * @return 来源描述
     */
    private static String getConfigSource(String key) {
        // 1. 检查系统属性
        String systemKey = SYSTEM_PROPERTY_PREFIX + key;
        String systemValue = System.getProperty(systemKey);
        if (StringUtil.isNotEmpty(systemValue)) {
            return "system property (-D" + systemKey + ")";
        }

        // 2. 检查配置文件
        if (configProperties != null) {
            String fileKey = CONFIG_KEY_PREFIX + key;
            String fileValue = configProperties.getProperty(fileKey);
            if (StringUtil.isNotEmpty(fileValue)) {
                return "config file (" + CONFIG_FILE_NAME + ")";
            }
        }

        // 3. 默认值
        return "default";
    }

    /**
     * 重新加载配置
     */
    public static void reload() {
        loadConfig();
        configDumped = false;
    }

}