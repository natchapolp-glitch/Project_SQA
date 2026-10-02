package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Regression tests for DOMNodePointer covering method signatures from fixed revision 1f.
 * Fixtures are constructed from DOM API; assertions derive from source code logic.
 * Tests use JUnit 3.x compatible inheritance.
 */
public class DOMNodePointerTest extends TestCase {

    private Document doc;
    private Element rootElement;
    private DocumentBuilder docBuilder;

    protected void setUp() throws Exception {
        super.setUp();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        docBuilder = factory.newDocumentBuilder();
        doc = docBuilder.newDocument();
        rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
    }

    protected void tearDown() throws Exception {
        doc = null;
        rootElement = null;
        docBuilder = null;
        super.tearDown();
    }

    // ============= Constructor Tests =============

    public void testConstructorWithNodeAndLocale() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertNotNull(pointer);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testConstructorWithNodeLocaleAndId() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US, "test-id");
        assertNotNull(pointer);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testConstructorWithParentAndNode() {
        DOMNodePointer parentPointer = new DOMNodePointer(rootElement, Locale.US);
        Element childElement = doc.createElement("child");
        rootElement.appendChild(childElement);
        DOMNodePointer childPointer = new DOMNodePointer(parentPointer, childElement);
        assertNotNull(childPointer);
        assertEquals(childElement, childPointer.getImmediateNode());
    }

    // ============= Basic Node Information Tests =============

    public void testGetImmediateNode() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testGetBaseValue() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(rootElement, pointer.getBaseValue());
    }

    public void testIsActual() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertTrue(pointer.isActual());
    }

    public void testIsCollection() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertFalse(pointer.isCollection());
    }

    public void testGetLength() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(1, pointer.getLength());
    }

    public void testIsLeafWithNoChildren() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertTrue(pointer.isLeaf());
    }

    public void testIsLeafWithChildren() {
        Element child = doc.createElement("child");
        rootElement.appendChild(child);
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertFalse(pointer.isLeaf());
    }

    // ============= Name and Namespace Tests =============

    public void testGetNameForElement() {
        QName name = new DOMNodePointer(rootElement, Locale.US).getName();
        assertNotNull(name);
        assertEquals("root", name.getName());
    }

    public void testGetNameForProcessingInstruction() {
        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        rootElement.appendChild(pi);
        DOMNodePointer pointer = new DOMNodePointer(pi, Locale.US);
        QName name = pointer.getName();
        assertNotNull(name);
        assertEquals("target", name.getName());
    }

    public void testGetNameForTextNode() {
        Text textNode = doc.createTextNode("text content");
        rootElement.appendChild(textNode);
        DOMNodePointer pointer = new DOMNodePointer(textNode, Locale.US);
        QName name = pointer.getName();
        assertNotNull(name);
    }

    public void testGetNamespaceURIForElementWithoutNamespace() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        // Source code indicates getNamespaceURI(node) returns null if no xmlns
        String nsUri = pointer.getNamespaceURI();
        // May be null or empty depending on DOM implementation
        assertTrue(nsUri == null || nsUri.length() == 0);
    }

    public void testGetDefaultNamespaceURIWhenNotSet() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        String defaultNs = pointer.getDefaultNamespaceURI();
        // Source caches empty string as "" and returns null if empty
        assertTrue(defaultNs == null || defaultNs.length() == 0);
    }

    // ============= Static Utility Method Tests =============

    

    

    

    

    

    

    

    public void testGetLocalNameForElement() {
        String localName = DOMNodePointer.getLocalName(rootElement);
        assertEquals("root", localName);
    }

    public void testGetLocalNameWithNamespace() {
        // Source extracts local name after ':' if present
        Element nsElement = doc.createElementNS("http://example.com", "ex:element");
        String localName = DOMNodePointer.getLocalName(nsElement);
        assertEquals("element", localName);
    }

    public void testGetPrefixForElementWithoutPrefix() {
        String prefix = DOMNodePointer.getPrefix(rootElement);
        assertNull(prefix);
    }

    public void testGetPrefixForElementWithNamespace() {
        Element nsElement = doc.createElementNS("http://example.com", "ex:element");
        String prefix = DOMNodePointer.getPrefix(nsElement);
        assertEquals("ex", prefix);
    }

    public void testGetNamespaceURIForElement() {
        Element nsElement = doc.createElementNS("http://example.com", "ex:element");
        String nsUri = DOMNodePointer.getNamespaceURI(nsElement);
        assertEquals("http://example.com", nsUri);
    }

    // ============= Node Test Evaluation =============

    public void testTestNodeWithNullTest() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        // Source: testNode(node, null) returns true
        assertTrue(DOMNodePointer.testNode(rootElement, null));
    }

    public void testTestNodeWithElementNodeType() {
        NodeTypeTest test = new NodeTypeTest(1); // Node.ELEMENT_NODE
        assertTrue(DOMNodePointer.testNode(rootElement, test));
    }

    public void testTestNodeWithTextNodeType() {
        Text textNode = doc.createTextNode("text");
        NodeTypeTest test = new NodeTypeTest(3); // Node.TEXT_NODE
        assertTrue(DOMNodePointer.testNode(textNode, test));
    }

    public void testTestNodeElementNameTest() {
        QName qname = new QName("root");
        NodeNameTest nameTest = new NodeNameTest(qname, null);
        assertTrue(DOMNodePointer.testNode(rootElement, nameTest));
    }

    public void testTestNodeElementNameTestMismatch() {
        QName qname = new QName("notroot");
        NodeNameTest nameTest = new NodeNameTest(qname, null);
        assertFalse(DOMNodePointer.testNode(rootElement, nameTest));
    }

    public void testTestNodeProcessingInstructionTest() {
        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        ProcessingInstructionTest piTest = new ProcessingInstructionTest("target");
        assertTrue(DOMNodePointer.testNode(pi, piTest));
    }

    public void testTestNodeProcessingInstructionMismatch() {
        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        ProcessingInstructionTest piTest = new ProcessingInstructionTest("other");
        assertFalse(DOMNodePointer.testNode(pi, piTest));
    }

    public void testTestNodeCommentType() {
        org.w3c.dom.Comment comment = doc.createComment("comment text");
        NodeTypeTest test = new NodeTypeTest(8); // Node.COMMENT_NODE
        assertTrue(DOMNodePointer.testNode(comment, test));
    }

    // ============= Value and Language Tests =============

    public void testGetValueForElement() {
        rootElement.setTextContent("element text");
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        Object value = pointer.getValue();
        assertEquals("element text", value);
    }

    public void testGetValueForTextNode() {
        Text textNode = doc.createTextNode("  text content  ");
        DOMNodePointer pointer = new DOMNodePointer(textNode, Locale.US);
        Object value = pointer.getValue();
        assertEquals("text content", value);
    }

    

    

    

    

    // ============= Escape and Path Tests =============

    

    

    

    

    

    

    // ============= Hash and Equality Tests =============

    

    

    

    

    

    // ============= Relative Position Tests =============

    

    

    

    

    

    

    

    

    

    // ============= Set Value Tests =============

    

    

    

    

    // ============= Compare Child Pointers Tests =============

    

    

    

    

    // ============= Helper Methods =============

    private DOMNodePointer pointer(Node node) {
        return new DOMNodePointer(node, Locale.US);
    }
}
