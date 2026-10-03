package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Deterministic JUnit 4 suite for org.jsoup.nodes.Document,
 * exercising the fixed API behavior of createShell, body, head,
 * createElement, nodeName, normalise, outerHtml, text, and title.
 */
public class DocumentTest {

    @Test
    public void createShellProducesHtmlHeadBody() {
        Document doc = Document.createShell("http://example.com/");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.child(0).tagName());
    }

    @Test
    public void createShellBaseUriIsSetOnDocument() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("http://example.com/", doc.baseUri());
    }

    @Test
    public void bodyReturnsNullWhenNoBodyPresent() {
        Document doc = new Document("http://example.com/");
        assertNull(doc.body());
    }

    @Test
    public void bodyReturnsElementAfterShellCreation() {
        Document doc = Document.createShell("http://example.com/");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void headReturnsNullWhenNoHeadPresent() {
        Document doc = new Document("http://example.com/");
        assertNull(doc.head());
    }

    @Test
    public void headReturnsElementAfterShellCreation() {
        Document doc = Document.createShell("http://example.com/");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void createElementUsesDocumentBaseUri() {
        Document doc = new Document("http://example.com/");
        Element element = doc.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals("http://example.com/", element.baseUri());
    }

    @Test
    public void createElementDoesNotAttachToDocument() {
        Document doc = Document.createShell("http://example.com/");
        Element element = doc.createElement("span");
        assertEquals(0, doc.getElementsByTag("span").size());
    }

    @Test
    public void nodeNameReturnsHashDocument() {
        Document doc = new Document("http://example.com/");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void normaliseCreatesHtmlHeadBodyWhenMissing() {
        Document doc = new Document("http://example.com/");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.children().size());
        assertEquals("html", doc.child(0).tagName());
    }

    @Test
    public void normaliseMovesTopLevelTextIntoBody() {
        Document doc = Document.createShell("http://example.com/");
        doc.appendChild(new TextNode("Hello", doc.baseUri()));
        doc.normalise();
        assertTrue(doc.body().text().contains("Hello"));
    }

    @Test
    public void normaliseReturnsSameDocumentInstance() {
        Document doc = new Document("http://example.com/");
        Document normalised = doc.normalise();
        assertSame(doc, normalised);
    }

    @Test
    public void normaliseIsIdempotentOnShell() {
        Document doc = Document.createShell("http://example.com/");
        doc.normalise();
        assertEquals(1, doc.children().size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void outerHtmlContainsHtmlHeadBodyTags() {
        Document doc = Document.createShell("http://example.com/");
        String html = doc.outerHtml();
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<head></head>"));
        assertTrue(html.contains("<body></body>"));
    }

    @Test
    public void outerHtmlOnEmptyDocumentIsEmptyString() {
        Document doc = new Document("http://example.com/");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void textSetsBodyTextContent() {
        Document doc = Document.createShell("http://example.com/");
        Element returned = doc.text("Hello World");
        assertEquals("Hello World", doc.body().text());
        assertSame(doc, returned);
    }

    @Test
    public void textClearsExistingBodyContentBeforeSetting() {
        Document doc = Document.createShell("http://example.com/");
        doc.body().appendElement("p").text("Old content");
        doc.text("New content");
        assertEquals("New content", doc.body().text());
        assertEquals(0, doc.body().children().size());
    }

    @Test
    public void textPreservesDocumentStructure() {
        Document doc = Document.createShell("http://example.com/");
        doc.text("Some text");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void titleReturnsEmptyStringWhenNoTitleElement() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("", doc.title());
    }

    @Test
    public void titleSetterAddsTitleElementToHead() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("My Title");
        assertEquals("My Title", doc.title());
        assertEquals(1, doc.head().getElementsByTag("title").size());
    }

    @Test
    public void titleGetterTrimsWhitespace() {
        Document doc = Document.createShell("http://example.com/");
        doc.head().appendElement("title").text("  Padded Title  ");
        assertEquals("Padded Title", doc.title());
    }

    @Test
    public void titleSetterUpdatesExistingTitleElement() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("First Title");
        doc.title("Second Title");
        assertEquals("Second Title", doc.title());
        assertEquals(1, doc.head().getElementsByTag("title").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void titleSetterThrowsOnNullTitle() {
        Document doc = Document.createShell("http://example.com/");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShellThrowsOnNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void headAndBodyAreDistinctElements() {
        Document doc = Document.createShell("http://example.com/");
        assertNotSame(doc.head(), doc.body());
    }

    @Test
    public void createElementWithDifferentTagNamesProducesDistinctTags() {
        Document doc = new Document("http://example.com/");
        Element div = doc.createElement("div");
        Element span = doc.createElement("span");
        assertEquals("div", div.tagName());
        assertEquals("span", span.tagName());
    }

    @Test
    public void nodeNameConsistentAcrossInstances() {
        Document doc1 = new Document("http://example.com/");
        Document doc2 = Document.createShell("http://other.com/");
        assertEquals(doc1.nodeName(), doc2.nodeName());
    }
}
