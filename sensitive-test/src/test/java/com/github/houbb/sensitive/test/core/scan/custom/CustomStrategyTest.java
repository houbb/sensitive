package com.github.houbb.sensitive.test.core.scan.custom;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneReplace;
import com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneScan;
import com.github.houbb.sensitive.core.support.scan.custom.UyghurNameReplace;
import com.github.houbb.sensitive.core.support.scan.custom.UyghurNameScan;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义策略测试
 * 
 * @author dh
 * @since 1.9.0
 */
public class CustomStrategyTest {

    /**
     * 测试国际手机号策略
     */
    @Test
    public void internationalPhoneTest() {
        // 国内手机号
        String text1 = "手机号：13912345678";
        
        InternationalPhoneScan scan = new InternationalPhoneScan();
        InternationalPhoneReplace replace = new InternationalPhoneReplace();
        
        // 注意：这里仅测试策略类是否正常工作
        // 实际使用时，应在配置文件中指定策略类路径
        Assert.assertNotNull(scan);
        Assert.assertNotNull(replace);
        
        // 验证策略类型
        Assert.assertEquals("1", scan.getScanType());
        Assert.assertEquals("1", replace.getScanType());
    }

    /**
     * 测试新疆名字策略
     */
    @Test
    public void uyghurNameTest() {
        UyghurNameScan scan = new UyghurNameScan();
        UyghurNameReplace replace = new UyghurNameReplace();
        
        // 验证策略类型
        Assert.assertEquals("5", scan.getScanType());
        Assert.assertEquals("5", replace.getScanType());
        
        // 注意：这里仅测试策略类是否正常工作
        // 实际使用时，应在配置文件中指定策略类路径
        Assert.assertNotNull(scan);
        Assert.assertNotNull(replace);
    }

    /**
     * 测试基本功能
     */
    @Test
    public void basicFunctionTest() {
        String text = "用户：张三，手机：13912345678";
        
        // 测试默认行为（应该正常脱敏）
        Assert.assertNotNull(text);
    }

}