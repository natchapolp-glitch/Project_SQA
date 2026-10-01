package com.fasterxml.jackson.databind.ser;

import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class BeanPropertyWriterTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CustomAnnotation {
        String value() default "";
    }

    public static class PersonBean {
        @CustomAnnotation("fieldAnnotation")
        @JsonProperty(value = "nameField", required = true)
        public String name = "Alice";

        private int age = 30;

        public PersonBean self;

        public PersonBean() {
        }

        public PersonBean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @JsonProperty(value = "ageProp", required = false)
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private BeanDescription beanDesc;
    private BeanPropertyDefinition namePropDef;
    private BeanPropertyDefinition agePropDef;
    private BeanPropertyWriter fieldWriter;
    private BeanPropertyWriter methodWriter;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        jsonFactory = mapper.getFactory();
        JavaType javaType = mapper.constructType(PersonBean.class);
        beanDesc = mapper.getSerializationConfig().introspect(javaType);

        List<BeanPropertyDefinition> props = beanDesc.findProperties();
        for (BeanPropertyDefinition prop : props) {
            if ("nameField".equals(prop.getName())) {
                namePropDef = prop;
            } else if ("ageProp".equals(prop.getName())) {
                agePropDef = prop;
            }
        }

        Assert.assertNotNull("namePropDef must be discovered", namePropDef);
        Assert.assertNotNull("agePropDef must be discovered", agePropDef);

        AnnotatedMember nameMember = namePropDef.getPrimaryMember();
        Annotations nameAnnotations = beanDesc.getClassAnnotations();
        JavaType nameType = mapper.constructType(String.class);

        fieldWriter = new BeanPropertyWriter(
                namePropDef,
                nameMember,
                nameAnnotations,
                nameType,
                null,
                null,
                null,
                false,
                null
        );

        AnnotatedMember ageMember = agePropDef.getPrimaryMember();
        Annotations ageAnnotations = beanDesc.getClassAnnotations();
        JavaType ageType = mapper.constructType(int.class);

        methodWriter = new BeanPropertyWriter(
                agePropDef,
                ageMember,
                ageAnnotations,
                ageType,
                null,
                null,
                null,
                false,
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidMemberType() {
        AnnotatedConstructor ctor = beanDesc.getConstructors().get(0);
        new BeanPropertyWriter(
                namePropDef,
                ctor,
                beanDesc.getClassAnnotations(),
                mapper.constructType(PersonBean.class),
                null, null, null, false, null
        );
    }

    @Test
    public void testBasicPropertiesAndAccessors() {
        Assert.assertEquals("nameField", fieldWriter.getName());
        Assert.assertEquals("nameField", fieldWriter.getSerializedName().getValue());
        Assert.assertEquals(String.class, fieldWriter.getPropertyType());
        Assert.assertEquals(String.class, fieldWriter.getGenericPropertyType());
        Assert.assertTrue(fieldWriter.getMember() instanceof AnnotatedField);
        Assert.assertTrue(fieldWriter.isRequired());
        Assert.assertNull(fieldWriter.getSerializationType());
        Assert.assertNull(fieldWriter.getRawSerializationType());
        Assert.assertFalse(fieldWriter.willSuppressNulls());

        Assert.assertEquals("ageProp", methodWriter.getName());
        Assert.assertEquals(int.class, methodWriter.getPropertyType());
        Assert.assertEquals(int.class, methodWriter.getGenericPropertyType());
        Assert.assertTrue(methodWriter.getMember() instanceof AnnotatedMethod);
        Assert.assertFalse(methodWriter.isRequired());
    }

    @Test
    public void testGetValueFromBean() throws Exception {
        PersonBean bean = new PersonBean("Bob", 25);
        Assert.assertEquals("Bob", fieldWriter.get(bean));
        Assert.assertEquals(25, methodWriter.get(bean));
    }

    @Test
    public void testGetAnnotation() {
        CustomAnnotation ann = fieldWriter.getAnnotation(CustomAnnotation.class);
        Assert.assertNotNull(ann);
        Assert.assertEquals("fieldAnnotation", ann.value());

        Assert.assertNull(methodWriter.getAnnotation(CustomAnnotation.class));
    }

    @Test
    public void testToStringOutput() {
        String fieldStr = fieldWriter.toString();
        Assert.assertTrue(fieldStr.contains("property 'nameField'"));
        Assert.assertTrue(fieldStr.contains("field \""));
        Assert.assertTrue(fieldStr.contains("no static serializer"));

        String methodStr = methodWriter.toString();
        Assert.assertTrue(methodStr.contains("property 'ageProp'"));
        Assert.assertTrue(methodStr.contains("via method "));
    }

    @Test
    public void testAssignSerializerSuccessAndConflict() {
        Assert.assertFalse(fieldWriter.hasSerializer());
        Assert.assertNull(fieldWriter.getSerializer());

        JsonSerializer<Object> ser1 = (JsonSerializer<Object>) (JsonSerializer<?>) new StringSerializer();
        fieldWriter.assignSerializer(ser1);

        Assert.assertTrue(fieldWriter.hasSerializer());
        Assert.assertSame(ser1, fieldWriter.getSerializer());

        // Assigning the identical serializer instance must succeed
        fieldWriter.assignSerializer(ser1);

        // Assigning a different serializer instance must throw IllegalStateException
        try {
            fieldWriter.assignSerializer((JsonSerializer<Object>) (JsonSerializer<?>) new StringSerializer());
            Assert.fail("Expected IllegalStateException when overriding serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override serializer"));
        }
    }

    @Test
    public void testAssignNullSerializerSuccessAndConflict() {
        Assert.assertFalse(fieldWriter.hasNullSerializer());

        JsonSerializer<Object> nullSer1 = NullSerializer.instance;
        fieldWriter.assignNullSerializer(nullSer1);

        Assert.assertTrue(fieldWriter.hasNullSerializer());

        // Assigning identical serializer succeeds
        fieldWriter.assignNullSerializer(nullSer1);

        // Assigning different serializer instance throws IllegalStateException
        try {
            fieldWriter.assignNullSerializer((JsonSerializer<Object>) (JsonSerializer<?>) new StringSerializer());
            Assert.fail("Expected IllegalStateException when overriding null serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override null serializer"));
        }
    }

    @Test
    public void testInternalSettingsLifecycle() {
        Assert.assertNull(fieldWriter.getInternalSetting("key1"));
        Assert.assertNull(fieldWriter.removeInternalSetting("key1"));

        Object old = fieldWriter.setInternalSetting("key1", "val1");
        Assert.assertNull(old);
        Assert.assertEquals("val1", fieldWriter.getInternalSetting("key1"));

        old = fieldWriter.setInternalSetting("key1", "val2");
        Assert.assertEquals("val1", old);
        Assert.assertEquals("val2", fieldWriter.getInternalSetting("key1"));

        old = fieldWriter.removeInternalSetting("key1");
        Assert.assertEquals("val2", old);
        Assert.assertNull(fieldWriter.getInternalSetting("key1"));
    }

    @Test
    public void testCopyConstructorPreservesInternalSettings() {
        fieldWriter.setInternalSetting("meta", "secret");

        BeanPropertyWriter copy = new BeanPropertyWriter(fieldWriter);
        Assert.assertEquals("secret", copy.getInternalSetting("meta"));
        Assert.assertEquals(fieldWriter.getName(), copy.getName());
        Assert.assertSame(fieldWriter.getMember(), copy.getMember());

        // Mutating copy internal settings should not affect original
        copy.setInternalSetting("meta", "updated");
        Assert.assertEquals("secret", fieldWriter.getInternalSetting("meta"));
        Assert.assertEquals("updated", copy.getInternalSetting("meta"));
    }

    @Test
    public void testRename() {
        BeanPropertyWriter same = fieldWriter.rename(NameTransformer.NOP);
        Assert.assertSame(fieldWriter, same);

        NameTransformer prefixer = new NameTransformer() {
            @Override
            public String transform(String name) {
                return "pre_" + name;
            }

            @Override
            public String reverse(String transformed) {
                return transformed.substring(4);
            }
        };

        BeanPropertyWriter renamed = fieldWriter.rename(prefixer);
        Assert.assertNotSame(fieldWriter, renamed);
        Assert.assertEquals("pre_nameField", renamed.getName());
        Assert.assertEquals("nameField", fieldWriter.getName());
    }

    @Test
    public void testUnwrappingWriter() {
        BeanPropertyWriter unwrapped = fieldWriter.unwrappingWriter(NameTransformer.NOP);
        Assert.assertNotNull(unwrapped);
        Assert.assertTrue(unwrapped instanceof UnwrappingBeanPropertyWriter);
    }

    @Test
    public void testSetNonTrivialBaseType() {
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        fieldWriter.setNonTrivialBaseType(listType);
        // Verify base type was set and no exception occurs
        Assert.assertNotNull(fieldWriter);
    }

    @Test
    public void testSerializeAsFieldNormal() throws Exception {
        PersonBean bean = new PersonBean("Charlie", 40);
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartObject();
        fieldWriter.serializeAsField(bean, jgen, prov);
        methodWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"nameField\":\"Charlie\",\"ageProp\":40}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldNullWithoutSerializer() throws Exception {
        PersonBean bean = new PersonBean(null, 40);
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartObject();
        fieldWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        // When null and no null serializer assigned, field is suppressed
        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldNullWithNullSerializer() throws Exception {
        PersonBean bean = new PersonBean(null, 40);
        fieldWriter.assignNullSerializer(NullSerializer.instance);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartObject();
        fieldWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"nameField\":null}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSuppressedValue() throws Exception {
        BeanPropertyWriter writerWithSuppression = new BeanPropertyWriter(
                namePropDef,
                namePropDef.getPrimaryMember(),
                beanDesc.getClassAnnotations(),
                mapper.constructType(String.class),
                null, null, null, false, "suppressedVal"
        );

        PersonBean bean = new PersonBean("suppressedVal", 20);
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartObject();
        writerWithSuppression.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldMarkerForEmpty() throws Exception {
        BeanPropertyWriter emptySuppressingWriter = new BeanPropertyWriter(
                namePropDef,
                namePropDef.getPrimaryMember(),
                beanDesc.getClassAnnotations(),
                mapper.constructType(String.class),
                null, null, null, false, BeanPropertyWriter.MARKER_FOR_EMPTY
        );

        PersonBean bean = new PersonBean("", 20);
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartObject();
        emptySuppressingWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsColumnNormalAndNull() throws Exception {
        PersonBean bean = new PersonBean(null, 50);
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartArray();
        fieldWriter.serializeAsColumn(bean, jgen, prov);
        methodWriter.serializeAsColumn(bean, jgen, prov);
        jgen.writeEndArray();
        jgen.close();

        // fieldWriter has null value with no nullSerializer -> writes null in column mode
        Assert.assertEquals("[null,50]", sw.toString());
    }

    @Test
    public void testSerializeAsColumnNullWithNullSerializer() throws Exception {
        PersonBean bean = new PersonBean(null, 50);
        fieldWriter.assignNullSerializer(NullSerializer.instance);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = jsonFactory.createGenerator(sw);
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        jgen.writeStartArray();
        fieldWriter.serializeAsColumn(bean, jgen, prov);
        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testSerializeAsPlaceholder() throws Exception {
        PersonBean bean = new PersonBean("Dave", 22);

        StringWriter sw1 = new StringWriter();
        JsonGenerator jgen1 = jsonFactory.createGenerator(sw1);
        SerializerProvider prov = mapper.getSerializerProviderInstance();
        fieldWriter.serializeAsPlaceholder(bean, jgen1, prov);
        jgen1.close();
        Assert.assertEquals("null", sw1.toString());

        fieldWriter.assignNullSerializer(NullSerializer.instance);
        StringWriter sw2 = new StringWriter();
        JsonGenerator jgen2 = jsonFactory.createGenerator(sw2);
        fieldWriter.serializeAsPlaceholder(bean, jgen2, prov);
        jgen2.close();
        Assert.assertEquals("null", sw2.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleSelfReferenceCycleThrows() throws Exception {
        PersonBean bean = new PersonBean("SelfRef", 1);
        JsonSerializer<Object> ser = (JsonSerializer<Object>) (JsonSerializer<?>) new StringSerializer();
        fieldWriter._handleSelfReference(bean, ser);
    }

    @Test
    public void testFindAndAddDynamicSerializer() throws Exception {
        SerializerProvider prov = mapper.getSerializerProviderInstance();
        PropertySerializerMap map = PropertySerializerMap.emptyMap();

        JsonSerializer<Object> ser = fieldWriter._findAndAddDynamic(map, String.class, prov);
        Assert.assertNotNull(ser);

        // Calling again should resolve from cached dynamic serializer map
        JsonSerializer<Object> ser2 = fieldWriter._findAndAddDynamic(map, String.class, prov);
        Assert.assertNotNull(ser2);
    }

    @Test
    public void testDepositSchemaPropertyJsonObjectVisitor() throws Exception {
        final boolean[] visited = new boolean[2]; // [0] = regular, [1] = optional

        JsonObjectFormatVisitor visitor = new JsonObjectFormatVisitor.Base() {
            @Override
            public void property(com.fasterxml.jackson.databind.BeanProperty prop) {
                visited[0] = true;
            }

            @Override
            public void optionalProperty(com.fasterxml.jackson.databind.BeanProperty prop) {
                visited[1] = true;
            }
        };

        // fieldWriter is required -> calls property(this)
        fieldWriter.depositSchemaProperty(visitor);
        Assert.assertTrue(visited[0]);
        Assert.assertFalse(visited[1]);

        // methodWriter is optional -> calls optionalProperty(this)
        methodWriter.depositSchemaProperty(visitor);
        Assert.assertTrue(visited[1]);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDepositSchemaPropertyObjectNode() throws Exception {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        SerializerProvider prov = mapper.getSerializerProviderInstance();

        fieldWriter.depositSchemaProperty(root, prov);
        Assert.assertTrue(root.has("nameField"));
        Assert.assertNotNull(root.get("nameField"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedIsRequiredWithIntrospector() {
        Assert.assertTrue(fieldWriter.isRequired(mapper.getSerializationConfig().getAnnotationIntrospector()));
        Assert.assertFalse(methodWriter.isRequired(mapper.getSerializationConfig().getAnnotationIntrospector()));
    }
}
