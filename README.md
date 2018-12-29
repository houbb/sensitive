# 项目介绍

日志脱敏是常见的安全需求。普通的基于工具类方法的方式，对代码的入侵性太强。编写起来又特别麻烦。

本项目提供基于注解的方式，并且内置了常见的脱敏方式，便于开发。

用户也可以基于自己的实际需要，自定义注解。

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

2. 可以自定义注解

3. 常见的脱敏内置方案

# 快速开始

## maven 导入

```xml

```

## 定义对象

- User.java

我们对 password 使用脱敏，指定脱敏策略为 StrategyPassword。(直接返回 null)

```java
public class User {

    private String username;

    private String idCard;

    @Sensitive(strategy = StrategyPassword.class)
    private String password;

    private String email;

    private String phone;
    
    //Getter & Setter
    //toString()
}
```

- 测试

```java
    @Test
    public void passwordSensitiveTest() {
        User user = newUser();
        System.out.println("脱敏前原始： " + user);
        User sensitiveUser = SensitiveUtil.desCopy(user);
        System.out.println("脱敏对象： " + sensitiveUser);
        System.out.println("脱敏后原始： " + user);
    }

    private User newUser() {
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
脱敏前原始： User{username='脱敏君', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}
脱敏对象： User{username='脱敏君', idCard='123456190001011234', password='null', email='12345@qq.com', phone='18888888888'}
脱敏后原始： User{username='脱敏君', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}
```

我们可以直接利用 `sensitiveUser` 去打印日志信息，而这个对象对于代码其他流程不影响，我们依然可以使用原来的 `user` 对象。

sensitiveUser 中 password 是已经做了脱敏处理。

# 使用技巧

## 结合 spring aop

我们可以结合 spring-aop，统一打印方法的入参和出参。

可以对入参和出参的字段指定脱敏策略，直接打印。

## 单个策略的使用

所有的脱敏策略都实现了接口 `IStrategy`，你可以手动创建实现(可结合 IOC 容器)的策略方法，针对单个的属性进行调用脱敏。

# 待优化的地方

## 独享创建

这种方式为了避免修改原始对象，创建了一个全新的对象，有点点浪费，应该可以优化。

## 其他方法

可以基于 log4j2/logback 等转换器进行敏感信息的脱敏，但是不具有不同的 log 框架的可移植性。





