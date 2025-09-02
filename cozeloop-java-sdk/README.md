# CozeLoop Java SDK

这是CozeLoop平台的Java SDK实现，完全对应Go SDK的架构和功能。

## 项目结构

```
cozeloop-java-sdk/
├── pom.xml                          # Maven配置
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/coze/cozeloop/
│   │           ├── CozeLoop.java           # 主入口类
│   │           └── trace/                  # Trace相关类
│   │               ├── Span.java           # Span接口
│   │               ├── SpanContext.java    # Span上下文
│   │               ├── SpanOptions.java    # Span选项
│   │               ├── TraceProvider.java  # Trace提供者接口
│   │               └── internal/           # 内部实现
│   │                   ├── DefaultSpan.java
│   │                   ├── DefaultTraceProvider.java
│   │                   ├── SpanProcessor.java
│   │                   ├── QueueManager.java
│   │                   └── Exporter.java
│   └── test/
│       └── java/
│           └── com/coze/cozeloop/
│               └── trace/                  # 测试类
└── README.md
```

## 核心特性

### 1. 完全对应Go SDK架构
- **TraceProvider**: 对应Go的TraceProvider
- **Span**: 对应Go的Span接口
- **SpanContext**: 对应Go的SpanContext结构体
- **队列管理**: 对应Go的QueueManager
- **数据导出**: 对应Go的Exporter

### 2. Java 8特性支持
- Lambda表达式
- Stream API
- Optional
- CompletableFuture
- 函数式接口

### 3. 跨服务追踪
- 支持W3C Trace Context标准
- 自动继承TraceID和Baggage
- HTTP Header传递

## 使用方法

### 1. 基本使用
```java
// 创建Span
Span span = CozeLoop.startSpan("operation_name", "span_type");

// 设置标签
span.setTags(Map.of("key", "value"));

// 设置输入输出
span.setInput("input_data");
span.setOutput("output_data");

// 完成Span
span.finish();
```

### 2. 跨服务追踪
```java
// 服务A：创建Span并转换为Header
Span span = CozeLoop.startSpan("service_a", "main");
Map<String, String> headers = span.toHeader();

// 服务B：从Header恢复Span上下文
SpanContext context = CozeLoop.getSpanFromHeader(headers);
Span childSpan = CozeLoop.startSpan("service_b", "main", 
    new SpanOptions().setParentSpanID(context.getSpanID()));
```

## 依赖要求

- Java 8+
- Maven 3.6+
- Apache HttpClient 4.5+
- Jackson 2.13+
- SLF4J 1.7+

## 开发状态

🚧 **开发中** - 正在实现核心功能

### 已完成
- [x] 项目结构创建
- [x] 核心接口定义
- [x] 基础类设计
- [x] 实体类创建
- [x] 常量类创建
- [x] 工具类创建
- [x] 实现类骨架
- [x] 测试类骨架

### 进行中
- [ ] 核心逻辑实现
- [ ] 队列管理实现
- [ ] 数据导出实现

### 待完成
- [ ] 数据导出实现
- [ ] 测试用例
- [ ] 示例代码
- [ ] 文档完善

## 贡献

欢迎提交Issue和Pull Request！

## 许可证

MIT License
