# Analysis & Test Generation

I will generate comprehensive regression tests for `NumberInput` and `TextBuffer` classes using JUnit 4 (as specified in pom.xml).

**Notes on approach:**
- JUnit 4.8.2 is available; using `@Test` annotations
- Java 1.6 source level compatibility (per pom.xml)
- Tests exercise normal cases, boundaries, exceptions, and edge cases
- All tests are deterministic with no external dependencies
- Tests validate against the documented reference behavior

---

## Generated Test Classes

`com/fasterxml/jackson/core/io/NumberInputTest.java`

```java
package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    // ============================================================================
    // parseInt(char[], int, int) tests
    // ============================================================================

    @Test
    public void testParseInt_SingleDigit() {
        char[] digits = {'5'};
        int result = NumberInput.parseInt(digits, 0, 1);
        assertEquals(5, result);
    }

    @Test
    public void testParseInt_TwoDigits() {
        char[] digits = {'4', '2'};
        int result = NumberInput.parseInt(digits, 0, 2);
        assertEquals(42, result);
    }

    @Test
    public void testParseInt_MaxNineDigits() {
        char[] digits = {'2', '1', '4', '7', '4', '8', '3', '6', '4'};
        int result = NumberInput.parseInt(digits, 0, 9);
        assertEquals(214748364, result);
    }

    @Test
    public void testParseInt_WithOffset() {
        char[] digits = {'x', 'x', '1', '2', '3'};
        int result = NumberInput.parseInt(digits, 2, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_Zero() {
        char[] digits = {'0'};
        int result = NumberInput.parseInt(digits, 0, 1);
        assertEquals(0, result);
    }

    @Test
    public void testParseInt_AllNines() {
        char[] digits = {'9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int result = NumberInput.parseInt(digits, 0, 9);
        assertEquals(999999999, result);
    }

    @Test
    public void testParseInt_ThreeDigits() {
        char[] digits = {'7', '7', '7'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(777, result);
    }

    // ============================================================================
    // parseInt(String) tests
    // ============================================================================

    @Test
    public void testParseIntString_Simple() {
        int result = NumberInput.parseInt("42");
        assertEquals(42, result);
    }

    @Test
    public void testParseIntString_Negative() {
        int result = NumberInput.parseInt("-42");
        assertEquals(-42, result);
    }

    @Test
    public void testParseIntString_Positive() {
        int result = NumberInput.parseInt("+42");
        assertEquals(42, result);
    }

    @Test
    public void testParseIntString_Zero() {
        int result = NumberInput.parseInt("0");
        assertEquals(0, result);
    }

    @Test
    public void testParseIntString_SingleDigit() {
        int result = NumberInput.parseInt("9");
        assertEquals(9, result);
    }

    @Test
    public void testParseIntString_NineDigits() {
        int result = NumberInput.parseInt("123456789");
        assertEquals(123456789, result);
    }

    @Test
    public void testParseIntString_NegativeNineDigits() {
        int result = NumberInput.parseInt("-123456789");
        assertEquals(-123456789, result);
    }

    @Test
    public void testParseIntString_TenDigitsUsesJDK() {
        // 10 digits should fall back to Integer.parseInt
        int result = NumberInput.parseInt("2147483647");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntString_WithDecimalFallsBackToDouble() {
        int result = NumberInput.parseInt("42.5");
        assertEquals(42, result);
    }

    // ============================================================================
    // parseLong(char[], int, int) tests
    // ============================================================================

    @Test
    public void testParseLong_TenDigits() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(digits, 0, 10);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLong_EighteenDigits() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '7'};
        long result = NumberInput.parseLong(digits, 0, 19);
        assertEquals(9223372036854775807L, result);
    }

    @Test
    public void testParseLong_WithOffset() {
        char[] digits = {'x', 'x', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(digits, 2, 10);
        assertEquals(1000000000L, result);
    }

    // ============================================================================
    // parseLong(String) tests
    // ============================================================================

    @Test
    public void testParseLongString_Simple() {
        long result = NumberInput.parseLong("9223372036854775807");
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongString_NineDigits() {
        long result = NumberInput.parseLong("123456789");
        assertEquals(123456789L, result);
    }

    @Test
    public void testParseLongString_TenDigits() {
        long result = NumberInput.parseLong("1234567890");
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLongString_Negative() {
        long result = NumberInput.parseLong("-9223372036854775808");
        assertEquals(Long.MIN_VALUE, result);
    }

    // ============================================================================
    // inLongRange(char[], int, int, boolean) tests
    // ============================================================================

    @Test
    public void testInLongRange_ShorterThanMax_Positive() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        boolean result = NumberInput.inLongRange(digits, 0, 18, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_ExactMax_Positive() {
        String maxStr = String.valueOf(Long.MAX_VALUE);
        char[] digits = maxStr.toCharArray();
        boolean result = NumberInput.inLongRange(digits, 0, maxStr.length(), false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_LongerThanMax_Positive() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8'};
        boolean result = NumberInput.inLongRange(digits, 0, 20, false);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_ShorterThanMinAbs_Negative() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        boolean result = NumberInput.inLongRange(digits, 0, 18, true);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_ExactMin_Negative() {
        String minStr = String.valueOf(Long.MIN_VALUE).substring(1);
        char[] digits = minStr.toCharArray();
        boolean result = NumberInput.inLongRange(digits, 0, minStr.length(), true);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_JustOverMin_Negative() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '9'};
        boolean result = NumberInput.inLongRange(digits, 0, 19, true);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_WithOffset_Positive() {
        String maxStr = String.valueOf(Long.MAX_VALUE);
        char[] buffer = new char[maxStr.length() + 5];
        System.arraycopy(maxStr.toCharArray(), 0, buffer, 5, maxStr.length());
        boolean result = NumberInput.inLongRange(buffer, 5, maxStr.length(), false);
        assertTrue(result);
    }

    // ============================================================================
    // inLongRange(String, boolean) tests
    // ============================================================================

    @Test
    public void testInLongRangeString_Positive_InRange() {
        boolean result = NumberInput.inLongRange("9223372036854775807", false);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeString_Positive_OutOfRange() {
        boolean result = NumberInput.inLongRange("9223372036854775808", false);
        assertFalse(result);
    }

    @Test
    public void testInLongRangeString_Negative_InRange() {
        boolean result = NumberInput.inLongRange("9223372036854775808", true);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeString_Negative_OutOfRange() {
        boolean result = NumberInput.inLongRange("9223372036854775809", true);
        assertFalse(result);
    }

    @Test
    public void testInLongRangeString_ShortNumber_Positive() {
        boolean result = NumberInput.inLongRange("123", false);
        assertTrue(result);
    }

    @Test
    public void testInLongRangeString_ShortNumber_Negative() {
        boolean result = NumberInput.inLongRange("123", true);
        assertTrue(result);
    }

    // ============================================================================
    // parseDouble(String) tests
    // ============================================================================

    @Test
    public void testParseDouble_Simple() {
        double result = NumberInput.parseDouble("42.5");
        assertEquals(42.5, result, 0.0);
    }

    @Test
    public void testParseDouble_NastySmallDouble() {
        double result = NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE);
        assertEquals(Double.MIN_VALUE, result, 0.0);
    }

    @Test
    public void testParseDouble_Zero() {
        double result = NumberInput.parseDouble("0.0");
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testParseDouble_ScientificNotation() {
        double result = NumberInput.parseDouble("1.23e-4");
        assertEquals(0.000123, result, 1e-10);
    }

    @Test
    public void testParseDouble_LargeNumber() {
        double result = NumberInput.parseDouble("1.7976931348623157e308");
        assertEquals(Double.MAX_VALUE, result, 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_Invalid() {
        NumberInput.parseDouble("not_a_number");
    }

    // ============================================================================
    // parseAsDouble(String, double) tests
    // ============================================================================

    @Test
    public void testParseAsDouble_Valid() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.0);
    }

    @Test
    public void testParseAsDouble_Null() {
        double result = NumberInput.parseAsDouble(null, 99.9);
        assertEquals(99.9, result, 0.0);
    }

    @Test
    public void testParseAsDouble_Empty() {
        double result = NumberInput.parseAsDouble("", 88.8);
        assertEquals(88.8, result, 0.0);
    }

    @Test
    public void testParseAsDouble_Whitespace() {
        double result = NumberInput.parseAsDouble("   ", 77.7);
        assertEquals(77.7, result, 0.0);
    }

    @Test
    public void testParseAsDouble_Invalid() {
        double result = NumberInput.parseAsDouble("xyz", 55.5);
        assertEquals(55.5, result, 0.0);
    }

    @Test
    public void testParseAsDouble_TrimmedWhitespace() {
        double result = NumberInput.parseAsDouble("  42.5  ", 0.0);
        assertEquals(42.5, result, 0.0);
    }

    // ============================================================================
    // parseAsInt(String, int) tests
    // ============================================================================

    @Test
    public void testParseAsInt_Valid() {
        int result = NumberInput.parseAsInt("42", 0);
        assertEquals(42, result);
    }

    @Test
    public void testParseAsInt_Null() {
        int result = NumberInput.parseAsInt(null, 99);
        assertEquals(99, result);
    }

    @Test
    public void testParseAsInt_Empty() {
        int result = NumberInput.parseAsInt("", 88);
        assertEquals(88, result);
    }

    @Test
    public void testParseAsInt_Whitespace() {
        int result = NumberInput.parseAsInt("   ", 77);
        assertEquals(77, result);
    }

    @Test
    public void testParseAsInt_WithDecimal() {
        int result = NumberInput.parseAsInt("42.5", 0);
        assertEquals(42, result);
    }

    @Test
    public void testParseAsInt_Negative() {
        int result = NumberInput.parseAsInt("-42", 0);
        assertEquals(-42, result);
    }

    @Test
    public void testParseAsInt_Positive() {
        int result = NumberInput.parseAsInt("+42", 0);
        assertEquals(42, result);
    }

    @Test
    public void testParseAsInt_Invalid() {
        int result = NumberInput.parseAsInt("xyz", 55);
        assertEquals(55, result);
    }

    // ============================================================================
    // parseAsLong(String, long) tests
    // ============================================================================

    @Test
    public void testParseAsLong_Valid() {
        long result = NumberInput.parseAsLong("9223372036854775807", 0L);
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseAsLong_Null() {
        long result = NumberInput.parseAsLong(null, 99L);
        assertEquals(99L, result);
    }

    @Test
    public void testParseAsLong_Empty() {
        long result = NumberInput.parseAsLong("", 88L);
        assertEquals(88L, result);
    }

    @Test
    public void testParseAsLong_Whitespace() {
        long result = NumberInput.parseAsLong("   ", 77L);
        assertEquals(77L, result);
    }

    @Test
    public void testParseAsLong_WithDecimal() {
        long result = NumberInput.parseAsLong("42.5", 0L);
        assertEquals(42L, result);
    }

    @Test
    public void testParseAsLong_Negative() {
        long result = NumberInput.parseAsLong("-42", 0L);
        assertEquals(-42L, result);
    }

    @Test
    public void testParseAsLong_Invalid() {
        long result = NumberInput.parseAsLong("xyz", 55L);
        assertEquals(55L, result);
    }

    // ============================================================================
    // parseBigDecimal(String) tests
    // ============================================================================

    @Test
    public void testParseBigDecimal_String_Simple() {
        BigDecimal result = NumberInput.parseBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), result);
    }

    @Test
    public void testParseBigDecimal_String_Zero() {
        BigDecimal result = NumberInput.parseBigDecimal("0");
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void testParseBigDecimal_String_Negative() {
        BigDecimal result = NumberInput.parseBigDecimal("-999.99");
        assertEquals(new BigDecimal("-999.99"), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_String_Invalid() {
        NumberInput.parseBigDecimal("not_a_number");
    }

    // ============================================================================
    // parseBigDecimal(char[]) tests
    // ============================================================================

    @Test
    public void testParseBigDecimal_CharArray_Simple() {
        char[] buffer = "456.789".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("456.789"), result);
    }

    @Test
    public void testParseBigDecimal_CharArray_Zero() {
        char[] buffer = "0".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_CharArray_Invalid() {
        char[] buffer = "invalid".toCharArray();
        NumberInput.parseBigDecimal(buffer);
    }

    // ============================================================================
    // parseBigDecimal(char[], int, int) tests
    // ============================================================================

    @Test
    public void testParseBigDecimal_CharArrayOffset_Simple() {
        char[] buffer = "xxx123.45yyy".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 3, 6);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimal_CharArrayOffset_AtStart() {
        char[] buffer = "99.99".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 5);
        assertEquals(new BigDecimal("99.99"), result);
    }

    @Test
    public void testParseBigDecimal_CharArrayOffset_SingleChar() {
        char[] buffer = "xxxyx".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 3, 1);
        assertEquals(new BigDecimal("0"), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_CharArrayOffset_Invalid() {
        char[] buffer = "xxxabcyyy".toCharArray();
        NumberInput.parseBigDecimal(buffer, 3, 3);
    }
}
```

