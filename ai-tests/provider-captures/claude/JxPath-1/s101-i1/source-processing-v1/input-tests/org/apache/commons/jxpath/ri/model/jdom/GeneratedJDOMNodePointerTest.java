package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

public class GeneratedJDOMNodePointerTest extends TestCase {

    private static final Locale LOCALE = Locale.ENGLISH;

    public void testBasicPropertiesEqualsAndHashCode() {
        Element root = new Element("root");
        Element child = new Element("child");
        root.addContent(child);
        Document doc = new Document(root);

        JDOMNodePointer rp = new JDOMNodePointer(root, LOCALE);
        assertSame(root, rp.getBaseValue());
        assertSame(root, rp.getImmediateNode());
        assertFalse(rp.isCollection());
        assertEquals(1, rp.getLength());
        assertFalse(rp.isLeaf());
        assertTrue(new JDOMNodePointer(child, LOCALE).isLeaf());
        assertFalse(new JDOMNodePointer(doc, LOCALE).isLeaf());
        assertTrue(new JDOMNodePointer(new Text("t"), LOCALE).isLeaf());

        JDOMNodePointer withParent = new JDOMNodePointer(rp, child);
        assertSame(child, withParent.getBaseValue());

        JDOMNodePointer same = new JDOMNodePointer(root, LOCALE);
        JDOMNodePointer other = new JDOMNodePointer(child, LOCALE);
        assertTrue(rp.equals(rp));
        assertTrue(rp.equals(same));
        assertEquals(rp.hashCode(), same.hashCode());
        assertFalse(rp.equals(other));
        assertFalse(rp.equals(null));
        assertFalse(rp.equals("root"));
    }

    public void testGetNameForElementAndProcessingInstruction() {
        Namespace ns = Namespace.getNamespace("a", "urn:a");
        QName prefixed = new JDOMNodePointer(new Element("root", ns), LOCALE).getName();
        assertEquals("a", prefixed.getPrefix());
        assertEquals("root", prefixed.getName());

        QName plain = new JDOMNodePointer(new Element("plain"), LOCALE).getName();
        assertNull(plain.getPrefix());
        assertEquals("plain", plain.getName());

        QName pi = new JDOMNodePointer(
                new ProcessingInstruction("target", "data"), LOCALE).getName();
        assertNull(pi.getPrefix());
        assertEquals("target", pi.getName());
    }

    public void testStaticPrefixAndLocalName() {
        Namespace ns = Namespace.getNamespace("a", "urn:a");
        Element prefixed = new Element("root", ns);
        Element plain = new Element("plain");
        Attribute nsAttr = new Attribute("id", "v", ns);
        Attribute plainAttr = new Attribute("id", "v");
        Text text = new Text("t");

        assertEquals("a", JDOMNodePointer.getPrefix(prefixed));
        assertNull(JDOMNodePointer.getPrefix(plain));
        assertEquals("a", JDOMNodePointer.getPrefix(nsAttr));
        assertNull(JDOMNodePointer.getPrefix(plainAttr));
        assertNull(JDOMNodePointer.getPrefix(text));

        assertEquals("root", JDOMNodePointer.getLocalName(prefixed));
        assertEquals("id", JDOMNodePointer.getLocalName(nsAttr));
        assertNull(JDOMNodePointer.getLocalName(text));
    }

    public void testNamespaceURILookup() {
        Element withNs = new Element("root", Namespace.getNamespace("urn:d"));
        assertEquals("urn:d", new JDOMNodePointer(withNs, LOCALE).getNamespaceURI());
        assertNull(new JDOMNodePointer(new Element("plain"), LOCALE).getNamespaceURI());
        assertNull(new JDOMNodePointer(new Text("t"), LOCALE).getNamespaceURI());

        Element root = new Element("root");
        root.addNamespaceDeclaration(Namespace.getNamespace("a", "urn:a"));
        Element child = new Element("child");
        root.addContent(child);
        Document doc = new Document(root);

        assertEquals("urn:a", new JDOMNodePointer(doc, LOCALE).getNamespaceURI("a"));
        assertEquals("urn:a", new JDOMNodePointer(root, LOCALE).getNamespaceURI("a"));
        assertEquals("urn:a", new JDOMNodePointer(child, LOCALE).getNamespaceURI("a"));
        assertNull(new JDOMNodePointer(child, LOCALE).getNamespaceURI("zz"));
        assertNull(new JDOMNodePointer(new Text("t"), LOCALE).getNamespaceURI("a"));
    }

