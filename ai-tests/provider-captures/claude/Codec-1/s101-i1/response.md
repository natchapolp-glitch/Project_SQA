Every test below targets JUnit 3.8.1 (`TestCase`, no annotations) and Java 1.6-compatible syntax, as `pom.xml` and `build.xml` require. Four points about the tests:
- **Private methods:** the private `Metaphone` helpers (`isLastChar`, `isNextChar`, `isPreviousChar`, `isVowel`, `regionMatch`) are called through reflection.
- **Same package:** `SoundexUtils` is package-private, so its test lives in the same package.
- **Expected values:** I traced each one by hand from the supplied source (for example, `metaphone("Thompson")` is `"0MPS"` in this revision), but I could not compile or run them.
- **Test count:** the response has 30 test methods in total (8 + 13 + 9).

`org/apache/commons/codec/language/CaverphoneTest.java`
```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone encoder;

    public CaverphoneTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new Caverphone();
    }

    public void testCaverphoneNullEmptyAndNoLetters() {
        assertEquals("1111111111", encoder.caverphone(null));
        assertEquals("1111111111", encoder.caverphone(""));
        assertEquals("1111111111", encoder.caverphone("123"));
    }

    public void testCaverphoneThompsonCaseAndPunctuation() {
        assertEquals("TMPSN11111", encoder.caverphone("Thompson"));
        assertEquals("TMPSN11111", encoder.caverphone("THOMPSON"));
        assertEquals("TMPSN11111", encoder.caverphone("Thomp-son"));
    }

    public void testCaverphoneStevenson() {
        assertEquals("STFNSN1111", encoder.caverphone("Stevenson"));
    }

    public void testCaverphoneSpecialStarts() {
        assertEquals("KF11111111", encoder.caverphone("cough"));
        assertEquals("ANF1111111", encoder.caverphone("enough"));
    }

    public void testCaverphoneFinalVowelBecomesA() {
        assertEquals("LA11111111", encoder.caverphone("Lee"));
    }

    public void testEncodeString() {
        assertEquals(encoder.caverphone("Thompson"), encoder.encode("Thompson"));
        assertEquals("1111111111", encoder.encode((String) null));
    }

    public void testEncodeObject() throws Exception {
        assertEquals("TMPSN11111", encoder.encode((Object) "Thompson"));
        try {
            encoder.encode(Integer.valueOf(42));
            fail("EncoderException expected for non-String");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            encoder.encode((Object) null);
            fail("EncoderException expected for null Object");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(encoder.isCaverphoneEqual("Stevenson", "Stephenson"));
        assertTrue(encoder.isCaverphoneEqual("Thompson", "THOMPSON"));
        assertFalse(encoder.isCaverphoneEqual("Thompson", "Smith"));
        assertTrue(encoder.isCaverphoneEqual(null, ""));
        assertTrue(encoder.isCaverphoneEqual(null, null));
    }
}
```

