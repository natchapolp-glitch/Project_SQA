package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartialConstruction() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNotNull(partial.getChronology());
        assertEquals(ISOChronology.getInstanceUTC(), partial.getChronology());
    }

    @Test
    public void testPartialWithFieldAndWithout() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        Partial updated = partial.with(DateTimeFieldType.monthOfYear( ), 6);
        assertEquals(2, updated.size());
        assertEquals(2023, updated.get(DateTimeFieldType.year()));
        assertEquals(6, updated.get(DateTimeFieldType.monthOfYear()));

        Partial reduced = updated.without(DateTimeFieldType.year());
        assertEquals(1, reduced.size());
        assertEquals(6, reduced.get(DateTimeFieldType.monthOfYear()));
    }

    @Test
    public void testPartialWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.monthOfYear(), 5);
        Partial added = partial.withFieldAdded(DurationFieldType.months(), 3);
        assertEquals(8, added.get(DateTimeFieldType.monthOfYear()));
    }

    @Test
    public void testPartialProperty() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial.Property property = partial.property(DateTimeFieldType.dayOfMonth());
        assertNotNull(property);
        assertEquals(15, property.get());
        assertEquals("dayOfMonth", property.getName());
    }

    @Test
    public void testPartialIsMatch() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023)
                .with(DateTimeFieldType.monthOfYear(), 6);
        DateTime instant = new DateTime(2023, 6, 15, 12, 0, ISOChronology.getInstanceUTC());
        assertTrue(partial.isMatch(instant));
    }
}
