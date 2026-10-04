package com.google.gson.stream;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JsonWriterTest {

    @Test
    public void testBasicDocumentWriting() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.beginArray();
        writer.value("hello");
        writer.value(123);
        writer.value(true);
        writer.nullValue();
        writer.beginObject();
        writer.name("k").value("v");
        writer.endObject();
        writer.endArray();
        writer.close();

        assertEquals("[\"hello\",123,true,null,{\"k\":\"v\"}]", stringWriter.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testMismatchedEndArrayThrows() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginArray();
        writer.endObject();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStrictNanInfinityThrows() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginArray();
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLenientNanInfinityAllowed() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setLenient(true);
        writer.beginArray();
        writer.value(Double.NaN);
    }

    @Test
    public void testIndentation() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setIndent("  ");
        writer.beginObject();
        writer.name("a").value(1);
        writer.endObject();
        writer.close();

        String expected = "{\n  \"a\": 1\n}";
        assertEquals(expected, stringWriter.toString());
    }
}
