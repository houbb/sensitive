package com.github.houbb.sensitive.test.model.sensitive.system;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyChineseName;
import com.github.houbb.sensitive.core.api.strategory.StrategyPassword;

/**
 * 系统内置+Sensitive注解混合模式
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.3
 */
public class SystemBuiltInMixed {

    /**
     * 测试字段
     * 1.当多种注解混合的时候，为了简化逻辑，优先选择 @Sensitive 注解。
     */
    @SensitiveStrategyChineseName
    @Sensitive(strategy = StrategyPassword.class)
    private String testField;

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }

    @Override
    public String toString() {
        return "SystemBuiltInMixed{" +
                "testField='" + testField + '\'' +
                '}';
    }

}
