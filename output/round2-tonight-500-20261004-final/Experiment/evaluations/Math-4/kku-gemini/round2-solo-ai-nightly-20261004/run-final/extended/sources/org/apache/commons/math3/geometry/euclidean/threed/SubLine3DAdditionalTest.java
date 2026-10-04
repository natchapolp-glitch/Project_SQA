package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class SubLine3DAdditionalTest {

    @Test
    public void testIntersectionParallelLines() {
        SubLine subLine1 = new SubLine(new Vector3D(0.0, 0.0, 0.0), new Vector3D(1.0, 0.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector3D(0.0, 1.0, 0.0), new Vector3D(1.0, 1.0, 0.0));
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionCollinearNoOverlap() {
        SubLine subLine1 = new SubLine(new Vector3D(0.0, 0.0, 0.0), new Vector3D(1.0, 0.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector3D(2.0, 0.0, 0.0), new Vector3D(3.0, 0.0, 0.0));
        assertNull(subLine1.intersection(subLine2, true));
    }
}
