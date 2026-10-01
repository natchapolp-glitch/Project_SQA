Below is one JUnit 4.10 test file, since the `pom.xml` pins JUnit 4.10. It uses Java 6-compatible syntax (no diamond, lambdas or try-with-resources), and it sits in the `ser` package so it can reach the protected members `_findAndAddDynamic`, `_handleSelfReference`, `isRequired(AnnotationIntrospector)` and the copy constructors. It has 29 test methods and I haven't compiled or run it.

`com/fasterxml/jackson/databind/ser/GeneratedBeanPropertyWriterTest.java`

```java
package com.fasterxml.jackson.databind.ser;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Regression tests for {@link BeanPropertyWriter}.
 */
public class GeneratedBeanPropertyWriterTest
{
    /*
    /**********************************************************
    /* Fixtures
    /**********************************************************
     */

    @JsonPropertyOrder({"id", "name"})
    public static class Bean {
        public int id = 7;
        public String nothing = null;
        public Bean self;
        @JsonProperty public String annotated = "a";
        private String name = "abc";
        public String getName() { return name; }
    }

    /** Simple serializer that records calls and writes "S:" + value as a String. */
    static class TestSerializer extends JsonSerializer<Object>
    {
        int calls;
        boolean empty;
        boolean objectId;

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov)
            throws IOException
        {
            calls++;
            jgen.writeString("S:" + value);
        }

        @Override
        public boolean isEmpty(Object value) { return empty; }

        @Override
        public boolean usesObjectId() { return objectId; }
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static BeanPropertyWriter writer(String prop, JsonSerializer<?> ser)
    {
        return writer(prop, ser, null, false, null);
    }

    private static BeanPropertyWriter writer(String prop, JsonSerializer<?> ser,
            boolean suppressNulls, Object suppressable)
    {
        return writer(prop, ser, null, suppressNulls, suppressable);
    }

    private static BeanPropertyWriter writer(String prop, JsonSerializer<?> ser,
            JavaType serType, boolean suppressNulls, Object suppressable)
    {
        BeanDescription desc = MAPPER.getSerializationConfig()
                .introspect(MAPPER.constructType(Bean.class));
        for (BeanPropertyDefinition def : desc.findProperties()) {
            if (prop.equals(def.getName())) {
                AnnotatedMember m = def.getAccessor();
                return new BeanPropertyWriter(def, m, desc.getClassAnnotations(),
                        MAPPER.constructType(m.getRawType()),
                        ser, null, serType, suppressNulls, suppressable);
            }
        }
        throw new IllegalStateException("No property '" + prop + "' found");
    }

    private static SerializerProvider provider()
    {
        return ((DefaultSerializerProvider) MAPPER.getSerializerProvider())
                .createInstance(MAPPER.getSerializationConfig(), MAPPER.getSerializerFactory());
    }

    private static String asField(BeanPropertyWriter w, Object bean, SerializerProvider prov)
        throws Exception
    {
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        g.writeStartObject();
        w.serializeAsField(bean, g, prov);
        g.writeEndObject();
        g.close();
        return sw.toString();
    }

    private static String asColumn(BeanPropertyWriter w, Object bean, SerializerProvider prov)
        throws Exception
    {
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        g.writeStartArray();
        w.serializeAsColumn(bean, g, prov);
        g.writeEndArray();
        g.close();
        return sw.toString();
    }

    private static String asPlaceholder(BeanPropertyWriter w, Object bean)
        throws Exception
    {
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        g.writeStartArray();
        w.serializeAsPlaceholder(bean, g, null);
        g.writeEndArray();
        g.close();
        return sw.toString();
    }

    /*
    /**********************************************************
    /* Basic accessors
    /**********************************************************
     */

    @Test
    public void testFieldPropertyAccessors()
    {
        BeanPropertyWriter w = writer("id", null);
        assertEquals("id", w.getName());
        assertEquals("id", w.getSerializedName().getValue());
        assertEquals(int.class, w.getPropertyType());
        assertEquals(int.class, w.getGenericPropertyType());
        assertEquals(int.class, w.getType().getRawClass());
        assertNotNull(w.getMember());
        assertEquals("id", w.getMember().getName());
        assertNull(w.getWrapperName());
    }

    @Test
    public void testMethodPropertyAccessors()
    {
        BeanPropertyWriter w = writer("name", null);
        assertEquals("name", w.getName());
        assertEquals(String.class, w.getPropertyType());
        assertEquals(String.class, w.getGenericPropertyType());
        assertEquals(String.class, w.getType().getRawClass());
        assertNotNull(w.getMember());
    }

    @Test
    public void testSerializerAndSuppressionFlags()
    {
        BeanPropertyWriter plain = writer("id", null);
        assertFalse(plain.hasSerializer());
        assertNull(plain.getSerializer());
        assertFalse(plain.hasNullSerializer());
        assertFalse(plain.willSuppressNulls());

        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter withSer = writer("id", ser, true, null);
        assertTrue(withSer.hasSerializer());
        assertSame(ser, withSer.getSerializer());
        assertFalse(withSer.hasNullSerializer());
        assertTrue(withSer.willSuppressNulls());
    }

    @Test
    public void testSerializationType()
    {
        BeanPropertyWriter noType = writer("id", null);
        assertNull(noType.getSerializationType());
        assertNull(noType.getRawSerializationType());

        JavaType t = MAPPER.constructType(String.class);
        BeanPropertyWriter withType = writer("name", null, t, false, null);
        assertSame(t, withType.getSerializationType());
        assertEquals(String.class, withType.getRawSerializationType());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testIsRequiredAndViews()
    {
        BeanPropertyWriter w = writer("id", null);
        assertFalse(w.isRequired());
        assertFalse(w.isRequired((AnnotationIntrospector) null));
        assertNull(w.getViews());
    }

    @Test
    public void testGetAnnotation()
    {
        BeanPropertyWriter w = writer("annotated", null);
        assertNotNull(w.getAnnotation(JsonProperty.class));
        assertNull(w.getAnnotation(JsonIgnore.class));
    }

    @Test
    public void testGetContextAnnotation()
    {
        BeanPropertyWriter w = writer("id", null);
        assertNotNull(w.getContextAnnotation(JsonPropertyOrder.class));
        assertNull(w.getContextAnnotation(JsonIgnore.class));
    }

    /*
    /**********************************************************
    /* Value access
    /**********************************************************
     */

    @Test
    public void testGetFromFieldAndMethod() throws Exception
    {
        Bean bean = new Bean();
        bean.id = 42;
        assertEquals(Integer.valueOf(42), writer("id", null).get(bean));
        assertEquals("abc", writer("name", null).get(bean));
        assertNull(writer("nothing", null).get(bean));
    }

    @Test
    public void testGetWithWrongBeanFails() throws Exception
    {
        try {
            writer("id", null).get("not a bean");
            fail("Should not pass");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /*
    /**********************************************************
    /* Mutators / configuration
    /**********************************************************
     */

    @Test
    public void testAssignSerializers()
    {
        TestSerializer s1 = new TestSerializer();
        TestSerializer s2 = new TestSerializer();

        BeanPropertyWriter w = writer("id", null);
        w.assignSerializer(s1);
        assertSame(s1, w.getSerializer());
        w.assignSerializer(s1); // same instance is fine
        try {
            w.assignSerializer(s2);
            fail("Should not pass");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("override serializer"));
        }

        assertFalse(w.hasNullSerializer());
        w.assignNullSerializer(s1);
        assertTrue(w.hasNullSerializer());
        w.assignNullSerializer(s1);
        try {
            w.assignNullSerializer(s2);
            fail("Should not pass");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("override null serializer"));
        }
    }

    @Test
    public void testInternalSettings()
    {
        BeanPropertyWriter w = writer("id", null);
        assertNull(w.getInternalSetting("k"));
        assertNull(w.removeInternalSetting("k"));

        assertNull(w.setInternalSetting("k", "v1"));
        assertEquals("v1", w.getInternalSetting("k"));
        assertEquals("v1", w.setInternalSetting("k", "v2"));
        assertEquals("v2", w.getInternalSetting("k"));

        assertNull(w.removeInternalSetting("other"));
        assertEquals("v2", w.removeInternalSetting("k"));
        assertNull(w.getInternalSetting("k"));
        assertNull(w.removeInternalSetting("k"));
    }

    @Test
    public void testRename()
    {
        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter w = writer("id", ser);
        w.setInternalSetting("k", "v");

        // no-op transformer -> same instance
        assertSame(w, w.rename(NameTransformer.NOP));

        NameTransformer prefixer = new NameTransformer() {
            @Override
            public String transform(String name) { return "p_" + name; }
            @Override
            public String reverse(String transformed) {
                return transformed.startsWith("p_") ? transformed.substring(2) : null;
            }
        };
        BeanPropertyWriter renamed = w.rename(prefixer);
        assertNotSame(w, renamed);
        assertEquals("p_id", renamed.getName());
        assertEquals("id", w.getName());
        assertSame(ser, renamed.getSerializer());
        assertEquals("v", renamed.getInternalSetting("k"));

        // settings map was copied, not shared
        renamed.setInternalSetting("k", "changed");
        assertEquals("v", w.getInternalSetting("k"));
    }

    @Test
    public void testCopyConstructors()
    {
        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter base = writer("name", ser, true, "x");
        BeanPropertyWriter copy = new BeanPropertyWriter(base);
        assertEquals("name", copy.getName());
        assertSame(ser, copy.getSerializer());
        assertTrue(copy.willSuppressNulls());
        assertEquals(base.getPropertyType(), copy.getPropertyType());

        BeanPropertyWriter named = new BeanPropertyWriter(base, new SerializedString("other"));
        assertEquals("other", named.getName());
        assertSame(ser, named.getSerializer());
    }

    @Test
    public void testUnwrappingWriter()
    {
        BeanPropertyWriter w = writer("name", null);
        BeanPropertyWriter unwrapping = w.unwrappingWriter(NameTransformer.NOP);
        assertNotNull(unwrapping);
        assertNotSame(w, unwrapping);
        assertTrue(unwrapping instanceof UnwrappingBeanPropertyWriter);
    }

    @Test
    public void testConstructorRejectsUnsupportedMember()
    {
        BeanDescription desc = MAPPER.getSerializationConfig()
                .introspect(MAPPER.constructType(Bean.class));
        BeanPropertyDefinition def = null;
        for (BeanPropertyDefinition d : desc.findProperties()) {
            if ("id".equals(d.getName())) {
                def = d;
            }
        }
        assertNotNull(def);
        AnnotatedConstructor ctor = desc.findDefaultConstructor();
        assertNotNull(ctor);
        try {
            new BeanPropertyWriter(def, ctor, desc.getClassAnnotations(),
                    MAPPER.constructType(int.class), null, null, null, false, null);
            fail("Should not pass");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not pass member of type"));
        }
    }

    @Test
    public void testToString()
    {
        String fieldStr = writer("id", null).toString();
        assertTrue(fieldStr.startsWith("property 'id' ("));
        assertTrue(fieldStr.contains("#id"));
        assertTrue(fieldStr.contains("no static serializer"));

        String methodStr = writer("name", new TestSerializer()).toString();
        assertTrue(methodStr.contains("via method"));
        assertTrue(methodStr.contains("#getName"));
        assertTrue(methodStr.contains("static serializer of type "
                + TestSerializer.class.getName()));
    }

    /*
    /**********************************************************
    /* serializeAsField
    /**********************************************************
     */

    @Test
    public void testSerializeAsFieldStaticSerializer() throws Exception
    {
        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter w = writer("id", ser);
        assertEquals("{\"id\":\"S:7\"}", asField(w, new Bean(), null));
        assertEquals(1, ser.calls);
    }

    @Test
    public void testSerializeAsFieldNulls() throws Exception
    {
        Bean bean = new Bean();
        TestSerializer ser = new TestSerializer();

        // no null serializer: suppressed
        BeanPropertyWriter w = writer("nothing", ser, true, null);
        assertEquals("{}", asField(w, bean, null));
        assertEquals(0, ser.calls);

        // with null serializer: written
        TestSerializer nullSer = new TestSerializer();
        w.assignNullSerializer(nullSer);
        assertEquals("{\"nothing\":\"S:null\"}", asField(w, bean, null));
        assertEquals(1, nullSer.calls);
        assertEquals(0, ser.calls);
    }

    @Test
    public void testSerializeAsFieldSuppressableValue() throws Exception
    {
        Bean bean = new Bean(); // id == 7
        TestSerializer ser = new TestSerializer();

        BeanPropertyWriter same = writer("id", ser, false, Integer.valueOf(7));
        assertEquals("{}", asField(same, bean, null));
        assertEquals(0, ser.calls);

        BeanPropertyWriter different = writer("id", ser, false, Integer.valueOf(8));
        assertEquals("{\"id\":\"S:7\"}", asField(different, bean, null));
        assertEquals(1, ser.calls);
    }

    @Test
    public void testSerializeAsFieldMarkerForEmpty() throws Exception
    {
        Bean bean = new Bean();
        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter w = writer("name", ser, false, BeanPropertyWriter.MARKER_FOR_EMPTY);

        ser.empty = true;
        assertEquals("{}", asField(w, bean, null));

        ser.empty = false;
        assertEquals("{\"name\":\"S:abc\"}", asField(w, bean, null));
    }

    @Test
    public void testSelfReference() throws Exception
    {
        Bean bean = new Bean();
        bean.self = bean;
        TestSerializer ser = new TestSerializer();
        BeanPropertyWriter w = writer("self", ser);
        try {
            asField(w, bean, null);
            fail("Should not pass");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("self-reference"));
        }

        // with object id handling, self-reference is allowed
        TestSerializer idSer = new TestSerializer();
        idSer.objectId = true;
        BeanPropertyWriter w2 = writer("self", idSer);
        String json = asField(w2, bean, null);
        assertTrue(json.startsWith("{\"self\":"));
        assertEquals(1, idSer.calls);

        // direct call of the protected helper
        w2._handleSelfReference(bean, idSer); // no exception
        try {
            w._handleSelfReference(bean, ser);
            fail("Should not pass");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testSerializeAsFieldDynamicSerializer() throws Exception
    {
        BeanPropertyWriter w = writer("name", null);
        assertFalse(w.hasSerializer());
        SerializerProvider prov = provider();
        assertEquals("{\"name\":\"abc\"}", asField(w, new Bean(), prov));
        // second call re-uses the serializer found for String
        assertEquals("{\"name\":\"abc\"}", asField(w, new Bean(), prov));
        assertNotNull(w._dynamicSerializers.serializerFor(String.class));
    }

    /*
    /**********************************************************
    /* serializeAsColumn / serializeAsPlaceholder
    /**********************************************************
     */

    @Test
    public void testSerializeAsColumnValuesAndNulls() throws Exception
    {
        Bean bean = new Bean();
        TestSerializer ser = new TestSerializer();

        assertEquals("[\"S:7\"]", asColumn(writer("id", ser), bean, null));

        // nulls can not be suppressed in tabular output
        BeanPropertyWriter nullable = writer("nothing", ser, true, null);
        assertEquals("[null]", asColumn(nullable, bean, null));

        TestSerializer nullSer = new TestSerializer();
        nullable.assignNullSerializer(nullSer);
        assertEquals("[\"S:null\"]", asColumn(nullable, bean, null));
    }

    @Test
    public void testSerializeAsColumnSuppressedGivesPlaceholder() throws Exception
    {
        Bean bean = new Bean();
        TestSerializer ser = new TestSerializer();

        BeanPropertyWriter byValue = writer("id", ser, false, Integer.valueOf(7));
        assertEquals("[null]", asColumn(byValue, bean, null));
        assertEquals(0, ser.calls);

        BeanPropertyWriter byEmpty = writer("name", ser, false, BeanPropertyWriter.MARKER_FOR_EMPTY);
        ser.empty = true;
        assertEquals("[null]", asColumn(byEmpty, bean, null));
        ser.empty = false;
        assertEquals("[\"S:abc\"]", asColumn(byEmpty, bean, null));
    }

    @Test
    public void testSerializeAsPlaceholder() throws Exception
    {
        BeanPropertyWriter w = writer("id", new TestSerializer());
        assertEquals("[null]", asPlaceholder(w, new Bean()));

        w.assignNullSerializer(new TestSerializer());
        assertEquals("[\"S:null\"]", asPlaceholder(w, new Bean()));
    }

    /*
    /**********************************************************
    /* Schema support
    /**********************************************************
     */

    @Test
    public void testDepositSchemaPropertyWithVisitor() throws Exception
    {
        final List<String> calls = new ArrayList<String>();
        JsonObjectFormatVisitor visitor = (JsonObjectFormatVisitor) Proxy.newProxyInstance(
                JsonObjectFormatVisitor.class.getClassLoader(),
                new Class<?>[] { JsonObjectFormatVisitor.class },
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        calls.add(method.getName());
                        return null;
                    }
                });
        BeanPropertyWriter w = writer("id", null);
        w.depositSchemaProperty(visitor);
        // property is not marked as required -> optional
        assertEquals(Arrays.asList("optionalProperty"), calls);

        // null visitor is tolerated
        w.depositSchemaProperty((JsonObjectFormatVisitor) null);
        assertEquals(1, calls.size());
    }

    @Test
    public void testDepositSchemaPropertyIntoObjectNode() throws Exception
    {
        BeanPropertyWriter w = writer("id", new TestSerializer());
        ObjectNode props = JsonNodeFactory.instance.objectNode();
        w.depositSchemaProperty(props, null);
        assertTrue(props.has("id"));
        // serializer is not schema-aware -> default schema node
        assertEquals("any", props.get("id").get("type").asText());
    }

    /*
    /**********************************************************
    /* _findAndAddDynamic
    /**********************************************************
     */

    @Test
    public void testFindAndAddDynamic() throws Exception
    {
        BeanPropertyWriter w = writer("name", null);
        PropertySerializerMap before = w._dynamicSerializers;
        assertNotNull(before);
        assertNull(before.serializerFor(String.class));

        JsonSerializer<Object> ser = w._findAndAddDynamic(before, String.class, provider());
        assertNotNull(ser);
        assertNotSame(before, w._dynamicSerializers);
        assertSame(ser, w._dynamicSerializers.serializerFor(String.class));
    }

    @Test
    public void testFindAndAddDynamicWithNonTrivialBaseType() throws Exception
    {
        BeanPropertyWriter w = writer("name", null);
        JavaType base = MAPPER.getTypeFactory().constructCollectionType(List.class, String.class);
        w.setNonTrivialBaseType(base);
        assertSame(base, w._nonTrivialBaseType);

        JsonSerializer<Object> ser = w._findAndAddDynamic(w._dynamicSerializers,
                ArrayList.class, provider());
        assertNotNull(ser);
        assertNotNull(w._dynamicSerializers.serializerFor(ArrayList.class));
    }
}
```

**Notes**
- Writers are built by introspecting a nested `Bean` class through `ObjectMapper` and passing the resulting `BeanPropertyDefinition` and `AnnotatedMember` to the public constructor. This avoids mocking Jackson internals and covers both the field and getter branches.
- A real `SerializerProvider` is created only for the two dynamic-serializer paths. All other tests pass `null`, because a static serializer never touches the provider.
- `depositSchemaProperty(JsonObjectFormatVisitor)` uses a `java.lang.reflect.Proxy` recorder, so the test doesn't depend on the exact method set of that interface in 2.2.x.
- The `serializeWithType` branch (non-null `_typeSerializer`) is not covered. The API list has no way to set a `TypeSerializer` after construction, and I didn't want to invent a fixture for it.
- The assertions on `getWrapperName()` (null) and the `"any"` default schema type rely on how Jackson 2.2 introspection and `JsonSchema.getDefaultSchemaNode()` behave. They are the ones most likely to need adjusting if the build shows a difference.