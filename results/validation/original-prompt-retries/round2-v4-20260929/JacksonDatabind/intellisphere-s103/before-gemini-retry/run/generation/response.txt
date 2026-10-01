Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
// com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link BeanPropertyWriter} focusing on the documented reference behavior.
 * Uses mocks for dependencies that are not easily instantiated.
 */
public class BeanPropertyWriterTest {

    // --- Helper to create a mock BeanPropertyDefinition ---
    private BeanPropertyDefinition mockPropDef(String name, boolean required) {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn(name);
        when(propDef.isRequired()).thenReturn(required);
        return propDef;
    }

    // --- Test hasSerializer ---
    @Test
    public void testHasSerializerWhenNull() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        assertFalse(writer.hasSerializer());
    }

    @Test
    public void testHasSerializerWhenNotNull() throws Exception {
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), ser);
        assertTrue(writer.hasSerializer());
    }

    // --- Test hasNullSerializer ---
    @Test
    public void testHasNullSerializerWhenNull() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        assertFalse(writer.hasNullSerializer());
    }

    @Test
    public void testHasNullSerializerAfterAssignment() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        writer.assignNullSerializer(mock(JsonSerializer.class));
        assertTrue(writer.hasNullSerializer());
    }

    // --- Test willSuppressNulls ---
    @Test
    public void testWillSuppressNullsTrue() throws Exception {
        BeanPropertyWriter writer = createWriterWithSuppression(true);
        assertTrue(writer.willSuppressNulls());
    }

    @Test
    public void testWillSuppressNullsFalse() throws Exception {
        BeanPropertyWriter writer = createWriterWithSuppression(false);
        assertFalse(writer.willSuppressNulls());
    }

    // --- Test isRequired ---
    @Test
    public void testIsRequiredTrue() throws Exception {
        BeanPropertyWriter writer = createWriterWithSuppressionAndRequired(true, true);
        assertTrue(writer.isRequired());
    }

    @Test
    public void testIsRequiredFalse() throws Exception {
        BeanPropertyWriter writer = createWriterWithSuppressionAndRequired(true, false);
        assertFalse(writer.isRequired());
    }

    // --- Test getSerializedName ---
    @Test
    public void testGetSerializedName() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndName("testProp", mockFieldMember(), null);
        assertEquals("testProp", writer.getSerializedName().getValue());
    }

    // --- Test getName ---
    @Test
    public void testGetNameMatchesSerializedName() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndName("testProp", mockFieldMember(), null);
        assertEquals("testProp", writer.getName());
    }

    // --- Test getType ---
    @Test
    public void testGetTypeReturnsDeclaredType() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        JavaType declaredType = writer.getType();
        assertNotNull(declaredType);
    }

    // --- Test getWrapperName ---
    @Test
    public void testGetWrapperNameNotNull() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        // getWrapperName may be null if not set in propDef; mock returns null by default
        assertNull(writer.getWrapperName());
    }

    // --- Test getMember ---
    @Test
    public void testGetMemberReturnsSameInstance() throws Exception {
        AnnotatedMember member = mockFieldMember();
        BeanPropertyWriter writer = createWriterWithMember(member, null);
        assertSame(member, writer.getMember());
    }

    // --- Test assignSerializer ---
    @Test
    public void testAssignSerializerSuccess() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        writer.assignSerializer(ser);
        assertSame(ser, writer.getSerializer());
    }

    @Test(expected = IllegalStateException.class)
    public void testAssignSerializerOverrideThrows() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), mock(JsonSerializer.class));
        writer.assignSerializer(mock(JsonSerializer.class));
    }

    // --- Test assignNullSerializer ---
    @Test(expected = IllegalStateException.class)
    public void testAssignNullSerializerOverrideThrows() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        writer.assignNullSerializer(mock(JsonSerializer.class));
        writer.assignNullSerializer(mock(JsonSerializer.class));
    }

    // --- Test getSerializationType ---
    @Test
    public void testGetSerializationTypeWhenConfigured() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndSerType(mockFieldMember(), null,
                com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(String.class));
        assertNotNull(writer.getSerializationType());
    }

    // --- Test getRawSerializationType ---
    @Test
    public void testGetRawSerializationTypeReturnsNullWhenSerTypeNull() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndSerType(mockFieldMember(), null, null);
        assertNull(writer.getRawSerializationType());
    }

    // --- Test rename ---
    @Test
    public void testRenameReturnsNewWriterWhenNameChanges() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndName("oldName", mockFieldMember(), null);
        NameTransformer transformer = NameTransformer.simpleTransformer("newPrefix", null);
        BeanPropertyWriter renamed = writer.rename(transformer);
        assertNotSame(writer, renamed);
        assertTrue(renamed.getName().startsWith("newPrefix"));
    }

    @Test
    public void testRenameReturnsSameWriterWhenNoChange() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndName("name", mockFieldMember(), null);
        NameTransformer transformer = NameTransformer.NOP;
        BeanPropertyWriter renamed = writer.rename(transformer);
        assertSame(writer, renamed);
    }

    // --- Test get ---
    @Test
    public void testGetFromField() throws Exception {
        TestBean bean = new TestBean();
        bean.publicField = "fieldValue";
        BeanPropertyWriter writer = createWriterForField(TestBean.class.getField("publicField"));
        assertEquals("fieldValue", writer.get(bean));
    }

    @Test
    public void testGetFromMethod() throws Exception {
        TestBean bean = new TestBean();
        bean.setValue(42);
        BeanPropertyWriter writer = createWriterForMethod(TestBean.class.getMethod("getValue"));
        assertEquals(42, writer.get(bean));
    }

    // --- Test getPropertyType ---
    @Test
    public void testGetPropertyTypeForField() throws Exception {
        BeanPropertyWriter writer = createWriterForField(TestBean.class.getField("publicField"));
        assertEquals(String.class, writer.getPropertyType());
    }

    @Test
    public void testGetPropertyTypeForMethod() throws Exception {
        BeanPropertyWriter writer = createWriterForMethod(TestBean.class.getMethod("getValue"));
        assertEquals(int.class, writer.getPropertyType());
    }

    // --- Test getGenericPropertyType ---
    @Test
    public void testGetGenericPropertyTypeForField() throws Exception {
        BeanPropertyWriter writer = createWriterForField(TestBean.class.getField("publicField"));
        assertEquals(String.class, writer.getGenericPropertyType());
    }

    @Test
    public void testGetGenericPropertyTypeForMethod() throws Exception {
        BeanPropertyWriter writer = createWriterForMethod(TestBean.class.getMethod("getValue"));
        assertEquals(int.class, writer.getGenericPropertyType());
    }

    // --- Test getViews ---
    @Test
    public void testGetViewsNotNull() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        assertNotNull(writer.getViews());
    }

    // --- Test getInternalSetting, setInternalSetting, removeInternalSetting ---
    @Test
    public void testInternalSettingsLifecycle() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        assertNull(writer.getInternalSetting("key"));

        Object old = writer.setInternalSetting("key", "value");
        assertNull(old);
        assertEquals("value", writer.getInternalSetting("key"));

        old = writer.setInternalSetting("key", "newValue");
        assertEquals("value", old);
        assertEquals("newValue", writer.getInternalSetting("key"));

        Object removed = writer.removeInternalSetting("key");
        assertEquals("newValue", removed);
        assertNull(writer.getInternalSetting("key"));

        removed = writer.removeInternalSetting("missing");
        assertNull(removed);
    }

    // --- Test toString ---
    @Test
    public void testToStringContainsName() throws Exception {
        BeanPropertyWriter writer = createWriterWithMemberAndName("myProp", mockFieldMember(), null);
        assertTrue(writer.toString().contains("myProp"));
    }

    // --- Test serializeAsPlaceholder ---
    @Test
    public void testSerializeAsPlaceholderWithoutNullSerializer() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);

        writer.serializeAsPlaceholder(new Object(), jgen, prov);
        verify(jgen).writeNull();
    }

    @Test
    public void testSerializeAsPlaceholderWithNullSerializer() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        JsonSerializer<Object> nullSer = mock(JsonSerializer.class);
        writer.assignNullSerializer(nullSer);

        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);

        writer.serializeAsPlaceholder(new Object(), jgen, prov);
        verify(nullSer).serialize(eq(null), eq(jgen), eq(prov));
    }

    // --- Test unwrappingWriter ---
    @Test
    public void testUnwrappingWriterReturnsDifferentInstance() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        BeanPropertyWriter unwrapped = writer.unwrappingWriter(NameTransformer.NOP);
        assertNotNull(unwrapped);
        assertNotSame(writer, unwrapped);
    }

    // --- Test setNonTrivialBaseType ---
    @Test
    public void testSetNonTrivialBaseType() throws Exception {
        BeanPropertyWriter writer = createWriterWithMember(mockFieldMember(), null);
        JavaType type = com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(String.class);
        writer.setNonTrivialBaseType(type);
        // No direct getter, but we verify no exception is thrown
    }

    // --- Helper classes and methods ---
    public static class TestBean {
        public String publicField;
        private int value;

        public int getValue() { return value; }
        public void setValue(int v) { value = v; }
    }

    private AnnotatedField mockFieldMember() {
        try {
            Field f = TestBean.class.getField("publicField");
            AnnotatedField af = mock(AnnotatedField.class);
            when(af.getMember()).thenReturn(f);
            when(af.getAnnotation(any())).thenReturn(null);
            return af;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private AnnotatedMethod mockMethodMember() {
        try {
            Method m = TestBean.class.getMethod("getValue");
            AnnotatedMethod am = mock(AnnotatedMethod.class);
            when(am.getMember()).thenReturn(m);
            when(am.getAnnotation(any())).thenReturn(null);
            return am;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private BeanPropertyWriter createWriterWithMember(AnnotatedMember member, JsonSerializer<?> ser) {
        return createWriter(member, "prop", false, ser, null, false, null);
    }

    private BeanPropertyWriter createWriterWithSuppression(boolean suppressNulls) {
        return createWriter(mockFieldMember(), "prop", false, null, null, suppressNulls, null);
    }

    private BeanPropertyWriter createWriterWithSuppressionAndRequired(boolean suppressNulls, boolean required) {
        return createWriter(mockFieldMember(), "prop", required, null, null, suppressNulls, null);
    }

    private BeanPropertyWriter createWriterWithMemberAndName(String name, AnnotatedMember member, JsonSerializer<?> ser) {
        return createWriter(member, name, false, ser, null, false, null);
    }

    private BeanPropertyWriter createWriterWithMemberAndSerType(AnnotatedMember member, JsonSerializer<?> ser, JavaType serType) {
        return createWriter(member, "prop", false, ser, null, false, serType);
    }

    private BeanPropertyWriter createWriterForField(Field f) {
        try {
            AnnotatedField af = mock(AnnotatedField.class);
            when(af.getMember()).thenReturn(f);
            when(af.getAnnotation(any())).thenReturn(null);
            return createWriter(af, f.getName(), false, null, null, false, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BeanPropertyWriter createWriterForMethod(Method m) {
        try {
            AnnotatedMethod am = mock(AnnotatedMethod.class);
            when(am.getMember()).thenReturn(m);
            when(am.getAnnotation(any())).thenReturn(null);
            return createWriter(am, m.getName(), false, null, null, false, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BeanPropertyWriter createWriter(AnnotatedMember member, String name, boolean required,
                                            JsonSerializer<?> ser, TypeSerializer typeSer,
                                            boolean suppressNulls, JavaType serType) {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn(name);
        when(propDef.isRequired()).thenReturn(required);
        if (serType == null) {
            // Use a default declared type: String for methods returning String, or generic Object
            JavaType declared;
            if (member instanceof AnnotatedMethod) {
                declared = com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(
                        ((AnnotatedMethod)member).getMember().getReturnType());
            } else {
                declared = com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(Object.class);
            }
            return new BeanPropertyWriter(propDef, member, null, declared,
                    ser, typeSer, serType, suppressNulls, null);
        }
        return new BeanPropertyWriter(propDef, member, null,
                com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance().constructType(Object.class),
                ser, typeSer, serType, suppressNulls, null);
    }
}

```