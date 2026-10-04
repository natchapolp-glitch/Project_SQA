package org.apache.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsExtraTest {

    @Test
    public void testDisjunctionWithEmptyCollection() {
        List<String> a = new ArrayList<String>();
        List<String> b = Arrays.asList("x", "y", "y");

        Collection disjunction = CollectionUtils.disjunction(a, b);
        assertEquals(1, CollectionUtils.cardinality("x", disjunction));
        assertEquals(2, CollectionUtils.cardinality("y", disjunction));
        assertEquals(3, disjunction.size());
    }

    @Test
    public void testUnionWithNullElements() {
        List<String> a = new ArrayList<String>();
        a.add("a");
        a.add(null);
        List<String> b = new ArrayList<String>();
        b.add(null);
        b.add("b");

        Collection union = CollectionUtils.union(a, b);
        assertEquals(1, CollectionUtils.cardinality("a", union));
        assertEquals(1, CollectionUtils.cardinality("b", union));
        assertEquals(1, CollectionUtils.cardinality(null, union));
        assertEquals(3, union.size());
    }

    @Test
    public void testIsEqualCollectionDifferentElementsSameSize() {
        List<String> a = Arrays.asList("a", "b");
        List<String> b = Arrays.asList("c", "d");
        assertFalse(CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsProperSubCollectionEqualSizeIsFalse() {
        List<String> a = Arrays.asList("a", "b");
        List<String> b = Arrays.asList("a", "b");
        assertFalse(CollectionUtils.isProperSubCollection(a, b));
        assertTrue(CollectionUtils.isSubCollection(a, b));
        assertTrue(CollectionUtils.isEqualCollection(a, b));
    }
}
