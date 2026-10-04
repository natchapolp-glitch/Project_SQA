package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testDefaultConstructorAndBasicEncodeDecode() {
        Base64 base64 = new Base64();
        assertFalse(base64.isUrlSafe());
        
        byte[] input = "Hello World!".getBytes();
        byte[] encoded = base64.encode(input);
        assertNotNull(encoded);
        
        byte[] decoded = base64.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testUrlSafeConstructorAndMethods() {
        Base64 base64 = new Base64(true);
        assertTrue(base64.isUrlSafe());

        byte[] input = new byte[] { (byte) 0xfd, (byte) 0xff, (byte) 0xff };
        byte[] encoded = base64.encode(input);
        
        byte[] decoded = base64.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test
    public void testStaticEncodeBase64Variants() {
        byte[] input = "Test String 123".getBytes();
        
        byte[] standard = Base64.encodeBase64(input);
        assertNotNull(standard);

        byte[] chunked = Base64.encodeBase64(input, true);
        assertNotNull(chunked);

        byte[] urlSafeChunked = Base64.encodeBase64(input, true, true);
        assertNotNull(urlSafeChunked);

        String strEncoded = Base64.encodeBase64String(input);
        assertNotNull(strEncoded);
        
        byte[] urlSafe = Base64.encodeBase64URLSafe(input);
        assertNotNull(urlSafe);
        
        String urlSafeStr = Base64.encodeBase64URLSafeString(input);
        assertNotNull(urlSafeStr);
    }

    @Test
    public void testStaticDecodeBase64Variants() {
        byte[] input = "SGVsbG8gV29ybGQ=".getBytes();
        
        byte[] decodedBytes = Base64.decodeBase64(input);
        assertArrayEquals("Hello World".getBytes(), decodedBytes);

        byte[] decodedString = Base64.decodeBase64("SGVsbG8gV29ybGQ=");
        assertArrayEquals("Hello World".getBytes(), decodedString);
    }

    @Test
    public void testIsBase64AndIsArrayByteBase64() {
        assertTrue(Base64.isBase64((byte) 'A'));
        assertTrue(Base64.isBase64((byte) 'z'));
        assertTrue(Base64.isBase64((byte) '0'));
        assertTrue(Base64.isBase64((byte) '+'));
        assertTrue(Base64.isBase64((byte) '/'));
        assertFalse(Base64.isBase64((byte) ' '));
        assertFalse(Base64.isBase64((byte) '#'));

        assertTrue(Base64.isArrayByteBase64("SGVsbG8=".getBytes()));
        assertFalse(Base64.isArrayByteBase64("SGVsbG8 !".getBytes()));
    }

    @Test
    public void testIntegerEncodingAndDecoding() {
        BigInteger original = BigInteger.valueOf(123456789L);
        byte[] encodedInt = Base64.encodeInteger(original);
        assertNotNull(encodedInt);

        BigInteger decodedInt = Base64.decodeInteger(encodedInt);
        assertEquals(original, decodedInt);
    }

    @Test
    public void testObjectEncodeAndDecode() throws EncoderException, DecoderException {
        Base64 base64 = new Base64();
        
        Object encodedObj = base64.encode("Hello".getBytes());
        assertTrue(encodedObj instanceof byte[]);

        Object decodedObj = base64.decode(encodedObj);
        assertTrue(decodedObj instanceof byte[]);
        assertArrayEquals("Hello".getBytes(), (byte[]) decodedObj);
    }

    @Test
    public void testEmptyAndNullInputs() {
        Base64 base64 = new Base64();
        
        assertNull(base64.encode(null));
        assertNull(base64.decode((byte[]) null));
        assertNull(base64.decode((String) null));

        assertArrayEquals(new byte[0], base64.encode(new byte[0]));
        assertArrayEquals(new byte[0], base64.decode(new byte[0]));
        assertArrayEquals(new byte[0], base64.decode(""));
    }
}
