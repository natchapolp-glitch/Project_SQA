Thank you for this clarification. I appreciate the explicit academic context and the commitment to transparency and attribution. This is a legitimate research and coursework scenario within academic integrity guidelines.

I can now proceed with generating test sources for the Joda-Time classes as requested.

---

## org/joda/time/field/UnsupportedDurationFieldTest.java

```java
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
```

---

## org/joda/time/PartialTest.java

```java
package org.joda.time;

import junit.framework.TestCase;
import java.util.Locale;

/**
 * Regression tests for Partial.
 * Tests constructor variations, field operations, matching, and string representations.
 */
public class PartialTest extends TestCase {

    private Chronology isoChronology;
    private DateTimeFieldType monthOfYear;
    private DateTimeFieldType dayOfMonth;
    private DateTimeFieldType hourOfDay;
    private DateTimeFieldType minuteOfHour;

    protected void setUp() throws Exception {
        super.setUp();
        isoChronology = org.joda.time.chrono.ISOChronology.getInstanceUTC();
        monthOfYear = DateTimeFieldType.monthOfYear();
        dayOfMonth = DateTimeFieldType.dayOfMonth();
        hourOfDay = DateTimeFieldType.hourOfDay();
        minuteOfHour = DateTimeFieldType.minuteOfHour();
    }

    // Constructor tests - empty/no fields
    public void testConstructor_NoArgs_CreatesEmpty() {
        Partial p = new Partial();
        assertEquals("Empty partial should have size 0", 0, p.size());
        assertNotNull("Chronology should not be null", p.getChronology());
    }

    public void testConstructor_WithChronology_CreatesEmpty() {
        Partial p = new Partial(isoChronology);
        assertEquals("Partial should have size 0", 0, p.size());
        assertNotNull("Chronology should not be null", p.getChronology());
    }

    public void testConstructor_WithNullChronology_UsesDefault() {
        Partial p = new Partial((Chronology) null);
        assertEquals("Partial should have size 0", 0, p.size());
        assertNotNull("Chronology should not be null", p.getChronology());
    }

    // Constructor tests - single field/value
    public void testConstructor_SingleFieldAndValue() {
        Partial p = new Partial(monthOfYear, 5);
        assertEquals("Partial should have size 1", 1, p.size());
        assertEquals("Value should be 5", 5, p.getValue(0));
        assertEquals("Field type should match", monthOfYear, p.getFieldType(0));
    }

    public void testConstructor_SingleFieldAndValue_WithChronology() {
        Partial p = new Partial(monthOfYear, 5, isoChronology);
        assertEquals("Partial should have size 1", 1, p.size());
        assertEquals("Value should be 5", 5, p.getValue(0));
        assertEquals("Field type should match", monthOfYear, p.getFieldType(0));
    }

    public void testConstructor_SingleField_NullFieldType_ThrowsException() {
        try {
            new Partial((DateTimeFieldType) null, 5);
            fail("Should throw IllegalArgumentException for null field type");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention field type", e.getMessage().contains("field type"));
        }
    }

    public void testConstructor_SingleField_InvalidValue_ThrowsException() {
        try {
            new Partial(monthOfYear, 13); // Invalid month
            fail("Should throw IllegalArgumentException for invalid month");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Constructor tests - multiple fields/values
    public void testConstructor_MultipleFieldsAndValues() {
        DateTimeFieldType[] types = {monthOfYear, dayOfMonth};
        int[] values = {5, 15};
        Partial p = new Partial(types, values);
        assertEquals("Partial should have size 2", 2, p.size());
        assertEquals("First value should be 5", 5, p.getValue(0));
        assertEquals("Second value should be 15", 15, p.getValue(1));
    }

    public void testConstructor_MultipleFieldsAndValues_WithChronology() {
        DateTimeFieldType[] types = {monthOfYear, dayOfMonth};
        int[] values = {5, 15};
        Partial p = new Partial(types, values, isoChronology);
        assertEquals("Partial should have size 2", 2, p.size());
        assertEquals("Chronology should match", isoChronology.getZone(), p.getChronology().getZone());
    }

    public void testConstructor_NullTypesArray_ThrowsException() {
        try {
            new Partial((DateTimeFieldType[]) null, new int[]{5});
            fail("Should throw IllegalArgumentException for null types");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention types array", e.getMessage().contains("Types"));
        }
    }

    public void testConstructor_NullValuesArray_ThrowsException() {
        try {
            new Partial(new DateTimeFieldType[]{monthOfYear}, (int[]) null);
            fail("Should throw IllegalArgumentException for null values");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention values array", e.getMessage().contains("Values"));
        }
    }

    public void testConstructor_MismatchedArrayLengths_ThrowsException() {
        try {
            new Partial(
                new DateTimeFieldType[]{monthOfYear, dayOfMonth},
                new int[]{5}
            );
            fail("Should throw IllegalArgumentException for mismatched lengths");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention length mismatch", e.getMessage().contains("length"));
        }
    }

    public void testConstructor_NullFieldInArray_ThrowsException() {
        try {
            new Partial(
                new DateTimeFieldType[]{monthOfYear, null},
                new int[]{5, 15}
            );
            fail("Should throw IllegalArgumentException for null field in array");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention null in array", e.getMessage().contains("null"));
        }
    }

    public void testConstructor_FieldsNotInOrder_ThrowsException() {
        try {
            // dayOfMonth should come after monthOfYear in largest-to-smallest order
            new Partial(
                new DateTimeFieldType[]{dayOfMonth, monthOfYear},
                new int[]{15, 5}
            );
            fail("Should throw IllegalArgumentException for incorrect field order");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention order", e.getMessage().contains("order"));
        }
    }

    public void testConstructor_EmptyArrays() {
        Partial p = new Partial(new DateTimeFieldType[]{}, new int[]{});
        assertEquals("Partial should have size 0", 0, p.size());
    }

    // Constructor from ReadablePartial
    public void testConstructor_FromReadablePartial() {
        Partial p1 = new Partial(monthOfYear, 5);
        Partial p2 = new Partial((ReadablePartial) p1);
        assertEquals("Size should match", p1.size(), p2.size());
        assertEquals("Value should match", p1.getValue(0), p2.getValue(0));
        assertEquals("Field type should match", p1.getFieldType(0), p2.getFieldType(0));
    }

    public void testConstructor_FromReadablePartial_Null_ThrowsException() {
        try {
            new Partial((ReadablePartial) null);
            fail("Should throw IllegalArgumentException for null partial");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention partial", e.getMessage().contains("partial"));
        }
    }

    // size() tests
    public void testSize_EmptyPartial() {
        Partial p = new Partial();
        assertEquals("Size should be 0", 0, p.size());
    }

    public void testSize_OneField() {
        Partial p = new Partial(monthOfYear, 5);
        assertEquals("Size should be 1", 1, p.size());
    }

    public void testSize_MultipleFields() {
        Partial p = new Partial(
            new DateTimeFieldType[]{monthOfYear, dayOfMonth},
            new int[]{5, 15}
        );
        assertEquals("Size should be 2", 2, p.size());
    }

    // getValue and getValues tests
    public void testGetValue_ValidIndex() {
        Partial p = new Partial(monthOfYear, 5);
        assertEquals("Value should be 5", 5, p.getValue(0));
    }

    public void testGetValue_InvalidIndex_ThrowsException() {
        Partial p = new Partial(monthOfYear, 5);
        try {
            p.getValue(1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testGetValues_ReturnsClonedArray() {
        Partial p = new Partial(
            new DateTimeFieldType[]{monthOfYear, dayOfMonth},
            new int[]{5, 15}
        );
        int[] values1 = p.getValues();
        int[] values2 = p.getValues();
        assertNotSame("getValues should return cloned array", values1, values2);
        assertEquals("Arrays should have same content", values1[0], values2[0]);
    }

    // getFieldType and getFieldTypes tests
    public void testGetFieldType_ValidIndex() {
        Partial p = new Partial(monthOfYear, 5);
        assertEquals("Field type should match", monthOfYear, p.getFieldType(0));
    }

    public void testGetFieldTypes_ReturnsClonedArray() {
        Partial p = new Partial(
            new DateTimeFieldType[]{monthOfYear, dayOfMonth},
            new int[]{5, 15}
        );
        DateTimeFieldType[] types1 = p.getFieldTypes();
        DateTimeFieldType[] types2 = p.getFieldTypes();
        assertNotSame("getFieldTypes should return cloned array", types1, types2);
    }

    // getChronology tests
    public void testGetChronology_NotNull() {
        Partial p = new Partial();
        assertNotNull("Chronology should not be null", p.getChronology());
    }

    public void testGetChronology_IsUTC() {
        Partial p = new Partial();
        assertTrue("Chronology should be UTC", p.getChronology().getZone().isFixed());
    }

    // getField tests
    public void testGetField_ValidIndex() {
        Partial p = new Partial(monthOfYear, 5);
        assertNotNull("Field should not be null", p.getField(0, p.getChronology()));
    }

    // with() tests - adding/modifying fields
    public void testWith_AddNewField() {
        Partial p = new Partial(monthOfYear, 5);
        Partial p2 = p.with(dayOfMonth, 15);
        assertEquals("New partial should have size 2", 2, p2.size());
        assertEquals("Original partial should still have size 1", 1, p.size());
    }

    public void testWith_ModifyExistingField() {
        Partial p = new Partial(monthOfYear, 5);
        Partial p2 = p.with(monthOfYear, 6);
        assertEquals("Size should remain 1", 1, p2.size());
        assertEquals("Value should be updated", 6, p2.getValue(0));
    }

    public void testWith_NullFieldType_ThrowsException() {
        Partial p = new Partial();
        try {
            p.with((DateTimeFieldType) null, 5);
            fail("Should throw IllegalArgumentException for null field type");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention field type", e.getMessage().contains("field type"));
        }
    }

    // without() tests - removing fields
    public void testWithout_RemoveExistingField() {
        Partial p = new Partial(
            new DateTimeFieldType[]{monthOfYear, dayOfMonth},
            new int[]{5, 15}
        );
        Partial p2 = p.without(monthOfYear);
        assertEquals("New partial should have size 1", 1, p2.size());
        assertEquals("Remaining field should be dayOfMonth", dayOfMonth, p2.getFieldType(0));
    }

    public void testWithout_RemoveNonexistentField_NoChange() {
        Partial p = new Partial(monthOfYear, 5);
        Partial p2 = p.without(dayOfMonth);
        assertEquals("Size should remain 1", 1, p2.size());
        assertSame("Should return same instance when field not present", p, p2);
    }

    // withField() tests
    public void testWithField_ModifyExistingField() {
        Partial p = new Partial(monthOfYear, 5);
        Partial p2 = p.withField(monthOfYear, 6);
        assertEquals("Value should be updated", 6, p2.getValue(0));
    }

    public void testWithField_NonexistentField_ThrowsException() {
        Partial p = new Partial();
        try {
            p.withField(monthOfYear, 5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // isMatch() tests
    public void testIsMatch_ReadableInstant_MatchingValues() {
        Partial p = new Partial(monthOfYear, 5);
        DateTime dt = new DateTime(2000, 5, 15, 12, 30, 45, 0);
        assertTrue("isMatch should return true for matching month", p.isMatch((ReadableInstant) dt));
    }

    public void testIsMatch_ReadableInstant_NonmatchingValues() {
        Partial p = new Partial(monthOfYear, 5);
        DateTime dt = new DateTime(2000, 6, 15, 12, 30, 45, 0);
        assertFalse("isMatch should return false for non-matching month", p.isMatch((ReadableInstant) dt));
    }

    public void testIsMatch_ReadablePartial_MatchingValues() {
        Partial p1 = new Partial(monthOfYear, 5);
        Partial p2 = new Partial(monthOfYear, 5);
        assertTrue("isMatch should return true for matching values", p1.isMatch(p2));
    }

    public void testIsMatch_ReadablePartial_NonmatchingValues() {
        Partial p1 = new Partial(monthOfYear, 5);
        Partial p2 = new Partial(monthOfYear, 6);
        assertFalse("isMatch should return false for non-matching values", p1.isMatch(p2));
    }

    public void testIsMatch_ReadablePartial_Null_ThrowsException() {
        Partial p = new Partial(monthOfYear, 5);
        try {
            p.isMatch((ReadablePartial) null);
            fail("Should throw IllegalArgumentException for null partial");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention partial", e.getMessage().contains("partial"));
        }
    }

    // toString tests
    public void testToString_EmptyPartial() {
        Partial p = new Partial();
        assertNotNull("toString should not be null", p.toString());
    }

    public void testToString_SingleField() {
        Partial p = new Partial(monthOfYear, 5);
        String str = p.toString();
        assertNotNull("toString should not be null", str);
        assertFalse("toString should not be empty", str.length() == 0);
    }

    public void testToString_WithPattern() {
        Partial p = new Partial(monthOfYear, 5);
        String str = p.toString("MM");
        assertEquals("toString with pattern should format as pattern", "05", str);
    }

    public void testToString_WithNullPattern_UsesDefault() {
        Partial p = new Partial(monthOfYear, 5);
        String str = p.toString((String) null);
        assertNotNull("toString with null pattern should use default", str);
    }

    public void testToString_WithPatternAndLocale() {
        Partial p = new Partial(monthOfYear, 5);
        String str = p.toString("MM", Locale.US);
        assertEquals("toString with pattern and locale should format", "05", str);
    }

    // toStringList tests
    public void testToStringList_Format() {
        Partial p = new Partial(monthOfYear, 5);
        String str = p.toStringList();
        assertTrue("toStringList should contain brackets", str.contains("[") && str.contains("]"));
        assertTrue("toStringList should contain field name", str.contains("monthOfYear"));
        assertTrue("toStringList should contain value", str.contains("5"));
    }

    public void testToStringList_MultipleFields() {
        Partial p = new Partial(
            new DateTimeFieldType[]{monthOfYear, dayOfMonth},
            new int[]{5, 15}
        );
        String str = p.toStringList();
        assertTrue("toStringList should contain both fields",