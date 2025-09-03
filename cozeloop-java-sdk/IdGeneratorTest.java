import com.coze.cozeloop.trace.internal.IdGen;
import com.coze.cozeloop.trace.internal.IdGenerator;
import com.coze.cozeloop.trace.internal.MultipleDeltaIdGenerator;
import com.coze.cozeloop.trace.internal.DeltaIdGenerator;

/**
 * ID生成器测试类
 */
public class IdGeneratorTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 测试ID生成器...");
        
        // 测试1: 基本ID生成
        System.out.println("\n📋 测试1: 基本ID生成");
        try {
            IdGenerator generator = IdGen.getMultipleDeltaIdGenerator();
            System.out.println("✅ 获取ID生成器成功");
            
            for (int i = 0; i < 5; i++) {
                long id = generator.genId();
                System.out.println("   生成ID " + (i + 1) + ": " + id);
            }
        } catch (Exception e) {
            System.err.println("❌ 基本ID生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 测试2: 十六进制ID生成
        System.out.println("\n📋 测试2: 十六进制ID生成");
        try {
            for (int i = 0; i < 3; i++) {
                String spanId = IdGen.nextHexId();
                String traceId = IdGen.nextHexTraceId();
                System.out.println("   Span ID " + (i + 1) + ": " + spanId + " (长度: " + spanId.length() + ")");
                System.out.println("   Trace ID " + (i + 1) + ": " + traceId + " (长度: " + traceId.length() + ")");
            }
        } catch (Exception e) {
            System.err.println("❌ 十六进制ID生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 测试3: 直接创建生成器
        System.out.println("\n📋 测试3: 直接创建生成器");
        try {
            DeltaIdGenerator deltaGen = new DeltaIdGenerator(1000L, 1L);
            MultipleDeltaIdGenerator multiGen = new MultipleDeltaIdGenerator(1000L, 1L, 3L);
            
            System.out.println("   Delta生成器测试:");
            for (int i = 0; i < 3; i++) {
                long id = deltaGen.genId();
                System.out.println("     ID " + (i + 1) + ": " + id);
            }
            
            System.out.println("   多重生成器测试:");
            for (int i = 0; i < 3; i++) {
                long id = multiGen.genId();
                System.out.println("     ID " + (i + 1) + ": " + id);
            }
        } catch (Exception e) {
            System.err.println("❌ 直接创建生成器失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n🎉 ID生成器测试完成！");
    }
}
