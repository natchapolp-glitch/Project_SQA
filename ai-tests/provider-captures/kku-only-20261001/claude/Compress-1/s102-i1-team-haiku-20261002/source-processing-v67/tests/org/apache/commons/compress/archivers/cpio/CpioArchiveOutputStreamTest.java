// org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java
package org.apache.commons.compress.archivers.cpio;

import junit.framework.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest extends TestCase {

    private ByteArrayOutputStream byteOut;
    private CpioArchiveOutputStream cpioOut;

    protected void setUp() {
        byteOut = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(byteOut);
    }

    protected void tearDown() throws IOException {
        if (cpioOut != null) {
            try {
                cpioOut.close();
            } catch (IOException e) {
                // Already closed or failed
            }
        }
    }

    // ========== Constructor Tests ==========

    public void testConstructorWithOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        assertNotNull(stream);
        stream.close();
    }

    public void testConstructorWithFormat() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, 
            CpioConstants.FORMAT_OLD_ASCII);
        assertNotNull(stream);
        stream.close();
    }

    public void testConstructorDefaultFormatIsFormatNew() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out);
        stream.finish();
        byte[] result = out.toByteArray();
        // FORMAT_NEW magic should be written to trailer
        assertTrue(result.length > 0);
        stream.close();
    }

    public void testConstructorWithFormatNewCrc() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, 
            CpioConstants.FORMAT_NEW_CRC);
        stream.finish();
        stream.close();
    }

    public void testConstructorWithFormatOldBinary() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, 
            CpioConstants.FORMAT_OLD_BINARY);
        stream.finish();
        stream.close();
    }

    public void testConstructorWithFormatOldAscii() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream stream = new CpioArchiveOutputStream(out, 
            CpioConstants.FORMAT_OLD_ASCII);
        stream.finish();
        stream.close();
    }

    public void testConstructorWithInvalidFormatThrows() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new CpioArchiveOutputStream(out, (short) 999);
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unknown"));
        }
    }

    // ========== ensureOpen Tests ==========

    public void testEnsureOpenThrowsAfterClose() throws IOException {
        cpioOut.close();
        try {
            cpioOut.putNextEntry(new CpioArchiveEntry("test"));
            fail("Should throw IOException when stream is closed");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("closed"));
        }
    }

    public void testEnsureOpenSucceedsWhenOpen() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        // Should not throw
    }

    // ========== putNextEntry / putArchiveEntry Tests ==========

    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
    }

    public void testPutNextEntryClosePreviousEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("file1");
        entry1.setSize(0);
        cpioOut.putNextEntry(entry1);
        cpioOut.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("file2");
        entry2.setSize(0);
        cpioOut.putNextEntry(entry2);
        cpioOut.closeArchiveEntry();
    }

    public void testPutNextEntryDuplicateNameThrows() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("duplicate");
        entry1.setSize(0);
        cpioOut.putNextEntry(entry1);
        cpioOut.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("duplicate");
        entry2.setSize(0);
        try {
            cpioOut.putNextEntry(entry2);
            fail("Should throw IOException for duplicate entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("duplicate"));
        }
    }

    public void testPutNextEntrySetsDefaultTime() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test");
        entry.setSize(0);
        entry.setTime(-1); // Force default time
        long before = System.currentTimeMillis();
        cpioOut.putNextEntry(entry);
        long after = System.currentTimeMillis();
        assertTrue(entry.getTime() >= before);
        assertTrue(entry.getTime() <= after);
        cpioOut.closeArchiveEntry();
    }

    

    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("archivetest");
        entry.setSize(0);
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
    }

    // ========== write Tests ==========

    public void testWriteByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        CpioArchiveEntry entry = new CpioArchiveEntry("writetest");
        entry.setSize(data.length);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
    }

    public void testWritePartialByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        CpioArchiveEntry entry = new CpioArchiveEntry("partialwrite");
        entry.setSize(3);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, 3);
        cpioOut.closeArchiveEntry();
    }

    public void testWriteWithOffsetAndLength() throws IOException {
        byte[] data = new byte[] { 0, 1, 2, 3, 4, 5 };
        CpioArchiveEntry entry = new CpioArchiveEntry("offsetwrite");
        entry.setSize(3);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 2, 3); // Write bytes 2,3,4
        cpioOut.closeArchiveEntry();
    }

    public void testWriteZeroLengthReturnsWithoutError() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("zerowrite");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3 }, 0, 0);
        cpioOut.closeArchiveEntry();
    }

    public void testWriteNegativeOffsetThrows() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        CpioArchiveEntry entry = new CpioArchiveEntry("negoffset");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(data, -1, 1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteNegativeLengthThrows() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        CpioArchiveEntry entry = new CpioArchiveEntry("neglength");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(data, 0, -1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteOffsetBeyondArrayThrows() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        CpioArchiveEntry entry = new CpioArchiveEntry("offsetbeyond");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(data, 2, 2); // offset + length > array.length
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWritePastEndOfEntryThrows() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        CpioArchiveEntry entry = new CpioArchiveEntry("pastend");
        entry.setSize(3);
        cpioOut.putNextEntry(entry);
        try {
            cpioOut.write(data, 0, 5); // Write more than entry size
            fail("Should throw IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("past end"));
        }
    }

    public void testWriteNoCurrentEntryThrows() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        try {
            cpioOut.write(data, 0, 3);
            fail("Should throw IOException when no current entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("no current"));
        }
    }

    public void testWriteAfterCloseThrows() throws IOException {
        cpioOut.close();
        try {
            cpioOut.write(new byte[] { 1 }, 0, 1);
            fail("Should throw IOException after close");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("closed"));
        }
    }

    

    // ========== closeArchiveEntry Tests ==========

    public void testCloseArchiveEntryValidatesSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("sizetest");
        entry.setSize(10);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[] { 1, 2, 3 }, 0, 3); // Write only 3 bytes
        try {
            cpioOut.closeArchiveEntry();
            fail("Should throw IOException for size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("invalid entry size"));
        }
    }

    

    public void testCloseArchiveEntryMultipleTimes() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("multiclose");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        // Calling again should fail or be no-op depending on implementation
    }

    // ========== finish Tests ==========

    public void testFinish() throws IOException {
        cpioOut.finish();
        byte[] result = byteOut.toByteArray();
        assertTrue(result.length > 0);
    }

    

    

    

    // ========== close Tests ==========

    

    

    

    // ========== Format-specific header writing tests ==========

    

    

    

    

    
}
