I'll analyze the supplied production source and build configuration to create comprehensive deterministic regression tests for `UnsupportedDurationField` and `Partial`.

From the build configuration, I can see this project uses JUnit 3.8.2 (TestCase-based) and Maven with Java 1.5 compatibility. I'll generate tests accordingly.

```java
org/joda/time/field/UnsupportedDurationFieldTest.java
```

```java
package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

/**
 * Regression tests for UnsupportedDurationField.
 * Tests the documented behavior of this placeholder implementation for unsupported duration fields.
 */
public class UnsupportedDurationFieldTest extends TestCase {

    private UnsupportedDurationField fieldSeconds;
    private UnsupportedDurationField fieldMinutes;

    public void setUp() {
        fieldSeconds = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        fieldMinutes = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    }

    // Constructor and getInstance tests

    public void testGetInstanceReturnsSameInstanceForSameType() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertSame("getInstance should return cached instance", field1, field2);
    }

    public void testGetInstanceReturnsDifferentInstancesForDifferentTypes() {
        UnsupportedDurationField seconds = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField minutes = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertNotSame("getInstance should return different instances for different types", seconds, minutes);
    }

    // Basic property tests

    public void testGetType() {
        assertEquals("getType should return the type used to construct field",
                DurationFieldType.seconds(), fieldSeconds.getType());
        assertEquals("getType should return the type used to construct field",
                DurationFieldType.minutes(), fieldMinutes.getType());
    }

    public void testGetName() {
        String nameSeconds = fieldSeconds.getName();
        assertNotNull("getName should not return null", nameSeconds);
        assertTrue("getName should contain 'second'", nameSeconds.toLowerCase().contains("second"));

        String nameMinutes = fieldMinutes.getName();
        assertNotNull("getName should not return null", nameMinutes);
        assertTrue("getName should contain 'minute'", nameMinutes.toLowerCase().contains("minute"));
    }

    public void testIsSupported() {
        assertFalse("isSupported should always return false", fieldSeconds.isSupported());
        assertFalse("isSupported should always return false", fieldMinutes.isSupported());
    }

    public void testIsPrecise() {
        assertTrue("isPrecise should always return true", fieldSeconds.isPrecise());
        assertTrue("isPrecise should always return true", fieldMinutes.isPrecise());
    }

    public void testGetUnitMillis() {
        assertEquals("getUnitMillis should always return 0", 0L, fieldSeconds.getUnitMillis());
        assertEquals("getUnitMillis should always return 0", 0L, fieldMinutes.getUnitMillis());
    }

    // Comparison tests

    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("compareTo should always return 0", 0, field1.compareTo(field2));
        assertEquals("compareTo should always return 0", 0, field2.compareTo(field1));
        assertEquals("compareTo should always return 0", 0, field1.compareTo(field1));
    }

    public void testEqualsWithSameType() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertTrue("equals should return true for same cached instance", field1.equals(field2));
    }

    public void testEqualsWithDifferentTypes() {
        UnsupportedDurationField seconds = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField minutes = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertFalse("equals should return false for different types", seconds.equals(minutes));
    }

    public void testEqualsWithSelf() {
        assertTrue("equals should return true for same object", fieldSeconds.equals(fieldSeconds));
    }

    public void testEqualsWithNull() {
        assertFalse("equals should return false for null", fieldSeconds.equals(null));
    }

    public void testEqualsWithDifferentClass() {
        assertFalse("equals should return false for different class", fieldSeconds.equals("not a field"));
        assertFalse("equals should return false for different class", fieldSeconds.equals(42));
    }

    public void testHashCode() {
        int hash = fieldSeconds.hashCode();
        assertEquals("hashCode should be based on name", fieldSeconds.getName().hashCode(), hash);
    }

    public void testHashCodeConsistent() {
        int hash1 = fieldSeconds.hashCode();
        int hash2 = fieldSeconds.hashCode();
        assertEquals("hashCode should be consistent", hash1, hash2);
    }

    public void testHashCodeEqualForEqualFields() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals("hashCode should be equal for equal fields", field1.hashCode(), field2.hashCode());
    }

    // toString tests

    public void testToString() {
        String str = fieldSeconds.toString();
        assertNotNull("toString should not return null", str);
        assertTrue("toString should contain 'UnsupportedDurationField'", str.contains("UnsupportedDurationField"));
        assertTrue("toString should contain field name", str.contains(fieldSeconds.getName()));
    }

    public void testToStringFormat() {
        String str = fieldSeconds.toString();
        assertTrue("toString should start with 'UnsupportedDurationField['", str.startsWith("UnsupportedDurationField["));
        assertTrue("toString should end with ']'", str.endsWith("]"));
    }

    // Exception throwing tests

    public void testGetValueThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getValue(1000L);
            fail("getValue(long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
            assertTrue("Exception message should mention the type", e.getMessage().contains(fieldSeconds.getType().toString()));
        }
    }

    public void testGetValueWithInstantThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getValue(1000L, 2000L);
            fail("getValue(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetValueAsLongThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getValueAsLong(1000L);
            fail("getValueAsLong(long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetValueAsLongWithInstantThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getValueAsLong(1000L, 2000L);
            fail("getValueAsLong(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetMillisIntThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getMillis(5);
            fail("getMillis(int) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetMillisLongThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getMillis(5L);
            fail("getMillis(long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetMillisIntWithInstantThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getMillis(5, 1000L);
            fail("getMillis(int, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetMillisLongWithInstantThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getMillis(5L, 1000L);
            fail("getMillis(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testAddIntThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.add(1000L, 5);
            fail("add(long, int) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testAddLongThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.add(1000L, 5L);
            fail("add(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetDifferenceThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getDifference(2000L, 1000L);
            fail("getDifference(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    public void testGetDifferenceAsLongThrowsUnsupportedOperationException() {
        try {
            fieldSeconds.getDifferenceAsLong(2000L, 1000L);
            fail("getDifferenceAsLong(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should have a message", e.getMessage());
        }
    }

    // Serialization test

    public void testReadResolveReturnsSameInstance() throws Exception {
        // Test serialization behavior by verifying readResolve returns cached instance
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        // Simulate readResolve by getting another instance of the same type
        UnsupportedDurationField resolvedField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        
        assertSame("readResolve should return the cached instance", field, resolvedField);
    }

    public void testBoundaryConditionsForExceptions() {
        // Test with boundary values
        try {
            fieldSeconds.getValue(Long.MAX_VALUE);
            fail("Should throw UnsupportedOperationException for Long.MAX_VALUE");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            fieldSeconds.getValue(Long.MIN_VALUE);
            fail("Should throw UnsupportedOperationException for Long.MIN_VALUE");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            fieldSeconds.getValue(0L);
            fail("Should throw UnsupportedOperationException for 0");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
```

