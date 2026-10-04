package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PartialTest {

    @Test
    public void testSingleFieldConstructor() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 5);
        assertEquals(1, p.size());
        assertEquals(DateTimeFieldType.monthOfYear(), p.getFieldType(0));
        assertEquals(5, p.getValue(0));
    }

    @Test
    public void testWithFieldAddWrappedUnsupportedFieldThrows() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        try {
            p.withFieldAddWrapped(DurationFieldType.hours(), 1);
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testWithFieldAddWrappedZeroAmountReturnsEqual() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        Partial result = p.withFieldAddWrapped(DurationFieldType.months(), 0);
        assertEquals(p, result);
    }

    @Test
    public void testWithFieldAddWrappedWrapsWithinRange() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 12);
        Partial result = p.withFieldAddWrapped(DurationFieldType.months(), 1);
        assertEquals(1, result.getValue(0));
    }

    @Test
    public void testWithAndWithoutImmutability() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        Partial withDay = p.with(DateTimeFieldType.dayOfMonth(), 15);
        assertEquals(1, p.size());
        assertEquals(2, withDay.size());
        assertEquals(6, p.getValue(0));

        Partial removed = withDay.without(DateTimeFieldType.dayOfMonth());
        assertEquals(1, removed.size());
        assertEquals(DateTimeFieldType.monthOfYear(), removed.getFieldType(0));
    }

    @Test
    public void testEmptyPartialFormatterNoException() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        assertNotNull(p.toString());
    }
}
