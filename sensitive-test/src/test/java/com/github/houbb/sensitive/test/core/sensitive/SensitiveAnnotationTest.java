package com.github.houbb.sensitive.test.core.sensitive;

import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.User;
import com.github.houbb.sensitive.test.model.sensitive.UserAnnotationBean;
import com.github.houbb.sensitive.test.model.sensitive.UserIdNo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Sensitive 脱敏测试类
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.1
 */
public class SensitiveAnnotationTest {

    /**
     * 注解属性测试
     * @since 0.0.1
     */
    @Test
    public void annotationSensitiveTest() {
        UserAnnotationBean bean  = new UserAnnotationBean();
        bean.setUsername("张三");
        bean.setPassword("123456");
        bean.setPassport("CN1234567");
        bean.setPhone("13066668888");
        bean.setAddress("中国上海市浦东新区外滩18号");
        bean.setEmail("whatanice@code.com");
        bean.setBirthday("20220831");
        bean.setGps("66.888888");
        bean.setIp("127.0.0.1");
        bean.setMaskAll("可恶啊我会被全部掩盖");
        bean.setMaskHalf("还好我只会被掩盖一半");
        bean.setMaskRange("我比较灵活指定掩盖范围");
        bean.setBandCardId("666123456789066");
        bean.setIdNo("360123202306018888");

        final String originalStr = "UserAnnotationBean{username='张三', password='123456', passport='CN1234567', idNo='360123202306018888', bandCardId='666123456789066', phone='13066668888', email='whatanice@code.com', address='中国上海市浦东新区外滩18号', birthday='20220831', gps='66.888888', ip='127.0.0.1', maskAll='可恶啊我会被全部掩盖', maskHalf='还好我只会被掩盖一半', maskRange='我比较灵活指定掩盖范围'}";
        final String sensitiveStr = "UserAnnotationBean{username='张*', password='null', passport='CN*****67', idNo='3****************8', bandCardId='666123*******66', phone='1306****888', email='wh************.com', address='中国上海********8号', birthday='20*****1', gps='66*****88', ip='127***0.1', maskAll='**********', maskHalf='还好我只会*****', maskRange='我*********围'}";
        final String expectSensitiveJson = "{\"address\":\"中国上海********8号\",\"bandCardId\":\"666123*******66\",\"birthday\":\"20*****1\",\"email\":\"wh************.com\",\"gps\":\"66*****88\",\"idNo\":\"3****************8\",\"ip\":\"127***0.1\",\"maskAll\":\"**********\",\"maskHalf\":\"还好我只会*****\",\"maskRange\":\"我*********围\",\"passport\":\"CN*****67\",\"phone\":\"1306****888\",\"username\":\"张*\"}";

        UserAnnotationBean sensitiveUser = SensitiveUtil.desCopy(bean);
//        System.out.println(sensitiveUser.toString());
//        System.out.println(bean.toString());
//        System.out.println(SensitiveUtil.desJson(bean));
        Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assert.assertEquals(originalStr, bean.toString());

        String sensitiveJson = SensitiveUtil.desJson(bean);
        Assert.assertEquals(expectSensitiveJson, sensitiveJson);
    }

}
