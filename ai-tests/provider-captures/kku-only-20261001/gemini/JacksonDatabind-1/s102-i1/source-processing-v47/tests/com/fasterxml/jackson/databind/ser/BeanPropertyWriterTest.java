package com.fasterxml.jackson.databind.ser;

import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class BeanPropertyWriterTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CustomAnnotation {
        String value() default "custom";
    }

    public static class ViewA {}
    public static class ViewB {}

    public static class SampleBean {
        @CustomAnnotation("annotatedField")
        public String textField = "fieldValue";

        public Integer numberField = 42;

        public Object self = this;

        public String emptyString = "";

        public String nullValue = null;

        public String getTextProperty() {
            return textField;
        }

        public void setTextProperty(String val) {
            this.textField = val;
        }
    }

    private ObjectMapper mapper;
    private BeanDescription beanDesc;
    private List<BeanPropertyDefinition> properties;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        JavaType javaType = mapper.constructType(SampleBean.class);
        beanDesc = mapper.getSerializationConfig().introspect(javaType);
        properties = beanDesc.findProperties();
    }

    private BeanPropertyDefinition findProperty(String name) {
        for (BeanPropertyDefinition prop : properties) {
            if (name.equals(prop.getName())) {
                return prop;
            }
        }
        Assert.fail("Property not found: " + name);
        return null;
    }

    private Annotations emptyAnnotations() {
        return new Annotations() {
            @Override
            public <A extends java.lang.annotation.Annotation> A get(Class<A> cls) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    private BeanPropertyWriter buildWriter(String propName, boolean suppressNulls, Object suppressableValue) {
        BeanPropertyDefinition propDef = findProperty(propName);
        AnnotatedMember member = propDef.getPrimaryMember();
        JavaType type = member.getType(beanDesc.bindingsForBeanType());
        return new BeanPropertyWriter(propDef, member, emptyAnnotations(), type,
                null, null, null, suppressNulls, suppressableValue);
    }

    @Test
    public void testBasicPropertiesAndAccessors() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);

        Assert.assertEquals("textField", writer.getName());
        Assert.assertEquals("textField", writer.getSerializedName().getValue());
        Assert.assertEquals(String.class, writer.getPropertyType());
        Assert.assertEquals(String.class, writer.getGenericPropertyType());
        Assert.assertNull(writer.getSerializationType());
        Assert.assertNull(writer.getRawSerializationType());
        Assert.assertNotNull(writer.getType());
        Assert.assertNotNull(writer.getMember());
        Assert.assertFalse(writer.isRequired());
        Assert.assertFalse(writer.willSuppressNulls());
        Assert.assertFalse(writer.hasSerializer());
        Assert.assertFalse(writer.hasNullSerializer());
    }

    @Test
    public void testGetFieldValueAndMethodValue() throws Exception {
        SampleBean bean = new SampleBean();
        bean.textField = "helloField";

        BeanPropertyWriter fieldWriter = buildWriter("textField", false, null);
        Assert.assertEquals("helloField", fieldWriter.get(bean));

        BeanPropertyWriter methodWriter = buildWriter("textProperty", false, null);
        Assert.assertEquals("helloField", methodWriter.get(bean));
    }

    

    @Test
    public void testInternalSettings() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);

        Assert.assertNull(writer.getInternalSetting("key1"));
        Object old = writer.setInternalSetting("key1", "val1");
        Assert.assertNull(old);
        Assert.assertEquals("val1", writer.getInternalSetting("key1"));

        Object replaced = writer.setInternalSetting("key1", "val2");
        Assert.assertEquals("val1", replaced);
        Assert.assertEquals("val2", writer.getInternalSetting("key1"));

        Object removed = writer.removeInternalSetting("key1");
        Assert.assertEquals("val2", removed);
        Assert.assertNull(writer.getInternalSetting("key1"));

        // Removing non-existing setting returns null
        Assert.assertNull(writer.removeInternalSetting("nonExistent"));
    }

    @Test
    public void testCopyConstructorCopiesInternalSettings() {
        BeanPropertyWriter base = buildWriter("textField", false, null);
        base.setInternalSetting("keyA", "valA");

        BeanPropertyWriter copy = new BeanPropertyWriter(base);
        Assert.assertEquals("valA", copy.getInternalSetting("keyA"));

        // Settings map in copy should be independent
        copy.setInternalSetting("keyA", "valB");
        Assert.assertEquals("valB", copy.getInternalSetting("keyA"));
        Assert.assertEquals("valA", base.getInternalSetting("keyA"));
    }

    @Test
    public void testRenameWithSameNameReturnsThis() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        NameTransformer noop = NameTransformer.NOP;

        BeanPropertyWriter renamed = writer.rename(noop);
        Assert.assertSame(writer, renamed);
    }

    @Test
    public void testRenameWithDifferentNameReturnsNewInstance() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        NameTransformer prefixer = NameTransformer.simpleTransformer("prefix_", "");

        BeanPropertyWriter renamed = writer.rename(prefixer);
        Assert.assertNotSame(writer, renamed);
        Assert.assertEquals("prefix_textField", renamed.getName());
        Assert.assertEquals("prefix_textField", renamed.getSerializedName().getValue());
    }

    @Test
    public void testAssignSerializerSuccessAndFailure() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        Assert.assertFalse(writer.hasSerializer());

        JsonSerializer<Object> ser1 = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory()).findNullValueSerializer(writer);
        writer.assignSerializer(ser1);
        Assert.assertTrue(writer.hasSerializer());
        Assert.assertSame(ser1, writer.getSerializer());

        // Reassigning same instance is allowed
        writer.assignSerializer(ser1);

        // Assigning a different serializer instance must throw IllegalStateException
        JsonSerializer<Object> ser2 = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) {}
        };
        try {
            writer.assignSerializer(ser2);
            Assert.fail("Expected IllegalStateException when overriding serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override serializer"));
        }
    }

    @Test
    public void testAssignNullSerializerSuccessAndFailure() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        Assert.assertFalse(writer.hasNullSerializer());

        JsonSerializer<Object> ser1 = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory()).findNullValueSerializer(writer);
        writer.assignNullSerializer(ser1);
        Assert.assertTrue(writer.hasNullSerializer());

        // Reassigning same instance is allowed
        writer.assignNullSerializer(ser1);

        // Assigning a different instance must throw IllegalStateException
        JsonSerializer<Object> ser2 = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) {}
        };
        try {
            writer.assignNullSerializer(ser2);
            Assert.fail("Expected IllegalStateException when overriding null serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override null serializer"));
        }
    }

    @Test
    public void testSetNonTrivialBaseType() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        JavaType baseType = mapper.constructType(CharSequence.class);
        writer.setNonTrivialBaseType(baseType);
        Assert.assertEquals(baseType, writer._nonTrivialBaseType);
    }

    @Test
    public void testUnwrappingWriter() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        NameTransformer transformer = NameTransformer.simpleTransformer("pre.", ".post");
        BeanPropertyWriter unwrapped = writer.unwrappingWriter(transformer);

        Assert.assertNotNull(unwrapped);
        Assert.assertTrue(unwrapped.isUnwrapping());
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        CustomAnnotation annotation = writer.getAnnotation(CustomAnnotation.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals("annotatedField", annotation.value());

        Assert.assertNull(writer.getAnnotation(Deprecated.class));
    }

    @Test
    public void testGetContextAnnotation() {
        final CustomAnnotation dummyAnnotation = SampleBean.class.getAnnotation(CustomAnnotation.class);
        Annotations contextAnnotations = new Annotations() {
            @SuppressWarnings("unchecked")
            @Override
            public <A extends java.lang.annotation.Annotation> A get(Class<A> cls) {
                if (cls == CustomAnnotation.class) {
                    return (A) dummyAnnotation;
                }
                return null;
            }

            @Override
            public int size() {
                return 1;
            }
        };

        BeanPropertyDefinition propDef = findProperty("textField");
        AnnotatedMember member = propDef.getPrimaryMember();
        BeanPropertyWriter writer = new BeanPropertyWriter(propDef, member, contextAnnotations,
                member.getType(beanDesc.bindingsForBeanType()), null, null, null, false, null);

        Assert.assertEquals(dummyAnnotation, writer.getContextAnnotation(CustomAnnotation.class));
        Assert.assertNull(writer.getContextAnnotation(Deprecated.class));
    }

    @Test
    public void testSerializeAsFieldNormal() throws Exception {
        SampleBean bean = new SampleBean();
        bean.textField = "testValue";
        BeanPropertyWriter writer = buildWriter("textField", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsField(bean, jgen, provider);

        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"textField\":\"testValue\"}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldNullWithoutNullSerializerSuppresses() throws Exception {
        SampleBean bean = new SampleBean();
        bean.nullValue = null;
        BeanPropertyWriter writer = buildWriter("nullValue", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsField(bean, jgen, provider);

        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldNullWithNullSerializerOutputsNull() throws Exception {
        SampleBean bean = new SampleBean();
        bean.nullValue = null;
        BeanPropertyWriter writer = buildWriter("nullValue", false, null);

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.assignNullSerializer(provider.findNullValueSerializer(writer));

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        writer.serializeAsField(bean, jgen, provider);

        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"nullValue\":null}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSuppressEmptyString() throws Exception {
        SampleBean bean = new SampleBean();
        bean.emptyString = "";
        BeanPropertyWriter writer = buildWriter("emptyString", false, BeanPropertyWriter.MARKER_FOR_EMPTY);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsField(bean, jgen, provider);

        jgen.writeEndObject();
        jgen.close();

        // Empty string should have been suppressed
        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSuppressDefaultValue() throws Exception {
        SampleBean bean = new SampleBean();
        bean.numberField = 42;
        BeanPropertyWriter writer = buildWriter("numberField", false, Integer.valueOf(42));

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsField(bean, jgen, provider);

        jgen.writeEndObject();
        jgen.close();

        // Default 42 should have been suppressed
        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSelfReferenceThrowsException() throws Exception {
        SampleBean bean = new SampleBean();
        BeanPropertyWriter writer = buildWriter("self", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartObject();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        try {
            writer.serializeAsField(bean, jgen, provider);
            Assert.fail("Expected JsonMappingException on self-reference");
        } catch (JsonMappingException e) {
            Assert.assertTrue(e.getMessage().contains("Direct self-reference leading to cycle"));
        } finally {
            jgen.close();
        }
    }

    @Test
    public void testSerializeAsColumnNormal() throws Exception {
        SampleBean bean = new SampleBean();
        bean.textField = "colVal";
        BeanPropertyWriter writer = buildWriter("textField", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartArray();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsColumn(bean, jgen, provider);

        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[\"colVal\"]", sw.toString());
    }

    @Test
    public void testSerializeAsColumnNullOutputsNull() throws Exception {
        SampleBean bean = new SampleBean();
        bean.nullValue = null;
        BeanPropertyWriter writer = buildWriter("nullValue", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartArray();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsColumn(bean, jgen, provider);

        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testSerializeAsColumnSuppressedValueOutputsPlaceholder() throws Exception {
        SampleBean bean = new SampleBean();
        bean.emptyString = "";
        BeanPropertyWriter writer = buildWriter("emptyString", false, BeanPropertyWriter.MARKER_FOR_EMPTY);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartArray();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsColumn(bean, jgen, provider);

        jgen.writeEndArray();
        jgen.close();

        // Suppressed value in column serialization must produce a placeholder (null)
        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testSerializeAsPlaceholder() throws Exception {
        SampleBean bean = new SampleBean();
        BeanPropertyWriter writer = buildWriter("textField", false, null);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = mapper.getFactory().createGenerator(sw);
        jgen.writeStartArray();

        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        writer.serializeAsPlaceholder(bean, jgen, provider);

        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testToStringFormat() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        String desc = writer.toString();

        Assert.assertTrue(desc.startsWith("property 'textField' ("));
        Assert.assertTrue(desc.contains("field \""));
        Assert.assertTrue(desc.contains("no static serializer"));

        BeanPropertyWriter methodWriter = buildWriter("textProperty", false, null);
        String methodDesc = methodWriter.toString();
        Assert.assertTrue(methodDesc.startsWith("property 'textProperty' ("));
        Assert.assertTrue(methodDesc.contains("via method "));
    }

    @Test
    public void testDepositSchemaPropertyLegacy() throws Exception {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        ObjectNode propertiesNode = JsonNodeFactory.instance.objectNode();
        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());

        writer.depositSchemaProperty(propertiesNode, provider);
        Assert.assertTrue(propertiesNode.has("textField"));
        Assert.assertEquals("string", propertiesNode.get("textField").get("type").asText());
    }

    @Test
    public void testDeprecatedIsRequiredWithIntrospector() {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        Assert.assertFalse(writer.isRequired(mapper.getSerializationConfig().getAnnotationIntrospector()));
    }

    @Test
    public void testFindAndAddDynamicSerializer() throws Exception {
        BeanPropertyWriter writer = buildWriter("textField", false, null);
        SerializerProvider provider = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory());
        PropertySerializerMap map = PropertySerializerMap.emptyMap();

        JsonSerializer<Object> ser = writer._findAndAddDynamic(map, String.class, provider);
        Assert.assertNotNull(ser);
        Assert.assertNotNull(writer._dynamicSerializers);
    }
}
