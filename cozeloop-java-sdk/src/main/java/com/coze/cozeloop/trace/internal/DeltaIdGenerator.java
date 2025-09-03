package com.coze.cozeloop.trace.internal;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

/**
 * 增量ID生成器，对应Go SDK的deltaIdGenerator结构
 * 生成递增的ID
 */
public class DeltaIdGenerator implements IdGenerator {
    
    private final LongSupplier randomNumber;
    private final AtomicLong seed;
    private final long maxId;
    private final long delta;
    
    /**
     * 构造函数
     * @param reseedThreshold 重新播种阈值
     * @param delta 增量值
     */
    public DeltaIdGenerator(long reseedThreshold, long delta) {
        // 对应Go SDK的newAccumulateIdGenerator
        SecureRandom secureRandom = new SecureRandom();
        this.randomNumber = () -> {
            // 对应Go SDK的randFunc
            return Math.abs(secureRandom.nextLong()) % Math.min(Long.MAX_VALUE, reseedThreshold);
        };
        
        this.seed = new AtomicLong(this.randomNumber.getAsLong());
        this.maxId = reseedThreshold;
        this.delta = delta;
    }
    
    @Override
    public long genId() {
        // 对应Go SDK的GenId()方法
        long id = addAndGet();
        if (id >= maxId) {
            resetSeed();
            return genId();
        }
        return id;
    }
    
    /**
     * 增加并获取，对应Go SDK的addAndGet()方法
     */
    private long addAndGet() {
        return seed.addAndGet(delta);
    }
    
    /**
     * 重置种子，对应Go SDK的resetSeed()方法
     */
    private void resetSeed() {
        seed.set(randomNumber.getAsLong());
    }
}
