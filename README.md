# 项目介绍

日志脱敏是常见的安全需求。普通的基于工具类方法的方式，对代码的入侵性太强。编写起来又特别麻烦。

本项目提供基于注解的方式，并且内置了常见的脱敏方式，便于开发。

用户也可以基于自己的实际需要，自定义注解。

[![Build Status](https://travis-ci.com/houbb/sensitive.svg?branch=master)](https://travis-ci.com/houbb/sensitive)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/sensitive/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/sensitive)

> [变更日志](CHANGE_LOG.md)

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

1. 基于注解的日志脱敏。

2. 可以自定义策略实现，策略生效条件。

3. 常见的脱敏内置方案。

4. java 深拷贝，且原始对象不用实现任何接口。

5. 支持用户自定义注解。

6. 支持基于 FastJSON 直接生成脱敏后的 json

## v0.0.10 变更

1. 更新 fast-json 为最新版本依赖（安全问题）

2. 更新 heaven 版本依赖为最新

# 快速开始

## 环境准备

JDK 7+

Maven 3.x

## maven 导入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-core</artifactId>
    <version>0.0.10</version>
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

# 自定义注解

v0.0.4 新增功能。允许功能自定义条件注解和策略注解。

## 案例

### 自定义注解

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
@Test
public void sensitiveJsonTest() {
    final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='脱敏君', email='12345@qq.com', cardId='123456190001011234'}";
    final String sensitiveJson = "{\"cardId\":\"123456**********34\",\"email\":\"123**@qq.com\",\"name\":\"脱*君\",\"phone\":\"188****8888\"}";

    SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
    Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(systemBuiltInAt));
    Assert.assertEquals(originalStr, systemBuiltInAt.toString());
}
```

## 注意

本次 JSON 脱敏基于 [FastJSON](https://github.com/alibaba/fastjson)。

FastJSON 在序列化本身存在一定限制。当对象中有集合，集合中还是对象时，结果不尽如人意。

### 示例代码

本测试案例可见测试代码。

```java
@Test
public void sensitiveUserCollectionJsonTest() {
    final String originalStr = "UserCollection{userList=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userMap={map=User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
    final String commonJson = "{\"userArray\":[{\"email\":\"12345@qq.com\",\"idCard\":\"123456190001011234\",\"password\":\"1234567\",\"phone\":\"18888888888\",\"username\":\"脱敏君\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";
    final String sensitiveJson = "{\"userArray\":[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"脱*君\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";

    UserCollection userCollection = DataPrepareTest.buildUserCollection();

    Assert.assertEquals(commonJson, JSON.toJSONString(userCollection));
    Assert.assertEquals(sensitiveJson, SensitiveUtil.desJson(userCollection));
    Assert.assertEquals(originalStr, userCollection.toString());
}
```

### 解决方案

如果有这种需求，建议使用原来的 `desCopy(Object)`。

# 针对集合的处理

`v0.0.7` 支持的新特性，便于用户处理集合相关的脱敏。

如果列表为空，则直接返回空列表。

更多测试代码参见 [SensitiveUtilCollectionTest.java](https://github.com/houbb/sensitive/blob/release_0.0.7/sensitive-test/src/test/java/com/github/houbb/sensitive/test/core/sensitive/collection/SensitiveUtilCollectionTest.java)

## 集合脱敏-对象拷贝

- List<T> desCopyCollection(Collection<T> collection)

返回脱敏后的对象集合

```java
List<User> userList = DataPrepareTest.buildUserList();
List<User> sensitiveList = SensitiveUtil.desCopyCollection(userList);
Assert.assertEquals("[User{username='脱*君', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, User{username='集**试', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}]", sensitiveList.toString());
```

## 集合脱敏-json

- List<String> desJsonCollection(Collection<?> collection)

返回脱敏后的 json 列表

```java
List<User> userList = DataPrepareTest.buildUserList();

List<String> sensitiveJsonList = SensitiveUtil.desJsonCollection(userList);
Assert.assertEquals("[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"脱*君\"}, {\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"集**试\"}]", sensitiveJsonList.toString());
```

# 脱敏引导类

为了配置的灵活性，引入了引导类。

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

## 配置深度拷贝实现

默认的使用 FastJson 进行对象的深度拷贝，等价于：

```java
SensitiveBs.newInstance()
                .deepCopy(DeepCopies.json())
                .desJson(user);
```

后期准备引入其他深度拷贝替代基于 json 的深度拷贝，提升性能。

# 需求 & BUGS

> [issues](https://github.com/houbb/sensitive/issues)

# 欢迎加入开发

如果你对本项目有兴趣，并且对代码有一定追求，可以申请加入本项目开发。

如果你善于写文档，或者愿意补全测试案例，也非常欢迎加入。