I'll analyze the production source code and create comprehensive deterministic regression tests for the `BigFraction` and `Fraction` classes. Based on the build configuration, this project uses JUnit 4.

Let me create test classes that exercise normal cases, boundaries, invalid inputs, exception paths, and branches.

```java
org/apache/commons/math3/fraction/BigFractionTest.java
```

```java
package org.apache.commons.math3.fraction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for BigFraction class.
 */
public class BigFractionTest {

    @Test
    public void testConstructorDoubleSimple() {
        BigFraction f = new BigFraction(0.5);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(2, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorDoubleZero() {
        BigFraction f = new BigFraction(0.0);
        assertEquals(0, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorDoubleOne() {
        BigFraction f = new BigFraction(1.0);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoubleNaN() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoubleInfinityPositive() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoubleInfinityNegative() {
        new BigFraction(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testConstructorInt() {
        BigFraction f = new BigFraction(5);
        assertEquals(5, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorIntZero() {
        BigFraction f = new BigFraction(0);
        assertEquals(0, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorLong() {
        BigFraction f = new BigFraction(123456789L);
        assertEquals(123456789L, f.getNumeratorAsLong());
        assertEquals(1L, f.getDenominatorAsLong());
    }

    @Test
    public void testConstructorBigInteger() {
        BigInteger num = BigInteger.valueOf(42);
        BigFraction f = new BigFraction(num);
        assertEquals(num, f.getNumerator());
        assertEquals(BigInteger.ONE, f.getDenominator());
    }

    @Test
    public void testConstructorIntInt() {
        BigFraction f = new BigFraction(3, 4);
        assertEquals(3, f.getNumeratorAsInt());
        assertEquals(4, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorIntIntReduced() {
        BigFraction f = new BigFraction(6, 8);
        assertEquals(3, f.getNumeratorAsInt());
        assertEquals(4, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        BigFraction f = new BigFraction(3, -4);
        assertEquals(-3, f.getNumeratorAsInt());
        assertEquals(4, f.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.ZeroException.class)
    public void testConstructorIntIntZeroDenominator() {
        new BigFraction(5, 0);
    }

    @Test
    public void testConstructorLongLong() {
        BigFraction f = new BigFraction(10L, 20L);
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(2, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorBigIntegerBigInteger() {
        BigFraction f = new BigFraction(BigInteger.valueOf(7), BigInteger.valueOf(14));
        assertEquals(1, f.getNumeratorAsInt());
        assertEquals(2, f.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.ZeroException.class)
    public void testConstructorBigIntegerBigIntegerZeroDenominator() {
        new BigFraction(BigInteger.ONE, BigInteger.ZERO);
    }

    @Test
    public void testConstructorBigIntegerBigIntegerZeroNumerator() {
        BigFraction f = new BigFraction(BigInteger.ZERO, BigInteger.valueOf(5));
        assertEquals(0, f.getNumeratorAsInt());
        assertEquals(1, f.getDenominatorAsInt());
    }

    @Test
    public void testConstructorDoubleDouble() throws FractionConversionException {
        BigFraction f = new BigFraction(0.5, 0.01, 10);
        assertTrue(Math.abs(f.doubleValue() - 0.5) < 0.01);
    }

    @Test
    public void testConstructorDoubleMaxDenominator() throws FractionConversionException {
        BigFraction f = new BigFraction(0.333333, 100);
        assertTrue(f.getDenominatorAsInt() <= 100);
    }

    @Test
    public void testEqualsIdentical() {
        BigFraction f1 = new BigFraction(3, 4);
        BigFraction f2 = new BigFraction(3, 4);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsReduced() {
        BigFraction f1 = new BigFraction(3, 4);
        BigFraction f2 = new BigFraction(6, 8);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsNotEqual() {
        BigFraction f1 = new BigFraction(3, 4);
        BigFraction f2 = new BigFraction(4, 5);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testEqualsNull() {
        BigFraction f = new BigFraction(3, 4);
        assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsOtherType() {
        BigFraction f = new BigFraction(3, 4);
        assertFalse(f.equals("3/4"));
    }

    @Test
    public void testCompareTo() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(3, 4);
        assertTrue(f1.compareTo(f2) < 0);
        assertTrue(f2.compareTo(f1) > 0);
        BigFraction f3 = new BigFraction(2, 3);
        assertEquals(0, f1.compareTo(f3));
    }

    @Test
    public void testCompareToNegative() {
        BigFraction f1 = new BigFraction(-1, 2);
        BigFraction f2 = new BigFraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testDoubleValue() {
        BigFraction f = new BigFraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 1e-10);
    }

    @Test
    public void testDoubleValueLarge() {
        BigFraction f = new BigFraction(BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(2));
        double d = f.doubleValue();
        assertTrue(d > 0 && d < Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFloatValue() {
        BigFraction f = new BigFraction(1, 4);
        assertEquals(0.25f, f.floatValue(), 1e-6f);
    }

    @Test
    public void testIntValue() {
        BigFraction f = new BigFraction(7, 2);
        assertEquals(3, f.intValue());
    }

    @Test
    public void testLongValue() {
        BigFraction f = new BigFraction(15, 2);
        assertEquals(7L, f.longValue());
    }

    @Test
    public void testPercentageValue() {
        BigFraction f = new BigFraction(1, 4);
        assertEquals(25.0, f.percentageValue(), 1e-10);
    }

    @Test
    public void testHashCode() {
        BigFraction f1 = new BigFraction(3, 4);
        BigFraction f2 = new BigFraction(3, 4);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testHashCodeConsistent() {
        BigFraction f = new BigFraction(7, 11);
        int h1 = f.hashCode();
        int h2 = f.hashCode();
        assertEquals(h1, h2);
    }

    @Test
    public void testToString() {
        BigFraction f = new BigFraction(3, 4);
        assertEquals("3 / 4", f.toString());
    }

    @Test
    public void testToStringInteger() {
        BigFraction f = new BigFraction(5, 1);
        assertEquals("5", f.toString());
    }

    @Test
    public void testToStringZero() {
        BigFraction f = new BigFraction(0, 1);
        assertEquals("0", f.toString());
    }

    @Test
    public void testNegate() {
        BigFraction f = new BigFraction(3, 4);
        BigFraction neg = f.negate();
        assertEquals(-3, neg.getNumeratorAsInt());
        assertEquals(4, neg.getDenominatorAsInt());
    }

    @Test
    public void testNegateNegative() {
        BigFraction f = new BigFraction(-5, 2);
        BigFraction neg = f.negate();
        assertEquals(5, neg.getNumeratorAsInt());
        assertEquals(2, neg.getDenominatorAsInt());
    }

    @Test
    public void testAbs() {
        BigFraction f = new BigFraction(-7, 3);
        BigFraction abs = f.abs();
        assertEquals(7, abs.getNumeratorAsInt());
        assertEquals(3, abs.getDenominatorAsInt());
    }

    @Test
    public void testAbsPositive() {
        BigFraction f = new BigFraction(7, 3);
        BigFraction abs = f.abs();
        assertEquals(7, abs.getNumeratorAsInt());
        assertEquals(3, abs.getDenominatorAsInt());
    }

    @Test
    public void testReciprocal() {
        BigFraction f = new BigFraction(3, 4);
        BigFraction rec = f.reciprocal();
        assertEquals(4, rec.getNumeratorAsInt());
        assertEquals(3, rec.getDenominatorAsInt());
    }

    @Test
    public void testAddInt() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.add(2);
        assertEquals(5, result.getNumeratorAsInt());
        assertEquals(2, result.getDenominatorAsInt());
    }

    @Test
    public void testAddLong() {
        BigFraction f = new BigFraction(3, 4);
        BigFraction result = f.add(1L);
        assertEquals(7, result.getNumeratorAsInt());
        assertEquals(4, result.getDenominatorAsInt());
    }

    @Test
    public void testAddBigInteger() {
        BigFraction f = new BigFraction(1, 3);
        BigFraction result = f.add(BigInteger.valueOf(2));
        assertEquals(7, result.getNumeratorAsInt());
        assertEquals(3, result.getDenominatorAsInt());
    }

    @Test
    public void testAddFraction() {
        BigFraction f1 = new BigFraction(1, 3);
        BigFraction f2 = new BigFraction(1, 6);
        BigFraction result = f1.add(f2);
        assertEquals(1, result.getNumeratorAsInt());
        assertEquals(2, result.getDenominatorAsInt());
    }

    @Test
    public void testAddFractionSameDenominator() {
        BigFraction f1 = new BigFraction(2, 5);
        BigFraction f2 = new BigFraction(1, 5);
        BigFraction result = f1.add(f2);
        assertEquals(3, result.getNumeratorAsInt());
        assertEquals(5, result.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testAddFractionNull() {
        BigFraction f = new BigFraction(1, 2);
        f.add((BigFraction) null);
    }

    @Test
    public void testSubtractInt() {
        BigFraction f = new BigFraction(7, 2);
        BigFraction result = f.subtract(2);
        assertEquals(3, result.getNumeratorAsInt());
        assertEquals(2, result.getDenominatorAsInt());
    }

    @Test
    public void testSubtractLong() {
        BigFraction f = new BigFraction(5, 3);
        BigFraction result = f.subtract(1L);
        assertEquals(2, result.getNumeratorAsInt());
        assertEquals(3, result.getDenominatorAsInt());
    }

    @Test
    public void testSubtractBigInteger() {
        BigFraction f = new BigFraction(10, 3);
        BigFraction result = f.subtract(BigInteger.valueOf(2));
        assertEquals(4, result.getNumeratorAsInt());
        assertEquals(3, result.getDenominatorAsInt());
    }

    @Test
    public void testSubtractFraction() {
        BigFraction f1 = new BigFraction(5, 6);
        BigFraction f2 = new BigFraction(1, 3);
        BigFraction result = f1.subtract(f2);
        assertEquals(1, result.getNumeratorAsInt());
        assertEquals(2, result.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testSubtractFractionNull() {
        BigFraction f = new BigFraction(1, 2);
        f.subtract((BigFraction) null);
    }

    @Test
    public void testMultiplyInt() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.multiply(3);
        assertEquals(2, result.getNumeratorAsInt());
        assertEquals(1, result.getDenominatorAsInt());
    }

    @Test
    public void testMultiplyLong() {
        BigFraction f = new BigFraction(3, 5);
        BigFraction result = f.multiply(5L);
        assertEquals(3, result.getNumeratorAsInt());
        assertEquals(1, result.getDenominatorAsInt());
    }

    @Test
    public void testMultiplyBigInteger() {
        BigFraction f = new BigFraction(1, 4);
        BigFraction result = f.multiply(BigInteger.valueOf(8));
        assertEquals(2, result.getNumeratorAsInt());
        assertEquals(1, result.getDenominatorAsInt());
    }

    @Test
    public void testMultiplyFraction() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(3, 5);
        BigFraction result = f1.multiply(f2);
        assertEquals(2, result.getNumeratorAsInt());
        assertEquals(5, result.getDenominatorAsInt());
    }

    @Test
    public void testMultiplyByZero() {
        BigFraction f = new BigFraction(5, 7);
        BigFraction result = f.multiply(0);
        assertEquals(0, result.getNumeratorAsInt());
        assertEquals(1, result.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testMultiplyFractionNull() {
        BigFraction f = new BigFraction(1, 2);
        f.multiply((BigFraction) null);
    }

    @Test
    public void testDivideInt() {
        BigFraction f = new BigFraction(3, 4);
        BigFraction result = f.divide(2);
        assertEquals(3, result.getNumeratorAsInt());
        assertEquals(8, result.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testDivideIntZero() {
        BigFraction f = new BigFraction(1, 2);
        f.divide(0);
    }

    @Test
    public void testDivideLong() {
        BigFraction f = new BigFraction(5, 2);
        BigFraction result = f.divide(5L);
        assertEquals(1, result.getNumeratorAsInt());
        assertEquals(2, result.getDenominatorAsInt());
    }

    @Test
    public void testDivideBigInteger() {
        BigFraction f = new BigFraction(4, 5);
        BigFraction result = f.divide(BigInteger.valueOf(2));
        assertEquals(2, result.getNumeratorAsInt());
        assertEquals(5, result.getDenominatorAsInt());
    }

    @Test
    public void testDivideFraction() {
        BigFraction f1 = new BigFraction(2, 3);
        BigFraction f2 = new BigFraction(4, 5);
        BigFraction result = f1.divide(f2);
        assertEquals(5, result.getNumeratorAsInt());
        assertEquals(6, result.getDenominatorAsInt());
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testDivideFractionZero() {
        BigFraction f1 = new BigFraction(1, 2);
        BigFraction f2 = new BigFraction(0, 1);
        f1.divide(f2);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testDivideFractionNull() {
        BigFraction f = new BigFraction(1, 2);
        f.divide((BigFraction) null);
    }

    @Test
    public void testPowInt() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.pow(3);
        assertEquals(8, result.getNumeratorAsInt());
        assertEquals(27, result.getDenominatorAsInt());
    }

    @Test
    public void testPowIntZero() {
        BigFraction f = new BigFraction(5, 7);
        BigFraction result = f.pow(0);
        assertEquals(1, result.getNumeratorAsInt());
        assertEquals(1, result.getDenominatorAsInt());
    }

    @Test
    public void testPowIntNegative() {
        BigFraction f = new BigFraction(2, 3);
        BigFraction result = f.pow(-2);
        assertEquals(9, result.getNumeratorAsInt());
        assertEquals(4, result.getDenominatorAsInt());
    }

    @Test
    public void testPowLong() {
        BigFraction f = new BigFraction(1, 2);
        BigFraction result = f.pow(4L);
        assertEquals(1, result.getNumeratorAsInt());
        assertEquals(16, result.getDenominatorAsInt());
    }

    @Test
    public void testPowBigInteger() {
        BigFraction f = new BigFraction(3, 4);
        BigFraction result = f.pow(BigInteger.TWO);
        assertEquals(9, result.getNumeratorAsInt());
        assertEquals(16, result.getDenominatorAsInt());
    }

    @Test
    public void testPowDouble() {
        BigFraction f = new BigFraction(1, 2);
        double result = f.pow(2.0);
        assertEquals(0.25, result, 1e-10);
    }

    @Test
    public void testBigDecimalValue() {
        BigFraction f = new BigFraction(1, 2);
        BigDecimal bd = f.bigDecimalValue();
        assertEquals(0.5, bd.doubleValue(), 1e-10);
    }

    @Test
    public void testBigDecimalValueWithRoundingMode() {
        BigFraction f = new BigFraction(1, 3);
        BigDecimal bd = f.bigDecimalValue(RoundingMode.DOWN.ordinal());
        assertNotNull(bd);
    }

    @Test
    public void testBigDecimalValueWithScaleAndRoundingMode() {
        BigFraction f = new BigFraction(2, 3);
        BigDecimal bd = f.bigDecimalValue(2, RoundingMode.HALF_UP.ordinal());
        assertNotNull(bd);
    }

    @Test
    public void testGetReducedFraction() {
        BigFraction f = BigFraction.getReducedFraction(6, 9);
        assertEquals(2, f.getNumeratorAsInt());
        assertEquals(3, f.getDenominatorAsInt());
    }

    @Test
    public void testGetReducedFractionZeroNumerator() {
        BigFraction f = BigFraction.getReducedFraction(0, 5);
        assertSame(BigFraction.ZERO, f);
    }

    @Test
    public void testReduce() {
        BigFraction f = new BigFraction(10, 15);
        BigFraction reduced = f.reduce();
        assertEquals(2, reduced.getNumeratorAsInt());
        assertEquals(3, reduced.getDenominatorAsInt());
    }

    @Test
    public void testGetField() {
        BigFraction f = new BigFraction(1, 2);
        assertNotNull(f.getField());
    }

    @Test
    public void testGetNumeratorAsInt() {
        BigFraction f = new BigFraction(42, 7);
        assertEquals(6, f.getNumeratorAsInt());
    }

    @Test
    public void testGetNumeratorAsLong() {
        BigFraction f = new BigFraction(Integer.MAX_VALUE + 1L, 2);
        assertEquals(Integer.MAX_VALUE + 1L, f.getNumeratorAsLong());
    }

    @Test
    public void testGetDenominatorAsInt() {
        BigFraction f = new BigFraction(3, 7);
        assertEquals(7, f.getDenominatorAsInt());
    }

    @Test
    public void testGetDenominatorAsLong() {
        BigFraction f = new BigFraction(1, 2);
        assertEquals(2L, f.getDenominatorAsLong());
    }

    @Test
    public void testGetNumerator() {
        BigFraction f = new BigFraction(11, 13);
        assertEquals(BigInteger.valueOf(11), f.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        BigFraction f = new BigFraction(11, 13);
        assertEquals(BigInteger.valueOf(13), f.getDenominator());
    }
}
```

