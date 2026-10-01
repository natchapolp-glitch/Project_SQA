package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for {@link Document}.
 */
public class DocumentRegressionTest {

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com/");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com/";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertEquals("", doc.title());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("html", doc.head().parent().tagName());
        assertEquals("html", doc.body().parent().tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellNullUriThrowsException() {
        Document.createShell(null);
    }

    @Test
    public void testTitleGetWhenNotPresent() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleSetWhenNotPresent() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Initial Title");
        assertEquals("Initial Title", doc.title());
        assertNotNull(doc.head().getElementsByTag("title").first());
        assertEquals("Initial Title", doc.head().getElementsByTag("title").first().text());
    }

    @Test
    public void testTitleSetUpdatesExistingTitle() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Initial Title");
        assertEquals("Initial Title", doc.title());

        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
        assertEquals(1, doc.head().getElementsByTag("title").size());
    }

    @Test
    public void testTitleGetTrimmed() {
        Document doc = Document.createShell("http://example.com/");
        doc.head().appendElement("title").text("   Trimmed Title   ");
        assertEquals("Trimmed Title", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNullThrowsException() {
        Document doc = Document.createShell("http://example.com/");
        doc.title(null);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com/");
        Element div = doc.createElement("div");

        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com/", div.baseUri());
        assertNull(div.parent());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void testTextSetsBodyTextPreservingStructure() {
        Document doc = Document.createShell("http://example.com/");
        Element returned = doc.text("Sample body content");

        assertSame(doc, returned);
        assertEquals("Sample body content", doc.body().text());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtmlDoesNotIncludeDocumentNodeTag() {
        Document doc = new Document("http://example.com/");
        assertEquals("", doc.outerHtml());

        Document shell = Document.createShell("http://example.com/");
        String html = shell.outerHtml();
        assertFalse(html.contains("#document"));
        assertTrue(html.contains("html"));
        assertTrue(html.contains("head"));
        assertTrue(html.contains("body"));
    }

    @Test
    public void testNormaliseCreatesHtmlHeadBodyIfEmpty() {
        Document doc = new Document("http://example.com/");
        Document normalised = doc.normalise();

        assertSame(doc, normalised);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().parent().tagName());
        assertEquals("html", doc.body().parent().tagName());
    }

    @Test
    public void testNormalisePrependsHeadIfMissing() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        html.appendElement("body");

        assertNull(doc.head());
        doc.normalise();

        assertNotNull(doc.head());
        assertEquals(0, doc.head().elementSiblingIndex());
    }

    @Test
    public void testNormaliseAppendsBodyIfMissing() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        html.appendElement("head");

        assertNull(doc.body());
        doc.normalise();

        assertNotNull(doc.body());
        assertEquals(1, doc.body().elementSiblingIndex());
    }

    @Test
    public void testNormaliseMovesTextFromRootToBody() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        html.appendElement("head");
        Element body = html.appendElement("body");

        doc.appendChild(new TextNode("Root text", "http://example.com/"));
        doc.normalise();

        assertTrue(body.text().contains("Root text"));
    }

    @Test
    public void testNormaliseMovesTextFromHeadToBody() {
        Document doc = Document.createShell("http://example.com/");
        doc.head().appendChild(new TextNode("Head text", "http://example.com/"));

        doc.normalise();
        assertTrue(doc.body().text().contains("Head text"));
    }

    @Test
    public void testNormaliseMovesTextFromHtmlToBody() {
        Document doc = Document.createShell("http://example.com/");
        Element html = doc.select("html").first();
        html.appendChild(new TextNode("Html text", "http://example.com/"));

        doc.normalise();
        assertTrue(doc.body().text().contains("Html text"));
    }

    @Test
    public void testNormaliseIgnoresBlankTextNodes() {
        Document doc = Document.createShell("http://example.com/");
        doc.head().appendChild(new TextNode("   \n\t  ", "http://example.com/"));

        doc.normalise();
        assertEquals("", doc.body().text());
    }
}
