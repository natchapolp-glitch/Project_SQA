package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DOMNodePointerTest extends TestCase {

    private Document doc;

    protected void setUp() throws Exception {
        super.setUp();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        doc = factory.newDocumentBuilder().newDocument();
    }

    public void testIsLeafAndCollectionAndLength() {
        Element root = doc.createElement("root");
        DOMNodePointer pointer = new DOMNodePointer(root, Locale.getDefault());

        assertTrue("Element with no children should be leaf", pointer.isLeaf());
        assertFalse("DOMNodePointer is not a collection", pointer.isCollection());
        assertEquals("Length should be 1", 1, pointer.getLength());
        assertTrue("DOMNodePointer should be actual", pointer.isActual());

        Text text = doc.createTextNode("hello");
        root.appendChild(text);
        assertFalse("Element with children should not be leaf", pointer.isLeaf());
    }

    public void testGetNameAndLocalName() {
        Element elem = doc.createElementNS("http://example.com/ns", "ex:item");
        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.getDefault());

        QName qName = pointer.getName();
        assertEquals("Prefix should match", "ex", qName.getPrefix());
        assertEquals("Local name should match", "item", qName.getName());

        assertEquals("Static getLocalName", "item", DOMNodePointer.getLocalName(elem));
        assertEquals("Static getPrefix", "ex", DOMNodePointer.getPrefix(elem));
    }

    public void testGetNamespaceURI() {
        Element root = doc.createElementNS("http://example.com/default", "root");
        root.setAttribute("xmlns:p", "http://example.com/prefix");
        doc.appendChild(root);

        DOMNodePointer pointer = new DOMNodePointer(root, Locale.getDefault());
        assertEquals("Namespace URI from node", "http://example.com/default", pointer.getNamespaceURI());
        assertEquals("XML namespace URI for xml prefix", DOMNodePointer.XML_NAMESPACE_URI, pointer.getNamespaceURI("xml"));
        assertEquals("XMLNS namespace URI for xmlns prefix", DOMNodePointer.XMLNS_NAMESPACE_URI, pointer.getNamespaceURI("xmlns"));
        assertEquals("Resolved prefix namespace", "http://example.com/prefix", pointer.getNamespaceURI("p"));
    }

    public void testGetValueAndSetValue() {
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Text text = doc.createTextNode("  initial  ");
        root.appendChild(text);

        DOMNodePointer rootPointer = new DOMNodePointer(root, Locale.getDefault());
        assertEquals("Element text content should be trimmed", "initial", rootPointer.getValue());

        DOMNodePointer textPointer = new DOMNodePointer(rootPointer, text);
        assertEquals("Text node value should be trimmed", "initial", textPointer.getValue());

        textPointer.setValue("updated");
        assertEquals("Updated text value", "updated", textPointer.getValue());
    }

    public void testAsPath() {
        Element root = doc.createElement("root");
        doc.appendChild(root);
        DOMNodePointer rootPointer = new DOMNodePointer(root, Locale.getDefault());

        Element child1 = doc.createElement("child");
        root.appendChild(child1);
        DOMNodePointer child1Pointer = new DOMNodePointer(rootPointer, child1);

        assertEquals("Root path when parent is null", "", rootPointer.asPath());
        assertEquals("Child path relative to root", "/child[1](undefined)", child1Pointer.asPath());

        DOMNodePointer idPointer = new DOMNodePointer(root, Locale.getDefault(), "elem1");
        assertEquals("Path with ID", "id('elem1')", idPointer.asPath());
    }

    public void testIsLanguage() {
        Element root = doc.createElement("root");
        root.setAttribute("xml:lang", "en-US");
        DOMNodePointer pointer = new DOMNodePointer(root, Locale.getDefault());

        assertTrue("Language en-US matches en", pointer.isLanguage("en"));
        assertTrue("Language en-US matches EN-US", pointer.isLanguage("EN-US"));
        assertFalse("Language en-US does not match fr", pointer.isLanguage("fr"));
    }

    public void testTestNode() {
        Element elem = doc.createElementNS("http://example.com", "item");
        DOMNodePointer pointer = new DOMNodePointer(elem, Locale.getDefault());

        assertTrue("Null test matches everything", pointer.testNode(null));
        assertTrue("NodeTypeTest NODE matches Element", pointer.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse("NodeTypeTest TEXT does not match Element", pointer.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));

        NodeNameTest nameTest = new NodeNameTest(new QName(null, "item"), "http://example.com");
        assertTrue("NodeNameTest matches name and NS", pointer.testNode(nameTest));

        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        DOMNodePointer piPointer = new DOMNodePointer(pi, Locale.getDefault());
        assertTrue("ProcessingInstructionTest matches PI", piPointer.testNode(new ProcessingInstructionTest("target")));
    }

    public void testEqualsAndHashCode() {
        Element root1 = doc.createElement("root");
        Element root2 = doc.createElement("root");

        DOMNodePointer p1 = new DOMNodePointer(root1, Locale.getDefault());
        DOMNodePointer p1Copy = new DOMNodePointer(root1, Locale.getDefault());
        DOMNodePointer p2 = new DOMNodePointer(root2, Locale.getDefault());

        assertEquals("Same underlying node pointers are equal", p1, p1Copy);
        assertFalse("Different underlying node pointers are not equal", p1.equals(p2));
        assertEquals("Same node yields same hashCode", p1.hashCode(), p1Copy.hashCode());
    }
}
