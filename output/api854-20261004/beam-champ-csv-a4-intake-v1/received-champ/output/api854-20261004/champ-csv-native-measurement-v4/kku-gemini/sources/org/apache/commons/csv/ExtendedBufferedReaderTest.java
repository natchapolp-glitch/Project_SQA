package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testGetLineNumberInitial() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadSingleCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        int c = reader.read();
        assertEquals('a', c);
        assertEquals('a', reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int c = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadNewlineIncrementsLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
        int c = reader.read();
        assertEquals('\n', c);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadCarriageReturnAndNewline() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r\n"));
        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());
        assertEquals('\n', reader.read());
        // \n following \r should not increment the line counter again
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAheadDoesNotConsumeCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.read());
        assertEquals('b', reader.lookAhead());
        assertEquals('a', reader.readAgain());
    }

    @Test
    public void testLookAheadAtEof() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadLineBasic() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2"));
        String line1 = reader.readLine();
        assertEquals("line1", line1);
        assertEquals(1, reader.getLineNumber());
        assertEquals('1', reader.readAgain());

        String line2 = reader.readLine();
        assertEquals("line2", line2);
        assertEquals(2, reader.getLineNumber());
        assertEquals('2', reader.readAgain());
    }

    @Test
    public void testReadLineEof() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        assertNull(reader.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadBufferZeroLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 0);
        assertEquals(0, len);
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadBufferPartial() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 3);
        assertEquals(3, len);
        assertEquals('h', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', reader.readAgain());
    }

    @Test
    public void testReadBufferWithNewlines() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\nb\r\nc"));
        char[] buf = new char[10];
        int len = reader.read(buf, 0, 6);
        assertEquals(6, len);
        assertEquals(2, reader.getLineNumber());
        assertEquals('\r', reader.readAgain());
    }

    @Test
    public void testReadBufferEof() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 5);
        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testMultipleLookAheads() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("xyz"));
        assertEquals('x', reader.lookAhead());
        assertEquals('x', reader.lookAhead());
        assertEquals('x', reader.read());
        assertEquals('y', reader.lookAhead());
    }

    @Test
    public void testReadLineEmptyLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
        String line = reader.readLine();
        assertEquals("", line);
        assertEquals(1, reader.getLineNumber());
    }
}
