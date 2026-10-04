package org.apache.commons.collections.map;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class Flat3MapExtraTest {

    @Test
    public void testPutDuplicateNullKeyAtSizeTwoUpdatesValue() {
        // Covers get/put branches for null key when size == 2 (key2 null check, drop-through)
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put(null, "firstNull");

        assertEquals(2, map.size());
        assertEquals("firstNull", map.get(null));

        Object old = map.put(null, "secondNull");
        assertEquals("firstNull", old);
        assertEquals(2, map.size());
        assertEquals("secondNull", map.get(null));
        assertEquals("1", map.get("a"));
    }

    @Test
    public void testPutAllTriggersConversionWhenCombinedSizeExceedsThree() {
        // putAll with map.size() >= 4 should convert to delegate mode directly
        Flat3Map map = new Flat3Map();
        Map source = new HashMap();
        source.put("a", "1");
        source.put("b", "2");
        source.put("c", "3");
        source.put("d", "4");

        map.putAll(source);

        assertEquals(4, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
        assertEquals("3", map.get("c"));
        assertEquals("4", map.get("d"));
    }

    @Test
    public void testKeySetValuesEntrySetSizesInFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        Set keys = map.keySet();
        Collection values = map.values();
        Set entries = map.entrySet();

        assertEquals(3, keys.size());
        assertEquals(3, values.size());
        assertEquals(3, entries.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
        assertTrue(keys.contains("c"));
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
        assertTrue(values.contains("3"));
    }

    @Test
    public void testRemoveMiddleEntryThenReaddReachesThreeAgain() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        Object removed = map.remove("b");
        assertEquals("2", removed);
        assertEquals(2, map.size());
        assertFalse(map.containsKey("b"));
        assertEquals("1", map.get("a"));
        assertEquals("3", map.get("c"));

        map.put("d", "4");
        assertEquals(3, map.size());
        assertEquals("4", map.get("d"));
        assertTrue(map.containsKey("d"));
    }
}
