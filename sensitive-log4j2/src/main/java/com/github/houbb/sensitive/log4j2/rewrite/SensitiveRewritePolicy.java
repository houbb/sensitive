package com.github.houbb.sensitive.log4j2.rewrite;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.sensitive.log4j2.support.chars.CharsScanBsUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
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
@Deprecated
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
        try {
            if(source instanceof Log4jLogEvent) {
                Log4jLogEvent log4jLogEvent = (Log4jLogEvent) source;
                Message message =  log4jLogEvent.getMessage();

                // mutableLogEvent.getFormat() 这里返回的竟然是 null，而不是 format 格式...
                String rawMessage = message.getFormattedMessage();

                // 所有基于参数，看起来只在 param 中有意义。
                String newMessage = charsScanBs.scanAndReplace(rawMessage);

                //builder
                Log4jLogEvent.Builder newEventBuilder = new Log4jLogEvent.Builder(log4jLogEvent);
                newEventBuilder.setMessage(new SimpleMessage(newMessage));
                return newEventBuilder.build();
            } else if(source instanceof MutableLogEvent) {
                MutableLogEvent log4jLogEvent = (MutableLogEvent) source;
                Message message = log4jLogEvent.getMessage();
                // mutableLogEvent.getFormat() 这里返回的竟然是 null，而不是 format 格式...
                String rawMessage = message.getFormattedMessage();

                // 所有基于参数，看起来只在 param 中有意义。
                String newMessage = charsScanBs.scanAndReplace(rawMessage);
                log4jLogEvent.setMessage(new SimpleMessage(newMessage));
                return source;
            }

            // 直接返回原始的。
            return source;
        } catch (Exception e) {
            return source;
        }
    }

    private Message getMessage(LogEvent source) {
        if(source instanceof Log4jLogEvent) {
            Log4jLogEvent log4jLogEvent = (Log4jLogEvent) source;
            return log4jLogEvent.getMessage();
        } else if(source instanceof MutableLogEvent) {
            MutableLogEvent log4jLogEvent = (MutableLogEvent) source;
            Message message = log4jLogEvent.getMessage();
            return message;
        }

        return source.getMessage();
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
     * @param defaultReplace 默认策略
     * @return 结果
     */
    @PluginFactory
    public static SensitiveRewritePolicy createPolicy(@PluginConfiguration Configuration pluginConfig,
                                                      @PluginNode Node pluginNode,
                                                      @PluginAttribute(value = "prefix", defaultString = "：‘“，| ,:\"'=") String prefix,
                                                      @PluginAttribute(value = "scanList", defaultString = "1,2,3,4") String scanList,
                                                      @PluginAttribute(value = "replaceList", defaultString = "1,2,3,4") String replaceList,
                                                      @PluginAttribute(value = "defaultReplace", defaultString = "12") String defaultReplace,
                                                      @PluginAttribute(value = "replaceHash", defaultString = "md5") String replaceHash,
                                                      @PluginAttribute(value = "whiteList", defaultString = "") String whiteList
                                                      ) {
        // 构建 bs
        charsScanBs = CharsScanBsUtils.buildCharsScanBs(prefix, scanList, replaceList, defaultReplace, replaceHash, whiteList);

        //TODO 根据用户指定的参数初始化
        return new SensitiveRewritePolicy();
    }

}
