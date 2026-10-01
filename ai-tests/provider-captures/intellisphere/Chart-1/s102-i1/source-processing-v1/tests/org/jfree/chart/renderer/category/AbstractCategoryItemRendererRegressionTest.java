package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.RectangleEdge;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Regression tests for {@link AbstractCategoryItemRenderer} using {@link AreaRenderer}.
 */
public class AbstractCategoryItemRendererRegressionTest extends TestCase {

    private AreaRenderer renderer;

    protected void setUp() throws Exception {
        super.setUp();
        this.renderer = new AreaRenderer();
    }

    public void testGetPassCount() {
        assertEquals(1, this.renderer.getPassCount());
    }

    public void testPlotAssignment() {
        assertNull(this.renderer.getPlot());
        CategoryPlot plot = new CategoryPlot();
        this.renderer.setPlot(plot);
        assertSame(plot, this.renderer.getPlot());

        try {
            this.renderer.setPlot(null);
            fail("Expected IllegalArgumentException for null plot");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testItemLabelGeneratorHierarchy() {
        assertNull(this.renderer.getBaseItemLabelGenerator());
        assertNull(this.renderer.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator baseGen = new StandardCategoryItemLabelGenerator();
        this.renderer.setBaseItemLabelGenerator(baseGen);
        assertSame(baseGen, this.renderer.getBaseItemLabelGenerator());
        assertSame(baseGen, this.renderer.getItemLabelGenerator(0, 0, false));

        CategoryItemLabelGenerator seriesGen = new StandardCategoryItemLabelGenerator();
        this.renderer.setSeriesItemLabelGenerator(0, seriesGen);
        assertSame(seriesGen, this.renderer.getSeriesItemLabelGenerator(0));
        assertSame(seriesGen, this.renderer.getItemLabelGenerator(0, 0, false));
        assertSame(baseGen, this.renderer.getItemLabelGenerator(1, 0, false));
    }

    public void testToolTipGeneratorHierarchy() {
        assertNull(this.renderer.getBaseToolTipGenerator());
        assertNull(this.renderer.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator baseTip = new StandardCategoryToolTipGenerator();
        this.renderer.setBaseToolTipGenerator(baseTip);
        assertSame(baseTip, this.renderer.getBaseToolTipGenerator());
        assertSame(baseTip, this.renderer.getToolTipGenerator(0, 0, false));

        CategoryToolTipGenerator seriesTip = new StandardCategoryToolTipGenerator();
        this.renderer.setSeriesToolTipGenerator(0, seriesTip);
        assertSame(seriesTip, this.renderer.getSeriesToolTipGenerator(0));
        assertSame(seriesTip, this.renderer.getToolTipGenerator(0, 0, false));
        assertSame(baseTip, this.renderer.getToolTipGenerator(1, 0, false));
    }

    public void testURLGeneratorHierarchy() {
        assertNull(this.renderer.getBaseURLGenerator());
        assertNull(this.renderer.getURLGenerator(0, 0, false));

        CategoryURLGenerator baseURL = new StandardCategoryURLGenerator();
        this.renderer.setBaseURLGenerator(baseURL);
        assertSame(baseURL, this.renderer.getBaseURLGenerator());
        assertSame(baseURL, this.renderer.getURLGenerator(0, 0, false));

        CategoryURLGenerator seriesURL = new StandardCategoryURLGenerator();
        this.renderer.setSeriesURLGenerator(0, seriesURL);
        assertSame(seriesURL, this.renderer.getSeriesURLGenerator(0));
        assertSame(seriesURL, this.renderer.getURLGenerator(0, 0, false));
        assertSame(baseURL, this.renderer.getURLGenerator(1, 0, false));
    }

    public void testLegendGenerators() {
        assertNotNull(this.renderer.getLegendItemLabelGenerator());

        CategorySeriesLabelGenerator labelGen = new StandardCategorySeriesLabelGenerator("{0}");
        this.renderer.setLegendItemLabelGenerator(labelGen);
        assertSame(labelGen, this.renderer.getLegendItemLabelGenerator());

        try {
            this.renderer.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException for null generator");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        CategorySeriesLabelGenerator tipGen = new StandardCategorySeriesLabelGenerator("{1}");
        this.renderer.setLegendItemToolTipGenerator(tipGen);
        assertSame(tipGen, this.renderer.getLegendItemToolTipGenerator());

        CategorySeriesLabelGenerator urlGen = new StandardCategorySeriesLabelGenerator("{2}");
        this.renderer.setLegendItemURLGenerator(urlGen);
        assertSame(urlGen, this.renderer.getLegendItemURLGenerator());
    }

    

    public void testInitialiseWithNullDataset() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        CategoryPlot plot = new CategoryPlot();
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        CategoryItemRendererState state = this.renderer.initialise(g2,
                new Rectangle2D.Double(0, 0, 100, 100), plot, null, info);

        assertNotNull(state);
        assertEquals(0, this.renderer.getRowCount());
        assertEquals(0, this.renderer.getColumnCount());
        assertSame(plot, this.renderer.getPlot());
        g2.dispose();
    }

    public void testInitialiseWithPopulatedDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        CategoryPlot plot = new CategoryPlot();
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        CategoryItemRendererState state = this.renderer.initialise(g2,
                new Rectangle2D.Double(0, 0, 100, 100), plot, dataset, info);

        assertNotNull(state);
        assertEquals(2, this.renderer.getRowCount());
        assertEquals(2, this.renderer.getColumnCount());
        int[] visibleSeries = state.getVisibleSeriesArray();
        assertNotNull(visibleSeries);
        assertEquals(2, visibleSeries.length);
        assertEquals(0, visibleSeries[0]);
        assertEquals(1, visibleSeries[1]);
        g2.dispose();
    }

    public void testCreateState() {
        PlotRenderingInfo info = new PlotRenderingInfo(null);
        CategoryItemRendererState state = this.renderer.createState(info);
        assertNotNull(state);
        assertNotNull(state.getVisibleSeriesArray());
        assertEquals(0, state.getVisibleSeriesArray().length);
    }

    public void testFindRangeBounds() {
        assertNull(this.renderer.findRangeBounds(null));
        assertNull(this.renderer.findRangeBounds(null, true));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        assertNull(this.renderer.findRangeBounds(dataset));

        dataset.addValue(10.0, "R1", "C1");
        dataset.addValue(-5.0, "R1", "C2");
        dataset.addValue(25.0, "R2", "C1");

        Range bounds = this.renderer.findRangeBounds(dataset);
        assertNotNull(bounds);
        assertEquals(-5.0, bounds.getLowerBound(), 1e-9);
        assertEquals(25.0, bounds.getUpperBound(), 1e-9);
    }

    

    public void testGetDomainAxisAndRangeAxis() {
        CategoryPlot plot = new CategoryPlot();
        CategoryAxis catAxis = new CategoryAxis("Cat");
        ValueAxis valAxis = new NumberAxis("Val");
        plot.setDomainAxis(catAxis);
        plot.setRangeAxis(valAxis);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        plot.setDataset(dataset);

        assertSame(catAxis, this.renderer.getDomainAxis(plot, dataset));
        assertSame(valAxis, this.renderer.getRangeAxis(plot, 0));
        assertSame(valAxis, this.renderer.getRangeAxis(plot, 999));
    }

    public void testGetLegendItems() {
        assertNotNull(this.renderer.getLegendItems());
        assertEquals(0, this.renderer.getLegendItems().getItemCount());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R2", "C1");

        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("X"), new NumberAxis("Y"), this.renderer);
        this.renderer.setPlot(plot);

        LegendItemCollection lic = this.renderer.getLegendItems();
        assertEquals(2, lic.getItemCount());

        this.renderer.setSeriesVisibleInLegend(0, Boolean.FALSE);
        LegendItemCollection lic2 = this.renderer.getLegendItems();
        assertEquals(1, lic2.getItemCount());
    }

    public void testGetLegendItem() {
        assertNull(this.renderer.getLegendItem(0, 0));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5.0, "Row1", "Col1");
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("Domain"), new NumberAxis("Range"), this.renderer);
        this.renderer.setPlot(plot);

        LegendItem item = this.renderer.getLegendItem(0, 0);
        assertNotNull(item);
        assertEquals("Row1", item.getLabel());

        this.renderer.setSeriesVisible(0, Boolean.FALSE);
        assertNull(this.renderer.getLegendItem(0, 0));
    }

