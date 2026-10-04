package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.CharArrayWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;

public class JacksonCoreParsersTest {

    @Test
    public void testReaderBasedParserLifecycleAndCodec() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        CharsToNameCanonicalizer symbols = CharsToNameCanonicalizer.createRoot();
        StringReader reader = new StringReader("{\"abc\": 123}");
        
        ReaderBasedJsonParser parser = new ReaderBasedJsonParser(ctxt, 0, reader, null, symbols);
        
        assertNull(parser.getCodec());
        assertNotNull(parser.getInputSource());
        
        ObjectCodec mockCodec = new com.fasterxml.jackson.core.ObjectCodec() {
            @Override
            public com.fasterxml.jackson.core.Version version() { return null; }
            @Override
            public <T> T readValue(com.fasterxml.jackson.core.JsonParser p, Class<T> valueType) throws IOException { return null; }
            @Override
            public <T> T readValue(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.core.type.TypeReference<?> valueTypeRef) throws IOException { return null; }
            @Override
            public <T> T readValue(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.core.type.ResolvedType valueType) throws IOException { return null; }
            @Override
            public <T> java.util.Iterator<T> readValues(com.fasterxml.jackson.core.JsonParser p, Class<T> valueType) throws IOException { return null; }
            @Override
            public <T> java.util.Iterator<T> readValues(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.core.type.TypeReference<?> valueTypeRef) throws IOException { return null; }
            @Override
            public <T> java.util.Iterator<T> readValues(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.core.type.ResolvedType valueType) throws IOException { return null; }
            @Override
            public void writeValue(com.fasterxml.jackson.core.JsonGenerator g, Object value) throws IOException {}
            @Override
            public <T> T treeToValue(com.fasterxml.jackson.core.TreeNode n, Class<T> valueType) throws IOException { return null; }
            @Override
            public com.fasterxml.jackson.core.TreeNode createObjectNode() { return null; }
            @Override
            public com.fasterxml.jackson.core.TreeNode createArrayNode() { return null; }
            @Override
            public com.fasterxml.jackson.core.JsonParser treeAsTokens(com.fasterxml.jackson.core.TreeNode n) { return null; }
            @Override
            public com.fasterxml.jackson.core.JsonFactory getJsonFactory() { return null; }
        };
        
        parser.setCodec(mockCodec);
        assertEquals(mockCodec, parser.getCodec());
        
        parser.close();
    }

    @Test
    public void testReaderBasedParserTokenization() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        CharsToNameCanonicalizer symbols = CharsToNameCanonicalizer.createRoot();
        StringReader reader = new StringReader("  [true, false, null, \"hello\"] ");
        
        ReaderBasedJsonParser parser = new ReaderBasedJsonParser(ctxt, 0, reader, null, symbols);
        
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertEquals(true, parser.getValueAsBoolean());
        assertEquals(JsonToken.VALUE_FALSE, parser.nextToken());
        assertEquals(false, parser.getValueAsBoolean());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getText());
        assertEquals("hello", parser.getValueAsString());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());
        
        parser.close();
    }

    @Test
    public void testReaderBasedParserReleaseBuffered() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        CharsToNameCanonicalizer symbols = CharsToNameCanonicalizer.createRoot();
        StringReader reader = new StringReader("testcontent");
        
        ReaderBasedJsonParser parser = new ReaderBasedJsonParser(ctxt, 0, reader, null, symbols);
        parser.loadMore();
        
        CharArrayWriter writer = new CharArrayWriter();
        int count = parser.releaseBuffered(writer);
        assertTrue(count >= 0);
        
        parser.close();
    }

    @Test
    public void testUTF8StreamParserLifecycleAndCodec() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        byte[] inputData = "{\"key\": 456}".getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        
        UTF8StreamJsonParser parser = new UTF8StreamJsonParser(
            ctxt, 0, inputStream, null, symbols, inputData, 0, inputData.length, false
        );
        
        assertNull(parser.getCodec());
        assertEquals(inputStream, parser.getInputSource());
        
        parser.close();
    }

    @Test
    public void testUTF8StreamParserTokenization() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        byte[] inputData = "{\"a\": 100}".getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        
        UTF8StreamJsonParser parser = new UTF8StreamJsonParser(
            ctxt, 0, inputStream, null, symbols, inputData, 0, inputData.length, false
        );
        
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(100, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        
        parser.close();
    }

    @Test
    public void testUTF8StreamParserReleaseBuffered() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        byte[] inputData = "streamdata".getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        
        UTF8StreamJsonParser parser = new UTF8StreamJsonParser(
            ctxt, 0, inputStream, null, symbols, inputData, 0, inputData.length, false
        );
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int count = parser.releaseBuffered(outputStream);
        assertTrue(count >= 0);
        
        parser.close();
    }

    @Test
    public void testReaderBasedParserNumbers() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        CharsToNameCanonicalizer symbols = CharsToNameCanonicalizer.createRoot();
        StringReader reader = new StringReader("12345 -6789 3.14159");
        
        ReaderBasedJsonParser parser = new ReaderBasedJsonParser(ctxt, 0, reader, null, symbols);
        
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(12345, parser.getIntValue());
        
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(-6789, parser.getIntValue());
        
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14159, parser.getDoubleValue(), 0.00001);
        
        parser.close();
    }

    @Test
    public void testUTF8StreamParserBinaryDecoding() throws Exception {
        BufferRecycler br = new BufferRecycler();
        IOContext ctxt = new IOContext(br, null, false);
        BytesToNameCanonicalizer symbols = BytesToNameCanonicalizer.createRoot();
        byte[] inputData = "\"AQID\"".getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        
        UTF8StreamJsonParser parser = new UTF8StreamJsonParser(
            ctxt, 0, inputStream, null, symbols, inputData, 0, inputData.length, false
        );
        
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        byte[] binary = parser.getBinaryValue(Base64Variants.MIME);
        assertNotNull(binary);
        assertEquals(3, binary.length);
        assertEquals(1, binary[0]);
        assertEquals(2, binary[1]);
        assertEquals(3, binary[2]);
        
        parser.close();
    }
}
