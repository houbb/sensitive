package com.github.houbb.sensitive.test.core.scan.custom;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.sensitive.core.support.scan.CustomCharsReplaceFactory;
import com.github.houbb.sensitive.core.support.scan.CustomCharsScanFactory;
import com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneReplace;
import com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneScan;
import com.github.houbb.sensitive.core.support.scan.custom.UyghurNameReplace;
import com.github.houbb.sensitive.core.support.scan.custom.UyghurNameScan;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
     * 验证配置的扫描列表和自定义策略真正进入执行工厂。
     */
    @Test
    public void customFactoryWiringTest() {
        InternationalPhoneScan customScan = new InternationalPhoneScan();
        InternationalPhoneReplace customReplace = new InternationalPhoneReplace();

        Map<String, ICharsScan> customScans = new HashMap<>();
        customScans.put("1", customScan);
        Map<String, ICharsReplace> customReplaces = new HashMap<>();
        customReplaces.put("1", customReplace);

        Properties config = new Properties();
        config.setProperty("chars.scan.custom.override", "true");
        config.setProperty("chars.scan.defaultReplace", "12");

        CustomCharsScanFactory scanFactory = new CustomCharsScanFactory(
                Collections.singletonList("1"), customScans, config);
        CustomCharsReplaceFactory replaceFactory = new CustomCharsReplaceFactory(
                Collections.singletonList("1"), customReplaces, config);

        Assert.assertEquals(Collections.singletonList("1"), scanFactory.scanTypeList());
        Assert.assertEquals(customScan.getClass(),
                scanFactory.getCharScan("1").getClass());
        Assert.assertNotSame(scanFactory.getCharScan("1"),
                scanFactory.getCharScan("1"));
        Assert.assertEquals(customScan.getClass(),
                scanFactory.allCharScanList().get(0).getClass());
        Assert.assertSame(customReplace, replaceFactory.getReplace("1"));
    }

}
