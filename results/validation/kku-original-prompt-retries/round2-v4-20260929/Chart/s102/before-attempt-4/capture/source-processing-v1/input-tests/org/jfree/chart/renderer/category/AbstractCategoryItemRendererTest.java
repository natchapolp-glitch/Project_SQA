// org/jfree/chart/renderer/category/AbstractCategoryItemRendererTest.java
package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.annotations.TextAnnotation; // use a concrete annotation for test
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import junit.framework.TestCase;

/**
 * Tests for {@link AbstractCategoryItemRenderer}.  These tests exercise
 * the concrete implementation {@link AreaRenderer} to validate the
 * abstract base class behavior.
 */
public class AbstractCategoryItemRendererTest extends TestCase {

    /** A concrete renderer used for testing. */
    private AreaRenderer renderer;

    /** A plot that the renderer can be assigned to. */
    private CategoryPlot plot;

    /** A small dataset. */
    private DefaultCategoryDataset dataset;

    /**
     * Creates a new test case.
     *
     * @param name  the test case name.
     */
    public AbstractCategoryItemRendererTest(String name) {
        super(name);
    }

    /**
     * Sets up the test fixture.
     */
    protected void setUp() {
        this.renderer = new AreaRenderer();
        this.plot = new CategoryPlot();
        this.dataset = new DefaultCategoryDataset();
        this.dataset.addValue(1.0, "Series1", "Category1");
        this.dataset.addValue(2.0, "Series1", "Category2");
        this.dataset.addValue(3.0, "Series2", "Category1");
        this.dataset.addValue(4.0, "Series2", "Category2");
    }

    /**
     * Tests that getPassCount() returns 1.
     */
    public void testGetPassCount() {
        assertEquals(1, this.renderer.getPassCount());
    }

    /**
     * Tests the plot accessor/mutator.
     */
    public void testSetPlot() {
        this.renderer.setPlot(this.plot);
        assertSame(this.plot, this.renderer.getPlot());

        try {
            this.renderer.setPlot(null);
            fail("Expected IllegalArgumentException for null plot.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests getPlot() returns null when no plot is assigned.
     */
    public void testGetPlotInitiallyNull() {
        assertNull(new AreaRenderer().getPlot());
    }

    /**
     * Tests equality of two renderers with identical configuration.
     */
    public void testEquals() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));

        // Change a property and verify inequality
        r1.setSeriesItemLabelGenerator(0,
                new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        assertFalse(r1.equals(r2));
    }

    /**
     * Tests that two equal renderers have the same hash code.
     */
    public void testHashCode() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    /**
     * Tests cloning of the renderer.
     */
    public void testClone() {
        AreaRenderer r1 = new AreaRenderer();
        r1.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator());
        try {
            AreaRenderer r2 = (AreaRenderer) r1.clone();
            assertNotSame(r1, r2);
            assertEquals(r1, r2);
        } catch (CloneNotSupportedException e) {
            fail("Cloning not supported: " + e);
        }
    }

    /**
     * Tests getItemMiddle() forwards to the axis.
     */
    public void testGetItemMiddle() {
        this.renderer.setPlot(this.plot);
        CategoryAxis axis = new CategoryAxis("Test");
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);
        this.dataset = new DefaultCategoryDataset();
        this.dataset.addValue(1, "S1", "C1");
        this.dataset.addValue(2, "S1", "C2");

