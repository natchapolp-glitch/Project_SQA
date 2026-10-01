package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DocumentGeneratedTest {

    // ---------- constructor / nodeName ----------

    @Test
    public void constructorSetsBaseUriAndIsEmpty() {
        Document doc = new Document("http://example.com/");
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals(0, doc.children().size());
    }

    @Test
    public void constructorAcceptsEmptyBaseUri() {
        Document doc = new Document("");
        assertEquals("", doc.baseUri());
    }

    @Test
    public void nodeNameIsDocument() {
        assertEquals("#document", new Document("").nodeName());
    }

    @Test
    public void nodeNameUnchangedOnShell() {
        assertEquals("#document", Document.createShell("http://a/").nodeName());
    }

    @Test
    public void tagNameOfDocumentIsRoot() {
        assertEquals("#root", new Document("").tagName());
    }

    // ---------- createShell ----------

    @Test
    public void createShellBuildsHtmlHeadBody() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals(1, doc.children().size());
        Element html = doc.children().first();
        assertEquals("html", html.tagName());
        assertEquals(2, html.children().size());
        assertEquals("head", html.children().first().tagName());
        assertEquals("body", html.children().get(1).tagName());
    }

    @Test
    public void createShellAllowsEmptyBaseUri() {
        Document doc = Document.createShell("");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShellNullBaseUriThrows() {
        Document.createShell(null);
    }

    // ---------- head / body ----------

    @Test
    public void headAndBodyNullOnEmptyDocument() {
        Document doc = new Document("");
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void headAndBodyReturnShellElements() {
        Document doc = Document.createShell("");
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void bodyReturnsFirstOfMultipleBodies() {
        Document doc = new Document("");
        Element html = doc.appendElement("html");
        Element first = html.appendElement("body");
        html.appendElement("body");
        assertSame(first, doc.body());
    }

    // ---------- title ----------

    @Test
    public void titleEmptyWhenNoTitleElement() {
        assertEquals("", new Document("").title());
        assertEquals("", Document.createShell("").title());
    }

    @Test
    public void titleReadsAndTrimsExistingTitle() {
        Document doc = Document.createShell("");
        doc.head().appendElement("title").text("  Hello World  ");
        assertEquals("Hello World", doc.title());
    }

    @Test
    public void setTitleAddsTitleToHead() {
        Document doc = Document.createShell("");
        doc.title("My Title");
        assertEquals("My Title", doc.title());
        assertEquals(1, doc.head().getElementsByTag("title").size());
    }

    @Test
    public void setTitleUpdatesExistingTitleWithoutDuplicating() {
        Document doc = Document.createShell("");
        doc.title("First");
        doc.title("Second");
        assertEquals("Second", doc.title());
        assertEquals(1, doc.getElementsByTag("title").size());
    }

    @Test
    public void setTitleEmptyString() {
        Document doc = Document.createShell("");
        doc.title("");
        assertEquals("", doc.title());
        assertEquals(1, doc.getElementsByTag("title").size());
    }

    @Test
    public void setTitleTrimmedOnRead() {
        Document doc = Document.createShell("");
        doc.title("  Padded  ");
        assertEquals("Padded", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTitleNullThrows() {
        Document.createShell("").title(null);
    }

    // ---------- createElement ----------

    @Test
    public void createElementUsesTagNameAndBaseUri() {
        Document doc = Document.createShell("http://example.com/base/");
        Element el = doc.createElement("div");
        assertEquals("div", el.tagName());
        assertEquals("http://example.com/base/", el.baseUri());
    }

    @Test
    public void createElementIsNotAttachedToDocument() {
        Document doc = Document.createShell("");
        int before = doc.getAllElements().size();
        Element el = doc.createElement("span");
        assertNull(el.parent());
        assertEquals(before, doc.getAllElements().size());
        assertEquals(0, doc.getElementsByTag("span").size());
    }

    @Test
    public void createElementReturnsDistinctInstances() {
        Document doc = new Document("");
        assertFalse(doc.createElement("p") == doc.createElement("p"));
    }

    // ---------- text(String) ----------

    @Test
    public void textSetsBodyTextAndReturnsDocument() {
        Document doc = Document.createShell("");
        Element result = doc.text("Hello");
        assertSame(doc, result);
        assertEquals("Hello", doc.body().text());
    }

    @Test
    public void textReplacesExistingBodyChildren() {
        Document doc = Document.createShell("");
        doc.body().appendElement("p").text("old");
        doc.text("new");
        assertEquals(0, doc.body().children().size());
        assertEquals("new", doc.body().text());
    }

    @Test
    public void textDoesNotDestroyDocumentStructure() {
        Document doc = Document.createShell("");
        doc.text("content");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.children().size());
    }

    // ---------- outerHtml ----------

    @Test
    public void outerHtmlEqualsInnerHtmlWithoutWrapper() {
        Document doc = Document.createShell("");
        doc.title("T");
        assertEquals(doc.html(), doc.outerHtml());
        assertFalse(doc.outerHtml().contains("#root"));
        assertFalse(doc.outerHtml().contains("#document"));
    }

    @Test
    public void outerHtmlContainsShellTags() {
        String html = Document.createShell("").outerHtml();
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<head>"));
        assertTrue(html.contains("<body>"));
    }

    @Test
    public void outerHtmlOfEmptyDocumentIsEmpty() {
        assertEquals("", new Document("").outerHtml().trim());
    }

    // ---------- normalise ----------

    @Test
    public void normaliseCreatesMissingStructureOnEmptyDocument() {
        Document doc = new Document("");
        Document result = doc.normalise();
        assertSame(doc, result);
        assertEquals(1, doc.select("html").size());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void normaliseAddsHeadBeforeBodyWhenHeadMissing() {
        Document doc = new Document("");
        Element html = doc.appendElement("html");
        html.appendElement("body");
        doc.normalise();
        assertNotNull(doc.head());
        assertEquals("head", html.children().first().tagName());
        assertEquals("body", html.children().get(1).tagName());
    }

    @Test
    public void normaliseAddsBodyWhenBodyMissing() {
        Document doc = new Document("");
        Element html = doc.appendElement("html");
        html.appendElement("head");
        doc.normalise();
        assertNotNull(doc.body());
        assertEquals(1, doc.getElementsByTag("body").size());
    }

    

    

    

    

    

    

    
}
