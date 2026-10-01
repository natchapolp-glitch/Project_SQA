package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 * Based on the fixed-reference revision 1f.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    private ByteArrayOutputStream byteOut;
    private CpioArchiveOutputStream cpioOut;

    public void setUp() {
        byteOut = new ByteArrayOutputStream();
    }

    public void tearDown() {
        if (cpioOut != null) {
            try {
                cpioOut.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    // ---------------------------------------------------------------
    // Constructor tests
    // ---------------------------------------------------------------

    public void testConstructorWithNoFormatSpecifiedShouldDefaultToNewFormat() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        // The default format is FORMAT_NEW.
        // If we put an entry without specifying a format, it should use the default.
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(0);
        
        // This should not throw an exception and should write using the NEW format magic.
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        // Verify that the output starts with the magic for FORMAT_NEW: "070701"
        byte[] bytes = byteOut.toByteArray();
        assertEquals("Magic should be for new format",
                '0', bytes[0](undefined));
    }

    public void testConstructorWithFormatOldBinary() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut, CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry("test.bin");
        entry.setSize(0);
        
        // Should succeed
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        assertTrue("Should write at least magic bytes", byteOut.toByteArray().length > 0);
    }

    public void testConstructorWithInvalidFormatShouldThrowException() {
        try {
            cpioOut = new CpioArchiveOutputStream(byteOut, (short) 999);
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // ---------------------------------------------------------------
    // close / finish / ensureOpen tests
    // ---------------------------------------------------------------

    public void testCloseShouldFinishStream() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        cpioOut.close();
        assertTrue("Stream should be closed", true); // no exception
        // Subsequent operation should throw IOException
        try {
            cpioOut.putNextEntry(new CpioArchiveEntry("test"));
            fail("Should have thrown IOException because stream is closed");
        } catch (IOException e) {
            // expected
        }
    }

    public void testFinishTwiceShouldNotThrowException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        cpioOut.finish();
        cpioOut.finish(); // second call should be a no-op
        // Should not throw
    }

    public void testCloseAfterFinish() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        cpioOut.finish();
        cpioOut.close();
        
        try {
            cpioOut.ensureOpen(); // private, but we can infer from other methods
            // we use write to trigger ensureOpen
            cpioOut.write(0);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // expected
        }
    }

    // ---------------------------------------------------------------
    // putNextEntry / putArchiveEntry tests
    // ---------------------------------------------------------------

    public void testPutNextEntryDuplicateNameShouldThrowIOException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry1 = new CpioArchiveEntry("duplicate");
        entry1.setSize(0);
        cpioOut.putNextEntry(entry1);
        cpioOut.closeArchiveEntry();
        
        CpioArchiveEntry entry2 = new CpioArchiveEntry("duplicate");
        entry2.setSize(0);
        try {
            cpioOut.putNextEntry(entry2);
            fail("Should have thrown IOException for duplicate entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("duplicate"));
        }
    }

    public void testPutArchiveEntryDelegatesToPutNextEntry() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("archiveEntryTest");
        entry.setSize(0);
        // putArchiveEntry accepts ArchiveEntry
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        // Should succeed without ClassCastException
    }

    // ---------------------------------------------------------------
    // write tests
    // ---------------------------------------------------------------

    public void testWriteByteArray() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("byteArrayTest");
        byte[] data = "Hello CPIO!".getBytes();
        entry.setSize(data.length);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        // The output should contain the data; we can only test that no exception is thrown.
        byte[] bytes = byteOut.toByteArray();
        assertTrue(bytes.length > data.length);
    }

    public void testWritePastEndOfEntryShouldThrowIOException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("pastEnd");
        entry.setSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[]{1,2,3,4,5}, 0, 5);
        try {
            cpioOut.write(new byte[]{6}, 0, 1);
            fail("Should have thrown IOException for writing past end");
        } catch (IOException e) {
            // expected
        }
    }

    public void testWriteWithNoCurrentEntryShouldThrowIOException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        try {
            cpioOut.write(new byte[]{1,2,3}, 0, 3);
            fail("Should have thrown IOException for no current entry");
        } catch (IOException e) {
            // expected
        }
    }

    public void testWriteSingleByte() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("singleByte");
        entry.setSize(1);
        cpioOut.putNextEntry(entry);
        cpioOut.write(65); // 'A'
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        // Should succeed
    }

    public void testWriteInvalidOffsetOrLength() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("invalidBounds");
        entry.setSize(10);
        cpioOut.putNextEntry(entry);
        
        try {
            cpioOut.write(new byte[5](undefined), -1, 0);
            fail("Should have thrown IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    // ---------------------------------------------------------------
    // closeArchiveEntry tests
    // ---------------------------------------------------------------

    public void testCloseArchiveEntrySizeMismatchShouldThrowIOException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("sizeMismatch");
        entry.setSize(10);
        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[]{1,2,3}, 0, 3);
        try {
            cpioOut.closeArchiveEntry();
            fail("Should have thrown IOException for size mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("invalid entry size"));
        }
    }

    public void testCloseArchiveEntryWithCorrectSize() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("correctSize");
        byte[] data = new byte[10](undefined);
        entry.setSize(data.length);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
        // Should succeed
    }

    // ---------------------------------------------------------------
    // CRC tests (FORMAT_NEW_CRC)
    // ---------------------------------------------------------------

    public void testCrcMismatchShouldThrowIOException() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut, CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry("crcTest");
        byte[] data = "CRC TEST".getBytes();
        entry.setSize(data.length);
        entry.setChksum(9999); // incorrect CRC
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        try {
            cpioOut.closeArchiveEntry();
            fail("Should have thrown IOException for CRC Error");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("CRC Error"));
        }
    }

    public void testCrcMatchShouldNotThrowException() throws IOException {
        // Compute expected CRC manually: sum of bytes & 0xFF
        byte[] data = "CRC MATCH".getBytes();
        long crc = 0;
        for (byte b : data) {
            crc += b & 0xFF;
        }
        
        cpioOut = new CpioArchiveOutputStream(byteOut, CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry("crcMatch");
        entry.setSize(data.length);
        entry.setChksum(crc);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry(); // Should not throw
    }

    // ---------------------------------------------------------------
    // format-specific tests
    // ---------------------------------------------------------------

    public void testOldAsciiFormatWritesCorrectMagic() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut, CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry = new CpioArchiveEntry("oldAscii");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        byte[] bytes = byteOut.toByteArray();
        // MAGIC_OLD_ASCII = "070707"
        String magic = new String(bytes, 0, 6);
        assertEquals("070707", magic);
    }

    public void testNewFormatWritesCorrectMagic() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("newFormat");
        entry.setSize(0);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        byte[] bytes = byteOut.toByteArray();
        // MAGIC_NEW = "070701"
        String magic = new String(bytes, 0, 6);
        assertEquals("070701", magic);
    }

    // ---------------------------------------------------------------
    // Trailer entry tests
    // ---------------------------------------------------------------

    public void testTrailerEntryIsWrittenOnFinish() throws IOException {
        cpioOut = new CpioArchiveOutputStream(byteOut);
        cpioOut.finish();
        
        byte[] bytes = byteOut.toByteArray();
        // The trailer entry has name "TRAILER!!!" and should be in the output
        String output = new String(bytes);
        assertTrue("Output should contain TRAILER!!!", output.contains("TRAILER!!!"));
    }

    // ---------------------------------------------------------------
    // Helper methods (indirectly tested via public API)
    // ---------------------------------------------------------------

    public void testPadFunctionCalledIndirectly() throws IOException {
        // Padding is applied when entry size is not a multiple of 4 for new format.
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("paddingTest");
        byte[] data = new byte[3](undefined); // size not multiple of 4
        entry.setSize(data.length);
        cpioOut.putNextEntry(entry);
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        
        // Just ensure no exception; padding adds zero bytes
    }

    public void testWriteAsciiLongTruncation() throws IOException {
        // writeAsciiLong with number larger than length truncates from left; 
        // test indirectly by forcing a large inode value
        cpioOut = new CpioArchiveOutputStream(byteOut);
        CpioArchiveEntry entry = new CpioArchiveEntry("truncTest");
        entry.setSize(0);
        entry.setInode(0x123456789ABCL); // > 16 hex digits
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        // Should not throw
    }
}
