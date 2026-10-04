package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Text;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMNodePointerTest {

    @Test
    public void testInitializationAndBasicProperties() {
        Element elem = new Element("root", "http://example.com");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.ENGLISH, "id99");

        assertEquals(elem, pointer.getBaseValue());
        assertEquals(elem, pointer.getImmediateNode());
        assertFalse(pointer.isCollection());
        assertEquals(1, pointer.getLength());
        assertNotNull(pointer.getLanguage());
        assertFalse(pointer.isLanguage("de"));
    }

    @Test
    public void testTestNodeTypes() {
        Element elem = new Element("elem");
        Text text = new Text("text data");

        assertTrue(JDOMNodePointer.testNode(null, elem, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertTrue(JDOMNodePointer.testNode(null, text, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertFalse(JDOMNodePointer.testNode(null, text, new NodeTypeTest(Compiler.NODE_TYPE_COMMENT)));
    }

    @Test
    public void testGetNameAndNamespace() {
        Element elem = new Element("local", "prefix", "http://example.com/ns");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.getDefault());

        QName name = pointer.getName();
        assertEquals("local", name.getName());
        assertEquals("http://example.com/ns", pointer.getNamespaceURI());
    }

    @Test
    public void testIteratorsAndPointers() {
        Element elem = new Element("parent");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.getDefault());

        NodeIterator childIter = pointer.childIterator(null, false, null);
        assertNotNull(childIter);

        NodeIterator attrIter = pointer.attributeIterator(new QName("attr"));
        assertNotNull(attrIter);

        NodeIterator nsIter = pointer.namespaceIterator();
        assertNotNull(nsIter);

        NodePointer nsPtr = pointer.namespacePointer("xml");
        assertNotNull(nsPtr);
    }

    @Test
    public void testCompareChildNodePointers() {
        Element parent = new Element("parent");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        parent.addContent(child1);
        parent.addContent(child2);

        JDOMNodePointer parentPtr = new JDOMNodePointer(parent, Locale.getDefault());
        NodePointer p1 = new JDOMNodePointer(parentPtr, child1);
        NodePointer p2 = new JDOMNodePointer(parentPtr, child2);

        assertTrue(parentPtr.compareChildNodePointers(p1, p2) < 0);
        assertTrue(parentPtr.compareChildNodePointers(p2, p1) > 0);
        assertEquals(0, parentPtr.compareChildNodePointers(p1, p1));
    }
}
