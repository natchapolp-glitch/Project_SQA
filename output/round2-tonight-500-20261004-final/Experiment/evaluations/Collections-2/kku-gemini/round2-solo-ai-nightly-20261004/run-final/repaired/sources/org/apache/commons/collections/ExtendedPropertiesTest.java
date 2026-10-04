package org.apache.commons.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.junit.Test;

public class ExtendedPropertiesTest {

    @Test
    public void testAddAndGetProperty() {
        ExtendedProperties props = new ExtendedProperties();
        props.addProperty("test.key", "value1");
        props.addProperty("test.key", "value2");

        assertEquals("value1, value2", props.getProperty("test.key"));
        assertEquals("value1, value2", props.getString("test.key"));

        List list = props.getList("test.key");
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testSetPropertyOverwrites() {
        ExtendedProperties props = new ExtendedProperties();
        props.setProperty("test.key", "initial");
        props.setProperty("test.key", "replaced");

        assertEquals("replaced", props.getString("test.key"));
    }

    @Test
    public void testClearProperty() {
        ExtendedProperties props = new ExtendedProperties();
        props.setProperty("test.key", "value");
        assertNotNull(props.getString("test.key"));

        props.clearProperty("test.key");
        assertNull(props.getString("test.key"));
        assertFalse(props.getKeys().hasNext());
    }

    @Test
    public void testSubset() {
        ExtendedProperties props = new ExtendedProperties();
        props.setProperty("app.name", "TestApp");
        props.setProperty("app.version", "1.0");
        props.setProperty("other.key", "value");

        ExtendedProperties subset = props.subset("app");
        assertNotNull(subset);
        assertEquals("TestApp", subset.getString("name"));
        assertEquals("1.0", subset.getString("version"));
        assertNull(subset.getString("other.key"));
    }

    @Test
    public void testCombine() {
        ExtendedProperties props1 = new ExtendedProperties();
        props1.setProperty("key1", "val1");

        ExtendedProperties props2 = new ExtendedProperties();
        props2.setProperty("key2", "val2");

        props1.combine(props2);

        assertEquals("val1", props1.getString("key1"));
        assertEquals("val2", props1.getString("key2"));
    }

    @Test
    public void testTypeGettersAndDefaults() {
        ExtendedProperties props = new ExtendedProperties();
        props.setProperty("prop.int", "42");
        props.setProperty("prop.bool", "true");
        props.setProperty("prop.double", "3.14");

        assertEquals(42, props.getInt("prop.int"));
        assertEquals(100, props.getInt("non.existent", 100));

        assertTrue(props.getBoolean("prop.bool"));
        assertFalse(props.getBoolean("non.existent", false));

        assertEquals(3.14, props.getDouble("prop.double"), 0.001);
        assertEquals(2.71, props.getDouble("non.existent", 2.71), 0.001);
    }

    @Test
    public void testLoadAndSaveStream() throws Exception {
        String content = "greeting=Hello\ntarget=World\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        ExtendedProperties props = new ExtendedProperties();
        props.load(bais);

        assertEquals("Hello", props.getString("greeting"));
        assertEquals("World", props.getString("target"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        props.save(baos, "Saved properties");
        assertTrue(baos.size() > 0);
    }

    @Test
    public void testConvertProperties() {
        Properties javaProps = new Properties();
        javaProps.setProperty("conversion.key", "conversion.value");

        ExtendedProperties props = ExtendedProperties.convertProperties(javaProps);
        assertNotNull(props);
        assertEquals("conversion.value", props.getString("conversion.key"));
    }

    @Test
    public void testGetKeysOrder() {
        ExtendedProperties props = new ExtendedProperties();
        props.setProperty("first", "1");
        props.setProperty("second", "2");

        Iterator it = props.getKeys();
        assertNotNull(it);
        assertTrue(it.hasNext());
        assertEquals("first", it.next());
        assertTrue(it.hasNext());
        assertEquals("second", it.next());
        assertFalse(it.hasNext());
    }
}
