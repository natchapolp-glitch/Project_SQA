package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class MetaphoneRegressionTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.metaphone = new Metaphone();
    }

    public void testMetaphoneNullAndEmpty() {
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleChar() {
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("Z", metaphone.metaphone("Z"));
        assertEquals("B", metaphone.metaphone("b"));
    }

    public void testMetaphoneInitialSpecialCases() {
        assertEquals("NT", metaphone.metaphone("knight"));
        assertEquals("NT", metaphone.metaphone("gnat"));
        assertEquals("RT", metaphone.metaphone("wright"));
        assertTrue(metaphone.metaphone("pneumonia").startsWith("N"));
        assertTrue(metaphone.metaphone("aegis").startsWith("E"));
        assertTrue(metaphone.metaphone("white").startsWith("W"));
        assertTrue(metaphone.metaphone("xavier").startsWith("S"));
    }

    public void testMetaphoneEncodeObject() throws Exception {
        Object result = metaphone.encode((Object) "testing");
        assertTrue(result instanceof String);
        assertEquals(metaphone.metaphone("testing"), result);

        try {
            metaphone.encode(new Long(100));
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException e) {
            // expected
        }

        try {
            metaphone.encode((Object) null);
            fail("Expected EncoderException for null Object input");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testMetaphoneEncodeString() {
        assertEquals(metaphone.metaphone("matrix"), metaphone.encode("matrix"));
        assertEquals("", metaphone.encode(""));
    }

    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("wright", "right"));
        assertTrue(metaphone.isMetaphoneEqual("knight", "night"));
        assertTrue(metaphone.isMetaphoneEqual(null, ""));
        assertFalse(metaphone.isMetaphoneEqual("apple", "orange"));
    }

    public void testMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
        assertEquals("0MPSN", metaphone.metaphone("THOMPSON"));

        metaphone.setMaxCodeLen(2);
        assertEquals("0M", metaphone.metaphone("THOMPSON"));
    }

    public void testMetaphoneSpecialC() {
        assertEquals("SKL", metaphone.metaphone("school"));
        assertEquals("XK", metaphone.metaphone("check"));
        assertEquals("SPXL", metaphone.metaphone("special"));
    }

    public void testMetaphoneSpecialD() {
        assertEquals("EJ", metaphone.metaphone("edge"));
        assertEquals("TJ", metaphone.metaphone("dodge"));
        assertEquals("TR", metaphone.metaphone("door"));
    }

    public void testMetaphoneSpecialG() {
        assertEquals("SN", metaphone.metaphone("sign"));
        assertEquals("H", metaphone.metaphone("high"));
        assertEquals("JNT", metaphone.metaphone("giant"));
        assertEquals("JM", metaphone.metaphone("gym"));
    }

    public void testMetaphoneSpecialT() {
        assertEquals("0TR", metaphone.metaphone("theater"));
        assertEquals("WX", metaphone.metaphone("watch"));
        assertEquals("RX", metaphone.metaphone("ratio"));
    }

    public void testMetaphoneVowelsAndSilentWY() {
        assertEquals("ORNJ", metaphone.metaphone("orange"));
        assertEquals("APL", metaphone.metaphone("apple"));
        assertEquals("K", metaphone.metaphone("cow"));
        assertEquals("T", metaphone.metaphone("day"));
    }
}
