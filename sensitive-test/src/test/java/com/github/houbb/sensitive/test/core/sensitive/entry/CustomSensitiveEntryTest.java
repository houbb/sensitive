package com.github.houbb.sensitive.test.core.sensitive.entry;

import com.alibaba.fastjson.JSON;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.entry.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * SensitiveEntry 注解-脱敏测试类
 * @author dev-sxl
 * date 2020-09-14
 * @since 0.0.10
 */
public class CustomSensitiveEntryTest {

    /**
     * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试
     * @since 0.0.10
     */
    @Test
    public void sensitiveEntryBaseTypeTest() {
        final String originalStr = "CustomUserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}";
        final String sensitiveStr = "CustomUserEntryBaseType{chineseNameList=[盘*, 女*, 伏*], chineseNameArray=[盘*, 女*, 伏*]}";

        CustomUserEntryBaseType userEntryBaseType = DataPrepareTest.buildCustomUserEntryBaseType();
        Assert.assertEquals(originalStr, userEntryBaseType.toString());

        CustomUserEntryBaseType sensitive = SensitiveUtil.desCopy(userEntryBaseType);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, userEntryBaseType.toString());
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试
     * @since 0.0.10
     */
    @Test
    public void sensitiveEntryObjectTest() {
        final String originalStr = "CustomUserEntryObject{user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}";
        final String sensitiveStr = "CustomUserEntryObject{user=User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}, userList=[User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}], userArray=[User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}]}";

        CustomUserEntryObject userEntryObject = DataPrepareTest.buildCustomUserEntryObject();
        Assert.assertEquals(originalStr, userEntryObject.toString());

        CustomUserEntryObject sensitiveUserEntryObject = SensitiveUtil.desCopy(userEntryObject);
//        System.out.println(sensitiveUserEntryObject.toString());
        Assert.assertEquals(sensitiveStr, sensitiveUserEntryObject.toString());
        Assert.assertEquals(originalStr, userEntryObject.toString());
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试
     * @since 0.0.10
     */
    @Test
    public void sensitiveUserGroupTest() {
        final String originalStr = "CustomUserGroup{coolUser=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], password='123456', userMap={map=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
        final String sensitiveStr = "CustomUserGroup{coolUser=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}, userList=[User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}], userSet=[User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}], userCollection=[User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}], password='123456', userMap={map=User{username='脱**', idCard='123456**********34', password='null', email='12******.com', phone='1888****888'}}}";

        CustomUserGroup userGroup = DataPrepareTest.buildCustomUserGroup();
        Assert.assertEquals(originalStr, userGroup.toString());

        CustomUserGroup sensitiveUserGroup = SensitiveUtil.desCopy(userGroup);
//        System.out.println(sensitiveUserGroup.toString());
        Assert.assertEquals(sensitiveStr, sensitiveUserGroup.toString());
        Assert.assertEquals(originalStr, userGroup.toString());
    }

    /**
     * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试-JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryBaseTypeJsonTest() {
        final String originalStr = "CustomUserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}";
        final String sensitiveJson = "{\"chineseNameArray\":[\"盘*\",\"女*\",\"伏*\"],\"chineseNameList\":[\"盘*\",\"女*\",\"伏*\"]}";

        CustomUserEntryBaseType userEntryBaseType = DataPrepareTest.buildCustomUserEntryBaseType();

        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(userEntryBaseType));
        Assert.assertEquals(originalStr, userEntryBaseType.toString());
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试-JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryObjectJsonTest() {
        final String originalStr = "CustomUserEntryObject{user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}";
        final String sensitiveJson = "{\"user\":{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"},\"userArray\":[{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"}],\"userList\":[{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"}]}";

        CustomUserEntryObject userEntryObject = DataPrepareTest.buildCustomUserEntryObject();

//        System.out.println(SensitiveUtil.desJson(userEntryObject));
        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(userEntryObject));
        Assert.assertEquals(originalStr, userEntryObject.toString());
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试-JSON
     * 备注：当为对象前台集合对象时，FastJSON 本身的转换结果就是不尽人意的。（或者说是 JSON 的规范）
     * @since 0.0.6
     */
    @Test
    public void sensitiveUserCollectionJsonTest() {
        final String originalStr = "CustomUserCollection{userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userMap={map=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
        final String commonJson = "{\"userArray\":[{\"email\":\"12345@qq.com\",\"idCard\":\"123456190001011234\",\"password\":\"1234567\",\"phone\":\"18888888888\",\"username\":\"脱敏君\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";
        final String sensitiveJson = "{\"userArray\":[{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";

        CustomUserCollection userCollection = DataPrepareTest.buildCustomUserCollection();

        Assert.assertEquals(commonJson, JSON.toJSONString(userCollection));
//        System.out.println(SensitiveUtil.desJson(userCollection));
        Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(userCollection));
        Assert.assertEquals(originalStr, userCollection.toString());
    }

}
