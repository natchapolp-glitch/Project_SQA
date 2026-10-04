package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class CSVParserAdditionalTest {

    @Test
    public void testFirstLineHeaderWithSkipHeaderRecord() throws IOException {
        String code = "Col1,Col2\nVal1,Val2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(true);
        CSVParser parser = CSVParser.parse(code, format);
        
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertEquals(Integer.valueOf(0), headerMap.get("Col1"));
        assertEquals(Integer.valueOf(1), headerMap.get("Col2"));
        
        parser.close();
    }
}
