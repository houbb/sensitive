package com.github.houbb.sensitive.test.core.sensitive.custom;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.custom.CustomPasswordEntryModel;
import com.github.houbb.sensitive.test.model.custom.CustomPasswordModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义注解测试
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
public class CustomAnnotationTest {

    /**
     * 自定义注解测试
     */
    @Test
    public void customAnnotationTest() {
        final String originalStr = "CustomPasswordModel{password='hello', fooPassword='123456'}";
        final String sensitiveStr = "CustomPasswordModel{password='**********************', fooPassword='123456'}";
        CustomPasswordModel model = buildCustomPasswordModel();
        Assert.assertEquals(originalStr, model.toString());

        CustomPasswordModel sensitive = SensitiveUtil.desCopy(model);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试
     */
    @Test
    public void customAnnotationEntryTest() {
        final String originalStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='hello', fooPassword='123456'}}";
        final String sensitiveStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='**********************', fooPassword='123456'}}";
        CustomPasswordModel entry = buildCustomPasswordModel();
        CustomPasswordEntryModel model = new CustomPasswordEntryModel();
        model.setEntry(entry);

        Assert.assertEquals(originalStr, model.toString());

        CustomPasswordEntryModel sensitive = SensitiveUtil.desCopy(model);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试 JSON
     * @since 0.0.6
     */
    @Test
    public void customAnnotationJsonTest() {
        final String originalStr = "CustomPasswordModel{password='hello', fooPassword='123456'}";
        final String sensitiveJson = "{\"fooPassword\":\"123456\",\"password\":\"**********************\"}";
        CustomPasswordModel model = buildCustomPasswordModel();

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(model));
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试 JSON
     * @since 0.0.6
     */
    @Test
    public void customAnnotationEntryJsonTest() {
        final String originalStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='hello', fooPassword='123456'}}";
        final String sensitiveJson = "{\"entry\":{\"fooPassword\":\"123456\",\"password\":\"**********************\"}}";
        CustomPasswordModel entry = buildCustomPasswordModel();
        CustomPasswordEntryModel model = new CustomPasswordEntryModel();
        model.setEntry(entry);

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(model));
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 构建自定义密码对象
     * @return 对象
     */
    private CustomPasswordModel buildCustomPasswordModel(){
        CustomPasswordModel model = new CustomPasswordModel();
        model.setPassword("hello");
        model.setFooPassword("123456");
        return model;
    }

}
