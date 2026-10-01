package org.apache.commons.compress.archivers.cpio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class CpioArchiveOutputStreamGeneratedTest extends TestCase {

    private ByteArrayOutputStream bos;

    protected void setUp() {
        bos = new ByteArrayOutputStream();
    }

    private static CpioArchiveEntry entry(short format, String name, long size) {
        CpioArchiveEntry e = new CpioArchiveEntry(format);
        e.setName(name);
        e.setSize(size);
        return e;
    }

    private static String ascii(byte[] data, int off, int len) {
        return new String(data, off, len);
    }

    // ---------------------------------------------------------------- ctor / setFormat

    public void testConstructorAcceptsAllKnownFormats() throws Exception {
        short[] formats = new short[] { CpioConstants.FORMAT_NEW,
                CpioConstants.FORMAT_NEW_CRC, CpioConstants.FORMAT_OLD_ASCII,
                CpioConstants.FORMAT_OLD_BINARY };
        for (int i = 0; i < formats.length; i++) {
            CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                    new ByteArrayOutputStream(), formats[i]);
            assertNotNull(out);
        }
    }

    public void testConstructorRejectsUnknownFormat() {
        try {
            new CpioArchiveOutputStream(bos, (short) 99);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    public void testConstructorRejectsZeroFormat() {
        try {
            new CpioArchiveOutputStream(bos, (short) 0);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // ---------------------------------------------------------------- putNextEntry / headers

    public void testNewFormatHeaderLengthAndMagic() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 0));
        byte[] data = bos.toByteArray();
        // 110 header bytes + "a\0" = 112, already 4-byte aligned
        assertEquals(112, data.length);
        assertEquals("070701", ascii(data, 0, 6));
        assertEquals(0, data[111]);
        assertEquals('a', data[110]);
    }

    public void testNewFormatHeaderIsPaddedToFourBytes() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "ab", 0));
        // 110 + 3 = 113 -> padded by 3 to 116
        assertEquals(116, bos.toByteArray().length);
    }

    public void testNewCrcFormatMagic() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_NEW_CRC);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW_CRC, "a", 0));
        byte[] data = bos.toByteArray();
        assertEquals(112, data.length);
        assertEquals("070702", ascii(data, 0, 6));
    }

    public void testOldAsciiHeaderLengthAndMagic() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_ASCII);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_ASCII, "a", 0));
        byte[] data = bos.toByteArray();
        // 76 header bytes + "a\0", no padding for old ASCII
        assertEquals(78, data.length);
        assertEquals("070707", ascii(data, 0, 6));
        assertEquals('a', data[76]);
        assertEquals(0, data[77]);
    }

    public void testOldBinaryHeaderLengthWithAndWithoutPadding() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_BINARY);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_BINARY, "a", 0));
        // 26 + 2 = 28, even -> no padding
        assertEquals(28, bos.size());

        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        CpioArchiveOutputStream out2 = new CpioArchiveOutputStream(bos2,
                CpioConstants.FORMAT_OLD_BINARY);
        out2.putNextEntry(entry(CpioConstants.FORMAT_OLD_BINARY, "ab", 0));
        // 26 + 3 = 29 -> padded by 1 to 30
        assertEquals(30, bos2.size());
    }

    public void testEntryFormatOverridesStreamDefault() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_NEW);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_ASCII, "a", 0));
        assertEquals("070707", ascii(bos.toByteArray(), 0, 6));
    }

    public void testPutArchiveEntryDelegatesToPutNextEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putArchiveEntry(entry(CpioConstants.FORMAT_NEW, "a", 0));
        byte[] data = bos.toByteArray();
        assertEquals(112, data.length);
        assertEquals("070701", ascii(data, 0, 6));
    }

    public void testPutArchiveEntryNullThrowsNullPointerException() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        try {
            out.putArchiveEntry(null);
            fail("expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    public void testDuplicateEntryNameIsRejected() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "dup", 0));
        try {
            out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "dup", 0));
            fail("expected IOException for duplicate entry");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("duplicate entry") >= 0);
        }
    }

    public void testPutNextEntryClosesPreviousEntry() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 0));
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "b", 0));
        // two 112-byte headers
        assertEquals(224, bos.size());
    }

    public void testPutNextEntryWithIncompletePreviousEntryFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 5));
        try {
            out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "b", 0));
            fail("expected IOException for invalid entry size");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("invalid entry size") >= 0);
        }
    }

    // ---------------------------------------------------------------- write

    public void testWriteDataAndPaddingNewFormat() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 5));
        out.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        out.closeArchiveEntry();
        byte[] data = bos.toByteArray();
        // 112 header + 5 data + 3 padding
        assertEquals(120, data.length);
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, data[112 + i]);
        }
        for (int i = 117; i < 120; i++) {
            assertEquals(0, data[i]);
        }
    }

    public void testWriteDataOldAsciiHasNoPadding() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_ASCII);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_ASCII, "a", 5));
        out.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        out.closeArchiveEntry();
        assertEquals(78 + 5, bos.size());
    }

    public void testWriteDataOldBinaryPadsToTwoBytes() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_BINARY);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_BINARY, "a", 5));
        out.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        out.closeArchiveEntry();
        // 28 header + 5 data + 1 padding
        assertEquals(34, bos.size());
    }

    public void testWriteWithOffsetWritesSubRange() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_ASCII);
        out.putNextEntry(entry(CpioConstants.FORMAT_OLD_ASCII, "a", 2));
        out.write(new byte[] { 9, 8, 7, 6 }, 1, 2);
        out.closeArchiveEntry();
        byte[] data = bos.toByteArray();
        assertEquals(80, data.length);
        assertEquals(8, data[78]);
        assertEquals(7, data[79]);
    }

    public void testWriteZeroLengthWithoutEntryIsNoOp() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.write(new byte[4], 0, 0);
        assertEquals(0, bos.size());
    }

    public void testWriteWithoutCurrentEntryFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        try {
            out.write(new byte[] { 1 }, 0, 1);
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("no current CPIO entry") >= 0);
        }
    }

    public void testWriteInvalidRangeThrowsIndexOutOfBounds() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        byte[] b = new byte[4];
        try {
            out.write(b, -1, 1);
            fail("negative offset");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
        try {
            out.write(b, 0, -1);
            fail("negative length");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
        try {
            out.write(b, 3, 2);
            fail("range past end of array");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
    }

    public void testWritePastEntrySizeFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 3));
        try {
            out.write(new byte[4], 0, 4);
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("past end") >= 0);
        }
    }

    public void testSingleByteWriteGoesStraightToUnderlyingStream() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.write(0x41);
        byte[] data = bos.toByteArray();
        assertEquals(1, data.length);
        assertEquals(0x41, data[0]);
    }

    public void testWriteAfterCloseFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.close();
        try {
            out.write(new byte[] { 1 }, 0, 1);
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("Stream closed") >= 0);
        }
    }

    // ---------------------------------------------------------------- closeArchiveEntry

    public void testCloseArchiveEntrySizeMismatchFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 5));
        out.write(new byte[] { 1, 2, 3 }, 0, 3);
        try {
            out.closeArchiveEntry();
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("invalid entry size") >= 0);
        }
    }

    public void testCloseArchiveEntryCrcMismatchFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_NEW_CRC);
        // default checksum of the entry is 0, data sums to 6
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW_CRC, "a", 3));
        out.write(new byte[] { 1, 2, 3 }, 0, 3);
        try {
            out.closeArchiveEntry();
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("CRC Error") >= 0);
        }
    }

    public void testCloseArchiveEntryCrcMatchSucceeds() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_NEW_CRC);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW_CRC, "a", 3));
        out.write(new byte[] { 0, 0, 0 }, 0, 3);
        out.closeArchiveEntry();
        // 112 header + 3 data + 1 padding
        assertEquals(116, bos.size());
    }

    public void testCloseArchiveEntryAfterCloseFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.close();
        try {
            out.closeArchiveEntry();
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("Stream closed") >= 0);
        }
    }

    // ---------------------------------------------------------------- finish / close

    public void testFinishNewFormatWritesTrailerOnly() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.finish();
        byte[] data = bos.toByteArray();
        // 110 header + "TRAILER!!!\0" (11) = 121, padded to 124
        assertEquals(124, data.length);
        assertEquals("070701", ascii(data, 0, 6));
        assertEquals("00000001", ascii(data, 38, 8)); // number of links
        assertEquals("0000000b", ascii(data, 94, 8)); // name size incl. NUL
        assertEquals("TRAILER!!!", ascii(data, 110, 10));
        assertEquals(0, data[120]);
    }

    public void testFinishNewCrcFormatWritesTrailer() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_NEW_CRC);
        out.finish();
        byte[] data = bos.toByteArray();
        assertEquals(124, data.length);
        assertEquals("070702", ascii(data, 0, 6));
        assertEquals("TRAILER!!!", ascii(data, 110, 10));
    }

    public void testFinishOldAsciiWritesTrailer() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_ASCII);
        out.finish();
        byte[] data = bos.toByteArray();
        // 76 header + 11 name bytes, no padding
        assertEquals(87, data.length);
        assertEquals("070707", ascii(data, 0, 6));
        assertEquals("000001", ascii(data, 36, 6)); // number of links
        assertEquals("000013", ascii(data, 59, 6)); // name size 11 in octal
        assertEquals("TRAILER!!!", ascii(data, 76, 10));
        assertEquals(0, data[86]);
    }

    public void testFinishOldBinaryWritesTrailer() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos,
                CpioConstants.FORMAT_OLD_BINARY);
        out.finish();
        // 26 header + 11 name bytes = 37, padded to 38
        assertEquals(38, bos.size());
    }

    public void testFinishClosesCurrentEntryBeforeTrailer() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 5));
        out.write(new byte[] { 1, 2, 3, 4, 5 }, 0, 5);
        out.finish();
        // 120 (entry incl. padding) + 124 (trailer)
        assertEquals(244, bos.size());
    }

    public void testFinishWithIncompleteEntryFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 5));
        try {
            out.finish();
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("invalid entry size") >= 0);
        }
    }

    public void testFinishAfterCloseFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.close();
        try {
            out.finish();
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("Stream closed") >= 0);
        }
    }

    public void testCloseWritesTrailerAndSecondCloseIsNoOp() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.close();
        int lengthAfterFirstClose = bos.size();
        assertEquals(124, lengthAfterFirstClose);
        out.close();
        assertEquals(lengthAfterFirstClose, bos.size());
    }

    public void testPutNextEntryAfterCloseFails() throws Exception {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(bos);
        out.close();
        try {
            out.putNextEntry(entry(CpioConstants.FORMAT_NEW, "a", 0));
            fail("expected IOException");
        } catch (IOException expected) {
            assertTrue(expected.getMessage().indexOf("Stream closed") >= 0);
        }
    }
}
