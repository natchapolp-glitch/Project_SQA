org/apache/commons/math3/fraction/FractionRegressionTest.java

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

public class FractionRegressionTest {

    // -------------------------------------------------------------------------
    // BigFraction Tests
    // -------------------------------------------------------------------------

    @Test
    public void testBigFractionConstructorAndReductions() {
        BigFraction bf = new BigFraction(4, 6);
        Assert.assertEquals(2, bf.getNumeratorAsInt());
        Assert.assertEquals(3, bf.getDenominatorAsInt());
        Assert.assertEquals(BigInteger.valueOf(2), bf.getNumerator());
        Assert.assertEquals(BigInteger.valueOf(3), bf.getDenominator());

        BigFraction negative = new BigFraction(2, -3);
        Assert.assertEquals(-2, negative.getNumeratorAsInt());
        Assert.assertEquals(3, negative.getDenominatorAsInt());

        BigFraction zeroFrac = new BigFraction(0, 5);
        Assert.assertEquals(BigInteger.ZERO, zeroFrac.getNumerator());
        Assert.assertEquals(BigInteger.ONE, zeroFrac.getDenominator());
    }

    @Test(expected = ZeroException.class)
    public void testBigFractionZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test(expected = NullArgumentException.class)
    public void testBigFractionNullConstructorArguments() {
        new BigFraction(null, BigInteger.ONE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBigFractionDoubleNaN() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBigFractionDoubleInfinity() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testBigFractionDoubleConversions() {
        BigFraction half = new BigFraction(0.5);
        Assert.assertEquals(1, half.getNumeratorAsInt());
        Assert.assertEquals(2, half.getDenominatorAsInt());
        Assert.assertEquals(0.5, half.doubleValue(), 1e-15);
        Assert.assertEquals(0.5f, half.floatValue(), 1e-7f);
        Assert.assertEquals(50.0, half.percentageValue(), 1e-15);
    }

    @Test
    public void testBigFractionArithmeticOperations() {
        BigFraction f1 = new BigFraction(1, 3);
        BigFraction f2 = new BigFraction(1, 6);

        BigFraction sum = f1.add(f2);
        Assert.assertEquals(new BigFraction(1, 2), sum);

        BigFraction diff = f1.subtract(f2);
        Assert.assertEquals(new BigFraction(1, 6), diff);

        BigFraction prod = f1.multiply(f2);
        Assert.assertEquals(new BigFraction(1, 18), prod);

        BigFraction quot = f1.divide(f2);
        Assert.assertEquals(new BigFraction(2, 1), quot);
    }

    @Test(expected = MathArithmeticException.class)
    public void testBigFractionDivideByZeroFraction() {
        BigFraction.ONE.divide(BigFraction.ZERO);
    }

    @Test(expected = MathArithmeticException.class)
    public void testBigFractionDivideByZeroInteger() {
        BigFraction.ONE.divide(0);
    }

    @Test
    public void testBigFractionPow() {
        BigFraction base = new BigFraction(2, 3);
        BigFraction powPos = base.pow(3);
        Assert.assertEquals(new BigFraction(8, 27), powPos);

        BigFraction powNeg = base.pow(-2);
        Assert.assertEquals(new BigFraction(9, 4), powNeg);

        Assert.assertEquals(1.0, base.pow(0).doubleValue(), 1e-15);
    }

    @Test
    public void testBigFractionAbsAndNegate() {
        BigFraction neg = new BigFraction(-3, 4);
        Assert.assertEquals(new BigFraction(3, 4), neg.abs());
        Assert.assertEquals(new BigFraction(3, 4), neg.negate());

        BigFraction pos = new BigFraction(3, 4);
        Assert.assertSame(pos, pos.abs());
        Assert.assertEquals(new BigFraction(-3, 4), pos.negate());
    }

    @Test
    public void testBigFractionBigDecimalValue() {
        BigFraction f = new BigFraction(1, 4);
        Assert.assertEquals(new BigDecimal("0.25"), f.bigDecimalValue());
        Assert.assertEquals(new BigDecimal("0.33"), new BigFraction(1, 3).bigDecimalValue(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testBigFractionCompareToAndEquals() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(4, 6);
        BigFraction f3 = new BigFraction(3, 4);

        Assert.assertEquals(f1, f2);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
        Assert.assertNotEquals(f1, f3);
        Assert.assertNotEquals(f1, null);
        Assert.assertNotEquals(f1, "other");

        Assert.assertEquals(0, f1.compareTo(f2));
        Assert.assertTrue(f1.compareTo(f3) < 0);
        Assert.assertTrue(f3.compareTo(f1) > 0);
    }

    @Test
    public void testBigFractionToString() {
        Assert.assertEquals("2 / 3", new BigFraction(2, 3).toString());
        Assert.assertEquals("5", new BigFraction(5, 1).toString());
        Assert.assertEquals("0", BigFraction.ZERO.toString());
    }

    @Test
    public void testBigFractionFieldAndReduced() {
        Assert.assertNotNull(BigFraction.ONE.getField());
        Assert.assertSame(BigFraction.ZERO, BigFraction.getReducedFraction(0, 10));
        Assert.assertEquals(new BigFraction(1, 2), BigFraction.getReducedFraction(2, 4));
    }

    // -------------------------------------------------------------------------
    // Fraction Tests
    // -------------------------------------------------------------------------

    @Test
    public void testFractionConstructorAndReductions() {
        Fraction f = new Fraction(6, 8);
        Assert.assertEquals(3, f.getNumerator());
        Assert.assertEquals(4, f.getDenominator());

        Fraction negDen = new Fraction(3, -4);
        Assert.assertEquals(-3, negDen.getNumerator());
        Assert.assertEquals(4, negDen.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionZeroDenominator() {
        new Fraction(1, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionIntegerMinOverflowInConstructor() {
        new Fraction(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testFractionConversions() {
        Fraction f = new Fraction(3, 4);
        Assert.assertEquals(0.75, f.doubleValue(), 1e-15);
        Assert.assertEquals(0.75f, f.floatValue(), 1e-7f);
        Assert.assertEquals(0, f.intValue());
        Assert.assertEquals(0L, f.longValue());
        Assert.assertEquals(75.0, f.percentageValue(), 1e-15);
    }

    @Test
    public void testFractionArithmetic() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);

        Assert.assertEquals(new Fraction(5, 6), f1.add(f2));
        Assert.assertEquals(new Fraction(1, 6), f1.subtract(f2));
        Assert.assertEquals(new Fraction(1, 6), f1.multiply(f2));
        Assert.assertEquals(new Fraction(3, 2), f1.divide(f2));

        Assert.assertEquals(new Fraction(3, 2), f1.add(1));
        Assert.assertEquals(new Fraction(-1, 2), f1.subtract(1));
        Assert.assertEquals(new Fraction(1, 1), f1.multiply(2));
        Assert.assertEquals(new Fraction(1, 4), f1.divide(2));
    }

    @Test(expected = NullArgumentException.class)
    public void testFractionAddNull() {
        Fraction.ONE.add((Fraction) null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionDivideByZero() {
        Fraction.ONE.divide(Fraction.ZERO);
    }

    @Test
    public void testFractionAbsAndNegate() {
        Fraction neg = new Fraction(-5, 6);
        Assert.assertEquals(new Fraction(5, 6), neg.abs());
        Assert.assertEquals(new Fraction(5, 6), neg.negate());

        Fraction pos = new Fraction(5, 6);
        Assert.assertSame(pos, pos.abs());
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionNegateOverflow() {
        new Fraction(Integer.MIN_VALUE, 1).negate();
    }

    @Test
    public void testFractionCompareToAndEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(2, 3);

        Assert.assertEquals(f1, f2);
        Assert.assertEquals(f1.hashCode(), f2.hashCode());
        Assert.assertNotEquals(f1, f3);
        Assert.assertNotEquals(f1, null);

        Assert.assertEquals(0, f1.compareTo(f2));
        Assert.assertTrue(f1.compareTo(f3) < 0);
        Assert.assertTrue(f3.compareTo(f1) > 0);
    }

    @Test
    public void testFractionToString() {
        Assert.assertEquals("1 / 2", new Fraction(1, 2).toString());
        Assert.assertEquals("3", new Fraction(3, 1).toString());
        Assert.assertEquals("0", Fraction.ZERO.toString());
    }

    @Test
    public void testFractionGetReducedFraction() {
        Assert.assertSame(Fraction.ZERO, Fraction.getReducedFraction(0, 7));
        Assert.assertEquals(new Fraction(1, 3), Fraction.getReducedFraction(3, 9));
        Assert.assertEquals(new Fraction(-1, 3), Fraction.getReducedFraction(3, -9));
    }

    @Test(expected = MathArithmeticException.class)
    public void testFractionGetReducedFractionZeroDenominator() {
        Fraction.getReducedFraction(1, 0);
    }
}

```



References

Apache Software Foundation. (2013). Commons Math: The Apache Commons Mathematics Library (Version 3.1/3.3). Apache Commons. https://commons.apache.org/proper/commons-math/
Just, R., Jalali, D., & Ernst, M. D. (2014). Defects4J: A database of existing faults to enable controlled testing studies for Java programs. In Proceedings of the 2014 International Symposium on Software Testing and Analysis (ISSTA 2014) (pp. 437–440). ACM. https://doi.org/10.1145/2610384.2628055
Knuth, D. E. (1997). The Art of Computer Programming, Volume 2: Seminumerical Algorithms (3rd ed.). Addison-Wesley. Section 4.5.1 (Fractions arithmetic).
