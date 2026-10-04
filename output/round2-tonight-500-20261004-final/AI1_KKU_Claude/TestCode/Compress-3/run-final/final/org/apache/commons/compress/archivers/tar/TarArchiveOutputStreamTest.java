package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void writeAndReadBackSmallEntry() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        byte[] content = "hello world".getBytes("UTF-8");
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(content.length);
        tos.putArchiveEntry(entry);
        tos.write(content, 0, content.length);
        tos.closeArchiveEntry();
        tos.close();

        TarArchiveInputStream tis = new TarArchiveInputStream(new ByteArrayInputStream(bos.toByteArray()));
        TarArchiveEntry readEntry = tis.getNextTarEntry();
        assertEquals("test.txt", readEntry.getName());
        assertEquals(content.length, readEntry.getSize());
        byte[] readBuf = new byte[content.length];
        int read = tis.read(readBuf);
        assertEquals(content.length, read);
        assertArrayEquals(content, readBuf);
        tis.close();
    }

    @Test
    public void writeMoreThanDeclaredSizeThrows() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        TarArchiveEntry entry = new TarArchiveEntry("toolarge.txt");
        entry.setSize(5);
        tos.putArchiveEntry(entry);
        byte[] content = "this is too long".getBytes("UTF-8");
        try {
            tos.write(content, 0, content.length);
            fail("Expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().contains("exceeds size"));
        }
    }

    @Test
    public void closeEntryBeforeWritingAllBytesThrows() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        TarArchiveEntry entry = new TarArchiveEntry("incomplete.txt");
        entry.setSize(10);
        tos.putArchiveEntry(entry);
        tos.write("short".getBytes("UTF-8"), 0, 5);
        try {
            tos.closeArchiveEntry();
            fail("Expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().contains("closed at"));
        }
    }

    @Test
    public void longFileNameWithErrorModeThrowsRuntimeException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 110; i++) {
            sb.append('a');
        }
        TarArchiveEntry entry = new TarArchiveEntry(sb.toString());
        entry.setSize(0);
        try {
            tos.putArchiveEntry(entry);
            fail("Expected RuntimeException");
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("too long"));
        }
    }

    @Test
    public void longFileNameWithGnuModeWritesLongLinkHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 110; i++) {
            sb.append('b');
        }
        String longName = sb.toString();
        TarArchiveEntry entry = new TarArchiveEntry(longName);
        entry.setSize(0);
        tos.putArchiveEntry(entry);
        tos.closeArchiveEntry();
        tos.close();

        TarArchiveInputStream tis = new TarArchiveInputStream(new ByteArrayInputStream(bos.toByteArray()));
        TarArchiveEntry readEntry = tis.getNextTarEntry();
        assertEquals(longName, readEntry.getName());
        tis.close();
    }

    @Test
    public void zeroByteEntryClosesWithoutWriting() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        TarArchiveEntry entry = new TarArchiveEntry("empty.txt");
        entry.setSize(0);
        tos.putArchiveEntry(entry);
        tos.closeArchiveEntry();
        tos.close();

        TarArchiveInputStream tis = new TarArchiveInputStream(new ByteArrayInputStream(bos.toByteArray()));
        TarArchiveEntry readEntry = tis.getNextTarEntry();
        assertEquals("empty.txt", readEntry.getName());
        assertEquals(0, readEntry.getSize());
        tis.close();
    }

    @Test
    public void getRecordSizeReturnsCustomValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos, 10240, 512);
        assertEquals(512, tos.getRecordSize());
    }

    @Test
    public void closeWithoutExplicitFinishStillWritesEOFRecords() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(0);
        tos.putArchiveEntry(entry);
        tos.closeArchiveEntry();
        tos.close();

        byte[] result = bos.toByteArray();
        int recordSize = 512;
        byte[] lastTwoRecords = new byte[2 * recordSize];
        System.arraycopy(result, result.length - lastTwoRecords.length, lastTwoRecords, 0, lastTwoRecords.length);
        byte[] zeros = new byte[2 * recordSize];
        assertArrayEquals(zeros, lastTwoRecords);
    }
}
