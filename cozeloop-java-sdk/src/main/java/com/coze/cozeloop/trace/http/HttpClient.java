package com.coze.cozeloop.trace.http;

import java.util.Map;

/**
 * HTTP客户端接口，对应Go代码中的httpclient包
 */
public interface HttpClient {
    
    /**
     * 发送POST请求
     * 对应Go代码中的Post方法
     */
    <T> T post(String url, Object data, Class<T> responseType);
    
    /**
     * 发送POST请求（带Headers）
     */
    <T> T post(String url, Object data, Map<String, String> headers, Class<T> responseType);
    
    /**
     * 上传文件
     * 对应Go代码中的UploadFile方法
     */
    <T> T uploadFile(String url, String fileKey, byte[] fileData, Map<String, String> metadata, Class<T> responseType);
    
    /**
     * 关闭HTTP客户端
     */
    void close();
}
