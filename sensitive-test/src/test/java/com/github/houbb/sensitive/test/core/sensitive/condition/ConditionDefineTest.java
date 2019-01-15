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
        final String sensitiveStr = "User{username='脱*君', idCard='123456**********34', password='123456', email='123**@qq.com', phone='188****8888'}";

        SensitiveConditionPassword user = buildUser();
        SensitiveConditionPassword sensitive = SensitiveUtil.desCopy(user);

        Assert.assertEquals(originalStr, user.toString());
        Assert.assertEquals(sensitiveStr, sensitive.toString());
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
