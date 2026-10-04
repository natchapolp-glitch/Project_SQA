package org.jfree.chart.renderer.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.junit.Test;

public class AbstractCategoryItemRendererTest {

    private static class ConcreteCategoryItemRenderer extends AbstractCategoryItemRenderer {
        private static final long serialVersionUID = 1L;
    }

    @Test
    public void testInitialStateAndPassCount() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        assertEquals(1, renderer.getPassCount());
        assertNull(renderer.getPlot());
        assertNotNull(renderer.getLegendItemLabelGenerator());
    }

    @Test
    public void testPlotAssociation() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        CategoryPlot plot = new CategoryPlot();
        renderer.setPlot(plot);
        assertSame(plot, renderer.getPlot());
        
        renderer.setPlot(null);
        assertNull(renderer.getPlot());
    }

    @Test
    public void testLegendItemLabelGenerator() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        CategorySeriesLabelGenerator generator = new StandardCategorySeriesLabelGenerator("Series: {0}");
        renderer.setLegendItemLabelGenerator(generator);
        assertSame(generator, renderer.getLegendItemLabelGenerator());
    }

    @Test
    public void testItemLabelGeneratorFallback() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        assertNull(renderer.getItemLabelGenerator(0, 0, true));
        assertNull(renderer.getSeriesItemLabelGenerator(0));
        assertNull(renderer.getBaseItemLabelGenerator());

        CategoryItemLabelGenerator baseGen = new StandardCategoryItemLabelGenerator();
        renderer.setBaseItemLabelGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseItemLabelGenerator());
        assertSame(baseGen, renderer.getItemLabelGenerator(0, 0, true));

        CategoryItemLabelGenerator seriesGen = new StandardCategoryItemLabelGenerator();
        renderer.setSeriesItemLabelGenerator(0, seriesGen);
        assertSame(seriesGen, renderer.getSeriesItemLabelGenerator(0));
        assertSame(seriesGen, renderer.getItemLabelGenerator(0, 0, true));
        assertSame(baseGen, renderer.getItemLabelGenerator(1, 0, true));
    }

    @Test
    public void testToolTipGeneratorFallback() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        assertNull(renderer.getToolTipGenerator(0, 0, true));

        CategoryToolTipGenerator baseGen = new StandardCategoryToolTipGenerator();
        renderer.setBaseToolTipGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseToolTipGenerator());
        assertSame(baseGen, renderer.getToolTipGenerator(0, 0, true));

        CategoryToolTipGenerator seriesGen = new StandardCategoryToolTipGenerator();
        renderer.setSeriesToolTipGenerator(0, seriesGen);
        assertSame(seriesGen, renderer.getSeriesToolTipGenerator(0));
        assertSame(seriesGen, renderer.getToolTipGenerator(0, 0, true));
    }

    @Test
    public void testURLGeneratorFallback() {
        AbstractCategoryItemRenderer renderer = new ConcreteCategoryItemRenderer();
        assertNull(renderer.getURLGenerator(0, 0, true));

        CategoryURLGenerator baseGen = new StandardCategoryURLGenerator();
        renderer.setBaseURLGenerator(baseGen);
        assertSame(baseGen, renderer.getBaseURLGenerator());
        assertSame(baseGen, renderer.getURLGenerator(0, 0, true));

        CategoryURLGenerator seriesGen = new StandardCategoryURLGenerator();
        renderer.setSeriesURLGenerator(0, seriesGen);
        assertSame(seriesGen, renderer.getSeriesURLGenerator(0));
        assertSame(seriesGen, renderer.getURLGenerator(0, 0, true));
    }
}
