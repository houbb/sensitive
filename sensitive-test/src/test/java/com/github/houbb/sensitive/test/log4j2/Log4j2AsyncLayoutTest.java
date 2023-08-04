package com.github.houbb.sensitive.test.log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by houbinbin on 2017/2/16.
 */
public class Log4j2AsyncLayoutTest {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2AsyncLayoutTest.class);


    private static final String TEST_LOG = "mobile:13088887777; bankCard:6217004470007335024, email:mahuateng@qq.com, amount:123.00, " + "IdNo:340110199801016666, name1:李明, name2:李晓明, name3:李泽明天, name4:山东小栗旬" + ", birthday:20220517, GPS:120.882222, IPV4:127.0.0.1, address:中国上海市徐汇区888号;";

    private static final ExecutorService executorService = Executors.newFixedThreadPool(30);

    public static void main(String[] args) throws InterruptedException {
        final AtomicInteger atomicInteger = new AtomicInteger(0);

        for(int i = 0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 100; i++) {
                        logger.info(atomicInteger.get() + "-" + TEST_LOG);
                        atomicInteger.incrementAndGet();
                    }

                    for(int i = 0; i < 100; i++) {
                        logger.error(atomicInteger.get() + "-" + "我异常了，甘。。。。。" + TEST_LOG);
                        atomicInteger.incrementAndGet();
                    }
                }
            });
        }
        logger.info("-------------------------- DONE --------------------- ");
    }

}
