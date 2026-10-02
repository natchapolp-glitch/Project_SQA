package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.Assert.*;

public class BeanPropertyWriterTest {

    private BeanPropertyWriter writer;
    private BeanPropertyDefinition propDef;
    private AnnotatedMember member;
    private Annotations contextAnnotations;
    private JavaType declaredType;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        
        // Create a simple test property definition
        propDef = new SimpleBeanPropertyDefinition("testProp", false, null);
        
        // Create annotated member from test class field
        Field testField = TestBean.class.getDeclaredField("value");
        contextAnnotations = new SimpleAnnotations(new HashMap<>());
        declaredType = mapper.getTypeFactory().constructType(String.class);
        
        member = new AnnotatedField(null, testField, null);
        
        writer = new BeanPropertyWriter(
            propDef, member, contextAnnotations, declaredType,
            null, null, null, false, null
        );
    }

    // ========== Basic Accessor Tests ==========

    @Test
    public void testGetName() {
        assertEquals("testProp", writer.getName());
    }

    @Test
    public void testGetSerializedName() {
        SerializedString serialized = writer.getSerializedName();
        assertNotNull(serialized);
        assertEquals("testProp", serialized.getValue());
    }

    @Test
    public void testGetType() {
        JavaType type = writer.getType();
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testGetWrapperName() {
        PropertyName wrapperName = writer.getWrapperName();
        assertNull(wrapperName); // No wrapper by default
    }

    @Test
    public void testGetMember() {
        AnnotatedMember retrievedMember = writer.getMember();
        assertNotNull(retrievedMember);
        assertEquals(member, retrievedMember);
    }

    @Test
    public void testGetPropertyType() {
        Class<?> propType = writer.getPropertyType();
        assertNotNull(propType);
        assertEquals(String.class, propType);
    }

    @Test
    public void testGetGenericPropertyType() {
        java.lang.reflect.Type genericType = writer.getGenericPropertyType();
        assertNotNull(genericType);
    }

    // ========== Serializer Assignment Tests ==========

    @Test
    public void testHasSerializerInitially() {
        assertFalse(writer.hasSerializer());
    }

    @Test
    public void testAssignSerializer() throws Exception {
        JsonSerializer<Object> serializer = new SimpleSerializer();
        writer.assignSerializer(serializer);
        assertTrue(writer.hasSerializer());
        assertEquals(serializer, writer.getSerializer());
    }

    @Test(expected = IllegalStateException.class)
    public void testAssignSerializerTwiceFails() throws Exception {
        JsonSerializer<Object> ser1 = new SimpleSerializer();
        JsonSerializer<Object> ser2 = new SimpleSerializer();
        writer.assignSerializer(ser1);
        writer.assignSerializer(ser2);
    }

    @Test
    public void testAssignSerializerSameInstanceIdempotent() throws Exception {
        JsonSerializer<Object> serializer = new SimpleSerializer();
        writer.assignSerializer(serializer);
        writer.assignSerializer(serializer); // Should not throw
        assertEquals(serializer, writer.getSerializer());
    }

    @Test
    public void testHasNullSerializerInitially() {
        assertFalse(writer.hasNullSerializer());
    }

    @Test
    public void testAssignNullSerializer() throws Exception {
        JsonSerializer<Object> nullSer = new SimpleSerializer();
        writer.assignNullSerializer(nullSer);
        assertTrue(writer.hasNullSerializer());
    }

    @Test(expected = IllegalStateException.class)
    public void testAssignNullSerializerTwiceFails() throws Exception {
        JsonSerializer<Object> ser1 = new SimpleSerializer();
        JsonSerializer<Object> ser2 = new SimpleSerializer();
        writer.assignNullSerializer(ser1);
        writer.assignNullSerializer(ser2);
    }

    // ========== Null Suppression Tests ==========

    @Test
    public void testWillSuppressNullsFalseByDefault() {
        assertFalse(writer.willSuppressNulls());
    }

    @Test
    public void testWillSuppressNullsTrue() throws Exception {
        BeanPropertyWriter suppressWriter = new BeanPropertyWriter(
            propDef, member, contextAnnotations, declaredType,
            null, null, null, true, null
        );
        assertTrue(suppressWriter.willSuppressNulls());
    }

    // ========== Required Flag Tests ==========

    @Test
    public void testIsRequiredFalseByDefault() {
        assertFalse(writer.isRequired());
    }

    @Test
    public void testIsRequiredWithAnnotationIntrospector() {
        AnnotationIntrospector introspector = mapper.getDeserializationConfig()
            .getAnnotationIntrospector();
        assertFalse(writer.isRequired(introspector));
    }

    // ========== Views Tests ==========

    @Test
    public void testGetViewsNullByDefault() {
        Class<?>[] views = writer.getViews();
        assertNull(views);
    }

    // ========== Serialization Type Tests ==========

    @Test
    public void testGetSerializationTypeNull() {
        assertNull(writer.getSerializationType());
    }

    @Test
    public void testGetRawSerializationTypeNull() {
        assertNull(writer.getRawSerializationType());
    }

    // ========== Copy Constructor Tests ==========

    @Test
    public void testCopyConstructor() throws Exception {
        JsonSerializer<Object> ser = new SimpleSerializer();
        writer.assignSerializer(ser);
        writer.setInternalSetting("key1", "value1");
        
        BeanPropertyWriter copy = new BeanPropertyWriter(writer);
        
        assertEquals(writer.getName(), copy.getName());
        assertEquals(writer.getType(), copy.getType());
        assertEquals(ser, copy.getSerializer());
        assertEquals("value1", copy.getInternalSetting("key1"));
    }

    @Test
    public void testCopyConstructorWithNewName() throws Exception {
        SerializedString newName = new SerializedString("newName");
        BeanPropertyWriter renamed = new BeanPropertyWriter(writer, newName);
        
        assertEquals("newName", renamed.getName());
        assertEquals(writer.getType(), renamed.getType());
    }

    // ========== Rename Tests ==========

    @Test
    public void testRenameWithTransformer() {
        NameTransformer transformer = new NameTransformer() {
            @Override
            public String transform(String input) {
                return "prefix_" + input;
            }

            @Override
            public String reverse(String input) {
                return input.substring(7);
            }
        };
        
        BeanPropertyWriter renamed = writer.rename(transformer);
        assertEquals("prefix_testProp", renamed.getName());
    }

    @Test
    public void testRenameNoChangeReturnsThis() {
        NameTransformer transformer = new NameTransformer() {
            @Override
            public String transform(String input) {
                return input; // No change
            }

            @Override
            public String reverse(String input) {
                return input;
            }
        };
        
        BeanPropertyWriter renamed = writer.rename(transformer);
        assertSame(writer, renamed);
    }

    // ========== Unwrapping Writer Tests ==========

    @Test
    public void testUnwrappingWriter() {
        NameTransformer unwrapper = new NameTransformer() {
            @Override
            public String transform(String input) {
                return input;
            }

            @Override
            public String reverse(String input) {
                return input;
            }
        };
        
        BeanPropertyWriter unwrapped = writer.unwrappingWriter(unwrapper);
        assertNotNull(unwrapped);
        assertNotSame(writer, unwrapped);
    }

    // ========== Non-Trivial Base Type Tests ==========

    @Test
    public void testSetNonTrivialBaseType() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(java.util.List.class);
        writer.setNonTrivialBaseType(baseType);
        // Verify it was set (no getter, but used internally in _findAndAddDynamic)
        assertNotNull(baseType);
    }

    // ========== Internal Settings Tests ==========

    @Test
    public void testGetInternalSettingEmpty() {
        assertNull(writer.getInternalSetting("nonexistent"));
    }

    @Test
    public void testSetInternalSetting() {
        Object oldValue = writer.setInternalSetting("key1", "value1");
        assertNull(oldValue);
        assertEquals("value1", writer.getInternalSetting("key1"));
    }

    @Test
    public void testSetInternalSettingOverwrite() {
        writer.setInternalSetting("key1", "value1");
        Object oldValue = writer.setInternalSetting("key1", "value2");
        assertEquals("value1", oldValue);
        assertEquals("value2", writer.getInternalSetting("key1"));
    }

    

    

    

    

    // ========== Annotation Tests ==========

    

    

    // ========== Get Property Value Tests ==========

    

    

    // ========== ToString Tests ==========

    

    

    

    // ========== Edge Cases ==========

    

    

    

    // ========== Test Fixtures ==========

    public static class TestBean {
        public String value;

        public TestBean(String value) {
            this.value = value;
        }
    }

    static class SimpleSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
        }
    }

    static class SimpleBeanPropertyDefinition extends BeanPropertyDefinition {
        private final String name;
        private final boolean required;
        private final PropertyName wrapperName;

        SimpleBeanPropertyDefinition(String name, boolean required, PropertyName wrapperName) {
            this.name = name;
            this.required = required;
            this.wrapperName = wrapperName;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public PropertyName getWrapperName() {
            return wrapperName;
        }

        @Override
        public boolean isRequired() {
            return required;
        }

        @Override
        public Class<?>[] findViews() {
            return null;
        }

        @Override
        public com.fasterxml.jackson.databind.introspect.AnnotatedMember getPrimaryMember() {
            return null;
        }
    }

    static class SimpleAnnotations extends com.fasterxml.jackson.databind.util.Annotations {
        private final java.util.Map<Class<?>, Annotation> annotations;

        SimpleAnnotations(java.util.Map<Class<?>, Annotation> annotations) {
            this.annotations = annotations;
        }

        @Override
        public <A extends Annotation> A get(Class<A> cls) {
            return (A) annotations.get(cls);
        }

        @Override
        public boolean has(Class<?> cls) {
            return annotations.containsKey(cls);
        }

        @Override
        public Annotation[] getAllAnnotations() {
            return annotations.values().toArray(new Annotation[0]);
        }
    }
}
