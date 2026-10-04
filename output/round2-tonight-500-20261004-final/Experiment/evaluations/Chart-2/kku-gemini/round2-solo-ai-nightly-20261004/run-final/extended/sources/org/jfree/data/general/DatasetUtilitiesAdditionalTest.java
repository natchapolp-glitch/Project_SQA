package org.jfree.data.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.pie.PieDataset;
import org.junit.Test;

public class DatasetUtilitiesAdditionalTest {

    @Test
    public void testCreatePieDatasetForRowByIndex() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5.0, "Row0", "ColA");
        dataset.addValue(15.0, "Row0", "ColB");
        dataset.addValue(25.0, "Row1", "ColA");

        PieDataset pieDataset = DatasetUtilities.createPieDatasetForRow(dataset, 0);
        assertNotNull(pieDataset);
        assertEquals(2, pieDataset.getItemCount());
        assertEquals(5.0, pieDataset.getValue("ColA").doubleValue(), 0.0001);
        assertEquals(15.0, pieDataset.getValue("ColB").doubleValue(), 0.0001);
    }

    @Test
    public void testCreatePieDatasetForColumnByIndex() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(7.0, "RowA", "Col0");
        dataset.addValue(14.0, "RowB", "Col0");
        dataset.addValue(21.0, "RowA", "Col1");

        PieDataset pieDataset = DatasetUtilities.createPieDatasetForColumn(dataset, 0);
        assertNotNull(pieDataset);
        assertEquals(2, pieDataset.getItemCount());
        assertEquals(7.0, pieDataset.getValue("RowA").doubleValue(), 0.0001);
        assertEquals(14.0, pieDataset.getValue("RowB").doubleValue(), 0.0001);
    }

}
