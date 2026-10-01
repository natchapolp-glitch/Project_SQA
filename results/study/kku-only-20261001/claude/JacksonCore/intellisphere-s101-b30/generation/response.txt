# Analysis and Test Generation

I have analyzed the provided JacksonCore production sources and build configuration. The project uses **JUnit 4.8.2** and targets **Java 1.6+**. I will generate comprehensive regression tests for the two target classes.

## Key Observations

1. **NumberInput**: Utility class for parsing various numeric types with special handling for edge cases (e.g., `NASTY_SMALL_DOUBLE`)
2. **TextBuffer**: Segment-based character buffer with shared/private modes, recycler support
3. Build uses Maven with JUnit 4; no external test dependencies beyond JUnit
4. Source level appears to be Java 1.6 compatible (pom.xml shows `<source>1.6</source>`)

## Test Strategy

- **NumberInput**: Test parsing methods with valid inputs, boundary values, invalid inputs, and special constants
- **TextBuffer**: Test initialization, append operations, segment management, and content retrieval in various states
- Use `BufferRecycler` (available in production code) for TextBuffer tests
- Avoid external dependencies, random values, and timing assumptions
- Keep tests deterministic and independent

---

```java
com/fasterxml/jackson/core/io/NumberInputTest.java
package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for NumberInput utility class.
 * Tests parsing of integers, longs, doubles, and BigDecimals with boundary conditions.
 */
public class NumberInputTest {

    // ============ parseInt(char[], int, int) tests ============

    @Test
    public void testParseIntFromCharArraySingleDigit() {
        char[] chars = {'5'};
        int result = NumberInput.parseInt(chars, 0, 1);
        assertEquals(5, result);
    }

    @Test
    public void testParseIntFromCharArrayNineDigits() {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int result = NumberInput.parseInt(chars, 0, 9);
        assertEquals(123456789, result);
    }

    @Test
    public void testParseIntFromCharArrayWithOffset() {
        char[] chars = {'x', 'x', '4', '2'};
        int result = NumberInput.parseInt(chars, 2, 2);
        assertEquals(42, result);
    }

    @Test
    public void testParseIntFromCharArrayZero() {
        char[] chars = {'0'};
        int result = NumberInput.parseInt(chars, 0, 1);
        assertEquals(0, result);
    }

    @Test
    public void testParseIntFromCharArrayThreeDigits() {
        char[] chars = {'1', '0', '0'};
        int result = NumberInput.parseInt(chars, 0, 3);
        assertEquals(100, result);
    }

    @Test
    public void testParseIntFromCharArrayMaxInt() {
        // 2147483647 = Integer.MAX_VALUE
        char[] chars = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '7'};
        int result = NumberInput.parseInt(chars, 0, 9);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntFromCharArrayOffsetAndLen() {
        char[] chars = {'a', 'b', '9', '9', '9', 'c', 'd'};
        int result = NumberInput.parseInt(chars, 2, 3);
        assertEquals(999, result);
    }

    @Test
    public void testParseIntFromCharArrayTwoDigits() {
        char[] chars = {'1', '2'};
        int result = NumberInput.parseInt(chars, 0, 2);
        assertEquals(12, result);
    }

    @Test
    public void testParseIntFromCharArrayFourDigits() {
        char[] chars = {'5', '6', '7', '8'};
        int result = NumberInput.parseInt(chars, 0, 4);
        assertEquals(5678, result);
    }

    // ============ parseInt(String) tests ============

    @Test
    public void testParseIntFromStringSingleDigit() {
        int result = NumberInput.parseInt("7");
        assertEquals(7, result);
    }

    @Test
    public void testParseIntFromStringPositive() {
        int result = NumberInput.parseInt("12345");
        assertEquals(12345, result);
    }

    @Test
    public void testParseIntFromStringNegative() {
        int result = NumberInput.parseInt("-42");
        assertEquals(-42, result);
    }

    @Test
    public void testParseIntFromStringPlus() {
        int result = NumberInput.parseInt("+100");
        assertEquals(100, result);
    }

    @Test
    public void testParseIntFromStringZero() {
        int result = NumberInput.parseInt("0");
        assertEquals(0, result);
    }

    @Test
    public void testParseIntFromStringMaxInt() {
        int result = NumberInput.parseInt("2147483647");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntFromStringWithNonDigit() {
        // Falls back to Integer.parseInt
        int result = NumberInput.parseInt("123.456");
        // This will throw NumberFormatException from Integer.parseInt,
        // but we test that it delegates correctly
        try {
            NumberInput.parseInt("not_a_number");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    // ============ parseLong(char[], int, int) tests ============

    @Test
    public void testParseLongFromCharArrayTenDigits() {
        char[] chars = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(chars, 0, 10);
        assertEquals(10000000000L, result);
    }

    @Test
    public void testParseLongFromCharArrayEighteenDigits() {
        // 999999999999999999
        char[] chars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        long result = NumberInput.parseLong(chars, 0, 18);
        assertEquals(999999999999999999L, result);
    }

    @Test
    public void testParseLongFromCharArrayWithOffset() {
        char[] chars = {'x', 'x', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(chars, 2, 10);
        assertEquals(1234567890L, result);
    }

    // ============ parseLong(String) tests ============

    @Test
    public void testParseLongFromStringSmall() {
        long result = NumberInput.parseLong("12345");
        assertEquals(12345L, result);
    }

    @Test
    public void testParseLongFromStringLarge() {
        long result = NumberInput.parseLong("9223372036854775807");
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongFromStringNegative() {
        long result = NumberInput.parseLong("-999999999999");
        assertEquals(-999999999999L, result);
    }

    // ============ inLongRange(char[], int, int, boolean) tests ============

    @Test
    public void testInLongRangeMaxPositive() {
        // 9223372036854775807 = Long.MAX_VALUE
        String maxStr = "9223372036854775807";
        char[] chars = maxStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeAboveMaxPositive() {
        // One more than max
        String aboveMax = "9223372036854775808";
        char[] chars = aboveMax.toCharArray();
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeMaxNegative() {
        // 9223372036854775808 (without sign) = -(Long.MIN_VALUE)
        String maxNegStr = "9223372036854775808";
        char[] chars = maxNegStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeBelowMinNegative() {
        String belowMin = "9223372036854775809";
        char[] chars = belowMin.toCharArray();
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeShorterLengthPositive() {
        char[] chars = "100".toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeWithOffset() {
        String str = "xxx9223372036854775807yyy";
        char[] chars = str.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 3, 19, false));
    }

    // ============ inLongRange(String, boolean) tests ============

    @Test
    public void testInLongRangeStringMaxPositive() {
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
    }

    @Test
    public void testInLongRangeStringAboveMaxPositive() {
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
    }

    @Test
    public void testInLongRangeStringMaxNegative() {
        assertTrue(NumberInput.inLongRange("9223372036854775808", true));
    }

    @Test
    public void testInLongRangeStringSmallValue() {
        assertTrue(NumberInput.inLongRange("42", false));
    }

    @Test
    public void testInLongRangeStringSmallNegative() {
        assertTrue(NumberInput.inLongRange("100", true));
    }

    @Test
    public void testInLongRangeStringBelowMinNegative() {
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));
    }

    // ============ parseDouble(String) tests ============

    @Test
    public void testParseDoubleNormalValue() {
        double result = NumberInput.parseDouble("3.14");
        assertEquals(3.14, result, 0.0001);
    }

    @Test
    public void testParseDoubleZero() {
        double result = NumberInput.parseDouble("0.0");
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testParseDoubleNegative() {
        double result = NumberInput.parseDouble("-2.5");
        assertEquals(-2.5, result, 0.0001);
    }

    @Test
    public void testParseDoubleExponent() {
        double result = NumberInput.parseDouble("1.5e10");
        assertEquals(1.5e10, result, 1e6);
    }

    @Test
    public void testParseDoubleNastySmall() {
        // Special case: NASTY_SMALL_DOUBLE returns Double.MIN_VALUE
        double result = NumberInput.parseDouble("2.2250738585072012e-308");
        assertEquals(Double.MIN_VALUE, result, 0.0);
    }

    @Test
    public void testParseDoubleInfinity() {
        double result = NumberInput.parseDouble("Infinity");
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testParseDoubleNegativeInfinity() {
        double result = NumberInput.parseDouble("-Infinity");
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testParseDoubleNaN() {
        double result = NumberInput.parseDouble("NaN");
        assertTrue(Double.isNaN(result));
    }

    // ============ parseAsDouble(String, double) tests ============

    @Test
    public void testParseAsDoubleValidValue() {
        double result = NumberInput.parseAsDouble("2.71", 0.0);
        assertEquals(2.71, result, 0.01);
    }

    @Test
    public void testParseAsDoubleNull() {
        double result = NumberInput.parseAsDouble(null, 99.0);
        assertEquals(99.0, result, 0.0);
    }

    @Test
    public void testParseAsDoubleEmpty() {
        double result = NumberInput.parseAsDouble("", 77.0);
        assertEquals(77.0, result, 0.0);
    }

    @Test
    public void testParseAsDoubleWhitespace() {
        double result = NumberInput.parseAsDouble("   ", 55.0);
        assertEquals(55.0, result, 0.0);
    }

    @Test
    public void testParseAsDoubleInvalid() {
        double result = NumberInput.parseAsDouble("not_a_number", 123.0);
        assertEquals(123.0, result, 0.0);
    }

    @Test
    public void testParseAsDoubleWithLeadingWhitespace() {
        double result = NumberInput.parseAsDouble("  3.14  ", 0.0);
        assertEquals(3.14, result, 0.01);
    }

    // ============ parseAsInt(String, int) tests ============

    @Test
    public void testParseAsIntValidValue() {
        int result = NumberInput.parseAsInt("42", 0);
        assertEquals(42, result);
    }

    @Test
    public void testParseAsIntNull() {
        int result = NumberInput.parseAsInt(null, 99);
        assertEquals(99, result);
    }

    @Test
    public void testParseAsIntEmpty() {
        int result = NumberInput.parseAsInt("", 88);
        assertEquals(88, result);
    }

    @Test
    public void testParseAsIntWhitespace() {
        int result = NumberInput.parseAsInt("   ", 77);
        assertEquals(77, result);
    }

    @Test
    public void testParseAsIntWithPlus() {
        int result = NumberInput.parseAsInt("+123", 0);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsIntWithMinus() {
        int result = NumberInput.parseAsInt("-456", 0);
        assertEquals(-456, result);
    }

    @Test
    public void testParseAsIntWithDecimal() {
        int result = NumberInput.parseAsInt("123.45", 0);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsIntInvalid() {
        int result = NumberInput.parseAsInt("xyz", 55);
        assertEquals(55, result);
    }

    @Test
    public void testParseAsIntWithWhitespace() {
        int result = NumberInput.parseAsInt("  789  ", 0);
        assertEquals(789, result);
    }

    // ============ parseAsLong(String, long) tests ============

    @Test
    public void testParseAsLongValidValue() {
        long result = NumberInput.parseAsLong("9999999999", 0L);
        assertEquals(9999999999L, result);
    }

    @Test
    public void testParseAsLongNull() {
        long result = NumberInput.parseAsLong(null, 111L);
        assertEquals(111L, result);
    }

    @Test
    public void testParseAsLongEmpty() {
        long result = NumberInput.parseAsLong("", 222L);
        assertEquals(222L, result);
    }

    @Test
    public void testParseAsLongWithPlus() {
        long result = NumberInput.parseAsLong("+1000000000", 0L);
        assertEquals(1000000000L, result);
    }

    @Test
    public void testParseAsLongWithMinus() {
        long result = NumberInput.parseAsLong("-5000000000", 0L);
        assertEquals(-5000000000L, result);
    }

    @Test
    public void testParseAsLongWithDecimal() {
        long result = NumberInput.parseAsLong("123.456", 0L);
        assertEquals(123L, result);
    }

    @Test
    public void testParseAsLongInvalid() {
        long result = NumberInput.parseAsLong("abc", 333L);
        assertEquals(333L, result);
    }

    // ============ parseBigDecimal(String) tests ============

    @Test
    public void testParseBigDecimalSimple() {
        BigDecimal result = NumberInput.parseBigDecimal("123.45");
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimalZero() {
        BigDecimal result = NumberInput.parseBigDecimal("0");
        assertEquals(new BigDecimal("0"), result);
    }

    @Test
    public void testParseBigDecimalNegative() {
        BigDecimal result = NumberInput.parseBigDecimal("-999.99");
        assertEquals(new BigDecimal("-999.99"), result);
    }

    @Test
    public void testParseBigDecimalLarge() {
        BigDecimal result = NumberInput.parseBigDecimal("99999999999999999999.123");
        assertEquals(new BigDecimal("99999999999999999999.123"), result);
    }

    @Test
    public void testParseBigDecimalInvalid() {
        try {
            NumberInput.parseBigDecimal("not_a_number");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            assertTrue(e.getMessage().contains("can not be represented as BigDecimal"));
        }
    }

    // ============ parseBigDecimal(char[]) tests ============

    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] chars = "456.78".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars);
        assertEquals(new BigDecimal("456.78"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayNegative() {
        char[] chars = "-111.22".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars);
        assertEquals(new BigDecimal("-111.22"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayZero() {
        char[] chars = "0".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars);
        assertEquals(new BigDecimal("0"), result);
    }

    // ============ parseBigDecimal(char[], int, int) tests ============

    @Test
    public void testParseBigDecimalFromCharArrayWithOffsetAndLen() {
        char[] chars = "xxx123.45yyy".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars, 3, 6);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayOffsetZero() {
        char[] chars = "789.01".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars, 0, 6);
        assertEquals(new BigDecimal("789.01"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayNegativeWithOffset() {
        char[] chars = "prefix-99.99".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(chars, 6, 6);
        assertEquals(new BigDecimal("-99.99"), result);
    }

    @Test
    public void testParseBigDecimalFromCharArrayInvalidWithOffset() {
        char[] chars = "xxxinvalidxxx".toCharArray();
        try {
            NumberInput.parseBigDecimal(chars, 3, 7);
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            assertTrue(e.getMessage().contains("can not be represented as BigDecimal"));
        }
    }
}
```

