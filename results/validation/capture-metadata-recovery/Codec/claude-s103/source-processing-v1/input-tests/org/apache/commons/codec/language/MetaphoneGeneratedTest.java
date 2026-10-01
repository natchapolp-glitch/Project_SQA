package org.apache.commons.codec.language;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class MetaphoneGeneratedTest extends TestCase {

    private Metaphone encoder;

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new Metaphone();
    }

    private Object invoke(String name, Class[] types, Object[] args) throws Exception {
        Method m = Metaphone.class.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m.invoke(encoder, args);
    }

    private boolean invokeBoolean(String name, Class[] types, Object[] args) throws Exception {
        return ((Boolean) invoke(name, types, args)).booleanValue();
    }

    public void testMetaphoneNullEmptyAndSingleChar() {
        assertEquals("", encoder.metaphone(null));
        assertEquals("", encoder.metaphone(""));
        assertEquals("A", encoder.metaphone("a"));
        assertEquals("X", encoder.metaphone("x"));
    }

    public void testMetaphoneInitialExceptions() {
        assertEquals("NT", encoder.metaphone("Knight"));
        assertEquals("NT", encoder.metaphone("knight"));
        assertEquals("RT", encoder.metaphone("Wright"));
        assertEquals("WL", encoder.metaphone("Wheel"));
        assertEquals("SFR", encoder.metaphone("Xavier"));
        assertEquals("EBRS", encoder.metaphone("Aebersold"));
    }

    public void testMetaphoneCAndDgRules() {
        assertEquals("0MPS", encoder.metaphone("Thompson"));
        assertEquals("FN", encoder.metaphone("Phone"));
        assertEquals("SNS", encoder.metaphone("Science"));
        assertEquals("KRX", encoder.metaphone("Church"));
        assertEquals("KK", encoder.metaphone("Quick"));
        assertEquals("TJ", encoder.metaphone("Dodge"));
        assertEquals("JJ", encoder.metaphone("Judge"));
        assertEquals("ATNX", encoder.metaphone("Attention"));
    }

    public void testMetaphoneSilentLettersAndDuplicates() {
        assertEquals("LM", encoder.metaphone("Lamb"));
        assertEquals("NT", encoder.metaphone("Night"));
        assertEquals("BBL", encoder.metaphone("Bubble"));
    }

    public void testSetAndGetMaxCodeLen() {
        assertEquals(4, encoder.getMaxCodeLen());

        encoder.setMaxCodeLen(2);
        assertEquals(2, encoder.getMaxCodeLen());
        assertEquals("0M", encoder.metaphone("Thompson"));

        encoder.setMaxCodeLen(6);
        assertEquals(6, encoder.getMaxCodeLen());
        assertEquals("0MPSN", encoder.metaphone("Thompson"));
    }

    public void testEncodeString() {
        assertEquals("NT", encoder.encode("Knight"));
        assertEquals("", encoder.encode((String) null));
        assertEquals(encoder.metaphone("Phone"), encoder.encode("Phone"));
    }

    public void testEncodeObject() throws Exception {
        assertEquals("NT", encoder.encode((Object) "Knight"));

        try {
            encoder.encode(new Integer(42));
            fail("Expected EncoderException for non-String parameter");
        } catch (EncoderException expected) {
            // expected
        }

        try {
            encoder.encode((Object) null);
            fail("Expected EncoderException for null parameter");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsMetaphoneEqual() {
        assertTrue(encoder.isMetaphoneEqual("Case", "Cause"));
        assertTrue(encoder.isMetaphoneEqual("Knight", "Night"));
        assertTrue(encoder.isMetaphoneEqual("Thompson", "Thumpson"));
        assertFalse(encoder.isMetaphoneEqual("Cat", "Dog"));
        assertTrue(encoder.isMetaphoneEqual(null, ""));
        assertTrue(encoder.isMetaphoneEqual(null, null));
    }

    public void testIsLastCharPrivate() throws Exception {
        Class[] types = new Class[] { Integer.TYPE, Integer.TYPE };
        assertTrue(invokeBoolean("isLastChar", types,
                new Object[] { new Integer(5), new Integer(4) }));
        assertFalse(invokeBoolean("isLastChar", types,
                new Object[] { new Integer(5), new Integer(2) }));
        assertFalse(invokeBoolean("isLastChar", types,
                new Object[] { new Integer(5), new Integer(5) }));
    }

    public void testIsNextCharPrivate() throws Exception {
        Class[] types = new Class[] { StringBuffer.class, Integer.TYPE, Character.TYPE };
        StringBuffer sb = new StringBuffer("ABC");
        assertTrue(invokeBoolean("isNextChar", types,
                new Object[] { sb, new Integer(0), new Character('B') }));
        assertTrue(invokeBoolean("isNextChar", types,
                new Object[] { sb, new Integer(1), new Character('C') }));
        assertFalse(invokeBoolean("isNextChar", types,
                new Object[] { sb, new Integer(0), new Character('C') }));
        // last index has no next character
        assertFalse(invokeBoolean("isNextChar", types,
                new Object[] { sb, new Integer(2), new Character('C') }));
        // negative index
        assertFalse(invokeBoolean("isNextChar", types,
                new Object[] { sb, new Integer(-1), new Character('A') }));
    }

    public void testIsPreviousCharPrivate() throws Exception {
        Class[] types = new Class[] { StringBuffer.class, Integer.TYPE, Character.TYPE };
        StringBuffer sb = new StringBuffer("ABC");
        assertTrue(invokeBoolean("isPreviousChar", types,
                new Object[] { sb, new Integer(1), new Character('A') }));
        assertTrue(invokeBoolean("isPreviousChar", types,
                new Object[] { sb, new Integer(2), new Character('B') }));
        assertFalse(invokeBoolean("isPreviousChar", types,
                new Object[] { sb, new Integer(2), new Character('A') }));
        // index 0 has no previous character
        assertFalse(invokeBoolean("isPreviousChar", types,
                new Object[] { sb, new Integer(0), new Character('A') }));
        // index equal to length is out of range
        assertFalse(invokeBoolean("isPreviousChar", types,
                new Object[] { sb, new Integer(3), new Character('C') }));
    }

    public void testIsVowelPrivate() throws Exception {
        Class[] types = new Class[] { StringBuffer.class, Integer.TYPE };
        StringBuffer vowels = new StringBuffer("AEIOU");
        for (int i = 0; i < vowels.length(); i++) {
            assertTrue("index " + i, invokeBoolean("isVowel", types,
                    new Object[] { vowels, new Integer(i) }));
        }
        StringBuffer consonants = new StringBuffer("BCD");
        assertFalse(invokeBoolean("isVowel", types,
                new Object[] { consonants, new Integer(0) }));
        // lowercase is not treated as a vowel
        assertFalse(invokeBoolean("isVowel", types,
                new Object[] { new StringBuffer("a"), new Integer(0) }));

        try {
            invoke("isVowel", types, new Object[] { consonants, new Integer(10) });
            fail("Expected an index exception");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IndexOutOfBoundsException);
        }
    }

    public void testRegionMatchPrivate() throws Exception {
        Class[] types = new Class[] { StringBuffer.class, Integer.TYPE, String.class };
        StringBuffer sb = new StringBuffer("HELLO");
        assertTrue(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(0), "HEL" }));
        assertTrue(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(1), "ELL" }));
        assertTrue(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(3), "LO" }));
        assertFalse(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(3), "LOX" }));
        // region would extend past the end
        assertFalse(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(4), "LO" }));
        // negative index
        assertFalse(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(-1), "H" }));
        // case-sensitive
        assertFalse(invokeBoolean("regionMatch", types,
                new Object[] { sb, new Integer(0), "hel" }));
    }
}
