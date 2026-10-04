package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class Math1AdditionalFractionTest {

    @Test
    public void testBigFractionDoubleConstructorAndReduce() {
        BigFraction bf = new BigFraction(0.5);
        assertEquals(BigInteger.ONE, bf.getNumerator());
        assertEquals(BigInteger.valueOf(2), bf.getDenominator());
        BigFraction reduced = bf.reduce();
        assertNotNull(reduced);
        assertEquals(bf, reduced);
    }

    @Test
    public void testFractionDoubleConstructorExact() {
        Fraction f = new Fraction(0.25);
        assertEquals(1, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testBigFractionFloatIntConstructors() {
        BigFraction bf = new BigFraction(3, 4);
        assertEquals(3, bf.getNumeratorAsInt());
        assertEquals(4, bf.getDenominatorAsInt());
        assertEquals(3L, bf.getNumeratorAsLong());
        assertEquals(4L, bf.getDenominatorAsLong());
    }

    @Test
    public void testBigFractionPowAndAbs() {
        BigFraction bf = new BigFraction(-1, 2);
        assertEquals(new BigFraction(1, 2), bf.abs());
        assertEquals(new BigFraction(1, 4), bf.pow(2));
    }
}
