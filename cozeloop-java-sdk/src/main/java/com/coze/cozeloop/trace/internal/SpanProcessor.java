package com.coze.cozeloop.trace.internal;

import com.coze.cozeloop.trace.Span;

/**
 * Span处理器接口，对应Go代码中的SpanProcessor接口
 * 负责处理Span的生命周期事件
 */
public interface SpanProcessor {
    
    /**
     * 当Span结束时调用
     * 对应Go代码中的OnSpanEnd方法
     */
    void onSpanEnd(Span span);
    
    /**
     * 强制刷新队列
     * 对应Go代码中的Flush方法
     */
    void flush();
    
    /**
     * 关闭处理器
     * 对应Go代码中的Shutdown方法
     */
    void shutdown();
}
