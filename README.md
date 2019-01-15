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

4. java 深拷贝，且原始对象不用实现任何接口。

# 快速开始

## maven 导入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive</artifactId>
    <version>0.0.2</version>
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

## 属性为集合或者对象

如果某个属性是单个集合或者对象，则需要使用注解 `@SensitiveEntry`。

- 放在集合属性上，且属性为普通对象

会遍历每一个属性，执行上面的脱敏策略。

- 放在对象属性上

会处理对象中各个字段上的脱敏注解信息。

- 放在集合属性上，且属性为对象

遍历每一个对象，处理对象中各个字段上的脱敏注解信息。

### 放在集合属性上，且属性为普通对象

- UserEntryBaseType.java

作为演示，集合中为普通的字符串。

```java
public class UserEntryBaseType {

    @SensitiveEntry
    @Sensitive(strategy = StrategyChineseName.class)
    private List<String> chineseNameList;

    @SensitiveEntry
    @Sensitive(strategy = StrategyChineseName.class)
    private String[] chineseNameArray;
    
    //Getter & Setter & toString()
}
```

- 构建对象

```java
/**
 * 构建用户-属性为列表，列表中为基础属性
 * @return 构建嵌套信息
 * @since 0.0.2
 */
public static UserEntryBaseType buildUserEntryBaseType() {
    UserEntryBaseType userEntryBaseType = new UserEntryBaseType();
    userEntryBaseType.setChineseNameList(Arrays.asList("盘古", "女娲", "伏羲"));
    userEntryBaseType.setChineseNameArray(new String[]{"盘古", "女娲", "伏羲"});
    return userEntryBaseType;
}
```

- 测试演示

```java
/**
 * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试
 * @since 0.0.2
 */
@Test
public void sensitiveEntryBaseTypeTest() {
    UserEntryBaseType userEntryBaseType = DataPrepareTest.buildUserEntryBaseType();
    System.out.println("脱敏前原始： " + userEntryBaseType);
    UserEntryBaseType sensitive = SensitiveUtil.desCopy(userEntryBaseType);
    System.out.println("脱敏对象： " + sensitive);
    System.out.println("脱敏后原始： " + userEntryBaseType);
}
```

- 日志信息

```
脱敏前原始： UserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}
脱敏对象： UserEntryBaseType{chineseNameList=[*古, *娲, *羲], chineseNameArray=[*古, *娲, *羲]}
脱敏后原始： UserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}
```

### 放在对象属性上

- 演示对象

这里的 User 和上面的 User 对象一致。


```java
public class UserEntryObject {

    @SensitiveEntry
    private User user;

    @SensitiveEntry
    private List<User> userList;

    @SensitiveEntry
    private User[] userArray;
    
    //...
}
```

- 对象构建

```java
/**
 * 构建用户-属性为列表，数组。列表中为对象。
 * @return 构建嵌套信息
 * @since 0.0.2
 */
public static UserEntryObject buildUserEntryObject() {
    UserEntryObject userEntryObject = new UserEntryObject();
    User user = buildUser();
    User user2 = buildUser();
    User user3 = buildUser();
    userEntryObject.setUser(user);
    userEntryObject.setUserList(Arrays.asList(user2));
    userEntryObject.setUserArray(new User[]{user3});
    return userEntryObject;
}
```

- 测试演示

```java
/**
 * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试
 * @since 0.0.2
 */
@Test
public void sensitiveEntryObjectTest() {
    UserEntryObject userEntryObject = DataPrepareTest.buildUserEntryObject();
    System.out.println("脱敏前原始： " + userEntryObject);
    UserEntryObject sensitiveUserEntryObject = SensitiveUtil.desCopy(userEntryObject);
    System.out.println("脱敏对象： " + sensitiveUserEntryObject);
    System.out.println("脱敏后原始： " + userEntryObject);
}
```

- 测试结果

```java
脱敏前原始： UserEntryObject{user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}
脱敏对象： UserEntryObject{user=User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, userList=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userArray=[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}]}
脱敏后原始： UserEntryObject{user=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}
```

