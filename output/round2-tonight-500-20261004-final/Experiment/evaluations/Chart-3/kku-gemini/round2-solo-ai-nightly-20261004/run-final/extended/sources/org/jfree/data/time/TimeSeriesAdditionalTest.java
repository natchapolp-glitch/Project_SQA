package org.jfree.data.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeSeriesAdditionalTest {

    @Test
    public void testAddOrUpdateUpdatesExistingPeriod() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        series.addOrUpdate(p1, 10.0);
        assertEquals(1, series.getItemCount());
        assertEquals(10.0, series.getValue(0).doubleValue(), 0.0000001);

        series.addOrUpdate(p1, 25.0);
        assertEquals(1, series.getItemCount());
        assertEquals(25.0, series.getValue(0).doubleValue(), 0.0000001);
        assertEquals(25.0, series.getMaxY(), 0.0000001);
    }

    @Test
    public void testRemoveAgedItems() {
        TimeSeries series = new TimeSeries("TestSeries");
        series.setMaximumItemAge(1L);
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(3, 1, 2020);

        series.add(p1, 10.0);
        series.add(p2, 20.0);
        
        assertEquals(1, series.getItemCount());
        assertEquals(p2, series.getTimePeriod(0));
    }

    @Test
    public void testUpdateByIndex() {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        series.add(p1, 10.0);
        assertEquals(10.0, series.getMinY(), 0.0000001);

        series.update(0, 50.0);
        assertEquals(50.0, series.getMaxY(), 0.0000001);
        assertEquals(50.0, series.getValue(0).doubleValue(), 0.0000001);
    }

    @Test
    public void testCreateCopyByIndexRange() throws CloneNotSupportedException {
        TimeSeries series = new TimeSeries("TestSeries");
        RegularTimePeriod p1 = new Day(1, 1, 2020);
        RegularTimePeriod p2 = new Day(2, 1, 2020);
        RegularTimePeriod p3 = new Day(3, 1, 2020);
        series.add(p1, 10.0);
        series.add(p2, 20.0);
        series.add(p3, 30.0);

        TimeSeries copy = series.createCopy(0, 1);
        assertNotNull(copy);
        assertEquals(2, copy.getItemCount());
        assertEquals(10.0, copy.getMinY(), 0.0000001);
        assertEquals(20.0, copy.getMaxY(), 0.0000001);
    }
}
