package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Regression tests for CpioArchiveOutputStream.
 */
public class CpioArchiveOutputStreamTest extends TestCase {

    public void testCreateWithInvalidFormat() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new CpioArchiveOutputStream(baos, (short) 999);
            fail("Expected IllegalArgumentException for unknown format");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown header type", e.getMessage());
        }
    }

    public void testWriteEmptyArchiveNewFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();
        out.close();

        byte[] result = baos.toByteArray();
        assertTrue("Output should not be empty", result.length > 0);
        String outputStr = new String(result);
        assertTrue("Output must contain trailer marker", outputStr.contains("TRAILER!!!"));
    }

    public void testWriteEmptyArchiveOldAscii() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_ASCII);
        out.finish();
        out.close();

        byte[] result = baos.toByteArray();
        assertTrue("Output should not be empty", result.length > 0);
        String outputStr = new String(result);
        assertTrue("Output must start with magic", outputStr.startsWith(CpioConstants.MAGIC_OLD_ASCII));
        assertTrue("Output must contain trailer marker", outputStr.contains("TRAILER!!!"));
    }

    public void testWriteEmptyArchiveOldBinary() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_OLD_BINARY);
        out.finish();
        out.close();

        byte[] result = baos.toByteArray();
        assertTrue("Output should not be empty", result.length > 0);
        String outputStr = new String(result);
        assertTrue("Output must contain trailer marker", outputStr.contains("TRAILER!!!"));
    }

    public void testWriteSingleEntryNewFormat() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW);

        byte[] content = "Hello World".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test.txt");
        entry.setSize(content.length);
        out.putArchiveEntry(entry);
        out.write(content);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        byte[] result = baos.toByteArray();
        assertTrue(result.length > content.length);
        String outputStr = new String(result);
        assertTrue(outputStr.startsWith(CpioConstants.MAGIC_NEW));
        assertTrue(outputStr.contains("test.txt"));
        assertTrue(outputStr.contains("TRAILER!!!"));
    }

    public void testWriteDuplicateEntryThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("file.txt");
        entry1.setSize(0);
        out.putNextEntry(entry1);
        out.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("file.txt");
        entry2.setSize(0);
        try {
            out.putNextEntry(entry2);
            fail("Expected IOException on duplicate entry name");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("duplicate entry: file.txt") != -1);
        }
    }

    public void testWritePastDeclaredSizeThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("file.txt");
        entry.setSize(4);
        out.putNextEntry(entry);
        out.write(new byte[]{1, 2, 3, 4});

        try {
            out.write(new byte[]{5});
            fail("Expected IOException when writing past declared size");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("attempt to write past end") != -1);
        }
    }

    public void testCloseArchiveEntryWithIncompleteDataThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("file.txt");
        entry.setSize(10);
        out.putNextEntry(entry);
        out.write(new byte[]{1, 2, 3});

        try {
            out.closeArchiveEntry();
            fail("Expected IOException when closing entry before size is reached");
        } catch (IOException e) {
            assertTrue(e.getMessage().indexOf("invalid entry size") != -1);
        }
    }

    public void testWriteWithoutCurrentEntryThrowsException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        try {
            out.write(new byte[]{1, 2, 3}, 0, 3);
            fail("Expected IOException when writing without active entry");
        } catch (IOException e) {
            assertEquals("no current CPIO entry", e.getMessage());
        }
    }

    public void testWriteInvalidBoundsThrowsIndexOutOfBounds() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test.txt");
        entry.setSize(5);
        out.putNextEntry(entry);

        byte[] buf = new byte[5];
        try {
            out.write(buf, -1, 3);
            fail("Expected IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            out.write(buf, 0, -1);
            fail("Expected IndexOutOfBoundsException for negative length");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            out.write(buf, 2, 4);
            fail("Expected IndexOutOfBoundsException for offset + length > buf.length");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testWriteZeroLengthDoesNothing() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry.setName("test.txt");
        entry.setSize(0);
        out.putNextEntry(entry);
        out.write(new byte[5], 0, 0);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        assertTrue(baos.size() > 0);
    }

    public void testSingleByteWrite() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        out.write(65);
        assertEquals(65, baos.toByteArray()[0]);
    }

    public void testAutoClosingPreviousEntryOnPutNextEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);

        CpioArchiveEntry entry1 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry1.setName("first.txt");
        entry1.setSize(0);
        out.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        entry2.setName("second.txt");
        entry2.setSize(0);
        out.putNextEntry(entry2);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        String result = new String(baos.toByteArray());
        assertTrue(result.contains("first.txt"));
        assertTrue(result.contains("second.txt"));
    }

    public void testFormatCrcValidationSuccess() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);

        byte[] data = new byte[]{10, 20, 30};
        long expectedCrc = 10 + 20 + 30;

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("crc.txt");
        entry.setSize(data.length);
        entry.setChksum(expectedCrc);
        out.putNextEntry(entry);
        out.write(data);
        out.closeArchiveEntry();
        out.finish();
        out.close();

        String result = new String(baos.toByteArray());
        assertTrue(result.startsWith(CpioConstants.MAGIC_NEW_CRC));
    }

    public void testFormatCrcValidationFailure() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos, CpioConstants.FORMAT_NEW_CRC);

        byte[] data = new byte[]{1, 2, 3};
        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW_CRC);
        entry.setName("crc_fail.txt");
        entry.setSize(data.length);
        entry.setChksum(9999L);
        out.putNextEntry(entry);
        out.write(data);

        try {
            out.closeArchiveEntry();
            fail("Expected CRC Error IOException");
        } catch (IOException e) {
            assertEquals("CRC Error", e.getMessage());
        }
    }

    public void testOperationsAfterCloseThrowException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.close();

        try {
            out.finish();
            fail("Expected IOException after stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.putNextEntry(new CpioArchiveEntry("test"));
            fail("Expected IOException after stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.write(new byte[]{1, 2}, 0, 2);
            fail("Expected IOException after stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }

        try {
            out.closeArchiveEntry();
            fail("Expected IOException after stream is closed");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    public void testMultipleFinishCallsIgnored() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(baos);
        out.finish();
        int sizeAfterFirstFinish = baos.size();
        out.finish();
        assertEquals("Subsequent finish() invocations should not write duplicate data",
                sizeAfterFirstFinish, baos.size());
        out.close();
    }
}
