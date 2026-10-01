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
