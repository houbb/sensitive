package com.github.houbb.sensitive.log4j2.layout;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.sensitive.log4j2.support.chars.CharsScanBsUtils;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 安全脱敏的格式
 * @since 1.2.3
 */
@Plugin(name = "SensitivePatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class SensitivePatternLayout extends AbstractStringLayout {

    protected SensitivePatternLayout(Charset charset) {
        super(charset);
    }

    protected SensitivePatternLayout(Charset aCharset, byte[] header, byte[] footer) {
        super(aCharset, header, footer);
    }

    /**
     * 引导类
     * @since 1.2.0
     */
    private CharsScanBs charsScanBs;

    /**
     * 模式格式化
     * @since result
     */
    private List<PatternFormatter> patternFormatterList;

    @Override
    public String toSerializable(LogEvent event) {
        // 格式化
        StringBuilder stringBuilder = new StringBuilder();
        for(PatternFormatter formatter : patternFormatterList) {
            formatter.format(event, stringBuilder);
        }
        String text = stringBuilder.toString();

        try {
            return charsScanBs.scanAndReplace(text);
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 创建对应的格式化列表
     * @param configuration 配置
     * @param pattern 格式化
     * @return 结果
     */
    private static List<PatternFormatter> getPatternFormatList(final Configuration configuration,
                                                               final String pattern) {
        PatternParser patternParser = createPatternParser(configuration);

        return patternParser.parse(pattern);
    }

    /**
     * 创建 pattern 转换
     * @param configuration 配置
     * @return 结果
     * @since 1.3.0
     */
    private static PatternParser createPatternParser(final Configuration configuration) {
        final String key = "Converter";

        if(configuration != null) {
            PatternParser patternParser = configuration.getComponent(key);
            if(patternParser != null) {
                return patternParser;
            }
        }

        return new PatternParser(configuration, key, LogEventPatternConverter.class);
    }

    /**
     * <pre>
     *   // 核心实现策略
     *   .charsCore(CharsCores.defaults())
     *   // 前缀处理策略
     *   .charsPrefix(CharsPrefixes.defaults())
     *   // 扫描策略，每一种对应唯一的 scanType
     *   .charsScanFactory(CharsScans.defaults())
     *   // 替换策略
     *   .charsReplaceFactory(CharsReplaces.defaults())
     *   // 替换对应的哈希策略
     *   .charsReplaceHash(CharsReplaceHashes.defaults())
     *   .init();
     * </pre>
     * @param pluginConfig 配置
     * @param pluginNode 节点
     * @param prefix 前缀
     * @param replaceHash 替换的哈希
     * @param scanList 扫描策略列表
     * @param replaceList 替换策略列表
     * @param defaultReplace 默认策略
     * @return 结果
     */
    @PluginFactory
    public static SensitivePatternLayout createPolicy(@PluginConfiguration Configuration pluginConfig,
                                                      @PluginNode Node pluginNode,
                                                      @PluginAttribute(value = "prefix", defaultString = "：‘“，| ,:\"'=") String prefix,
                                                      @PluginAttribute(value = "scanList", defaultString = "1,2,3,4") String scanList,
                                                      @PluginAttribute(value = "replaceList", defaultString = "1,2,3,4") String replaceList,
                                                      @PluginAttribute(value = "defaultReplace", defaultString = "12") String defaultReplace,
                                                      @PluginAttribute(value = "replaceHash", defaultString = "md5") String replaceHash,
                                                      @PluginAttribute(value = "charset", defaultString = "UTF-8") String charset,
                                                      @PluginAttribute(value = "pattern", defaultString = "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n") String pattern,
                                                      @PluginAttribute(value = "whiteList", defaultString = "") String whiteList
    ) {
        // 初始化
        SensitivePatternLayout sensitivePatternLayout = new SensitivePatternLayout(Charset.forName(charset));
        sensitivePatternLayout.charsScanBs = CharsScanBsUtils.buildCharsScanBs(prefix, scanList, replaceList, defaultReplace, replaceHash, whiteList);;
        sensitivePatternLayout.patternFormatterList = getPatternFormatList(pluginConfig, pattern);

        //TODO 根据用户指定的参数初始化
        return sensitivePatternLayout;
    }

}
