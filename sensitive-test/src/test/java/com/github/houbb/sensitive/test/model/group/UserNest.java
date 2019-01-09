package com.github.houbb.sensitive.test.model.group;

import com.github.houbb.sensitive.annotation.SensitiveNest;
import com.github.houbb.sensitive.test.model.User;

/**
 * @author binbin.hou
 * date 2019/1/9
 */
public class UserNest {

    @SensitiveNest
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserNest{" +
                "user=" + user +
                '}';
    }
}
