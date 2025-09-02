package com.coze.cozeloop.trace.internal;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.entity.UploadSpan;
import com.coze.cozeloop.trace.http.HttpClient;
import com.coze.cozeloop.trace.util.JsonUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Span数据导出器，对应Go代码中的SpanExporter
 * 负责将Span数据转换为UploadSpan并导出到CozeLoop平台
 */
public class SpanExporter implements Exporter {
    
    private final HttpClient httpClient;
    private final String spanUploadPath;
    private final String fileUploadPath;
    
    /**
     * 构造函数
     */
    public SpanExporter(HttpClient httpClient, String spanUploadPath, String fileUploadPath) {
        this.httpClient = httpClient;
        this.spanUploadPath = spanUploadPath;
        this.fileUploadPath = fileUploadPath;
    }
    
    @Override
    public void exportSpans(List<Object> spans) {
        if (spans == null || spans.isEmpty()) {
            return;
        }
        
        try {
            // 将Span转换为UploadSpan
            List<UploadSpan> uploadSpans = new ArrayList<>();
            for (Object obj : spans) {
                if (obj instanceof Span) {
                    UploadSpan uploadSpan = convertSpanToUploadSpan((Span) obj);
                    uploadSpans.add(uploadSpan);
                }
            }
            
            if (!uploadSpans.isEmpty()) {
                // 发送到CozeLoop平台
                httpClient.post(spanUploadPath, uploadSpans, String.class);
            }
            
        } catch (Exception e) {
            // TODO: 添加重试机制和错误处理
            System.err.println("Failed to export spans: " + e.getMessage());
        }
    }
    
    @Override
    public void exportFiles(List<Object> files) {
        // TODO: 实现文件导出功能
        System.out.println("File export not implemented yet");
    }
    
    @Override
    public void close() {
        if (httpClient != null) {
            httpClient.close();
        }
    }
    
    /**
     * 将Span转换为UploadSpan
     */
    private UploadSpan convertSpanToUploadSpan(Span span) {
        UploadSpan uploadSpan = new UploadSpan();
        
        // 设置基本字段
        uploadSpan.setSpanID(span.getSpanID());
        uploadSpan.setTraceID(span.getTraceID());
        uploadSpan.setParentID(span.getParentID());
        uploadSpan.setWorkspaceID(span.getSpaceID());
        uploadSpan.setSpanName(span.getSpanName());
        uploadSpan.setSpanType(span.getSpanType());
        uploadSpan.setStatusCode(span.getStatusCode());
        uploadSpan.setDurationMicros(span.getDuration());
        
        // 设置时间字段
        long startTimeMicros = span.getStartTime() * 1_000_000; // 转换为微秒
        uploadSpan.setStartedAtMicros(startTimeMicros);
        
        // 设置标签
        Map<String, Object> tags = span.getTagMap();
        if (tags != null) {
            // 分离不同类型的标签
            Map<String, String> tagsString = new HashMap<>();
            Map<String, Long> tagsLong = new HashMap<>();
            Map<String, Double> tagsDouble = new HashMap<>();
            Map<String, Boolean> tagsBool = new HashMap<>();
            
            for (Map.Entry<String, Object> entry : tags.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                if (value instanceof String) {
                    tagsString.put(key, (String) value);
                } else if (value instanceof Long || value instanceof Integer) {
                    tagsLong.put(key, ((Number) value).longValue());
                } else if (value instanceof Double || value instanceof Float) {
                    tagsDouble.put(key, ((Number) value).doubleValue());
                } else if (value instanceof Boolean) {
                    tagsBool.put(key, (Boolean) value);
                } else {
                    // 其他类型转换为字符串
                    tagsString.put(key, value != null ? value.toString() : "");
                }
            }
            
            uploadSpan.setTagsString(tagsString);
            uploadSpan.setTagsLong(tagsLong);
            uploadSpan.setTagsDouble(tagsDouble);
            uploadSpan.setTagsBool(tagsBool);
        }
        
        // 设置Baggage
        Map<String, String> baggage = span.getBaggage();
        if (baggage != null) {
            // 将Baggage转换为系统标签
            Map<String, String> systemTagsString = new HashMap<>();
            for (Map.Entry<String, String> entry : baggage.entrySet()) {
                systemTagsString.put("baggage." + entry.getKey(), entry.getValue());
            }
            uploadSpan.setSystemTagsString(systemTagsString);
        }
        
        return uploadSpan;
    }
    
    /**
     * 获取Span上传路径
     */
    public String getSpanUploadPath() {
        return spanUploadPath;
    }
    
    /**
     * 获取文件上传路径
     */
    public String getFileUploadPath() {
        return fileUploadPath;
    }
}
