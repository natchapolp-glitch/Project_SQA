package com.fasterxml.jackson.databind.ser;

import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class BeanPropertyWriterTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CustomPropAnnotation {
        String value();
    }

    public static class SampleBean {
        @CustomPropAnnotation("fieldAnnotation")
        public String fieldProp = "initialField";

        protected int count = 42;

        public SampleBean self;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    private ObjectMapper _mapper;
    private BeanPropertyWriter _fieldWriter;
    private BeanPropertyWriter _methodWriter;
    private BeanDescription _beanDesc;

    @Before
    public void setUp() {
        _mapper = new ObjectMapper();
        JavaType javaType = _mapper.constructType(SampleBean.class);
        _beanDesc = _mapper.getSerializationConfig().introspect(javaType);
        List<BeanPropertyDefinition> properties = _beanDesc.findProperties();

        for (BeanPropertyDefinition prop : properties) {
            if ("fieldProp".equals(prop.getName())) {
                AnnotatedField f = prop.getField();
                _fieldWriter = new BeanPropertyWriter(prop, f, _beanDesc.getClassAnnotations(),
                        _mapper.constructType(prop.getAccessor().getGenericType()), null, null, null, false, null);
            } else if ("count".equals(prop.getName())) {
                AnnotatedMethod m = prop.getGetter();
                _methodWriter = new BeanPropertyWriter(prop, m, _beanDesc.getClassAnnotations(),
                        _mapper.constructType(prop.getAccessor().getGenericType()), null, null, null, false, null);
            }
        }
    }

    @Test
    public void testBasicPropertiesAndAccessors() {
        Assert.assertNotNull(_fieldWriter);
        Assert.assertNotNull(_methodWriter);

        Assert.assertEquals("fieldProp", _fieldWriter.getName());
        Assert.assertEquals(new SerializedString("fieldProp"), _fieldWriter.getSerializedName());
        Assert.assertEquals(String.class, _fieldWriter.getPropertyType());
        Assert.assertEquals(String.class, _fieldWriter.getGenericPropertyType());

        Assert.assertEquals("count", _methodWriter.getName());
        Assert.assertEquals(Integer.TYPE, _methodWriter.getPropertyType());
        Assert.assertEquals(Integer.TYPE, _methodWriter.getGenericPropertyType());
        Assert.assertFalse(_methodWriter.isRequired());
        Assert.assertNull(_methodWriter.getWrapperName());
    }

    @Test
    public void testGetMemberAndAnnotations() {
        Assert.assertTrue(_fieldWriter.getMember() instanceof AnnotatedField);
        Assert.assertTrue(_methodWriter.getMember() instanceof AnnotatedMethod);

        CustomPropAnnotation annot = _fieldWriter.getAnnotation(CustomPropAnnotation.class);
        Assert.assertNotNull(annot);
        Assert.assertEquals("fieldAnnotation", annot.value());

        Assert.assertNull(_fieldWriter.getAnnotation(Override.class));
    }

    @Test
    public void testGetValueFromBean() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = "customValue";
        bean.setCount(99);

        Assert.assertEquals("customValue", _fieldWriter.get(bean));
        Assert.assertEquals(99, _methodWriter.get(bean));
    }

    

    @Test
    public void testInternalSettingsManagement() {
        Assert.assertNull(_fieldWriter.getInternalSetting("testKey"));
        Object old = _fieldWriter.setInternalSetting("testKey", "testVal");
        Assert.assertNull(old);
        Assert.assertEquals("testVal", _fieldWriter.getInternalSetting("testKey"));

        old = _fieldWriter.setInternalSetting("testKey", "newVal");
        Assert.assertEquals("testVal", old);
        Assert.assertEquals("newVal", _fieldWriter.getInternalSetting("testKey"));

        Object removed = _fieldWriter.removeInternalSetting("testKey");
        Assert.assertEquals("newVal", removed);
        Assert.assertNull(_fieldWriter.getInternalSetting("testKey"));

        // Removing non-existent or after cleanup returns null
        Assert.assertNull(_fieldWriter.removeInternalSetting("nonExistent"));
    }

    @Test
    public void testAssignSerializerSuccessAndConflict() {
        Assert.assertFalse(_fieldWriter.hasSerializer());
        JsonSerializer<Object> ser1 = ToStringSerializer.instance;
        _fieldWriter.assignSerializer(ser1);

        Assert.assertTrue(_fieldWriter.hasSerializer());
        Assert.assertSame(ser1, _fieldWriter.getSerializer());

        // Assigning the same serializer must succeed
        _fieldWriter.assignSerializer(ser1);

        // Assigning a different serializer must fail
        try {
            _fieldWriter.assignSerializer(NullSerializer.instance);
            Assert.fail("Expected IllegalStateException when reassigning different serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override serializer"));
        }
    }

    @Test
    public void testAssignNullSerializerSuccessAndConflict() {
        Assert.assertFalse(_fieldWriter.hasNullSerializer());
        JsonSerializer<Object> nullSer = NullSerializer.instance;
        _fieldWriter.assignNullSerializer(nullSer);

        Assert.assertTrue(_fieldWriter.hasNullSerializer());

        // Same serializer succeeds
        _fieldWriter.assignNullSerializer(nullSer);

        // Different serializer fails
        try {
            _fieldWriter.assignNullSerializer(ToStringSerializer.instance);
            Assert.fail("Expected IllegalStateException on overriding null serializer");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Can not override null serializer"));
        }
    }

    @Test
    public void testRename() {
        NameTransformer transformerNoOp = NameTransformer.NOP;
        BeanPropertyWriter same = _fieldWriter.rename(transformerNoOp);
        Assert.assertSame(_fieldWriter, same);

        NameTransformer prefixTransformer = NameTransformer.simpleTransformer("pre_", "");
        BeanPropertyWriter renamed = _fieldWriter.rename(prefixTransformer);
        Assert.assertNotSame(_fieldWriter, renamed);
        Assert.assertEquals("pre_fieldProp", renamed.getName());
        Assert.assertEquals(_fieldWriter.getType(), renamed.getType());
    }

    @Test
    public void testUnwrappingWriter() {
        NameTransformer transformer = NameTransformer.simpleTransformer("unwrapped_", "");
        BeanPropertyWriter unwrapping = _fieldWriter.unwrappingWriter(transformer);
        Assert.assertTrue(unwrapping instanceof UnwrappingBeanPropertyWriter);
    }

    @Test
    public void testToStringFormat() {
        String methodStr = _methodWriter.toString();
        Assert.assertTrue(methodStr.contains("property 'count'"));
        Assert.assertTrue(methodStr.contains("via method"));

        String fieldStr = _fieldWriter.toString();
        Assert.assertTrue(fieldStr.contains("property 'fieldProp'"));
        Assert.assertTrue(fieldStr.contains("field \""));
        Assert.assertTrue(fieldStr.contains("no static serializer"));

        _fieldWriter.assignSerializer(ToStringSerializer.instance);
        Assert.assertTrue(_fieldWriter.toString().contains("static serializer"));
    }

    @Test
    public void testSerializeAsFieldNormal() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = "text123";

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartObject();
        _fieldWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"fieldProp\":\"text123\"}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSuppressNull() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = null;

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartObject();
        // Since no nullSerializer is assigned, null should be suppressed entirely
        _fieldWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldWithNullSerializer() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = null;

        _fieldWriter.assignNullSerializer(NullSerializer.instance);

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartObject();
        _fieldWriter.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{\"fieldProp\":null}", sw.toString());
    }

    @Test
    public void testSerializeAsFieldSuppressedValue() throws Exception {
        BeanPropertyDefinition prop = _beanDesc.findProperties().get(0);
        for (BeanPropertyDefinition p : _beanDesc.findProperties()) {
            if ("fieldProp".equals(p.getName())) {
                prop = p;
                break;
            }
        }
        // Create writer with suppressableValue = "skipMe"
        BeanPropertyWriter writerWithSuppression = new BeanPropertyWriter(prop, prop.getField(),
                _beanDesc.getClassAnnotations(), _mapper.constructType(prop.getAccessor().getGenericType()),
                ToStringSerializer.instance, null, null, false, "skipMe");

        SampleBean bean = new SampleBean();
        bean.fieldProp = "skipMe";

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartObject();
        writerWithSuppression.serializeAsField(bean, jgen, prov);
        jgen.writeEndObject();
        jgen.close();

        Assert.assertEquals("{}", sw.toString());
    }

    @Test
    public void testSerializeAsColumnNullWithoutNullSerializer() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = null;

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartArray();
        _fieldWriter.serializeAsColumn(bean, jgen, prov);
        jgen.writeEndArray();
        jgen.close();

        // In column/tabular mode, null value outputs null token even if nullSerializer is null
        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testSerializeAsColumnWithValue() throws Exception {
        SampleBean bean = new SampleBean();
        bean.fieldProp = "colValue";

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartArray();
        _fieldWriter.serializeAsColumn(bean, jgen, prov);
        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[\"colValue\"]", sw.toString());
    }

    @Test
    public void testSerializeAsPlaceholder() throws Exception {
        SampleBean bean = new SampleBean();
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        jgen.writeStartArray();
        _fieldWriter.serializeAsPlaceholder(bean, jgen, prov);
        jgen.writeEndArray();
        jgen.close();

        Assert.assertEquals("[null]", sw.toString());
    }

    @Test
    public void testDirectSelfReferenceThrowsException() throws Exception {
        BeanPropertyDefinition prop = null;
        for (BeanPropertyDefinition p : _beanDesc.findProperties()) {
            if ("self".equals(p.getName())) {
                prop = p;
                break;
            }
        }
        Assert.assertNotNull("self property definition not found", prop);

        BeanPropertyWriter selfWriter = new BeanPropertyWriter(prop, prop.getField(),
                _beanDesc.getClassAnnotations(), _mapper.constructType(prop.getAccessor().getGenericType()),
                null, null, null, false, null);

        SampleBean bean = new SampleBean();
        bean.self = bean; // create direct cycle

        StringWriter sw = new StringWriter();
        JsonGenerator jgen = new JsonFactory().createGenerator(sw);
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        try {
            selfWriter.serializeAsField(bean, jgen, prov);
            Assert.fail("Expected JsonMappingException on direct self-reference cycle");
        } catch (JsonMappingException e) {
            Assert.assertTrue(e.getMessage().contains("Direct self-reference leading to cycle"));
        }
    }

    @Test
    public void testDepositSchemaPropertyJsonObjectFormatVisitor() throws Exception {
        final boolean[] visitedOptional = new boolean[1];
        final boolean[] visitedRequired = new boolean[1];

        JsonObjectFormatVisitor visitor = new JsonObjectFormatVisitor.Base() {
            @Override
            public void optionalProperty(com.fasterxml.jackson.databind.BeanProperty prop) {
                visitedOptional[0] = true;
            }

            @Override
            public void property(com.fasterxml.jackson.databind.BeanProperty prop) {
                visitedRequired[0] = true;
            }
        };

        // _fieldWriter is not required by default
        _fieldWriter.depositSchemaProperty(visitor);
        Assert.assertTrue(visitedOptional[0]);
        Assert.assertFalse(visitedRequired[0]);
    }

    @Test
    public void testDepositSchemaPropertyObjectNode() throws Exception {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        SerializerProvider prov = ((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory());

        _fieldWriter.depositSchemaProperty(objectNode, prov);
        Assert.assertTrue(objectNode.has("fieldProp"));
        Assert.assertNotNull(objectNode.get("fieldProp"));
    }

    @Test
    public void testSetNonTrivialBaseType() {
        JavaType baseType = _mapper.constructType(Object.class);
        _fieldWriter.setNonTrivialBaseType(baseType);
        // Method executes without error and updates internal state
    }
}
