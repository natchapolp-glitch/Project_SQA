package org.apache.commons.math3.fraction;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BigFractionTest {

    @Test
    public void testConstructorReducesAndNormalizesSign() {
        BigFraction f = new BigFraction(-2, -4);
        assertEquals(BigInteger.ONE, f.getNumerator());
        assertEquals(BigInteger.valueOf(2), f.getDenominator());
        assertEquals("1 / 2", f.toString());
    }

    @Test
    public void testZeroNumeratorNormalized() {
        BigFraction f = new BigFraction(0, -5);
        assertEquals(BigInteger.ZERO, f.getNumerator());
        assertEquals(BigInteger.ONE, f.getDenominator());
        assertEquals("0", f.toString());
    }

    @Test(expected = org.apache.commons.math3.exception.ZeroException.class)
    public void testConstructorZeroDenominatorThrows() {
        new BigFraction(1, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testBigIntegerConstructorNullNumeratorThrows() {
        new BigFraction((BigInteger) null, BigInteger.ONE);
    }

    @Test(expected = NullArgumentException.class)
    public void testBigIntegerConstructorNullDenominatorThrows() {
        new BigFraction(BigInteger.ONE, null);
    }

    @Test
    public void testDoubleConstructorExactHalf() {
        BigFraction f = new BigFraction(0.5d);
        assertEquals(BigInteger.ONE, f.getNumerator());
        assertEquals(BigInteger.valueOf(2), f.getDenominator());
        assertEquals(0.5d, f.doubleValue(), 0.0d);
        assertEquals(0.5f, f.floatValue(), 0.0f);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoubleConstructorNaNThrows() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoubleConstructorInfiniteThrows() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testArithmeticOperationsWithFractionsAndIntegers() {
        BigFraction base = new BigFraction(1, 2);

        assertEquals(new BigFraction(5, 2), base.add(2));
        assertEquals(new BigFraction(7, 2), base.add(3L));
        assertEquals(new BigFraction(9, 2), base.add(BigInteger.valueOf(4)));

        assertEquals(new BigFraction(-3, 2), base.subtract(2));
        assertEquals(new BigFraction(-5, 2), base.subtract(3L));
        assertEquals(new BigFraction(-7, 2), base.subtract(BigInteger.valueOf(4)));

        assertEquals(new BigFraction(1, 1), base.multiply(2));
        assertEquals(new BigFraction(3, 2), base.multiply(3L));
        assertEquals(new BigFraction(2, 1), base.multiply(BigInteger.valueOf(4)));

        assertEquals(new BigFraction(1, 4), base.divide(2));
        assertEquals(new BigFraction(1, 6), base.divide(3L));
        assertEquals(new BigFraction(1, 8), base.divide(BigInteger.valueOf(4)));
    }

    @Test
    public void testAddSubtractMultiplyDivideWithBigFraction() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(1, 3);

        assertEquals(new BigFraction(5, 6), a.add(b));
        assertEquals(new BigFraction(1, 6), a.subtract(b));
        assertEquals(new BigFraction(1, 6), a.multiply(b));
        assertEquals(new BigFraction(3, 2), a.divide(b));
    }

    @Test
    public void testAddAndSubtractZeroFractionReturnThisInstance() {
        BigFraction f = new BigFraction(3, 7);
        assertSame(f, f.add(BigFraction.ZERO));
        assertSame(f, f.subtract(BigFraction.ZERO));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullFractionThrows() {
        new BigFraction(1, 2).add((BigFraction) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testMultiplyNullBigIntegerThrows() {
        new BigFraction(1, 2).multiply((BigInteger) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideNullBigIntegerThrows() {
        new BigFraction(1, 2).divide((BigInteger) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroBigIntegerThrows() {
        new BigFraction(1, 2).divide(BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testDivideNullFractionThrows() {
        new BigFraction(1, 2).divide((BigFraction) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFractionThrows() {
        new BigFraction(1, 2).divide(BigFraction.ZERO);
    }

    @Test
    public void testAbsNegateReciprocalReduce() {
        BigFraction negative = new BigFraction(-2, 4);
        assertEquals(new BigFraction(1, 2), negative.abs());
        assertEquals(new BigFraction(1, 2), negative.negate());
        assertEquals(new BigFraction(-2, 1), negative.reciprocal());
        assertEquals(new BigFraction(-1, 2), negative.reduce());
    }

    @Test
    public void testPowVariants() {
        BigFraction f = new BigFraction(2, 3);

        assertEquals(new BigFraction(8, 27), f.pow(3));
        assertEquals(new BigFraction(9, 4), f.pow(-2));
        assertEquals(new BigFraction(8, 27), f.pow(3L));
        assertEquals(new BigFraction(9, 4), f.pow(-2L));
        assertEquals(new BigFraction(8, 27), f.pow(BigInteger.valueOf(3)));
        assertEquals(new BigFraction(9, 4), f.pow(BigInteger.valueOf(-2)));

        assertEquals(4.0d / 9.0d, f.pow(2.0d), 0.0d);
    }

    @Test
    public void testCompareEqualsAndHashCode() {
        BigFraction a = new BigFraction(1, 2);
        BigFraction b = new BigFraction(2, 4);
        BigFraction c = new BigFraction(3, 4);

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
        BigFraction f = new BigFraction(7, 2);

        assertEquals(3, f.intValue());
        assertEquals(3L, f.longValue());
        assertEquals(3.5d, f.doubleValue(), 0.0d);
        assertEquals(3.5f, f.floatValue(), 0.0f);
        assertEquals(350.0d, f.percentageValue(), 0.0d);
    }

    @Test
    public void testBigDecimalValueTerminatingAndRounded() {
        BigFraction half = new BigFraction(1, 2);
        assertEquals(new BigDecimal("0.5"), half.bigDecimalValue());
        assertEquals(new BigDecimal("1"), half.bigDecimalValue(BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("0.50"), half.bigDecimalValue(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test(expected = ArithmeticException.class)
    public void testBigDecimalValueNonTerminatingThrows() {
        new BigFraction(1, 3).bigDecimalValue();
    }

    @Test
    public void testGettersAsPrimitiveTypes() {
        BigFraction f = new BigFraction(5, 8);
        assertEquals(5, f.getNumeratorAsInt());
        assertEquals(8, f.getDenominatorAsInt());
        assertEquals(5L, f.getNumeratorAsLong());
        assertEquals(8L, f.getDenominatorAsLong());
    }

    @Test
    public void testGetReducedFractionZeroReturnsSharedZero() {
        assertSame(BigFraction.ZERO, BigFraction.getReducedFraction(0, 5));
    }

    @Test
    public void testGetReducedFractionReduces() {
        BigFraction f = BigFraction.getReducedFraction(6, -8);
        assertEquals(new BigFraction(-3, 4), f);
    }

    @Test
    public void testToStringForWholeNumberAndZero() {
        assertEquals("3", new BigFraction(3, 1).toString());
        assertEquals("0", new BigFraction(0, 7).toString());
        assertEquals("3 / 2", new BigFraction(3, 2).toString());
    }

    @Test
    public void testGetFieldReturnsSingleton() {
        BigFraction f = new BigFraction(1, 2);
        assertSame(BigFractionField.getInstance(), f.getField());
    }
}
