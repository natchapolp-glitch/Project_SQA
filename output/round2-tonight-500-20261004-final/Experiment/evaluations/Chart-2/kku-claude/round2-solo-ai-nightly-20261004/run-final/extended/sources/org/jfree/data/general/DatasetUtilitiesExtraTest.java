package org.jfree.data.general;

import static org.junit.Assert.assertEquals;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.pie.PieDataset;
import org.junit.Test;

public class DatasetUtilitiesExtraTest {

    @Test
    public void testCreatePieDatasetForRowByKey() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R2", "C1");
        dataset.addValue(4.0, "R2", "C2");

        PieDataset pie = DatasetUtilities.createPieDatasetForRow(
                (CategoryDataset) dataset, "R1");
        assertEquals(1.0, pie.getValue("C1").doubleValue(), 0.0000001);
        assertEquals(2.0, pie.getValue("C2").doubleValue(), 0.0000001);
    }

    @Test
    public void testCreatePieDatasetForRowByIndex() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "R1", "C1");
        dataset.addValue(20.0, "R1", "C2");

        PieDataset pie = DatasetUtilities.createPieDatasetForRow(
                (CategoryDataset) dataset, 0);
        assertEquals(10.0, pie.getValue("C1").doubleValue(), 0.0000001);
        assertEquals(20.0, pie.getValue("C2").doubleValue(), 0.0000001);
    }

    @Test
    public void testCreatePieDatasetForColumnByKey() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5.0, "R1", "C1");
        dataset.addValue(6.0, "R2", "C1");

        PieDataset pie = DatasetUtilities.createPieDatasetForColumn(
                (CategoryDataset) dataset, "C1");
        assertEquals(5.0, pie.getValue("R1").doubleValue(), 0.0000001);
        assertEquals(6.0, pie.getValue("R2").doubleValue(), 0.0000001);
    }

    @Test
    public void testCreatePieDatasetForColumnByIndex() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(7.0, "R1", "C1");
        dataset.addValue(8.0, "R2", "C1");

        PieDataset pie = DatasetUtilities.createPieDatasetForColumn(
                (CategoryDataset) dataset, 0);
        assertEquals(7.0, pie.getValue("R1").doubleValue(), 0.0000001);
        assertEquals(8.0, pie.getValue("R2").doubleValue(), 0.0000001);
    }
}
