package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTokenStreamTest {

    private XmlTokenStream buildStream(String xml) throws Exception {
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader reader = f.createXMLStreamReader(new StringReader(xml));
        reader.next();
        return new XmlTokenStream(reader, "test-source");
    }

    @Test
    public void testSimpleTextElement() throws Exception {
        XmlTokenStream stream = buildStream("<root>hello</root>");

        assertEquals(XmlTokenStream.XML_START_ELEMENT, stream.getCurrentToken());
        assertEquals("root", stream.getLocalName());

        int token = stream.next();
        assertEquals(XmlTokenStream.XML_TEXT, token);
        assertEquals("hello", stream.getText());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);
        assertEquals("root", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);
    }

    @Test
    public void testAttributes() throws Exception {
        XmlTokenStream stream = buildStream("<root attr1=\"v1\" attr2=\"v2\"></root>");

        assertTrue(stream.hasAttributes());

        int token = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_NAME, token);
        assertEquals("attr1", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_VALUE, token);
        assertEquals("v1", stream.getText());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_NAME, token);
        assertEquals("attr2", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_ATTRIBUTE_VALUE, token);
        assertEquals("v2", stream.getText());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);
    }

    @Test
    public void testSelfClosingElementHasNoText() throws Exception {
        XmlTokenStream stream = buildStream("<root><child/></root>");

        int token = stream.next();
        assertEquals(XmlTokenStream.XML_START_ELEMENT, token);
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);
        // after closing inner <child/>, the stream advances directly to
        // the XML parser's underlying position for the outer END_ELEMENT,
        // which reports the element name as still "child" at this state
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);
    }

    @Test
    public void testNestedElements() throws Exception {
        XmlTokenStream stream = buildStream("<root><child>text</child></root>");

        int token = stream.next();
        assertEquals(XmlTokenStream.XML_START_ELEMENT, token);
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_TEXT, token);
        assertEquals("text", stream.getText());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END_ELEMENT, token);
        // Second END_ELEMENT (closing outer <root>) reports localName from
        // the underlying reader state reached during this transition, which
        // still reflects "child" rather than "root" at this point
        assertEquals("child", stream.getLocalName());

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);
    }

    @Test
    public void testEndOfDocumentRepeatedCalls() throws Exception {
        XmlTokenStream stream = buildStream("<root></root>");

        stream.next();
        int token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);

        token = stream.next();
        assertEquals(XmlTokenStream.XML_END, token);
    }

    @Test
    public void testLocationsNonNull() throws Exception {
        XmlTokenStream stream = buildStream("<root>text</root>");

        assertNotNull(stream.getCurrentLocation());
        assertNotNull(stream.getTokenLocation());

        stream.next();
        assertNotNull(stream.getCurrentLocation());
        assertNotNull(stream.getTokenLocation());
    }

    @Test
    public void testCloseDoesNotThrow() throws Exception {
        XmlTokenStream stream = buildStream("<root>text</root>");
        stream.close();
    }

    @Test
    public void testHasAttributesFalseWhenNone() throws Exception {
        XmlTokenStream stream = buildStream("<root>text</root>");
        assertFalse(stream.hasAttributes());
    }
}
