package com.github.houbb.sensitive.test.core;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.test.model.User;
import com.github.houbb.sensitive.test.model.group.UserGroup;
import com.github.houbb.sensitive.test.model.group.UserNest;
import org.junit.Test;

/**
 * 脱敏测试类
 * @author binbin.hou
 * date 2018/12/29
 */
public class SensitiveUtilTest {

    /**
     * 普通脱敏测试
     */
    @Test
    public void commonSensitiveTest() {
        User user = DataPrepareTest.buildUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    /**
     * 嵌套类用户脱敏测试
     */
    @Test
    public void sensitiveUserNestTest() {
        UserNest userNest = DataPrepareTest.buildUserNest();
        System.out.println("脱敏前原始： " + userNest);
        UserNest sensitiveUserNest = SensitiveUtil.desCopy(userNest);
        System.out.println("脱敏对象： " + sensitiveUserNest);
        System.out.println("脱敏后原始： " + userNest);
    }

    /**
     * 嵌套类用户组脱敏测试
     */
    @Test
    public void sensitiveUserGroupTest() {
        UserGroup userGroup = DataPrepareTest.buildUserGroup();
        System.out.println("脱敏前原始： " + userGroup);
        UserGroup sensitiveUserGroup = SensitiveUtil.desCopy(userGroup);
        System.out.println("脱敏对象： " + sensitiveUserGroup);
        System.out.println("脱敏后原始： " + userGroup);
    }

    @Test
    public void singleSensitiveTest() {
        final String email = "123456@qq.com";
        IStrategy strategy = new StrategyEmail();
        final String emailSensitive = (String) strategy.des(email, null);
        System.out.println("脱敏后的邮箱：" + emailSensitive);
    }

}
