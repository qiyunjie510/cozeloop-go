package com.coze.cozeloop.trace.log;

/**
 * 日志接口，对应Go代码中的logger包
 */
public interface Logger {
    
    /**
     * 调试级别日志
     */
    void debug(String message);
    
    /**
     * 调试级别日志（带参数）
     */
    void debug(String format, Object... args);
    
    /**
     * 信息级别日志
     */
    void info(String message);
    
    /**
     * 信息级别日志（带参数）
     */
    void info(String format, Object... args);
    
    /**
     * 警告级别日志
     */
    void warn(String message);
    
    /**
     * 警告级别日志（带参数）
     */
    void warn(String format, Object... args);
    
    /**
     * 错误级别日志
     */
    void error(String message);
    
    /**
     * 错误级别日志（带参数）
     */
    void error(String format, Object... args);
    
    /**
     * 错误级别日志（带异常）
     */
    void error(String message, Throwable throwable);
}
