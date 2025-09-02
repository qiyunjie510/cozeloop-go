import com.coze.cozeloop.trace.http.DefaultHttpClient;
import com.coze.cozeloop.trace.entity.BaseResponse;
import com.coze.cozeloop.trace.entity.UploadSpan;
import com.coze.cozeloop.trace.entity.UploadSpanData;
import java.util.ArrayList;
import java.util.List;

/**
 * 详细的HTTP测试类，直接对比Go SDK的请求格式
 */
public class DetailedHttpTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 详细测试HTTP客户端...");
        
        // 设置系统属性（对应Go SDK的环境变量）
        System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
        
        try {
            // 创建HTTP客户端
            DefaultHttpClient httpClient = new DefaultHttpClient("https://api.coze.cn");
            
            // 测试1: 发送空数据（对应Go SDK的UploadSpanData{ss}）
            System.out.println("\n📋 测试1: 发送空数据");
            try {
                UploadSpanData emptyData = new UploadSpanData();
                emptyData.setSpans(new ArrayList<>());
                
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", emptyData, BaseResponse.class);
                System.out.println("✅ 空数据响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ 空数据测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 测试2: 发送测试Span数据（对应Go SDK的完整Span结构）
            System.out.println("\n📋 测试2: 发送测试Span数据");
            try {
                UploadSpan testSpan = new UploadSpan();
                testSpan.setSpanID("test_span_123");
                testSpan.setTraceID("test_trace_456");
                testSpan.setSpanName("test_span");
                testSpan.setSpanType("test_type");
                testSpan.setWorkspaceID("7534944671286558755");
                testSpan.setServiceName("java-test-service");
                testSpan.setStartedATMicros(System.currentTimeMillis() * 1000); // 微秒
                testSpan.setDurationMicros(1000000); // 1秒
                testSpan.setStatusCode(0);
                
                // 设置标签（对应Go SDK的tags）
                testSpan.setTagsString(new java.util.HashMap<>());
                testSpan.getTagsString().put("test_key", "test_value");
                testSpan.getTagsString().put("mode", "java_test");
                
                List<UploadSpan> spans = new ArrayList<>();
                spans.add(testSpan);
                
                UploadSpanData spanData = new UploadSpanData();
                spanData.setSpans(spans);
                
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", spanData, BaseResponse.class);
                System.out.println("✅ Span数据响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ Span数据测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 测试3: 直接发送JSON字符串（对比测试）
            System.out.println("\n📋 测试3: 直接发送JSON字符串");
            try {
                String jsonData = "{\"spans\":[{\"span_id\":\"json_test_123\",\"trace_id\":\"json_trace_456\",\"span_name\":\"json_test_span\",\"span_type\":\"json_test_type\",\"workspace_id\":\"7534944671286558755\",\"service_name\":\"java-json-test\",\"started_at_micros\":" + (System.currentTimeMillis() * 1000) + ",\"duration_micros\":1000000,\"status_code\":0,\"tags_string\":{\"test_key\":\"json_test_value\",\"mode\":\"json_test\"}}]}";
                
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", jsonData, BaseResponse.class);
                System.out.println("✅ JSON字符串响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ JSON字符串测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("❌ HTTP客户端创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
