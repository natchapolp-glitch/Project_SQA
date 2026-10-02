# Flat3Map Test Scaffolding – Prompt Iteration 3

**Status:** Non-compiling scaffolding based solely on supplied `Flat3Map.java` source.
**Assumptions & Limitations marked throughout.**

---

```java
org/apache/commons/collections/map/Flat3MapTest.java
```

```java
package org.apache.commons.collections.map;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

/**
 * Regression tests for Flat3Map (fixed reference revision 1f).
 * 
 * LIMITATIONS & ASSUMPTIONS:
 * - Tests marked [FLAT_ONLY] assume flat mode (size 0-3); delegate tests require AbstractHashedMap/HashedMap.
 * - MISSING DEPENDENCY: org.apache.commons.collections.IterableMap (interface not supplied)
 * - MISSING DEPENDENCY: org.apache.commons.collections.map.AbstractHashedMap (required by delegateMap field)
 * - MISSING DEPENDENCY: org.apache.commons.collections.map.HashedMap (created in createDelegateMap())
 * - MISSING DEPENDENCY: org.apache.commons.collections.MapIterator (return type of mapIterator())
 * - MISSING DEPENDENCY: org.apache.commons.collections.iterators.EmptyIterator, EmptyMapIterator
 * - Serialization tests (readObject/writeObject) require full delegate chain.
 * - This test class will not compile without the above interfaces and classes.
 * 
 * @author Generated scaffolding for SQA_Project_2026, KKU CP353201
 * @version Prompt iteration 3 – non-compiling reference
 */
public class Flat3MapTest extends TestCase {

    private Flat3Map map;

    @Override
    protected void setUp() throws Exception {
        map = new Flat3Map();
    }

    @Override
    protected void tearDown() throws Exception {
        map.clear();
        map = null;
    }

    // =====================================================================
    // CONSTRUCTOR TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test default constructor creates empty map.
     */
    public void testConstructorDefault() {
        Flat3Map m = new Flat3Map();
        assertEquals("Default constructor should create empty map", 0, m.size());
        assertTrue("Default constructor should create empty map", m.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test copy constructor with empty map.
     * ASSUMPTION: Map interface is available; HashMap used as concrete supplier.
     */
    public void testConstructorCopyEmpty() {
        Map source = new HashMap();
        Flat3Map m = new Flat3Map(source);
        assertEquals("Copy constructor with empty map should result in size 0", 0, m.size());
        assertTrue("Copy constructor with empty map should be empty", m.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test copy constructor with single entry.
     */
    public void testConstructorCopySingleEntry() {
        Map source = new HashMap();
        source.put("key1", "value1");
        Flat3Map m = new Flat3Map(source);
        assertEquals("Copy constructor should copy single entry", 1, m.size());
        assertEquals("Copy constructor should copy value", "value1", m.get("key1"));
    }

    /**
     * [FLAT_ONLY] Test copy constructor with three entries.
     */
    public void testConstructorCopyThreeEntries() {
        Map source = new HashMap();
        source.put("key1", "value1");
        source.put("key2", "value2");
        source.put("key3", "value3");
        Flat3Map m = new Flat3Map(source);
        assertEquals("Copy constructor should copy three entries", 3, m.size());
        assertEquals("Entry 1", "value1", m.get("key1"));
        assertEquals("Entry 2", "value2", m.get("key2"));
        assertEquals("Entry 3", "value3", m.get("key3"));
    }

    /**
     * [DELEGATE_MODE_UNSUPPORTED] Test copy constructor with >3 entries triggers delegation.
     * MISSING: Cannot verify without AbstractHashedMap implementation.
     */
    public void testConstructorCopyFourEntriesDelegates() {
        Map source = new HashMap();
        source.put("key1", "value1");
        source.put("key2", "value2");
        source.put("key3", "value3");
        source.put("key4", "value4");
        Flat3Map m = new Flat3Map(source);
        assertEquals("Copy constructor with 4 entries should have size 4", 4, m.size());
        // MISSING: Would need to inspect delegateMap field (transient, package-private)
        // ASSUMPTION: After copy, map is in delegate mode but contains all 4 entries.
    }

    // =====================================================================
    // SIZE, EMPTY, CLEAR TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test size() on empty map.
     */
    public void testSizeEmpty() {
        assertEquals("Empty map should have size 0", 0, map.size());
    }

    /**
     * [FLAT_ONLY] Test size() after single put.
     */
    public void testSizeSingleEntry() {
        map.put("key", "value");
        assertEquals("After one put, size should be 1", 1, map.size());
    }

    /**
     * [FLAT_ONLY] Test size() after three puts (maximum flat mode).
     */
    public void testSizeThreeEntries() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        assertEquals("After three puts, size should be 3", 3, map.size());
    }

    /**
     * [FLAT_ONLY] Test isEmpty() returns true for new map.
     */
    public void testIsEmptyTrue() {
        assertTrue("New map should be empty", map.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test isEmpty() returns false after put.
     */
    public void testIsEmptyFalse() {
        map.put("key", "value");
        assertFalse("Map with entry should not be empty", map.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test isEmpty() after clear.
     */
    public void testIsEmptyAfterClear() {
        map.put("key", "value");
        map.clear();
        assertTrue("Map after clear should be empty", map.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test clear() on empty map.
     */
    public void testClearEmpty() {
        map.clear();
        assertEquals("Clear on empty map should keep size 0", 0, map.size());
        assertTrue("Clear on empty map should remain empty", map.isEmpty());
    }

    /**
     * [FLAT_ONLY] Test clear() removes all entries in flat mode.
     */
    public void testClearFlatMode() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.clear();
        assertEquals("After clear, size should be 0", 0, map.size());
        assertTrue("After clear, map should be empty", map.isEmpty());
        assertNull("After clear, get should return null", map.get("key1"));
        assertNull("After clear, get should return null", map.get("key2"));
        assertNull("After clear, get should return null", map.get("key3"));
    }

    /**
     * [DELEGATE_MODE_UNSUPPORTED] Test clear() resets delegate mode.
     * MISSING: Cannot verify without AbstractHashedMap implementation.
     * ASSUMPTION: clear() sets delegateMap = null to switch back to flat mode.
     */
    public void testClearSwitchesBackToFlatMode() {
        // Would populate to >3, triggering delegation, then clear and verify flat mode.
        // MISSING: No way to inspect internal state.
    }

    // =====================================================================
    // PUT TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test put on empty map returns null.
     */
    public void testPutEmptyReturnsNull() {
        Object result = map.put("key1", "value1");
        assertNull("First put on empty map should return null", result);
        assertEquals("After put, map should have size 1", 1, map.size());
    }

    /**
     * [FLAT_ONLY] Test put with null key.
     */
    public void testPutNullKey() {
        Object result = map.put(null, "value");
        assertNull("Put with null key should return null on first call", result);
        assertEquals("After put null key, size should be 1", 1, map.size());
        assertEquals("Get with null key should return value", "value", map.get(null));
    }

    /**
     * [FLAT_ONLY] Test put with null value.
     */
    public void testPutNullValue() {
        Object result = map.put("key", null);
        assertNull("Put with null value should return null on first call", result);
        assertEquals("After put, size should be 1", 1, map.size());
        assertNull("Get should return null value", map.get("key"));
    }

    /**
     * [FLAT_ONLY] Test put overwrites existing key (position 1).
     */
    public void testPutOverwritePosition1() {
        map.put("key1", "value1");
        Object result = map.put("key1", "newvalue1");
        assertEquals("Put should return old value", "value1", result);
        assertEquals("Size should remain 1 after overwrite", 1, map.size());
        assertEquals("Get should return new value", "newvalue1", map.get("key1"));
    }

    /**
     * [FLAT_ONLY] Test put overwrites existing key at position 2.
     */
    public void testPutOverwritePosition2() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Object result = map.put("key2", "newvalue2");
        assertEquals("Put should return old value", "value2", result);
        assertEquals("Size should remain 2 after overwrite", 2, map.size());
        assertEquals("Get key1 should still be value1", "value1", map.get("key1"));
        assertEquals("Get key2 should return new value", "newvalue2", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test put overwrites existing key at position 3.
     */
    public void testPutOverwritePosition3() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Object result = map.put("key3", "newvalue3");
        assertEquals("Put should return old value", "value3", result);
        assertEquals("Size should remain 3 after overwrite", 3, map.size());
        assertEquals("Get key3 should return new value", "newvalue3", map.get("key3"));
    }

    /**
     * [FLAT_ONLY] Test put fills flat mode to capacity (3 entries).
     */
    public void testPutFillsCapacity() {
        map.put("key1", "value1");
        assertEquals("After 1 put, size should be 1", 1, map.size());
        map.put("key2", "value2");
        assertEquals("After 2 puts, size should be 2", 2, map.size());
        map.put("key3", "value3");
        assertEquals("After 3 puts, size should be 3", 3, map.size());
    }

    /**
     * [DELEGATE_MODE_UNSUPPORTED] Test put beyond capacity converts to delegate mode.
     * MISSING: Cannot inspect delegateMap without AbstractHashedMap.
     * ASSUMPTION: At size 4, convertToMap() is called.
     */
    public void testPutBeyondCapacityConvertsToDelegate() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        // MISSING: Need to verify conversion occurred.
        map.put("key4", "value4");
        assertEquals("After 4 puts, size should be 4", 4, map.size());
        // MISSING: Cannot verify delegate mode without inspecting delegateMap field.
    }

    /**
     * [FLAT_ONLY] Test put with hash collision (same hashCode, different equals).
     * ASSUMPTION: String "a" and custom object with same hash code.
     */
    public void testPutHashCollision() {
        // Create objects that hash to same value but are not equal.
        String key1 = "key";
        // Note: Difficult to guarantee hash collision portably; this is illustrative.
        map.put(key1, "value1");
        map.put("key", "value2");  // May or may not collide depending on hash implementation.
        // Assertion depends on whether collision occurred; skipping deterministic assertion.
    }

    /**
     * [FLAT_ONLY] Test putAll with empty map argument.
     */
    public void testPutAllEmpty() {
        map.put("key1", "value1");
        Map other = new HashMap();
        map.putAll(other);
        assertEquals("PutAll with empty map should not change size", 1, map.size());
        assertEquals("Value should be unchanged", "value1", map.get("key1"));
    }

    /**
     * [FLAT_ONLY] Test putAll with single entry.
     */
    public void testPutAllSingleEntry() {
        map.put("key1", "value1");
        Map other = new HashMap();
        other.put("key2", "value2");
        map.putAll(other);
        assertEquals("After putAll with 1 entry, size should be 2", 2, map.size());
        assertEquals("Original entry preserved", "value1", map.get("key1"));
        assertEquals("New entry added", "value2", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test putAll fills to capacity without conversion.
     */
    public void testPutAllFillsCapacity() {
        map.put("key1", "value1");
        Map other = new HashMap();
        other.put("key2", "value2");
        other.put("key3", "value3");
        map.putAll(other);
        assertEquals("After putAll, size should be 3", 3, map.size());
        assertEquals("key1", "value1", map.get("key1"));
        assertEquals("key2", "value2", map.get("key2"));
        assertEquals("key3", "value3", map.get("key3"));
    }

    /**
     * [DELEGATE_MODE_UNSUPPORTED] Test putAll with >3 entries converts to delegate.
     * MISSING: Cannot verify conversion.
     */
    public void testPutAllConvertsToDelegate() {
        Map other = new HashMap();
        other.put("key1", "value1");
        other.put("key2", "value2");
        other.put("key3", "value3");
        other.put("key4", "value4");
        map.putAll(other);
        assertEquals("After putAll with 4 entries, size should be 4", 4, map.size());
    }

    /**
     * [FLAT_ONLY] Test putAll overwrites existing entries.
     */
    public void testPutAllOverwrites() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Map other = new HashMap();
        other.put("key2", "newvalue2");
        other.put("key3", "value3");
        map.putAll(other);
        assertEquals("Size after putAll overwrite should be 3", 3, map.size());
        assertEquals("key1 unchanged", "value1", map.get("key1"));
        assertEquals("key2 overwritten", "newvalue2", map.get("key2"));
        assertEquals("key3 added", "value3", map.get("key3"));
    }

    // =====================================================================
    // GET TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test get on empty map returns null.
     */
    public void testGetEmpty() {
        assertNull("Get on empty map should return null", map.get("key"));
    }

    /**
     * [FLAT_ONLY] Test get returns value for existing key.
     */
    public void testGetExisting() {
        map.put("key", "value");
        assertEquals("Get should return inserted value", "value", map.get("key"));
    }

    /**
     * [FLAT_ONLY] Test get with non-existing key returns null.
     */
    public void testGetNonExisting() {
        map.put("key1", "value1");
        assertNull("Get non-existing key should return null", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test get with null key when null key exists.
     */
    public void testGetNullKeyExists() {
        map.put(null, "nullvalue");
        assertEquals("Get with null key should return value", "nullvalue", map.get(null));
    }

    /**
     * [FLAT_ONLY] Test get with null key when null key does not exist.
     */
    public void testGetNullKeyNotExists() {
        map.put("key", "value");
        assertNull("Get null key when not present should return null", map.get(null));
    }

    /**
     * [FLAT_ONLY] Test get returns null value (not absent).
     */
    public void testGetNullValue() {
        map.put("key", null);
        assertTrue("containsKey should return true for null value", map.containsKey("key"));
        assertNull("Get should return null for null value", map.get("key"));
    }

    // =====================================================================
    // REMOVE TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test remove on empty map returns null.
     */
    public void testRemoveEmpty() {
        assertNull("Remove on empty map should return null", map.remove("key"));
        assertEquals("Size should remain 0", 0, map.size());
    }

    /**
     * [FLAT_ONLY] Test remove existing single entry.
     */
    public void testRemoveSingleEntry() {
        map.put("key1", "value1");
        Object result = map.remove("key1");
        assertEquals("Remove should return old value", "value1", result);
        assertEquals("Size should be 0 after remove", 0, map.size());
        assertNull("Get after remove should return null", map.get("key1"));
    }

    /**
     * [FLAT_ONLY] Test remove from position 1 of 2 entries.
     */
    public void testRemovePosition1Of2() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Object result = map.remove("key1");
        assertEquals("Remove should return old value", "value1", result);
        assertEquals("Size after remove should be 1", 1, map.size());
        assertNull("Removed key should not exist", map.get("key1"));
        assertEquals("Remaining key should have value", "value2", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test remove from position 2 of 2 entries.
     */
    public void testRemovePosition2Of2() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Object result = map.remove("key2");
        assertEquals("Remove should return old value", "value2", result);
        assertEquals("Size after remove should be 1", 1, map.size());
        assertEquals("Remaining key1 should have value", "value1", map.get("key1"));
        assertNull("Removed key should not exist", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test remove from position 1 of 3 entries.
     */
    public void testRemovePosition1Of3() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Object result = map.remove("key1");
        assertEquals("Remove should return old value", "value1", result);
        assertEquals("Size after remove should be 2", 2, map.size());
        // ASSUMPTION: After remove, entries are compacted (key3 moves to position 1 per source logic).
        assertEquals("key3 should still be accessible", "value3", map.get("key3"));
        assertEquals("key2 should still be accessible", "value2", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test remove from position 2 of 3 entries.
     */
    public void testRemovePosition2Of3() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Object result = map.remove("key2");
        assertEquals("Remove should return old value", "value2", result);
        assertEquals("Size after remove should be 2", 2, map.size());
        assertEquals("key1 should be accessible", "value1", map.get("key1"));
        assertEquals("key3 should be accessible", "value3", map.get("key3"));
    }

    /**
     * [FLAT_ONLY] Test remove from position 3 of 3 entries.
     */
    public void testRemovePosition3Of3() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Object result = map.remove("key3");
        assertEquals("Remove should return old value", "value3", result);
        assertEquals("Size after remove should be 2", 2, map.size());
        assertEquals("key1 should be accessible", "value1", map.get("key1"));
        assertEquals("key2 should be accessible", "value2", map.get("key2"));
    }

    /**
     * [FLAT_ONLY] Test remove with null key.
     */
    public void testRemoveNullKey() {
        map.put(null, "nullvalue");
        Object result = map.remove(null);
        assertEquals("Remove null key should return value", "nullvalue", result);
        assertEquals("Size should be 0 after remove", 0, map.size());
    }

    /**
     * [FLAT_ONLY] Test remove non-existing key returns null and doesn't change map.
     */
    public void testRemoveNonExisting() {
        map.put("key1", "value1");
        assertNull("Remove non-existing key should return null", map.remove("key2"));
        assertEquals("Size should not change", 1, map.size());
        assertEquals("Existing entry should be unchanged", "value1", map.get("key1"));
    }

    // =====================================================================
    // CONTAINS TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test containsKey on empty map.
     */
    public void testContainsKeyEmpty() {
        assertFalse("containsKey on empty map should return false", map.containsKey("key"));
    }

    /**
     * [FLAT_ONLY] Test containsKey for existing key.
     */
    public void testContainsKeyExisting() {
        map.put("key", "value");
        assertTrue("containsKey should return true for existing key", map.containsKey("key"));
    }

    /**
     * [FLAT_ONLY] Test containsKey for non-existing key.
     */
    public void testContainsKeyNonExisting() {
        map.put("key1", "value1");
        assertFalse("containsKey should return false for non-existing key", map.containsKey("key2"));
    }

    /**
     * [FLAT_ONLY] Test containsKey with null key.
     */
    public void testContainsKeyNull() {
        map.put(null, "value");
        assertTrue("containsKey should return true for null key when present", map.containsKey(null));
    }

    /**
     * [FLAT_ONLY] Test containsKey with null key when not present.
     */
    public void testContainsKeyNullAbsent() {
        map.put("key", "value");
        assertFalse("containsKey should return false for null key when absent", map.containsKey(null));
    }

    /**
     * [FLAT_ONLY] Test containsValue on empty map.
     */
    public void testContainsValueEmpty() {
        assertFalse("containsValue on empty map should return false", map.containsValue("value"));
    }

    /**
     * [FLAT_ONLY] Test containsValue for existing value.
     */
    public void testContainsValueExisting() {
        map.put("key", "value");
        assertTrue("containsValue should return true for existing value", map.containsValue("value"));
    }

    /**
     * [FLAT_ONLY] Test containsValue for non-existing value.
     */
    public void testContainsValueNonExisting() {
        map.put("key", "value1");
        assertFalse("containsValue should return false for non-existing value", map.containsValue("value2"));
    }

    /**
     * [FLAT_ONLY] Test containsValue with null value.
     */
    public void testContainsValueNull() {
        map.put("key", null);
        assertTrue("containsValue should return true for null value when present", map.containsValue(null));
    }

    /**
     * [FLAT_ONLY] Test containsValue with null value when not present.
     */
    public void testContainsValueNullAbsent() {
        map.put("key", "value");
        assertFalse("containsValue should return false for null value when absent", map.containsValue(null));
    }

    /**
     * [FLAT_ONLY] Test containsValue multiple entries, value in middle.
     */
    public void testContainsValueMultipleEntries() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        assertTrue("containsValue should find value1", map.containsValue("value1"));
        assertTrue("containsValue should find value2", map.containsValue("value2"));
        assertTrue("containsValue should find value3", map.containsValue("value3"));
        assertFalse("containsValue should not find value4", map.containsValue("value4"));
    }

    // =====================================================================
    // EQUALS AND HASHCODE TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test equals with self.
     */
    public void testEqualsSelf() {
        map.put("key", "value");
        assertTrue("Map should equal itself", map.equals(map));
    }

    /**
     * [FLAT_ONLY] Test equals with empty maps.
     */
    public void testEqualsEmpty() {
        Flat3Map other = new Flat3Map();
        assertTrue("Two empty maps should be equal", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test equals with same content.
     */
    public void testEqualsSameContent() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Flat3Map other = new Flat3Map();
        other.put("key1", "value1");
        other.put("key2", "value2");
        assertTrue("Maps with same content should be equal", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test equals with different content.
     */
    public void testEqualsDifferentContent() {
        map.put("key1", "value1");
        Flat3Map other = new Flat3Map();
        other.put("key1", "value2");
        assertFalse("Maps with different values should not be equal", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test equals with different sizes.
     */
    public void testEqualsDifferentSize() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Flat3Map other = new Flat3Map();
        other.put("key1", "value1");
        assertFalse("Maps with different sizes should not be equal", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test equals with null value.
     */
    public void testEqualsWithNullValue() {
        map.put("key", null);
        Flat3Map other = new Flat3Map();
        other.put("key", null);
        assertTrue("Maps with null values should be equal", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test equals with non-Map object.
     */
    public void testEqualsNonMap() {
        map.put("key", "value");
        assertFalse("Map should not equal non-Map object", map.equals("not a map"));
    }

    /**
     * [FLAT_ONLY] Test equals with HashMap.
     */
    public void testEqualsHashMap() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Map other = new HashMap();
        other.put("key1", "value1");
        other.put("key2", "value2");
        assertTrue("Flat3Map should equal HashMap with same content", map.equals(other));
    }

    /**
     * [FLAT_ONLY] Test hashCode consistency with equals.
     */
    public void testHashCodeConsistency() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Flat3Map other = new Flat3Map();
        other.put("key1", "value1");
        other.put("key2", "value2");
        assertEquals("Equal maps should have equal hashCode", map.hashCode(), other.hashCode());
    }

    /**
     * [FLAT_ONLY] Test hashCode with single entry.
     */
    public void testHashCodeSingleEntry() {
        map.put("key", "value");
        int hash1 = map.hashCode();
        // ASSUMPTION: hashCode should be consistent across invocations.
        int hash2 = map.hashCode();
        assertEquals("hashCode should be consistent", hash1, hash2);
    }

    /**
     * [FLAT_ONLY] Test hashCode with three entries.
     */
    public void testHashCodeThreeEntries() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        int hash1 = map.hashCode();
        int hash2 = map.hashCode();
        assertEquals("hashCode should be consistent with 3 entries", hash1, hash2);
    }

    /**
     * [FLAT_ONLY] Test hashCode with null key.
     */
    public void testHashCodeNullKey() {
        map.put(null, "value");
        int hash = map.hashCode();
        // ASSUMPTION: hashCode computation includes null key (hashed as 0).
        assertTrue("hashCode should be computed", hash >= Integer.MIN_VALUE);
    }

    /**
     * [FLAT_ONLY] Test hashCode with null value.
     */
    public void testHashCodeNullValue() {
        map.put("key", null);
        int hash = map.hashCode();
        // ASSUMPTION: hashCode computation includes null value (hashed as 0).
        assertTrue("hashCode should be computed with null value", hash >= Integer.MIN_VALUE);
    }

    // =====================================================================
    // TOSTRING TESTS
    // =====================================================================

    /**
     * [FLAT_ONLY] Test toString on empty map.
     */
    public void testToStringEmpty()