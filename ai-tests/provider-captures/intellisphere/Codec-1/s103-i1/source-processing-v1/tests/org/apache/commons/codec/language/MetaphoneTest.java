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

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
