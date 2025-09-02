package com.coze.cozeloop;

import com.coze.cozeloop.trace.http.DefaultHttpClient;
import com.coze.cozeloop.trace.entity.BaseResponse;

/**
 * HTTP客户端测试类
 */
public class HttpTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 测试HTTP客户端...");
        
        // 设置系统属性
        System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
        
        try {
            // 创建HTTP客户端
            DefaultHttpClient httpClient = new DefaultHttpClient("https://api.coze.cn");
            
            // 测试1: 发送空数据
            System.out.println("\n📋 测试1: 发送空数据");
            try {
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", "{\"spans\":[]}", BaseResponse.class);
                System.out.println("✅ 响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ 测试1失败: " + e.getMessage());
            }
            
            // 测试2: 发送测试数据
            System.out.println("\n📋 测试2: 发送测试数据");
            try {
                String testData = "{\"spans\":[{\"span_id\":\"test123\",\"trace_id\":\"trace123\",\"span_name\":\"test_span\"}]}";
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", testData, BaseResponse.class);
                System.out.println("✅ 响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ 测试2失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ HTTP客户端创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
