package com.coze.cozeloop.trace.entity;

/**
 * BaseResponse类，对应Go SDK中的BaseResponse结构体
 * 用于处理API响应的标准格式
 */
public class BaseResponse {
    
    private int code;
    private String msg;
    private Object detail;
    
    /**
     * 默认构造函数
     */
    public BaseResponse() {}

    
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
    public Object getDetail() {
        return detail;
    }
    
    /**
     * 设置日志ID
     */
    public void setDetail(Object detail) {
        this.detail = detail;
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
                ", detail='" + detail.toString() + '\'' +
                '}';
    }
}

