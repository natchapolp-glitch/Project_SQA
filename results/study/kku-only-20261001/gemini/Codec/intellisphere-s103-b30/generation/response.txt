An independent regression test suite for `Caverphone`, `Metaphone`, and `SoundexUtils` using JUnit 3.8.1 (compatible with Defects4J Codec 1f build environment).

### `org/apache/commons/codec/language/CaverphoneRegressionTest.java`

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneRegressionTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.caverphone = new Caverphone();
    }

    public void testCaverphoneNullAndEmpty() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("   "));
        assertEquals("1111111111", caverphone.caverphone("12345!@#$%"));
    }

    public void testCaverphoneEncodeObject() throws Exception {
        Object result = caverphone.encode((Object) "Lee");
        assertTrue(result instanceof String);
        assertEquals(caverphone.caverphone("Lee"), result);
    }

    public void testCaverphoneEncodeObjectInvalidType() {
        try {
            caverphone.encode(new Integer(42));
            fail("Expected EncoderException for non-String parameter");
        } catch (EncoderException e) {
            // expected
        }

        try {
            caverphone.encode((Object) null);
            fail("Expected EncoderException for null Object parameter");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testCaverphoneEncodeString() {
        assertEquals(caverphone.caverphone("Stevenson"), caverphone.encode("Stevenson"));
        assertEquals("1111111111", caverphone.encode(""));
    }

    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Lee", "Li"));
        assertTrue(caverphone.isCaverphoneEqual("Peter", "Peter"));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("Peter", "Stevenson"));
        assertFalse(caverphone.isCaverphoneEqual("Smith", "Lee"));
    }

    public void testCaverphoneStartPrefixes() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
        assertEquals(10, caverphone.caverphone("rough").length());
        assertEquals(10, caverphone.caverphone("tough").length());
        assertEquals(10, caverphone.caverphone("enough").length());
        assertEquals(10, caverphone.caverphone("trough").length());
        assertEquals(10, caverphone.caverphone("gnome").length());
        assertEquals(10, caverphone.caverphone("mbappe").length());
    }

    public void testCaverphoneReplacements() {
        assertEquals(10, caverphone.caverphone("acquire").length());
        assertEquals(10, caverphone.caverphone("circle").length());
        assertEquals(10, caverphone.caverphone("center").length());
        assertEquals(10, caverphone.caverphone("cyan").length());
        assertEquals(10, caverphone.caverphone("catch").length());
        assertEquals(10, caverphone.caverphone("phone").length());
        assertEquals(10, caverphone.caverphone("edge").length());
        assertEquals(10, caverphone.caverphone("nation").length());
        assertEquals(10, caverphone.caverphone("spatial").length());
    }

    public void testCaverphoneEndings() {
        // Final 'e' is dropped: "fade" and "fad" produce identical encoding
        assertEquals(caverphone.caverphone("fad"), caverphone.caverphone("fade"));
        assertTrue(caverphone.isCaverphoneEqual("fade", "fad"));
    }

    public void testCaverphoneNonAscii() {
        assertEquals(caverphone.caverphone("hello"), caverphone.caverphone("123hello!@#"));
    }
}
```

### `org/apache/commons/codec/language/MetaphoneRegressionTest.java`

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class MetaphoneRegressionTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.metaphone = new Metaphone();
    }

    public void testMetaphoneNullAndEmpty() {
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
    }

    public void testMetaphoneSingleChar() {
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("Z", metaphone.metaphone("Z"));
        assertEquals("B", metaphone.metaphone("b"));
    }

    public void testMetaphoneInitialSpecialCases() {
        assertEquals("NT", metaphone.metaphone("knight"));
        assertEquals("NT", metaphone.metaphone("gnat"));
        assertEquals("RT", metaphone.metaphone("wright"));
        assertTrue(metaphone.metaphone("pneumonia").startsWith("N"));
        assertTrue(metaphone.metaphone("aegis").startsWith("E"));
        assertTrue(metaphone.metaphone("white").startsWith("W"));
        assertTrue(metaphone.metaphone("xavier").startsWith("S"));
    }

    public void testMetaphoneEncodeObject() throws Exception {
        Object result = metaphone.encode((Object) "testing");
        assertTrue(result instanceof String);
        assertEquals(metaphone.metaphone("testing"), result);

        try {
            metaphone.encode(new Long(100));
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException e) {
            // expected
        }

        try {
            metaphone.encode((Object) null);
            fail("Expected EncoderException for null Object input");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testMetaphoneEncodeString() {
        assertEquals(metaphone.metaphone("matrix"), metaphone.encode("matrix"));
        assertEquals("", metaphone.encode(""));
    }

    public void testIsMetaphoneEqual() {
        assertTrue(metaphone.isMetaphoneEqual("wright", "right"));
        assertTrue(metaphone.isMetaphoneEqual("knight", "night"));
        assertTrue(metaphone.isMetaphoneEqual(null, ""));
        assertFalse(metaphone.isMetaphoneEqual("apple", "orange"));
    }

    public void testMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
        assertEquals("0MPSN", metaphone.metaphone("THOMPSON"));

        metaphone.setMaxCodeLen(2);
        assertEquals("0M", metaphone.metaphone("THOMPSON"));
    }

    public void testMetaphoneSpecialC() {
        assertEquals("SKL", metaphone.metaphone("school"));
        assertEquals("XK", metaphone.metaphone("check"));
        assertEquals("SPXL", metaphone.metaphone("special"));
    }

    public void testMetaphoneSpecialD() {
        assertEquals("EJ", metaphone.metaphone("edge"));
        assertEquals("TJ", metaphone.metaphone("dodge"));
        assertEquals("TR", metaphone.metaphone("door"));
    }

    public void testMetaphoneSpecialG() {
        assertEquals("SN", metaphone.metaphone("sign"));
        assertEquals("H", metaphone.metaphone("high"));
        assertEquals("JNT", metaphone.metaphone("giant"));
        assertEquals("JM", metaphone.metaphone("gym"));
    }

    public void testMetaphoneSpecialT() {
        assertEquals("0TR", metaphone.metaphone("theater"));
        assertEquals("WX", metaphone.metaphone("watch"));
        assertEquals("RX", metaphone.metaphone("ratio"));
    }

    public void testMetaphoneVowelsAndSilentWY() {
        assertEquals("ORNJ", metaphone.metaphone("orange"));
        assertEquals("APL", metaphone.metaphone("apple"));
        assertEquals("K", metaphone.metaphone("cow"));
        assertEquals("T", metaphone.metaphone("day"));
    }
}
```

