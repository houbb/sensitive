package com.github.houbb.sensitive.log4j2.layout;

import com.github.houbb.chars.scan.api.ICharsPrefix;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.prefix.CharsPrefixes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.heaven.util.lang.StringUtil;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    private static CharsScanBs charsScanBs;

    @Override
    public String toSerializable(LogEvent event) {
        String msg = event.getMessage().getFormattedMessage();
        return charsScanBs.scanAndReplace(msg);
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
                                                      @PluginAttribute(value = "prefix", defaultString = ":\"'=") String prefix,
                                                      @PluginAttribute(value = "scanList", defaultString = "1,2,3,4") String scanList,
                                                      @PluginAttribute(value = "replaceList", defaultString = "1,2,3,4") String replaceList,
                                                      @PluginAttribute(value = "defaultReplace", defaultString = "12") String defaultReplace,
                                                      @PluginAttribute(value = "replaceHash", defaultString = "md5") String replaceHash
    ) {
        // 初始化
        final ICharsPrefix charsPrefix = CharsPrefixes.simple(prefix);
        final ICharsScanFactory charsScanFactory = CharsScans.defaults(StringUtil.splitToList(scanList));
        final ICharsReplaceFactory replaceFactory = CharsReplaces.defaultsReplaceFactory(StringUtil.splitToList(replaceList), defaultReplace);
        final ICharsReplaceHash replaceHashStrategy = CharsReplaceHashes.newInstance(replaceHash);

        // 构建 bs
        charsScanBs = CharsScanBs.newInstance()
                // 核心实现策略
                .charsCore(CharsCores.defaults())
                // 前缀处理策略
                .charsPrefix(charsPrefix)
                // 扫描策略，每一种对应唯一的 scanType
                .charsScanFactory(charsScanFactory)
                // 替换策略
                .charsReplaceFactory(replaceFactory)
                // 替换对应的哈希策略
                .charsReplaceHash(replaceHashStrategy)
                .init();

        //TODO 根据用户指定的参数初始化
        return new SensitivePatternLayout(StandardCharsets.UTF_8);
    }

}
