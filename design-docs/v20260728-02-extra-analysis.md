# 脱敏策略扩展性分析

## 问题场景

### 问题1：新疆名字
- **需求**：支持超过2-3个字的中文姓名（新疆名字可能很长）
- **现状**：StrategyChineseName 固定处理 2-3 个字

### 问题2：00开头手机号
- **需求**：支持识别和脱敏 00 开头的手机号
- **现状**：chars-scan 的手机号识别只识别 1 开头

## 当前架构分析

### 1. 注解方式（sensitive-core）

#### 架构设计
```
IStrategy (接口)
    ↓
AbstractStrategy (抽象类，支持哈希)
    ↓
AbstractStringStrategy (字符串策略基类)
    ↓
具体策略：StrategyPhone, StrategyChineseName 等
```

#### 扩展能力
✅ **完全支持自定义策略**

**实现方式**：
```java
// 1. 实现自定义策略
public class CustomChineseNameStrategy implements IStrategy {
    @Override
    public Object des(Object original, IContext context) {
        // 自定义逻辑：支持任意长度的中文姓名
        String strValue = (String) original;
        int len = strValue.length();
        
        if (len <= 1) {
            return strValue;
        } else if (len <= 3) {
            // 标准：张三 -> 张*
            return strValue.charAt(0) + "*";
        } else {
            // 长名字：阿里木江·买买提 -> 阿里木江*
            return strValue.substring(0, len - 1) + "*";
        }
    }
}

// 2. 使用自定义策略
@Sensitive(strategy = CustomChineseNameStrategy.class)
private String name;
```

**优点**：
- 完全控制脱敏逻辑
- 支持任意复杂的场景
- 代码侵入性小

**缺点**：
- 只能用于注解方式
- 日志插件无法使用

### 2. 日志插件方式（log4j2/logback）

#### 架构设计
```
日志输出
    ↓
CharsScanPropertyHelper.scanAndReplace(text)
    ↓
chars-scan 库（正则匹配 + 内置策略）
    ↓
脱敏结果（含哈希值）
```

#### 配置方式
```properties
# chars-scan-config.properties
chars.scan.scanList=1,2,3,4,9      # 扫描策略：1=手机号，2=身份证，5=中国人名
chars.scan.replaceList=1,2,3,4,9   # 替换策略
chars.scan.replaceHash=md5         # 哈希策略
```

#### 扩展能力
❌ **不支持自定义识别规则**

**限制**：
- chars-scan 是外部依赖库
- 手机号识别规则（策略1）硬编码为 `^1[3-9]\d{9}$`
- 中文识别规则（策略5）固定为 2-3 个字
- 无法修改正则表达式或识别逻辑

**只能通过配置调整**：
- `chars.scan.scanList`：选择使用哪些内置策略
- `chars.scan.replaceList`：选择替换方式
- `chars.scan.whiteList`：白名单（跳过特定文本）

## 扩展性评估

### 注解方式：★★★★★（优秀）
- ✅ 完全支持自定义策略
- ✅ API 设计清晰（IStrategy 接口）
- ✅ 提供基础实现类（AbstractStringStrategy）
- ✅ 扩展成本低（实现一个接口即可）

### 日志插件方式：★★☆☆☆（受限）
- ❌ 无法自定义识别规则
- ❌ 依赖 chars-scan 库的更新
- ✅ 可以通过白名单跳过某些文本
- ⚠️ 只能使用内置的10种策略

## 解决方案

### 方案1：注解方式（推荐用于新项目）

**适用场景**：可以使用注解标注字段的项目

**实现成本**：低

**代码示例**：

#### 新疆名字策略
```java
public class UyghurNameStrategy implements IStrategy {
    @Override
    public Object des(Object original, IContext context) {
        if (original == null) return null;
        
        String name = original.toString();
        int len = name.length();
        
        if (len <= 1) {
            return name;
        } else if (len <= 3) {
            // 标准：张三 -> 张*
            return name.charAt(0) + "*";
        } else {
            // 长名字：保留前3个字，其余用*替代
            // 阿里木江·买买提 -> 阿里木*******
            StringBuilder sb = new StringBuilder(name.substring(0, 3));
            for (int i = 3; i < len; i++) {
                sb.append("*");
            }
            return sb.toString();
        }
    }
}
```

#### 00开头手机号策略
```java
public class InternationalPhoneStrategy implements IStrategy {
    @Override
    public Object des(Object original, IContext context) {
        if (original == null) return null;
        
        String phone = original.toString();
        
        // 支持 00 开头的国际号码
        if (phone.startsWith("00")) {
            // 008613912345678 -> 0086****5678
            if (phone.length() >= 8) {
                return phone.substring(0, 4) + "****" + phone.substring(phone.length() - 4);
            }
        } else if (phone.startsWith("1") && phone.length() == 11) {
            // 标准国内手机号：13912345678 -> 139****5678
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        
        return phone;
    }
}
```

