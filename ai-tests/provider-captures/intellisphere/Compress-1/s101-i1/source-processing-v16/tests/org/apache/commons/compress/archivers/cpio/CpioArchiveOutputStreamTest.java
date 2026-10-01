package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Deterministic unit tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    public void testConstructorWithInvalidFormatThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new CpioArchiveOutputStream(baos, (short) 999);
            fail("Expected IllegalArgumentException for unknown format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    public void testDefaultConstructorUsesNewFormat() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        byte[] data = baos.toByteArray();
        // FORMAT_NEW magic number is "070701"
        String magic = new String(data, 0, 6);
        assertEquals(CpioConstants.MAGIC_NEW, magic);
    }

    public void testFormatNewCrcCreation() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("crc.txt");
        byte[] content = new byte[] { 'A', 'B', 'C' };
        entry.setSize(content.length);
        long expectedCrc = ('A' & 0xFF) + ('B' & 0xFF) + ('C' & 0xFF);
        entry.setChksum(expectedCrc);

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        byte[] data = baos.toByteArray();
        String magic = new String(data, 0, 6);
        assertEquals(CpioConstants.MAGIC_NEW_CRC, magic);
    }

    public void testFormatNewCrcMismatchThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("badcrc.txt");
        byte[] content = new byte[] { 'A' };
        entry.setSize(content.length);
        entry.setChksum(999); // incorrect checksum

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        try {
            out.closeArchiveEntry();
            fail("Expected CRC Error IOException");
        } catch (IOException e) {
            assertEquals("CRC Error", e.getMessage());
        }
    }

    public void testFormatOldAsciiCreation() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("oldascii.txt");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        byte[] data = baos.toByteArray();
        String magic = new String(data, 0, 6);
        assertEquals(CpioConstants.MAGIC_OLD_ASCII, magic);
    }

    public void testFormatOldBinaryCreation() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_BINARY);
        entry.setName("oldbin.txt");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        byte[] data = baos.toByteArray();
        assertTrue("Output should contain at least magic number bytes", data.length >= 2);
    }

    public void testPutDuplicateEntryThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry1 = new CpioArchiveEntry("dup.txt");
        entry1.setSize(0);
        out.putNextEntry(entry1);
        out.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("dup.txt");
        entry2.setSize(0);
        try {
            out.putNextEntry(entry2);
            fail("Expected IOException for duplicate entry");
        } catch (IOException e) {
            assertEquals("duplicate entry: dup.txt", e.getMessage());
        }
    }

    public void testWriteWithoutActiveEntryThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.write(new byte[] { 1, 2, 3 }, 0, 3);
            fail("Expected IOException when no entry is active");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWriteLengthZeroDoesNotThrowException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        // Writing 0 bytes with no current entry should return cleanly
        out.write(new byte[10], 0, 0);
        out.close();
    }

    public void testWriteOutOfBoundsThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        byte[] b = new byte[10];

        try {
            out.write(b, -1, 5);
            fail("Expected IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            out.write(b, 0, -1);
            fail("Expected IndexOutOfBoundsException for negative length");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            out.write(b, 5, 6);
            fail("Expected IndexOutOfBoundsException for offset + length > array size");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testWritePastEndOfEntryThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry("limit.txt");
        entry.setSize(2);
        out.putNextEntry(entry);
        out.write(new byte[] { 1, 2 }, 0, 2);

        try {
            out.write(new byte[] { 3 }, 0, 1);
            fail("Expected IOException for writing past end of entry");
        } catch (IOException e) {
            assertEquals("attempt to write past end of STORED entry", e.getMessage());
        }
    }

    public void testCloseArchiveEntryWithIncompleteDataThrowsException() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry("undersized.txt");
        entry.setSize(10);
        out.putNextEntry(entry);
        out.write(new byte[] { 1, 2, 3 }, 0, 3);

        try {
            out.closeArchiveEntry();
            fail("Expected IOException for invalid entry size");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("invalid entry size (expected 10 but got 3 bytes)"));
        }
    }

    public void testOperationsFailAfterStreamClosed() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.close();

        try {
            out.putNextEntry(new CpioArchiveEntry("afterClose.txt"));
            fail("Expected IOException on putNextEntry after close");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.write(new byte[] { 1 }, 0, 1);
            fail("Expected IOException on write after close");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.closeArchiveEntry();
            fail("Expected IOException on closeArchiveEntry after close");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.finish();
            fail("Expected IOException on finish after close");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testPutArchiveEntryDelegatesToPutNextEntry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        CpioArchiveEntry entry = new CpioArchiveEntry("archiveEntry.txt");
        entry.setSize(4);
        out.putArchiveEntry(entry);
        out.write(new byte[] { 1, 2, 3, 4 }, 0, 4);
        out.closeArchiveEntry();
        out.finish();
        out.close();
        assertTrue(baos.toByteArray().length > 0);
    }

    public void testPutNextEntryClosesPreviousOpenEntry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("file1.txt");
        entry1.setSize(2);
        out.putNextEntry(entry1);
        out.write(new byte[] { 1, 2 }, 0, 2);

        // Putting file2 should automatically close file1 successfully
        CpioArchiveEntry entry2 = new CpioArchiveEntry("file2.txt");
        entry2.setSize(0);
        out.putNextEntry(entry2);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        assertTrue(baos.toByteArray().length > 0);
    }

    public void testWriteSingleByte() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.write(42);
        byte[] written = baos.toByteArray();
        assertEquals(1, written.length);
        assertEquals(42, written[0]);
    }

    public void testFinishAppendsTrailerEntry() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();

        byte[] data = baos.toByteArray();
        String outputStr = new String(data);
        assertTrue("Stream must contain TRAILER!!! header", outputStr.indexOf("TRAILER!!!") != -1);
    }

    public void testMultipleCloseCallsAreSafe() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.close();
        out.close(); // Should not throw an exception
    }
}
