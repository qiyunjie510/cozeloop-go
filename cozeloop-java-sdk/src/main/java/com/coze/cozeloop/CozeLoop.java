package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
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
}
