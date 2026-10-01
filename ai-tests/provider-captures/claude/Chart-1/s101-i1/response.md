The supplied production source is `AbstractCategoryItemRenderer`, but the eligible API list targets `AreaRenderer`. `AreaRenderer`'s own source wasn't supplied. The listed methods are all inherited or protected members declared on `AbstractCategoryItemRenderer`, and `AreaRenderer` has a public no-arg constructor. So the tests instantiate `AreaRenderer` and call only those inherited members, without relying on any `AreaRenderer`-specific behaviour.

- **Build:** `ant/build.xml` compiles at `source="1.4"` against a plain `lib/junit.jar`, so I used JUnit 3 style (`TestCase`, `test*` methods) and no generics, annotations or autoboxing.
- **Package:** the test sits in `org.jfree.chart.renderer.category` so it can call the protected `createState`, `findRangeBounds(dataset, boolean)`, `getDomainAxis`, `getRangeAxis` and `addEntity`.
- **Drawing tests:** they render to an in-memory `BufferedImage`, so they need no display.
- **Not asserted:** the return value of `removeAnnotation`, whose foreground and background results are combined with `&`, and the 8-argument `addEntity`, which is outside the eligible list.
- **Count:** 30 test methods.

`org/jfree/chart/renderer/category/AreaRendererInheritedApiTest.java`

