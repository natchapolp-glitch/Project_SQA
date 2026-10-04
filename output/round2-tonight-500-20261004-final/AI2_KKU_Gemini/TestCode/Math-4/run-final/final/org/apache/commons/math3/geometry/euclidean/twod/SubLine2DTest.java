package org.apache.commons.math3.geometry.euclidean.twod;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SubLine2DTest {

    @Test
    public void testCreationAndSegments() {
        Vector2D p1 = new Vector2D(0.0, 0.0);
        Vector2D p2 = new Vector2D(2.0, 0.0);
        SubLine subLine = new SubLine(p1, p2);

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(p1, segments.get(0).getStart());
        assertEquals(p2, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionValid() {
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector2D(1.0, -1.0), new Vector2D(1.0, 1.0));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(1.0, intersection.getX(), 1e-10);
        assertEquals(0.0, intersection.getY(), 1e-10);
    }

    @Test
    public void testIntersectionNoIntersection() {
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector2D(0.0, 1.0), new Vector2D(1.0, 1.0));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionEndpointsHandling() {
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector2D(1.0, -1.0), new Vector2D(1.0, 1.0));

        Vector2D intersectionClosed = subLine1.intersection(subLine2, true);
        assertNotNull(intersectionClosed);

        Vector2D intersectionOpen = subLine1.intersection(subLine2, false);
        assertNull(intersectionOpen);
    }

    @Test
    public void testSegmentConstructor() {
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        SubLine subLine = new SubLine(segment);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
    }
}
