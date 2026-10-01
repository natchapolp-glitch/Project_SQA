// src/test/org/apache/commons/codec/language/SoundexUtilsTest.java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

public class SoundexUtilsTest extends TestCase {

    public void testCleanNull() {
        assertNull(SoundexUtils.clean(null));
    }

    public void testCleanEmpty() {
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanUpperCase() {
        assertEquals("HELLO", SoundexUtils.clean("hello"));
        assertEquals("WORLD", SoundexUtils.clean("worLd"));
        assertEquals("JAVA", SoundexUtils.clean("JAVA"));
    }

    public void testCleanWithDigits() {
        assertEquals("TEST", SoundexUtils.clean("test123"));
        assertEquals("HELLO", SoundexUtils.clean("h3el7lo"));
    }

    public void testCleanWithPunctuation() {
        assertEquals("HELLO", SoundexUtils.clean("hel-lo"));
        assertEquals("WORLD", SoundexUtils.clean("wor'ld"));
        assertEquals("CODEC", SoundexUtils.clean("co.dec"));
    }

    public void testCleanWithWhitespace() {
        assertEquals("HELLO", SoundexUtils.clean("hel lo"));
        assertEquals("OPEN", SoundexUtils.clean(" o p e n "));
    }

    public void testCleanOnlyLetters() {
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
                     SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
                     SoundexUtils.clean("abcdefghijklmnopqrstuvwxyz"));
    }

    public void testCleanSpecialCharacters() {
        assertEquals("TEST", SoundexUtils.clean("t@e#s$t%^"));
        assertEquals("HELLO", SoundexUtils.clean("{hello}"));
    }

    public void testCleanMixedInput() {
        assertEquals("HELLO123WORLD", SoundexUtils.clean("Hello 123 World!!!"));
        assertEquals("APACHECOMMONS", SoundexUtils.clean("Apache, Commons!"));
    }

    public void testDifferenceEncodedNullFirst() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "test"));
    }

    public void testDifferenceEncodedNullSecond() {
        assertEquals(0, SoundexUtils.differenceEncoded("test", null));
    }

    public void testDifferenceEncodedBothNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedEmpty() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", "test"));
        assertEquals(0, SoundexUtils.differenceEncoded("test", ""));
    }

    public void testDifferenceEncodedIdentical() {
        assertEquals(4, SoundexUtils.differenceEncoded("TEST", "TEST"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABC"));
    }

    public void testDifferenceEncodedPartialMatch() {
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABCD", "AXXX"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "XXXX"));
    }

    public void testDifferenceEncodedDifferentLengths() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABCDEF"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCDEF", "ABC"));
    }

    public void testDifferenceWithSoundex() throws Exception {
        Soundex soundex = new Soundex();
        assertEquals(4, SoundexUtils.difference(soundex, "Smith", "Smythe"));
        assertEquals(4, SoundexUtils.difference(soundex, "Robert", "Rupert"));
    }

    public void testDifferenceWithRefinedSoundex() throws Exception {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        int result = SoundexUtils.difference(refinedSoundex, "hello", "hallo");
        assertTrue(result >= 0);
    }

    public void testDifferenceWithMetaphone() throws Exception {
        Metaphone metaphone = new Metaphone();
        int result = SoundexUtils.difference(metaphone, "center", "centre");
        assertTrue(result > 0);
    }
}
