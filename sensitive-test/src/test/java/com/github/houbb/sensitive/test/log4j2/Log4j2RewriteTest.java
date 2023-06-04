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
  public void testRewrite() {
    logger.info("my phone: {}", "13077779999");
    logger.info("my phone is 13077779999");
  }

}
