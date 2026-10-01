# Understanding the Task

This is a regression test generation task for the Apache Commons Math library. I need to create comprehensive, deterministic unit tests for `BigFraction` and `Fraction` classes from the fixed reference revision. The tests should exercise the API signatures listed, covering normal cases, boundaries, invalid inputs, and exception paths.

Let me analyze the key characteristics:
- **Build system**: Maven with JUnit 4.11 (from pom.xml)
- **Source level**: Java 1.5+ (maven.compiler.source = 1.5)
- **Key constraint**: Tests must be deterministic, with no random values or time-dependent behavior
- **Focus**: The double-constructor variants and their associated methods

I'll create comprehensive tests organized by functionality, ensuring each test is independent and reproducible.

```java
org/apache/commons/math3/fraction/BigFractionTest.java
```

```java
package org.apache.commons.math3.fraction;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Comprehensive regression tests for BigFraction constructed from double values.
 * Tests exercise the reference behavior of the fixed revision.
 */
public class BigFractionTest {

    private BigFraction halfFromDouble;
    private BigFraction oneThirdFromDouble;
    private BigFraction twoThirdsFromDouble;
    private BigFraction oneFromDouble;
    private BigFraction zeroFromDouble;
    private BigFraction negativeHalf;

    @Before
    public void setUp() {
        // Using exact double representations that will convert precisely
        halfFromDouble = new BigFraction(0.5);           // 1/2
        oneFromDouble = new BigFraction(1.0);            // 1/1
        zeroFromDouble = new BigFraction(0.0);           // 0/1
        negativeHalf = new BigFraction(-0.5);            // -1/2
        // Note: 1/3 and 2/3 won't be exact in double, but constructor handles this
        oneThirdFromDouble = new BigFraction(1.0 / 3.0);
        twoThirdsFromDouble = new BigFraction(2.0 / 3.0);
    }

    // ===== Constructor Tests (double) =====

    @Test
    public void testConstructorDoublePositiveHalf() {
        BigFraction frac = new BigFraction(0.5);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleOne() {
        BigFraction frac = new BigFraction(1.0);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.ONE, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleZero() {
        BigFraction frac = new BigFraction(0.0);
        assertEquals(BigInteger.ZERO, frac.getNumerator());
        assertEquals(BigInteger.ONE, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleNegativeHalf() {
        BigFraction frac = new BigFraction(-0.5);
        assertEquals(BigInteger.ONE.negate(), frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleQuarter() {
        BigFraction frac = new BigFraction(0.25);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(4), frac.getDenominator());
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoubleNaN() {
        new BigFraction(Double.NaN);
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoublePositiveInfinity() {
        new BigFraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = org.apache.commons.math3.exception.MathIllegalArgumentException.class)
    public void testConstructorDoubleNegativeInfinity() {
        new BigFraction(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testConstructorDoubleSmallPositive() {
        BigFraction frac = new BigFraction(0.125);  // 1/8
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(8), frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleSmallNegative() {
        BigFraction frac = new BigFraction(-0.125);  // -1/8
        assertEquals(BigInteger.ONE.negate(), frac.getNumerator());
        assertEquals(BigInteger.valueOf(8), frac.getDenominator());
    }

    // ===== equals() Tests =====

    @Test
    public void testEqualsIdentity() {
        assertTrue(halfFromDouble.equals(halfFromDouble));
    }

    @Test
    public void testEqualsEqualValues() {
        BigFraction anotherHalf = new BigFraction(1, 2);
        assertTrue(halfFromDouble.equals(anotherHalf));
    }

    @Test
    public void testEqualsNotEqual() {
        BigFraction oneQuarter = new BigFraction(0.25);
        assertFalse(halfFromDouble.equals(oneQuarter));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(halfFromDouble.equals(null));
    }

    @Test
    public void testEqualsWrongType() {
        assertFalse(halfFromDouble.equals("0.5"));
    }

    @Test
    public void testEqualsZeros() {
        BigFraction anotherZero = new BigFraction(0, 1);
        assertTrue(zeroFromDouble.equals(anotherZero));
    }

    @Test
    public void testEqualsNegatives() {
        BigFraction anotherNegHalf = new BigFraction(-1, 2);
        assertTrue(negativeHalf.equals(anotherNegHalf));
    }

    @Test
    public void testEqualsReducedFractions() {
        BigFraction reduced = new BigFraction(2, 4);  // Will be reduced to 1/2
        assertTrue(halfFromDouble.equals(reduced));
    }

    // ===== doubleValue() Tests =====

    @Test
    public void testDoubleValueHalf() {
        double value = halfFromDouble.doubleValue();
        assertEquals(0.5, value, 1e-15);
    }

    @Test
    public void testDoubleValueOne() {
        double value = oneFromDouble.doubleValue();
        assertEquals(1.0, value, 1e-15);
    }

    @Test
    public void testDoubleValueZero() {
        double value = zeroFromDouble.doubleValue();
        assertEquals(0.0, value, 1e-15);
    }

    // ===== floatValue() Tests =====

    @Test
    public void testFloatValueHalf() {
        float value = halfFromDouble.floatValue();
        assertEquals(0.5f, value, 1e-6f);
    }

    @Test
    public void testFloatValueOne() {
        float value = oneFromDouble.floatValue();
        assertEquals(1.0f, value, 1e-6f);
    }

    @Test
    public void testFloatValueZero() {
        float value = zeroFromDouble.floatValue();
        assertEquals(0.0f, value, 1e-6f);
    }

    // ===== intValue() Tests =====

    @Test
    public void testIntValueHalf() {
        int value = halfFromDouble.intValue();
        assertEquals(0, value);
    }

    @Test
    public void testIntValueOne() {
        int value = oneFromDouble.intValue();
        assertEquals(1, value);
    }

    @Test
    public void testIntValueNegativeHalf() {
        int value = negativeHalf.intValue();
        assertEquals(0, value);
    }

    // ===== longValue() Tests =====

    @Test
    public void testLongValueHalf() {
        long value = halfFromDouble.longValue();
        assertEquals(0L, value);
    }

    @Test
    public void testLongValueOne() {
        long value = oneFromDouble.longValue();
        assertEquals(1L, value);
    }

    // ===== Accessor Tests =====

    @Test
    public void testGetNumeratorHalf() {
        assertEquals(BigInteger.ONE, halfFromDouble.getNumerator());
    }

    @Test
    public void testGetDenominatorHalf() {
        assertEquals(BigInteger.valueOf(2), halfFromDouble.getDenominator());
    }

    @Test
    public void testGetNumeratorAsInt() {
        assertEquals(1, halfFromDouble.getNumeratorAsInt());
    }

    @Test
    public void testGetDenominatorAsInt() {
        assertEquals(2, halfFromDouble.getDenominatorAsInt());
    }

    @Test
    public void testGetNumeratorAsLong() {
        assertEquals(1L, halfFromDouble.getNumeratorAsLong());
    }

    @Test
    public void testGetDenominatorAsLong() {
        assertEquals(2L, halfFromDouble.getDenominatorAsLong());
    }

    // ===== percentageValue() Tests =====

    @Test
    public void testPercentageValueHalf() {
        double pct = halfFromDouble.percentageValue();
        assertEquals(50.0, pct, 1e-10);
    }

    @Test
    public void testPercentageValueOne() {
        double pct = oneFromDouble.percentageValue();
        assertEquals(100.0, pct, 1e-10);
    }

    @Test
    public void testPercentageValueZero() {
        double pct = zeroFromDouble.percentageValue();
        assertEquals(0.0, pct, 1e-10);
    }

    // ===== hashCode() Tests =====

    @Test
    public void testHashCodeConsistency() {
        int hash1 = halfFromDouble.hashCode();
        int hash2 = halfFromDouble.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashCodeEqualObjects() {
        BigFraction anotherHalf = new BigFraction(1, 2);
        assertEquals(halfFromDouble.hashCode(), anotherHalf.hashCode());
    }

    @Test
    public void testHashCodeDifferentObjects() {
        BigFraction oneQuarter = new BigFraction(1, 4);
        assertNotEquals(halfFromDouble.hashCode(), oneQuarter.hashCode());
    }

    // ===== compareTo() Tests =====

    @Test
    public void testCompareToEqual() {
        BigFraction anotherHalf = new BigFraction(1, 2);
        assertEquals(0, halfFromDouble.compareTo(anotherHalf));
    }

    @Test
    public void testCompareToLess() {
        BigFraction three = new BigFraction(3, 1);
        assertTrue(halfFromDouble.compareTo(three) < 0);
    }

    @Test
    public void testCompareToGreater() {
        BigFraction quarter = new BigFraction(1, 4);
        assertTrue(halfFromDouble.compareTo(quarter) > 0);
    }

    @Test
    public void testCompareToNegative() {
        assertTrue(negativeHalf.compareTo(halfFromDouble) < 0);
    }

    @Test
    public void testCompareToZero() {
        assertTrue(halfFromDouble.compareTo(zeroFromDouble) > 0);
    }

    // ===== toString() Tests =====

    @Test
    public void testToStringHalf() {
        String str = halfFromDouble.toString();
        assertEquals("1 / 2", str);
    }

    @Test
    public void testToStringOne() {
        String str = oneFromDouble.toString();
        assertEquals("1", str);
    }

    @Test
    public void testToStringZero() {
        String str = zeroFromDouble.toString();
        assertEquals("0", str);
    }

    @Test
    public void testToStringNegative() {
        String str = negativeHalf.toString();
        assertEquals("-1 / 2", str);
    }

    // ===== bigDecimalValue() Tests =====

    @Test
    public void testBigDecimalValueNoArgs() {
        BigDecimal bd = halfFromDouble.bigDecimalValue();
        assertEquals(new BigDecimal("0.5"), bd);
    }

    @Test
    public void testBigDecimalValueWithRoundingMode() {
        BigDecimal bd = halfFromDouble.bigDecimalValue(RoundingMode.HALF_UP.ordinal());
        assertNotNull(bd);
    }

    @Test
    public void testBigDecimalValueWithScaleAndRounding() {
        BigDecimal bd = halfFromDouble.bigDecimalValue(2, RoundingMode.HALF_UP.ordinal());
        assertNotNull(bd);
    }

    // ===== Arithmetic: add() Tests =====

    @Test
    public void testAddInt() {
        BigFraction result = halfFromDouble.add(1);
        assertEquals(new BigFraction(3, 2), result);
    }

    @Test
    public void testAddIntNegative() {
        BigFraction result = halfFromDouble.add(-1);
        assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testAddLong() {
        BigFraction result = halfFromDouble.add(2L);
        assertEquals(new BigFraction(5, 2), result);
    }

    @Test
    public void testAddBigInteger() {
        BigFraction result = halfFromDouble.add(BigInteger.valueOf(1));
        assertEquals(new BigFraction(3, 2), result);
    }

    @Test
    public void testAddBigFraction() {
        BigFraction quarter = new BigFraction(1, 4);
        BigFraction result = halfFromDouble.add(quarter);
        assertEquals(new BigFraction(3, 4), result);
    }

    @Test
    public void testAddBigFractionZero() {
        BigFraction result = halfFromDouble.add(zeroFromDouble);
        assertEquals(halfFromDouble, result);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullBigInteger() {
        halfFromDouble.add((BigInteger) null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullBigFraction() {
        halfFromDouble.add((BigFraction) null);
    }

    // ===== Arithmetic: subtract() Tests =====

    @Test
    public void testSubtractInt() {
        BigFraction result = oneFromDouble.subtract(1);
        assertEquals(zeroFromDouble, result);
    }

    @Test
    public void testSubtractIntNegative() {
        BigFraction result = halfFromDouble.subtract(-1);
        assertEquals(new BigFraction(3, 2), result);
    }

    @Test
    public void testSubtractLong() {
        BigFraction result = halfFromDouble.subtract(1L);
        assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testSubtractBigInteger() {
        BigFraction result = halfFromDouble.subtract(BigInteger.ONE);
        assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testSubtractBigFraction() {
        BigFraction quarter = new BigFraction(1, 4);
        BigFraction result = halfFromDouble.subtract(quarter);
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test
    public void testSubtractBigFractionZero() {
        BigFraction result = halfFromDouble.subtract(zeroFromDouble);
        assertEquals(halfFromDouble, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSubtractNullBigInteger() {
        halfFromDouble.subtract((BigInteger) null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubtractNullBigFraction() {
        halfFromDouble.subtract((BigFraction) null);
    }

    // ===== Arithmetic: multiply() Tests =====

    @Test
    public void testMultiplyInt() {
        BigFraction result = halfFromDouble.multiply(2);
        assertEquals(oneFromDouble, result);
    }

    @Test
    public void testMultiplyIntZero() {
        BigFraction result = halfFromDouble.multiply(0);
        assertEquals(zeroFromDouble, result);
    }

    @Test
    public void testMultiplyLong() {
        BigFraction result = halfFromDouble.multiply(3L);
        assertEquals(new BigFraction(3, 2), result);
    }

    @Test
    public void testMultiplyBigInteger() {
        BigFraction result = halfFromDouble.multiply(BigInteger.valueOf(2));
        assertEquals(oneFromDouble, result);
    }

    @Test
    public void testMultiplyBigFraction() {
        BigFraction quarter = new BigFraction(1, 4);
        BigFraction result = halfFromDouble.multiply(quarter);
        assertEquals(new BigFraction(1, 8), result);
    }

    @Test
    public void testMultiplyBigFractionZero() {
        BigFraction result = halfFromDouble.multiply(zeroFromDouble);
        assertEquals(zeroFromDouble, result);
    }

    @Test(expected = NullPointerException.class)
    public void testMultiplyNullBigInteger() {
        halfFromDouble.multiply((BigInteger) null);
    }

    @Test(expected = NullPointerException.class)
    public void testMultiplyNullBigFraction() {
        halfFromDouble.multiply((BigFraction) null);
    }

    // ===== Arithmetic: divide() Tests =====

    @Test
    public void testDivideInt() {
        BigFraction result = halfFromDouble.divide(2);
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testDivideIntZero() {
        halfFromDouble.divide(0);
    }

    @Test
    public void testDivideLong() {
        BigFraction result = halfFromDouble.divide(2L);
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test
    public void testDivideBigInteger() {
        BigFraction result = halfFromDouble.divide(BigInteger.valueOf(2));
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testDivideBigIntegerZero() {
        halfFromDouble.divide(BigInteger.ZERO);
    }

    @Test
    public void testDivideBigFraction() {
        BigFraction quarter = new BigFraction(1, 4);
        BigFraction result = halfFromDouble.divide(quarter);
        assertEquals(new BigFraction(2, 1), result);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testDivideBigFractionZero() {
        halfFromDouble.divide(zeroFromDouble);
    }

    @Test(expected = NullPointerException.class)
    public void testDivideNullBigInteger() {
        halfFromDouble.divide((BigInteger) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDivideNullBigFraction() {
        halfFromDouble.divide((BigFraction) null);
    }

    // ===== Unary Operations =====

    @Test
    public void testNegate() {
        BigFraction result = halfFromDouble.negate();
        assertEquals(new BigFraction(-1, 2), result);
    }

    @Test
    public void testNegateNegative() {
        BigFraction result = negativeHalf.negate();
        assertEquals(halfFromDouble, result);
    }

    @Test
    public void testAbs() {
        BigFraction result = negativeHalf.abs();
        assertEquals(halfFromDouble, result);
    }

    @Test
    public void testAbsPositive() {
        BigFraction result = halfFromDouble.abs();
        assertEquals(halfFromDouble, result);
    }

    @Test
    public void testReciprocal() {
        BigFraction result = halfFromDouble.reciprocal();
        assertEquals(new BigFraction(2, 1), result);
    }

    @Test
    public void testReciprocalReciprocal() {
        BigFraction result = halfFromDouble.reciprocal().reciprocal();
        assertEquals(halfFromDouble, result);
    }

    // ===== Power Operations =====

    @Test
    public void testPowInt() {
        BigFraction result = halfFromDouble.pow(2);
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test
    public void testPowIntZero() {
        BigFraction result = halfFromDouble.pow(0);
        assertEquals(oneFromDouble, result);
    }

    @Test
    public void testPowIntNegative() {
        BigFraction result = halfFromDouble.pow(-1);
        assertEquals(new BigFraction(2, 1), result);
    }

    @Test
    public void testPowLong() {
        BigFraction result = halfFromDouble.pow(2L);
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test
    public void testPowBigInteger() {
        BigFraction result = halfFromDouble.pow(BigInteger.valueOf(2));
        assertEquals(new BigFraction(1, 4), result);
    }

    @Test
    public void testPowDouble() {
        double result = halfFromDouble.pow(2.0);
        assertEquals(0.25, result, 1e-15);
    }

    @Test
    public void testPowDoubleOne() {
        double result = oneFromDouble.pow(2.0);
        assertEquals(1.0, result, 1e-15);
    }

    // ===== Other Operations =====

    @Test
    public void testReduce() {
        BigFraction unreduced = new BigFraction(2, 4);
        BigFraction result = unreduced.reduce();
        assertEquals(halfFromDouble, result);
    }

    @Test
    public void testReduceAlreadyReduced() {
        BigFraction result = halfFromDouble.reduce();
        assertEquals(halfFromDouble, result);
    }

    @Test
    public void testGetField() {
        assertNotNull(halfFromDouble.getField());
    }

    // ===== Static Factory Methods =====

    @Test
    public void testGetReducedFraction() {
        BigFraction result = BigFraction.getReducedFraction(2, 4);
        assertEquals(new BigFraction(1, 2), result);
    }

    @Test
    public void testGetReducedFractionZero() {
        BigFraction result = BigFraction.getReducedFraction(0, 5);
        assertEquals(BigFraction.ZERO, result);
    }

    @Test
    public void testGetReducedFractionNegative() {
        BigFraction result = BigFraction.getReducedFraction(-1, 2);
        assertEquals(new BigFraction(-1, 2), result);
    }

    // ===== Other Constructors =====

    @Test
    public void testConstructorInt() {
        BigFraction frac = new BigFraction(5);
        assertEquals(BigInteger.valueOf(5), frac.getNumerator());
        assertEquals(BigInteger.ONE, frac.getDenominator());
    }

    @Test
    public void testConstructorLong() {
        BigFraction frac = new BigFraction(5L);
        assertEquals(BigInteger.valueOf(5), frac.getNumerator());
        assertEquals(BigInteger.ONE, frac.getDenominator());
    }

    @Test
    public void testConstructorBigInteger() {
        BigFraction frac = new BigFraction(BigInteger.valueOf(5));
        assertEquals(BigInteger.valueOf(5), frac.getNumerator());
        assertEquals(BigInteger.ONE, frac.getDenominator());
    }

    @Test
    public void testConstructorIntIntNegativeDenominator() {
        BigFraction frac = new BigFraction(1, -2);
        assertEquals(BigInteger.ONE.negate(), frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test
    public void testConstructorBigIntegerBigInteger() {
        BigFraction frac = new BigFraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test(expected = org.apache.commons.math3.exception.ZeroException.class)
    public void testConstructorBigIntegerBigIntegerZeroDenom() {
        new BigFraction(BigInteger.valueOf(1), BigInteger.ZERO);
    }

    @Test
    public void testConstructorLongLong() {
        BigFraction frac = new BigFraction(1L, 2L);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleWithMaxDenominator() throws FractionConversionException {
        BigFraction frac = new BigFraction(0.5, 10);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleWithEpsilonAndMaxIterations() throws FractionConversionException {
        BigFraction frac = new BigFraction(0.5, 1e-5, 100);
        assertEquals(BigInteger.ONE, frac.getNumerator());
        assertEquals(BigInteger.valueOf(2), frac.getDenominator());
    }

}
```

