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

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