---

`com/fasterxml/jackson/core/util/TextBufferTest.java`

```java
package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextBufferTest {

    private TextBuffer textBuffer;
    private BufferRecycler recycler;

    @Before
    public void setUp() {
        recycler = new BufferRecycler();
        textBuffer = new TextBuffer(recycler);
    }

    // ============================================================================
    // Constructor and initialization tests
    // ============================================================================

    @Test
    public void testConstructor_WithRecycler() {
        TextBuffer tb = new TextBuffer(recycler);
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }

    @Test
    public void testConstructor_WithoutRecycler() {
        TextBuffer tb = new TextBuffer(null);
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }

    // ============================================================================
    // resetWithEmpty() tests
    // ============================================================================

    @Test
    public void testResetWithEmpty_InitialState() {
        textBuffer.append('a');
        textBuffer.resetWithEmpty();
        assertEquals(0, textBuffer.size());
        assertEquals("", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithEmpty_ClearsSegments() {
        // Fill buffer to create segments
        for (int i = 0; i < 2000; i++) {
            textBuffer.append('x');
        }
        textBuffer.resetWithEmpty();
        assertEquals(0, textBuffer.size());
    }

    // ============================================================================
    // resetWithShared() tests
    // ============================================================================

    @Test
    public void testResetWithShared_Simple() {
        char[] shared = "hello".toCharArray();
        textBuffer.resetWithShared(shared, 0, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("hello", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithShared_WithOffset() {
        char[] shared = "xxxhelloyyy".toCharArray();
        textBuffer.resetWithShared(shared, 3, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("hello", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithShared_ZeroLength() {
        char[] shared = "hello".toCharArray();
        textBuffer.resetWithShared(shared, 0, 0);
        assertEquals(0, textBuffer.size());
        assertEquals("", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithShared_PreservesSharedBuffer() {
        char[] shared = "test".toCharArray();
        textBuffer.resetWithShared(shared, 0, 4);
        char[] buffer = textBuffer.getTextBuffer();
        assertSame(shared, buffer);
    }

    // ============================================================================
    // resetWithCopy() tests
    // ============================================================================

    @Test
    public void testResetWithCopy_Simple() {
        char[] source = "copy".toCharArray();
        textBuffer.resetWithCopy(source, 0, 4);
        assertEquals(4, textBuffer.size());
        assertEquals("copy", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithCopy_WithOffset() {
        char[] source = "xxxcopyyyy".toCharArray();
        textBuffer.resetWithCopy(source, 3, 4);
        assertEquals(4, textBuffer.size());
        assertEquals("copy", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithCopy_ZeroLength() {
        char[] source = "hello".toCharArray();
        textBuffer.resetWithCopy(source, 0, 0);
        assertEquals(0, textBuffer.size());
        assertEquals("", textBuffer.contentsAsString());
    }

    // ============================================================================
    // resetWithString() tests
    // ============================================================================

    @Test
    public void testResetWithString_Simple() {
        textBuffer.resetWithString("string");
        assertEquals(6, textBuffer.size());
        assertEquals("string", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithString_Empty() {
        textBuffer.resetWithString("");
        assertEquals(0, textBuffer.size());
        assertEquals("", textBuffer.contentsAsString());
    }

    @Test
    public void testResetWithString_ClearsSegments() {
        for (int i = 0; i < 1500; i++) {
            textBuffer.append('a');
        }
        textBuffer.resetWithString("new");
        assertEquals(3, textBuffer.size());
        assertEquals("new", textBuffer.contentsAsString());
    }

    // ============================================================================
    // append(char) tests
    // ============================================================================

    @Test
    public void testAppendChar_Simple() {
        textBuffer.append('a');
        assertEquals(1, textBuffer.size());
        assertEquals("a", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendChar_Multiple() {
        textBuffer.append('a');
        textBuffer.append('b');
        textBuffer.append('c');
        assertEquals(3, textBuffer.size());
        assertEquals("abc", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendChar_ToSharedBuffer() {
        char[] shared = "start".toCharArray();
        textBuffer.resetWithShared(shared, 0, 5);
        textBuffer.append('x');
        assertEquals(6, textBuffer.size());
        assertEquals("startx", textBuffer.contentsAsString());
    }

    // ============================================================================
    // append(char[], int, int) tests
    // ============================================================================

    @Test
    public void testAppendCharArray_Simple() {
        char[] source = "world".toCharArray();
        textBuffer.append(source, 0, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("world", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendCharArray_WithOffset() {
        char[] source = "xxxworldyyy".toCharArray();
        textBuffer.append(source, 3, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("world", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendCharArray_Multiple() {
        textBuffer.append("hello".toCharArray(), 0, 5);
        textBuffer.append(" ".toCharArray(), 0, 1);
        textBuffer.append("world".toCharArray(), 0, 5);
        assertEquals(11, textBuffer.size());
        assertEquals("hello world", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendCharArray_LargeAppend() {
        char[] large = new char[5000];
        for (int i = 0; i < 5000; i++) {
            large[i] = 'x';
        }
        textBuffer.append(large, 0, 5000);
        assertEquals(5000, textBuffer.size());
    }

    @Test
    public void testAppendCharArray_ToSharedBuffer() {
        char[] shared = "prefix".toCharArray();
        textBuffer.resetWithShared(shared, 0, 6);
        textBuffer.append("suffix".toCharArray(), 0, 6);
        assertEquals(12, textBuffer.size());
        assertEquals("prefixsuffix", textBuffer.contentsAsString());
    }

    // ============================================================================
    // append(String, int, int) tests
    // ============================================================================

    @Test
    public void testAppendString_Simple() {
        textBuffer.append("hello", 0, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("hello", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendString_WithOffset() {
        textBuffer.append("hello world", 6, 5);
        assertEquals(5, textBuffer.size());
        assertEquals("world", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendString_Partial() {
        textBuffer.append("0123456789", 2, 4);
        assertEquals(4, textBuffer.size());
        assertEquals("2345", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendString_Multiple() {
        textBuffer.append("hello", 0, 5);
        textBuffer.append(" world", 0, 6);
        assertEquals(11, textBuffer.size());
        assertEquals("hello world", textBuffer.contentsAsString());
    }

    @Test
    public void testAppendString_LargeString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2000; i++) {
            sb.append('x');
        }
        String large = sb.toString();
        textBuffer.append(large, 0, 2000);
        assertEquals(2000, textBuffer.size());
    }

    // ============================================================================
    // size() tests
    // ============================================================================

    @Test
    public void testSize_Empty() {
        assertEquals(0, textBuffer.size());
    }

    @Test
    public void testSize_AfterAppendChar() {
        textBuffer.append('a');
        assertEquals(1, textBuffer.size());
    }

    @Test
    public void testSize_AfterAppendArray() {
        textBuffer.append("test".toCharArray(), 0, 4);
        assertEquals(4, textBuffer.size());
    }

    @Test
    public void testSize_WithSharedBuffer() {
        textBuffer.resetWithShared("shared".toCharArray(), 0, 6);
        assertEquals(6, textBuffer.size());
    }

    @Test
    public void testSize_WithResultString() {
        textBuffer.resetWithString("result");
        assertEquals(6, textBuffer.size());
    }

    // ============================================================================
    // getTextOffset() tests
    // ============================================================================

    @Test
    public void testGetTextOffset_Default() {
        assertEquals(0, textBuffer.getTextOffset());
    }

    @Test
    public void testGetTextOffset_WithSharedBuffer() {
        char[] shared = "xxxhello".toCharArray();
        textBuffer.resetWithShared(shared, 3, 5);
        assertEquals(3, textBuffer.getTextOffset());
    }

    @Test
    public void testGetTextOffset_AfterAppend() {
        textBuffer.append('a');
        assertEquals(0, textBuffer.getTextOffset());
    }

    // ============================================================================
    // hasTextAsCharacters() tests
    // ============================================================================

    @Test
    public void testHasTextAsCharacters_Empty() {
        assertTrue(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testHasTextAsCharacters_WithAppend() {
        textBuffer.append('x');
        assertTrue(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testHasTextAsCharacters_WithSharedBuffer() {
        textBuffer.resetWithShared("test".toCharArray(), 0, 4);
        assertTrue(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testHasTextAsCharacters_WithString() {
        textBuffer.resetWithString("string");
        assertFalse(textBuffer.hasTextAsCharacters());
    }

    // ============================================================================
    // getTextBuffer() tests
    // ============================================================================

    @Test
    public void testGetTextBuffer_Empty() {
        char[] buffer = textBuffer.getTextBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testGetTextBuffer_WithAppend() {