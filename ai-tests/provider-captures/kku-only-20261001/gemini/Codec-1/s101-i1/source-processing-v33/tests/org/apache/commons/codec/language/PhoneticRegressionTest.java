package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Deterministic regression tests covering Caverphone, Metaphone, and SoundexUtils.
 */
public class PhoneticRegressionTest extends TestCase {

    public void testCaverphoneNullAndEmpty() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.encode(""));
    }

    public void testCaverphoneEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object encoded = caverphone.encode((Object) "Stevenson");
        assertEquals(caverphone.caverphone("Stevenson"), encoded);
    }

    public void testCaverphoneEncodeInvalidObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(Integer.valueOf(42));
            fail("Expected EncoderException when passing non-String object");
        } catch (EncoderException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("Lee", "Lee"));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("Peter", "John"));
    }

    public void testCaverphonePrefixReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals(caverphone.caverphone("gnat"), caverphone.caverphone("nat"));
        assertEquals(caverphone.caverphone("cough"), caverphone.caverphone("couf"));
    }

    public void testCaverphoneDropFinalE() {
        Caverphone caverphone = new Caverphone();
        assertEquals(caverphone.caverphone("care"), caverphone.caverphone("car"));
    }

    public void testCaverphoneOutputLength() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("Thompson");
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    public void testMetaphoneNullAndEmpty() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleChar() {
        Metaphone metaphone = new Metaphone();
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("Z", metaphone.metaphone("z"));
        assertEquals("K", metaphone.metaphone("K"));
    }

    public void testMetaphoneInitialsKnGn() {
        Metaphone metaphone = new Metaphone();
        assertEquals("NT", metaphone.metaphone("knight"));
        assertEquals("NT", metaphone.metaphone("gnat"));
    }

    public void testMetaphoneInitialsPnAe() {
        Metaphone metaphone = new Metaphone();
        assertEquals(metaphone.metaphone("neumonia"), metaphone.metaphone("pneumonia"));
        assertEquals("EJS", metaphone.metaphone("aegis"));
    }

    public void testMetaphoneInitialsWrWhX() {
        Metaphone metaphone = new Metaphone();
        assertEquals("RT", metaphone.metaphone("wright"));
        assertEquals("WT", metaphone.metaphone("white"));
        assertEquals("SR", metaphone.metaphone("xray"));
    }

    public void testMetaphoneDuplicateLetters() {
        Metaphone metaphone = new Metaphone();
        assertEquals("APL", metaphone.metaphone("apple"));
        assertEquals("AKSN", metaphone.metaphone("accent"));
    }

    public void testMetaphoneSilentBAfterM() {
        Metaphone metaphone = new Metaphone();
        assertEquals("TM", metaphone.metaphone("dumb"));
        assertEquals("TMBR", metaphone.metaphone("dumber"));
    }

    public void testMetaphoneCSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SNS", metaphone.metaphone("science"));
        assertEquals("SPXL", metaphone.metaphone("special"));
        assertEquals("KRKT", metaphone.metaphone("character"));
        assertEquals("ARX", metaphone.metaphone("arch"));
        assertEquals("SKL", metaphone.metaphone("school"));
    }

    public void testMetaphoneDSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("TJ", metaphone.metaphone("dodge"));
        assertEquals("TK", metaphone.metaphone("dog"));
    }

    public void testMetaphoneGSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("L", metaphone.metaphone("laugh"));
        assertEquals("SN", metaphone.metaphone("sign"));
        assertEquals("JL", metaphone.metaphone("gel"));
        assertEquals("TK", metaphone.metaphone("tag"));
    }

    public void testMetaphoneHSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("B", metaphone.metaphone("bah"));
        assertEquals("HT", metaphone.metaphone("hat"));
    }

    public void testMetaphonePH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("FN", metaphone.metaphone("phone"));
        assertEquals("FSKS", metaphone.metaphone("physics"));
    }

    public void testMetaphoneSSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("XT", metaphone.metaphone("shout"));
        assertEquals("AX", metaphone.metaphone("asia"));
        assertEquals("SXN", metaphone.metaphone("session"));
    }

    public void testMetaphoneTSpecialCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("RX", metaphone.metaphone("ratio"));
        assertEquals("MLX", metaphone.metaphone("militia"));
        assertEquals("MX", metaphone.metaphone("match"));
        assertEquals("0K", metaphone.metaphone("thick"));
