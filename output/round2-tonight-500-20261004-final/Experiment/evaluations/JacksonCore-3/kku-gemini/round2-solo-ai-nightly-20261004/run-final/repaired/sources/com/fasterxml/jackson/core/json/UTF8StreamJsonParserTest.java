package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class UTF8StreamJsonParserTest {

    private UTF8StreamJsonParser createParser(byte[] jsonBytes) {
        BufferRecycler recycler = new BufferRecycler();
        IOContext ctxt = new IOContext(recycler, recycler, true);
        InputStream in = new ByteArrayInputStream(jsonBytes);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        return new UTF8StreamJsonParser(ctxt, 0, in, null, symbols, jsonBytes, 0, jsonBytes.length, true);
    }

    @Test
    public void testGetInputSource() throws IOException {
        byte[] data = "{}".getBytes();
        UTF8StreamJsonParser parser = createParser(data);
        assertNotNull(parser.getInputSource());
        parser.close();
    }

    @Test
    public void testSetAndGetCodec() throws IOException {
        byte[] data = "{}".getBytes();
        UTF8StreamJsonParser parser = createParser(data);
        assertNull(parser.getCodec());
        parser.setCodec(null);
        assertNull(parser.getCodec());
        parser.close();
    }

    @Test
    public void testReleaseBuffered() throws IOException {
        byte[] data = "hello".getBytes();
        BufferRecycler recycler = new BufferRecycler();
        IOContext ctxt = new IOContext(recycler, recycler, true);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        UTF8StreamJsonParser parser = new UTF8StreamJsonParser(ctxt, 0, null, null, symbols, data, 0, data.length, false);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int released = parser.releaseBuffered(out);
        assertEquals(5, released);
        assertEquals("hello", out.toString("UTF-8"));
        parser.close();
    }

    @Test
    public void testNextTokenBasicObject() throws IOException {
        byte[] data = "{\"key\": 123}".getBytes();
        UTF8StreamJsonParser parser = createParser(data);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getText());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());

        parser.close();
    }

    @Test
    public void testGetTextCharactersAndLength() throws IOException {
        byte[] data = "[\"abc\"]".getBytes();
        UTF8StreamJsonParser parser = createParser(data);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("abc", parser.getText());
        assertEquals(3, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());
        char[] chars = parser.getTextCharacters();
        assertNotNull(chars);
        assertTrue(chars.length >= 3);

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testGetBinaryValueAndReadBinary() throws IOException {
        // Base64 for "abc" is "YWJj"
        byte[] data = "[\"YWJj\"]".getBytes();
        UTF8StreamJsonParser parser = createParser(data);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());

        byte[] binary = parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        assertNotNull(binary);
        assertEquals("abc", new String(binary, "UTF-8"));

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testLocations() throws IOException {
        byte[] data = "{\n  \"a\": 1\n}".getBytes();
        UTF8StreamJsonParser parser = createParser(data);

        assertNotNull(parser.getCurrentLocation());
        assertNotNull(parser.getTokenLocation());

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertNotNull(parser.getCurrentLocation());

        parser.close();
    }

    @Test
    public void testGetValueAsStringMethods() throws IOException {
        byte[] data = "[\"hello\", 456]".getBytes();
        UTF8StreamJsonParser parser = createParser(data);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getValueAsString());
        assertEquals("hello", parser.getValueAsString("fallback"));

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("456", parser.getValueAsString());
        assertEquals("456", parser.getValueAsString("fallback"));

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }
}
