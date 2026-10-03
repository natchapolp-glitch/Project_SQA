package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testGetLineNumberInitial() {
        StringReader reader = new StringReader("line1\nline2\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals(0, ebr.getLineNumber());
    }

    @Test
    public void testGetLineNumberAfterRead() throws IOException {
        StringReader reader = new StringReader("a\nb\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read();
        ebr.read();
        assertEquals(1, ebr.getLineNumber());
    }

    @Test
    public void testLookAheadBasic() throws IOException {
        StringReader reader = new StringReader("XYZ");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals('X', ebr.lookAhead());
        assertEquals('X', ebr.read());
        assertEquals('Y', ebr.lookAhead());
    }

    @Test
    public void testLookAheadAtEof() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
    }

    @Test
    public void testReadSingleChar() throws IOException {
        StringReader reader = new StringReader("AB");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals('A', ebr.read());
        assertEquals('B', ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
    }

    @Test
    public void testReadAgainInitial() {
        StringReader reader = new StringReader("A");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals(ExtendedBufferedReader.UNDEFINED, ebr.readAgain());
    }

    @Test
    public void testReadAgainAfterRead() throws IOException {
        StringReader reader = new StringReader("A");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read();
        assertEquals('A', ebr.readAgain());
    }

    @Test
    public void testReadLineBasic() throws IOException {
        StringReader reader = new StringReader("line1\nline2");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals("line1", ebr.readLine());
        assertEquals("line2", ebr.readLine());
        assertNull(ebr.readLine());
    }

    @Test
    public void testReadLineEmptyFile() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertNull(ebr.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadLineIncrementsLineNumber() throws IOException {
        StringReader reader = new StringReader("one\ntwo\nthree");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.readLine();
        assertEquals(1, ebr.getLineNumber());
        ebr.readLine();
        assertEquals(2, ebr.getLineNumber());
    }

    @Test
    public void testReadArrayZeroLength() throws IOException {
        StringReader reader = new StringReader("content");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        int len = ebr.read(buf, 0, 0);
        assertEquals(0, len);
    }

    @Test
    public void testReadArrayWithOffset() throws IOException {
        StringReader reader = new StringReader("ABCDE");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[10];
        int len = ebr.read(buf, 2, 3);
        assertEquals(3, len);
        assertEquals('A', buf[2]);
        assertEquals('B', buf[3]);
        assertEquals('C', buf[4]);
        assertEquals('C', ebr.readAgain());
    }

    @Test
    public void testReadArrayEof() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[5];
        int len = ebr.read(buf, 0, 5);
        assertEquals(-1, len);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testCarriageReturnLineNumber() throws IOException {
        StringReader reader = new StringReader("a\rb\nc");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read(); // 'a'
        ebr.read(); // '\r' -> lineCounter = 1
        ebr.read(); // 'b'
        ebr.read(); // '\n' -> lineCounter = 2 (not preceded by \r since last was \n)
        assertEquals(2, ebr.getLineNumber());
    }

    @Test
    public void testCrLfLineNumber() throws IOException {
        StringReader reader = new StringReader("a\r\nb");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read(); // 'a'
        ebr.read(); // '\r' -> lineCounter = 1
        ebr.read(); // '\n' -> lastChar was '\r', so lineCounter does not increment again for '\n'
        assertEquals(1, ebr.getLineNumber());
    }

    @Test
    public void testLookAheadDoesNotConsume() throws IOException {
        StringReader reader = new StringReader("123");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertEquals('1', ebr.lookAhead());
        assertEquals('1', ebr.lookAhead());
        assertEquals('1', ebr.read());
        assertEquals('2', ebr.read());
    }

    @Test
    public void testReadArrayLineCounting() throws IOException {
        StringReader reader = new StringReader("a\nb\r\nc");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[10];
        int len = ebr.read(buf, 0, 10);
        assertEquals(6, len);
        assertEquals(2, ebr.getLineNumber());
    }

    @Test
    public void testReadLineLastChar() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.readLine();
        assertEquals('o', ebr.readAgain());
        ebr.readLine();
        assertEquals('d', ebr.readAgain());
        ebr.readLine();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

}
