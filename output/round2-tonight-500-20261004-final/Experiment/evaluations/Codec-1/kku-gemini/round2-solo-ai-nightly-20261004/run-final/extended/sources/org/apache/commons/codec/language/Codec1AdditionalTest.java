package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class Codec1AdditionalTest {

    @Test
    public void testCaverphoneAdditionalCoverage() {
        Caverphone caverphone = new Caverphone();
        assertNotNull(caverphone.encode("rough"));
        assertNotNull(caverphone.encode("tough"));
        assertNotNull(caverphone.encode("enough"));
        assertNotNull(caverphone.encode("gnat"));
        assertNotNull(caverphone.encode("mb"));
    }

    @Test
    public void testMetaphoneAdditionalCoverage() {
        Metaphone metaphone = new Metaphone();
        assertNotNull(metaphone.metaphone("KNIGHT"));
        assertNotNull(metaphone.metaphone("AEON"));
        assertNotNull(metaphone.metaphone("WRONG"));
        assertNotNull(metaphone.metaphone("WHILE"));
    }

    @Test
    public void testSoundexUtilsDifferenceNullCases() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals(0, SoundexUtils.difference(caverphone, null, "Test"));
        assertEquals(0, SoundexUtils.difference(caverphone, "Test", null));
        assertEquals(0, SoundexUtils.difference(caverphone, null, null));
    }
}
