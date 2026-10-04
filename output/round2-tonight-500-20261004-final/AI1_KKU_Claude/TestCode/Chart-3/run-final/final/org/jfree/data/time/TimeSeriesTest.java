package org.jfree.data.time;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimeSeriesTest {

    @Test
    public void addOrUpdate_updatesExistingPeriod_returnsOldItem_countUnchanged() {
        TimeSeries series = new TimeSeries("Test");
        Day day1 = new Day(1, 1, 2010);
        Day day2 = new Day(2, 1, 2010);
        series.add(day1, 10.0);
        series.add(day2, 20.0);

        TimeSeriesDataItem old = series.addOrUpdate(day1, 100.0);

        assertNotNull(old);
        assertEquals(10.0, old.getValue().doubleValue(), 0.0001);
        assertEquals(2, series.getItemCount());
        assertEquals(100.0, series.getValue(day1).doubleValue(), 0.0001);
    }

    @Test
    public void addOrUpdate_newPeriod_increasesCount_returnsNull() {
        TimeSeries series = new TimeSeries("Test");
        Day day1 = new Day(1, 1, 2010);
        series.add(day1, 10.0);

        Day day2 = new Day(2, 1, 2010);
        TimeSeriesDataItem old = series.addOrUpdate(day2, 20.0);

        assertNull(old);
        assertEquals(2, series.getItemCount());
        assertEquals(20.0, series.getValue(day2).doubleValue(), 0.0001);
    }

    @Test
    public void addOrUpdate_respectsMaximumItemCount() {
        TimeSeries series = new TimeSeries("Test");
        series.setMaximumItemCount(2);
        Day day1 = new Day(1, 1, 2010);
        Day day2 = new Day(2, 1, 2010);
        Day day3 = new Day(3, 1, 2010);

        series.addOrUpdate(day1, 10.0);
        series.addOrUpdate(day2, 20.0);
        series.addOrUpdate(day3, 30.0);

        assertEquals(2, series.getItemCount());
        assertNull(series.getDataItem(day1));
        assertEquals(20.0, series.getValue(day2).doubleValue(), 0.0001);
        assertEquals(30.0, series.getValue(day3).doubleValue(), 0.0001);
    }

    @Test
    public void addOrUpdate_zeroMaximumItemCount_doesNotAdd() {
        TimeSeries series = new TimeSeries("Test");
        series.setMaximumItemCount(0);
        Day day1 = new Day(1, 1, 2010);

        series.addOrUpdate(day1, 10.0);

        assertEquals(0, series.getItemCount());
    }

    @Test
    public void minYmaxY_updatedCorrectlyAfterAddOrUpdate() {
        TimeSeries series = new TimeSeries("Test");
        Day day1 = new Day(1, 1, 2010);
        Day day2 = new Day(2, 1, 2010);
        Day day3 = new Day(3, 1, 2010);
        series.add(day1, 5.0);
        series.add(day2, 10.0);
        series.add(day3, 1.0);

        assertEquals(1.0, series.getMinY(), 0.0001);
        assertEquals(10.0, series.getMaxY(), 0.0001);

        series.addOrUpdate(day2, -50.0);
        assertEquals(-50.0, series.getMinY(), 0.0001);

        series.addOrUpdate(day2, 1000.0);
        assertEquals(1000.0, series.getMaxY(), 0.0001);
    }

    @Test
    public void addOrUpdate_itemsRemainSortedWithoutDuplicates() {
        TimeSeries series = new TimeSeries("Test");
        Day day1 = new Day(1, 1, 2010);
        Day day2 = new Day(2, 1, 2010);
        Day day3 = new Day(3, 1, 2010);

        series.addOrUpdate(day2, 20.0);
        series.addOrUpdate(day1, 10.0);
        series.addOrUpdate(day3, 30.0);
        series.addOrUpdate(day2, 200.0);

        assertEquals(3, series.getItemCount());
        assertEquals(day1, series.getTimePeriod(0));
        assertEquals(day2, series.getTimePeriod(1));
        assertEquals(day3, series.getTimePeriod(2));
        assertEquals(200.0, series.getValue(day2).doubleValue(), 0.0001);
    }
}
