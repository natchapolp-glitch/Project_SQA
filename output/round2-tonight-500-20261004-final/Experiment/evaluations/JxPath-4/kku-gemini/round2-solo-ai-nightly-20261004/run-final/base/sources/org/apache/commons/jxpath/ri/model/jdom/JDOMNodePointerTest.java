package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.Compiler;
import org.jdom.Element;
import org.junit.Test;

public class JDOMNodePointerTest {

    @Test
    public void testJDOMNodePointerBasics() {
        Element element = new Element("testElement");
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());
        assertNotNull(pointer);
        assertEquals(element, pointer.getImmediateNode());
        assertEquals(element, pointer.getBaseValue());
        assertTrue(!pointer.isCollection());
        assertEquals(1, pointer.getLength());
    }

    @Test
    public void testJDOMNodePointerGetName() {
        Element element = new Element("myElement");
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());
        QName name = pointer.getName();
        assertNotNull(name);
        assertEquals("myElement", name.getName());
    }

    @Test
    public void testJDOMNodePointerTestNode() {
        Element element = new Element("element");
        NodeNameTest nameTest = new NodeNameTest(new QName(null, "element"));
        assertTrue(JDOMNodePointer.testNode(null, element, nameTest));

        NodeTypeTest typeTest = new NodeTypeTest(Compiler.NODE_TYPE_NODE);
        assertTrue(JDOMNodePointer.testNode(null, element, typeTest));
    }

    @Test
    public void testJDOMNodePointerIterators() {
        Element element = new Element("root");
        JDOMNodePointer pointer = new JDOMNodePointer(element, Locale.getDefault());
        assertNotNull(pointer.childIterator(null, false, null));
        assertNotNull(pointer.attributeIterator(new QName("attr")));
        assertNotNull(pointer.namespaceIterator());
    }
}
