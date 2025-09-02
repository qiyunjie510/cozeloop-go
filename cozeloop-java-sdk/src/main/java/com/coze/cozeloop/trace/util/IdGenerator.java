package com.coze.cozeloop.trace.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * ID生成器工具类，对应Go代码中的util.Gen16CharID和util.Gen32CharID
 */
public class IdGenerator {
    
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    
    /**
     * 生成16字符的ID，对应Go代码中的util.Gen16CharID
     */
    public static String gen16CharID() {
        return generateRandomString(16);
    }
    
    /**
     * 生成32字符的ID，对应Go代码中的util.Gen32CharID
     */
    public static String gen32CharID() {
        return generateRandomString(32);
    }
    
    /**
     * 生成指定长度的随机字符串
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
    
    /**
     * 生成UUID（去掉横线）
     */
    public static String genUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
    
    private IdGenerator() {}
}
