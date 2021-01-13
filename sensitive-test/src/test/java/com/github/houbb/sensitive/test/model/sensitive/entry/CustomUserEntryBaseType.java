package com.github.houbb.sensitive.test.model.sensitive.entry;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.core.api.strategory.StrategyChineseName;

import java.util.Arrays;
import java.util.List;

/**
 * 属性为列表，列表中放置的为基础属性
 *
 * @author dev-sxl
 * date 2020-09-14
 * @since 0.0.11
 */
public class CustomUserEntryBaseType {


    @Sensitive(strategy = StrategyChineseName.class)
    private List<String> chineseNameList;

    @Sensitive(strategy = StrategyChineseName.class)
    private String[] chineseNameArray;

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
        return "CustomUserEntryBaseType{" +
                "chineseNameList=" + chineseNameList +
                ", chineseNameArray=" + Arrays.toString(chineseNameArray) +
                '}';
    }
}
