package com.coze.cozeloop;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.SpanOptions;
import com.coze.cozeloop.trace.TraceOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * 主测试类，用于测试Java SDK的基本功能
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("🚀 启动CozeLoop Java SDK测试...");
        
        // 设置系统属性（对应Go SDK的环境变量）
        System.setProperty("COZELOOP_WORKSPACE_ID", "7534944671286558755");
        System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
        System.setProperty("COZELOOP_API_BASE_URL", "https://api.coze.cn");
        
        try {
            testSpanCreation();
            System.out.println("\n🎉 所有测试完成！");
            
        } catch (Exception e) {
            System.err.println("❌ 测试过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试完整的Span创建流程 - 仿照Go SDK的examples/trace/simple/simple.go
     */
    private static void testSpanCreation() {
        System.out.println("\n📋完整的Span创建流程（仿照Go SDK）");
        
        try {
            // 设置环境变量（仿照Go SDK）
            System.setProperty("COZELOOP_WORKSPACE_ID", "7534944671286558755");
            System.setProperty("COZELOOP_API_TOKEN", "pat_8sGW3PZB9ON8jKfWHKXGuBdv5kRq1aX9Dha3xK7bTHEXY68VJf1koIlwtfrrys9t");
            
            // 使用Go SDK风格的客户端创建方法
            CozeLoopClient client = CozeLoop.newClient();
            
            // 1. 创建根Span（仿照Go SDK的root_span）
            Span rootSpan = client.startSpan("root_span", "main_span");
            
            // 2. 设置自定义标签（仿照Go SDK的SetTags）
            Map<String, Object> customTags = new HashMap<>();
            customTags.put("mode", "simple");
            customTags.put("node_id", 6076665);
            customTags.put("node_process_duration", 228.6);
            customTags.put("is_first_node", true);
            rootSpan.setTags(customTags);
            
            // 3. 设置自定义Baggage（仿照Go SDK的SetBaggage）
            rootSpan.setBaggage("product_id", "123456654321");  // 假设product_id是全局字段
            rootSpan.setUserIDBaggage("123456");                // 设置用户ID（仿照Go SDK的SetUserIDBaggage）
            
            System.out.println("   ✅ 根Span创建成功:");
            System.out.println("     - SpanID: " + rootSpan.getSpanID());
            System.out.println("     - TraceID: " + rootSpan.getTraceID());
            System.out.println("     - Name: " + rootSpan.getSpanName());
            System.out.println("     - Type: " + rootSpan.getSpanType());
            System.out.println("     - WorkspaceID: " + rootSpan.getSpaceID());
            
            // 4. 模拟LLM调用（仿照Go SDK的llmCall方法）
            try {
                simulateLLMCall(client, rootSpan);
            } catch (Exception e) {
                // 设置错误状态码和错误信息（仿照Go SDK的错误处理）
                rootSpan.setStatusCode(600789111);  // 使用Go SDK中的错误码
                rootSpan.setError(e);
                System.out.println("   ⚠️  LLM调用失败，已设置错误状态: " + e.getMessage());
            }
            
            // 5. 完成根Span
            rootSpan.finish();
            System.out.println("   ✅ 根Span完成，持续时间: " + rootSpan.getDuration() + " 微秒");
            
            // 6. 测试Header转换
            Map<String, String> headers = rootSpan.toHeader();
            System.out.println("   ✅ Header转换成功:");
            headers.forEach((key, value) -> System.out.println("     " + key + ": " + value));
            
            // 7. 强制刷新队列（仿照Go SDK的Flush）
            System.out.println("   🔄 强制刷新队列...");
            client.flush();
            System.out.println("   ✅ 队列刷新完成");
            
            // 8. 关闭CozeLoop（可选，仿照Go SDK的Close）
            System.out.println("   🔄 关闭CozeLoop...");
            client.close();
            System.out.println("   ✅ CozeLoop关闭完成");
            
        } catch (Exception e) {
            System.out.println("   ❌ Span创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 模拟LLM调用（仿照Go SDK的llmCall方法）
     */
    private static void simulateLLMCall(CozeLoopClient client, Span parentSpan) throws Exception {
        System.out.println("   🔄 开始模拟LLM调用...");
        
        // 创建子Span（仿照Go SDK的llmCall span）
        SpanOptions childOptions = new SpanOptions();
        childOptions.setParentSpanID(parentSpan.getSpanID());
        childOptions.setTraceID(parentSpan.getTraceID());
        
        Span llmSpan = client.startSpan("llmCall", "v_model_span", childOptions);
        
        try {
            // 模拟LLM处理时间
            Thread.sleep(1000);  // 1秒，仿照Go SDK的time.Sleep(1 * time.Second)
            
            // 模拟LLM响应数据
            String input = "上海天气怎么样？";
            String output = "上海天气晴朗，气温25摄氏度。";
            String modelName = "gpt-4o-2024-05-13";
            int inputTokens = 11;
            int outputTokens = 52;
            
            // 设置LLM相关的标签（仿照Go SDK）
            llmSpan.setInput(input);                    // 设置输入
            llmSpan.setOutput(output);                  // 设置输出
            llmSpan.setModelProvider("openai");         // 设置模型提供商
            llmSpan.setModelName(modelName);            // 设置模型名称
            llmSpan.setInputTokens(inputTokens);        // 设置输入token数
            llmSpan.setOutputTokens(outputTokens);      // 设置输出token数
            
            // 设置首次响应时间（仿照Go SDK的SetStartTimeFirstResp）
            long firstRespTime = System.currentTimeMillis() * 1000; // 转换为微秒
            llmSpan.setStartTimeFirstResp(firstRespTime);
            
            System.out.println("   ✅ LLM调用成功:");
            System.out.println("     - 输入: " + input);
            System.out.println("     - 输出: " + output);
            System.out.println("     - 模型: " + modelName);
            System.out.println("     - 输入Token: " + inputTokens);
            System.out.println("     - 输出Token: " + outputTokens);
            
        } finally {
            // 完成LLM Span
            llmSpan.finish();
            System.out.println("   ✅ LLM Span完成，持续时间: " + llmSpan.getDuration() + " 微秒");
        }
    }
}