```java
org/apache/commons/math3/fraction/FractionTest.java
```

```java
package org.apache.commons.math3.fraction;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Comprehensive regression tests for Fraction constructed from double values.
 * Tests exercise the reference behavior of the fixed revision.
 */
public class FractionTest {

    private Fraction halfFromDouble;
    private Fraction oneFromDouble;
    private Fraction zeroFromDouble;
    private Fraction negativeHalf;
    private Fraction quarter;

    @Before
    public void setUp() throws FractionConversionException {
        halfFromDouble = new Fraction(0.5);
        oneFromDouble = new Fraction(1.0);
        zeroFromDouble = new Fraction(0.0);
        negativeHalf = new Fraction(-0.5);
        quarter = new Fraction(0.25);
    }

    // ===== Constructor Tests (double) =====

    @Test
    public void testConstructorDoubleHalf() throws FractionConversionException {
        Fraction frac = new Fraction(0.5);
        assertEquals(1, frac.getNumerator());
        assertEquals(2, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleOne() throws FractionConversionException {
        Fraction frac = new Fraction(1.0);
        assertEquals(1, frac.getNumerator());
        assertEquals(1, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleZero() throws FractionConversionException {
        Fraction frac = new Fraction(0.0);
        assertEquals(0, frac.getNumerator());
        assertEquals(1, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleNegativeHalf() throws FractionConversionException {
        Fraction frac = new Fraction(-0.5);
        assertEquals(-1, frac.getNumerator());
        assertEquals(2, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleQuarter() throws FractionConversionException {
        Fraction frac = new Fraction(0.25);
        assertEquals(1, frac.getNumerator());
        assertEquals(4, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleSmallPositive() throws FractionConversionException {
        Fraction frac = new Fraction(0.125);
        assertEquals(1, frac.getNumerator());
        assertEquals(8, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleSmallNegative() throws FractionConversionException {
        Fraction frac = new Fraction(-0.125);
        assertEquals(-1, frac.getNumerator());
        assertEquals(8, frac.getDenominator());
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleNaN() throws FractionConversionException {
        // Should fail to converge when trying to represent NaN
        new Fraction(Double.NaN);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoublePositiveInfinity() throws FractionConversionException {
        new Fraction(Double.POSITIVE_INFINITY);
    }

    @Test(expected = FractionConversionException.class)
    public void testConstructorDoubleNegativeInfinity() throws FractionConversionException {
        new Fraction(Double.NEGATIVE_INFINITY);
    }

    // ===== Constructor with epsilon and max iterations =====

    @Test
    public void testConstructorDoubleWithEpsilon() throws FractionConversionException {
        Fraction frac = new Fraction(0.5, 1e-5, 100);
        assertEquals(1, frac.getNumerator());
        assertEquals(2, frac.getDenominator());
    }

    @Test
    public void testConstructorDoubleWithMaxDenominator() throws FractionConversionException {
        Fraction frac = new Fraction(0.5, 10);
        assertEquals(1, frac.getNumerator());
        assertEquals(2, frac.getDenominator());
    }

    // ===== equals() Tests =====

    @Test
    public void testEqualsIdentity() {
        assertTrue(halfFromDouble.equals(halfFromDouble));
    }

    @Test
    public void testEqualsEqualValues() {
        Fraction anotherHalf = new Fraction(1, 2);
        assertTrue(halfFromDouble.equals(anotherHalf));
    }

    @Test
    public void testEqualsNotEqual() {
        Fraction oneThird = new Fraction(1, 3);
        assertFalse(halfFromDouble.equals(oneThird));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(halfFromDouble.equals(null));
    }

    @Test