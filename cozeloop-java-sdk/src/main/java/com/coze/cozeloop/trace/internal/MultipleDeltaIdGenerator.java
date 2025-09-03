package com.coze.cozeloop.trace.internal;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;

/**
 * 多重增量ID生成器，对应Go SDK的multiDeltaIdGenerator结构
 * 使用多个ID生成器来生成ID
 */
public class MultipleDeltaIdGenerator implements IdGenerator {
    
    private final List<IdGenerator> idGenerators;
    private final AtomicLong index;
    private final long num;
    
    /**
     * 构造函数
     * @param reseedThreshold 重新播种阈值
     * @param delta 增量值
     * @param num 生成器数量
     */
    public MultipleDeltaIdGenerator(long reseedThreshold, long delta, long num) {
        // 对应Go SDK的newMultipleDeltaIdGenerator
        this.idGenerators = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            idGenerators.add(new DeltaIdGenerator(reseedThreshold, delta));
        }
        
        this.index = new AtomicLong(0);
        this.num = num;
    }
    
    @Override
    public long genId() {
        // 对应Go SDK的GenId()方法
        return idGenerators.get((int) getIndex()).genId();
    }
    
    /**
     * 获取索引，对应Go SDK的getIndex()方法
     */
    private long getIndex() {
        long id = index.incrementAndGet();
        return id % num;
    }
}
