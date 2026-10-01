Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
org/apache/commons/codec/language/CaverphoneTest.java

```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() {
        caverphone = new Caverphone();
    }

    public void testCaverphoneNull() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    public void testCaverphoneEmptyString() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testCaverphoneSimple() {
        assertEquals("STFN111111", caverphone.caverphone("Stevenson"));
    }

    public void testCaverphonePeter() {
        assertEquals("PTA1111111", caverphone.caverphone("Peter"));
    }

    public void testCaverphoneDavid() {
        assertEquals("TFT1111111", caverphone.caverphone("David"));
    }

    public void testCaverphoneWithDigitsAndPunctuation() {
        assertEquals("STFN111111", caverphone.caverphone("Stevenson!@#123"));
    }

    public void testCaverphoneCaseInsensitive() {
        assertEquals(caverphone.caverphone("hello"), caverphone.caverphone("HELLO"));
    }

    public void testEncodeObjectString() throws EncoderException {
        Object result = caverphone.encode("Stevenson");
        assertTrue(result instanceof String);
        assertEquals("STFN111111", (String) result);
    }

    public void testEncodeObjectNonString() {
        try {
            caverphone.encode(new Integer(42));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testEncodeString() {
        assertEquals("STFN111111", caverphone.encode("Stevenson"));
    }

    public void testIsCaverphoneEqualTrue() {
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "Stevenson"));
    }

    public void testIsCaverphoneEqualFalse() {
        assertFalse(caverphone.isCaverphoneEqual("Stevenson", "David"));
    }

    public void testIsCaverphoneEqualBothNull() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
    }

    public void testIsCaverphoneEqualOneNull() {
        assertFalse(caverphone.isCaverphoneEqual(null, "David"));
    }

    public void testCaverphoneFinalE() {
        assertEquals("BRN1111111", caverphone.caverphone("brown"));
    }

    public void testCaverphonePrefixCough() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
    }

    public void testCaverphonePrefixGn() {
        assertEquals("N111111111", caverphone.caverphone("gnome"));
    }

    public void testCaverphonePrefixMb() {
        assertEquals("M111111111", caverphone.caverphone("mb"));
    }

    public void testCaverphoneCq() {
        assertEquals("K111111111", caverphone.caverphone("acquit"));
    }
}

```

```java
org/apache/commons/codec/language/MetaphoneTest.java

```

```java
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

```

