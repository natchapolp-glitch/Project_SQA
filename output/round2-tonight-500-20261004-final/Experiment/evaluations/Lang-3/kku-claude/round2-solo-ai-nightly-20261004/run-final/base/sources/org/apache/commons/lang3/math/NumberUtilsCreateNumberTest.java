package org.apache.commons.lang3.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberUtilsCreateNumberTest {

    @Test
    public void testCreateNumberNullAndEmpty() {
        assertNull(NumberUtils.createNumber(null));
        try {
            NumberUtils.createNumber("");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException expected) {
        }
    }

    @Test
    public void testCreateNumberPlainIntegers() {
        assertEquals(Integer.valueOf(123), NumberUtils.createNumber("123"));
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), NumberUtils.createNumber(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Long.valueOf((long) Integer.MAX_VALUE + 1L),
                NumberUtils.createNumber(String.valueOf((long) Integer.MAX_VALUE + 1L)));
        assertEquals(Long.valueOf(Long.MAX_VALUE), NumberUtils.createNumber(String.valueOf(Long.MAX_VALUE)));
        BigInteger beyondLong = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        assertEquals(beyondLong, NumberUtils.createNumber(beyondLong.toString()));
    }

    @Test
    public void testCreateNumberSuffixes() {
        assertEquals(Long.valueOf(1L), NumberUtils.createNumber("1L"));
        assertEquals(Long.valueOf(1L), NumberUtils.createNumber("1l"));
        assertEquals(Float.valueOf(1.0f), NumberUtils.createNumber("1F"));
        assertEquals(Float.valueOf(1.0f), NumberUtils.createNumber("1f"));
        assertEquals(Double.valueOf(1.0d), NumberUtils.createNumber("1D"));
        assertEquals(Double.valueOf(1.0d), NumberUtils.createNumber("1d"));
        try {
            NumberUtils.createNumber("1LL");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException expected) {
        }
    }

    @Test
    public void testCreateNumberHex() {
        assertEquals(Integer.valueOf(0x1A), NumberUtils.createNumber("0x1A"));
        assertEquals(Integer.valueOf(-0x1A), NumberUtils.createNumber("-0x1A"));
        assertEquals(Integer.valueOf(0x1A), NumberUtils.createNumber("#1A"));
    }

    @Test
    public void testCreateNumberScientificAndDecimal() {
        assertEquals(Double.valueOf(1.2e3), NumberUtils.createNumber("1.2e3"));
        assertEquals(Double.valueOf(1.2e3), NumberUtils.createNumber("1.2e3D"));
        assertEquals(Double.valueOf(1e10), NumberUtils.createNumber("1e10"));
        assertEquals(new BigDecimal("0.0"), NumberUtils.createNumber("0.0"));
        assertEquals(new BigDecimal("-0.0"), NumberUtils.createNumber("-0.0"));
    }

    @Test
    public void testCreateNumberInvalidStrings() {
        String[] invalid = { "1.2.3", "abc", "--1", "." , "d", "f" };
        for (String s : invalid) {
            try {
                NumberUtils.createNumber(s);
                fail("Expected NumberFormatException for input: " + s);
            } catch (NumberFormatException expected) {
            }
        }
    }
}
