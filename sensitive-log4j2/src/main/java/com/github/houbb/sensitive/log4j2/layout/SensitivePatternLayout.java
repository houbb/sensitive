package com.github.houbb.sensitive.log4j2.layout;

import com.github.houbb.chars.scan.util.CharsScanPropertyHelper;
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
            return CharsScanPropertyHelper.scanAndReplace(text);
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
     * 配置
     * @param pluginConfig 配置
     * @param pluginNode 节点
     * @return 结果
     */
    @PluginFactory
    public static SensitivePatternLayout createPolicy(@PluginConfiguration Configuration pluginConfig,
                                                      @PluginNode Node pluginNode,
                                                      @PluginAttribute(value = "charset", defaultString = "UTF-8") String charset,
                                                      @PluginAttribute(value = "pattern", defaultString = "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n") String pattern
    ) {
        // 初始化
        SensitivePatternLayout sensitivePatternLayout = new SensitivePatternLayout(Charset.forName(charset));
        sensitivePatternLayout.patternFormatterList = getPatternFormatList(pluginConfig, pattern);

        //TODO 根据用户指定的参数初始化
        return sensitivePatternLayout;
    }

}
