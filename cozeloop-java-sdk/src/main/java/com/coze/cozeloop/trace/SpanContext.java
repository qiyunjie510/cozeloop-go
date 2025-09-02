package com.coze.cozeloop.trace;

import java.util.HashMap;
import java.util.Map;

/**
 * Span上下文，对应Go代码中的SpanContext结构体
 * 用于在Span之间传递上下文信息
 */
public class SpanContext {
    
    private String spanID;
    private String traceID;
    private Map<String, String> baggage;
    
    public SpanContext() {
        this.baggage = new HashMap<>();
    }
    
    public SpanContext(String spanID, String traceID) {
        this.spanID = spanID;
        this.traceID = traceID;
        this.baggage = new HashMap<>();
    }
    
    public SpanContext(String spanID, String traceID, Map<String, String> baggage) {
        this.spanID = spanID;
        this.traceID = traceID;
        this.baggage = baggage != null ? new HashMap<>(baggage) : new HashMap<>();
    }
    
    /**
     * 获取Span ID
     */
    public String getSpanID() {
        return spanID;
    }
    
    /**
     * 设置Span ID
     */
    public void setSpanID(String spanID) {
        this.spanID = spanID;
    }
    
    /**
     * 获取Trace ID
     */
    public String getTraceID() {
        return traceID;
    }
    
    /**
     * 设置Trace ID
     */
    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }
    
    /**
     * 获取Baggage
     */
    public Map<String, String> getBaggage() {
        return baggage;
    }
    
    /**
     * 设置Baggage
     */
    public void setBaggage(Map<String, String> baggage) {
        this.baggage = baggage != null ? new HashMap<>(baggage) : new HashMap<>();
    }
    
    /**
     * 添加单个Baggage
     */
    public void addBaggage(String key, String value) {
        if (this.baggage == null) {
            this.baggage = new HashMap<>();
        }
        this.baggage.put(key, value);
    }
    
    /**
     * 获取单个Baggage
     */
    public String getBaggage(String key) {
        return baggage != null ? baggage.get(key) : null;
    }
    
    /**
     * 复制当前SpanContext
     */
    public SpanContext copy() {
        return new SpanContext(this.spanID, this.traceID, this.baggage);
    }
    
    @Override
    public String toString() {
        return "SpanContext{" +
                "spanID='" + spanID + '\'' +
                ", traceID='" + traceID + '\'' +
                ", baggage=" + baggage +
                '}';
    }
}
