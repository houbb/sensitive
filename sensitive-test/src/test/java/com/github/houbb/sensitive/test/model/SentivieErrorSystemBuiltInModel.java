package com.github.houbb.sensitive.test.model;

import com.github.houbb.sensitive.test.annotation.SensitiveErrorSystemBuiltIn;

/**
 * 模拟错误的注解使用
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.03
 */
public class SentivieErrorSystemBuiltInModel {

    @SensitiveErrorSystemBuiltIn
    private String errorField;

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

}
