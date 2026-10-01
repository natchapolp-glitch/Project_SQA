/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Locale;

/**
 * Test class for Partial.
 *
 * @author test generation
 */
public class PartialTest extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(PartialTest.class);
    }

    public PartialTest(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    // Constructor tests
    //-----------------------------------------------------------------------

    public void testDefaultConstructor() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNotNull(partial.getChronology());
    }

    public void testConstructorWithSingleField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        assertEquals(1, partial.size());
        assertEquals(2023, partial.getValue(0));
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
    }

    public void testConstructorWithSingleFieldAndChronology() {
        Partial partial = new Partial(DateTimeFieldType.monthOfYear(), 6, null);
        assertEquals(1, partial.size());
        assertEquals(6, partial.getValue(0));
        assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldType(0));
    }

    public void testConstructorWithNullTypeThrowsException() {
        try {
            new Partial(null, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testConstructorWithInvalidValueThrowsException() {
        try {
            new Partial(DateTimeFieldType.monthOfYear(), 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testConstructorWithMultipleFieldsInOrder() {
        DateTimeFieldType[] types = {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };
        int[] values = {2023, 6, 15};
        Partial partial = new Partial(types, values);
        assertEquals(3, partial.size());
        assertEquals(2023, partial.getValue(0));
        assertEquals(6, partial.getValue(1));
        assertEquals(15, partial.getValue(2));
    }

    public void testConstructorWithMultipleFieldsInOrderWithChronology() {
        DateTimeFieldType[] types = {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear()
        };
        int[] values = {2023, 6};
        Partial partial = new Partial(types, values, null);
        assertEquals(2, partial.size());
        assertEquals(2023, partial.getValue(0));
        assertEquals(6, partial.getValue(1));
    }

    public void testConstructorWithEmptyArrays() {
        DateTimeFieldType[] types = new DateTimeFieldType[0];
        int[] values = new int[0];
        Partial partial = new Partial(types, values);
        assertEquals(0, partial.size());
    }

    public void testConstructorWithNullTypesArrayThrowsException() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[]{1});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Types array must not be null"));
        }
    }

    public void testConstructorWithNullValuesArrayThrowsException() {
        try {
            new Partial(new DateTimeFieldType[]{DateTimeFieldType.year()}, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Values array must not be null"));
        }
    }

    public void testConstructorWithMismatchedArrayLengthsThrowsException() {
        try {
            new Partial(
                new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
                new int[]{2023}
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("same length"));
        }
    }

    public void testConstructorWithNullElementInTypesArrayThrowsException() {
        try {
            new Partial(
                new DateTimeFieldType[]{DateTimeFieldType.year(), null},
                new int[]{2023, 6}
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Types array must not contain null"));
        }
    }

    public void testConstructorWithDuplicateTypesThrowsException() {
        try {
            new Partial(
                new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.year()},
                new int[]{2023, 2024}
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("duplicate"));
        }
    }

    public void testConstructorWithWrongOrderThrowsException() {
        // monthOfYear is smaller than year, so year should come first
        try {
            new Partial(
                new DateTimeFieldType[]{DateTimeFieldType.monthOfYear(), DateTimeFieldType.year()},
                new int[]{6, 2023}
            );
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected - wrong order
        }
    }

    public void testCopyConstructor() {
        Partial original = new Partial(
            new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[]{2023, 6}
        );
        Partial copy = new Partial(original);
        assertEquals(2, copy.size());
        assertEquals(2023, copy.getValue(0));
        assertEquals(6, copy.getValue(1));
        assertEquals(DateTimeFieldType.year(), copy.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), copy.getFieldType(1));
    }

    public void testCopyConstructorWithNullThrowsException() {
        try {
            new Partial((ReadablePartial) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be null"));
        }
    }

    public void testConstructorWithChronology() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023, ISOChronology.getInstanceUTC());
        assertEquals(2023, partial.getValue(0));
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
    }

    //-----------------------------------------------------------------------
    // Tests for size
    //-----------------------------------------------------------------------

    public void testSize() {
        Partial empty = new Partial();
        assertEquals(0, empty.size());

        Partial single = new Partial(DateTimeFieldType.year(), 2023);
        assertEquals(1, single.size());

        Partial multiple = new Partial(
            new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[]{2023, 6}
        );
        assertEquals(2, multiple.size());
    }

    //-----------------------------------------------------------------------
    // Tests for getChronology
    //-----------------------------------------------------------------------

    public void testGetChronology() {
        Partial partial = new Partial();
        assertNotNull(partial.getChronology());
        assertEquals(DateTimeZone.UTC, partial.getChronology().getZone());
    }

    public void testGetChronologyWithISOChronology() {
        Partial partial = new Partial(ISOChronology.getInstanceUTC());
        assertEquals(DateTimeZone.UTC, partial.getChronology().getZone());
    }

    //-----------------------------------------------------------------------
    // Tests for getFieldType
    //-----------------------------------------------------------------------

    public void testGetFieldType() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
    }

    public void testGetFieldTypeWithIndexOutOfBoundsThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        try {
            partial.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getFieldTypes
    //-----------------------------------------------------------------------

    public void testGetFieldTypes() {
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(types, new int[]{2023, 6});
        DateTimeFieldType[] returned = partial.getFieldTypes();
        assertEquals(2, returned.length);
        assertEquals(DateTimeFieldType.year(), returned[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), returned[1]);
        // Should be a clone
        assertNotSame(types, returned);
    }

    //-----------------------------------------------------------------------
    // Tests for getValue
    //-----------------------------------------------------------------------

    public void testGetValueWithIndex() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        assertEquals(2023, partial.getValue(0));
    }

    public void testGetValueWithIndexOutOfBoundsThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        try {
            partial.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getValues
    //-----------------------------------------------------------------------

    public void testGetValues() {
        int[] values = {2023, 6, 15};
        Partial partial = new Partial(
            new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()},
            values
        );
        int[] returned = partial.getValues();
        assertEquals(3, returned.length);
        assertEquals(2023, returned[0]);
        assertEquals(6, returned[1]);
        assertEquals(15, returned[2]);
        // Should be a clone
        assertNotSame(values, returned);
    }

    //-----------------------------------------------------------------------
    // Tests for with
    //-----------------------------------------------------------------------

    public void testWithNewField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        Partial result = partial.with(DateTimeFieldType.monthOfYear(), 6);
        assertEquals(2, result.size());
        assertEquals(2023, result.getValue(0));
        assertEquals(6, result.getValue(1));
    }

    public void testWithReplaceExistingField() {
        Partial partial = new Partial(
            new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[]{2023, 6}
        );
        Partial result = partial.with(DateTimeFieldType.monthOfYear(), 12);
        assertEquals(2, result.size());
        assertEquals(2023, result.getValue(0));
        assertEquals(12, result.getValue(1));
    }

    public void testWithSameValueReturnsSameInstance() {
        Partial partial = new Partial(
            new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
            new int[]{2023, 6}
        );
        Partial result = partial.with(DateTimeFieldType.year(), 2023);
        assertSame(partial, result);
    }

    public void testWithNullFieldTypeThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        try {
            partial.with(null, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must not be null"));
        }
    }

    

    

    //-----------------------------------------------------------------------
    // Tests for without
    //-----------------------------------------------------------------------

    

    

    

    //-----------------------------------------------------------------------
    // Tests for withField
    //-----------------------------------------------------------------------

    

    

    

    //-----------------------------------------------------------------------
    // Tests for withFieldAdded
    //-----------------------------------------------------------------------

    

    

    

    

    //-----------------------------------------------------------------------
    // Tests for withFieldAddWrapped
    //-----------------------------------------------------------------------

    

    

    //-----------------------------------------------------------------------
    // Tests for withPeriodAdded
    //-----------------------------------------------------------------------

    

    

    

    //-----------------------------------------------------------------------
    // Tests for plus
    //-----------------------------------------------------------------------

    

    

    //-----------------------------------------------------------------------
    // Tests for minus
    //-----------------------------------------------------------------------

    

    

    //-----------------------------------------------------------------------
    // Tests for property
    //-----------------------------------------------------------------------

    

    

    
}
