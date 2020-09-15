package com.github.houbb.sensitive.test.core.util;

import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.test.model.bugs.Father;
import org.junit.Test;

/**
 * BUGS 5
 * @author binbin.hou
 * @since 0.0.11
 */
public class SensitiveUtilTest {

    @Test
    public void bugs5NpeTest() {
        Father father = new Father();

        Father copy = SensitiveUtil.desCopy(father);
        System.out.println(copy);
    }

}
