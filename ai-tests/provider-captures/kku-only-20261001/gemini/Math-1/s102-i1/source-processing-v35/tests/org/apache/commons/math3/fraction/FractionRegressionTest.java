package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class FractionRegressionTest {

    @Test
    public void testIntegerConstructorAndReductions() {
        Fraction f1 = new Fraction(6, -8);
        Assert.assertEquals(-3, f1.getNumerator());
        Assert.assertEquals(4, f1.getDenominator());

        Fraction f2 = new Fraction(7);
        Assert.assertEquals(7, f2.getNumerator());
        Assert.assertEquals(1, f2.getDenominator());
    }

    @Test
    public void testDoubleConstructor() {
        Fraction f = new Fraction(0.25);
        Assert.assertEquals(1, f.getNumerator());
        Assert.assertEquals(4, f.getDenominator());

        Fraction fWithEpsilon = new Fraction(0.3333333333, 1e-4, 10);
        Assert.assertEquals(1, fWithEpsilon.getNumerator());
        Assert.assertEquals(3, fWithEpsilon.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominatorThrows() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDenominatorOverflowThrows() {
        new Fraction(1, Integer.MIN_VALUE);
    }

    @Test(expected = MathArithmeticException.class)
    public void testNumeratorOverflowOnNegativeDenominatorThrows() {
        new Fraction(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction zero = Fraction.getReducedFraction(0, 5);
        Assert.assertSame(Fraction.ZERO, zero);

        Fraction reduced = Fraction.getReducedFraction(6, -9);
        Assert.assertEquals(-2, reduced.getNumerator());
        Assert.assertEquals(3, reduced.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFractionZeroDenominatorThrows() {
        Fraction.getReducedFraction(1, 0);
    }

    @Test
    public void testAddAndSubtract() {
        Fraction f1 = new Fraction(1, 4);
        Fraction f2 = new Fraction(1, 2);

        Fraction sum = f1.add(f2);
        Assert.assertEquals(3, sum.getNumerator());
        Assert.assertEquals(4, sum.getDenominator());

        Fraction diff = f1.subtract(f2);
        Assert.assertEquals(-1, diff.getNumerator());
        Assert.assertEquals(4, diff.getDenominator());

        Assert.assertEquals(new Fraction(5, 4), f1.add(1));
        Assert.assertEquals(new Fraction(-3, 4), f1.subtract(1));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullThrows() {
        Fraction.ONE.add((Fraction) null);
    }

    @Test
    public void testMultiplyAndDivide() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 5);

        Fraction prod = f1.multiply(f2);
        Assert.assertEquals(2, prod.getNumerator());
        Assert.assertEquals(5, prod.getDenominator());

        Fraction quot = f1.divide(f2);
        Assert.assertEquals(10, quot.getNumerator());
        Assert.assertEquals(9, quot.getDenominator());

        Assert.assertEquals(new Fraction(4, 3), f1.multiply(2));
        Assert.assertEquals(new Fraction(1, 3), f1.divide(2));
        Assert.assertSame(Fraction.ZERO, f1.multiply(Fraction.ZERO));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFractionThrows() {
        Fraction.ONE.divide(Fraction.ZERO);
    }

    @Test
    public void testAbsNegateReciprocal() {
        Fraction f = new Fraction(-2, 5);
        Assert.assertEquals(new Fraction(2, 5), f.abs());
        Assert.assertEquals(new Fraction(2, 5), f.negate());
        Assert.assertEquals(new Fraction(-5, 2), f.reciprocal());

        Fraction pos = new Fraction(2, 5);
        Assert.assertSame(pos, pos.abs());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateOverflowThrows() {
        Fraction minNumerator = new Fraction(Integer.MIN_VALUE, 1);
        minNumerator.negate();
    }

    @Test
    public void testConversionsAndPercentage() {
        Fraction f = new Fraction(1, 8);
        Assert.assertEquals(0.125, f.doubleValue(), 1e-10);
        Assert.assertEquals(0.125f, f.floatValue(), 1e-5f);
        Assert.assertEquals(0, f.intValue());
        Assert.assertEquals(0L, f.longValue());
        Assert.assertEquals(12.5, f.percentageValue(), 1e-10);

        Fraction mixed = new Fraction(11, 4);
        Assert.assertEquals(2, mixed.intValue());
        Assert.assertEquals(2L, mixed.longValue());
    }

    @Test
    public void testCompareToEqualsHashCodeAndString() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(3, 4);

        Assert.assertEquals(0, f1.compareTo(f2));
        Assert.assertTrue(f1.compareTo(f3) < 0);
        Assert.assertTrue(f3.compareTo(f1) > 0);

        Assert.assertTrue(f1.equals(f2));
        Assert.assertFalse(f1.equals(f3));
        Assert.assertFalse(f1.equals(null));
        Assert.assertFalse(f1.equals(new Object()));

        Assert.assertEquals(f1.hashCode(), f2.hashCode());
        Assert.assertEquals("0", Fraction.ZERO.toString());
        Assert.assertEquals("1", Fraction.ONE.toString());
        Assert.assertEquals("1 / 2", f1.toString());
        Assert.assertNotNull(f1.getField());
    }
}
