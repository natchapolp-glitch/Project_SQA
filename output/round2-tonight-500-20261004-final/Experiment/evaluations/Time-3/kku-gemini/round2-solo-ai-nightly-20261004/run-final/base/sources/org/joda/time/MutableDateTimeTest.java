package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class MutableDateTimeTest {

    @Test
    public void testDefaultConstructorAndNow() {
        MutableDateTime mdt1 = new MutableDateTime();
        MutableDateTime mdt2 = MutableDateTime.now();
        assertNotNull(mdt1);
        assertNotNull(mdt2);
        assertTrue(mdt1.getMillis() <= System.currentTimeMillis());
    }

    @Test
    public void testZoneAndChronologyConstructors() {
        DateTimeZone zone = DateTimeZone.forID("UTC");
        Chronology chrono = ISOChronology.getInstance(zone);
        
        MutableDateTime mdt = new MutableDateTime(2023, 6, 15, 12, 30, 0, 0, chrono);
        assertEquals(zone, mdt.getZone());
        assertEquals(chrono, mdt.getChronology());
        assertEquals(2023, mdt.getYear());
        assertEquals(6, mdt.getMonthOfYear());
        assertEquals(15, mdt.getDayOfMonth());
    }

    @Test
    public void testParseMethods() {
        MutableDateTime mdt = MutableDateTime.parse("2020-02-29T15:45:30.000Z");
        assertEquals(2020, mdt.getYear());
        assertEquals(2, mdt.getMonthOfYear());
        assertEquals(29, mdt.getDayOfMonth());
        assertEquals(15, mdt.getHourOfDay());
        assertEquals(45, mdt.getMinuteOfHour());
        assertEquals(30, mdt.getSecondOfMinute());
    }

    @Test
    public void testSettersAndAdders() {
        MutableDateTime mdt = new MutableDateTime(2023, 1, 15, 10, 0, 0, 0);
        mdt.setYear(2024);
        mdt.addMonths(2);
        mdt.addDays(5);
        mdt.addHours(3);
        mdt.addMinutes(15);
        mdt.addSeconds(10);
        mdt.addMillis(500);

        assertEquals(2024, mdt.getYear());
        assertEquals(3, mdt.getMonthOfYear());
        assertEquals(20, mdt.getDayOfMonth());
        assertEquals(13, mdt.getHourOfDay());
        assertEquals(15, mdt.getMinuteOfHour());
        assertEquals(10, mdt.getSecondOfMinute());
        assertEquals(500, mdt.getMillisOfSecond());
    }

    @Test
    public void testSetDateAndTimeBulk() {
        MutableDateTime mdt = new MutableDateTime(0L);
        mdt.setDate(2015, 10, 5);
        mdt.setTime(14, 20, 35, 123);

        assertEquals(2015, mdt.getYear());
        assertEquals(10, mdt.getMonthOfYear());
        assertEquals(5, mdt.getDayOfMonth());
        assertEquals(14, mdt.getHourOfDay());
        assertEquals(20, mdt.getMinuteOfHour());
        assertEquals(35, mdt.getSecondOfMinute());
        assertEquals(123, mdt.getMillisOfSecond());
    }

    @Test
    public void testRoundingConfiguration() {
        MutableDateTime mdt = new MutableDateTime(2023, 6, 15, 12, 34, 56, 789);
        DateTimeField field = mdt.getChronology().minuteOfHour();
        mdt.setRounding(field, MutableDateTime.ROUND_FLOOR);

        assertEquals(field, mdt.getRoundingField());
        assertEquals(MutableDateTime.ROUND_FLOOR, mdt.getRoundingMode());
    }

    @Test
    public void testZoneRetainFields() {
        MutableDateTime mdt = new MutableDateTime(2023, 6, 15, 12, 0, 0, 0, DateTimeZone.forID("UTC"));
        mdt.setZoneRetainFields(DateTimeZone.forID("America/New_York"));
        assertEquals(DateTimeZone.forID("America/New_York"), mdt.getZone());
        assertEquals(12, mdt.getHourOfDay());
    }

    @Test
    public void testPropertiesAndCloning() {
        MutableDateTime mdt = new MutableDateTime(2023, 5, 10, 8, 0, 0, 0);
        MutableDateTime clone = (MutableDateTime) mdt.clone();
        assertEquals(mdt, clone);

        mdt.property(DateTimeFieldType.dayOfMonth()).add(5);
        assertEquals(15, mdt.getDayOfMonth());
        assertEquals(10, clone.getDayOfMonth());
    }
}
