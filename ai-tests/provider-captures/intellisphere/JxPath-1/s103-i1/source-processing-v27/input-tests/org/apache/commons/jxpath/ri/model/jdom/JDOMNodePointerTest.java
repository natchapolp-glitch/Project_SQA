package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

public class JDOMNodePointerTest extends TestCase {

    private Document document;
    private Element root;
    private Element child1;
    private Element child2;
    private Text textNode;
    private CDATA cdataNode;
    private ProcessingInstruction piNode;
    private Comment commentNode;
    private JDOMNodePointer rootPointer;
    private JDOMNodePointer child1Pointer;
    private JDOMNodePointer child2Pointer;
    private JDOMNodePointer textPointer;
    private JDOMNodePointer cdataPointer;
    private JDOMNodePointer piPointer;
    private JDOMNodePointer commentPointer;

    protected void setUp() throws Exception {
        root = new Element("root");
        root.setAttribute("lang", "en-US", org.jdom.Namespace.XML_NAMESPACE);
        document = new Document(root);
        
        child1 = new Element("child");
        child1.setAttribute("id", "c1");
        root.addContent(child1);
        
        textNode = new Text("some text");
        root.addContent(textNode);
        
        cdataNode = new CDATA("cdata content");
        root.addContent(cdataNode);
        
        piNode = new ProcessingInstruction("target", "data");
        root.addContent(piNode);
        
        child2 = new Element("child");
        root.addContent(child2);
        
        commentNode = new Comment("comment text");
        root.addContent(commentNode);

        rootPointer = new JDOMNodePointer(root, Locale.US);
        child1Pointer = new JDOMNodePointer(rootPointer, child1);
        child2Pointer = new JDOMNodePointer(rootPointer, child2);
        textPointer = new JDOMNodePointer(rootPointer, textNode);
        cdataPointer = new JDOMNodePointer(rootPointer, cdataNode);
        piPointer = new JDOMNodePointer(rootPointer, piNode);
        commentPointer = new JDOMNodePointer(rootPointer, commentNode);
    }

    // --- Tests for getRelativePositionByName ---
    public void testGetRelativePositionByNameFirstChild() {
        assertEquals(1, ((JDOMNodePointer) child1Pointer).getRelativePositionByName());
    }

    public void testGetRelativePositionByNameSecondChild() {
        assertEquals(2, ((JDOMNodePointer) child2Pointer).getRelativePositionByName());
    }

    public void testGetRelativePositionByNameNonElement() {
        assertEquals(1, ((JDOMNodePointer) textPointer).getRelativePositionByName());
    }

    // --- Tests for getRelativePositionOfElement ---
    public void testGetRelativePositionOfElementFirst() {
        assertEquals(1, ((JDOMNodePointer) child1Pointer).getRelativePositionOfElement());
    }

    public void testGetRelativePositionOfElementSecond() {
        assertEquals(2, ((JDOMNodePointer) child2Pointer).getRelativePositionOfElement());
    }

