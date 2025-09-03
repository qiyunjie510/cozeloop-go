import com.coze.cozeloop.trace.internal.IdGen;

/**
 * Trace ID生成测试类
 */
public class TraceIdTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 测试Trace ID生成逻辑...");
        
        // 测试1: 生成多个Trace ID
        System.out.println("\n📋 测试1: 生成多个Trace ID");
        try {
            for (int i = 0; i < 5; i++) {
                String traceId = IdGen.nextHexTraceId();
                System.out.println("   Trace ID " + (i + 1) + ": " + traceId + " (长度: " + traceId.length() + ")");
                
                // 验证长度是否为32位
                if (traceId.length() != 32) {
                    System.err.println("   ❌ Trace ID长度不正确，期望32位，实际" + traceId.length() + "位");
                } else {
                    System.out.println("   ✅ Trace ID长度正确");
                }
                
                // 验证是否为有效的十六进制字符串
                if (traceId.matches("[0-9a-f]{32}")) {
                    System.out.println("   ✅ Trace ID格式正确（十六进制）");
                } else {
                    System.err.println("   ❌ Trace ID格式不正确，不是有效的十六进制字符串");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Trace ID生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 测试2: 生成多个Span ID
        System.out.println("\n📋 测试2: 生成多个Span ID");
        try {
            for (int i = 0; i < 5; i++) {
                String spanId = IdGen.nextHexId();
                System.out.println("   Span ID " + (i + 1) + ": " + spanId + " (长度: " + spanId.length() + ")");
                
                // 验证长度是否为16位
                if (spanId.length() != 16) {
                    System.err.println("   ❌ Span ID长度不正确，期望16位，实际" + spanId.length() + "位");
                } else {
                    System.out.println("   ✅ Span ID长度正确");
                }
                
                // 验证是否为有效的十六进制字符串
                if (spanId.matches("[0-9a-f]{16}")) {
                    System.out.println("   ✅ Span ID格式正确（十六进制）");
                } else {
                    System.err.println("   ❌ Span ID格式不正确，不是有效的十六进制字符串");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Span ID生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 测试3: 对比Go SDK的逻辑
        System.out.println("\n📋 测试3: 对比Go SDK逻辑");
        try {
            long currentTimeSeconds = System.currentTimeMillis() / 1000;
            System.out.println("   当前时间戳（秒）: " + currentTimeSeconds);
            
            // 模拟Go SDK的Gen32CharID逻辑
            long high = (currentTimeSeconds + IdGen.nextId()) & Long.MAX_VALUE;
            long low = IdGen.nextId() & Long.MAX_VALUE;
            String expectedTraceId = String.format("%016x%016x", high, low);
            
            System.out.println("   模拟Go SDK生成的Trace ID: " + expectedTraceId);
            System.out.println("   Java版本生成的Trace ID: " + IdGen.nextHexTraceId());
            
            System.out.println("   ✅ 逻辑对比完成");
        } catch (Exception e) {
            System.err.println("❌ 逻辑对比失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n🎉 Trace ID生成测试完成！");
    }
}
