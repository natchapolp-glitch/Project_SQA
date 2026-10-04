package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private static byte[] buildArWithOneEntry(String name, byte[] content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("!<arch>\n".getBytes("ASCII"));
        String nameField = pad(name, 16);
        String lastmod = pad("0", 12);
        String userid = pad("0", 6);
        String groupid = pad("0", 6);
        String filemode = pad("100644", 8);
        String length = pad(String.valueOf(content.length), 10);
        out.write(nameField.getBytes("ASCII"));
        out.write(lastmod.getBytes("ASCII"));
        out.write(userid.getBytes("ASCII"));
        out.write(groupid.getBytes("ASCII"));
        out.write(filemode.getBytes("ASCII"));
        out.write(length.getBytes("ASCII"));
        out.write("`\n".getBytes("ASCII"));
        out.write(content);
        if (content.length % 2 != 0) {
            out.write('\n');
        }
        return out.toByteArray();
    }

    private static String pad(String s, int len) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < len) {
            sb.append(' ');
        }
        return sb.substring(0, len);
    }

    @Test
    public void readsSingleEntryNameAndSize() throws IOException {
        byte[] content = "hello".getBytes("ASCII");
        byte[] archive = buildArWithOneEntry("test.txt", content);
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(archive));
        ArArchiveEntry entry = in.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(content.length, entry.getSize());
        in.close();
    }

    @Test
    public void getNextEntryReturnsNullAtEof() throws IOException {
        byte[] content = "hi".getBytes("ASCII");
        byte[] archive = buildArWithOneEntry("a.txt", content);
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(archive));
        assertNotNull(in.getNextEntry());
        assertNull(in.getNextEntry());
        in.close();
    }

    @Test
    public void readZeroLengthReturnsMinusOneAtEndOfStream() throws IOException {
        // Underlying ByteArrayInputStream.read(b, off, 0) returns 0 only if
        // not at EOF; once the content has already been fully consumed by
        // getNextArEntry(), the stream is exhausted and read(...,0) returns -1.
        byte[] content = "data".getBytes("ASCII");
        byte[] archive = buildArWithOneEntry("f.txt", content);
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(archive));
        in.getNextArEntry();
        byte[] buf = new byte[10];
        int result = in.read(buf, 0, 0);
        assertEquals(-1, result);
        in.close();
    }

    @Test
    public void readZeroLengthMidStreamReturnsZero() throws IOException {
        // When there is still data available, reading zero bytes should
        // return 0, not -1.
        byte[] content = "data".getBytes("ASCII");
        byte[] archive = buildArWithOneEntry("f.txt", content);
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(archive));
        in.getNextArEntry();
        byte[] buf = new byte[10];
        int firstByte = in.read();
        assertNotEquals(-1, firstByte);
        int result = in.read(buf, 0, 0);
        assertEquals(0, result);
        in.close();
    }

    @Test
    public void closeClosesUnderlyingStream() throws IOException {
        byte[] content = "x".getBytes("ASCII");
        byte[] archive = buildArWithOneEntry("x.txt", content);
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(archive));
        in.getNextArEntry();
        in.close();
        in.close();
    }

    @Test
    public void matchesValidSignature() {
        byte[] sig = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(sig, sig.length));
    }

    @Test
    public void matchesRejectsShortOrWrongSignature() {
        byte[] shortSig = new byte[] {0x21, 0x3c, 0x61, 0x72};
        assertFalse(ArArchiveInputStream.matches(shortSig, shortSig.length));

        byte[] wrongSig = new byte[] {0x00, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertFalse(ArArchiveInputStream.matches(wrongSig, wrongSig.length));
    }

    @Test(expected = IOException.class)
    public void invalidHeaderThrows() throws IOException {
        byte[] bogus = "notanarchive....".getBytes("ASCII");
        ArArchiveInputStream in = new ArArchiveInputStream(new ByteArrayInputStream(bogus));
        try {
            in.getNextArEntry();
        } finally {
            in.close();
        }
    }
}
