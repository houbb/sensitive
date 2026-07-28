package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.bs.CharsScanBs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 日志插件共用的敏感信息扫描上下文。
 *
 * <p>log4j2 和 logback 均通过这里加载同一份配置，并由
 * {@link SensitiveScanBsBuilder} 对齐 {@link CharsScanBs} 的扩展点。</p>
 *
 * @author dh
 * @since 1.9.0
 */
public final class SensitiveScanBsContext {

    private static final String CONFIG_FILE_NAME =
            "chars-scan-config.properties";

    private static volatile CharsScanBs charsScanBs;

    private static volatile boolean initialized;

    private SensitiveScanBsContext() {
    }

    /**
     * 初始化上下文。
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
                charsScanBs = SensitiveScanBsBuilder.build(loadConfig());
            } catch (Exception e) {
                System.err.println(
                        "[ERROR] Failed to initialize sensitive scanner: "
                                + e.getMessage());
                e.printStackTrace();
                charsScanBs = CharsScanBs.newInstance().init();
            }
            initialized = true;
        }
    }

    /**
     * 扫描并替换敏感信息。
     *
     * @param text 原文
     * @return 脱敏结果；运行期扫描失败时返回原文
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
     * 获取共享扫描器。
     *
     * @return 扫描器
     */
    public static CharsScanBs getCharsScanBs() {
        init();
        return charsScanBs;
    }

    /**
     * 重新加载类路径中的配置。
     */
    public static void reload() {
        synchronized (SensitiveScanBsContext.class) {
            initialized = false;
            charsScanBs = null;
        }
        init();
    }

    private static Properties loadConfig() {
        Properties config = new Properties();
        InputStream inputStream = SensitiveScanBsContext.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE_NAME);
        if (inputStream == null) {
            return config;
        }

        try (InputStream stream = inputStream;
             InputStreamReader reader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            config.load(reader);
        } catch (IOException e) {
            // 配置读取失败时由 builder 使用默认值。
        }
        return config;
    }

}
