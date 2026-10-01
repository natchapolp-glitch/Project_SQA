An implementation of deterministic regression unit tests for `UnsupportedDurationField` and `Partial` targeting JUnit 3.8.2 follows.

org/joda/time/field/UnsupportedDurationFieldTest.java
```java
package org.joda.time.field;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

/**
 * Regression tests for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest extends TestCase {

    public UnsupportedDurationFieldTest(String name) {
        super(name);
    }

    public void testSingletonAndProperties() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertSame("Instances for same type should be cached singletons", field1, field2);

        assertEquals(DurationFieldType.years(), field1.getType());
        assertEquals("years", field1.getName());
        assertFalse(field1.isSupported());
        assertTrue(field1.isPrecise());
    }

    public void testUnsupportedCalculations() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testUnsupportedCalculationsWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.getValue(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getValueAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getMillis(5L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testUnsupportedAddAndDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            field.add(100L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.add(100L, 5L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getDifference(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }

        try {
            field.getDifferenceAsLong(200L, 100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testCompareToAndUnitMillis() {
        UnsupportedDurationField years = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField months = UnsupportedDurationField.getInstance(DurationFieldType.months());

        assertEquals(0L, years.getUnitMillis());
        assertEquals(0, years.compareTo(months));
    }

    public void testEqualsAndHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());

        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals("seconds"));

        assertEquals(field1.hashCode(), field2.hashCode());
        assertEquals("UnsupportedDurationField[seconds]", field1.toString());
    }

    public void testSerialization() throws Exception {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(field);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object deserialized = ois.readObject();
        ois.close();

        assertSame(field, deserialized);
    }
}
```

