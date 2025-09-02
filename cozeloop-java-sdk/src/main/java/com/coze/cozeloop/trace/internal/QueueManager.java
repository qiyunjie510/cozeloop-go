package com.coze.cozeloop.trace.internal;

import java.util.List;

/**
 * 队列管理器接口，对应Go代码中的QueueManager接口
 * 负责管理Span和文件的队列
 */
public interface QueueManager<T> {
    
    /**
     * 将项目加入队列
     * 对应Go代码中的Enqueue方法
     */
    boolean enqueue(T item);
    
    /**
     * 批量获取项目
     * 对应Go代码中的DequeueBatch方法
     */
    List<T> dequeueBatch();
    
    /**
     * 获取队列大小
     * 对应Go代码中的Size方法
     */
    int size();
    
    /**
     * 检查队列是否为空
     * 对应Go代码中的IsEmpty方法
     */
    boolean isEmpty();
    
    /**
     * 关闭队列管理器
     * 对应Go代码中的Close方法
     */
    void close();
}
