import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/** Generated from 15 unique fixed-revision observations. */
public class GeneratedLang1RegressionTest {
    @Test
    public void generatedCase1() {
        Number actual = NumberUtils.createNumber("0x0000000000000000000010000000000000");
        assertEquals("java.lang.Long", actual.getClass().getName());
        assertEquals("4503599627370496", actual.toString());
    }
    @Test
    public void generatedCase2() {
        Number actual = NumberUtils.createNumber("0x0000000010000");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("65536", actual.toString());
    }
    @Test
    public void generatedCase3() {
        Number actual = NumberUtils.createNumber("0x0000000000000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase4() {
        Number actual = NumberUtils.createNumber("0x000000000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase5() {
        Number actual = NumberUtils.createNumber("0x000000001000000000000");
        assertEquals("java.lang.Long", actual.getClass().getName());
        assertEquals("281474976710656", actual.toString());
    }
    @Test
    public void generatedCase6() {
        Number actual = NumberUtils.createNumber("0x000000000000000000000000010000000");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("268435456", actual.toString());
    }
    @Test
    public void generatedCase7() {
        Number actual = NumberUtils.createNumber("0x00000000000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase8() {
        Number actual = NumberUtils.createNumber("0x00000000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase9() {
        Number actual = NumberUtils.createNumber("0x00000000100");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("256", actual.toString());
    }
    @Test
    public void generatedCase10() {
        Number actual = NumberUtils.createNumber("0x000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase11() {
        Number actual = NumberUtils.createNumber("0x000000000000000000100000000");
        assertEquals("java.lang.Long", actual.getClass().getName());
        assertEquals("4294967296", actual.toString());
    }
    @Test
    public void generatedCase12() {
        Number actual = NumberUtils.createNumber("0x00000000001");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1", actual.toString());
    }
    @Test
    public void generatedCase13() {
        Number actual = NumberUtils.createNumber("0x00000010000000");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("268435456", actual.toString());
    }
    @Test
    public void generatedCase14() {
        Number actual = NumberUtils.createNumber("0x000000010000");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("65536", actual.toString());
    }
    @Test
    public void generatedCase15() {
        Number actual = NumberUtils.createNumber("0x00000000000000000100000");
        assertEquals("java.lang.Integer", actual.getClass().getName());
        assertEquals("1048576", actual.toString());
    }
}
