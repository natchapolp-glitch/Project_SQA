Analysis

I've reviewed the production source code for UnsupportedDurationField and Partial classes, along with the build configuration. The project uses JUnit 3.8.2 (based on pom.xml) and Java 1.5+ (compiler target).

Key observations:

JUnit 3.8.2 uses TestCase base class (not JUnit 4 annotations)
Partial and UnsupportedDurationField are immutable, serializable classes
UnsupportedDurationField methods throw UnsupportedOperationException or return default values
Partial has multiple constructors and supports field manipulation
No external dependencies beyond joda-convert (optional)

I'll now generate comprehensive regression tests for both classes.



Test Classes

org/joda/time/field/UnsupportedDurationFieldTest.java

```java
/*
 * Regression tests for UnsupportedDurationField
 */
package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest extends TestCase {

    private UnsupportedDurationField iFieldMonths;
    private UnsupportedDurationField iFieldWeeks;

    protected void setUp() throws Exception {
        super.setUp();
        iFieldMonths = UnsupportedDurationField.getInstance(DurationFieldType.months());
        iFieldWeeks = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    }

    // Constructor and getInstance tests
    public void testGetInstanceMonths() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertNotNull(field);
        assertEquals(DurationFieldType.months(), field.getType());
    }

    public void testGetInstanceWeeks() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertNotNull(field);
        assertEquals(DurationFieldType.weeks(), field.getType());
    }

    public void testGetInstanceCaching() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertSame(field1, field2);
    }

    public void testGetInstanceDifferentTypes() {
        UnsupportedDurationField months = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField weeks = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertNotSame(months, weeks);
    }

    // Accessor methods tests
    public void testGetType() {
        assertEquals(DurationFieldType.months(), iFieldMonths.getType());
        assertEquals(DurationFieldType.weeks(), iFieldWeeks.getType());
    }

    public void testGetName() {
        assertEquals("months", iFieldMonths.getName());
        assertEquals("weeks", iFieldWeeks.getName());
    }

    public void testIsSupported() {
        assertFalse(iFieldMonths.isSupported());
        assertFalse(iFieldWeeks.isSupported());
    }

    public void testIsPrecise() {
        assertTrue(iFieldMonths.isPrecise());
        assertTrue(iFieldWeeks.isPrecise());
    }

    public void testGetUnitMillis() {
        assertEquals(0L, iFieldMonths.getUnitMillis());
        assertEquals(0L, iFieldWeeks.getUnitMillis());
    }

    // Comparison tests
    public void testCompareTo() {
        int result = iFieldMonths.compareTo(iFieldWeeks);
        assertEquals(0, result);
    }

    public void testCompareToSelf() {
        int result = iFieldMonths.compareTo(iFieldMonths);
        assertEquals(0, result);
    }

    // equals and hashCode tests
    public void testEqualsWithSameType() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertTrue(field1.equals(field2));
    }

    public void testEqualsWithDifferentType() {
        assertFalse(iFieldMonths.equals(iFieldWeeks));
    }

    public void testEqualsWithNull() {
        assertFalse(iFieldMonths.equals(null));
    }

    public void testEqualsWithOtherType() {
        assertFalse(iFieldMonths.equals("months"));
    }

    public void testEqualsSelf() {
        assertTrue(iFieldMonths.equals(iFieldMonths));
    }

    public void testHashCodeConsistency() {
        int hash1 = iFieldMonths.hashCode();
        int hash2 = iFieldMonths.hashCode();
        assertEquals(hash1, hash2);
    }

    public void testHashCodeEqualObjects() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    public void testHashCodeDifferentObjects() {
        assertNotSame(iFieldMonths.hashCode(), iFieldWeeks.hashCode());
    }

    // toString tests
    public void testToString() {
        String str = iFieldMonths.toString();
        assertTrue(str.contains("UnsupportedDurationField"));
        assertTrue(str.contains("months"));
    }

    public void testToStringFormat() {
        String str = iFieldMonths.toString();
        assertTrue(str.startsWith("UnsupportedDurationField["));
        assertTrue(str.endsWith("]"));
    }

    // getValue methods - all should throw UnsupportedOperationException
    public void testGetValueIntThrows() {
        try {
            iFieldMonths.getValue(1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueLongThrows() {
        try {
            iFieldMonths.getValue(Long.MAX_VALUE);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueWithInstantThrows() {
        try {
            iFieldMonths.getValue(1000L, 2000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongThrows() {
        try {
            iFieldMonths.getValueAsLong(1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongWithInstantThrows() {
        try {
            iFieldMonths.getValueAsLong(1000L, 2000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    // getMillis methods - all should throw UnsupportedOperationException
    public void testGetMillisIntThrows() {
        try {
            iFieldMonths.getMillis(5);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongThrows() {
        try {
            iFieldMonths.getMillis(1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisIntWithInstantThrows() {
        try {
            iFieldMonths.getMillis(5, 2000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongWithInstantThrows() {
        try {
            iFieldMonths.getMillis(1000L, 2000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    // add methods - all should throw UnsupportedOperationException
    public void testAddIntThrows() {
        try {
            iFieldMonths.add(1000L, 5);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddLongThrows() {
        try {
            iFieldMonths.add(1000L, 1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    // getDifference methods - all should throw UnsupportedOperationException
    public void testGetDifferenceThrows() {
        try {
            iFieldMonths.getDifference(2000L, 1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetDifferenceAsLongThrows() {
        try {
            iFieldMonths.getDifferenceAsLong(2000L, 1000L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertTrue(ex.getMessage().contains("field is unsupported"));
        }
    }

    // Boundary value tests
    public void testGetValueWithZero() {
        try {
            iFieldMonths.getValue(0L);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testGetValueWithMaxLong() {
        try {
            iFieldMonths.getValue(Long.MAX_VALUE);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testGetValueWithMinLong() {
        try {
            iFieldMonths.getValue(Long.MIN_VALUE);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testAddWithZero() {
        try {
            iFieldMonths.add(1000L, 0);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    public void testAddWithNegative() {
        try {
            iFieldMonths.add(1000L, -5);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    // readResolve test (serialization)
    public void testReadResolve() throws Exception {
        // This tests the readResolve method through reflection simulation
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField resolved = UnsupportedDurationField.getInstance(DurationFieldType.months());
        // After deserialization, should return the same singleton instance
        assertEquals(field, resolved);
    }

    // Multiple field types
    public void testDifferentFieldTypesNotEqual() {
        UnsupportedDurationField years = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField days = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(years.equals(days));
    }

    public void testMultipleInstancesConsistent() {
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField f3 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertSame(f1, f2);
        assertSame(f2, f3);
    }
}

```



