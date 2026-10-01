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

    @Test
    public void testSubtractInt() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.subtract(1);
        Assert.assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testSubtractLong() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.subtract(1L);
        Assert.assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testSubtractBigInteger() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.subtract(BigInteger.ONE);
        Assert.assertEquals(new BigFraction(-1, 2), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSubtractBigIntegerNull() {
        new BigFraction(1).subtract((BigInteger) null);
    }

    @Test
    public void testMultiplyBigFraction() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(3, 4);
        BigFraction result = f1.multiply(f2);
        Assert.assertEquals(new BigFraction(1, 2), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testMultiplyBigFractionNull() {
        new BigFraction(1).multiply((BigFraction) null);
    }

    @Test
    public void testMultiplyBigFractionZero() {
        BigFraction f1 = new BigFraction(0);
        BigFraction f2 = new BigFraction(3, 4);
        Assert.assertEquals(BigFraction.ZERO, f1.multiply(f2));
        Assert.assertEquals(BigFraction.ZERO, f2.multiply(f1));
    }

    @Test
    public void testMultiplyInt() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.multiply(4);
        Assert.assertEquals(new BigFraction(8, 3), result);
    }

    @Test
    public void testMultiplyLong() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.multiply(4L);
        Assert.assertEquals(new BigFraction(8, 3), result);
    }

    @Test
    public void testMultiplyBigInteger() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.multiply(BigInteger.valueOf(4));
        Assert.assertEquals(new BigFraction(8, 3), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testMultiplyBigIntegerNull() {
        new BigFraction(1).multiply((BigInteger) null);
    }

    @Test
    public void testDivideBigFraction() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(3, 4);
        BigFraction result = f1.divide(f2);
        Assert.assertEquals(new BigFraction(8, 9), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideBigFractionNull() {
        new BigFraction(1).divide((BigFraction) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideBigFractionZero() {
        new BigFraction(1).divide(BigFraction.ZERO);
    }

    @Test
    public void testDivideInt() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.divide(4);
        Assert.assertEquals(new BigFraction(1, 6), result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideIntZero() {
        new BigFraction(1).divide(0);
    }

    @Test
    public void testDivideLong() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.divide(4L);
        Assert.assertEquals(new BigFraction(1, 6), result);
    }

    @Test
    public void testDivideBigInteger() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.divide(BigInteger.valueOf(4));
        Assert.assertEquals(new BigFraction(1, 6), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideBigIntegerNull() {
        new BigFraction(1).divide((BigInteger) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideBigIntegerZero() {
        new BigFraction(1).divide(BigInteger.ZERO);
    }

    @Test
    public void testNegate() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.negate();
        Assert.assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testNegateNegative() {
        BigFraction f = new BigFraction(-1, 2);
        BigFraction result = f.negate();
        Assert.assertEquals(new BigFraction(1, 2), result);
    }

    @Test
    public void testReciprocal() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.reciprocal();
        Assert.assertEquals(new BigFraction(3, 2), result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testReciprocalZero() {
        BigFraction.ZERO.reciprocal();
    }

    @Test
    public void testAbs() {
        Assert.assertEquals(new BigFraction(1, 2), new BigFraction(-1, 2).abs());
        Assert.assertEquals(new BigFraction(1, 2), new BigFraction(1, 2).abs());
    }

    @Test
    public void testPowIntPositive() {
        BigFraction f = new BigFraction(2, 3);
        Assert.assertEquals(new BigFraction(4, 9), f.pow(2));
    }

    @Test
    public void testPowIntNegative() {
        BigFraction f = new BigFraction(2, 3);
        Assert.assertEquals(new BigFraction(9, 4), f.pow(-2));
    }

    @Test
    public void testPowIntZero() {
        BigFraction f = new BigFraction(2, 3);
        Assert.assertEquals(new BigFraction(1, 1), f.pow(0));
    }

    @Test
    public void testPowLong() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(new BigFraction(27, 64), f.pow(3L));
    }

    @Test
    public void testPowBigInteger() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(new BigFraction(27, 64), f.pow(BigInteger.valueOf(3L)));
    }

    @Test
    public void testPowDouble() {
        BigFraction f = new BigFraction(4, 9);
        Assert.assertEquals(2.0 / 3.0, f.pow(0.5), DELTA);
    }

    @Test
    public void testReduce() {
        BigFraction f = new BigFraction(2, 4);
        BigFraction reduced = f.reduce();
        Assert.assertEquals(new BigFraction(1, 2), reduced);
    }

    @Test
    public void testReduceAlreadyReduced() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertEquals(f, f.reduce());
    }

    // ==================== Value Conversion Tests ====================

    @Test
    public void testDoubleValue() {
        BigFraction f = new BigFraction(1, 4);
        Assert.assertEquals(0.25, f.doubleValue(), DELTA);
    }

    @Test
    public void testFloatValue() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertEquals(0.5f, f.floatValue(), DELTA);
    }

    @Test
    public void testIntValue() {
        BigFraction f = new BigFraction(5, 2);
        Assert.assertEquals(2, f.intValue());
    }

    @Test
    public void testLongValue() {
        BigFraction f = new BigFraction(5, 2);
        Assert.assertEquals(2L, f.longValue());
    }

    @Test
    public void testPercentageValue() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertEquals(50.0, f.percentageValue(), DELTA);
    }

    @Test
    public void testBigDecimalValue() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertEquals(new BigDecimal("0.5"), f.bigDecimalValue());
    }

    @Test
    public void testBigDecimalValueRoundingMode() {
        BigFraction f = new BigFraction(1, 3);
        Assert.assertEquals(new BigDecimal("0.33"), f.bigDecimalValue(2, RoundingMode.HALF_UP.ordinal()));
    }

    @Test
    public void testBigDecimalValueScaleRoundingMode() {
        BigFraction f = new BigFraction(1, 3);
        Assert.assertEquals(new BigDecimal("0.33"), f.bigDecimalValue(2, RoundingMode.FLOOR.ordinal()));
    }

    // ==================== Equality and Comparison Tests ====================

    @Test
    public void testEqualsSameObject() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertTrue(f.equals(f));
    }

    @Test
    public void testEqualsNull() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        BigFraction f = new BigFraction(1, 2);
        Assert.assertFalse(f.equals("1/2"));
    }

    @Test
    public void testEqualsIdentical() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(1, 2);
        Assert.assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsEquivalent() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(2, 4);
        Assert.assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsDifferent() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(1, 3);
        Assert.assertFalse(f1.equals(f2));
    }

    @Test
    public void testHashCodeEqualFractions() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(2, 4);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testCompareToLess() {
        BigFraction f1 = new BigFraction(1, 3);
        BigFraction f2 = new BigFraction(1, 2);
        Assert.assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToEqual() {
        BigFraction f1 = new BigFraction(2, 4);
        BigFraction f2 = new BigFraction(1, 2);
        Assert.assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testCompareToGreater() {
        BigFraction f1 = new BigFraction(3, 4);
        BigFraction f2 = new BigFraction(1, 2);
        Assert.assertTrue(f1.compareTo(f2) > 0);
    }

    // ==================== Accessor Tests ====================

    @Test
    public void testGetNumerator() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(BigInteger.valueOf(3), f.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(BigInteger.valueOf(4), f.getDenominator());
    }

    @Test
    public void testGetNumeratorAsInt() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(3, f.getNumeratorAsInt());
    }

    @Test
    public void testGetDenominatorAsInt() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(4, f.getDenominatorAsInt());
    }

    @Test
    public void testGetNumeratorAsLong() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(3L, f.getNumeratorAsLong());
    }

    @Test
    public void testGetDenominatorAsLong() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(4L, f.getDenominatorAsLong());
    }

    // ==================== Other Tests ====================

    @Test
    public void testToStringInteger() {
        BigFraction f = new BigFraction(5);
        Assert.assertEquals("5", f.toString());
    }

    @Test
    public void testToStringFraction() {
        BigFraction f = new BigFraction(1, 3);
        Assert.assertEquals("1 / 3", f.toString());
    }

    @Test
    public void testToStringZero() {
        BigFraction f = BigFraction.ZERO;
        Assert.assertEquals("0", f.toString());
    }

    @Test
    public void testGetField() {
        Assert.assertTrue(new BigFraction(1).getField() instanceof BigFractionField);
    }
}
