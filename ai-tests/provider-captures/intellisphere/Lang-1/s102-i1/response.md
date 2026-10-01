Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
// org/apache/commons/lang3/math/NumberUtilsTest.java
package org.apache.commons.lang3.math;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtilsTest {

    // --- isAllZeros tests ---
    // Note: isAllZeros is private. We will test it indirectly through createNumber.

    @Test
    public void testCreateNumber_nullString() {
        assertNull(NumberUtils.createNumber(null));
    }

    @Test
    public void testCreateNumber_blankString() {
        try {
            NumberUtils.createNumber("   ");
            fail("Expected NumberFormatException for blank string");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testCreateNumber_negativeZeroHex() {
        Number n = NumberUtils.createNumber("-0x0");
        assertTrue(n instanceof Integer);
        assertEquals(Integer.valueOf(0), n);
    }

    @Test
    public void testCreateNumber_hashHex() {
        Number n = NumberUtils.createNumber("#F");
        assertTrue(n instanceof Integer);
        assertEquals(Integer.valueOf(15), n);
    }

    @Test
    public void testCreateNumber_negativeHashHex() {
        Number n = NumberUtils.createNumber("-#10");
        assertTrue(n instanceof Integer);
        assertEquals(Integer.valueOf(-16), n);
    }

    @Test
    public void testCreateNumber_hexWithLeadingZeros() {
        Number n = NumberUtils.createNumber("0x00000001");
        assertTrue(n instanceof Integer);
        assertEquals(Integer.valueOf(1), n);
    }

    @Test
    public void testCreateNumber_hexLongMax() {
        Number n = NumberUtils.createNumber("0x7FFFFFFFF");
        assertTrue(n instanceof Long);
        assertEquals(Long.valueOf(0x7FFFFFFFFL), n);
    }

    @Test
    public void testCreateNumber_hexBigInteger() {
        Number n = NumberUtils.createNumber("0x7FFFFFFFFFFFFFFF0");
        assertTrue(n instanceof BigInteger);
        assertEquals(new BigInteger("7FFFFFFFFFFFFFFF0", 16), n);
    }

    @Test
    public void testCreateNumber_longSuffix() {
        Number n = NumberUtils.createNumber("123L");
        assertEquals(Long.valueOf(123), n);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_longSuffixWithDecimal() {
        NumberUtils.createNumber("123.0L");
    }

    @Test
    public void testCreateNumber_floatSuffix() {
        Number n = NumberUtils.createNumber("1.5f");
        assertTrue(n instanceof Float);
        assertEquals(1.5f, n.floatValue(), 0.0f);
    }

    @Test
    public void testCreateNumber_doubleSuffix() {
        Number n = NumberUtils.createNumber("1.5d");
        assertTrue(n instanceof Double);
        assertEquals(1.5d, n.doubleValue(), 0.0d);
    }

    @Test
    public void testCreateNumber_scientificNotationFloat() {
        Number n = NumberUtils.createNumber("1.23E2");
        assertTrue(n instanceof Float);
        assertEquals(123.0f, n.floatValue(), 0.0f);
    }

    @Test
    public void testCreateNumber_scientificNotationDouble() {
        Number n = NumberUtils.createNumber("1.2345678910E2");
        assertTrue(n instanceof Double);
        assertEquals(123.45678910d, n.doubleValue(), 0.0000000001d);
    }

    @Test
    public void testCreateNumber_bigDecimal() {
        Number n = NumberUtils.createNumber("1.2345678912345678910");
        assertTrue(n instanceof BigDecimal);
        assertEquals(new BigDecimal("1.2345678912345678910"), n);
    }

    @Test
    public void testCreateNumber_integerOctal() {
        Number n = NumberUtils.createNumber("010");
        assertTrue(n instanceof Integer);
        assertEquals(Integer.valueOf(8), n);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_invalidFormat() {
        NumberUtils.createNumber("abc");
    }

    // --- validateArray tests ---
    // Note: validateArray is private. Tested through public max/min methods.

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArray_null() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArray_empty() {
        NumberUtils.min(new long[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArray_null() {
        NumberUtils.max((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArray_empty() {
        NumberUtils.min(new short[]{});
    }

    // --- isDigits tests ---
    @Test
    public void testIsDigits_null() {
        assertFalse(NumberUtils.isDigits(null));
    }

    @Test
    public void testIsDigits_emptyString() {
        assertFalse(NumberUtils.isDigits(""));
    }

    @Test
    public void testIsDigits_validDigits() {
        assertTrue(NumberUtils.isDigits("12345"));
    }

    @Test
    public void testIsDigits_withLetters() {
        assertFalse(NumberUtils.isDigits("1234a5"));
    }

    @Test
    public void testIsDigits_withSpecialChars() {
        assertFalse(NumberUtils.isDigits("123-45"));
    }

    // --- isNumber tests ---
    @Test
    public void testIsNumber_null() {
        assertFalse(NumberUtils.isNumber(null));
    }

    @Test
    public void testIsNumber_emptyString() {
        assertFalse(NumberUtils.isNumber(""));
    }

    @Test
    public void testIsNumber_hexadecimal() {
        assertTrue(NumberUtils.isNumber("0xABCDEF"));
    }
}

```