package com.coze.cozeloop.trace.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 默认HTTP客户端实现，对应Go代码中的httpclient包
 * 使用Apache HttpClient来发送HTTP请求
 */
public class DefaultHttpClient implements HttpClient {
    
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseURL;
    
    /**
     * 构造函数
     */
    public DefaultHttpClient(String baseURL) {
        this.baseURL = baseURL;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public <T> T post(String url, Object data, Class<T> responseType) {
        return post(url, data, null, responseType);
    }
    
    @Override
    public <T> T post(String url, Object data, Map<String, String> headers, Class<T> responseType) {
        String fullURL = baseURL + url;
        HttpPost httpPost = new HttpPost(fullURL);
        
        try {
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // 设置默认请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "CozeLoop-Java-SDK/1.0");
            
            // 设置认证头（从系统属性获取，对应Go SDK的NewTokenAuth）
            String apiToken = System.getProperty("COZELOOP_API_TOKEN");
            if (apiToken != null && !apiToken.trim().isEmpty()) {
                // 对应Go SDK的setAuthorizationHeader方法
                httpPost.setHeader("Authorization", "Bearer " + apiToken);
                System.out.println("🔐 设置认证头: Bearer " + apiToken.substring(0, Math.min(apiToken.length(), 20)) + "...");
            } else {
                System.err.println("⚠️  警告: 未设置COZELOOP_API_TOKEN系统属性");
            }
            
            // 设置请求体
            if (data != null) {
                String jsonData = objectMapper.writeValueAsString(data);
                System.out.println("📤 发送请求到: " + fullURL);
                System.out.println("📤 请求数据: " + jsonData.substring(0, Math.min(jsonData.length(), 200)) + "...");
                // 安全地获取请求头值
                String contentType = getHeaderValue(httpPost, "Content-Type");
                String authorization = getHeaderValue(httpPost, "Authorization");
                String userAgent = getHeaderValue(httpPost, "User-Agent");
                
                System.out.println("📤 请求头: Content-Type=" + contentType + 
                                 ", Authorization=" + (authorization != null ? authorization.substring(0, Math.min(authorization.length(), 30)) + "..." : "null") + 
                                 ", User-Agent=" + userAgent);
                httpPost.setEntity(new StringEntity(jsonData, StandardCharsets.UTF_8));
            }
            
            // 发送请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("📥 响应状态码: " + statusCode);
                
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    System.out.println("📥 响应内容: " + responseBody);
                    
                    // 检查HTTP状态码
                    if (statusCode >= 400) {
                        throw new RuntimeException("HTTP request failed with status " + statusCode + ": " + responseBody);
                    }
                    
                    // 如果响应类型是String，直接返回
                    if (responseType == String.class) {
                        return responseType.cast(responseBody);
                    }
                    
                    // 否则尝试解析JSON
                    if (!responseBody.trim().isEmpty()) {
                        return objectMapper.readValue(responseBody, responseType);
                    }
                }
                
                // 如果没有响应体，尝试创建默认实例
                return responseType.getDeclaredConstructor().newInstance();
            }
            
        } catch (Exception e) {
            throw new RuntimeException("HTTP POST request failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public <T> T uploadFile(String url, String fileKey, byte[] fileData, Map<String, String> metadata, Class<T> responseType) {
        // TODO: 实现文件上传功能
        // 这里需要实现multipart/form-data的上传
        throw new UnsupportedOperationException("File upload not implemented yet");
    }
    
    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            // 忽略关闭时的异常
        }
    }
    
    /**
     * 获取基础URL
     */
    public String getBaseURL() {
        return baseURL;
    }
    
    /**
     * 安全地获取HTTP请求头的值
     */
    private String getHeaderValue(HttpPost httpPost, String headerName) {
        try {
            org.apache.http.Header header = httpPost.getFirstHeader(headerName);
            return header != null ? header.getValue() : null;
        } catch (Exception e) {
            return "error";
        }
    }
}
