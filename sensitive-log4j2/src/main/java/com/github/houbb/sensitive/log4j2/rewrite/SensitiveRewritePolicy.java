package com.github.houbb.sensitive.log4j2.rewrite;

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
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * 脱敏策略
 *
 * https://www.cnblogs.com/leonlipfsj/p/14691848.html
 * https://blog.csdn.net/LNF568611/article/details/113083401
 * https://blog.csdn.net/qq_19983129/article/details/129854932
 * https://it.cha138.com/jingpin/show-55489.html
 * https://www.dianjilingqu.com/597663.html
 *
 * @author dh
 */
@Plugin(name = "SensitiveRewritePolicy", category = Node.CATEGORY, elementType = "rewritePolicy", printObject = true)
public class SensitiveRewritePolicy implements RewritePolicy {

    /**
     * 引导类
     * @since 1.2.0
     */
    private static CharsScanBs charsScanBs;

    public SensitiveRewritePolicy() {
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        MutableLogEvent log4jLogEvent = (MutableLogEvent) source;
        Message message = log4jLogEvent.getMessage();

        //TODO: 这里测试，暂时都是这种类别，有没有特殊的？
        if(message instanceof MutableLogEvent) {
            // mutableLogEvent.getFormat() 这里返回的竟然是 null，而不是 format 格式...
            String rawMessage = message.getFormattedMessage();

            // 所有基于参数，看起来只在 param 中有意义。
            String newMessage = charsScanBs.scanAndReplace(rawMessage);
            log4jLogEvent.setMessage(new SimpleMessage(newMessage));
            return log4jLogEvent;
        }

        // 直接返回原始的。
        return source;
    }

    // 指定对应的 factory

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
     * @return 结果
     */
    @PluginFactory
    public static SensitiveRewritePolicy createPolicy(@PluginConfiguration Configuration pluginConfig,
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
        return new SensitiveRewritePolicy();
    }

}
