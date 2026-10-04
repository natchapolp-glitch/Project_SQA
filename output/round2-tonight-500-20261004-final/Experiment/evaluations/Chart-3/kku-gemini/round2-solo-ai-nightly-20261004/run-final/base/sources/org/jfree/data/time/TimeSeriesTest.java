package org.jfree.data.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeSeriesTest {

    @Test
    public void testInitialMinMaxY() {
        TimeSeries series = new TimeSeries("TestSeries");
        assertEquals(Double.NaN, series.getMinY(), 0.0000001);
        assertEquals(Double.NaN, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testMinMaxYWithPositiveAndNegativeValues() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        RegularTimePeriod p3 = new Day(3, 1, 2020);

        series.add(p1, -5.5);
        series.add(p2, 10.0);
        series.add(p3, 2.5);

        assertEquals(-5.5, series.getMinY(), 0.0000001);
        assertEquals(10.0, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testMinMaxYWithNaNValues() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        RegularTimePeriod p3 = new Day(3, 1, 2020);

        series.add(p1, Double.NaN);
        series.add(p2, 3.0);
        series.add(p3, Double.NaN);

        assertEquals(3.0, series.getMinY(), 0.0000001);
        assertEquals(3.0, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testUpdateRecalculatesBounds() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        series.add(p1, 5.0);

        assertEquals(5.0, series.getMinY(), 0.0000001);
        assertEquals(5.0, series.getMaxY(), 0.0000001);

        series.update(p1, -10.0);

        assertEquals(-10.0, series.getMinY(), 0.0000001);
        assertEquals(-10.0, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testClearResetsBounds() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        series.add(p1, 42.0);

        assertEquals(42.0, series.getMinY(), 0.0000001);
        assertEquals(42.0, series.getMaxY(), 0.0000001);

        series.clear();

        assertEquals(Double.NaN, series.getMinY(), 0.0000001);
        assertEquals(Double.NaN, series.getMaxY(), 0.0000001);
        assertEquals(0, series.getItemCount());
    }

    @Test
    public void testDeleteRecalculatesBounds() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        series.add(p1, -10.0);
        series.add(p2, 20.0);

        assertEquals(-10.0, series.getMinY(), 0.0000001);
        assertEquals(20.0, series.getMaxY(), 0.0000001);

        series.delete(p1);

        assertEquals(20.0, series.getMinY(), 0.0000001);
        assertEquals(20.0, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testCreateCopyPreservesDataAndBounds() throws CloneNotSupportedException {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        series.add(p1, 1.0);
        series.add(p2, 5.0);

        TimeSeries copy = series.createCopy(p1, p2);
        assertNotNull(copy);
        assertEquals(2, copy.getItemCount());
        assertEquals(1.0, copy.getMinY(), 0.0000001);
        assertEquals(5.0, copy.getMaxY(), 0.0000001);
    }

    @Test
    public void testMaximumItemCountRemovesOldest() {
        TimeSeries series = new TimeSeries("TestSeries");
        series.setMaximumItemCount(2);
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        RegularTimePeriod p3 = new Day(3, 1, 2020);

        series.add(p1, 10.0);
        series.add(p2, 20.0);
        series.add(p3, 30.0);

        assertEquals(2, series.getItemCount());
        assertEquals(p2, series.getTimePeriod(0));
        assertEquals(p3, series.getTimePeriod(1));
        assertEquals(20.0, series.getMinY(), 0.0000001);
        assertEquals(30.0, series.getMaxY(), 0.0000001);
    }
}
