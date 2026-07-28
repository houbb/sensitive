package com.github.houbb.sensitive.test.log4j2;

import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import org.junit.Assert;
import org.junit.Test;

public class CharsScanTypeEnumTest {

    @Test
    public void allTypeTest() {
        assertType(CharsScanTypeEnum.PHONE, "1", "手机号");
        assertType(CharsScanTypeEnum.ID_NO, "2", "身份证");
        assertType(CharsScanTypeEnum.BANK_CARD, "3", "银行卡");
        assertType(CharsScanTypeEnum.EMAIL, "4", "邮箱");
        assertType(CharsScanTypeEnum.CHINESE_NAME, "5", "中国人名");
        assertType(CharsScanTypeEnum.BIRTHDAY, "6", "出生日期");
        assertType(CharsScanTypeEnum.GPS, "7", "GPS");
        assertType(CharsScanTypeEnum.IPV4, "8", "IPV4");
        assertType(CharsScanTypeEnum.ADDRESS, "9", "地址");
        assertType(CharsScanTypeEnum.PASSPORT, "10", "护照");
        assertType(CharsScanTypeEnum.ANY_NONE, "11", "匹配任意不掩盖");
        assertType(CharsScanTypeEnum.ANY_HALF, "12", "匹配任意半掩盖");
        assertType(CharsScanTypeEnum.ANY_ALL, "13", "匹配任意全掩盖");
    }

    private void assertType(CharsScanTypeEnum strategy, String identifier, String description) {
        Assert.assertEquals(identifier, strategy.getScanType());
        Assert.assertEquals(description, strategy.getDesc());
    }
}
