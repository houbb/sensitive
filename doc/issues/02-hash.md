# 日志脱敏之殇

小明同学在一家金融公司上班，为了满足安全监管要求，最近天天忙着做日志脱敏。

无意间看到了一篇文章[金融用户敏感数据如何优雅地实现脱敏？](https://mp.weixin.qq.com/s/ljChFiNLzV6GLaUDjehA0Q) 感觉写的不错，用起来也很方便。

不过日志脱敏之后，新的问题就诞生了：日志脱敏之后，很多问题无法定位。

比如身份证号日志中看到的是 `3****************8`，业务方给一个身份证号也没法查日志。这可怎么办？

![唯一](https://img-blog.csdnimg.cn/7abc105864f14fb494ec4564b622ce10.jpeg#pic_center)

# 安全与数据唯一性

类似于数据库中敏感信息的存储，一般都会有一个哈希值，用来定位数据信息，同时保障安全。

那么日志中是否也可以使用类似的方式呢？

说干就干，小明在开源项目 [sensitive](https://github.com/houbb/sensitive) 基础上，添加了对应的哈希实现。

# 使用入门

## 开源地址

> [https://github.com/houbb/sensitive](https://github.com/houbb/sensitive)

## 使用方式

1）maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

2）引导类指定

```java
SensitiveBs.newInstance()
.hash(Hashes.md5())
```

将哈希策略指定为 md5

3）功能测试

```java
final SensitiveBs sensitiveBs = SensitiveBs.newInstance()
                .hash(Hashes.md5());

User sensitiveUser = sensitiveBs.desCopy(user);
String sensitiveJson = sensitiveBs.desJson(user);

Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
Assert.assertEquals(originalStr, user.toString());
Assert.assertEquals(expectJson, sensitiveJson);
```

可以把如下的对象

```
User{username='脱敏君', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}
```

直接脱敏为：

```
User{username='脱**|00871641C1724BB717DD01E7E5F7D98A', idCard='123456**********34|1421E4C0F5BF57D3CC557CFC3D667C4E', password='null', email='12******.com|6EAA6A25C8D832B63429C1BEF149109C', phone='1888****888|5425DE6EC14A0722EC09A6C2E72AAE18'}
```

这样就可以通过明文，获取对应的哈希值，然后搜索日志了。

# 新的问题

不过小明还是觉得不是很满意，因为有很多系统是已经存在的。

如果全部用注解的方式实现，就会很麻烦，也很难推动。

应该怎么实现呢？

小伙伴们有什么好的思路？欢迎评论区留言
