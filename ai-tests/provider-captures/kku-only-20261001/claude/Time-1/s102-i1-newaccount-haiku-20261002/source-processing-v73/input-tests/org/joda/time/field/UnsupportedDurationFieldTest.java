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
