package org.apache.commons.csv;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        // Reset for each test
    }

    // Constructor tests
    @Test
    public void testConstructorWithSimpleReader() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test"));
        assertNotNull(reader);
        assertEquals(0, reader.getLineNumber());
    }

    // getLineNumber() tests
    @Test
    public void testGetLineNumberInitial() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("line1"));
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumberAfterLF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\nb"));
        reader.read(); // 'a'
        assertEquals(0, reader.getLineNumber());
        reader.read(); // '\n'
        assertEquals(1, reader.getLineNumber());
        reader.read(); // 'b'
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumberAfterCR() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\rb"));
        reader.read(); // 'a'
        assertEquals(0, reader.getLineNumber());
        reader.read(); // '\r'
        assertEquals(1, reader.getLineNumber());
        reader.read(); // 'b'
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumberAfterCRLF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\r\nb"));
        reader.read(); // 'a'
        assertEquals(0, reader.getLineNumber());
        reader.read(); // '\r'
        assertEquals(1, reader.getLineNumber());
        reader.read(); // '\n' (should not increment after '\r')
        assertEquals(1, reader.getLineNumber());
        reader.read(); // 'b'
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumberMultipleLines() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\nb\nc"));
        reader.read(); // 'a'
        assertEquals(0, reader.getLineNumber());
        reader.read(); // '\n'
        assertEquals(1, reader.getLineNumber());
        reader.read(); // 'b'
        assertEquals(1, reader.getLineNumber());
        reader.read(); // '\n'
        assertEquals(2, reader.getLineNumber());
        reader.read(); // 'c'
        assertEquals(2, reader.getLineNumber());
    }

    // read() tests - single character
    @Test
    public void testReadSingleChar() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a"));
        assertEquals('a', reader.read());
    }

    @Test
    public void testReadMultipleChars() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
    }

    @Test
    public void testReadEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAfterEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read(); // 'a'
        assertEquals(-1, reader.read());
        assertEquals(-1, reader.read()); // Multiple EOFs
    }

    // readAgain() tests
    @Test
    public void testReadAgainInitial() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test"));
        assertEquals(-2, reader.readAgain()); // UNDEFINED
    }

    @Test
    public void testReadAgainAfterRead() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read(); // 'a'
        assertEquals('a', reader.readAgain());
    }

    @Test
    public void testReadAgainAfterEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read(); // 'a'
        reader.read(); // EOF
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM
    }

    @Test
    public void testReadAgainMultipleReads() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("ab"));
        reader.read(); // 'a'
        assertEquals('a', reader.readAgain());
        reader.read(); // 'b'
        assertEquals('b', reader.readAgain());
    }

    // lookAhead() tests
    @Test
    public void testLookAheadSingleChar() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("ab"));
        assertEquals('a', reader.lookAhead());
        // Next read should still return 'a'
        assertEquals('a', reader.read());
    }

    @Test
    public void testLookAheadMultiple() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.lookAhead()); // Multiple peeks
        assertEquals('a', reader.read());
        assertEquals('b', reader.lookAhead());
        assertEquals('b', reader.read());
    }

    @Test
    public void testLookAheadEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testLookAheadAtEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read(); // 'a'
        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testLookAheadNewline() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\nb"));
        assertEquals('a', reader.lookAhead());
        reader.read(); // 'a'
        assertEquals('\n', reader.lookAhead());
        reader.read(); // '\n'
        assertEquals('b', reader.lookAhead());
    }

    // read(char[], int, int) tests
    @Test
    public void testReadCharArrayZeroLength() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[3];
        assertEquals(0, reader.read(buf, 0, 0));
    }

    @Test
    public void testReadCharArraySimple() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[3];
        assertEquals(3, reader.read(buf, 0, 3));
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals('c', buf[2]);
    }

    @Test
    public void testReadCharArrayPartial() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abcde"));
        char[] buf = new char[5];
        assertEquals(2, reader.read(buf, 1, 2));
        assertEquals('a', buf[1]);
        assertEquals('b', buf[2]);
    }

    @Test
    public void testReadCharArrayEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("ab"));
        char[] buf = new char[5];
        assertEquals(2, reader.read(buf, 0, 5));
        assertEquals(-1, reader.read(buf, 0, 5));
    }

    @Test
    public void testReadCharArrayLineCountingLF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\nb\nc"));
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCountingCR() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\rb\rc"));
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCountingCRLF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\r\nb\r\nc"));
        char[] buf = new char[7];
        reader.read(buf, 0, 7);
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLastCharTracking() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[3];
        reader.read(buf, 0, 3);
        assertEquals('c', reader.readAgain());
    }

    @Test
    public void testReadCharArrayMixedOffsets() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("abcdefgh"));
        char[] buf = new char[8];
        assertEquals(3, reader.read(buf, 2, 3));
        assertEquals('a', buf[2]);
        assertEquals('b', buf[3]);
        assertEquals('c', buf[4]);
    }

    // readLine() tests
    @Test
    public void testReadLineSimple() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("hello"));
        assertEquals("hello", reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithLF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("hello\nworld"));
        assertEquals("hello", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("world", reader.readLine());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadLineEmpty() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader(""));
        assertNull(reader.readLine());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadLineEOF() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("line"));
        reader.readLine();
        assertNull(reader.readLine());
    }

    @Test
    public void testReadLineLastCharTracking() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("hello"));
        reader.readLine();
        assertEquals('o', reader.readAgain());
    }

    @Test
    public void testReadLineEmptyLine() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("\n"));
        String line = reader.readLine();
        assertEquals("", line);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineMultipleEmptyLines() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\n\n\nb"));
        assertEquals("a", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertEquals("", reader.readLine());
        assertEquals(3, reader.getLineNumber());
        assertEquals("b", reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    // Integration tests combining multiple operations
    @Test
    public void testMixedReadOperations() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("a\nbc"));
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());
        assertEquals('\n', reader.read());
        assertEquals(1, reader.getLineNumber());
        char[] buf = new char[2];
        assertEquals(2, reader.read(buf, 0, 2));
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAheadPreservesState() throws IOException {
        reader = new ExtendedBufferedReader(new StringReader("test"));
        assertEquals('t', reader.lookAhead());
        assertEquals(-2, reader.readAgain()); // Still UNDEFINED until actual read
        assertEquals('t', reader.read());
        assertEquals('t', reader.readAgain());
    }
}
