package org.apache.commons.collections.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class Flat3MapAdditionalTest {

    @Test
    public void testEqualsAndHashCode() {
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");

        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");

        assertEquals(map1, map2);
        assertEquals(map1.hashCode(), map2.hashCode());

        Map<String, String> normalMap = new HashMap<String, String>();
        normalMap.put("k1", "v1");
        assertTrue(map1.equals(normalMap));
        assertFalse(map1.equals(null));
        assertFalse(map1.equals("string"));
    }

    private void assertFalse(boolean condition) {
        org.junit.Assert.assertFalse(condition);
    }

    @Test
    public void testToString() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        String str = map.toString();
        assertNotNull(str);
        assertTrue(str.contains("a=1"));

        // Test in delegate mode
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
        String delegateStr = map.toString();
        assertNotNull(delegateStr);
        assertTrue(delegateStr.contains("a=1"));
    }

    @Test
    public void testRemoveVariationsFlatAndDelegate() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        
        // Remove non-existent key
        assertNull(map.remove("nonexistent"));

        // Remove from size 3 flat mode
        assertEquals("b", map.remove("b"));
        assertEquals(2, map.size());
        assertNull(map.get("b"));

        // Push into delegate mode
        map.put("d", "4");
        map.put("e", "5");
        assertEquals(4, map.size());

        // Remove from delegate mode
        assertEquals("4", map.remove("d"));
        assertEquals(3, map.size());
        assertNull(map.remove("nonexistent-delegate"));
    }

    @Test
    public void testPutAllLargeMap() {
        Map<String, String> largeMap = new HashMap<String, String>();
        for (int i = 0; i < 5; i++) {
            largeMap.put("key" + i, "val" + i);
        }

        Flat3Map map = new Flat3Map();
        map.putAll(largeMap);
        assertEquals(5, map.size());
        assertEquals("val0", map.get("key0"));
        assertEquals("val4", map.get("key4"));
    }
}
