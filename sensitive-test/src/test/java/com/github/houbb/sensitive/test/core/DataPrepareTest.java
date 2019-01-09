package com.github.houbb.sensitive.test.core;

import com.github.houbb.sensitive.test.model.User;
import com.github.houbb.sensitive.test.model.group.UserGroup;
import com.github.houbb.sensitive.test.model.group.UserNest;

import java.util.*;

/**
 * 数据准备工具
 * @author binbin.hou
 * date 2019/1/9
 */
public final class DataPrepareTest {

    /**
     * 构建用户嵌套信息
     * @return 构建嵌套信息
     */
    public static UserNest buildUserNest() {
        UserNest userNest = new UserNest();
        User user = buildUser();
        userNest.setUser(user);
        userNest.setPasswordList(Arrays.asList("1222, 23453245, 444"));
        userNest.setPasswordArray(new String[]{"1214", "werqwerwqe", "asfsdaf"});
        return userNest;
    }

    /**
     * 构建用户组对象
     * @return 对象
     */
    public static UserGroup buildUserGroup() {
        UserGroup userGroup = new UserGroup();
        User user = buildUser();
        User coolUser = buildUser();

        userGroup.setPassword("123456");
        userGroup.setCoolUser(coolUser);
        userGroup.setUser(user);
        userGroup.setUserCollection(Collections.singletonList(user));
        userGroup.setUserList(Arrays.asList(user));
        userGroup.setUserSet(new HashSet<>(Arrays.asList(user)));
        Map<String, User> map = new HashMap<>();
        map.put("map", user);
        userGroup.setUserMap(map);
        return userGroup;
    }

    /**
     * 构建测试用户对象
     * @return 创建后的对象
     */
    public static User buildUser() {
        User user = new User();
        user.setUsername("脱敏君");
        user.setPassword("1234567");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }

}
