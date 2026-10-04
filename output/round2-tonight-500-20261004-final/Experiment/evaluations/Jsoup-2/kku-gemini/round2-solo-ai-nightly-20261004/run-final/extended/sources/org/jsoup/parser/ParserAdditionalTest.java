package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;

public class ParserAdditionalTest {

    @Test
    public void testParseXmlDeclarationAndCdata() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><![CDATA[Some CData content]]></root>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.select("root").first());
    }

    @Test
    public void testParseEmptyStartTagAndEndTag() {
        String html = "<div><></></div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testBaseTagUpdatesBaseUri() {
        String html = "<html><head><base href=\"http://jsoup.org\"></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://jsoup.org", doc.baseUri());
    }

    @Test
    public void testParseEmptyAttributeKey() {
        String html = "<div =value>Text</div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        assertNotNull(doc);
    }
}
