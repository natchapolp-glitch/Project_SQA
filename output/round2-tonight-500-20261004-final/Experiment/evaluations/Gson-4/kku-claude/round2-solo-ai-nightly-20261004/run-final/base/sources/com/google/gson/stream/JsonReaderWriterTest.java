package com.google.gson.stream;

import org.junit.Test;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import static org.junit.Assert.*;

public class JsonReaderWriterTest {

    @Test
    public void nextLongOnNonIntegralNumberThrowsNumberFormatException() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("[1.5]"));
        reader.beginArray();
        try {
            reader.nextLong();
            fail("Expected NumberFormatException");
        } catch (NumberFormatException expected) {
        }
    }

    @Test
    public void nextLongOnQuotedStringValue() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("[\"123\"]"));
        reader.beginArray();
        assertEquals(123L, reader.nextLong());
        reader.endArray();
    }

    @Test
    public void nextDoubleNonFiniteRequiresLenient() throws IOException {
        JsonReader strictReader = new JsonReader(new StringReader("[NaN]"));
        strictReader.beginArray();
        try {
            strictReader.nextDouble();
            fail("Expected MalformedJsonException");
        } catch (com.google.gson.stream.MalformedJsonException expected) {
        }

        JsonReader lenientReader = new JsonReader(new StringReader("[NaN]"));
        lenientReader.setLenient(true);
        lenientReader.beginArray();
        double value = lenientReader.nextDouble();
        assertTrue(Double.isNaN(value));
    }

    @Test
    public void longMaxAndMinValueRoundTrip() throws IOException {
        JsonReader maxReader = new JsonReader(new StringReader("[9223372036854775807]"));
        maxReader.beginArray();
        assertEquals(Long.MAX_VALUE, maxReader.nextLong());
        maxReader.endArray();

        JsonReader minReader = new JsonReader(new StringReader("[-9223372036854775808]"));
        minReader.beginArray();
        assertEquals(Long.MIN_VALUE, minReader.nextLong());
        minReader.endArray();
    }

    @Test
    public void nextIntOnOutOfRangeLongThrowsNumberFormatException() throws IOException {
        JsonReader reader = new JsonReader(new StringReader("[9223372036854775807]"));
        reader.beginArray();
        try {
            reader.nextInt();
            fail("Expected NumberFormatException");
        } catch (NumberFormatException expected) {
        }
    }

    @Test
    public void writerValueStringNullRespectsSerializeNulls() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject();
        writer.name("a");
        writer.value((String) null);
        writer.endObject();
        writer.close();
        assertEquals("{}", stringWriter.toString());

        StringWriter stringWriter2 = new StringWriter();
        JsonWriter writer2 = new JsonWriter(stringWriter2);
        writer2.setSerializeNulls(true);
        writer2.beginObject();
        writer2.name("a");
        writer2.value((String) null);
        writer2.endObject();
        writer2.close();
        assertEquals("{\"a\":null}", stringWriter2.toString());
    }

    @Test
    public void writeThenReadLongValuesRoundTrip() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginArray();
        writer.value(Long.MIN_VALUE);
        writer.value(Long.MAX_VALUE);
        writer.value(0L);
        writer.endArray();
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(stringWriter.toString()));
        reader.beginArray();
        assertEquals(Long.MIN_VALUE, reader.nextLong());
        assertEquals(Long.MAX_VALUE, reader.nextLong());
        assertEquals(0L, reader.nextLong());
        reader.endArray();
    }
}
