package org.apache.commons.jxpath.ri.model.dom;

import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

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
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class GeneratedDOMNodePointerTest extends TestCase {

    private static final Locale LOCALE = Locale.ENGLISH;

    private static DocumentBuilderFactory factory() {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(true);
        return f;
    }

    private static Document parse(String xml) throws Exception {
        return factory().newDocumentBuilder().parse(
                new InputSource(new StringReader(xml)));
    }

    private static Document newDocument() throws Exception {
        return factory().newDocumentBuilder().newDocument();
    }

    public void testBasicPropertiesEqualsAndHashCode() throws Exception {
        Document doc = parse("<root><a/><b>text</b></root>");
        Element root = doc.getDocumentElement();
        Element a = (Element) root.getFirstChild();

        DOMNodePointer ptr = new DOMNodePointer(root, LOCALE);
        assertSame(root, ptr.getBaseValue());
        assertSame(root, ptr.getImmediateNode());
        assertTrue(ptr.isActual());
        assertFalse(ptr.isCollection());
        assertEquals(1, ptr.getLength());
        assertFalse(ptr.isLeaf());
        assertTrue(new DOMNodePointer(a, LOCALE).isLeaf());

        DOMNodePointer withParent = new DOMNodePointer(ptr, a);
        assertSame(a, withParent.getBaseValue());

        DOMNodePointer same = new DOMNodePointer(root, LOCALE);
        DOMNodePointer other = new DOMNodePointer(a, LOCALE);
        assertTrue(ptr.equals(ptr));
        assertTrue(ptr.equals(same));
        assertEquals(ptr.hashCode(), same.hashCode());
        assertFalse(ptr.equals(other));
        assertFalse(ptr.equals(null));
        assertFalse(ptr.equals("root"));
    }

    public void testGetNameForElementAndProcessingInstruction()
            throws Exception {
        Document doc = parse("<p:root xmlns:p=\"urn:p\"><item/></p:root>");
        Element root = doc.getDocumentElement();
        Element item = (Element) root.getFirstChild();

        QName rootName = new DOMNodePointer(root, LOCALE).getName();
        assertEquals("p", rootName.getPrefix());
        assertEquals("root", rootName.getName());

        QName itemName = new DOMNodePointer(item, LOCALE).getName();
        assertNull(itemName.getPrefix());
        assertEquals("item", itemName.getName());

        Node pi = doc.createProcessingInstruction("target", "data");
        QName piName = new DOMNodePointer(pi, LOCALE).getName();
        assertNull(piName.getPrefix());
        assertEquals("target", piName.getName());
    }

    public void testStaticPrefixAndLocalName() throws Exception {
        Document doc = newDocument();
        Element level1Prefixed = doc.createElement("p:x");
        assertEquals("p", DOMNodePointer.getPrefix(level1Prefixed));
        assertEquals("x", DOMNodePointer.getLocalName(level1Prefixed));

        Element level1Plain = doc.createElement("plain");
        assertNull(DOMNodePointer.getPrefix(level1Plain));
        assertEquals("plain", DOMNodePointer.getLocalName(level1Plain));

        Element ns = doc.createElementNS("urn:a", "a:b");
        assertEquals("a", DOMNodePointer.getPrefix(ns));
        assertEquals("b", DOMNodePointer.getLocalName(ns));
    }

    public void testStaticNamespaceURI() throws Exception {
        Document doc = newDocument();
        Element withNs = doc.createElementNS("urn:x", "x:e");
        assertEquals("urn:x", DOMNodePointer.getNamespaceURI(withNs));

        Element plain = doc.createElement("e");
        doc.appendChild(plain);
        assertNull(DOMNodePointer.getNamespaceURI(plain));

        plain.setAttribute("xmlns", "urn:d");
        Element child = doc.createElement("c");
        plain.appendChild(child);
        assertEquals("urn:d", DOMNodePointer.getNamespaceURI(child));
        assertEquals("urn:d", DOMNodePointer.getNamespaceURI(doc));

        plain.setAttribute("xmlns:p", "urn:p");
        Element prefixed = doc.createElement("p:e2");
        plain.appendChild(prefixed);
        assertEquals("urn:p", DOMNodePointer.getNamespaceURI(prefixed));
    }

    public void testTestNodeWithNodeNameTests() throws Exception {
        Document doc = parse("<r xmlns:a=\"urn:a\"><a:item/><item/></r>");
        Element r = doc.getDocumentElement();
        Element nsItem = (Element) r.getFirstChild();
        Element plainItem = (Element) r.getLastChild();

        NodeNameTest plainTest = new NodeNameTest(new QName(null, "item"), null);
        assertTrue(DOMNodePointer.testNode(plainItem, plainTest));
        assertFalse(DOMNodePointer.testNode(nsItem, plainTest));

        NodeNameTest nsTest = new NodeNameTest(new QName("a", "item"), "urn:a");
        assertTrue(DOMNodePointer.testNode(nsItem, nsTest));
        assertFalse(DOMNodePointer.testNode(plainItem, nsTest));

        NodeNameTest emptyNsTest = new NodeNameTest(new QName(null, "item"), "");
        assertTrue(DOMNodePointer.testNode(plainItem, emptyNsTest));

        NodeNameTest otherName = new NodeNameTest(new QName(null, "other"), null);
        assertFalse(DOMNodePointer.testNode(plainItem, otherName));

        NodeNameTest wildcard = new NodeNameTest(new QName(null, "*"), null);
        assertTrue(DOMNodePointer.testNode(plainItem, wildcard));
        assertTrue(DOMNodePointer.testNode(nsItem, wildcard));

        NodeNameTest prefixWildcard = new NodeNameTest(new QName("a", "*"), "urn:a");
        assertTrue(DOMNodePointer.testNode(nsItem, prefixWildcard));
        assertFalse(DOMNodePointer.testNode(plainItem, prefixWildcard));

        assertFalse(DOMNodePointer.testNode(doc.createTextNode("x"), plainTest));
        assertTrue(DOMNodePointer.testNode(plainItem, null));
        assertTrue(new DOMNodePointer(plainItem, LOCALE).testNode(null));
        assertTrue(new DOMNodePointer(plainItem, LOCALE).testNode(plainTest));
        assertFalse(new DOMNodePointer(nsItem, LOCALE).testNode(plainTest));
    }

    public void testTestNodeWithNodeTypeAndProcessingInstructionTests()
            throws Exception {
        Document doc = parse("<r><?target data?></r>");
        Element r = doc.getDocumentElement();
        Node pi = r.getFirstChild();
        Node text = doc.createTextNode("t");
        Node cdata = doc.createCDATASection("c");
        Node comment = doc.createComment("m");

        NodeTypeTest nodeType = new NodeTypeTest(Compiler.NODE_TYPE_NODE);
        assertTrue(DOMNodePointer.testNode(r, nodeType));
        assertTrue(DOMNodePointer.testNode(doc, nodeType));
        assertFalse(DOMNodePointer.testNode(text, nodeType));

        NodeTypeTest textType = new NodeTypeTest(Compiler.NODE_TYPE_TEXT);
        assertTrue(DOMNodePointer.testNode(text, textType));
        assertTrue(DOMNodePointer.testNode(cdata, textType));
        assertFalse(DOMNodePointer.testNode(comment, textType));

        NodeTypeTest commentType = new NodeTypeTest(Compiler.NODE_TYPE_COMMENT);
        assertTrue(DOMNodePointer.testNode(comment, commentType));
        assertFalse(DOMNodePointer.testNode(text, commentType));

        NodeTypeTest piType = new NodeTypeTest(Compiler.NODE_TYPE_PI);
        assertTrue(DOMNodePointer.testNode(pi, piType));
        assertFalse(DOMNodePointer.testNode(r, piType));

        assertTrue(DOMNodePointer.testNode(pi, new ProcessingInstructionTest("target")));
        assertFalse(DOMNodePointer.testNode(pi, new ProcessingInstructionTest("other")));
        assertFalse(DOMNodePointer.testNode(r, new ProcessingInstructionTest("target")));
    }

    public void testNamespaceLookup() throws Exception {
        Document doc = parse(
                "<root xmlns=\"urn:d\" xmlns:a=\"urn:a\"><c/></root>");
        Element c = (Element) doc.getDocumentElement().getFirstChild();
        DOMNodePointer ptr = new DOMNodePointer(c, LOCALE);

        assertEquals("urn:d", ptr.getNamespaceURI());
        assertEquals("urn:a", ptr.getNamespaceURI("a"));
        assertEquals("urn:a", ptr.getNamespaceURI("a"));
        assertEquals(DOMNodePointer.XML_NAMESPACE_URI, ptr.getNamespaceURI("xml"));
        assertEquals(DOMNodePointer.XMLNS_NAMESPACE_URI, ptr.getNamespaceURI("xmlns"));
        assertNull(ptr.getNamespaceURI("unknown"));
        assertNull(ptr.getNamespaceURI("unknown"));
        assertEquals("urn:d", ptr.getNamespaceURI(null));
        assertEquals("urn:d", ptr.getNamespaceURI(""));
        assertEquals("urn:d", ptr.getDefaultNamespaceURI());
        assertEquals("urn:d", new DOMNodePointer(doc, LOCALE).getDefaultNamespaceURI());

        Document plain = parse("<root><c/></root>");
        DOMNodePointer plainPtr = new DOMNodePointer(
                plain.getDocumentElement().getFirstChild(), LOCALE);
        assertNull(plainPtr.getDefaultNamespaceURI());
        assertNull(plainPtr.getNamespaceURI(""));
        assertNull(plainPtr.getNamespaceURI());
    }

    public void testLanguage() throws Exception {
        Document doc = parse("<root xml:lang=\"en-US\"><c/></root>");
        Element c = (Element) doc.getDocumentElement().getFirstChild();
        DOMNodePointer ptr = new DOMNodePointer(c, LOCALE);
        assertEquals("en-US", ptr.getLanguage());
        assertTrue(ptr.isLanguage("en"));
        assertTrue(ptr.isLanguage("EN-us"));
        assertFalse(ptr.isLanguage("fr"));

        Document plain = parse("<root><c/></root>");
        DOMNodePointer plainPtr = new DOMNodePointer(
                plain.getDocumentElement().getFirstChild(), LOCALE);
        assertNull(plainPtr.getLanguage());
        assertTrue(plainPtr.isLanguage("en"));
        assertFalse(plainPtr.isLanguage("fr"));
    }

    public void testGetValue() throws Exception {
        Document doc = parse(
                "<r><a>  hi  </a><n>x<m>y</m>z</n><!--  note  --><?tgt  body  ?></r>");
        Element r = doc.getDocumentElement();
        Node a = r.getFirstChild();
        Node n = a.getNextSibling();
        Node comment = n.getNextSibling();
        Node pi = comment.getNextSibling();

        assertEquals("hi", new DOMNodePointer(a, LOCALE).getValue());
        assertEquals("xyz", new DOMNodePointer(n, LOCALE).getValue());
        assertEquals("note", new DOMNodePointer(comment, LOCALE).getValue());
        assertEquals("body", new DOMNodePointer(pi, LOCALE).getValue());
        assertEquals("cd",
                new DOMNodePointer(doc.createCDATASection("  cd "), LOCALE).getValue());
        assertEquals("tx",
                new DOMNodePointer(doc.createTextNode(" tx "), LOCALE).getValue());
    }

    public void testSetValue() throws Exception {
        Document doc = parse("<r><t>old</t><e><x/></e></r>");
        Element r = doc.getDocumentElement();
        Element t = (Element) r.getFirstChild();
        Element e = (Element) r.getLastChild();
        Text textNode = (Text) t.getFirstChild();

        new DOMNodePointer(textNode, LOCALE).setValue("new");
        assertEquals("new", textNode.getNodeValue());

        new DOMNodePointer(textNode, LOCALE).setValue("");
        assertNull(t.getFirstChild());

        DOMNodePointer ePtr = new DOMNodePointer(e, LOCALE);
        ePtr.setValue("abc");
        assertEquals(1, e.getChildNodes().getLength());
        assertEquals("abc", e.getFirstChild().getNodeValue());

        Element src = doc.createElement("src");
        src.appendChild(doc.createElement("p"));
        src.appendChild(doc.createTextNode("text"));
        ePtr.setValue(src);
        assertEquals(2, e.getChildNodes().getLength());
        assertEquals("p", e.getFirstChild().getNodeName());
        assertEquals("text", e.getLastChild().getNodeValue());

        ePtr.setValue("");
        assertEquals(0, e.getChildNodes().getLength());
    }

    public void testRemove() throws Exception {
        Document doc = parse("<r><a/><b/></r>");
        Element r = doc.getDocumentElement();
        Element a = (Element) r.getFirstChild();

        new DOMNodePointer(a, LOCALE).remove();
        assertEquals(1, r.getChildNodes().getLength());
        assertEquals("b", r.getFirstChild().getNodeName());

        try {
            new DOMNodePointer(doc, LOCALE).remove();
            fail("Expected JXPathException for root node");
        }
        catch (JXPathException expected) {
            // expected
        }
    }

    public void testAsPath() throws Exception {
        Document doc = parse("<root><a/><a/><b/>text<?pi x?></root>");
        JXPathContext ctx = JXPathContext.newContext(doc);
        assertEquals("/root[1]", ctx.getPointer("/root").asPath());
        assertEquals("/root[1]/a[2]", ctx.getPointer("/root/a[2]").asPath());
        assertEquals("/root[1]/b[1]", ctx.getPointer("/root/b").asPath());
        assertEquals("/root[1]/text()[1]", ctx.getPointer("/root/text()").asPath());
        assertEquals("/root[1]/processing-instruction('pi')[1]",
                ctx.getPointer("/root/processing-instruction('pi')").asPath());

        DOMNodePointer withId = new DOMNodePointer(
                doc.getDocumentElement(), LOCALE, "a'b\"c");
        assertEquals("id('a&apos;b&quot;c')", withId.asPath());
    }

    public void testCompareChildNodePointers() throws Exception {
        Document doc = parse("<r x=\"1\" y=\"2\"><a/><b/></r>");
        Element r = doc.getDocumentElement();
        Element a = (Element) r.getFirstChild();
        Element b = (Element) r.getLastChild();
        Attr x = r.getAttributeNode("x");
        Attr y = r.getAttributeNode("y");

        DOMNodePointer rp = new DOMNodePointer(r, LOCALE);
        NodePointer pa = new DOMNodePointer(rp, a);
        NodePointer pb = new DOMNodePointer(rp, b);
        NodePointer px = new DOMNodePointer(rp, x);
        NodePointer py = new DOMNodePointer(rp, y);

        assertEquals(-1, rp.compareChildNodePointers(pa, pb));
        assertEquals(1, rp.compareChildNodePointers(pb, pa));
        assertEquals(0, rp.compareChildNodePointers(pa, pa));
        assertEquals(-1, rp.compareChildNodePointers(px, pa));
        assertEquals(1, rp.compareChildNodePointers(pa, px));
        assertEquals(-1, rp.compareChildNodePointers(px, py));
        assertEquals(1, rp.compareChildNodePointers(py, px));
    }

    public void testCreateChild() throws Exception {
        final Document doc = parse("<root/>");
        Element root = doc.getDocumentElement();
        DOMNodePointer ptr = new DOMNodePointer(root, LOCALE);
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
                ((Node) parent).appendChild(doc.createElement(name));
                return true;
            }
        });
        NodePointer created = ptr.createChild(ctx, kid, 0);
        assertSame(root.getFirstChild(), created.getBaseValue());

        NodePointer withValue =
                ptr.createChild(ctx, new QName(null, "other"), 0, "v");
        Node other = root.getLastChild();
        assertEquals("other", other.getNodeName());
        assertSame(other, withValue.getBaseValue());
        assertEquals("v", other.getFirstChild().getNodeValue());
    }

    public void testCreateAttributeAndIterators() throws Exception {
        Document doc = parse("<r x=\"1\" y=\"2\"><a/><b/><a/></r>");
        Element r = doc.getDocumentElement();
        DOMNodePointer ptr = new DOMNodePointer(r, LOCALE);
        JXPathContext ctx = JXPathContext.newContext(doc);

        NodePointer attr = ptr.createAttribute(ctx, new QName(null, "id"));
        assertNotNull(attr);
        assertTrue(r.hasAttribute("id"));
        assertEquals("", r.getAttribute("id"));

        ptr.createAttribute(ctx, new QName(null, "x"));
        assertEquals("1", r.getAttribute("x"));

        try {
            ptr.createAttribute(ctx, new QName("zz", "q"));
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
        assertSame(r.getAttributeNode("x"), attrs.getNodePointer().getBaseValue());
        assertFalse(attrs.setPosition(2));

        assertNotNull(ptr.namespaceIterator());
        assertNotNull(ptr.namespacePointer("a"));
    }

    public void testGetPointerByIdWithoutMatch() throws Exception {
        Document doc = parse("<r><a/></r>");
        JXPathContext ctx = JXPathContext.newContext(doc);

        Pointer fromDocument =
                new DOMNodePointer(doc, LOCALE).getPointerByID(ctx, "none");
        assertTrue(fromDocument instanceof NullPointer);

        Pointer fromElement = new DOMNodePointer(doc.getDocumentElement(), LOCALE)
                .getPointerByID(ctx, "none");
        assertTrue(fromElement instanceof NullPointer);
    }
}
