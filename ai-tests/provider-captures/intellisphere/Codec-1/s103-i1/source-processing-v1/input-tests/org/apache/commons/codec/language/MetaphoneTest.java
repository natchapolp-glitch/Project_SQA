package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Tests for {@link Metaphone}.
 *
 * @version $Id$
 */
public class MetaphoneTest extends TestCase {

    private Metaphone metaphone;

    public void setUp() {
        metaphone = new Metaphone();
    }

    public void tearDown() {
        metaphone = null;
    }

    // Test metaphone method basic outputs

    public void testMetaphoneNull() {
        assertEquals("", metaphone.metaphone(null));
    }

    public void testMetaphoneEmpty() {
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleCharacter() {
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));
        assertEquals("Z", metaphone.metaphone("z"));
    }

    public void testMetaphoneBasicExamples() {
        assertEquals("TST", metaphone.metaphone("TEST"));
        assertEquals("LKN", metaphone.metaphone("lucene"));
    }

    public void testMetaphoneMaxCodeLen() {
        assertEquals("ALKN", metaphone.metaphone("ALKNSSS"));
        assertEquals("ABRS", metaphone.metaphone("ABRSSS"));
    }

    // Test encode methods

    public void testEncodeString() {
        assertEquals("TST", metaphone.encode("test"));
    }

    public void testEncodeObjectWithString() throws EncoderException {
        Object result = metaphone.encode((Object) "test");
        assertTrue(result instanceof String);
        assertEquals("TST", result);
    }

    public void testEncodeObjectWithNonString() {
        try {
            metaphone.encode(new Integer(1));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("Parameter supplied to Metaphone encode"));
        }
    }

    // Test isMetaphoneEqual

    public void testIsMetaphoneEqualTrue() {
        assertTrue(metaphone.isMetaphoneEqual("test", "test"));
    }

    public void testIsMetaphoneEqualFalse() {
        assertFalse(metaphone.isMetaphoneEqual("test", "jest"));
    }

    public void testIsMetaphoneEqualCaseInsensitive() {
        assertTrue(metaphone.isMetaphoneEqual("Test", "test"));
    }

    // Test getMaxCodeLen and setMaxCodeLen

    public void testDefaultMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(7);
        assertEquals(7, metaphone.getMaxCodeLen());
        // Test that truncation occurs at new length
        String encoded = metaphone.encode("ALGORITHM");
        assertTrue(encoded.length() <= 7);
    }

    public void testSetMaxCodeLenZero() {
        metaphone.setMaxCodeLen(0);
        assertEquals(0, metaphone.getMaxCodeLen());
        assertEquals("", metaphone.encode("test"));
    }

    // private method logic tested via public API

    public void testInitialAE() {
        assertEquals("LKN", metaphone.metaphone("AELUCENE"));
    }

    public void testInitialWR() {
        assertEquals("RKN", metaphone.metaphone("WRECKON"));
    }

    public void testInitialWH() {
        assertEquals("W", metaphone.metaphone("WHE"));
    }

    public void testInitialX() {
        assertEquals("S", metaphone.metaphone("XE"));
    }

    public void testInitialKN() {
        assertEquals("N", metaphone.metaphone("KNE"));
    }

    public void testInitialGN() {
        assertEquals("N", metaphone.metaphone("GNOME"));
    }

    public void testInitialPN() {
        assertEquals("N", metaphone.metaphone("PNOMON"));
    }

    public void testSCH() {
        assertEquals("SK", metaphone.metaphone("SCHOOL"));
    }

    public void testCIA() {
        assertEquals("X", metaphone.metaphone("CI"));
    }

    public void testCE() {
        assertEquals("S", metaphone.metaphone("CERT"));
    }

    public void testDoubleMetaphone() {
        // Testing Los Vegas vs. Los Angeles context?
        // Just ensure stable output
        String m1 = metaphone.metaphone("LOS");
        String m2 = metaphone.metaphone("LOZ");
        // LOS vs LOZ both evaluate to LS
        assertEquals(m1, m2);
    }

    public void testPH() {
        assertEquals("F", metaphone.metaphone("PHO"));
    }

    public void testTH() {
        assertEquals("0", metaphone.metaphone("THEO"));
    }

    public void testDGE() {
        assertEquals("J", metaphone.metaphone("DGE"));
    }

    public void testTIA() {
        assertEquals("X", metaphone.metaphone("PATIA"));
    }
}
