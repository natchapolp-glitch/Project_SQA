package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;
import org.apache.commons.compress.archivers.ArchiveEntry;

/**
 * Regression tests for CpioArchiveOutputStream covering normal cases, boundaries,
 * invalid inputs, exception paths and branches.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    private ByteArrayOutputStream baos;
    private CpioArchiveOutputStream cos;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        baos = new ByteArrayOutputStream();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (cos != null) {
            try {
                cos.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    // ============== Constructor Tests ==============

    public void testConstructorWithOutputStream() throws IOException {
        cos = new CpioArchiveOutputStream(baos);
        assertNotNull(cos);
        cos.close();
        assertTrue(baos.toByteArray().length > 0);
    }

    public void testConstructorWithOutputStreamAndFormatNew() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        assertNotNull(cos);
        cos.finish();
        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0);
    }

    public void testConstructorWithOutputStreamAndFormatNewCrc() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        assertNotNull(cos);
        cos.finish();
        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0);
    }

    public void testConstructorWithOutputStreamAndFormatOldAscii() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);
        assertNotNull(cos);
        cos.finish();
        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0);
    }

    public void testConstructorWithOutputStreamAndFormatOldBinary() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_BINARY);
        assertNotNull(cos);
        cos.finish();
        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0);
    }

    public void testConstructorWithInvalidFormat() {
        try {
            cos = new CpioArchiveOutputStream(baos, (short) 999);
            fail("Should throw IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    // ============== ensureOpen Tests ==============

    public void testEnsureOpenOnClosedStream() throws IOException {
        cos = new CpioArchiveOutputStream(baos);
        cos.close();
        try {
            CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
            entry.setName("test");
            entry.setSize(0);
            cos.putNextEntry(entry);
            fail("Should throw IOException for closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testEnsureOpenOnWriteAfterClose() throws IOException {
        cos = new CpioArchiveOutputStream(baos);
        cos.close();
        try {
            cos.write(new byte[] { 1 }, 0, 1);
            fail("Should throw IOException for closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testEnsureOpenOnFinishAfterClose() throws IOException {
        cos = new CpioArchiveOutputStream(baos);
        cos.close();
        try {
            cos.finish();
            fail("Should throw IOException for closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    // ============== setFormat Tests ==============

    public void testSetFormatViaConstructor() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("test");
        entry.setSize(0);
        cos.putNextEntry(entry);
        byte[] data = baos.toByteArray();
        String header = new String(data, 0, Math.min(6, data.length));
        assertTrue(header.length() > 0);
    }

    

    // ============== putNextEntry / putArchiveEntry Tests ==============

    public void testPutNextEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("testfile");
        entry.setSize(5);
        cos.putNextEntry(entry);
        assertNotNull(baos.toByteArray());
    }

    public void testPutNextEntryWithoutTime() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("testfile");
        entry.setSize(0);
        entry.setTime(-1);
        cos.putNextEntry(entry);
        assertTrue(entry.getTime() > 0);
    }

    public void testPutNextEntryDuplicateName() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("duplicate");
        entry1.setSize(0);
        cos.putNextEntry(entry1);
        cos.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("duplicate");
        entry2.setSize(0);
        try {
            cos.putNextEntry(entry2);
            fail("Should throw IOException for duplicate entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("duplicate entry"));
        }
    }

    public void testPutArchiveEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        ((CpioArchiveEntry) entry).setName("test");
        ((CpioArchiveEntry) entry).setSize(0);
        cos.putArchiveEntry(entry);
        assertNotNull(baos.toByteArray());
    }

    public void testPutNextEntryClosesCurrentEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("first");
        entry1.setSize(0);
        cos.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("second");
        entry2.setSize(0);
        cos.putNextEntry(entry2); // Should close entry1
        assertNotNull(baos.toByteArray());
    }

    // ============== write Tests ==============

    public void testWriteByteArraySingleByte() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(1);
        cos.putNextEntry(entry);
        cos.write(new byte[] { 65 }, 0, 1);
        assertNotNull(baos.toByteArray());
    }

    public void testWriteByteArrayMultipleBytes() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(5);
        cos.putNextEntry(entry);
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        cos.write(data, 0, 5);
        assertNotNull(baos.toByteArray());
    }

    public void testWriteByteArrayWithOffset() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(3);
        cos.putNextEntry(entry);
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        cos.write(data, 2, 3);
        assertNotNull(baos.toByteArray());
    }

    public void testWriteInt() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        cos.write(65);
        assertEquals(1, baos.toByteArray().length);
    }

    public void testWriteZeroLength() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(0);
        cos.putNextEntry(entry);
        cos.write(new byte[] { 1 }, 0, 0);
        assertNotNull(baos.toByteArray());
    }

    public void testWriteWithoutEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        try {
            cos.write(new byte[] { 1 }, 0, 1);
            fail("Should throw IOException when no current entry");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWritePastEndOfEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(3);
        cos.putNextEntry(entry);
        try {
            cos.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
            fail("Should throw IOException when writing past end");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("past end of STORED entry"));
        }
    }

    public void testWriteNegativeOffset() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(5);
        cos.putNextEntry(entry);
        try {
            cos.write(new byte[] { 1, 2, 3 }, -1, 1);
            fail("Should throw IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteNegativeLength() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(5);
        cos.putNextEntry(entry);
        try {
            cos.write(new byte[] { 1, 2, 3 }, 0, -1);
            fail("Should throw IndexOutOfBoundsException for negative length");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    public void testWriteOffsetPastArray() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(5);
        cos.putNextEntry(entry);
        try {
            cos.write(new byte[] { 1, 2, 3 }, 5, 1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // ============== closeArchiveEntry Tests ==============

    public void testCloseArchiveEntry() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(0);
        cos.putNextEntry(entry);
        cos.closeArchiveEntry();
        assertNotNull(baos.toByteArray());
    }

    public void testCloseArchiveEntrySizeMismatch() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(10);
        cos.putNextEntry(entry);
        try {
            cos.closeArchiveEntry();
            fail("Should throw IOException for size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("invalid entry size"));
        }
    }

    public void testCloseArchiveEntryCRCMismatch() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("test");
        entry.setSize(1);
        entry.setChksum(100);
        cos.putNextEntry(entry);
        cos.write(new byte[] { 1 }, 0, 1);
        try {
            cos.closeArchiveEntry();
            fail("Should throw IOException for CRC mismatch");
        } catch (IOException e) {
            assertEquals("CRC Error", e.getMessage());
        }
    }

    // ============== finish Tests ==============

    public void testFinish() throws IOException {
        cos = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test");
        entry.setSize(0);
        cos.putNextEntry(entry);
        cos.finish();
        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0);
    }

    

    

    // ============== close Tests ==============

    

    

    

    // ============== Format-Specific Header Tests ==============

    

    

    

    

    // ============== CRC Calculation Tests ==============

    

    // ============== writeNewEntry Tests ==============

    

    // ============== Complete Workflow Tests ==============

    

    

    
}
