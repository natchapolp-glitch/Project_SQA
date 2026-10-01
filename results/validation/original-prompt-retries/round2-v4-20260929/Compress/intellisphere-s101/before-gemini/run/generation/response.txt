Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java

```

```java
package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Regression tests for CpioArchiveOutputStream.
 * Based on JUnit 3.8.2 and Java 1.4 source level.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    // ---------------------------------------------------------------- helpers

    private CpioArchiveEntry newEntry(String name, long size) {
        CpioArchiveEntry entry = new CpioArchiveEntry(name);
        entry.setSize(size);
        return entry;
    }

    /**
     * Convenience: create an entry with format – used for FORMAT_NEW_CRC tests.
     */
    private CpioArchiveEntry newEntry(String name, long size, short format) {
        CpioArchiveEntry entry = new CpioArchiveEntry(name);
        entry.setSize(size);
        entry.setFormat(format);
        return entry;
    }

    /**
     * Compute padding bytes required to reach a multiple of border.
     */
    private int padAmount(long value, int border) {
        long mod = value % border;
        if (mod == 0) {
            return 0;
        }
        return border - (int) mod;
    }

    // ------------------------------------------------------- constructor tests

    public void testConstructorValidFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(baos, CpioArchiveOutputStream.FORMAT_NEW);
        baos = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(baos, CpioArchiveOutputStream.FORMAT_NEW_CRC);
        baos = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(baos, CpioArchiveOutputStream.FORMAT_OLD_ASCII);
        baos = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(baos, CpioArchiveOutputStream.FORMAT_OLD_BINARY);
        // no exception should be thrown
    }

    public void testConstructorInvalidFormat() {
        try {
            new CpioArchiveOutputStream(new ByteArrayOutputStream(), (short) 999);
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    public void testConstructorDefaultFormat() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        // put an entry without explicit format – default FORMAT_NEW should be used
        CpioArchiveEntry entry = newEntry("dummy", 0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        // if we reach here the default format was accepted
    }

    // ------------------------------------------------- putNextEntry / close tests

    public void testPutNextEntryDuplicateName() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry e1 = newEntry("dup", 10);
        CpioArchiveEntry e2 = newEntry("dup", 10);
        out.putNextEntry(e1);
        try {
            out.putNextEntry(e2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("duplicate entry") >= 0);
        }
    }

    public void testPutNextEntryClosesPreviousEntryOnSizeMismatch()
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry e1 = newEntry("a", 5);
        out.putNextEntry(e1);
        out.write(new byte[2](undefined), 0, 2); // write less than size

        CpioArchiveEntry e2 = newEntry("b", 10);
        try {
            out.putNextEntry(e2); // should close e1 and fail
            fail("Expected IOException because previous entry size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("invalid entry size") >= 0);
        }
    }

    public void testPutNextEntrySetsTimeIfNotSet() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry e = newEntry("time", 0);
        e.setTime(-1); // not set
        long before = System.currentTimeMillis();
        out.putNextEntry(e);
        long after = System.currentTimeMillis();
        // entry time should be within [before, after]
        assertTrue("time not set", e.getTime() >= before && e.getTime() <= after);
    }

    public void testPutNextEntrySetsFormatIfNotSet() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry e = newEntry("fmt", 0);
        e.setFormat((short) -1);
        out.putNextEntry(e);
        assertEquals("format should be default (NEW)", 
                     CpioArchiveOutputStream.FORMAT_NEW, e.getFormat());
    }

    // ------------------------------------------------- write tests

    public void testWriteWithoutEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.write(new byte[] { 1, 2 }, 0, 2);
            fail("Expected IOException – no current CPIO entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("no current CPIO entry") >= 0);
        }
    }

    public void testWriteBeyondSize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = newEntry("size", 1);
        out.putNextEntry(entry);
        out.write(0); // write one byte, size is 1
        try {
            out.write(0); // second byte exceeds size
            fail("Expected IOException – past end of STORED entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("past end") >= 0);
        }
    }

    public void testWriteInvalidOffsetOrLength() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = newEntry("off", 10);
        out.putNextEntry(entry);
        try {
            out.write(new byte[5](undefined), -1, 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            out.write(new byte[5](undefined), 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            out.write(new byte[5](undefined), 3, 3); // off + len > length
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testWriteZeroLengthDoesNothing() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = newEntry("zero", 10);
        out.putNextEntry(entry);
        int before = baos.size();
        out.write(new byte[5](undefined), 0, 0);
        out.write(0, 0, 0); // same
        int after = baos.size();
        assertEquals(before, after); // no bytes written
    }

    // ---------------------------------------- closeArchiveEntry tests

    public void testCloseArchiveEntrySizeMismatch() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry e = newEntry("x", 10);
        out.putNextEntry(e);
        out.write(new byte[5](undefined)); // write 5 bytes
        try {
            out.closeArchiveEntry();
            fail("Expected IOException – invalid entry size");
        } catch (IOException ioe) {
            assertTrue(ioe.getMessage().indexOf("invalid entry size") >= 0);
        }
    }

    public void testCloseArchiveEntryWRONGwithoutEntryThrowsNPE() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.closeArchiveEntry(); // cpioEntry is null
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected behaviour – no current entry
        }
    }

    public void testCloseArchiveEntryCRCError() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos,
                CpioArchiveOutputStream.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = newEntry("crc", 3, CpioArchiveOutputStream.FORMAT_NEW_CRC);
        entry.setChksum(0); // will not match written bytes (1+1+1 = 3)
        out.putNextEntry(entry);
        out.write(new byte[] { 1, 1, 1 });
        try {
            out.closeArchiveEntry();
            fail("Expected IOException – CRC Error");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("CRC Error") >= 0);
        }
    }

    // ---------------------------------------------------- finish tests

    public void testFinishWritesTrailer() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();
        // the trailer entry name "TRAILER!!!" should appear somewhere
        String content = baos.toString();
        assertTrue("trailer not written: " + content,
                   content.indexOf("TRAILER!!!") >= 0);
    }

    public void testFinishIsIdempotent() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();
        int lenAfterFirst = baos.size();
        out.finish(); // should not write again
        assertEquals(lenAfterFirst, baos.size());
    }

    public void testAfterFinishWriteFails() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();
        try {
            out.write(new byte[] { 1, 2 });
            fail("Expected IOException – no current CPIO entry after finish");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("no current CPIO entry") >= 0);
        }
    }

    // ------------------------------------------------------ close tests

    public void testCloseCallsFinish() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.close();
        // finish should have been called, hence TRAILER!!! must be present
        assertTrue(baos.toString().indexOf("TRAILER!!!") >= 0);
    }

    public void testOperationsAfterCloseThrow() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.close();
        try {
            out.write(new byte[] { 1, 2 });
            fail("Expected IOException – stream closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
        try {
            out.putNextEntry(newEntry("a", 10));
            fail("Expected IOException – stream closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
        try {
            out.closeArchiveEntry();
            fail("Expected IOException – stream closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
        try {
            out.finish();
            fail("Expected IOException – stream closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    // ------------------------------------------------- padding tests

    public void testPaddingNewFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos,
                CpioArchiveOutputStream.FORMAT_NEW);
        CpioArchiveEntry entry = newEntry("pad", 3);
        out.putNextEntry(entry);
        out.write(new byte[] { 1, 2, 3 });
        out.closeArchiveEntry();

        int headerSize = entry.getHeaderSize();
        int nameLen = entry.getName().length();
        int totalHeader = headerSize + nameLen + 1;
        int padHeader = padAmount(totalHeader, 4);
        int padData = padAmount(3, 4);
        int expectedTotal = totalHeader + padHeader + 3 + padData;
        assertEquals(expectedTotal, baos.size());
    }

    public void testPaddingOldBinaryFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos,
                CpioArchiveOutputStream.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = newEntry("binpad", 3, 
                CpioArchiveOutputStream.FORMAT_OLD_BINARY);
        out.putNextEntry(entry);
        out.write(new byte[] { 1, 2, 3 });
        out.closeArchiveEntry();

        int headerSize = entry.getHeaderSize();
        int nameLen = entry.getName().length();
        int totalHeader = headerSize + nameLen + 1;
        int padHeader = padAmount(totalHeader, 2); // old binary pad for header
        int padData = padAmount(3, 2);              // data pad to 2 bytes
        int expectedTotal = totalHeader + padHeader + 3 + padData;
        assertEquals(expectedTotal, baos.size());
    }

    // -------------------------------------- write(int) and putArchiveEntry

    public void testWriteIntDelegatesToOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = newEntry("int", 1);
        out.putNextEntry(entry);
        out.write('X');
        out.closeArchiveEntry();
        // the byte 'X' should be somewhere in the stream (past the header).
        byte[] raw = baos.toByteArray();
        boolean found = false;
        // simply check that last 5 bytes contain 'X' – crude but adequate
        for (int i = Math.max(0, raw.length - 5); i < raw.length; i++) {
            if (raw[i] == 'X') {
                found = true;
                break;
            }
        }
        assertTrue("written byte not found in stream", found);
    }

    public void testPutArchiveEntryInvokesPutNextEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = newEntry("viaput", 10);
        out.putArchiveEntry(entry); // calls putNextEntry internally
        // verify that duplicate detection works through this method
        try {
            out.putArchiveEntry(newEntry("viaput", 10));
            fail("Expected IOException for duplicate entry via putArchiveEntry");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("duplicate entry") >= 0);
        }
    }

    // --------------------------------- multiple entries sanity

    public void testMultipleEntries() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        for (int i = 0; i < 5; i++) {
            CpioArchiveEntry e = newEntry("file" + i, i + 1);
            out.putNextEntry(e);
            byte[] data = new byte[i + 1];
            for (int j = 0; j < data.length; j++) {
                data[j] = (byte) (i + 1);
            }
            out.write(data);
            out.closeArchiveEntry();
        }
        out.finish();
        // no exception up to here = success
        assertTrue(baos.size() > 0);
    }
}

```