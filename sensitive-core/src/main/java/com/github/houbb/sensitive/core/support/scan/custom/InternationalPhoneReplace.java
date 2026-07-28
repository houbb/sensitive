package com.github.houbb.sensitive.core.support.scan.custom;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.replace.AbstractRangeCharReplace;

/**
 * 国际手机号替换策略
 * <p>
 * 支持国际手机号（00开头）和国内手机号（1开头）
 * 
 * @author dh
 * @since 1.9.0
 */
public class InternationalPhoneReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PHONE.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem item) {
        String phone = new String(chars, item.getStartIndex(), itemLen);

        if (phone.startsWith("00")) {
            // 国际号码：008613912345678 -> 0086****5678
            return item.getStartIndex() + 4;
        } else {
            // 国内号码：13912345678 -> 139****5678
            return item.getStartIndex() + 3;
        }
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem item) {
        return item.getEndIndex() - 4;
    }

}