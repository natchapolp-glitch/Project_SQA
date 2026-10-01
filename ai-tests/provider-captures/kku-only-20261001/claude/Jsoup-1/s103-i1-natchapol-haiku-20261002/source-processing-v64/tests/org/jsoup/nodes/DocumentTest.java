package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for Document class focusing on documented behavior
 * and fixed reference implementation.
 */
public class DocumentTest {
    
    private static final String TEST_BASE_URI = "http://example.com/";
    
    @Test
    public void testDocumentConstructorWithBaseUri() {
        // Document constructor should accept a base URI string
        Document doc = new Document(TEST_BASE_URI);
        assertNotNull("Document should be created", doc);
        assertEquals("Base URI should be preserved", TEST_BASE_URI, doc.baseUri());
    }
    
    @Test
    public void testDocumentConstructorWithEmptyBaseUri() {
        // Document should handle empty string base URI
        Document doc = new Document("");
        assertNotNull("Document should be created with empty URI", doc);
        assertEquals("Base URI should be empty string", "", doc.baseUri());
    }
    
    @Test
    public void testDocumentNodeName() {
        // nodeName() should return the literal string "#document"
        Document doc = new Document(TEST_BASE_URI);
        assertEquals("Node name should be #document", "#document", doc.nodeName());
    }
    
    @Test
    public void testDocumentNodeNameConsistent() {
        // nodeName() should consistently return the same value
        Document doc = new Document("http://test.com");
        String name1 = doc.nodeName();
        String name2 = doc.nodeName();
        assertEquals("Node name should be consistent", name1, name2);
        assertEquals("Node name should always be #document", "#document", name1);
    }
    
    @Test
    public void testCreateShellStructure() {
        // createShell should create a document with html, head, and body elements
        Document doc = Document.createShell(TEST_BASE_URI);
        assertNotNull("Document should be created", doc);
        assertNotNull("Head element should exist", doc.head());
        assertNotNull("Body element should exist", doc.body());
    }
    
    @Test
    public void testCreateShellWithDifferentUris() {
        // createShell should preserve the base URI
        String uri1 = "http://example1.com/";
        String uri2 = "http://example2.com/";
        
        Document doc1 = Document.createShell(uri1);
        Document doc2 = Document.createShell(uri2);
        
        assertEquals("First document URI should be preserved", uri1, doc1.baseUri());
        assertEquals("Second document URI should be preserved", uri2, doc2.baseUri());
    }
    
    
    
    @Test
    public void testHeadAccessor() {
        // head() should return the head element from shell document
        Document doc = Document.createShell(TEST_BASE_URI);
        Element head = doc.head();
        assertNotNull("Head should not be null", head);
        assertEquals("Head tag should be 'head'", "head", head.tagName());
    }
    
    @Test
    public void testHeadAccessorEmpty() {
        // head() should return null when no head element exists
        Document doc = new Document(TEST_BASE_URI);
        Element head = doc.head();
        assertNull("Head should be null in empty document", head);
    }
    
    @Test
    public void testBodyAccessor() {
        // body() should return the body element from shell document
        Document doc = Document.createShell(TEST_BASE_URI);
        Element body = doc.body();
        assertNotNull("Body should not be null", body);
        assertEquals("Body tag should be 'body'", "body", body.tagName());
    }
    
    @Test
    public void testBodyAccessorEmpty() {
        // body() should return null when no body element exists
        Document doc = new Document(TEST_BASE_URI);
        Element body = doc.body();
        assertNull("Body should be null in empty document", body);
    }
    
    @Test
    public void testTitleGetterEmpty() {
        // title() with no title element should return empty string
        Document doc = Document.createShell(TEST_BASE_URI);
        String title = doc.title();
        assertEquals("Title should be empty string when not set", "", title);
    }
    
    @Test
    public void testTitleGetterWithWhitespace() {
        // title() should trim whitespace from title element text
        Document doc = Document.createShell(TEST_BASE_URI);
        Element titleEl = doc.head().appendElement("title");
        titleEl.text("  Test Title  ");
        String title = doc.title();
        assertEquals("Title should be trimmed", "Test Title", title);
    }
    
    @Test
    public void testTitleSetter() {
        // title(String) should set title in head element
        Document doc = Document.createShell(TEST_BASE_URI);
        String newTitle = "New Document Title";
        doc.title(newTitle);
        assertEquals("Title should be set", newTitle, doc.title());
    }
    
    
    
    @Test
    public void testTitleSetterUpdatesExisting() {
        // title(String) should update existing title element
        Document doc = Document.createShell(TEST_BASE_URI);
        doc.title("First Title");
        doc.title("Second Title");
        assertEquals("Title should be updated", "Second Title", doc.title());
        // Verify only one title element exists
        assertEquals("Should have exactly one title", 1, doc.select("title").size());
    }
    
    @Test
    public void testCreateElementBasic() {
        // createElement should create a new Element with document's base URI
        Document doc = new Document(TEST_BASE_URI);
        Element elem = doc.createElement("div");
        assertNotNull("Element should be created", elem);
        assertEquals("Element tag should match", "div", elem.tagName());
        assertEquals("Element should inherit document base URI", TEST_BASE_URI, elem.baseUri());
    }
    