```java
package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryLineAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Regression tests for members that AreaRenderer inherits from
 * AbstractCategoryItemRenderer.
 */
public class AreaRendererInheritedApiTest extends TestCase {

    private static final Rectangle2D AREA
            = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);

    private static class CountingListener implements RendererChangeListener {
        int count;
        public void rendererChanged(RendererChangeEvent event) {
            this.count++;
        }
    }

    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "S1", "C1");
        d.addValue(2.0, "S1", "C2");
        d.addValue(10.0, "S2", "C1");
        d.addValue(20.0, "S2", "C2");
        return d;
    }

    private static CategoryPlot createPlot(AreaRenderer r,
            CategoryDataset d) {
        NumberAxis rangeAxis = new NumberAxis("Value");
        rangeAxis.setRange(0.0, 100.0);
        return new CategoryPlot(d, new CategoryAxis("Category"), rangeAxis, r);
    }

    private static BufferedImage newImage() {
        return new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    }

    private static boolean isBlank(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (img.getRGB(x, y) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // 1
    public void testPassCountAndPlotAccessors() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(1, r.getPassCount());
        assertNull(r.getPlot());
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
        try {
            r.setPlot(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        CategoryPlot p = new CategoryPlot();
        r.setPlot(p);
        assertSame(p, r.getPlot());
    }

    // 2
    public void testItemLabelGeneratorPrecedence() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getBaseItemLabelGenerator());
        assertNull(r.getItemLabelGenerator(0, 0, false));
        CategoryItemLabelGenerator base
                = new StandardCategoryItemLabelGenerator();
        CategoryItemLabelGenerator s0
                = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(base);
        assertSame(base, r.getBaseItemLabelGenerator());
        assertSame(base, r.getItemLabelGenerator(0, 0, false));
        r.setSeriesItemLabelGenerator(0, s0);
        assertSame(s0, r.getSeriesItemLabelGenerator(0));
        assertSame(s0, r.getItemLabelGenerator(0, 3, true));
        assertSame(base, r.getItemLabelGenerator(1, 0, false));
        assertNull(r.getSeriesItemLabelGenerator(1));
        r.setSeriesItemLabelGenerator(0, null);
        assertSame(base, r.getItemLabelGenerator(0, 0, false));
    }

    // 3
    public void testItemLabelGeneratorNotification() {
        AreaRenderer r = new AreaRenderer();
        CountingListener l = new CountingListener();
        r.addChangeListener(l);
        CategoryItemLabelGenerator g = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(g);
        assertEquals(1, l.count);
        r.setBaseItemLabelGenerator(g, false);
        assertEquals(1, l.count);
        r.setBaseItemLabelGenerator(g, true);
        assertEquals(2, l.count);
        r.setSeriesItemLabelGenerator(0, g);
        assertEquals(3, l.count);
        r.setSeriesItemLabelGenerator(0, g, false);
        assertEquals(3, l.count);
        r.setSeriesItemLabelGenerator(0, g, true);
        assertEquals(4, l.count);
    }

    // 4
    public void testToolTipGeneratorPrecedence() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getBaseToolTipGenerator());
        assertNull(r.getToolTipGenerator(0, 0, false));
        CategoryToolTipGenerator base = new StandardCategoryToolTipGenerator();
        CategoryToolTipGenerator s1 = new StandardCategoryToolTipGenerator();
        r.setBaseToolTipGenerator(base);
        assertSame(base, r.getBaseToolTipGenerator());
        assertSame(base, r.getToolTipGenerator(0, 0, false));
        r.setSeriesToolTipGenerator(1, s1);
        assertSame(s1, r.getSeriesToolTipGenerator(1));
        assertSame(s1, r.getToolTipGenerator(1, 0, false));
        assertSame(base, r.getToolTipGenerator(0, 0, true));
        assertNull(r.getSeriesToolTipGenerator(0));
    }

    // 5
    public void testUrlGeneratorPrecedence() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getBaseURLGenerator());
        assertNull(r.getURLGenerator(0, 0, false));
        CategoryURLGenerator base = new StandardCategoryURLGenerator();
        CategoryURLGenerator s2 = new StandardCategoryURLGenerator();
        r.setBaseURLGenerator(base);
        assertSame(base, r.getBaseURLGenerator());
        assertSame(base, r.getURLGenerator(0, 0, false));
        r.setSeriesURLGenerator(2, s2);
        assertSame(s2, r.getSeriesURLGenerator(2));
        assertSame(s2, r.getURLGenerator(2, 1, false));
        assertSame(base, r.getURLGenerator(1, 1, false));
        assertNull(r.getSeriesURLGenerator(1));
    }

    // 6
    public void testToolTipAndUrlGeneratorNotification() {
        AreaRenderer r = new AreaRenderer();
        CountingListener l = new CountingListener();
        r.addChangeListener(l);
        CategoryToolTipGenerator t = new StandardCategoryToolTipGenerator();
        CategoryURLGenerator u = new StandardCategoryURLGenerator();
        r.setBaseToolTipGenerator(t);
        assertEquals(1, l.count);
        r.setBaseToolTipGenerator(t, false);
        assertEquals(1, l.count);
        r.setSeriesToolTipGenerator(0, t);
        assertEquals(2, l.count);
        r.setSeriesToolTipGenerator(0, t, false);
        assertEquals(2, l.count);
        r.setBaseURLGenerator(u);
        assertEquals(3, l.count);
        r.setBaseURLGenerator(u, false);
        assertEquals(3, l.count);
        r.setSeriesURLGenerator(0, u);
        assertEquals(4, l.count);
        r.setSeriesURLGenerator(0, u, false);
        assertEquals(4, l.count);
    }

    // 7
    public void testLegendItemGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener l = new CountingListener();
        r.addChangeListener(l);
        assertTrue(r.getLegendItemLabelGenerator()
                instanceof StandardCategorySeriesLabelGenerator);
        assertNull(r.getLegendItemToolTipGenerator());
        assertNull(r.getLegendItemURLGenerator());
        try {
            r.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, l.count);
        CategorySeriesLabelGenerator g = new StandardCategorySeriesLabelGenerator();
        r.setLegendItemLabelGenerator(g);
        assertSame(g, r.getLegendItemLabelGenerator());
        assertEquals(1, l.count);
        r.setLegendItemToolTipGenerator(g);
        assertSame(g, r.getLegendItemToolTipGenerator());
        assertEquals(2, l.count);
        r.setLegendItemURLGenerator(g);
        assertSame(g, r.getLegendItemURLGenerator());
        assertEquals(3, l.count);
        r.setLegendItemToolTipGenerator(null);
        assertNull(r.getLegendItemToolTipGenerator());
        assertEquals(4, l.count);
    }

    // 8
    public void testAnnotationsAffectEquality() {
        AreaRenderer r = new AreaRenderer();
        AreaRenderer fresh = new AreaRenderer();
        CountingListener l = new CountingListener();
        r.addChangeListener(l);
        try {
            r.addAnnotation(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            r.addAnnotation(null, Layer.BACKGROUND);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, l.count);
        CategoryLineAnnotation a = new CategoryLineAnnotation("C1", 1.0,
                "C2", 2.0, Color.RED, new BasicStroke(1.0f));
        r.addAnnotation(a);
        assertEquals(1, l.count);
        assertTrue(!r.equals(fresh));
        r.removeAnnotation(a);
        assertTrue(r.equals(fresh));
        r.addAnnotation(a, Layer.BACKGROUND);
        assertTrue(!r.equals(fresh));
        r.removeAnnotations();
        assertTrue(r.equals(fresh));
    }

    // 9
    public void testInitialiseUpdatesCountsAndRejectsNullPlot() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        d.addValue(3.0, "S1", "C3");
        CategoryPlot plot = new CategoryPlot();
        CategoryItemRendererState state = r.initialise(null, AREA, plot, d,
                null);
        assertNotNull(state);
        assertSame(plot, r.getPlot());
        assertEquals(2, r.getRowCount());
        assertEquals(3, r.getColumnCount());
        int[] visible = state.getVisibleSeriesArray();
        assertEquals(2, visible.length);
        assertEquals(0, visible[0]);
        assertEquals(1, visible[1]);
        try {
            r.initialise(null, AREA, null, d, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    // 10
    public void testInitialiseWithNullDatasetResetsCounts() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        r.initialise(null, AREA, plot, createDataset(), null);
        assertEquals(2, r.getRowCount());
        CategoryItemRendererState state = r.initialise(null, AREA, plot,
                null, null);
        assertNotNull(state);
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
        assertEquals(0, state.getVisibleSeriesArray().length);
    }

    // 11
    public void testFindRangeBounds() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.findRangeBounds(null));
        assertNull(r.findRangeBounds(null, true));
        assertNull(r.findRangeBounds(new DefaultCategoryDataset()));
        DefaultCategoryDataset d = createDataset();
        Range range = r.findRangeBounds(d);
        assertEquals(1.0, range.getLowerBound(), 0.0);
        assertEquals(20.0, range.getUpperBound(), 0.0);
        Range withInterval = r.findRangeBounds(d, true);
        assertEquals(1.0, withInterval.getLowerBound(), 0.0);
        assertEquals(20.0, withInterval.getUpperBound(), 0.0);
    }

    // 12
    public void testFindRangeBoundsVisibleSeriesOnly() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        r.setSeriesVisible(1, Boolean.FALSE);
        Range range = r.findRangeBounds(d);
        assertEquals(1.0, range.getLowerBound(), 0.0);
        assertEquals(2.0, range.getUpperBound(), 0.0);
        r.setDataBoundsIncludesVisibleSeriesOnly(false);
        range = r.findRangeBounds(d);
        assertEquals(1.0, range.getLowerBound(), 0.0);
        assertEquals(20.0, range.getUpperBound(), 0.0);
    }

    // 13
    public void testGetItemMiddle() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryAxis axis = new CategoryAxis("Category");
        double m1 = r.getItemMiddle("S1", "C1", d, axis, AREA,
                RectangleEdge.BOTTOM);
        double m2 = r.getItemMiddle("S1", "C2", d, axis, AREA,
                RectangleEdge.BOTTOM);
        assertEquals(axis.getCategoryMiddle("C1", d.getColumnKeys(), AREA,
                RectangleEdge.BOTTOM), m1, 1.0e-9);
        assertEquals(axis.getCategoryMiddle("C2", d.getColumnKeys(), AREA,
                RectangleEdge.BOTTOM), m2, 1.0e-9);
        assertTrue(m1 > 0.0 && m1 < m2 && m2 < 100.0);
    }

    // 14
    public void testGetDomainAndRangeAxis() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryPlot plot = createPlot(r, d);
        assertSame(plot.getDomainAxis(), r.getDomainAxis(plot, d));
        assertSame(plot.getRangeAxis(), r.getRangeAxis(plot, 0));
        // no axis at index 1, so the primary range axis is used
        assertSame(plot.getRangeAxis(), r.getRangeAxis(plot, 1));
    }

    // 15
    public void testGetLegendItem() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getLegendItem(0, 0));
        DefaultCategoryDataset d = createDataset();
        createPlot(r, d);
        LegendItem item = r.getLegendItem(0, 0);
        assertNotNull(item);
        assertEquals("S1", item.getLabel());
        assertEquals("S1", item.getDescription());
        assertEquals("S1", item.getSeriesKey());
        assertEquals(0, item.getSeriesIndex());
        assertEquals(0, item.getDatasetIndex());
        assertSame(d, item.getDataset());
        assertNull(item.getToolTipText());
        assertNull(item.getURLText());
        LegendItem item2 = r.getLegendItem(0, 1);
        assertEquals("S2", item2.getLabel());
        assertEquals(1, item2.getSeriesIndex());

        r.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator());
        r.setLegendItemURLGenerator(new StandardCategorySeriesLabelGenerator());
        item = r.getLegendItem(0, 0);
        assertEquals("S1", item.getToolTipText());
        assertEquals("S1", item.getURLText());

        r.setSeriesVisible(0, Boolean.FALSE);
        assertNull(r.getLegendItem(0, 0));
        r.setSeriesVisible(0, Boolean.TRUE);
        r.setSeriesVisibleInLegend(0, Boolean.FALSE);
        assertNull(r.getLegendItem(0, 0));
        assertNotNull(r.getLegendItem(0, 1));
    }

    // 16
    public void testGetLegendItems() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(0, r.getLegendItems().getItemCount());
        DefaultCategoryDataset d = createDataset();
        CategoryPlot plot = createPlot(r, d);
        LegendItemCollection c = r.getLegendItems();
        assertEquals(2, c.getItemCount());
        assertEquals("S1", c.get(0).getLabel());
        assertEquals("S2", c.get(1).getLabel());

        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        c = r.getLegendItems();
        assertEquals(2, c.getItemCount());
        assertEquals("S2", c.get(0).getLabel());
        assertEquals("S1", c.get(1).getLabel());

        plot.setRowRenderingOrder(SortOrder.ASCENDING);
        r.setSeriesVisibleInLegend(0, Boolean.FALSE);
        c = r.getLegendItems();
        assertEquals(1, c.getItemCount());
        assertEquals("S2", c.get(0).getLabel());
    }

    // 17
    public void testEqualsAndHashCode() {
        AreaRenderer a = new AreaRenderer();
        AreaRenderer b = new AreaRenderer();
        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(!a.equals(null));
        assertTrue(!a.equals("not a renderer"));

        a.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertTrue(!a.equals(b));
        b.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        a.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());
        assertTrue(!a.equals(b));
        b.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());
        assertTrue(a.equals(b));

        a.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertTrue(!a.equals(b));
        b.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertTrue(a.equals(b));

        a.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator());
        assertTrue(!a.equals(b));
        b.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator());
        assertTrue(a.equals(b));
    }

    // 18
    public void testClone() throws CloneNotSupportedException {
        AreaRenderer r = new AreaRenderer();
        r.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        r.setBaseURLGenerator(new StandardCategoryURLGenerator());
        r.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator());
        AreaRenderer c = (AreaRenderer) r.clone();
        assertNotSame(r, c);
        assertSame(r.getClass(), c.getClass());
        assertTrue(r.equals(c));
        assertNotSame(r.getBaseToolTipGenerator(), c.getBaseToolTipGenerator());
        assertNotSame(r.getBaseItemLabelGenerator(),
                c.getBaseItemLabelGenerator());
        assertNotSame(r.getBaseURLGenerator(), c.getBaseURLGenerator());
        assertNotSame(r.getLegendItemLabelGenerator(),
                c.getLegendItemLabelGenerator());

        // the series generator lists must be independent
        c.setSeriesToolTipGenerator(1, new StandardCategoryToolTipGenerator());
        assertNull(r.getSeriesToolTipGenerator(1));
        assertTrue(!r.equals(c));
    }

    // 19
    public void testCloneWithNonCloneableGeneratorFails() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseToolTipGenerator(new CategoryToolTipGenerator() {
            public String generateToolTip(CategoryDataset dataset, int row,
                    int column) {
                return "tip";
            }
        });
        try {
            r.clone();
            fail("Expected CloneNotSupportedException");
        }
        catch (CloneNotSupportedException e) {
            // expected
        }
    }

    // 20
    public void testGetDrawingSupplier() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getDrawingSupplier());
        CategoryPlot plot = createPlot(r, createDataset());
        assertNotNull(r.getDrawingSupplier());
        assertSame(plot.getDrawingSupplier(), r.getDrawingSupplier());
    }

    // 21
    public void testDrawBackgroundFillsDataArea() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        plot.setBackgroundPaint(Color.RED);
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        r.drawBackground(g2, plot, AREA);
        g2.dispose();
        assertEquals(Color.RED.getRGB(), img.getRGB(50, 50));
        assertEquals(Color.RED.getRGB(), img.getRGB(5, 95));
    }

    // 22
    public void testDrawOutline() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        Rectangle2D area = new Rectangle2D.Double(20.0, 20.0, 60.0, 60.0);

        plot.setOutlineVisible(true);
        plot.setOutlinePaint(Color.BLUE);
        plot.setOutlineStroke(new BasicStroke(4.0f));
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        r.drawOutline(g2, plot, area);
        g2.dispose();
        assertTrue(!isBlank(img));

        plot.setOutlineVisible(false);
        BufferedImage img2 = newImage();
        g2 = img2.createGraphics();
        r.drawOutline(g2, plot, area);
        g2.dispose();
        assertTrue(isBlank(img2));
    }

    // 23
    public void testDrawDomainLine() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        BasicStroke stroke = new BasicStroke(5.0f);

        // vertical orientation: the line is vertical at x = value
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        r.drawDomainLine(g2, plot, AREA, 50.0, Color.RED, stroke);
        g2.dispose();
        assertEquals(Color.RED.getRGB(), img.getRGB(50, 50));
        assertEquals(Color.RED.getRGB(), img.getRGB(50, 10));
        assertEquals(0, img.getRGB(10, 50));

        // horizontal orientation: the line is horizontal at y = value
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        BufferedImage img2 = newImage();
        g2 = img2.createGraphics();
        r.drawDomainLine(g2, plot, AREA, 50.0, Color.RED, stroke);
        g2.dispose();
        assertEquals(Color.RED.getRGB(), img2.getRGB(10, 50));
        assertEquals(0, img2.getRGB(50, 10));

        g2 = newImage().createGraphics();
        try {
            r.drawDomainLine(g2, plot, AREA, 50.0, null, stroke);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            r.drawDomainLine(g2, plot, AREA, 50.0, Color.RED, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        g2.dispose();
    }

    // 24
    public void testDrawRangeMarkerWithValueMarker() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());

        ValueMarker inside = new ValueMarker(50.0, Color.RED,
                new BasicStroke(5.0f));
        inside.setAlpha(1.0f);
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        java.awt.Composite before = g2.getComposite();
        r.drawRangeMarker(g2, plot, plot.getRangeAxis(), inside, AREA);
        assertSame(before, g2.getComposite());
        g2.dispose();
        assertEquals(Color.RED.getRGB(), img.getRGB(10, 50));
        assertEquals(Color.RED.getRGB(), img.getRGB(90, 50));
        assertEquals(0, img.getRGB(10, 10));

        // a value outside the axis range draws nothing
        ValueMarker outside = new ValueMarker(500.0, Color.RED,
                new BasicStroke(5.0f));
        BufferedImage img2 = newImage();
        g2 = img2.createGraphics();
        r.drawRangeMarker(g2, plot, plot.getRangeAxis(), outside, AREA);
        g2.dispose();
        assertTrue(isBlank(img2));
    }

    // 25
    public void testDrawRangeMarkerWithIntervalMarker() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        IntervalMarker marker = new IntervalMarker(20.0, 40.0);
        marker.setPaint(Color.BLUE);
        marker.setAlpha(1.0f);
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        r.drawRangeMarker(g2, plot, plot.getRangeAxis(), marker, AREA);
        g2.dispose();
        // values 20..40 map to y = 80..60 on a 0..100 axis
        assertEquals(Color.BLUE.getRGB(), img.getRGB(50, 70));
        assertEquals(0, img.getRGB(50, 10));
        assertEquals(0, img.getRGB(50, 95));

        // an interval outside the axis range draws nothing
        IntervalMarker outside = new IntervalMarker(200.0, 300.0);
        BufferedImage img2 = newImage();
        g2 = img2.createGraphics();
        r.drawRangeMarker(g2, plot, plot.getRangeAxis(), outside, AREA);
        g2.dispose();
        assertTrue(isBlank(img2));
    }

    // 26
    public void testDrawDomainMarker() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        CategoryAxis axis = plot.getDomainAxis();

        CategoryMarker marker = new CategoryMarker("C1");
        marker.setPaint(Color.GREEN);
        marker.setAlpha(1.0f);
        BufferedImage img = newImage();
        Graphics2D g2 = img.createGraphics();
        r.drawDomainMarker(g2, plot, axis, marker, AREA);
        g2.dispose();
        int mid = (int) axis.getCategoryMiddle(0, 2, AREA,
                RectangleEdge.BOTTOM);
        assertEquals(Color.GREEN.getRGB(), img.getRGB(mid, 50));
        assertEquals(0, img.getRGB(95, 50));

        // a category that is not in the dataset draws nothing
        CategoryMarker unknown = new CategoryMarker("does not exist");
        BufferedImage img2 = newImage();
        g2 = img2.createGraphics();
        r.drawDomainMarker(g2, plot, axis, unknown, AREA);
        g2.dispose();
        assertTrue(isBlank(img2));
    }

    // 27
    public void testAddEntity() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        StandardEntityCollection entities = new StandardEntityCollection();
        Rectangle2D hotspot = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        r.addEntity(entities, hotspot, d, 0, 1, false);
        assertEquals(1, entities.getEntityCount());
        ChartEntity e = entities.getEntity(0);
        assertTrue(e instanceof CategoryItemEntity);
        CategoryItemEntity ce = (CategoryItemEntity) e;
        assertEquals("S1", ce.getRowKey());
        assertEquals("C2", ce.getColumnKey());
        assertSame(hotspot, ce.getArea());
        assertNull(ce.getToolTipText());
        assertNull(ce.getURLText());

        r.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        r.setBaseURLGenerator(new StandardCategoryURLGenerator());
        r.addEntity(entities, hotspot, d, 1, 0, false);
        assertEquals(2, entities.getEntityCount());
        CategoryItemEntity ce2 = (CategoryItemEntity) entities.getEntity(1);
        assertEquals("S2", ce2.getRowKey());
        assertEquals("C1", ce2.getColumnKey());
        assertNotNull(ce2.getToolTipText());
        assertTrue(ce2.getToolTipText().indexOf("S2") >= 0);
        assertNotNull(ce2.getURLText());
        assertTrue(ce2.getURLText().indexOf("S2") >= 0);
    }

    // 28
    public void testAddEntityNullHotspotThrows() {
        AreaRenderer r = new AreaRenderer();
        StandardEntityCollection entities = new StandardEntityCollection();
        try {
            r.addEntity(entities, null, createDataset(), 0, 0, false);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, entities.getEntityCount());
    }

    // 29
    public void testDrawAnnotationsByLayer() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        r.addAnnotation(new CategoryLineAnnotation("C1", 10.0, "C2", 90.0,
                Color.RED, new BasicStroke(3.0f)), Layer.FOREGROUND);

        BufferedImage background = newImage();
        Graphics2D g2 = background.createGraphics();
        r.drawAnnotations(g2, AREA, plot.getDomainAxis(), plot.getRangeAxis(),
                Layer.BACKGROUND, null);
        g2.dispose();
        assertTrue(isBlank(background));

        BufferedImage foreground = newImage();
        g2 = foreground.createGraphics();
        r.drawAnnotations(g2, AREA, plot.getDomainAxis(), plot.getRangeAxis(),
                Layer.FOREGROUND, null);
        g2.dispose();
        assertTrue(!isBlank(foreground));
    }

    // 30
    public void testCreateStateVisibleSeries() {
        AreaRenderer r = new AreaRenderer();
        // no dataset has been seen yet, so there are no rows
        CategoryItemRendererState empty = r.createState(null);
        assertNotNull(empty);
        assertEquals(0, empty.getVisibleSeriesArray().length);

        r.setSeriesVisible(1, Boolean.FALSE);
        CategoryItemRendererState state = r.initialise(null, AREA,
                new CategoryPlot(), createDataset(), null);
        int[] visible = state.getVisibleSeriesArray();
        assertEquals(1, visible.length);
        assertEquals(0, visible[0]);
    }
}
```