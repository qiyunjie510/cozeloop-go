package com.coze.cozeloop;

import com.coze.cozeloop.trace.http.DefaultHttpClient;
import com.coze.cozeloop.trace.entity.BaseResponse;
import com.coze.cozeloop.trace.util.JsonUtils;

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
            //
            String data = "{\n" +
                    "  \"spans\": [\n" +
                    "    {\n" +
                    "      \"started_at_micros\": 1756812632979962,\n" +
                    "      \"log_id\": \"\",\n" +
                    "      \"span_id\": \"70c84876fba3d71e\",\n" +
                    "      \"parent_id\": \"5b7e468b44d5890a\",\n" +
                    "      \"trace_id\": \"2e669322083d71ef4770b0b85ddb9e9f\",\n" +
                    "      \"duration_micros\": 1000604,\n" +
                    "      \"service_name\": \"\",\n" +
                    "      \"workspace_id\": \"7534944671286558755\",\n" +
                    "      \"span_name\": \"llmCall\",\n" +
                    "      \"span_type\": \"model\",\n" +
                    "      \"status_code\": 0,\n" +
                    "      \"input\": \"我的屁股怎么这么大呢？\",\n" +
                    "      \"output\": \"[\\\"上海天气晴朗，气温25摄氏度。\\\"]\",\n" +
                    "      \"object_storage\": \"\",\n" +
                    "      \"system_tags_string\": {\n" +
                    "        \"runtime\": \"{\\\"language\\\":\\\"go\\\",\\\"scene\\\":\\\"custom\\\",\\\"loop_sdk_version\\\":\\\"v0.1.9\\\"}\"\n" +
                    "      },\n" +
                    "      \"system_tags_long\": {},\n" +
                    "      \"system_tags_double\": {},\n" +
                    "      \"tags_string\": {\n" +
                    "        \"model_name\": \"gpt-4o-2024-05-13\",\n" +
                    "        \"model_provider\": \"openai\",\n" +
                    "        \"product_id\": \"123456654321\",\n" +
                    "        \"user_id\": \"123456\"\n" +
                    "      },\n" +
                    "      \"tags_long\": {\n" +
                    "        \"input_tokens\": 11,\n" +
                    "        \"latency_first_resp\": 1000604,\n" +
                    "        \"output_tokens\": 52,\n" +
                    "        \"start_time_first_resp\": 1756812633980566,\n" +
                    "        \"tokens\": 63\n" +
                    "      },\n" +
                    "      \"tags_double\": {},\n" +
                    "      \"tags_bool\": {}\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"started_at_micros\": 1756812632979962,\n" +
                    "      \"log_id\": \"\",\n" +
                    "      \"span_id\": \"5b7e468b44d5890a\",\n" +
                    "      \"parent_id\": \"0\",\n" +
                    "      \"trace_id\": \"2e669322083d71ef4770b0b85ddb9e9f\",\n" +
                    "      \"duration_micros\": 1000604,\n" +
                    "      \"service_name\": \"\",\n" +
                    "      \"workspace_id\": \"7534944671286558755\",\n" +
                    "      \"span_name\": \"root_span\",\n" +
                    "      \"span_type\": \"main_span\",\n" +
                    "      \"status_code\": 0,\n" +
                    "      \"input\": \"\",\n" +
                    "      \"output\": \"\",\n" +
                    "      \"object_storage\": \"\",\n" +
                    "      \"system_tags_string\": {\n" +
                    "        \"runtime\": \"{\\\"language\\\":\\\"go\\\",\\\"scene\\\":\\\"custom\\\",\\\"loop_sdk_version\\\":\\\"v0.1.9\\\"}\"\n" +
                    "      },\n" +
                    "      \"system_tags_long\": {},\n" +
                    "      \"system_tags_double\": {},\n" +
                    "      \"tags_string\": {\n" +
                    "        \"mode\": \"simple\",\n" +
                    "        \"product_id\": \"123456654321\",\n" +
                    "        \"user_id\": \"123456\"\n" +
                    "      },\n" +
                    "      \"tags_long\": {\n" +
                    "        \"node_id\": 6076665\n" +
                    "      },\n" +
                    "      \"tags_double\": {\n" +
                    "        \"node_process_duration\": 228.6\n" +
                    "      },\n" +
                    "      \"tags_bool\": {\n" +
                    "        \"is_first_node\": true\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            // 解析成对象
            Object dataObj = JsonUtils.fromJson(data, Object.class);

            // 测试1: 发送空数据
            System.out.println("\n📋 测试1: 发送空数据");
            try {
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", dataObj, BaseResponse.class);
                System.out.println("✅ 响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ 测试1失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ HTTP客户端创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

