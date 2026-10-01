package org.apache.commons.collections.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;
import org.apache.commons.collections.MapIterator;

/**
 * Deterministic regression tests for {@link Flat3Map}.
 */
public class Flat3MapRegressionTest extends TestCase {

    public Flat3MapRegressionTest(String name) {
        super(name);
    }

    public void testEmptyMapBasicOperations() {
        Flat3Map map = new Flat3Map();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("nonExistent"));
        assertNull(map.get(null));
        assertFalse(map.containsKey("k1"));
        assertFalse(map.containsKey(null));
        assertFalse(map.containsValue("v1"));
        assertFalse(map.containsValue(null));
        assertEquals("{}", map.toString());
        assertEquals(0, map.hashCode());
    }

    public void testPutAndGetSizeOne() {
        Flat3Map map = new Flat3Map();
        assertNull(map.put("key1", "val1"));
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
        assertEquals("val1", map.get("key1"));
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsValue("val1"));
        assertFalse(map.containsKey("key2"));
        assertFalse(map.containsValue("val2"));
    }

    public void testPutAndGetSizeTwo() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        assertEquals(2, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
        assertTrue(map.containsKey("k1"));
        assertTrue(map.containsKey("k2"));
        assertTrue(map.containsValue("v1"));
        assertTrue(map.containsValue("v2"));
    }

    public void testPutAndGetSizeThree() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        assertEquals(3, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
        assertEquals("v3", map.get("k3"));
        assertTrue(map.containsKey("k3"));
        assertTrue(map.containsValue("v3"));
    }

    public void testPutOverwriteKeys() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        // Overwrite key 1
        Object old1 = map.put("k1", "v1_new");
        assertEquals("v1", old1);
        assertEquals("v1_new", map.get("k1"));
        assertEquals(3, map.size());

        // Overwrite key 2
        Object old2 = map.put("k2", "v2_new");
        assertEquals("v2", old2);
        assertEquals("v2_new", map.get("k2"));

        // Overwrite key 3
        Object old3 = map.put("k3", "v3_new");
        assertEquals("v3", old3);
        assertEquals("v3_new", map.get("k3"));
    }

    public void testPutAndGetWithNullKeyAndNullValue() {
        Flat3Map map = new Flat3Map();
        // size 1 with null key and null value
        assertNull(map.put(null, null));
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertTrue(map.containsValue(null));
        assertNull(map.get(null));

        // overwrite null key with non-null value
        assertEquals(null, map.put(null, "nullValue"));
        assertEquals("nullValue", map.get(null));

        // add a non-null key with null value
        map.put("k2", null);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("k2"));
        assertTrue(map.containsValue(null));
        assertNull(map.get("k2"));

        // add third entry
        map.put("k3", "v3");
        assertEquals(3, map.size());
        assertEquals("nullValue", map.get(null));
        assertNull(map.get("k2"));
        assertEquals("v3", map.get("k3"));
    }

    public void testTransitionToDelegateModeOnFourthPut() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        assertNull(map.put("k4", "v4"));

        assertEquals(4, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
        assertEquals("v3", map.get("k3"));
        assertEquals("v4", map.get("k4"));

        // Overwrite in delegate mode
        assertEquals("v4", map.put("k4", "v4_updated"));
        assertEquals("v4_updated", map.get("k4"));
    }

    public void testRemoveFromSizeOne() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        assertEquals("v1", map.remove("k1"));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("k1"));
        assertFalse(map.containsKey("k1"));
    }

    public void testRemoveFromSizeTwoShifting() {
        // Case 1: remove key1, key2 shifts into key1 slot
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");
        map1.put("k2", "v2");
        assertEquals("v1", map1.remove("k1"));
        assertEquals(1, map1.size());
        assertNull(map1.get("k1"));
        assertEquals("v2", map1.get("k2"));

        // Case 2: remove key2
        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put("k2", "v2");
        assertEquals("v2", map2.remove("k2"));
        assertEquals(1, map2.size());
        assertEquals("v1", map2.get("k1"));
        assertNull(map2.get("k2"));
    }

    public void testRemoveFromSizeThreeShifting() {
        // Remove key1 from size 3 (key3 moves to key1 slot)
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");
        map1.put("k2", "v2");
        map1.put("k3", "v3");
        assertEquals("v1", map1.remove("k1"));
        assertEquals(2, map1.size());
        assertEquals("v2", map1.get("k2"));
        assertEquals("v3", map1.get("k3"));
        assertNull(map1.get("k1"));

        // Remove key2 from size 3 (key3 moves to key2 slot)
        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put("k2", "v2");
        map2.put("k3", "v3");
        assertEquals("v2", map2.remove("k2"));
        assertEquals(2, map2.size());
        assertEquals("v1", map2.get("k1"));
        assertEquals("v3", map2.get("k3"));
        assertNull(map2.get("k2"));

        // Remove key3 from size 3
        Flat3Map map3 = new Flat3Map();
        map3.put("k1", "v1");
        map3.put("k2", "v2");
        map3.put("k3", "v3");
        assertEquals("v3", map3.remove("k3"));
        assertEquals(2, map3.size());
        assertEquals("v1", map3.get("k1"));
        assertEquals("v2", map3.get("k2"));
        assertNull(map3.get("k3"));
    }

    public void testRemoveNullKeyAtVariousPositions() {
        // Null at position 1 (size 3)
        Flat3Map map = new Flat3Map();
        map.put(null, "valNull");
        map.put("k2", "v2");
        map.put("k3", "v3");
        assertEquals("valNull", map.remove(null));
        assertEquals(2, map.size());
        assertFalse(map.containsKey(null));
        assertEquals("v2", map.get("k2"));
        assertEquals("v3", map.get("k3"));

        // Null at position 2 (size 2)
        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put(null, "valNull");
        assertEquals("valNull", map2.remove(null));
        assertEquals(1, map2.size());
        assertEquals("v1", map2.get("k1"));
        assertFalse(map2.containsKey(null));

        // Null at position 1 (size 1)
        Flat3Map map3 = new Flat3Map();
        map3.put(null, "valNull");
        assertEquals("valNull", map3.remove(null));
        assertEquals(0, map3.size());
        assertFalse(map3.containsKey(null));
    }

    public void testRemoveNonExistentKey() {
        Flat3Map map = new Flat3Map();
        assertNull(map.remove("absent"));
        map.put("k1", "v1");
        map.put("k2", "v2");
        assertNull(map.remove("k3"));
        assertNull(map.remove(null));
        assertEquals(2, map.size());
    }

    public void testClearFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("k1"));
        assertFalse(map.containsKey("k1"));
    }

    public void testClearDelegateModeSwitchesBackToFlat() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");
        assertEquals(4, map.size());

        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        // Re-adding 1 item should use flat mode
        map.put("new1", "val1");
        assertEquals(1, map.size());
        assertEquals("val1", map.get("new1"));
    }

    public void testPutAllSmallMap() {
        Map source = new HashMap();
        source.put("a", "1");
        source.put("b", "2");

        Flat3Map map = new Flat3Map();
        map.putAll(source);
        assertEquals(2, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
    }

    public void testPutAllLargeMapTriggersDelegation() {
        Map source = new HashMap();
        source.put("a", "1");
        source.put("b", "2");
        source.put("c", "3");
        source.put("d", "4");

        Flat3Map map = new Flat3Map();
        map.putAll(source);
        assertEquals(4, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
        assertEquals("3", map.get("c"));
        assertEquals("4", map.get("d"));
    }

    public void testConstructorWithMap() {
        Map source = new HashMap();
        source.put("x", "10");
        source.put("y", "20");

        Flat3Map map = new Flat3Map(source);
        assertEquals(2, map.size());
        assertEquals("10", map.get("x"));
        assertEquals("20", map.get("y"));
    }

    public void testConstructorWithNullMapThrowsNPE() {
        try {
            new Flat3Map(null);
            fail("Expected NullPointerException when initializing with null map");
        } catch (NullPointerException expected) {
            // Success
        }
    }

    public void testEqualsAndHashCodeFlatMode() {
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");
        map1.put("k2", "v2");

        Flat3Map map2 = new Flat3Map();
        map2.put("k2", "v2");
        map2.put("k1", "v1");

        assertTrue(map1.equals(map2));
        assertTrue(map2.equals(map1));
        assertEquals(map1.hashCode(), map2.hashCode());
        assertTrue(map1.equals(map1));

        Map standardMap = new HashMap();
        standardMap.put("k1", "v1");
        standardMap.put("k2", "v2");
        assertTrue(map1.equals(standardMap));
        assertEquals(standardMap.hashCode(), map1.hashCode());
    }

    public void testEqualsWithDifferentSizesAndNonMap() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");

        assertFalse(map.equals(null));
        assertFalse(map.equals("not a map"));

        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put("k2", "v2");
        assertFalse(map.equals(map2));

        Flat3Map map3 = new Flat3Map();
        map3.put("k1", "differentVal");
        assertFalse(map.equals(map3));
    }

    public void testCloneFlatMode() {
        Flat3Map original = new Flat3Map();
        original.put("k1", "v1");
        original.put("k2", "v2");

        Flat3Map cloned = (Flat3Map) original.clone();
        assertEquals(original, cloned);

        cloned.put("k3", "v3");
        assertEquals(2, original.size());
        assertEquals(3, cloned.size());
    }

    public void testCloneDelegateMode() {
        Flat3Map original = new Flat3Map();
        original.put("k1", "v1");
        original.put("k2", "v2");
        original.put("k3", "v3");
        original.put("k4", "v4");

        Flat3Map cloned = (Flat3Map) original.clone();
        assertEquals(4, cloned.size());
        assertEquals(original, cloned);

        cloned.remove("k1");
        assertEquals(4, original.size());
        assertEquals(3, cloned.size());
    }

    public void testSerializationFlatAndDelegate() throws Exception {
        // Flat mode serialization
        Flat3Map flat = new Flat3Map();
        flat.put("k1", "v1");
        flat.put("k2", "v2");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(flat);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Flat3Map flatDeser = (Flat3Map) ois.readObject();
        assertEquals(flat, flatDeser);

        // Delegate mode serialization (> 3 items)
        Flat3Map delegate = new Flat3Map();
        delegate.put("k1", "v1");
        delegate.put("k2", "v2");
        delegate.put("k3", "v3");
        delegate.put("k4", "v4");

        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(delegate);
        oos.close();

        ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Flat3Map delegateDeser = (Flat3Map) ois.readObject();
        assertEquals(delegate, delegateDeser);
    }

    public void testMapIteratorBasicAndSetValue() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        MapIterator it = map.mapIterator();
        try {
            it.getKey();
            fail("Expected IllegalStateException before next()");
        } catch (IllegalStateException expected) {
            // expected
        }

        int count = 0;
        while (it.hasNext()) {
            Object key = it.next();
            assertNotNull(key);
            assertEquals(key, it.getKey());
            if ("k1".equals(key)) {
                assertEquals("v1", it.getValue());
                it.setValue("v1_mod");
            }
            count++;
        }
        assertEquals(2, count);
        assertEquals("v1_mod", map.get("k1"));

        try {
            it.next();
            fail("Expected NoSuchElementException after exhausting iterator");
        } catch (NoSuchElementException expected) {
            // expected
        }
    }

    public void testMapIteratorRemoveAndReset() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        MapIterator it = map.mapIterator();
        it.next();
        it.remove();
        assertEquals(1, map.size());

        try {
            it.remove();
            fail("Cannot call remove() twice consecutively");
        } catch (IllegalStateException expected) {
            // expected
        }

        ((org.apache.commons.collections.ResettableIterator) it).reset();
        assertTrue(it.hasNext());
        Object remainingKey = it.next();
        assertEquals(remainingKey, it.getKey());
    }

    public void testKeySetAndValuesViews() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put("b", "2");

        Set keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
        assertTrue(keys.remove("a"));
        assertFalse(map.containsKey("a"));
        assertEquals(1, map.size());

        Collection values = map.values();
        assertEquals(1, values.size());
        assertTrue(values.contains("2"));
    }

    public void testEntrySetViewAndEntryContract() {
        Flat3Map map = new Flat3Map();
        map.put("x", "100");

        Set entries = map.entrySet();
        assertEquals(1, entries.size());
        Iterator it = entries.iterator();
        assertTrue(it.hasNext());
        Map.Entry entry = (Map.Entry) it.next();

        assertEquals("x", entry.getKey());
        assertEquals("100", entry.getValue());
        entry.setValue("200");
        assertEquals("200", map.get("x"));

        Map.Entry dummyEntry = (Map.Entry) new HashMap().entrySet().iterator().hasNext() ? null : null;
        assertFalse(entry.equals("not an entry"));

        it.remove();
        assertEquals(0, map.size());
    }

    public void testToStringFormat() {
        Flat3Map map = new Flat3Map();
        assertEquals("{}", map.toString());

        map.put("k1", "v1");
        assertEquals("{k1=v1}", map.toString());

        map.put("k2", "v2");
        assertEquals("{k2=v2,k1=v1}", map.toString());

        map.put("k3", "v3");
        assertEquals("{k3=v3,k2=v2,k1=v1}", map.toString());
    }

    public void testDelegateModeOperations() {
        Flat3Map map = new Flat3Map();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");
        map.put("4", "four");

        // Verify operations in delegate mode
        assertTrue(map.containsKey("3"));
        assertTrue(map.containsValue("four"));
        assertEquals("three", map.remove("3"));
        assertEquals(3, map.size());
        assertFalse(map.containsKey("3"));
        assertEquals("one", map.get("1"));
        assertEquals("two", map.get("2"));
        assertEquals("four", map.get("4"));
    }
}
