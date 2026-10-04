package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NumberUtilsLang1Test {

    @Test
    public void testCreateNumberExt1() {
        assertEquals(Float.valueOf(1.0f), NumberUtils.createNumber("1f"));
        assertEquals(Double.valueOf(1.0d), NumberUtils.createNumber("1d"));
        assertEquals(Long.valueOf(1L), NumberUtils.createNumber("1l"));
    }

    @Test
    public void testCreateNumberExt2() {
        assertNotNull(NumberUtils.createNumber("123.456"));
        assertNotNull(NumberUtils.createNumber("1e-10"));
        assertNotNull(NumberUtils.createNumber("1E+10"));
    }

    @Test
    public void testCreateNumberBigDecAndBigInt() {
        assertNotNull(NumberUtils.createNumber("12345678901234567890"));
        assertNotNull(NumberUtils.createNumber("123.45678901234567890"));
    }

    @Test
    public void testCreateNumberHexAndOctal() {
        assertEquals(Integer.valueOf(255), NumberUtils.createNumber("0xFF"));
        assertEquals(Long.valueOf(255L), NumberUtils.createNumber("0xFFL"));
        assertEquals(Integer.valueOf(0), NumberUtils.createNumber("0"));
    }

}
