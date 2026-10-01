package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class MetaphoneTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() {
        metaphone = new Metaphone();
    }

    public void testMetaphoneNull() {
        assertEquals("", metaphone.metaphone(null));
    }

    public void testMetaphoneEmptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleChar() {
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("B", metaphone.metaphone("B"));
        assertEquals("Z", metaphone.metaphone("z"));
    }

    public void testMetaphoneKN() {
        assertEquals("N", metaphone.metaphone("Knight"));
    }

    public void testMetaphoneGN() {
        assertEquals("N", metaphone.metaphone("Gnome"));
    }

    public void testMetaphonePN() {
        assertEquals("N", metaphone.metaphone("Pneumatic"));
    }

    public void testMetaphoneAE() {
        assertEquals("E", metaphone.metaphone("Aerial"));
    }

    public void testMetaphoneWR() {
        assertEquals("R", metaphone.metaphone("Write"));
    }

    public void testMetaphoneWH() {
        assertEquals("W", metaphone.metaphone("When"));
    }

    public void testMetaphoneInitialX() {
        assertEquals("S", metaphone.metaphone("Xylophone"));
    }

    public void testMetaphoneMaxCodeLenDefault() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    public void testMetaphoneSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }

    public void testMetaphoneSetMaxCodeLenNegative() {
        metaphone.setMaxCodeLen(-1);
        assertEquals(-1, metaphone.getMaxCodeLen());
        // With negative max, while condition fails immediately
        assertEquals("", metaphone.metaphone("hello"));
    }

    public void testMetaphoneMaxCodeLenZero() {
        metaphone.setMaxCodeLen(0);
        assertEquals(0, metaphone.getMaxCodeLen());
        assertEquals("", metaphone.metaphone("hello"));
    }

    public void testEncodeObjectString() throws EncoderException {
        Object result = metaphone.encode("Knight");
        assertTrue(result instanceof String);
        assertEquals("N", (String) result);
    }

    public void testEncodeObjectNonString() {
        try {
            metaphone.encode(new Integer(42));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testEncodeString() {
        assertEquals("N", metaphone.encode("Knight"));
    }

    public void testIsMetaphoneEqualTrue() {
        assertTrue(metaphone.isMetaphoneEqual("Knight", "Night"));
    }

    public void testIsMetaphoneEqualFalse() {
        assertFalse(metaphone.isMetaphoneEqual("Knight", "Day"));
    }

    public void testIsMetaphoneEqualBothNull() {
        assertTrue(metaphone.isMetaphoneEqual(null, null));
    }

    public void testIsMetaphoneEqualOneNull() {
        assertFalse(metaphone.isMetaphoneEqual(null, "Day"));
    }

    public void testMetaphoneBAtEndAfterM() {
        assertEquals("TM", metaphone.metaphone("tomb"));
    }

    public void testMetaphoneCIA() {
        assertEquals("X", metaphone.metaphone("special"));
    }

    public void testMetaphoneSCH() {
        assertEquals("SK", metaphone.metaphone("school"));
    }

    public void testMetaphoneCH() {
        assertEquals("K", metaphone.metaphone("character"));
    }

    public void testMetaphoneDGE() {
        assertEquals("J", metaphone.metaphone("edge"));
    }

    public void testMetaphonePH() {
        assertEquals("F", metaphone.metaphone("phone"));
    }

    public void testMetaphoneTH() {
        assertEquals("0", metaphone.metaphone("thin"));
    }

    public void testMetaphoneX() {
        assertEquals("KS", metaphone.metaphone("box"));
    }

    public void testMetaphoneZ() {
        assertEquals("S", metaphone.metaphone("zebra"));
    }
}
