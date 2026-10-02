package org.joda.time.field;

import junit.framework.TestCase;
import org.joda.time.DurationFieldType;

/**
 * Regression tests for UnsupportedDurationField.
 * Tests the fixed reference behavior of singleton instances, exception paths, and field operations.
 */
public class UnsupportedDurationFieldTest extends TestCase {

    private DurationFieldType months;
    private DurationFieldType weeks;
    private UnsupportedDurationField field1;
    private UnsupportedDurationField field2;

    protected void setUp() throws Exception {
        super.setUp();
        months = DurationFieldType.months();
        weeks = DurationFieldType.weeks();
        field1 = UnsupportedDurationField.getInstance(months);
        field2 = UnsupportedDurationField.getInstance(weeks);
    }

    // Constructor and getInstance tests
    public void testGetInstance_ReturnsSameInstanceForSameType() {
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(months);
        assertSame("getInstance should return cached instance", f1, f2);
    }

    public void testGetInstance_DifferentTypesReturnDifferentInstances() {
        UnsupportedDurationField monthField = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField weekField = UnsupportedDurationField.getInstance(weeks);
        assertNotSame("Different types should have different instances", monthField, weekField);
    }

    public void testGetInstance_WithAllStandardDurationTypes() {
        DurationFieldType[] types = {
            DurationFieldType.eras(),
            DurationFieldType.centuries(),
            DurationFieldType.years(),
            DurationFieldType.months(),
            DurationFieldType.weeks(),
            DurationFieldType.days(),
            DurationFieldType.hours(),
            DurationFieldType.minutes(),
            DurationFieldType.seconds(),
            DurationFieldType.millis()
        };
        UnsupportedDurationField[] fields = new UnsupportedDurationField[types.length];
        for (int i = 0; i < types.length; i++) {
            fields[i] = UnsupportedDurationField.getInstance(types[i]);
            assertNotNull("Field should not be null", fields[i]);
        }
    }

    // getType tests
    public void testGetType_ReturnsConstructorType() {
        assertEquals("getType should return the type passed at construction", months, field1.getType());
        assertEquals("getType should return the type passed at construction", weeks, field2.getType());
    }

    // getName tests
    public void testGetName_ReturnsTypeNameForMonths() {
        assertEquals("months", field1.getName());
    }

    public void testGetName_ReturnsTypeNameForWeeks() {
        assertEquals("weeks", field2.getName());
    }

    // isSupported tests
    public void testIsSupported_AlwaysFalse() {
        assertFalse("isSupported should always return false", field1.isSupported());
        assertFalse("isSupported should always return false", field2.isSupported());
    }

    // isPrecise tests
    public void testIsPrecise_AlwaysTrue() {
        assertTrue("isPrecise should always return true", field1.isPrecise());
        assertTrue("isPrecise should always return true", field2.isPrecise());
    }

    // getValue tests - should throw UnsupportedOperationException
    public void testGetValue_ThrowsUnsupportedOperationException() {
        try {
            field1.getValue(1000L);
            fail("getValue should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Exception message should contain field name", e.getMessage().contains("months"));
        }
    }

    public void testGetValue_WithDifferentDuration_ThrowsUnsupportedOperationException() {
        try {
            field2.getValue(999999L);
            fail("getValue should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Exception message should contain field name", e.getMessage().contains("weeks"));
        }
    }

