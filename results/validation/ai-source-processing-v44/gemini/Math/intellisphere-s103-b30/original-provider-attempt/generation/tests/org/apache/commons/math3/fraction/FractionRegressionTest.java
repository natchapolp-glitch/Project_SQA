package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class FractionRegressionTest {

    @Test
    public void testConstructorAndReductions() {
        Fraction fraction = new Fraction(4, -6);
        Assert.assertEquals(-2, fraction.getNumerator());
        Assert.assertEquals(3, fraction.getDenominator());
        Assert.assertEquals(-2.0 / 3.0, fraction.doubleValue(), 1e-15);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorMinIntOverflow() {
        new Fraction(Integer.MIN_VALUE, -1);
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

    @Test
    public void testMultiplyAndDivide() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);

        Fraction prod = f1.multiply(f2);
        Assert.assertEquals(new Fraction(1, 2), prod);

        Fraction quot = f1.divide(f2);
        Assert.assertEquals(new Fraction(8, 9), quot);

        Assert.assertEquals(new Fraction(4, 3), f1.multiply(2));
        Assert.assertEquals(new Fraction(1, 3), f1.divide(2));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFraction() {
        Fraction f = new Fraction(1, 2);
        f.divide(Fraction.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullArgument() {
        new Fraction(1, 2).add((Fraction) null);
    }

    @Test
    public void testNegateReciprocalAndAbs() {
        Fraction f = new Fraction(-3, 5);

        Assert.assertEquals(new Fraction(3, 5), f.negate());
        Assert.assertEquals(new Fraction(3, 5), f.abs());
        Assert.assertEquals(new Fraction(-5, 3), f.reciprocal());

        Fraction pos = new Fraction(3, 5);
        Assert.assertSame(pos, pos.abs());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateIntegerMinValue() {
        new Fraction(Integer.MIN_VALUE, 1).negate();
    }

    @Test
    public void testConversions() {
        Fraction fraction = new Fraction(5, 2);
        Assert.assertEquals(2, fraction.intValue());
        Assert.assertEquals(2L, fraction.longValue());
        Assert.assertEquals(2.5f, fraction.floatValue(), 1e-6f);
        Assert.assertEquals(2.5, fraction.doubleValue(), 1e-15);
        Assert.assertEquals(250.0, fraction.percentageValue(), 1e-15);
    }

    @Test
    public void testEqualsAndHashCode() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);

        Assert.assertEquals(f1, f2);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
        Assert.assertFalse(f1.equals(f3));
        Assert.assertFalse(f1.equals(null));
        Assert.assertFalse(f1.equals("1/2"));

        Assert.assertTrue(f1.compareTo(f3) > 0);
        Assert.assertTrue(f3.compareTo(f1) < 0);
        Assert.assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testToStringAndReducedFraction() {
        Assert.assertEquals("1 / 2", new Fraction(1, 2).toString());
        Assert.assertEquals("5", new Fraction(5, 1).toString());
        Assert.assertEquals("0", Fraction.ZERO.toString());

        Assert.assertEquals(Fraction.ZERO, Fraction.getReducedFraction(0, 10));
        Assert.assertEquals(new Fraction(1, 2), Fraction.getReducedFraction(2, 4));
        Assert.assertEquals(new Fraction(-1, 3), Fraction.getReducedFraction(2, -6));
    }

    @Test
    public void testGetField() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertNotNull(fraction.getField());
        Assert.assertSame(FractionField.getInstance(), fraction.getField());
    }
}
