package org.joda.time.field;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

/**
 * Regression tests for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest extends TestCase {

    public UnsupportedDurationFieldTest(String name) {
        super(name);
    }

    public void testSingletonAndProperties() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertSame("Instances for same type should be cached singletons", field1, field2);

        assertEquals(DurationFieldType.years(), field1.getType());
        assertEquals("years", field1.getName());
        assertFalse(field1.isSupported());
        assertTrue(field1.isPrecise());
    }

    public void testUnsupportedCalculations() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testUnsupportedCalculationsWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.getValue(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getValueAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testUnsupportedAddAndDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            field.add(100L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.add(100L, 5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getDifference(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getDifferenceAsLong(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testCompareToAndUnitMillis() {
        UnsupportedDurationField years = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField months = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertEquals(0L, years.getUnitMillis());
        assertEquals(0, years.compareTo(months));
    }

    public void testEqualsAndHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());

        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals("seconds"));

        assertEquals(field1.hashCode(), field2.hashCode());
        assertEquals("UnsupportedDurationField[seconds]", field1.toString());
    }

    public void testSerialization() throws Exception {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(field);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object deserialized = ois.readObject();
        ois.close();

        assertSame(field, deserialized);
    }
}
