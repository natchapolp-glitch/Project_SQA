package org.joda.time.field;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest extends TestCase {

    public UnsupportedDurationFieldTest(String name) {
        super(name);
    }

    public void testBasicProperties() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(DurationFieldType.years(), field.getType());
        assertEquals("years", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
        assertEquals(0L, field.getUnitMillis());
        assertEquals(0, field.compareTo(field));
    }

    public void testSingletonAndEqualsHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertSame(field1, field2);
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals("years"));

        assertEquals(field1.hashCode(), field2.hashCode());
        assertEquals("UnsupportedDurationField[years]", field1.toString());
    }

    public void testSerialization() throws Exception {
        UnsupportedDurationField original = UnsupportedDurationField.getInstance(DurationFieldType.days());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object deserialized = ois.readObject();
        ois.close();

        assertSame(original, deserialized);
    }

    public void testUnsupportedOperations_getValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());

        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("seconds field is unsupported", expected.getMessage());
        }

        try {
            field.getValue(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("seconds field is unsupported", expected.getMessage());
        }

        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("seconds field is unsupported", expected.getMessage());
        }

        try {
            field.getValueAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("seconds field is unsupported", expected.getMessage());
        }
    }

    public void testUnsupportedOperations_getMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());

        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("hours field is unsupported", expected.getMessage());
        }

        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("hours field is unsupported", expected.getMessage());
        }

        try {
            field.getMillis(5, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("hours field is unsupported", expected.getMessage());
        }

        try {
            field.getMillis(5L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("hours field is unsupported", expected.getMessage());
        }
    }

    public void testUnsupportedOperations_addAndDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());

        try {
            field.add(100L, 10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("minutes field is unsupported", expected.getMessage());
        }

        try {
            field.add(100L, 10L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("minutes field is unsupported", expected.getMessage());
        }

        try {
            field.getDifference(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("minutes field is unsupported", expected.getMessage());
        }

        try {
            field.getDifferenceAsLong(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            assertEquals("minutes field is unsupported", expected.getMessage());
        }
    }
}
