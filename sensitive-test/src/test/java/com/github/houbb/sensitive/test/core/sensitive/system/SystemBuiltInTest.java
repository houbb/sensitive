package com.github.houbb.sensitive.test.core.sensitive.system;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.system.SystemBuiltInAt;
import com.github.houbb.sensitive.test.model.sensitive.system.SystemBuiltInAtEntry;
import com.github.houbb.sensitive.test.model.sensitive.system.SystemBuiltInAtIdNo;
import org.junit.Assert;
import org.junit.Test;

/**
 * 系统内置注解测试
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.3
 */
public class SystemBuiltInTest {

    /**
     * 普通属性脱敏测试
     */
    @Test
    public void sensitiveTest() {
        final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}";
        final String sensitiveStr = "SystemBuiltInAt{phone='1888****888', password='null', name='脱**', email='12******.com', cardId='123456**********34'}";

        SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());

        SystemBuiltInAt sensitive = SensitiveUtil.desCopy(systemBuiltInAt);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());
    }

    /**
     * 属性包含对象
     */
    @Test
    public void sensitiveEntryTest() {
        final String originalStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}}";
        final String sensitiveStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='1888****888', password='null', name='脱**', email='12******.com', cardId='123456**********34'}}";

        SystemBuiltInAtEntry entry = DataPrepareTest.buildSystemBuiltInAtEntry();
        Assert.assertEquals(originalStr, entry.toString());

        SystemBuiltInAtEntry sensitive = SensitiveUtil.desCopy(entry);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, entry.toString());
    }

    /**
     * 普通属性脱敏 JSON 测试
     * @since 0.0.6
     */
    @Test
    public void sensitiveJsonTest() {
        final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}";
        final String sensitiveJson = "{\"cardId\":\"123456**********34\",\"email\":\"12******.com\",\"name\":\"脱**\",\"phone\":\"1888****888\"}";

        SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(systemBuiltInAt));
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());
    }

    /**
     * 属性包含对象 JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryJsonTest() {
        final String originalStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}}";
        final String sensitiveJson = "{\"entry\":{\"cardId\":\"123456**********34\",\"email\":\"12******.com\",\"name\":\"脱**\",\"phone\":\"1888****888\"}}";

        SystemBuiltInAtEntry entry = DataPrepareTest.buildSystemBuiltInAtEntry();

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(entry));
        Assert.assertEquals(originalStr, entry.toString());
    }

    /**
     * 普通属性脱敏测试
     * @since 0.0.15
     */
    @Test
    public void sensitiveTest2() {
        final String expectOriginalStr = "SystemBuiltInAtIdNo{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234', idNo='130701199310308888'}";
        final String expectSensitiveStr = "SystemBuiltInAtIdNo{phone='1888****888', password='null', name='脱**', email='12******.com', cardId='123456**********34', idNo='1****************8'}";
        final String expectSensitiveJson = "{\"cardId\":\"123456**********34\",\"email\":\"12******.com\",\"idNo\":\"1****************8\",\"name\":\"脱**\",\"phone\":\"1888****888\"}";

        SystemBuiltInAtIdNo original = DataPrepareTest.buildSystemBuiltInAtIdNo();
        SystemBuiltInAtIdNo sensitive = SensitiveUtil.desCopy(original);
        Assert.assertEquals(expectOriginalStr, original.toString());
        Assert.assertEquals(expectSensitiveStr, sensitive.toString());

        String sensitiveJson = SensitiveUtil.desJson(original);
        Assert.assertEquals(expectSensitiveJson, sensitiveJson);
    }

}
