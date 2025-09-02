package com.coze.cozeloop.trace.entity;

/**
 * BaseResponse类，对应Go SDK中的BaseResponse结构体
 * 用于处理API响应的标准格式
 */
public class BaseResponse {
    
    private int code;
    private String msg;
    private String logID;
    
    /**
     * 默认构造函数
     */
    public BaseResponse() {}
    
    /**
     * 构造函数
     */
    public BaseResponse(int code, String msg, String logID) {
        this.code = code;
        this.msg = msg;
        this.logID = logID;
    }
    
    /**
     * 获取响应码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 设置响应码
     */
    public void setCode(int code) {
        this.code = code;
    }
    
    /**
     * 获取响应消息
     */
    public String getMsg() {
        return msg;
    }
    
    /**
     * 设置响应消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    /**
     * 获取日志ID
     */
    public String getLogID() {
        return logID;
    }
    
    /**
     * 设置日志ID
     */
    public void setLogID(String logID) {
        this.logID = logID;
    }
    
    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return code == 0;
    }
    
    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", logID='" + logID + '\'' +
                '}';
    }
}

