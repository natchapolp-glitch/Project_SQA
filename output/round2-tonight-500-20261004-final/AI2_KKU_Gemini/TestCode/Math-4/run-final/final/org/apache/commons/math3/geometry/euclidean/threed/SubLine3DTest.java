package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SubLine3DTest {

    @Test
    public void testCreationAndSegments() {
        Vector3D p1 = new Vector3D(0.0, 0.0, 0.0);
        Vector3D p2 = new Vector3D(1.0, 0.0, 0.0);
        SubLine subLine = new SubLine(p1, p2);

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(p1, segments.get(0).getStart());
        assertEquals(p2, segments.get(0).getEnd());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testCoincidentPointsThrowsException() {
        Vector3D p = new Vector3D(1.0, 2.0, 3.0);
        new SubLine(p, p);
    }

    @Test
    public void testIntersectionValid() {
        SubLine subLine1 = new SubLine(new Vector3D(0.0, 0.0, 0.0), new Vector3D(2.0, 0.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector3D(1.0, -1.0, 0.0), new Vector3D(1.0, 1.0, 0.0));

        Vector3D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(1.0, intersection.getX(), 1e-10);
        assertEquals(0.0, intersection.getY(), 1e-10);
        assertEquals(0.0, intersection.getZ(), 1e-10);
    }

    @Test
    public void testIntersectionNoIntersection() {
        SubLine subLine1 = new SubLine(new Vector3D(0.0, 0.0, 0.0), new Vector3D(1.0, 0.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector3D(0.0, 1.0, 0.0), new Vector3D(1.0, 1.0, 0.0));

        Vector3D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionEndpointsHandling() {
        SubLine subLine1 = new SubLine(new Vector3D(0.0, 0.0, 0.0), new Vector3D(1.0, 0.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector3D(1.0, -1.0, 0.0), new Vector3D(1.0, 1.0, 0.0));

        Vector3D intersectionClosed = subLine1.intersection(subLine2, true);
        assertNotNull(intersectionClosed);

        Vector3D intersectionOpen = subLine1.intersection(subLine2, false);
        assertNull(intersectionOpen);
    }

    @Test
    public void testSegmentConstructor() {
        Segment segment = new Segment(new Vector3D(1.0, 2.0, 3.0), new Vector3D(4.0, 5.0, 6.0), new Line(new Vector3D(1.0, 2.0, 3.0), new Vector3D(4.0, 5.0, 6.0)));
        SubLine subLine = new SubLine(segment);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
    }
}
