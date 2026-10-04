package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstanceCachesSingleton() {
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertSame(f1, f2);

        UnsupportedDurationField f3 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertNotEquals(f1, f3);
    }

    @Test
    public void testIsSupportedAndPrecise() {
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertFalse(f.isSupported());
        assertEquals(true, f.isPrecise());
        assertEquals(0L, f.getUnitMillis());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField f3 = UnsupportedDurationField.getInstance(DurationFieldType.days());

        assertEquals(f1, f2);
        assertFalse(f1.equals(f3));
        assertFalse(f1.equals(null));
        assertFalse(f1.equals("notAField"));
    }

    @Test
    public void testUnsupportedOperationsThrow() {
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(DurationFieldType.hours());

        try {
            f.getValue(1L);
            fail();
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            f.add(1L, 1);
            fail();
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            f.getMillis(1);
            fail();
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            f.getDifference(1L, 2L);
            fail();
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField f = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        DurationField supportedLike = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(0, f.compareTo(supportedLike));
    }
}
