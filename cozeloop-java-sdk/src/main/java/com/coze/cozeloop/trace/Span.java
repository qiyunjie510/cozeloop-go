package com.coze.cozeloop.trace;

import java.util.Map;

/**
 * Span接口，对应Go代码中的Span接口
 * 用于表示分布式追踪中的一个操作单元
 */
public interface Span {
    
    /**
     * 设置标签
     */
    void setTags(Map<String, Object> tags);
    
    /**
     * 设置单个标签
     */
    void setTag(String key, Object value);
    
    /**
     * 设置输入
     */
    void setInput(Object input);
    
    /**
     * 设置输出
     */
    void setOutput(Object output);
    
    /**
     * 设置Baggage（跨Span传递的上下文信息）
     */
    void setBaggage(Map<String, String> baggage);
    
    /**
     * 设置单个Baggage
     */
    void setBaggage(String key, String value);
    
    /**
     * 设置用户ID到Baggage
     */
    void setUserIDBaggage(String userID);
    
    /**
     * 设置模型提供者
     */
    void setModelProvider(String provider);
    
    /**
     * 设置模型名称
     */
    void setModelName(String modelName);
    
    /**
     * 设置首次响应时间
     */
    void setStartTimeFirstResp(long micros);
    
    /**
     * 设置输入token数量
     */
    void setInputTokens(int tokens);
    
    /**
     * 设置输出token数量
     */
    void setOutputTokens(int tokens);
    
    /**
     * 设置状态码
     */
    void setStatusCode(int statusCode);
    
    /**
     * 设置错误
     */
    void setError(Throwable error);
    
    /**
     * 转换为HTTP Header格式
     */
    Map<String, String> toHeader();
    
    /**
     * 完成Span
     */
    void finish();
    
    /**
     * 获取Span ID
     */
    String getSpanID();
    
    /**
     * 获取Trace ID
     */
    String getTraceID();
    
    /**
     * 获取父Span ID
     */
    String getParentID();
    
    /**
     * 获取Baggage
     */
    Map<String, String> getBaggage();
    
    /**
     * 获取标签
     */
    Map<String, Object> getTagMap();
    
    /**
     * 获取持续时间（微秒）
     */
    long getDuration();
    
    /**
     * 获取工作空间ID
     */
    String getSpaceID();
    
    /**
     * 获取Span名称
     */
    String getSpanName();
    
    /**
     * 获取Span类型
     */
    String getSpanType();
    
    /**
     * 获取状态码
     */
    int getStatusCode();
    
    /**
     * 获取开始时间
     */
    long getStartTime();
    
    /**
     * 是否为根Span
     */
    boolean isRootSpan();
}
