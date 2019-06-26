package com.github.houbb.sensitive.core.api.context;

import com.github.houbb.heaven.annotation.NotThreadSafe;
import com.github.houbb.sensitive.api.IContext;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 脱敏上下文
 * @author binbin.hou
 * date 2019/1/2
 * @since 0.0.1
 */
@NotThreadSafe
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

    /**
     * 类信息
     * @since 0.0.6
     */
    private Class beanClass;

    /**
     * 明细信息
     * @since 0.0.6
     */
    private Object entry;

    /**
     * 新建一个对象实例
     * @return this
     * @since 0.0.6
     */
    public static SensitiveContext newInstance() {
        return new SensitiveContext();
    }

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

    /**
     * @since 0.0.4
     * @return 获取当前字段名称
     */
    @Override
    public String getCurrentFieldName() {
        return this.currentField.getName();
    }

    /**
     * @since 0.0.4
     * @return 获取当前字段值
     */
    @Override
    public Object getCurrentFieldValue() {
        try {
            return this.currentField.get(this.currentObject);
        } catch (IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    @Override
    public List<Field> getAllFieldList() {
        return allFieldList;
    }

    /**
     * 设置当前字段
     * @param allFieldList 所有字段列表
     */
    public void setAllFieldList(List<Field> allFieldList) {
        this.allFieldList = allFieldList;
    }

    /**
     * 添加字段信息
     * 本方法不再使用，将在下个版本直接移除。
     * @param fieldList 字段列表信息
     */
    @Deprecated
    public void addFieldList(List<Field> fieldList) {
        this.allFieldList.addAll(fieldList);
    }

    @Override
    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Object getEntry() {
        return entry;
    }

    public void setEntry(Object entry) {
        this.entry = entry;
    }

}
