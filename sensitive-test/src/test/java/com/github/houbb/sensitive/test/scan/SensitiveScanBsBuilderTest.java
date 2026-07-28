package com.github.houbb.sensitive.test.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.ICharsCore;
import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.replace.PhoneCharsReplace;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.chars.scan.support.scan.PhoneConditionCharScan;
import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 日志插件统一扫描器的扩展契约测试。
 */
public class SensitiveScanBsBuilderTest {

    @Test
    public void customStrategiesUseTheirOwnTypeTest() {
        Properties config = new Properties();
        config.setProperty(
                SensitiveScanBsBuilder.KEY_BUILT_IN_SCAN_TYPES, "");
        config.setProperty(
                SensitiveScanBsBuilder.KEY_BUILT_IN_REPLACE_TYPES, "");
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CUSTOM_SCAN_CLASSES,
                CustomPhoneScan.class.getName());
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CUSTOM_REPLACE_CLASSES,
                CustomPhoneReplace.class.getName());
        config.setProperty(
                SensitiveScanBsBuilder.KEY_SHOW_HASH, "false");

        String result = SensitiveScanBsBuilder.build(config)
                .scanAndReplace("value:13912345678");

        Assert.assertEquals("value:139****5678", result);
    }

    @Test
    public void completeComponentsHaveHighestPriorityTest() {
        TestScanFactory.created = 0;
        TestReplaceFactory.created = 0;
        TestCore.created = 0;
        TestHash.created = 0;

        Properties config = new Properties();
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CHARS_SCAN_FACTORY_CLASS,
                TestScanFactory.class.getName());
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CHARS_REPLACE_FACTORY_CLASS,
                TestReplaceFactory.class.getName());
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CHARS_CORE_CLASS,
                TestCore.class.getName());
        config.setProperty(
                SensitiveScanBsBuilder.KEY_CHARS_REPLACE_HASH_CLASS,
                TestHash.class.getName());

        // 完整工厂存在时，不应再解析内置工厂的策略列表。
        config.setProperty(
                SensitiveScanBsBuilder.KEY_BUILT_IN_SCAN_TYPES,
                "not-a-built-in-type");
        config.setProperty(
                SensitiveScanBsBuilder.KEY_BUILT_IN_REPLACE_TYPES,
                "not-a-built-in-type");

        String result = SensitiveScanBsBuilder.build(config)
                .scanAndReplace("value:13912345678");

        Assert.assertEquals("value:139****5678", result);
        Assert.assertEquals(1, TestScanFactory.created);
        Assert.assertEquals(1, TestReplaceFactory.created);
        Assert.assertEquals(1, TestCore.created);
        Assert.assertEquals(1, TestHash.created);
    }

    @Test
    public void legacyNumericConfigurationRemainsCompatibleTest() {
        Properties config = new Properties();
        config.setProperty("chars.scan.scanList", "1");
        config.setProperty("chars.scan.replaceList", "1");
        config.setProperty("chars.scan.defaultReplace", "12");
        config.setProperty("chars.scan.showHash", "false");

        String result = SensitiveScanBsBuilder.build(config)
                .scanAndReplace("value:13912345678");

        Assert.assertEquals("value:139****5678", result);
    }

    /**
     * 使用非数字标识，验证标识来自 getScanType()。
     */
    public static class CustomPhoneScan extends PhoneConditionCharScan {

        @Override
        public String getScanType() {
            return "custom-phone";
        }
    }

    public static class CustomPhoneReplace extends PhoneCharsReplace {

        @Override
        public String getScanType() {
            return "custom-phone";
        }
    }

    public static class TestScanFactory implements ICharsScanFactory {

        private static int created;

        private final ICharsScanFactory delegate =
                CharsScans.defaults(Collections.singletonList("1"));

        public TestScanFactory() {
            created++;
        }

        @Override
        public List<String> scanTypeList() {
            return delegate.scanTypeList();
        }

        @Override
        public ICharsScan getCharScan(String scanType) {
            return delegate.getCharScan(scanType);
        }

        @Override
        public List<ICharsScan> allCharScanList() {
            return delegate.allCharScanList();
        }
    }

    public static class TestReplaceFactory implements ICharsReplaceFactory {

        private static int created;

        private final ICharsReplaceFactory delegate =
                CharsReplaces.defaultsReplaceFactory(
                        Collections.singletonList("1"), "12");

        public TestReplaceFactory() {
            created++;
        }

        @Override
        public ICharsReplace getReplace(String scanType) {
            return delegate.getReplace(scanType);
        }
    }

    public static class TestCore implements ICharsCore {

        private static int created;

        private final ICharsCore delegate = CharsCores.defaults();

        public TestCore() {
            created++;
        }

        @Override
        public String scanAndReplace(
                String originalString, CharsScanContext context) {
            return delegate.scanAndReplace(originalString, context);
        }
    }

    public static class TestHash implements ICharsReplaceHash {

        private static int created;

        public TestHash() {
            created++;
        }

        @Override
        public void hash(
                byte[] bytes,
                CharsScanContext context,
                StringBuilder builder) {
            // 测试用无哈希实现。
        }
    }

}
