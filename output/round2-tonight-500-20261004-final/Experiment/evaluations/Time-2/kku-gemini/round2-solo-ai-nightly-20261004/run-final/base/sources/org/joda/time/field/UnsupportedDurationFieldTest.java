package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstanceCaching() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertNotNull(field1);
        assertEquals(field1, field2);
        assertEquals(DurationFieldType.eras(), field1.getType());
        assertEquals("eras", field1.getName());
    }

    @Test
    public void testFieldProperties() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueThrowsException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddThrowsException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        field.add(1000L, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceThrowsException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        field.getDifference(2000L, 1000L);
    }
}
