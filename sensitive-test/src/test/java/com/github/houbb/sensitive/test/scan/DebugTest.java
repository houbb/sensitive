package com.github.houbb.sensitive.test.scan;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;
import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 调试测试类
 */
public class DebugTest {

    @Test
    public void debugTest() {
        String originalPhone = "13912345678";
        String result = SensitiveScanBsContext.scanAndReplace("用户手机号：" + originalPhone);

        Assert.assertNotNull(result);
        Assert.assertFalse("上下文扫描不应保留完整手机号", result.contains(originalPhone));
        Assert.assertTrue("上下文扫描应使用手机号掩码", result.contains("139****5678"));
        Assert.assertNotNull(SensitiveScanBsContext.getCharsScanBs());
    }

    @Test
    public void testCharsScanBsDirectly() {
        List<String> scanTypes = Arrays.asList("1", "2", "3", "4", "5", "9");
        CharsScanBs charsScanBs = CharsScanBs.newInstance()
                .charsScanFactory(new SimpleCharsScanFactory(scanTypes))
                .init();

        String originalPhone = "13912345678";
        String result = charsScanBs.scanAndReplace("用户手机号：" + originalPhone);

        Assert.assertFalse("直接扫描不应保留完整手机号", result.contains(originalPhone));
        Assert.assertTrue("直接扫描应使用手机号掩码", result.contains("139****5678"));
    }

    @Test
    public void testCharsScanBsWithPrefix() {
        List<String> scanTypes = Arrays.asList("1", "2", "3", "4", "5", "9");

        java.util.Set<Character> prefixCharSet = new java.util.HashSet<>();
        String prefixStr = ":：,，'\"‘“=| +()（）";
        for (char c : prefixStr.toCharArray()) {
            prefixCharSet.add(c);
        }

        CharsScanBs charsScanBs = CharsScanBs.newInstance()
                .charsScanFactory(new SimpleCharsScanFactory(scanTypes))
                .prefixCharSet(prefixCharSet)
                .init();

        String originalPhone = "13912345678";
        String result = charsScanBs.scanAndReplace("用户手机号：" + originalPhone);

        Assert.assertFalse("自定义前缀扫描不应保留完整手机号", result.contains(originalPhone));
        Assert.assertTrue("自定义前缀扫描应使用手机号掩码", result.contains("139****5678"));
    }
}
