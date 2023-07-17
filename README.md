# 项目介绍

日志脱敏是常见的安全需求。普通的基于工具类方法的方式，对代码的入侵性太强，编写起来又特别麻烦。

本项目提供基于注解的方式，并且内置了常见的脱敏方式，便于开发。

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

[6. 支持基于 FastJSON 直接生成脱敏后的 json](https://github.com/houbb/sensitive#%E7%94%9F%E6%88%90%E8%84%B1%E6%95%8F%E5%90%8E%E7%9A%84-json)

[7. 支持自定义哈希策略，更加方便定位日志问题](https://github.com/houbb/sensitive#%E9%85%8D%E7%BD%AE%E5%93%88%E5%B8%8C%E7%AD%96%E7%95%A5)

![8. 支持基于 log4j2 的统一脱敏策略](https://github.com/houbb/sensitive#log4j2-%E6%8F%92%E4%BB%B6%E7%BB%9F%E4%B8%80%E8%84%B1%E6%95%8F)

## 变更日志

> [变更日志](https://github.com/houbb/sensitive/blob/master/CHANGE_LOG.md)

## 拓展阅读

[金融用户敏感数据如何优雅地实现脱敏？](https://mp.weixin.qq.com/s/ljChFiNLzV6GLaUDjehA0Q)

[日志脱敏之后，无法根据信息快速定位怎么办？](https://mp.weixin.qq.com/s/tZqOH_8QTKrD1oaclNoewg)

# 快速开始

## 环境准备

JDK 1.8+

Maven 3.x

## maven 导入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-core</artifactId>
    <version>1.3.0</version>
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

本次 JSON 脱敏基于 [FastJSON](https://github.com/alibaba/fastjson)。

FastJSON 在序列化本身存在一定限制。当对象中有集合，集合中还是对象时，结果不尽如人意。

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
.deepCopy(FastJsonDeepCopy.getInstance())
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

## 配置深度拷贝实现

默认的使用 FastJson 进行对象的深度拷贝，等价于：

```java
SensitiveBs.newInstance()
                .deepCopy(FastJsonDeepCopy.getInstance())
                .desJson(user);
```

参见 [SensitiveBsTest.java](https://github.com/houbb/sensitive/blob/master/sensitive-test/src/test/java/com/github/houbb/sensitive/test/bs/SensitiveBsTest.java)

deepCopy 用于指定深度复制的具体实现，支持用户自定义。

# 深度复制（DeepCopy）

## 说明

深度复制可以保证我们日志输出对象脱敏，同时不影响正常业务代码的使用。

可以实现深度复制的方式有很多种，默认基于 [fastjson](https://github.com/alibaba/fastjson) 实现的。

为保证后续良性发展，v0.0.13 版本之后将深度复制接口抽离为单独的项目：

> [deep-copy](https://github.com/houbb/deep-copy)

## 内置策略

目前支持 6 种基于序列化实现的深度复制，便于用户替换使用。

每一种都可以单独使用，保证依赖更加轻量。

## 自定义

为满足不同场景的需求，深度复制策略支持用户自定义。

> [自定义深度复制](https://github.com/houbb/deep-copy#%E8%87%AA%E5%AE%9A%E4%B9%89)

# log4j2 插件统一脱敏

## 说明

上面的方法非常适用于新的项目，按照响应的规范进行推广。

但是很多金融公司都有很多历史遗留项目，或者使用不规范，比如使用 map 等，导致上面的方法在脱敏技改时需要耗费大量的时间，而且回溯成本很高。

有没有什么方法，可以直接在日志层统一处理呢？

## log4j2 Rewrite

我们可以基于 log4j2 RewritePolicy 统一使用脱敏策略。

本项目自 V1.2.0 添加对应支持，后续将提升对应的可拓展性。

说明：如果使用 slf4j 接口，实现为 log4j2 时也是支持的。

## 使用入门

### maven 引入

引入核心脱敏包。

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-log4j2</artifactId>
    <version>1.3.0</version>
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

### log4j2.xml 配置

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

### 测试

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

# 性能耗时

## 注解

100W 次耗时统计

| 方法      | 耗时(ms)  | 说明                         |
|:--------|:--------|:---------------------------|
| 原始工具类方法 | 122     | 性能最好，但是最麻烦。拓展性最差           |
| JSON.toJSONString(user) | 304     | 性能较好，拓展性不错。缺点是强依赖 fastjson |
| SensitiveUtil.desJson(user) | 1541    | 性能较差，拓展性最好，比较灵活            |

# ROAD-MAP

- [ ] 添加统一的工具类方法，便于开发单独使用

喜欢重载 toString()，或特殊的场景

- [ ] 考虑添加针对 MAP 的脱敏支持

- [x] 针对身份证的默认脱敏策略

- [x] log4j2 等日志组件的脱敏策略

提升可拓展性

- [ ] log4j2 layout 对应的脱敏策略

- [ ] 优化代码实现，直接继承自 patternLayout
