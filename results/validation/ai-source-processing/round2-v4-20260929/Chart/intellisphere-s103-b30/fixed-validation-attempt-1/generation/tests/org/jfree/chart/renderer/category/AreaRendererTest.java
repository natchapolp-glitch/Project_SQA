package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.annotations.CategoryTextAnnotation;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Tests for {@link AreaRenderer} and inherited behavior from
 * {@link AbstractCategoryItemRenderer}.
 */
public class AreaRendererTest extends TestCase {

    /**
     * Test default initialization and basic properties.
     */
    public void testDefaults() {
        AreaRenderer renderer = new AreaRenderer();
        assertEquals(1, renderer.getPassCount());
        assertEquals(0, renderer.getRowCount());
        assertEquals(0, renderer.getColumnCount());
        assertNull(renderer.getPlot());
        assertNull(renderer.getBaseItemLabelGenerator());
        assertNull(renderer.getBaseToolTipGenerator());
        assertNull(renderer.getBaseURLGenerator());
        assertNotNull(renderer.getLegendItemLabelGenerator());
        assertNull(renderer.getLegendItemToolTipGenerator());
        assertNull(renderer.getLegendItemURLGenerator());
        assertNull(renderer.getDrawingSupplier());
    }

    /**
     * Test setting and getting the plot reference, including null argument rejection.
     */
    public void testSetPlot() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        renderer.setPlot(plot);
        assertSame(plot, renderer.getPlot());

        try {
            renderer.setPlot(null);
            fail("Expected IllegalArgumentException for null plot");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test item label generator accessors and fallback logic.
     */
    public void testItemLabelGenerators() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator baseGen = new StandardCategoryItemLabelGenerator();
        renderer.setBaseItemLabelGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseItemLabelGenerator());
        assertSame(baseGen, renderer.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator seriesGen = new StandardCategoryItemLabelGenerator();
        renderer.setSeriesItemLabelGenerator(0, seriesGen);
        assertSame(seriesGen, renderer.getSeriesItemLabelGenerator(0));
        assertSame(seriesGen, renderer.getItemLabelGenerator(0, 0, false));
        assertSame(baseGen, renderer.getItemLabelGenerator(1, 0, false));

        renderer.setSeriesItemLabelGenerator(0, null, true);
        assertNull(renderer.getSeriesItemLabelGenerator(0));
        assertSame(baseGen, renderer.getItemLabelGenerator(0, 0, false));
    }

