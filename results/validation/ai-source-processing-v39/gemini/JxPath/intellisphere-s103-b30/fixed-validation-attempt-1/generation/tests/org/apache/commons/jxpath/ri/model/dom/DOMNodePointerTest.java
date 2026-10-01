package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathContext;
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

    public void testBasics() {
        Element root = document.createElement("root");
        DOMNodePointer pointer = new DOMNodePointer(root, Locale.US);

        assertEquals(1, pointer.getLength());
        assertFalse(pointer.isCollection());
        assertTrue(pointer.isActual());
        assertSame(root, pointer.getBaseValue());
        assertSame(root, pointer.getImmediateNode());
        assertTrue(pointer.isLeaf());

        root.appendChild(document.createElement("child"));
        assertFalse(pointer.isLeaf());
    }

    public void testGetName() {
        Element root = document.createElementNS("http://example.com/ns", "ex:root");
        DOMNodePointer rootPointer = new DOMNodePointer(root, Locale.US);
        QName qName = rootPointer.getName();
        assertEquals("ex", qName.getPrefix());
        assertEquals("root", qName.getName());

        ProcessingInstruction pi = document.createProcessingInstruction("test-target", "data");
        DOMNodePointer piPointer = new DOMNodePointer(pi, Locale.US);
        QName piName = piPointer.getName();
        assertNull(piName.getPrefix());
        assertEquals("test-target", piName.getName());
    }

    public void testGetNamespaceURI() {
        Element root = document.createElementNS("http://example.com/ns", "ex:root");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:other", "http://other.com/ns");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://default.com/ns");
        document.appendChild(root);

        DOMNodePointer pointer = new DOMNodePointer(root, Locale.US);
        assertEquals(DOMNodePointer.XML_NAMESPACE_URI, pointer.getNamespaceURI("xml"));
        assertEquals(DOMNodePointer.XMLNS_NAMESPACE_URI, pointer.getNamespaceURI("xmlns"));
        assertEquals("http://other.com/ns", pointer.getNamespaceURI("other"));
        assertEquals("http://default.com/ns", pointer.getDefaultNamespaceURI());
        assertEquals("http://default.com/ns", pointer.getNamespaceURI(""));
        assertNull(pointer.getNamespaceURI("unregistered"));
    }

    public void testNodeTest() {
        Element elem = document.createElementNS("http://example.com/ns", "test");
        document.appendChild(elem);
        DOMNodePointer elemPtr = new DOMNodePointer(elem, Locale.US);

        assertTrue(elemPtr.testNode(null));
        assertTrue(elemPtr.testNode(new NodeNameTest(new QName("test"), "http://example.com/ns")));
        assertFalse(elemPtr.testNode(new NodeNameTest(new QName("other"), "http://example.com/ns")));
        assertTrue(elemPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse(elemPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));

        Text textNode = document.createTextNode("content");
        DOMNodePointer textPtr = new DOMNodePointer(textNode, Locale.US);
        assertTrue(textPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertFalse(textPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));

        ProcessingInstruction pi = document.createProcessingInstruction("target", "data");
        DOMNodePointer piPtr = new DOMNodePointer(pi, Locale.US);
        assertTrue(piPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_PI)));
        assertTrue(piPtr.testNode(new ProcessingInstructionTest("target")));
        assertFalse(piPtr.testNode(new ProcessingInstructionTest("non-matching")));
    }

    public void testLanguage() {
        Element root = document.createElement("root");
        root.setAttribute("xml:lang", "en-US");
        Element child = document.createElement("child");
        root.appendChild(child);
        document.appendChild(root);

        DOMNodePointer rootPointer = new DOMNodePointer(root, Locale.US);
        DOMNodePointer childPointer = new DOMNodePointer(rootPointer, child);

        assertTrue(rootPointer.isLanguage("en"));
        assertTrue(rootPointer.isLanguage("EN"));
        assertTrue(rootPointer.isLanguage("en-US"));
        assertFalse(rootPointer.isLanguage("fr"));

        assertTrue(childPointer.isLanguage("en"));
        assertEquals("en-US", childPointer.getLanguage());
    }

    public void testGetValueAndSetValue() {
        Element elem = document.createElement("root");
        document.appendChild(elem);
        DOMNodePointer elemPtr = new DOMNodePointer(elem, Locale.US);

        elemPtr.setValue("Hello World");
        assertEquals("Hello World", elemPtr.getValue());

        Comment comment = document.createComment(" a comment ");
        DOMNodePointer commentPtr = new DOMNodePointer(comment, Locale.US);
        assertEquals("a comment", commentPtr.getValue());

        ProcessingInstruction pi = document.createProcessingInstruction("target", " pi data ");
        DOMNodePointer piPtr = new DOMNodePointer(pi, Locale.US);
        assertEquals("pi data", piPtr.getValue());
    }

    public void testAsPath() {
        Element root = document.createElement("root");
        document.appendChild(root);
        DOMNodePointer rootPtr = new DOMNodePointer(root, Locale.US);
        assertEquals("", rootPtr.asPath());

        DOMNodePointer idPtr = new DOMNodePointer(root, Locale.US, "my'id");
        assertEquals("id('my&apos;id')", idPtr.asPath());

        Element child1 = document.createElement("item");
        Element child2 = document.createElement("item");
        root.appendChild(child1);
        root.appendChild(child2);

        DOMNodePointer childPtr1 = new DOMNodePointer(rootPtr, child1);
        DOMNodePointer childPtr2 = new DOMNodePointer(rootPtr, child2);
        assertEquals("/item[1]", childPtr1.asPath());
        assertEquals("/item[2]", childPtr2.asPath());

        Text text = document.createTextNode("text");
        child1.appendChild(text);
        DOMNodePointer textPtr = new DOMNodePointer(childPtr1, text);
        assertEquals("/item[1]/text()[1]", textPtr.asPath());

        ProcessingInstruction pi = document.createProcessingInstruction("target", "data");
        child1.appendChild(pi);
        DOMNodePointer piPtr = new DOMNodePointer(childPtr1, pi);
        assertEquals("/item[1]/processing-instruction('target')[1]", piPtr.asPath());
    }

    public void testCompareChildNodePointers() {
        Element root = document.createElement("root");
        root.setAttribute("attr1", "v1");
        root.setAttribute("attr2", "v2");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        DOMNodePointer rootPtr = new DOMNodePointer(root, Locale.US);
        DOMNodePointer ptr1 = new DOMNodePointer(rootPtr, child1);
        DOMNodePointer ptr2 = new DOMNodePointer(rootPtr, child2);

        assertEquals(-1, rootPtr.compareChildNodePointers(ptr1, ptr2));
        assertEquals(1, rootPtr.compareChildNodePointers(ptr2, ptr1));
        assertEquals(0, rootPtr.compareChildNodePointers(ptr1, ptr1));

        DOMNodePointer attrPtr = (DOMNodePointer) rootPtr.createAttribute(JXPathContext.newContext(root), new QName("attr1"));
        assertEquals(-1, rootPtr.compareChildNodePointers(attrPtr, ptr1));
        assertEquals(1, rootPtr.compareChildNodePointers(ptr1, attrPtr));
    }

    public void testRemove() {
        Element root = document.createElement("root");
        Element child = document.createElement("child");
        root.appendChild(child);

        DOMNodePointer rootPtr = new DOMNodePointer(root, Locale.US);
        try {
            rootPtr.remove();
            fail("Should throw JXPathException when removing root DOM node");
        }
        catch (JXPathException expected) {
            // expected
        }

        DOMNodePointer childPtr = new DOMNodePointer(rootPtr, child);
        childPtr.remove();
        assertEquals(0, root.getChildNodes().getLength());
    }

    public void testEqualsAndHashCode() {
        Element root1 = document.createElement("root");
        Element root2 = document.createElement("root");
        DOMNodePointer ptr1a = new DOMNodePointer(root1, Locale.US);
        DOMNodePointer ptr1b = new DOMNodePointer(root1, Locale.US);
        DOMNodePointer ptr2 = new DOMNodePointer(root2, Locale.US);

        assertTrue(ptr1a.equals(ptr1a));
        assertTrue(ptr1a.equals(ptr1b));
        assertFalse(ptr1a.equals(ptr2));
        assertFalse(ptr1a.equals("string"));
        assertFalse(ptr1a.equals(null));
        assertEquals(ptr1a.hashCode(), ptr1b.hashCode());
    }

    public void testStaticGetLocalNameAndPrefix() {
        Element prefixed = document.createElementNS("http://example.com/ns", "pfx:elementName");
        assertEquals("pfx", DOMNodePointer.getPrefix(prefixed));
        assertEquals("elementName", DOMNodePointer.getLocalName(prefixed));

        Element unprefixed = document.createElement("simple");
        assertNull(DOMNodePointer.getPrefix(unprefixed));
        assertEquals("simple", DOMNodePointer.getLocalName(unprefixed));
    }
}
