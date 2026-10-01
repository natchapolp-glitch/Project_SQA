package org.joda.time.field;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest extends TestCase {

    public UnsupportedDurationFieldTest(String name) {
        super(name);
    }

    public void testBasicProperties() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertNotNull(field);
        assertSame(DurationFieldType.days(), field.getType());
        assertEquals("days", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
        assertEquals(0L, field.getUnitMillis());
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    public void testUnsupportedOperations_getValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());

        try {
            field.getValue(100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("hours field is unsupported", ex.getMessage());
        }

        try {
            field.getValue(100L, 50L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("hours field is unsupported", ex.getMessage());
        }

        try {
            field.getValueAsLong(100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("hours field is unsupported", ex.getMessage());
        }

        try {
            field.getValueAsLong(100L, 50L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("hours field is unsupported", ex.getMessage());
        }
    }

    public void testUnsupportedOperations_getMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());

        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("minutes field is unsupported", ex.getMessage());
        }

        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("minutes field is unsupported", ex.getMessage());
        }

        try {
            field.getMillis(5, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("minutes field is unsupported", ex.getMessage());
        }

        try {
            field.getMillis(5L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("minutes field is unsupported", ex.getMessage());
        }
    }

    public void testUnsupportedOperations_addAndDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());

        try {
            field.add(1000L, 10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("seconds field is unsupported", ex.getMessage());
        }

        try {
            field.add(1000L, 10L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("seconds field is unsupported", ex.getMessage());
        }

        try {
            field.getDifference(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("seconds field is unsupported", ex.getMessage());
        }

        try {
            field.getDifferenceAsLong(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("seconds field is unsupported", ex.getMessage());
        }
    }

    public void testEqualsAndHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.years());

        assertSame(field1, field2);
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals("months"));
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(0, field1.compareTo(field2));
        assertEquals(0, field1.compareTo(null));
    }

    public void testSerialization() throws Exception {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(field);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        UnsupportedDurationField result = (UnsupportedDurationField) ois.readObject();
        ois.close();

        assertSame(field, result);
    }
}
