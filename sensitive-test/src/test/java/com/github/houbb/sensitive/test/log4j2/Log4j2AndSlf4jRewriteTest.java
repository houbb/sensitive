package com.github.houbb.sensitive.test.log4j2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by houbinbin on 2017/2/16.
 */
public class Log4j2AndSlf4jRewriteTest {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2AndSlf4jRewriteTest.class);


    private static final String TEST_LOG = "mobile:13088887777; bankCard:6217004470007335024, email:mahuateng@qq.com, amount:123.00, " + "IdNo:340110199801016666, name1:李明, name2:李晓明, name3:李泽明天, name4:山东小栗旬" + ", birthday:20220517, GPS:120.882222, IPV4:127.0.0.1, address:中国上海市徐汇区888号;";

    @Test
    public void simpleTest() {
        logger.info(TEST_LOG);
    }

    @Test
    public void formatTest() {
        logger.info("log {}", TEST_LOG);
    }

    @Test
    public void errorTest() {
        try {
            throw new RuntimeException("异常");
        } catch (Exception exception) {
            logger.error(TEST_LOG, exception);
        }
    }

}
