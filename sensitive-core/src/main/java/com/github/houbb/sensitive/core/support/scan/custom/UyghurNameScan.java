package com.github.houbb.sensitive.core.support.scan.custom;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.scan.AbstractConditionCharScan;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 长中文姓名扫描策略
 * <p>
 * 支持新疆名字等长中文姓名（2-10个汉字）
 * 
 * @author dh
 * @since 1.9.0
 */
public class UyghurNameScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isChinese(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, CharsScanContext context) {
        StringBuilder buffer = getBuffer();
        int bufferLen = buffer.length();

        // 支持 2-10 个汉字
        return bufferLen >= 2 && bufferLen <= 10;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.CHINESE_NAME.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.CHINESE_NAME.getPriority();
    }

}