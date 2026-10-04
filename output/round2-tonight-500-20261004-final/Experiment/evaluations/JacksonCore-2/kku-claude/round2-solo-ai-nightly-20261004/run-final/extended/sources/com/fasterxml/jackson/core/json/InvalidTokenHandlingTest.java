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

public class InvalidTokenHandlingTest {

    private JsonParser createReaderParser(String content) throws IOException {
        JsonFactory f = new JsonFactory();
        return f.createParser(new StringReader(content));
    }

    private JsonParser createStreamParser(String content) throws IOException {
        JsonFactory f = new JsonFactory();
        return f.createParser(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testValidLiteralsStillParseReader() throws IOException {
        JsonParser p = createReaderParser("[true, false, null]");
        assertEquals(JsonToken.START_ARRAY, p.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, p.nextToken());
        assertEquals(JsonToken.VALUE_FALSE, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        p.close();
    }

    @Test
    public void testValidLiteralsStillParseStream() throws IOException {
        JsonParser p = createStreamParser("[true, false, null]");
        assertEquals(JsonToken.START_ARRAY, p.nextToken());
        assertEquals(JsonToken.VALUE_TRUE, p.nextToken());
        assertEquals(JsonToken.VALUE_FALSE, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        p.close();
    }

    @Test
    public void testInvalidTokenMessageReaderBased() throws IOException {
        JsonParser p = createReaderParser("nul]");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            String msg = e.getMessage();
            assertNotNull(msg);
            assertTrue(msg.contains("nul"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testInvalidTokenMessageStreamBased() throws IOException {
        JsonParser p = createStreamParser("nul]");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            String msg = e.getMessage();
            assertNotNull(msg);
            assertTrue(msg.contains("nul"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testInvalidTrueLiteralMessageConsistency() throws IOException {
        String badToken = "tru7";
        String readerMsg = null;
        String streamMsg = null;

        JsonParser rp = createReaderParser(badToken);
        try {
            rp.nextToken();
            fail("Expected JsonParseException from reader parser");
        } catch (JsonParseException e) {
            readerMsg = e.getMessage();
        } finally {
            rp.close();
        }

        JsonParser sp = createStreamParser(badToken);
        try {
            sp.nextToken();
            fail("Expected JsonParseException from stream parser");
        } catch (JsonParseException e) {
            streamMsg = e.getMessage();
        } finally {
            sp.close();
        }

        assertNotNull(readerMsg);
        assertNotNull(streamMsg);
        assertTrue(readerMsg.contains("tru"));
        assertTrue(streamMsg.contains("tru"));
    }

    @Test
    public void testInvalidNullLiteralVariant() throws IOException {
        JsonParser p = createReaderParser("nulx");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertTrue(e.getMessage().contains("nul"));
        } finally {
            p.close();
        }
    }

    @Test
    public void testInvalidFalseLiteralStream() throws IOException {
        JsonParser p = createStreamParser("fals3");
        try {
            p.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertTrue(e.getMessage().contains("fals"));
        } finally {
            p.close();
        }
    }
}
