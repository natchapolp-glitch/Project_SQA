package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NullPropertyPointer pointer;

    @Before
    public void setUp() {
        NodePointer root = NodePointer.newNodePointer(
                new QName(null, "root"), new Object(), java.util.Locale.getDefault());
        NullPointer nullParent = new NullPointer(root, new QName(null, "parent"));
        pointer = new NullPropertyPointer(nullParent);
    }

    @Test
    public void testDefaultPropertyName() {
        assertEquals("*", pointer.getPropertyName());
        assertEquals(new QName("*"), pointer.getName());
    }

    @Test
    public void testSetPropertyNameUpdatesNameAndAsPath() {
        pointer.setPropertyName("foo");
        assertEquals("foo", pointer.getPropertyName());
        assertEquals(new QName("foo"), pointer.getName());
    }

    @Test
    public void testBaseAndImmediateNodeAreNull() {
        assertNull(pointer.getBaseValue());
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeafAndIsActualAndIsContainer() {
        assertTrue(pointer.isLeaf());
        assertFalse(pointer.isActual());
        assertTrue(pointer.isContainer());
    }

    @Test
    public void testLengthAndPropertyCountAndNames() {
        assertEquals(0, pointer.getLength());
        assertEquals(0, pointer.getPropertyCount());
        assertNotNull(pointer.getPropertyNames());
        assertEquals(0, pointer.getPropertyNames().length);
    }

    @Test
    public void testIsCollectionDependsOnIndex() {
        assertFalse(pointer.isCollection());
        pointer.setPropertyIndex(2);
        assertTrue(pointer.isCollection());
    }

    @Test
    public void testSetNameAttributeValueAsPathEscaping() {
        pointer.setNameAttributeValue("a'b\"c");
        String path = pointer.asPath();
        assertTrue(path.contains("&apos;"));
        assertTrue(path.contains("&quot;"));
        assertTrue(path.endsWith("']"));
    }

    @Test
    public void testSetPropertyIndexWithNegativeAndLargeValues() {
        pointer.setPropertyIndex(-1);
        pointer.setPropertyIndex(1000000);
    }
}
