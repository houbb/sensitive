package com.github.houbb.sensitive.test.scan;

import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SensitiveScanBsContext 测试类
 * @since 1.9.0
 */
public class SensitiveScanBsContextTest {

    @Test
    public void testScanAndReplace_null() {
        String result = SensitiveScanBsContext.scanAndReplace(null);
        assertNull(result);
    }

    @Test
    public void testScanAndReplace_empty() {
        String result = SensitiveScanBsContext.scanAndReplace("");
        assertEquals("", result);
    }

    @Test
    public void testScanAndReplace_noSensitive() {
        String text = "这是一条普通日志消息";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        assertEquals(text, result);
    }

    @Test
    public void testScanAndReplace_phone() {
        String text = "用户手机号：13912345678";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        // 验证手机号被脱敏（不应包含完整手机号）
        assertNotNull(result);
        assertFalse("结果不应包含完整手机号", result.contains("13912345678"));
    }

    @Test
    public void testScanAndReplace_idCard() {
        String text = "身份证号码：320123199001011234";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        // 验证身份证被脱敏（不应包含完整身份证号）
        assertNotNull(result);
        assertFalse("结果不应包含完整身份证号", result.contains("320123199001011234"));
    }

    @Test
    public void testScanAndReplace_bankCard() {
        String bankCard = "6217000010002105024";
        String text = "银行卡号：" + bankCard;
        String result = SensitiveScanBsContext.scanAndReplace(text);
        // 验证银行卡被脱敏（不应包含完整银行卡号）
        assertNotNull(result);
        assertFalse("结果不应包含完整银行卡号", result.contains(bankCard));
        assertTrue("结果应包含银行卡掩码", result.contains("6217***********5024"));
    }

    @Test
    public void testScanAndReplace_email() {
        String email = "tester@example.com";
        String text = "邮箱：" + email;
        String result = SensitiveScanBsContext.scanAndReplace(text);
        // 验证邮箱被脱敏（不应包含完整邮箱）
        assertNotNull(result);
        assertFalse("结果不应包含完整邮箱", result.contains(email));
        assertTrue("结果应包含邮箱掩码", result.contains("test**@example.com"));
    }

    @Test
    public void testScanAndReplace_multipleSensitive() {
        String text = "用户手机：13912345678，身份证：320123199001011234，邮箱：tester@example.com";
        String result = SensitiveScanBsContext.scanAndReplace(text);
        // 验证多个敏感信息都被脱敏
        assertNotNull(result);
        assertFalse("结果不应包含完整手机号", result.contains("13912345678"));
        assertFalse("结果不应包含完整身份证号", result.contains("320123199001011234"));
        assertFalse("结果不应包含完整邮箱", result.contains("tester@example.com"));
    }

    @Test
    public void testGetCharsScanBs() {
        assertNotNull(SensitiveScanBsContext.getCharsScanBs());
    }

    @Test
    public void testReload() {
        // 先初始化
        SensitiveScanBsContext.scanAndReplace("test");
        // 重新加载
        SensitiveScanBsContext.reload();
        // 再次验证
        assertNotNull(SensitiveScanBsContext.getCharsScanBs());
    }
}
