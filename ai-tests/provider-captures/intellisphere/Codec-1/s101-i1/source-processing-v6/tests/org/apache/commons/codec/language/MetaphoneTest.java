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

    

    

    

    

    

    

    

    public void testMetaphoneMaxCodeLenDefault() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
