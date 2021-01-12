package com.github.houbb.sensitive.test.model.sensitive.entry;

import com.github.houbb.sensitive.test.model.sensitive.User;

import java.util.Arrays;
import java.util.List;

/**
 * 对象中有列表，列表中放置的为对象
 * @author dev-sxl
 * date 2020-09-14
 * @since 0.0.11
 */
public class CustomUserEntryObject {

    private User user;

    private List<User> userList;

    private User[] userArray;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User[] getUserArray() {
        return userArray;
    }

    public void setUserArray(User[] userArray) {
        this.userArray = userArray;
    }

    @Override
    public String toString() {
        return "CustomUserEntryObject{" +
                "user=" + user +
                ", userList=" + userList +
                ", userArray=" + Arrays.toString(userArray) +
                '}';
    }
}
