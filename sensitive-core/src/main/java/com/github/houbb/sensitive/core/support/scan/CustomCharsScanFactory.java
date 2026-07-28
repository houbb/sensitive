package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.support.scan.CharsScans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义扫描策略工厂
 * <p>
 * 支持用户自定义扫描策略，可以覆盖或补充内置策略
 * 
 * @author dh
 * @since 1.9.0
 */
public class CustomCharsScanFactory implements ICharsScanFactory {

    /**
     * 默认扫描策略工厂
     */
    private final ICharsScanFactory defaultFactory;

    /**
     * 自定义扫描策略映射
     */
    private final Map<String, ICharsScan> customScans;

    /**
     * 是否覆盖内置策略
     */
    private final boolean override;

    /**
     * 构造函数
     *
     * @param scanTypes   扫描类型列表
     * @param customScans 自定义扫描策略映射
     * @param config      配置属性
     */
    public CustomCharsScanFactory(List<String> scanTypes,
                                   Map<String, ICharsScan> customScans,
                                   Properties config) {
        this.defaultFactory = CharsScans.defaults();
        this.customScans = customScans;
        this.override = Boolean.parseBoolean(
                config.getProperty("chars.scan.custom.override", "true"));
    }

    @Override
    public List<String> scanTypeList() {
        return defaultFactory.scanTypeList();
    }

    @Override
    public ICharsScan getCharScan(String scanType) {
        if (override && customScans.containsKey(scanType)) {
            return customScans.get(scanType);
        }
        
        ICharsScan defaultScan = defaultFactory.getCharScan(scanType);
        if (!override && customScans.containsKey(scanType)) {
            return customScans.get(scanType);
        }
        
        return defaultScan;
    }

    @Override
    public List<ICharsScan> allCharScanList() {
        List<ICharsScan> allScans = new ArrayList<>();
        
        if (override) {
            // 自定义优先：先添加默认，再添加自定义覆盖
            allScans.addAll(defaultFactory.allCharScanList());
            
            // 用自定义策略替换默认策略
            for (int i = 0; i < allScans.size(); i++) {
                ICharsScan scan = allScans.get(i);
                String scanType = scan.getScanType();
                if (customScans.containsKey(scanType)) {
                    allScans.set(i, customScans.get(scanType));
                }
            }
        } else {
            // 默认优先：先添加自定义，再添加默认覆盖
            allScans.addAll(defaultFactory.allCharScanList());
        }
        
        return allScans;
    }

}