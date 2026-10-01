// org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointerTest.java
package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

import junit.framework.TestCase;

public class JDOMNodePointerTest extends TestCase {

    private Document doc;
    private Element root;
    private Element child;
    private Text textNode;
    private CDATA cdata;
    private ProcessingInstruction pi;
    private Element langElem;
    private JDOMNodePointer rootPtr;

    public void setUp() throws Exception {
        Namespace ns = Namespace.getNamespace("ex", "http://example.com");
        root = new Element("root", ns);
        // set default namespace via xmlns attribute
        root.addNamespaceDeclaration(Namespace.getNamespace("", "http://default.example.com"));
        // namespace prefix
        root.addNamespaceDeclaration(Namespace.getNamespace("ns", "http://ns.example.com"));

        child = new Element("child", ns);
        root.addContent(child);
        // text node
        textNode = new Text("text content");
        child.addContent(textNode);
        // CDATA
        cdata = new CDATA("cdata content");
        root.addContent(cdata);
        // processing instruction
        pi = new ProcessingInstruction("target", "data");
        root.addContent(pi);
        // lang element
        langElem = new Element("langElem");
        langElem.setAttribute("lang", "en-US", Namespace.XML_NAMESPACE);
        root.addContent(langElem);

        doc = new Document(root);

        rootPtr = new JDOMNodePointer(root, Locale.US);
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
    public void testGetNamespaceURI_withPrefix() {
        assertEquals("http://ns.example.com", rootPtr.getNamespaceURI("ns"));
    }

    // 5.
    public void testGetName_element() {
        QName name = rootPtr.getName();
        assertEquals("ex", name.getPrefix());
        assertEquals("root", name.getName());
    }

    // 6.
    public void testGetName_pi() {
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        QName piName = piPtr.getName();
        assertEquals("target", piName.getName());
        assertNull(piName.getPrefix());
    }

    // 7.
    public void testAsPath_element() {
        String path = rootPtr.asPath();
        assertTrue(path.contains("ex:root["));
    }

    // 8.
    public void testAsPath_textNode() {
        JDOMNodePointer textPtr = new JDOMNodePointer(textNode, Locale.US);
        String path = textPtr.asPath();
        assertTrue(path.startsWith("/text()["));
    }

    // 9.
    public void testAsPath_pi() {
        JDOMNodePointer piPtr = new JDOMNodePointer(pi, Locale.US);
        String path = piPtr.asPath();
        assertTrue(path.contains("processing-instruction('target')"));
    }

    // 10.
    public void testSetValue_text_replace() {
        JDOMNodePointer textPtr = new JDOMNodePointer(textNode, Locale.US);
        textPtr.setValue("new text");
        assertEquals("new text", textPtr.getValue());
    }

    // 11.
    public void testSetValue_empty_remove() {
        JDOMNodePointer textPtr = new JDOMNodePointer(textNode, Locale.US);
        textPtr.setValue("");
        assertNull(textNode.getParent());
    }

    // 12.
}
