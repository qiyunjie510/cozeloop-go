package com.coze.cozeloop.trace.internal;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.entity.UploadSpan;
import com.coze.cozeloop.trace.entity.UploadSpanData;
import com.coze.cozeloop.trace.http.HttpClient;
import com.coze.cozeloop.trace.util.JsonUtils;
import com.coze.cozeloop.trace.entity.BaseResponse;

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
    
    /**
     * 构造函数（使用默认路径）
     */
    public SpanExporter(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.spanUploadPath = "/v1/loop/traces/ingest";  // 对应Go SDK的pathIngestTrace
        this.fileUploadPath = "/v1/loop/files/upload";   // 对应Go SDK的pathUploadFile
    }
    
    @Override
    public void exportSpans(List<Object> spans) {
        try {
            // 转换为UploadSpan列表
            List<UploadSpan> uploadSpans = new ArrayList<>();
            for (Object span : spans) {
                if (span instanceof Span) {
                    Span s = (Span) span;
                    UploadSpan uploadSpan = convertToUploadSpan(s);
                    uploadSpans.add(uploadSpan);
                }
            }
            
            if (!uploadSpans.isEmpty()) {
                // 创建UploadSpanData结构（对应Go SDK的UploadSpanData）
                UploadSpanData uploadData = new UploadSpanData();
                uploadData.setSpans(uploadSpans);
                
                // 发送到CozeLoop平台（对应Go SDK的ExportSpans方法）
                System.out.println("📤 正在上报 " + uploadSpans.size() + " 个Span到: " + spanUploadPath);
                
                // 使用BaseResponse处理响应（对应Go SDK的BaseResponse）
                BaseResponse response = httpClient.post(spanUploadPath, uploadData, BaseResponse.class);
                
                // 检查响应码（对应Go SDK的resp.GetCode() != 0检查）
                if (response != null && response.isSuccess()) {
                    System.out.println("✅ Span上报成功，响应: " + response);
                } else {
                    String errorMsg = response != null ? 
                        String.format("code:[%d], msg:[%s]", response.getCode(), response.getMsg()) :
                        "response is null";
                    System.err.println("❌ Span上报失败: " + errorMsg);
                    throw new RuntimeException("Span export failed: " + errorMsg);
                }
            }
            
        } catch (Exception e) {
            // TODO: 添加重试机制和错误处理
            System.err.println("❌ Span上报失败: " + e.getMessage());
            e.printStackTrace();
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
    private UploadSpan convertToUploadSpan(Span span) {
        UploadSpan uploadSpan = new UploadSpan();
        
        // 设置基本字段
        uploadSpan.setLogID("");
        uploadSpan.setServiceName("");
        uploadSpan.setInput(span.getInput() != null ? span.getInput().toString() : "");
        uploadSpan.setOutput(span.getOutput() != null ? span.getOutput().toString() : "");
        uploadSpan.setObjectStorage("");
        uploadSpan.setSystemTagsString(new HashMap<>());

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
