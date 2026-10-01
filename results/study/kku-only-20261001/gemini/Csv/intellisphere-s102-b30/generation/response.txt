org/apache/commons/csv/ExtendedBufferedReaderTest.java

```java
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for {@link ExtendedBufferedReader}.
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader createReader(String input) {
        return new ExtendedBufferedReader(new StringReader(input));
    }

    @Test
    public void testInitialState() {
        ExtendedBufferedReader reader = createReader("abc");
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testLookAheadEmptyStream() throws IOException {
        ExtendedBufferedReader reader = createReader("");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testLookAheadDoesNotConsume() throws IOException {
        ExtendedBufferedReader reader = createReader("a");
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.lookAhead());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        assertEquals('a', reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testReadSingleChars() throws IOException {
        ExtendedBufferedReader reader = createReader("hi");
        assertEquals('h', reader.read());
        assertEquals('h', reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        assertEquals('i', reader.read());
        assertEquals('i', reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadLineCounterLF() throws IOException {
        ExtendedBufferedReader reader = createReader("a\nb\n");
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());

        assertEquals('\n', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('b', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('\n', reader.read());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadLineCounterCR() throws IOException {
        ExtendedBufferedReader reader = createReader("a\rb\r");
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());

        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('b', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('\r', reader.read());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadLineCounterCRLF() throws IOException {
        ExtendedBufferedReader reader = createReader("a\r\nb\r\n");
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());

        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('\n', reader.read());
        assertEquals(1, reader.getLineNumber()); // \r\n counts as 1 newline

        assertEquals('b', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('\r', reader.read());
        assertEquals(2, reader.getLineNumber());

        assertEquals('\n', reader.read());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadLineCounterConsecutiveNewlines() throws IOException {
        ExtendedBufferedReader reader = createReader("\r\r\n\n");
        assertEquals('\r', reader.read());
        assertEquals(1, reader.getLineNumber());

        assertEquals('\r', reader.read());
        assertEquals(2, reader.getLineNumber());

        assertEquals('\n', reader.read());
        assertEquals(2, reader.getLineNumber()); // preceded by \r, no increment

        assertEquals('\n', reader.read());
        assertEquals(3, reader.getLineNumber()); // preceded by \n, increments
    }

    @Test
    public void testReadCharArrayZeroLength() throws IOException {
        ExtendedBufferedReader reader = createReader("abcdef");
        char[] buf = new char[10];
        int read = reader.read(buf, 0, 0);

        assertEquals(0, read);
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayEmptyStream() throws IOException {
        ExtendedBufferedReader reader = createReader("");
        char[] buf = new char[10];
        int read = reader.read(buf, 0, 5);

        assertEquals(-1, read);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadCharArrayBasic() throws IOException {
        ExtendedBufferedReader reader = createReader("hello world");
        char[] buf = new char[5];
        int read = reader.read(buf, 0, 5);

        assertEquals(5, read);
        assertEquals("hello", new String(buf, 0, 5));
        assertEquals('o', reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadCharArrayWithOffset() throws IOException {
        ExtendedBufferedReader reader = createReader("abc");
        char[] buf = new char[5];
        int read = reader.read(buf, 2, 3);

        assertEquals(3, read);
        assertEquals('a', buf[2]);
        assertEquals('b', buf[3]);
        assertEquals('c', buf[4]);
        assertEquals('c', reader.readAgain());
    }

    @Test
    public void testReadCharArrayLineCounting() throws IOException {
        ExtendedBufferedReader reader = createReader("a\nb\r\nc\rd");
        char[] buf = new char[10];
        int read = reader.read(buf, 0, 10);

        assertEquals(8, read);
        // \n (1), \r\n (1), \r (1) => total 3 lines
        assertEquals(3, reader.getLineNumber());
        assertEquals('d', reader.readAgain());
    }

    @Test
    public void testReadCharArraySplitCRLF() throws IOException {
        ExtendedBufferedReader reader = createReader("a\r\nb");
        char[] buf = new char[2];

        int read1 = reader.read(buf, 0, 2); // "a\r"
        assertEquals(2, read1);
        assertEquals('a', buf[0]);
        assertEquals('\r', buf[1]);
        assertEquals(1, reader.getLineNumber());
        assertEquals('\r', reader.readAgain());

        int read2 = reader.read(buf, 0, 2); // "\nb"
        assertEquals(2, read2);
        assertEquals('\n', buf[0]);
        assertEquals('b', buf[1]);
        // \n immediately following \r from previous read shouldn't increment
        assertEquals(1, reader.getLineNumber());
        assertEquals('b', reader.readAgain());
    }

    @Test
    public void testReadLineSimple() throws IOException {
        ExtendedBufferedReader reader = createReader("first line\nsecond line");
        String line1 = reader.readLine();

        assertEquals("first line", line1);
        assertEquals(1, reader.getLineNumber());
        assertEquals('e', reader.readAgain());

        String line2 = reader.readLine();
        assertEquals("second line", line2);
        assertEquals(2, reader.getLineNumber());
        assertEquals('e', reader.readAgain());

        String line3 = reader.readLine();
        assertNull(line3);
        assertEquals(2, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLineEmptyLine() throws IOException {
        ExtendedBufferedReader reader = createReader("\ncontent\n");
        String line1 = reader.readLine();

        assertEquals("", line1);
        assertEquals(1, reader.getLineNumber());
        // For an empty line, lastChar is not overwritten
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        String line2 = reader.readLine();
        assertEquals("content", line2);
        assertEquals(2, reader.getLineNumber());
        assertEquals('t', reader.readAgain());
    }

    @Test
    public void testReadLineAtEOF() throws IOException {
        ExtendedBufferedReader reader = createReader("");
        String line = reader.readLine();

        assertNull(line);
        assertEquals(0, reader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testMixedReadAndReadLine() throws IOException {
        ExtendedBufferedReader reader = createReader("ab\ncd");
        assertEquals('a', reader.read());
        assertEquals(0, reader.getLineNumber());

        String remainingLine = reader.readLine();
        assertEquals("b", remainingLine);
        assertEquals(1, reader.getLineNumber());
        assertEquals('b', reader.readAgain());

        String nextLine = reader.readLine();
        assertEquals("cd", nextLine);
        assertEquals(2, reader.getLineNumber());
        assertEquals('d', reader.readAgain());
    }

    @Test
    public void testLookAheadAndReadInterleaved() throws IOException {
        ExtendedBufferedReader reader = createReader("xy");
        assertEquals('x', reader.lookAhead());
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        assertEquals('x', reader.read());
        assertEquals('x', reader.readAgain());

        assertEquals('y', reader.lookAhead());
        assertEquals('x', reader.readAgain());

        assertEquals('y', reader.read());
        assertEquals('y', reader.readAgain());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
        assertEquals('y', reader.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
}

```



References

Apache Commons CSV Component, Apache Software Foundation:
Source: src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java
Issue Tracking & Repository: https://gitbox.apache.org/repos/asf/commons-csv.git / https://issues.apache.org/jira/projects/CSV
JUnit 4 Framework Documentation:
Package org.junit (JUnit 4.10): https://junit.org/junit4/javadoc/4.10/
