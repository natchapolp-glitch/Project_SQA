package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordExtraTest {

    private Map<String, Integer> createMapping(String... names) {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        for (int i = 0; i < names.length; i++) {
            mapping.put(names[i], i);
        }
        return mapping;
    }

    @Test
    public void testGetByNameWhenMappedIndexOutOfBoundsThrows() {
        // mapping declares col2 at index 1, but values array only has 1 element
        String[] values = {"a"};
        Map<String, Integer> mapping = createMapping("col1", "col2");
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // isSet correctly reports false for col2 since its index exceeds values.length
        assertFalse(record.isSet("col2"));

        // get("col1") still works fine as index 0 is within bounds
        assertEquals("a", record.get("col1"));

        // get("col2") attempts values[1] on a length-1 array -> should throw
        try {
            record.get("col2");
            fail("Expected ArrayIndexOutOfBoundsException when mapped index exceeds values length");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected per current (buggy) implementation which lacks bounds checking
        }
    }

    @Test
    public void testIsSetFalseForMissingName() {
        String[] values = {"a", "b"};
        Map<String, Integer> mapping = createMapping("col1", "col2");
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isSet("notThere"));
    }
}
