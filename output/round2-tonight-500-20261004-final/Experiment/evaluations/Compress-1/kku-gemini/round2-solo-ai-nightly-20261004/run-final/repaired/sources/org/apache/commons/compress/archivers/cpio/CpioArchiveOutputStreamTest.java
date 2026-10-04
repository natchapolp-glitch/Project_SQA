package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatConstructor() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new CpioArchiveOutputStream(out, (short) 9999);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutEntryThrowsIOException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioOut.write(new byte[] { 1, 2, 3 }, 0, 3);
    }

    @Test(expected = IOException.class)
    public void testPutEntryAfterCloseThrowsIOException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioOut.close();
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioOut.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntryNameThrowsIOException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        
        CpioArchiveEntry entry1 = new CpioArchiveEntry("duplicate.txt");
        entry1.setFileSize(0);
        CpioArchiveEntry entry2 = new CpioArchiveEntry("duplicate.txt");
        entry2.setFileSize(0);

        try {
            cpioOut.putNextEntry(entry1);
            cpioOut.closeArchiveEntry();
            cpioOut.putNextEntry(entry2);
        } finally {
            cpioOut.close();
        }
    }

    @Test
    public void testValidSingleEntryWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        
        String content = "Hello CPIO";
        CpioArchiveEntry entry = new CpioArchiveEntry("hello.txt");
        entry.setFileSize(content.getBytes().length);
        
        cpioOut.putNextEntry(entry);
        cpioOut.write(content.getBytes());
        cpioOut.closeArchiveEntry();
        cpioOut.finish();
        cpioOut.close();

        assertTrue(out.size() > 0);
    }

    @Test
    public void testFinishWithoutEntries() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioOut.finish();
        cpioOut.close();

        assertNotNull(out.toByteArray());
    }
}
