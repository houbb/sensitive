package com.github.houbb.sensitive.test.log4j2;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by houbinbin on 2017/2/16.
 */
public class Log4j2AndSlf4jRewriteTest {

  private static final Logger logger = LoggerFactory.getLogger(Log4j2AndSlf4jRewriteTest.class);


  @Test
  public void simpleTest() {
    logger.info("my phone is :13077779999");
  }

  @Test
  public void formatTest() {
    logger.info("my phone:{}", "13077779999");
  }

  @Test
  public void errorTest() {
    try {
      throw new RuntimeException("异常");
    } catch (Exception exception) {
      logger.error("my phone:{}", "13077779999", exception);
    }
  }

}
