package com.fasterxml.jackson.dataformat.xml.deser;

import static org.junit.Assert.*;

import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

public class XmlTokenStreamTest {

    private XmlTokenStream createTokenStream(String xml) throws Exception {
        XMLInputFactory f = XMLInputFactory.newFactory();
        XMLStreamReader sr = f.createXMLStreamReader(new StringReader(xml));
        while (sr.hasNext() && sr.getEventType() != XMLStreamReader.START_ELEMENT) {
            sr.next();
        }
        return new XmlTokenStream(sr, "sourceRef");
    }

    @Test
    public void testInitialStateAndGetters() throws Exception {
        String xml = "<root attr='val'>hello</root>";
        XmlTokenStream stream = createTokenStream(xml);

        assertEquals(XmlTokenStream.XML_START_ELEMENT, stream.getCurrentToken());
        assertEquals("root", stream.getLocalName());
        assertNotNull(stream.getXmlReader());
        assertTrue(stream.hasAttributes());
        assertNotNull(stream.getCurrentLocation());
        assertNotNull(stream.getTokenLocation());
    }

    @Test
    public void testNextIterationSimple() throws Exception {
        String xml = "<root>text</root>";
        XmlTokenStream stream = createTokenStream(xml);

        assertEquals(XmlTokenStream.XML_START_ELEMENT, stream.getCurrentToken());
        
        int token1 = stream.next();
        assertEquals(XmlTokenStream.XML_TEXT, token1);
        assertEquals("text", stream.getText());

        int token2 = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token2);

        int token3 = stream.next();
        assertEquals(XmlTokenStream.XML_END, token3);
    }

    @Test
    public void testAttributesIteration() throws Exception {
        String xml = "<root id='123'></root>";
        XmlTokenStream stream = createTokenStream(xml);

        assertTrue(stream.hasAttributes());
        
        int token1 = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_NAME, token1);
        assertEquals("id", stream.getLocalName());

        int token2 = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_VALUE, token2);
        assertEquals("123", stream.getText());
    }

    @Test
    public void testSkipEndElement() throws Exception {
        String xml = "<root></root>";
        XmlTokenStream stream = createTokenStream(xml);

        stream.skipEndElement();
        assertEquals(XmlTokenStream.XML_END, stream.getCurrentToken());
    }

    @Test
    public void testCloseOperations() throws Exception {
        String xml = "<root/>";
        XmlTokenStream stream = createTokenStream(xml);

        stream.close();
        stream.closeCompletely();
        assertTrue(stream.toString() != null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSkipAttributesInvalidState() throws Exception {
        String xml = "<root><child/></root>";
        XmlTokenStream stream = createTokenStream(xml);
        stream.next(); // moves to child or text
        stream.skipAttributes();
    }
}