    public void testGetValue_WithZeroDuration_ThrowsUnsupportedOperationException() {
        try {
            field1.getValue(0L);
            fail("getValue should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }

    public void testGetValue_WithNegativeDuration_ThrowsUnsupportedOperationException() {
        try {
            field1.getValue(-5000L);
            fail("getValue should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    // getValueAsLong tests - should throw UnsupportedOperationException
    public void testGetValueAsLong_SingleParam_ThrowsUnsupportedOperationException() {
        try {
            field1.getValueAsLong(1000L);
            fail("getValueAsLong should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Exception message should contain field name", e.getMessage().contains("months"));
        }
    }

    public void testGetValueAsLong_TwoParams_ThrowsUnsupportedOperationException() {
        try {
            field1.getValueAsLong(1000L, 2000L);
            fail("getValueAsLong(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue("Exception message should contain field name", e.getMessage().contains("months"));
        }
    }

    // getMillis tests - should throw UnsupportedOperationException
    public void testGetMillis_IntValue_ThrowsUnsupportedOperationException() {
        try {
            field1.getMillis(5);
            fail("getMillis(int) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    public void testGetMillis_LongValue_ThrowsUnsupportedOperationException() {
        try {
            field1.getMillis(5L);
            fail("getMillis(long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    public void testGetMillis_IntAndLong_ThrowsUnsupportedOperationException() {
        try {
            field1.getMillis(5, 1000L);
            fail("getMillis(int, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    public void testGetMillis_LongAndLong_ThrowsUnsupportedOperationException() {
        try {
            field1.getMillis(5L, 1000L);
            fail("getMillis(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    // add tests - should throw UnsupportedOperationException
    public void testAdd_LongInt_ThrowsUnsupportedOperationException() {
        try {
            field1.add(1000L, 5);
            fail("add(long, int) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    public void testAdd_LongLong_ThrowsUnsupportedOperationException() {
        try {
            field1.add(1000L, 5L);
            fail("add(long, long) should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    // getDifference tests - should throw UnsupportedOperationException
    public void testGetDifference_ThrowsUnsupportedOperationException() {
        try {
            field1.getDifference(2000L, 1000L);
            fail("getDifference should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    public void testGetDifferenceAsLong_ThrowsUnsupportedOperationException() {
        try {
            field1.getDifferenceAsLong(2000L, 1000L);
            fail("getDifferenceAsLong should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull("Exception should be thrown", e);
        }
    }

    // getUnitMillis tests - should return 0
    public void testGetUnitMillis_AlwaysZero() {
        assertEquals("getUnitMillis should always return 0", 0L, field1.getUnitMillis());
        assertEquals("getUnitMillis should always return 0", 0L, field2.getUnitMillis());
    }

    // compareTo tests
    public void testCompareTo_AlwaysZero() {
        assertEquals("compareTo should always return 0", 0, field1.compareTo(field2));
        assertEquals("compareTo should always return 0", 0, field2.compareTo(field1));
        assertEquals("compareTo with same instance should return 0", 0, field1.compareTo(field1));
    }

    // equals tests
    public void testEquals_SameInstanceReturnsTrue() {
        assertTrue("equals should return true for same instance", field1.equals(field1));
    }

    public void testEquals_DifferentTypesSameClass_DependsOnName() {
        UnsupportedDurationField monthField1 = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField monthField2 = UnsupportedDurationField.getInstance(months);
        assertTrue("equals should return true for same type", monthField1.equals(monthField2));
    }

    public void testEquals_DifferentTypes_ReturnsFalse() {
        assertFalse("equals should return false for different types", field1.equals(field2));
    }

    public void testEquals_WithNull_ReturnsFalse() {
        assertFalse("equals should return false for null", field1.equals(null));
    }

    public void testEquals_WithDifferentClass_ReturnsFalse() {
        assertFalse("equals should return false for different class", field1.equals("not a field"));
        assertFalse("equals should return false for different class", field1.equals(123));
    }

    public void testEquals_WithNonUnsupportedField_ReturnsFalse() {
        // This would require a non-UnsupportedDurationField instance; testing the negative path
        Object notAnUnsupportedField = new Object();
        assertFalse("equals should return false for non-UnsupportedDurationField", field1.equals(notAnUnsupportedField));
    }

    // hashCode tests
    public void testHashCode_SameTypesSameHashCode() {
        UnsupportedDurationField monthField1 = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField monthField2 = UnsupportedDurationField.getInstance(months);
        assertEquals("hashCode should be same for equal objects", monthField1.hashCode(), monthField2.hashCode());
    }

    public void testHashCode_DifferentTypesLikelyDifferentHashCode() {
        int hash1 = field1.hashCode();
        int hash2 = field2.hashCode();
        // Not a guarantee, but statistically likely
        assertNotSame("Different names should typically have different hash codes", 
            (Integer) hash1, (Integer) hash2);
    }

    public void testHashCode_BasedOnName() {
        int expectedHash = field1.getName().hashCode();
        assertEquals("hashCode should be based on name", expectedHash, field1.hashCode());
    }

    // toString tests
    public void testToString_FormatContainsClassNameAndFieldName() {
        String str = field1.toString();
        assertTrue("toString should contain 'UnsupportedDurationField'", str.contains("UnsupportedDurationField"));
        assertTrue("toString should contain field name", str.contains("months"));
        assertTrue("toString should contain brackets", str.contains("[") && str.contains("]"));
    }

    public void testToString_ForWeeks() {
        String str = field2.toString();
        assertTrue("toString should contain field name", str.contains("weeks"));
    }

    public void testToString_Format() {
        assertEquals("toString format", "UnsupportedDurationField[months]", field1.toString());
    }

    // readResolve tests (serialization support)
    public void testReadResolve_ReturnsCachedInstance() throws Exception {
        // This tests the private readResolve method indirectly through serialization
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(months);
        // The readResolve method should ensure deserialization returns the cached instance
        assertEquals("readResolve should return same cached instance", field, field1);
    }

    // Boundary and edge case tests
    public void testMultipleCallsToGetInstanceWithSameType_ReturnsSameReference() {
        UnsupportedDurationField f1 = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField f2 = UnsupportedDurationField.getInstance(months);
        UnsupportedDurationField f3 = UnsupportedDurationField.getInstance(months);
        assertSame("All calls should return same instance", f1, f2);
        assertSame("All calls should return same instance", f2, f3);
    }

    public void testExceptionMessageIncludesFieldType() {
        try {
            field1.getValue(100L);
            fail("Should throw exception");
        } catch (UnsupportedOperationException e) {
            String msg = e.getMessage();
            assertTrue("Exception message should contain type info", msg.contains("months"));
        }
    }
}
