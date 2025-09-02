package com.coze.cozeloop.trace.http;

import com.coze.cozeloop.trace.entity.BaseResponse;
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
            // 1. 首先设置基础请求头（对应Go SDK的setHeaders中的基础头）
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "CozeLoop-Java-SDK/1.0");
            
            // 2. 设置自定义请求头（对应Go SDK的headers参数）
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            
            // 3. 设置认证头（对应Go SDK的setAuthorizationHeader）
            String apiToken = System.getProperty("COZELOOP_API_TOKEN");
            if (apiToken != null && !apiToken.trim().isEmpty()) {
                httpPost.setHeader("Authorization", "Bearer " + apiToken);
                System.out.println("🔐 设置认证头: Bearer " + apiToken.substring(0, Math.min(apiToken.length(), 20)) + "...");
            } else {
                System.err.println("⚠️  警告: 未设置COZELOOP_API_TOKEN系统属性");
            }
            
            // 4. 设置环境相关头（对应Go SDK的环境变量头）
            String ttEnv = System.getProperty("x_tt_env");
            if (ttEnv != null && !ttEnv.trim().isEmpty()) {
                httpPost.setHeader("x-tt-env", ttEnv);
            }
            String usePpe = System.getProperty("x_use_ppe");
            if (usePpe != null && !usePpe.trim().isEmpty()) {
                httpPost.setHeader("x-use-ppe", usePpe);
            }
            
            // 5. 设置请求体
            if (data != null) {
                String jsonData = objectMapper.writeValueAsString(data);
                System.out.println("📤 发送请求到: " + fullURL);
                System.out.println("📤 请求数据: " + jsonData.substring(0, Math.min(jsonData.length(), 200)) + "...");
                
                // 打印所有请求头（调试用）
                System.out.println("📤 请求头详情:");
                for (org.apache.http.Header header : httpPost.getAllHeaders()) {
                    System.out.println("   " + header.getName() + ": " + header.getValue());
                }
                
                httpPost.setEntity(new StringEntity(jsonData, StandardCharsets.UTF_8));
            }
            
            // 6. 发送请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("📥 响应状态码: " + statusCode);
                
                // 7. 获取LogID（对应Go SDK的logID处理）
                String logID = response.getFirstHeader("x-tt-logid") != null ? 
                    response.getFirstHeader("x-tt-logid").getValue() : "";
                System.out.println("📥 LogID: " + logID);
                
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    System.out.println("📥 响应内容: " + responseBody);
                    
                    // 8. 检查HTTP状态码（对应Go SDK的状态码检查）
                    if (statusCode >= 400) {
                        // 尝试解析OAuth错误（对应Go SDK的checkOAuthError）
                        try {
                            if (responseBody.contains("error_code") || responseBody.contains("errorCode")) {
                                System.err.println("❌ OAuth认证失败: " + responseBody);
                                throw new RuntimeException("OAuth authentication failed: " + responseBody);
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                        
                        throw new RuntimeException("HTTP request failed with status " + statusCode + ": " + responseBody);
                    }
                    
                    // 9. 处理响应（对应Go SDK的parseResponse）
                    if (responseType == String.class) {
                        return responseType.cast(responseBody);
                    }
                    
                    // 10. 解析JSON响应
                    if (!responseBody.trim().isEmpty()) {
                        T result = objectMapper.readValue(responseBody, responseType);
                        
                        // 设置LogID（对应Go SDK的resp.SetLogID）
                        if (result instanceof BaseResponse) {
                            ((BaseResponse) result).setLogID(logID);
                        }
                        
                        return result;
                    }
                }
                
                // 11. 创建默认响应实例
                T result = responseType.getDeclaredConstructor().newInstance();
                if (result instanceof BaseResponse) {
                    ((BaseResponse) result).setLogID(logID);
                }
                return result;
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
