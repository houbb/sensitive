package com.github.houbb.sensitive.core.support.scan;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 可向现有工厂追加或覆盖替换策略的组合工厂。
 *
 * <p>自定义策略标识直接取自 {@link ICharsReplace#getScanType()}。</p>
 *
 * @author dh
 * @since 1.9.0
 */
public class CustomCharsReplaceFactory implements ICharsReplaceFactory {

    private final ICharsReplaceFactory defaultFactory;

    private final Set<String> defaultTypes;

    private final Map<String, ICharsReplace> customReplaces;

    private final ICharsReplace customDefaultReplace;

    private final boolean override;

    /**
     * 创建组合替换工厂。
     *
     * @param defaultFactory       底层替换工厂
     * @param defaultTypes         底层工厂已显式配置的策略标识
     * @param customReplaces       要追加的替换策略
     * @param customDefaultReplace 自定义兜底策略；可以为空
     * @param override             标识相同时是否由自定义策略覆盖底层策略
     */
    public CustomCharsReplaceFactory(ICharsReplaceFactory defaultFactory,
                                     Collection<String> defaultTypes,
                                     Collection<ICharsReplace> customReplaces,
                                     ICharsReplace customDefaultReplace,
                                     boolean override) {
        if (defaultFactory == null) {
            throw new IllegalArgumentException("defaultFactory must not be null");
        }

        this.defaultFactory = defaultFactory;
        this.defaultTypes = defaultTypes == null
                ? Collections.<String>emptySet()
                : Collections.unmodifiableSet(
                        new HashSet<String>(defaultTypes));
        this.customReplaces = indexReplaces(customReplaces);
        this.customDefaultReplace = customDefaultReplace;
        this.override = override;
    }

    /**
     * 创建不带自定义兜底策略的组合替换工厂。
     */
    public CustomCharsReplaceFactory(ICharsReplaceFactory defaultFactory,
                                     Collection<String> defaultTypes,
                                     Collection<ICharsReplace> customReplaces,
                                     boolean override) {
        this(defaultFactory, defaultTypes, customReplaces, null, override);
    }

    /**
     * 兼容 1.9.0 早期的数字映射构造方式。
     *
     * @deprecated 优先使用按 {@code getScanType()} 自动注册的新构造方法
     */
    @Deprecated
    public CustomCharsReplaceFactory(List<String> replaceTypes,
                                     Map<String, ICharsReplace> customReplaces,
                                     Properties config) {
        this(CharsReplaces.defaultsReplaceFactory(
                        replaceTypes,
                        config.getProperty(
                                "chars.scan.defaultReplace", "12").trim()),
                replaceTypes,
                customReplaces == null
                        ? Collections.<ICharsReplace>emptyList()
                        : customReplaces.values(),
                Boolean.parseBoolean(config.getProperty(
                        SensitiveScanBsBuilder.KEY_CUSTOM_OVERRIDE, "true")));
    }

    @Override
    public ICharsReplace getReplace(String scanType) {
        ICharsReplace customReplace = customReplaces.get(scanType);
        if (customReplace != null
                && (override || !defaultTypes.contains(scanType))) {
            return customReplace;
        }

        if (defaultTypes.contains(scanType)) {
            return defaultFactory.getReplace(scanType);
        }
        if (customDefaultReplace != null) {
            return customDefaultReplace;
        }
        return defaultFactory.getReplace(scanType);
    }

    private static Map<String, ICharsReplace> indexReplaces(
            Collection<ICharsReplace> replaces) {
        Map<String, ICharsReplace> result =
                new LinkedHashMap<String, ICharsReplace>();
        if (replaces == null) {
            return result;
        }

        for (ICharsReplace replace : replaces) {
            if (replace == null) {
                throw new IllegalArgumentException(
                        "custom replace must not be null");
            }

            String scanType = trimToNull(replace.getScanType());
            if (scanType == null) {
                throw new IllegalArgumentException(
                        replace.getClass().getName()
                                + " returned an empty scan type");
            }
            if (result.put(scanType, replace) != null) {
                throw new IllegalArgumentException(
                        "Duplicate custom replace type: " + scanType);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
