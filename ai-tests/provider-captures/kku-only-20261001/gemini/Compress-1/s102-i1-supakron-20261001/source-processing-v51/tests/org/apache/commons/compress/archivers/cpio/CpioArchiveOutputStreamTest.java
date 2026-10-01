package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

public class CpioArchiveOutputStreamTest extends TestCase {

    public void testConstructWithInvalidFormatThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new CpioArchiveOutputStream(baos, (short) 999);
            fail("Expected IllegalArgumentException for unknown header format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    public void testConstructWithValidFormats() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);
        new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);
        new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);
        new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_BINARY);
    }

    public void testDefaultConstructorUsesFormatNew() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("entry1");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();

        byte[] data = baos.toByteArray();
        assertTrue("Output should start with MAGIC_NEW",
                new String(data, 0, 6).equals(CpioConstants.MAGIC_NEW));
    }

    public void testWriteWithoutEntryThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.write(new byte[]{1, 2, 3}, 0, 3);
            fail("Expected IOException when writing without an active entry");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWriteIntWithoutEntryThrowsException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        try {
            out.write(42);
            // write(int) writes directly to the underlying stream
            // so verifying no exception is thrown
        } catch (IOException e) {
            fail("Direct write(int) should write byte: " + e.getMessage());
        }
    }

    public void testWriteZeroLengthDoesNothing() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("zeroLength");
        entry.setSize(1);
        out.putNextEntry(entry);

        out.write(new byte[]{1, 2, 3}, 0, 0);
        out.write(new byte[]{1}, 0, 1);
        out.closeArchiveEntry();
        out.finish();
    }

    public void testWriteInvalidBoundsThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("boundsTest");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] b = new byte[5];
        try {
            out.write(b, -1, 3);
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
            out.write(b, 3, 3);
            fail("Expected IndexOutOfBoundsException for off + len > b.length");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testWritePastEndOfEntryThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("tooLong");
        entry.setSize(2);
        out.putNextEntry(entry);

        byte[] data = new byte[]{1, 2, 3};
        try {
            out.write(data, 0, 3);
            fail("Expected IOException when writing past declared entry size");
        } catch (IOException e) {
            assertEquals("attempt to write past end of STORED entry", e.getMessage());
        }
    }

    public void testCloseArchiveEntryWithIncompleteSizeThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("incomplete");
        entry.setSize(5);
        out.putNextEntry(entry);
        out.write(new byte[]{1, 2}, 0, 2);

        try {
            out.closeArchiveEntry();
            fail("Expected IOException when entry size is smaller than expected");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("invalid entry size"));
        }
    }

    public void testDuplicateEntryNameThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("sameName");
        entry1.setSize(0);
        out.putNextEntry(entry1);
        out.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("sameName");
        entry2.setSize(0);
        try {
            out.putNextEntry(entry2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException e) {
            assertEquals("duplicate entry: sameName", e.getMessage());
        }
    }

    public void testPutNextEntryClosesPreviousOpenEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("file1");
        entry1.setSize(3);
        out.putNextEntry(entry1);
        out.write(new byte[]{'a', 'b', 'c'}, 0, 3);

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("file2");
        entry2.setSize(0);
        out.putNextEntry(entry2);
        out.closeArchiveEntry();
        out.finish();
    }

    public void testPutArchiveEntryDelegatesToPutNextEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("viaArchiveEntry");
        entry.setSize(0);
        out.putArchiveEntry(entry);
        out.closeArchiveEntry();
        out.finish();
    }

    public void testEntryTimeAndFormatDefaultedIfNotSet() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("timeFormatDefault");
        entry.setTime(-1);
        entry.setSize(0);

        out.putNextEntry(entry);
        out.closeArchiveEntry();
        out.finish();

        assertTrue(entry.getTime() > 0);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, entry.getFormat());
    }

    public void testCrcVerificationInNewCrcFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("crcTest");
        byte[] content = new byte[]{1, 2, 3, 4};
        entry.setSize(content.length);

        long expectedCrc = 0;
        for (int i = 0; i < content.length; i++) {
            expectedCrc += content[i] & 0xFF;
        }
        entry.setChksum(expectedCrc);

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        out.closeArchiveEntry();
        out.finish();
    }

    public void testCrcMismatchThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("badCrcTest");
        byte[] content = new byte[]{1, 2, 3, 4};
        entry.setSize(content.length);
        entry.setChksum(99999);

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        try {
            out.closeArchiveEntry();
            fail("Expected IOException due to CRC Error");
        } catch (IOException e) {
            assertEquals("CRC Error", e.getMessage());
        }
    }

    public void testWriteOldAsciiFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("oldAsciiFile");
        byte[] content = "Hello Ascii".getBytes();
        entry.setSize(content.length);

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        out.closeArchiveEntry();
        out.finish();

        byte[] output = baos.toByteArray();
        assertTrue("Output should start with MAGIC_OLD_ASCII",
                new String(output, 0, 6).equals(CpioConstants.MAGIC_OLD_ASCII));
    }

    public void testWriteOldBinaryFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_OLD_BINARY);
        entry.setName("oldBinaryFile");
        byte[] content = "BinaryData".getBytes();
        entry.setSize(content.length);

        out.putNextEntry(entry);
        out.write(content, 0, content.length);
        out.closeArchiveEntry();
        out.finish();

        byte[] output = baos.toByteArray();
        assertTrue(output.length > 0);
    }

    

    public void testCloseFinishesAndClosesStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("closeFile");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.closeArchiveEntry();

        out.close();
        // Subsequent operations should throw IOException("Stream closed")
        try {
            out.putNextEntry(new CpioArchiveEntry(CpioConstants.FORMAT_NEW));
            fail("Expected IOException on closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.write(new byte[]{1}, 0, 1);
            fail("Expected IOException on closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.finish();
            fail("Expected IOException on closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.closeArchiveEntry();
            fail("Expected IOException on closed stream");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        // Calling close again should not throw exception
        out.close();
    }
}
