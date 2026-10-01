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

    

    

    

    

    

    // ===== percentageValue() Tests =====

    

    

    

    // ===== hashCode() Tests =====

    

    

    

    // ===== compareTo() Tests =====

    

    

    

    

    

    // ===== toString() Tests =====

    

    

    

    

    // ===== bigDecimalValue() Tests =====

    

    

    

    // ===== Arithmetic: add() Tests =====

    

    

    

    

    

    

    

    

    // ===== Arithmetic: subtract() Tests =====

    

    

    

    

    

    

    

    

    // ===== Arithmetic: multiply() Tests =====

    

    

    

    

    

    

    

    

    // ===== Arithmetic: divide() Tests =====

    

    

    

    

    

    

    

    

    

    // ===== Unary Operations =====

    

    

    

    

    

    

    // ===== Power Operations =====

    

    

    

    

    

    

    

    // ===== Other Operations =====

    

    

    

    // ===== Static Factory Methods =====

    

    

    

    // ===== Other Constructors =====

    

    

    

    

    

    

    

    

    

}
