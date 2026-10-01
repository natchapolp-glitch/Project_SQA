package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentRegressionTest {

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com/");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com/dir/";
        Document doc = new Document(baseUri);

        assertEquals(baseUri, doc.baseUri());
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);

        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("html", doc.head().parent().tagName());
        assertEquals("html", doc.body().parent().tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testGetTitleWhenNotSet() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("", doc.title());
    }

    @Test
    public void testSetTitleWhenTitleElementNotPresent() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("My Title");

        assertEquals("My Title", doc.title());
        Element titleEl = doc.head().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("My Title", titleEl.text());
    }

    @Test
    public void testSetTitleUpdatesExistingTitleElement() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Initial Title");
        assertEquals("Initial Title", doc.title());

        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
        assertEquals(1, doc.head().getElementsByTag("title").size());
    }

    @Test
    public void testGetTitleTrimsWhitespace() {
        Document doc = Document.createShell("http://example.com/");
        doc.head().appendElement("title").text("   Spaced Title   ");

        assertEquals("Spaced Title", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleNullThrowsException() {
        Document doc = Document.createShell("http://example.com/");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com/test/");
        Element div = doc.createElement("div");

        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com/test/", div.baseUri());
        assertNull(div.parent());
    }

    @Test
    public void testTextSetsBodyText() {
        Document doc = Document.createShell("http://example.com/");
        doc.body().appendElement("p").text("Old content");

        Element ret = doc.text("New body text");

        assertSame(doc, ret);
        assertEquals("New body text", doc.body().text());
        assertEquals(0, doc.body().getElementsByTag("p").size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("http://example.com/");
        assertNull(doc.head());
        assertNull(doc.body());

        Document normalised = doc.normalise();

        assertSame(doc, normalised);
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseMovesRootAndHtmlTextNodesToBody() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        // Add text nodes outside body
        doc.prependChild(new TextNode("Root Text", "http://example.com/"));
        html.prependChild(new TextNode("Html Text", "http://example.com/"));
        head.prependChild(new TextNode("Head Text", "http://example.com/"));

        doc.normalise();

        assertTrue(doc.body().text().contains("Root Text"));
        assertTrue(doc.body().text().contains("Html Text"));
        assertTrue(doc.body().text().contains("Head Text"));
    }

    @Test
    public void testNormaliseIgnoresBlankTextNodes() {
        Document doc = Document.createShell("http://example.com/");
        doc.prependChild(new TextNode("   \n\t  ", "http://example.com/"));

        int originalBodyChildren = doc.body().childNodes.size();
        doc.normalise();

        assertEquals(originalBodyChildren, doc.body().childNodes.size());
    }

    @Test
    public void testOuterHtmlMatchesSuperHtml() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Hello");
        doc.body().appendElement("p").text("Sample text");

        assertEquals(doc.html(), doc.outerHtml());
        assertFalse(doc.outerHtml().startsWith("<#root>"));
    }

    @Test
    public void testHeadAndBodySelectors() {
        Document doc = Document.createShell("http://example.com/");

        Element head = doc.head();
        Element body = doc.body();

        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
        assertSame(head, doc.getElementsByTag("head").first());
        assertSame(body, doc.getElementsByTag("body").first());
    }
}
