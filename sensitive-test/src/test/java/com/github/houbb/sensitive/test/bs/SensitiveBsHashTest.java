package com.github.houbb.sensitive.test.bs;

import com.github.houbb.hash.core.core.hash.Hashes;
import com.github.houbb.sensitive.core.bs.SensitiveBs;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.9
 */
public class SensitiveBsHashTest {

    @Test
    public void desCopyHashTest() {
        final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='脱**|00871641C1724BB717DD01E7E5F7D98A', idCard='123456**********34|1421E4C0F5BF57D3CC557CFC3D667C4E', password='null', email='12******.com|6EAA6A25C8D832B63429C1BEF149109C', phone='1888****888|5425DE6EC14A0722EC09A6C2E72AAE18'}";
        final String expectJson = "{\"email\":\"12******.com|6EAA6A25C8D832B63429C1BEF149109C\",\"idCard\":\"123456**********34|1421E4C0F5BF57D3CC557CFC3D667C4E\",\"phone\":\"1888****888|5425DE6EC14A0722EC09A6C2E72AAE18\",\"username\":\"脱**|00871641C1724BB717DD01E7E5F7D98A\"}";

        User user = DataPrepareTest.buildUser();
        Assert.assertEquals(originalStr, user.toString());

        // 指定哈希策略
        final SensitiveBs sensitiveBs = SensitiveBs.newInstance()
                .hash(Hashes.md5());

        User sensitiveUser = sensitiveBs.desCopy(user);
        String sensitiveJson = sensitiveBs.desJson(user);

        Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assert.assertEquals(originalStr, user.toString());
        Assert.assertEquals(expectJson, sensitiveJson);
    }

}
