package com.coze.cozeloop.trace.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 默认队列管理器实现，对应Go代码中的QueueManager
 * 使用Java的BlockingQueue来实现线程安全的队列
 */
public class DefaultQueueManager<T> implements QueueManager<T> {
    
    private final BlockingQueue<T> queue;
    private final int maxSize;
    private final int batchSize;
    private volatile boolean isClosed = false;
    
    /**
     * 构造函数
     */
    public DefaultQueueManager(int maxSize, int batchSize) {
        this.maxSize = maxSize;
        this.batchSize = batchSize;
        this.queue = new LinkedBlockingQueue<>(maxSize);
    }
    
    @Override
    public boolean enqueue(T item) {
        if (isClosed || item == null) {
            return false;
        }
        
        try {
            // 如果队列满了，尝试添加，超时时间为100ms
            return queue.offer(item, 100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    @Override
    public List<T> dequeueBatch() {
        if (isClosed) {
            return new ArrayList<>();
        }
        
        List<T> batch = new ArrayList<>();
        try {
            // 尝试获取一个项目，超时时间为100ms
            T first = queue.poll(100, TimeUnit.MILLISECONDS);
            if (first != null) {
                batch.add(first);
                
                // 继续获取剩余项目，直到达到batchSize或队列为空
                queue.drainTo(batch, batchSize - 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return batch;
    }
    
    @Override
    public int size() {
        return queue.size();
    }
    
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    @Override
    public void close() {
        isClosed = true;
        // 清空队列
        queue.clear();
    }
    
    /**
     * 检查是否已关闭
     */
    public boolean isClosed() {
        return isClosed;
    }
    
    /**
     * 获取最大队列大小
     */
    public int getMaxSize() {
        return maxSize;
    }
    
    /**
     * 获取批处理大小
     */
    public int getBatchSize() {
        return batchSize;
    }
}
