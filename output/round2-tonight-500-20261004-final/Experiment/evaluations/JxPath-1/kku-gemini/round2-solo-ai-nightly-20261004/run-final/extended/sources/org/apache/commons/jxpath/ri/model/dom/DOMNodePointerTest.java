package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class DOMNodePointerTest {

    @Test
    public void testInitializationAndBasicProperties() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("root");
        doc.appendChild(elem);

        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.ENGLISH, "id123");
        assertEquals(elem, pointer.getBaseValue());
        assertEquals(elem, pointer.getImmediateNode());
        assertFalse(pointer.isCollection());
        assertEquals(1, pointer.getLength());
        assertFalse(pointer.isLanguage("fr"));
    }

    @Test
    public void testTestNodeTypes() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("element");
        Text text = doc.createTextNode("content");
        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");

        assertTrue(DOMNodePointer.testNode(elem, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse(DOMNodePointer.testNode(text, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertTrue(DOMNodePointer.testNode(text, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertTrue(DOMNodePointer.testNode(pi, new NodeTypeTest(Compiler.NODE_TYPE_PI)));
        assertTrue(DOMNodePointer.testNode(pi, new ProcessingInstructionTest("target")));
    }

    @Test
    public void testGetNameAndNamespace() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElementNS("http://example.com/ns", "prefix:local");
        doc.appendChild(elem);

        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.getDefault());
        QName name = pointer.getName();
        assertEquals("local", name.getName());
        assertEquals("prefix", name.getPrefix());
        assertEquals("http://example.com/ns", pointer.getNamespaceURI());
    }

    @Test
    public void testIteratorsAndPointers() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("parent");
        Element child = doc.createElement("child");
        elem.appendChild(child);
        doc.appendChild(elem);

        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.getDefault());
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
    public void testCompareChildNodePointers() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("parent");
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        elem.appendChild(child1);
        elem.appendChild(child2);
        doc.appendChild(elem);

        DOMNodePointer parentPtr = new DOMNodePointer(elem, Locale.getDefault());
        NodePointer p1 = new DOMNodePointer(parentPtr, child1);
        NodePointer p2 = new DOMNodePointer(parentPtr, child2);

        assertTrue(parentPtr.compareChildNodePointers(p1, p2) < 0);
        assertTrue(parentPtr.compareChildNodePointers(p2, p1) > 0);
        assertEquals(0, parentPtr.compareChildNodePointers(p1, p1));
    }
}
