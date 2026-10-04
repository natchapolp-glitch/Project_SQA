package org.apache.commons.collections.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class MultiValueMapTest {

    @Test
    public void testPutAndGetCollection() {
        MultiValueMap map = new MultiValueMap();
        assertEquals("val1", map.put("key1", "val1"));
        assertNull(map.put("key1", "val2"));

        Collection<?> col = map.getCollection("key1");
        assertNotNull(col);
        assertEquals(2, col.size());
        assertTrue(col.contains("val1"));
        assertTrue(col.contains("val2"));
    }

    @Test
    public void testRemoveMapping() {
        MultiValueMap map = new MultiValueMap();
        map.put("key1", "val1");
        map.put("key1", "val2");

        Object removed = map.removeMapping("key1", "val1");
        assertEquals("val1", removed);
        assertEquals(1, map.size("key1"));
        assertNull(map.removeMapping("key1", "nonexistent"));
        assertNull(map.removeMapping("nonexistentKey", "val2"));
    }

    @Test
    public void testContainsValue() {
        MultiValueMap map = new MultiValueMap();
        map.put("key1", "val1");
        map.put("key2", "val2");

        assertTrue(map.containsValue("val1"));
        assertTrue(map.containsValue("val2"));
        assertFalse(map.containsValue("val3"));

        assertTrue(map.containsValue("key1", "val1"));
        assertFalse(map.containsValue("key1", "val2"));
        assertFalse(map.containsValue("nonexistent", "val1"));
    }

    @Test
    public void testPutAllCollection() {
        MultiValueMap map = new MultiValueMap();
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");

        boolean changed = map.putAll("key1", list);
        assertTrue(changed);
        assertEquals(2, map.size("key1"));

        boolean noChange = map.putAll("key1", null);
        assertFalse(noChange);
    }

    @Test
    public void testDecorateFactory() {
        MultiValueMap map = MultiValueMap.decorate(new HashMap<Object, Object>(), ArrayList.class);
        assertNotNull(map);
        map.put("k", "v");
        assertEquals(1, map.size("k"));
    }

    @Test
    public void testIterator() {
        MultiValueMap map = new MultiValueMap();
        map.put("key1", "val1");
        map.put("key1", "val2");

        Iterator<?> it = map.iterator("key1");
        assertNotNull(it);
        assertTrue(it.hasNext());

        Iterator<?> emptyIt = map.iterator("nonexistent");
        assertNotNull(emptyIt);
        assertFalse(emptyIt.hasNext());
    }

    @Test
    public void testClearAndSize() {
        MultiValueMap map = new MultiValueMap();
        map.put("key1", "val1");
        map.put("key2", "val2");

        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertEquals(0, map.size("key1"));
    }
}
