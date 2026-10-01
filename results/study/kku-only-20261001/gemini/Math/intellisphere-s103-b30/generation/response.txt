org/apache/commons/math3/fraction/BigFractionRegressionTest.java
```java
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

public class BigFractionRegressionTest {

    @Test
    public void testConstructorDoubleExact() {
        BigFraction fraction = new BigFraction(0.5);
        Assert.assertEquals(BigInteger.ONE, fraction.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(2), fraction.getDenominator());
        Assert.assertEquals(0.5, fraction.doubleValue(), 1e-15);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorDoubleNaN() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorDoubleInfinity() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructorBigIntegerReductionAndSign() {
        BigFraction fraction = new BigFraction(BigInteger.valueOf(6), BigInteger.valueOf(-8));
        Assert.assertEquals(BigInteger.valueOf(-3), fraction.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(4), fraction.getDenominator());
        Assert.assertEquals(-3, fraction.getNumeratorAsInt());
        Assert.assertEquals(4, fraction.getDenominatorAsInt());
        Assert.assertEquals(-3L, fraction.getNumeratorAsLong());
        Assert.assertEquals(4L, fraction.getDenominatorAsLong());
    }

    @Test(expected = ZeroException.class)
    public void testConstructorBigIntegerZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullNumerator() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test
    public void testArithmeticOperations() {
        BigFraction first = new BigFraction(1, 3);
        BigFraction second = new BigFraction(1, 6);

        BigFraction sum = first.add(second);
        Assert.assertEquals(new BigFraction(1, 2), sum);

        BigFraction diff = first.subtract(second);
        Assert.assertEquals(new BigFraction(1, 6), diff);

        BigFraction prod = first.multiply(second);
        Assert.assertEquals(new BigFraction(1, 18), prod);

        BigFraction quot = first.divide(second);
        Assert.assertEquals(new BigFraction(2, 1), quot);
    }

    @Test
    public void testPrimitiveOverloads() {
        BigFraction fraction = new BigFraction(2, 5);

        Assert.assertEquals(new BigFraction(7, 5), fraction.add(1));
        Assert.assertEquals(new BigFraction(7, 5), fraction.add(1L));
        Assert.assertEquals(new BigFraction(7, 5), fraction.add(BigInteger.ONE));

        Assert.assertEquals(new BigFraction(-3, 5), fraction.subtract(1));
        Assert.assertEquals(new BigFraction(-3, 5), fraction.subtract(1L));
        Assert.assertEquals(new BigFraction(-3, 5), fraction.subtract(BigInteger.ONE));

        Assert.assertEquals(new BigFraction(4, 5), fraction.multiply(2));
        Assert.assertEquals(new BigFraction(4, 5), fraction.multiply(2L));
        Assert.assertEquals(new BigFraction(4, 5), fraction.multiply(BigInteger.valueOf(2)));

        Assert.assertEquals(new BigFraction(1, 5), fraction.divide(2));
        Assert.assertEquals(new BigFraction(1, 5), fraction.divide(2L));
        Assert.assertEquals(new BigFraction(1, 5), fraction.divide(BigInteger.valueOf(2)));
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZeroFraction() {
        BigFraction fraction = new BigFraction(3, 4);
        fraction.divide(BigFraction.ZERO);
    }

    @Test
    public void testAbsNegateReciprocalAndPow() {
        BigFraction neg = new BigFraction(-2, 3);

        Assert.assertEquals(new BigFraction(2, 3), neg.abs());
        Assert.assertEquals(new BigFraction(2, 3), neg.negate());
        Assert.assertEquals(new BigFraction(-3, 2), neg.reciprocal());

        BigFraction base = new BigFraction(2, 3);
        Assert.assertEquals(new BigFraction(8, 27), base.pow(3));
        Assert.assertEquals(new BigFraction(27, 8), base.pow(-3));
        Assert.assertEquals(new BigFraction(8, 27), base.pow(3L));
        Assert.assertEquals(new BigFraction(8, 27), base.pow(BigInteger.valueOf(3)));
        Assert.assertEquals(FastMath.pow(2.0 / 3.0, 2.0), base.pow(2.0), 1e-12);
    }

    @Test
    public void testConversions() {
        BigFraction fraction = new BigFraction(7, 2);
        Assert.assertEquals(3, fraction.intValue());
        Assert.assertEquals(3L, fraction.longValue());
        Assert.assertEquals(3.5f, fraction.floatValue(), 1e-6f);
        Assert.assertEquals(3.5, fraction.doubleValue(), 1e-15);
        Assert.assertEquals(350.0, fraction.percentageValue(), 1e-15);
        Assert.assertEquals(new BigDecimal("3.5"), fraction.bigDecimalValue());
        Assert.assertEquals(new BigDecimal("3.50"), fraction.bigDecimalValue(2, RoundingMode.HALF_UP.ordinal()));
    }

    @Test
    public void testComparisonsAndEquality() {
        BigFraction half = new BigFraction(1, 2);
        BigFraction sameHalf = new BigFraction(2, 4);
        BigFraction third = new BigFraction(1, 3);

        Assert.assertEquals(half, sameHalf);
        Assert.assertEquals(half.hashCode(), sameHalf.hashCode());
        Assert.assertFalse(half.equals(null));
        Assert.assertFalse(half.equals(new Object()));

        Assert.assertTrue(half.compareTo(third) > 0);
        Assert.assertTrue(third.compareTo(half) < 0);
        Assert.assertEquals(0, half.compareTo(sameHalf));
    }

    @Test
    public void testToStringAndReducedFraction() {
        Assert.assertEquals("1 / 2", new BigFraction(1, 2).toString());
        Assert.assertEquals("3", new BigFraction(3, 1).toString());
        Assert.assertEquals("0", BigFraction.ZERO.toString());

        BigFraction reduced = BigFraction.getReducedFraction(0, 5);
        Assert.assertEquals(BigFraction.ZERO, reduced);

        BigFraction reducedNonZero = BigFraction.getReducedFraction(4, 6);
        Assert.assertEquals(new BigFraction(2, 3), reducedNonZero);
    }

    @Test
    public void testGetField() {
        BigFraction fraction = new BigFraction(1, 2);
        Assert.assertNotNull(fraction.getField());
        Assert.assertSame(BigFractionField.getInstance(), fraction.getField());
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
```

---

### References

1. **Apache Software Foundation.** (2013). *Commons Math: The Apache Commons Mathematics Library (Version 3.3)*. Apache Commons. https://commons.apache.org/proper/commons-math/
2. **Knuth, D. E.** (1997). *The Art of Computer Programming, Volume 2: Seminumerical Algorithms* (3rd ed.). Addison-Wesley Professional. (Referenced in `org.apache.commons.math3.fraction.Fraction` for continued fractions and exact rational arithmetic algorithms 4.5.1).
3. **JUnit Team.** (2013). *JUnit 4 Framework Documentation*. https://junit.org/junit4/