package org.apache.commons.math3.fraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Assert;
import org.junit.Test;

public class BigFractionTest {

    private static final double DELTA = 1e-15;

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructorInteger() {
        BigFraction f = new BigFraction(5);
        Assert.assertEquals(BigInteger.valueOf(5), f.getNumerator());
        Assert.assertEquals(BigInteger.ONE, f.getDenominator());
    }

    @Test
    public void testConstructorLong() {
        BigFraction f = new BigFraction(7L);
        Assert.assertEquals(BigInteger.valueOf(7), f.getNumerator());
        Assert.assertEquals(BigInteger.ONE, f.getDenominator());
    }

    @Test
    public void testConstructorBigInteger() {
        BigFraction f = new BigFraction(BigInteger.valueOf(11));
        Assert.assertEquals(BigInteger.valueOf(11), f.getNumerator());
        Assert.assertEquals(BigInteger.ONE, f.getDenominator());
    }

    @Test
    public void testConstructorDouble() {
        BigFraction f = new BigFraction(0.5);
        Assert.assertEquals(0.5, f.doubleValue(), DELTA);
        Assert.assertEquals(BigInteger.valueOf(1), f.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(2), f.getDenominator());
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
    public void testConstructorIntInt() {
        BigFraction f = new BigFraction(2, 4);
        Assert.assertEquals(BigInteger.valueOf(1), f.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(2), f.getDenominator());
    }

    @Test(expected = ZeroException.class)
    public void testConstructorIntIntZeroDenominator() {
        new BigFraction(1, 0);
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        BigFraction f = new BigFraction(1, -2);
        Assert.assertEquals(BigInteger.valueOf(-1), f.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(2), f.getDenominator());
    }

    @Test
    public void testConstructorLongLong() {
        BigFraction f = new BigFraction(3L, 9L);
        Assert.assertEquals(BigInteger.valueOf(1), f.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(3), f.getDenominator());
    }

    @Test(expected = ZeroException.class)
    public void testConstructorLongLongZeroDenominator() {
        new BigFraction(1L, 0L);
    }

    @Test
    public void testConstructorBigIntegerBigInteger() {
        BigFraction f = new BigFraction(BigInteger.valueOf(6), BigInteger.valueOf(8));
        Assert.assertEquals(BigInteger.valueOf(3), f.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(4), f.getDenominator());
    }

    @Test(expected = ZeroException.class)
    public void testConstructorBigIntegerBigIntegerZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorBigIntegerBigIntegerNullNumerator() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorBigIntegerBigIntegerNullDenominator() {
        new BigFraction(BigInteger.ONE, null);
    }

    @Test
    public void testConstructorDoubleEpsilonMaxIterations() throws FractionConversionException {
        BigFraction f = new BigFraction(Math.PI, 1e-10, 100);
        Assert.assertEquals(Math.PI, f.doubleValue(), 1e-10);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleEpsilonMaxIterationsFail() throws FractionConversionException {
        new BigFraction(1000000.0, 1e-15, 2);
    }

    @Test
    public void testConstructorDoubleMaxDenominator() throws FractionConversionException {
        BigFraction f = new BigFraction(Math.PI, 100);
        Assert.assertTrue(f.getDenominator().compareTo(BigInteger.valueOf(100)) <= 0);
    }

    // ==================== getReducedFraction Tests ====================

    @Test
    public void testGetReducedFraction() {
        BigFraction f = BigFraction.getReducedFraction(2, 4);
        Assert.assertEquals(1, f.getNumerator().intValue());
        Assert.assertEquals(2, f.getDenominator().intValue());
    }

    @Test
    public void testGetReducedFractionZeroNumerator() {
        BigFraction f = BigFraction.getReducedFraction(0, 5);
        Assert.assertEquals(BigFraction.ZERO, f);
    }

    @Test(expected = ArithmeticException.class)
    public void testGetReducedFractionZeroDenominator() {
        BigFraction.getReducedFraction(1, 0);
    }

    // ==================== Arithmetic Operation Tests ====================

    @Test
    public void testAddBigFraction() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(1, 3);
        BigFraction result = f1.add(f2);
        Assert.assertEquals(new BigFraction(5, 6), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddBigFractionNull() {
        new BigFraction(1).add((BigFraction) null);
    }

    @Test
    public void testAddInt() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.add(3);
        Assert.assertEquals(new BigFraction(7, 2), result);
    }

    @Test
    public void testAddLong() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.add(3L);
        Assert.assertEquals(new BigFraction(7, 2), result);
    }

    @Test
    public void testAddBigInteger() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.add(BigInteger.valueOf(3));
        Assert.assertEquals(new BigFraction(7, 2), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddBigIntegerNull() {
        new BigFraction(1).add((BigInteger) null);
    }

    @Test
    public void testSubtractBigFraction() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(1, 3);
        BigFraction result = f1.subtract(f2);
        Assert.assertEquals(new BigFraction(1, 6), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSubtractBigFractionNull() {
        new BigFraction(1).subtract((BigFraction) null);
    }

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    // ==================== Value Conversion Tests ====================

    

    

    

    

    

    

    

    

    // ==================== Equality and Comparison Tests ====================

    

    

    

    

    

    

    

    

    

    

    // ==================== Accessor Tests ====================

    

    

    

    

    

    

    // ==================== Other Tests ====================

    

    

    

    
}
