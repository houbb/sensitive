package com.github.houbb.sensitive.test.core.sensitive;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.User;
import org.junit.Assert;
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
        final String exceptResult = "123***@qq.com";
        IStrategy strategy = new StrategyEmail();
        final String emailSensitive = (String) strategy.des(email, null);
        Assert.assertEquals(exceptResult, emailSensitive);
    }

    /**
     * 普通脱敏测试
     * @since 0.0.1
     */
    @Test
    public void commonSensitiveTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}";

        User user = DataPrepareTest.buildUser();
        Assert.assertEquals(originalStr, user.toString());

        User sensitiveUser = SensitiveUtil.desCopy(user);
        Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assert.assertEquals(originalStr, user.toString());
    }

}