org/joda/time/PartialTest.java
```java
package org.joda.time;

import java.util.Locale;

import junit.framework.TestCase;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * Regression tests for Partial.
 */
public class PartialTest extends TestCase {

    public PartialTest(String name) {
        super(name);
    }

    public void testDefaultConstructor() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        assertEquals(ISOChronology.getInstanceUTC(), p.getChronology());
        assertEquals(0, p.getFieldTypes().length);
        assertEquals(0, p.getValues().length);
        assertNull(p.getFormatter());
        assertEquals("[]", p.toStringList());
    }

    public void testSingleFieldConstructor() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 14);
        assertEquals(1, p.size());
        assertEquals(DateTimeFieldType.hourOfDay(), p.getFieldType(0));
        assertEquals(14, p.getValue(0));
        assertEquals(14, p.get(DateTimeFieldType.hourOfDay()));

        try {
            new Partial(null, 14);
            fail("Expected IllegalArgumentException for null field type");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            new Partial(DateTimeFieldType.hourOfDay(), 25);
            fail("Expected IllegalArgumentException for invalid value");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testArrayConstructorValid() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = new int[] { 2024, 6, 15 };

        Partial p = new Partial(types, values);
        assertEquals(3, p.size());
        assertEquals(2024, p.getValue(0));
        assertEquals(6, p.getValue(1));
        assertEquals(15, p.getValue(2));
    }

    public void testArrayConstructorInvalidOrderOrDuplicate() {
        DateTimeFieldType[] invalidOrder = new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.year()
        };
        try {
            new Partial(invalidOrder, new int[] { 6, 2024 });
            fail("Expected IllegalArgumentException due to invalid order");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        DateTimeFieldType[] duplicates = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.year()
        };
        try {
            new Partial(duplicates, new int[] { 2024, 2025 });
            fail("Expected IllegalArgumentException due to duplicates");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testArrayConstructorMismatchedLengths() {
        try {
            new Partial(new DateTimeFieldType[] { DateTimeFieldType.year() }, new int[] { 2024, 1 });
            fail("Expected IllegalArgumentException for mismatched lengths");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            new Partial(null, new int[] { 2024 });
            fail("Expected IllegalArgumentException for null types array");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testWithChronologyRetainFields() {
        Partial p = new Partial(DateTimeFieldType.year(), 2024, ISOChronology.getInstanceUTC());
        Chronology bChronology = BuddhistChronology.getInstanceUTC();
        Partial bPartial = p.withChronologyRetainFields(bChronology);

        assertEquals(bChronology, bPartial.getChronology());
        assertEquals(2024, bPartial.get(DateTimeFieldType.year()));
        assertSame(p, p.withChronologyRetainFields(null));
    }

    public void testWithNewFieldAndExistingField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2024);
        Partial updated = p.with(DateTimeFieldType.monthOfYear(), 10);

        assertEquals(2, updated.size());
        assertEquals(DateTimeFieldType.year(), updated.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), updated.getFieldType(1));
        assertEquals(10, updated.getValue(1));

        Partial sameVal = updated.with(DateTimeFieldType.monthOfYear(), 10);
        assertSame(updated, sameVal);

        Partial diffVal = updated.with(DateTimeFieldType.monthOfYear(), 12);
        assertEquals(12, diffVal.get(DateTimeFieldType.monthOfYear()));
    }

    public void testWithoutField() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() },
            new int[] { 10, 30 }
        );
        Partial removed = p.without(DateTimeFieldType.minuteOfHour());
        assertEquals(1, removed.size());
        assertEquals(DateTimeFieldType.hourOfDay(), removed.getFieldType(0));

        assertSame(p, p.without(DateTimeFieldType.secondOfMinute()));
    }

    public void testWithField() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial modified = p.withField(DateTimeFieldType.dayOfMonth(), 20);
        assertEquals(20, modified.get(DateTimeFieldType.dayOfMonth()));

        assertSame(p, p.withField(DateTimeFieldType.dayOfMonth(), 15));

        try {
            p.withField(DateTimeFieldType.monthOfYear(), 5);
            fail("Expected IllegalArgumentException for unsupported field");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testWithFieldAdded() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() },
            new int[] { 10, 45 }
        );
        assertSame(p, p.withFieldAdded(DurationFieldType.minutes(), 0));

        Partial added = p.withFieldAdded(DurationFieldType.minutes(), 20);
        assertEquals(11, added.get(DateTimeFieldType.hourOfDay()));
        assertEquals(5, added.get(DateTimeFieldType.minuteOfHour()));

        try {
            p.withFieldAdded(DurationFieldType.days(), 1);
            fail("Expected IllegalArgumentException for unsupported duration field");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testWithFieldAddWrapped() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() },
            new int[] { 23, 50 }
        );
        assertSame(p, p.withFieldAddWrapped(DurationFieldType.hours(), 0));

        Partial wrapped = p.withFieldAddWrapped(DurationFieldType.hours(), 2);
        assertEquals(1, wrapped.get(DateTimeFieldType.hourOfDay()));
        assertEquals(50, wrapped.get(DateTimeFieldType.minuteOfHour()));
    }

    public void testPlusAndMinusPeriod() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() },
            new int[] { 12, 30 }
        );
        Period period = Period.hours(2).withMinutes(15);

        Partial plus = p.plus(period);
        assertEquals(14, plus.get(DateTimeFieldType.hourOfDay()));
        assertEquals(45, plus.get(DateTimeFieldType.minuteOfHour()));

        Partial minus = p.minus(period);
        assertEquals(10, minus.get(DateTimeFieldType.hourOfDay()));
        assertEquals(15, minus.get(DateTimeFieldType.minuteOfHour()));

        assertSame(p, p.plus(null));
        assertSame(p, p.minus(null));
    }

    public void testIsMatchInstant() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
            new int[] { 2024, 6 }
        );
        DateTime dtMatch = new DateTime(2024, 6, 15, 12, 0, 0, 0, ISOChronology.getInstanceUTC());
        DateTime dtMismatch = new DateTime(2023, 6, 15, 12, 0, 0, 0, ISOChronology.getInstanceUTC());

        assertTrue(p.isMatch(dtMatch));
        assertFalse(p.isMatch(dtMismatch));
    }

    public void testIsMatchPartial() {
        Partial p1 = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
            new int[] { 2024, 6 }
        );
        Partial p2 = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() },
            new int[] { 2024, 6, 10 }
        );
        Partial pMismatch = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
            new int[] { 2024, 7 }
        );

        assertTrue(p1.isMatch(p2));
        assertFalse(p1.isMatch(pMismatch));

        try {
            p1.isMatch((ReadablePartial) null);
            fail("Expected IllegalArgumentException for null partial");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    public void testToStringFormats() {
        Partial p = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour() },
            new int[] { 14, 30 }
        );
        assertEquals("14:30", p.toString());
        assertEquals("[hourOfDay=14, minuteOfHour=30]", p.toStringList());
        assertEquals("14-30", p.toString("HH-mm"));
        assertEquals("14-30", p.toString("HH-mm", Locale.US));
        assertEquals("14:30", p.toString((String) null));
    }

    public void testPropertyMethods() {
        Partial p = new Partial(DateTimeFieldType.hourOfDay(), 10);
        Partial.Property prop = p.property(DateTimeFieldType.hourOfDay());

        assertEquals(10, prop.get());
        assertEquals(DateTimeFieldType.hourOfDay().getField(ISOChronology.getInstanceUTC()), prop.getField());
        assertSame(p, prop.getPartial());

        Partial pAdded = prop.addToCopy(5);
        assertEquals(15, pAdded.get(DateTimeFieldType.hourOfDay()));

        Partial pWrapped = prop.addWrapFieldToCopy(16);
        assertEquals(2, pWrapped.get(DateTimeFieldType.hourOfDay()));

        Partial pSet = prop.setCopy(8);
        assertEquals(8, pSet.get(DateTimeFieldType.hourOfDay()));

        Partial pSetText = prop.setCopy("9");
        assertEquals(9, pSetText.get(DateTimeFieldType.hourOfDay()));

        Partial pMax = prop.withMaximumValue();
        assertEquals(23, pMax.get(DateTimeFieldType.hourOfDay()));

        Partial pMin = prop.withMinimumValue();
        assertEquals(0, pMin.get(DateTimeFieldType.hourOfDay()));
    }
}
```

### References
- Joda-Time Documentation & API specification: `org.joda.time.field.UnsupportedDurationField` and `org.joda.time.Partial` (Apache 2.0 License).
- JUnit 3.8.x Test Framework specification (`junit.framework.TestCase`).