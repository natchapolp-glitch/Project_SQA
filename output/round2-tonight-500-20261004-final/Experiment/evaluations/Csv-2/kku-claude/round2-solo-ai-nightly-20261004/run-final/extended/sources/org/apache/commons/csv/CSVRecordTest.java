package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    private Map<String, Integer> createMapping(String... names) {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        for (int i = 0; i < names.length; i++) {
            mapping.put(names[i], i);
        }
        return mapping;
    }

    @Test
    public void testGetByIndexAndOutOfBounds() {
        String[] values = {"one", "two", "three"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertEquals("one", record.get(0));
        assertEquals("three", record.get(2));
        try {
            record.get(3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetByNameNoMappingThrows() {
        String[] values = {"a", "b"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        try {
            record.get("name");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetByNameMappedValue() {
        String[] values = {"a", "b"};
        Map<String, Integer> mapping = createMapping("col1", "col2");
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("a", record.get("col1"));
        assertEquals("b", record.get("col2"));
        assertNull(record.get("missing"));
    }

    @Test
    public void testIsMapped() {
        String[] values = {"a"};
        Map<String, Integer> mapping = createMapping("col1");
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));

        CSVRecord noMapping = new CSVRecord(values, null, null, 1L);
        assertFalse(noMapping.isMapped("col1"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"a"};
        Map<String, Integer> mapping = createMapping("col1", "col2");
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("col2"));
        assertFalse(record.isSet("missing"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"a", "b"};
        Map<String, Integer> consistentMapping = createMapping("col1", "col2");
        CSVRecord consistent = new CSVRecord(values, consistentMapping, null, 1L);
        assertTrue(consistent.isConsistent());

        Map<String, Integer> inconsistentMapping = createMapping("col1", "col2", "col3");
        CSVRecord inconsistent = new CSVRecord(values, inconsistentMapping, null, 1L);
        assertFalse(inconsistent.isConsistent());

        CSVRecord noMapping = new CSVRecord(values, null, null, 1L);
        assertTrue(noMapping.isConsistent());
    }

    @Test
    public void testIteratorAndValuesAndSize() {
        String[] values = {"x", "y", "z"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Iterator<String> it = record.iterator();
        assertTrue(it.hasNext());
        assertEquals("x", it.next());
        assertEquals("y", it.next());
        assertEquals("z", it.next());
        assertFalse(it.hasNext());

        assertEquals(3, record.size());
        assertTrue(Arrays.equals(values, record.values()));

        CSVRecord empty = new CSVRecord(new String[0], null, null, 1L);
        assertFalse(empty.iterator().hasNext());
        assertEquals(0, empty.size());
    }

    @Test
    public void testGetCommentAndRecordNumberAndToString() {
        String[] values = {"a", "b"};
        CSVRecord record = new CSVRecord(values, null, "a comment", 42L);
        assertEquals("a comment", record.getComment());
        assertEquals(42L, record.getRecordNumber());
        assertEquals(Arrays.toString(values), record.toString());

        CSVRecord noComment = new CSVRecord(values, null, null, 0L);
        assertNull(noComment.getComment());
        assertEquals(0L, noComment.getRecordNumber());
    }
}
