com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link BeanPropertyWriter}.
 * The tests use mock objects for complex dependencies to isolate the unit.
 */
public class BeanPropertyWriterTest {

    // Helper bean used in some tests
    public static class TestBean {
        public String name = "test";
        private int id;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }

    private AnnotatedMember mockMember;
    private Annotations mockAnnotations;
    private JavaType declaredType;
    private SerializerProvider mockProvider;
    private JsonGenerator mockGenerator;
    private BeanPropertyDefinition mockPropDef;

    @Before
    public void setUp() throws Exception {
        mockMember = mock(AnnotatedField.class);
        when(mockMember.getMember()).thenReturn(TestBean.class.getField("name"));
        when(mockMember.getAnnotation(any())).thenReturn(null);

        mockAnnotations = mock(Annotations.class);
        when(mockAnnotations.get(any())).thenReturn(null);

        declaredType = mock(JavaType.class);

        mockProvider = mock(SerializerProvider.class);
        when(mockProvider.constructSpecializedType(any(JavaType.class), any(Class.class)))
            .thenReturn(mock(JavaType.class));

        mockGenerator = mock(JsonGenerator.class);
        when(mockGenerator.writeFieldName(any(SerializedString.class))).thenReturn(null);

        mockPropDef = mock(BeanPropertyDefinition.class);
        when(mockPropDef.getName()).thenReturn("testProp");
        when(mockPropDef.getWrapperName()).thenReturn(null);
        when(mockPropDef.isRequired()).thenReturn(false);
        when(mockPropDef.findViews()).thenReturn(null);
    }

    // -------------------------------------------
    // Constructor and basic accessors
    // -------------------------------------------

