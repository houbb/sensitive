package com.github.houbb.sensitive.test.core;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.User;
import org.junit.Test;

/**
 * @author binbin.hou
 * @date 2018/12/29
 */
public class SensitiveUtilTest {

    @Test
    public void passwordSensitiveTest() {
        User user = newUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    private User newUser() {
        User user = new User();
        user.setUsername("脱敏君");
        user.setPassword("123456");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }

}