`org/apache/commons/codec/language/MetaphoneTest.java`
```java
package org.apache.commons.codec.language;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class MetaphoneTest extends TestCase {

    private Metaphone encoder;

    public MetaphoneTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new Metaphone();
    }

    private Object invoke(String name, Class<?>[] types, Object[] args) throws Exception {
        Method m = Metaphone.class.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m.invoke(encoder, args);
    }

    private boolean invokeBool(String name, Class<?>[] types, Object[] args) throws Exception {
        return ((Boolean) invoke(name, types, args)).booleanValue();
    }

    public void testMetaphoneNullEmptySingleChar() {
        assertEquals("", encoder.metaphone(null));
        assertEquals("", encoder.metaphone(""));
        assertEquals("A", encoder.metaphone("a"));
        assertEquals("X", encoder.metaphone("x"));
    }

    public void testMetaphoneInitialExceptions() {
        assertEquals("NT", encoder.metaphone("Knight"));
        assertEquals("RT", encoder.metaphone("Wright"));
        assertEquals("SFR", encoder.metaphone("Xavier"));
        assertEquals("EBRS", encoder.metaphone("Aebersold"));
        assertEquals("0MPS", encoder.metaphone("Thompson"));
    }

    public void testMetaphoneSpecialConsonantRules() {
        assertEquals("FN", encoder.metaphone("Phone"));
        assertEquals("SNS", encoder.metaphone("Science"));
        assertEquals("KRX", encoder.metaphone("Church"));
        assertEquals("JJ", encoder.metaphone("Judge"));
        assertEquals("TM", encoder.metaphone("Dumb"));
        assertEquals("KSXN", encoder.metaphone("Question"));
        assertEquals("BBL", encoder.metaphone("Bubble"));
    }

    public void testMaxCodeLenGetterAndSetter() {
        assertEquals(4, encoder.getMaxCodeLen());
        encoder.setMaxCodeLen(6);
        assertEquals(6, encoder.getMaxCodeLen());
    }

    public void testMetaphoneRespectsMaxCodeLen() {
        encoder.setMaxCodeLen(2);
        assertEquals("0M", encoder.metaphone("Thompson"));
        encoder.setMaxCodeLen(10);
        assertEquals("0MPSN", encoder.metaphone("Thompson"));
    }

    public void testEncodeString() {
        assertEquals("0MPS", encoder.encode("Thompson"));
        assertEquals("", encoder.encode((String) null));
    }

    public void testEncodeObject() throws Exception {
        assertEquals("0MPS", encoder.encode((Object) "Thompson"));
        try {
            encoder.encode(Integer.valueOf(1));
            fail("EncoderException expected for non-String");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            encoder.encode((Object) null);
            fail("EncoderException expected for null Object");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsMetaphoneEqual() {
        assertTrue(encoder.isMetaphoneEqual("Knight", "Night"));
        assertFalse(encoder.isMetaphoneEqual("Thompson", "Knight"));
        assertTrue(encoder.isMetaphoneEqual(null, ""));
    }

    public void testPrivateIsLastChar() throws Exception {
        Class<?>[] types = new Class<?>[] { int.class, int.class };
        assertTrue(invokeBool("isLastChar", types, new Object[] { Integer.valueOf(3), Integer.valueOf(2) }));
        assertFalse(invokeBool("isLastChar", types, new Object[] { Integer.valueOf(3), Integer.valueOf(1) }));
        assertTrue(invokeBool("isLastChar", types, new Object[] { Integer.valueOf(0), Integer.valueOf(-1) }));
    }

    public void testPrivateIsVowel() throws Exception {
        Class<?>[] types = new Class<?>[] { StringBuffer.class, int.class };
        StringBuffer sb = new StringBuffer("HELLO");
        assertFalse(invokeBool("isVowel", types, new Object[] { sb, Integer.valueOf(0) }));
        assertTrue(invokeBool("isVowel", types, new Object[] { sb, Integer.valueOf(1) }));
        assertTrue(invokeBool("isVowel", types, new Object[] { sb, Integer.valueOf(4) }));
        // lower-case vowels are not recognised
        assertFalse(invokeBool("isVowel", types,
                new Object[] { new StringBuffer("e"), Integer.valueOf(0) }));
        try {
            invoke("isVowel", types, new Object[] { sb, Integer.valueOf(5) });
            fail("index out of range expected");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IndexOutOfBoundsException);
        }
    }

    public void testPrivateIsPreviousChar() throws Exception {
        Class<?>[] types = new Class<?>[] { StringBuffer.class, int.class, char.class };
        StringBuffer sb = new StringBuffer("ABC");
        assertTrue(invokeBool("isPreviousChar", types,
                new Object[] { sb, Integer.valueOf(1), Character.valueOf('A') }));
        assertTrue(invokeBool("isPreviousChar", types,
                new Object[] { sb, Integer.valueOf(2), Character.valueOf('B') }));
        assertFalse(invokeBool("isPreviousChar", types,
                new Object[] { sb, Integer.valueOf(2), Character.valueOf('C') }));
        assertFalse(invokeBool("isPreviousChar", types,
                new Object[] { sb, Integer.valueOf(0), Character.valueOf('A') }));
        assertFalse(invokeBool("isPreviousChar", types,
                new Object[] { sb, Integer.valueOf(3), Character.valueOf('C') }));
    }

    public void testPrivateIsNextChar() throws Exception {
        Class<?>[] types = new Class<?>[] { StringBuffer.class, int.class, char.class };
        StringBuffer sb = new StringBuffer("ABC");
        assertTrue(invokeBool("isNextChar", types,
                new Object[] { sb, Integer.valueOf(0), Character.valueOf('B') }));
        assertTrue(invokeBool("isNextChar", types,
                new Object[] { sb, Integer.valueOf(1), Character.valueOf('C') }));
        assertFalse(invokeBool("isNextChar", types,
                new Object[] { sb, Integer.valueOf(0), Character.valueOf('C') }));
        assertFalse(invokeBool("isNextChar", types,
                new Object[] { sb, Integer.valueOf(2), Character.valueOf('C') }));
        assertFalse(invokeBool("isNextChar", types,
                new Object[] { sb, Integer.valueOf(-1), Character.valueOf('A') }));
    }

    public void testPrivateRegionMatch() throws Exception {
        Class<?>[] types = new Class<?>[] { StringBuffer.class, int.class, String.class };
        StringBuffer sb = new StringBuffer("ABCDEF");
        assertTrue(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(0), "ABC" }));
        assertTrue(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(2), "CDE" }));
        assertTrue(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(4), "EF" }));
        assertFalse(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(4), "EFG" }));
        assertFalse(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(2), "CDX" }));
        assertFalse(invokeBool("regionMatch", types, new Object[] { sb, Integer.valueOf(-1), "AB" }));
    }
}
```

