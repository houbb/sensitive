package com.github.houbb.sensitive.log4j2.rewrite;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharReplaceHashes;
import com.github.houbb.chars.scan.support.prefix.CharPrefixes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * https://www.cnblogs.com/leonlipfsj/p/14691848.html
 * https://blog.csdn.net/LNF568611/article/details/113083401
 * https://blog.csdn.net/qq_19983129/article/details/129854932
 * https://it.cha138.com/jingpin/show-55489.html
 * https://www.dianjilingqu.com/597663.html
 */
@Plugin(name = "SensitiveRewritePolicy", category = Node.CATEGORY, elementType = "rewritePolicy", printObject = true)
public class SensitiveRewritePolicy implements RewritePolicy {

    /**
     * 引导类
     * @since 1.2.0
     */
    private CharsScanBs charsScanBs;

    public SensitiveRewritePolicy() {
        charsScanBs = CharsScanBs.newInstance()
                // 核心实现策略
                .setCharsCore(CharsCores.sequence())
                // 前缀处理策略
                .setCharsPrefix(CharPrefixes.simple())
                // 扫描策略，每一种对应唯一的 scanType
                .setScanList(CharsScans.defaultCharScanList())
                // 替换策略
                .setReplaceMap(CharsReplaces.defaultReplaceMap())
                // 没有匹配时的默认替换策略
                .setDefaultReplace(CharsReplaces.defaultReplace())
                // 替换对应的哈希策略
                .setCharsReplaceHash(CharReplaceHashes.getMd5())
                .init();
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        MutableLogEvent log4jLogEvent = (MutableLogEvent) source;

        Message message = log4jLogEvent.getMessage();

        //TODO: 这里测试，暂时都是这种类别，有没有特殊的？
        if(message instanceof MutableLogEvent) {
            // mutableLogEvent.getFormat() 这里返回的竟然是 null，而不是 format 格式...
            String rawMessage = message.getFormattedMessage();
//            String rawFormat = message.getFormat();
//            Object[] parameters = message.getParameters();

            // 所有基于参数，看起来只在 param 中有意义。

            String newMessage = charsScanBs.scanAndReplace(rawMessage);
            log4jLogEvent.setMessage(new SimpleMessage(newMessage));
            return log4jLogEvent;
        }

        // 直接返回原始的。
        return source;
    }

    // 指定对应的 factory
    @PluginFactory
    public static SensitiveRewritePolicy createPolicy() {
        //TODO 根据用户指定的参数初始化
        return new SensitiveRewritePolicy();
    }

}
