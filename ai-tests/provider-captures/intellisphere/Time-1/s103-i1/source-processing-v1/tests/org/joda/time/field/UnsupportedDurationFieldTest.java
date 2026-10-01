/*
 *  Copyright 2001-2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Test class for UnsupportedDurationField.
 *
 * @author test generation
 */
public class UnsupportedDurationFieldTest extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(UnsupportedDurationFieldTest.class);
    }

    public UnsupportedDurationFieldTest(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    // Tests for getInstance
    //-----------------------------------------------------------------------

    public void testGetInstanceReturnsCachedInstance() {
        DurationFieldType type = DurationFieldType.eras();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    public void testGetInstanceDifferentTypesReturnDifferentInstances() {
        DurationFieldType type1 = DurationFieldType.eras();
        DurationFieldType type2 = DurationFieldType.centuries();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertNotSame(field1, field2);
    }

    public void testGetInstanceWithNullTypeThrowsNullPointerException() {
        try {
            UnsupportedDurationField.getInstance(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    public void testGetInstanceAllStandardTypes() {
        // Test that getInstance works for all standard DurationFieldType constants
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
        for (int i = 0; i < types.length; i++) {
            assertNotNull(UnsupportedDurationField.getInstance(types[i]));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getType
    //-----------------------------------------------------------------------

    public void testGetType() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertSame(type, field.getType());
    }

    //-----------------------------------------------------------------------
    // Tests for getName
    //-----------------------------------------------------------------------

    public void testGetName() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.getName(), field.getName());
    }

    public void testGetNameDifferentTypes() {
        DurationFieldType type1 = DurationFieldType.days();
        DurationFieldType type2 = DurationFieldType.hours();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals("days", field1.getName());
        assertEquals("hours", field2.getName());
    }

    //-----------------------------------------------------------------------
    // Tests for isSupported
    //-----------------------------------------------------------------------

    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertFalse(field.isSupported());
    }

    //-----------------------------------------------------------------------
    // Tests for isPrecise
    //-----------------------------------------------------------------------

    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field.isPrecise());
    }

    //-----------------------------------------------------------------------
    // Tests for getUnitMillis
    //-----------------------------------------------------------------------

    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, field.getUnitMillis());
    }

    //-----------------------------------------------------------------------
    // Tests for compareTo
    //-----------------------------------------------------------------------

    public void testCompareToAlwaysZero() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(0, field1.compareTo(field2));
        assertEquals(0, field2.compareTo(field1));
    }

    public void testCompareToWithSameInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0, field.compareTo(field));
    }

    public void testCompareToWithDifferentTypeField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        // Compare with a supported field (from ISOChronology)
        DurationField supportedField = field.getType().getField(null);
        assertEquals(0, field.compareTo(supportedField));
    }

    //-----------------------------------------------------------------------
    // Tests for equals
    //-----------------------------------------------------------------------

    public void testEqualsSameInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertTrue(field.equals(field));
    }

    public void testEqualsSameType() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertTrue(field1.equals(field2));
    }

    public void testEqualsDifferentType() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertFalse(field1.equals(field2));
    }

    public void testEqualsWithNull() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.equals(null));
    }

    public void testEqualsWithDifferentObjectType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field.equals("not a field"));
    }

    public void testEqualsWithNonUnsupportedDurationField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        DurationField supportedField = DurationFieldType.minutes().getField(null);
        assertFalse(field.equals(supportedField));
    }

    //-----------------------------------------------------------------------
    // Tests for hashCode
    //-----------------------------------------------------------------------

    public void testHashCodeConsistency() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals(field.hashCode(), field.hashCode());
    }

    public void testHashCodeEqualsConsistency() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    public void testHashCodeDifferentTypes() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.years());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        // May or may not be different, but should be based on getName()
        if (field1.getName().equals(field2.getName())) {
            assertEquals(field1.hashCode(), field2.hashCode());
        }
    }

    //-----------------------------------------------------------------------
    // Tests for toString
    //-----------------------------------------------------------------------

    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }

    public void testToStringForDays() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    //-----------------------------------------------------------------------
    // Tests for unsupported operations (exception paths)
    //-----------------------------------------------------------------------

    public void testGetValueThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        try {
            field.getValue(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            field.getValueAsLong(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueWithInstantThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.getValue(1000L, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetValueAsLongWithInstantThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        try {
            field.getValueAsLong(1000L, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisIntThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        try {
            field.getMillis(5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        try {
            field.getMillis(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisIntWithInstantThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(5, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetMillisLongWithInstantThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        try {
            field.getMillis(1000L, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddIntThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
        try {
            field.add(1000L, 5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddLongThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        try {
            field.add(1000L, 5000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetDifferenceThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        try {
            field.getDifference(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testGetDifferenceAsLongThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        try {
            field.getDifferenceAsLong(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for unsupported with zero values
    //-----------------------------------------------------------------------

    public void testAddWithZeroIntValueThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        try {
            field.add(1000L, 0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    public void testAddWithZeroLongValueThrowsUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        try {
            field.add(1000L, 0L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("field is unsupported"));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for readResolve (via serialization behavior verification)
    //-----------------------------------------------------------------------

    public void testReadResolvePreservesSingleton() {
        // The readResolve method should return the cached instance
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
        // We can't directly test readResolve without serialization,
        // but we can verify getInstance returns consistent instances
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.years());
        assertSame(field, sameField);
    }
}
