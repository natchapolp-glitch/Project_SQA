package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructorAndBaseUri() {
        Document doc = new Document("http://example.com/");
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com/shell");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.select("html").first().tagName());
        assertEquals("http://example.com/shell", doc.baseUri());
    }

    @Test
    public void testTitleGetAndSet() {
        Document doc = new Document("http://example.com/");
        assertEquals("", doc.title());
        
        doc.title("My Test Title");
        assertEquals("My Test Title", doc.title());
        assertNotNull(doc.head().getElementsByTag("title").first());
        
        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com/");
        Element span = doc.createElement("span");
        assertEquals("span", span.tagName());
        assertEquals("http://example.com/", span.baseUri());
        assertFalse(doc.getAllElements().contains(span));
    }

    @Test
    public void testBodyAndHeadAccessors() {
        Document doc = Document.createShell("http://example.com/");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com/");
        doc.appendChild(new TextNode("orphan text", "http://example.com/"));
        doc.normalise();
        
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("orphan text"));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com/");
        doc.body().text("Hello world");
        String html = doc.outerHtml();
        assertTrue(html.contains("Hello world"));
    }

    @Test
    public void testDocumentText() {
        Document doc = Document.createShell("http://example.com/");
        doc.text("New body content");
        assertEquals("New body content", doc.body().text());
        assertEquals("New body content", doc.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com/");
        assertEquals("#document", doc.nodeName());
    }
}
