import com.coze.cozeloop.trace.http.DefaultHttpClient;
import com.coze.cozeloop.trace.entity.BaseResponse;

/**
 * 简单的HTTP测试类
 */
public class SimpleHttpTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 测试HTTP客户端...");
        
        // 设置系统属性
        System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
        
        try {
            // 创建HTTP客户端
            DefaultHttpClient httpClient = new DefaultHttpClient("https://api.coze.cn");
            
            // 测试发送空数据
            System.out.println("\n📋 测试发送空数据到 /v1/loop/traces/ingest");
            try {
                BaseResponse response = httpClient.post("/v1/loop/traces/ingest", "{\"spans\":[]}", BaseResponse.class);
                System.out.println("✅ 响应: " + response);
            } catch (Exception e) {
                System.err.println("❌ 测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("❌ HTTP客户端创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

