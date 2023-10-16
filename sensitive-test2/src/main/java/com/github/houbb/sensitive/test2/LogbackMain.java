package com.github.houbb.sensitive.test2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackMain {

    private static final Logger LOG = LoggerFactory.getLogger(LogbackMain.class);

    public static void main(String[] args) {
        LOG.info("mobile:13088887777; bankCard:6217004470007335024, email:mahuateng@qq.com, amount:123.00, \" + \"IdNo:340110199801016666, name1:李明, name2:李晓明, name3:李泽明天, name4:山东小栗旬\" + \", birthday:20220517, GPS:120.882222, IPV4:127.0.0.1, address:中国上海市徐汇区888号;");
    }

}
