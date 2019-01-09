# 项目介绍

日志脱敏是常见的安全需求。普通的基于工具类方法的方式，对代码的入侵性太强。编写起来又特别麻烦。

本项目提供基于注解的方式，并且内置了常见的脱敏方式，便于开发。

用户也可以基于自己的实际需要，自定义注解。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/sensitive/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/sensitive)
[![Build Status](https://www.travis-ci.org/houbb/sensitive.svg?branch=master)](https://www.travis-ci.org/houbb/sensitive?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/sensitive/badge.svg?branch=master)](https://coveralls.io/github/houbb/sensitive?branch=master)

> [变更日志](doc/CHANGE_LOG.md)

## 日志脱敏

为了金融交易的安全性，国家强制规定对于以下信息是要日志脱敏的：

1. 用户名

2. 手机号

3. 邮箱

4. 银行卡号

5. 密码

## 持久化加密

存储的时候上面的信息都需要加密，密码为不可逆加密，其他为可逆加密。

类似的功能有很多。不在本系统的解决范围内。

# 特性

1. 基于注解的日志脱敏

2. 可以自定义策略实现，策略生效条件

3. 常见的脱敏内置方案

4. 支持 jdk1.7+

# 快速开始

## maven 导入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 定义对象

- User.java

我们对 password 使用脱敏，指定脱敏策略为 StrategyPassword。(直接返回 null)

```java
public class User {

    @Sensitive(strategy = StrategyChineseName.class)
    private String username;
    
    @Sensitive(strategy = StrategyCardId.class)
    private String idCard;
    
    @Sensitive(strategy = StrategyPassword.class)
    private String password;
    
    @Sensitive(strategy = StrategyEmail.class)
    private String email;
    
    @Sensitive(strategy = StrategyPhone.class)
    private String phone;
    
    //Getter & Setter
    //toString()
}
```

- 测试

```java
    @Test
    public void UserSensitiveTest() {
        User user = buildUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    private User buildUser() {
        User user = new User();
        user.setUsername("脱敏君");
        user.setPassword("123456");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }
```

- 输出信息如下

```
脱敏前原始： User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}
脱敏对象： User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}
脱敏后原始： User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}
```

我们可以直接利用 `sensitiveUser` 去打印日志信息，而这个对象对于代码其他流程不影响，我们依然可以使用原来的 `user` 对象。

## 自定义脱敏策略生效的场景

默认情况下，我们指定的场景都是生效的。

但是你可能需要有些情况下不进行脱敏，比如有些用户密码为 123456，你觉得这种用户不脱敏也罢。

- UserPasswordCondition.java

```java
@Sensitive(condition = ConditionFooPassword.class, strategy = StrategyPassword.class)
private String password;
``` 

其他保持不变，我们指定了一个 condition，实现如下：

- ConditionFooPassword.java

```java
public class ConditionFooPassword implements ICondition {
    @Override
    public boolean valid(IContext context) {
        try {
            Field field = context.getCurrentField();
            final Object currentObj = context.getCurrentObject();
            final String password = (String) field.get(currentObj);
            return !password.equals("123456");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
```

也就是只有当密码不是 123456 时密码脱敏策略才会生效。

## 针对单个字段

上面的例子是基于注解式的编程，如果你只是单个字段。比如 

- singleSensitiveTest

```java
@Test
public void singleSensitiveTest() {
    final String email = "123456@qq.com";
    IStrategy strategy = new StrategyEmail();
    final String emailSensitive = (String) strategy.des(email, null);
    System.out.println("脱敏后的邮箱：" + emailSensitive);
}
```

- 日志信息

```
脱敏后的邮箱：123***@qq.com
```






