package com.github.houbb.sensitive.test.core.sensitive.condition;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.condition.SensitiveConditionPassword;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义条件测试
 * @author binbin.hou
 * date 2019/1/15
 * @since 1.0.0
 */
public class ConditionDefineTest {

    /**
     * 条件测试
     */
    @Test
    public void conditionPasswordTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='脱**', idCard='123456**********34', password='123456', email='12******.com', phone='1888****888'}";

        SensitiveConditionPassword user = buildUser();
        SensitiveConditionPassword sensitive = SensitiveUtil.desCopy(user);

        Assert.assertEquals(originalStr, user.toString());
        Assert.assertEquals(sensitiveStr, sensitive.toString());
    }

    /**
     * 条件测试 JSON
     * @since 0.0.6
     */
    @Test
    public void conditionPasswordJsonTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveJson = "{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"password\":\"123456\",\"phone\":\"1888****888\",\"username\":\"脱**\"}";

        SensitiveConditionPassword user = buildUser();

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(user));
        Assert.assertEquals(originalStr, user.toString());
    }

    /**
     * 构建测试用户对象
     * @return 创建后的对象
     * @since 0.0.1
     */
    private static SensitiveConditionPassword buildUser() {
        SensitiveConditionPassword user = new SensitiveConditionPassword();
        user.setUsername("脱敏君");
        user.setPassword("123456");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }
}
