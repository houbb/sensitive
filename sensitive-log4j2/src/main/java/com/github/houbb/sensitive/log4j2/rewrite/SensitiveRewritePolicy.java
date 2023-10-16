package com.github.houbb.sensitive.log4j2.rewrite;

import com.github.houbb.chars.scan.util.CharsScanPropertyHelper;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
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
                String newMessage = CharsScanPropertyHelper.scanAndReplace(rawMessage);

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
                String newMessage = CharsScanPropertyHelper.scanAndReplace(rawMessage);
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
     * @param pluginConfig 配置
     * @param pluginNode 节点
     * @return 结果
     */
    @PluginFactory
    public static SensitiveRewritePolicy createPolicy(@PluginConfiguration Configuration pluginConfig,
                                                      @PluginNode Node pluginNode
                                                      ) {
        //TODO 根据用户指定的参数初始化
        return new SensitiveRewritePolicy();
    }

}
