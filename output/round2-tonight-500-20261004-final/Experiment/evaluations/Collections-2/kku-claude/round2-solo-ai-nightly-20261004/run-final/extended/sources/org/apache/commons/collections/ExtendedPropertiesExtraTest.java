package org.apache.commons.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedPropertiesExtraTest {

    @Test
    public void testEscapeHandlesCommaAndBackslashInLoadedValue() throws Exception {
        String content = "commas.escaped = Hi\\, what'up?\n";
        ExtendedProperties ep = new ExtendedProperties();
        ep.load(new java.io.ByteArrayInputStream(content.getBytes("UTF-8")));
        assertEquals("Hi, what'up?", ep.getString("commas.escaped"));
    }

    @Test
    public void testCombineOverwritesAndMergesProperties() {
        ExtendedProperties ep1 = new ExtendedProperties();
        ep1.setProperty("key1", "value1");
        ep1.setProperty("key2", "value2");

        ExtendedProperties ep2 = new ExtendedProperties();
        ep2.setProperty("key2", "newvalue2");
        ep2.setProperty("key3", "value3");

        ep1.combine(ep2);

        assertEquals("value1", ep1.getString("key1"));
        assertEquals("newvalue2", ep1.getString("key2"));
        assertEquals("value3", ep1.getString("key3"));
    }

    @Test
    public void testConvertPropertiesFromJavaProperties() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("alpha", "1");
        props.setProperty("beta", "2");

        ExtendedProperties ep = ExtendedProperties.convertProperties(props);

        assertEquals("1", ep.getString("alpha"));
        assertEquals("2", ep.getString("beta"));
    }

    @Test
    public void testGetKeysWithPrefixReturnsOnlyMatchingSuffixes() {
        ExtendedProperties ep = new ExtendedProperties();
        ep.setProperty("db.driver", "mysql");
        ep.setProperty("db.url", "jdbc:mysql://localhost");
        ep.setProperty("other.key", "value");

        java.util.Iterator it = ep.getKeys("db");
        java.util.List<String> found = new java.util.ArrayList<String>();
        while (it.hasNext()) {
            found.add((String) it.next());
        }
        assertTrue(found.contains("db.driver"));
        assertTrue(found.contains("db.url"));
        assertFalse(found.contains("other.key"));
    }
}
