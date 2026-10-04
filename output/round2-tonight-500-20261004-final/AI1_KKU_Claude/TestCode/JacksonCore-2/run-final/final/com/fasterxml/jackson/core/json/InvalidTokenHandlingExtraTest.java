package com.fasterxml.jackson.core.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

public class InvalidTokenHandlingExtraTest {

    private JsonParser createReaderParser(String content) throws IOException {
        JsonFactory f = new JsonFactory();
        return f.createParser(new StringReader(content));
    }

    private JsonParser createStreamParser(String content) throws IOException {
        JsonFactory f = new JsonFactory();
        return f.createParser(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testInvalidTrueLiteralVariantStream() throws IOException {
        JsonParser p = createStreamParser("trux");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("tru"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testInvalidFalseLiteralReaderBased() throws IOException {
        JsonParser p = createReaderParser("fals3");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("fals"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testInvalidNullLiteralVariantStream() throws IOException {
        JsonParser p = createStreamParser("nulx");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("nul"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testValidLiteralsInNestedStructureReaderAndStream() throws IOException {
        String content = "{\"a\":true,\"b\":[false,null]}";

        JsonParser rp = createReaderParser(content);
        assertEquals(JsonToken.START_OBJECT, rp.nextToken());
        assertEquals(JsonToken.FIELD_NAME, rp.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, rp.nextToken());
        assertEquals(JsonToken.FIELD_NAME, rp.nextToken());
        assertEquals(JsonToken.START_ARRAY, rp.nextToken());
        assertEquals(JsonToken.VALUE_FALSE, rp.nextToken());
        assertEquals(JsonToken.VALUE_NULL, rp.nextToken());
        assertEquals(JsonToken.END_ARRAY, rp.nextToken());
        assertEquals(JsonToken.END_OBJECT, rp.nextToken());
        rp.close();

        JsonParser sp = createStreamParser(content);
        assertEquals(JsonToken.START_OBJECT, sp.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sp.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, sp.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sp.nextToken());
        assertEquals(JsonToken.START_ARRAY, sp.nextToken());
        assertEquals(JsonToken.VALUE_FALSE, sp.nextToken());
        assertEquals(JsonToken.VALUE_NULL, sp.nextToken());
        assertEquals(JsonToken.END_ARRAY, sp.nextToken());
        assertEquals(JsonToken.END_OBJECT, sp.nextToken());
        sp.close();
    }
}