`org/apache/commons/codec/language/SoundexUtilsTest.java`
```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest extends TestCase {

    public SoundexUtilsTest(String name) {
        super(name);
    }

    public void testCleanNullAndEmpty() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanRemovesNonLettersAndUppercases() {
        assertEquals("ABC", SoundexUtils.clean("a-b c1"));
        assertEquals("", SoundexUtils.clean("123 -!"));
        assertEquals("OBRIEN", SoundexUtils.clean("O'Brien"));
    }

    public void testCleanAllLettersUppercased() {
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("\u00c9", SoundexUtils.clean("\u00e9"));
    }

    public void testDifferenceEncodedNullArguments() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "S530"));
        assertEquals(0, SoundexUtils.differenceEncoded("S530", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedCountsPositionalMatches() {
        assertEquals(4, SoundexUtils.differenceEncoded("S530", "S530"));
        assertEquals(3, SoundexUtils.differenceEncoded("S530", "S532"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "WXYZ"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "BCDA"));
    }

    public void testDifferenceEncodedDifferentLengths() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABCDEF"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCDEF", "ABC"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", "ABC"));
    }

    public void testDifferenceWithCaverphoneEncoder() throws Exception {
        Caverphone encoder = new Caverphone();
        assertEquals(10, SoundexUtils.difference(encoder, "Thompson", "Thompson"));
        // TMPSN11111 vs STFNSN1111 -> only the trailing four '1' characters align
        assertEquals(4, SoundexUtils.difference(encoder, "Thompson", "Stevenson"));
    }

    public void testDifferenceWithNullResultsAndNullEncoder() throws Exception {
        StringEncoder nullEncoder = new StringEncoder() {
            public Object encode(Object pObject) throws EncoderException {
                return null;
            }

            public String encode(String pString) throws EncoderException {
                return null;
            }
        };
        assertEquals(0, SoundexUtils.difference(nullEncoder, "a", "b"));
        try {
            SoundexUtils.difference(null, "a", "b");
            fail("NullPointerException expected for null encoder");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    public void testDifferencePropagatesEncoderException() {
        StringEncoder failing = new StringEncoder() {
            public Object encode(Object pObject) throws EncoderException {
                throw new EncoderException("boom");
            }

            public String encode(String pString) throws EncoderException {
                throw new EncoderException("boom");
            }
        };
        try {
            SoundexUtils.difference(failing, "a", "b");
            fail("EncoderException expected");
        } catch (EncoderException e) {
            assertEquals("boom", e.getMessage());
        }
    }
}
```

Assumptions and limits:
- `StringEncoder` is assumed to declare `String encode(String) throws EncoderException` and inherit `Object encode(Object)` from `Encoder`, since `Caverphone` and `Metaphone` implement exactly those two methods. That interface's source was not supplied.
- The reflection tests break if the private helpers are renamed. They compile against the reference source as given.