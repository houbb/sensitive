package com.github.houbb.sensitive.test.core.sensitive.custom;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.custom.CustomPasswordModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * date 2019/1/17
 */
public class CustomConditionTest {

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
