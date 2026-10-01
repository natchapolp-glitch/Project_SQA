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

    private static int invokeOriginalJdomPosition(JDOMNodePointer pointer, String name,
            Class[] parameterTypes, Object[] arguments) {
        try {
            java.lang.reflect.Method method = JDOMNodePointer.class
                .getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return ((Integer) method.invoke(pointer, arguments)).intValue();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }


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
    

    

    

    // --- Tests for getRelativePositionOfElement ---
    public void testGetRelativePositionOfElementFirst() {
        assertEquals(1, invokeOriginalJdomPosition((JDOMNodePointer) child1Pointer, "getRelativePositionOfElement", new Class[0], new Object[0]));
    }

    

    // --- Tests for getRelativePositionOfPI ---
    

    

    

    // --- Tests for getRelativePositionOfTextNode ---
    

    

    

    // --- Tests for escape ---
    

    

    

    // --- Tests for getValue ---
    

    

    

    

    

    // --- Tests for getLanguage ---
    

    

    

    // --- Tests for equals and hashCode ---
    

    

    

    

    

    // --- Tests for isCollection, getLength, isLeaf ---
    

    

    

    

    

    // --- Tests for getBaseValue and getImmediateNode ---
    

    
}
