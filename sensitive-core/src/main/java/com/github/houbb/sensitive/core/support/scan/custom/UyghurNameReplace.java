package com.github.houbb.sensitive.core.support.scan.custom;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.support.replace.AbstractRangeCharReplace;

/**
 * 长中文姓名替换策略
 * <p>
 * 支持新疆名字等长中文姓名
 * 
 * @author dh
 * @since 1.9.0
 */
public class UyghurNameReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.CHINESE_NAME.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem item) {
        // 保留第一个字
        return item.getStartIndex() + 1;
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem item) {
        // 不同长度不同处理
        if (itemLen <= 2) {
            // 两个字：张三 -> 张*
            return item.getEndIndex() - 1;
        } else if (itemLen <= 3) {
            // 三个字：李晓明 -> 李**
            return item.getEndIndex();
        } else {
            // 四个字及以上：阿里木江·买买提 -> 阿里木***
            return item.getEndIndex();
        }
    }

}