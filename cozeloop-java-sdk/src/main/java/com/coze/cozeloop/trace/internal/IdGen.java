package com.coze.cozeloop.trace.internal;

/**
 * ID生成器工具类，对应Go SDK的idgen包
 * 提供全局ID生成器访问
 */
public class IdGen {
    
    private static volatile IdGenerator idGenerator;
    private static final Object lock = new Object();
    
    /**
     * 获取多重增量ID生成器，对应Go SDK的GetMultipleDeltaIdGenerator()
     * @return ID生成器实例
     */
    public static IdGenerator getMultipleDeltaIdGenerator() {
        if (idGenerator == null) {
            synchronized (lock) {
                if (idGenerator == null) {
                    // 对应Go SDK的newMultipleDeltaIdGenerator(math.MaxInt64, 1, 10)
                    idGenerator = new MultipleDeltaIdGenerator(Long.MAX_VALUE, 1L, 10L);
                }
            }
        }
        return idGenerator;
    }
    
    /**
     * 生成下一个ID，对应Go SDK的idGenerator.GenId()
     * @return 生成的ID
     */
    public static long nextId() {
        return getMultipleDeltaIdGenerator().genId();
    }
    
    /**
     * 生成16位十六进制字符串ID，用于Span ID
     * 对应Go SDK的Gen16CharID()方法
     * @return 16位十六进制字符串
     */
    public static String nextHexId() {
        // 对应Go SDK的Gen16CharID()逻辑：
        // rand := idgen.GetMultipleDeltaIdGenerator().GenId()
        // return fmt.Sprintf("%016x", rand&math.MaxInt64)
        long rand = nextId() & Long.MAX_VALUE;
        return String.format("%016x", rand);
    }
    
    /**
     * 生成32位十六进制字符串ID，用于Trace ID
     * 对应Go SDK的Gen32CharID()方法
     * @return 32位十六进制字符串
     */
    public static String nextHexTraceId() {
        // 对应Go SDK的Gen32CharID()逻辑：
        // high := uint64(time.Now().Unix()) + idgen.GetMultipleDeltaIdGenerator().GenId()
        // high = high & math.MaxInt64
        // low := idgen.GetMultipleDeltaIdGenerator().GenId() & math.MaxInt64
        // return fmt.Sprintf("%016x%016x", high, low)
        
        long currentTimeSeconds = System.currentTimeMillis() / 1000; // 对应time.Now().Unix()
        long high = (currentTimeSeconds + nextId()) & Long.MAX_VALUE; // 对应high & math.MaxInt64
        long low = nextId() & Long.MAX_VALUE; // 对应low & math.MaxInt64
        
        return String.format("%016x%016x", high, low);
    }
}
