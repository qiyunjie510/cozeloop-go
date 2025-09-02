package com.coze.cozeloop.trace.entity;

import java.util.List;

/**
 * UploadSpanData类，对应Go SDK中的UploadSpanData结构体
 * 用于批量上报Span数据到CozeLoop平台
 */
public class UploadSpanData {
    
    private List<UploadSpan> spans;
    
    /**
     * 构造函数
     */
    public UploadSpanData() {}
    
    /**
     * 构造函数
     */
    public UploadSpanData(List<UploadSpan> spans) {
        this.spans = spans;
    }
    
    /**
     * 获取Span列表
     */
    public List<UploadSpan> getSpans() {
        return spans;
    }
    
    /**
     * 设置Span列表
     */
    public void setSpans(List<UploadSpan> spans) {
        this.spans = spans;
    }
    
    @Override
    public String toString() {
        return "UploadSpanData{" +
                "spans=" + (spans != null ? spans.size() : 0) + " items" +
                '}';
    }
}
