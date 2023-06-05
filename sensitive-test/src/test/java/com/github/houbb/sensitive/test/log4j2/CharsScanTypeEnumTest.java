package com.github.houbb.sensitive.test.log4j2;

import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class CharsScanTypeEnumTest {

    @Test
    public void allTypeTest() {
        String format = "| %s | %s | ";

        for(CharsScanTypeEnum charsScanTypeEnum : CharsScanTypeEnum.values()) {
//            System.out.println(String.format(format, charsScanTypeEnum.getScanType(), charsScanTypeEnum.getDesc()));
        }
    }
}