---

```java
com/fasterxml/jackson/core/util/TextBufferTest.java
package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for TextBuffer class.
 * Tests segment management, append operations, and content retrieval.
 */
public class TextBufferTest {

    private TextBuffer buffer;
    private BufferRecycler recycler;

    @Before
    public void setUp() {
        recycler = new BufferRecycler();
        buffer = new TextBuffer(recycler);
    }

    // ============ Initialization and Reset tests ============

    @Test
    public void testConstructorWithRecycler() {
        assertNotNull(buffer);
        assertEquals(0, buffer.size());
    }

    @Test
    public void testResetWithEmpty() {
        buffer.append('a');
        buffer.resetWithEmpty();
        assertEquals(0, buffer.size());
        assertEquals("", buffer.contentsAsString());
    }

    @Test
    public void testResetWithString() {
        buffer.resetWithString("hello");
        assertEquals(5, buffer.size());
        assertEquals("hello", buffer.contentsAsString());
    }

    @Test
    public void testResetWithShared() {
        char[] shared = {'h', 'e', 'l', 'l', 'o'};
        buffer.resetWithShared(shared, 0, 5);
        assertEquals(5, buffer.size());
        assertEquals("hello", buffer.contentsAsString());
    }

    @Test
    public void testResetWithSharedOffset() {
        char[] shared = {'x', 'x', 'w', 'o', 'r', 'l', 'd', 'x', 'x'};
        buffer.resetWithShared(shared, 2, 5);
        assertEquals(5, buffer.size());
        assertEquals("world", buffer.contentsAsString());
    }

    @Test
    public void testResetWithCopy() {
        char[] source = {'t', 'e', 's', 't'};
        buffer.resetWithCopy(source, 0, 4);
        assertEquals(4, buffer.size());
        assertEquals("test", buffer.contentsAsString());
    }

    @Test
    public void testResetWithCopyOffset() {
        char[] source = {'a', 'b', 'c', 'd', 'e'};
        buffer.resetWithCopy(source, 1, 3);
        assertEquals(3, buffer.size());
        assertEquals("bcd", buffer.contentsAsString());
    }

    // ============ Append tests ============

    @Test
    public void testAppendSingleChar() {
        buffer.append('x');
        assertEquals(1, buffer.size());
        assertEquals("x", buffer.contentsAsString());
    }

    @Test
    public void testAppendMultipleChars() {
        buffer.append('a');
        buffer.append('b');
        buffer.append('c');
        assertEquals(3, buffer.size());
        assertEquals("abc", buffer.contentsAsString());
    }

    @Test
    public void testAppendCharArray() {
        char[] chars = {'h', 'e', 'l', 'l', 'o'};
        buffer.append(chars, 0, 5);
        assertEquals(5, buffer.size());
        assertEquals("hello", buffer.contentsAsString());
    }

    @Test
    public void testAppendCharArrayOffset() {
        char[] chars = {'x', 'x', 't', 'e', 's', 't', 'x', 'x'};
        buffer.append(chars, 2, 4);
        assertEquals(4, buffer.size());
        assertEquals("test", buffer.contentsAsString());
    }

    @Test
    public void testAppendCharArrayLarge() {
        char[] chars = new char[2000];
        for (int i = 0; i < 2000; i++) {
            chars[i] = (char) ('a' + (i % 26));
        }
        buffer.append(chars, 0, 2000);
        assertEquals(2000, buffer.size());
    }

    @Test
    public void testAppendString() {
        buffer.append("hello", 0, 5);
        assertEquals(5, buffer.size());
        assertEquals("hello", buffer.contentsAsString());
    }

    @Test
    public void testAppendStringOffset() {
        buffer.append("testing", 1, 3);
        assertEquals(3, buffer.size());
        assertEquals("est", buffer.contentsAsString());
    }

    @Test
    public void testAppendStringLarge() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("x");
        }
        String large = sb.toString();
        buffer.append(large, 0, large.length());
        assertEquals(1000, buffer.size());
    }

    @Test
    public void testAppendMixed() {
        buffer.append('a');
        buffer.append(new char[]{'b', 'c'}, 0, 2);
        buffer.append("de", 0, 2);
        assertEquals(5, buffer.size());
        assertEquals("abcde", buffer.contentsAsString());
    }

    // ============ Segment management tests ============

    @Test
    public void testCurrentSegment() {
        char[] segment = buffer.getCurrentSegment();
        assertNotNull(segment);
        assertTrue(segment.length > 0);
    }

    @Test
    public void testFinishCurrentSegment() {
        buffer.append("test", 0, 4);
        char[] newSegment = buffer.finishCurrentSegment();
        assertNotNull(newSegment);
        // After finishing, should have a new segment
        assertEquals(4, buffer.size());
    }

    @Test
    public void testExpandCurrentSegment() {
        char[] segment = buffer.getCurrentSegment();
        int originalLen = segment.length;
        char[] expanded = buffer.expandCurrentSegment();
        assertTrue(expanded.length >= originalLen);
    }

    @Test
    public void testEmptyAndGetCurrentSegment() {
        buffer.append("data", 0, 4);
        char[] segment = buffer.emptyAndGetCurrentSegment();
        assertNotNull(segment);
        assertEquals(0, buffer.size());
    }

    @Test
    public void testClearSegments() {
        buffer.append("first", 0, 5);
        buffer.finishCurrentSegment();
        buffer.append("second", 0, 6);
        buffer.clearSegments();
        // After clearing, size should reflect only current segment
        assertEquals(6, buffer.size());
    }

    // ============ Content access tests ============

    @Test
    public void testContentsAsString() {
        buffer.append("hello world", 0, 11);
        String result = buffer.contentsAsString();
        assertEquals("hello world", result);
    }

    @Test
    public void testContentsAsStringEmpty() {
        String result = buffer.contentsAsString();
        assertEquals("", result);
    }

    @Test
    public void testContentsAsStringMultiSegment() {
        buffer.append("part1", 0, 5);
        buffer.finishCurrentSegment();
        buffer.append("part2", 0, 5);
        String result = buffer.contentsAsString();
        assertEquals("part1part2", result);
    }

    @Test
    public void testContentsAsArray() {
        buffer.append("test", 0, 4);
        char[] result = buffer.contentsAsArray();
        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("test", new String(result));
    }

    @Test
    public void testContentsAsArrayEmpty() {
        char[] result = buffer.contentsAsArray();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testContentsAsArrayMultiSegment() {
        buffer.append("ab", 0, 2);
        buffer.finishCurrentSegment();
        buffer.append("cd", 0, 2);
        char[] result = buffer.contentsAsArray();
        assertEquals("abcd", new String(result));
    }

    @Test
    public void testGetTextBuffer() {
        buffer.append("data", 0, 4);
        char[] textBuf = buffer.getTextBuffer();
        assertNotNull(textBuf);
    }

    @Test
    public void testGetTextBufferFromShared() {
        char[] shared = {'s', 'h', 'a', 'r', 'e', 'd'};
        buffer.resetWithShared(shared, 0, 6);
        char[] textBuf = buffer.getTextBuffer();
        assertSame(shared, textBuf);
    }

    @Test
    public void testContentsAsDecimal() {
        buffer.append("123.45", 0, 6);
        BigDecimal result = buffer.contentsAsDecimal();
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testContentsAsDouble() {
        buffer.append("3.14", 0, 4);
        double result = buffer.contentsAsDouble();
        assertEquals(3.14, result, 0.01);
    }

    @Test
    public void testToString() {
        buffer.append("toString", 0, 8);
        String result = buffer.toString();
        assertEquals("toString", result);
    }

    // ============ State query tests ============

    @Test
    public void testSize() {
        assertEquals(0, buffer.size());
        buffer.append('a');
        assertEquals(1, buffer.size());
        buffer.append("bc", 0, 2);
        assertEquals(3, buffer.size());
    }

    @Test
    public void testSizeMultiSegment() {
        buffer.append("test", 0, 4);
        buffer.finishCurrentSegment();
        buffer.append("more", 0, 4);
        assertEquals(8, buffer.size());
    }

    @Test
    public void testGetTextOffset() {
        buffer.append("offset", 0, 6);
        assertEquals(0, buffer.getTextOffset());
    }

    @Test
    public void testGetTextOffsetShared() {
        char[] shared = {'a', 'b', 'c', 'd'};
        buffer.resetWithShared(shared, 2, 2);
        assertEquals(2, buffer.getTextOffset());
    }

    @Test
    public void testGetCurrentSegmentSize() {
        buffer.append("test", 0, 4);
        assertEquals(4, buffer.getCurrentSegmentSize());
    }

    @Test
    public void testSetCurrentLength() {
        buffer.append("abcde", 0, 5);
        buffer.setCurrentLength(3);
        assertEquals(3, buffer.getCurrentSegmentSize());
        assertEquals("abc", buffer.contentsAsString());
    }

    @Test