package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;

public class Time1Test {

    @Test
    public void testPartialWithFieldAddedUnsupported() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 12);
        Partial result = partial.withFieldAdded(DurationFieldType.eras(), 0);
        assertEquals(12, result.get(DateTimeFieldType.hourOfDay()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartialWithFieldAddedThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 12);
        partial.withFieldAdded(DurationFieldType.eras(), 1);
    }

    @Test
    public void testPartialPlusPeriodUnsupportedZero() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Period period = new Period(0, DurationFieldType.eras());
        Partial result = partial.plus(period);
        assertEquals(10, result.get(DateTimeFieldType.hourOfDay()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPartialPlusPeriodThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Period period = new Period(1, DurationFieldType.eras());
        partial.plus(period);
    }

    @Test
    public void testUnsupportedDurationFieldGetInstanceAndProperties() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertNotNull(field);
        assertEquals(DurationFieldType.eras(), field.getType());
        assertEquals("eras", field.getName());
        assertTrue(!field.isSupported());
        assertTrue(field.isPrecise());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedDurationFieldAddThrows() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        field.add(0L, 1);
    }
}
