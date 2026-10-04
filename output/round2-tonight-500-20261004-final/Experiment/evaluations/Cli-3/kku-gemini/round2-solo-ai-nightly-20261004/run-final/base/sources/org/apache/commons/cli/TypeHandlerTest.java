package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() {
        Object result = TypeHandler.createValue("hello", PatternOptionBuilder.STRING_VALUE);
        assertEquals("hello", result);
    }

    @Test
    public void testCreateValueWithClass() {
        Object result = TypeHandler.createValue("java.util.Date", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(java.util.Date.class, result);
    }

    @Test
    public void testCreateValueWithUnknownType() {
        Object result = TypeHandler.createValue("test", Object.class);
        assertNull(result);
    }

    @Test
    public void testCreateObjectSuccess() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testCreateObjectNotFound() {
        Object result = TypeHandler.createObject("non.existent.ClassName");
        assertNull(result);
    }

    @Test
    public void testCreateNumberLong() {
        Number result = TypeHandler.createNumber("12345");
        assertNotNull(result);
        assertTrue(result instanceof Long);
        assertEquals(12345L, result.longValue());
    }

    @Test
    public void testCreateNumberDouble() {
        Number result = TypeHandler.createNumber("123.45");
        assertNotNull(result);
        assertTrue(result instanceof Double);
        assertEquals(123.45, result.doubleValue(), 0.0001);
    }

    @Test
    public void testCreateClassSuccess() {
        Class<?> clazz = TypeHandler.createClass("java.util.ArrayList");
        assertEquals(java.util.ArrayList.class, clazz);
    }

    @Test
    public void testCreateClassFailure() {
        Class<?> clazz = TypeHandler.createClass("non.existent.Class");
        assertNull(clazz);
    }

    @Test
    public void testCreateURLSuccess() throws Exception {
        URL url = TypeHandler.createURL("http://localhost/");
        assertNotNull(url);
        assertEquals("http", url.getProtocol());
    }

    @Test
    public void testCreateURLFailure() {
        URL url = TypeHandler.createURL("invalid-url");
        assertNull(url);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test-file.txt");
        assertNotNull(file);
        assertEquals("test-file.txt", file.getName());
    }
}
