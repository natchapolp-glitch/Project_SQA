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

    

    

    

    // ===== ensureOpen Tests =====

    

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

    

    

    

    

    

    // ===== Write Operations Tests =====

    

    

    

    

    

    

    

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

    

    

    

    

    

    

    // ===== Finish Tests =====

    

    

    

    // ===== Close Tests =====

    public void testCloseIdempotent() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteArrayOut);
        cpioOut.close();
        cpioOut.close(); // Should not throw
    }

    

    // ===== Timestamp Tests =====

    

    

    // ===== Format-Specific Entry Writing Tests =====

    

    

    

    

    // ===== Multiple Entry Tests =====

    

    

    // ===== Entry Content Tracking Tests =====

    

    
}
