Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
// src/test/java/com/fasterxml/jackson/core/io/NumberInputTest.java
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberInputTest {

    // ========================= parseInt(String) =========================

    @Test
    public void testParseIntStringZero() {
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseIntStringPositive() {
        assertEquals(123, NumberInput.parseInt("123"));
    }

    @Test
    public void testParseIntStringNegative() {
        assertEquals(-456, NumberInput.parseInt("-456"));
    }

    @Test
    public void testParseIntStringMaxInt() {
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
    }

    @Test
    public void testParseIntStringMinInt() {
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testParseIntStringOverflow() {
        // larger than max int should fail (but code defers to JDK, which throws)
        try {
            NumberInput.parseInt("2147483648");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseIntStringInvalidCharacters() {
        try {
            NumberInput.parseInt("12a3");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // ========================= parseInt(char[], int, int) =========================

    @Test
    public void testParseIntCharArrayOneDigit() {
        char[] digits = {'7'};
        assertEquals(7, NumberInput.parseInt(digits, 0, 1));
    }

    @Test
    public void testParseIntCharArrayMultipleDigits() {
        char[] digits = {'1','2','3','4','5','6','7','8','9'};
        assertEquals(123456789, NumberInput.parseInt(digits, 0, 9));
    }

    @Test
    public void testParseIntCharArrayOffsetAndLen() {
        char[] digits = {'x','x','4','2','x'};
        assertEquals(42, NumberInput.parseInt(digits, 2, 2));
    }

    // ========================= parseLong(String) =========================

    @Test
    public void testParseLongStringSmall() {
        assertEquals(123L, NumberInput.parseLong("123"));
    }

    @Test
    public void testParseLongStringLarge() {
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
    }

    @Test
    public void testParseLongStringMinValue() {
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    // ========================= parseLong(char[], int, int) =========================

    @Test
    public void testParseLongCharArray() {
        // len = 18, parseLong splits into 9+9 digits
        char[] digits = "123456789987654321".toCharArray();
        assertEquals(123456789987654321L, NumberInput.parseLong(digits, 0, 18));
    }

    // ========================= inLongRange =========================

    @Test
    public void testInLongRangeStringBelowMax() {
        assertTrue(NumberInput.inLongRange("922337203685477580", false));
    }

    @Test
    public void testInLongRangeStringEqualToMax() {
        assertTrue(NumberInput.inLongRange(NumberInput.MAX_LONG_STR, false));
    }

    @Test
    public void testInLongRangeStringAboveMax() {
        assertFalse(NumberInput.inLongRange("9223372036854775808", false)); // one more than max len doesn't matter here, but length equal and comparison
    }

    @Test
    public void testInLongRangeStringNegativeWithinMin() {
        assertTrue(NumberInput.inLongRange(NumberInput.MIN_LONG_STR_NO_SIGN, true));
    }

    @Test
    public void testInLongRangeCharArrayPositive() {
        assertTrue(NumberInput.inLongRange("9223372036854775807".toCharArray(), 0, 19, false));
    }

    // ========================= parseAsInt =========================

    @Test
    public void testParseAsIntNullReturnsDefault() {
        assertEquals(42, NumberInput.parseAsInt(null, 42));
    }

    @Test
    public void testParseAsIntEmptyReturnsDefault() {
        assertEquals(-1, NumberInput.parseAsInt("", -1));
    }

    @Test
    public void testParseAsIntNormal() {
        assertEquals(15, NumberInput.parseAsInt("15", 0));
    }

    @Test
    public void testParseAsIntNegative() {
        assertEquals(-100, NumberInput.parseAsInt("-100", 0));
    }

    @Test
    public void testParseAsIntWithPlusSign() {
        assertEquals(20, NumberInput.parseAsInt("+20", 0));
    }

    @Test
    public void testParseAsIntDoubleCoercion() {
        assertEquals(3, NumberInput.parseAsInt("3.14", 0));
    }

    // ========================= parseAsLong =========================

    @Test
    public void testParseAsLongNullReturnsDefault() {
        assertEquals(99L, NumberInput.parseAsLong(null, 99L));
    }

    @Test
    public void testParseAsLongEmptyReturnsDefault() {
        assertEquals(100L, NumberInput.parseAsLong("", 100L));
    }

    @Test
    public void testParseAsLongNormal() {
        assertEquals(123456789L, NumberInput.parseAsLong("123456789", 0L));
    }

    @Test
    public void testParseAsLongWithPlus() {
        assertEquals(50L, NumberInput.parseAsLong("+50", 0L));
    }

    // ========================= parseAsDouble =========================

    @Test
    public void testParseAsDoubleNullReturnsDefault() {
        assertEquals(3.14, NumberInput.parseAsDouble(null, 3.14), 0.0);
    }

    @Test
    public void testParseAsDoubleEmptyReturnsDefault() {
        assertEquals(2.71, NumberInput.parseAsDouble("", 2.71), 0.0);
    }

    @Test
    public void testParseAsDoubleNormal() {
        assertEquals(1.5, NumberInput.parseAsDouble("1.5", 0.0), 0.0);
    }

    @Test
    public void testParseAsDoubleInvalidFormat() {
        assertEquals(-1.0, NumberInput.parseAsDouble("abc", -1.0), 0.0);
    }

    // ========================= parseDouble =========================

    @Test
    public void testParseDoubleNastySmallDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleInvalidThrows() {
        NumberInput.parseDouble("not-a-number");
    }

    // ========================= parseBigDecimal =========================

    @Test
    public void testParseBigDecimalString() {
        assertEquals(BigDecimal.valueOf(123, 2), NumberInput.parseBigDecimal("1.23"));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalStringInvalid() {
        NumberInput.parseBigDecimal("xyz");
    }

    @Test
    public void testParseBigDecimalCharArray() {
        char[] buffer = {'4','.','5'};
        assertEquals(new BigDecimal("4.5"), NumberInput.parseBigDecimal(buffer, 0, 3));
    }
}

```

```java
// src/test/java/com/fasterxml/jackson/core/util/TextBufferTest.java
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TextBufferTest {

    private TextBuffer textBuffer;
    private BufferRecycler recycler;

    @Before
    public void setUp() {
        recycler = new BufferRecycler();
        textBuffer = new TextBuffer(recycler);
    }

    @After
    public void tearDown() {
        // release buffers to avoid leaks, but test isolation is maintained
        textBuffer.releaseBuffers();
    }

    // ============ Constructor and initialization ============

    @Test
    public void testDefaultConstructor() {
        // TextBuffer(BufferRecycler) already tested indirectly; we also have no-arg?
        // The spec mentions constructor parameter types "com.fasterxml.jackson.core.util.BufferRecycler", testing that.
        assertNotNull(textBuffer);
    }

    @Test
    public void testSizeAfterConstruction() {
        assertEquals(0, textBuffer.size());
    }

    @Test
    public void testGetTextOffsetInitiallyZero() {
        assertEquals(0, textBuffer.getTextOffset());
    }

    // ============ resetWithEmpty and size ============

    @Test
    public void testResetWithEmptyClearsBuffer() {
        textBuffer.append("data");
        textBuffer.resetWithEmpty();
        assertEquals(0, textBuffer.size());
    }

    // ============ resetWithShared ============

    @Test
    public void testResetWithSharedAndGetTextBuffer() {
        char[] shared = {'a','b','c','d','e','f'};
        textBuffer.resetWithShared(shared, 1, 3); // b,c,d
        assertEquals(3, textBuffer.size());
        assertEquals("bcd", textBuffer.toString());
    }

    @Test
    public void testResetWithSharedGetTextOffset() {
        char[] shared = "HelloWorld".toCharArray();
        textBuffer.resetWithShared(shared, 4, 3); // 'o','W','o'
        assertEquals(4, textBuffer.getTextOffset());
    }

    // ============ resetWithCopy ============

    @Test
    public void testResetWithCopy() {
        char[] source = {'x','y','z','a'};
        textBuffer.resetWithCopy(source, 1, 2); // 'y','z'
        assertEquals(2, textBuffer.size());
        assertEquals("yz", textBuffer.contentsAsString());
    }

    // ============ resetWithString ============

    @Test
    public void testResetWithString() {
        textBuffer.resetWithString("foo");
        assertEquals(3, textBuffer.size());
        assertEquals("foo", textBuffer.toString());
    }

    // ============ append(char) ============

    @Test
    public void testAppendSingleChar() {
        textBuffer.append('Z');
        assertEquals(1, textBuffer.size());
        assertEquals("Z", textBuffer.toString());
    }

    @Test
    public void testAppendCharAfterSharedUnsharesCorrectly() {
        char[] shared = "ABC".toCharArray();
        textBuffer.resetWithShared(shared, 0, 3);
        textBuffer.append('D');
        assertEquals(4, textBuffer.size());
        assertEquals("ABCD", textBuffer.toString());
    }

    // ============ append(char[], int, int) ============

    @Test
    public void testAppendCharArray() {
        textBuffer.append(new char[]{'p','q','r'}, 0, 3);
        assertEquals("pqr", textBuffer.contentsAsString());
    }

    // ============ append(String, int, int) ============

    @Test
    public void testAppendStringSegment() {
        textBuffer.append("universe", 3, 4); // "vers"
        assertEquals("vers", textBuffer.toString());
    }

    // ============ getTextBuffer and hasTextAsCharacters ============

    @Test
    public void testHasTextAsCharactersAfterEmptyReset() {
        textBuffer.resetWithEmpty();
        assertTrue(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testHasTextAsCharactersAfterStringReset() {
        textBuffer.resetWithString("str");
        assertFalse(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testHasTextAsCharactersAfterShared() {
        char[] shared = "data".toCharArray();
        textBuffer.resetWithShared(shared, 0, 4);
        assertTrue(textBuffer.hasTextAsCharacters());
    }

    @Test
    public void testGetTextBufferReturnsInternalArray() {
        textBuffer.resetWithString("data");
        char[] buf = textBuffer.getTextBuffer();
        assertArrayEquals("data".toCharArray(), buf);
    }

    // ============ buildResultArray ============

    @Test
    public void testBuildResultArrayFromEmptyBuffer() {
        assertArrayEquals(new char[0](undefined), textBuffer.buildResultArray());
    }

    @Test
    public void testBuildResultArrayFromShared() {
        char[] shared = {'x','y','z'};
        textBuffer.resetWithShared(shared, 0, 3);
        assertArrayEquals(new char[]{'x','y','z'}, textBuffer.contentsAsArray());
    }

    // ============ expand and segment handling ============

    @Test
    public void testExpandCurrentSegmentIncreasesSize() {
        textBuffer.getCurrentSegment(); // ensure segment exists
        char[] before = textBuffer.getCurrentSegment();
        char[] expanded = textBuffer.expandCurrentSegment();
        assertTrue(expanded.length > before.length);
    }

    @Test
    public void testFinishCurrentSegmentAdvancesSegment() {
        textBuffer.emptyAndGetCurrentSegment();
        int size1 = textBuffer.getCurrentSegmentSize();
        textBuffer.finishCurrentSegment();
        // After finish, current segment is new and size reset to 0
        assertEquals(0, textBuffer.getCurrentSegmentSize());
        assertNotNull(textBuffer.getCurrentSegment());
    }

    @Test
    public void testClearSegmentsResetsState() {
        textBuffer.getCurrentSegment();
        textBuffer.finishCurrentSegment();
        textBuffer.clearSegments();
        assertEquals(0, textBuffer.size());
        assertFalse(textBuffer.hasTextAsCharacters()); // after clearSegments it might still have segments but size 0?
        // Actually after clearSegments, _hasSegments = false
    }

    // ============ unshare implicitly tested via append after shared ============
    // ============ contentsAsArray ============

    @Test
    public void testContentsAsArrayFromAppendedChars() {
        textBuffer.append('H');
        textBuffer.append('i');
        char[] result = textBuffer.contentsAsArray();
        assertArrayEquals(new char[]{'H','i'}, result);
    }

    // ============ contentsAsString ============

    @Test
    public void testContentsAsStringReturnsCachedOrNew() {
        textBuffer.append("hello");
        String s1 = textBuffer.contentsAsString();
        String s2 = textBuffer.contentsAsString();
        assertSame(s1, s2);
        assertEquals("hello", s1);
    }

    // ============ contentsAsDouble / contentsAsDecimal ============

    @Test
    public void testContentsAsDouble() {
        textBuffer.append("2.5");
        assertEquals(2.5, textBuffer.contentsAsDouble(), 0.0);
    }

    @Test
    public void testContentsAsDoubleAfterShared() {
        char[] shared = "1.25".toCharArray();
        textBuffer.resetWithShared(shared, 0, 4);
        assertEquals(1.25, textBuffer.contentsAsDouble(), 1e-9);
    }

    @Test
    public void testContentsAsDecimal() {
        textBuffer.append("3.14159");
        assertEquals(new BigDecimal("3.14159"), textBuffer.contentsAsDecimal());
    }

    @Test
    public void testContentsAsDecimalFromEmptyThrows() {
        // NumberInput parses empty eventually, but BigDecimal constructor might fail
        // This test characterizes handling: empty should parse as BigDecimal? Actually NumberInput.parseBigDecimal("") might fail.
        // We trust the production code: it may throw NumberFormatException
        try {
            textBuffer.contentsAsDecimal();
            fail("Should have thrown NumberFormatException for empty");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // ============ setCurrentLength and getCurrentSegmentSize ============

    @Test
    public void testSetCurrentLengthTruncatesSegment() {
        char[] seg = textBuffer.emptyAndGetCurrentSegment();
        seg[0](undefined) = 'A'; seg[1](undefined) = 'B'; seg[2](undefined) = 'C';
        textBuffer.setCurrentLength(2);
        assertEquals(2, textBuffer.size());
        char[] arr = textBuffer.contentsAsArray();
        assertEquals(2, arr.length);
        assertEquals('A', arr[0](undefined));
        assertEquals('B', arr[1](undefined));
    }

    // ============ releaseBuffers ============

    @Test
    public void testReleaseBuffersClearsAndFrees() {
        textBuffer.append("stuff");
        textBuffer.releaseBuffers();
        assertEquals(0, textBuffer.size());
        textBuffer.append("new");
        assertEquals(3, textBuffer.size()); // should be reusable
    }

    // ============ ensureNotShared ============

    @Test
    public void testEnsureNotSharedAfterSharedReset() {
        char[] shared = "temporary".toCharArray();
        textBuffer.resetWithShared(shared, 0, 9);
        textBuffer.ensureNotShared();
        // should now have local copy
        textBuffer.append('!');
        assertEquals("temporary!", textBuffer.toString());
    }

    // ============ size() coverage for all states ============

    @Test
    public void testSizeAfterStringReset() {
        textBuffer.resetWithString("sizeTest");
        assertEquals(8, textBuffer.size());
    }

    @Test
    public void testSizeAfterResetWithCopy() {
        textBuffer.resetWithCopy(new char[]{'1','2','3'}, 0, 3);
        assertEquals(3, textBuffer.size());
    }
}

```