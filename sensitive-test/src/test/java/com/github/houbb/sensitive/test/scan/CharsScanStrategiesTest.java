package com.github.houbb.sensitive.test.scan;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * chars-scan 内置策略契约测试。
 *
 * <p>1-10 是扫描及对应替换策略；11-13 是未命中专用替换策略，
 * 依赖库没有为 11-13 提供独立扫描器。</p>
 */
public class CharsScanStrategiesTest {

    @Test
    public void builtInScanAndReplaceStrategiesTest() {
        assertStrategy("1", "13912345678", "139****5678");
        assertStrategy("2", "11010519491231002X", "1****************X");
        assertStrategy("3", "6217000010002105024", "6217***********5024");
        assertStrategy("4", "tester@example.com", "test**@example.com");
        assertStrategy("5", "张三", "张*");
        assertStrategy("6", "20220517", "********");
        assertStrategy("7", "120.882222", "**********");
        assertStrategy("8", "127.0.0.1", "127******");
        assertStrategy("9", "中国上海市徐汇区888号", "中国上海市徐******");
        assertStrategy("10", "E12345678", "E1*****78");
    }

    @Test
    public void defaultReplacementStrategiesTest() {
        Assert.assertEquals("value:13912345678", replacePhoneWithDefaultStrategy("11"));
        Assert.assertEquals("value:13912******", replacePhoneWithDefaultStrategy("12"));
        Assert.assertEquals("value:***********", replacePhoneWithDefaultStrategy("13"));
    }

    private void assertStrategy(String strategy, String original, String masked) {
        String result = CharsScanBs.newInstance()
                .charsScanFactory(new SimpleCharsScanFactory(Collections.singletonList(strategy)))
                .charsReplaceHash(CharsReplaceHashes.none())
                .init()
                .scanAndReplace("value:" + original);

        Assert.assertEquals("策略 " + strategy + " 的脱敏结果不符合预期",
                "value:" + masked, result);
    }

    private String replacePhoneWithDefaultStrategy(String replacementStrategy) {
        return CharsScanBs.newInstance()
                .charsScanFactory(new SimpleCharsScanFactory(Collections.singletonList("1")))
                .charsReplaceFactory(CharsReplaces.defaultsReplaceFactory(
                        Collections.<String>emptyList(), replacementStrategy))
                .charsReplaceHash(CharsReplaceHashes.none())
                .init()
                .scanAndReplace("value:13912345678");
    }
}
