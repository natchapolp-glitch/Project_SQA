package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class DoubleMetaphoneTest {

    @Test
    public void testDoubleMetaphoneBasicAndNull() {
        DoubleMetaphone dm = new DoubleMetaphone();
        assertNull(dm.doubleMetaphone(null));
        assertNull(dm.doubleMetaphone(""));
        assertNull(dm.doubleMetaphone("   "));
        
        assertEquals("SMTH", dm.doubleMetaphone("Smith"));
        assertEquals("SM", dm.doubleMetaphone("Smith", true));
    }

    @Test
    public void testMaxCodeLen() {
        DoubleMetaphone dm = new DoubleMetaphone();
        assertEquals(4, dm.getMaxCodeLen());
        
        dm.setMaxCodeLen(2);
        assertEquals(2, dm.getMaxCodeLen());
        assertEquals("SM", dm.doubleMetaphone("Smith"));
        
        dm.setMaxCodeLen(6);
        assertEquals(6, dm.getMaxCodeLen());
        assertEquals("SM00", dm.doubleMetaphone("Smith"));
    }

    @Test
    public void testIsDoubleMetaphoneEqual() {
        DoubleMetaphone dm = new DoubleMetaphone();
        assertTrue(dm.isDoubleMetaphoneEqual("Smith", "Smythe"));
        assertTrue(dm.isDoubleMetaphoneEqual("CIA", "CIA", true));
        assertFalse(dm.isDoubleMetaphoneEqual("Smith", "Jones"));
    }

    @Test
    public void testEncodeMethods() throws EncoderException {
        DoubleMetaphone dm = new DoubleMetaphone();
        assertEquals("SMTH", dm.encode("Smith"));
        
        Object encodedObj = dm.encode((Object) "Washington");
        assertNotNull(encodedObj);
        assertTrue(encodedObj instanceof String);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        DoubleMetaphone dm = new DoubleMetaphone();
        dm.encode(Integer.valueOf(123));
    }

    @Test
    public void testSpecialCasesAndConsonants() {
        DoubleMetaphone dm = new DoubleMetaphone();
        
        assertEquals("KK", dm.doubleMetaphone("caesar"));
        assertEquals("X", dm.doubleMetaphone("focaccia"));
        assertEquals("SK", dm.doubleMetaphone("Czerny"));
        assertEquals("N", dm.doubleMetaphone("\u00D1et"));
        assertEquals("S", dm.doubleMetaphone("\u00C7ade"));
    }

    @Test
    public void testSilentStartsAndSlavoGermanic() {
        DoubleMetaphone dm = new DoubleMetaphone();
        
        assertEquals("N", dm.doubleMetaphone("gnat"));
        assertEquals("N", dm.doubleMetaphone("knight"));
        assertEquals("N", dm.doubleMetaphone("pneumatic"));
        assertEquals("R", dm.doubleMetaphone("wrench"));
        assertEquals("S", dm.doubleMetaphone("psalm"));
    }
}
