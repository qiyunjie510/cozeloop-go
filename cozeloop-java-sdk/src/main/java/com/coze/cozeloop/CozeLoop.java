package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
import com.coze.cozeloop.trace.TraceOptions;
import com.coze.cozeloop.trace.TraceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * CozeLoop主入口类，对应Go代码中的主入口函数
 * 提供静态方法来创建和管理Trace
 */
public class CozeLoop {
    
    private static TraceProvider defaultProvider;
    
    /**
     * 创建新的Span
     * 对应Go代码中的StartSpan函数
     */
    public static Span startSpan(String name, String spanType) {
        return startSpan(name, spanType, new SpanOptions());
    }
    
    /**
     * 创建新的Span（带选项）
     */
    public static Span startSpan(String name, String spanType, SpanOptions options) {
        if (defaultProvider == null) {
            throw new IllegalStateException("CozeLoop not initialized. Call CozeLoop.init() first.");
        }
        return defaultProvider.startSpan(name, spanType, options);
    }
    
    /**
     * 从HTTP Header获取Span上下文
     * 对应Go代码中的GetSpanFromHeader函数
     */
    public static com.coze.cozeloop.trace.SpanContext getSpanFromHeader(Map<String, String> headers) {
        if (defaultProvider == null) {
            throw new IllegalStateException("CozeLoop not initialized. Call CozeLoop.init() first.");
        }
        return defaultProvider.getSpanFromHeader(headers);
    }
    
    /**
     * 强制刷新队列
     * 对应Go代码中的Flush函数
     */
    public static void flush() {
        if (defaultProvider != null) {
            defaultProvider.flush();
        }
    }
    
    /**
     * 关闭CozeLoop
     * 对应Go代码中的Close函数
     */
    public static void close() {
        if (defaultProvider != null) {
            defaultProvider.shutdown();
            defaultProvider = null;
        }
    }
    
    /**
     * 初始化CozeLoop（内部使用）
     */
    public static void init(TraceProvider provider) {
        defaultProvider = provider;
    }
    
    /**
     * 获取默认的TraceProvider
     */
    public static TraceProvider getDefaultProvider() {
        return defaultProvider;
    }
    
    /**
     * 创建新的CozeLoop客户端（仿照Go SDK的NewClient）
     * 自动从系统属性读取配置
     */
    public static CozeLoopClient newClient() {
        // 从系统属性读取配置（对应Go SDK的buildOptionsFromEnv）
        String workspaceID = System.getProperty("COZELOOP_WORKSPACE_ID");
        String apiToken = System.getProperty("COZELOOP_API_TOKEN");
        String apiBaseURL = System.getProperty("COZELOOP_API_BASE_URL");
        
        if (workspaceID == null || apiToken == null) {
            throw new IllegalStateException("Missing required configuration: COZELOOP_WORKSPACE_ID and COZELOOP_API_TOKEN must be set");
        }
        
        if (apiBaseURL == null) {
            apiBaseURL = "https://api.coze.cn";  // 默认值
        }
        
        // 创建TraceOptions
        TraceOptions options = new TraceOptions();
        options.setWorkspaceID(workspaceID);
        options.setApiBaseURL(apiBaseURL);
        options.setServiceName("java-sdk-client");
        
        // 创建TraceProvider
        TraceProvider provider = new com.coze.cozeloop.trace.internal.DefaultTraceProvider(options);
        
        // 初始化CozeLoop
        init(provider);
        
        return new CozeLoopClient(provider);
    }
}
