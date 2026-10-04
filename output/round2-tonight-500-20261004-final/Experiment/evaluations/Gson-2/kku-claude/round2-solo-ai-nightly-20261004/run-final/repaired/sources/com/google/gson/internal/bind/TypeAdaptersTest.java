package com.google.gson.internal.bind;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

public class TypeAdaptersTest {

  private static String write(TypeAdapter<Object> adapter, Object value) throws Exception {
    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    adapter.write(writer, value);
    writer.flush();
    return sw.toString();
  }

  @SuppressWarnings("unchecked")
  private static <T> TypeAdapter<T> cast(TypeAdapter<?> adapter) {
    return (TypeAdapter<T>) adapter;
  }

  @Test
  public void bitSetWriteProducesBinaryArray() throws Exception {
    BitSet bitSet = new BitSet();
    bitSet.set(0);
    bitSet.set(2);
    TypeAdapter<BitSet> adapter = TypeAdapters.BIT_SET;
    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    adapter.write(writer, bitSet);
    writer.flush();
    assertEquals("[1,0,1]", sw.toString());
  }

  @Test
  public void bitSetReadParsesNumbersBooleansAndStrings() throws Exception {
    TypeAdapter<BitSet> adapter = TypeAdapters.BIT_SET;
    JsonReader reader = new JsonReader(new StringReader("[1,true,\"0\"]"));
    BitSet result = adapter.read(reader);
    assertTrue(result.get(0));
    assertTrue(result.get(1));
    assertFalse(result.get(2));
  }

  @Test(expected = JsonSyntaxException.class)
  public void bitSetReadInvalidStringThrows() throws Exception {
    TypeAdapter<BitSet> adapter = TypeAdapters.BIT_SET;
    JsonReader reader = new JsonReader(new StringReader("[\"notanumber\"]"));
    adapter.read(reader);
  }

  @Test
  public void booleanReadSupportsStringAndNull() throws Exception {
    TypeAdapter<Boolean> adapter = TypeAdapters.BOOLEAN;
    JsonReader stringReader = new JsonReader(new StringReader("\"true\""));
    assertEquals(Boolean.TRUE, adapter.read(stringReader));

    JsonReader nullReader = new JsonReader(new StringReader("null"));
    assertNull(adapter.read(nullReader));
  }

  @Test
  public void booleanAsStringWritesQuotedValue() throws Exception {
    TypeAdapter<Boolean> adapter = TypeAdapters.BOOLEAN_AS_STRING;
    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    writer.setLenient(true);
    adapter.write(writer, Boolean.TRUE);
    writer.flush();
    assertEquals("\"true\"", sw.toString());
  }

  @Test
  public void stringAdapterCoercesBooleanAndHandlesNull() throws Exception {
    TypeAdapter<String> adapter = TypeAdapters.STRING;

    JsonReader boolReader = new JsonReader(new StringReader("true"));
    boolReader.setLenient(true);
    assertEquals("true", adapter.read(boolReader));

    JsonReader nullReader = new JsonReader(new StringReader("null"));
    assertNull(adapter.read(nullReader));

    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    writer.setLenient(true);
    adapter.write(writer, null);
    writer.flush();
    assertEquals("null", sw.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void classAdapterWriteNonNullThrows() throws Exception {
    StringWriter sw = new StringWriter();
    JsonWriter writer = new JsonWriter(sw);
    TypeAdapters.CLASS.write(writer, String.class);
  }

  @Test
  public void factoriesReturnNullForNonMatchingType() {
    Gson gson = new Gson();
    TypeAdapter<?> adapter = TypeAdapters.INTEGER_FACTORY.create(gson, TypeToken.get(String.class));
    assertNull(adapter);

    TypeAdapter<?> matching = TypeAdapters.INTEGER_FACTORY.create(gson, TypeToken.get(Integer.class));
    assertNotNull(matching);
    assertSame(TypeAdapters.INTEGER, matching);
  }

  @Test
  public void newFactoryForMultipleTypesMatchesBaseAndSubtype() {
    TypeAdapterFactory factory = TypeAdapters.newFactoryForMultipleTypes(
        Number.class, Integer.class, TypeAdapters.NUMBER);
    Gson gson = new Gson();

    assertNotNull(factory.create(gson, TypeToken.get(Number.class)));
    assertNotNull(factory.create(gson, TypeToken.get(Integer.class)));
    assertNull(factory.create(gson, TypeToken.get(String.class)));
  }
}
