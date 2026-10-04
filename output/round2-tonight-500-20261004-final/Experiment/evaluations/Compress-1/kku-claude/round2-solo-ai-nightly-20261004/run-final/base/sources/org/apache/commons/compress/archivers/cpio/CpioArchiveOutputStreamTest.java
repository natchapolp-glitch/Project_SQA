package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void writeEntryWithMatchingSizeSucceeds() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        byte[] data = "12345".getBytes("UTF-8");
        entry.setSize(data.length);
        cos.putNextEntry(entry);
        cos.write(data, 0, data.length);
        cos.closeArchiveEntry();
        cos.finish();
        cos.close();
        assertTrue(bos.size() > 0);
    }

    @Test
    public void closeArchiveEntryWithSizeMismatchThrows() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(10);
        cos.putNextEntry(entry);
        cos.write(new byte[]{1, 2, 3}, 0, 3);
        try {
            cos.closeArchiveEntry();
            fail("Expected IOException for size mismatch");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void writeBeyondDeclaredSizeThrows() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(2);
        cos.putNextEntry(entry);
        try {
            cos.write(new byte[]{1, 2, 3}, 0, 3);
            fail("Expected IOException writing past declared size");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void operationsAfterCloseThrow() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        cos.finish();
        cos.close();
        try {
            cos.putNextEntry(new CpioArchiveEntry("anything"));
            fail("Expected IOException after close");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void duplicateEntryNameThrows() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        CpioArchiveEntry entry1 = new CpioArchiveEntry("dup");
        entry1.setSize(0);
        cos.putNextEntry(entry1);
        cos.closeArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry("dup");
        entry2.setSize(0);
        try {
            cos.putNextEntry(entry2);
            fail("Expected IOException for duplicate entry name");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void finishWithoutEntriesWritesTrailer() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        cos.finish();
        cos.close();
        assertTrue(bos.size() > 0);
    }

    @Test
    public void doubleCloseIsSafe() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        cos.finish();
        cos.close();
        cos.close();
    }

    @Test
    public void writeWithInvalidOffsetThrowsIndexOutOfBounds() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CpioArchiveOutputStream cos = new CpioArchiveOutputStream(bos);
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        cos.putNextEntry(entry);
        byte[] data = new byte[]{1, 2, 3};
        try {
            cos.write(data, 1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
    }
}
