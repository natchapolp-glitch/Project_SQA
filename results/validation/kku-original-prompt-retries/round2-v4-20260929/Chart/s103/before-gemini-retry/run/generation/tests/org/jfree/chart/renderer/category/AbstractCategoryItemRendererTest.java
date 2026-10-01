package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Regression tests for {@link AbstractCategoryItemRenderer} behavior exercised
 * through {@link AreaRenderer}, using only supported shared API.
 *
 * References:
 * - source/org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java
 * - ant/build.xml (JUnit 3 style tests, Java 1.4 source level)
 */
public class AbstractCategoryItemRendererTest extends TestCase {

    private static class PublicCloneableLabelGenerator
            implements CategoryItemLabelGenerator, PublicCloneable {

        public String generateLabel(CategoryDataset dataset, int row, int column) {
            return "L" + row + "-" + column;
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public boolean equals(Object obj) {
            return obj instanceof PublicCloneableLabelGenerator;
        }
    }

    private static class PublicCloneableToolTipGenerator
            implements CategoryToolTipGenerator, PublicCloneable {

        public String generateToolTip(CategoryDataset dataset, int row, int column) {
            return "T" + row + "-" + column;
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public boolean equals(Object obj) {
            return obj instanceof PublicCloneableToolTipGenerator;
        }
    }

    private static class PublicCloneableURLGenerator
            implements CategoryURLGenerator, PublicCloneable {

        public String generateURL(CategoryDataset dataset, int row, int column) {
            return "U" + row + "-" + column;
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public boolean equals(Object obj) {
            return obj instanceof PublicCloneableURLGenerator;
        }
    }

    private static class PublicCloneableSeriesLabelGenerator
            implements CategorySeriesLabelGenerator, PublicCloneable {

        private String prefix;

        public PublicCloneableSeriesLabelGenerator(String prefix) {
            this.prefix = prefix;
        }

        public String generateLabel(CategoryDataset dataset, int series) {
            return this.prefix + series;
        }

        public Object clone() throws CloneNotSupportedException {
            return new PublicCloneableSeriesLabelGenerator(this.prefix);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PublicCloneableSeriesLabelGenerator)) {
                return false;
            }
            PublicCloneableSeriesLabelGenerator that
                    = (PublicCloneableSeriesLabelGenerator) obj;
            return this.prefix.equals(that.prefix);
        }
    }

    private static class NonCloneableLabelGenerator
            implements CategoryItemLabelGenerator {
        public String generateLabel(CategoryDataset dataset, int row, int column) {
            return "N";
        }
    }

    private static class NonCloneableToolTipGenerator
            implements CategoryToolTipGenerator {
        public String generateToolTip(CategoryDataset dataset, int row, int column) {
            return "N";
        }
    }

    private static class NonCloneableURLGenerator
            implements CategoryURLGenerator {
        public String generateURL(CategoryDataset dataset, int row, int column) {
            return "N";
        }
    }

    private static class ExposedAreaRenderer extends AreaRenderer {
        public Range findRangeBoundsPublic(CategoryDataset dataset,
                boolean includeInterval) {
            return super.findRangeBounds(dataset, includeInterval);
        }

        public void addEntityPublic(StandardEntityCollection entities,
                Shape hotspot, CategoryDataset dataset, int row, int column,
                boolean selected) {
            super.addEntity(entities, hotspot, dataset, row, column, selected);
        }

        public CategoryAxis getDomainAxisPublic(CategoryPlot plot,
                CategoryDataset dataset) {
            return super.getDomainAxis(plot, dataset);
        }

        public NumberAxis getRangeAxisPublic(CategoryPlot plot, int index) {
            return (NumberAxis) super.getRangeAxis(plot, index);
        }
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "S1", "C1");
        d.addValue(2.0, "S1", "C2");
        d.addValue(3.0, "S2", "C1");
        d.addValue(4.0, "S2", "C2");
        return d;
    }

