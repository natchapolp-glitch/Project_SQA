package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DOMNodePointerTest extends TestCase {

    private Document document;

    protected void setUp() throws Exception {
        super.setUp();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        document = factory.newDocumentBuilder().newDocument();
    }

    public void testBasicProperties() {
        Element root = document.createElement("root");
        document.appendChild(root);
        DOMNodePointer pointer = new DOMNodePointer(root, Locale.US);

        assertEquals("Length must be 1", 1, pointer.getLength());
        assertFalse("NodePointer should not be a collection", pointer.isCollection());
        assertTrue("NodePointer should be actual", pointer.isActual());
        assertTrue("Leaf should be true when no children exist", pointer.isLeaf());

        root.appendChild(document.createTextNode("text"));
        assertFalse("Leaf should be false when children exist", pointer.isLeaf());
    }

    public void testEqualsAndHashCode() {
        Element elem1 = document.createElement("a");
        Element elem2 = document.createElement("a");

        DOMNodePointer ptr1 = new DOMNodePointer(elem1, Locale.US);
        DOMNodePointer ptr1Same = new DOMNodePointer(elem1, Locale.US);
        DOMNodePointer ptr2 = new DOMNodePointer(elem2, Locale.US);

        assertTrue("Reflexive equality", ptr1.equals(ptr1));
        assertTrue("Equal when wrapping the exact same Node", ptr1.equals(ptr1Same));
        assertFalse("Not equal when wrapping different Nodes", ptr1.equals(ptr2));
        assertFalse("Not equal to non-DOMNodePointer", ptr1.equals("a"));
        assertEquals("HashCode should match identityHashCode of node",
                System.identityHashCode(elem1), ptr1.hashCode());
    }

    public void testGetLocalNameAndPrefix() {
        Element elem = document.createElementNS("http://example.com/ns", "p:testNode");
        assertEquals("p", DOMNodePointer.getPrefix(elem));
        assertEquals("testNode", DOMNodePointer.getLocalName(elem));

        Element simpleElem = document.createElement("plain");
        assertNull("Prefix should be null when not qualified", DOMNodePointer.getPrefix(simpleElem));
        assertEquals("plain", DOMNodePointer.getLocalName(simpleElem));
    }

    public void testGetName() {
        Element elem = document.createElementNS("http://example.com/ns", "ns1:elem");
        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.US);
        QName name = pointer.getName();
        assertEquals("ns1", name.getPrefix());
        assertEquals("elem", name.getName());

        ProcessingInstruction pi = document.createProcessingInstruction("myTarget", "myData");
        DOMNodePointer piPointer = new DOMNodePointer(pi, Locale.US);
        assertEquals("myTarget", piPointer.getName().getName());
    }

    public void testTestNodeTypes() {
        Element elem = document.createElement("elem");
        Text text = document.createTextNode("sample");
        Comment comment = document.createComment("comment");
        ProcessingInstruction pi = document.createProcessingInstruction("piTarget", "data");

        assertTrue(DOMNodePointer.testNode(elem, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertTrue(DOMNodePointer.testNode(text, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertTrue(DOMNodePointer.testNode(comment, new NodeTypeTest(Compiler.NODE_TYPE_COMMENT)));
        assertTrue(DOMNodePointer.testNode(pi, new NodeTypeTest(Compiler.NODE_TYPE_PI)));

        assertFalse(DOMNodePointer.testNode(text, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse(DOMNodePointer.testNode(elem, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
    }

    public void testTestNodeName() {
        Element elem = document.createElementNS("http://example.com/ns", "p:elem");

        NodeNameTest nameTest = new NodeNameTest(new QName("p", "elem"), "http://example.com/ns");
        assertTrue("Matching element name and namespace URI", DOMNodePointer.testNode(elem, nameTest));

        NodeNameTest wildcardTest = new NodeNameTest(new QName(null, "*"));
        assertTrue("Wildcard name test without prefix should match any element",
                DOMNodePointer.testNode(elem, wildcardTest));

        NodeNameTest mismatchTest = new NodeNameTest(new QName("p", "other"), "http://example.com/ns");
        assertFalse("Mismatched local name should fail", DOMNodePointer.testNode(elem, mismatchTest));
    }

    public void testProcessingInstructionTest() {
        ProcessingInstruction pi = document.createProcessingInstruction("targetX", "val");
        ProcessingInstructionTest match = new ProcessingInstructionTest("targetX");
        ProcessingInstructionTest mismatch = new ProcessingInstructionTest("targetY");

        assertTrue(DOMNodePointer.testNode(pi, match));
        assertFalse(DOMNodePointer.testNode(pi, mismatch));
    }

    public void testGetValueAndSetValueOnElement() {
        Element elem = document.createElement("textElem");
        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.US);

        pointer.setValue("Hello World");
        assertEquals("Hello World", pointer.getValue());

        pointer.setValue("Replaced");
        assertEquals("Replaced", pointer.getValue());
    }

    public void testIsLanguage() {
        Element root = document.createElement("root");
        root.setAttribute("xml:lang", "en-US");
        Element child = document.createElement("child");
        root.appendChild(child);

        DOMNodePointer childPointer = new DOMNodePointer(child, Locale.US);
        assertTrue(childPointer.isLanguage("en"));
        assertTrue(childPointer.isLanguage("en-US"));
        assertFalse(childPointer.isLanguage("fr"));
    }

    public void testAsPathWithId() {
        Element elem = document.createElement("test");
        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.US, "my'Id\"");
        assertEquals("id('my&apos;Id&quot;')", pointer.asPath());
    }

    

    public void testCompareChildNodePointers() {
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child1 = document.createElement("c1");
        Element child2 = document.createElement("c2");
        root.appendChild(child1);
        root.appendChild(child2);

        DOMNodePointer rootPointer = new DOMNodePointer(root, Locale.US);
        DOMNodePointer ptr1 = new DOMNodePointer(rootPointer, child1);
        DOMNodePointer ptr2 = new DOMNodePointer(rootPointer, child2);

        assertEquals(-1, rootPointer.compareChildNodePointers(ptr1, ptr2));
        assertEquals(1, rootPointer.compareChildNodePointers(ptr2, ptr1));
        assertEquals(0, rootPointer.compareChildNodePointers(ptr1, ptr1));
    }

    public void testRemove() {
        Element root = document.createElement("root");
        document.appendChild(root);
        Element child = document.createElement("child");
        root.appendChild(child);

        DOMNodePointer childPointer = new DOMNodePointer(child, Locale.US);
        childPointer.remove();
        assertEquals(0, root.getChildNodes().getLength());

        DOMNodePointer orphanPointer = new DOMNodePointer(root, Locale.US);
        orphanPointer.remove(); // removes root from document
        try {
            orphanPointer.remove(); // orphan node with null parent
            fail("Removing a parentless node should throw JXPathException");
        }
        catch (JXPathException expected) {
            // expected
        }
    }
}
