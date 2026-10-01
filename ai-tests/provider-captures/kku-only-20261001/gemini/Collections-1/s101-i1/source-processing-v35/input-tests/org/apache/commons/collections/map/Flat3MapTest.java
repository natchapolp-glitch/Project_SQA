package org.apache.commons.collections.map;

import junit.framework.TestCase;
import org.apache.commons.collections.MapIterator;

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

/**
 * Unit tests for {@link Flat3Map}.
 */
public class Flat3MapTest extends TestCase {

    public Flat3MapTest(String testName) {
        super(testName);
    }

    public void testEmptyMap() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("a"));
        assertNull(map.get(null));
        assertFalse(map.containsKey("a"));
        assertFalse(map.containsKey(null));
        assertFalse(map.containsValue("v"));
        assertFalse(map.containsValue(null));
        assertNull(map.remove("a"));
        assertNull(map.remove(null));
        assertEquals("{}", map.toString());
        assertEquals(0, map.hashCode());

        MapIterator it = map.mapIterator();
        assertFalse(it.hasNext());
    }

    public void testPutAndGetUpToThreeEntries() {
        Flat3Map map = new Flat3Map();
        assertNull(map.put("key1", "val1"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertEquals("val1", map.get("key1"));
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsValue("val1"));

        assertNull(map.put("key2", "val2"));
        assertEquals(2, map.size());
        assertEquals("val2", map.get("key2"));
        assertTrue(map.containsKey("key2"));
        assertTrue(map.containsValue("val2"));

        assertNull(map.put("key3", "val3"));
        assertEquals(3, map.size());
        assertEquals("val3", map.get("key3"));
        assertTrue(map.containsKey("key3"));
        assertTrue(map.containsValue("val3"));
    }

    public void testPutOverwriteExistingKeys() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        assertEquals("v1", map.put("k1", "new1"));
        assertEquals("new1", map.get("k1"));
        assertEquals(3, map.size());

        assertEquals("v2", map.put("k2", "new2"));
        assertEquals("new2", map.get("k2"));
        assertEquals(3, map.size());

        assertEquals("v3", map.put("k3", "new3"));
        assertEquals("new3", map.get("k3"));
        assertEquals(3, map.size());
    }

    public void testNullKeysAndValuesInFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put(null, "nullVal");
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertEquals("nullVal", map.get(null));
        assertTrue(map.containsValue("nullVal"));

        assertEquals("nullVal", map.put(null, "newNullVal"));
        assertEquals("newNullVal", map.get(null));

        map.put("k2", null);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("k2"));
        assertTrue(map.containsValue(null));
        assertNull(map.get("k2"));

        map.put("k3", "v3");
        assertEquals(3, map.size());
        assertEquals("newNullVal", map.get(null));
        assertNull(map.get("k2"));
        assertEquals("v3", map.get("k3"));
    }

    public void testSwitchToDelegateMode() {
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

        assertTrue(map.containsKey("k1"));
        assertTrue(map.containsKey("k4"));
        assertTrue(map.containsValue("v1"));
        assertTrue(map.containsValue("v4"));

        assertEquals("v4", map.put("k4", "new4"));
        assertEquals("new4", map.get("k4"));
        assertEquals(4, map.size());
    }

    public void testRemoveFromFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        // Remove middle element
        assertEquals("v2", map.remove("k2"));
        assertEquals(2, map.size());
        assertFalse(map.containsKey("k2"));
        assertTrue(map.containsKey("k1"));
        assertTrue(map.containsKey("k3"));
        assertEquals("v1", map.get("k1"));
        assertEquals("v3", map.get("k3"));

        // Remove last element (key3 was shifted to key2)
        assertEquals("v3", map.remove("k3"));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("k3"));
        assertTrue(map.containsKey("k1"));
        assertEquals("v1", map.get("k1"));

        // Remove remaining element
        assertEquals("v1", map.remove("k1"));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.remove("k1"));
    }

    public void testRemoveNullKey() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put(null, "nullVal");
        map.put("k3", "v3");

        assertTrue(map.containsKey(null));
        assertEquals("nullVal", map.remove(null));
        assertEquals(2, map.size());
        assertFalse(map.containsKey(null));
        assertEquals("v1", map.get("k1"));
        assertEquals("v3", map.get("k3"));

        map.clear();
        map.put(null, "onlyNull");
        assertEquals("onlyNull", map.remove(null));
        assertEquals(0, map.size());
    }

    public void testRemoveNonExistent() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        assertNull(map.remove("k3"));
        assertNull(map.remove(null));
        assertEquals(2, map.size());
    }

    public void testClearSwitchesBackFromDelegateMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");
        assertEquals(4, map.size());

        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        // Re-inserting up to 3 stays in flat mode
        map.put("a", "b");
        assertEquals(1, map.size());
        assertEquals("b", map.get("a"));
    }

    public void testPutAllSmallMap() {
        Flat3Map map = new Flat3Map();
        Map source = new HashMap();
        source.put("k1", "v1");
        source.put("k2", "v2");

        map.putAll(source);
        assertEquals(2, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
    }

    public void testPutAllLargeMap() {
        Flat3Map map = new Flat3Map();
        Map source = new HashMap();
        source.put("k1", "v1");
        source.put("k2", "v2");
        source.put("k3", "v3");
        source.put("k4", "v4");

        map.putAll(source);
        assertEquals(4, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v4", map.get("k4"));
    }

    public void testPutAllEmptyMap() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.putAll(new HashMap());
        assertEquals(1, map.size());
    }

    public void testConstructorWithMap() {
        Map source = new HashMap();
        source.put("k1", "v1");
        source.put("k2", "v2");

        Flat3Map map = new Flat3Map(source);
        assertEquals(2, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));

        try {
            new Flat3Map(null);
            fail("Expected NullPointerException on null map argument");
        } catch (NullPointerException e) {
            // expected
        }
    }

    public void testMapIteratorFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        MapIterator it = map.mapIterator();
        assertTrue(it.hasNext());
        Object key1 = it.next();
        assertEquals("k1", key1);
        assertEquals("k1", it.getKey());
        assertEquals("v1", it.getValue());
        assertEquals("v1", it.setValue("newV1"));
        assertEquals("newV1", map.get("k1"));

        assertTrue(it.hasNext());
        Object key2 = it.next();
        assertEquals("k2", key2);
        assertEquals("k2", it.getKey());
        assertEquals("v2", it.getValue());

        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }

        it.reset();
        assertTrue(it.hasNext());
        assertEquals("k1", it.next());
    }

    public void testMapIteratorRemove() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        MapIterator it = map.mapIterator();
        try {
            it.remove();
            fail("Expected IllegalStateException before next()");
        } catch (IllegalStateException e) {
            // expected
        }

        it.next();
        it.remove();
        assertEquals(1, map.size());
        assertFalse(map.containsKey("k1"));
        assertTrue(map.containsKey("k2"));

        try {
            it.remove();
            fail("Expected IllegalStateException on duplicate remove()");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    public void testKeySetOperations() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        Set keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("k1"));
        assertFalse(keys.contains("k3"));

        assertTrue(keys.remove("k1"));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("k1"));
        assertFalse(keys.remove("nonexistent"));

        keys.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    public void testValuesOperations() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", null);

        Collection values = map.values();
        assertEquals(3, values.size());
        assertTrue(values.contains("v1"));
        assertTrue(values.contains(null));
        assertFalse(values.contains("vX"));

        values.clear();
        assertEquals(0, map.size());
    }

    public void testEntrySetOperations() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");

        Set entries = map.entrySet();
        assertEquals(2, entries.size());

        Iterator it = entries.iterator();
        assertTrue(it.hasNext());
        Map.Entry entry = (Map.Entry) it.next();
        assertEquals("k1", entry.getKey());
        assertEquals("v1", entry.getValue());
        assertEquals("v1", entry.setValue("updated"));
        assertEquals("updated", map.get("k1"));

        assertEquals(entry, entry);
        assertFalse(entry.equals("notAnEntry"));

        it.remove();
        assertEquals(1, map.size());
        assertFalse(map.containsKey("k1"));
    }

    public void testEqualsAndHashCodeSameContent() {
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");
        map1.put("k2", "v2");

        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put("k2", "v2");

        Map standardMap = new HashMap();
        standardMap.put("k1", "v1");
        standardMap.put("k2", "v2");

        assertEquals(map1, map1);
        assertEquals(map1, map2);
        assertEquals(map2, map1);
        assertEquals(map1, standardMap);
        assertEquals(map1.hashCode(), map2.hashCode());
        assertEquals(map1.hashCode(), standardMap.hashCode());
    }

    public void testEqualsDifferentContent() {
        Flat3Map map1 = new Flat3Map();
        map1.put("k1", "v1");
        map1.put("k2", "v2");

        Flat3Map map2 = new Flat3Map();
        map2.put("k1", "v1");
        map2.put("k2", "differentVal");

        Flat3Map map3 = new Flat3Map();
        map3.put("k1", "v1");

        assertFalse(map1.equals(map2));
        assertFalse(map1.equals(map3));
        assertFalse(map1.equals("notAMap"));
        assertFalse(map1.equals(null));
    }

    public void testEqualsWithNullKeysAndValues() {
        Flat3Map map1 = new Flat3Map();
        map1.put(null, "v1");
        map1.put("k2", null);

        Flat3Map map2 = new Flat3Map();
        map2.put(null, "v1");
        map2.put("k2", null);

        assertEquals(map1, map2);
        assertEquals(map1.hashCode(), map2.hashCode());

        Flat3Map map3 = new Flat3Map();
        map3.put(null, "different");
        map3.put("k2", null);
        assertFalse(map1.equals(map3));
    }

    public void testCloneFlatMode() {
        Flat3Map original = new Flat3Map();
        original.put("k1", "v1");
        original.put("k2", "v2");

        Flat3Map clone = (Flat3Map) original.clone();
        assertEquals(original.size(), clone.size());
        assertEquals(original, clone);

        clone.put("k3", "v3");
        assertEquals(3, clone.size());
        assertEquals(2, original.size());
    }

    public void testCloneDelegateMode() {
        Flat3Map original = new Flat3Map();
        original.put("k1", "v1");
        original.put("k2", "v2");
        original.put("k3", "v3");
        original.put("k4", "v4");

        Flat3Map clone = (Flat3Map) original.clone();
        assertEquals(4, clone.size());
        assertEquals(original, clone);

        clone.put("k5", "v5");
        assertEquals(5, clone.size());
        assertEquals(4, original.size());
    }

    public void testSerializationFlatMode() throws Exception {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Flat3Map deserialized = (Flat3Map) ois.readObject();
        ois.close();

        assertEquals(3, deserialized.size());
        assertEquals("v1", deserialized.get("k1"));
        assertEquals("v2", deserialized.get("k2"));
        assertEquals("v3", deserialized.get("k3"));
        assertEquals(map, deserialized);
    }

    public void testSerializationDelegateMode() throws Exception {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Flat3Map deserialized = (Flat3Map) ois.readObject();
        ois.close();

        assertEquals(4, deserialized.size());
        assertEquals("v1", deserialized.get("k1"));
        assertEquals("v4", deserialized.get("k4"));
        assertEquals(map, deserialized);
    }

    public void testToStringRepresentation() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        String str = map.toString();
        assertTrue(str.startsWith("{") && str.endsWith("}"));
        assertTrue(str.contains("k1=v1"));

        map.put("k2", "v2");
        str = map.toString();
        assertTrue(str.contains("k1=v1"));
        assertTrue(str.contains("k2=v2"));
    }

    public void testDelegateModeEntrySetAndKeySet() {
        Flat3Map map = new Flat3Map();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");
        map.put("4", "four"); // triggers delegate mode

        Set keys = map.keySet();
        assertEquals(4, keys.size());
        assertTrue(keys.contains("1"));
        assertTrue(keys.remove("4"));
        assertEquals(3, map.size());

        Set entries = map.entrySet();
        assertEquals(3, entries.size());

        Collection values = map.values();
        assertEquals(3, values.size());
        assertTrue(values.contains("one"));
    }
}
