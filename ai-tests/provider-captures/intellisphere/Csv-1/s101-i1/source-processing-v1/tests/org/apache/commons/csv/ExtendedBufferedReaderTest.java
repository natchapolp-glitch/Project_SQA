// org/apache/commons/csv/ExtendedBufferedReaderTest.java
package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Regression tests for {@link ExtendedBufferedReader}.
 *
 * Note: ExtendedBufferedReader is package-private, so this test class
 * lives in the same package (org.apache.commons.csv) to access it.
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testReadAgainBeforeAnyRead() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("ab"));
        // Before any read has occurred, readAgain() returns UNDEFINED.
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
    }

    @Test
    public void testReadSingleCharAndReadAgain() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("ab"));

        assertEquals('a', br.read());
        assertEquals('a', br.readAgain());

        assertEquals('b', br.read());
        assertEquals('b', br.readAgain());

        // End of stream
        assertEquals(-1, br.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
    }

    @Test
    public void testLookAheadDoesNotConsume() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("xy"));

        assertEquals('x', br.lookAhead());
        // lookAhead should not consume - reading again returns same char
        assertEquals('x', br.lookAhead());

        assertEquals('x', br.read());
        assertEquals('y', br.lookAhead());
        assertEquals('y', br.read());

        // At end of stream, lookAhead returns -1
        assertEquals(-1, br.lookAhead());
        assertEquals(-1, br.read());
    }

    @Test
    public void testReadCharArrayBasic() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("hello"));
        char[] buf = new char[5];

        int len = br.read(buf, 0, 5);

        assertEquals(5, len);
        assertArrayEquals("hello".toCharArray(), buf);
        assertEquals('o', br.readAgain());
    }

    @Test
    public void testReadCharArrayZeroLengthReturnsZero() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("hello"));
        char[] buf = new char[5];

        int len = br.read(buf, 0, 0);

        assertEquals(0, len);
        // readAgain unaffected - still UNDEFINED since nothing was read
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
    }

    @Test
    public void testReadCharArrayAtEofReturnsMinusOne() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader(""));
        char[] buf = new char[5];

        int len = br.read(buf, 0, 5);

        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
    }

    @Test
    public void testReadLineBasicAndLineNumber() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("line1\nline2"));

        String l1 = br.readLine();
        assertEquals("line1", l1);
        assertEquals('1', br.readAgain());
        assertEquals(1, br.getLineNumber());

        String l2 = br.readLine();
        assertEquals("line2", l2);
        assertEquals('2', br.readAgain());
        assertEquals(2, br.getLineNumber());

        // Reached EOF
        String l3 = br.readLine();
        assertNull(l3);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        // Line counter is not incremented when EOF is reached
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadLineEmptyLine() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("\nabc"));

        String l1 = br.readLine();
        assertEquals("", l1);
        // lastChar unchanged (empty line has no chars) - remains UNDEFINED
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(1, br.getLineNumber());

        String l2 = br.readLine();
        assertEquals("abc", l2);
        assertEquals('c', br.readAgain());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testGetLineNumberWithCrLfSingleReads() throws IOException {
        // "a\r\nb" : reading char-by-char via read()
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("a\r\nb"));

        assertEquals('a', br.read());
        assertEquals(0, br.getLineNumber());

        assertEquals('\r', br.read());
        assertEquals(1, br.getLineNumber());

        // '\n' immediately following '\r' should NOT increment the counter again
        assertEquals('\n', br.read());
        assertEquals(1, br.getLineNumber());

        assertEquals('b', br.read());
        assertEquals(1, br.getLineNumber());

        assertEquals(-1, br.read());
        assertEquals(1, br.getLineNumber());
    }

    @Test
    public void testGetLineNumberWithLoneLfAndCr() throws IOException {
        // Two separate lines using bare LF then bare CR
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("a\nb\rc"));

        assertEquals('a', br.read());
        assertEquals(0, br.getLineNumber());

        assertEquals('\n', br.read());
        assertEquals(1, br.getLineNumber());

        assertEquals('b', br.read());
        assertEquals(1, br.getLineNumber());

        assertEquals('\r', br.read());
        assertEquals(2, br.getLineNumber());

        assertEquals('c', br.read());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadCharArrayCrLfSplitAcrossReadCalls() throws IOException {
        // First read consumes the '\r' via single-char read(),
        // then the '\n' is read via the buffer read() method.
        // The line counter must NOT be incremented a second time
        // for the '\n' immediately following a '\r'.
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("\r\n"));

        assertEquals('\r', br.read());
        assertEquals(1, br.getLineNumber());

        char[] buf = new char[1];
        int len = br.read(buf, 0, 1);

        assertEquals(1, len);
        assertEquals('\n', buf[0]);
        // No additional increment because lastChar was '\r'
        assertEquals(1, br.getLineNumber());
        assertEquals('\n', br.readAgain());
    }

    @Test
    public void testReadCharArrayCountsNewlinesWithinBuffer() throws IOException {
        // Buffer contains a bare LF not preceded by CR -> should increment.
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("x\ny"));
        char[] buf = new char[3];

        int len = br.read(buf, 0, 3);

        assertEquals(3, len);
        assertArrayEquals(new char[] { 'x', '\n', 'y' }, buf);
        assertEquals(1, br.getLineNumber());
        assertEquals('y', br.readAgain());
    }

    @Test
    public void testReadCharArrayCountsCrWithinBuffer() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("x\ry"));
        char[] buf = new char[3];

        int len = br.read(buf, 0, 3);

        assertEquals(3, len);
        assertArrayEquals(new char[] { 'x', '\r', 'y' }, buf);
        assertEquals(1, br.getLineNumber());
        assertEquals('y', br.readAgain());
    }

    @Test
    public void testReadCharArrayCrLfEntirelyWithinBuffer() throws IOException {
        // "\r\n" fully within a single buffer read: only one increment
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("a\r\nb"));
        char[] buf = new char[4];

        int len = br.read(buf, 0, 4);

        assertEquals(4, len);
        assertArrayEquals(new char[] { 'a', '\r', '\n', 'b' }, buf);
        // '\r' increments (1), '\n' preceded by '\r' in buffer does not increment again
        assertEquals(1, br.getLineNumber());
        assertEquals('b', br.readAgain());
    }

    @Test
    public void testReadCharArrayInvalidLengthThrows() {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abcde"));
        char[] buf = new char[5];

        try {
            br.read(buf, 0, -1);
            fail("Expected IndexOutOfBoundsException for negative length");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testReadCharArrayInvalidOffsetThrows() {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abcde"));
        char[] buf = new char[5];

        try {
            br.read(buf, -1, 2);
            fail("Expected IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testReadAfterCloseThrowsIOException() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abc"));
        br.close();

        try {
            br.read();
            fail("Expected IOException after close");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void testLookAheadAfterCloseThrowsIOException() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abc"));
        br.close();

        try {
            br.lookAhead();
            fail("Expected IOException after close");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void testGetLineNumberInitiallyZero() {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals(0, br.getLineNumber());
    }

    @Test
    public void testMultipleLookAheadsThenReadConsistency() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("Z"));

        int la1 = br.lookAhead();
        int la2 = br.lookAhead();
        assertEquals(la1, la2);
        assertEquals('Z', la1);

        int r = br.read();
        assertEquals('Z', r);
        assertEquals(-1, br.lookAhead());
        assertEquals(-1, br.read());
    }

    @Test
    public void testReadCharArrayPartialLengthLessThanBuffer() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abcdef"));
        char[] buf = new char[10];

        int len = br.read(buf, 2, 3);

        assertEquals(3, len);
        assertEquals('a', buf[2]);
        assertEquals('b', buf[3]);
        assertEquals('c', buf[4]);
        assertEquals('c', br.readAgain());
    }
}