    // --- Tests for getRelativePositionOfPI ---
    public void testGetRelativePositionOfPIFirst() {
        assertEquals(1, ((JDOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    public void testGetRelativePositionOfPIDifferentTargets() {
        ProcessingInstruction pi2 = new ProcessingInstruction("other", "data2");
        root.addContent(0, pi2);
        JDOMNodePointer pi2Pointer = new JDOMNodePointer(rootPointer, pi2);
        assertEquals(1, ((JDOMNodePointer) pi2Pointer).getRelativePositionOfPI("other"));
        assertEquals(1, ((JDOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    public void testGetRelativePositionOfPISameTarget() {
        ProcessingInstruction pi2 = new ProcessingInstruction("target", "data2");
        root.addContent(0, pi2);
        JDOMNodePointer pi2Pointer = new JDOMNodePointer(rootPointer, pi2);
        assertEquals(1, ((JDOMNodePointer) pi2Pointer).getRelativePositionOfPI("target"));
        assertEquals(2, ((JDOMNodePointer) piPointer).getRelativePositionOfPI("target"));
    }

    // --- Tests for getRelativePositionOfTextNode ---
    public void testGetRelativePositionOfTextNodeFirst() {
        assertEquals(1, ((JDOMNodePointer) textPointer).getRelativePositionOfTextNode());
    }

    public void testGetRelativePositionOfTextNodeSecond() {
        Text text2 = new Text("more text");
        root.addContent(text2);
        JDOMNodePointer text2Pointer = new JDOMNodePointer(rootPointer, text2);
        assertEquals(2, ((JDOMNodePointer) text2Pointer).getRelativePositionOfTextNode());
    }

    public void testGetRelativePositionOfTextNodeMultipleTypes() {
        assertEquals(2, ((JDOMNodePointer) cdataPointer).getRelativePositionOfTextNode());
    }

    // --- Tests for escape ---
    public void testEscapeNoSpecialChars() {
        assertEquals("test", ((JDOMNodePointer) rootPointer).escape("test"));
    }

    public void testEscapeSingleQuote() {
        assertEquals("test&apos;value", ((JDOMNodePointer) rootPointer).escape("test'value"));
    }

    public void testEscapeDoubleQuote() {
        assertEquals("test&quot;value", ((JDOMNodePointer) rootPointer).escape("test\"value"));
    }

    // --- Tests for getValue ---
    public void testGetValueElement() {
        assertEquals("some textcdata content", rootPointer.getValue());
    }

    public void testGetValueTextNode() {
        assertEquals("some text", textPointer.getValue());
    }

    public void testGetValueCDATANode() {
        assertEquals("cdata content", cdataPointer.getValue());
    }

    public void testGetValuePINode() {
        assertEquals("data", piPointer.getValue());
    }

    public void testGetValueCommentNode() {
        assertEquals("comment text", commentPointer.getValue());
    }

    // --- Tests for getLanguage ---
    public void testGetLanguageRoot() {
        assertEquals("en-US", rootPointer.getLanguage());
    }

    public void testGetLanguageChildInherits() {
        assertEquals("en-US", child1Pointer.getLanguage());
    }

    public void testGetLanguageNone() {
        Element noLang = new Element("noLang");
        JDOMNodePointer noLangPointer = new JDOMNodePointer(noLang, Locale.US);
        assertNull(noLangPointer.getLanguage());
    }

    // --- Tests for equals and hashCode ---
    public void testEqualsSameObject() {
        assertTrue(rootPointer.equals(rootPointer));
    }

    public void testEqualsSameNode() {
        JDOMNodePointer p1 = new JDOMNodePointer(root, Locale.US);
        JDOMNodePointer p2 = new JDOMNodePointer(root, Locale.US);
        assertTrue(p1.equals(p2));
    }

    public void testEqualsDifferentNodes() {
        assertFalse(child1Pointer.equals(child2Pointer));
    }

    public void testEqualsNonJDOMNodePointer() {
        assertFalse(rootPointer.equals(new Object()));
    }

    public void testHashCodeEqualsSameNode() {
        JDOMNodePointer p1 = new JDOMNodePointer(root, Locale.US);
        JDOMNodePointer p2 = new JDOMNodePointer(root, Locale.US);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    // --- Tests for isCollection, getLength, isLeaf ---
    public void testIsCollection() {
        assertFalse(rootPointer.isCollection());
    }

    public void testGetLength() {
        assertEquals(1, rootPointer.getLength());
    }

    public void testIsLeafFalseElement() {
        assertFalse(rootPointer.isLeaf());
    }

    public void testIsLeafTrueEmptyElement() {
        Element empty = new Element("empty");
        JDOMNodePointer emptyPointer = new JDOMNodePointer(empty, Locale.US);
        assertTrue(emptyPointer.isLeaf());
    }

    public void testIsLeafDocument() {
        JDOMNodePointer docPointer = new JDOMNodePointer(new Document(), Locale.US);
        assertTrue(docPointer.isLeaf());
    }

    // --- Tests for getBaseValue and getImmediateNode ---
    public void testGetBaseValue() {
        assertSame(root, rootPointer.getBaseValue());
    }

    public void testGetImmediateNode() {
        assertSame(root, rootPointer.getImmediateNode());
    }
}