    public void testAddEntity() {
        EntityCollection collection = new StandardEntityCollection();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        Shape shape = new Rectangle2D.Double(0, 0, 10, 10);

        this.renderer.addEntity(collection, shape, dataset, 0, 0, false);
        assertEquals(1, collection.getEntityCount());

        try {
            this.renderer.addEntity(collection, null, dataset, 0, 0, false);
            fail("Expected IllegalArgumentException on null hotspot");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testDrawBackgroundAndOutline() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        CategoryPlot plot = new CategoryPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(10, 10, 180, 180);

        this.renderer.drawBackground(g2, plot, dataArea);
        this.renderer.drawOutline(g2, plot, dataArea);
        g2.dispose();
    }

    public void testDrawDomainLine() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        CategoryPlot plot = new CategoryPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(10, 10, 80, 80);
        Paint paint = Color.RED;
        Stroke stroke = new BasicStroke(1.0f);

        plot.setOrientation(PlotOrientation.VERTICAL);
        this.renderer.drawDomainLine(g2, plot, dataArea, 50.0, paint, stroke);

        plot.setOrientation(PlotOrientation.HORIZONTAL);
        this.renderer.drawDomainLine(g2, plot, dataArea, 50.0, paint, stroke);

        try {
            this.renderer.drawDomainLine(g2, plot, dataArea, 50.0, null, stroke);
            fail("Expected IllegalArgumentException for null paint");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

        try {
            this.renderer.drawDomainLine(g2, plot, dataArea, 50.0, paint, null);
            fail("Expected IllegalArgumentException for null stroke");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
        g2.dispose();
    }

    public void testDrawDomainMarker() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        CategoryAxis axis = new CategoryAxis("X");
        CategoryPlot plot = new CategoryPlot(dataset, axis, new NumberAxis("Y"), this.renderer);
        this.renderer.setPlot(plot);

        Rectangle2D dataArea = new Rectangle2D.Double(10, 10, 180, 180);
        CategoryMarker marker = new CategoryMarker("C1");
        marker.setDrawAsLine(true);
        marker.setLabel("Marker Label");

        this.renderer.drawDomainMarker(g2, plot, axis, marker, dataArea);

        marker.setDrawAsLine(false);
        this.renderer.drawDomainMarker(g2, plot, axis, marker, dataArea);
        g2.dispose();
    }

    public void testDrawRangeMarker() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        CategoryPlot plot = new CategoryPlot();
        NumberAxis axis = new NumberAxis("Y");
        axis.setRange(0.0, 100.0);
        Rectangle2D dataArea = new Rectangle2D.Double(10, 10, 180, 180);

        ValueMarker vm = new ValueMarker(50.0);
        vm.setLabel("Value");
        this.renderer.drawRangeMarker(g2, plot, axis, vm, dataArea);

        IntervalMarker im = new IntervalMarker(20.0, 40.0);
        im.setLabel("Interval");
        this.renderer.drawRangeMarker(g2, plot, axis, im, dataArea);
        g2.dispose();
    }

    

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

        assertFalse(r1.equals(null));
        assertFalse(r1.equals(new String("other")));
    }

    
}