```java
org/joda/time/PartialTest.java
```

```java
package org.joda.time;

import junit.framework.TestCase;
import java.util.Locale;
import org.joda.time.chrono.ISOChronology;

/**
 * Regression tests for Partial.
 * Tests the immutable partial datetime implementation supporting any set of datetime fields.
 */
public class PartialTest extends TestCase {

    private Chronology iso;

    public void setUp() {
        iso = ISOChronology.getInstanceUTC();
    }

    // Constructor tests

    public void testNoArgConstructor() {
        Partial p = new Partial();
        assertNotNull("Partial should be created", p);
        assertEquals("Empty partial should have size 0", 0, p.size());
        assertNotNull("Chronology should not be null", p.getChronology());
    }

    public void testConstructorWithChronology() {
        Partial p = new Partial(iso);
        assertNotNull("Partial should be created with chronology", p);
        assertEquals("Empty partial should have size 0", 0, p.size());
        assertNotNull("Chronology should be set", p.getChronology());
    }

    public void testConstructorWithChronologyNull() {
        Partial p = new Partial((Chronology) null);
        assertNotNull("Partial should be created with null chronology", p);
        assertNotNull("Chronology should default to ISO", p.getChronology());
    }

    public void testConstructorWithSingleFieldAndValue() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertNotNull("Partial should be created", p);
        assertEquals("Partial should have size 1", 1, p.size());
        assertEquals("Value should be 15", 15, p.getValue(0));
        assertEquals("Field type should match", DateTimeFieldType.dayOfMonth(), p.getFieldType(0));
    }

    public void testConstructorWithFieldAndValueAndChronology() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15, iso);
        assertNotNull("Partial should be created", p);
        assertEquals("Partial should have size 1", 1, p.size());
        assertEquals("Value should be 15", 15, p.getValue(0));
    }

    public void testConstructorWithNullFieldType() {
        try {
            new Partial(null, 15);
            fail("Constructor should throw IllegalArgumentException for null field type");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception message should mention field type", e.getMessage().contains("field type"));
        }
    }

    public void testConstructorWithFieldsAndValues() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = new int[] { 2013, 5, 20 };
        
        Partial p = new Partial(types, values);
        assertNotNull("Partial should be created", p);
        assertEquals("Partial should have size 3", 3, p.size());
        assertEquals("Year should be 2013", 2013, p.getValue(0));
        assertEquals("Month should be 5", 5, p.getValue(1));
        assertEquals("Day should be 20", 20, p.getValue(2));
    }

    public void testConstructorWithFieldsValuesAndChronology() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        int[] values = new int[] { 2013, 5 };
        
        Partial p = new Partial(types, values, iso);
        assertEquals("Size should be 2", 2, p.size());
        assertNotNull("Chronology should be set", p.getChronology());
    }

    public void testConstructorWithNullFieldsArray() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[] {});
            fail("Should throw IllegalArgumentException for null types array");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention types array", e.getMessage().contains("Types array"));
        }
    }

    public void testConstructorWithNullValuesArray() {
        try {
            new Partial(new DateTimeFieldType[] {}, null);
            fail("Should throw IllegalArgumentException for null values array");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention values array", e.getMessage().contains("Values array"));
        }
    }

    public void testConstructorWithMismatchedArrayLengths() {
        DateTimeFieldType[] types = new DateTimeFieldType[] { DateTimeFieldType.dayOfMonth() };
        int[] values = new int[] { 15, 20 };
        
        try {
            new Partial(types, values);
            fail("Should throw IllegalArgumentException for mismatched array lengths");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention same length", e.getMessage().contains("same length"));
        }
    }

    public void testConstructorWithEmptyArrays() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {};
        int[] values = new int[] {};
        
        Partial p = new Partial(types, values);
        assertEquals("Size should be 0", 0, p.size());
    }

    public void testConstructorFromReadablePartial() {
        Partial p1 = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial p2 = new Partial(p1);
        
        assertEquals("Copy should have same size", p1.size(), p2.size());
        assertEquals("Copy should have same values", p1.getValue(0), p2.getValue(0));
        assertEquals("Copy should have same field types", p1.getFieldType(0), p2.getFieldType(0));
    }

    public void testConstructorFromReadablePartialNull() {
        try {
            new Partial((ReadablePartial) null);
            fail("Should throw IllegalArgumentException for null partial");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention partial", e.getMessage().contains("partial"));
        }
    }

    // Size and accessors tests

    public void testSize() {
        Partial empty = new Partial();
        assertEquals("Empty partial should have size 0", 0, empty.size());
        
        Partial single = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        assertEquals("Single field partial should have size 1", 1, single.size());
        
        Partial multi = new Partial(
            new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() },
            new int[] { 2013, 5 }
        );
        assertEquals("Multi field partial should have size 2", 2, multi.size());
    }

    public void testGetValue() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        assertEquals("Year value should be 2013", 2013, p.getValue(0));
        assertEquals("Month value should be 5", 5, p.getValue(1));
        assertEquals("Day value should be 20", 20, p.getValue(2));
    }

    public void testGetValueOutOfBounds() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        
        try {
            p.getValue(1);
            fail("Should throw IndexOutOfBoundsException for invalid index");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testGetValues() {
        int[] values = new int[] { 2013, 5, 20 };
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, values);
        
        int[] retrieved = p.getValues();
        assertNotNull("Values should not be null", retrieved);
        assertEquals("Should have 3 values", 3, retrieved.length);
        assertEquals("Year should be 2013", 2013, retrieved[0]);
        assertEquals("Month should be 5", 5, retrieved[1]);
        assertEquals("Day should be 20", 20, retrieved[2]);
    }

    public void testGetValuesIsClone() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 5 });
        
        int[] values1 = p.getValues();
        int[] values2 = p.getValues();
        
        assertNotSame("getValues should return a clone", values1, values2);
    }

    public void testGetChronology() {
        Partial p1 = new Partial();
        assertNotNull("Chronology should not be null", p1.getChronology());
        
        Partial p2 = new Partial(iso);
        assertNotNull("Chronology should be set", p2.getChronology());
    }

    public void testGetFieldType() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        assertEquals("Field 0 should be year", DateTimeFieldType.year(), p.getFieldType(0));
        assertEquals("Field 1 should be month", DateTimeFieldType.monthOfYear(), p.getFieldType(1));
        assertEquals("Field 2 should be day", DateTimeFieldType.dayOfMonth(), p.getFieldType(2));
    }

    public void testGetFieldTypeOutOfBounds() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        
        try {
            p.getFieldType(1);
            fail("Should throw IndexOutOfBoundsException for invalid index");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testGetFieldTypes() {
        DateTimeFieldType[] types = new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        Partial p = new Partial(types, new int[] { 2013, 5, 20 });
        
        DateTimeFieldType[] retrieved = p.getFieldTypes();
        assertNotNull("Field types should not be null", retrieved);
        assertEquals("Should have 3 field types", 3, retrieved.length);
        assertEquals("Field 0 should be year", types[0], retrieved[0]);
        assertEquals("Field 1 should be month", types[1], retrieved[1]);
        assertEquals("Field 2 should be day", types[2], retrieved[2]);
    }

    public void testGetFieldTypesIsClone() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 5 });
        
        DateTimeFieldType[] types1 = p.getFieldTypes();
        DateTimeFieldType[] types2 = p.getFieldTypes();
        
        assertNotSame("getFieldTypes should return a clone", types1, types2);
    }

    // Matching tests

    public void testIsMatchWithReadableInstant() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 5, 20 });
        
        LocalDate date = new LocalDate(2013, 5, 20);
        assertTrue("Should match instant with same month and day", p.isMatch(date));
        
        LocalDate differentDate = new LocalDate(2013, 5, 21);
        assertFalse("Should not match instant with different day", p.isMatch(differentDate));
    }

    public void testIsMatchWithReadablePartial() {
        Partial p1 = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 5, 20 });
        
        Partial p2 = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        assertTrue("Partial should match another partial with same fields", p1.isMatch(p2));
    }

    public void testIsMatchWithPartialNull() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        
        try {
            p.isMatch((ReadablePartial) null);
            fail("Should throw IllegalArgumentException for null partial");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention partial", e.getMessage().contains("partial"));
        }
    }

    public void testIsMatchEmptyPartial() {
        Partial empty = new Partial();
        LocalDate date = new LocalDate(2013, 5, 20);
        assertTrue("Empty partial should match any instant", empty.isMatch(date));
    }

    // With operations tests

    public void testWithField() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial result = p.with(DateTimeFieldType.monthOfYear(), 5);
        
        assertNotSame("with should return a new instance", p, result);
        assertEquals("New partial should have 2 fields", 2, result.size());
        assertEquals("Day should still be 15", 15, result.getValue(0));
        assertEquals("Month should be 5", 5, result.getValue(1));
    }

    public void testWithFieldUpdateExisting() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial result = p.with(DateTimeFieldType.dayOfMonth(), 20);
        
        assertNotSame("with should return a new instance", p, result);
        assertEquals("Should still have 1 field", 1, result.size());
        assertEquals("Day should be updated to 20", 20, result.getValue(0));
    }

    public void testWithFieldSameValue() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial result = p.with(DateTimeFieldType.dayOfMonth(), 15);
        
        assertSame("with same value should return same instance", p, result);
    }

    public void testWithFieldNull() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        
        try {
            p.with(null, 20);
            fail("Should throw IllegalArgumentException for null field type");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention field type", e.getMessage().contains("field type"));
        }
    }

    public void testWithout() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        Partial result = p.without(DateTimeFieldType.monthOfYear());
        
        assertNotSame("without should return a new instance", p, result);
        assertEquals("Result should have 2 fields", 2, result.size());
        assertEquals("Year should be 2013", 2013, result.getValue(0));
        assertEquals("Day should be 20", 20, result.getValue(1));
    }

    public void testWithoutNonExistentField() {
        Partial p = new Partial(DateTimeFieldType.dayOfMonth(), 15);
        Partial result = p.without(DateTimeFieldType.monthOfYear());
        
        assertSame("without non-existent field should return same instance", p, result);
    }

    public void testWithChronologyRetainFields() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        }, new int[] { 2013, 5 }, iso);
        
        Partial result = p.withChronologyRetainFields(iso);
        assertSame("withChronologyRetainFields with same chronology should return same instance", p, result);
    }

    // toString tests

    public void testToString() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        String str = p.toString();
        assertNotNull("toString should not return null", str);
    }

    public void testToStringWithPattern() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        String str = p.toString("yyyy-MM-dd");
        assertNotNull("toString with pattern should not return null", str);
    }

    public void testToStringWithPatternNull() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        String str = p.toString((String) null);
        assertNotNull("toString with null pattern should not return null", str);
    }

    public void testToStringWithPatternAndLocale() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        String str = p.toString("yyyy-MM-dd", Locale.ENGLISH);
        assertNotNull("toString with pattern and locale should not return null", str);
    }

    public void testToStringList() {
        Partial p = new Partial(new DateTimeFieldType[] {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        }, new int[] { 2013, 5, 20 });
        
        String str = p.toStringList();
        assertNotNull("toStringList should not return null", str);
        assertTrue("toStringList should contain field names", str.contains("year"));
        assertTrue("toStringList should contain values", str.contains("2013"));
    }

    public void testToStringListEmpty() {
        Partial p = new Partial();
        String str = p.toStringList();
        assertNotNull("toString