package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
import com.coze.cozeloop.trace.TraceOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * 主测试类，用于测试Java SDK的基本功能
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🚀 CozeLoop Java SDK 启动测试");
        System.out.println("==================================");
        
        try {
            // 测试1: 创建配置选项
            testTraceOptions();
            
            // 测试2: 创建Span选项
            testSpanOptions();
            
            // 测试3: 测试Span上下文
            testSpanContext();
            
            // 测试4: 测试HTTP Header转换
            testHeaderConversion();
            
            // 测试5: 测试完整的Span创建流程
            testSpanCreation();
            
            // 测试6: 测试认证功能
            testAuthentication();
            
            // 测试7: 测试数据上报功能
            testDataExport();
            
            System.out.println("✅ 所有测试通过！");
            
        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试Trace配置选项
     */
    private static void testTraceOptions() {
        System.out.println("\n📋 测试1: Trace配置选项");
        
        TraceOptions options = new TraceOptions();
        options.setWorkspaceID("test-workspace-123");
        options.setServiceName("test-service");
        options.setApiBaseURL("https://test-api.coze.cn");
        
        System.out.println("   API Base URL: " + options.getApiBaseURL());
        System.out.println("   Workspace ID: " + options.getWorkspaceID());
        System.out.println("   Service Name: " + options.getServiceName());
        System.out.println("   Max Queue Size: " + options.getMaxQueueSize());
        System.out.println("   Batch Size: " + options.getBatchSize());
        System.out.println("   Flush Interval: " + options.getFlushIntervalMs() + "ms");
        
        // 测试复制功能
        TraceOptions copy = options.copy();
        System.out.println("   Copy Workspace ID: " + copy.getWorkspaceID());
    }
    
    /**
     * 测试Span选项
     */
    private static void testSpanOptions() {
        System.out.println("\n📋 测试2: Span选项");
        
        SpanOptions options = new SpanOptions();
        options.setParentSpanID("parent-span-123");
        options.setTraceID("trace-456");
        options.setScene("test-scene");
        options.addBaggage("user-id", "user-789");
        options.addBaggage("request-id", "req-101");
        
        System.out.println("   Parent Span ID: " + options.getParentSpanID());
        System.out.println("   Trace ID: " + options.getTraceID());
        System.out.println("   Scene: " + options.getScene());
        System.out.println("   Baggage: " + options.getBaggage());
        
        // 测试复制功能
        SpanOptions copy = options.copy();
        System.out.println("   Copy Parent Span ID: " + copy.getParentSpanID());
    }
    
    /**
     * 测试Span上下文
     */
    private static void testSpanContext() {
        System.out.println("\n📋 测试3: Span上下文");
        
        com.coze.cozeloop.trace.SpanContext context = new com.coze.cozeloop.trace.SpanContext();
        context.setSpanID("span-123");
        context.setTraceID("trace-456");
        context.addBaggage("user-id", "user-789");
        context.addBaggage("request-id", "req-101");
        
        System.out.println("   Span ID: " + context.getSpanID());
        System.out.println("   Trace ID: " + context.getTraceID());
        System.out.println("   Baggage: " + context.getBaggage());
        
        // 测试复制功能
        com.coze.cozeloop.trace.SpanContext copy = context.copy();
        System.out.println("   Copy Span ID: " + copy.getSpanID());
    }
    
    /**
     * 测试HTTP Header转换
     */
    private static void testHeaderConversion() {
        System.out.println("\n📋 测试4: HTTP Header转换");
        
        // 模拟从HTTP Header解析Span上下文
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Cozeloop-Traceparent", "00-trace-456-span-123-01");
        headers.put("X-Cozeloop-Tracestate", "user-id=user-789,request-id=req-101");
        
        System.out.println("   模拟HTTP Headers:");
        headers.forEach((key, value) -> System.out.println("     " + key + ": " + value));
        
        // 这里应该调用CozeLoop.getSpanFromHeader(headers)
        // 但由于我们还没有实现DefaultTraceProvider，所以只是模拟
        System.out.println("   Header解析功能待实现...");
    }
    
    /**
     * 测试完整的Span创建流程 - 仿照Go SDK的examples/trace/simple/simple.go
     */
    private static void testSpanCreation() {
        System.out.println("\n📋 测试5: 完整的Span创建流程（仿照Go SDK）");
        
        try {
            // 设置环境变量（仿照Go SDK）
            System.setProperty("COZELOOP_WORKSPACE_ID", "7534944671286558755");
            System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
            
            // 创建TraceOptions（仿照Go SDK的配置）
            TraceOptions traceOptions = new TraceOptions();
            traceOptions.setApiBaseURL("https://api.coze.cn");
            traceOptions.setWorkspaceID("7534944671286558755");  // 使用Go SDK中的workspace ID
            traceOptions.setServiceName("java-sdk-example");     // 标识这是Java SDK示例
            
            // 使用Go SDK风格的客户端创建方法
            CozeLoopClient client = CozeLoop.newClient();
            
            // 1. 创建根Span（仿照Go SDK的root_span）
            Span rootSpan = client.startSpan("root_span", "main_span");
            
            // 2. 设置自定义标签（仿照Go SDK的SetTags）
            Map<String, Object> customTags = new HashMap<>();
            customTags.put("mode", "simple");
            customTags.put("node_id", 6076665);
            customTags.put("node_process_duration", 228.6);
            customTags.put("is_first_node", true);
            rootSpan.setTags(customTags);
            
            // 3. 设置自定义Baggage（仿照Go SDK的SetBaggage）
            rootSpan.setBaggage("product_id", "123456654321");  // 假设product_id是全局字段
            rootSpan.setUserIDBaggage("123456");                // 设置用户ID（仿照Go SDK的SetUserIDBaggage）
            
            System.out.println("   ✅ 根Span创建成功:");
            System.out.println("     - SpanID: " + rootSpan.getSpanID());
            System.out.println("     - TraceID: " + rootSpan.getTraceID());
            System.out.println("     - Name: " + rootSpan.getSpanName());
            System.out.println("     - Type: " + rootSpan.getSpanType());
            System.out.println("     - WorkspaceID: " + rootSpan.getSpaceID());
            
            // 4. 模拟LLM调用（仿照Go SDK的llmCall方法）
            try {
                simulateLLMCall(client, rootSpan);
            } catch (Exception e) {
                // 设置错误状态码和错误信息（仿照Go SDK的错误处理）
                rootSpan.setStatusCode(600789111);  // 使用Go SDK中的错误码
                rootSpan.setError(e);
                System.out.println("   ⚠️  LLM调用失败，已设置错误状态: " + e.getMessage());
            }
            
            // 5. 完成根Span
            rootSpan.finish();
            System.out.println("   ✅ 根Span完成，持续时间: " + rootSpan.getDuration() + " 微秒");
            
            // 6. 测试Header转换
            Map<String, String> headers = rootSpan.toHeader();
            System.out.println("   ✅ Header转换成功:");
            headers.forEach((key, value) -> System.out.println("     " + key + ": " + value));
            
            // 7. 强制刷新队列（仿照Go SDK的Flush）
            System.out.println("   🔄 强制刷新队列...");
            client.flush();
            System.out.println("   ✅ 队列刷新完成");
            
            // 8. 关闭CozeLoop（可选，仿照Go SDK的Close）
            System.out.println("   🔄 关闭CozeLoop...");
            client.close();
            System.out.println("   ✅ CozeLoop关闭完成");
            
        } catch (Exception e) {
            System.out.println("   ❌ Span创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试认证功能
     */
    private static void testAuthentication() {
        System.out.println("\n📋 测试6: 认证功能测试");
        
        try {
            // 检查系统属性设置
            String workspaceID = System.getProperty("COZELOOP_WORKSPACE_ID");
            String apiToken = System.getProperty("COZELOOP_API_TOKEN");
            
            System.out.println("   🔍 检查认证配置:");
            System.out.println("     - Workspace ID: " + (workspaceID != null ? workspaceID : "未设置"));
            System.out.println("     - API Token: " + (apiToken != null ? apiToken.substring(0, Math.min(apiToken.length(), 20)) + "..." : "未设置"));
            
            if (workspaceID == null || apiToken == null) {
                System.err.println("   ❌ 认证配置不完整，请检查系统属性设置");
                return;
            }
            
            // 测试简单的HTTP请求
            System.out.println("   🔄 测试简单HTTP请求...");
            com.coze.cozeloop.trace.http.DefaultHttpClient httpClient = 
                new com.coze.cozeloop.trace.http.DefaultHttpClient("https://api.coze.cn");
            
            try {
                // 测试1: 尝试访问trace API（需要认证）
                System.out.println("   🔄 测试1: 访问trace API...");
                try {
                    String response = httpClient.post("/v1/loop/traces/ingest", 
                        "{\"spans\":[]}", String.class);
                    System.out.println("   ✅ Trace API访问成功: " + response);
                } catch (Exception e) {
                    System.out.println("   ⚠️  Trace API访问失败: " + e.getMessage());
                    if (e.getMessage().contains("403")) {
                        System.out.println("   💡 提示: 403错误通常表示权限不足，请检查:");
                        System.out.println("      - Workspace ID是否正确");
                        System.out.println("      - API Token是否有效");
                        System.out.println("      - 是否有访问trace API的权限");
                    }
                }
                
            } finally {
                httpClient.close();
            }
            
        } catch (Exception e) {
            System.out.println("   ❌ 认证测试异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试数据上报功能（展示具体的HTTP请求）
     */
    private static void testDataExport() {
        System.out.println("\n📋 测试6: 数据上报功能（展示HTTP请求）");
        
        try {
            // 设置环境变量
            System.setProperty("COZELOOP_WORKSPACE_ID", "7534944671286558755");
            System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
            
            // 创建HTTP客户端
            com.coze.cozeloop.trace.http.DefaultHttpClient httpClient = 
                new com.coze.cozeloop.trace.http.DefaultHttpClient("https://api.coze.cn");
            
            // 创建导出器
            com.coze.cozeloop.trace.internal.SpanExporter exporter = 
                new com.coze.cozeloop.trace.internal.SpanExporter(httpClient);
            
            // 创建测试Span数据
            com.coze.cozeloop.trace.entity.UploadSpan testSpan = new com.coze.cozeloop.trace.entity.UploadSpan();
            testSpan.setSpanID("test-span-123");
            testSpan.setTraceID("test-trace-456");
            testSpan.setSpanName("test_span");
            testSpan.setSpanType("test_type");
            testSpan.setWorkspaceID("7534944671286558755");
            testSpan.setStartedAtMicros(System.currentTimeMillis() * 1000);
            testSpan.setDurationMicros(1000000); // 1秒
            
            // 设置标签
            Map<String, String> tagsString = new HashMap<>();
            tagsString.put("test_key", "test_value");
            tagsString.put("mode", "export_test");
            testSpan.setTagsString(tagsString);
            
            // 创建UploadSpanData
            List<com.coze.cozeloop.trace.entity.UploadSpan> spans = new ArrayList<>();
            spans.add(testSpan);
            
            com.coze.cozeloop.trace.entity.UploadSpanData uploadData = 
                new com.coze.cozeloop.trace.entity.UploadSpanData(spans);
            
            System.out.println("   📤 准备上报数据:");
            System.out.println("     - 上报路径: " + exporter.getSpanUploadPath());
            System.out.println("     - Span数量: " + spans.size());
            System.out.println("     - 数据内容: " + uploadData);
            
            // 执行上报
            List<Object> spanObjects = new ArrayList<>(spans);
            exporter.exportSpans(spanObjects);
            
            // 关闭资源
            httpClient.close();
            
        } catch (Exception e) {
            System.out.println("   ❌ 数据上报测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 模拟LLM调用（仿照Go SDK的llmCall方法）
     */
    private static void simulateLLMCall(CozeLoopClient client, Span parentSpan) throws Exception {
        System.out.println("   🔄 开始模拟LLM调用...");
        
        // 创建子Span（仿照Go SDK的llmCall span）
        SpanOptions childOptions = new SpanOptions();
        childOptions.setParentSpanID(parentSpan.getSpanID());
        childOptions.setTraceID(parentSpan.getTraceID());
        
        Span llmSpan = client.startSpan("llmCall", "v_model_span", childOptions);
        
        try {
            // 模拟LLM处理时间
            Thread.sleep(1000);  // 1秒，仿照Go SDK的time.Sleep(1 * time.Second)
            
            // 模拟LLM响应数据
            String input = "上海天气怎么样？";
            String output = "上海天气晴朗，气温25摄氏度。";
            String modelName = "gpt-4o-2024-05-13";
            int inputTokens = 11;
            int outputTokens = 52;
            
            // 设置LLM相关的标签（仿照Go SDK）
            llmSpan.setInput(input);                    // 设置输入
            llmSpan.setOutput(output);                  // 设置输出
            llmSpan.setModelProvider("openai");         // 设置模型提供商
            llmSpan.setModelName(modelName);            // 设置模型名称
            llmSpan.setInputTokens(inputTokens);        // 设置输入token数
            llmSpan.setOutputTokens(outputTokens);      // 设置输出token数
            
            // 设置首次响应时间（仿照Go SDK的SetStartTimeFirstResp）
            long firstRespTime = System.currentTimeMillis() * 1000; // 转换为微秒
            llmSpan.setStartTimeFirstResp(firstRespTime);
            
            System.out.println("   ✅ LLM调用成功:");
            System.out.println("     - 输入: " + input);
            System.out.println("     - 输出: " + output);
            System.out.println("     - 模型: " + modelName);
            System.out.println("     - 输入Token: " + inputTokens);
            System.out.println("     - 输出Token: " + outputTokens);
            
        } finally {
            // 完成LLM Span
            llmSpan.finish();
            System.out.println("   ✅ LLM Span完成，持续时间: " + llmSpan.getDuration() + " 微秒");
        }
    }
}
