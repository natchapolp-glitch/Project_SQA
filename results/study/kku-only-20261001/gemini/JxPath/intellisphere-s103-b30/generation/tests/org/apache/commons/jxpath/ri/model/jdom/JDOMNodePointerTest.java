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
