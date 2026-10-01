```java
package org.apache.commons.collections.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.ResettableIterator;

public class GeneratedFlat3MapTest extends TestCase {

    /** Subclass that counts how often the delegate map is created. */
    static class CountingFlat3Map extends Flat3Map {
        static int created = 0;

        CountingFlat3Map() {
            super();
        }

        protected AbstractHashedMap createDelegateMap() {
            created++;
            return new HashedMap();
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        CountingFlat3Map.created = 0;
    }

    protected void tearDown() throws Exception {
        CountingFlat3Map.created = 0;
        super.tearDown();
    }

    // ------------------------------------------------------------ helpers

    private static Flat3Map filled(int n) {
        Flat3Map m = new Flat3Map();
        for (int i = 1; i <= n; i++) {
            m.put("k" + i, "v" + i);
        }
        return m;
    }

    private static HashMap hashMap(int n) {
        HashMap m = new HashMap();
        for (int i = 1; i <= n; i++) {
            m.put("k" + i, "v" + i);
        }
        return m;
    }

    private static Object roundTrip(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        try {
            return in.readObject();
        } finally {
            in.close();
        }
    }

    // ------------------------------------------------------------ basics

    public void testNewMapIsEmpty() {
        Flat3Map m = new Flat3Map();
        assertEquals(0, m.size());
        assertTrue(m.isEmpty());
        assertNull(m.get("a"));
        assertNull(m.get(null));
        assertFalse(m.containsKey("a"));
        assertFalse(m.containsKey(null));
        assertFalse(m.containsValue("a"));
        assertFalse(m.containsValue(null));
        assertEquals("{}", m.toString());
        assertEquals(0, m.hashCode());
        assertNull(m.remove("a"));
        assertNull(m.remove(null));
        assertFalse(m.mapIterator().hasNext());
        assertTrue(m.entrySet().isEmpty());
        assertTrue(m.keySet().isEmpty());
        assertTrue(m.values().isEmpty());
    }

    public void testPutAndGetUpToThree() {
        Flat3Map m = new Flat3Map();
        assertNull(m.put("k1", "v1"));
        assertEquals(1, m.size());
        assertNull(m.put("k2", "v2"));
        assertEquals(2, m.size());
        assertNull(m.put("k3", "v3"));
        assertEquals(3, m.size());
        assertFalse(m.isEmpty());

        assertEquals("v1", m.get("k1"));
        assertEquals("v2", m.get("k2"));
        assertEquals("v3", m.get("k3"));
        assertNull(m.get("missing"));

        assertEquals("v1", m.put("k1", "n1"));
        assertEquals("v2", m.put("k2", "n2"));
        assertEquals("v3", m.put("k3", "n3"));
        assertEquals(3, m.size());
        assertEquals("n1", m.get("k1"));
        assertEquals("n2", m.get("k2"));
        assertEquals("n3", m.get("k3"));
    }

    public void testPutNullKeyAtVariousPositions() {
        // null key as third, first and second entry
        Object[][] orders = new Object[][] {
            { "a", "b", null },
            { null, "a", "b" },
            { "a", null, "b" }
        };
        for (int i = 0; i < orders.length; i++) {
            Flat3Map m = new Flat3Map();
            for (int j = 0; j < 3; j++) {
                assertNull(m.put(orders[i][j], "val" + j));
            }
            assertEquals(3, m.size());
            assertTrue(m.containsKey(null));
            String expected = null;
            for (int j = 0; j < 3; j++) {
                if (orders[i][j] == null) {
                    expected = "val" + j;
                }
            }
            assertEquals(expected, m.get(null));
            assertEquals(expected, m.put(null, "replaced"));
            assertEquals(3, m.size());
            assertEquals("replaced", m.get(null));
        }
    }

    public void testNullValues() {
        Flat3Map m = new Flat3Map();
        assertNull(m.put("a", null));
        assertTrue(m.containsKey("a"));
        assertNull(m.get("a"));
        assertTrue(m.containsValue(null));
        assertNull(m.put("a", "x"));
        assertFalse(m.containsValue(null));

        Flat3Map m3 = new Flat3Map();
        m3.put("a", null);
        m3.put("b", null);
        m3.put("c", "x");
        assertTrue(m3.containsValue(null));
        assertTrue(m3.containsValue("x"));
        assertEquals(3, m3.size());
    }

    public void testContainsKeyAndValueFlat() {
        Flat3Map m = filled(3);
        for (int i = 1; i <= 3; i++) {
            assertTrue(m.containsKey("k" + i));
            assertTrue(m.containsValue("v" + i));
        }
        assertFalse(m.containsKey("k4"));
        assertFalse(m.containsKey(null));
        assertFalse(m.containsValue("v4"));
        assertFalse(m.containsValue(null));
    }

    // ------------------------------------------------------------ delegate mode

    public void testSizeGrowthAcrossThreshold() {
        Flat3Map m = new Flat3Map();
        for (int i = 1; i <= 6; i++) {
            assertNull(m.put("k" + i, "v" + i));
            assertEquals(i, m.size());
            assertFalse(m.isEmpty());
        }
        assertEquals("v1", m.put("k1", "new"));
        assertEquals(6, m.size());
        assertEquals("new", m.get("k1"));
    }

    public void testConvertToMapPreservesEntries() {
        Flat3Map m = filled(3);
        assertNull(m.put("k4", "v4")); // triggers convertToMap
        assertEquals(4, m.size());
        for (int i = 1; i <= 4; i++) {
            assertEquals("v" + i, m.get("k" + i));
            assertTrue(m.containsKey("k" + i));
            assertTrue(m.containsValue("v" + i));
        }
        assertFalse(m.containsKey("k5"));
        assertEquals(hashMap(4), m);
    }

    public void testCreateDelegateMapCalledOnFourthPut() {
        CountingFlat3Map m = new CountingFlat3Map();
        m.put("k1", "v1");
        m.put("k2", "v2");
        m.put("k3", "v3");
        assertEquals(0, CountingFlat3Map.created);
        m.put("k4", "v4");
        assertEquals(1, CountingFlat3Map.created);
        m.put("k5", "v5");
        assertEquals(1, CountingFlat3Map.created);
        assertEquals(5, m.size());
    }

    public void testClearReturnsToFlatMode() {
        Flat3Map flat = filled(2);
        flat.clear();
        assertEquals(0, flat.size());
        assertTrue(flat.isEmpty());
        assertNull(flat.get("k1"));
        assertFalse(flat.containsKey("k2"));

        CountingFlat3Map m = new CountingFlat3Map();
        for (int i = 1; i <= 4; i++) {
            m.put("k" + i, "v" + i);
        }
        assertEquals(1, CountingFlat3Map.created);
        m.clear();
        assertEquals(0, m.size());
        assertTrue(m.isEmpty());
        assertNull(m.get("k1"));
        // back in flat mode: three puts do not create a delegate
        m.put("a", "1");
        m.put("b", "2");
        m.put("c", "3");
        assertEquals(1, CountingFlat3Map.created);
        m.put("d", "4");
        assertEquals(2, CountingFlat3Map.created);
    }

    public void testPutAllVariants() {
        // empty map: no change
        Flat3Map m = filled(2);
        m.putAll(new HashMap());
        assertEquals(2, m.size());

        // small map does not create a delegate
        CountingFlat3Map c = new CountingFlat3Map();
        c.putAll(hashMap(3));
        assertEquals(3, c.size());
        assertEquals(0, CountingFlat3Map.created);
        assertEquals(hashMap(3), c);

        // map with four entries creates a delegate
        CountingFlat3Map c4 = new CountingFlat3Map();
        c4.putAll(hashMap(4));
        assertEquals(4, c4.size());
        assertEquals(1, CountingFlat3Map.created);
        assertEquals(hashMap(4), c4);

        // flat map with overlap, grows past three through individual puts
        Flat3Map f = filled(2);
        HashMap extra = new HashMap();
        extra.put("k2", "new");
        extra.put("k3", "v3");
        extra.put("k4", "v4");
        f.putAll(extra);
        assertEquals(4, f.size());
        assertEquals("v1", f.get("k1"));
        assertEquals("new", f.get("k2"));
        assertEquals("v3", f.get("k3"));
        assertEquals("v4", f.get("k4"));

        // putAll while in delegate mode
        Flat3Map d = filled(5);
        HashMap one = new HashMap();
        one.put("extra", "x");
        d.putAll(one);
        assertEquals(6, d.size());
        assertEquals("x", d.get("extra"));

        // null map
        try {
            new Flat3Map().putAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    public void testCopyConstructor() {
        Flat3Map small = new Flat3Map(hashMap(2));
        assertEquals(2, small.size());
        assertEquals(hashMap(2), small);

        Flat3Map large = new Flat3Map(hashMap(5));
        assertEquals(5, large.size());
        assertEquals(hashMap(5), large);

        Flat3Map empty = new Flat3Map(new HashMap());
        assertTrue(empty.isEmpty());

        try {
            new Flat3Map(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    // ------------------------------------------------------------ remove

    public void testRemoveFromSizeThree() {
        // remove the last entry
        Flat3Map m = filled(3);
        assertEquals("v3", m.remove("k3"));
        assertEquals(2, m.size());
        assertFalse(m.containsKey("k3"));
        assertEquals("v1", m.get("k1"));
        assertEquals("v2", m.get("k2"));

        // remove the middle entry (check resulting state)
        m = filled(3);
        m.remove("k2");
        assertEquals(2, m.size());
        assertFalse(m.containsKey("k2"));
        assertEquals("v1", m.get("k1"));
        assertEquals("v3", m.get("k3"));

        // remove the first entry (check resulting state)
        m = filled(3);
        m.remove("k1");
        assertEquals(2, m.size());
        assertFalse(m.containsKey("k1"));
        assertEquals("v2", m.get("k2"));
        assertEquals("v3", m.get("k3"));
    }

    public void testRemoveFromSizeTwoAndOne() {
        Flat3Map m = filled(2);
        assertEquals("v2", m.remove("k2"));
        assertEquals(1, m.size());
        assertEquals("v1", m.get("k1"));

        m = filled(2);
        m.remove("k1");
        assertEquals(1, m.size());
        assertFalse(m.containsKey("k1"));
        assertEquals("v2", m.get("k2"));

        m = filled(1);
        assertEquals("v1", m.remove("k1"));
        assertEquals(0, m.size());
        assertTrue(m.isEmpty());
        assertNull(m.remove("k1"));
    }

    public void testRemoveAbsentKeys() {
        for (int n = 1; n <= 3; n++) {
            Flat3Map m = filled(n);
            assertNull(m.remove("absent"));
            assertNull(m.remove(null));
            assertEquals(n, m.size());
        }
    }

    public void testRemoveNullKey() {
        Flat3Map m = new Flat3Map();
        m.put(null, "n");
        assertEquals("n", m.remove(null));
        assertTrue(m.isEmpty());

        m = new Flat3Map();
        m.put("k1", "v1");
        m.put(null, "n");
        assertEquals("n", m.remove(null));
        assertEquals(1, m.size());
        assertEquals("v1", m.get("k1"));

        m = new Flat3Map();
        m.put("k1", "v1");
        m.put("k2", "v2");
        m.put(null, "n");
        assertEquals("n", m.remove(null));
        assertEquals(2, m.size());
        assertFalse(m.containsKey(null));

        // null key in the first slot (check state only)
        m = new Flat3Map();
        m.put(null, "n");
        m.put("k2", "v2");
        m.remove(null);
        assertEquals(1, m.size());
        assertFalse(m.containsKey(null));
        assertEquals("v2", m.get("k2"));
    }

    public void testRemoveInDelegateMode() {
        Flat3Map m = filled(5);
        assertEquals("v5", m.remove("k5"));
        assertEquals(4, m.size());
        assertFalse(m.containsKey("k5"));
        assertNull(m.remove("k5"));
        assertEquals(hashMap(4), m);
    }

    // ------------------------------------------------------------ equals / hashCode / toString / clone

    public void testEquals() {
        Flat3Map m = filled(3);
        assertTrue(m.equals(m));
        assertTrue(m.equals(hashMap(3)));
        assertTrue(hashMap(3).equals(m));
        assertTrue(m.equals(filled(3)));
        assertFalse(m.equals(null));
        assertFalse(m.equals("not a map"));
        assertFalse(m.equals(hashMap(2)));
        assertFalse(m.equals(hashMap(4)));

        HashMap differentValue = hashMap(3);
        differentValue.put("k3", "zz");
        assertFalse(m.equals(differentValue));

        HashMap differentKey = hashMap(2);
        differentKey.put("other", "v3");
        assertFalse(m.equals(differentKey));

        // null values
        Flat3Map nm = new Flat3Map();
        nm.put("a", null);
        HashMap nh = new HashMap();
        nh.put("a", null);
        assertTrue(nm.equals(nh));
        nh.put("a", "x");
        assertFalse(nm.equals(nh));
        HashMap other = new HashMap();
        other.put("b", null);
        assertFalse(nm.equals(other));

        Flat3Map vm = new Flat3Map();
        vm.put("a", "x");
        HashMap nullValue = new HashMap();
        nullValue.put("a", null);
        assertFalse(vm.equals(nullValue));

        // delegate mode
        Flat3Map d = filled(5);
        assertTrue(d.equals(hashMap(5)));
        HashMap changed = hashMap(5);
        changed.put("k5", "zz");
        assertFalse(d.equals(changed));
        assertFalse(d.equals(null));
    }

    public void testHashCode() {
        for (int n = 1; n <= 3; n++) {
            assertEquals(hashMap(n).hashCode(), filled(n).hashCode());
        }
        assertEquals(hashMap(5).hashCode(), filled(5).hashCode());

        Flat3Map m = new Flat3Map();
        m.put(null, null);
        m.put("a", null);
        m.put(null, "x");
        HashMap h = new HashMap();
        h.put(null, "x");
        h.put("a", null);
        assertEquals(h.hashCode(), m.hashCode());
    }

    public void testToString() {
        assertEquals("{k1=v1}", filled(1).toString());
        assertEquals("{k2=v2,k1=v1}", filled(2).toString());
        assertEquals("{k3=v3,k2=v2,k1=v1}", filled(3).toString());

        Flat3Map m = new Flat3Map();
        m.put(null, null);
        assertEquals("{null=null}", m.toString());

        Flat3Map selfValue = new Flat3Map();
        selfValue.put("a", selfValue);
        assertEquals("{a=(this Map)}", selfValue.toString());

        Flat3Map selfKey = new Flat3Map();
        selfKey.put(selfKey, "v");
        assertEquals("{(this Map)=v}", selfKey.toString());

        String delegateString = filled(5).toString();
        assertTrue(delegateString.startsWith("{"));
        assertTrue(delegateString.endsWith("}"));
        assertTrue(delegateString.indexOf("k3=v3") >= 0);
    }

    public void testClone() {
        Flat3Map original = filled(3);
        Flat3Map copy = (Flat3Map) original.clone();
        assertNotSame(original, copy);
        assertEquals(original, copy);
        copy.put("k4", "v4");
        assertEquals(3, original.size());
        assertFalse(original.containsKey("k4"));
        assertEquals(4, copy.size());

        Flat3Map flatCopy = (Flat3Map) original.clone();
        flatCopy.remove("k3");
        assertEquals(3, original.size());
        assertEquals("v3", original.get("k3"));

        Flat3Map delegate = filled(5);
        Flat3Map delegateCopy = (Flat3Map) delegate.clone();
        assertNotSame(delegate, delegateCopy);
        assertEquals(delegate, delegateCopy);
        delegateCopy.put("k6", "v6");
        delegateCopy.remove("k1");
        assertEquals(5, delegate.size());
        assertEquals("v1", delegate.get("k1"));
        assertFalse(delegate.containsKey("k6"));
    }

    // ------------------------------------------------------------ map iterator

    public void testMapIteratorFlat() {
        Flat3Map m = filled(3);
        MapIterator it = m.mapIterator();
        assertTrue(it.hasNext());
        assertEquals("k1", it.next());
        assertEquals("k1", it.getKey());
        assertEquals("v1", it.getValue());
        assertEquals("v1", it.setValue("x1"));
        assertEquals("x1", it.getValue());
        assertEquals("x1", m.get("k1"));
        assertEquals("k2", it.next());
        assertEquals("v2", it.getValue());
        assertEquals("k3", it.next());
        assertEquals("v3", it.getValue());
        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {
            // expected
        }

        ((ResettableIterator) it).reset();
        assertTrue(it.hasNext());
        try {
            it.getKey();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        assertEquals("k1", it.next());

        // remove every entry through the iterator
        it = m.mapIterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            it.remove();
            count++;
        }
        assertEquals(3, count);
        assertTrue(m.isEmpty());
    }

    public void testMapIteratorEmptyAndInvalidState() {
        Flat3Map empty = new Flat3Map();
        MapIterator emptyIt = empty.mapIterator();
        assertFalse(emptyIt.hasNext());
        try {
            emptyIt.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {
            // expected
        }

        Flat3Map m = filled(2);
        MapIterator it = m.mapIterator();
        try {
            it.getKey();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        try {
            it.getValue();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        try {
            it.setValue("x");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        try {
            it.remove();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        it.next();
        it.remove();
        try {
            it.remove();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException expected) {
            // expected
        }
        assertEquals(1, m.size());
    }

    public void testMapIteratorDelegate() {
        Flat3Map m = filled(5);
        MapIterator it = m.mapIterator();
        Set keys = new HashSet();
        while (it.hasNext()) {
            Object key = it.next();
            keys.add(key);
            assertEquals(m.get(key), it.getValue());
        }
        assertEquals(new HashSet(Arrays.asList(new Object[] { "k1", "k2", "k3", "k4", "k5" })), keys);
    }

    // ------------------------------------------------------------ views

    public void testEntrySet() {
        Flat3Map m = filled(3);
        Set es = m.entrySet();
        assertEquals(3, es.size());

        Map collected = new HashMap();
        for (Iterator it = es.iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            collected.put(e.getKey(), e.getValue());
        }
        assertEquals(hashMap(3), collected);

        // entry behaviour via iterator
        Iterator it = es.iterator();
        Map.Entry e = (Map.Entry) it.next();
        assertEquals("k1=v1", e.toString());
        assertEquals("k1".hashCode() ^ "v1".hashCode(), e.hashCode());
        Map.Entry same = (Map.Entry) Collections.singletonMap("k1", "v1").entrySet().iterator().next();
        assertTrue(e.equals(same));
        assertFalse(e.equals("not an entry"));
        assertEquals("v1", e.setValue("z"));
        assertEquals("z", m.get("k1"));

        // iterator.remove
        it = es.iterator();
        it.next();
        it.remove();
        assertEquals(2, m.size());

        // remove(Object)
        m = filled(3);
        es = m.entrySet();
        Map.Entry k2 = (Map.Entry) Collections.singletonMap("k2", "v2").entrySet().iterator().next();
        assertTrue(es.remove(k2));
        assertFalse(m.containsKey("k2"));
        assertEquals(2, m.size());
        assertFalse(es.remove("not an entry"));
        assertFalse(es.remove(k2));

        es.clear();
        assertTrue(m.isEmpty());
        assertFalse(es.iterator().hasNext());
    }

    public void testKeySet() {
        Flat3Map m = filled(3);
        Set ks = m.keySet();
        assertEquals(3, ks.size());
        assertTrue(ks.contains("k2"));
        assertFalse(ks.contains("k4"));

        List order = new ArrayList();
        for (Iterator it = ks.iterator(); it.hasNext();) {
            order.add(it.next());
        }
        assertEquals(Arrays.asList(new Object[] { "k1", "k2", "k3" }), order);

        assertTrue(ks.remove("k2"));
        assertFalse(ks.remove("k2"));
        assertEquals(2, m.size());
        assertFalse(m.containsKey("k2"));

        Iterator it = ks.iterator();
        it.next();
        it.remove();
        assertEquals(1, m.size());

        ks.clear();
        assertTrue(m.isEmpty());
        assertFalse(ks.iterator().hasNext());
    }

    public void testValues() {
        Flat3Map m = filled(3);
        java.util.Collection vals = m.values();
        assertEquals(3, vals.size());
        assertTrue(vals.contains("v3"));
        assertFalse(vals.contains("v4"));

        List order = new ArrayList();
        for (Iterator it = vals.iterator(); it.hasNext();) {
            order.add(it.next());
        }
        assertEquals(Arrays.asList(new Object[] { "v1", "v2", "v3" }), order);

        vals.clear();
        assertTrue(m.isEmpty());
        assertEquals(0, vals.size());
        assertFalse(vals.iterator().hasNext());
    }

    public void testViewsInDelegateMode() {
        Flat3Map m = filled(5);
        assertEquals(5, m.keySet().size());
        assertEquals(5, m.values().size());
        assertEquals(5, m.entrySet().size());
        assertTrue(m.keySet().contains("k5"));
        assertTrue(m.values().contains("v5"));
        assertTrue(m.keySet().remove("k5"));
        assertEquals(4, m.size());
        Set seen = new HashSet();
        for (Iterator it = m.keySet().iterator(); it.hasNext();) {
            seen.add(it.next());
        }
        assertEquals(4, seen.size());
        m.values().clear();
        assertTrue(m.isEmpty());
    }

    // ------------------------------------------------------------ serialization

    public void testSerializationFlat() throws Exception {
        Flat3Map m = new Flat3Map();
        m.put("k1", "v1");
        m.put(null, "n");
        m.put("k3", null);
        Flat3Map copy = (Flat3Map) roundTrip(m);
        assertNotSame(m, copy);
        assertEquals(3, copy.size());
        assertEquals(m, copy);
        assertEquals("v1", copy.get("k1"));
        assertEquals("n", copy.get(null));
        assertTrue(copy.containsKey("k3"));
        assertNull(copy.get("k3"));
        copy.put("k1", "changed");
        assertEquals("v1", m.get("k1"));

        Flat3Map empty = (Flat3Map) roundTrip(new Flat3Map());
        assertTrue(empty.isEmpty());
    }

    public void testSerializationDelegateAndSubclassMode() throws Exception {
        Flat3Map big = filled(6);
        Flat3Map bigCopy = (Flat3Map) roundTrip(big);
        assertEquals(6, bigCopy.size());
        assertEquals(big, bigCopy);
        assertEquals(hashMap(6), bigCopy);

        CountingFlat3Map three = new CountingFlat3Map();
        three.put("k1", "v1");
        three.put("k2", "v2");
        three.put("k3", "v3");
        CountingFlat3Map.created = 0;
        Object threeCopy = roundTrip(three);
        assertTrue(threeCopy instanceof CountingFlat3Map);
        assertEquals(0, CountingFlat3Map.created);
        assertEquals(three, threeCopy);

        CountingFlat3Map four = new CountingFlat3Map();
        for (int i = 1; i <= 4; i++) {
            four.put("k" + i, "v" + i);
        }
        CountingFlat3Map.created = 0;
        Object fourCopy = roundTrip(four);
        assertTrue(fourCopy instanceof CountingFlat3Map);
        assertEquals(1, CountingFlat3Map.created);
        assertEquals(four, fourCopy);
        assertEquals(4, ((Flat3Map) fourCopy).size());
    }
}
```