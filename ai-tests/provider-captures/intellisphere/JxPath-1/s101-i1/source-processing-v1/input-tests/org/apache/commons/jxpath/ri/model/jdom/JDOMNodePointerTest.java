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
