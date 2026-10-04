package org.apache.commons.jxpath.ri.model.dom;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class DOMNodePointerExtraTest {

    @Test
    public void testTestNodeWithNameTestAndWildcard() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("element");

        NodeNameTest wildcardTest = new NodeNameTest(new QName(null, "*"));
        assertTrue(DOMNodePointer.testNode(elem, wildcardTest));

        NodeNameTest specificTest = new NodeNameTest(new QName(null, "element"));
        assertTrue(DOMNodePointer.testNode(elem, specificTest));

        NodeNameTest mismatchTest = new NodeNameTest(new QName(null, "other"));
        assertFalse(DOMNodePointer.testNode(elem, mismatchTest));
    }

    @Test
    public void testCompareChildNodePointersAttributesAndErrors() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element elem = doc.createElement("parent");
        elem.setAttribute("att1", "val1");
        elem.setAttribute("att2", "val2");
        doc.appendChild(elem);

        DOMNodePointer parentPtr = new DOMNodePointer(elem, Locale.getDefault());
        NodePointer attrPtr1 = parentPtr.createAttribute(null, new QName("att1"));
        NodePointer attrPtr2 = parentPtr.createAttribute(null, new QName("att2"));

        assertTrue(parentPtr.compareChildNodePointers(attrPtr1, attrPtr2) < 0);
        assertTrue(parentPtr.compareChildNodePointers(attrPtr2, attrPtr1) > 0);
        assertEquals(0, parentPtr.compareChildNodePointers(attrPtr1, attrPtr1));

        // Mixed child and attribute or invalid base
        Element child = doc.createElement("child");
        elem.appendChild(child);
        NodePointer childPtr = new DOMNodePointer(parentPtr, child);

        assertTrue(parentPtr.compareChildNodePointers(attrPtr1, childPtr) < 0);
        assertTrue(parentPtr.compareChildNodePointers(childPtr, attrPtr1) > 0);
    }
}
