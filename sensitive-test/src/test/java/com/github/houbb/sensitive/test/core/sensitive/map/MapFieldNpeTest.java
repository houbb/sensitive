package com.github.houbb.sensitive.test.core.sensitive.map;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.util.entry.SensitiveEntryUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试包含 Map 字段的对象脱敏时是否会出现 NPE
 */
public class MapFieldNpeTest {

    /**
     * 包含 Map 字段的测试对象
     */
    static class UserWithMap {
        @Sensitive(strategy = com.github.houbb.sensitive.core.api.strategory.StrategyPassword.class)
        private String password;
        
        private Map<String, String> userInfo;
        
        private String username;

        public UserWithMap() {
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, String> getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(Map<String, String> userInfo) {
            this.userInfo = userInfo;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    /**
     * 测试包含 Map 字段的对象脱敏，复现 NPE 问题
     */
    @Test
    public void testMapFieldNpe() {
        UserWithMap user = new UserWithMap();
        user.setPassword("123456");
        user.setUsername("testUser");
        
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("key1", "value1");
        userInfo.put("key2", "value2");
        user.setUserInfo(userInfo);

        // 这行代码在修复前会抛出 NullPointerException
        String json = SensitiveUtil.desJson(user);
        
        Assert.assertNotNull(json);
        System.out.println("JSON: " + json);
        
        // 验证脱敏后的结果（fastjson2 使用 NotWriteDefaultValue，null 值不会写入）
        // password 字段会被脱敏处理，但因为测试对象中 password 字段有值，所以应该出现在 JSON 中
        Assert.assertTrue(json.contains("\"userInfo\""));
        Assert.assertTrue(json.contains("\"username\":\"testUser\""));
    }

    /**
     * 测试空的 Map 字段
     */
    @Test
    public void testEmptyMapField() {
        UserWithMap user = new UserWithMap();
        user.setPassword("123456");
        user.setUsername("testUser");
        user.setUserInfo(new HashMap<>());

        String json = SensitiveUtil.desJson(user);
        
        Assert.assertNotNull(json);
        System.out.println("Empty Map JSON: " + json);
    }

    /**
     * 测试 null 的 Map 字段
     */
    @Test
    public void testNullMapField() {
        UserWithMap user = new UserWithMap();
        user.setPassword("123456");
        user.setUsername("testUser");
        user.setUserInfo(null);

        String json = SensitiveUtil.desJson(user);
        
        Assert.assertNotNull(json);
        System.out.println("Null Map JSON: " + json);
    }

    /**
     * 测试复杂的嵌套 Map 结构
     */
    static class ComplexMapObject {
        @Sensitive(strategy = com.github.houbb.sensitive.core.api.strategory.StrategyPassword.class)
        private String password;
        
        private Map<String, Object> nestedMap;
        
        public ComplexMapObject() {
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, Object> getNestedMap() {
            return nestedMap;
        }

        public void setNestedMap(Map<String, Object> nestedMap) {
            this.nestedMap = nestedMap;
        }
    }

    /**
     * 测试嵌套 Map 结构
     */
    @Test
    public void testNestedMapStructure() {
        ComplexMapObject obj = new ComplexMapObject();
        obj.setPassword("secret123");
        
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("level1", "value1");
        
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("innerKey", "innerValue");
        nestedMap.put("innerMap", innerMap);
        
        obj.setNestedMap(nestedMap);

        // 这行代码在修复前可能会抛出 NullPointerException
        String json = SensitiveUtil.desJson(obj);
        
        Assert.assertNotNull(json);
        System.out.println("Nested Map JSON: " + json);
        
        // 验证脱敏后的结果（password 字段有值，应该被脱敏处理）
        Assert.assertTrue(json.contains("\"nestedMap\""));
        // password 字段有值，应该出现在 JSON 中并被脱敏
    }
    
    /**
     * 直接测试 SensitiveEntryUtil.hasSensitiveEntry 方法对 null field 的处理
     */
    @Test
    public void testNullFieldHandling() {
        // 测试当 field 为 null 时，不应该抛出 NPE
        boolean result = SensitiveEntryUtil.hasSensitiveEntry(null);
        Assert.assertFalse(result);
    }
    
    /**
     * 测试使用反射调用 hasSensitiveEntry 方法
     */
    @Test
    public void testHasSensitiveEntryWithNullField() {
        // 这个测试确保 hasSensitiveEntry 方法能够安全处理 null 参数
        try {
            Field nullField = null;
            boolean hasEntry = SensitiveEntryUtil.hasSensitiveEntry(nullField);
            Assert.assertFalse("Null field should not have sensitive entry", hasEntry);
        } catch (NullPointerException e) {
            Assert.fail("Should not throw NPE for null field, but got: " + e.getMessage());
        }
    }
}