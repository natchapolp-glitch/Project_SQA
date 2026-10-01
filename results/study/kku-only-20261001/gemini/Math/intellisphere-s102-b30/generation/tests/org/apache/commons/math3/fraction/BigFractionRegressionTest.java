package org.apache.commons.math3.fraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Assert;
import org.junit.Test;

public class BigFractionRegressionTest {

    @Test
    public void testIntegerConstructorsAndGetters() {
        BigFraction bf1 = new BigFraction(6, -8);
        Assert.assertEquals(-3, bf1.getNumeratorAsInt());
        Assert.assertEquals(4, bf1.getDenominatorAsInt());
        Assert.assertEquals(-3L, bf1.getNumeratorAsLong());
        Assert.assertEquals(4L, bf1.getDenominatorAsLong());
        Assert.assertEquals(BigInteger.valueOf(-3), bf1.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(4), bf1.getDenominator());

        BigFraction bf2 = new BigFraction(5);
        Assert.assertEquals(BigInteger.valueOf(5), bf2.getNumerator());
        Assert.assertEquals(BigInteger.ONE, bf2.getDenominator());

        BigFraction bf3 = new BigFraction(10L);
        Assert.assertEquals(BigInteger.valueOf(10), bf3.getNumerator());
        Assert.assertEquals(BigInteger.ONE, bf3.getDenominator());
    }

    

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoubleConstructorNaNThrows() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testDoubleConstructorInfinityThrows() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = ZeroException.class)
    public void testZeroDenominatorThrows() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullNumeratorThrows() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test
    public void testGetReducedFraction() {
        BigFraction reduced = BigFraction.getReducedFraction(0, 10);
        Assert.assertSame(BigFraction.ZERO, reduced);

        BigFraction normal = BigFraction.getReducedFraction(4, 6);
        Assert.assertEquals(2, normal.getNumeratorAsInt());
        Assert.assertEquals(3, normal.getDenominatorAsInt());
    }

    @Test
    public void testArithmeticAddSubtract() {
        BigFraction f1 = new BigFraction(1, 3);
        BigFraction f2 = new BigFraction(1, 6);

        BigFraction sum = f1.add(f2);
        Assert.assertEquals(new BigFraction(1, 2), sum);

        BigFraction diff = f1.subtract(f2);
        Assert.assertEquals(new BigFraction(1, 6), diff);

        Assert.assertEquals(new BigFraction(4, 3), f1.add(1));
        Assert.assertEquals(new BigFraction(-2, 3), f1.subtract(1));
        Assert.assertEquals(new BigFraction(7, 3), f1.add(2L));
        Assert.assertEquals(new BigFraction(-5, 3), f1.subtract(2L));
        Assert.assertEquals(new BigFraction(10, 3), f1.add(BigInteger.valueOf(3)));
        Assert.assertEquals(new BigFraction(-8, 3), f1.subtract(BigInteger.valueOf(3)));
    }

    @Test
    public void testArithmeticMultiplyDivide() {
        BigFraction f1 = new BigFraction(2, 5);
        BigFraction f2 = new BigFraction(3, 4);

        BigFraction prod = f1.multiply(f2);
        Assert.assertEquals(new BigFraction(3, 10), prod);

        BigFraction quot = f1.divide(f2);
        Assert.assertEquals(new BigFraction(8, 15), quot);

        Assert.assertEquals(new BigFraction(4, 5), f1.multiply(2));
        Assert.assertEquals(new BigFraction(1, 5), f1.divide(2));
        Assert.assertEquals(new BigFraction(6, 5), f1.multiply(3L));
        Assert.assertEquals(new BigFraction(2, 15), f1.divide(3L));
        Assert.assertEquals(new BigFraction(8, 5), f1.multiply(BigInteger.valueOf(4)));
        Assert.assertEquals(new BigFraction(1, 10), f1.divide(BigInteger.valueOf(4)));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFractionThrows() {
        BigFraction f = new BigFraction(1, 2);
        f.divide(BigFraction.ZERO);
    }

    @Test
    public void testAbsNegateReciprocalReduce() {
        BigFraction neg = new BigFraction(-3, 5);
        Assert.assertEquals(new BigFraction(3, 5), neg.abs());
        Assert.assertEquals(new BigFraction(3, 5), neg.negate());
        Assert.assertEquals(new BigFraction(-5, 3), neg.reciprocal());

        BigFraction pos = new BigFraction(3, 5);
        Assert.assertSame(pos, pos.abs());
        Assert.assertEquals(new BigFraction(3, 5), pos.reduce());
    }

    @Test
    public void testPowOperations() {
        BigFraction base = new BigFraction(2, 3);
        Assert.assertEquals(new BigFraction(4, 9), base.pow(2));
        Assert.assertEquals(new BigFraction(9, 4), base.pow(-2));
        Assert.assertEquals(new BigFraction(8, 27), base.pow(3L));
        Assert.assertEquals(new BigFraction(27, 8), base.pow(-3L));
        Assert.assertEquals(new BigFraction(16, 81), base.pow(BigInteger.valueOf(4)));
        Assert.assertEquals(new BigFraction(81, 16), base.pow(BigInteger.valueOf(-4)));
        Assert.assertEquals(1.5, new BigFraction(9, 4).pow(0.5), 1e-10);
    }

    @Test
    public void testConversions() {
        BigFraction f = new BigFraction(3, 4);
        Assert.assertEquals(0.75, f.doubleValue(), 1e-10);
        Assert.assertEquals(0.75f, f.floatValue(), 1e-5f);
        Assert.assertEquals(0, f.intValue());
        Assert.assertEquals(0L, f.longValue());
        Assert.assertEquals(75.0, f.percentageValue(), 1e-10);

        BigFraction whole = new BigFraction(7, 2);
        Assert.assertEquals(3, whole.intValue());
        Assert.assertEquals(3L, whole.longValue());

        Assert.assertEquals(new BigDecimal("0.75"), f.bigDecimalValue());
        Assert.assertEquals(new BigDecimal("0.8"), f.bigDecimalValue(1, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testCompareToEqualsAndHashCode() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(2, 4);
        BigFraction f3 = new BigFraction(2, 3);
        BigFraction f4 = new BigFraction(1, 3);

        Assert.assertEquals(0, f1.compareTo(f2));
        Assert.assertTrue(f1.compareTo(f3) < 0);
        Assert.assertTrue(f1.compareTo(f4) > 0);

        Assert.assertTrue(f1.equals(f2));
        Assert.assertFalse(f1.equals(f3));
        Assert.assertFalse(f1.equals(null));
        Assert.assertFalse(f1.equals("1/2"));

        Assert.assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testToStringAndField() {
        Assert.assertEquals("0", BigFraction.ZERO.toString());
        Assert.assertEquals("5", new BigFraction(5).toString());
        Assert.assertEquals("3 / 4", new BigFraction(3, 4).toString());
        Assert.assertNotNull(new BigFraction(1, 2).getField());
    }
}
