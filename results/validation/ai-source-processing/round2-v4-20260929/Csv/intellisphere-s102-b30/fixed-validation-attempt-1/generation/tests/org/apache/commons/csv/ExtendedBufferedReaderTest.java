package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testInitialStateAndLookAheadDoNotConsume() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));

        assertEquals(0, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        assertEquals((int) 'a', reader.lookAhead());
        assertEquals(0, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        assertEquals((int) 'a', reader.read());
        assertEquals((int) 'a', reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadSingleCharactersTracksLineNumbersForCrLfAndLf() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\r\nb\nc\rd"));

        assertEquals((int) 'a', reader.read());
        assertEquals(0, reader.getLineNumber());

        assertEquals((int) '\r', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals((int) '\n', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals((int) 'b', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals((int) '\n', reader.read());
        assertEquals(2, reader.getLineNumber());

        assertEquals((int) 'c', reader.read());
        assertEquals(2, reader.getLineNumber());

        assertEquals((int) '\r', reader.read());
        assertEquals(3, reader.getLineNumber());

        assertEquals((int) 'd', reader.read());
        assertEquals(3, reader.getLineNumber());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(3, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArrayLengthZeroReturnsZeroAndKeepsState() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("xyz"));
        char[] buffer = new char[] { 'a', 'b', 'c' };

        int count = reader.read(buffer, 1, 0);

        assertEquals(0, count);
        assertEquals(0, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals('a', buffer[0]);
        assertEquals('b', buffer[1]);
        assertEquals('c', buffer[2]);
    }

    @Test
    public void testReadCharArrayCountsNewlinesWithinBufferAndAcrossCrLf() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A\r\nB\nC\rD"));
        char[] buffer = new char[8];

        int count = reader.read(buffer, 0, 4);
        assertEquals(4, count);
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) 'B', reader.readAgain());
        assertEquals("A\r\nB", new String(buffer, 0, 4));

        count = reader.read(buffer, 0, 4);
        assertEquals(4, count);
        assertEquals(3, reader.getLineNumber());
        assertEquals((int) 'D', reader.readAgain());
        assertEquals("\nC\rD", new String(buffer, 0, 4));

        count = reader.read(buffer, 0, 4);
        assertEquals(-1, count);
        assertEquals(3, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArrayCountsLfAfterPreviousNonCrLastChar() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("X\nY"));
        char[] buffer = new char[2];

        int count = reader.read(buffer, 0, 1);
        assertEquals(1, count);
        assertEquals(0, reader.getLineNumber());
        assertEquals((int) 'X', reader.readAgain());

        count = reader.read(buffer, 0, 2);
        assertEquals(2, count);
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) 'Y', reader.readAgain());
        assertEquals('\n', buffer[0]);
        assertEquals('Y', buffer[1]);
    }

    @Test
    public void testReadCharArrayDoesNotDoubleCountLfAfterPreviousCrLastChar() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r\nZ"));
        char[] buffer = new char[1];

        int count = reader.read(buffer, 0, 1);
        assertEquals(1, count);
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) '\r', reader.readAgain());

        count = reader.read(buffer, 0, 2);
        assertEquals(2, count);
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) 'Z', reader.readAgain());
    }

    @Test
    public void testReadLineUpdatesLastCharAndLineCount() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef"));

        assertEquals("abc", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) 'c', reader.readAgain());

        assertEquals("def", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals((int) 'f', reader.readAgain());

        assertNull(reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLineWithEmptyLineIncrementsCountButLeavesLastCharUnchanged() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A\n\nB"));

        assertEquals("A", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals((int) 'A', reader.readAgain());

        assertEquals("", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals((int) 'A', reader.readAgain());

        assertEquals("B", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals((int) 'B', reader.readAgain());
    }

    @Test
    public void testLookAheadAtEndOfStreamReturnsEndOfStreamWithoutChangingLastChar() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Q"));

        assertEquals((int) 'Q', reader.read());
        assertEquals((int) 'Q', reader.readAgain());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        assertEquals((int) 'Q', reader.readAgain());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArrayFromEmptyReaderSetsEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        char[] buffer = new char[3];

        int count = reader.read(buffer, 0, buffer.length);

        assertEquals(-1, count);
        assertEquals(0, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
}
