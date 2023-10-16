package com.github.houbb.sensitive.logback.layout;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.houbb.chars.scan.util.CharsScanPropertyHelper;

/**
 * logback 通过转换类的脱敏
 * @since 1.6.0
 */
public class SensitiveLogbackLayout extends PatternLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        String originalMessage = super.doLayout(event);
        return CharsScanPropertyHelper.scanAndReplace(originalMessage);
    }

}
