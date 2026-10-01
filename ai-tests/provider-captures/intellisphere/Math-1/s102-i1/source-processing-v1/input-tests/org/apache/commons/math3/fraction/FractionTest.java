package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructorInt() {
        Fraction f = new Fraction(5);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorIntInt() {
        Fraction f = new Fraction(2, 4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorIntIntZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test
    public void testConstructorDouble() {
        Fraction f = new Fraction(0.25);
        assertEquals(1, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testConstructorDoubleInt() {
        Fraction f = new Fraction(Math.PI, 10);
        assertTrue(f.doubleValue() > 3.0);
        assertTrue(f.doubleValue() < 4.0);
    }

    @Test
    public void testConstructorDoubleDoubleInt() {
        Fraction f = new Fraction(0.3333, 0.001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    public void testGetReducedFraction() {
        Fraction f = Fraction.getReducedFraction(4, 8);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFractionZeroDenominator() {
        Fraction.getReducedFraction(1, 0);
    }

    @Test
    public void testEqualsSameObject() {
        Fraction f = new Fraction(1, 2);
        assertTrue(f.equals(f));
    }

    @Test
    public void testEqualsNull() {
        Fraction f = new Fraction(1, 2);
        assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Fraction f = new Fraction(1, 2);
        assertFalse(f.equals("1/2"));
    }

    @Test
    public void testEqualsEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsNotEqual() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testHashCodeConsistencyWithEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testCompareToLessThan() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testCompareToGreaterThan() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) > 0);
    }

    @Test
    public void testCompareToEqual() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(0, f1.compareTo(f2));
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 1e-15);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(0.25f, f.floatValue(), 1e-7);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(7, 2);
        assertEquals(3, f.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(9, 2);
        assertEquals(4L, f.longValue());
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(25.0, f.percentageValue(), 1e-15);
    }

    @Test
    public void testGetNumerator() {
        Fraction f = new Fraction(3, 5);
        assertEquals(3, f.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        Fraction f = new Fraction(3, 5);
        assertEquals(5, f.getDenominator());
    }

    @Test
    public void testToStringInteger() {
        Fraction f = new Fraction(5);
        assertEquals("5", f.toString());
    }

    @Test
    public void testToStringZero() {
        Fraction f = Fraction.ZERO;
        assertEquals("0", f.toString());
    }

    @Test
    public void testToStringFraction() {
        Fraction f = new Fraction(3, 4);
        assertEquals("3 / 4", f.toString());
    }

    @Test
    public void testAbsPositive() {
        Fraction f = new Fraction(3, 5);
        assertEquals(f, f.abs());
    }

    @Test
    public void testAbsNegative() {
        Fraction f = new Fraction(-3, 5);
        Fraction expected = new Fraction(3, 5);
        assertEquals(expected, f.abs());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(3, 5);
        Fraction expected = new Fraction(-3, 5);
        assertEquals(expected, f.negate());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateIntegerMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        f.negate();
    }

    @Test
    public void testReciprocal() {
        Fraction f = new Fraction(3, 5);
        Fraction expected = new Fraction(5, 3);
        assertEquals(expected, f.reciprocal());
    }

    @Test
    public void testAddFraction() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 2);
        Fraction expected = new Fraction(5, 6);
        assertEquals(expected, f1.add(f2));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddFractionNull() {
        new Fraction(1).add((Fraction) null);
    }

    @Test
    public void testAddInt() {
        Fraction f = new Fraction(1, 3);
        Fraction expected = new Fraction(4, 3);
        assertEquals(expected, f.add(1));
    }

    @Test
    public void testSubtractFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction expected = new Fraction(1, 6);
        assertEquals(expected, f1.subtract(f2));
    }

    @Test(expected = NullArgumentException.class)
    public void testSubtractFractionNull() {
        new Fraction(1).subtract((Fraction) null);
    }

    @Test
    public void testSubtractInt() {
        Fraction f = new Fraction(3, 2);
        Fraction expected = new Fraction(1, 2);
        assertEquals(expected, f.subtract(1));
    }
}