```java
org/apache/commons/codec/language/SoundexUtilsTest.java

```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest extends TestCase {

    public void testCleanNull() {
        assertNull(SoundexUtils.clean(null));
    }

    public void testCleanEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLetters() {
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
    }

    public void testCleanLowerCase() {
        assertEquals("HELLO", SoundexUtils.clean("hello"));
    }

    public void testCleanWithNonLetters() {
        assertEquals("HELLO", SoundexUtils.clean("He1llo2!@#"));
    }

    public void testCleanOnlyNonLetters() {
        assertEquals("", SoundexUtils.clean("123!@#"));
    }

    public void testCleanMixedCase() {
        assertEquals("ABC", SoundexUtils.clean("aBc"));
    }

    public void testCleanPreservesLetterOrder() {
        assertEquals("AC", SoundexUtils.clean("a1b2c"));
    }

    public void testDifferenceEncodedBothNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedFirstNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
    }

    public void testDifferenceEncodedSecondNull() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
    }

    public void testDifferenceEncodedIdentical() {
        assertEquals(20, SoundexUtils.differenceEncoded("ABCDEFGHIJKLMNOPQRST", "ABCDEFGHIJKLMNOPQRST"));
    }

    public void testDifferenceEncodedNoMatch() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", "XYZ"));
    }

    public void testDifferenceEncodedLengthMismatch() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABC"));
    }

    public void testDifferenceEncodedLongerFirst() {
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "ABCD"));
    }

    public void testDifferenceEncodedEmptyStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedWithEmptyString() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", ""));
    }

    public void testDifferenceWithCaVerphoneEncoder() throws EncoderException {
        stringEncoder = new Caverphone();
        assertEquals(5, SoundexUtils.difference(stringEncoder, "hello", "hello"));
    }

    public void testDifferenceWithMetaphoneEncoder() throws EncoderException {
        StringEncoder encoder = new Metaphone();
        // "Knight" and "Night" both encode to "N"
        assertEquals(1, SoundexUtils.difference(encoder, "Knight", "Night"));
    }

    private StringEncoder stringEncoder;

    public void testDifferenceWithEncoderException() {
        StringEncoder throwingEncoder = new StringEncoder() {
            public String encode(String s) throws EncoderException {
                throw new EncoderException("Test exception");
            }

            public Object encode(Object obj) throws EncoderException {
                throw new EncoderException("Test exception");
            }
        };
        try {
            SoundexUtils.difference(throwingEncoder, "hello", "world");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    public void testCleanWithSpaceOnly() {
        assertEquals("", SoundexUtils.clean("   "));
    }

    public void testCleanWithUnicodeLetter() {
        assertEquals("AE", SoundexUtils.clean("a\u00e9"));
    }
}

```

```java
org/apache/commons/codec/language/SoundexUtilsTest.java

```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest extends TestCase {

    public void testCleanNull() {
        assertNull(SoundexUtils.clean(null));
    }

    public void testCleanEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLetters() {
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
    }

    public void testCleanLowerCase() {
        assertEquals("HELLO", SoundexUtils.clean("hello"));
    }

    public void testCleanWithNonLetters() {
        assertEquals("HELLO", SoundexUtils.clean("He1llo2!@#"));
    }

    public void testCleanOnlyNonLetters() {
        assertEquals("", SoundexUtils.clean("123!@#"));
    }

    public void testCleanMixedCase() {
        assertEquals("ABC", SoundexUtils.clean("aBc"));
    }

    public void testCleanPreservesLetterOrder() {
        assertEquals("AC", SoundexUtils.clean("a1b2c"));
    }

    public void testCleanWithSpaceOnly() {
        assertEquals("", SoundexUtils.clean("   "));
    }

    public void testCleanWithUnicodeLetter() {
        assertEquals("AE", SoundexUtils.clean("a\u00e9"));
    }

    public void testDifferenceEncodedBothNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedFirstNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
    }

    public void testDifferenceEncodedSecondNull() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
    }

    public void testDifferenceEncodedIdentical() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABC"));
    }

    public void testDifferenceEncodedNoMatch() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", "XYZ"));
    }

    public void testDifferenceEncodedLengthMismatch() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABC"));
    }

    public void testDifferenceEncodedLongerFirst() {
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "ABCD"));
    }

    public void testDifferenceEncodedEmptyStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedWithEmptyString() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", ""));
    }

    public void testDifferenceWithCaVerphoneEncoder() throws EncoderException {
        StringEncoder encoder = new Caverphone();
        // Caverphone encoding of "hello" is identical to itself
        assertEquals(10, SoundexUtils.difference(encoder, "hello", "hello"));
    }

    public void testDifferenceWithMetaphoneEncoder() throws EncoderException {
        StringEncoder encoder = new Metaphone();
        // "Knight" and "Night" both encode to "N"
        assertEquals(1, SoundexUtils.difference(encoder, "Knight", "Night"));
    }

    public void testDifferenceWithEncoderThrowsException() {
        StringEncoder throwingEncoder = new StringEncoder() {
            public String encode(String s) throws EncoderException {
                throw new EncoderException("Test exception");
            }

            public Object encode(Object obj) throws EncoderException {
                throw new EncoderException("Test exception");
            }
        };
        try {
            SoundexUtils.difference(throwingEncoder, "hello", "world");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }
}

```

```java
txt = txt.replaceAll("s+", "S");
txt = txt.replaceAll("t+", "T");

```