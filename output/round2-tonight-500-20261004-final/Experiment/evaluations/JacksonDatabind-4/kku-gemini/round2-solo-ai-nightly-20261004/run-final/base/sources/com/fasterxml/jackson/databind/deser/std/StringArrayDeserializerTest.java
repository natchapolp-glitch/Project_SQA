package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;

public class StringArrayDeserializerTest {

    @Test
    public void testInstanceNotNull() {
        assertNotNull(StringArrayDeserializer.instance);
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        jp.nextToken();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeStringArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser("[\"hello\", \"world\", null]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        jp.nextToken();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { "hello", "world", null }, result);
    }

    @Test
    public void testDeserializeCustomDeserializerConstructor() {
        StringArrayDeserializer customDeser = new StringArrayDeserializer(null);
        assertNotNull(customDeser);
    }

    @Test(expected = Exception.class)
    public void testHandleNonArrayFailsByDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser("\"not-an-array\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        jp.nextToken();
        StringArrayDeserializer.instance.deserialize(jp, ctxt);
    }

    @Test
    public void testAcceptSingleValueAsArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser("\"single\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        jp.nextToken();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] { "single" }, result);
    }
}
