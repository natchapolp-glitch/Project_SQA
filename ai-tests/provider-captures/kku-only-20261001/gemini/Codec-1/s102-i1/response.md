An extensive regression test suite for `Caverphone`, `Metaphone`, and `SoundexUtils` is provided below, compatible with JUnit 3.8.1 and the project's source level.

### `org/apache/commons/codec/language/CaverphoneTest.java`

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.caverphone = new Caverphone();
    }

    public void testCaverphoneNullAndEmpty() {
        assertEquals("1111111111", this.caverphone.caverphone(null));
        assertEquals("1111111111", this.caverphone.caverphone(""));
        assertEquals("1111111111", this.caverphone.caverphone("   "));
        assertEquals("1111111111", this.caverphone.caverphone("12345"));
    }

    public void testCaverphoneBasic() {
        assertEquals("LA11111111", this.caverphone.caverphone("Lee"));
        assertEquals("LA11111111", this.caverphone.caverphone("Li"));
        assertEquals("ATA1111111", this.caverphone.caverphone("Peter"));
    }

    public void testCaverphoneSpecialStarts() {
        assertEquals("KF11111111", this.caverphone.caverphone("cough"));
        assertEquals("RF11111111", this.caverphone.caverphone("rough"));
        assertEquals("TF11111111", this.caverphone.caverphone("tough"));
        assertEquals("ANF1111111", this.caverphone.caverphone("enough"));
        assertEquals("NM11111111", this.caverphone.caverphone("gnome"));
        assertEquals("MPA1111111", this.caverphone.caverphone("mbaba"));
    }

    public void testCaverphoneReplacements() {
        // Final e removed, cq -> 2q -> k, d -> t
        assertEquals("ST11111111", this.caverphone.caverphone("side"));
        // ph -> fh -> F
        assertEquals("FA11111111", this.caverphone.caverphone("photo"));
    }

    public void testEncodeObject() throws Exception {
        Object result = this.caverphone.encode((Object) "Lee");
        assertTrue(result instanceof String);
        assertEquals("LA11111111", result);
        assertEquals("LA11111111", this.caverphone.encode("Lee"));
    }

    public void testEncodeObjectException() {
        try {
            this.caverphone.encode(new Integer(42));
            fail("Expected EncoderException when encoding non-String object");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(this.caverphone.isCaverphoneEqual("Lee", "Li"));
        assertTrue(this.caverphone.isCaverphoneEqual("cough", "COUGH"));
        assertFalse(this.caverphone.isCaverphoneEqual("Peter", "Stevenson"));
    }
}
```

### `org/apache/commons/codec/language/MetaphoneTest.java`

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class MetaphoneTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.metaphone = new Metaphone();
    }

    public void testMetaphoneNullAndEmptyAndSingleChar() {
        assertEquals("", this.metaphone.metaphone(null));
        assertEquals("", this.metaphone.metaphone(""));
        assertEquals("A", this.metaphone.metaphone("a"));
        assertEquals("Z", this.metaphone.metaphone("z"));
    }

    public void testInitialExceptions() {
        assertEquals("NT", this.metaphone.metaphone("knight"));
        assertEquals("NT", this.metaphone.metaphone("gnat"));
        assertEquals("NMN", this.metaphone.metaphone("pneumonia"));
        assertEquals("EJS", this.metaphone.metaphone("aegis"));
        assertEquals("RT", this.metaphone.metaphone("write"));
        assertEquals("WT", this.metaphone.metaphone("white"));
        assertEquals("SNN", this.metaphone.metaphone("xenon"));
    }

    public void testSilentB() {
        assertEquals("TM", this.metaphone.metaphone("dumb"));
        assertEquals("KLM", this.metaphone.metaphone("climb"));
        assertEquals("STL", this.metaphone.metaphone("subtle"));
    }

    public void testCaseC() {
        assertEquals("MSXN", this.metaphone.metaphone("musician"));
        assertEquals("ST", this.metaphone.metaphone("city"));
        assertEquals("SNS", this.metaphone.metaphone("science"));
        assertEquals("SKL", this.metaphone.metaphone("school"));
        assertEquals("KR", this.metaphone.metaphone("chair"));
        assertEquals("ARX", this.metaphone.metaphone("arch"));
    }

    public void testCaseD() {
        assertEquals("EJ", this.metaphone.metaphone("edge"));
        assertEquals("TJ", this.metaphone.metaphone("dodge"));
        assertEquals("TRFT", this.metaphone.metaphone("draft"));
    }

    public void testCaseG() {
        assertEquals("SNT", this.metaphone.metaphone("signed"));
        assertEquals("L", this.metaphone.metaphone("laugh"));
        assertEquals("KST", this.metaphone.metaphone("ghost"));
        assertEquals("JM", this.metaphone.metaphone("gem"));
    }

    public void testCaseH() {
        assertEquals("H", this.metaphone.metaphone("high"));
        assertEquals("H", this.metaphone.metaphone("ha"));
    }

    public void testCaseP() {
        assertEquals("FN", this.metaphone.metaphone("phone"));
        assertEquals("PRT", this.metaphone.metaphone("party"));
    }

    public void testCaseSAndT() {
        assertEquals("XT", this.metaphone.metaphone("shoot"));
        assertEquals("NXN", this.metaphone.metaphone("nation"));
        assertEquals("WX", this.metaphone.metaphone("watch"));
        assertEquals("0N", this.metaphone.metaphone("thin"));
    }

    public void testCaseWAndY() {
        assertEquals("WST", this.metaphone.metaphone("west"));
        assertEquals("YL", this.metaphone.metaphone("yellow"));
        assertEquals("BL", this.metaphone.metaphone("blow"));
    }

    public void testMaxCodeLen() {
        assertEquals(4, this.metaphone.getMaxCodeLen());
        assertEquals("KMNK", this.metaphone.metaphone("communication"));
        this.metaphone.setMaxCodeLen(6);
        assertEquals(6, this.metaphone.getMaxCodeLen());
        assertEquals("KMNKXN", this.metaphone.metaphone("communication"));
    }

    public void testEncodeObject() throws Exception {
        Object result = this.metaphone.encode((Object) "flight");
        assertTrue(result instanceof String);
        assertEquals("FLT", result);
        assertEquals("FLT", this.metaphone.encode("flight"));

        try {
            this.metaphone.encode(new Long(100L));
            fail("Expected EncoderException when encoding non-String object");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testIs