#### 使用方式
```java
@Sensitive(strategy = UyghurNameStrategy.class)
private String name;

@Sensitive(strategy = InternationalPhoneStrategy.class)
private String phone;
```

### 方案2：日志插件方式（暂不支持自定义）

**当前限制**：
- chars-scan 的手机号和中文识别规则无法自定义
- 只能等待 chars-scan 库更新或提 issue

**临时方案**：
- 使用 `chars.scan.whiteList` 白名单跳过特定文本
- 配合后处理（在日志输出后手动处理）

### 方案3：混合方式（推荐用于历史项目）

**策略**：
1. **日志输出**：使用 chars-scan 内置策略（覆盖大部分场景）
2. **特殊字段**：使用注解 + 自定义策略（处理特殊需求）
3. **手动处理**：对于日志插件无法处理的场景，在日志输出前后手动脱敏

**实施步骤**：

#### Step 1: 标准字段使用日志插件
```properties
# chars-scan-config.properties
chars.scan.scanList=1,2,3,4,9
chars.scan.replaceList=1,2,3,4,9
```

#### Step 2: 特殊字段使用注解
```java
public class User {
    // 标准字段：使用日志插件自动脱敏
    private String idCard;
    private String bankCard;
    private String email;
    
    // 特殊字段：使用自定义策略
    @Sensitive(strategy = UyghurNameStrategy.class)
    private String name;
    
    @Sensitive(strategy = InternationalPhoneStrategy.class)
    private String phone;
}
```

#### Step 3: 日志输出时手动处理
```java
// 对于特殊字段，在日志输出前手动脱敏
User user = getUser();
User sensitiveUser = SensitiveBs.newInstance()
    .hash(Hashes.md5())
    .desCopy(user);
logger.info("User: {}", sensitiveUser);
```

## 架构改进建议

### 短期方案（最小修改）

**目标**：支持自定义策略，不修改 chars-scan

**实现**：
1. 提供 `CustomStrategyUtil` 工具类
2. 提供常用自定义策略示例（新疆名字、国际手机号等）
3. 完善文档和示例

**修改范围**：
- 新增 `sensitive-core/src/main/java/.../strategy/custom/` 包
- 新增常用自定义策略实现类
- 更新 README.md 文档

### 中期方案（扩展性改进）

**目标**：提升日志插件的扩展性

**方案A：包装 chars-scan**
```java
public class EnhancedSensitivePatternLayout extends SensitivePatternLayout {
    @Override
    public String toSerializable(LogEvent event) {
        String text = super.toSerializable(event);
        
        // 后处理：处理 chars-scan 无法识别的特殊场景
        return postProcess(text);
    }
    
    private String postProcess(String text) {
        // 自定义后处理逻辑
        // 例如：识别 00 开头的手机号
        return text.replaceAll("00(\\d{2})\\d+(\\d{4})", "00$1****$2");
    }
}
```

**方案B：提供配置化策略**
```properties
# chars-scan-config.properties
# 新增自定义正则策略
chars.scan.custom.1.pattern=00\\d{13}
chars.scan.custom.1.replace=00****{LAST4}
chars.scan.custom.2.pattern=[\\u4e00-\\u9fa5]{4,}
chars.scan.custom.2.replace={FIRST3}****
```

### 长期方案（架构升级）

**目标**：实现类似 log4j2 plugins 的插件化架构

**设计思路**：
1. 定义策略插件接口 `IStrategyPlugin`
2. 支持动态注册策略
3. 支持配置文件定义策略
4. 日志插件和注解方式共享策略

## 总结

### 当前支持情况

| 场景 | 注解方式 | 日志插件方式 | 备注 |
|:---|:---:|:---:|:---|
| 标准中文姓名（2-3字） | ✅ | ✅ | 内置支持 |
| 长中文姓名（4+字） | ✅ | ❌ | 需自定义策略 |
| 标准手机号（1开头） | ✅ | ✅ | 内置支持 |
| 国际手机号（00开头） | ✅ | ❌ | 需自定义策略 |
| 自定义脱敏逻辑 | ✅ | ❌ | 实现IStrategy |

### 推荐方案

**新项目**：注解方式 + 自定义策略
- 灵活性高
- 代码清晰
- 易于维护

**历史项目**：日志插件 + 注解补充
- 日志插件处理大部分场景
- 特殊字段使用注解
- 降低改造成本

**最佳实践**：
1. 优先使用内置策略
2. 特殊需求使用自定义策略
3. 完善文档和示例
4. 提供 `CustomStrategyUtil` 工具类