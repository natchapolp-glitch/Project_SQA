package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionAdditionalTest {

    @Test(expected = NotPositiveException.class)
    public void testInvalidNegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test
    public void testUpperCumulativeProbabilityBoundsAndDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(2), 1e-12);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(6), 1e-12);
    }

    @Test
    public void testCumulativeProbabilityDomainEdgeCases() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(0.0, dist.cumulativeProbability(-5), 1e-12);
        Assert.assertEquals(1.0, dist.cumulativeProbability(10), 1e-12);
    }

    @Test
    public void testInnerCumulativeProbabilityDirections() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertTrue(dist.cumulativeProbability(3) > dist.cumulativeProbability(1));
        Assert.assertTrue(dist.upperCumulativeProbability(1) > dist.upperCumulativeProbability(4));
    }
}
