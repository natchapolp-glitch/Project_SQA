package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.Compiler;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DOMNodePointerTest {

    @Test
    public void testDOMNodePointerBasics() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = document.createElement("testElement");
        document.appendChild(element);

        DOMNodePointer pointer = new DOMNodePointer(element, Locale.getDefault());
        assertNotNull(pointer);
        assertEquals(element, pointer.getImmediateNode());
        assertEquals(element, pointer.getBaseValue());
        assertTrue(!pointer.isCollection());
        assertEquals(1, pointer.getLength());
    }

    @Test
    public void testDOMNodePointerGetNameAndPath() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = document.createElement("ns:myElement");
        document.appendChild(element);

        DOMNodePointer pointer = new DOMNodePointer(element, Locale.getDefault());
        QName name = pointer.getName();
        assertNotNull(name);
        assertEquals("myElement", name.getName());
    }

    @Test
    public void testDOMNodePointerTestNode() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = document.createElement("element");
        document.appendChild(element);

        NodeNameTest nameTest = new NodeNameTest(new QName(null, "element"));
        assertTrue(DOMNodePointer.testNode(element, nameTest));

        NodeTypeTest typeTest = new NodeTypeTest(Compiler.NODE_TYPE_NODE);
        assertTrue(DOMNodePointer.testNode(element, typeTest));
    }

    @Test
    public void testDOMNodePointerIterators() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element element = document.createElement("root");
        document.appendChild(element);

        DOMNodePointer pointer = new DOMNodePointer(element, Locale.getDefault());
        assertNotNull(pointer.childIterator(null, false, null));
        assertNotNull(pointer.attributeIterator(new QName("attr")));
        assertNotNull(pointer.namespaceIterator());
    }
}
