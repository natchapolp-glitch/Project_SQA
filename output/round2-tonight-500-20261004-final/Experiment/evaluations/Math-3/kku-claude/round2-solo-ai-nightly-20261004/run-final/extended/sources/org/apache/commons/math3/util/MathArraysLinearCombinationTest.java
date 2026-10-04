package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;

public class MathArraysLinearCombinationTest {

    private static final double EPS = 1e-15;

    @Test
    public void testTwoTermConsistencyWithArrayOverload() {
        double a1 = 3.5, b1 = -2.5, a2 = 1.25, b2 = 4.75;
        double expected = MathArrays.linearCombination(
                new double[] {a1, a2}, new double[] {b1, b2});
        double actual = MathArrays.linearCombination(a1, b1, a2, b2);
        assertEquals(expected, actual, EPS);
    }

    @Test
    public void testThreeTermConsistencyWithArrayOverload() {
        double a1 = 2.0, b1 = 3.0, a2 = -1.5, b2 = 4.0, a3 = 0.5, b3 = -2.0;
        double expected = MathArrays.linearCombination(
                new double[] {a1, a2, a3}, new double[] {b1, b2, b3});
        double actual = MathArrays.linearCombination(a1, b1, a2, b2, a3, b3);
        assertEquals(expected, actual, EPS);
    }

    @Test
    public void testFourTermConsistencyWithArrayOverload() {
        double a1 = 1.0, b1 = 2.0, a2 = 3.0, b2 = 4.0,
               a3 = 5.0, b3 = 6.0, a4 = 7.0, b4 = 8.0;
        double expected = MathArrays.linearCombination(
                new double[] {a1, a2, a3, a4}, new double[] {b1, b2, b3, b4});
        double actual = MathArrays.linearCombination(a1, b1, a2, b2, a3, b3, a4, b4);
        assertEquals(expected, actual, EPS);
    }

    @Test
    public void testHighPrecisionCancellation() {
        double[] a = {1.0, 1.0e17, 1.0, -1.0e17};
        double[] b = {1.0e17, 1.0, -1.0e17, -1.0};
        double result = MathArrays.linearCombination(a, b);
        assertEquals(2.0e17, result, 1.0);
    }

    @Test
    public void testSymmetry() {
        double[] a = {1.3, -4.7, 2.2, 0.0001};
        double[] b = {5.6, 3.3, -1.1, 9999.0};
        double ab = MathArrays.linearCombination(a, b);
        double ba = MathArrays.linearCombination(b, a);
        assertEquals(ab, ba, 0.0);
    }

    @Test
    public void testSingleElementArray() {
        double[] a = {3.14159};
        double[] b = {2.71828};
        double expected = a[0] * b[0];
        double actual = MathArrays.linearCombination(a, b);
        assertEquals(expected, actual, 1e-12);
    }

    @Test
    public void testDimensionMismatchThrows() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {1.0, 2.0};
        try {
            MathArrays.linearCombination(a, b);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testNaNPropagation() {
        double[] a = {1.0, Double.NaN, 3.0};
        double[] b = {1.0, 2.0, 3.0};
        double result = MathArrays.linearCombination(a, b);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testInfinityPropagation() {
        double result = MathArrays.linearCombination(
                Double.POSITIVE_INFINITY, 1.0, 2.0, 3.0);
        assertTrue(Double.isInfinite(result));
        assertTrue(result > 0);
    }

    @Test
    public void testNegativeAndZeroValues() {
        double[] a = {-2.0, 0.0, 5.0};
        double[] b = {3.0, -0.0, -1.0};
        double expected = (-2.0 * 3.0) + (0.0 * -0.0) + (5.0 * -1.0);
        double actual = MathArrays.linearCombination(a, b);
        assertEquals(expected, actual, 1e-12);
    }
}