# 系统内置脱敏策略

## 引入原因

如果你看了前面的内容，会看到这样的代码：

```java
@Sensitive(strategy = StrategyChineseName.class)
private String username;
```

但是这种需求很常见，就引入更简单的写法，如下：

```java
@SensitiveStrategyChineseName
private String name;
```

和上面效果是一致的。

不足：无法灵活指定生效条件，下个版本准备解决这个问题。

## 系统内置注解

| 注解 | 等价 @Sensitive | 备注 |
|:-------|:------|:------|
| `@SensitiveStrategyChineseName` | `@Sensitive(strategy = StrategyChineseName.class)` | 中文名称脱敏 |
| `@SensitiveStrategyPassword` | `@Sensitive(strategy = StrategyPassword.class)` |  密码脱敏 |
| `@SensitiveStrategyEmail` | `@Sensitive(strategy = StrategyEmail.class)` | email 脱敏 |
| `@SensitiveStrategyCardId` | `@Sensitive(strategy = StrategyCardId.class)` | 卡号脱敏 |
| `@SensitiveStrategyPhone` | `@Sensitive(strategy = StrategyPhone.class)` | 手机号脱敏 |

## 使用案例

使用的方式和 `@Sensitive` 是一样的，只是一种简化，方便日常使用。

与 `@SensitiveEntry` 的结合和 `@Sensitive` 完全一致，此处不再演示。

- SystemBuiltInAt.java

定义测试对象

```java
@SensitiveStrategyPhone
    private String phone;

    @SensitiveStrategyPassword
    private String password;

    @SensitiveStrategyChineseName
    private String name;

    @SensitiveStrategyEmail
    private String email;

    @SensitiveStrategyCardId
    private String cardId;
    
    //Getter Setter
    //toString()
}
```

- 对象构建

```java
/**
 * 构建系统内置对象
 * @return 构建后的对象
 * @since 0.0.3
 */
public static SystemBuiltInAt buildSystemBuiltInAt() {
    SystemBuiltInAt systemBuiltInAt = new SystemBuiltInAt();
    systemBuiltInAt.setName("脱敏君");
    systemBuiltInAt.setPassword("1234567");
    systemBuiltInAt.setEmail("12345@qq.com");
    systemBuiltInAt.setCardId("123456190001011234");
    systemBuiltInAt.setPhone("18888888888");
    return systemBuiltInAt;
}
```

- 测试方法

测试方法断言如下。

```java
/**
 * 普通属性脱敏测试
 */
@Test
public void sensitiveTest() {
    final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}";
    final String sensitiveStr = "SystemBuiltInAt{phone='188****8888', password='null', name='脱*君', email='123**@qq.com', cardId='123456**********34'}";

    SystemBuiltInAt systemBuiltInAt = buildSystemBuiltInAt();
    Assert.assertEquals(originalStr, systemBuiltInAt.toString());

    SystemBuiltInAt sensitive = SensitiveUtil.desCopy(systemBuiltInAt);
    Assert.assertEquals(sensitiveStr, sensitive.toString());
    Assert.assertEquals(originalStr, systemBuiltInAt.toString());
}
```

## 与 @Sensitive 混合使用

如果你将新增的注解 `@SensitiveStrategyChineseName` 与 `@Sensitive` 同时在一个字段上使用。

为了简化逻辑，优先选择执行 `@Sensitive`，如果 `@Sensitive` 执行脱敏，
那么 `@SensitiveStrategyChineseName` 将不会生效。  

如：

```java
/**
 * 测试字段
 * 1.当多种注解混合的时候，为了简化逻辑，优先选择 @Sensitive 注解。
 */
@SensitiveStrategyChineseName
@Sensitive(strategy = StrategyPassword.class)
private String testField;
```

# 需求 & BUGS

> [issues](https://github.com/houbb/sensitive/issues)

# 欢迎加入开发

如果你对本项目有兴趣，并且对代码有一定追求，可以申请加入本项目开发。

如果你善于写文档，或者愿意补全测试案例，也非常欢迎加入。





