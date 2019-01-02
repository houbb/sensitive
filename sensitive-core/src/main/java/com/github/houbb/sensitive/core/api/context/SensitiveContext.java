package com.github.houbb.sensitive.core.api.context;

import com.github.houbb.sensitive.api.IContext;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 脱敏上下文
 * @author binbin.hou
 * @date 2019/1/2
 */
public class SensitiveContext implements IContext {

    /**
     * 当前字段
     */
    private Field currentField;

    /**
     * 所有字段
     */
    private List<Field> allFieldList;

    @Override
    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    @Override
    public List<Field> getAllFieldList() {
        return allFieldList;
    }

    public void setAllFieldList(List<Field> allFieldList) {
        this.allFieldList = allFieldList;
    }
}
