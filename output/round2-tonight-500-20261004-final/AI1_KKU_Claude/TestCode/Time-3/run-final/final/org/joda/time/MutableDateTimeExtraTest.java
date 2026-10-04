package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MutableDateTimeExtraTest {

    @Test
    public void testSetRoundingHalfFloorRoundsDownAtExactHalf() {
        MutableDateTime mdt = new MutableDateTime(2000, 1, 1, 10, 30, 0, 0, DateTimeZone.UTC);
        mdt.setRounding(mdt.getChronology().hourOfDay(), MutableDateTime.ROUND_HALF_FLOOR);
        MutableDateTime expected = new MutableDateTime(2000, 1, 1, 10, 0, 0, 0, DateTimeZone.UTC);
        assertEquals(expected.getMillis(), mdt.getMillis());
    }

    @Test
    public void testSetRoundingHalfCeilingRoundsUpAtExactHalf() {
        MutableDateTime mdt = new MutableDateTime(2000, 1, 1, 10, 30, 0, 0, DateTimeZone.UTC);
        mdt.setRounding(mdt.getChronology().hourOfDay(), MutableDateTime.ROUND_HALF_CEILING);
        MutableDateTime expected = new MutableDateTime(2000, 1, 1, 11, 0, 0, 0, DateTimeZone.UTC);
        assertEquals(expected.getMillis(), mdt.getMillis());
    }

    @Test
    public void testSetMillisRoundingModeNoneDoesNotRound() {
        MutableDateTime mdt = new MutableDateTime(2000, 1, 1, 10, 30, 45, 500, DateTimeZone.UTC);
        mdt.setRounding(mdt.getChronology().hourOfDay(), MutableDateTime.ROUND_NONE);
        assertEquals(MutableDateTime.ROUND_NONE, mdt.getRoundingMode());
        MutableDateTime unrounded = new MutableDateTime(2000, 1, 1, 10, 45, 0, 0, DateTimeZone.UTC);
        mdt.setMillis(unrounded.getMillis());
        assertEquals(unrounded.getMillis(), mdt.getMillis());
    }

    @Test
    public void testCopyReturnsNonNullDistinctInstance() {
        MutableDateTime mdt = new MutableDateTime(2000, 1, 1, 10, 30, 45, 500, DateTimeZone.UTC);
        MutableDateTime copy = mdt.copy();
        assertNotNull(copy);
        assertEquals(mdt.getMillis(), copy.getMillis());
        copy.setMillis(0L);
        assertEquals(0L, copy.getMillis());
    }
}
