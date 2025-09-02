package com.coze.cozeloop.trace.constants;

/**
 * Trace相关常量类，对应Go代码中的consts包
 */
public class TraceConstants {
    
    // 默认API基础URL
    public static final String DEFAULT_API_BASE_URL = "https://api.coze.cn";
    
    // W3C Trace Context标准Header
    public static final String TRACE_CONTEXT_HEADER_PARENT = "X-Cozeloop-Traceparent";
    public static final String TRACE_CONTEXT_HEADER_BAGGAGE = "X-Cozeloop-Tracestate";
    
    // 队列配置常量
    public static final int MAX_QUEUE_SIZE = 10000;
    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final long DEFAULT_FLUSH_INTERVAL_MS = 5000;
    
    // 标签配置常量
    public static final int MAX_TAG_KV_COUNT_IN_ONE_SPAN = 100;
    public static final int MAX_BYTES_OF_ONE_TAG_VALUE_DEFAULT = 1024;
    public static final int MAX_BYTES_OF_ONE_TAG_VALUE_OF_INPUT_OUTPUT = 8192;
    
    // 文件类型常量
    public static final String FILE_TYPE_TEXT = "text";
    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_FILE = "file";
    
    // 特殊标签键
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";
    public static final String ERROR = "error";
    public static final String INPUT_TOKENS = "input_tokens";
    public static final String OUTPUT_TOKENS = "output_tokens";
    public static final String TOKENS = "tokens";
    public static final String CUT_OFF = "cut_off";
    
    // 运行时相关常量
    public static final String RUNTIME = "runtime";
    public static final String SCENE = "scene";
    
    // 私有构造函数，防止实例化
    private TraceConstants() {}
}
