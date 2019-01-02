# 文档说明

作者：侯宾宾

时间：2018-04-24 10:11:43

说明：如何进行项目的持续集成+测试覆盖率

# Travis-CI

[https://www.travis-ci.org](https://www.travis-ci.org) 直接添加此项目

# Coveralls

- 添加项目

[https://coveralls.io/repos/new](https://coveralls.io/repos/new) 直接添加项目

- 生成密匙

```
travis encrypt COVERALLS_TOKEN=${your_repo_token}
```

- 添加到文件 

```
travis encrypt COVERALLS_TOKEN=${your_repo_token} --add
```

