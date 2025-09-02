package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
import com.coze.cozeloop.trace.TraceProvider;

import java.util.Map;

/**
 * CozeLoop客户端类，对应Go SDK的Client接口
 * 提供与Go SDK一致的API
 */
public class CozeLoopClient {
    
    private final TraceProvider traceProvider;
    private final String workspaceID;
    
    /**
     * 构造函数
     */
    public CozeLoopClient(TraceProvider traceProvider) {
        this.traceProvider = traceProvider;
        this.workspaceID = traceProvider.getOptions().getWorkspaceID();
    }
    
    /**
     * 开始一个新的Span（对应Go SDK的StartSpan）
     */
    public Span startSpan(String name, String spanType) {
        return startSpan(name, spanType, new SpanOptions());
    }
    
    /**
     * 开始一个新的Span（带选项）
     */
    public Span startSpan(String name, String spanType, SpanOptions options) {
        return traceProvider.startSpan(name, spanType, options);
    }
    
    /**
     * 从HTTP Header获取Span上下文
     */
    public com.coze.cozeloop.trace.SpanContext getSpanFromHeader(Map<String, String> headers) {
        return traceProvider.getSpanFromHeader(headers);
    }
    
    /**
     * 强制刷新队列
     */
    public void flush() {
        traceProvider.flush();
    }
    
    /**
     * 关闭客户端
     */
    public void close() {
        traceProvider.shutdown();
    }
    
    /**
     * 获取Workspace ID
     */
    public String getWorkspaceID() {
        return workspaceID;
    }
    
    /**
     * 获取TraceProvider
     */
    public TraceProvider getTraceProvider() {
        return traceProvider;
    }
}
