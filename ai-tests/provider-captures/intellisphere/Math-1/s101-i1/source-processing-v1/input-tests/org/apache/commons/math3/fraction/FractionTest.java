package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorReducesAndNormalizesSign() {
        Fraction f = new Fraction(-2, -4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals("1 / 2", f.toString());
    }

    @Test
    public void testZeroNumeratorKeepsDenominatorPositiveAfterReduction() {
        Fraction f = new Fraction(0, -5);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());
        assertEquals("0", f.toString());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorZeroDenominatorThrows() {
        new Fraction(1, 0);
    }

    @Test
    public void testDoubleConstructorApproximatesSimpleDecimal() throws Exception {
        Fraction f = new Fraction(0.5d);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals(0.5d, f.doubleValue(), 0.0d);
        assertEquals(0.5f, f.floatValue(), 0.0f);
    }

    @Test
    public void testAddSubtractMultiplyDivideWithFractions() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);

        assertEquals(new Fraction(5, 6), a.add(b));
        assertEquals(new Fraction(1, 6), a.subtract(b));
        assertEquals(new Fraction(1, 6), a.multiply(b));
        assertEquals(new Fraction(3, 2), a.divide(b));
    }

    @Test
    public void testAddSubtractMultiplyDivideWithIntegers() {
        Fraction base = new Fraction(1, 2);

        assertEquals(new Fraction(5, 2), base.add(2));
        assertEquals(new Fraction(-3, 2), base.subtract(2));
        assertEquals(new Fraction(1, 1), base.multiply(2));
        assertEquals(new Fraction(1, 4), base.divide(2));
    }

    @Test
    public void testOperationsWithZeroIdentityCases() {
        Fraction nonZero = new Fraction(3, 7);
        assertSame(nonZero, nonZero.add(Fraction.ZERO));
        assertSame(nonZero, nonZero.subtract(Fraction.ZERO));

        Fraction zero = Fraction.ZERO;
        assertEquals(nonZero, zero.add(nonZero));
        assertEquals(nonZero.negate(), zero.subtract(nonZero));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullThrows() {
        new Fraction(1, 2).add((Fraction) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testSubtractNullThrows() {
        new Fraction(1, 2).subtract((Fraction) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testMultiplyNullThrows() {
        new Fraction(1, 2).multiply((Fraction) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideNullThrows() {
        new Fraction(1, 2).divide((Fraction) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFractionThrows() {
        new Fraction(1, 2).divide(Fraction.ZERO);
    }

    @Test
    public void testAbsNegateReciprocal() {
        Fraction negative = new Fraction(-1, 2);
        assertEquals(new Fraction(1, 2), negative.abs());
        assertEquals(new Fraction(1, 2), negative.negate());
        assertEquals(new Fraction(-2, 1), negative.reciprocal());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegateMinValueThrows() {
        new Fraction(Integer.MIN_VALUE, 1).negate();
    }

    @Test
    public void testCompareEqualsHashCode() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 4);
        Fraction c = new Fraction(3, 4);

        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(c));
        assertFalse(a.equals("not a fraction"));

        assertEquals(0, a.compareTo(b));
        assertTrue(a.compareTo(c) < 0);
        assertTrue(c.compareTo(a) > 0);
    }

    @Test
    public void testNumericConversionsAndPercentage() {
        Fraction f = new Fraction(7, 2);

        assertEquals(3, f.intValue());
        assertEquals(3L, f.longValue());
        assertEquals(3.5d, f.doubleValue(), 0.0d);
        assertEquals(3.5f, f.floatValue(), 0.0f);
        assertEquals(350.0d, f.percentageValue(), 0.0d);
    }

    @Test
    public void testGetReducedFractionZeroReturnsSharedZero() {
        assertSame(Fraction.ZERO, Fraction.getReducedFraction(0, 5));
    }

    @Test
    public void testGetReducedFractionReducesAndNormalizes() {
        Fraction f = Fraction.getReducedFraction(6, -8);
        assertEquals(new Fraction(-3, 4), f);
    }

    @Test(expected = MathArithmeticException.class)
    public void testGetReducedFractionZeroDenominatorThrows() {
        Fraction.getReducedFraction(1, 0);
    }

    @Test
    public void testToStringForWholeNumberZeroAndProperFraction() {
        assertEquals("3", new Fraction(3, 1).toString());
        assertEquals("0", new Fraction(0, 7).toString());
        assertEquals("3 / 2", new Fraction(3, 2).toString());
    }

    @Test
    public void testGetFieldReturnsSingleton() {
        Fraction f = new Fraction(1, 2);
        assertSame(FractionField.getInstance(), f.getField());
    }
}