    @Test
    public void testCreateElementDifferentTags() {
        // createElement should work with various tag names
        Document doc = new Document(TEST_BASE_URI);
        
        Element divElem = doc.createElement("div");
        assertEquals("Div tag should be created", "div", divElem.tagName());
        
        Element spanElem = doc.createElement("span");
        assertEquals("Span tag should be created", "span", spanElem.tagName());
        
        Element linkElem = doc.createElement("a");
        assertEquals("Link tag should be created", "a", linkElem.tagName());
    }
    
    @Test
    public void testCreateElementNotAttached() {
        // createElement should not attach the element to the document
        Document doc = new Document(TEST_BASE_URI);
        Element elem = doc.createElement("p");
        assertFalse("Created element should not be attached", elem.hasParent());
    }
    
    @Test
    public void testOuterHtmlEmpty() {
        // outerHtml() should return HTML content without outer document wrapper
        Document doc = new Document(TEST_BASE_URI);
        String html = doc.outerHtml();
        assertNotNull("Outer HTML should not be null", html);
        // Document outerHtml should not have wrapper tag
        assertFalse("Should not contain document wrapper", html.contains("#root"));
    }
    
    @Test
    public void testOuterHtmlWithContent() {
        // outerHtml() should return the HTML representation of document content
        Document doc = Document.createShell(TEST_BASE_URI);
        doc.head().appendElement("title").text("Test");
        String html = doc.outerHtml();
        assertNotNull("Outer HTML should not be null", html);
        assertTrue("Should contain html tag", html.contains("html"));
        assertTrue("Should contain head tag", html.contains("head"));
        assertTrue("Should contain body tag", html.contains("body"));
    }
    
    @Test
    public void testTextSetterWithBody() {
        // text(String) should set text content in body element
        Document doc = Document.createShell(TEST_BASE_URI);
        String testText = "Test document text";
        Document result = (Document) doc.text(testText);
        assertEquals("Text should be set in body", testText, doc.body().text());
        assertSame("text() should return the document itself", doc, result);
    }
    
    @Test
    public void testTextSetterPreservesStructure() {
        // text(String) should not nuke the document structure
        Document doc = Document.createShell(TEST_BASE_URI);
        doc.head().appendElement("title").text("Original Title");
        doc.text("Body text");
        assertNotNull("Head should still exist", doc.head());
        assertNotNull("Body should still exist", doc.body());
        assertEquals("Title should be preserved", "Original Title", doc.title());
        assertEquals("Body text should be set", "Body text", doc.body().text());
    }
    
    @Test
    public void testNormaliseCreatesHtml() {
        // normalise() should create html element if missing
        Document doc = new Document(TEST_BASE_URI);
        assertTrue("Should have no html element before normalise", doc.select("html").isEmpty());
        Document result = doc.normalise();
        assertFalse("Should have html element after normalise", doc.select("html").isEmpty());
        assertSame("normalise() should return the document", doc, result);
    }
    
    @Test
    public void testNormaliseCreatesHeadAndBody() {
        // normalise() should create head and body if missing
        Document doc = new Document(TEST_BASE_URI);
        doc.normalise();
        assertNotNull("Head should be created", doc.head());
        assertNotNull("Body should be created", doc.body());
    }
    
    @Test
    public void testNormaliseWithShellStructure() {
        // normalise() should maintain existing valid structure
        Document doc = Document.createShell(TEST_BASE_URI);
        doc.head().appendElement("title").text("Test Title");
        Element origHead = doc.head();
        Element origBody = doc.body();
        
        doc.normalise();
        
        // Same elements should still be there
        assertEquals("Head should be the same", origHead, doc.head());
        assertEquals("Body should be the same", origBody, doc.body());
        assertEquals("Title should be preserved", "Test Title", doc.title());
    }
    
    @Test
    public void testNormaliseMultipleCalls() {
        // normalise() should be idempotent
        Document doc = Document.createShell(TEST_BASE_URI);
        String titleBefore = "Test";
        doc.title(titleBefore);
        
        doc.normalise();
        doc.normalise();
        
        assertEquals("Title should be preserved after multiple normalise", titleBefore, doc.title());
        assertNotNull("Head should exist after multiple normalise", doc.head());
        assertNotNull("Body should exist after multiple normalise", doc.body());
    }
    
    @Test
    public void testTitleGetterWithSpecialCharacters() {
        // title() should handle special characters in title text
        Document doc = Document.createShell(TEST_BASE_URI);
        String specialTitle = "Test & <Title> with \"quotes\"";
        doc.title(specialTitle);
        assertEquals("Title with special chars should be preserved", specialTitle, doc.title());
    }
    
    @Test
    public void testCreateShellReturnType() {
        // createShell should return a Document instance
        Document doc = Document.createShell(TEST_BASE_URI);
        assertTrue("Result should be Document instance", doc instanceof Document);
    }
    
    @Test
    public void testDocumentOuterHtmlNotEmpty() {
        // outerHtml() should return non-empty string for non-empty document
        Document doc = Document.createShell(TEST_BASE_URI);
        doc.body().appendElement("p").text("Content");
        String html = doc.outerHtml();
        assertTrue("Outer HTML should contain content", html.length() > 0);
        assertTrue("Should contain paragraph", html.contains("p"));
    }
}
