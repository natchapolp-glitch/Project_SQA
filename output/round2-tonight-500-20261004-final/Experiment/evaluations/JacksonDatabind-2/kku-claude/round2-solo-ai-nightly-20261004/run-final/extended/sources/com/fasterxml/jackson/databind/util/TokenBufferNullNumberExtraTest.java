package com.fasterxml.jackson.databind.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class TokenBufferNullNumberExtraTest {

    @Test
    public void testWriteNullBigIntegerThenValueNumberIntAfter() throws Exception {
        TokenBuffer buf = new TokenBuffer(null, false);
        buf.writeStartArray();
        buf.writeNumber((BigInteger) null);
        buf.writeNumber(7);
        buf.writeEndArray();
        buf.close();

        JsonParser p = buf.asParser();
        assertEquals(JsonToken.START_ARRAY, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, p.nextToken());
        assertEquals(7, p.getIntValue());
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        assertNull(p.nextToken());
        p.close();
    }

    @Test
    public void testWriteNullBigDecimalThenValueNumberFloatAfter() throws Exception {
        TokenBuffer buf = new TokenBuffer(null, false);
        buf.writeStartArray();
        buf.writeNumber((BigDecimal) null);
        buf.writeNumber(1.25);
        buf.writeEndArray();
        buf.close();

        JsonParser p = buf.asParser();
        assertEquals(JsonToken.START_ARRAY, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, p.nextToken());
        assertEquals(1.25, p.getDoubleValue(), 0.0001);
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        assertNull(p.nextToken());
        p.close();
    }

    @Test
    public void testAppendBufferWithNullBigIntegerPreservesNull() throws Exception {
        TokenBuffer src = new TokenBuffer(null, false);
        src.writeStartArray();
        src.writeNumber((BigInteger) null);
        src.writeEndArray();
        src.close();

        TokenBuffer dest = new TokenBuffer(null, false);
        dest.append(src);
        dest.close();

        JsonParser p = dest.asParser();
        assertEquals(JsonToken.START_ARRAY, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        assertNull(p.nextToken());
        p.close();
    }

    @Test
    public void testSerializeBufferWithNullBigDecimalToAnotherBuffer() throws Exception {
        TokenBuffer src = new TokenBuffer(null, false);
        src.writeStartObject();
        src.writeFieldName("v");
        src.writeNumber((BigDecimal) null);
        src.writeEndObject();
        src.close();

        TokenBuffer dest = new TokenBuffer(null, false);
        src.serialize(dest);
        dest.close();

        JsonParser p = dest.asParser();
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("v", p.getCurrentName());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals(JsonToken.END_OBJECT, p.nextToken());
        assertNull(p.nextToken());
        p.close();
    }
}
