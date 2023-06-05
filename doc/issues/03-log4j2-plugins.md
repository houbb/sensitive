# 前言

[金融用户敏感数据如何优雅地实现脱敏？](https://mp.weixin.qq.com/s/ljChFiNLzV6GLaUDjehA0Q)

[日志脱敏之后，无法根据信息快速定位怎么办？](https://mp.weixin.qq.com/s/tZqOH_8QTKrD1oaclNoewg)

经过了这两篇文章之后，我们对日志脱敏应该有了一定的理解。

但是实际项目中，我们遇到的情况往往更加复杂：

1）项目的 java bean 定义不规范，大量接口使用 map。

2）历史项目众多，改造成本巨大。

种种原因，导致使用注解的方式耗费大量的时间。但是一般给我们改造的时间是有限的。

那么，有没有一种方法可以统一对敏感信息进行脱敏处理呢？

答案是有的，我们可以基于 log4j2 实现自己的脱敏策略，统一实现日志的脱敏。

## log4j2 Rewrite

我们可以基于 log4j2 RewritePolicy 统一使用脱敏策略。

本项目自 V1.2.0 添加对应支持，后续将提升对应的可拓展性。

说明：如果使用 slf4j 接口，实现为 log4j2 时也是支持的。

# 使用入门

## maven 引入

引入核心脱敏包。

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-log4j2</artifactId>
    <version>1.2.1</version>
</dependency>
```

其他的一般项目中也有，如 log4j2 包：

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>${log4j2.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>${log4j2.version}</version>
</dependency>
```

## log4j2.xml 配置

例子如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" packages = "com.github.houbb.sensitive.log4j2.rewrite">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
        </Console>
        <Rewrite name="rewrite">
            <AppenderRef ref="Console"/>
            <SensitiveRewritePolicy/>
        </Rewrite>
    </Appenders>
    <Loggers>
        <Root level="DEBUG">
            <AppenderRef ref="rewrite" />
        </Root>
    </Loggers>
</Configuration>
```

几个步骤：

1. 指定 package 为 `packages = "com.github.houbb.sensitive.log4j2.rewrite"`

2. 按照 log4j2 Rewrite 规范，指定重写策略为 `SensitiveRewritePolicy`

3. 输出时，直接指定为对应的重写之后的结果 `<AppenderRef ref="rewrite" />`

## 测试

正常的日志打印：

```java
private static final String TEST_LOG = "mobile:13088887777; bankCard:6217004470007335024, email:mahuateng@qq.com, amount:123.00, " +
        "IdNo:340110199801016666, name1:李明, name2:李晓明, name3:李泽明天, name4:山东小栗旬" +
        ", birthday:20220517, GPS:120.882222, IPV4:127.0.0.1, address:中国上海市徐汇区888号;";

logger.info(TEST_LOG);
```

自动脱敏效果如下：

```
mobile:130****7777|9FC4D36D63D2B6DC5AE1297544FBC5A2; bankCard:6217***********5024|444F49289B30944AB8C6C856AEA21180, email:mahu*****@qq.com|897915594C94D981BA86C9E83ADD449C, amount:123.00, IdNo:3****************6|F9F05E4ABB3591FC8EA481E8DE1FA4D6, name1:李*|15095D14367F7F02655030D498A4BA03, name2:李**|035E3C0D1A0410367FE6EB8335B2BFDE, name3:李泽**|B87138E5E80AEC87D2581A25CAA3809D, name4:山东***|6F2178D34BC7DD0A07936B5AFF39A16F, birthday:********|1F88D983FAFC50022651122B42F084A0, GPS:**********|E281A9A52DE915154285148D68872CA2, IPV4:127******|F528764D624DB129B32C21FBCA0CB8D6, address:中国上海市徐******|821A601949B1BD18DCBAAE27F2E27147;
```

ps: 这里是为了演示各种效果，实际默认对应为 1,2,3,4 这几种策略。

## log4j2 配置定制化

为了满足各种用户的场景，在 V1.2.1 引入了 SensitiveRewritePolicy 策略的可配置化。

### 默认配置

log4j2 配置中，`SensitiveRewritePolicy` 配置默认等价于

```xml
<SensitiveRewritePolicy
        prefix=":=&apos;&quot;"
        scanList = "1,2,3,4"
        replaceList = "1,2,3,4"
        defaultReplace = "12"
        replaceHash = "md5"
/>
```

### 属性说明

SensitiveRewritePolicy 策略的属性说明。

| 属性 | 说明          | 默认值                    | 备注                                       |
|:---|:------------|:-----------------------|:-----------------------------------------|
|  prefix  | 需要脱敏信息的匹配前缀 | `:='"`                 | 降低误判率                                    |
|  replaceHash  | 哈希策略模式      | `md5`                  | 支持 md5/none 两种模式                         |
|  scanList  | 敏感扫描策略列表    | `1,2,3,4` | 1~10 内置的10种敏感信息扫描策略，多个用逗号隔开              |
|  replaceList  | 敏感替换策略列表    | `1,2,3,4` | 1~10 内置的10种敏感信息替换策略，多个用逗号隔开              |
|  defaultReplace  | 敏感替换默认策略  | `12`                   | 1~13 内置的13种敏感信息替换策略，指定一个。当列表没有匹配时，默认使用这个 |

其中 1-13 的内置策略说明如下：

| 策略标识 | 说明 |
|:----|:----|
| 1 | 手机号 |
| 2 | 身份证 |
| 3 | 银行卡 |
| 4 | 邮箱 |
| 5 | 中国人名 |
| 6 | 出生日期 |
| 7 | GPS |
| 8 | IPV4 |
| 9 | 地址 |
| 10 | 护照 |
| 11 | 匹配任意不掩盖 |
| 12 | 匹配任意半掩盖 |
| 13 | 匹配任意全掩盖 |

### 不足之处

这里的策略自定义和 log4j2 的插件化比起来，确实算不上强大，但是可以满足 99% 的脱敏场景。

后续有时间考虑类似 log4j2 的 plugins 思想，实现更加灵活的自定义策略。

# 性能

正则的替换可能会导致 cpu 飙升等问题，替换的策略也有限制。

实现的底层不是基于正则的，性能要远高于正则，大概是 2 倍左右，符合企业级应用性能。

后续将添加对应的 benchmark。

# 小结

实际项目中，建议二者结合使用。

基于 log4j2 的方式统一处理非常方便，但是是性能和准确性要有一定的折中。

如果是新项目，建议使用注解的方式，通过日志标准规范开发，后续拓展性也更加灵活。
