# 项目介绍

日志脱敏是常见的安全需求。普通的基于工具类方法的方式，对代码的入侵性太强，编写起来又特别麻烦。

[sensitive](https://github.com/houbb/sensitive) 项目提供基于注解的方式，并且内置了常见的脱敏方式，便于开发。

支持 logback 和 log4j2 等常见的日志脱敏插件。

**日志插件解决正则匹配长文本可能出现的回溯问题，性能远超正则**。

[![Build Status](https://travis-ci.com/houbb/sensitive.svg?branch=master)](https://travis-ci.com/houbb/sensitive)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/sensitive/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/sensitive)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/houbb/sensitive/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/houbb/sensitive)

## 日志脱敏

为了金融交易的安全性，国家强制规定对于以下信息是要日志脱敏的：

1. 用户名

2. 手机号

3. 邮箱

4. 银行卡号

5. 密码

6. 身份证号

## 持久化加密

存储的时候上面的信息都需要加密，密码为不可逆加密，其他为可逆加密。

类似的功能有很多。不在本系统的解决范围内。

# 特性

1. 基于注解的日志脱敏。

2. 可以自定义策略实现，策略生效条件。

3. 内置常见的十几种脱敏内置方案。

4. java 深拷贝，且原始对象不用实现任何接口。

[5. 支持用户自定义注解。](https://github.com/houbb/sensitive#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%B3%A8%E8%A7%A3)

[6. 支持基于 FastJSON2 直接生成脱敏后的 json](https://github.com/houbb/sensitive#%E7%94%9F%E6%88%90%E8%84%B1%E6%95%8F%E5%90%8E%E7%9A%84-json)

[7. 支持自定义哈希策略，更加方便定位日志问题](https://github.com/houbb/sensitive#%E9%85%8D%E7%BD%AE%E5%93%88%E5%B8%8C%E7%AD%96%E7%95%A5)

[8. 支持基于 log4j2 的统一脱敏策略](https://github.com/houbb/sensitive#log4j2-%E6%8F%92%E4%BB%B6%E7%BB%9F%E4%B8%80%E8%84%B1%E6%95%8F)

[9. 支持基于 logback 的统一脱敏策略](https://github.com/houbb/sensitive#logback-%E8%84%B1%E6%95%8F%E6%8F%92%E4%BB%B6)

## 项目推荐

下面是一些日志、加解密、脱敏安全相关的库推荐：

| 项目                                                                    | 介绍                    |
|:----------------------------------------------------------------------|:----------------------|
| [sensitive-word](https://github.com/houbb/sensitive-word)             | 高性能敏感词核心库             |
| [sensitive-word-admin](https://github.com/houbb/sensitive-word-admin) | 敏感词控台，前后端分离           |
| [sensitive](https://github.com/houbb/sensitive)                       | 高性能日志脱敏组件             |
| [auto-log](https://github.com/houbb/auto-log)                         | 统一日志切面组件，支持全链路traceId |
| [encryption-local](https://github.com/houbb/encryption-local)         | 离线加密机组件               |
| [encryption](https://github.com/houbb/encryption)         | 加密机标准API+本地客户端        |
| [encryption-server](https://github.com/houbb/encryption-server)        | 加密机服务                 |

## 变更日志

> [变更日志](https://github.com/houbb/sensitive/blob/master/CHANGE_LOG.md)

### v-1.8.0 新特性

- 新增 showHash 配置项，支持控制是否显示哈希值
- 新增全局配置类 SensitiveConfig，统一配置管理
- 新增配置文件 sensitive.properties，便于配置管理
- 支持多种配置方式：编程式配置、系统属性、配置文件

### v-1.6.0 新特性

- 添加 logback 脱敏插件

## 拓展阅读

[日志开源组件（一）java 注解结合 spring aop 实现自动输出日志](https://houbb.github.io/2023/08/06/auto-log-01-overview)

[日志开源组件（二）java 注解结合 spring aop 实现日志traceId唯一标识](https://houbb.github.io/2023/08/06/auto-log-02-trace-id)

[日志开源组件（三）java 注解结合 spring aop 自动输出日志新增拦截器与过滤器](https://houbb.github.io/2023/08/06/auto-log-03-filter)

[日志开源组件（四）如何动态修改 spring aop 切面信息？让自动日志输出框架更好用](https://houbb.github.io/2023/08/06/auto-log-04-dynamic-aop)

[日志开源组件（五）如何将 dubbo filter 拦截器原理运用到日志拦截器中？](https://houbb.github.io/2023/08/06/auto-log-05-dubbo-interceptor)

[日志开源组件（六）Adaptive Sampling 自适应采样](https://mp.weixin.qq.com/s/9JH3WfR6Y474LCbY2mZxZQ)

[高性能日志脱敏组件（一）java 日志脱敏框架 sensitive，优雅的打印脱敏日志](https://mp.weixin.qq.com/s/xzQNDF7s705iurk7N0uheQ)

[高性能日志脱敏组件（二）金融用户敏感数据如何优雅地实现脱敏？](https://mp.weixin.qq.com/s/ljChFiNLzV6GLaUDjehA0Q)

[高性能日志脱敏组件（三）日志脱敏之后，无法根据信息快速定位怎么办？](https://mp.weixin.qq.com/s/tZqOH_8QTKrD1oaclNoewg)

[高性能日志脱敏组件（四）基于 log4j2 插件实现统一日志脱敏，性能远超正则替换](https://mp.weixin.qq.com/s/ZlWRqT7S92aXFuy-l9Uh3Q)

[高性能日志脱敏组件（五）已支持 log4j2 和 logback 插件](https://mp.weixin.qq.com/s/3ARK6PW7pyUhAbO2ctnndg)

# 快速开始

## 环境准备

JDK 1.8+

Maven 3.x

## maven 导入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-core</artifactId>
    <version>1.9.0</version>
</dependency>
```

## 核心 api 简介

`SensitiveUtil` 工具类的核心方法列表如下：

| 序号 | 方法 | 参数 | 结果 | 说明 |
|:---|:---|:---|:---|:---|
| 1 | desCopy() | 目标对象 | 深度拷贝脱敏对象 | 适应性更强 |
| 2 | desJson() | 目标对象 | 脱敏对象 json | 性能较好 |
| 3 | desCopyCollection() | 目标对象集合 | 深度拷贝脱敏对象集合 | |
| 4 | desJsonCollection() | 目标对象集合 | 脱敏对象 json 集合 | |

## 定义对象

- UserAnnotationBean.java

通过注解，指定每一个字段的脱敏策略。

```java
public class UserAnnotationBean {

    @SensitiveStrategyChineseName
    private String username;

    @SensitiveStrategyPassword
    private String password;

    @SensitiveStrategyPassport
    private String passport;

    @SensitiveStrategyIdNo
    private String idNo;

    @SensitiveStrategyCardId
    private String bandCardId;

    @SensitiveStrategyPhone
    private String phone;

    @SensitiveStrategyEmail
    private String email;

    @SensitiveStrategyAddress
    private String address;

    @SensitiveStrategyBirthday
    private String birthday;

    @SensitiveStrategyGps
    private String gps;

    @SensitiveStrategyIp
    private String ip;

    @SensitiveStrategyMaskAll
    private String maskAll;

    @SensitiveStrategyMaskHalf
    private String maskHalf;

    @SensitiveStrategyMaskRange
    private String maskRange;

    //Getter & Setter
    //toString()
}
```

- 数据准备

构建一个最简单的测试对象：

```java
UserAnnotationBean bean  = new UserAnnotationBean();
bean.setUsername("张三");
bean.setPassword("123456");
bean.setPassport("CN1234567");
bean.setPhone("13066668888");
bean.setAddress("中国上海市浦东新区外滩18号");
bean.setEmail("whatanice@code.com");
bean.setBirthday("20220831");
bean.setGps("66.888888");
bean.setIp("127.0.0.1");
bean.setMaskAll("可恶啊我会被全部掩盖");
bean.setMaskHalf("还好我只会被掩盖一半");
bean.setMaskRange("我比较灵活指定掩盖范围");
bean.setBandCardId("666123456789066");
bean.setIdNo("360123202306018888");
```

- 测试代码

```
final String originalStr = "UserAnnotationBean{username='张三', password='123456', passport='CN1234567', idNo='360123202306018888', bandCardId='666123456789066', phone='13066668888', email='whatanice@code.com', address='中国上海市浦东新区外滩18号', birthday='20220831', gps='66.888888', ip='127.0.0.1', maskAll='可恶啊我会被全部掩盖', maskHalf='还好我只会被掩盖一半', maskRange='我比较灵活指定掩盖范围'}";
final String sensitiveStr = "UserAnnotationBean{username='张*', password='null', passport='CN*****67', idNo='3****************8', bandCardId='666123*******66', phone='1306****888', email='wh************.com', address='中国上海********8号', birthday='20*****1', gps='66*****88', ip='127***0.1', maskAll='**********', maskHalf='还好我只会*****', maskRange='我*********围'}";
final String expectSensitiveJson = "{\"address\":\"中国上海********8号\",\"bandCardId\":\"666123*******66\",\"birthday\":\"20*****1\",\"email\":\"wh************.com\",\"gps\":\"66*****88\",\"idNo\":\"3****************8\",\"ip\":\"127***0.1\",\"maskAll\":\"**********\",\"maskHalf\":\"还好我只会*****\",\"maskRange\":\"我*********围\",\"passport\":\"CN*****67\",\"phone\":\"1306****888\",\"username\":\"张*\"}";

UserAnnotationBean sensitiveUser = SensitiveUtil.desCopy(bean);
Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
Assert.assertEquals(originalStr, bean.toString());

String sensitiveJson = SensitiveUtil.desJson(bean);
Assert.assertEquals(expectSensitiveJson, sensitiveJson);
```

我们可以直接利用 `sensitiveUser` 去打印日志信息，而这个对象对于代码其他流程不影响，我们依然可以使用原来的 `user` 对象。

当然，也可以使用 `sensitiveJson` 打印日志信息。

# @Sensitive 注解

## 说明

`@SensitiveStrategyChineseName` 这种注解是为了便于用户使用，本质上等价于 `@Sensitive(strategy = StrategyChineseName.class)`。

`@Sensitive` 注解可以指定对应的脱敏策略。

## 内置注解与映射

| 编号 | 注解                              | 等价 @Sensitive                                      | 备注       |
|:---|:--------------------------------|:---------------------------------------------------|:---------|
| 1  | `@SensitiveStrategyChineseName` | `@Sensitive(strategy = StrategyChineseName.class)` | 中文名称脱敏   |
| 2  | `@SensitiveStrategyPassword`    | `@Sensitive(strategy = StrategyPassword.class)`    | 密码脱敏     |
| 3  | `@SensitiveStrategyEmail`       | `@Sensitive(strategy = StrategyEmail.class)`       | email 脱敏 |
| 4  | `@SensitiveStrategyCardId`      | `@Sensitive(strategy = StrategyCardId.class)`      | 卡号脱敏     |
| 5  | `@SensitiveStrategyPhone`       | `@Sensitive(strategy = StrategyPhone.class)`       | 手机号脱敏    |
| 6  | `@SensitiveStrategyIdNo`        | `@Sensitive(strategy = StrategyIdNo.class)`        | 身份证脱敏    |
| 6  | `@SensitiveStrategyAddress`     | `@Sensitive(strategy = StrategyAddress.class)`     | 地址脱敏     |
| 7  | `@SensitiveStrategyGps`         | `@Sensitive(strategy = StrategyGps.class)`     | GPS 脱敏   |
| 8  | `@SensitiveStrategyIp`          | `@Sensitive(strategy = StrategyIp.class)`     | IP 脱敏    |
| 9  | `@SensitiveStrategyBirthday`    | `@Sensitive(strategy = StrategyBirthday.class)`     | 生日脱敏     |
| 10 | `@SensitiveStrategyPassport`    | `@Sensitive(strategy = StrategyPassport.class)`     | 护照脱敏     |
| 11 | `@SensitiveStrategyMaskAll`     | `@Sensitive(strategy = StrategyMaskAll.class)`     | 全部脱敏     |
| 12 | `@SensitiveStrategyMaskHalf`    | `@Sensitive(strategy = StrategyMaskHalf.class)`     | 一半脱敏     |
| 13 | `@SensitiveStrategyMaskRange`   | `@Sensitive(strategy = StrategyMaskRange.class)`     | 指定范围脱敏   |

## @Sensitive 定义

```java
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 注解生效的条件
     * @return 条件对应的实现类
     */
    Class<? extends ICondition> condition() default ConditionAlwaysTrue.class;

    /**
     * 执行的策略
     * @return 策略对应的类型
     */
    Class<? extends IStrategy> strategy();

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

# 更多特性

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

### 放在对象属性上

例子如下：

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

# 自定义注解

- v0.0.4 新增功能。允许功能自定义条件注解和策略注解。
- v0.0.11 新增功能。允许功能自定义级联脱敏注解。

## 案例1

### 自定义密码脱敏策略&自定义密码脱敏策略生效条件

- 策略脱敏

```java
/**
 * 自定义密码脱敏策略
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(CustomPasswordStrategy.class)
public @interface SensitiveCustomPasswordStrategy {
}
```

- 脱敏生效条件

```java
/**
 * 自定义密码脱敏策略生效条件
 * @author binbin.hou
 * date 2019/1/17
 * @since 0.0.4
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveCondition(ConditionFooPassword.class)
public @interface SensitiveCustomPasswordCondition{
}
```

- TIPS

`@SensitiveStrategy` 策略单独使用的时候，默认是生效的。

如果有 `@SensitiveCondition` 注解，则只有当条件满足时，才会执行脱敏策略。

`@SensitiveCondition` 只会对系统内置注解和自定义注解生效，因为 `@Sensitive` 有属于自己的策略生效条件。

- 策略优先级

`@Sensitive` 优先生效，然后是系统内置注解，最后是用户自定义注解。

### 对应的实现

两个元注解 `@SensitiveStrategy`、`@SensitiveCondition` 分别指定了对应的实现。

- CustomPasswordStrategy.java

```java
public class CustomPasswordStrategy implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return "**********************";
    }

}
```

- ConditionFooPassword.java

```java
/**
 * 让这些 123456 的密码不进行脱敏
 * @author binbin.hou
 * date 2019/1/2
 * @since 0.0.1
 */
public class ConditionFooPassword implements ICondition {
    @Override
    public boolean valid(IContext context) {
        try {
            Field field = context.getCurrentField();
            final Object currentObj = context.getCurrentObject();
            final String name = (String) field.get(currentObj);
            return !name.equals("123456");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
```

### 定义测试对象

定义一个使用自定义注解的对象。
 
```java
public class CustomPasswordModel {

    @SensitiveCustomPasswordCondition
    @SensitiveCustomPasswordStrategy
    private String password;

    @SensitiveCustomPasswordCondition
    @SensitiveStrategyPassword
    private String fooPassword;
    
    //其他方法
}
```

### 测试

```java
/**
 * 自定义注解测试
 */
@Test
public void customAnnotationTest() {
    final String originalStr = "CustomPasswordModel{password='hello', fooPassword='123456'}";
    final String sensitiveStr = "CustomPasswordModel{password='**********************', fooPassword='123456'}";
    CustomPasswordModel model = buildCustomPasswordModel();
    Assert.assertEquals(originalStr, model.toString());

    CustomPasswordModel sensitive = SensitiveUtil.desCopy(model);
    Assert.assertEquals(sensitiveStr, sensitive.toString());
    Assert.assertEquals(originalStr, model.toString());
}
```

构建对象的方法如下：

```java
/**
 * 构建自定义密码对象
 * @return 对象
 */
private CustomPasswordModel buildCustomPasswordModel(){
    CustomPasswordModel model = new CustomPasswordModel();
    model.setPassword("hello");
    model.setFooPassword("123456");
    return model;
}
```

## 案例2

- v0.0.11 新增功能。允许功能自定义级联脱敏注解。

### 自定义级联脱敏注解

- 自定义级联脱敏注解

可以根据自己的业务需要，在自定义的注解上使用 `@SensitiveEntry`。

使用方式保持和 `@SensitiveEntry` 一样即可。

```java
/**
 * 级联脱敏注解,如果对象中属性为另外一个对象(集合)，则可以使用这个注解指定。
 * <p>
 * 1. 如果属性为 Iterable 的子类集合，则当做列表处理，遍历其中的对象
 * 2. 如果是普通对象，则处理对象中的脱敏信息
 * 3. 如果是普通字段/MAP，则不做处理
 * @since 0.0.11
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveEntry
public @interface SensitiveEntryCustom {
}
```

### 定义测试对象

定义一个使用自定义注解的对象。
 
```java
public class CustomUserEntryObject {

    @SensitiveEntryCustom
    private User user;

    @SensitiveEntryCustom
    private List<User> userList;

    @SensitiveEntryCustom
    private User[] userArray;

    // 其他方法...
}
```

# 生成脱敏后的 JSON

## 说明

为了避免生成中间脱敏对象，v0.0.6 之后直接支持生成脱敏后的 JSON。

## 使用方法

新增工具类方法，可以直接返回脱敏后的 JSON。

生成的 JSON 是脱敏的，原对象属性值不受影响。

```java
public static String desJson(Object object)
```

## 注解的使用方式

和 `SensitiveUtil.desCopy()` 完全一致。

## 使用示例代码

所有的测试案例中，都添加了对应的 `desJson(Object)` 测试代码，可以参考。

此处只展示最基本的使用。

```java
final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}";
final String sensitiveJson = "{\"cardId\":\"123456**********34\",\"email\":\"12******.com\",\"name\":\"脱**\",\"phone\":\"1888****888\"}";

SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(systemBuiltInAt));
Assert.assertEquals(originalStr, systemBuiltInAt.toString());
```

## 注意

本次 JSON 脱敏基于 [FastJSON2](https://github.com/alibaba/fastjson2)。

FastJSON2 在序列化本身存在一定限制。当对象中有集合，集合中还是对象时，结果不尽如人意。

### 示例代码

本测试案例可见测试代码。

```java
final String originalStr = "UserCollection{userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userMap={map=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
final String commonJson = "{\"userArray\":[{\"email\":\"12345@qq.com\",\"idCard\":\"123456190001011234\",\"password\":\"1234567\",\"phone\":\"18888888888\",\"username\":\"脱敏君\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";
final String sensitiveJson = "{\"userArray\":[{\"email\":\"12******.com\",\"idCard\":\"123456**********34\",\"phone\":\"1888****888\",\"username\":\"脱**\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";

UserCollection userCollection = DataPrepareTest.buildUserCollection();

Assert.assertEquals(commonJson, JSON.toJSONString(userCollection));
Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(userCollection));
Assert.assertEquals(originalStr, userCollection.toString());
```

### 解决方案

如果有这种需求，建议使用原来的 `desCopy(Object)`。

# 脱敏引导类

为了配置的灵活性，引入了引导类。

## 配置属性

引导类 SensitiveBs 的默认配置属性如下：

```java
SensitiveBs.newInstance()
.deepCopy(FastJson2DeepCopy.getInstance())
.hash(Hashes.empty())
```

## 核心 api 简介

`SensitiveBs` 引导类的核心方法列表如下：

| 序号 | 方法 | 参数 | 结果 | 说明 |
|:---|:---|:---|:---|:---|
| 1 | desCopy() | 目标对象 | 深度拷贝脱敏对象 | 适应性更强 |
| 2 | desJson() | 目标对象 | 脱敏对象 json | 性能较好 |

## 使用示例

使用方式和工具类一致，示意如下：

```java
SensitiveBs.newInstance().desCopy(user);
```

## 配置哈希策略

直接指定哈希策略即可，比如下面以 md5 作为值的哈希策略。

```java
// 指定哈希策略
final SensitiveBs sensitiveBs = SensitiveBs.newInstance().hash(Hashes.md5());
```

效果如下：

```
final String originalStr = "User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
final String sensitiveStr = "User{username='脱**|00871641C1724BB717DD01E7E5F7D98A', idCard='123456**********34|1421E4C0F5BF57D3CC557CFC3D667C4E', password='null', email='12******.com|6EAA6A25C8D832B63429C1BEF149109C', phone='1888****888|5425DE6EC14A0722EC09A6C2E72AAE18'}";
final String expectJson = "{\"email\":\"12******.com|6EAA6A25C8D832B63429C1BEF149109C\",\"idCard\":\"123456**********34|1421E4C0F5BF57D3CC557CFC3D667C4E\",\"phone\":\"1888****888|5425DE6EC14A0722EC09A6C2E72AAE18\",\"username\":\"脱**|00871641C1724BB717DD01E7E5F7D98A\"}";
```

## 控制哈希值显示

### 说明

默认情况下，配置哈希策略后，脱敏结果会包含哈希值，格式为 `脱敏值|哈希值`。

v1.8.0 新增 `showHash` 配置项，支持控制是否显示哈希值。

### 配置方式

支持多种配置方式，优先级从高到低：

1. **编程式配置**（最高优先级）

```java
// 不显示哈希值
SensitiveBs.newInstance()
    .hash(Hashes.md5())
    .showHash(false)
    .desJson(user);

// 显示哈希值（默认）
SensitiveBs.newInstance()
    .hash(Hashes.md5())
    .showHash(true)
    .desJson(user);
```

2. **系统属性配置**

```bash
# 通过 JVM 参数配置
java -Dsensitive.showHash=false -jar app.jar
```

3. **配置文件配置**

在 `chars-scan-config.properties` 文件中配置：

```properties
# 是否显示哈希值（默认 true）
chars.scan.showHash=false
```

配置文件需要放在 classpath 根目录下。

### 优先级说明

配置优先级：编程式配置 > 系统属性 > 配置文件 > 默认值（true）

**配置影响范围**：

- **注解方式**：编程式 `showHash`、系统属性和配置文件依次生效。
- **日志插件方式**：log4j2/logback 共用 `chars.scan.showHash`；编程式
  `SensitiveBs.showHash(...)` 和 `sensitive.showHash` 系统属性只属于注解脱敏，不会覆盖日志插件配置。

### 配置调试

首次使用时会自动输出配置信息，方便排查问题：

```
========== Sensitive Configuration ==========
chars.scan.showHash    = false      (source: config file (chars-scan-config.properties))
=============================================
```

也可以手动调用：

```java
String configInfo = SensitiveConfig.dumpConfig();
System.out.println(configInfo);
```

### 效果对比

- 显示哈希值（showHash=true）：

```json
{"phone":"1888****888|5425DE6EC14A0722EC09A6C2E72AAE18"}
```

- 不显示哈希值（showHash=false）：

```json
{"phone":"1888****888"}
```

## 配置深度拷贝实现

默认的使用 FastJson2 进行对象的深度拷贝，等价于：

```java
SensitiveBs.newInstance()
                .deepCopy(FastJson2DeepCopy.getInstance())
                .desJson(user);
```

参见 [SensitiveBsTest.java](https://github.com/houbb/sensitive/blob/master/sensitive-test/src/test/java/com/github/houbb/sensitive/test/bs/SensitiveBsTest.java)

deepCopy 用于指定深度复制的具体实现，支持用户自定义。

# 深度复制（DeepCopy）

## 说明

深度复制可以保证我们日志输出对象脱敏，同时不影响正常业务代码的使用。

可以实现深度复制的方式有很多种，默认基于 [fastjson2](https://github.com/alibaba/fastjson2) 实现的。

为保证后续良性发展，v1.7.1 版本之后将深度复制接口内置到项目中，移除 deep-copy 二方依赖。

## 内置策略

目前支持 6 种基于序列化实现的深度复制，便于用户替换使用。

每一种都可以单独使用，保证依赖更加轻量。

## 自定义

为满足不同场景的需求，深度复制策略支持用户自定义。

> [自定义深度复制](https://github.com/houbb/deep-copy#%E8%87%AA%E5%AE%9A%E4%B9%89)

# 日志插件统一脱敏

对于历史项目、Map/字符串日志或无法逐个改造字段注解的场景，可以在日志输出层统一脱敏。
log4j2 和 logback 只负责接入日志事件，后续都委托给
`SensitiveScanBsContext`，因此两者共用同一份配置、策略和运行时实例。

| 内容 | log4j2 | logback |
|:---|:---|:---|
| 推荐接入点 | `SensitivePatternLayout` | `SensitiveLogbackConverter` |
| 兼容接入点 | `SensitiveRewritePolicy`（已废弃） | `SensitiveLogbackLayout` |
| 公共配置 | `chars-scan-config.properties` | `chars-scan-config.properties` |
| 扫描与替换 | `SensitiveScanBsContext` | `SensitiveScanBsContext` |

## 公共配置与扩展

log4j2 和 logback 都由 `SensitiveScanBsContext` 创建底层 `CharsScanBs`，所以应用只需在
`resources` 下维护一份 `chars-scan-config.properties`。配置按 UTF-8 读取，可以直接写
中文前缀和白名单。

数字 `1`、`2`、`3` 等只表示 chars-scan 的**内置策略**，适合做默认选择，不再作为
自定义扩展的配置协议。自定义扫描器和替换器的标识来自实现自身的 `getScanType()`，
可以是 `customer-phone` 等业务标识。

### 与 CharsScanBs 一一对应

需要完全控制底层行为时，可以直接替换 `CharsScanBs` 的组件。实现类必须是 `public`、
具有 `public` 无参构造方法，并实现表中的接口。

| 配置 | CharsScanBs 构建方法 | 实现接口 |
|:---|:---|:---|
| `chars.scan.charsCore.class` | `charsCore(...)` | `ICharsCore` |
| `chars.scan.charsScanFactory.class` | `charsScanFactory(...)` | `ICharsScanFactory` |
| `chars.scan.charsReplaceFactory.class` | `charsReplaceFactory(...)` | `ICharsReplaceFactory` |
| `chars.scan.charsReplaceHash.class` | `charsReplaceHash(...)` | `ICharsReplaceHash` |
| `chars.scan.whiteListTrie.class` | `whiteListTrie(...)` | `ITrieTree` |
| `chars.scan.prefixCharSet` | `prefixCharSet(...)` | 字符集合 |
| `chars.scan.escapePrefixCharSet` | `escapePrefixCharSet(...)` | 转义字符集合 |
| `chars.scan.scanStartIndex` | `scanStartIndex(...)` | 非负整数 |

扫描和替换各有两级扩展，优先级如下：

1. `charsScanFactory.class` / `charsReplaceFactory.class`：替换整个工厂，扩展能力与
   `CharsScanBs` 完全一致。
2. 未配置完整工厂时，组合 `builtIn.*` 内置策略和 `custom.*` 自定义实现。

`chars.scan.showHash=false` 是哈希总开关，此时强制不输出哈希；其他情况下可通过
`charsReplaceHash.class` 替换完整哈希实现。

### 默认配置

```properties
# 完整组件扩展；留空表示使用下方默认或组合配置
chars.scan.charsScanFactory.class=
chars.scan.charsReplaceFactory.class=
chars.scan.charsCore.class=
chars.scan.charsReplaceHash.class=
chars.scan.whiteListTrie.class=

# 内置策略选择
chars.scan.builtIn.scanTypes=1,2,3,4,5,9
chars.scan.builtIn.replaceTypes=1,2,3,4,5,9
chars.scan.builtIn.defaultReplaceType=12

# 单个实现扩展；多个类使用英文逗号分隔
chars.scan.custom.scans=
chars.scan.custom.replaces=
chars.scan.custom.override=true

# 其他 CharsScanBs 配置
chars.scan.charsCore=defaults
chars.scan.charsCore.threadSize=10
chars.scan.charsReplaceHash=md5
chars.scan.showHash=true
chars.scan.prefixCharSet=:：,，'"‘“=| +-*/()（）【】[]{}><
chars.scan.escapePrefixCharSet=ntr
chars.scan.whiteList=
chars.scan.scanStartIndex=0
```

`charsCore` 支持 `defaults`/`common`、`concurrency` 和 `threadLocal`；
`charsReplaceHash` 支持 `md5` 和 `none`。`whiteList` 中的多项使用英文逗号分隔。

### 按实现类扩展扫描与替换

这是大多数业务推荐的方式。扫描类实现 `ICharsScan`，替换类实现 `ICharsReplace`；
两者返回相同的 `getScanType()` 即可关联，不需要再在属性名中重复填写标识。
这些实现类同样需要是 `public` 并提供 `public` 无参构造方法；扫描器按调用创建新实例，
避免日志并发或连续扫描时共享可变缓冲区。

```properties
# 覆盖内置手机号和中文姓名策略；标识分别由实现返回 1 和 5
chars.scan.custom.scans=\
com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneScan,\
com.github.houbb.sensitive.core.support.scan.custom.UyghurNameScan

chars.scan.custom.replaces=\
com.github.houbb.sensitive.core.support.scan.custom.InternationalPhoneReplace,\
com.github.houbb.sensitive.core.support.scan.custom.UyghurNameReplace

# true：同标识时自定义优先；false：同标识时内置优先
chars.scan.custom.override=true
```

新增业务策略时，实现类可以返回 `customer-phone` 之类的非数字标识。该标识不需要加入
`builtIn.scanTypes` 或 `builtIn.replaceTypes`；框架会自动追加它。也可以只扩展扫描或
只扩展替换，缺少专用替换器时使用 `builtIn.defaultReplaceType` 的兜底规则。

### 内置策略标识

| 标识 | 说明 | 内置扫描 | 内置替换 | 可自定义 |
|:---|:---|:---:|:---:|:---:|
| 1 | 手机号 | ✅ | ✅ | ✅ |
| 2 | 身份证 | ✅ | ✅ | ✅ |
| 3 | 银行卡 | ✅ | ✅ | ✅ |
| 4 | 邮箱 | ✅ | ✅ | ✅ |
| 5 | 中国人名 | ✅ | ✅ | ✅ |
| 6 | 出生日期 | ✅ | ✅ | ✅ |
| 7 | GPS | ✅ | ✅ | ✅ |
| 8 | IPV4 | ✅ | ✅ | ✅ |
| 9 | 地址 | ✅ | ✅ | ✅ |
| 10 | 护照 | ✅ | ✅ | ✅ |
| 11 | 匹配任意不掩盖 | — | ✅ | ✅ |
| 12 | 匹配任意半掩盖 | — | ✅ | ✅ |
| 13 | 匹配任意全掩盖 | — | ✅ | ✅ |

11～13 是 chars-scan 内置的通用替换器，没有对应的内置扫描器；用户仍然可以自行提供
使用任意标识的扫描实现。

### 旧配置迁移

旧属性仍可读取，建议逐步迁移到语义更明确的新名称。

| 旧配置 | 新配置 |
|:---|:---|
| `chars.scan.scanList` | `chars.scan.builtIn.scanTypes` |
| `chars.scan.replaceList` | `chars.scan.builtIn.replaceTypes` |
| `chars.scan.defaultReplace` | `chars.scan.builtIn.defaultReplaceType` |
| `chars.scan.core` | `chars.scan.charsCore` |
| `chars.scan.replaceHash` | `chars.scan.charsReplaceHash` |
| `chars.scan.prefix` | `chars.scan.prefixCharSet` |
| `chars.scan.custom.scan.{id}.class` | 将类名加入 `chars.scan.custom.scans` |
| `chars.scan.custom.replace.{id}.class` | 将类名加入 `chars.scan.custom.replaces` |

### 运行时重载

`SensitiveScanBsContext` 默认按单例延迟初始化。如果运行时变更了配置，可主动重载：

```java
// 重新加载配置
SensitiveScanBsContext.reload();

// 获取 CharsScanBs 实例（高级用法）
CharsScanBs charsScanBs = SensitiveScanBsContext.getCharsScanBs();
```

配置重载会重新创建底层 `CharsScanBs`，后续日志同时使用新配置。调用方不需要分别刷新
log4j2 和 logback。

## log4j2 接入

### Maven 依赖

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-log4j2</artifactId>
    <version>1.9.0</version>
</dependency>
```

项目还需要正常引入 `log4j-api` 和 `log4j-core`。使用 slf4j API、底层绑定到 log4j2
的项目同样适用。

### log4j2.xml

推荐使用 `SensitivePatternLayout`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" packages="com.github.houbb.sensitive.log4j2.layout">
    <Properties>
        <Property name="PATTERN">%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <SensitivePatternLayout pattern="${PATTERN}" charset="UTF-8"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="INFO">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

`SensitiveRewritePolicy` 仅用于兼容已有 Rewrite 配置，已经废弃，新项目不要再使用。

## logback 接入

### Maven 依赖

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-logback</artifactId>
    <version>1.9.0</version>
</dependency>
```

项目还需要正常引入 `logback-classic`。

### logback.xml

推荐使用 `SensitiveLogbackConverter`：

```xml
<configuration>
    <conversionRule
        conversionWord="sensitive"
        converterClass="com.github.houbb.sensitive.logback.converter.SensitiveLogbackConverter"/>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %sensitive%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

`SensitiveLogbackLayout` 是可选的 Layout 接入方式。Converter 和 Layout 二选一，
不要同时挂到 Root Logger，否则同一条日志会输出两次。

## 统一脱敏效果

两个框架使用相同输入和公共配置时，脱敏结果一致。例如：

```text
输入：mobile:13088887777, email:mahuateng@qq.com
输出：mobile:130****7777|9FC4D36D63D2B6DC5AE1297544FBC5A2, email:mahu*****@qq.com|897915594C94D981BA86C9E83ADD449C
```

# 性能耗时

## 注解

100W 次耗时统计

| 方法      | 耗时(ms)  | 说明                         |
|:--------|:--------|:---------------------------|
| 原始工具类方法 | 122     | 性能最好，但是最麻烦。拓展性最差           |
| JSON.toJSONString(user) | 304     | 性能较好，拓展性不错。缺点是强依赖 fastjson2 |
| SensitiveUtil.desJson(user) | 1541    | 性能较差，拓展性最好，比较灵活            |

# ROAD-MAP

- [ ] 配置的抽象化

- [x] 添加统一的工具类方法，便于开发单独使用

喜欢重载 toString()，或特殊的场景

- [x] 考虑添加针对 MAP 的脱敏支持

- [x] 针对身份证的默认脱敏策略

- [x] log4j2 等日志组件的脱敏策略

提升可拓展性

- [x] log4j2 layout 对应的脱敏策略

- [x] 优化代码实现，直接继承自 patternLayout

- [x] log4j2 脱敏配置添加指定配置文件，而不是放在 pattern 中

- [ ] 日志插件脱敏的 benchmark 性能报告
