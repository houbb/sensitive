package com.github.houbb.sensitive.core.support.scan.custom;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.scan.AbstractConditionCharScan;

/**
 * 国际手机号扫描策略
 * <p>
 * 支持国际手机号（00开头）和国内手机号（1开头）
 * 
 * @author dh
 * @since 1.9.0
 */
public class InternationalPhoneScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return Character.isDigit(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, CharsScanContext context) {
        StringBuilder buffer = getBuffer();
        int bufferLen = buffer.length();

        // 支持 11 位国内号码 + 10-16 位国际号码（00开头）
        if (bufferLen >= 11 && bufferLen <= 16) {
            String phone = buffer.toString();
            return phone.startsWith("00") || phone.startsWith("1");
        }

        return false;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PHONE.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.PHONE.getPriority();
    }

}