package com.coze.cozeloop.trace;

import com.coze.cozeloop.trace.internal.DefaultSpan;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Span接口的测试类
 */
public class SpanTest {
    
    private DefaultSpan span;
    
    @Before
    public void setUp() {
        span = new DefaultSpan("test-span", "test-type", "test-workspace", "0", null);
    }
    
    @After
    public void tearDown() {
        if (span != null) {
            span.finish();
        }
    }
    
    @Test
    public void testBasicProperties() {
        // 测试基本属性
        assertEquals("test-span", span.getSpanName());
        assertEquals("test-type", span.getSpanType());
        assertEquals("test-workspace", span.getSpaceID());
        assertTrue(span.isRootSpan());
        assertNotNull(span.getSpanID());
        assertNotNull(span.getTraceID());
    }
    
    @Test
    public void testSetAndGetTags() {
        // 测试标签设置和获取
        Map<String, Object> tags = new HashMap<>();
        tags.put("key1", "value1");
        tags.put("key2", 123);
        
        span.setTags(tags);
        span.setTag("key3", true);
        
        Map<String, Object> retrievedTags = span.getTagMap();
        assertEquals("value1", retrievedTags.get("key1"));
        assertEquals(123, retrievedTags.get("key2"));
        assertEquals(true, retrievedTags.get("key3"));
    }
    
    @Test
    public void testSetAndGetBaggage() {
        // 测试Baggage设置和获取
        span.setBaggage("user-id", "user-123");
        span.setBaggage("request-id", "req-456");
        
        Map<String, String> baggage = span.getBaggage();
        assertEquals("user-123", baggage.get("user-id"));
        assertEquals("req-456", baggage.get("request-id"));
    }
    
    @Test
    public void testInputOutput() {
        // 测试输入输出设置
        span.setInput("test input");
        span.setOutput("test output");
        
        Map<String, Object> tags = span.getTagMap();
        assertEquals("test input", tags.get("input"));
        assertEquals("test output", tags.get("output"));
    }
    
    @Test
    public void testFinish() {
        // 测试Span完成
        assertFalse(span.getDuration() > 0);
        
        span.finish();
        
        assertTrue(span.getDuration() > 0);
    }
    
    @Test
    public void testToHeader() {
        // 测试转换为HTTP Header
        span.setBaggage("user-id", "user-123");
        
        Map<String, String> headers = span.toHeader();
        
        assertNotNull(headers.get("X-Cozeloop-Traceparent"));
        assertNotNull(headers.get("X-Cozeloop-Tracestate"));
        assertTrue(headers.get("X-Cozeloop-Tracestate").contains("user-id=user-123"));
    }
}
