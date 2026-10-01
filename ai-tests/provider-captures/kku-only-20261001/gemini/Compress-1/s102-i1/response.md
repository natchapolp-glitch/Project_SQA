An independent unit test suite for `org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream` using JUnit 3 (compatible with the build configuration):

```java
package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class CpioArchiveOutputStreamTest extends TestCase {

    public void testConstructorDefaultFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("file1");
        entry.setFileSize(0);
        out.putNextEntry(entry);
        out.close();

        byte[] data = baos.toByteArray();
        assertTrue(data.length > 6);
        String magic = new String(data, 0, 6);
        assertEquals(CpioConstants.MAGIC_NEW, magic);
    }

    public void testConstructorInvalidFormatThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new CpioArchiveOutputStream(baos, (short) 999);
            fail("Expected IllegalArgumentException for unknown format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    public void testConstructorValidFormats() throws IOException {
        short[] formats = new short[] {
            CpioConstants.FORMAT_NEW,
            CpioConstants.FORMAT_NEW_CRC,
            CpioConstants.FORMAT_OLD_ASCII,
            CpioConstants.FORMAT_OLD_BINARY
        };
        for (int i = 0; i < formats.length; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, formats[i]);
            out.finish();
            out.close();
            assertTrue(baos.toByteArray().length > 0);
        }
    }

    public void testPutNextEntryNullNameThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        try {
            out.putNextEntry(entry);
            fail("Expected NullPointerException when entry name is null");
        } catch (NullPointerException e) {
            // Expected
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    public void testPutNextEntryDuplicateNameThrowsIOException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("test.txt");
        entry1.setFileSize(0);
        out.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("test.txt");
        entry2.setFileSize(0);
        try {
            out.putNextEntry(entry2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("duplicate entry") != -1);
        }
    }

    public void testPutNextEntryClosesPreviousEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("file1");
        entry1.setFileSize(3);
        out.putNextEntry(entry1);
        out.write(new byte[] { 1, 2, 3 });

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("file2");
        entry2.setFileSize(0);
        out.putNextEntry(entry2);

        out.closeArchiveEntry();
        out.finish();
        out.close();
    }

    public void testPutNextEntryFailsIfPreviousEntryUnderwritten() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("file1");
        entry1.setFileSize(5);
        out.putNextEntry(entry1);
        out.write(new byte[] { 1, 2 });

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("file2");
        entry2.setFileSize(0);
        try {
            out.putNextEntry(entry2);
            fail("Expected IOException due to previous entry size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("invalid entry size") != -1);
        }
    }

    public void testPutArchiveEntryDelegation() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        ArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        ((CpioArchiveEntry) entry).setName("archiveEntryTest");
        ((CpioArchiveEntry) entry).setFileSize(0);

        out.putArchiveEntry(entry);
        out.closeArchiveEntry();
        out.close();
    }

    public void testWriteWithoutEntryThrowsIOException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.write(new byte[] { 1, 2, 3 }, 0, 3);
            fail("Expected IOException when writing without current entry");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWriteZeroBytesWithoutEntrySucceeds() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.write(new