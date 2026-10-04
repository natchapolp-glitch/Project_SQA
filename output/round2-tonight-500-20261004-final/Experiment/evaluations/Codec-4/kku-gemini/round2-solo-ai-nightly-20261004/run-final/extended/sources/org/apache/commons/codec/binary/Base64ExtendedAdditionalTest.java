package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class Base64ExtendedAdditionalTest {

    @Test(expected = IllegalArgumentException.class)
    public void testLineSeparatorContainsBase64CharThrowsException() {
        byte[] lineSeparator = new byte[] { 'A' };
        new Base64(76, lineSeparator);
    }

    @Test
    public void testObjectEncodeInvalidTypeThrowsEncoderException() {
        Base64 base64 = new Base64();
        try {
            base64.encode("NotAByteArray");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testObjectDecodeInvalidTypeThrowsDecoderException() {
        Base64 base64 = new Base64();
        try {
            base64.decode("NotAByteArrayOrString");
            fail("Expected DecoderException");
        } catch (DecoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testEncodeBase64WithMaxResultSize() {
        byte[] input = "The quick brown fox jumps over the lazy dog.".getBytes();
        byte[] encoded = Base64.encodeBase64(input, true, false, 10);
        assertNotNull(encoded);
    }
}
