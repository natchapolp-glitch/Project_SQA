package org.joda.time;

import java.util.Locale;
import junit.framework.TestCase;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;

public class PartialTest extends TestCase {

    public PartialTest(String name) {
        super(name);
    }

    public void testDefaultConstructor() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        assertEquals(0, p.getFieldTypes().length);
        assertEquals(0, p.getValues().length);
        assertEquals(ISOChronology.getInstanceUTC(), p.getChronology());
        assertNull(p.getFormatter());
        assertEquals("[]", p.toString());
        assertEquals("[]", p.toStringList());
    }

    public void testConstructorSingleField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2024);
        assertEquals(1, p.size());
        assertEquals(DateTimeFieldType.year(), p.getFieldType(0));
        assertEquals(2024, p.getValue(0));
        assertEquals(ISOChronology.getInstanceUTC(), p.getChronology());

        try {
            new Partial(null, 2024);
            fail("Expected IllegalArgumentException for null field type");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testConstructorArrayValidation_nullsAndMismatchedLengths() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[] {1});
            fail("Expected IllegalArgumentException for null types");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year()}, null);
            fail("Expected IllegalArgumentException for null values");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year()}, new int[] {2024, 1});
            fail("Expected IllegalArgumentException for mismatched array length");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), null}, new int[] {2024, 1});
            fail("Expected IllegalArgumentException for null array element");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testConstructorArrayValidation_orderAndDuplicates() {
        // Correct order is largest to smallest
        DateTimeFieldType[] validTypes = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        Partial p = new Partial(validTypes, new int[] {2024, 6});
        assertEquals(2, p.size());

        // Incorrect order: month before year
        DateTimeFieldType[] invalidOrder = new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.year()
        };
        try {
            new Partial(invalidOrder, new int[] {6, 2024});
            fail("Expected IllegalArgumentException for invalid field order");
        } catch (IllegalArgumentException expected) {
        }

        // Duplicate fields
        DateTimeFieldType[] duplicates = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.year()
        };
        try {
            new Partial(duplicates, new int[] {2024, 2025});
            fail("Expected IllegalArgumentException for duplicate fields");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testConstructorFromReadablePartial() {
        Partial original = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial copy = new Partial(original);
        assertEquals(1, copy.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), copy.getFieldType(0));
        assertEquals(15, copy.getValue(0));

        try {
            new Partial((ReadablePartial) null);
            fail("Expected IllegalArgumentException for null partial");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testWithChronologyRetainFields() {
        Partial p = new Partial(DateTimeFieldType.year(), 2024);
        assertSame(p, p.withChronologyRetainFields(ISOChronology.getInstanceUTC()));

        Chronology gj = GregorianChronology.getInstanceUTC();
        Partial p2 = p.withChronologyRetainFields(gj);
        assertEquals(gj, p2.getChronology());
        assertEquals(2024, p2.getValue(0));

        Partial p3 = p2.withChronologyRetainFields(null);
        assertEquals(ISOChronology.getInstanceUTC(), p3.getChronology());
    }

    public void testWith_addAndModify() {
        Partial p = new Partial();
        Partial p1 = p.with(DateTimeFieldType.year(), 2024);
        assertEquals(1, p1.size());
        assertEquals(2024, p1.getValue(0));

        // Add smaller field, maintain order
        Partial p2 = p1.with(DateTimeFieldType.monthOfYear(), 12);
        assertEquals(2, p2.size());
        assertEquals(DateTimeFieldType.year(), p2.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), p2.getFieldType(1));

        // Setting same value returns same instance
        assertSame(p2, p2.with(DateTimeFieldType.monthOfYear(), 12));

        // Setting new value on existing field
        Partial p3 = p2.with(DateTimeFieldType.monthOfYear(), 5);
        assertEquals(5, p3.getValue(1));
        assertEquals(2024, p3.getValue(0));

        try {
            p.with(null, 1);
            fail("Expected IllegalArgumentException for null field type");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testWithout() {
        Partial p = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[] {2024, 6}
        );

        Partial withoutMonth = p.without(DateTimeFieldType.monthOfYear());
        assertEquals(1, withoutMonth.size());
        assertEquals(DateTimeFieldType.year(), withoutMonth.getFieldType(0));

        // Removing non-existent field returns this
        assertSame(withoutMonth, withoutMonth.without(DateTimeFieldType.dayOfMonth()));
        assertSame(withoutMonth, withoutMonth.without(null));
    }

    public void testWithField() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 10);
        Partial modified = p.withField(DateTimeFieldType.minuteOfHour(), 45);
        assertEquals(45, modified.getValue(0));
        assertSame(p, p.withField(DateTimeFieldType.minuteOfHour(), 10));

        try {
            p.withField(DateTimeFieldType.hourOfDay(), 5);
            fail("Expected IllegalArgumentException for unsupported field");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testWithFieldAddedAndAddWrapped() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 50);

        Partial added = p.withFieldAdded(DurationFieldType.minutes(), 5);
        assertEquals(55, added.getValue(0));
        assertSame(p, p.withFieldAdded(DurationFieldType.minutes(), 0));

        Partial wrapped = p.withFieldAddWrapped(DurationFieldType.minutes(), 15);
        assertEquals(5, wrapped.getValue(0));
        assertSame(p, p.withFieldAddWrapped(DurationFieldType.minutes(), 0));

        try {
            p.withFieldAdded(DurationFieldType.hours(), 1);
            fail("Expected IllegalArgumentException for unsupported field");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testPlusMinusPeriod() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);

        Partial plus = p.plus(Period.hours(3));
        assertEquals(13, plus.getValue(0));
        assertSame(p, p.plus(null));

        Partial minus = p.minus(Period.hours(4));
        assertEquals(6, minus.getValue(0));
        assertSame(p, p.minus(null));

        // Adding a period containing fields not present in partial has no effect on those fields
        Partial plusDays = p.plus(Period.days(2));
        assertEquals(10, plusDays.getValue(0));
    }

    public void testIsMatchInstant() {
        Partial p = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[] {2024, 6}
        );

        DateTime matching = new DateTime(2024, 6, 15, 12, 0, 0, 0, ISOChronology.getInstanceUTC());
        DateTime nonMatching = new DateTime(2023, 6, 15, 12, 0, 0, 0, ISOChronology.getInstanceUTC());

        assertTrue(p.isMatch(matching));
        assertFalse(p.isMatch(nonMatching));

        // Empty partial matches any instant
        assertTrue(new Partial().isMatch(matching));
    }

    public void testIsMatchPartial() {
        Partial full = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[] {2024, 6}
        );

        Partial subset = new Partial(DateTimeFieldType.year(), 2024);
        // subset matches full since all fields of subset (year=2024) match full
        assertTrue(subset.isMatch(full));

        Partial differentYear = new Partial(DateTimeFieldType.year(), 2023);
        assertFalse(differentYear.isMatch(full));

        try {
            subset.isMatch((ReadablePartial) null);
            fail("Expected IllegalArgumentException for null partial");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testToStringAndFormatting() {
        Partial p = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[] {2024, 6}
        );

        assertEquals("2024-06", p.toString());
        assertEquals("[year=2024, monthOfYear=6]", p.toStringList());
        assertEquals("2024/06", p.toString("yyyy/MM"));
        assertEquals("2024/06", p.toString("yyyy/MM", Locale.US));
        assertEquals(p.toString(), p.toString(null));
        assertEquals(p.toString(), p.toString(null, Locale.US));
    }

    public void testPropertyMethods() {
        Partial p = new Partial(
            new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[] {2024, 6}
        );

        Partial.Property prop = p.property(DateTimeFieldType.monthOfYear());
        assertNotNull(prop);
        assertEquals(6, prop.get());
        assertSame(p, prop.getPartial());

        Partial modifiedAdd = prop.addToCopy(3);
        assertEquals(9, modifiedAdd.getValue(1));

        Partial modifiedWrap = prop.addWrapFieldToCopy(8);
        assertEquals(2, modifiedWrap.getValue(1));

        Partial modifiedSet = prop.setCopy(11);
        assertEquals(11, modifiedSet.getValue(1));

        Partial modifiedSetText = prop.setCopy("December", Locale.ENGLISH);
        assertEquals(12, modifiedSetText.getValue(1));

        Partial max = prop.withMaximumValue();
        assertEquals(12, max.getValue(1));

        Partial min = prop.withMinimumValue();
        assertEquals(1, min.getValue(1));

        try {
            p.property(DateTimeFieldType.dayOfMonth());
            fail("Expected IllegalArgumentException for unsupported field property");
        } catch (IllegalArgumentException expected) {
        }
    }
}
