package com.github.houbb.sensitive.test.model.sensitive.entry;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveIgnore;
import com.github.houbb.sensitive.core.api.strategory.StrategyPassword;
import com.github.houbb.sensitive.test.model.sensitive.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 */
public class UserGroup {

    /**
     * 不参与脱敏的用户
     */
    @SensitiveIgnore
    private User coolUser;

    private User user;

    private List<User> userList;

    private Set<User> userSet;

    private Collection<User> userCollection;

    @Sensitive(strategy = StrategyPassword.class)
    private String password;

    private Map<String, User> userMap;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCoolUser() {
        return coolUser;
    }

    public void setCoolUser(User coolUser) {
        this.coolUser = coolUser;
    }

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
        return "UserGroup{" +
                "coolUser=" + coolUser +
                ", user=" + user +
                ", userList=" + userList +
                ", userSet=" + userSet +
                ", userCollection=" + userCollection +
                ", password='" + password + '\'' +
                ", userMap=" + userMap +
                '}';
    }
}
