package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DOMNodePointerTest extends TestCase {

    private Document document;
    private Element root;
    private Element child1;
    private Element child2;
    private Text textNode;
    private ProcessingInstruction piNode;
    private DOMNodePointer rootPointer;
    private DOMNodePointer child1Pointer;
    private DOMNodePointer child2Pointer;
    private DOMNodePointer textPointer;
    private DOMNodePointer piPointer;

    protected void setUp() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.newDocument();
        root = document.createElement("root");
        root.setAttribute("xml:lang", "en-US");
        document.appendChild(root);
        
        child1 = document.createElement("child");
        child1.setAttribute("id", "c1");
        root.appendChild(child1);
        
        textNode = document.createTextNode("some text");
        root.appendChild(textNode);
        
        piNode = document.createProcessingInstruction("target", "data");
        root.appendChild(piNode);
        
        child2 = document.createElement("child");
        root.appendChild(child2);

        rootPointer = new DOMNodePointer(root, Locale.US);
        child1Pointer = new DOMNodePointer(rootPointer, child1);
        child2Pointer = new DOMNodePointer(rootPointer, child2);
        textPointer = new DOMNodePointer(rootPointer, textNode);
        piPointer = new DOMNodePointer(rootPointer, piNode);
    }

    // --- Tests for getRelativePositionByName ---
    
    public void testGetRelativePositionByNameFirstChild() {
        assertEquals(1, ((DOMNodePointer) child1Pointer).getRelativePositionByName());
    }

    public void testGetRelativePositionByNameSecondChild() {
        assertEquals(2, ((DOMNodePointer) child2Pointer).getRelativePositionByName());
    }

    public void testGetRelativePositionByNameOnlyChild() {
        Element solo = document.createElement("solo");
        root.insertBefore(solo, root.getFirstChild());
        DOMNodePointer soloPointer = new DOMNodePointer(rootPointer, solo);
        assertEquals(1, ((DOMNodePointer) soloPointer).getRelativePositionByName());
    }

    // --- Tests for getRelativePositionOfElement ---
    public void testGetRelativePositionOfElementFirst() {
        assertEquals(1, ((DOMNodePointer) child1Pointer).getRelativePositionOfElement());
    }

    public void testGetRelativePositionOfElementSecond() {
        assertEquals(2, ((DOMNodePointer) child2Pointer).getRelativePositionOfElement());
    }

    public void testGetRelativePositionOfElementSolo() {
        Element newElement = document.createElement("newElement");
        root.insertBefore(newElement, root.getFirstChild());
        DOMNodePointer newPointer = new DOMNodePointer(rootPointer, newElement);
        assertEquals(1, ((DOMNodePointer) newPointer).getRelativePositionOfElement());
    }

    // --- Tests for getRelativePositionOfPI ---
    public void testGetRelativePositionOfPIFirst() {
        assertEquals(1, ((DOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    public void testGetRelativePositionOfPIDifferentTarget() {
        ProcessingInstruction pi2 = document.createProcessingInstruction("other", "data2");
        root.insertBefore(pi2, piNode);
        DOMNodePointer pi2Pointer = new DOMNodePointer(rootPointer, pi2);
        assertEquals(1, ((DOMNodePointer) pi2Pointer).getRelativePositionOfPI("other"));
        assertEquals(1, ((DOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    public void testGetRelativePositionOfPISameTarget() {
        ProcessingInstruction pi2 = document.createProcessingInstruction("target", "data2");
        root.insertBefore(pi2, piNode);
        DOMNodePointer pi2Pointer = new DOMNodePointer(rootPointer, pi2);
        assertEquals(1, ((DOMNodePointer) pi2Pointer).getRelativePositionOfPI("target"));
        assertEquals(2, ((DOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    // --- Tests for getRelativePositionOfTextNode ---
    public void testGetRelativePositionOfTextNodeFirst() {
        assertEquals(1, ((DOMNodePointer) textPointer).getRelativePositionOfTextNode());
    }

    public void testGetRelativePositionOfTextNodeSecond() {
        Text text2 = document.createTextNode("more text");
        root.appendChild(text2);
        DOMNodePointer text2Pointer = new DOMNodePointer(rootPointer, text2);
        assertEquals(2, ((DOMNodePointer) text2Pointer).getRelativePositionOfTextNode());
    }

    // --- Tests for escape ---
    public void testEscapeNoSpecialChars() {
        assertEquals("test", ((DOMNodePointer) rootPointer).escape("test"));
    }

    public void testEscapeSingleQuote() {
        assertEquals("test&apos;value", ((DOMNodePointer) rootPointer).escape("test'value"));
    }

    public void testEscapeDoubleQuote() {
        assertEquals("test&quot;value", ((DOMNodePointer) rootPointer).escape("test\"value"));
    }

    // --- Tests for stringValue ---
    public void testStringValueElement() {
        assertEquals("some textsome more text", ((DOMNodePointer) rootPointer).stringValue(root));
    }

    public void testStringValueTextNode() {
        assertEquals("some text", ((DOMNodePointer) rootPointer).stringValue(textNode));
    }

    public void testStringValuePINode() {
        assertEquals("data", ((DOMNodePointer) rootPointer).stringValue(piNode));
    }

    // --- Tests for getLanguage ---
    public void testGetLanguageRoot() {
        assertEquals("en-US", rootPointer.getLanguage());
    }

    public void testGetLanguageChildInherits() {
        assertEquals("en-US", child1Pointer.getLanguage());
    }

    public void testGetLanguageNone() {
        Element noLang = document.createElement("noLang");
        DOMNodePointer noLangPointer = new DOMNodePointer(rootPointer, noLang);
        assertNull(noLangPointer.getLanguage());
    }

    // --- Tests for equals and hashCode ---
    public void testEqualsSameObject() {
        assertTrue(rootPointer.equals(rootPointer));
    }

    public void testEqualsSameNode() {
        DOMNodePointer p1 = new DOMNodePointer(root, Locale.US);
        DOMNodePointer p2 = new DOMNodePointer(root, Locale.US);
        assertTrue(p1.equals(p2));
    }

    public void testEqualsDifferentNodes() {
        assertFalse(child1Pointer.equals(child2Pointer));
    }

    public void testEqualsNonDOMNodePointer() {
        assertFalse(rootPointer.equals(new Object()));
    }

    public void testHashCodeEqualsSameNode() {
        DOMNodePointer p1 = new DOMNodePointer(root, Locale.US);
        DOMNodePointer p2 = new DOMNodePointer(root, Locale.US);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    // --- Tests for isActual, isCollection, getLength, isLeaf ---
    public void testIsActual() {
        assertTrue(rootPointer.isActual());
    }

    public void testIsCollection() {
        assertFalse(rootPointer.isCollection());
    }

    public void testGetLength() {
        assertEquals(1, rootPointer.getLength());
    }

    public void testIsLeafFalse() {
        assertFalse(rootPointer.isLeaf());
    }

    public void testIsLeafTrueEmpty() {
        Element empty = document.createElement("empty");
        DOMNodePointer emptyPointer = new DOMNodePointer(rootPointer, empty);
        assertTrue(emptyPointer.isLeaf());
    }

    // --- Tests for getBaseValue and getImmediateNode ---
    public void testGetBaseValue() {
        assertSame(root, rootPointer.getBaseValue());
    }

    public void testGetImmediateNode() {
        assertSame(root, rootPointer.getImmediateNode());
    }
}
