package org.jfree.chart.plot;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.junit.Test;

public class XYPlotExtraTest {

    @Test
    public void testSetDomainAxisAtIndexZeroViaArray() {
        XYPlot plot = new XYPlot();
        ValueAxis axis0 = new NumberAxis("X0");
        ValueAxis axis1 = new NumberAxis("X1");
        plot.setDomainAxes(new ValueAxis[] { axis0, axis1 });
        assertSame(axis0, plot.getDomainAxis(0));
        assertSame(axis1, plot.getDomainAxis(1));
        assertSame(axis0, plot.getDomainAxis());
    }

    @Test
    public void testSetRangeAxisAtIndexZeroViaArray() {
        XYPlot plot = new XYPlot();
        ValueAxis axis0 = new NumberAxis("Y0");
        ValueAxis axis1 = new NumberAxis("Y1");
        plot.setRangeAxes(new ValueAxis[] { axis0, axis1 });
        assertSame(axis0, plot.getRangeAxis(0));
        assertSame(axis1, plot.getRangeAxis(1));
        assertSame(axis0, plot.getRangeAxis());
    }

    @Test
    public void testReplaceSecondaryRangeAxis() {
        XYPlot plot = new XYPlot();
        ValueAxis original = new NumberAxis("OriginalY");
        ValueAxis replacement = new NumberAxis("ReplacementY");
        plot.setRangeAxis(1, original);
        assertSame(original, plot.getRangeAxis(1));
        plot.setRangeAxis(1, replacement);
        assertSame(replacement, plot.getRangeAxis(1));
    }

    @Test
    public void testGetRangeAxisForUnsetIndexReturnsNull() {
        XYPlot plot = new XYPlot();
        assertNull(plot.getRangeAxis(7));
        plot.setRangeAxis(0, new NumberAxis("Y"));
        assertNotNull(plot.getRangeAxis(0));
    }
}
