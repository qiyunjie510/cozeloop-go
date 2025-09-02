package com.coze.cozeloop.trace.entity;

/**
 * 上传文件实体类，对应Go代码中的UploadFile结构体
 * 用于向CozeLoop平台上传文件数据
 */
public class UploadFile {
    
    private String tosKey;        // 对象存储键
    private String data;          // 文件数据
    private UploadType uploadType; // 上传类型
    private String tagKey;        // 标签键
    private String name;          // 文件名
    private String fileType;      // 文件类型
    private String spaceID;       // 空间ID
    
    // 构造函数
    public UploadFile() {}
    
    // Getters and Setters
    public String getTosKey() { return tosKey; }
    public void setTosKey(String tosKey) { this.tosKey = tosKey; }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public UploadType getUploadType() { return uploadType; }
    public void setUploadType(UploadType uploadType) { this.uploadType = uploadType; }
    
    public String getTagKey() { return tagKey; }
    public void setTagKey(String tagKey) { this.tagKey = tagKey; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public String getSpaceID() { return spaceID; }
    public void setSpaceID(String spaceID) { this.spaceID = spaceID; }
    
    /**
     * 上传类型枚举，对应Go代码中的UploadType
     */
    public enum UploadType {
        LONG(1),           // 长文本
        MULTI_MODALITY(2); // 多模态
        
        private final int value;
        
        UploadType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public static UploadType fromValue(int value) {
            for (UploadType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown UploadType value: " + value);
        }
    }
}
