package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testAccessors() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 40, 20);
        Assert.assertEquals(100, dist.getPopulationSize());
        Assert.assertEquals(40, dist.getNumberOfSuccesses());
        Assert.assertEquals(20, dist.getSampleSize());
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSupportBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 40, 20);
        Assert.assertEquals(0, dist.getSupportLowerBound());
        Assert.assertEquals(20, dist.getSupportUpperBound());
    }

    @Test
    public void testMeanAndVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 40, 20);
        Assert.assertEquals(8.0, dist.getNumericalMean(), 1e-12);
        Assert.assertEquals(3.878787878787879, dist.getNumericalVariance(), 1e-12);
        // Verify variance caching
        Assert.assertEquals(3.878787878787879, dist.getNumericalVariance(), 1e-12);
    }

    @Test
    public void testProbabilityAndCumulative() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(0.0, dist.probability(-1), 1e-12);
        Assert.assertEquals(0.0, dist.probability(6), 1e-12);
        Assert.assertTrue(dist.probability(2) > 0.0);

        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 1e-12);
        Assert.assertEquals(1.0, dist.cumulativeProbability(5), 1e-12);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(-1), 1e-12);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(6), 1e-12);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSuccessesExceedPopulation() {
        new HypergeometricDistribution(10, 11, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeExceedsPopulation() {
        new HypergeometricDistribution(10, 5, 11);
    }
}
