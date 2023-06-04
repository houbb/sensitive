package com.github.houbb.sensitive.test.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author d
 * @since 1.0.0
 */
public class Log4j2Test {

    private static final Logger logger = LogManager.getLogger(Log4j2Test.class);

    public static void main(String[] args) {
        logger.info("simple info message");
        logger.info("simple info message with format {}", "hello");
    }

}
