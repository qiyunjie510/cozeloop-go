package com.coze.cozeloop.trace;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Span选项类，对应Go代码中的StartSpanOptions结构体
 * 用于配置Span的创建参数
 */
public class SpanOptions {
    
    private Instant startTime;
    private String parentSpanID;
    private String traceID;
    private Map<String, String> baggage;
    private boolean startNewTrace;
    private String scene;
    private String workspaceID;
    
    public SpanOptions() {
        this.baggage = new HashMap<>();
        this.startNewTrace = false;
    }
    
    // Getters and Setters
    public Instant getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
    
    public String getParentSpanID() {
        return parentSpanID;
    }
    
    public void setParentSpanID(String parentSpanID) {
        this.parentSpanID = parentSpanID;
    }
    
    public String getTraceID() {
        return traceID;
    }
    
    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }
    
    public Map<String, String> getBaggage() {
        return baggage;
    }
    
    public void setBaggage(Map<String, String> baggage) {
        this.baggage = baggage != null ? new HashMap<>(baggage) : new HashMap<>();
    }
    
    public boolean isStartNewTrace() {
        return startNewTrace;
    }
    
    public void setStartNewTrace(boolean startNewTrace) {
        this.startNewTrace = startNewTrace;
    }
    
    public String getScene() {
        return scene;
    }
    
    public void setScene(String scene) {
        this.scene = scene;
    }
    
    public String getWorkspaceID() {
        return workspaceID;
    }
    
    public void setWorkspaceID(String workspaceID) {
        this.workspaceID = workspaceID;
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
     * 复制当前选项
     */
    public SpanOptions copy() {
        SpanOptions copy = new SpanOptions();
        copy.startTime = this.startTime;
        copy.parentSpanID = this.parentSpanID;
        copy.traceID = this.traceID;
        copy.baggage = new HashMap<>(this.baggage);
        copy.startNewTrace = this.startNewTrace;
        copy.scene = this.scene;
        copy.workspaceID = this.workspaceID;
        return copy;
    }
    
    @Override
    public String toString() {
        return "SpanOptions{" +
                "startTime=" + startTime +
                ", parentSpanID='" + parentSpanID + '\'' +
                ", traceID='" + traceID + '\'' +
                ", baggage=" + baggage +
                ", startNewTrace=" + startNewTrace +
                ", scene='" + scene + '\'' +
                ", workspaceID='" + workspaceID + '\'' +
                '}';
    }
}