    public void testSetPlotRejectsNull() {
        AreaRenderer r = new AreaRenderer();
        try {
            r.setPlot(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'plot' argument.", e.getMessage());
        }
    }

    public void testInitialiseWithNullDatasetResetsRowAndColumnCounts() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        Graphics2D g2 = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB).createGraphics();
        try {
            r.initialise(g2, new Rectangle2D.Double(0.0, 0.0, 1.0, 1.0),
                    plot, null, null);
            assertEquals(0, r.getRowCount());
            assertEquals(0, r.getColumnCount());
            assertSame(plot, r.getPlot());
        }
        finally {
            g2.dispose();
        }
    }

    public void testInitialiseWithDatasetUpdatesRowAndColumnCounts() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryPlot plot = new CategoryPlot();
        Graphics2D g2 = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_ARGB).createGraphics();
        try {
            r.initialise(g2, new Rectangle2D.Double(0.0, 0.0, 1.0, 1.0),
                    plot, d, null);
            assertEquals(2, r.getRowCount());
            assertEquals(2, r.getColumnCount());
        }
        finally {
            g2.dispose();
        }
    }

    public void testItemLabelGeneratorFallsBackToBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryItemLabelGenerator g = new PublicCloneableLabelGenerator();
        r.setBaseItemLabelGenerator(g);
        assertSame(g, r.getItemLabelGenerator(0, 0, false));
    }

    public void testSeriesItemLabelGeneratorOverridesBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryItemLabelGenerator base = new PublicCloneableLabelGenerator();
        CategoryItemLabelGenerator series = new PublicCloneableLabelGenerator();
        r.setBaseItemLabelGenerator(base);
        r.setSeriesItemLabelGenerator(1, series);
        assertSame(series, r.getItemLabelGenerator(1, 0, false));
        assertSame(base, r.getItemLabelGenerator(0, 0, false));
    }

    public void testToolTipGeneratorFallsBackToBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryToolTipGenerator g = new PublicCloneableToolTipGenerator();
        r.setBaseToolTipGenerator(g);
        assertSame(g, r.getToolTipGenerator(0, 0, false));
    }

    public void testSeriesToolTipGeneratorOverridesBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryToolTipGenerator base = new PublicCloneableToolTipGenerator();
        CategoryToolTipGenerator series = new PublicCloneableToolTipGenerator();
        r.setBaseToolTipGenerator(base);
        r.setSeriesToolTipGenerator(1, series);
        assertSame(series, r.getToolTipGenerator(1, 0, false));
        assertSame(base, r.getToolTipGenerator(0, 0, false));
    }

    public void testURLGeneratorFallsBackToBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryURLGenerator g = new PublicCloneableURLGenerator();
        r.setBaseURLGenerator(g);
        assertSame(g, r.getURLGenerator(0, 0, false));
    }

    public void testSeriesURLGeneratorOverridesBaseGenerator() {
        AreaRenderer r = new AreaRenderer();
        CategoryURLGenerator base = new PublicCloneableURLGenerator();
        CategoryURLGenerator series = new PublicCloneableURLGenerator();
        r.setBaseURLGenerator(base);
        r.setSeriesURLGenerator(1, series);
        assertSame(series, r.getURLGenerator(1, 0, false));
        assertSame(base, r.getURLGenerator(0, 0, false));
    }

    public void testAddAnnotationRejectsNull() {
        AreaRenderer r = new AreaRenderer();
        try {
            r.addAnnotation(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'annotation' argument.", e.getMessage());
        }
    }

    public void testSetLegendItemLabelGeneratorRejectsNull() {
        AreaRenderer r = new AreaRenderer();
        try {
            r.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'generator' argument.", e.getMessage());
        }
    }

    public void testFindRangeBoundsReturnsNullForNullDataset() {
        ExposedAreaRenderer r = new ExposedAreaRenderer();
        assertNull(r.findRangeBounds((CategoryDataset) null));
        assertNull(r.findRangeBoundsPublic(null, false));
    }

    public void testFindRangeBoundsReturnsDatasetRange() {
        ExposedAreaRenderer r = new ExposedAreaRenderer();
        DefaultCategoryDataset d = createDataset();
        Range range = r.findRangeBounds(d);
        assertEquals(1.0, range.getLowerBound(), 0.0000001);
        assertEquals(4.0, range.getUpperBound(), 0.0000001);
    }

    public void testGetItemMiddleDelegatesToAxis() {
        AreaRenderer r = new AreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryAxis axis = new CategoryAxis();
        Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, 200.0, 100.0);
        double m1 = r.getItemMiddle("S1", "C1", d, axis, area,
                org.jfree.chart.util.RectangleEdge.BOTTOM);
        double m2 = axis.getCategoryMiddle("C1", d.getColumnKeys(), area,
                org.jfree.chart.util.RectangleEdge.BOTTOM);
        assertEquals(m2, m1, 0.0000001);
    }

    public void testDrawDomainLineRejectsNullPaint() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        plot.setOrientation(PlotOrientation.VERTICAL);
        Graphics2D g2 = new BufferedImage(20, 20,
                BufferedImage.TYPE_INT_ARGB).createGraphics();
        try {
            r.drawDomainLine(g2, plot, new Rectangle2D.Double(0.0, 0.0, 10.0, 10.0),
                    5.0, null, new BasicStroke(1.0f));
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'paint' argument.", e.getMessage());
        }
        finally {
            g2.dispose();
        }
    }

    public void testDrawDomainLineRejectsNullStroke() {
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        plot.setOrientation(PlotOrientation.VERTICAL);
        Graphics2D g2 = new BufferedImage(20, 20,
                BufferedImage.TYPE_INT_ARGB).createGraphics();
        try {
            r.drawDomainLine(g2, plot, new Rectangle2D.Double(0.0, 0.0, 10.0, 10.0),
                    5.0, java.awt.Color.black, null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'stroke' argument.", e.getMessage());
        }
        finally {
            g2.dispose();
        }
    }

    public void testGetLegendItemReturnsNullWhenRendererHasNoPlot() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getLegendItem(0, 0));
    }

    public void testGetLegendItemPopulatesDatasetSeriesAndLabel() {
        DefaultCategoryDataset d = createDataset();
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot(d, new CategoryAxis(),
                new NumberAxis(), r);

        LegendItem item = r.getLegendItem(0, 1);
        assertNotNull(item);
        assertEquals("S2", item.getLabel());
        assertEquals("S2", item.getSeriesKey());
        assertEquals(1, item.getSeriesIndex());
        assertSame(d, item.getDataset());
        assertEquals(0, item.getDatasetIndex());
    }

    public void testLegendItemUsesCustomGeneratorsForTooltipAndURL() {
        DefaultCategoryDataset d = createDataset();
        AreaRenderer r = new AreaRenderer();
        r.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator("L{0}"));
        r.setLegendItemToolTipGenerator(new PublicCloneableSeriesLabelGenerator("TT"));
        r.setLegendItemURLGenerator(new PublicCloneableSeriesLabelGenerator("UU"));
        new CategoryPlot(d, new CategoryAxis(), new NumberAxis(), r);

        LegendItem item = r.getLegendItem(0, 0);
        assertNotNull(item);
        assertEquals("LS1", item.getLabel());
        assertEquals("TT0", item.getToolTipText());
        assertEquals("UU0", item.getURLText());
    }

    public void testGetLegendItemsReturnsAscendingOrderByDefault() {
        DefaultCategoryDataset d = createDataset();
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot(d, new CategoryAxis(),
                new NumberAxis(), r);

        LegendItemCollection items = r.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("S1", items.get(0).getSeriesKey());
        assertEquals("S2", items.get(1).getSeriesKey());
        assertSame(plot, r.getPlot());
    }

    public void testGetLegendItemsRespectsDescendingRowRenderingOrder() {
        DefaultCategoryDataset d = createDataset();
        AreaRenderer r = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot(d, new CategoryAxis(),
                new NumberAxis(), r);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);

        LegendItemCollection items = r.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("S2", items.get(0).getSeriesKey());
        assertEquals("S1", items.get(1).getSeriesKey());
    }

    public void testEqualsReflectsConfiguredGenerators() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r2));

        r1.setBaseURLGenerator(new PublicCloneableURLGenerator());
        assertFalse(r1.equals(r2));

        r2.setBaseURLGenerator(new PublicCloneableURLGenerator());
        assertTrue(r1.equals(r2));

        r1.setLegendItemToolTipGenerator(
                new PublicCloneableSeriesLabelGenerator("A"));
        assertFalse(r1.equals(r2));

        r2.setLegendItemToolTipGenerator(
                new PublicCloneableSeriesLabelGenerator("A"));
        assertTrue(r1.equals(r2));
    }

    public void testCloneCreatesIndependentCopyForCloneableGenerators()
            throws CloneNotSupportedException {
        AreaRenderer r1 = new AreaRenderer();
        r1.setBaseItemLabelGenerator(new PublicCloneableLabelGenerator());
        r1.setBaseToolTipGenerator(new PublicCloneableToolTipGenerator());
        r1.setBaseURLGenerator(new PublicCloneableURLGenerator());
        r1.setLegendItemLabelGenerator(
                new PublicCloneableSeriesLabelGenerator("L"));
        r1.setLegendItemToolTipGenerator(
                new PublicCloneableSeriesLabelGenerator("T"));
        r1.setLegendItemURLGenerator(
                new PublicCloneableSeriesLabelGenerator("U"));

        AreaRenderer r2 = (AreaRenderer) r1.clone();
        assertNotSame(r1, r2);
        assertTrue(r1.equals(r2));

        r1.setSeriesURLGenerator(0, new PublicCloneableURLGenerator());
        assertFalse(r1.equals(r2));
    }

    public void testCloneFailsForNonCloneableBaseItemLabelGenerator() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseItemLabelGenerator(new NonCloneableLabelGenerator());
        try {
            r.clone();
            fail("Expected CloneNotSupportedException.");
        }
        catch (CloneNotSupportedException e) {
            assertEquals("ItemLabelGenerator not cloneable.", e.getMessage());
        }
    }

    public void testCloneFailsForNonCloneableBaseToolTipGenerator() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseToolTipGenerator(new NonCloneableToolTipGenerator());
        try {
            r.clone();
            fail("Expected CloneNotSupportedException.");
        }
        catch (CloneNotSupportedException e) {
            assertEquals("Base tool tip generator not cloneable.", e.getMessage());
        }
    }

    public void testCloneFailsForNonCloneableBaseURLGenerator() {
        AreaRenderer r = new AreaRenderer();
        r.setBaseURLGenerator(new NonCloneableURLGenerator());
        try {
            r.clone();
            fail("Expected CloneNotSupportedException.");
        }
        catch (CloneNotSupportedException e) {
            assertEquals("Base item URL generator not cloneable.", e.getMessage());
        }
    }

    public void testGetDrawingSupplierReturnsNullWithoutPlot() {
        AreaRenderer r = new AreaRenderer();
        assertNull(r.getDrawingSupplier());
    }

    public void testGetDomainAxisAndRangeAxisSelectionFromPlot() {
        ExposedAreaRenderer r = new ExposedAreaRenderer();
        DefaultCategoryDataset d = createDataset();
        CategoryAxis domainAxis = new CategoryAxis("D0");
        NumberAxis rangeAxis0 = new NumberAxis("R0");
        NumberAxis rangeAxis1 = new NumberAxis("R1");
        CategoryPlot plot = new CategoryPlot(d, domainAxis, rangeAxis0, r);
        plot.setRangeAxis(1, rangeAxis1);

        assertSame(domainAxis, r.getDomainAxisPublic(plot, d));
        assertSame(rangeAxis1, r.getRangeAxisPublic(plot, 1));
        assertSame(rangeAxis0, r.getRangeAxisPublic(plot, 2));
    }

    public void testAddEntityRejectsNullHotspot() {
        ExposedAreaRenderer r = new ExposedAreaRenderer();
        try {
            r.addEntityPublic(new StandardEntityCollection(), null,
                    createDataset(), 0, 0, false);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'hotspot' argument.", e.getMessage());
        }
    }

    public void testAddEntityCreatesCategoryItemEntityWithTooltipAndURL() {
        ExposedAreaRenderer r = new ExposedAreaRenderer();
        r.setBaseToolTipGenerator(new PublicCloneableToolTipGenerator());
        r.setBaseURLGenerator(new PublicCloneableURLGenerator());
        DefaultCategoryDataset d = createDataset();
        CategoryPlot plot = new CategoryPlot(d, new CategoryAxis(),
                new NumberAxis(), r);

        StandardEntityCollection entities = new StandardEntityCollection();
        r.addEntityPublic(entities, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                d, 0, 1, false);

        assertEquals(1, entities.getEntityCount());
        org.jfree.chart.entity.CategoryItemEntity e
                = (org.jfree.chart.entity.CategoryItemEntity) entities.getEntity(0);
        assertEquals("T0-1", e.getToolTipText());
        assertEquals("U0-1", e.getURLText());
        assertEquals("S1", e.getRowKey());
        assertEquals("C2", e.getColumnKey());
        assertSame(d, e.getDataset());
        assertSame(plot, r.getPlot());
    }
}
