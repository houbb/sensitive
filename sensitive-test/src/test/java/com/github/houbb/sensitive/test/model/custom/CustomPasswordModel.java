package com.github.houbb.sensitive.test.model.custom;

import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyPassword;
import com.github.houbb.sensitive.test.annotation.custom.SensitiveCustomPasswordCondition;
import com.github.houbb.sensitive.test.annotation.custom.SensitiveCustomPasswordStrategy;

/**
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
public class CustomPasswordModel {

    @SensitiveCustomPasswordCondition
    @SensitiveCustomPasswordStrategy
    private String password;

    @SensitiveCustomPasswordCondition
    @SensitiveStrategyPassword
    private String fooPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFooPassword() {
        return fooPassword;
    }

    public void setFooPassword(String fooPassword) {
        this.fooPassword = fooPassword;
    }

    @Override
    public String toString() {
        return "CustomPasswordModel{" +
                "password='" + password + '\'' +
                ", fooPassword='" + fooPassword + '\'' +
                '}';
    }
}
