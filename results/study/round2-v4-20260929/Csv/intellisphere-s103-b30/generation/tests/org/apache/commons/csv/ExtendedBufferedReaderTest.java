package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    // --------------------------------------------------------------
    // readAgain tests
    // --------------------------------------------------------------

    @Test
    public void testReadAgainInitiallyUndefined() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
    }

    @Test
    public void testReadAgainAfterRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        reader.read();
        assertEquals('a', reader.readAgain());
    }

    @Test
    public void testReadAgainAfterReadToEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read(); // 'a'
        reader.read(); // -1 end of stream
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadAgainAfterCharArrayRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[2];
        reader.read(buf, 0, 2);
        assertEquals('b', reader.readAgain());
    }

    @Test
    public void testReadAgainAfterCharArrayReadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        char[] buf = new char[2];
        int len = reader.read(buf, 0, 2); // reads 1, returns 1
        assertEquals(1, len);
        len = reader.read(buf, 0, 2); // returns -1
        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    // --------------------------------------------------------------
    // lookAhead tests
    // --------------------------------------------------------------

    @Test
    public void testLookAheadDoesNotConsume() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int ahead = reader.lookAhead();
        assertEquals('a', ahead);
        assertEquals('a', reader.read());
    }

    @Test
    public void testLookAheadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.lookAhead());
        assertEquals(-1, reader.read());
    }

    @Test
    public void testLookAheadDoesNotAffectLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\nb"));
        reader.lookAhead(); // 'a'
        assertEquals(0, reader.getLineNumber());
        reader.read(); // 'a'
        reader.lookAhead(); // '\n'
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testLookAheadAfterReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));
        reader.read(); // 'a'
        assertEquals('a', reader.readAgain());
        assertEquals('b', reader.lookAhead());
    }

    // --------------------------------------------------------------
    // getLineNumber tests
    // --------------------------------------------------------------

    @Test
    public void testGetLineNumberInitial() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals(0, reader.getLineNumber());
    }

    

    

    // --------------------------------------------------------------
    // read() tests
    // --------------------------------------------------------------

    @Test
    public void testReadSingleChar() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadEmptyStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadIncrementsLineNumberForLF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\nb"));
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());
        assertEquals('\n', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadIncrementsLineNumberForCRLF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\r\nb"));
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());
        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());
        assertEquals('\n', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadIncrementsLineNumberForCR() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\rb"));
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());
        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());
        assertEquals('b', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadMultipleLinesWithCRLF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\r\nb\r\nc"));
        reader.read(); // a
        reader.read(); // \r
        assertEquals(1, reader.getLineNumber());
        reader.read(); // \n
        assertEquals(1, reader.getLineNumber());
        reader.read(); // b
        reader.read(); // \r
        assertEquals(2, reader.getLineNumber());
        reader.read(); // \n
        assertEquals(2, reader.getLineNumber());
        reader.read(); // c
        assertEquals(2, reader.getLineNumber());
    }

    // --------------------------------------------------------------
    // read(char[], int, int) tests
    // --------------------------------------------------------------

    @Test
    public void testReadCharArrayZeroLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[10];
        int len = reader.read(buf, 0, 0);
        assertEquals(0, len);
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayNormal() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef"));
        char[] buf = new char[4];
        int len = reader.read(buf, 0, 4);
        assertEquals(4, len);
        assertEquals("abcd", new String(buf, 0, len));
        assertEquals('d', reader.readAgain());
    }

    @Test
    public void testReadCharArrayWithOffset() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef"));
        char[] buf = new char[8];
        int len = reader.read(buf, 2, 3);
        assertEquals(3, len);
        assertEquals('a', buf[2]);
        assertEquals('b', buf[3]);
        assertEquals('c', buf[4]);
    }

    @Test
    public void testReadCharArrayShorterThanBuffer() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 5);
        assertEquals(2, len);
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
    }

    @Test
    public void testReadCharArrayEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        char[] buf = new char[2];
        int len = reader.read(buf, 0, 2);
        assertEquals(1, len);
        len = reader.read(buf, 0, 2);
        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArrayLineCounterLF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\ncd"));
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCounterCRLF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\r\ncd"));
        char[] buf = new char[6];
        reader.read(buf, 0, 6);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCounterTwoLines() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\ncd\nef"));
        char[] buf = new char[8];
        reader.read(buf, 0, 8);
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCounterCRWithinBuffer() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\rcd"));
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayLineCounterSoloCR() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\rcd"));
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals('d', reader.readAgain());
    }

    

    @Test
    public void testReadCharArrayCRLFLineCountingWithSplit() throws IOException {
        // Read "a\r" first, then "\nb"
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a\r\nb"));
        char[] buf1 = new char[2];
        int len1 = reader.read(buf1, 0, 2); // reads 'a','\r'
        assertEquals(2, len1);
        assertEquals(1, reader.getLineNumber()); // CR increments line

        char[] buf2 = new char[2];
        int len2 = reader.read(buf2, 0, 2); // reads '\n','b'
        assertEquals(2, len2);
        assertEquals(1, reader.getLineNumber()); // LF should not increment because lastChar before it was '\r'
        assertEquals('b', reader.readAgain());
    }

    

    // --------------------------------------------------------------
    // readLine tests
    // --------------------------------------------------------------

    

    

    

    

    
}
