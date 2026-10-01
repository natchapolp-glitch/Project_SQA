package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    private static final double DELTA = 1e-15;

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructorInteger() {
        Fraction f = new Fraction(5);
        Assert.assertEquals(5, f.getNumerator());
        Assert.assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorDouble() throws FractionConversionException {
        Fraction f = new Fraction(0.5);
        Assert.assertEquals(0.5, f.doubleValue(), DELTA);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void testConstructorIntInt() {
        Fraction f = new Fraction(2, 4);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorIntIntZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        Fraction f = new Fraction(1, -2);
        Assert.assertEquals(-1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void testConstructorDoubleEpsilonMaxIterations() throws FractionConversionException {
        Fraction f = new Fraction(Math.PI, 1e-10, 100);
        Assert.assertEquals(Math.PI, f.doubleValue(), 1e-10);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleEpsilonMaxIterationsFail() throws FractionConversionException {
        new Fraction(1000000.0, 1e-15, 2);
    }

    @Test
    public void testConstructorDoubleMaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(Math.PI, 100);
        Assert.assertTrue(f.getDenominator() <= 100);
    }

    // ==================== getReducedFraction Tests ====================

    @Test
    public void testGetReducedFraction() {
        Fraction f = Fraction.getReducedFraction(2, 4);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(2, f.getDenominator());
    }

    @Test
    public void testGetReducedFractionZeroNumerator() {
        Fraction f = Fraction.getReducedFraction(0, 5);
        Assert.assertEquals(Fraction.ZERO, f);
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFractionZeroDenominator() {
        Fraction.getReducedFraction(1, 0);
    }

    // ==================== Arithmetic Operation Tests ====================

    @Test
    public void testAddFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        Assert.assertEquals(new Fraction(5, 6), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddFractionNull() {
        new Fraction(1).add((Fraction) null);
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(3);
        Assert.assertEquals(new Fraction(7, 2), result);
    }

    @Test
    public void testSubtractFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.subtract(f2);
        Assert.assertEquals(new Fraction(1, 6), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testSubtractFractionNull() {
        new Fraction(1).subtract((Fraction) null);
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.subtract(1);
        Assert.assertEquals(new Fraction(-1, 2), result);
    }

    @Test
    public void testMultiplyFraction() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.multiply(f2);
        Assert.assertEquals(new Fraction(1, 2), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testMultiplyFractionNull() {
        new Fraction(1).multiply((Fraction) null);
    }

    @Test
    public void testMultiplyInt() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.multiply(4);
        Assert.assertEquals(new Fraction(8, 3), result);
    }

    @Test
    public void testDivideFraction() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.divide(f2);
        Assert.assertEquals(new Fraction(8, 9), result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideFractionNull() {
        new Fraction(1).divide((Fraction) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideFractionZero() {
        new Fraction(1).divide(Fraction.ZERO);
    }

    @Test
    public void testDivideInt() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.divide(4);
        Assert.assertEquals(new Fraction(1, 6), result);
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        Assert.assertEquals(new Fraction(-1, 2), result);
    }

    @Test
    public void testNegateNegative() {
        Fraction f = new Fraction(-1, 2);
        Fraction result = f.negate();
        Assert.assertEquals(new Fraction(1, 2), result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateOverflow() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        f.negate();
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        Assert.assertEquals(new Fraction(3, 2), result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testReciprocalZero() {
        Fraction.ZERO.reciprocal();
    }

    @Test
    public void testAbs() {
        Assert.assertEquals(new Fraction(1, 2), new Fraction(-1, 2).abs());
        Assert.assertEquals(new Fraction(1, 2), new Fraction(1, 2).abs());
    }

    // ==================== Value Conversion Tests ====================

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 4);
        Assert.assertEquals(0.25, f.doubleValue(), DELTA);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 2);
        Assert.assertEquals(0.5f, f.floatValue(), DELTA);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(5, 2);
        Assert.assertEquals(2, f.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(5, 2);
        Assert.assertEquals(2L, f.longValue());
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        Assert.assertEquals(50.0, f.percentageValue(), DELTA);
    }

    // ==================== Equality and Comparison Tests ====================

    @Test
    public void testEqualsSameObject() {
        Fraction f = new Fraction(1, 2);
        Assert.assertTrue(f.equals(f));
    }

    @Test
    public void testEqualsNull() {
        Fraction f = new Fraction(1, 2);
        Assert.assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Fraction f = new Fraction(1, 2);
        Assert.assertFalse(f.equals("1/2"));
    }

    @Test
    public void testEqualsIdentical() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Assert.assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsDifferent() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Assert.assertFalse(f1.equals(f2));
    }

    @Test
    public void testHashCodeEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testCompareToLess() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 2);
        Assert.assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToEqual() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        Assert.assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testCompareToGreater() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        Assert.assertTrue(f1.compareTo(f2) > 0);
    }

    // ==================== Accessor Tests ====================

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(3, f.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(4, f.getDenominator());
    }

    // ==================== Other Tests ====================

    @Test
    public void testToStringInteger() {
        Fraction f = new Fraction(5);
        Assert.assertEquals("5", f.toString());
    }

    @Test
    public void testToStringFraction() {
        Fraction f = new Fraction(1, 3);
        Assert.assertEquals("1 / 3", f.toString());
    }

    @Test
    public void testToStringZero() {
        Fraction f = Fraction.ZERO;
        Assert.assertEquals("0", f.toString());
    }

    @Test
    public void testGetField() {
        Assert.assertTrue(new Fraction(1).getField() instanceof FractionField);
    }

    // ==================== addSub Exerciser Tests ====================

    @Test
    public void testAddDifferentDenominators() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.add(f2);
        Assert.assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testAddSameDenominators() {
        Fraction f1 = new Fraction(1, 5);
        Fraction f2 = new Fraction(3, 5);
        Fraction result = f1.add(f2);
        Assert.assertEquals(new Fraction(4, 5), result);
    }

    @Test
    public void testSubtractDifferentDenominators() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        Fraction result = f1.subtract(f2);
        Assert.assertEquals(new Fraction(1, 4), result);
    }

    @Test
    public void testSubtractSameDenominators() {
        Fraction f1 = new Fraction(4, 5);
        Fraction f2 = new Fraction(1, 5);
        Fraction result = f1.subtract(f2);
        Assert.assertEquals(new Fraction(3, 5), result);
    }
}
