package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.BitSet;
import java.util.UUID;
import org.junit.Test;

public class TypeAdaptersTest {

  @Test
  public void testBooleanAdapter() throws IOException {
    TypeAdapter<Boolean> adapter = TypeAdapters.BOOLEAN;
    String jsonTrue = adapter.toJson(true);
    assertEquals("true", jsonTrue);

    Boolean parsedTrue = adapter.fromJson("true");
    assertTrue(parsedTrue);

    Boolean parsedNull = adapter.fromJson("null");
    assertNull(parsedNull);
  }

  @Test
  public void testIntegerAdapter() throws IOException {
    TypeAdapter<Number> adapter = TypeAdapters.INTEGER;
    String json = adapter.toJson(123);
    assertEquals("123", json);

    Number parsed = adapter.fromJson("456");
    assertEquals(456, parsed.intValue());

    assertNull(adapter.fromJson("null"));
  }

  @Test(expected = JsonSyntaxException.class)
  public void testIntegerAdapterInvalid() throws IOException {
    TypeAdapters.INTEGER.fromJson("\"abc\"");
  }

  @Test
  public void testStringAdapter() throws IOException {
    TypeAdapter<String> adapter = TypeAdapters.STRING;
    String json = adapter.toJson("hello");
    assertEquals("\"hello\"", json);

    String parsed = adapter.fromJson("\"world\"");
    assertEquals("world", parsed);

    assertNull(adapter.fromJson("null"));
  }

  @Test
  public void testBitSetAdapter() throws IOException {
    TypeAdapter<BitSet> adapter = TypeAdapters.BIT_SET;
    BitSet bitSet = new BitSet();
    bitSet.set(0);
    bitSet.set(2);

    String json = adapter.toJson(bitSet);
    assertNotNull(json);

    BitSet parsed = adapter.fromJson(json);
    assertNotNull(parsed);
    assertTrue(parsed.get(0));
    assertTrue(!parsed.get(1));
    assertTrue(parsed.get(2));

    assertNull(adapter.fromJson("null"));
  }

  @Test
  public void testUuidAdapter() throws IOException {
    TypeAdapter<UUID> adapter = TypeAdapters.UUID;
    UUID uuid = UUID.randomUUID();
    String json = adapter.toJson(uuid);
    assertNotNull(json);

    UUID parsed = adapter.fromJson(json);
    assertEquals(uuid, parsed);

    assertNull(adapter.fromJson("null"));
  }

  @Test
  public void testFactoryCreation() {
    TypeAdapterFactory factory = TypeAdapters.newFactory(Integer.class, TypeAdapters.INTEGER);
    assertNotNull(factory);
    
    Gson gson = new Gson();
    assertNotNull(factory.create(gson, TypeToken.get(Integer.class)));
  }
}
