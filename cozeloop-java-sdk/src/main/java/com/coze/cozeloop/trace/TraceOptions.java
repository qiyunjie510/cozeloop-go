package com.coze.cozeloop.trace;

import java.util.HashMap;
import java.util.Map;

/**
 * Trace配置选项类，对应Go代码中的TraceOptions结构体
 * 用于配置TraceProvider的行为
 */
public class TraceOptions {
    
    private String apiBaseURL;
    private String workspaceID;
    private String serviceName;
    private int maxQueueSize;
    private int batchSize;
    private long flushIntervalMs;
    private boolean enableRetry;
    private int maxRetryAttempts;
    private Map<String, String> customHeaders;
    
    public TraceOptions() {
        this.apiBaseURL = "https://api.coze.cn";
        this.maxQueueSize = 10000;
        this.batchSize = 100;
        this.flushIntervalMs = 5000;
        this.enableRetry = true;
        this.maxRetryAttempts = 3;
        this.customHeaders = new HashMap<>();
    }
    
    // Getters and Setters
    public String getApiBaseURL() {
        return apiBaseURL;
    }
    
    public void setApiBaseURL(String apiBaseURL) {
        this.apiBaseURL = apiBaseURL;
    }
    
    public String getWorkspaceID() {
        return workspaceID;
    }
    
    public void setWorkspaceID(String workspaceID) {
        this.workspaceID = workspaceID;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public int getMaxQueueSize() {
        return maxQueueSize;
    }
    
    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    public long getFlushIntervalMs() {
        return flushIntervalMs;
    }
    
    public void setFlushIntervalMs(long flushIntervalMs) {
        this.flushIntervalMs = flushIntervalMs;
    }
    
    public boolean isEnableRetry() {
        return enableRetry;
    }
    
    public void setEnableRetry(boolean enableRetry) {
        this.enableRetry = enableRetry;
    }
    
    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }
    
    public void setMaxRetryAttempts(int maxRetryAttempts) {
        this.maxRetryAttempts = maxRetryAttempts;
    }
    
    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }
    
    public void setCustomHeaders(Map<String, String> customHeaders) {
        this.customHeaders = customHeaders != null ? new HashMap<>(customHeaders) : new HashMap<>();
    }
    
    /**
     * 添加自定义Header
     */
    public void addCustomHeader(String key, String value) {
        if (this.customHeaders == null) {
            this.customHeaders = new HashMap<>();
        }
        this.customHeaders.put(key, value);
    }
    
    /**
     * 复制当前选项
     */
    public TraceOptions copy() {
        TraceOptions copy = new TraceOptions();
        copy.apiBaseURL = this.apiBaseURL;
        copy.workspaceID = this.workspaceID;
        copy.serviceName = this.serviceName;
        copy.maxQueueSize = this.maxQueueSize;
        copy.batchSize = this.batchSize;
        copy.flushIntervalMs = this.flushIntervalMs;
        copy.enableRetry = this.enableRetry;
        copy.maxRetryAttempts = this.maxRetryAttempts;
        copy.customHeaders = new HashMap<>(this.customHeaders);
        return copy;
    }
    
    @Override
    public String toString() {
        return "TraceOptions{" +
                "apiBaseURL='" + apiBaseURL + '\'' +
                ", workspaceID='" + workspaceID + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", maxQueueSize=" + maxQueueSize +
                ", batchSize=" + batchSize +
                ", flushIntervalMs=" + flushIntervalMs +
                ", enableRetry=" + enableRetry +
                ", maxRetryAttempts=" + maxRetryAttempts +
                ", customHeaders=" + customHeaders +
                '}';
    }
}
