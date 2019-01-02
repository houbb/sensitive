package com.github.houbb.sensitive.test.core;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.test.model.User;
import org.junit.Test;

/**
 * 脱敏测试类
 * @author binbin.hou
 * date 2018/12/29
 */
public class SensitiveUtilTest {

    @Test
    public void passwordSensitiveTest() {
        User user = buildUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    @Test
    public void singleSensitiveTest() {
        final String email = "123456@qq.com";
        IStrategy strategy = new StrategyEmail();
        final String emailSensitive = (String) strategy.des(email, null);
        System.out.println("脱敏后的邮箱：" + emailSensitive);
    }

    /**
     * 构建测试对象
     * @return 创建后的对象
     */
    private User buildUser() {
        User user = new User();
        user.setUsername("脱敏君");
        user.setPassword("1234567");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }

}