### `org/apache/commons/codec/language/SoundexUtilsRegressionTest.java`

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsRegressionTest extends TestCase {

    public void testCleanNullAndEmpty() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLetters() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("XYZ", SoundexUtils.clean("XYZ"));
        assertEquals("HELLOWORLD", SoundexUtils.clean("HelloWORLD"));
    }

    public void testCleanMixedAndPunctuation() {
        assertEquals("ABC", SoundexUtils.clean("a-b-c"));
        assertEquals("HELLO", SoundexUtils.clean("123hello 456"));
        assertEquals("", SoundexUtils.clean("!@#$%^&*()1234567890"));
    }

    public void testDifferenceEncodedNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedMatches() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(1, SoundexUtils.differenceEncoded("A", "A"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "WXYZ"));
    }

    public void testDifferenceEncodedDifferentLengths() {
        assertEquals(2, SoundexUtils.differenceEncoded("ABCDE", "AB"));
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "ABCDE"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABC", "A"));
    }

    public void testDifferenceWithStringEncoder() throws Exception {
        StringEncoder encoder = new Metaphone();
        assertEquals(2, SoundexUtils.difference(encoder, "wright", "right"));
        assertEquals(2, SoundexUtils.difference(encoder, "night", "knight"));
        assertEquals(0, SoundexUtils.difference(encoder, "cat", "dog"));
    }

    public void testDifferenceWithNullInputs() throws Exception {
        StringEncoder encoder = new Metaphone();
        assertEquals(0, SoundexUtils.difference(encoder, null, "test"));
        assertEquals(0, SoundexUtils.difference(encoder, "test", null));
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }
}
```

### References
- Apache Commons Codec: `org.apache.commons.codec.language.Caverphone`
- Apache Commons Codec: `org.apache.commons.codec.language.Metaphone`
- Apache Commons Codec