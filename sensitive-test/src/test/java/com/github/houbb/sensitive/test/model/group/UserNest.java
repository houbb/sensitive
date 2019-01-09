package com.github.houbb.sensitive.test.model.group;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveNest;
import com.github.houbb.sensitive.core.api.strategory.StrategyPassword;
import com.github.houbb.sensitive.test.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * @author binbin.hou
 * date 2019/1/9
 */
public class UserNest {

    @SensitiveNest
    private User user;

    @SensitiveNest
    @Sensitive(strategy = StrategyPassword.class)
    private List<String> passwordList;

    @SensitiveNest
    @Sensitive(strategy = StrategyPassword.class)
    private String[] passwordArray;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getPasswordList() {
        return passwordList;
    }

    public void setPasswordList(List<String> passwordList) {
        this.passwordList = passwordList;
    }

    public String[] getPasswordArray() {
        return passwordArray;
    }

    public void setPasswordArray(String[] passwordArray) {
        this.passwordArray = passwordArray;
    }

    @Override
    public String toString() {
        return "UserNest{" +
                "user=" + user +
                ", passwordList=" + passwordList +
                ", passwordArray=" + Arrays.toString(passwordArray) +
                '}';
    }

}
