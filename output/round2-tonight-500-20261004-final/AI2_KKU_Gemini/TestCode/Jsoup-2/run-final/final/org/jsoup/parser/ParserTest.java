package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseBasicHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNotNull(doc.body());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String fragment = "<div><p>Fragment text</p></div>";
        Document doc = Parser.parseBodyFragment(fragment, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Fragment text", doc.body().text());
    }

    @Test
    public void testParseAttributes() {
        String html = "<div id='myId' class=\"myClass\" attr=unquoted>Text</div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element div = doc.select("div").first();
        assertNotNull(div);
        assertEquals("myId", div.id());
        assertEquals("myClass", div.className());
        assertEquals("unquoted", div.attr("attr"));
    }

    @Test
    public void testParseCommentsAndCdata() {
        String html = "<div><!-- Comment text --><![CDATA[CDATA text]]></div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.body().text().contains("CDATA text"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtmlThrowsException() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUriThrowsException() {
        Parser.parse("<html></html>", null);
    }
}
