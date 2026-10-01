// src/test/org/apache/commons/codec/language/MetaphoneTest.java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class MetaphoneTest extends TestCase {
    
    private Metaphone metaphone;

    protected void setUp() throws Exception {
        super.setUp();
        metaphone = new Metaphone();
    }

    public void testMetaphoneNull() {
        assertEquals("", metaphone.metaphone(null));
    }

    public void testMetaphoneEmpty() {
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleChar() {
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("B", metaphone.metaphone("b"));
        assertEquals("K", metaphone.metaphone("c"));
    }

    public void testMetaphoneBasicWords() {
        assertEquals("KST", metaphone.metaphone("cost"));
        assertEquals("BR", metaphone.metaphone("berry"));
        assertEquals("JMS", metaphone.metaphone("James"));
    }

    public void testMetaphoneInitialGn() {
        assertEquals("N", metaphone.metaphone("gnaw"));
        assertEquals("N", metaphone.metaphone("gnome"));
    }

    public void testMetaphoneInitialKn() {
        assertEquals("N", metaphone.metaphone("knee"));
        assertEquals("N", metaphone.metaphone("know"));
    }

    public void testMetaphoneInitialPn() {
        assertEquals("N", metaphone.metaphone("pneumatic"));
    }

    public void testMetaphoneInitialAe() {
        assertEquals("E", metaphone.metaphone("aeon"));
    }

    public void testMetaphoneInitialWr() {
        assertEquals("R", metaphone.metaphone("wrong"));
        assertEquals("R", metaphone.metaphone("wrap"));
    }

    public void testMetaphoneInitialWh() {
        assertEquals("W", metaphone.metaphone("what"));
        assertEquals("W", metaphone.metaphone("when"));
    }

    public void testMetaphoneInitialX() {
        assertEquals("S", metaphone.metaphone("xavier"));
    }

    public void testMetaphoneB() {
        assertEquals("BM", metaphone.metaphone("bamb"));
        assertEquals("BK", metaphone.metaphone("back"));
        assertEquals("BM", metaphone.metaphone("bomb"));
    }

    public void testMetaphoneC() {
        assertEquals("SNS", metaphone.metaphone("science"));
        assertEquals("SK", metaphone.metaphone("school"));
        assertEquals("K", metaphone.metaphone("cat"));
        assertEquals("X", metaphone.metaphone("ciao"));
        assertEquals("S", metaphone.metaphone("center"));
        assertEquals("S", metaphone.metaphone("cycle"));
    }

    public void testMetaphoneD() {
        assertEquals("JT", metaphone.metaphone("edge"));
        assertEquals("TR", metaphone.metaphone("door"));
    }

    public void testMetaphoneG() {
        assertEquals("J", metaphone.metaphone("giant"));
        assertEquals("K", metaphone.metaphone("goat"));
        assertEquals("", metaphone.metaphone("agh"));
        assertEquals("K", metaphone.metaphone("egg"));
        assertEquals("N", metaphone.metaphone("gnome"));
        assertEquals("NK", metaphone.metaphone("ginkgo"));
    }

    public void testMetaphoneH() {
        assertEquals("H", metaphone.metaphone("hat"));
        assertEquals("", metaphone.metaphone("lah"));
        assertEquals("", metaphone.metaphone("fish"));
        assertEquals("", metaphone.metaphone("ghost"));
    }

    public void testMetaphoneK() {
        assertEquals("K", metaphone.metaphone("king"));
        assertEquals("K", metaphone.metaphone("cookie"));
        assertEquals("K", metaphone.metaphone("back"));
    }

    public void testMetaphoneP() {
        assertEquals("FS", metaphone.metaphone("philosophy"));
        assertEquals("P", metaphone.metaphone("pen"));
    }

    public void testMetaphoneQ() {
        assertEquals("K", metaphone.metaphone("quick"));
        assertEquals("K", metaphone.metaphone("queen"));
    }

    public void testMetaphoneS() {
        assertEquals("X", metaphone.metaphone("shoes"));
        assertEquals("XS", metaphone.metaphone("sugar"));
        assertEquals("S", metaphone.metaphone("sun"));
        assertEquals("X", metaphone.metaphone("sciatic"));
    }

    public void testMetaphoneT() {
        assertEquals("X", metaphone.metaphone("nation"));
        assertEquals("X", metaphone.metaphone("ratio"));
        assertEquals("0", metaphone.metaphone("thin"));
        assertEquals("T", metaphone.metaphone("top"));
        assertEquals("", metaphone.metaphone("tch"));
    }

    public void testMetaphoneV() {
        assertEquals("F", metaphone.metaphone("voice"));
        assertEquals("F", metaphone.metaphone("vocal"));
    }

    public void testMetaphoneW() {
        assertEquals("W", metaphone.metaphone("wait"));
        assertEquals("", metaphone.metaphone("low"));
    }

    public void testMetaphoneX() {
        assertEquals("KS", metaphone.metaphone("xmas"));
        assertEquals("KS", metaphone.metaphone("exact"));
    }

    public void testMetaphoneZ() {
        assertEquals("S", metaphone.metaphone("zoo"));
        assertEquals("S", metaphone.metaphone("zero"));
    }

    public void testMetaphoneMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
        assertEquals("KS", metaphone.metaphone("xmas"));
        metaphone.setMaxCodeLen(4); // restore
    }

    public void testMetaphoneMaxCodeLenEffect() {
        metaphone.setMaxCodeLen(2);
        String result = metaphone.metaphone("chemistry");
        assertTrue(result.length() <= 2);
        metaphone.setMaxCodeLen(4); // restore
    }

    public void testEncodeString() {
        assertEquals("", metaphone.encode((String) null));
        assertEquals("", metaphone.encode(""));
        assertEquals(metaphone.metaphone("test"), metaphone.encode("test"));
    }

    public void testEncodeObject() throws EncoderException {
        assertEquals(metaphone.metaphone("hello"), metaphone.encode((Object) "hello"));
    }

    public void testEncodeObjectNonString() {
        try {
            metaphone.encode(new Integer(123));
            fail("Expected EncoderException for non-String object");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().indexOf("Metaphone") >= 0);
        }
    }

    public void testIsMetaphoneEqualTrue() {
        assertTrue(metaphone.isMetaphoneEqual("center", "centre"));
        assertTrue(metaphone.isMetaphoneEqual("dol", "doll"));
    }

    public void testIsMetaphoneEqualFalse() {
        assertFalse(metaphone.isMetaphoneEqual("cat", "dog"));
        assertFalse(metaphone.isMetaphoneEqual("hello", "world"));
    }

    public void testIsMetaphoneEqualCaseSensitive() {
        assertTrue(metaphone.isMetaphoneEqual("HELLO", "hello"));
    }
}
