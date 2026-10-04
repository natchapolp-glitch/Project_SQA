package org.jfree.chart.renderer.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

public class AbstractCategoryItemRendererTest {

    private AbstractCategoryItemRenderer createRenderer() {
        return new LineAndShapeRenderer();
    }

    @Test
    public void findRangeBounds_nullDataset_returnsNull() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        Range range = renderer.findRangeBounds((CategoryDataset) null);
        assertNull(range);
    }

    @Test
    public void findRangeBounds_emptyDataset_returnsNull() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Range range = renderer.findRangeBounds(dataset);
        assertNull(range);
    }

    @Test
    public void findRangeBounds_singleValue_returnsEqualBounds() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5.0, "Row1", "Col1");
        Range range = renderer.findRangeBounds(dataset);
        assertEquals(5.0, range.getLowerBound(), 0.0001);
        assertEquals(5.0, range.getUpperBound(), 0.0001);
    }

    @Test
    public void findRangeBounds_multipleValues_returnsMinMax() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Row1", "Col1");
        dataset.addValue(10.0, "Row1", "Col2");
        dataset.addValue(-3.0, "Row2", "Col1");
        dataset.addValue(7.0, "Row2", "Col2");
        Range range = renderer.findRangeBounds(dataset);
        assertEquals(-3.0, range.getLowerBound(), 0.0001);
        assertEquals(10.0, range.getUpperBound(), 0.0001);
    }

    @Test
    public void findRangeBounds_nullValuesIgnored() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(2.0, "Row1", "Col1");
        dataset.addValue((Number) null, "Row1", "Col2");
        dataset.addValue(4.0, "Row2", "Col1");
        Range range = renderer.findRangeBounds(dataset);
        assertEquals(2.0, range.getLowerBound(), 0.0001);
        assertEquals(4.0, range.getUpperBound(), 0.0001);
    }

    @Test
    public void findRangeBounds_allNullValues_returnsNull() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue((Number) null, "Row1", "Col1");
        dataset.addValue((Number) null, "Row2", "Col1");
        Range range = renderer.findRangeBounds(dataset);
        assertNull(range);
    }

    @Test
    public void findRangeBounds_negativeOnlyValues_correctBounds() {
        AbstractCategoryItemRenderer renderer = createRenderer();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(-5.0, "Row1", "Col1");
        dataset.addValue(-1.0, "Row1", "Col2");
        dataset.addValue(-10.0, "Row2", "Col1");
        Range range = renderer.findRangeBounds(dataset);
        assertEquals(-10.0, range.getLowerBound(), 0.0001);
        assertEquals(-1.0, range.getUpperBound(), 0.0001);
    }
}