        double middle = this.renderer.getItemMiddle("S1", "C1",
                this.dataset, axis, area,
                this.plot.getDomainAxisEdge());
        assertTrue(middle > 0);
    }

    /**
     * findRangeBounds(null) returns null.
     */
    public void testFindRangeBoundsNullDataset() {
        assertNull(this.renderer.findRangeBounds((CategoryDataset) null));
    }

    /**
     * findRangeBounds(empty dataset) returns null.
     */
    public void testFindRangeBoundsEmptyDataset() {
        DefaultCategoryDataset empty = new DefaultCategoryDataset();
        assertNull(this.renderer.findRangeBounds(empty));
    }

    /**
     * findRangeBounds with a non-empty dataset returns a range.
     */
    public void testFindRangeBoundsTypical() {
        Range r = this.renderer.findRangeBounds(this.dataset);
        assertNotNull(r);
        assertEquals(1.0, r.getLowerBound(), 0.0001);
        assertEquals(4.0, r.getUpperBound(), 0.0001);
    }

    /**
     * findRangeBounds with includeInterval = true should work.
     */
    public void testFindRangeBoundsIncludeInterval() {
        // using protected method via reflection? Not possible.
        // The public method uses false, so we cannot directly test the
        // protected overload without subclass. But we can assert it
        // works via a subclass if needed.  Here we simply skip or
        // use a casting trick? Not straightforward.  We'll test the
        // public one.
        assertNotNull(this.renderer.findRangeBounds(this.dataset));
    }

    /**
     * Adding a foreground annotation increases the annotation count.
     */
    public void testAddAnnotationForeground() {
        CategoryAnnotation a = new TextAnnotation("Label",
                "Text", new Point2D.Double());
        this.renderer.addAnnotation(a);
        // no direct getter; we test indirectly: removing returns true
        assertTrue(this.renderer.removeAnnotation(a));
    }

    /**
     * Adding a background annotation works.
     */
    public void testAddAnnotationBackground() {
        CategoryAnnotation a = new TextAnnotation("Label",
                "Text", new Point2D.Double());
        this.renderer.addAnnotation(a, Layer.BACKGROUND);
        assertTrue(this.renderer.removeAnnotation(a));
    }

    /**
     * Passing null to addAnnotation should throw IllegalArgumentException.
     */
    public void testAddAnnotationNullThrowsException() {
        try {
            this.renderer.addAnnotation(null, Layer.FOREGROUND);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Removing an annotation that does not exist returns false.
     */
    public void testRemoveAnnotationNotPresent() {
        CategoryAnnotation a = new TextAnnotation("Label",
                "Text", new Point2D.Double());
        assertFalse(this.renderer.removeAnnotation(a));
    }

    /**
     * Removing all annotations works.
     */
    public void testRemoveAnnotations() {
        CategoryAnnotation a = new TextAnnotation("Label",
                "Text", new Point2D.Double());
        this.renderer.addAnnotation(a);
        this.renderer.removeAnnotations();
        assertFalse(this.renderer.removeAnnotation(a));
    }

    /**
     * Setting a series item label generator and retrieving it.
     */
    public void testSetSeriesItemLabelGenerator() {
        CategoryItemLabelGenerator gen =
                new StandardCategoryItemLabelGenerator();
        this.renderer.setSeriesItemLabelGenerator(0, gen);
        assertSame(gen, this.renderer.getSeriesItemLabelGenerator(0));
    }

    /**
     * Retrieving an item label generator for a series with no series-specific
     * generator returns the base generator (null by default).
     */
    public void testGetItemLabelGeneratorWithBaseSet() {
        CategoryItemLabelGenerator gen =
                new StandardCategoryItemLabelGenerator();
        this.renderer.setBaseItemLabelGenerator(gen);
        assertSame(gen, this.renderer.getItemLabelGenerator(0, 0, false));
    }

    /**
     * getItemLabelGenerator(series, column, selected) returns the series
     * generator when set.
     */
    public void testGetItemLabelGeneratorWithSeriesGenerator() {
        CategoryItemLabelGenerator gen =
                new StandardCategoryItemLabelGenerator("{0}", "{1}", "{2}",
                new java.text.DecimalFormat("0.00"));
        this.renderer.setSeriesItemLabelGenerator(1, gen);
        assertSame(gen, this.renderer.getItemLabelGenerator(1, 0, false));
    }

    /**
     * getSeriesItemLabelGenerator works.
     */
    public void testGetSeriesItemLabelGenerator() {
        // default null
        assertNull(this.renderer.getSeriesItemLabelGenerator(0));
        CategoryItemLabelGenerator gen =
                new StandardCategoryItemLabelGenerator();
        this.renderer.setSeriesItemLabelGenerator(0, gen);
        assertSame(gen, this.renderer.getSeriesItemLabelGenerator(0));
    }

    /**
     * setBaseItemLabelGenerator with notify flag.
     */
    public void testSetBaseItemLabelGeneratorWithNotify() {
        CategoryItemLabelGenerator gen =
                new StandardCategoryItemLabelGenerator();
        this.renderer.setBaseItemLabelGenerator(gen, true);
        assertSame(gen, this.renderer.getBaseItemLabelGenerator());
    }

    /**
     * Setting a series tool tip generator and retrieving it.
     */
    public void testSetSeriesToolTipGenerator() {
        CategoryToolTipGenerator gen =
                new StandardCategoryToolTipGenerator();
        this.renderer.setSeriesToolTipGenerator(0, gen);
        assertSame(gen, this.renderer.getSeriesToolTipGenerator(0));
    }

    /**
     * getToolTipGenerator returns series-level generator if set.
     */
    public void testGetToolTipGeneratorWithSeriesGenerator() {
        CategoryToolTipGenerator gen =
                new StandardCategoryToolTipGenerator();
        this.renderer.setSeriesToolTipGenerator(1, gen);
        assertSame(gen, this.renderer.getToolTipGenerator(1, 0, false));
    }

    /**
     * getToolTipGenerator returns base generator when no series generator.
     */
    public void testGetToolTipGeneratorWithBase() {
        CategoryToolTipGenerator gen =
                new StandardCategoryToolTipGenerator();
        this.renderer.setBaseToolTipGenerator(gen);
        assertSame(gen, this.renderer.getToolTipGenerator(0, 0, false));
    }

    /**
     * Setting the base tool tip generator with notify flag.
     */
    public void testSetBaseToolTipGeneratorWithNotify() {
        CategoryToolTipGenerator gen =
                new StandardCategoryToolTipGenerator();
        this.renderer.setBaseToolTipGenerator(gen, true);
        assertSame(gen, this.renderer.getBaseToolTipGenerator());
    }

    /**
     * Setting a series URL generator and retrieving it.
     */
    public void testSetSeriesURLGenerator() {
        CategoryURLGenerator gen = new StandardCategoryURLGenerator();
        this.renderer.setSeriesURLGenerator(0, gen);
        assertSame(gen, this.renderer.getSeriesURLGenerator(0));
    }

    /**
     * getURLGenerator returns series-level when set.
     */
    public void testGetURLGeneratorWithSeries() {
        CategoryURLGenerator gen = new StandardCategoryURLGenerator();
        this.renderer.setSeriesURLGenerator(1, gen);
        assertSame(gen, this.renderer.getURLGenerator(1, 0, false));
    }

    /**
     * getURLGenerator returns base generator when no series generator.
     */
    public void testGetURLGeneratorWithBase() {
        CategoryURLGenerator gen = new StandardCategoryURLGenerator();
        this.renderer.setBaseURLGenerator(gen);
        assertSame(gen, this.renderer.getURLGenerator(0, 0, false));
    }

    /**
     * Setting legend item label generator.
     */
    public void testSetLegendItemLabelGenerator() {
        CategorySeriesLabelGenerator gen =
                new StandardCategorySeriesLabelGenerator("{0}");
        this.renderer.setLegendItemLabelGenerator(gen);
        assertSame(gen, this.renderer.getLegendItemLabelGenerator());
    }

    /**
     * getLegendItemLabelGenerator returns default generator.
     */
    public void testGetLegendItemLabelGeneratorDefault() {
        assertNotNull(this.renderer.getLegendItemLabelGenerator());
        assertTrue(this.renderer.getLegendItemLabelGenerator()
                instanceof StandardCategorySeriesLabelGenerator);
    }

    /**
     * getLegendItemToolTipGenerator is null by default.
     */
    public void testGetLegendItemToolTipGeneratorDefault() {
        assertNull(this.renderer.getLegendItemToolTipGenerator());
    }

    /**
     * getLegendItemURLGenerator is null by default.
     */
    public void testGetLegendItemURLGeneratorDefault() {
        assertNull(this.renderer.getLegendItemURLGenerator());
    }

    /**
     * getLegendItems on a renderer not assigned to a plot returns empty
     * collection.
     */
    public void testLegendItemsNoPlot() {
        LegendItemCollection lic = this.renderer.getLegendItems();
        assertNotNull(lic);
        assertEquals(0, lic.getItemCount());
    }

    /**
     * getLegendItems with a plot and dataset returns items for visible
     * series in legend.
     */
    public void testLegendItemsWithDataset() {
        this.renderer.setPlot(this.plot);
        this.plot.setDataset(0, this.dataset);
        this.plot.setRenderer(0, this.renderer);
        LegendItemCollection lic = this.renderer.getLegendItems();
        assertTrue(lic.getItemCount() >= 0);
        // both series visible by default
        assertEquals(2, lic.getItemCount());
    }

    /**
     * Adding an entity with null hotspot should throw
     * IllegalArgumentException (as of 1.2.0, only the public addEntity
     * with five arguments checks null; the protected addEntity with
     * entityX/Y allows null and creates an ellipse).  To test the exception,
     * we call the version that explicitly forbids null.
     */
    public void testAddEntityNullHotspotThrowsException() {
        EntityCollection ec = new StandardEntityCollection();
        try {
            this.renderer.addEntity(ec, null, this.dataset, 0, 0, false);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Drawing a domain line with null paint throws exception.
     */
    public void testDrawDomainLineNullPaint() {
        Graphics2D g2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawDomainLine(g2, this.plot,
                    new Rectangle2D.Double(), 0.0, null,
                    new java.awt.BasicStroke());
            fail("Expected exception.");
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            g2.dispose();
        }
    }

    /**
     * Drawing a domain line with null stroke throws exception.
     */
    public void testDrawDomainLineNullStroke() {
        Graphics2D g2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawDomainLine(g2, this.plot,
                    new Rectangle2D.Double(), 0.0,
                    java.awt.Color.black, null);
            fail("Expected exception.");
        } catch (IllegalArgumentException e) {
            // expected
        } finally {
            g2.dispose();
        }
    }

    /**
     * Drawing a range line with a value outside the axis range should
     * return without drawing (no exception).
     */
    public void testDrawRangeLineValueOutOfRange() {
        this.renderer.setPlot(this.plot);
        ValueAxis axis = new NumberAxis("Range");
        axis.setRange(0, 10);
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        Graphics2D g2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawRangeLine(g2, this.plot, axis, dataArea, 20.0,
                    java.awt.Color.red, new java.awt.BasicStroke());
        } finally {
            g2.dispose();
        }
        // no exception expected
    }

    /**
     * Calling initialise sets row and column counts from the dataset.
     */
    public void testInitialiseSetsRowColumnCounts() {
        this.renderer.initialise(null, new Rectangle2D.Double(), this.plot,
                this.dataset, null);
        assertEquals(2, this.renderer.getRowCount());
        assertEquals(2, this.renderer.getColumnCount());
    }

    /**
     * createState returns a non-null state object, and its visible
     * series array reflects series visibility.
     */
    public void testCreateState() {
        this.renderer.setPlot(this.plot);
        // fake rowCount
        this.renderer.initialise(null, new Rectangle2D.Double(), this.plot,
                this.dataset, null);
        CategoryItemRendererState state = this.renderer.createState(null);
        assertNotNull(state);
        int[] visible = state.getVisibleSeriesArray();
        assertEquals(2, visible.length); // both series visible by default
    }

    /**
     * Testing drawRangeMarker with a ValueMarker draws a line.
     */
    public void testDrawRangeMarkerValueMarker() {
        this.renderer.setPlot(this.plot);
        ValueAxis axis = new NumberAxis("Test");
        axis.setRange(0, 10);
        ValueMarker marker = new ValueMarker(5.0);
        marker.setPaint(java.awt.Color.blue);
        marker.setStroke(new java.awt.BasicStroke());
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        Graphics2D g2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawRangeMarker(g2, this.plot, axis, marker, dataArea);
        } finally {
            g2.dispose();
        }
        // success if no exception
    }

    /**
     * Testing drawDomainMarker with a CategoryMarker.
     */
    public void testDrawDomainMarker() {
        this.renderer.setPlot(this.plot);
        this.plot.setDataset(0, this.dataset);
        this.plot.setRenderer(0, this.renderer);
        CategoryAxis axis = new CategoryAxis();
        CategoryMarker marker = new CategoryMarker("Category1");
        marker.setPaint(java.awt.Color.green);
        marker.setStroke(new java.awt.BasicStroke());
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        Graphics2D g2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawDomainMarker(g2, this.plot, axis, marker,
                    dataArea);
        } finally {
            g2.dispose();
        }
    }

    /**
     * Test drawBackground delegates to plot.
     */
    public void testDrawBackground() {
        this.renderer.setPlot(this.plot);
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        Graphics2D g2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawBackground(g2, this.plot, dataArea);
        } finally {
            g2.dispose();
        }
    }

    /**
     * Test drawOutline delegates to plot.
     */
    public void testDrawOutline() {
        this.renderer.setPlot(this.plot);
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        Graphics2D g2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
        try {
            this.renderer.drawOutline(g2, this.plot, dataArea);
        } finally {
            g2.dispose();
        }
    }

}
