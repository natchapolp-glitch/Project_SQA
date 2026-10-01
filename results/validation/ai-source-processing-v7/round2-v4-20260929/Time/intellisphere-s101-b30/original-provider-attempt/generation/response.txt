Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
org/joda/time/field/UnsupportedDurationFieldTest.java
package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest extends TestCase {

    private UnsupportedDurationField field;
    private UnsupportedDurationField other;

    protected void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        other = UnsupportedDurationField.getInstance(DurationFieldType.centuries());
    }

    public void testGetInstanceReturnsSingleton() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        UnsupportedDurationField b = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertSame(a, b);
    }

    public void testGetInstanceDifferentTypes() {
        assertNotSame(field, other);
    }

    public void testGetType() {
        assertEquals(DurationFieldType.eras(), field.getType());
    }

    public void testGetName() {
        assertEquals(DurationFieldType.eras().getName(), field.getName());
    }

    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    public void testCompareTo() {
        assertEquals(0, field.compareTo(other));
    }

    public void testEqualsSameInstance() {
        assertTrue(field.equals(field));
    }

    public void testEqualsSameFieldType() {
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertTrue(field.equals(another));
    }

    public void testEqualsDifferentFieldType() {
        assertFalse(field.equals(other));
    }

    public void testEqualsNull() {
        assertFalse(field.equals(null));
    }

    public void testEqualsDifferentClass() {
        assertFalse(field.equals("string"));
    }

    public void testHashCode() {
        assertTrue(field.hashCode() == field.getName().hashCode());
    }

    public void testToString() {
        assertEquals("UnsupportedDurationField[" + DurationFieldType.eras().getName() + "]", field.toString());
    }

    public void testGetValueThrowsException() {
        try {
            field.getValue(123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongThrowsException() {
        try {
            field.getValueAsLong(123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueWithInstantThrowsException() {
        try {
            field.getValue(123L, 456L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongWithInstantThrowsException() {
        try {
            field.getValueAsLong(123L, 456L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisIntThrowsException() {
        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongThrowsException() {
        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisIntWithInstantThrowsException() {
        try {
            field.getMillis(5, 123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongWithInstantThrowsException() {
        try {
            field.getMillis(5L, 123L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddIntThrowsException() {
        try {
            field.add(1000L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddLongThrowsException() {
        try {
            field.add(1000L, 1L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetDifferenceThrowsException() {
        try {
            field.getDifference(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetDifferenceAsLongThrowsException() {
        try {
            field.getDifferenceAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }
}

```

```java
org/joda/time/PartialTest.java
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
            new Partial(null, new int[] {1});
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
        assertEquals(DateTimeFieldType.minuteOfHour(), types[0));
    }

    public void testGetValue() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertEquals(15, p.getValue(0));
    }

    public void testGetValues() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        int[] values = p.getValues();
        assertEquals(1, values.length);
        assertEquals(15, values[0](undefined));
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

    public void testWithPeriodAddedNullPeriod() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertSame(p, p.withPeriodAdded(null, 1));
    }

    public void testWithPeriodAddedZeroScalar() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertSame(p, p.withPeriodAdded(new Period(0, 0, 0, 0), 0));
    }

    public void testPlusMinus() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()}, new int[] {12, 0});
        Partial plus = p.plus(Period.minutes(30));
        assertEquals(30, plus.getValue(1));
        Partial minus = p.minus(Period.hours(1));
        assertEquals(11, minus.getValue(0));
    }

    // property

    public void testProperty() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial.Property prop = p.property(DateTimeFieldType.dayOfMonth());
        assertEquals(15, prop.get());
        assertEquals(DateTimeFieldType.dayOfMonth(), prop.getFieldType());
    }

    public void testPropertySetCopy() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial modified = p.property(DateTimeFieldType.dayOfMonth()).setCopy(20);
        assertEquals(20, modified.getValue(0));
        assertEquals(15, p.getValue(0)); // original unchanged
    }

    public void testPropertyMissingTypeThrows() {
        Partial p = new Partial(DateTimeFieldType.year(), 2014);
        try {
            p.property(DateTimeFieldType.monthOfYear());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // isMatch

    public void testIsMatchInstantMatching() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        DateTime dt = new DateTime(2014, 7, 15, 0, 0, 0, chronologyUTC);
        assertTrue(p.isMatch(dt));
    }

    public void testIsMatchInstantNonMatching() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        DateTime dt = new DateTime(2014, 8, 1, 0, 0, 0, chronologyUTC);
        assertFalse(p.isMatch(dt));
    }

    public void testIsMatchPartialMatching() {
        Partial a = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        Partial b = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        assertTrue(a.isMatch(b));
    }

    public void testIsMatchPartialNonMatching() {
        Partial a = new Partial(DateTimeFieldType.year(), 2014);
        Partial b = new Partial(DateTimeFieldType.year(), 2015);
        assertFalse(a.isMatch(b));
    }

    public void testIsMatchPartialNullThrows() {
        Partial p = new Partial(DateTimeFieldType.year(), 2014);
        try {
            p.isMatch((ReadablePartial) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // toString variants

    public void testToStringEmpty() {
        Partial p = new Partial();
        assertEquals("[]", p.toString());
    }

    public void testToStringWithFields() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()}, new int[] {2014, 7, 15});
        String s = p.toString();
        // the formatter may produce "2014-07-15"
        assertTrue(s.contains("2014") && s.contains("07") && s.contains("15"));
    }

    public void testToStringList() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        String s = p.toStringList();
        assertTrue(s.startsWith("[") && s.endsWith("]"));
    }

    public void testToStringPattern() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        assertEquals("07", p.toString("MM"));
    }

    public void testToStringPatternLocale() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 7);
        assertEquals("07", p.toString("MM", Locale.ENGLISH));
    }

    public void testGetFormatterOnNonEmpty() {
        Partial p = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2014, 7});
        assertNotNull(p.getFormatter());
    }

    public void testGetFormatterOnEmpty() {
        Partial p = new Partial();
        assertNull(p.getFormatter());
    }
}

```