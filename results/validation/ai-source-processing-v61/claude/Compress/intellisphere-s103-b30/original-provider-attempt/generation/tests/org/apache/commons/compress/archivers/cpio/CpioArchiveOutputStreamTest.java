package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * Regression tests for CpioArchiveOutputStream.
 * Tests cover constructor initialization, format validation, entry operations,
 * data writing, padding, and stream lifecycle management.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    private ByteArrayOutputStream byteArrayOut;
    private CpioArchiveOutputStream cpioOut;

    protected void setUp() throws Exception {
        super.setUp();
        byteArrayOut = new ByteArrayOutputStream();
    }

    protected void tearDown() throws Exception {
        if (cpioOut != null) {
            try {
                cpioOut.close();
            } catch (IOException e) {
                // Ignore during teardown
            }
        }
        super.tearDown();
    }

    // ===== Constructor Tests =====

    public void testConstructorWithOutputStreamDefaultFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        assertNotNull(cpioOut);
        cpioOut.close();
    }

    public void testConstructorWithOutputStreamAndNewFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_NEW);
        assertNotNull(cpioOut);
        cpioOut.close();
    }

    public void testConstructorWithOutputStreamAndNewCrcFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_NEW_CRC);
        assertNotNull(cpioOut);
        cpioOut.close();
    }

    public void testConstructorWithOutputStreamAndOldAsciiFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_ASCII);
        assertNotNull(cpioOut);
        cpioOut.close();
    }

    public void testConstructorWithOutputStreamAndOldBinaryFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_BINARY);
        assertNotNull(cpioOut);
        cpioOut.close();
    }

    public void testConstructorWithInvalidFormatThrowsException() {
        try {
            cpioOut = new CpioArchiveOutputStream(byteArrayOut, (short) 999);
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    // ===== Format Validation Tests =====

    public void testSetFormatNewFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.close();
    }

    public void testSetFormatOldBinaryFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.close();
    }

    public void testSetFormatOldAsciiFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.close();
    }

    // ===== ensureOpen Tests =====

    public void testEnsureOpenThrowsAfterClose() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        cpioOut.close();
        try {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("test");
            cpioOut.putNextEntry(entry);
            fail("Expected IOException when stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testEnsureOpenThrowsOnFinishAfterClose() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        cpioOut.close();
        try {
            cpioOut.finish();
            fail("Expected IOException when stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testEnsureOpenThrowsOnWriteAfterClose() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        cpioOut.close();
        try {
            cpioOut.write(new byte[1], 0, 1);
            fail("Expected IOException when stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    // ===== Entry Lifecycle Tests =====

    public void testPutNextEntryWithValidEntry() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.close();
    }

    public void testPutNextEntryWithDuplicateNameThrowsException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("duplicate");
        entry1.setSize(0);
        cpioOut.putNextEntry(entry1);
        cpioOut.closeArchiveEntry();

        try {
            CpioArchiveEntry entry2 = new CpioArchiveEntry();
            entry2.setName("duplicate");
            entry2.setSize(0);
            cpioOut.putNextEntry(entry2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException e) {
            assertEquals("duplicate entry: duplicate", e.getMessage());
        }
    }

    public void testPutNextEntryWithoutFormatUseDefault() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test");
        entry.setSize(0);
        entry.setFormat((short) -1);
        cpioOut.putNextEntry(entry);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, entry.getFormat());
        cpioOut.close();
    }

    public void testPutNextEntryClosePreviousEntry() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("first");
        entry1.setSize(0);
        cpioOut.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("second");
        entry2.setSize(0);
        cpioOut.putNextEntry(entry2);
        cpioOut.close();
    }

    public void testPutArchiveEntryDelegates() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(0);
        cpioOut.putArchiveEntry(entry);
        cpioOut.close();
    }

    // ===== Write Operations Tests =====

    public void testWriteByteArrayValidData() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        cpioOut.close();
    }

    public void testWriteByteArrayWithOffset() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(3);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 0, 1, 2, 3, 4 }, 1, 3);
        cpioOut.close();
    }

    public void testWriteByteArrayZeroLength() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3 }, 0, 0);
        cpioOut.close();
    }

    public void testWriteByteArrayNegativeOffsetThrowsException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(new byte[] { 1, 2, 3 }, -1, 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteByteArrayNegativeLengthThrowsException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(new byte[] { 1, 2, 3 }, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteByteArrayOffsetOutOfBoundsThrowsException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(new byte[] { 1, 2, 3 }, 2, 2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteByteArrayPastEntrySize() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(3);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
            fail("Expected IOException when writing past entry size");
        } catch (IOException e) {
            assertEquals("attempt to write past end of STORED entry", e.getMessage());
        }
    }

    public void testWriteByteArrayNoCurrentEntry() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        try {
            cpioOut.write(new byte[] { 1, 2, 3 }, 0, 3);
            fail("Expected IOException when no current entry");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWriteSingleByte() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        cpioOut.write(42);
        cpioOut.close();
    }

    // ===== closeArchiveEntry Tests =====

    public void testCloseArchiveEntryValidSize() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
    }

    public void testCloseArchiveEntryInvalidSize() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        try {
            cpioOut.closeArchiveEntry();
            fail("Expected IOException for size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("invalid entry size"));
        }
    }

    public void testCloseArchiveEntryWithNewFormatPadding() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
    }

    public void testCloseArchiveEntryWithOldBinaryFormatPadding() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut, CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
    }

    

    

    // ===== Finish Tests =====

    

    

    

    // ===== Close Tests =====

    

    

    // ===== Timestamp Tests =====

    

    

    // ===== Format-Specific Entry Writing Tests =====

    

    

    

    

    // ===== Multiple Entry Tests =====

    

    

    // ===== Entry Content Tracking Tests =====

    

    
}
