package com.coze.cozeloop.trace.internal;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanContext;
import com.coze.cozeloop.trace.util.IdGenerator;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DefaultSpan实现类，对应Go代码中的Span结构体
 * 这是Span接口的默认实现
 */
public class DefaultSpan implements Span {
    
    // Span上下文
    private final SpanContext spanContext;
    
    // 基本参数
    private Object input;
    private Object output;
    private final String spanType;
    private final String name;
    private final String workspaceID;
    private final String parentSpanID;
    private final Instant startTime;
    private Instant finishTime;
    private long durationMicros;
    private final Map<String, Object> tagMap;
    private final Map<String, Object> systemTagMap;
    private int statusCode;
    
    // 内部字段
    private final AtomicBoolean isFinished;
    private SpanProcessor spanProcessor;
    
    /**
     * 构造函数
     */
    public DefaultSpan(String name, String spanType, String workspaceID, String parentSpanID, String traceID) {
        this.name = name;
        this.spanType = spanType;
        this.workspaceID = workspaceID;
        this.parentSpanID = parentSpanID;
        this.startTime = Instant.now();
        
        // 生成新的Span ID和Trace ID，使用Go SDK兼容的ID生成器
        String spanID = com.coze.cozeloop.trace.internal.IdGen.nextHexId();
        String finalTraceID = traceID != null ? traceID : com.coze.cozeloop.trace.internal.IdGen.nextHexTraceId();
        
        this.spanContext = new SpanContext(spanID, finalTraceID);
        this.tagMap = new HashMap<>();
        this.systemTagMap = new HashMap<>();
        this.isFinished = new AtomicBoolean(false);
        this.statusCode = 0;
    }
    
    // ========== Span接口实现 ==========
    
    @Override
    public void setTags(Map<String, Object> tags) {
        if (tags != null) {
            tagMap.putAll(tags);
        }
    }
    
    @Override
    public void setTag(String key, Object value) {
        tagMap.put(key, value);
    }
    
    @Override
    public void setInput(Object input) {
        this.input = input;
    }

    @Override
    public Object getInput() {
        return this.input;
    }

    @Override
    public void setOutput(Object output) {
        this.output = output;
    }

    @Override
    public Object getOutput() {
        return this.output;
    }

    @Override
    public void setBaggage(Map<String, String> baggage) {
        if (baggage != null) {
            spanContext.setBaggage(baggage);
        }
    }
    
    @Override
    public void setBaggage(String key, String value) {
        spanContext.addBaggage(key, value);
    }
    
    @Override
    public void setUserIDBaggage(String userID) {
        setBaggage("user_id", userID);
    }
    
    @Override
    public void setModelProvider(String provider) {
        setTag("model_provider", provider);
    }
    
    @Override
    public void setModelName(String modelName) {
        setTag("model_name", modelName);
    }
    
    @Override
    public void setStartTimeFirstResp(long micros) {
        setTag("start_time_first_resp", micros);
    }
    
    @Override
    public void setInputTokens(int tokens) {
        setTag("input_tokens", tokens);
    }
    
    @Override
    public void setOutputTokens(int tokens) {
        setTag("output_tokens", tokens);
    }
    
    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    @Override
    public void setError(Throwable error) {
        setTag("error", error.getMessage());
        setStatusCode(500);
    }
    
    @Override
    public Map<String, String> toHeader() {
        Map<String, String> headers = new HashMap<>();
        
        // W3C Trace Context标准
        String traceparent = String.format("00-%s-%s-01", 
            spanContext.getTraceID(), spanContext.getSpanID());
        headers.put("X-Cozeloop-Traceparent", traceparent);
        
        // Baggage
        if (!spanContext.getBaggage().isEmpty()) {
            StringBuilder baggage = new StringBuilder();
            for (Map.Entry<String, String> entry : spanContext.getBaggage().entrySet()) {
                if (baggage.length() > 0) {
                    baggage.append(",");
                }
                baggage.append(entry.getKey()).append("=").append(entry.getValue());
            }
            headers.put("X-Cozeloop-Tracestate", baggage.toString());
        }
        
        return headers;
    }
    
    @Override
    public void finish() {
        if (isFinished.compareAndSet(false, true)) {
            this.finishTime = Instant.now();
            this.durationMicros = (finishTime.getEpochSecond() - startTime.getEpochSecond()) * 1_000_000;
            
            // 通知SpanProcessor
            if (spanProcessor != null) {
                spanProcessor.onSpanEnd(this);
            }
        }
    }
    
    @Override
    public String getSpanID() {
        return spanContext.getSpanID();
    }
    
    @Override
    public String getTraceID() {
        return spanContext.getTraceID();
    }
    
    @Override
    public String getParentID() {
        return parentSpanID;
    }
    
    @Override
    public Map<String, String> getBaggage() {
        return spanContext.getBaggage();
    }
    
    @Override
    public Map<String, Object> getTagMap() {
        return new HashMap<>(tagMap);
    }
    
    @Override
    public long getDuration() {
        return durationMicros;
    }
    
    @Override
    public String getSpaceID() {
        return workspaceID;
    }
    
    @Override
    public String getSpanName() {
        return name;
    }
    
    @Override
    public String getSpanType() {
        return spanType;
    }
    
    @Override
    public int getStatusCode() {
        return statusCode;
    }
    
    @Override
    public long getStartTime() {
        return startTime.getEpochSecond();
    }
    
    @Override
    public boolean isRootSpan() {
        return "0".equals(parentSpanID);
    }
    
    // ========== 内部方法 ==========
    
    /**
     * 设置SpanProcessor
     */
    void setSpanProcessor(SpanProcessor processor) {
        this.spanProcessor = processor;
    }
    
    /**
     * 获取系统标签
     */
    Map<String, Object> getSystemTagMap() {
        return new HashMap<>(systemTagMap);
    }
}
