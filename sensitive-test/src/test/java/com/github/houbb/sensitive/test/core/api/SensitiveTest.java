package com.github.houbb.sensitive.test.core.api;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.User;
import com.github.houbb.sensitive.test.model.group.UserEntryBaseType;
import com.github.houbb.sensitive.test.model.group.UserEntryObject;
import com.github.houbb.sensitive.test.model.group.UserGroup;
import org.junit.Test;

/**
 * Sensitive 脱敏测试类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public class SensitiveTest {

    /**
     * 单个属性测试
     * @since 0.0.1
     */
    @Test
    public void singleSensitiveTest() {
        final String email = "123456@qq.com";
        IStrategy strategy = new StrategyEmail();
        final String emailSensitive = (String) strategy.des(email, null);
        System.out.println("脱敏后的邮箱：" + emailSensitive);
    }

    /**
     * 普通脱敏测试
     * @since 0.0.1
     */
    @Test
    public void commonSensitiveTest() {
        User user = DataPrepareTest.buildUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

}
