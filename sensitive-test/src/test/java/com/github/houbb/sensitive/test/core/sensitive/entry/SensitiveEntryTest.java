package com.github.houbb.sensitive.test.core.sensitive.entry;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.entry.UserEntryBaseType;
import com.github.houbb.sensitive.test.model.sensitive.entry.UserEntryObject;
import com.github.houbb.sensitive.test.model.sensitive.entry.UserGroup;
import org.junit.Assert;
import org.junit.Test;

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
        final String originalStr = "UserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}";
        final String sensitiveStr = "UserEntryBaseType{chineseNameList=[*古, *娲, *羲], chineseNameArray=[*古, *娲, *羲]}";

        UserEntryBaseType userEntryBaseType = DataPrepareTest.buildUserEntryBaseType();
        Assert.assertEquals(originalStr, userEntryBaseType.toString());

        UserEntryBaseType sensitive = SensitiveUtil.desCopy(userEntryBaseType);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, userEntryBaseType.toString());
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveEntryObjectTest() {
        final String originalStr = "UserEntryObject{user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}";
        final String sensitiveStr = "UserEntryObject{user=User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, userList=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userArray=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}]}";

        UserEntryObject userEntryObject = DataPrepareTest.buildUserEntryObject();
        Assert.assertEquals(originalStr, userEntryObject.toString());

        UserEntryObject sensitiveUserEntryObject = SensitiveUtil.desCopy(userEntryObject);
        Assert.assertEquals(sensitiveStr, sensitiveUserEntryObject.toString());
        Assert.assertEquals(originalStr, userEntryObject.toString());
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveUserGroupTest() {
        final String originalStr = "UserGroup{coolUser=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], password='123456', userMap={map=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
        final String sensitiveStr = "UserGroup{coolUser=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, userList=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userSet=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userCollection=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], password='123456', userMap={map=User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}}}";

        UserGroup userGroup = DataPrepareTest.buildUserGroup();
        Assert.assertEquals(originalStr, userGroup.toString());

        UserGroup sensitiveUserGroup = SensitiveUtil.desCopy(userGroup);
        Assert.assertEquals(sensitiveStr, sensitiveUserGroup.toString());
        Assert.assertEquals(originalStr, userGroup.toString());
    }

}
