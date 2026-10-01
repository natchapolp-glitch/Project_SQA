package org.apache.commons.collections.map;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.apache.commons.collections.MapIterator;

public class Flat3MapTest {

    private Flat3Map map;

    @Before
    public void setUp() {
        map = new Flat3Map();
    }

    @Test
    public void testConstructorEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testConstructorMap() {
        Map source = new HashMap();
        source.put("A", "1");
        source.put("B", "2");
        Flat3Map newMap = new Flat3Map(source);
        assertEquals(2, newMap.size());
        assertEquals("1", newMap.get("A"));
        assertEquals("2", newMap.get("B"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorMapNull() {
        new Flat3Map(null);
    }

    @Test
    public void testPutAndGetFlatMode() {
        map.put("K1", "V1");
        assertEquals(1, map.size());
        assertEquals("V1", map.get("K1"));

        map.put("K2", "V2");
        assertEquals(2, map.size());
        assertEquals("V2", map.get("K2"));

        map.put("K3", "V3");
        assertEquals(3, map.size());
        assertEquals("V3", map.get("K3"));
        
        assertNull(map.get("K4"));
    }

    @Test
    public void testPutAndGetDelegateMode() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");
        map.put("K4", "V4"); 
        
        assertEquals(4, map.size());
        assertEquals("V1", map.get("K1"));
        assertEquals("V2", map.get("K2"));
        assertEquals("V3", map.get("K3"));
        assertEquals("V4", map.get("K4"));
    }

    @Test
    public void testPutNullKeyAndValue() {
        map.put(null, "NullKey");
        assertEquals(1, map.size());
        assertEquals("NullKey", map.get(null));
        assertTrue(map.containsKey(null));

        map.put("NullVal", null);
        assertEquals(2, map.size());
        assertNull(map.get("NullVal"));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void testPutOverwrite() {
        map.put("K1", "V1");
        Object old = map.put("K1", "V1_NEW");
        assertEquals("V1", old);
        assertEquals("V1_NEW", map.get("K1"));
        assertEquals(1, map.size());
    }

    @Test
    public void testRemoveFlatMode() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");

        map.remove("K3");
        assertEquals(2, map.size());
        assertFalse(map.containsKey("K3"));
        assertTrue(map.containsKey("K1"));
        assertTrue(map.containsKey("K2"));

        map.remove("K1");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("K1"));
        assertTrue(map.containsKey("K2"));
    }

    @Test
    public void testRemoveNullKey() {
        map.put(null, "V1");
        map.put("K2", "V2");
        
        map.remove(null);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(null));
        assertTrue(map.containsKey("K2"));
    }

    @Test
    public void testRemoveDelegateMode() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");
        map.put("K4", "V4"); 

        map.remove("K2");
        assertEquals(3, map.size());
        assertFalse(map.containsKey("K2"));
        assertEquals("V1", map.get("K1"));
        assertEquals("V3", map.get("K3"));
        assertEquals("V4", map.get("K4"));
    }

    @Test
    public void testContainsKeyAndValue() {
        map.put("K1", "V1");
        map.put("K2", null);

        assertTrue(map.containsKey("K1"));
        assertFalse(map.containsKey("K3"));
        assertTrue(map.containsValue("V1"));
        assertTrue(map.containsValue(null));
        assertFalse(map.containsValue("V3"));
    }

    @Test
    public void testClear() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());

        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");
        map.put("K4", "V4");
        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testPutAll() {
        Map source = new HashMap();
        source.put("A", "1");
        source.put("B", "2");
        source.put("C", "3");
        source.put("D", "4");

        map.putAll(source);
        assertEquals(4, map.size());
        assertEquals("1", map.get("A"));
        assertEquals("4", map.get("D"));
    }
    
    @Test
    public void testPutAllEmpty() {
        map.put("K1", "V1");
        map.putAll(new HashMap());
        assertEquals(1, map.size());
    }

    @Test
    public void testMapIterator() {
        map.put("K1", "V1");
        map.put("K2", "V2");

        MapIterator it = map.mapIterator();
        assertTrue(it.hasNext());
        
        Object k1 = it.next();
        assertEquals("V1", it.getValue());
        it.setValue("V1_NEW");
        assertEquals("V1_NEW", map.get(k1));

        Object k2 = it.next();
        assertEquals("V2", it.getValue());
        
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapIteratorNoNext() {
        MapIterator it = map.mapIterator();
        it.next();
    }
    
    @Test
    public void testMapIteratorRemove() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        
        MapIterator it = map.mapIterator();
        it.next();
        it.remove();
        
        assertEquals(1, map.size());
    }

    @Test
    public void testEntrySet() {
        map.put("K1", "V1");
        map.put("K2", "V2");

        Set entries = map.entrySet();
        assertEquals(2, entries.size());
        
        Iterator it = entries.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
    }
    
    @Test
    public void testEntrySetRemove() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        
        Set entries = map.entrySet();
        Iterator it = entries.iterator();
        it.next();
        it.remove();
        
        assertEquals(1, map.size());
    }

    @Test
    public void testKeySet() {
        map.put("K1", "V1");
        map.put("K2", "V2");

        Set keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("K1"));
        assertTrue(keys.contains("K2"));
    }

    @Test
    public void testValues() {
        map.put("K1", "V1");
        map.put("K2", "V2");

        Collection vals = map.values();
        assertEquals(2, vals.size());
        assertTrue(vals.contains("V1"));
        assertTrue(vals.contains("V2"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Flat3Map map2 = new Flat3Map();
        map.put("K1", "V1");
        map2.put("K1", "V1");

        assertEquals(map, map2);
        assertEquals(map.hashCode(), map2.hashCode());

        map2.put("K2", "V2");
        assertFalse(map.equals(map2));
        
        assertFalse(map.equals("Not a map"));
    }

    @Test
    public void testToString() {
        assertEquals("{}", map.toString());
        map.put("K1", "V1");
        String str = map.toString();
        assertTrue(str.contains("K1"));
        assertTrue(str.contains("V1"));
    }

    @Test
    public void testClone() {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");
        map.put("K4", "V4"); 

        Flat3Map cloned = (Flat3Map) map.clone();
        assertEquals(map, cloned);
        
        cloned.put("K5", "V5");
        assertFalse(map.containsKey("K5"));
    }

    @Test
    public void testSerialization() throws Exception {
        map.put("K1", "V1");
        map.put("K2", "V2");
        map.put("K3", "V3");
        map.put("K4", "V4");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Flat3Map deserialized = (Flat3Map) ois.readObject();

        assertEquals(map, deserialized);
        assertEquals(4, deserialized.size());
    }
}
