package com.github.houbb.sensitive.core.api.context;

import com.github.houbb.sensitive.api.IContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 脱敏上下文
 * @author binbin.hou
 * date 2019/1/2
 */
public class SensitiveContext implements IContext {

    /**
     * 当前对象
     */
    private Object currentObject;

    /**
     * 当前字段
     */
    private Field currentField;

    /**
     * 所有字段
     */
    private List<Field> allFieldList = new ArrayList<>();

    @Override
    public Object getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(Object currentObject) {
        this.currentObject = currentObject;
    }

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

    /**
     * 添加字段信息
     * @param fieldList 字段列表信息
     */
    public void addFieldList(List<Field> fieldList) {
        this.allFieldList.addAll(fieldList);
    }

}
