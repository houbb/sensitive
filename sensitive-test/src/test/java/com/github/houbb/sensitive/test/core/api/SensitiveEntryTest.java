package com.github.houbb.sensitive.test.core.api;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.util.ClassUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.group.UserEntryBaseType;
import com.github.houbb.sensitive.test.model.group.UserEntryObject;
import com.github.houbb.sensitive.test.model.group.UserGroup;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * SensitiveEntry 注解-脱敏测试类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.2
 */
public class SensitiveEntryTest {

    /**
     * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveEntryBaseTypeTest() {
        UserEntryBaseType userEntryBaseType = DataPrepareTest.buildUserEntryBaseType();
        System.out.println("脱敏前原始： " + userEntryBaseType);
        UserEntryBaseType sensitive = SensitiveUtil.desCopy(userEntryBaseType);
        System.out.println("脱敏对象： " + sensitive);
        System.out.println("脱敏后原始： " + userEntryBaseType);
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveEntryObjectTest() {
        UserEntryObject userEntryObject = DataPrepareTest.buildUserEntryObject();
        System.out.println("脱敏前原始： " + userEntryObject);
        UserEntryObject sensitiveUserEntryObject = SensitiveUtil.desCopy(userEntryObject);
        System.out.println("脱敏对象： " + sensitiveUserEntryObject);
        System.out.println("脱敏后原始： " + userEntryObject);
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveUserGroupTest() {
        UserGroup userGroup = DataPrepareTest.buildUserGroup();
        System.out.println("脱敏前原始： " + userGroup);
        UserGroup sensitiveUserGroup = SensitiveUtil.desCopy(userGroup);
        System.out.println("脱敏对象： " + sensitiveUserGroup);
        System.out.println("脱敏后原始： " + userGroup);
    }

}
