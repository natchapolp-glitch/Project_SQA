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

    

    
}
