package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
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
 * Deterministic regression tests for behaviour that AreaRenderer inherits
 * from AbstractCategoryItemRenderer (JUnit 3 style, Java 1.4 compatible).
 */
public class AreaRendererInheritedBehaviourTest extends TestCase {

    private static final double EPSILON = 1.0e-9;

    /** Counts renderer change events. */
    static class CountingListener implements RendererChangeListener {
        int count = 0;
        public void rendererChanged(RendererChangeEvent event) {
            this.count++;
        }
    }

    /** Annotation that records how often it is drawn. */
    static class CountingAnnotation extends CategoryTextAnnotation {
        int drawCount = 0;
        CountingAnnotation() {
            super("A", "C1", 1.0);
        }
        public void draw(Graphics2D g2, CategoryPlot plot,
                Rectangle2D dataArea, CategoryAxis domainAxis,
                ValueAxis rangeAxis, int rendererIndex,
                PlotRenderingInfo info) {
            this.drawCount++;
        }
    }

    /** A label generator whose clone() always fails. */
    static class ThrowingLabelGenerator
            extends StandardCategoryItemLabelGenerator {
        public Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException("not cloneable");
        }
    }

    // ---------------------------------------------------------------- helpers

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "S1", "C1");
        d.addValue(5.0, "S1", "C2");
        d.addValue(3.0, "S2", "C1");
        d.addValue(-2.0, "S2", "C2");
        return d;
    }

    private CategoryPlot createPlot(AreaRenderer r, CategoryDataset d) {
        return new CategoryPlot(d, new CategoryAxis("Category"),
                new NumberAxis("Value"), r);
    }

    private BufferedImage createImage() {
        return new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    }

    // ------------------------------------------------------------------ tests

    public void testPassCountAndInitialState() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(1, r.getPassCount());
        assertNull(r.getPlot());
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
    }

    public void testSetPlot() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        r.setPlot(plot);
        assertSame(plot, r.getPlot());
        try {
            r.setPlot(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertSame(plot, r.getPlot());
    }

    public void testInitialise() {
        AreaRenderer r = new AreaRenderer();
        CategoryDataset d = createDataset();
        CategoryPlot plot = createPlot(r, d);
        CategoryItemRendererState state = r.initialise(null,
                new Rectangle2D.Double(0, 0, 100, 100), plot, d, null);
        assertNotNull(state);
        assertSame(plot, r.getPlot());
        assertEquals(2, r.getRowCount());
        assertEquals(2, r.getColumnCount());
    }

    public void testInitialiseNullDataset() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        CategoryItemRendererState state = r.initialise(null,
                new Rectangle2D.Double(0, 0, 100, 100), plot, null, null);
        assertNotNull(state);
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
        assertSame(plot, r.getPlot());
    }

    public void testCreateState() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        d.addValue(7.0, "S3", "C1");
        CategoryPlot plot = createPlot(r, d);
        r.setSeriesVisible(1, Boolean.FALSE);
        CategoryItemRendererState state = r.initialise(null,
                new Rectangle2D.Double(0, 0, 100, 100), plot, d, null);
        int[] visible = state.getVisibleSeriesArray();
        assertEquals(2, visible.length);
        assertEquals(0, visible[0]);
        assertEquals(2, visible[1]);
        // createState() directly reflects the same row count
        CategoryItemRendererState state2 = r.createState(null);
        assertEquals(2, state2.getVisibleSeriesArray().length);
    }

    public void testFindRangeBounds() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        Range range = r.findRangeBounds(d);
        assertNotNull(range);
        assertEquals(-2.0, range.getLowerBound(), EPSILON);
        assertEquals(5.0, range.getUpperBound(), EPSILON);
        Range range2 = r.findRangeBounds(d, false);
        assertEquals(range, range2);
    }

    public void testFindRangeBoundsNullAndEmpty() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.findRangeBounds(null));
        assertNull(r.findRangeBounds(null, true));
        assertNull(r.findRangeBounds(new DefaultCategoryDataset()));
    }

    public void testFindRangeBoundsVisibleSeriesOnly() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        r.setSeriesVisible(1, Boolean.FALSE);

        r.setDataBoundsIncludesVisibleSeriesOnly(true);
        Range visibleOnly = r.findRangeBounds(d);
        assertEquals(1.0, visibleOnly.getLowerBound(), EPSILON);
        assertEquals(5.0, visibleOnly.getUpperBound(), EPSILON);

        r.setDataBoundsIncludesVisibleSeriesOnly(false);
        Range all = r.findRangeBounds(d);
        assertEquals(-2.0, all.getLowerBound(), EPSILON);
        assertEquals(5.0, all.getUpperBound(), EPSILON);
    }

    public void testGetItemMiddle() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryAxis axis = new CategoryAxis("Category");
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        double m1 = r.getItemMiddle("S1", "C1", d, axis, area, edge);
        double m2 = r.getItemMiddle("S1", "C2", d, axis, area, edge);
        assertEquals(axis.getCategoryMiddle("C1", d.getColumnKeys(), area,
                edge), m1, EPSILON);
        assertEquals(axis.getCategoryMiddle("C2", d.getColumnKeys(), area,
                edge), m2, EPSILON);
        assertTrue(m1 < m2);
    }

    public void testGetDomainAxisAndRangeAxis() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryAxis domain = new CategoryAxis("Category");
        NumberAxis range = new NumberAxis("Value");
        CategoryPlot plot = new CategoryPlot(d, domain, range, r);

        assertSame(domain, r.getDomainAxis(plot, d));
        assertSame(range, r.getRangeAxis(plot, 0));
        // index with no axis falls back to the primary range axis
        assertSame(range, r.getRangeAxis(plot, 1));
    }

    public void testItemLabelGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseItemLabelGenerator());
        assertNull(r.getSeriesItemLabelGenerator(0));
        assertNull(r.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator base = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(base);
        assertSame(base, r.getBaseItemLabelGenerator());
        assertSame(base, r.getItemLabelGenerator(0, 0, false));
        assertEquals(1, listener.count);

        CategoryItemLabelGenerator series
                = new StandardCategoryItemLabelGenerator();
        r.setSeriesItemLabelGenerator(0, series);
        assertSame(series, r.getSeriesItemLabelGenerator(0));
        assertSame(series, r.getItemLabelGenerator(0, 1, false));
        assertSame(base, r.getItemLabelGenerator(1, 0, false));
        assertEquals(2, listener.count);

        // notify == false suppresses events
        r.setBaseItemLabelGenerator(null, false);
        r.setSeriesItemLabelGenerator(0, null, false);
        assertEquals(2, listener.count);
        assertNull(r.getItemLabelGenerator(0, 0, false));
    }

    public void testToolTipGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseToolTipGenerator());
        assertNull(r.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator base = new StandardCategoryToolTipGenerator();
        r.setBaseToolTipGenerator(base);
        assertSame(base, r.getBaseToolTipGenerator());
        assertSame(base, r.getToolTipGenerator(0, 0, false));
        assertEquals(1, listener.count);

        CategoryToolTipGenerator series = new StandardCategoryToolTipGenerator();
        r.setSeriesToolTipGenerator(1, series);
        assertSame(series, r.getSeriesToolTipGenerator(1));
        assertNull(r.getSeriesToolTipGenerator(0));
        assertSame(series, r.getToolTipGenerator(1, 0, false));
        assertSame(base, r.getToolTipGenerator(0, 0, false));
        assertEquals(2, listener.count);

        r.setBaseToolTipGenerator(null, false);
        r.setSeriesToolTipGenerator(1, null, false);
        assertEquals(2, listener.count);
        assertNull(r.getToolTipGenerator(1, 0, false));
    }

    public void testURLGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseURLGenerator());
        assertNull(r.getURLGenerator(0, 0, false));

        CategoryURLGenerator base = new StandardCategoryURLGenerator();
        r.setBaseURLGenerator(base);
        assertSame(base, r.getBaseURLGenerator());
        assertSame(base, r.getURLGenerator(0, 0, false));
        assertEquals(1, listener.count);

        CategoryURLGenerator series = new StandardCategoryURLGenerator();
        r.setSeriesURLGenerator(0, series);
        assertSame(series, r.getSeriesURLGenerator(0));
        assertSame(series, r.getURLGenerator(0, 1, false));
        assertSame(base, r.getURLGenerator(1, 1, false));
        assertEquals(2, listener.count);

        r.setBaseURLGenerator(null, false);
        r.setSeriesURLGenerator(0, null, false);
        assertEquals(2, listener.count);
        assertNull(r.getURLGenerator(0, 0, false));
    }

    public void testLegendItemGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNotNull(r.getLegendItemLabelGenerator());
        assertNull(r.getLegendItemToolTipGenerator());
        assertNull(r.getLegendItemURLGenerator());

        StandardCategorySeriesLabelGenerator g
                = new StandardCategorySeriesLabelGenerator("X {0}");
        r.setLegendItemLabelGenerator(g);
        assertSame(g, r.getLegendItemLabelGenerator());
        assertEquals(1, listener.count);

        r.setLegendItemToolTipGenerator(g);
        assertSame(g, r.getLegendItemToolTipGenerator());
        assertEquals(2, listener.count);

        r.setLegendItemURLGenerator(g);
        assertSame(g, r.getLegendItemURLGenerator());
        assertEquals(3, listener.count);

        r.setLegendItemToolTipGenerator(null);
        r.setLegendItemURLGenerator(null);
        assertNull(r.getLegendItemToolTipGenerator());
        assertNull(r.getLegendItemURLGenerator());

        try {
            r.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertSame(g, r.getLegendItemLabelGenerator());
    }

    public void testGetLegendItem() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        createPlot(r, d);
        r.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("Tip {0}"));
        r.setLegendItemURLGenerator(
                new StandardCategorySeriesLabelGenerator("url-{0}"));

        LegendItem item = r.getLegendItem(0, 1);
        assertNotNull(item);
        assertEquals("S2", item.getLabel());
        assertEquals("Tip S2", item.getToolTipText());
        assertEquals("url-S2", item.getURLText());
        assertEquals("S2", item.getSeriesKey());
        assertEquals(1, item.getSeriesIndex());
        assertEquals(0, item.getDatasetIndex());
        assertSame(d, item.getDataset());
    }

    public void testGetLegendItemNotAvailable() {
        AreaRenderer r = new AreaRenderer();
        // no plot assigned
        assertNull(r.getLegendItem(0, 0));

        DefaultCategoryDataset d = createDataset();
        createPlot(r, d);
        assertNotNull(r.getLegendItem(0, 0));

        r.setSeriesVisible(0, Boolean.FALSE);
        assertNull(r.getLegendItem(0, 0));
        assertNotNull(r.getLegendItem(0, 1));

        r.setSeriesVisible(0, Boolean.TRUE);
        r.setSeriesVisibleInLegend(1, Boolean.FALSE);
        assertNotNull(r.getLegendItem(0, 0));
        assertNull(r.getLegendItem(0, 1));
    }

    public void testGetLegendItems() {
        AreaRenderer r = new AreaRenderer();
        // no plot: empty collection
        LegendItemCollection none = r.getLegendItems();
        assertNotNull(none);
        assertEquals(0, none.getItemCount());

        DefaultCategoryDataset d = createDataset();
        CategoryPlot plot = createPlot(r, d);
        LegendItemCollection items = r.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("S1", items.get(0).getLabel());
        assertEquals("S2", items.get(1).getLabel());

        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        items = r.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("S2", items.get(0).getLabel());
        assertEquals("S1", items.get(1).getLabel());

        r.setSeriesVisibleInLegend(1, Boolean.FALSE);
        items = r.getLegendItems();
        assertEquals(1, items.getItemCount());
        assertEquals("S1", items.get(0).getLabel());
    }

    public void testAnnotationsAddAndDraw() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);
        CountingAnnotation fg = new CountingAnnotation();
        CountingAnnotation bg = new CountingAnnotation();

        r.addAnnotation(fg);                         // foreground by default
        r.addAnnotation(bg, Layer.BACKGROUND);
        assertEquals(2, listener.count);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);
        r.drawAnnotations(null, area, null, null, Layer.FOREGROUND, null);
        assertEquals(1, fg.drawCount);
        assertEquals(0, bg.drawCount);

        r.drawAnnotations(null, area, null, null, Layer.BACKGROUND, null);
        assertEquals(1, fg.drawCount);
        assertEquals(1, bg.drawCount);

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
    }

    public void testRemoveAnnotations() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);
        CountingAnnotation a = new CountingAnnotation();
        CountingAnnotation b = new CountingAnnotation();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);

        // an annotation present in both layers is reported as removed
        r.addAnnotation(a, Layer.FOREGROUND);
        r.addAnnotation(a, Layer.BACKGROUND);
        int before = listener.count;
        assertTrue(r.removeAnnotation(a));
        assertEquals(before + 1, listener.count);
        r.drawAnnotations(null, area, null, null, Layer.FOREGROUND, null);
        r.drawAnnotations(null, area, null, null, Layer.BACKGROUND, null);
        assertEquals(0, a.drawCount);

        // removeAnnotations clears both layers and notifies
        r.addAnnotation(a, Layer.FOREGROUND);
        r.addAnnotation(b, Layer.BACKGROUND);
        before = listener.count;
        r.removeAnnotations();
        assertEquals(before + 1, listener.count);
        r.drawAnnotations(null, area, null, null, Layer.FOREGROUND, null);
        r.drawAnnotations(null, area, null, null, Layer.BACKGROUND, null);
        assertEquals(0, a.drawCount);
        assertEquals(0, b.drawCount);
    }

    public void testEqualsAndHashCode() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r1));
        assertTrue(r1.equals(r2));
        assertEquals(r1.hashCode(), r2.hashCode());
        assertFalse(r1.equals(null));
        assertFalse(r1.equals("not a renderer"));

        r1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertTrue(r1.equals(r2));

        r1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        assertTrue(r1.equals(r2));

        r1.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertTrue(r1.equals(r2));

        r1.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());
        assertFalse(r1.equals(r2));
        r2.setSeriesToolTipGenerator(0, new StandardCategoryToolTipGenerator());
        assertTrue(r1.equals(r2));

        r1.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("A {0}"));
        assertFalse(r1.equals(r2));
        r2.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("A {0}"));
        assertTrue(r1.equals(r2));

        r1.addAnnotation(new CountingAnnotation());
        assertFalse(r1.equals(r2));
        r2.addAnnotation(new CountingAnnotation());
        assertTrue(r1.equals(r2));
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    

    public void testCloneRejectsNonCloneableGenerator() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseItemLabelGenerator(new ThrowingLabelGenerator());
        try {
            r.clone();
            fail("Expected CloneNotSupportedException");
        }
        catch (CloneNotSupportedException e) {
            // expected
        }
    }

    public void testGetDrawingSupplier() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getDrawingSupplier());
        CategoryPlot plot = createPlot(r, createDataset());
        assertNotNull(r.getDrawingSupplier());
        assertSame(plot.getDrawingSupplier(), r.getDrawingSupplier());
    }

    public void testAddEntity() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        createPlot(r, d);
        StandardCategoryToolTipGenerator tt
                = new StandardCategoryToolTipGenerator();
        StandardCategoryURLGenerator url = new StandardCategoryURLGenerator();
        r.setBaseToolTipGenerator(tt);
        r.setBaseURLGenerator(url);

        StandardEntityCollection entities = new StandardEntityCollection();
        Rectangle2D hotspot = new Rectangle2D.Double(1, 2, 10, 10);
        r.addEntity(entities, hotspot, d, 0, 1, false);

        assertEquals(1, entities.getEntityCount());
        CategoryItemEntity entity = (CategoryItemEntity) entities.getEntity(0);
        assertSame(hotspot, entity.getArea());
        assertEquals(tt.generateToolTip(d, 0, 1), entity.getToolTipText());
        assertEquals(url.generateURL(d, 0, 1), entity.getURLText());
        assertEquals("S1", entity.getRowKey());
        assertEquals("C2", entity.getColumnKey());
    }

    public void testAddEntityNullHotspotAndDisabledEntities() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        createPlot(r, d);
        StandardEntityCollection entities = new StandardEntityCollection();

        try {
            r.addEntity(entities, null, d, 0, 0, false);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, entities.getEntityCount());

        // entity creation can be disabled per series
        r.setSeriesCreateEntities(0, Boolean.FALSE);
        r.addEntity(entities, new Rectangle2D.Double(0, 0, 5, 5), d, 0, 0,
                false);
        assertEquals(0, entities.getEntityCount());
        r.addEntity(entities, new Rectangle2D.Double(0, 0, 5, 5), d, 1, 0,
                false);
        assertEquals(1, entities.getEntityCount());
    }

    public void testDrawBackground() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        plot.setBackgroundPaint(Color.RED);
        BufferedImage image = createImage();
        Graphics2D g2 = image.createGraphics();
        r.drawBackground(g2, plot, new Rectangle2D.Double(10, 10, 80, 80));
        g2.dispose();
        assertEquals(Color.RED.getRGB(), image.getRGB(50, 50));
        assertEquals(0, image.getRGB(2, 2));
    }

    public void testDrawDomainLine() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        BufferedImage image = createImage();
        Graphics2D g2 = image.createGraphics();
        // default plot orientation is vertical: a vertical line at x = 50
        r.drawDomainLine(g2, plot, new Rectangle2D.Double(0, 0, 100, 100),
                50.0, Color.BLUE, new BasicStroke(4f));
        g2.dispose();
        assertEquals(Color.BLUE.getRGB(), image.getRGB(50, 50));
        assertEquals(0, image.getRGB(10, 50));
    }

    public void testDrawDomainLineNullArguments() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        BufferedImage image = createImage();
        Graphics2D g2 = image.createGraphics();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);
        try {
            r.drawDomainLine(g2, plot, area, 50.0, null, new BasicStroke(1f));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            r.drawDomainLine(g2, plot, area, 50.0, Color.BLUE, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        g2.dispose();
    }

    public void testDrawRangeMarker() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        ValueAxis axis = plot.getRangeAxis();
        axis.setRange(0.0, 10.0);
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);

        // marker inside the axis range is drawn as a horizontal line
        ValueMarker inside = new ValueMarker(5.0);
        inside.setPaint(Color.GREEN);
        inside.setStroke(new BasicStroke(4f));
        inside.setAlpha(1.0f);
        BufferedImage image = createImage();
        Graphics2D g2 = image.createGraphics();
        r.drawRangeMarker(g2, plot, axis, inside, area);
        g2.dispose();
        assertEquals(Color.GREEN.getRGB(), image.getRGB(10, 50));
        assertEquals(0, image.getRGB(10, 10));

        // marker outside the axis range is skipped
        ValueMarker outside = new ValueMarker(20.0);
        outside.setPaint(Color.GREEN);
        outside.setStroke(new BasicStroke(4f));
        outside.setAlpha(1.0f);
        BufferedImage image2 = createImage();
        Graphics2D g22 = image2.createGraphics();
        r.drawRangeMarker(g22, plot, axis, outside, area);
        g22.dispose();
        for (int y = 0; y < 100; y += 10) {
            assertEquals(0, image2.getRGB(10, y));
        }
    }

    public void testDrawDomainMarker() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = createPlot(r, createDataset());
        CategoryAxis axis = plot.getDomainAxis();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);

        // a marker for an existing category fills that category's band
        CategoryMarker marker = new CategoryMarker("C1", Color.RED,
                new BasicStroke(1f));
        marker.setAlpha(1.0f);
        BufferedImage image = createImage();
        Graphics2D g2 = image.createGraphics();
        r.drawDomainMarker(g2, plot, axis, marker, area);
        g2.dispose();
        assertEquals(Color.RED.getRGB(), image.getRGB(25, 50));
        assertEquals(0, image.getRGB(75, 50));

        // a marker for an unknown category draws nothing
        CategoryMarker missing = new CategoryMarker("Missing", Color.RED,
                new BasicStroke(1f));
        missing.setAlpha(1.0f);
        BufferedImage image2 = createImage();
        Graphics2D g22 = image2.createGraphics();
        r.drawDomainMarker(g22, plot, axis, missing, area);
        g22.dispose();
        assertEquals(0, image2.getRGB(25, 50));
        assertEquals(0, image2.getRGB(75, 50));
    }
}
