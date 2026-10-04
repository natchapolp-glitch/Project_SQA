package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadSingleCharUpdatesLastCharAndLineCounter() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("a\nb"));
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(0, br.getLineNumber());

        int c1 = br.read();
        assertEquals('a', c1);
        assertEquals('a', br.readAgain());
        assertEquals(0, br.getLineNumber());

        int c2 = br.read();
        assertEquals('\n', c2);
        assertEquals('\n', br.readAgain());
        assertEquals(1, br.getLineNumber());

        int c3 = br.read();
        assertEquals('b', c3);
        assertEquals('b', br.readAgain());
        assertEquals(1, br.getLineNumber());

        int c4 = br.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        assertEquals(1, br.getLineNumber());
    }

    @Test
    public void testReadCharArrayZeroLengthReturnsZeroWithoutSideEffects() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[5];
        int len = br.read(buf, 0, 0);
        assertEquals(0, len);
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(0, br.getLineNumber());
    }

    @Test
    public void testReadCharArrayNormalSetsLastCharAndCountsNewlines() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("ab\ncd\r\nef\r"));
        char[] buf = new char[20];
        int len = br.read(buf, 0, 20);
        assertEquals(10, len);
        // last character read is '\r'
        assertEquals('\r', br.readAgain());
        // line counting: '\n' at index2 (not preceded by '\r' in buffer) -> +1
        // '\r' at index6 -> +1 ; '\n' at index7 preceded by '\r' at index6 -> no increment
        // trailing '\r' at index9 -> +1
        assertEquals(3, br.getLineNumber());
    }

    @Test
    public void testReadCharArrayEofSetsLastCharEndOfStream() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader(""));
        char[] buf = new char[5];
        int len = br.read(buf, 0, 5);
        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        assertEquals(0, br.getLineNumber());
    }

    @Test
    public void testReadLineStripsTerminatorAndSetsLastChar() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("hello\nworld"));
        String line1 = br.readLine();
        assertEquals("hello", line1);
        assertEquals('o', br.readAgain());
        assertEquals(1, br.getLineNumber());

        String line2 = br.readLine();
        assertEquals("world", line2);
        assertEquals('d', br.readAgain());
        assertEquals(2, br.getLineNumber());

        String line3 = br.readLine();
        assertNull(line3);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadLineEmptyLineSetsLastCharFromPreviousLineAndIncrementsCounter() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("\n\n"));
        String line1 = br.readLine();
        assertEquals("", line1);
        // empty line: lastChar unchanged (still UNDEFINED since no chars in line)
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(1, br.getLineNumber());

        String line2 = br.readLine();
        assertEquals("", line2);
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(2, br.getLineNumber());

        String line3 = br.readLine();
        assertNull(line3);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testLookAheadDoesNotConsumeOrAffectState() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("xy"));
        int peeked = br.lookAhead();
        assertEquals('x', peeked);
        // lastChar and lineCounter unaffected by lookAhead
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(0, br.getLineNumber());

        int actual = br.read();
        assertEquals('x', actual);
        assertEquals('x', br.readAgain());

        int peeked2 = br.lookAhead();
        assertEquals('y', peeked2);
        assertEquals('x', br.readAgain());
    }

    @Test
    public void testLookAheadAtEndOfStreamReturnsEndOfStream() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader(""));
        int peeked = br.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, peeked);
        assertEquals(ExtendedBufferedReader.UNDEFINED, br.readAgain());
        assertEquals(0, br.getLineNumber());
    }

    @Test
    public void testGetLineNumberInitiallyZero() {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals(0, br.getLineNumber());
    }

    @Test
    public void testInterleavedReadMethodsConsistency() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("ab\ncd\nef"));
        int c1 = br.read();
        assertEquals('a', c1);
        assertEquals('a', br.readAgain());
        assertEquals(0, br.getLineNumber());

        char[] buf = new char[2];
        int len = br.read(buf, 0, 2);
        assertEquals(2, len);
        assertEquals('b', buf[0]);
        assertEquals('\n', buf[1]);
        assertEquals('\n', br.readAgain());
        assertEquals(1, br.getLineNumber());

        String line = br.readLine();
        assertEquals("cd", line);
        assertEquals('d', br.readAgain());
        assertEquals(2, br.getLineNumber());

        int c2 = br.read();
        assertEquals('e', c2);
        assertEquals('e', br.readAgain());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadCharArrayCarriageReturnOnlyCountsEachOccurrence() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("a\rb\rc"));
        char[] buf = new char[5];
        int len = br.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals('c', br.readAgain());
        assertEquals(2, br.getLineNumber());
    }

    @Test
    public void testReadLineNoTrailingTerminatorStillIncrementsCounter() throws IOException {
        ExtendedBufferedReader br = new ExtendedBufferedReader(new StringReader("lastline"));
        String line = br.readLine();
        assertEquals("lastline", line);
        assertEquals('e', br.readAgain());
        assertEquals(1, br.getLineNumber());

        String eof = br.readLine();
        assertNull(eof);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        assertEquals(1, br.getLineNumber());
    }
}