```java
org/apache/commons/math3/fraction/FractionTest.java
```

```java
package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for Fraction class.
 */
public class FractionTest {

    @Test
    public void testConstructorDouble() throws FractionConversionException {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    public void testConstructorDoubleZero() throws FractionConversionException {
        Fraction f = new Fraction(0.0);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorInt() {
        Fraction f = new Fraction(5);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorIntZero() {
        Fraction f = new Fraction(0);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    public void testConstructorIntInt() {
        Fraction f = new Fraction(3, 4);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testConstructorIntIntReduced() {
        Fraction f = new Fraction(6, 8);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        Fraction f = new Fraction(3, -4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testConstructorIntIntZeroDenominator() {
        new Fraction(5, 0);
    }

    @Test
    public void testConstructorDoubleEpsilon() throws FractionConversionException {
        Fraction f = new Fraction(0.5, 0.01, 10);
        assertTrue(Math.abs(f.doubleValue() - 0.5) < 0.01);
    }

    @Test
    public void testConstructorDoubleMaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(0.333333, 100);
        assertTrue(f.getDenominator() <= 100);
    }

    @Test
    public void testEqualsIdentical() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(3, 4);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testEqualsNotEqual() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(4, 5);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testEqualsNull() {
        Fraction f = new Fraction(3, 4);
        assertFalse(f.equals(null));
    }

    @Test
    public void testEqualsOtherType() {
        Fraction f = new Fraction(3, 4);
        assertFalse(f.equals("3/4"));
    }

    @Test
    public void testCompareTo() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        assertTrue(f1.compareTo(f2) < 0);
        assertTrue(f2.compareTo(f1) > 0);
        Fraction f3 = new Fraction(2, 3);
        assertEquals(0, f1.compareTo(f3));
    }

    @Test
    public void testCompareToNegative() {
        Fraction f1 = new Fraction(-1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 1e-10);
    }

    @Test
    public void testFloatValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(0.25f, f.floatValue(), 1e-6f);
    }

    @Test
    public void testIntValue() {
        Fraction f = new Fraction(7, 2);
        assertEquals(3, f.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction f = new Fraction(15, 2);
        assertEquals(7L, f.longValue());
    }

    @Test
    public void testPercentageValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(25.0, f.percentageValue(), 1e-10);
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(3, 4);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testHashCodeConsistent() {
        Fraction f = new Fraction(7, 11);
        int h1 = f.hashCode();
        int h2 = f.hashCode();
        assertEquals(h1, h2);
    }

    @Test
    public void testToString() {
        Fraction f = new Fraction(3, 4);
        assertEquals("3 / 4", f.toString());
    }

    @Test
    public void testToStringInteger() {
        Fraction f = new Fraction(5, 1);
        assertEquals("5", f.toString());
    }

    @Test
    public void testToStringZero() {
        Fraction f = new Fraction(0, 1);
        assertEquals("0", f.toString());
    }

    @Test
    public void testNegate() {
        Fraction f = new Fraction(3, 4);
        Fraction neg = f.negate();
        assertEquals(-3, neg.getNumerator());
        assertEquals(4, neg.getDenominator());
    }

    @Test
    public void testNegateNegative() {
        Fraction f = new Fraction(-5, 2);
        Fraction neg = f.negate();
        assertEquals(5, neg.getNumerator());
        assertEquals(2, neg.getDenominator());
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testNegateMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        f.negate();
    }

    @Test
    public void testAbs() {
        Fraction f = new Fraction(-7, 3);
        Fraction abs = f.abs();
        assertEquals(7, abs.getNumerator());
        assertEquals(3, abs.getDenominator());
    }

    @Test
    public void testAbsPositive() {
        Fraction f = new Fraction(7, 3);
        Fraction abs =