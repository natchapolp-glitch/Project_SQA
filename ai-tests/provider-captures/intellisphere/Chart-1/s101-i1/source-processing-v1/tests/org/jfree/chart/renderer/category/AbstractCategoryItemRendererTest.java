package org.jfree.chart.renderer.category;

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
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Regression tests for {@link AbstractCategoryItemRenderer}, exercised
 * through the concrete {@link AreaRenderer} subclass and (for protected
 * members) via same-package access.
 */
public class AbstractCategoryItemRendererTest extends TestCase {

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Row0", "Col0");
        dataset.addValue(2.0, "Row0", "Col1");
        dataset.addValue(3.0, "Row0", "Col2");
        dataset.addValue(4.0, "Row1", "Col0");
        dataset.addValue(5.0, "Row1", "Col1");
        dataset.addValue(6.0, "Row1", "Col2");
        return dataset;
    }

    // ---- basic state ----

    public void testGetPassCount() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(1, r.getPassCount());
    }

    public void testPlotDefaultNull() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getPlot());
    }

    public void testSetPlotNullThrows() {
        AreaRenderer r = new AreaRenderer();
        try {
            r.setPlot(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testSetGetPlot() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset dataset = createDataset();
        CategoryAxis domainAxis = new CategoryAxis("Category");
        ValueAxis rangeAxis = new NumberAxis("Value");
        CategoryPlot plot = new CategoryPlot(dataset, domainAxis, rangeAxis, r);
        r.setPlot(plot);
        assertSame(plot, r.getPlot());
    }

    public void testRowColumnCountDefaultZero() {
        AreaRenderer r = new AreaRenderer();
        assertEquals(0, r.getRowCount());
        assertEquals(0, r.getColumnCount());
    }

    // ---- item label generator ----

    public void testBaseItemLabelGeneratorDefaultNull() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getBaseItemLabelGenerator());
    }

    public void testSetGetBaseItemLabelGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryItemLabelGenerator g = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(g);
        assertSame(g, r.getBaseItemLabelGenerator());
    }

    public void testSeriesItemLabelGenerator() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getSeriesItemLabelGenerator(0));
        CategoryItemLabelGenerator g = new StandardCategoryItemLabelGenerator();
        r.setSeriesItemLabelGenerator(0, g);
        assertSame(g, r.getSeriesItemLabelGenerator(0));
        assertNull(r.getSeriesItemLabelGenerator(1));
    }

    public void testGetItemLabelGeneratorFallback() {
        AreaRenderer r = new AreaRenderer();
        CategoryItemLabelGenerator base = new StandardCategoryItemLabelGenerator();
        r.setBaseItemLabelGenerator(base);
        // no series-level generator set for row 0 -> fallback to base
        assertSame(base, r.getItemL
