package com.google.gson.stream;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonReaderTest {

    @Test
    public void testBasicObjectAndArrayParsing() throws IOException {
        String json = "[\"hello\", 42, true, null, {\"key\": \"value\"}]";
        JsonReader reader = new JsonReader(new StringReader(json));

        reader.beginArray();
        assertTrue(reader.hasNext());
        assertEquals("hello", reader.nextString());
        assertTrue(reader.hasNext());
        assertEquals(42, reader.nextInt());
        assertTrue(reader.hasNext());
        assertTrue(reader.nextBoolean());
        assertTrue(reader.hasNext());
        reader.nextNull();
        assertTrue(reader.hasNext());

        reader.beginObject();
        assertTrue(reader.hasNext());
        assertEquals("key", reader.nextName());
        assertEquals("value", reader.nextString());
        assertFalse(reader.hasNext());
        reader.endObject();

        assertFalse(reader.hasNext());
        reader.endArray();
        reader.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testStrictPeekTypeMismatchThrows() throws IOException {
        String json = "[\"not-a-number\"]";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginArray();
        reader.nextInt();
    }

    @Test
    public void testLenientModeAllowsUnquotedAndSingleQuotes() throws IOException {
        String json = "{key: 'value'}";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);

        reader.beginObject();
        assertTrue(reader.hasNext());
        assertEquals("key", reader.nextName());
        assertEquals("value", reader.nextString());
        reader.endObject();
        reader.close();
    }

    @Test(expected = IOException.class)
    public void testMalformedJsonThrowsIOException() throws IOException {
        String json = "{\"key\": unclosed_string";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        reader.nextName();
        reader.nextString();
    }

    @Test
    public void testSkipValue() throws IOException {
        String json = "{\"a\": [1, 2, {" +
                " \"b\": 3" +
                "}], \"c\": 4}";
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        assertEquals("a", reader.nextName());
        reader.skipValue();
        assertEquals("c", reader.nextName());
        assertEquals(4, reader.nextInt());
        reader.endObject();
        reader.close();
    }
}
