package com.github.houbb.sensitive.logback.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsContext;

/**
 * logback 通过转换类的脱敏
 * @since 1.6.0
 */
public class SensitiveLogbackConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String originalMessage = iLoggingEvent.getFormattedMessage();
        return SensitiveScanBsContext.scanAndReplace(originalMessage);
    }

}