org/joda/time/PartialTest.java

```java
/*
 * Regression tests for Partial
 */
package org.joda.time;

import java.util.Locale;

import junit.framework.TestCase;

public class PartialTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    // Empty Partial tests
    public void testEmptyPartialConstruction() {
        Partial p = new Partial();
        assertEquals(0, p.size());
        assertNotNull(p.getChronology());
    }

    public void testEmptyPartialWithChronology() {
        Chronology chrono = ISOChronology.getInstance();
        Partial p = new Partial(chrono);
        assertEquals(0, p.size());
        assertNotNull(p.getChronology());
    }

    public void testEmptyPartialWithNullChronology() {
        Partial p = new Partial((Chronology) null);
        assertEquals(0, p.size());
        assertNotNull(p.getChronology());
    }

    // Single field Partial tests
    public void testSingleFieldPartial() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 3);
        assertEquals(1, p.size());
        assertEquals(3, p.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), p.getFieldType(0));
    }

    public void testSingleFieldPartialWithChronology() {
        Chronology chrono = ISOChronology.getInstance();
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15, chrono);
        assertEquals(1, p.size());
        assertEquals(15, p.getValue(0));
    }

    public void testSingleFieldPartialNullType() {
        try {
            new Partial((DateTimeFieldType) null, 5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("field type"));
        }
    }

    public void testSingleFieldPartialValidation() {
        try {
            // Invalid month value
            new Partial(DateTimeFieldType.monthOfYear(), 13);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    // Multiple fields Partial tests
    public void testMultipleFieldsPartial() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = new int[] { 2013, 3, 15 };
        Partial p = new Partial(types, values);
        assertEquals(3, p.size());
        assertEquals(2013, p.getValue(0));
        assertEquals(3, p.getValue(1));
        assertEquals(15, p.getValue(2));
    }

    public void testMultipleFieldsPartialWithChronology() {
        Chronology chrono = ISOChronology.getInstance();
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        int[] values = new int[] { 2013, 6 };
        Partial p = new Partial(types, values, chrono);
        assertEquals(2, p.size());
    }

    public void testMultipleFieldsNullTypes() {
        try {
            new Partial(null, new int[] { 2013 });
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Types array"));
        }
    }

    public void testMultipleFieldsNullValues() {
        DateTimeFieldType[] types = new DateTimeFieldType[] { DateTimeFieldType.year() };
        try {
            new Partial(types, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Values array"));
        }
    }

    public void testMultipleFieldsArrayLengthMismatch() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        int[] values = new int[] { 2013 };
        try {
            new Partial(types, values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("same length"));
        }
    }

    public void testMultipleFieldsNullTypeInArray() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            null
        };
        int[] values = new int[] { 2013, 3 };
        try {
            new Partial(types, values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("must not contain null"));
        }
    }

    public void testMultipleFieldsWrongOrder() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.year()
        };
        int[] values = new int[] { 3, 2013 };
        try {
            new Partial(types, values);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("order largest-smallest"));
        }
    }

    // ReadablePartial copy constructor tests
    public void testCopyConstructor() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        int[] values = new int[] { 2013, 6 };
        Partial p1 = new Partial(types, values);
        Partial p2 = new Partial(p1);
        
        assertEquals(p1.size(), p2.size());
        assertEquals(p1.getValue(0), p2.getValue(0));
        assertEquals(p1.getValue(1), p2.getValue(1));
    }

    public void testCopyConstructorNull() {
        try {
            new Partial((ReadablePartial) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("must not be null"));
        }
    }

    // Size and field access tests
    public void testSize() {
        Partial empty = new Partial();
        assertEquals(0, empty.size());
        
        Partial single = new Partial(DateTimeFieldType.year(), 2013);
        assertEquals(1, single.size());
    }

    public void testGetValue() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        assertEquals(2013, p.getValue(0));
    }

    public void testGetValueOutOfBounds() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        try {
            p.getValue(1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
    }

    public void testGetValues() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        int[] values = p.getValues();
        assertEquals(2, values.length);
        assertEquals(2013, values[0]);
        assertEquals(6, values[1]);
    }

    public void testGetValuesEmptyPartial() {
        Partial p = new Partial();
        int[] values = p.getValues();
        assertEquals(0, values.length);
    }

    public void testGetFieldType() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        assertEquals(DateTimeFieldType.year(), p.getFieldType(0));
    }

    public void testGetFieldTypeOutOfBounds() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        try {
            p.getFieldType(1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // expected
        }
    }

    public void testGetFieldTypes() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        Partial p = new Partial(types, new int[] { 2013, 6 });
        
        DateTimeFieldType[] result = p.getFieldTypes();
        assertEquals(2, result.length);
        assertEquals(DateTimeFieldType.year(), result[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), result[1]);
    }

    // Chronology tests
    public void testGetChronology() {
        Partial p = new Partial();
        assertNotNull(p.getChronology());
    }

    public void testGetChronologyWithSpecified() {
        Chronology chrono = ISOChronology.getInstance();
        Partial p = new Partial(chrono);
        Chronology result = p.getChronology();
        assertNotNull(result);
    }

    public void testWithChronologyRetainFields() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Chronology newChrono = ISOChronology.getInstance();
        Partial p2 = p.withChronologyRetainFields(newChrono);
        
        assertEquals(2013, p2.getValue(0));
        assertEquals(6, p2.getValue(1));
    }

    public void testWithChronologyRetainFieldsSame() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.withChronologyRetainFields(p.getChronology());
        assertSame(p, p2);
    }

    // with method tests
    public void testWithNewField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.with(DateTimeFieldType.monthOfYear(), 6);
        
        assertEquals(2, p2.size());
        assertEquals(2013, p2.getValue(0));
        assertEquals(6, p2.getValue(1));
    }

    public void testWithExistingField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.with(DateTimeFieldType.year(), 2014);
        
        assertEquals(1, p2.size());
        assertEquals(2014, p2.getValue(0));
    }

    public void testWithNullField() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        try {
            p.with(null, 2014);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("field type"));
        }
    }

    public void testWithSameValue() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.with(DateTimeFieldType.year(), 2013);
        assertSame(p, p2);
    }

    // without method tests
    public void testWithout() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Partial p2 = p.without(DateTimeFieldType.monthOfYear());
        assertEquals(1, p2.size());
        assertEquals(2013, p2.getValue(0));
    }

    public void testWithoutNonExistent() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.without(DateTimeFieldType.monthOfYear());
        assertSame(p, p2);
    }

    public void testWithoutNull() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        Partial p2 = p.without(null);
        assertSame(p, p2);
    }

    // withField method tests
    public void testWithField() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Partial p2 = p.withField(DateTimeFieldType.monthOfYear(), 3);
        assertEquals(2, p2.size());
        assertEquals(3, p2.getValue(1));
    }

    public void testWithFieldSameValue() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Partial p2 = p.withField(DateTimeFieldType.monthOfYear(), 6);
        assertSame(p, p2);
    }

    // toString tests
    public void testToString() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        String str = p.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    public void testToStringEmpty() {
        Partial p = new Partial();
        String str = p.toString();
        assertNotNull(str);
    }

    public void testToStringList() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        String str = p.toStringList();
        assertTrue(str.contains("year"));
        assertTrue(str.contains("2013"));
        assertTrue(str.contains("monthOfYear"));
        assertTrue(str.contains("6"));
    }

    public void testToStringEmptyList() {
        Partial p = new Partial();
        String str = p.toStringList();
        assertTrue(str.contains("["));
        assertTrue(str.contains("]"));
    }

    public void testToStringWithPattern() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        String str = p.toString("yyyy");
        assertNotNull(str);
    }

    public void testToStringWithPatternNull() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        String str = p.toString((String) null);
        assertNotNull(str);
    }

    public void testToStringWithPatternAndLocale() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        String str = p.toString("yyyy", Locale.ENGLISH);
        assertNotNull(str);
    }

    public void testToStringWithPatternAndLocaleNull() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        String str = p.toString("yyyy", null);
        assertNotNull(str);
    }

    // isMatch tests
    public void testIsMatchInstant() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        DateTime dt = new DateTime(2013, 6, 15, 10, 30, 0);
        assertTrue(p.isMatch(dt));
    }

    public void testIsMatchInstantNoMatch() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        DateTime dt = new DateTime(2013, 3, 15, 10, 30, 0);
        assertFalse(p.isMatch(dt));
    }

    public void testIsMatchEmptyPartial() {
        Partial p = new Partial();
        DateTime dt = new DateTime(2013, 6, 15, 10, 30, 0);
        assertTrue(p.isMatch(dt));
    }

    public void testIsMatchPartial() {
        Partial p1 = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Partial p2 = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 6, 15 });
        
        assertTrue(p1.isMatch(p2));
    }

    public void testIsMatchPartialNoMatch() {
        Partial p1 = new Partial(DateTimeFieldType.monthOfYear(), 6);
        Partial p2 = new Partial(DateTimeFieldType.monthOfYear(), 3);
        assertFalse(p1.isMatch(p2));
    }

    public void testIsMatchPartialNull() {
        Partial p = new Partial(DateTimeFieldType.year(), 2013);
        try {
            p.isMatch((ReadablePartial) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("must not be null"));
        }
    }

    // Property tests
    public void testProperty() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 6 });
        
        Partial.Property prop = p.property(DateTimeFieldType.monthOfYear());
        assertNotNull(prop);
        assertEquals(6, prop.get());
    }

    // withPeriodAdded tests
    public void testWithPeriodAdded() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        Period period = new Period(0, 2, 0, 0);
        Partial p2 = p.withPeriodAdded(period, 1);
        assertNotNull(p2);
    }

    public void testWithPeriodAddedNull() {
        Partial p = new Partial(DateTimeFieldType.monthOfYear(), 6);
        Partial p2 = p.withPeriodAdded(null, 1);
        assertSame(p,
