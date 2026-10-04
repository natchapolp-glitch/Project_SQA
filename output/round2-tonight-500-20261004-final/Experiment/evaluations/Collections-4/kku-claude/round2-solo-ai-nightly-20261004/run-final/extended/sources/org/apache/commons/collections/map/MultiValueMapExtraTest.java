package org.apache.commons.collections.map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.collections.Factory;
import org.junit.Test;

public class MultiValueMapExtraTest {

    @Test
    public void testDecorateWithFactory() {
        Factory factory = new Factory() {
            public Object create() {
                return new ArrayList();
            }
        };
        MultiValueMap map = MultiValueMap.decorate(new HashMap(), factory);
        map.put("A", "1");
        Collection coll = map.getCollection("A");
        assertNotNull(coll);
        assertTrue(coll instanceof ArrayList);
        assertEquals(1, coll.size());
    }

    @Test
    public void testPutAllMapWithMultiMap() {
        MultiValueMap source = MultiValueMap.decorate(new HashMap());
        source.put("A", "1");
        source.put("A", "2");

        MultiValueMap target = MultiValueMap.decorate(new HashMap());
        target.putAll(source);

        assertEquals(2, target.size("A"));
        assertTrue(target.containsValue("A", "1"));
        assertTrue(target.containsValue("A", "2"));
    }

    @Test
    public void testRemoveMappingRemovesKeyWhenEmpty() {
        MultiValueMap map = MultiValueMap.decorate(new HashMap());
        map.put("A", "1");
        assertTrue(map.containsKey("A"));
        map.removeMapping("A", "1");
        assertFalse(map.containsKey("A"));
        assertEquals(0, map.totalSize());
    }

    @Test
    public void testContainsValueOnEmptyMap() {
        MultiValueMap map = MultiValueMap.decorate(new HashMap());
        assertFalse(map.containsValue("anything"));
        assertFalse(map.containsValue("someKey", "someValue"));
        assertEquals(0, map.size("missingKey"));
    }
}
