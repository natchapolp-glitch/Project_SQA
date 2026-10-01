package org.joda.time;

import junit.framework.TestCase;
import org.joda.time.chrono.ISOChronology;

import java.util.Locale;

public class PartialTest extends TestCase {

    private Chronology chronologyUTC;

    protected void setUp() {
        chronologyUTC = ISOChronology.getInstanceUTC();
    }

    // constructors

    public void testEmptyConstructor() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        assertNotNull(p.getChronology());
        assertEquals(0, p.getValues().length);
        assertEquals(0, p.getFieldTypes().length);
    }

    public void testConstructorWithChronology() {
        Partial p = new Partial(chronologyUTC);
        assertEquals(0, p.size());
        assertEquals(chronologyUTC, p.getChronology());
        assertEquals(0, p.getValues().length);
    }

    public void testSingleFieldConstructor() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        assertEquals(1, p.size());
        assertEquals(7, p.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), p.getFieldType(0));
    }

    public void testSingleFieldConstructorWithChronology() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7, chronologyUTC);
        assertEquals(1, p.size());
        assertEquals(7, p.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), p.getFieldType(0));
        assertEquals(chronologyUTC, p.getChronology());
    }

    public void testArrayConstructorValid() {
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        int[] values = {2014, 7};
        Partial p = new Partial(types, values);
        assertEquals(2, p.size());
        assertEquals(2014, p.getValue(0));
        assertEquals(7, p.getValue(1));
    }

    public void testArrayConstructorWithChronology() {
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        int[] values = {2014, 7};
        Partial p = new Partial(types, values, chronologyUTC);
        assertEquals(2, p.size());
        assertEquals(2014, p.getValue(0));
        assertEquals(7, p.getValue(1));
        assertEquals(chronologyUTC, p.getChronology());
    }

    public void testArrayConstructorTypesNull() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[] {1});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testArrayConstructorValuesNull() {
        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year()}, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testArrayConstructorLengthMismatch() {
        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year()}, new int[] {2014, 7});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testArrayConstructorDuplicateTypes() {
        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.year()}, new int[] {2014, 2015});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testArrayConstructorOutOfOrder() {
        try {
            new Partial(new DateTimeFieldType[] {DateTimeFieldType.monthOfYear(), DateTimeFieldType.year()}, new int[] {7, 2014});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testCopyConstructor() {
        Partial src = new Partial(DateTimeFieldType.monthOfYear(), 7);
        Partial copy = new Partial(src);
        assertEquals(src.size(), copy.size());
        assertEquals(src.getValue(0), copy.getValue(0));
        assertEquals(src.getFieldType(0), copy.getFieldType(0));
    }

    // size, chronology

    public void testSize() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        p = new Partial(DateTimeFieldType.hourOfDay(), 12);
        assertEquals(1, p.size());
    }

    public void testGetChronology() {
        Partial p = new Partial();
        Chronology chrono = p.getChronology();
        assertNotNull(chrono);
        assertEquals(ISOChronology.getInstanceUTC(), chrono);
    }

    // field types and values

    public void testGetFieldType() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 30);
        assertEquals(DateTimeFieldType.minuteOfHour(), p.getFieldType(0));
    }

    public void testGetFieldTypes() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 30);
        DateTimeFieldType[] types = p.getFieldTypes();
        assertEquals(1, types.length);
        assertEquals(DateTimeFieldType.minuteOfHour(), types[0]);
    }

    public void testGetValue() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertEquals(15, p.getValue(0));
    }

    public void testGetValues() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        int[] values = p.getValues();
        assertEquals(1, values.length);
        assertEquals(15, values[0]);
    }

    // with

    public void testWithNewField() {
        Partial p = new Partial();
        Partial result = p.with(DateTimeFieldType.monthOfYear(), 7);
        assertEquals(1, result.size());
        assertEquals(7, result.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), result.getFieldType(0));
    }

    public void testWithExistingFieldChange() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        Partial result = p.with(DateTimeFieldType.monthOfYear(), 8);
        assertEquals(1, result.size());
        assertEquals(8, result.getValue(0));
        assertNotSame(p, result);
    }

    public void testWithExistingFieldSameValue() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        Partial result = p.with(DateTimeFieldType.monthOfYear(), 7);
        assertSame(p, result);
    }

    // without

    public void testWithoutExistingField() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        Partial result = p.without(DateTimeFieldType.monthOfYear());
        assertEquals(1, result.size());
        assertEquals(DateTimeFieldType.year(), result.getFieldType(0));
    }

    public void testWithoutNonExistingField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2014);
        Partial result = p.without(DateTimeFieldType.monthOfYear());
        assertSame(p, result);
    }

    // withField

    public void testWithFieldChange() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        Partial result = p.withField(DateTimeFieldType.monthOfYear(), 8);
        assertEquals(8, result.getValue(0));
        assertNotSame(p, result);
    }

    public void testWithFieldSameValue() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        Partial result = p.withField(DateTimeFieldType.monthOfYear(), 7);
        assertSame(p, result);
    }

    public void testWithFieldMissingField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2014);
        try {
            p.withField(DateTimeFieldType.monthOfYear(), 7);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // withFieldAdded

    public void testWithFieldAdded() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()}, new int[] {10, 30});
        Partial result = p.withFieldAdded(DurationFieldType.minutes(), 15);
        assertEquals(45, result.getValue(1));
    }

    public void testWithFieldAddedZero() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Partial result = p.withFieldAdded(DurationFieldType.hours(), 0);
        assertSame(p, result);
    }

    // withFieldAddWrapped

    public void testWithFieldAddWrapped() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 50);
        Partial result = p.withFieldAddWrapped(DurationFieldType.minutes(), 20);
        assertEquals(10, result.getValue(0)); // wraps within 0-59
    }

    public void testWithFieldAddWrappedZero() {
        Partial p = new Partial(DateTimeFieldType.minuteOfHour(), 30);
        Partial result = p.withFieldAddWrapped(DurationFieldType.minutes(), 0);
        assertSame(p, result);
    }

    // withPeriodAdded / plus / minus

    

    

    

    // property

    

    

    

    // isMatch

    

    

    

    

    

    // toString variants

    

    

    

    

    

    

    
}
