package com.coze.cozeloop.trace.internal;

import com.coze.cozeloop.trace.Span;
import com.coze.cozeloop.trace.TraceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 批量Span处理器，对应Go代码中的BatchSpanProcessor
 * 负责批量处理Span数据
 */
public class BatchSpanProcessor implements SpanProcessor {
    
    private final TraceOptions options;
    private final QueueManager<Span> spanQueue;
    private final QueueManager<Object> fileQueue;
    private final Exporter exporter;
    private final ScheduledExecutorService scheduler;
    
    /**
     * 构造函数
     */
    public BatchSpanProcessor(TraceOptions options, Exporter exporter) {
        this.options = options;
        this.exporter = exporter;
        this.spanQueue = createSpanQueue();
        this.fileQueue = createFileQueue();
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        // 启动定时刷新任务
        startScheduledFlush();
    }
    
    @Override
    public void onSpanEnd(Span span) {
        if (span != null && spanQueue != null) {
            // 将Span加入队列
            boolean success = spanQueue.enqueue(span);
            if (!success) {
                // 如果队列满了，记录警告
                System.err.println("Warning: Span queue is full, dropping span: " + span.getSpanID());
            }
        }
    }
    
    @Override
    public void flush() {
        // 处理Span队列
        if (spanQueue != null && !spanQueue.isEmpty()) {
            List<Span> spans = spanQueue.dequeueBatch();
            if (!spans.isEmpty()) {
                try {
                    // 转换为Object列表
                    List<Object> spanObjects = new ArrayList<>(spans);
                    exporter.exportSpans(spanObjects);
                } catch (Exception e) {
                    System.err.println("Failed to export spans: " + e.getMessage());
                }
            }
        }
        
        // 处理文件队列
        if (fileQueue != null && !fileQueue.isEmpty()) {
            List<Object> files = fileQueue.dequeueBatch();
            if (!files.isEmpty()) {
                try {
                    exporter.exportFiles(files);
                } catch (Exception e) {
                    System.err.println("Failed to export files: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public void shutdown() {
        // 停止定时任务
        scheduler.shutdown();
        
        // 强制刷新
        flush();
        
        // 关闭队列
        if (spanQueue != null) {
            spanQueue.close();
        }
        if (fileQueue != null) {
            fileQueue.close();
        }
        
        // 关闭导出器
        if (exporter != null) {
            exporter.close();
        }
    }
    
    /**
     * 创建Span队列
     */
    private QueueManager<Span> createSpanQueue() {
        return new DefaultQueueManager<>(
            options.getMaxQueueSize(), 
            options.getBatchSize()
        );
    }
    
    /**
     * 创建文件队列
     */
    private QueueManager<Object> createFileQueue() {
        return new DefaultQueueManager<>(
            options.getMaxQueueSize(), 
            options.getBatchSize()
        );
    }
    
    /**
     * 启动定时刷新任务
     */
    private void startScheduledFlush() {
        scheduler.scheduleAtFixedRate(this::flush, 
            options.getFlushIntervalMs(), 
            options.getFlushIntervalMs(), 
            TimeUnit.MILLISECONDS);
    }
}
