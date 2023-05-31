package com.github.houbb.sensitive.test.bs;

import com.github.houbb.deep.copy.fastjson.FastJsonDeepCopy;
import com.github.houbb.sensitive.core.bs.SensitiveBs;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.9
 */
public class SensitiveBsTest {

    @Test
    public void desCopyTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}";

        User user = DataPrepareTest.buildUser();
        Assert.assertEquals(originalStr, user.toString());

        User sensitiveUser = SensitiveBs.newInstance().desCopy(user);
        Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assert.assertEquals(originalStr, user.toString());
    }

    @Test
    public void desJsonTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"}";

        User user = DataPrepareTest.buildUser();
        Assert.assertEquals(originalStr, user.toString());

        String sensitiveUserJson = SensitiveBs.newInstance().desJson(user);
        Assert.assertEquals(sensitiveStr, sensitiveUserJson);
        Assert.assertEquals(originalStr, user.toString());
    }

    @Test
    public void configTest() {
        User user = DataPrepareTest.buildUser();

        SensitiveBs.newInstance()
                .deepCopy(FastJsonDeepCopy.getInstance())
                .desJson(user);
    }

}
