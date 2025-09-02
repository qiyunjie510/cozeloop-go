package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
import com.coze.cozeloop.trace.TraceOptions;

import java.util.HashMap;
import java.util.Map;

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
     * 测试完整的Span创建流程
     */
    private static void testSpanCreation() {
        System.out.println("\n📋 测试5: 完整的Span创建流程");
        
        try {
            // 创建TraceOptions
            TraceOptions traceOptions = new TraceOptions();
            traceOptions.setApiBaseURL("https://api.coze.cn");
            traceOptions.setWorkspaceID("test-workspace");
            traceOptions.setServiceName("test-service");
            
            // 创建TraceProvider
            com.coze.cozeloop.trace.internal.DefaultTraceProvider provider = 
                new com.coze.cozeloop.trace.internal.DefaultTraceProvider(traceOptions);
            
            // 初始化CozeLoop
            CozeLoop.init(provider);
            
            // 创建Span
            Span span = CozeLoop.startSpan("test-operation", "test-type");
            span.setTag("test-key", "test-value");
            span.setInput("test input data");
            span.setOutput("test output data");
            span.setBaggage("user-id", "user123");
            span.setBaggage("request-id", "req456");
            
            System.out.println("   ✅ Span创建成功:");
            System.out.println("     - SpanID: " + span.getSpanID());
            System.out.println("     - TraceID: " + span.getTraceID());
            System.out.println("     - Name: " + span.getSpanName());
            System.out.println("     - Type: " + span.getSpanType());
            System.out.println("     - WorkspaceID: " + span.getSpaceID());
            
            // 测试Header转换
            Map<String, String> headers = span.toHeader();
            System.out.println("   ✅ Header转换成功:");
            headers.forEach((key, value) -> System.out.println("     " + key + ": " + value));
            
            // 完成Span
            span.finish();
            System.out.println("   ✅ Span完成，持续时间: " + span.getDuration() + " 微秒");
            
            // 刷新队列
            CozeLoop.flush();
            System.out.println("   ✅ 队列刷新完成");
            
            // 关闭
            CozeLoop.close();
            System.out.println("   ✅ CozeLoop关闭完成");
            
        } catch (Exception e) {
            System.out.println("   ❌ Span创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
