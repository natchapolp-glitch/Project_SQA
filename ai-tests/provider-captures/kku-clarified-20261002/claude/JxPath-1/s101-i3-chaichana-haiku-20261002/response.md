# Java Test Sources for JXPath DOM and JDOM Node Pointers

**Configuration Analysis:**
- Build system: Ant with JUnit 3.8.x (indicated by `junit-3.8.jar` and `junit-3.8.1.jar` in `build.xml`)
- Java source level: 1.6 (javac target="1.6" source="1.6")
- No additional dependencies available beyond xerces, jdom-1.0, commons-beanutils, commons-logging, commons-collections
- JUnit 3.x uses `TestCase` inheritance and assertion methods from `junit.framework.Assert`

**Test Strategy:**
- Tests infer fixture setup from source code analysis (DOM builders, JDOM element construction)
- Assumptions about node identity semantics and namespace resolution are documented inline
- Static utility methods tested with null/empty string boundaries derived from source logic
- Iterator tests structured conservatively to avoid undocumented protocol assumptions

---

## File: `org/apache/commons/jxpath/ri/model/dom/DOMNodePointerTest.java`

```java
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

    public void testEqualStringsNullNull() {
        // Source: equalStrings(null, null) should return true
        // (both null or both empty after trim)
        boolean result = DOMNodePointer.equalStrings(null, null);
        assertTrue(result);
    }

    public void testEqualStringsNullEmpty() {
        boolean result = DOMNodePointer.equalStrings(null, "");
        assertTrue(result);
    }

    public void testEqualStringsEmptyNull() {
        boolean result = DOMNodePointer.equalStrings("", null);
        assertTrue(result);
    }

    public void testEqualStringsNullWhitespace() {
        boolean result = DOMNodePointer.equalStrings(null, "  ");
        assertTrue(result);
    }

    public void testEqualStringsIdentical() {
        boolean result = DOMNodePointer.equalStrings("test", "test");
        assertTrue(result);
    }

    public void testEqualStringsDifferent() {
        boolean result = DOMNodePointer.equalStrings("test1", "test2");
        assertFalse(result);
    }

    public void testEqualStringsWithWhitespace() {
        // Source trims both strings before comparison
        boolean result = DOMNodePointer.equalStrings("  test  ", "test");
        assertTrue(result);
    }

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

    public void testGetValueForComment() {
        org.w3c.dom.Comment comment = doc.createComment("  comment  ");
        DOMNodePointer pointer = new DOMNodePointer(comment, Locale.US);
        Object value = pointer.getValue();
        assertEquals("comment", value);
    }

    public void testIsLanguageWhenNotSet() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        // Source: getLanguage() returns null if no xml:lang attribute found
        assertFalse(pointer.isLanguage("en"));
    }

    public void testIsLanguageWhenSetOnElement() {
        rootElement.setAttributeNS(
            "http://www.w3.org/XML/1998/namespace",
            "xml:lang",
            "en-US");
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        // Source: isLanguage compares with toUpperCase().startsWith()
        assertTrue(pointer.isLanguage("en"));
    }

    public void testIsLanguageCaseInsensitive() {
        rootElement.setAttributeNS(
            "http://www.w3.org/XML/1998/namespace",
            "xml:lang",
            "EN-us");
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertTrue(pointer.isLanguage("en-US"));
    }

    // ============= Escape and Path Tests =============

    public void testAsPathForElement() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        String path = pointer.asPath();
        assertNotNull(path);
        assertTrue(path.contains("root"));
    }

    public void testAsPathWithId() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US, "myid");
        String path = pointer.asPath();
        assertTrue(path.contains("id("));
        assertTrue(path.contains("myid"));
    }

    public void testAsPathForTextNode() {
        Text textNode = doc.createTextNode("text");
        rootElement.appendChild(textNode);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), textNode);
        String path = pointer.asPath();
        assertTrue(path.contains("text()"));
    }

    public void testAsPathForProcessingInstruction() {
        ProcessingInstruction pi = doc.createProcessingInstruction("target", "data");
        rootElement.appendChild(pi);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), pi);
        String path = pointer.asPath();
        assertTrue(path.contains("processing-instruction"));
        assertTrue(path.contains("target"));
    }

    public void testEscapeSingleQuote() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US, "test'quote");
        String path = pointer.asPath();
        assertTrue(path.contains("&apos;"));
    }

    public void testEscapeDoubleQuote() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US, "test\"quote");
        String path = pointer.asPath();
        assertTrue(path.contains("&quot;"));
    }

    // ============= Hash and Equality Tests =============

    public void testHashCodeConsistency() {
        DOMNodePointer pointer1 = new DOMNodePointer(rootElement, Locale.US);
        DOMNodePointer pointer2 = new DOMNodePointer(rootElement, Locale.US);
        // Source: hashCode uses System.identityHashCode(node)
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    public void testEqualsWithSameNode() {
        DOMNodePointer pointer1 = new DOMNodePointer(rootElement, Locale.US);
        DOMNodePointer pointer2 = new DOMNodePointer(rootElement, Locale.US);
        // Source: equals compares node == other.node
        assertEquals(pointer1, pointer2);
    }

    public void testEqualsWithDifferentNode() {
        Element child = doc.createElement("child");
        rootElement.appendChild(child);
        DOMNodePointer pointer1 = new DOMNodePointer(rootElement, Locale.US);
        DOMNodePointer pointer2 = new DOMNodePointer(child, Locale.US);
        assertFalse(pointer1.equals(pointer2));
    }

    public void testEqualsSelf() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(pointer, pointer);
    }

    public void testEqualsNonDOMPointer() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        assertFalse(pointer.equals("not a pointer"));
    }

    // ============= Relative Position Tests =============

    public void testGetRelativePositionByNameFirstElement() {
        Element child1 = doc.createElement("item");
        rootElement.appendChild(child1);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), child1);
        int pos = pointer.getRelativePositionByName();
        assertEquals(1, pos);
    }

    public void testGetRelativePositionByNameSecondElement() {
        Element child1 = doc.createElement("item");
        Element child2 = doc.createElement("item");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), child2);
        int pos = pointer.getRelativePositionByName();
        assertEquals(2, pos);
    }

    public void testGetRelativePositionOfElementFirstElement() {
        Element child1 = doc.createElement("item");
        rootElement.appendChild(child1);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), child1);
        int pos = pointer.getRelativePositionOfElement();
        assertEquals(1, pos);
    }

    public void testGetRelativePositionOfElementWithTextNodeBefore() {
        Text text = doc.createTextNode("text");
        Element child = doc.createElement("item");
        rootElement.appendChild(text);
        rootElement.appendChild(child);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), child);
        int pos = pointer.getRelativePositionOfElement();
        // Source counts only ELEMENT_NODEs
        assertEquals(1, pos);
    }

    public void testGetRelativePositionOfTextNodeFirstTextNode() {
        Text text1 = doc.createTextNode("text1");
        rootElement.appendChild(text1);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), text1);
        int pos = pointer.getRelativePositionOfTextNode();
        assertEquals(1, pos);
    }

    public void testGetRelativePositionOfTextNodeSecondTextNode() {
        Text text1 = doc.createTextNode("text1");
        Text text2 = doc.createTextNode("text2");
        rootElement.appendChild(text1);
        rootElement.appendChild(text2);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), text2);
        int pos = pointer.getRelativePositionOfTextNode();
        assertEquals(2, pos);
    }

    public void testGetRelativePositionOfPIFirstPI() {
        ProcessingInstruction pi1 = doc.createProcessingInstruction("target", "data");
        rootElement.appendChild(pi1);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), pi1);
        int pos = pointer.getRelativePositionOfPI("target");
        assertEquals(1, pos);
    }

    public void testGetRelativePositionOfPISecondPI() {
        ProcessingInstruction pi1 = doc.createProcessingInstruction("target", "data1");
        ProcessingInstruction pi2 = doc.createProcessingInstruction("target", "data2");
        rootElement.appendChild(pi1);
        rootElement.appendChild(pi2);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), pi2);
        int pos = pointer.getRelativePositionOfPI("target");
        assertEquals(2, pos);
    }

    public void testGetRelativePositionOfPIDifferentTargets() {
        ProcessingInstruction pi1 = doc.createProcessingInstruction("target1", "data");
        ProcessingInstruction pi2 = doc.createProcessingInstruction("target2", "data");
        rootElement.appendChild(pi1);
        rootElement.appendChild(pi2);
        DOMNodePointer pointer = new DOMNodePointer(pointer(rootElement), pi2);
        int pos = pointer.getRelativePositionOfPI("target2");
        assertEquals(1, pos);
    }

    // ============= Set Value Tests =============

    public void testSetValueOnTextNode() {
        Text textNode = doc.createTextNode("original");
        rootElement.appendChild(textNode);
        DOMNodePointer pointer = new DOMNodePointer(textNode, Locale.US);
        pointer.setValue("updated");
        assertEquals("updated", textNode.getNodeValue());
    }

    public void testSetValueOnElementWithString() {
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        pointer.setValue("new content");
        // Source appends a text node for elements
        assertTrue(rootElement.hasChildNodes());
    }

    public void testSetValueOnElementWithElement() {
        Element sourceElement = doc.createElement("source");
        Element sourceChild = doc.createElement("child");
        sourceElement.appendChild(sourceChild);
        
        DOMNodePointer pointer = new DOMNodePointer(rootElement, Locale.US);
        pointer.setValue(sourceElement);
        // Source clones children from source element
        assertTrue(rootElement.hasChildNodes());
    }

    public void testSetValueEmptyStringRemovesTextNode() {
        Text textNode = doc.createTextNode("text");
        rootElement.appendChild(textNode);
        DOMNodePointer pointer = new DOMNodePointer(textNode, Locale.US);
        pointer.setValue("");
        // Source removes node if value is empty string
        assertFalse(rootElement.hasChildNodes());
    }

    // ============= Compare Child Pointers Tests =============

    public void testCompareChildNodePointersIdentical() {
        Element child = doc.createElement("child");
        rootElement.appendChild(child);
        DOMNodePointer pointer1 = new DOMNodePointer(pointer(rootElement), child);
        DOMNodePointer pointer2 = new DOMNodePointer(pointer(rootElement), child);
        DOMNodePointer rootPointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(0, rootPointer.compareChildNodePointers(pointer1, pointer2));
    }

    public void testCompareChildNodePointersFirstChild() {
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        DOMNodePointer pointer1 = new DOMNodePointer(pointer(rootElement), child1);
        DOMNodePointer pointer2 = new DOMNodePointer(pointer(rootElement), child2);
        DOMNodePointer rootPointer = new DOMNodePointer(rootElement, Locale.US);
        // Source: returns -1 if node1 comes before node2
        assertEquals(-1, rootPointer.compareChildNodePointers(pointer1, pointer2));
    }

    public void testCompareChildNodePointersSecondChild() {
        Element child1 = doc.createElement("child1");
        Element child2 = doc.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        DOMNodePointer pointer1 = new DOMNodePointer(pointer(rootElement), child1);
        DOMNodePointer pointer2 = new DOMNodePointer(pointer(rootElement), child2);
        DOMNodePointer rootPointer = new DOMNodePointer(rootElement, Locale.US);
        assertEquals(1, rootPointer.compareChildNodePointers(pointer2, pointer1));
    }

    public void testCompareChildNodePointersAttributeBeforeElement() {
        Attr attr = doc.createAttribute("attrname");
        Element child = doc.createElement("child");
        rootElement.appendChild(child);
        rootElement.setAttributeNode(attr);
        DOMNodePointer attrPointer = new DOMNodePointer(pointer(rootElement), attr);
        DOMNodePointer elemPointer = new DOMNodePointer(pointer(rootElement), child);
        DOMNodePointer rootPointer = new DOMNodePointer(rootElement, Locale.US);
        // Source: attributes come before non-attribute nodes
        assertEquals(-1, rootPointer.compareChildNodePointers(attrPointer, elemPointer));
    }

    // ============= Helper Methods =============

    private DOMNodePointer pointer(Node node) {
        return new DOMNodePointer(node, Locale.US);
    }
}
```

