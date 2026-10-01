package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

public class BigFractionTest {

    // --- Constructor tests ---

    @Test
    public void testConstructorBigInteger() {
        BigFraction f = new BigFraction(BigInteger.valueOf(3));
        assertEquals(BigInteger.valueOf(3), f.getNumerator());
        assertEquals(BigInteger.ONE, f.getDenominator());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorBigIntegerBigIntegerNullNumerator() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorBigIntegerBigIntegerNullDenominator() {
        new BigFraction(BigInteger.ONE, null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorBigIntegerBigIntegerZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test
    public void testConstructorBigIntegerBigIntegerReduction() {
        BigFraction f = new BigFraction(BigInteger.valueOf(4), BigInteger.valueOf(6));
        assertEquals(BigInteger.valueOf(2), f.getNumerator());
        assertEquals(BigInteger.valueOf(3), f.getDenominator());
    }

    @Test
    public void testConstructorInt() {
        BigFraction f = new BigFraction(5);
        assertEquals(5, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorLong() {
        BigFraction f = new BigFraction(7L);
        assertEquals(7L, f.getNumeratorAsLong());
        assertEquals(1L, f.getDenominatorAsLong());
    }

    @Test
    public void testConstructorIntInt() {
        BigFraction f = new BigFraction(2, 4);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(2, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorLongLong() {
        BigFraction f = new BigFraction(6L, 9L);
        assertEquals(2L, f.getNumeratorAsLong());
        assertEquals(3L, f.getDenominatorAsLong());
    }

    @Test
    public void testConstructorDouble() {
        BigFraction f = new BigFraction(0.5);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(2, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorDoubleNegative() {
        BigFraction f = new BigFraction(-0.75);
        assertEquals(-3, f.getNumeratorAsInt());
        assertEquals(4, f.getDenominatorAsInt());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorDoubleNaN() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorDoublePositiveInfinity() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorDoubleNegativeInfinity() {
        new BigFraction(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testConstructorDoubleInt() {
        BigFraction f = new BigFraction(Math.PI, 10);
        // Just ensure construction succeeds and produces a reasonable approximation
        assertTrue(f.doubleValue() > 3.0);
        assertTrue(f.doubleValue() < 4.0);
    }

    @Test
    public void testConstructorDoubleDoubleInt() {
        BigFraction f = new BigFraction(0.3333, 0.001, 100);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(3, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorDoubleDoubleIntInt() {
        // This constructor is private, tested indirectly via (double, double, int) which calls it.
        BigFraction f = new BigFraction(0.142857, 0.0001, 100);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(7, f.getDenominatorAsInt());
    }

    // --- Method tests ---

    @Test
    public void testEqualsSameObject() {
        BigFraction f = new BigFraction(1, 2);
        assertTrue(f.equals(f));
    }

    @Test
    public void testEqualsNull() {
        BigFraction f = new BigFraction(1, 2);
        assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        BigFraction f = new BigFraction(1, 2);
        assertFalse(f.equals("1/2"));
    }

    @Test
    public void testEqualsEqualFractions() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(2, 4);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsNotEqual() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(1, 3);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testHashCodeConsistencyWithEquals() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(2, 4);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testCompareToLessThan() {
        BigFraction f1 = new BigFraction(1, 3);
        BigFraction f2 = new BigFraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToGreaterThan() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(1, 2);
        assertTrue(f1.compareTo(f2) > 0);
    }

    @Test
    public void testCompareToEqual() {
        BigFraction f1 = new BigFraction(2, 4);
        BigFraction f2 = new BigFraction(1, 2);
        assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testDoubleValue() {
        BigFraction f = new BigFraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 1e-15);
    }

    @Test
    public void testFloatValue() {
        BigFraction f = new BigFraction(1, 4);
        assertEquals(0.25f, f.floatValue(), 1e-7);
    }

    @Test
    public void testIntValue() {
        BigFraction f = new BigFraction(7, 2);
        assertEquals(3, f.intValue());
    }

    @Test
    public void testLongValue() {
        BigFraction f = new BigFraction(9, 2);
        assertEquals(4L, f.longValue());
    }

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