    public void testGetValue() {
        Element e = new Element("e");
        e.setText("  hi  ");
        assertEquals("hi", new JDOMNodePointer(e, LOCALE).getValue());
        assertEquals("note",
                new JDOMNodePointer(new Comment("  note  "), LOCALE).getValue());
        assertEquals("x", new JDOMNodePointer(new Text("  x "), LOCALE).getValue());
        assertEquals("y", new JDOMNodePointer(new CDATA(" y "), LOCALE).getValue());
        assertEquals("d", new JDOMNodePointer(
                new ProcessingInstruction("t", "  d  "), LOCALE).getValue());
        assertNull(new JDOMNodePointer(new Document(new Element("r")), LOCALE).getValue());
    }

    public void testSetValue() {
        Element root = new Element("root");
        Text t = new Text("old");
        root.addContent(t);

        new JDOMNodePointer(t, LOCALE).setValue("new");
        assertEquals("new", t.getText());
        new JDOMNodePointer(t, LOCALE).setValue("");
        assertEquals(0, root.getContent().size());

        Element target = new Element("e");
        target.addContent(new Element("old"));
        JDOMNodePointer tp = new JDOMNodePointer(target, LOCALE);

        tp.setValue("abc");
        assertEquals(1, target.getContent().size());
        assertEquals("abc", target.getText());

        Element src = new Element("src");
        src.addContent(new Element("p"));
        src.addContent(new Text("hello"));
        tp.setValue(src);
        assertEquals(2, target.getContent().size());
        assertNotNull(target.getChild("p"));
        assertEquals("hello", target.getText());

        tp.setValue(new Comment("c"));
        assertEquals(1, target.getContent().size());
        assertTrue(target.getContent().get(0) instanceof Comment);

        tp.setValue(new Text("tv"));
        assertEquals("tv", target.getText());

        tp.setValue("");
        assertEquals(0, target.getContent().size());
    }

    public void testTestNodeWithNodeNameTests() {
        Namespace ns = Namespace.getNamespace("a", "urn:a");
        Element root = new Element("root");
        Element nsItem = new Element("item", ns);
        Element plainItem = new Element("item");
        root.addContent(nsItem);
        root.addContent(plainItem);
        JDOMNodePointer rp = new JDOMNodePointer(root, LOCALE);

        NodeNameTest plainTest = new NodeNameTest(new QName(null, "item"), null);
        assertTrue(JDOMNodePointer.testNode(rp, plainItem, plainTest));
        assertFalse(JDOMNodePointer.testNode(rp, nsItem, plainTest));

        NodeNameTest nsTest = new NodeNameTest(new QName("a", "item"), "urn:a");
        assertTrue(JDOMNodePointer.testNode(rp, nsItem, nsTest));
        assertFalse(JDOMNodePointer.testNode(rp, plainItem, nsTest));

        NodeNameTest otherName = new NodeNameTest(new QName(null, "other"), null);
        assertFalse(JDOMNodePointer.testNode(rp, plainItem, otherName));

        NodeNameTest wildcard = new NodeNameTest(new QName(null, "*"), null);
        assertTrue(JDOMNodePointer.testNode(rp, plainItem, wildcard));
        assertTrue(JDOMNodePointer.testNode(rp, nsItem, wildcard));

        assertFalse(JDOMNodePointer.testNode(rp, new Text("x"), plainTest));
        assertTrue(JDOMNodePointer.testNode(rp, plainItem, null));
        assertTrue(rp.testNode(null));
        assertFalse(rp.testNode(plainTest));
        assertTrue(new JDOMNodePointer(plainItem, LOCALE).testNode(plainTest));
    }

