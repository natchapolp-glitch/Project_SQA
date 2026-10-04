package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

public class FractionAndBigFractionTest {

    @Test
    public void testFractionBasicAndReduction() {
        Fraction f = new Fraction(2, 4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.5, f.doubleValue(), 1e-15);
        assertEquals("1 / 2", f.toString());
    }

    @Test(expected = ZeroException.class)
    public void testFractionZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testFractionArithmetic() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        
        Fraction sum = f1.add(f2);
        assertEquals(new Fraction(5, 6), sum);

        Fraction diff = f1.subtract(f2);
        assertEquals(new Fraction(1, 6), diff);

        Fraction prod = f1.multiply(f2);
        assertEquals(new Fraction(1, 6), prod);

        Fraction quot = f1.divide(f2);
        assertEquals(new Fraction(3, 2), quot);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionDivideByZero() {
        Fraction f1 = new Fraction(1, 2);
        f1.divide(Fraction.ZERO);
    }

    @Test
    public void testFractionCompareAndEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(1, 3);

        assertTrue(f1.equals(f2));
        assertFalse(f1.equals(f3));
        assertEquals(0, f1.compareTo(f2));
        assertTrue(f1.compareTo(f3) > 0);
        assertTrue(f3.compareTo(f1) < 0);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testFractionConversions() {
        Fraction f = new Fraction(7, 3);
        assertEquals(2, f.intValue());
        assertEquals(2L, f.longValue());
        assertEquals(2.33333, f.doubleValue(), 1e-4);
        assertEquals(2.33333f, f.floatValue(), 1e-4f);
    }

    @Test
    public void testBigFractionBasicAndReduction() {
        BigFraction bf = new BigFraction(BigInteger.valueOf(10), BigInteger.valueOf(20));
        assertEquals(BigInteger.ONE, bf.getNumerator());
        assertEquals(BigInteger.valueOf(2), bf.getDenominator());
        assertEquals(0.5, bf.doubleValue(), 1e-15);
        assertNotNull(bf.getField());
    }

    @Test(expected = ZeroException.class)
    public void testBigFractionZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testBigFractionNullArgumentConstructor() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test
    public void testBigFractionArithmetic() {
        BigFraction bf1 = new BigFraction(1, 2);
        BigFraction bf2 = new BigFraction(1, 3);

        BigFraction sum = bf1.add(bf2);
        assertEquals(new BigFraction(5, 6), sum);

        BigFraction diff = bf1.subtract(bf2);
        assertEquals(new BigFraction(1, 6), diff);

        BigFraction prod = bf1.multiply(bf2);
        assertEquals(new BigFraction(1, 6), prod);

        BigFraction quot = bf1.divide(bf2);
        assertEquals(new BigFraction(3, 2), quot);
    }

    @Test
    public void testBigFractionGetReducedFraction() {
        BigFraction reduced = BigFraction.getReducedFraction(-6, -9);
        assertEquals(BigInteger.valueOf(2), reduced.getNumerator());
        assertEquals(BigInteger.valueOf(3), reduced.getDenominator());
    }

    @Test
    public void testBigFractionBigDecimalValue() {
        BigFraction bf = new BigFraction(1, 4);
        BigDecimal bd = bf.bigDecimalValue();
        assertEquals(0, new BigDecimal("0.25").compareTo(bd));
    }
}
