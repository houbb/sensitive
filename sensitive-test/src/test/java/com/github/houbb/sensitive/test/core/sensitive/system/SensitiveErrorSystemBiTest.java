package com.github.houbb.sensitive.test.core.sensitive.system;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.sensitive.system.SensitiveErrorSystemBuiltInModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 错误的使用系统内置类测试
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.3
 */
public class SensitiveErrorSystemBiTest {

    /**
     * 错误的使用系统的内置类
     */
    @Test
    public void errorSystemBuiltInTest() {
        try {
            SensitiveErrorSystemBuiltInModel model = new SensitiveErrorSystemBuiltInModel();
            SensitiveErrorSystemBuiltInModel copy = SensitiveUtil.desCopy(model);
        } catch (Exception e) {
            Assert.assertEquals("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!", e.getMessage());
        }
    }

    /**
     * 错误的使用系统的内置类JSON
     * @since 0.0.6
     */
    @Test
    public void errorSystemBuiltInJsonTest() {
        try {
            SensitiveErrorSystemBuiltInModel model = new SensitiveErrorSystemBuiltInModel();
            final String json = SensitiveUtil.desJson(model);
        } catch (Exception e) {
            Assert.assertEquals("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!", e.getMessage());
        }
    }

}