    public void testTestNodeWithNodeTypeAndProcessingInstructionTests() {
        Element root = new Element("root");
        Document doc = new Document(root);
        Text text = new Text("t");
        CDATA cdata = new CDATA("c");
        Comment comment = new Comment("m");
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        JDOMNodePointer rp = new JDOMNodePointer(root, LOCALE);

        NodeTypeTest nodeType = new NodeTypeTest(Compiler.NODE_TYPE_NODE);
        assertTrue(JDOMNodePointer.testNode(rp, root, nodeType));
        assertTrue(JDOMNodePointer.testNode(rp, doc, nodeType));
        assertFalse(JDOMNodePointer.testNode(rp, text, nodeType));

        NodeTypeTest textType = new NodeTypeTest(Compiler.NODE_TYPE_TEXT);
        assertTrue(JDOMNodePointer.testNode(rp, text, textType));
        assertTrue(JDOMNodePointer.testNode(rp, cdata, textType));
        assertFalse(JDOMNodePointer.testNode(rp, comment, textType));

        NodeTypeTest commentType = new NodeTypeTest(Compiler.NODE_TYPE_COMMENT);
        assertTrue(JDOMNodePointer.testNode(rp, comment, commentType));
        assertFalse(JDOMNodePointer.testNode(rp, text, commentType));

        NodeTypeTest piType = new NodeTypeTest(Compiler.NODE_TYPE_PI);
        assertTrue(JDOMNodePointer.testNode(rp, pi, piType));
        assertFalse(JDOMNodePointer.testNode(rp, root, piType));

        assertTrue(JDOMNodePointer.testNode(rp, pi, new ProcessingInstructionTest("target")));
        assertFalse(JDOMNodePointer.testNode(rp, pi, new ProcessingInstructionTest("other")));
        assertFalse(JDOMNodePointer.testNode(rp, root, new ProcessingInstructionTest("target")));
    }

    public void testLanguage() {
        Element root = new Element("root");
        root.setAttribute("lang", "en-US", Namespace.XML_NAMESPACE);
        Element child = new Element("child");
        root.addContent(child);
        Text text = new Text("t");
        child.addContent(text);
        new Document(root);

        JDOMNodePointer childPtr = new JDOMNodePointer(child, LOCALE);
        assertEquals("en-US", childPtr.getLanguage());
        assertTrue(childPtr.isLanguage("en"));
        assertTrue(childPtr.isLanguage("EN-us"));
        assertFalse(childPtr.isLanguage("fr"));
        assertEquals("en-US", new JDOMNodePointer(text, LOCALE).getLanguage());

        Element plainRoot = new Element("root");
        Element plainChild = new Element("child");
        plainRoot.addContent(plainChild);
        JDOMNodePointer plainPtr = new JDOMNodePointer(plainChild, LOCALE);
        assertNull(plainPtr.getLanguage());
        assertTrue(plainPtr.isLanguage("en"));
        assertFalse(plainPtr.isLanguage("fr"));
    }

    public void testAsPath() {
        Element root = new Element("root");
        root.addContent(new Element("a"));
        root.addContent(new Element("a"));
        root.addContent(new Element("b"));
        root.addContent(new Text("text"));
        root.addContent(new ProcessingInstruction("pi", "x"));
        Document doc = new Document(root);

        JXPathContext ctx = JXPathContext.newContext(doc);
        assertEquals("/root[1]", ctx.getPointer("/root").asPath());
        assertEquals("/root[1]/a[2]", ctx.getPointer("/root/a[2]").asPath());
        assertEquals("/root[1]/b[1]", ctx.getPointer("/root/b").asPath());
        assertEquals("/root[1]/text()[1]", ctx.getPointer("/root/text()").asPath());
        assertEquals("/root[1]/processing-instruction('pi')[1]",
                ctx.getPointer("/root/processing-instruction('pi')").asPath());

        JDOMNodePointer withId = new JDOMNodePointer(root, LOCALE, "a'b\"c");
        assertEquals("id('a&apos;b&quot;c')", withId.asPath());
    }

    public void testCompareChildNodePointers() {
        Element root = new Element("r");
        root.setAttribute("x", "1");
        root.setAttribute("y", "2");
        Element a = new Element("a");
        Element b = new Element("b");
        root.addContent(a);
        root.addContent(b);

        JDOMNodePointer rp = new JDOMNodePointer(root, LOCALE);
        NodePointer pa = new JDOMNodePointer(rp, a);
        NodePointer pb = new JDOMNodePointer(rp, b);
        NodePointer px = new JDOMNodePointer(rp, root.getAttribute("x"));
        NodePointer py = new JDOMNodePointer(rp, root.getAttribute("y"));

        assertEquals(-1, rp.compareChildNodePointers(pa, pb));
        assertEquals(1, rp.compareChildNodePointers(pb, pa));
        assertEquals(0, rp.compareChildNodePointers(pa, pa));
        assertEquals(-1, rp.compareChildNodePointers(px, pa));
        assertEquals(1, rp.compareChildNodePointers(pa, px));
        assertEquals(-1, rp.compareChildNodePointers(px, py));
        assertEquals(1, rp.compareChildNodePointers(py, px));

        JDOMNodePointer textPtr = new JDOMNodePointer(new Text("t"), LOCALE);
        try {
            textPtr.compareChildNodePointers(pa, pb);
            fail("Expected RuntimeException for non-element parent");
        }
        catch (RuntimeException expected) {
            // expected
        }
    }

