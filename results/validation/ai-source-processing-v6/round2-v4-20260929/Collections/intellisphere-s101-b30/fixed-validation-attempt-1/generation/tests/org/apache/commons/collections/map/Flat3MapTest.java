package org.apache.commons.collections.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.collections.MapIterator;

/**
 * Regression tests for {@link Flat3Map} based on its documented, fixed
 * reference behavior.
 */
public class Flat3MapTest extends TestCase {

    public Flat3MapTest(String name) {
        super(name);
    }

    public Flat3MapTest() {
        super();
    }

    //-----------------------------------------------------------------------
    // Basic construction / empty state
    //-----------------------------------------------------------------------

    public void testDefaultConstructorEmpty() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertEquals("{}", map.toString());
    }

    public void testMapConstructorCopiesEntries() {
        HashMap source = new HashMap();
        source.put("A", "1");
        source.put("B", "2");

        Flat3Map map = new Flat3Map(source);
        assertEquals(2, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("B"));
    }

    public void testMapConstructorNullThrows() {
        try {
            new Flat3Map(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    // Flat-mode put/get/containsKey/containsValue
    //-----------------------------------------------------------------------

    public void testPutGetSingleEntry() {
        Flat3Map map = new Flat3Map();
        Object old = map.put("key1", "value1");
        assertNull(old);
        assertEquals(1, map.size());
        assertEquals("value1", map.get("key1"));
        assertTrue(map.containsKey("key1"));
        assertTrue(map.containsValue("value1"));
        assertFalse(map.containsKey("missing"));
        assertFalse(map.containsValue("missing"));
    }

    public void testPutUpdatesExistingKeyReturnsOldValue() {
        Flat3Map map = new Flat3Map();
        map.put("key1", "value1");
        Object old = map.put("key1", "value2");
        assertEquals("value1", old);
        assertEquals(1, map.size());
        assertEquals("value2", map.get("key1"));
    }

    public void testPutThreeEntriesFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        assertEquals(3, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
        assertEquals("v3", map.get("k3"));
        assertTrue(map.containsKey("k1"));
        assertTrue(map.containsKey("k2"));
        assertTrue(map.containsKey("k3"));
        assertTrue(map.containsValue("v1"));
        assertTrue(map.containsValue("v2"));
        assertTrue(map.containsValue("v3"));
    }

    public void testPutNullKey() {
        Flat3Map map = new Flat3Map();
        map.put(null, "nullValue");
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertEquals("nullValue", map.get(null));

        Object old = map.put(null, "updated");
        assertEquals("nullValue", old);
        assertEquals("updated", map.get(null));
        assertEquals(1, map.size());
    }

    public void testPutNullValue() {
        Flat3Map map = new Flat3Map();
        map.put("key", null);
        assertEquals(1, map.size());
        assertTrue(map.containsKey("key"));
        assertTrue(map.containsValue(null));
        assertNull(map.get("key"));
    }

    public void testGetMissingKeyReturnsNull() {
        Flat3Map map = new Flat3Map();
        assertNull(map.get("nothing"));
        map.put("a", "1");
        assertNull(map.get("b"));
    }

    //-----------------------------------------------------------------------
    // Transition to delegate mode
    //-----------------------------------------------------------------------

    public void testFourthPutSwitchesToDelegateMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.put("k4", "v4");

        assertEquals(4, map.size());
        assertEquals("v1", map.get("k1"));
        assertEquals("v2", map.get("k2"));
        assertEquals("v3", map.get("k3"));
        assertEquals("v4", map.get("k4"));
        assertTrue(map.containsKey("k4"));
        assertTrue(map.containsValue("v4"));
    }

    public void testDelegateModeUpdateAndRemove() {
        Flat3Map map = new Flat3Map();
        for (int i = 1; i <= 5; i++) {
            map.put("k" + i, "v" + i);
        }
        assertEquals(5, map.size());

        Object old = map.put("k3", "updated");
        assertEquals("v3", old);
        assertEquals("updated", map.get("k3"));

        Object removed = map.remove("k2");
        assertEquals("v2", removed);
        assertEquals(4, map.size());
        assertFalse(map.containsKey("k2"));
    }

    //-----------------------------------------------------------------------
    // remove() in flat mode
    //-----------------------------------------------------------------------

    public void testRemoveFromEmptyMap() {
        Flat3Map map = new Flat3Map();
        assertNull(map.remove("nothing"));
        assertEquals(0, map.size());
    }

    public void testRemoveSingleEntry() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        Object removed = map.remove("k1");
        assertEquals("v1", removed);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey("k1"));
    }

    public void testRemoveMiddleEntryFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");

        Object removed = map.remove("k2");
        assertEquals("v2", removed);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("k1"));
        assertTrue(map.containsKey("k3"));
        assertFalse(map.containsKey("k2"));
        assertEquals("v1", map.get("k1"));
        assertEquals("v3", map.get("k3"));
    }

    public void testRemoveNonExistentKeyReturnsNull() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        assertNull(map.remove("missing"));
        assertEquals(1, map.size());
    }

    public void testRemoveNullKeyFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put(null, "vnull");
        map.put("k2", "v2");

        Object removed = map.remove(null);
        assertEquals("vnull", removed);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(null));
        assertTrue(map.containsKey("k2"));
    }

    //-----------------------------------------------------------------------
    // clear()
    //-----------------------------------------------------------------------

    public void testClearFlatMode() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey("k1"));
    }

    public void testClearDelegateModeReturnsToFlatMode() {
        Flat3Map map = new Flat3Map();
        for (int i = 1; i <= 5; i++) {
            map.put("k" + i, "v" + i);
        }
        assertEquals(5, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        // after clear, map should behave as flat mode again and accept new entries
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        assertEquals(3, map.size());
    }

    //-----------------------------------------------------------------------
    // putAll
    //-----------------------------------------------------------------------

    public void testPutAllEmptyMapNoOp() {
        Flat3Map map = new Flat3Map();
        map.put("k1", "v1");
        map.putAll(new HashMap());
        assertEquals(1, map.size());
    }

    public void testPutAllSmallMapStaysFlat() {
        Flat3Map map = new Flat3Map();
        HashMap source = new HashMap();
        source.put("a", "1");
        source.put("b", "2");
        map.putAll(source);
        assertEquals(2, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
    }

    public void testPutAllLargeMapSwitchesToDelegate() {
        Flat3Map map = new Flat3Map();
        HashMap source = new HashMap();
        source.put("a", "1");
        source.put("b", "2");
        source.put("c", "3");
        source.put("d", "4");
        map.putAll(source);
        assertEquals(4, map.size());
        assertEquals("1", map.get("a"));
        assertEquals("4", map.get("d"));
    }

    public void testPutAllNullThrows() {
        Flat3Map map = new Flat3Map();
        try {
            map.putAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException ex) {
            // expected
        }
    }

    //-----------------------------------------------------------------------
    // equals / hashCode
    //-----------------------------------------------------------------------

    public void testEqualsSameContentsDifferentImplementation() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        map.put("b", "2");

        HashMap other = new HashMap();
        other.put("a", "1");
        other.put("b", "2");

        assertTrue(map.equals(other));
        assertTrue(other.equals(map));
        assertEquals(map.hashCode(), other.hashCode());
    }

    public void testEqualsDifferentSizeFalse() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");

        HashMap other = new HashMap();
        other.put("a", "1");
        other.put("b", "2");

        assertFalse(map.equals(other));
    }

    public void testEqualsNonMapFalse() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        assertFalse(map.equals("not a map"));
        assertFalse(map.equals(null));
    }

    public void testEqualsSelfTrue() {
        Flat3Map map = new Flat3Map();
        map.put("a", "1");
        assertTrue(map.equals(map));
    }

    public void testEqualsDelegateMode() {
        Flat3Map map = new Flat3Map();
        for (int i = 1; i <= 5; i++) {
            map.put("k" + i, "v" + i);
        }
        HashMap other = new HashMap();
        for (int i = 1; i <= 5; i++) {
            other.put("k" + i, "v" + i);
        }
        assertTrue(map.equals(other));
        assertEquals(map.hashCode(), other.hashCode());
    }

    public void testHashCodeEmptyMapIsZero() {
        Flat3Map map = new Flat3Map();
        assertEquals(0, map.hashCode());
    }

    //-----------------------------------------------------------------------
    // toString
    //-----------------------------------------------------------------------

    public void testToStringEmpty() {
        Flat3Map map = new Flat3Map();
        assertEquals("{}", map.toString());
    }

    public void testToStringSingleEntry() {
        Flat3Map map = new Flat3Map();
        map.put("key", "value");
        assertEquals("{key=value}", map.toString());
    }

    //-----------------------------------------------------------------------
    // clone
    //-----------------------------------------------------------------------

    

    

    //-----------------------------------------------------------------------
    // keySet / values / entrySet views
    //-----------------------------------------------------------------------

    

    

    

    

    

    

    

    //-----------------------------------------------------------------------
    // mapIterator
    //-----------------------------------------------------------------------

    

    

    

    

    
}
