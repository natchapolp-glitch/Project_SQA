Below are deterministic regression unit test suites for BigFraction and Fraction, targeting the eligible shared API declarations, boundary values, exception paths, and conversion operations using JUnit 4.

org/apache/commons/math3/fraction/BigFractionRegressionTest.java

```java
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

    @Test
    public void testDoubleConstructorExact() {
        BigFraction bf = new BigFraction(0.5);
        Assert.assertEquals(BigInteger.ONE, bf.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(2), bf.getDenominator());

        BigFraction zero = new BigFraction(0.0);
        Assert.assertEquals(BigInteger.ZERO, zero.getNumerator());
        Assert.assertEquals(BigInteger.ONE, zero.getDenominator());
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

```



org/apache/commons/math3/fraction/FractionRegressionTest.java

```java
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

```



References

Apache Commons Math BigFraction Documentation and Source (Commons Math 3.x / Defects4J Math-1f reference)
Apache Commons Math Fraction Documentation and Source (Commons Math 3.x / Defects4J Math-1f reference)
Knuth, D. E. The Art of Computer Programming, Volume 2: Seminumerical Algorithms, Section 4.5.1: Rational Arithmetic.