    public void testCreateChild() {
        final Element root = new Element("root");
        Document doc = new Document(root);
        JDOMNodePointer ptr = new JDOMNodePointer(root, LOCALE);
        JXPathContext ctx = JXPathContext.newContext(doc);
        QName kid = new QName(null, "kid");

        try {
            ptr.createChild(ctx, kid, 0);
            fail("Expected JXPathException when no factory is set");
        }
        catch (JXPathException expected) {
            // expected
        }

        ctx.setFactory(new AbstractFactory() {
            public boolean createObject(JXPathContext context, Pointer pointer,
                    Object parent, String name, int index) {
                return false;
            }
        });
        try {
            ptr.createChild(ctx, kid, 0);
            fail("Expected JXPathAbstractFactoryException");
        }
        catch (JXPathAbstractFactoryException expected) {
            // expected
        }

        ctx.setFactory(new AbstractFactory() {
            public boolean createObject(JXPathContext context, Pointer pointer,
                    Object parent, String name, int index) {
                ((Element) parent).addContent(new Element(name));
                return true;
            }
        });
        NodePointer created = ptr.createChild(ctx, kid, 0);
        assertSame(root.getChild("kid"), created.getBaseValue());

        NodePointer withValue =
                ptr.createChild(ctx, new QName(null, "other"), 0, "v");
        assertSame(root.getChild("other"), withValue.getBaseValue());
        assertEquals("v", root.getChild("other").getText());
    }

    public void testCreateAttributeAndIterators() {
        Namespace ns = Namespace.getNamespace("a", "urn:a");
        Element root = new Element("r");
        root.addNamespaceDeclaration(ns);
        root.setAttribute("x", "1");
        root.setAttribute("y", "2");
        root.addContent(new Element("a"));
        root.addContent(new Element("b"));
        root.addContent(new Element("a"));
        Document doc = new Document(root);

        JDOMNodePointer ptr = new JDOMNodePointer(root, LOCALE);
        JXPathContext ctx = JXPathContext.newContext(doc);

        NodePointer created = ptr.createAttribute(ctx, new QName(null, "id"));
        assertNotNull(created);
        assertNotNull(root.getAttribute("id"));
        assertEquals("", root.getAttributeValue("id"));

        ptr.createAttribute(ctx, new QName(null, "x"));
        assertEquals("1", root.getAttributeValue("x"));

        assertNotNull(ptr.createAttribute(ctx, new QName("a", "q")));
        assertNotNull(root.getAttribute("q", ns));

        try {
            ptr.createAttribute(ctx, new QName("zz", "w"));
            fail("Expected JXPathException for unknown prefix");
        }
        catch (JXPathException expected) {
            // expected
        }

        NodeIterator children = ptr.childIterator(
                new NodeNameTest(new QName(null, "a"), null), false, null);
        int count = 0;
        for (int pos = 1; children.setPosition(pos); pos++) {
            count++;
        }
        assertEquals(2, count);

        NodeIterator attrs = ptr.attributeIterator(new QName(null, "x"));
        assertTrue(attrs.setPosition(1));
        assertSame(root.getAttribute("x"), attrs.getNodePointer().getBaseValue());
        assertFalse(attrs.setPosition(2));

        assertNotNull(ptr.namespaceIterator());
        assertNotNull(ptr.namespacePointer("a"));
    }

    public void testRemove() {
        Element root = new Element("root");
        Element child = new Element("child");
        Text text = new Text("t");
        root.addContent(child);
        root.addContent(text);
        new Document(root);

        new JDOMNodePointer(child, LOCALE).remove();
        assertEquals(1, root.getContent().size());
        assertSame(text, root.getContent().get(0));

        new JDOMNodePointer(text, LOCALE).remove();
        assertEquals(0, root.getContent().size());

        try {
            new JDOMNodePointer(root, LOCALE).remove();
            fail("Expected JXPathException for root element");
        }
        catch (JXPathException expected) {
            // expected
        }

        try {
            new JDOMNodePointer(new Element("detached"), LOCALE).remove();
            fail("Expected JXPathException for detached element");
        }
        catch (JXPathException expected) {
            // expected
        }
    }
}
