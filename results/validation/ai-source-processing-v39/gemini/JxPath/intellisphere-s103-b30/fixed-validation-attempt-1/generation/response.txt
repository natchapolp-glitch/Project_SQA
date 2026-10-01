Here are deterministic regression tests for `DOMNodePointer` and `JDOMNodePointer`, covering normal cases, boundaries, namespace handling, node testing, path construction, and exception paths using JUnit 3.8.

### `org/apache/commons/jxpath/ri/model/dom/DOMNodePointerTest.java`

```java
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
```

---

### `org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointerTest.java`

```java
package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;
import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

public class JDOMNodePointerTest extends TestCase {

    public void testBasics() {
        Element root = new Element("root");
        JDOMNodePointer pointer = new JDOMNodePointer(root, Locale.US);

        assertEquals(1, pointer.getLength());
        assertFalse(pointer.isCollection());
        assertSame(root, pointer.getBaseValue());
        assertSame(root, pointer.getImmediateNode());
        assertTrue(pointer.isLeaf());

        root.addContent(new Element("child"));
        assertFalse(pointer.isLeaf());
    }

    public void testGetName() {
        Element elem = new Element("test", "pfx", "http://example.com/ns");
        JDOMNodePointer elemPtr = new JDOMNodePointer(elem, Locale.US);
        QName name = elemPtr.getName();
        assertEquals("pfx", name.getPrefix());
        assertEquals("test", name.getName());

        ProcessingInstruction pi = new ProcessingInstruction("test-target", "data");
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        QName piName = piPtr.getName();
        assertNull(piName.getPrefix());
        assertEquals("test-target", piName.getName());
    }

    public void testGetNamespaceURI() {
        Element root = new Element("root", "http://default.com/ns");
        Namespace ns = Namespace.getNamespace("other", "http://other.com/ns");
        root.addNamespaceDeclaration(ns);
        Document doc = new Document(root);

        JDOMNodePointer docPointer = new JDOMNodePointer(doc, Locale.US);
        assertEquals("http://other.com/ns", docPointer.getNamespaceURI("other"));
        assertNull(docPointer.getNamespaceURI("unknown"));

        JDOMNodePointer elemPointer = new JDOMNodePointer(root, Locale.US);
        assertEquals("http://default.com/ns", elemPointer.getNamespaceURI());
        assertEquals("http://other.com/ns", elemPointer.getNamespaceURI("other"));
    }

    public void testTestNode() {
        Element elem = new Element("item", "http://example.com/ns");
        JDOMNodePointer elemPtr = new JDOMNodePointer(elem, Locale.US);

        assertTrue(elemPtr.testNode(null));
        assertTrue(elemPtr.testNode(new NodeNameTest(new QName("item"), "http://example.com/ns")));
        assertFalse(elemPtr.testNode(new NodeNameTest(new QName("other"), "http://example.com/ns")));
        assertTrue(elemPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse(elemPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));

        Text text = new Text("sample");
        JDOMNodePointer textPtr = new JDOMNodePointer(text, Locale.US);
        assertTrue(textPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertFalse(textPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_NODE)));

        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        assertTrue(piPtr.testNode(new NodeTypeTest(Compiler.NODE_TYPE_PI)));
        assertTrue(piPtr.testNode(new ProcessingInstructionTest("target")));
        assertFalse(piPtr.testNode(new ProcessingInstructionTest("other")));
    }

    public void testLanguage() {
        Element root = new Element("root");
        root.setAttribute("lang", "en-US", Namespace.XML_NAMESPACE);
        Element child = new Element("child");
        root.addContent(child);

        JDOMNodePointer rootPtr = new JDOMNodePointer(root, Locale.US);
        JDOMNodePointer childPtr = new JDOMNodePointer(rootPtr, child);

        assertTrue(rootPtr.isLanguage("en"));
        assertTrue(rootPtr.isLanguage("en-US"));
        assertFalse(rootPtr.isLanguage("fr"));

        assertTrue(childPtr.isLanguage("en"));
        assertEquals("en-US", childPtr.getLanguage());
    }

    public void testGetValueAndSetValue() {
        Element root = new Element("root");
        JDOMNodePointer rootPtr = new JDOMNodePointer(root, Locale.US);

        rootPtr.setValue("New Value");
        assertEquals("New Value", rootPtr.getValue());

        Comment comment = new Comment(" my comment ");
        JDOMNodePointer commentPtr = new JDOMNodePointer(comment, Locale.US);
        assertEquals("my comment", commentPtr.getValue());

        CDATA cdata = new CDATA(" cdata-text ");
        JDOMNodePointer cdataPtr = new JDOMNodePointer(cdata, Locale.US);
        assertEquals("cdata-text", cdataPtr.getValue());

        ProcessingInstruction pi = new ProcessingInstruction("target", " data ");
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        assertEquals("data", piPtr.getValue());
    }

    public void testAsPath() {
        Element root = new Element("root");
        JDOMNodePointer rootPtr = new JDOMNodePointer(root, Locale.US);
        assertEquals("", rootPtr.asPath());

        JDOMNodePointer idPtr = new JDOMNodePointer(root, Locale.US, "my\"id");
        assertEquals("id('my&quot;id')", idPtr.asPath());

        Element child1 = new Element("child");
        Element child2 = new Element("child");
        root.addContent(child1);
        root.addContent(child2);

        JDOMNodePointer childPtr1 = new JDOMNodePointer(rootPtr, child1);
        JDOMNodePointer childPtr2 = new JDOMNodePointer(rootPtr, child2);
        assertEquals("/child[1]", childPtr1.asPath());
        assertEquals("/child[2]", childPtr2.asPath());

        Text text = new Text("text");
        child1.addContent(text);
        JDOMNodePointer textPtr = new JDOMNodePointer(childPtr1, text);
        assertEquals("/child[1]/text()[1]", textPtr.asPath());

        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        child1.addContent(pi);
        JDOMNodePointer piPtr = new JDOMNodePointer(childPtr1, pi);
        assertEquals("/child[1]/processing-instruction('target')[1]", piPtr.asPath());
    }

    public void testCompareChildNodePointers() {
        Element root = new Element("root");
        root.setAttribute("a1", "v1");
        root.setAttribute("a2", "v2");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        root.addContent(child1);
        root.addContent(child2);

        JDOMNodePointer rootPtr = new JDOMNodePointer(root, Locale.US);
        JDOMNodePointer childPtr1 = new JDOMNodePointer(rootPtr, child1);
        JDOMNodePointer childPtr2 = new JDOMNodePointer(rootPtr, child2);

        assertEquals(-1, rootPtr.compareChildNodePointers(childPtr1, childPtr2));
        assertEquals(1, rootPtr.compareChildNodePointers(childPtr2, childPtr1));
        assertEquals(0, rootPtr.compareChildNodePointers(childPtr1, childPtr1));

        JDOMNodePointer attrPtr = (JDOMNodePointer) rootPtr.createAttribute(JXPathContext.newContext(root), new QName("a1"));
        assertEquals(-1, rootPtr.compareChildNodePointers(attrPtr, childPtr1));
        assertEquals(1, rootPtr.compareChildNodePointers(childPtr1, attrPtr));
    }

    public void testRemove() {
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);

        JDOMNodePointer rootPtr = new JDOMNodePointer(root, Locale.US);
        try {
            rootPtr.remove();
            fail("Should throw JXPathException when removing root JDOM node");
        }
        catch (JXPathException expected) {
            // expected
        }

        JDOMNodePointer childPtr = new JDOMNodePointer(rootPtr, child);
        childPtr.remove();
        assertEquals(0, root.getContent().size());
    }

    public void testEqualsAndHashCode() {
        Element root1 = new Element("root");
        Element root2 = new Element("root");
        JDOMNodePointer ptr1a = new JDOMNodePointer(root1, Locale.US);
        JDOMNodePointer ptr1b = new JDOMNodePointer(root1, Locale.US);
        JDOMNodePointer ptr2 = new JDOMNodePointer(root2, Locale.US);

        assertTrue(ptr1a.equals(ptr1a));
        assertTrue(ptr1a.equals(ptr1b));
        assertFalse(ptr1a.equals(ptr2));
        assertFalse(ptr1a.equals("other"));
        assertFalse(ptr1a.equals(null));
        assertEquals(ptr1a.hashCode(), ptr1b.hashCode());
    }

    public void testStaticGetLocalNameAndPrefix() {
        Element element = new Element("name", "pfx", "http://example.com");
        assertEquals("name", JDOMNodePointer.getLocalName(element));
        assertEquals("pfx", JDOMNodePointer.getPrefix(element));

        Attribute attr = new Attribute("attrName", "val", Namespace.getNamespace("apfx", "http://attr.com"));
        assertEquals("attrName", JDOMNodePointer.getLocalName(attr));
        assertEquals("apfx", JDOMNodePointer.getPrefix(attr));

        assertNull(JDOMNodePointer.getLocalName(new Object()));
        assertNull(JDOMNodePointer.getPrefix(new Object()));
    }
}
```

---

### References
- Apache Commons JXPath Repository & Documentation: https://commons.apache.org/proper/commons-jxpath/
- W3C Document Object Model (DOM) Level 2 Core Specification: https://www.w3.org/TR/DOM-Level-2-Core/
- JDOM 1.0 API Documentation: http://www.jdom.org/docs/apidocs/