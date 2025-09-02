package com.coze.cozeloop.trace.entity;

import java.util.Map;

/**
 * 上传Span实体类，对应Go代码中的UploadSpan结构体
 * 用于向CozeLoop平台上传Span数据
 */
public class UploadSpan {
    
    private long startedAtMicros;           // 开始时间（微秒）
    private String logID;                   // 日志ID
    private String spanID;                  // Span ID
    private String parentID;                // 父Span ID
    private String traceID;                 // Trace ID
    private long durationMicros;            // 持续时间（微秒）
    private String serviceName;             // 服务名称
    private String workspaceID;             // 工作空间ID
    private String spanName;                // Span名称
    private String spanType;                // Span类型
    private int statusCode;                 // 状态码
    private String input;                   // 输入
    private String output;                  // 输出
    private String objectStorage;           // 对象存储
    private Map<String, String> systemTagsString;   // 系统标签（字符串）
    private Map<String, Long> systemTagsLong;       // 系统标签（长整型）
    private Map<String, Double> systemTagsDouble;   // 系统标签（双精度）
    private Map<String, String> tagsString;         // 标签（字符串）
    private Map<String, Long> tagsLong;             // 标签（长整型）
    private Map<String, Double> tagsDouble;         // 标签（双精度）
    private Map<String, Boolean> tagsBool;          // 标签（布尔型）
    
    // 构造函数
    public UploadSpan() {}
    
    // Getters and Setters
    public long getStartedAtMicros() { return startedAtMicros; }
    public void setStartedAtMicros(long startedAtMicros) { this.startedAtMicros = startedAtMicros; }
    
    public String getLogID() { return logID; }
    public void setLogID(String logID) { this.logID = logID; }
    
    public String getSpanID() { return spanID; }
    public void setSpanID(String spanID) { this.spanID = spanID; }
    
    public String getParentID() { return parentID; }
    public void setParentID(String parentID) { this.parentID = parentID; }
    
    public String getTraceID() { return traceID; }
    public void setTraceID(String traceID) { this.traceID = traceID; }
    
    public long getDurationMicros() { return durationMicros; }
    public void setDurationMicros(long durationMicros) { this.durationMicros = durationMicros; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getWorkspaceID() { return workspaceID; }
    public void setWorkspaceID(String workspaceID) { this.workspaceID = workspaceID; }
    
    public String getSpanName() { return spanName; }
    public void setSpanName(String spanName) { this.spanName = spanName; }
    
    public String getSpanType() { return spanType; }
    public void setSpanType(String spanType) { this.spanType = spanType; }
    
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    
    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
    
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    
    public String getObjectStorage() { return objectStorage; }
    public void setObjectStorage(String objectStorage) { this.objectStorage = objectStorage; }
    
    public Map<String, String> getSystemTagsString() { return systemTagsString; }
    public void setSystemTagsString(Map<String, String> systemTagsString) { this.systemTagsString = systemTagsString; }
    
    public Map<String, Long> getSystemTagsLong() { return systemTagsLong; }
    public void setSystemTagsLong(Map<String, Long> systemTagsLong) { this.systemTagsLong = systemTagsLong; }
    
    public Map<String, Double> getSystemTagsDouble() { return systemTagsDouble; }
    public void setSystemTagsDouble(Map<String, Double> systemTagsDouble) { this.systemTagsDouble = systemTagsDouble; }
    
    public Map<String, String> getTagsString() { return tagsString; }
    public void setTagsString(Map<String, String> tagsString) { this.tagsString = tagsString; }
    
    public Map<String, Long> getTagsLong() { return tagsLong; }
    public void setTagsLong(Map<String, Long> tagsLong) { this.tagsLong = tagsLong; }
    
    public Map<String, Double> getTagsDouble() { return tagsDouble; }
    public void setTagsDouble(Map<String, Double> tagsDouble) { this.tagsDouble = tagsDouble; }
    
    public Map<String, Boolean> getTagsBool() { return tagsBool; }
    public void setTagsBool(Map<String, Boolean> tagsBool) { this.tagsBool = tagsBool; }
}
