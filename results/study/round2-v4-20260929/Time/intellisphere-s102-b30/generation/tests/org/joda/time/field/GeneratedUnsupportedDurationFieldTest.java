package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class GeneratedUnsupportedDurationFieldTest extends TestCase {

    public void testGetInstanceAndAccessors() {
        DurationFieldType type = DurationFieldType.eras();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        
        assertSame(field1, field2);
        assertEquals(type, field1.getType());
        assertEquals("eras", field1.getName());
        assertFalse(field1.isSupported());
        assertTrue(field1.isPrecise());
        assertEquals(0L, field1.getUnitMillis());
        assertEquals(0, field1.compareTo((DurationField) field1));
    }

    public void testEqualsHashCodeToString() {
        UnsupportedDurationField erasField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        UnsupportedDurationField centuriesField = UnsupportedDurationField.getInstance(DurationFieldType.centuries());

        assertTrue(erasField.equals(erasField));
        assertTrue(erasField.equals(UnsupportedDurationField.getInstance(DurationFieldType.eras())));
        assertFalse(erasField.equals(centuriesField));
        assertFalse(erasField.equals("string"));
        assertFalse(erasField.equals(null));

        assertEquals(DurationFieldType.eras().getName().hashCode(), erasField.hashCode());
        assertEquals("UnsupportedDurationField[eras]", erasField.toString());
    }

    public void testGetValueMethods() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getValue(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getValueAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}
    }

    public void testGetMillisMethods() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getMillis(5, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getMillis(5L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}
    }

    public void testAddAndDifferenceMethods() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        try {
            field.add(1000L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.add(1000L, 5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getDifference(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}

        try {
            field.getDifferenceAsLong(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {}
    }
}
