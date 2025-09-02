package com.coze.cozeloop.trace;

import java.util.Map;

/**
 * Trace提供者接口，对应Go代码中的TraceProvider接口
 * 负责创建和管理Span
 */
public interface TraceProvider {
    
    /**
     * 创建新的Span
     * 对应Go代码中的StartSpan方法
     */
    Span startSpan(String name, String spanType, SpanOptions options);
    
    /**
     * 从Context获取Span
     * 对应Go代码中的GetSpanFromContext方法
     */
    Span getSpanFromContext();
    
    /**
     * 从HTTP Header解析Span上下文
     * 对应Go代码中的GetSpanFromHeader方法
     */
    SpanContext getSpanFromHeader(Map<String, String> headers);
    
    /**
     * 强制刷新队列
     * 对应Go代码中的Flush方法
     */
    void flush();
    
    /**
     * 关闭TraceProvider
     * 对应Go代码中的CloseTrace方法
     */
    void shutdown();
    
    /**
     * 获取配置选项
     */
    TraceOptions getOptions();
}