    @Test
    public void testConstructorFieldBased() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertNotNull(writer);
        assertEquals("testProp", writer.getName());
        assertFalse(writer.isRequired());
        assertNull(writer.getSerializer());
        assertNull(writer.getNullSerializer());
        assertFalse(writer.hasSerializer());
        assertFalse(writer.hasNullSerializer());
        assertFalse(writer.willSuppressNulls());
    }

    @Test
    public void testConstructorMethodBased() throws Exception {
        AnnotatedMethod mockMethod = mock(AnnotatedMethod.class);
        when(mockMethod.getMember()).thenReturn(TestBean.class.getMethod("getId"));
        when(mockMethod.getAnnotation(any())).thenReturn(null);

        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMethod, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertNotNull(writer);
        assertEquals("testProp", writer.getName());
    }

    @Test
    public void testGetTypeAndPropertyType() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertSame(declaredType, writer.getType());
        assertEquals(String.class, writer.getPropertyType());
    }

    @Test
    public void testGetGenericPropertyType() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertEquals(TestBean.class.getField("name").getGenericType(),
                     writer.getGenericPropertyType());
    }

    @Test
    public void testGetSerializedName() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertEquals("testProp", writer.getSerializedName().getValue());
    }

    @Test
    public void testGetViewsNullByDefault() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertNull(writer.getViews());
    }

    @Test
    public void testToStringContainsName() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertTrue(writer.toString().contains("testProp"));
    }

    // -------------------------------------------
    // _isRequired and isRequired
    // -------------------------------------------

    @Test
    public void testIsRequiredFromDefinition() throws Exception {
        when(mockPropDef.isRequired()).thenReturn(true);
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertTrue(writer.isRequired());
    }

    @Test
    public void testDeprecatedIsRequiredWithIntrospector() throws Exception {
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertFalse(writer.isRequired(introspector));
    }

    // -------------------------------------------
    // hasSerializer, hasNullSerializer, getSerializer
    // -------------------------------------------

    @Test
    public void testHasSerializerFalseWhenNull() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertFalse(writer.hasSerializer());
    }

    @Test
    public void testHasSerializerTrueWhenSet() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, mock(JsonSerializer.class), null, null,
                false, null
        );
        assertTrue(writer.hasSerializer());
    }

    @Test
    public void testHasNullSerializerInitiallyFalse() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertFalse(writer.hasNullSerializer());
    }

    // -------------------------------------------
    // willSuppressNulls
    // -------------------------------------------

    @Test
    public void testWillSuppressNullsTrue() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                true, null
        );
        assertTrue(writer.willSuppressNulls());
    }

    @Test
    public void testWillSuppressNullsFalse() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertFalse(writer.willSuppressNulls());
    }

    // -------------------------------------------
    // getWrapperName, getMember
    // -------------------------------------------

    @Test
    public void testWrapperNameIsNullByDefault() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertNull(writer.getWrapperName());
    }

    @Test
    public void testGetMember() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertSame(mockMember, writer.getMember());
    }

    // -------------------------------------------
    // assignSerializer and assignNullSerializer
    // -------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void testAssignSerializer() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        writer.assignSerializer(ser);
        assertSame(ser, writer.getSerializer());
        assertTrue(writer.hasSerializer());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAssignNullSerializer() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        JsonSerializer<Object> nullSer = mock(JsonSerializer.class);
        writer.assignNullSerializer(nullSer);
        assertSame(nullSer, writer.getNullSerializer());
        assertTrue(writer.hasNullSerializer());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalStateException.class)
    public void testAssignSerializerFailsIfOverridden() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, mock(JsonSerializer.class), null, null,
                false, null
        );
        writer.assignSerializer(mock(JsonSerializer.class));
    }

    // -------------------------------------------
    // Rename (copy constructor behavior)
    // -------------------------------------------

    @Test
    public void testRenameChangesName() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        NameTransformer transformer = NameTransformer.simpleTransformer("prefix_", null);
        BeanPropertyWriter renamed = writer.rename(transformer);
        assertNotSame(writer, renamed);
        assertEquals("prefix_testProp", renamed.getName());
        // Original name unchanged
        assertEquals("testProp", writer.getName());
    }

    @Test
    public void testRenameNoOpIfSameName() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        NameTransformer identity = NameTransformer.NOP;
        BeanPropertyWriter renamed = writer.rename(identity);
        assertSame(writer, renamed);
    }

    // -------------------------------------------
    // Internal settings
    // -------------------------------------------

    @Test
    public void testGetInternalSettingNullWhenEmpty() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        assertNull(writer.getInternalSetting("key"));
    }

    @Test
    public void testSetAndGetInternalSetting() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        Object old = writer.setInternalSetting("key", "value");
        assertNull(old);
        assertEquals("value", writer.getInternalSetting("key"));
    }

    @Test
    public void testSetInternalSettingOverwrite() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.setInternalSetting("key", "old");
        Object old = writer.setInternalSetting("key", "new");
        assertEquals("old", old);
        assertEquals("new", writer.getInternalSetting("key"));
    }

    @Test
    public void testRemoveInternalSetting() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.setInternalSetting("key", "value");
        Object removed = writer.removeInternalSetting("key");
        assertEquals("value", removed);
        assertNull(writer.getInternalSetting("key"));
    }

    @Test
    public void testRemoveInternalSettingWhenMapBecomesEmpty() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.setInternalSetting("key", "value");
        writer.removeInternalSetting("key");
        // Internal settings should be nulled after removal of last entry
        assertNull(writer.getInternalSetting("key"));
        // Second removal should be safe
        assertNull(writer.removeInternalSetting("key"));
    }

    // -------------------------------------------
    // serializeAsField (null handling)
    // -------------------------------------------

    @Test
    public void testSerializeAsFieldNullValueWithoutNullSerializer() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        TestBean bean = new TestBean();
        bean.name = null; // make field null

        // Should not throw, and no field name written
        writer.serializeAsField(bean, mockGenerator, mockProvider);
        verify(mockGenerator, never()).writeFieldName(any(SerializedString.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeAsFieldNullValueWithNullSerializer() throws Exception {
        JsonSerializer<Object> nullSer = mock(JsonSerializer.class);
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.assignNullSerializer(nullSer);

        TestBean bean = new TestBean();
        bean.name = null;

        writer.serializeAsField(bean, mockGenerator, mockProvider);
        verify(mockGenerator).writeFieldName(any(SerializedString.class));
        verify(nullSer).serialize(eq(null), eq(mockGenerator), eq(mockProvider));
    }

    // -------------------------------------------
    // serializeAsPlaceholder
    // -------------------------------------------

    @Test
    public void testSerializeAsPlaceholderNoNullSerializer() throws Exception {
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.serializeAsPlaceholder(new TestBean(), mockGenerator, mockProvider);
        verify(mockGenerator).writeNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeAsPlaceholderWithNullSerializer() throws Exception {
        JsonSerializer<Object> nullSer = mock(JsonSerializer.class);
        BeanPropertyWriter writer = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        writer.assignNullSerializer(nullSer);
        writer.serializeAsPlaceholder(new TestBean(), mockGenerator, mockProvider);
        verify(nullSer).serialize(eq(null), eq(mockGenerator), eq(mockProvider));
    }

    // -------------------------------------------
    // Copy constructor internal settings preservation
    // -------------------------------------------

    @Test
    public void testCopyConstructorPreservesInternalSettings() throws Exception {
        BeanPropertyWriter original = new BeanPropertyWriter(
                mockPropDef, mockMember, mockAnnotations,
                declaredType, null, null, null,
                false, null
        );
        original.setInternalSetting("k1", "v1");

        BeanPropertyWriter copy = new BeanPropertyWriter(original);
        assertEquals("v1", copy.getInternalSetting("k1"));
        assertNotSame(original, copy);
    }
}
