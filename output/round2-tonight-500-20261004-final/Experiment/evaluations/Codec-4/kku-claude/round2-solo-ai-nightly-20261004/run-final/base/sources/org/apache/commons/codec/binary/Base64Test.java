package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testRoundTripVariousLengths() {
        for (int len = 0; len <= 6; len++) {
            byte[] data = new byte[len];
            for (int i = 0; i < len; i++) {
                data[i] = (byte) (i + 1);
            }
            byte[] encoded = Base64.encodeBase64(data);
            byte[] decoded = Base64.decodeBase64(encoded);
            assertArrayEquals("length=" + len, data, decoded);
        }
    }

    @Test
    public void testNullAndEmptyHandling() {
        assertNull(Base64.encodeBase64(null));
        assertNull(Base64.decodeBase64((byte[]) null));
        byte[] encodedEmpty = Base64.encodeBase64(new byte[0]);
        assertNotNull(encodedEmpty);
        assertEquals(0, encodedEmpty.length);
    }

    @Test
    public void testIsBase64Boundaries() {
        assertTrue(Base64.isBase64((byte) 'A'));
        assertTrue(Base64.isBase64((byte) 'z'));
        assertTrue(Base64.isBase64((byte) '0'));
        assertTrue(Base64.isBase64((byte) '+'));
        assertTrue(Base64.isBase64((byte) '/'));
        assertTrue(Base64.isBase64((byte) '='));
        assertTrue(Base64.isBase64((byte) ' '));
        assertTrue(Base64.isBase64((byte) '\n'));
        assertFalse(Base64.isBase64((byte) '!'));
        assertFalse(Base64.isBase64((byte) -1));
    }

    @Test
    public void testChunkingBehavior() {
        byte[] data = new byte[60];
        Arrays.fill(data, (byte) 'x');
        byte[] chunked = Base64.encodeBase64Chunked(data);
        String chunkedStr = new String(chunked);
        assertTrue(chunkedStr.contains("\r\n"));

        byte[] unchunked = Base64.encodeBase64(data, false);
        String unchunkedStr = new String(unchunked);
        assertFalse(unchunkedStr.contains("\r\n"));

        byte[] decodedChunked = Base64.decodeBase64(chunked);
        assertArrayEquals(data, decodedChunked);
    }

    @Test
    public void testUrlSafeEncoding() {
        byte[] data = {(byte) 0xFB, (byte) 0xFF, (byte) 0xFF};
        byte[] urlSafe = Base64.encodeBase64URLSafe(data);
        String urlSafeStr = new String(urlSafe);
        assertFalse(urlSafeStr.contains("+"));
        assertFalse(urlSafeStr.contains("/"));
        assertFalse(urlSafeStr.contains("="));
        byte[] decoded = Base64.decodeBase64(urlSafe);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void testDiscardWhitespace() {
        byte[] input = "A B\tC\nD\rE".getBytes();
        byte[] result = Base64.discardWhitespace(input);
        assertArrayEquals("ABCDE".getBytes(), result);
    }

    @Test
    public void testIntegerEncodeDecodeRoundTrip() {
        BigInteger[] values = {
                BigInteger.ZERO,
                BigInteger.valueOf(-1),
                BigInteger.valueOf(255),
                BigInteger.valueOf(-255),
                new BigInteger("123456789012345678901234567890"),
                new BigInteger("-123456789012345678901234567890")
        };
        for (BigInteger value : values) {
            byte[] encoded = Base64.encodeInteger(value);
            BigInteger decoded = Base64.decodeInteger(encoded);
            assertEquals(value, decoded);
        }
    }

    @Test
    public void testStreamingEncodeAcrossMultipleCalls() {
        Base64 base64 = new Base64(0);
        byte[] part1 = {1, 2};
        byte[] part2 = {3, 4, 5};
        base64.encode(part1, 0, part1.length);
        base64.encode(part2, 0, part2.length);
        base64.encode(new byte[0], 0, -1);
        assertTrue(base64.hasData());
        int avail = base64.avail();
        byte[] out = new byte[avail];
        int read = base64.readResults(out, 0, avail);
        assertEquals(avail, read);
        byte[] expectedEncoded = Base64.encodeBase64(new byte[]{1, 2, 3, 4, 5});
        assertArrayEquals(expectedEncoded, out);
    }

    @Test
    public void testConstructorRejectsBase64LineSeparator() {
        try {
            new Base64(76, new byte[]{'A'});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testIsArrayByteBase64() {
        assertTrue(Base64.isArrayByteBase64("SGVsbG8=".getBytes()));
        assertFalse(Base64.isArrayByteBase64("Hello!".getBytes()));
        assertFalse(Base64.isArrayByteBase64("   ".getBytes()));
    }
}
