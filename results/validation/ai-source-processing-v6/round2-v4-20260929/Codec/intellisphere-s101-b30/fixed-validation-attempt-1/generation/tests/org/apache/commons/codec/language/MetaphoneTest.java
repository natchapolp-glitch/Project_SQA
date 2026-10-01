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

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
