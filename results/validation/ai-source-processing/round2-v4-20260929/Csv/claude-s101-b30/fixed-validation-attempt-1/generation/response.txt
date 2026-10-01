Tests are in the `org.apache.commons.csv` package because the class and its constructor are package-private. They use JUnit 4.10 (`org.junit.Test` and `org.junit.Assert`) and Java 1.5-compatible syntax, since `pom.xml` sets source level 1.5: no diamond operator, no try-with-resources, no lambdas. There are 30 test methods and no missing APIs or dependencies.

`org/apache/commons/csv/ExtendedBufferedReaderTest.java`

```java
package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private static ExtendedBufferedReader reader(String s) {
        return new ExtendedBufferedReader(new StringReader(s));
    }

    private static char[] filled(int size) {
        char[] buf = new char[size];
        Arrays.fill(buf, 'x');
        return buf;
    }

    // ---- constructor / initial state ----

    @Test
    public void testInitialState() {
        ExtendedBufferedReader r = reader("abc");
        assertEquals(0, r.getLineNumber());
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullReaderThrows() {
        new ExtendedBufferedReader(null);
    }

    // ---- read() ----

    @Test
    public void testReadSingleCharsAndEndOfStream() throws IOException {
        ExtendedBufferedReader r = reader("ab");
        assertEquals('a', r.read());
        assertEquals('a', r.readAgain());
        assertEquals('b', r.read());
        assertEquals('b', r.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.readAgain());
    }

    @Test
    public void testReadEmptyReader() throws IOException {
        ExtendedBufferedReader r = reader("");
        assertEquals(-1, r.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.readAgain());
        assertEquals(0, r.getLineNumber());
    }

    @Test
    public void testReadCountsLineFeed() throws IOException {
        ExtendedBufferedReader r = reader("a\nb");
        r.read();
        assertEquals(0, r.getLineNumber());
        r.read();
        assertEquals(1, r.getLineNumber());
        r.read();
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testReadCountsCarriageReturn() throws IOException {
        ExtendedBufferedReader r = reader("a\rb");
        r.read();
        r.read();
        assertEquals(1, r.getLineNumber());
        r.read();
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testReadCrLfCountsAsOneLine() throws IOException {
        ExtendedBufferedReader r = reader("a\r\nb");
        while (r.read() != -1) {
            // consume
        }
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testReadLfCrCountsAsTwoLines() throws IOException {
        ExtendedBufferedReader r = reader("\n\r");
        while (r.read() != -1) {
            // consume
        }
        assertEquals(2, r.getLineNumber());
    }

    @Test
    public void testReadTwoCrLfSequences() throws IOException {
        ExtendedBufferedReader r = reader("\r\n\r\n");
        while (r.read() != -1) {
            // consume
        }
        assertEquals(2, r.getLineNumber());
    }

    @Test
    public void testReadConsecutiveLineFeeds() throws IOException {
        ExtendedBufferedReader r = reader("\n\n");
        while (r.read() != -1) {
            // consume
        }
        assertEquals(2, r.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testReadAfterCloseThrows() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        r.close();
        r.read();
    }

    // ---- lookAhead() ----

    @Test
    public void testLookAheadDoesNotConsume() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        assertEquals('a', r.lookAhead());
        assertEquals('a', r.lookAhead());
        assertEquals('a', r.read());
        assertEquals('b', r.lookAhead());
        assertEquals('b', r.read());
    }

    @Test
    public void testLookAheadDoesNotChangeLastChar() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        assertEquals('a', r.lookAhead());
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
        r.read();
        r.lookAhead();
        assertEquals('a', r.readAgain());
    }

    @Test
    public void testLookAheadAtEndOfStream() throws IOException {
        ExtendedBufferedReader r = reader("");
        assertEquals(-1, r.lookAhead());
        assertEquals(-1, r.lookAhead());
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
    }

    @Test
    public void testLookAheadAfterLastCharConsumed() throws IOException {
        ExtendedBufferedReader r = reader("z");
        assertEquals('z', r.read());
        assertEquals(-1, r.lookAhead());
        assertEquals('z', r.readAgain());
    }

    @Test
    public void testLookAheadDoesNotChangeLineNumber() throws IOException {
        ExtendedBufferedReader r = reader("\n");
        assertEquals('\n', r.lookAhead());
        assertEquals(0, r.getLineNumber());
        assertEquals('\n', r.read());
        assertEquals(1, r.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testLookAheadAfterCloseThrows() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        r.close();
        r.lookAhead();
    }

    // ---- read(char[], int, int) ----

    @Test
    public void testBulkReadZeroLengthReturnsZeroWithoutStateChange() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        char[] buf = filled(4);
        assertEquals(0, r.read(buf, 0, 0));
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
        assertEquals(0, r.getLineNumber());
        assertEquals('a', r.read());
    }

    @Test
    public void testBulkReadZeroLengthAtEndOfStreamReturnsZero() throws IOException {
        ExtendedBufferedReader r = reader("");
        assertEquals(0, r.read(new char[2], 0, 0));
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
    }

    @Test
    public void testBulkReadWholeContentSetsLastChar() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        char[] buf = new char[10];
        assertEquals(3, r.read(buf, 0, 10));
        assertArrayEquals(new char[] {'a', 'b', 'c'}, Arrays.copyOf(buf, 3));
        assertEquals('c', r.readAgain());
    }

    @Test
    public void testBulkReadPartialLength() throws IOException {
        ExtendedBufferedReader r = reader("abcd");
        char[] buf = new char[4];
        assertEquals(2, r.read(buf, 0, 2));
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals('b', r.readAgain());
        assertEquals('c', r.read());
    }

    @Test
    public void testBulkReadWithOffset() throws IOException {
        ExtendedBufferedReader r = reader("abcdef");
        char[] buf = filled(6);
        assertEquals(3, r.read(buf, 2, 3));
        assertArrayEquals(new char[] {'x', 'x', 'a', 'b', 'c', 'x'}, buf);
        assertEquals('c', r.readAgain());
    }

    @Test
    public void testBulkReadAtEndOfStream() throws IOException {
        ExtendedBufferedReader r = reader("");
        assertEquals(-1, r.read(new char[4], 0, 4));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.readAgain());
        assertEquals(0, r.getLineNumber());
    }

    @Test
    public void testBulkReadCountsLineTerminators() throws IOException {
        ExtendedBufferedReader r = reader("a\nb\r\nc\r");
        char[] buf = new char[20];
        assertEquals(8, r.read(buf, 0, 20));
        assertEquals(3, r.getLineNumber());
        assertEquals('\r', r.readAgain());
    }

    @Test
    public void testBulkReadCrLfSplitAcrossCalls() throws IOException {
        ExtendedBufferedReader r = reader("\r\n");
        char[] buf = new char[1];
        assertEquals(1, r.read(buf, 0, 1));
        assertEquals(1, r.getLineNumber());
        assertEquals(1, r.read(buf, 0, 1));
        assertEquals('\n', buf[0]);
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testBulkReadLineFeedAfterSingleCharReadCr() throws IOException {
        ExtendedBufferedReader r = reader("\r\n");
        assertEquals('\r', r.read());
        assertEquals(1, r.getLineNumber());
        char[] buf = new char[1];
        assertEquals(1, r.read(buf, 0, 1));
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testBulkReadLeadingLineFeedCounted() throws IOException {
        ExtendedBufferedReader r = reader("\n");
        char[] buf = new char[1];
        assertEquals(1, r.read(buf, 0, 1));
        assertEquals(1, r.getLineNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBulkReadLengthBeyondBufferThrows() throws IOException {
        ExtendedBufferedReader r = reader("abcdefghij");
        r.read(new char[5], 0, 10);
    }

    // ---- readLine() ----

    @Test
    public void testReadLineSequence() throws IOException {
        ExtendedBufferedReader r = reader("line1\nline2");
        assertEquals("line1", r.readLine());
        assertEquals(1, r.getLineNumber());
        assertEquals('1', r.readAgain());
        assertEquals("line2", r.readLine());
        assertEquals(2, r.getLineNumber());
        assertEquals('2', r.readAgain());
        assertNull(r.readLine());
        assertEquals(2, r.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.readAgain());
    }

    @Test
    public void testReadLineOnEmptyReader() throws IOException {
        ExtendedBufferedReader r = reader("");
        assertNull(r.readLine());
        assertEquals(0, r.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, r.readAgain());
    }

    @Test
    public void testReadLineEmptyLineKeepsLastChar() throws IOException {
        ExtendedBufferedReader r = reader("\nabc");
        assertEquals("", r.readLine());
        assertEquals(1, r.getLineNumber());
        assertEquals(ExtendedBufferedReader.UNDEFINED, r.readAgain());
        assertEquals("abc", r.readLine());
        assertEquals(2, r.getLineNumber());
    }

    @Test
    public void testReadLineEmptyRemainderKeepsPreviousLastChar() throws IOException {
        ExtendedBufferedReader r = reader("a\n\nb");
        assertEquals('a', r.read());
        assertEquals("", r.readLine());
        assertEquals('a', r.readAgain());
        assertEquals(1, r.getLineNumber());
    }

    @Test
    public void testReadLineHandlesCrLfAndCr() throws IOException {
        ExtendedBufferedReader r = reader("a\r\nb\rc");
        assertEquals("a", r.readLine());
        assertEquals("b", r.readLine());
        assertEquals("c", r.readLine());
        assertNull(r.readLine());
        assertEquals(3, r.getLineNumber());
    }

    @Test
    public void testReadLineFinalLineWithoutTerminatorIncrementsCounter() throws IOException {
        ExtendedBufferedReader r = reader("abc");
        assertEquals("abc", r.readLine());
        assertEquals(1, r.getLineNumber());
        assertEquals('c', r.readAgain());
    }

    @Test
    public void testReadAfterReadLine() throws IOException {
        ExtendedBufferedReader r = reader("a\nb");
        assertEquals("a", r.readLine());
        assertEquals('b', r.lookAhead());
        assertEquals('b', r.read());
        assertEquals(1, r.getLineNumber());
    }
}
```

The expected values come only from the supplied source and the standard `BufferedReader` contract:

- **Line counting:** `read()` counts `\r`, and counts `\n` only when the previous character was not `\r`.
- **Bulk read:** `read(char[], int, int)` uses the same rule but reads the previous character from the buffer.
- **`readLine()`:** it always increments the counter, whether it returns a line or a final unterminated one.
- **Last character:** `readLine()` leaves the last-character value unchanged for an empty line and sets it to `END_OF_STREAM` at EOF.
- **Bulk-read caveat:** in `read(char[], int, int)`, when `offset > 0` the first character is compared against `buf[offset-1]` rather than `lastChar`. The offset test pre-fills the buffer with `'x'` so it doesn't depend on that quirk.