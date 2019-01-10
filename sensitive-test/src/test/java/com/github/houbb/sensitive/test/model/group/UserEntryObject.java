package com.github.houbb.sensitive.test.model.group;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveEntry;
import com.github.houbb.sensitive.core.api.strategory.StrategyPassword;
import com.github.houbb.sensitive.test.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * 对象中有列表，列表中放置的为对象
 * @author binbin.hou
 * date 2019/1/9
 * @since 0.0.2
 */
public class UserEntryObject {

    @SensitiveEntry
    private User user;

    @SensitiveEntry
    @Sensitive(strategy = StrategyPassword.class)
    private List<String> chineseNameList;

    @SensitiveEntry
    @Sensitive(strategy = StrategyPassword.class)
    private String[] chineseNameArray;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getChineseNameList() {
        return chineseNameList;
    }

    public void setChineseNameList(List<String> chineseNameList) {
        this.chineseNameList = chineseNameList;
    }

    public String[] getChineseNameArray() {
        return chineseNameArray;
    }

    public void setChineseNameArray(String[] chineseNameArray) {
        this.chineseNameArray = chineseNameArray;
    }

    @Override
    public String toString() {
        return "UserEntryObject{" +
                "user=" + user +
                ", chineseNameList=" + chineseNameList +
                ", chineseNameArray=" + Arrays.toString(chineseNameArray) +
                '}';
    }
}
