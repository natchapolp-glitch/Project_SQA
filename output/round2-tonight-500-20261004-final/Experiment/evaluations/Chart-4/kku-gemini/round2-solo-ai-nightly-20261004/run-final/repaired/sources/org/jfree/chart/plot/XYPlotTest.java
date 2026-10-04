package org.jfree.chart.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.junit.Test;

public class XYPlotTest {

    @Test
    public void testDefaultConstructor() {
        XYPlot plot = new XYPlot();
        assertNotNull(plot.getPlotType());
        assertEquals(PlotOrientation.VERTICAL, plot.getOrientation());
    }

    @Test
    public void testParameterizedConstructor() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        NumberAxis domainAxis = new NumberAxis("X");
        NumberAxis rangeAxis = new NumberAxis("Y");
        XYItemRenderer renderer = new StandardXYItemRenderer();

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        assertEquals(dataset, plot.getDataset());
        assertEquals(domainAxis, plot.getDomainAxis());
        assertEquals(rangeAxis, plot.getRangeAxis());
        assertEquals(renderer, plot.getRenderer());
    }

    @Test
    public void testAxisManagement() {
        XYPlot plot = new XYPlot();
        NumberAxis extraDomainAxis = new NumberAxis("Extra X");
        plot.setDomainAxis(1, extraDomainAxis);
        assertEquals(extraDomainAxis, plot.getDomainAxis(1));
        assertEquals(2, plot.getDomainAxisCount());

        NumberAxis extraRangeAxis = new NumberAxis("Extra Y");
        plot.setRangeAxis(1, extraRangeAxis);
        assertEquals(extraRangeAxis, plot.getRangeAxis(1));
        assertEquals(2, plot.getRangeAxisCount());
    }

    @Test
    public void testClearAxes() {
        XYPlot plot = new XYPlot();
        plot.setDomainAxis(1, new NumberAxis("X2"));
        plot.clearDomainAxes();
        assertEquals(0, plot.getDomainAxisCount());
        assertNull(plot.getDomainAxis(0));

        plot.setRangeAxis(1, new NumberAxis("Y2"));
        plot.clearRangeAxes();
        assertEquals(0, plot.getRangeAxisCount());
        assertNull(plot.getRangeAxis(0));
    }

    @Test
    public void testOrientationAndOffset() {
        XYPlot plot = new XYPlot();
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        assertEquals(PlotOrientation.HORIZONTAL, plot.getOrientation());

        assertNotNull(plot.getAxisOffset());
    }
}
