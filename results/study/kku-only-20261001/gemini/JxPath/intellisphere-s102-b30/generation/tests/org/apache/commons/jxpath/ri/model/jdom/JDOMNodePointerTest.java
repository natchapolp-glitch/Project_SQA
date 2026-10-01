package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

public class JDOMNodePointerTest extends TestCase {

    public void testBasicProperties() {
        Element elem = new Element("sample");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.US);

        assertEquals(1, pointer.getLength());
        assertFalse(pointer.isCollection());
        assertTrue(pointer.isLeaf());

        elem.addContent("child text");
        assertFalse(pointer.isLeaf());
    }

    public void testEqualsAndHashCode() {
        Element elem1 = new Element("test");
        Element elem2 = new Element("test");

        JDOMNodePointer ptr1 = new JDOMNodePointer(elem1, Locale.US);
        JDOMNodePointer ptr1Same = new JDOMNodePointer(elem1, Locale.US);
        JDOMNodePointer ptr2 = new JDOMNodePointer(elem2, Locale.US);

        assertTrue(ptr1.equals(ptr1));
        assertTrue(ptr1.equals(ptr1Same));
        assertFalse(ptr1.equals(ptr2));
        assertFalse(ptr1.equals("non-pointer"));
        assertEquals(System.identityHashCode(elem1), ptr1.hashCode());
    }

    public void testGetNameAndPrefix() {
        Namespace ns = Namespace.getNamespace("p", "http://example.com/ns");
        Element elem = new Element("item", ns);

        assertEquals("p", JDOMNodePointer.getPrefix(elem));
        assertEquals("item", JDOMNodePointer.getLocalName(elem));

        JDOMNodePointer ptr = new JDOMNodePointer(elem, Locale.US);
        QName qName = ptr.getName();
        assertEquals("p", qName.getPrefix());
        assertEquals("item", qName.getName());

        ProcessingInstruction pi = new ProcessingInstruction("targetPI", "data");
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        assertEquals("targetPI", piPtr.getName().getName());
    }

    public void testTestNodeTypes() {
        Element elem = new Element("e");
        Text text = new Text("text");
        CDATA cdata = new CDATA("cdata");
        Comment comment = new Comment("comm");
        ProcessingInstruction pi = new ProcessingInstruction("pi", "data");

        assertTrue(JDOMNodePointer.testNode(null, elem, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertTrue(JDOMNodePointer.testNode(null, text, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertTrue(JDOMNodePointer.testNode(null, cdata, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
        assertTrue(JDOMNodePointer.testNode(null, comment, new NodeTypeTest(Compiler.NODE_TYPE_COMMENT)));
        assertTrue(JDOMNodePointer.testNode(null, pi, new NodeTypeTest(Compiler.NODE_TYPE_PI)));

        assertFalse(JDOMNodePointer.testNode(null, text, new NodeTypeTest(Compiler.NODE_TYPE_NODE)));
        assertFalse(JDOMNodePointer.testNode(null, elem, new NodeTypeTest(Compiler.NODE_TYPE_TEXT)));
    }

    public void testTestNodeName() {
        Namespace ns = Namespace.getNamespace("p", "http://example.com/ns");
        Element elem = new Element("elem", ns);

        NodeNameTest match = new NodeNameTest(new QName("p", "elem"), "http://example.com/ns");
        assertTrue(JDOMNodePointer.testNode(null, elem, match));

        NodeNameTest wildcard = new NodeNameTest(new QName(null, "*"));
        assertTrue(JDOMNodePointer.testNode(null, elem, wildcard));

        NodeNameTest mismatch = new NodeNameTest(new QName("p", "wrong"), "http://example.com/ns");
        assertFalse(JDOMNodePointer.testNode(null, elem, mismatch));
    }

    public void testProcessingInstructionTest() {
        ProcessingInstruction pi = new ProcessingInstruction("targetA", "val");
        ProcessingInstructionTest match = new ProcessingInstructionTest("targetA");
        ProcessingInstructionTest mismatch = new ProcessingInstructionTest("targetB");

        assertTrue(JDOMNodePointer.testNode(null, pi, match));
        assertFalse(JDOMNodePointer.testNode(null, pi, mismatch));
    }

    public void testGetValueAndSetValue() {
        Element elem = new Element("node");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.US);

        pointer.setValue("First Text");
        assertEquals("First Text", pointer.getValue());

        pointer.setValue("Updated Text");
        assertEquals("Updated Text", pointer.getValue());

        Comment comment = new Comment(" my comment ");
        JDOMNodePointer commentPtr = new JDOMNodePointer(comment, Locale.US);
        assertEquals("my comment", commentPtr.getValue());
    }

    public void testIsLanguage() {
        Element root = new Element("root");
        root.setAttribute("lang", "en-GB", Namespace.XML_NAMESPACE);
        Element child = new Element("child");
        root.addContent(child);

        JDOMNodePointer childPointer = new JDOMNodePointer(child, Locale.US);
        assertTrue(childPointer.isLanguage("en"));
        assertTrue(childPointer.isLanguage("en-GB"));
        assertFalse(childPointer.isLanguage("de"));
    }

    public void testAsPathWithId() {
        Element elem = new Element("root");
        JDOMNodePointer pointer = new JDOMNodePointer(elem, Locale.US, "foo'bar\"");
        assertEquals("id('foo&apos;bar&quot;')", pointer.asPath());
    }

    

    public void testCompareChildNodePointers() {
        Element root = new Element("root");
        Element c1 = new Element("c1");
        Element c2 = new Element("c2");
        root.addContent(c1);
        root.addContent(c2);

        JDOMNodePointer rootPointer = new JDOMNodePointer(root, Locale.US);
        JDOMNodePointer ptr1 = new JDOMNodePointer(rootPointer, c1);
        JDOMNodePointer ptr2 = new JDOMNodePointer(rootPointer, c2);

        assertEquals(-1, rootPointer.compareChildNodePointers(ptr1, ptr2));
        assertEquals(1, rootPointer.compareChildNodePointers(ptr2, ptr1));
        assertEquals(0, rootPointer.compareChildNodePointers(ptr1, ptr1));
    }

    public void testRemove() {
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);

        JDOMNodePointer childPointer = new JDOMNodePointer(child, Locale.US);
        childPointer.remove();
        assertEquals(0, root.getContent().size());

        JDOMNodePointer rootPointer = new JDOMNodePointer(root, Locale.US);
        try {
            rootPointer.remove();
            fail("Removing a node without parent should throw JXPathException");
        }
        catch (JXPathException expected) {
            // expected
        }
    }
}
