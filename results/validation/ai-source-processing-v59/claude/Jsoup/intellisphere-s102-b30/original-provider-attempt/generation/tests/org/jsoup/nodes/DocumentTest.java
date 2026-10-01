package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for org.jsoup.nodes.Document
 * Testing fixed reference behavior from revision 1f
 */
public class DocumentTest {

    private static final String BASE_URI = "http://example.com/";

    @Test
    public void testConstructorWithBaseUri() {
        Document doc = new Document(BASE_URI);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testConstructorWithNullBaseUri() {
        Document doc = new Document(null);
        assertNotNull(doc);
        assertNull(doc.baseUri());
    }

    @Test
    public void testConstructorEmptyBaseUri() {
        Document doc = new Document("");
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }

    @Test
    public void testNodeNameIsDocument() {
        Document doc = new Document(BASE_URI);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellWithValidBaseUri() {
        Document doc = Document.createShell(BASE_URI);
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        Element html = doc.select("html").first();
        assertNotNull(html);
    }

    @Test
    public void testCreateShellStructure() {
        Document doc = Document.createShell(BASE_URI);
        Element head = doc.head();
        Element body = doc.body();
        Element html = doc.select("html").first();
        
        assertNotNull(head);
        assertNotNull(body);
        assertNotNull(html);
        assertEquals("head", head.tagName());
        assertEquals("body", body.tagName());
        assertEquals("html", html.tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateShellWithNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHeadReturnsHeadElement() {
        Document doc = Document.createShell(BASE_URI);
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testHeadReturnsNullWhenNoHead() {
        Document doc = new Document(BASE_URI);
        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testBodyReturnsBodyElement() {
        Document doc = Document.createShell(BASE_URI);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testBodyReturnsNullWhenNoBody() {
        Document doc = new Document(BASE_URI);
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testTitleGetEmpty() {
        Document doc = Document.createShell(BASE_URI);
        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleGetSet() {
        Document doc = Document.createShell(BASE_URI);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleSetTrimsWhitespace() {
        Document doc = Document.createShell(BASE_URI);
        doc.title("  Padded Title  ");
        assertEquals("Padded Title", doc.title());
    }

    @Test
    public void testTitleSetCreatesElement() {
        Document doc = Document.createShell(BASE_URI);
        assertNull(doc.getElementsByTag("title").first());
        doc.title("New Title");
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }

    @Test
    public void testTitleUpdateExisting() {
        Document doc = Document.createShell(BASE_URI);
        doc.title("First");
        doc.title("Second");
        assertEquals("Second", doc.title());
        // Verify only one title element exists
        assertEquals(1, doc.getElementsByTag("title").size());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleSetNullThrows() {
        Document doc = Document.createShell(BASE_URI);
        doc.title(null);
    }

    @Test
    public void testCreateElementWithTagName() {
        Document doc = new Document(BASE_URI);
        Element el = doc.createElement("div");
        assertNotNull(el);
        assertEquals("div", el.tagName());
    }

    @Test
    public void testCreateElementInheritsBaseUri() {
        Document doc = new Document(BASE_URI);
        Element el = doc.createElement("a");
        assertEquals(BASE_URI, el.baseUri());
    }

    

    @Test
    public void testCreateElementWithSpecialChars() {
        Document doc = new Document(BASE_URI);
        Element el = doc.createElement("custom-element");
        assertNotNull(el);
        assertEquals("custom-element", el.tagName());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document(BASE_URI);
        doc.normalise();
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseReturnsThis() {
        Document doc = new Document(BASE_URI);
        Document result = doc.normalise();
        assertSame(doc, result);
    }

    @Test
    public void testNormaliseWithExistingStructure() {
        Document doc = Document.createShell(BASE_URI);
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseElement() {
        Document doc = Document.createShell(BASE_URI);
        Element div = doc.body().appendElement("div");
        div.appendText("Some text");
        Element element = doc.select("html").first();
        doc.normalise(element);
        // Text should have been moved
        assertNotNull(doc.body());
    }

    @Test
    public void testOuterHtmlNoWrapper() {
        Document doc = Document.createShell(BASE_URI);
        String html = doc.outerHtml();
        assertFalse(html.contains("#root"));
        assertTrue(html.contains("<html>"));
    }

    @Test
    public void testTextSetterClearsPreviousContent() {
        Document doc = Document.createShell(BASE_URI);
        doc.body().appendElement("div").text("old");
        doc.text("new content");
        assertEquals("new content", doc.body().text());
    }

    @Test
    public void testTextSetterReturnsThis() {
        Document doc = Document.createShell(BASE_URI);
        Element result = doc.text("content");
        assertSame(doc, result);
    }

    @Test
    public void testTextSetterWithEmptyString() {
        Document doc = Document.createShell(BASE_URI);
        doc.text("");
        assertEquals("", doc.body().text());
    }

    @Test
    public void testTextSetterWithSpecialCharacters() {
        Document doc = Document.createShell(BASE_URI);
        String text = "Hello & <world>";
        doc.text(text);
        assertEquals(text, doc.body().text());
    }

    @Test
    public void testNormaliseWithTextNodesInRoot() {
        Document doc = new Document(BASE_URI);
        doc.normalise();
        // After normalise, text at root should be moved to body
        assertNotNull(doc.body());
    }
}
