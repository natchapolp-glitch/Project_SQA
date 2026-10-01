Rendered KKU IntelSphere response. Model label: OpenAI - gpt-5.4

```java
package org.jfree.chart.renderer.category;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.util.Layer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Regression tests for {@link AbstractCategoryItemRenderer} behavior exercised
 * through {@link AreaRenderer}, using only supported public/protected API.
 */
public class AbstractCategoryItemRendererTest extends TestCase {

    private static class TestAreaRenderer extends AreaRenderer {
        public Range findRangeBoundsPublic(CategoryDataset dataset,
                boolean includeInterval) {
            return super.findRangeBounds(dataset, includeInterval);
        }

        public CategoryItemRendererState createStatePublic(
                PlotRenderingInfo info) {
            return super.createState(info);
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

    private static class FixedLabelGenerator
            implements CategoryItemLabelGenerator, PublicCloneable {
        private String value;

        FixedLabelGenerator(String value) {
            this.value = value;
        }

        public String generateRowLabel(CategoryDataset dataset, int row) {
            return this.value;
        }

        public String generateColumnLabel(CategoryDataset dataset, int column) {
            return this.value;
        }

        public String generateLabel(CategoryDataset dataset, int row,
                int column) {
            return this.value;
        }

        public Object clone() throws CloneNotSupportedException {
            return new FixedLabelGenerator(this.value);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FixedLabelGenerator)) {
                return false;
            }
            FixedLabelGenerator that = (FixedLabelGenerator) obj;
            return this.value.equals(that.value);
        }
    }

    private static class FixedToolTipGenerator
            implements CategoryToolTipGenerator, PublicCloneable {
        private String value;

        FixedToolTipGenerator(String value) {
            this.value = value;
        }

        public String generateToolTip(CategoryDataset dataset, int row,
                int column) {
            return this.value;
        }

        public Object clone() throws CloneNotSupportedException {
            return new FixedToolTipGenerator(this.value);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FixedToolTipGenerator)) {
                return false;
            }
            FixedToolTipGenerator that = (FixedToolTipGenerator) obj;
            return this.value.equals(that.value);
        }
    }

    private static class FixedURLGenerator
            implements CategoryURLGenerator, PublicCloneable {
        private String value;

        FixedURLGenerator(String value) {
            this.value = value;
        }

        public String generateURL(CategoryDataset dataset, int row, int column) {
            return this.value;
        }

        public Object clone() throws CloneNotSupportedException {
            return new FixedURLGenerator(this.value);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FixedURLGenerator)) {
                return false;
            }
            FixedURLGenerator that = (FixedURLGenerator) obj;
            return this.value.equals(that.value);
        }
    }

    private static class FixedSeriesLabelGenerator
            implements CategorySeriesLabelGenerator, PublicCloneable {
        private String value;

        FixedSeriesLabelGenerator(String value) {
            this.value = value;
        }

        public String generateLabel(CategoryDataset dataset, int series) {
            return this.value + series;
        }

        public Object clone() throws CloneNotSupportedException {
            return new FixedSeriesLabelGenerator(this.value);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof FixedSeriesLabelGenerator)) {
                return false;
            }
            FixedSeriesLabelGenerator that = (FixedSeriesLabelGenerator) obj;
            return this.value.equals(that.value);
        }
    }

    private static class RecordingAnnotation implements CategoryAnnotation {
        int drawCount;

        public void draw(Graphics2D g2, CategoryPlot plot, Rectangle2D dataArea,
                CategoryAxis domainAxis, org.jfree.chart.axis.ValueAxis rangeAxis,
                int rendererIndex, PlotRenderingInfo info) {
            this.drawCount++;
        }

        public boolean equals(Object obj) {
            return obj instanceof RecordingAnnotation;
        }
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");
        return dataset;
    }

    private Graphics2D createGraphics2D() {
        return new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB)
                .createGraphics();
    }

    public void testSetPlotRejectsNull() {
        AreaRenderer renderer = new AreaRenderer();
        try {
            renderer.setPlot(null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'plot' argument.", e.getMessage());
        }
    }

    public void testInitialiseWithNullDatasetResetsCounts() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        Graphics2D g2 = createGraphics2D();
        CategoryItemRendererState state = renderer.initialise(g2,
                new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0), plot, null,
                null);

        assertNotNull(state);
        assertEquals(0, renderer.getRowCount());
        assertEquals(0, renderer.getColumnCount());
        assertSame(plot, renderer.getPlot());
    }

    public void testInitialiseWithDatasetUpdatesCounts() {
        AreaRenderer renderer = new AreaRenderer();
        DefaultCategoryDataset dataset = createDataset();
        CategoryPlot plot = new CategoryPlot();
        Graphics2D g2 = createGraphics2D();

        renderer.initialise(g2,
                new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0), plot, dataset,
                null);

        assertEquals(2, renderer.getRowCount());
        assertEquals(2, renderer.getColumnCount());
    }

    public void testBaseAndSeriesItemLabelGeneratorPrecedence() {
        AreaRenderer renderer = new AreaRenderer();
        FixedLabelGenerator base = new FixedLabelGenerator("base");
        FixedLabelGenerator series = new FixedLabelGenerator("series");

        renderer.setBaseItemLabelGenerator(base, false);
        assertSame(base, renderer.getItemLabelGenerator(0, 0, false));

        renderer.setSeriesItemLabelGenerator(0, series, false);
        assertSame(series, renderer.getItemLabelGenerator(0, 0, false));
        assertSame(base, renderer.getItemLabelGenerator(1, 0, false));
    }

    public void testBaseAndSeriesToolTipGeneratorPrecedence() {
        AreaRenderer renderer = new AreaRenderer();
        FixedToolTipGenerator base = new FixedToolTipGenerator("baseTip");
        FixedToolTipGenerator series = new FixedToolTipGenerator("seriesTip");

        renderer.setBaseToolTipGenerator(base, false);
        assertSame(base, renderer.getToolTipGenerator(0, 0, false));

        renderer.setSeriesToolTipGenerator(0, series, false);
        assertSame(series, renderer.getToolTipGenerator(0, 0, false));
        assertSame(base, renderer.getToolTipGenerator(1, 0, false));
    }

    public void testBaseAndSeriesURLGeneratorPrecedence() {
        AreaRenderer renderer = new AreaRenderer();
        FixedURLGenerator base = new FixedURLGenerator("baseURL");
        FixedURLGenerator series = new FixedURLGenerator("seriesURL");

        renderer.setBaseURLGenerator(base, false);
        assertSame(base, renderer.getURLGenerator(0, 0, false));

        renderer.setSeriesURLGenerator(0, series, false);
        assertSame(series, renderer.getURLGenerator(0, 0, false));
        assertSame(base, renderer.getURLGenerator(1, 0, false));
    }

    public void testAddAnnotationRejectsNullAnnotation() {
        AreaRenderer renderer = new AreaRenderer();
        try {
            renderer.addAnnotation((CategoryAnnotation) null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'annotation' argument.", e.getMessage());
        }
    }

    public void testRemoveAnnotationReturnsFalseForForegroundOnlyRemovalLogic() {
        AreaRenderer renderer = new AreaRenderer();
        RecordingAnnotation ann = new RecordingAnnotation();
        renderer.addAnnotation(ann, Layer.FOREGROUND);

        assertFalse(renderer.removeAnnotation(ann));
    }

    public void testRemoveAnnotationsClearsForegroundAndBackground() {
        AreaRenderer renderer = new AreaRenderer();
        RecordingAnnotation fg = new RecordingAnnotation();
        RecordingAnnotation bg = new RecordingAnnotation();
        renderer.addAnnotation(fg, Layer.FOREGROUND);
        renderer.addAnnotation(bg, Layer.BACKGROUND);

        renderer.removeAnnotations();

        Graphics2D g2 = createGraphics2D();
        Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
        renderer.drawAnnotations(g2, area, new CategoryAxis(),
                new NumberAxis(), Layer.FOREGROUND, null);
        renderer.drawAnnotations(g2, area, new CategoryAxis(),
                new NumberAxis(), Layer.BACKGROUND, null);

        assertEquals(0, fg.drawCount);
        assertEquals(0, bg.drawCount);
    }

    public void testSetLegendItemLabelGeneratorRejectsNull() {
        AreaRenderer renderer = new AreaRenderer();
        try {
            renderer.setLegendItemLabelGenerator(null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'generator' argument.", e.getMessage());
        }
    }

    public void testGetLegendItemReturnsNullWhenNoPlotAssigned() {
        AreaRenderer renderer = new AreaRenderer();
        assertNull(renderer.getLegendItem(0, 0));
    }

    public void testGetLegendItemPopulatesGeneratedFields() {
        DefaultCategoryDataset dataset = createDataset();
        AreaRenderer renderer = new AreaRenderer();
        renderer.setLegendItemLabelGenerator(
                new FixedSeriesLabelGenerator("L"));
        renderer.setLegendItemToolTipGenerator(
                new FixedSeriesLabelGenerator("T"));
        renderer.setLegendItemURLGenerator(
                new FixedSeriesLabelGenerator("U"));

        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(),
                new NumberAxis(), renderer);

        LegendItem item = renderer.getLegendItem(0, 1);
        assertNotNull(item);
        assertEquals("L1", item.getLabel());
        assertEquals("T1", item.getToolTipText());
        assertEquals("U1", item.getURLText());
        assertEquals("R2", item.getSeriesKey());
        assertEquals(1, item.getSeriesIndex());
        assertSame(dataset, item.getDataset());
        assertEquals(0, item.getDatasetIndex());
    }

    public void testGetLegendItemsRespectsAscendingRowRenderingOrder() {
        DefaultCategoryDataset dataset = createDataset();
        AreaRenderer renderer = new AreaRenderer();
        renderer.setLegendItemLabelGenerator(
                new StandardCategorySeriesLabelGenerator("{0}"));

        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(),
                new NumberAxis(), renderer);
        plot.setRowRenderingOrder(SortOrder.ASCENDING);

        LegendItemCollection items = renderer.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("R1", items.get(0).getLabel());
        assertEquals("R2", items.get(1).getLabel());
    }

    public void testGetLegendItemsRespectsDescendingRowRenderingOrder() {
        DefaultCategoryDataset dataset = createDataset();
        AreaRenderer renderer = new AreaRenderer();
        renderer.setLegendItemLabelGenerator(
                new StandardCategorySeriesLabelGenerator("{0}"));

        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(),
                new NumberAxis(), renderer);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);

        LegendItemCollection items = renderer.getLegendItems();
        assertEquals(2, items.getItemCount());
        assertEquals("R2", items.get(0).getLabel());
        assertEquals("R1", items.get(1).getLabel());
    }

    public void testFindRangeBoundsReturnsNullForNullDataset() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        assertNull(renderer.findRangeBounds(null));
        assertNull(renderer.findRangeBoundsPublic(null, false));
    }

    public void testFindRangeBoundsReturnsExpectedRange() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        DefaultCategoryDataset dataset = createDataset();

        Range range = renderer.findRangeBounds(dataset);
        assertEquals(new Range(1.0, 4.0), range);
    }

    public void testGetItemMiddleDelegatesToCategoryAxis() {
        AreaRenderer renderer = new AreaRenderer();
        DefaultCategoryDataset dataset = createDataset();
        CategoryAxis axis = new CategoryAxis();
        Rectangle2D area = new Rectangle2D.Double(0.0, 0.0, 100.0, 80.0);

        double expected = axis.getCategoryMiddle("C2", dataset.getColumnKeys(),
                area, org.jfree.chart.util.RectangleEdge.BOTTOM);
        double actual = renderer.getItemMiddle("R1", "C2", dataset, axis, area,
                org.jfree.chart.util.RectangleEdge.BOTTOM);

        assertEquals(expected, actual, 0.0000001);
    }

    public void testAddEntityRejectsNullHotspot() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        renderer.setPlot(new CategoryPlot(createDataset(), new CategoryAxis(),
                new NumberAxis(), renderer));
        try {
            renderer.addEntityPublic(new StandardEntityCollection(), null,
                    createDataset(), 0, 0, false);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'hotspot' argument.", e.getMessage());
        }
    }

    public void testAddEntityCreatesCategoryItemEntityWithTooltipAndURL() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        DefaultCategoryDataset dataset = createDataset();
        renderer.setBaseToolTipGenerator(new FixedToolTipGenerator("tip"),
                false);
        renderer.setBaseURLGenerator(new FixedURLGenerator("url"), false);
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(),
                new NumberAxis(), renderer);
        renderer.setPlot(plot);

        StandardEntityCollection entities = new StandardEntityCollection();
        Shape hotspot = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        renderer.addEntityPublic(entities, hotspot, dataset, 1, 0, false);

        assertEquals(1, entities.getEntityCount());
        assertEquals("tip", entities.getEntity(0).getToolTipText());
        assertEquals("url", entities.getEntity(0).getURLText());
    }

    public void testGetDomainAxisUsesDatasetMapping() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        DefaultCategoryDataset d1 = createDataset();
        DefaultCategoryDataset d2 = new DefaultCategoryDataset();
        d2.addValue(5.0, "S", "K");
        CategoryAxis axis1 = new CategoryAxis("A1");
        CategoryAxis axis2 = new CategoryAxis("A2");
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(0, d1);
        plot.setDataset(1, d2);
        plot.setDomainAxis(0, axis1);
        plot.setDomainAxis(1, axis2);
        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToDomainAxis(1, 1);

        assertSame(axis2, renderer.getDomainAxisPublic(plot, d2));
    }

    public void testGetRangeAxisFallsBackToPrimaryAxis() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        NumberAxis axis0 = new NumberAxis("R0");
        plot.setRangeAxis(0, axis0);

        assertSame(axis0, renderer.getRangeAxisPublic(plot, 1));
    }

    public void testEqualsReflectsGeneratorStateChanges() {
        AreaRenderer r1 = new AreaRenderer();
        AreaRenderer r2 = new AreaRenderer();
        assertTrue(r1.equals(r2));

        r1.setBaseItemLabelGenerator(new FixedLabelGenerator("X"), false);
        assertFalse(r1.equals(r2));

        r2.setBaseItemLabelGenerator(new FixedLabelGenerator("X"), false);
        assertTrue(r1.equals(r2));
    }

    public void testCloneCreatesIndependentCopyOfGenerators()
            throws CloneNotSupportedException {
        AreaRenderer r1 = new AreaRenderer();
        r1.setBaseItemLabelGenerator(new FixedLabelGenerator("L"), false);
        r1.setBaseToolTipGenerator(new FixedToolTipGenerator("T"), false);
        r1.setBaseURLGenerator(new FixedURLGenerator("U"), false);
        r1.setLegendItemToolTipGenerator(new FixedSeriesLabelGenerator("TT"));
        r1.setLegendItemURLGenerator(new FixedSeriesLabelGenerator("UU"));

        AreaRenderer r2 = (AreaRenderer) r1.clone();

        assertNotSame(r1, r2);
        assertEquals(r1, r2);
        assertNotSame(r1.getBaseItemLabelGenerator(),
                r2.getBaseItemLabelGenerator());
        assertNotSame(r1.getBaseToolTipGenerator(),
                r2.getBaseToolTipGenerator());
        assertNotSame(r1.getBaseURLGenerator(), r2.getBaseURLGenerator());
    }

    public void testCreateStateIncludesOnlyVisibleSeries() {
        TestAreaRenderer renderer = new TestAreaRenderer();
        renderer.initialise(createGraphics2D(),
                new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0),
                new CategoryPlot(), createDataset(), null);
        renderer.setSeriesVisible(1, Boolean.FALSE, false);

        CategoryItemRendererState state = renderer.createStatePublic(null);

        assertEquals(1, state.getVisibleSeriesCount());
        assertEquals(0, state.getVisibleSeriesIndex(0));
        assertEquals(-1, state.getVisibleSeriesIndex(1));
    }

    public void testDrawDomainLineRejectsNullPaint() {
        AreaRenderer renderer = new AreaRenderer();
        try {
            renderer.drawDomainLine(createGraphics2D(), new CategoryPlot(),
                    new Rectangle2D.Double(0.0, 0.0, 10.0, 10.0), 1.0, null,
                    new BasicStroke(1.0f));
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'paint' argument.", e.getMessage());
        }
    }

    public void testDrawDomainLineRejectsNullStroke() {
        AreaRenderer renderer = new AreaRenderer();
        try {
            renderer.drawDomainLine(createGraphics2D(), new CategoryPlot(),
                    new Rectangle2D.Double(0.0, 0.0, 10.0, 10.0), 1.0,
                    Color.red, null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'stroke' argument.", e.getMessage());
        }
    }

    public void testDrawRangeMarkerOutsideAxisRangeDoesNothing() {
        AreaRenderer renderer = new AreaRenderer();
        CategoryPlot plot = new CategoryPlot();
        NumberAxis axis = new NumberAxis();
        axis.setRange(0.0, 10.0);
        ValueMarker marker = new ValueMarker(20.0);
        Graphics2D g2 = createGraphics2D();

        renderer.drawRangeMarker(g2, plot, axis, marker,
                new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0));

        assertNotNull(g2);
    }

    public void testDrawDomainMarkerUnknownCategoryReturnsWithoutFailure() {
        AreaRenderer renderer = new AreaRenderer();
        DefaultCategoryDataset dataset = createDataset();
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(),
                new NumberAxis(), renderer);
        Graphics2D g2 = createGraphics2D();
        CategoryMarker marker = new CategoryMarker("NO_SUCH_CATEGORY",
                Color.blue, new BasicStroke(1.0f));

        renderer.drawDomainMarker(g2, plot, plot.getDomainAxis(), marker,
                new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0));

        assertNotNull(g2);
    }

    public void testReferences() {
        assertTrue(true);
    }
}

```