package org.joda.time;

import java.util.Locale;
import junit.framework.TestCase;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.ISOChronology;

public class GeneratedPartialTest extends TestCase {

    public void testDefaultConstructors() {
        Partial p1 = new Partial();
        assertEquals(0, p1.size());
        assertEquals(ISOChronology.getInstanceUTC(), p1.getChronology());
        assertEquals(0, p1.getFieldTypes().length);
        assertEquals(0, p1.getValues().length);

        Partial p2 = new Partial(BuddhistChronology.getInstance());
        assertEquals(0, p2.size());
        assertEquals(BuddhistChronology.getInstanceUTC(), p2.getChronology());
    }

    public void testSingleFieldConstructor() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 14);
        assertEquals(1, p.size());
        assertEquals(DateTimeFieldType.hourOfDay(), p.getFieldType(0));
        assertEquals(14, p.getValue(0));
        assertEquals(14, p.getValues()[0](undefined));
    }

    public void testSingleFieldConstructorInvalidNull() {
        try {
            new Partial((DateTimeFieldType) null, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    public void testArrayConstructorValid() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = new int[] {2023, 5, 20};
        Partial p = new Partial(types, values);
        assertEquals(3, p.size());
        assertEquals(DateTimeFieldType.year(), p.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), p.getFieldType(1));
        assertEquals(DateTimeFieldType.dayOfMonth(), p.getFieldType(2));
        assertEquals(2023, p.getValue(0));
        assertEquals(5, p.getValue(1));
        assertEquals(20, p.getValue(2));
    }

    public void testArrayConstructorInvalidOrder() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.year()
        };
        int[] values = new int[] {5, 2023};
        try {
            new Partial(types, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    public void testArrayConstructorValidationErrors() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[] {1});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.hourOfDay()}, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.hourOfDay()}, new int[] {1, 2});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        try {
            new Partial(new DateTimeFieldType[] {null}, new int[] {1});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    public void testReadablePartialConstructor() {
        Partial source = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Partial copy = new Partial(source);
        assertEquals(1, copy.size());
        assertEquals(10, copy.getValue(0));

        try {
            new Partial((ReadablePartial) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    public void testWithAndWithout() {
        Partial p = new Partial();
        Partial p1 = p.with(DateTimeFieldType.hourOfDay(), 12);
        assertEquals(1, p1.size());
        assertEquals(12, p1.getValue(0));

        Partial p2 = p1.with(DateTimeFieldType.minuteOfHour(), 30);
        assertEquals(2, p2.size());
        assertEquals(DateTimeFieldType.hourOfDay(), p2.getFieldType(0));
        assertEquals(DateTimeFieldType.minuteOfHour(), p2.getFieldType(1));
        assertEquals(12, p2.getValue(0));
        assertEquals(30, p2.getValue(1));

        assertSame(p2, p2.with(DateTimeFieldType.hourOfDay(), 12));

        Partial p3 = p2.without(DateTimeFieldType.hourOfDay());
        assertEquals(1, p3.size());
        assertEquals(DateTimeFieldType.minuteOfHour(), p3.getFieldType(0));
        assertEquals(30, p3.getValue(0));

        assertSame(p3, p3.without(DateTimeFieldType.hourOfDay()));
    }

    public void testWithFieldAndAdded() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Partial p1 = p.withField(DateTimeFieldType.hourOfDay(), 15);
        assertEquals(15, p1.getValue(0));
        assertSame(p, p.withField(DateTimeFieldType.hourOfDay(), 10));

        Partial p2 = p.withFieldAdded(DurationFieldType.hours(), 3);
        assertEquals(13, p2.getValue(0));
        assertSame(p, p.withFieldAdded(DurationFieldType.hours(), 0));

        Partial p3 = p.withFieldAddWrapped(DurationFieldType.hours(), 16);
        assertEquals(2, p3.getValue(0));
    }

    public void testPeriodOperations() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Hours h = Hours.hours(4);
        Partial pPlus = p.plus(h);
        assertEquals(14, pPlus.getValue(0));

        Partial pMinus = p.minus(h);
        assertEquals(6, pMinus.getValue(0));

        assertSame(p, p.plus(null));
        assertSame(p, p.minus(null));
    }

    public void testPropertyAccess() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Partial.Property prop = p.property(DateTimeFieldType.hourOfDay());
        assertNotNull(prop);
        assertEquals(10, prop.get());
        assertEquals(p, prop.getPartial());

        Partial pSet = prop.setCopy(15);
        assertEquals(15, pSet.getValue(0));

        Partial pAdd = prop.addToCopy(2);
        assertEquals(12, pAdd.getValue(0));

        Partial pMax = prop.withMaximumValue();
        assertEquals(23, pMax.getValue(0));

        Partial pMin = prop.withMinimumValue();
        assertEquals(0, pMin.getValue(0));
    }

    public void testIsMatch() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 12);
        DateTime dt1 = new DateTime(2023, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC);
        DateTime dt2 = new DateTime(2023, 1, 1, 15, 0, 0, 0, DateTimeZone.UTC);

        assertTrue(p.isMatch(dt1));
        assertFalse(p.isMatch(dt2));

        Partial p2 = new Partial(DateTimeFieldType.hourOfDay(), 12);
        Partial p3 = new Partial(DateTimeFieldType.hourOfDay(), 15);
        assertTrue(p.isMatch((ReadablePartial) p2));
        assertFalse(p.isMatch((ReadablePartial) p3));

        try {
            p.isMatch((ReadablePartial) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    public void testToStringAndFormatting() {
        Partial pEmpty = new Partial();
        assertEquals("[]", pEmpty.toStringList());
        assertEquals("[]", pEmpty.toString());

        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.hourOfDay(),
            DateTimeFieldType.minuteOfHour()
        }, new int[] {10, 30});

        assertEquals("[hourOfDay=10, minuteOfHour=30]", p.toStringList());
        assertEquals("10:30", p.toString());
        assertEquals("10:30", p.toString(null));
        assertEquals("10", p.toString("HH"));
        assertEquals("10", p.toString("HH", Locale.ENGLISH));
    }

    public void testWithChronologyRetainFields() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Chronology isoUtc = ISOChronology.getInstanceUTC();
        assertSame(p, p.withChronologyRetainFields(isoUtc));

        Chronology coptic = CopticChronology.getInstanceUTC();
        Partial pCoptic = p.withChronologyRetainFields(coptic);
        assertEquals(coptic, pCoptic.getChronology());
        assertEquals(10, pCoptic.getValue(0));
    }
}
