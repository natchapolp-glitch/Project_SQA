// org/apache/commons/jxpath/ri/model/dom/DOMNodePointerTest.java
package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import junit.framework.TestCase;

public class DOMNodePointerTest extends TestCase {

    private Document doc;
    private Element root;
    private Element child;
    private Text textNode;
    private ProcessingInstruction pi;
    private Element langElem;
    private DOMNodePointer rootPtr;

    public void setUp() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.newDocument();
        // root element with namespace
        root = doc.createElementNS("http://example.com", "root");
        root.setPrefix("ex");
        doc.appendChild(root);
        // child element with same namespace
        child = doc.createElementNS("http://example.com", "child");
        child.setPrefix("ex");
        root.appendChild(child);
        // text node inside child
        textNode = doc.createTextNode("text content");
        child.appendChild(textNode);
        // processing instruction
        pi = doc.createProcessingInstruction("target", "data");
        root.appendChild(pi);
        // element with xml:lang
        langElem = doc.createElement("langElem");
        langElem.setAttribute("xml:lang", "en-US");
        root.appendChild(langElem);
        // set default namespace on root (xmlns)
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://default.example.com");
        // set a namespace prefix
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ns", "http://ns.example.com");

        rootPtr = new DOMNodePointer(root, Locale.US);
    }

    // 1.
    public void testGetBaseValue() {
        assertSame(root, rootPtr.getBaseValue());
    }

    // 2.
    public void testGetImmediateNode() {
        assertSame(root, rootPtr.getImmediateNode());
    }

    // 3.
    public void testGetNamespaceURI_element() {
        assertEquals("http://example.com", rootPtr.getNamespaceURI());
    }

    // 4.
    public void testGetDefaultNamespaceURI() {
        // root has xmlns attribute default namespace
        assertEquals("http://default.example.com", rootPtr.getDefaultNamespaceURI());
    }

    // 5.
    public void testGetNamespaceURI_withPrefix() {
        // ns prefix defined on root
        assertEquals("http://ns.example.com", rootPtr.getNamespaceURI("ns"));
    }

    // 6.
    public void testGetName_elementAndPI() {
        // element
        QName name = rootPtr.getName();
        assertEquals("ex", name.getPrefix());
        assertEquals("root", name.getName());
        // PI
        DOMNodePointer piPtr = new DOMNodePointer(pi, Locale.US);
        QName piName = piPtr.getName();
        assertEquals("target", piName.getName());
        assertNull(piName.getPrefix());
    }

    // 7.
    

    // 8.
    public void testAsPath_textNode() {
        DOMNodePointer textPtr = new DOMNodePointer(textNode, Locale.US);
        String path = textPtr.asPath();
        assertTrue(path.startsWith("/text()["));
    }

    // 9.
    public void testAsPath_withId_escape() {
        DOMNodePointer idPtr = new DOMNodePointer(root, Locale.US, "id'\"value");
        String path = idPtr.asPath();
        assertTrue(path.startsWith("id('"));
        assertTrue(path.contains("&apos;"));
        assertTrue(path.contains("&quot;"));
    }

    // 10.
    public void testSetValue_textNode_replace() {
        DOMNodePointer textPtr = new DOMNodePointer(textNode, Locale.US);
        textPtr.setValue("new text");
        assertEquals("new text", textPtr.getValue());
    }

    // 11.
    public void testSetValue_emptyRemove() {
        DOMNodePointer textPtr = new DOMNodePointer(textNode, Locale.US);
        Node parent = textNode.getParentNode();
        assertNotNull(parent);
        textPtr.setValue("");
        // verify node removed
        assertNull(textNode.getParentNode());
    }

    // 12.
    public void testRemove() {
        DOMNodePointer childPtr = new DOMNodePointer(child, Locale.US);
        childPtr.remove();
        assertNull(child.getParentNode());
    }

    // 13.
    

    // 14.
    public void testCompareChildNodePointers() {
        DOMNodePointer childPtr = new DOMNodePointer(child, Locale.US);
        DOMNodePointer piPtr = new DOMNodePointer(pi, Locale.US);
        // child comes before pi in document order
        assertTrue(rootPtr.compareChildNodePointers(childPtr, piPtr) < 0);
        assertTrue(rootPtr.compareChildNodePointers(piPtr, childPtr) > 0);
        // same node
        assertEquals(0, rootPtr.compareChildNodePointers(childPtr, childPtr));
    }

    // 15.
    
}
