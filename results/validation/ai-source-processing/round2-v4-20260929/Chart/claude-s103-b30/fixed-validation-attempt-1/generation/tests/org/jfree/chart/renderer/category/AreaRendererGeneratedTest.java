package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
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
 * Regression tests for the behaviour that AreaRenderer inherits from
 * AbstractCategoryItemRenderer (JUnit 3 style, Java 1.4 compatible).
 */
public class AreaRendererGeneratedTest extends TestCase {

    static class CountingListener implements RendererChangeListener {
        int count = 0;
        public void rendererChanged(RendererChangeEvent event) {
            this.count++;
        }
    }

    private DefaultCategoryDataset dataset;
    private AreaRenderer renderer;
    private CategoryPlot plot;
    private BufferedImage image;
    private Graphics2D g2;
    private Rectangle2D area;

    public AreaRendererGeneratedTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.dataset = new DefaultCategoryDataset();
        this.dataset.addValue(1.0, "R1", "C1");
        this.dataset.addValue(2.0, "R1", "C2");
        this.dataset.addValue(10.0, "R2", "C1");
        this.dataset.addValue(20.0, "R2", "C2");
        this.renderer = new AreaRenderer();
        this.plot = new CategoryPlot(this.dataset, new CategoryAxis("Category"),
                new NumberAxis("Value"), this.renderer);
        this.image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        this.g2 = this.image.createGraphics();
        this.area = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
    }

    protected void tearDown() throws Exception {
        this.g2.dispose();
        super.tearDown();
    }

    private boolean painted(int x, int y) {
        return (this.image.getRGB(x, y) >>> 24) != 0;
    }

    // ---------------------------------------------------------------
    // pass count / plot
    // ---------------------------------------------------------------

    public void testPassCountAndPlotAccessors() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(1, r.getPassCount());
        assertNull(r.getPlot());
        r.setPlot(this.plot);
        assertSame(this.plot, r.getPlot());
    }

    public void testSetPlotNullThrows() {
        AreaRenderer r = new AreaRenderer();
        try {
            r.setPlot(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(r.getPlot());
    }

    // ---------------------------------------------------------------
    // item label generators
    // ---------------------------------------------------------------

    public void testItemLabelGeneratorPrecedenceAndNotify() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseItemLabelGenerator());
        assertNull(r.getSeriesItemLabelGenerator(0));
        assertNull(r.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator base = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(base);
        assertEquals(1, listener.count);
        assertSame(base, r.getBaseItemLabelGenerator());
        assertSame(base, r.getItemLabelGenerator(0, 0, false));

        r.setBaseItemLabelGenerator(base, false);
        assertEquals(1, listener.count);

        CategoryItemLabelGenerator series
                = new StandardCategoryItemLabelGenerator();
        r.setSeriesItemLabelGenerator(1, series);
        assertEquals(2, listener.count);
        assertSame(series, r.getSeriesItemLabelGenerator(1));
        assertSame(series, r.getItemLabelGenerator(1, 0, false));
        assertSame(base, r.getItemLabelGenerator(0, 0, false));
        assertNull(r.getSeriesItemLabelGenerator(0));

        r.setSeriesItemLabelGenerator(0, series, false);
        assertEquals(2, listener.count);
        assertSame(series, r.getSeriesItemLabelGenerator(0));
    }

    // ---------------------------------------------------------------
    // tool tip generators
    // ---------------------------------------------------------------

    public void testToolTipGeneratorPrecedenceAndNotify() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseToolTipGenerator());
        assertNull(r.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator base = new StandardCategoryToolTipGenerator();
        r.setBaseToolTipGenerator(base);
        assertEquals(1, listener.count);
        assertSame(base, r.getBaseToolTipGenerator());
        assertSame(base, r.getToolTipGenerator(0, 0, false));

        r.setBaseToolTipGenerator(base, false);
        assertEquals(1, listener.count);

        CategoryToolTipGenerator series = new StandardCategoryToolTipGenerator();
        r.setSeriesToolTipGenerator(1, series);
        assertEquals(2, listener.count);
        assertSame(series, r.getSeriesToolTipGenerator(1));
        assertSame(series, r.getToolTipGenerator(1, 0, false));
        assertSame(base, r.getToolTipGenerator(0, 0, false));
        assertNull(r.getSeriesToolTipGenerator(0));

        r.setSeriesToolTipGenerator(0, series, false);
        assertEquals(2, listener.count);
    }

    // ---------------------------------------------------------------
    // URL generators
    // ---------------------------------------------------------------

    public void testURLGeneratorPrecedenceAndNotify() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getBaseURLGenerator());
        assertNull(r.getURLGenerator(0, 0, false));

        CategoryURLGenerator base = new StandardCategoryURLGenerator();
        r.setBaseURLGenerator(base);
        assertEquals(1, listener.count);
        assertSame(base, r.getBaseURLGenerator());
        assertSame(base, r.getURLGenerator(0, 0, false));

        r.setBaseURLGenerator(base, false);
        assertEquals(1, listener.count);

        CategoryURLGenerator series = new StandardCategoryURLGenerator();
        r.setSeriesURLGenerator(1, series);
        assertEquals(2, listener.count);
        assertSame(series, r.getSeriesURLGenerator(1));
        assertSame(series, r.getURLGenerator(1, 0, false));
        assertSame(base, r.getURLGenerator(0, 0, false));
        assertNull(r.getSeriesURLGenerator(0));

        r.setSeriesURLGenerator(0, series, false);
        assertEquals(2, listener.count);
    }

    // ---------------------------------------------------------------
    // legend item generators
    // ---------------------------------------------------------------

    public void testLegendItemLabelGenerator() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNotNull(r.getLegendItemLabelGenerator());
        try {
            r.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, listener.count);

        StandardCategorySeriesLabelGenerator g
                = new StandardCategorySeriesLabelGenerator("Series {0}");
        r.setLegendItemLabelGenerator(g);
        assertSame(g, r.getLegendItemLabelGenerator());
        assertEquals(1, listener.count);
    }

    public void testLegendItemToolTipAndURLGenerators() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);

        assertNull(r.getLegendItemToolTipGenerator());
        assertNull(r.getLegendItemURLGenerator());

        StandardCategorySeriesLabelGenerator tip
                = new StandardCategorySeriesLabelGenerator("Tip {0}");
        StandardCategorySeriesLabelGenerator url
                = new StandardCategorySeriesLabelGenerator("URL {0}");
        r.setLegendItemToolTipGenerator(tip);
        assertSame(tip, r.getLegendItemToolTipGenerator());
        assertEquals(1, listener.count);
        r.setLegendItemURLGenerator(url);
        assertSame(url, r.getLegendItemURLGenerator());
        assertEquals(2, listener.count);

        r.setLegendItemToolTipGenerator(null);
        r.setLegendItemURLGenerator(null);
        assertNull(r.getLegendItemToolTipGenerator());
        assertNull(r.getLegendItemURLGenerator());
        assertEquals(4, listener.count);
    }

    // ---------------------------------------------------------------
    // initialise / counts / state
    // ---------------------------------------------------------------

    public void testInitialiseUpdatesCountsAndPlot() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());

        Object state = r.initialise(null, this.area, this.plot, this.dataset,
                null);
        assertNotNull(state);
        assertSame(this.plot, r.getPlot());
        assertEquals(2, r.getRowCount());
        assertEquals(2, r.getColumnCount());

        state = r.initialise(null, this.area, this.plot, null, null);
        assertNotNull(state);
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
    }

    public void testCreateStateVisibleSeries() {
        this.dataset.addValue(100.0, "R3", "C1");
        this.renderer.initialise(null, this.area, this.plot, this.dataset,
                null);
        this.renderer.setSeriesVisible(1, Boolean.FALSE);
        CategoryItemRendererState state = this.renderer.createState(null);
        assertNotNull(state);
        int[] visible = state.getVisibleSeriesArray();
        assertEquals(2, visible.length);
        assertEquals(0, visible[0]);
        assertEquals(2, visible[1]);
    }

    // ---------------------------------------------------------------
    // range bounds
    // ---------------------------------------------------------------

    public void testFindRangeBoundsBasicAndNull() {
        AreaRenderer r = new AreaRenderer();
        r.setDataBoundsIncludesVisibleSeriesOnly(false);
        assertNull(r.findRangeBounds((CategoryDataset) null));
        assertNull(r.findRangeBounds((CategoryDataset) null, true));
        assertNull(r.findRangeBounds(new DefaultCategoryDataset()));
        assertEquals(new Range(1.0, 20.0), r.findRangeBounds(this.dataset));
        assertEquals(new Range(1.0, 20.0),
                r.findRangeBounds(this.dataset, true));
    }

    public void testFindRangeBoundsVisibleSeriesOnly() {
        AreaRenderer r = new AreaRenderer();
        r.setDataBoundsIncludesVisibleSeriesOnly(true);
        r.setSeriesVisible(1, Boolean.FALSE);
        assertEquals(new Range(1.0, 2.0), r.findRangeBounds(this.dataset));

        r.setDataBoundsIncludesVisibleSeriesOnly(false);
        assertEquals(new Range(1.0, 20.0), r.findRangeBounds(this.dataset));
    }

    // ---------------------------------------------------------------
    // item middle
    // ---------------------------------------------------------------

    public void testGetItemMiddle() {
        CategoryAxis axis = new CategoryAxis("Category");
        Rectangle2D a = new Rectangle2D.Double(0.0, 0.0, 200.0, 100.0);
        List keys = this.dataset.getColumnKeys();
        double m1 = this.renderer.getItemMiddle("R1", "C1", this.dataset, axis,
                a, RectangleEdge.BOTTOM);
        double m2 = this.renderer.getItemMiddle("R1", "C2", this.dataset, axis,
                a, RectangleEdge.BOTTOM);
        assertEquals(axis.getCategoryMiddle("C1", keys, a,
                RectangleEdge.BOTTOM), m1, 0.0);
        assertEquals(axis.getCategoryMiddle("C2", keys, a,
                RectangleEdge.BOTTOM), m2, 0.0);
        assertTrue(m1 > 0.0 && m1 < 200.0);
        assertTrue(m2 > m1 && m2 < 200.0);
    }

    // ---------------------------------------------------------------
    // annotations
    // ---------------------------------------------------------------

    public void testAddAndRemoveAnnotations() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);
        CategoryTextAnnotation a = new CategoryTextAnnotation("x", "C1", 1.0);

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
        assertEquals(0, listener.count);

        r.addAnnotation(a);
        assertEquals(1, listener.count);
        assertFalse(r.equals(new AreaRenderer()));

        r.addAnnotation(a, Layer.BACKGROUND);
        assertEquals(2, listener.count);

        r.removeAnnotations();
        assertEquals(3, listener.count);
        assertTrue(r.equals(new AreaRenderer()));
    }

    public void testRemoveAnnotationReturnValue() {
        AreaRenderer r = new AreaRenderer();
        CountingListener listener = new CountingListener();
        r.addChangeListener(listener);
        CategoryTextAnnotation a = new CategoryTextAnnotation("x", "C1", 1.0);

        // present in both layers: both removals succeed
        r.addAnnotation(a, Layer.FOREGROUND);
        r.addAnnotation(a, Layer.BACKGROUND);
        int before = listener.count;
        assertTrue(r.removeAnnotation(a));
        assertEquals(before + 1, listener.count);
        assertTrue(r.equals(new AreaRenderer()));

        // present in the foreground only: the implementation combines the
        // two removal results with '&', so the result is false
        r.addAnnotation(a);
        assertFalse(r.removeAnnotation(a));
        assertTrue(r.equals(new AreaRenderer()));
    }

    public void testDrawAnnotationsLayers() {
        this.renderer.drawAnnotations(this.g2, this.area,
                this.plot.getDomainAxis(), this.plot.getRangeAxis(),
                Layer.FOREGROUND, null);
        this.renderer.drawAnnotations(this.g2, this.area,
                this.plot.getDomainAxis(), this.plot.getRangeAxis(),
                Layer.BACKGROUND, null);
        try {
            this.renderer.drawAnnotations(this.g2, this.area,
                    this.plot.getDomainAxis(), this.plot.getRangeAxis(),
                    null, null);
            fail("Expected NullPointerException");
        }
        catch (NullPointerException e) {
            // expected
        }
    }

    // ---------------------------------------------------------------
    // drawing
    // ---------------------------------------------------------------

    public void testDrawBackgroundAndOutline() {
        this.plot.setBackgroundPaint(Color.red);
        this.renderer.drawBackground(this.g2, this.plot, this.area);
        assertTrue(painted(50, 50));
        assertEquals(0x00FF0000, this.image.getRGB(50, 50) & 0x00FF0000);

        // should simply delegate to the plot without failing
        this.renderer.drawOutline(this.g2, this.plot, this.area);
    }

    public void testDrawDomainLineVertical() {
        this.renderer.drawDomainLine(this.g2, this.plot, this.area, 50.0,
                Color.black, new BasicStroke(4.0f));
        assertTrue(painted(50, 50));
        assertTrue(painted(50, 5));
        assertFalse(painted(10, 50));
    }

    public void testDrawDomainLineHorizontal() {
        this.plot.setOrientation(PlotOrientation.HORIZONTAL);
        this.renderer.drawDomainLine(this.g2, this.plot, this.area, 50.0,
                Color.black, new BasicStroke(4.0f));
        assertTrue(painted(50, 50));
        assertTrue(painted(5, 50));
        assertFalse(painted(50, 10));
    }

    public void testDrawDomainLineNullArguments() {
        try {
            this.renderer.drawDomainLine(this.g2, this.plot, this.area, 50.0,
                    null, new BasicStroke(1.0f));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        try {
            this.renderer.drawDomainLine(this.g2, this.plot, this.area, 50.0,
                    Color.black, (Stroke) null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testDrawRangeMarkerValueMarker() {
        NumberAxis axis = (NumberAxis) this.plot.getRangeAxis();
        axis.setRange(0.0, 10.0);

        // outside the axis range: nothing is drawn
        ValueMarker outside = new ValueMarker(50.0);
        outside.setPaint(Color.red);
        outside.setStroke(new BasicStroke(4.0f));
        this.renderer.drawRangeMarker(this.g2, this.plot, axis, outside,
                this.area);
        assertEquals(0, this.image.getRGB(50, 50));

        // inside the range: a horizontal line through the middle
        ValueMarker inside = new ValueMarker(5.0);
        inside.setPaint(Color.red);
        inside.setStroke(new BasicStroke(4.0f));
        inside.setAlpha(1.0f);
        this.renderer.drawRangeMarker(this.g2, this.plot, axis, inside,
                this.area);
        assertTrue(painted(50, 50));
        assertFalse(painted(50, 10));
    }

    public void testDrawDomainMarker() {
        CategoryAxis axis = this.plot.getDomainAxis();

        // unknown category: nothing is drawn
        CategoryMarker missing = new CategoryMarker("missing");
        this.renderer.drawDomainMarker(this.g2, this.plot, axis, missing,
                this.area);
        assertEquals(0, this.image.getRGB(20, 50));

        // known category: the first of two categories is filled
        CategoryMarker marker = new CategoryMarker("C1");
        marker.setPaint(Color.blue);
        marker.setAlpha(1.0f);
        this.renderer.drawDomainMarker(this.g2, this.plot, axis, marker,
                this.area);
        assertTrue(painted(20, 50));
        assertFalse(painted(80, 50));
    }

    // ---------------------------------------------------------------
    // axes
    // ---------------------------------------------------------------

    public void testGetDomainAxisAndRangeAxis() {
        assertSame(this.plot.getDomainAxis(),
                this.renderer.getDomainAxis(this.plot, this.dataset));
        assertSame(this.plot.getRangeAxis(),
                this.renderer.getRangeAxis(this.plot, 0));
        // an index with no axis falls back to the primary range axis
        assertSame(this.plot.getRangeAxis(),
                this.renderer.getRangeAxis(this.plot, 5));
    }

    // ---------------------------------------------------------------
    // entities
    // ---------------------------------------------------------------

    public void testAddEntity() {
        StandardEntityCollection entities = new StandardEntityCollection();
        try {
            this.renderer.addEntity(entities, null, this.dataset, 0, 0, false);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(0, entities.getEntityCount());

        this.renderer.setBaseToolTipGenerator(
                new StandardCategoryToolTipGenerator());
        this.renderer.setBaseURLGenerator(new StandardCategoryURLGenerator());
        this.renderer.addEntity(entities, new Rectangle2D.Double(0, 0, 10, 10),
                this.dataset, 0, 1, false);
        assertEquals(1, entities.getEntityCount());
        CategoryItemEntity entity = (CategoryItemEntity) entities.getEntity(0);
        assertEquals("R1", entity.getRowKey());
        assertEquals("C2", entity.getColumnKey());
        assertNotNull(entity.getToolTipText());
        assertNotNull(entity.getURLText());
    }

    // ---------------------------------------------------------------
    // legend items
    // ---------------------------------------------------------------

    public void testGetLegendItem() {
        // no plot
        assertNull(new AreaRenderer().getLegendItem(0, 0));

        this.renderer.setLegendItemLabelGenerator(
                new StandardCategorySeriesLabelGenerator("Series {0}"));
        this.renderer.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("Tip {0}"));
        this.renderer.setLegendItemURLGenerator(
                new StandardCategorySeriesLabelGenerator("URL {0}"));
        LegendItem item = this.renderer.getLegendItem(0, 0);
        assertNotNull(item);
        assertEquals("Series R1", item.getLabel());
        assertEquals("Series R1", item.getDescription());
        assertEquals("Tip R1", item.getToolTipText());
        assertEquals("URL R1", item.getURLText());
        assertEquals(0, item.getSeriesIndex());
        assertEquals(0, item.getDatasetIndex());
        assertSame(this.dataset, item.getDataset());

        this.renderer.setSeriesVisible(0, Boolean.FALSE);
        assertNull(this.renderer.getLegendItem(0, 0));
        this.renderer.setSeriesVisibleInLegend(1, Boolean.FALSE);
        assertNull(this.renderer.getLegendItem(0, 1));
    }

    public void testGetLegendItemsOrder() {
        assertEquals(0, new AreaRenderer().getLegendItems().getItemCount());

        LegendItemCollection items = this.renderer.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("R1", items.get(0).getLabel());
        assertEquals("R2", items.get(1).getLabel());

        this.plot.setRowRenderingOrder(SortOrder.DESCENDING);
        items = this.renderer.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("R2", items.get(0).getLabel());
        assertEquals("R1", items.get(1).getLabel());

        this.renderer.setSeriesVisibleInLegend(0, Boolean.FALSE);
        items = this.renderer.getLegendItems();
        assertEquals(1, items.getItemCount());
        assertEquals("R2", items.get(0).getLabel());
    }

    public void testGetDrawingSupplier() {
        assertNull(new AreaRenderer().getDrawingSupplier());
        assertNotNull(this.plot.getDrawingSupplier());
        assertSame(this.plot.getDrawingSupplier(),
                this.renderer.getDrawingSupplier());
    }

    // ---------------------------------------------------------------
    // equals / hashCode / clone
    // ---------------------------------------------------------------

    public void testEquals() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r1));
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));
        assertFalse(r1.equals(null));
        assertFalse(r1.equals("not a renderer"));

        r1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
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
                new StandardCategorySeriesLabelGenerator("Tip {0}"));
        assertFalse(r1.equals(r2));
        r2.setLegendItemToolTipGenerator(
                new StandardCategorySeriesLabelGenerator("Tip {0}"));
        assertTrue(r1.equals(r2));
    }

    public void testHashCode() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r2));
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(r1.hashCode(), r1.hashCode());
    }

    public void testCloneIsIndependentCopy() throws CloneNotSupportedException {
        AreaRenderer r = new AreaRenderer();
        r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        r.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        r.setBaseURLGenerator(new StandardCategoryURLGenerator());

        AreaRenderer c = (AreaRenderer) r.clone();
        assertNotSame(r, c);
        assertSame(r.getClass(), c.getClass());
        assertTrue(r.equals(c));
        assertNotSame(r.getBaseItemLabelGenerator(),
                c.getBaseItemLabelGenerator());
        assertNotSame(r.getBaseToolTipGenerator(),
                c.getBaseToolTipGenerator());
        assertNotSame(r.getBaseURLGenerator(), c.getBaseURLGenerator());

        c.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator());
        assertNull(r.getSeriesItemLabelGenerator(0));
        assertFalse(r.equals(c));
    }

    public void testCloneWithNonCloneableGeneratorThrows() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseToolTipGenerator(new CategoryToolTipGenerator() {
            public String generateToolTip(CategoryDataset d, int row,
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
}
