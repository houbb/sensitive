package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.support.scan.CharsScans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 可向现有工厂追加或覆盖扫描策略的组合工厂。
 *
 * <p>自定义策略标识直接取自 {@link ICharsScan#getScanType()}，不要求调用方再维护
 * 一份“标识到类名”的重复映射。</p>
 *
 * @author dh
 * @since 1.9.0
 */
public class CustomCharsScanFactory implements ICharsScanFactory {

    private final ICharsScanFactory defaultFactory;

    private final Map<String, Class<? extends ICharsScan>> customScanClasses;

    private final List<String> scanTypes;

    private final boolean override;

    /**
     * 创建组合扫描工厂。
     *
     * @param defaultFactory 底层扫描工厂
     * @param customScans    要追加的扫描策略
     * @param override       标识相同时是否由自定义策略覆盖底层策略
     */
    public CustomCharsScanFactory(ICharsScanFactory defaultFactory,
                                  Collection<ICharsScan> customScans,
                                  boolean override) {
        if (defaultFactory == null) {
            throw new IllegalArgumentException("defaultFactory must not be null");
        }

        this.defaultFactory = defaultFactory;
        this.customScanClasses = indexScans(customScans);
        this.override = override;

        List<String> types = new ArrayList<String>(defaultFactory.scanTypeList());
        for (String customType : this.customScanClasses.keySet()) {
            if (!types.contains(customType)) {
                types.add(customType);
            }
        }
        this.scanTypes = Collections.unmodifiableList(types);
    }

    /**
     * 兼容 1.9.0 早期的数字映射构造方式。
     *
     * @param scanTypes   扫描类型
     * @param customScans 自定义策略映射
     * @param config      配置
     * @deprecated 优先使用按 {@code getScanType()} 自动注册的新构造方法
     */
    @Deprecated
    public CustomCharsScanFactory(List<String> scanTypes,
                                  Map<String, ICharsScan> customScans,
                                  Properties config) {
        this(CharsScans.defaults(scanTypes),
                customScans == null
                        ? Collections.<ICharsScan>emptyList()
                        : customScans.values(),
                Boolean.parseBoolean(config.getProperty(
                        SensitiveScanBsBuilder.KEY_CUSTOM_OVERRIDE, "true")));
    }

    @Override
    public List<String> scanTypeList() {
        return scanTypes;
    }

    @Override
    public ICharsScan getCharScan(String scanType) {
        Class<? extends ICharsScan> customClass =
                customScanClasses.get(scanType);
        if (override && customClass != null) {
            return newCustomScan(customClass);
        }

        try {
            return defaultFactory.getCharScan(scanType);
        } catch (RuntimeException e) {
            if (customClass != null) {
                return newCustomScan(customClass);
            }
            throw e;
        }
    }

    @Override
    public List<ICharsScan> allCharScanList() {
        List<ICharsScan> allScans = new ArrayList<ICharsScan>(scanTypes.size());
        for (String scanType : scanTypes) {
            allScans.add(getCharScan(scanType));
        }
        return allScans;
    }

    private static Map<String, Class<? extends ICharsScan>> indexScans(
            Collection<ICharsScan> scans) {
        Map<String, Class<? extends ICharsScan>> result =
                new LinkedHashMap<String, Class<? extends ICharsScan>>();
        if (scans == null) {
            return result;
        }

        for (ICharsScan scan : scans) {
            if (scan == null) {
                throw new IllegalArgumentException("custom scan must not be null");
            }

            String scanType = trimToNull(scan.getScanType());
            if (scanType == null) {
                throw new IllegalArgumentException(
                        scan.getClass().getName() + " returned an empty scan type");
            }

            Class<? extends ICharsScan> scanClass = scan.getClass();
            try {
                scanClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(
                        scanClass.getName()
                                + " must provide a public no-argument constructor",
                        e);
            }

            if (result.put(scanType, scanClass) != null) {
                throw new IllegalArgumentException(
                        "Duplicate custom scan type: " + scanType);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private static ICharsScan newCustomScan(
            Class<? extends ICharsScan> scanClass) {
        try {
            return scanClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cannot create custom scan " + scanClass.getName(), e);
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
