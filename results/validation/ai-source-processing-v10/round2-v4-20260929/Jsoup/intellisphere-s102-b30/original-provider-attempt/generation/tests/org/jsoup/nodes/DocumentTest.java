package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for Document class - normalise, title, body, head, createElement, etc.
 */
public class DocumentTest {

    // --- createShell tests ---

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellNullBaseUri() {
        Document.createShell(null);
    }

    // --- constructor tests ---

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://test.com");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testDocumentConstructorNoArgsShell() {
        Document doc = new Document("");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    // --- nodeName tests ---

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    // --- head tests ---

    @Test
    public void testHeadOnEmptyDoc() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
    }

    @Test
    public void testHeadAfterCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    // --- body tests ---

    @Test
    public void testBodyOnEmptyDoc() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
    }

    @Test
    public void testBodyAfterCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    // --- title() getter tests ---

    @Test
    public void testTitleNoTitleElement() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleWithTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleTrimmed() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("  Trimmed Title  ");
        assertEquals("Trimmed Title", doc.title());
    }

    @Test
    public void testTitleEmptyTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title");
        assertEquals("", doc.title());
    }

    // --- title(String) setter tests ---

    @Test
    public void testSetTitleWhenHeadHasNoTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        Element titleEl = doc.head().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }

    @Test
    public void testSetTitleUpdatesExistingTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Old Title");
        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
        Element titleEl = doc.head().getElementsByTag("title").first();
        assertEquals("Updated Title", titleEl.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleNull() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    // --- createElement tests ---

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element el = doc.createElement("div");
        assertNotNull(el);
        assertEquals("div", el.nodeName());
        assertEquals("http://example.com", el.baseUri());
        assertTrue(el.parent() == null); // not added as child
    }

    @Test
    public void testCreateElementUnknownTag() {
        Document doc = new Document("http://example.com");
        Element el = doc.createElement("custom-tag");
        assertNotNull(el);
        assertEquals("custom-tag", el.nodeName());
    }

    // --- text(String) tests ---

    @Test
    public void testSetTextOnBody() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Hello world");
        assertEquals("Hello world", doc.body().text());
        assertEquals("Hello world", doc.text());
    }

    @Test
    public void testSetTextClearsExistingBodyNodes() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("p").text("Old text");
        doc.text("New body text");
        assertEquals("New body text", doc.body().text());
        assertEquals(1, doc.body().childNodes().size());
        assertTrue(doc.body().childNode(0) instanceof TextNode);
    }

    // --- outerHtml tests ---

    @Test
    public void testOuterHtmlIsHtmlOfDocument() {
        Document doc = Document.createShell("http://example.com");
        String html = doc.outerHtml();
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<head>"));
        assertTrue(html.contains("<body>"));
    }

    @Test
    public void testOuterHtmlEmptyDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("", doc.outerHtml());
    }

    // --- normalise() tests ---

    @Test
    public void testNormaliseEmptyDocumentCreatesStructure() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseMovesTextNodesToBody() {
        Document doc = new Document("http://example.com");
        doc.appendText("Root text");
        Element html = doc.appendElement("html");
        html.appendText("HTML text");
        Element head = html.appendElement("head");
        head.appendText("Head text");
        html.appendElement("body");

        doc.normalise();

        // Check that no text nodes remain in root, html, head
        assertTrue(doc.textNodes().isEmpty());
        assertTrue(doc.select("html").first().textNodes().isEmpty());
        assertTrue(doc.head().textNodes().isEmpty());

        // Body should contain the moved text
        String bodyText = doc.body().text();
        assertTrue(bodyText.contains("Root text"));
        assertTrue(bodyText.contains("HTML text"));
        assertTrue(bodyText.contains("Head text"));
    }

    @Test
    public void testNormaliseBlankTextNodesNotMoved() {
        Document doc = new Document("http://example.com");
        doc.appendText("   ");
        doc.appendElement("html").appendElement("body");
        doc.normalise();
        assertEquals(0, doc.body().childNodes().size());
    }

    @Test
    public void testNormaliseAlreadyHasHtmlElement() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html").appendElement("body");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseAlreadyHasHeadBody() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseReturnsThis() {
        Document doc = new Document("http://example.com");
        assertSame(doc, doc.normalise());
    }
}
