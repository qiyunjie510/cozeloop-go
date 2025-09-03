package com.coze.cozeloop.trace.internal;

/**
 * ID生成器接口，对应Go SDK的IDGenerator接口
 */
public interface IdGenerator {
    
    /**
     * 生成ID，对应Go SDK的GenId()方法
     * @return 生成的ID
     */
    long genId();
}
