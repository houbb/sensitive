# pom.xml 

改为最新的 profiles

# 指定 name

最新的 maven 规范，需要指定 maven name


# 指定 jdk 版本

## test

```
$env:JAVA_HOME="D:\tool\jdk\jdk-1.8"; mvn clean test
```

or

```
$env:JAVA_HOME="D:\tools\jdk\jdk1.8.0_192"; mvn clean test
```

## 发布

```
$env:JAVA_HOME="D:\tool\jdk\jdk-1.8"; mvn clean deploy -P release
```

or

```
$env:JAVA_HOME="D:\tools\jdk\jdk1.8.0_192"; mvn clean deploy -P release
```