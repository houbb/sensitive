package com.github.houbb.sensitive.test.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * Created by houbinbin on 2017/2/16.
 */
public class Log4j2RewriteTest {

  private Logger logger = LogManager.getLogger();


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