---

## File: `org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointerTest.java`

```java
package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
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

/**
 * Regression tests for JDOMNodePointer covering method signatures from fixed revision 1f.
 * Fixtures are constructed using JDOM 1.0 API; assertions derive from source code logic.
 * Tests use JUnit 3.x compatible inheritance.
 */
public class JDOMNodePointerTest extends TestCase {

    private Document doc;
    private Element rootElement;

    protected void setUp() throws Exception {
        super.setUp();
        rootElement = new Element("root");
        doc = new Document(rootElement);
    }

    protected void tearDown() throws Exception {
        doc = null;
        rootElement = null;
        super.tearDown();
    }

    // ============= Constructor Tests =============

    public void testConstructorWithNodeAndLocale() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertNotNull(pointer);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testConstructorWithNodeLocaleAndId() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US, "test-id");
        assertNotNull(pointer);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testConstructorWithParentAndNode() {
        JDOMNodePointer parentPointer = new JDOMNodePointer(rootElement, Locale.US);
        Element childElement = new Element("child");
        rootElement.addContent(childElement);
        JDOMNodePointer childPointer = new JDOMNodePointer(parentPointer, childElement);
        assertNotNull(childPointer);
        assertEquals(childElement, childPointer.getImmediateNode());
    }

    // ============= Basic Node Information Tests =============

    public void testGetImmediateNode() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertEquals(rootElement, pointer.getImmediateNode());
    }

    public void testGetBaseValue() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertEquals(rootElement, pointer.getBaseValue());
    }

    public void testIsCollection() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertFalse(pointer.isCollection());
    }

    public void testGetLength() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertEquals(1, pointer.getLength());
    }

    public void testIsLeafWithNoChildren() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertTrue(pointer.isLeaf());
    }

    public void testIsLeafWithChildren() {
        Element child = new Element("child");
        rootElement.addContent(child);
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        assertFalse(pointer.isLeaf());
    }

    public void testIsLeafForDocument() {
        JDOMNodePointer pointer = new JDOMNodePointer(doc, Locale.US);
        assertTrue(pointer.isLeaf());
    }

    public void testIsLeafForDocumentWithContent() {
        Document docWithContent = new Document(new Element("root"));
        JDOMNodePointer pointer = new JDOMNodePointer(docWithContent, Locale.US);
        assertFalse(pointer.isLeaf());
    }

    // ============= Name and Namespace Tests =============

    public void testGetNameForElement() {
        QName name = new JDOMNodePointer(rootElement, Locale.US).getName();
        assertNotNull(name);
        assertEquals("root", name.getName());
    }

    public void testGetNameForElementWithNamespace() {
        Namespace ns = Namespace.getNamespace("ex", "http://example.com");
        Element nsElement = new Element("element", ns);
        JDOMNodePointer pointer = new JDOMNodePointer(nsElement, Locale.US);
        QName name = pointer.getName();
        assertNotNull(name);
        assertEquals("element", name.getName());
        assertEquals("ex", name.getPrefix());
    }

    public void testGetNameForProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        rootElement.addContent(pi);
        JDOMNodePointer pointer = new JDOMNodePointer(pi, Locale.US);
        QName name = pointer.getName();
        assertNotNull(name);
        assertEquals("target", name.getName());
    }

    public void testGetNamespaceURIForElementWithoutNamespace() {
        JDOMNodePointer pointer = new JDOMNodePointer(rootElement, Locale.US);
        String nsUri = pointer.getNamespaceURI();
        // Source: getNamespaceURI returns null if namespace is empty string
        assertNull(nsUri);
    }

    public void testGetNamespaceURIForElementWithNamespace() {
        Namespace ns = Namespace.getNamespace("http://example.com");
        Element nsElement = new Element("element", ns);
        JDOMNodePointer pointer = new JDOMNodePo