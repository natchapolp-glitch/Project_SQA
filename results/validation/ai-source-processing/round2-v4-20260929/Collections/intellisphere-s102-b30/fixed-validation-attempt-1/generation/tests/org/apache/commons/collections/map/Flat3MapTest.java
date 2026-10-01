package org.apache.commons.collections.map;

import junit.framework.TestCase;

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

import org.apache.commons.collections.MapIterator;

/**
 * Unit tests for {@link Flat3Map}.
 */
public class Flat3MapTest extends TestCase {

    public Flat3MapTest(String testName) {
        super(testName);
    }

    public void testConstructorAndEmptyMap() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey("A"));
        assertFalse(map.containsValue("1"));
        assertNull(map.get("A"));
    }

    public void testConstructorWithMap() {
        Map src = new HashMap();
        src.put("A", "1");
        src.put("B", "2");

        Flat3Map map = new Flat3Map(src);
        assertEquals(2, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("B"));
    }

    public void testPutAndGetFlatMode() {
        Flat3Map map = new Flat3Map();

        assertNull(map.put("A", "1"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertEquals("1", map.get("A"));

        assertNull(map.put("B", "2"));
        assertEquals(2, map.size());
        assertEquals("2", map.get("B"));

        assertNull(map.put("C", "3"));
        assertEquals(3, map.size());
        assertEquals("3", map.get("C"));
    }

    public void testPutUpdateExistingKeyFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        assertEquals("1", map.put("A", "10"));
        assertEquals("10", map.get("A"));
        assertEquals(3, map.size());

        assertEquals("2", map.put("B", "20"));
        assertEquals("20", map.get("B"));

        assertEquals("3", map.put("C", "30"));
        assertEquals("30", map.get("C"));
    }

    public void testNullKeysAndValuesInFlatMode() {
        Flat3Map map = new Flat3Map();

        assertNull(map.put(null, "nullVal"));
        assertTrue(map.containsKey(null));
        assertEquals("nullVal", map.get(null));

        assertNull(map.put("nullKey", null));
        assertTrue(map.containsValue(null));
        assertNull(map.get("nullKey"));

        assertEquals("nullVal", map.put(null, "newNullVal"));
        assertEquals("newNullVal", map.get(null));
    }

    public void testTransitionsToDelegateModeAndBack() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        // Adding 4th element switches to delegate mode
        assertNull(map.put("D", "4"));
        assertEquals(4, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("B"));
        assertEquals("3", map.get("C"));
        assertEquals("4", map.get("D"));

        // Updating key in delegate mode
        assertEquals("1", map.put("A", "10"));
        assertEquals("10", map.get("A"));

        // Clear switches back to flat mode
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("A"));

        // Verify flat mode can be used again
        map.put("X", "100");
        assertEquals(1, map.size());
        assertEquals("100", map.get("X"));
    }

    public void testRemoveFromFlatModeSize3() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        assertEquals("2", map.remove("B"));
        assertEquals(2, map.size());
        assertFalse(map.containsKey("B"));
        assertEquals("1", map.get("A"));
        assertEquals("3", map.get("C"));

        assertEquals("1", map.remove("A"));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("A"));
        assertEquals("3", map.get("C"));

        assertEquals("3", map.remove("C"));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    public void testRemoveFromFlatModeSize2() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        assertEquals("1", map.remove("A"));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("A"));
        assertEquals("2", map.get("B"));
    }

    public void testRemoveFromFlatModeSize1() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");

        assertEquals("1", map.remove("A"));
        assertEquals(0, map.size());
        assertNull(map.remove("A"));
    }

    public void testRemoveNonExistentKey() {
        Flat3Map map = new Flat3Map();
        assertNull(map.remove("X"));

        map.put("A", "1");
        map.put("B", "2");
        assertNull(map.remove("Z"));
        assertEquals(2, map.size());
    }

    public void testRemoveNullKey() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put(null, "nullVal");
        map.put("B", "2");

        assertTrue(map.containsKey(null));
        assertEquals("nullVal", map.remove(null));
        assertFalse(map.containsKey(null));
        assertEquals(2, map.size());
    }

    public void testContainsKeyAndValueFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        assertTrue(map.containsKey("A"));
        assertTrue(map.containsKey("B"));
        assertFalse(map.containsKey("C"));

        assertTrue(map.containsValue("1"));
        assertTrue(map.containsValue("2"));
        assertFalse(map.containsValue("3"));
    }

    public void testContainsKeyAndValueDelegateMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");
        map.put("D", "4");

        assertTrue(map.containsKey("C"));
        assertTrue(map.containsKey("D"));
        assertFalse(map.containsKey("E"));

        assertTrue(map.containsValue("3"));
        assertTrue(map.containsValue("4"));
        assertFalse(map.containsValue("5"));
    }

    public void testPutAllFlatAndDelegate() {
        Flat3Map map = new Flat3Map();
        Map smallMap = new HashMap();
        smallMap.put("A", "1");
        smallMap.put("B", "2");

        map.putAll(smallMap);
        assertEquals(2, map.size());

        Map emptyMap = new HashMap();
        map.putAll(emptyMap);
        assertEquals(2, map.size());

        Map largeMap = new HashMap();
        largeMap.put("C", "3");
        largeMap.put("D", "4");
        largeMap.put("E", "5");
        largeMap.put("F", "6");

        map.putAll(largeMap);
        assertEquals(6, map.size());
        assertEquals("5", map.get("E"));
    }

    public void testCloneFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        Flat3Map clone = (Flat3Map) map.clone();
        assertEquals(map.size(), clone.size());
        assertEquals("1", clone.get("A"));
        assertEquals("2", clone.get("B"));

        clone.put("C", "3");
        assertFalse(map.containsKey("C"));
    }

    public void testCloneDelegateMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");
        map.put("D", "4");

        Flat3Map clone = (Flat3Map) map.clone();
        assertEquals(4, clone.size());
        assertEquals("4", clone.get("D"));

        clone.remove("D");
        assertTrue(map.containsKey("D"));
    }

    public void testEqualsAndHashCodeFlatMode() {
        Flat3Map map1 = new Flat3Map();
        map1.put("A", "1");
        map1.put("B", "2");

        assertEquals(map1, map1);

        Flat3Map map2 = new Flat3Map();
        map2.put("A", "1");
        map2.put("B", "2");

        assertEquals(map1, map2);
        assertEquals(map1.hashCode(), map2.hashCode());

        map2.put("C", "3");
        assertFalse(map1.equals(map2));

        assertFalse(map1.equals("not a map"));
    }

    public void testEqualsAndHashCodeDelegateMode() {
        Flat3Map map1 = new Flat3Map();
        map1.put("A", "1");
        map1.put("B", "2");
        map1.put("C", "3");
        map1.put("D", "4");

        Flat3Map map2 = new Flat3Map();
        map2.put("A", "1");
        map2.put("B", "2");
        map2.put("C", "3");
        map2.put("D", "4");

        assertEquals(map1, map2);
        assertEquals(map1.hashCode(), map2.hashCode());
    }

    public void testToStringFlatMode() {
        Flat3Map map = new Flat3Map();
        assertEquals("{}", map.toString());

        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        assertEquals("{C=3,B=2,A=1}", map.toString());
    }

    public void testToStringSelfReference() {
        Flat3Map map = new Flat3Map();
        map.put("selfKey", map);
        map.put(map, "selfValue");

        String str = map.toString();
        assertTrue(str.contains("(this Map)"));
    }

    public void testMapIteratorFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        MapIterator it = map.mapIterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertEquals("A", it.getKey());
        assertEquals("1", it.getValue());

        assertEquals("1", it.setValue("10"));
        assertEquals("10", map.get("A"));

        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertEquals("B", it.getKey());
        assertEquals("2", it.getValue());

        it.remove();
        assertEquals(1, map.size());
        assertFalse(map.containsKey("B"));
    }

    public void testMapIteratorExceptions() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");

        MapIterator it = map.mapIterator();
        try {
            it.getKey();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            it.getValue();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            it.setValue("x");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            it.remove();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        it.next();
        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    public void testEntrySetKeySetValuesViews() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        Set keySet = map.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains("A"));

        Collection values = map.values();
        assertEquals(2, values.size());
        assertTrue(values.contains("1"));

        Set entrySet = map.entrySet();
        assertEquals(2, entrySet.size());

        keySet.remove("A");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("A"));

        entrySet.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    public void testEntrySetIteratorAndEntry() {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");

        Set entrySet = map.entrySet();
        Iterator it = entrySet.iterator();
        assertTrue(it.hasNext());

        Map.Entry entry = (Map.Entry) it.next();
        assertEquals("A", entry.getKey());
        assertEquals("1", entry.getValue());

        assertEquals("1", entry.setValue("10"));
        assertEquals("10", map.get("A"));

        assertEquals("A=10", entry.toString());
        assertTrue(entry.equals(entry));

        it.remove();
        assertEquals(0, map.size());
    }

    public void testSerialization() throws Exception {
        Flat3Map map = new Flat3Map();
        map.put("A", "1");
        map.put("B", "2");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Flat3Map deserialized = (Flat3Map) ois.readObject();
        ois.close();

        assertEquals(2, deserialized.size());
        assertEquals("1", deserialized.get("A"));
        assertEquals("2", deserialized.get("B"));
    }
}
