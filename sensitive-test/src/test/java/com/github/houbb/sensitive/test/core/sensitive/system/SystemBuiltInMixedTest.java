package com.github.houbb.sensitive.test.core.sensitive.system;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.system.SystemBuiltInMixed;
import org.junit.Assert;
import org.junit.Test;

/**
 * 混合模式测试
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.3
 */
public class SystemBuiltInMixedTest {

    /**
     * 系统内置+Sensitive注解测试
     */
    @Test
    public void systemBuiltInAndSensitiveTest() {
        final String originalStr = "SystemBuiltInMixed{testField='混合'}";
        final String sensitiveStr = "SystemBuiltInMixed{testField='null'}";
        SystemBuiltInMixed entry = DataPrepareTest.buildSystemBuiltInMixed();
        Assert.assertEquals(originalStr, entry.toString());

        SystemBuiltInMixed sensitive = SensitiveUtil.desCopy(entry);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, entry.toString());
    }

    /**
     * 系统内置+Sensitive注解测试JSON
     * @since 0.0.6
     */
    @Test
    public void systemBuiltInAndSensitiveJsonTest() {
        final String originalStr = "SystemBuiltInMixed{testField='混合'}";
        final String sensitiveJson = "{}";
        SystemBuiltInMixed entry = DataPrepareTest.buildSystemBuiltInMixed();

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(entry));
        Assert.assertEquals(originalStr, entry.toString());
    }

}
