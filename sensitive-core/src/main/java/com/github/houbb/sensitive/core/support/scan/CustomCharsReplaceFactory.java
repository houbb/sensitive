package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义替换策略工厂
 * <p>
 * 支持用户自定义替换策略，可以覆盖或补充内置策略
 * 
 * @author dh
 * @since 1.9.0
 */
public class CustomCharsReplaceFactory implements ICharsReplaceFactory {

    /**
     * 默认替换策略工厂
     */
    private final ICharsReplaceFactory defaultFactory;

    /**
     * 自定义替换策略映射
     */
    private final Map<String, ICharsReplace> customReplaces;

    /**
     * 是否覆盖内置策略
     */
    private final boolean override;

    /**
     * 构造函数
     *
     * @param replaceTypes   替换类型列表
     * @param customReplaces 自定义替换策略映射
     * @param config         配置属性
     */
    public CustomCharsReplaceFactory(List<String> replaceTypes,
                                       Map<String, ICharsReplace> customReplaces,
                                       Properties config) {
        this.defaultFactory = CharsReplaces.defaults();
        this.customReplaces = customReplaces;
        this.override = Boolean.parseBoolean(
                config.getProperty("chars.scan.custom.override", "true"));
    }

    @Override
    public ICharsReplace getReplace(String scanType) {
        if (override && customReplaces.containsKey(scanType)) {
            return customReplaces.get(scanType);
        }
        
        ICharsReplace defaultReplace = defaultFactory.getReplace(scanType);
        if (!override && customReplaces.containsKey(scanType)) {
            return customReplaces.get(scanType);
        }
        
        return defaultReplace;
    }

}