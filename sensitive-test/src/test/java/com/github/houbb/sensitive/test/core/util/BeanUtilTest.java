package com.github.houbb.sensitive.test.core.util;

import com.github.houbb.sensitive.core.util.BeanUtil;
import com.github.houbb.sensitive.test.core.DataPrepareTest;
import com.github.houbb.sensitive.test.model.group.UserGroup;
import org.junit.Test;

/**
 * @author binbin.hou
 * date 2019/1/9
 */
public class BeanUtilTest {

    @Test
    public void copyPropertiesTest() {
        UserGroup userGroup = DataPrepareTest.buildUserGroup();
        System.out.println("原始信息: " + userGroup);
        UserGroup copyUserGroup = new UserGroup();
        BeanUtil.copyProperties(userGroup, copyUserGroup);
        System.out.println("复制信息: " + userGroup);
    }


}
