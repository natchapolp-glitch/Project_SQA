package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MathArraysTest {

    private static final double EPS = 1e-14;

    @Test
    public void testScaleAndScaleInPlace() {
        double[] arr = {1.0, 2.0, 3.0};
        double[] scaled = MathArrays.scale(2.5, arr);
        assertArrayEquals(new double[]{2.5, 5.0, 7.5}, scaled, EPS);

        MathArrays.scaleInPlace(2.0, arr);
        assertArrayEquals(new double[]{2.0, 4.0, 6.0}, arr, EPS);
    }

    @Test
    public void testEbeOperations() {
        double[] a = {1.0, 4.0, 9.0};
        double[] b = {1.0, 2.0, 3.0};

        assertArrayEquals(new double[]{2.0, 6.0, 12.0}, MathArrays.ebeAdd(a, b), EPS);
        assertArrayEquals(new double[]{0.0, 2.0, 6.0}, MathArrays.ebeSubtract(a, b), EPS);
        assertArrayEquals(new double[]{1.0, 8.0, 27.0}, MathArrays.ebeMultiply(a, b), EPS);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, MathArrays.ebeDivide(a, b), EPS);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEbeAddDimensionMismatch() {
        double[] a = {1.0, 2.0};
        double[] b = {1.0, 2.0, 3.0};
        MathArrays.ebeAdd(a, b);
    }

    @Test
    public void testDistances() {
        double[] p1 = {1.0, 2.0};
        double[] p2 = {4.0, 6.0};

        assertEquals(7.0, MathArrays.distance1(p1, p2), EPS);
        assertEquals(5.0, MathArrays.distance(p1, p2), EPS);
        assertEquals(4.0, MathArrays.distanceInf(p1, p2), EPS);

        int[] ip1 = {1, 2};
        int[] ip2 = {4, 6};
        assertEquals(7, MathArrays.distance1(ip1, ip2));
        assertEquals(5.0, MathArrays.distance(ip1, ip2), EPS);
        assertEquals(4, MathArrays.distanceInf(ip1, ip2));
    }

    @Test
    public void testIsMonotonic() {
        Double[] increasing = {1.0, 2.0, 2.0, 3.0};
        assertTrue(MathArrays.isMonotonic(increasing, MathArrays.OrderDirection.INCREASING, false));
        assertFalse(MathArrays.isMonotonic(increasing, MathArrays.OrderDirection.INCREASING, true));

        double[] decreasing = {5.0, 3.0, 1.0};
        assertTrue(MathArrays.isMonotonic(decreasing, MathArrays.OrderDirection.DECREASING, true));
    }

    @Test(expected = NonMonotonicSequenceException.class)
    public void testCheckOrderViolation() {
        double[] values = {1.0, 3.0, 2.0};
        MathArrays.checkOrder(values, MathArrays.OrderDirection.INCREASING, true);
    }

    @Test
    public void testLinearCombination() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        double result = MathArrays.linearCombination(a, b);
        assertEquals(32.0, result, EPS);

        assertEquals(14.0, MathArrays.linearCombination(1.0, 2.0, 3.0, 4.0), EPS);
        assertEquals(32.0, MathArrays.linearCombination(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), EPS);
        assertEquals(56.0, MathArrays.linearCombination(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), EPS);
    }

    @Test
    public void testNormalizeArray() throws MathIllegalArgumentException, MathArithmeticException {
        double[] values = {1.0, 2.0, 3.0};
        double[] normalized = MathArrays.normalizeArray(values, 12.0);
        assertArrayEquals(new double[]{2.0, 4.0, 6.0}, normalized, EPS);
    }

    @Test
    public void testConvolve() {
        double[] u = {1.0, 2.0, 3.0};
        double[] v = {1.0, 1.0};
        double[] expected = {1.0, 3.0, 5.0, 3.0};
        assertArrayEquals(expected, MathArrays.convolve(u, v), EPS);
    }

    @Test(expected = NullArgumentException.class)
    public void testConvolveNull() {
        MathArrays.convolve(null, new double[]{1.0});
    }

    @Test(expected = NoDataException.class)
    public void testConvolveEmpty() {
        MathArrays.convolve(new double[0], new double[]{1.0});
    }
}
