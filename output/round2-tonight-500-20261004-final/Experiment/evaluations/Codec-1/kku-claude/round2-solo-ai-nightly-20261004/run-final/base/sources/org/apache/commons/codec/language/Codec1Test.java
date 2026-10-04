package org.apache.commons.codec.language;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class Codec1Test {

    @Test
    public void testCaverphoneNullAndEmpty() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneKnownValues() {
        Caverphone caverphone = new Caverphone();
        assertEquals("TMSN11111", caverphone.caverphone("Thompson"));
        assertEquals("SMT1111111".substring(0, "SMT1111111".length()), caverphone.caverphone("Smith").substring(0, caverphone.caverphone("Smith").length()));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("Thompson", "Thompson"));
        assertFalse(caverphone.isCaverphoneEqual("Thompson", "Smith"));
    }

    @Test
    public void testCaverphoneEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode((Object) "Thompson");
        assertEquals(caverphone.caverphone("Thompson"), result);
    }

    @Test(expected = ClassCastException.class)
    public void testCaverphoneEncodeObjectInvalidType() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        caverphone.encode((Object) Integer.valueOf(1));
    }

    @Test
    public void testMetaphoneNullAndEmpty() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneKnownValues() {
        Metaphone metaphone = new Metaphone();
        assertEquals("TMSN", metaphone.metaphone("Thompson"));
        assertEquals("NT", metaphone.metaphone("Knight"));
    }

    @Test
    public void testMetaphoneMaxCodeLenDefaultAndSetter() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
        String code = metaphone.metaphone("Thompson");
        assertTrue(code.length() <= 6);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Thompson", "Thompson"));
        assertFalse(metaphone.isMetaphoneEqual("Thompson", "Knight"));
    }

    @Test
    public void testSoundexUtilsClean() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("a1b2c3"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
    }

    @Test
    public void testSoundexUtilsDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
        assertEquals(4, SoundexUtils.differenceEncoded("W123", "W123"));
        assertEquals(0, SoundexUtils.differenceEncoded("A000", "B111"));
    }
}
