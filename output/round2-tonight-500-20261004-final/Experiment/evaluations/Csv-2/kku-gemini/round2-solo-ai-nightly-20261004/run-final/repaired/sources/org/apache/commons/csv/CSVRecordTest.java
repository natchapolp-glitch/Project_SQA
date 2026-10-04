package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = new String[] { "A", "B", "C" };
        CSVRecord record = new CSVRecord(values, null, "comment", 1L);

        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
        assertEquals("comment", record.getComment());
        assertEquals(1L, record.getRecordNumber());
        assertEquals(3, record.size());
    }

    @Test
    public void testGetByName() {
        String[] values = new String[] { "Val1", "Val2" };
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("First", 0);
        mapping.put("Second", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 2L);

        assertEquals("Val1", record.get("First"));
        assertEquals("Val2", record.get("Second"));
        assertNull(record.get("NonExistent"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = new String[] { "Val1" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("First");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] { "A", "B" };
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);

        CSVRecord consistentRecord = new CSVRecord(values, mapping, null, 1L);
        assertTrue(consistentRecord.isConsistent());

        Map<String, Integer> inconsistentMapping = new HashMap<String, Integer>(mapping);
        inconsistentMapping.put("Col3", 2);
        CSVRecord inconsistentRecord = new CSVRecord(values, inconsistentMapping, null, 2L);
        assertFalse(inconsistentRecord.isConsistent());

        CSVRecord noMappingRecord = new CSVRecord(values, null, null, 3L);
        assertTrue(noMappingRecord.isConsistent());
    }

    @Test
    public void testIsMappedAndIsSet() {
        String[] values = new String[] { "Val1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("Present", 0);
        mapping.put("OutOfBounds", 5);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("Present"));
        assertTrue(record.isMapped("OutOfBounds"));
        assertFalse(record.isMapped("Missing"));

        assertTrue(record.isSet("Present"));
        assertFalse(record.isSet("OutOfBounds"));
        assertFalse(record.isSet("Missing"));
    }

    @Test
    public void testIteratorAndToString() {
        String[] values = new String[] { "X", "Y" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("X", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Y", iterator.next());
        assertFalse(iterator.hasNext());

        assertEquals(Arrays.toString(values), record.toString());
    }
}
