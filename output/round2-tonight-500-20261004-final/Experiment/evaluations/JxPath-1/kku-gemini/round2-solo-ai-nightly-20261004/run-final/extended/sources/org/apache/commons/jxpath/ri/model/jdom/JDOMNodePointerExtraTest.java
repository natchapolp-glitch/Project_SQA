package org.apache.commons.jxpath.ri.model.jdom;

import java.util.Locale;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMNodePointerExtraTest {

    @Test
    public void testCompareChildNodePointersJDOMAttributes() {
        Element parent = new Element("parent");
        Attribute attr1 = new Attribute("att1", "val1");
        Attribute attr2 = new Attribute("att2", "val2");
        parent.setAttribute(attr1);
        parent.setAttribute(attr2);

        JDOMNodePointer parentPtr = new JDOMNodePointer(parent, Locale.getDefault());
        NodePointer p1 = new JDOMNodePointer(parentPtr, attr1);
        NodePointer p2 = new JDOMNodePointer(parentPtr, attr2);

        assertTrue(parentPtr.compareChildNodePointers(p1, p2) < 0);
        assertTrue(parentPtr.compareChildNodePointers(p2, p1) > 0);
        assertEquals(0, parentPtr.compareChildNodePointers(p1, p1));
    }

    @Test(expected = RuntimeException.class)
    public void testCompareChildNodePointersNonElementThrowsException() {
        Element parent = new Element("parent");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        parent.addContent(child1);
        parent.addContent(child2);

        // Point to a child element instead of parent for the pointer itself
        JDOMNodePointer childPtrParent = new JDOMNodePointer(child1, Locale.getDefault());
        NodePointer p1 = new JDOMNodePointer(childPtrParent, child1);
        NodePointer p2 = new JDOMNodePointer(childPtrParent, child2);

        childPtrParent.compareChildNodePointers(p1, p2);
    }
}
