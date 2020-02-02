package com.github.houbb.sensitive.test.benchmark;

import com.alibaba.fastjson.JSON;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.sensitive.User;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 性能测试
 * @author binbin.hou
 * @since 0.0.9
 */
@Ignore
public class SensitiveBenchmarkTest {

    private static final int COUNT = 1000000;

    @Test
    public void toJsonTest() {
        User user = DataPrepareTest.buildUser();

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < COUNT; i++) {
            String json = JSON.toJSONString(user);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("fast json " + (endTime-startTime));
    }

    @Test
    public void handleSetterTest() {
        User user = DataPrepareTest.buildUser();

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < COUNT; i++) {
            User sensitiveUser = new User();
            sensitiveUser.setUsername(SensitiveStrategyUtil.chineseName(user.getUsername()));
            sensitiveUser.setPassword(SensitiveStrategyUtil.password(user.getPassword()));
            sensitiveUser.setEmail(SensitiveStrategyUtil.password(user.getEmail()));
            sensitiveUser.setIdCard(SensitiveStrategyUtil.cardId(user.getIdCard()));
            sensitiveUser.setPhone(SensitiveStrategyUtil.phone(user.getPhone()));
        }
        long endTime = System.currentTimeMillis();

        System.out.println("handle set " + (endTime-startTime));
    }

    @Test
    public void desJsonTest() {
        User user = DataPrepareTest.buildUser();

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < COUNT; i++) {
            String json = SensitiveUtil.desJson(user);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("des json " + (endTime-startTime));
    }

    @Test
    public void desCopyTest() {
        User user = DataPrepareTest.buildUser();

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < COUNT; i++) {
            User sensitiveUser = SensitiveUtil.desCopy(user);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("des copy " + (endTime-startTime));
    }

}
