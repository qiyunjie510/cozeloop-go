package com.coze.cozeloop.trace.internal;

import java.util.List;

/**
 * 数据导出器接口，对应Go代码中的Exporter接口
 * 负责将Span和文件数据导出到CozeLoop平台
 */
public interface Exporter {
    
    /**
     * 导出Span数据
     * 对应Go代码中的ExportSpans方法
     */
    void exportSpans(List<Object> spans);
    
    /**
     * 导出文件数据
     * 对应Go代码中的ExportFiles方法
     */
    void exportFiles(List<Object> files);
    
    /**
     * 关闭导出器
     * 对应Go代码中的Close方法
     */
    void close();
}
