package com.github.houbb.sensitive.test.model.sensitive.entry;

import com.github.houbb.sensitive.test.model.sensitive.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象集合
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.6
 */
public class UserCollection {

    private User[] userArray;

    private List<User> userList;

    private Set<User> userSet;

    private Collection<User> userCollection;

    private Map<String, User> userMap;

    public User[] getUserArray() {
        return userArray;
    }

    public void setUserArray(User[] userArray) {
        this.userArray = userArray;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public String toString() {
        return "UserCollection{" +
                "userList=" + userList +
                ", userSet=" + userSet +
                ", userCollection=" + userCollection +
                ", userMap=" + userMap +
                '}';
    }

}