    /**
     * Test tooltip generator accessors and fallback logic.
     */
    public void testToolTipGenerators() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator baseGen = new StandardCategoryToolTipGenerator();
        renderer.setBaseToolTipGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseToolTipGenerator());
        assertSame(baseGen, renderer.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator seriesGen = new StandardCategoryToolTipGenerator();
        renderer.setSeriesToolTipGenerator(1, seriesGen);
        assertSame(seriesGen, renderer.getSeriesToolTipGenerator(1));
        assertSame(seriesGen, renderer.getToolTipGenerator(1, 0, false));
        assertSame(baseGen, renderer.getToolTipGenerator(0, 0, false));
    }

    /**
     * Test URL generator accessors and fallback logic.
     */
    public void testURLGenerators() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.getURLGenerator(0, 0, false));

        CategoryURLGenerator baseGen = new StandardCategoryURLGenerator();
        renderer.setBaseURLGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseURLGenerator());
        assertSame(baseGen, renderer.getURLGenerator(0, 0, false));

        CategoryURLGenerator seriesGen = new StandardCategoryURLGenerator();
        renderer.setSeriesURLGenerator(2, seriesGen);
        assertSame(seriesGen, renderer.getSeriesURLGenerator(2));
        assertSame(seriesGen, renderer.getURLGenerator(2, 0, false));
        assertSame(baseGen, renderer.getURLGenerator(0, 0, false));
    }

    /**
     * Test legend item label generator configuration and validation.
     */
    public void testLegendItemGenerators() {
        AreaRenderer renderer = new AreaRenderer();
        CategorySeriesLabelGenerator labelGen = new StandardCategorySeriesLabelGenerator("{0}");
        renderer.setLegendItemLabelGenerator(labelGen);
        assertSame(labelGen, renderer.getLegendItemLabelGenerator());

        try {
            renderer.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException on null label generator");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        CategorySeriesLabelGenerator tipGen = new StandardCategorySeriesLabelGenerator("{0} tips");
        renderer.setLegendItemToolTipGenerator(tipGen);
        assertSame(tipGen, renderer.getLegendItemToolTipGenerator());
        renderer.setLegendItemToolTipGenerator(null);
        assertNull(renderer.getLegendItemToolTipGenerator());

        CategorySeriesLabelGenerator urlGen = new StandardCategorySeriesLabelGenerator("{0} urls");
        renderer.setLegendItemURLGenerator(urlGen);
        assertSame(urlGen, renderer.getLegendItemURLGenerator());
        renderer.setLegendItemURLGenerator(null);
        assertNull(renderer.getLegendItemURLGenerator());
    }

    /**
     * Test adding and removing annotations in different layers.
     */
    public void testAnnotations() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryAnnotation a1 = new CategoryTextAnnotation("Note 1", "Category 1", 10.0);
        CategoryAnnotation a2 = new CategoryTextAnnotation("Note 2", "Category 2", 20.0);

        renderer.addAnnotation(a1);
        renderer.addAnnotation(a2, Layer.BACKGROUND);

        assertTrue(renderer.removeAnnotation(a1));
        assertFalse(renderer.removeAnnotation(a1));

        renderer.removeAnnotations();
        assertFalse(renderer.removeAnnotation(a2));

        try {
            renderer.addAnnotation(null);
            fail("Expected IllegalArgumentException for null annotation");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test findRangeBounds with null or populated datasets.
     */
    public void testFindRangeBounds() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.findRangeBounds(null));
        assertNull(renderer.findRangeBounds(null, true));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        assertNull(renderer.findRangeBounds(dataset));

        dataset.addValue(1.5, "Row 1", "Col 1");
        dataset.addValue(4.5, "Row 1", "Col 2");
        dataset.addValue(-2.0, "Row 2", "Col 1");

        Range range = renderer.findRangeBounds(dataset);
        assertNotNull(range);
        assertEquals(-2.0, range.getLowerBound(), 1e-9);
        assertEquals(4.5, range.getUpperBound(), 1e-9);

        Range rangeWithInterval = renderer.findRangeBounds(dataset, true);
        assertEquals(range, rangeWithInterval);
    }

    /**
     * Test renderer initialisation updates row and column counts.
     */
    public void testInitialise() {
        AreaRenderer renderer = new AreaRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("X"), new NumberAxis("Y"), renderer);
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        CategoryItemRendererState state = renderer.initialise(g2, new Rectangle2D.Double(0, 0, 200, 200),
                plot, dataset, info);

        assertNotNull(state);
        assertEquals(2, renderer.getRowCount());
        assertEquals(2, renderer.getColumnCount());
        assertSame(plot, renderer.getPlot());

        // Initialise with null dataset should reset counts
        renderer.initialise(g2, new Rectangle2D.Double(0, 0, 200, 200), plot, null, info);
        assertEquals(0, renderer.getRowCount());
        assertEquals(0, renderer.getColumnCount());

        g2.dispose();
    }

    /**
     * Test createState method returns non-null state.
     */
    public void testCreateState() {
        AreaRenderer renderer = new AreaRenderer();
        PlotRenderingInfo info = new PlotRenderingInfo(null);
        CategoryItemRendererState state = renderer.createState(info);
        assertNotNull(state);
        assertSame(info, state.getInfo());
    }

    /**
     * Test getItemMiddle coordinate computation.
     */
    public void testGetItemMiddle() {
        AreaRenderer renderer = new AreaRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Row1", "CatA");
        dataset.addValue(20.0, "Row1", "CatB");

        CategoryAxis axis = new CategoryAxis("Categories");
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 0.0, 300.0, 200.0);
        double midA = renderer.getItemMiddle("Row1", "CatA", dataset, axis, dataArea, RectangleEdge.BOTTOM);
        double midB = renderer.getItemMiddle("Row1", "CatB", dataset, axis, dataArea, RectangleEdge.BOTTOM);

        assertTrue("midA should be within data area width", midA >= 0.0 && midA <= 300.0);
        assertTrue("midB should be within data area width", midB >= 0.0 && midB <= 300.0);
        assertTrue("midA should precede midB", midA < midB);
    }

    /**
     * Test getDomainAxis and getRangeAxis utility methods.
     */
    public void testGetAxes() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryAxis domainAxis = new CategoryAxis("Domain");
        NumberAxis rangeAxis = new NumberAxis("Range");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CategoryPlot plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer);

        assertSame(domainAxis, renderer.getDomainAxis(plot, dataset));
        assertSame(rangeAxis, renderer.getRangeAxis(plot, 0));
        // Non-existent secondary index falls back to primary range axis
        assertSame(rangeAxis, renderer.getRangeAxis(plot, 99));
    }

    /**
     * Test drawing supplier delegation from plot.
     */
    public void testGetDrawingSupplier() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.getDrawingSupplier());

        CategoryPlot plot = new CategoryPlot();
        renderer.setPlot(plot);
        assertNotNull(renderer.getDrawingSupplier());
    }

    /**
     * Test getLegendItems when plot or dataset is null or present.
     */
    public void testGetLegendItems() {
        AreaRenderer renderer = new AreaRenderer();
        LegendItemCollection items = renderer.getLegendItems();
        assertNotNull(items);
        assertEquals(0, items.getItemCount());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Type A");
        dataset.addValue(20.0, "Series 2", "Type A");

        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("X"), new NumberAxis("Y"), renderer);
        LegendItemCollection populatedItems = renderer.getLegendItems();
        assertEquals(2, populatedItems.getItemCount());

        LegendItem item0 = renderer.getLegendItem(0, 0);
        assertNotNull(item0);
        assertEquals("Series 1", item0.getLabel());
    }

    /**
     * Test addEntity invalid argument handling and entity generation.
     */
    public void testAddEntity() {
        AreaRenderer renderer = new AreaRenderer();
        EntityCollection entities = new StandardEntityCollection();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Row1", "Col1");

        try {
            renderer.addEntity(entities, null, dataset, 0, 0, false);
            fail("Expected IllegalArgumentException for null hotspot");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        Rectangle2D hotspot = new Rectangle2D.Double(5.0, 5.0, 10.0, 10.0);
        renderer.addEntity(entities, hotspot, dataset, 0, 0, false);
        assertEquals(1, entities.getEntityCount());
    }

    /**
     * Test drawDomainLine parameter validation.
     */
    public void testDrawDomainLineValidation() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 100);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        try {
            renderer.drawDomainLine(g2, plot, area, 50.0, null, new BasicStroke(1.0f));
            fail("Expected IllegalArgumentException for null paint");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        try {
            renderer.drawDomainLine(g2, plot, area, 50.0, Color.BLUE, null);
            fail("Expected IllegalArgumentException for null stroke");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        g2.dispose();
    }

    /**
     * Test equality and hashCode contract.
     */
    public void testEqualsAndHashCode() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        assertEquals(r1, r2);

        r1.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertFalse(r1.equals(r2));
        r2.setBaseURLGenerator(new StandardCategoryURLGenerator());
        assertEquals(r1, r2);
    }

    /**
     * Test cloning creates an independent copy.
     */
    public void testCloning() throws CloneNotSupportedException {
        AreaRenderer r1 = new AreaRenderer();
        r1.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        r1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        r1.setBaseURLGenerator(new StandardCategoryURLGenerator());

        AreaRenderer r2 = (AreaRenderer) r1.clone();
        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);

        r2.setSeriesItemLabelGenerator(0, new StandardCategoryItemLabelGenerator());
        assertFalse(r1.equals(r2));
    }
}
