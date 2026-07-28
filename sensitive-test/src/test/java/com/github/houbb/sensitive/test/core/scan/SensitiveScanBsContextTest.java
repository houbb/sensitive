package com.github.houbb.sensitive.test.core.scan;

import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * SensitiveScanBsContext 单元测试
 * 
 * @author dh
 * @since 1.9.0
 */
public class SensitiveScanBsContextTest {

    @Test
    public void basicTest() {
        String text = "手机号：13912345678，身份证：110101199003077774";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        
        // 验证基本脱敏功能
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("****"));
        Assert.assertFalse(result.contains("13912345678"));
        Assert.assertFalse(result.contains("110101199003077774"));
    }

    @Test
    public void phoneTest() {
        String text = "联系方式：13912345678";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("139****"));
        Assert.assertFalse(result.contains("1234"));
    }

    @Test
    public void idCardTest() {
        String text = "身份证号：110101199003077774";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("*"));
        Assert.assertFalse(result.contains("19900307"));
    }

    @Test
    public void emptyTest() {
        String result = SensitiveScanBsContext.scanAndReplace("");
        Assert.assertEquals("", result);
        
        result = SensitiveScanBsContext.scanAndReplace(null);
        Assert.assertNull(result);
    }

    @Test
    public void noMatchTest() {
        String text = "这是一段普通的文本";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        
        Assert.assertEquals(text, result);
    }

    @Test
    public void reloadTest() {
        // 测试重新加载配置
        SensitiveScanBsContext.reload();
        
        String text = "手机号：13912345678";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("****"));
    }

}