Rendered KKU IntelSphere response. Model label: Gemini - gemini-3.6-flash

```java
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

```

```java
package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;

public class JDOMNodePointerTest extends TestCase {

    public void testIsLeafAndCollectionAndLength() {
        Element element = new Element("root");
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());

        assertTrue("Empty Element is leaf", pointer.isLeaf());
        assertFalse("JDOMNodePointer is not a collection", pointer.isCollection());
        assertEquals("Length should be 1", 1, pointer.getLength());

        element.addContent(new Element("child"));
        assertFalse("Element with children is not leaf", pointer.isLeaf());
    }

    public void testGetNameAndLocalName() {
        Namespace ns = Namespace.getNamespace("ex", "http://example.com/ns");
        Element element = new Element("item", ns);
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());

        QName qName = pointer.getName();
        assertEquals("Prefix should match", "ex", qName.getPrefix());
        assertEquals("Local name should match", "item", qName.getName());

        assertEquals("Static getLocalName", "item", JDOMNodePointer.getLocalName(element));
        assertEquals("Static getPrefix", "ex", JDOMNodePointer.getPrefix(element));
    }

    public void testGetNamespaceURI() {
        Namespace ns = Namespace.getNamespace("p", "http://example.com/prefix");
        Element element = new Element("root", ns);

        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());
        assertEquals("Namespace URI from element", "http://example.com/prefix", pointer.getNamespaceURI());
        assertEquals("Resolved prefix namespace", "http://example.com/prefix", pointer.getNamespaceURI("p"));
        assertNull("Unresolved prefix returns null", pointer.getNamespaceURI("unknown"));
    }

    public void testGetValueAndSetValue() {
        Element element = new Element("root");
        element.setText(" initial ");
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());

        assertEquals("Value should be trimmed text", "initial", pointer.getValue());

        pointer.setValue("updated");
        assertEquals("Updated text value", "updated", pointer.getValue());
    }

    public void testAsPath() {
        Element root = new Element("root");
        JDOMNodePointer rootPointer = new JDOMNodePointer(root, Locale.getDefault());

        Element child = new Element("child");
        root.addContent(child);
        JDOMNodePointer childPointer = new JDOMNodePointer(rootPointer, child);

        assertEquals("Root path when parent is null", "", rootPointer.asPath());
        assertEquals("Child path relative to root", "/child[1](undefined)", childPointer.asPath());

        JDOMNodePointer idPointer = new JDOMNodePointer(root, Locale.getDefault(), "elem1");
        assertEquals("Path with ID", "id('elem1')", idPointer.asPath());
    }

    public void testIsLanguage() {
        Element root = new Element("root");
        root.setAttribute("lang", "en-US", Namespace.XML_NAMESPACE);
        JDOMNodePointer pointer = new JDOMNodePointer(root, Locale.getDefault());

        assertTrue("Language matches prefix", pointer.isLanguage("en"));
        assertTrue("Language matches case-insensitive", pointer.isLanguage("EN-US"));
        assertFalse("Language does not match wrong lang", pointer.isLanguage("fr"));
    }

    public void testTestNode() {
        Element element = new Element("item", Namespace.getNamespace("http://example.com"));
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());

        assertTrue("Null test matches everything", pointer.testNode(null));
        assertTrue("NodeTypeTest NODE matches Element", pointer.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse("NodeTypeTest TEXT does not match Element", pointer.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));

        NodeNameTest nameTest = new NodeNameTest(new QName(null, "item"), "http://example.com");
        assertTrue("NodeNameTest matches name and NS", pointer.testNode(nameTest));

        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        JDOMNodePointer piPointer = new JDOMNodePointer(pi, Locale.getDefault());
        assertTrue("ProcessingInstructionTest matches PI", piPointer.testNode(new ProcessingInstructionTest("target")));
    }

    public void testEqualsAndHashCode() {
        Element e1 = new Element("root");
        Element e2 = new Element("root");

        JDOMNodePointer p1 = new JDOMNodePointer(e1, Locale.getDefault());
        JDOMNodePointer p1Copy = new JDOMNodePointer(e1, Locale.getDefault());
        JDOMNodePointer p2 = new JDOMNodePointer(e2, Locale.getDefault());

        assertEquals("Same underlying node pointers are equal", p1, p1Copy);
        assertFalse("Different underlying node pointers are not equal", p1.equals(p2));
        assertEquals("Same node yields same hashCode", p1.hashCode(), p1Copy.hashCode());
    }
}

```