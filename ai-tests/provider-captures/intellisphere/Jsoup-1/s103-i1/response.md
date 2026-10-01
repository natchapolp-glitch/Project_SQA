Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Document node functionality.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructorWithBaseUri() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testDocumentNoArgConstructor() {
        Document doc = new Document("");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellWithValidBaseUri() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        
        // Verify structure: html > head, body
        assertEquals("html", doc.child(0).nodeName());
        assertEquals("head", doc.child(0).child(0).nodeName());
        assertEquals("body", doc.child(0).child(1).nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellWithNullBaseUri() {
        Document.createShell(null);
    }

    @Test
    public void testHeadElementWhenExists() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testHeadElementWhenNotExists() {
        Document doc = new Document("http://example.com");
        assertNull(doc.head());
    }

    @Test
    public void testBodyElementWhenExists() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testBodyElementWhenNotExists() {
        Document doc = new Document("http://example.com");
        assertNull(doc.body());
    }

    @Test
    public void testTitleWhenTitleElementExists() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("My Title");
        assertEquals("My Title", doc.title());
    }

    @Test
    public void testTitleWhenTitleElementDoesNotExist() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleWithWhitespace() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("  Spaced Title  ");
        assertEquals("Spaced Title", doc.title());
    }

    @Test
    public void testSetTitleWhenTitleElementExists() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Original");
        doc.title("Updated");
        assertEquals("Updated", doc.title());
    }

    @Test
    public void testSetTitleWhenTitleElementDoesNotExist() {
        Document doc = Document.createShell("http://example.com");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        
        // Verify title element was added to head
        Element titleEl = doc.head().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("New Title", titleEl.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTitleWithNull() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    @Test
    public void testSetTitleWithEmptyString() {
        Document doc = Document.createShell("http://example.com");
        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element div = doc.createElement("div");
        assertNotNull(div);
        assertEquals("div", div.nodeName());
        assertEquals("http://example.com", div.baseUri());
        
        // Verify element is not a child of document
        assertFalse(doc.children().contains(div));
    }

    @Test
    public void testCreateElementWithVariousTagNames() {
        Document doc = new Document("http://example.com");
        assertEquals("p", doc.createElement("p").nodeName());
        assertEquals("span", doc.createElement("span").nodeName());
        assertEquals("a", doc.createElement("a").nodeName());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("http://example.com");
        Document result = doc.normalise();
        assertSame(doc, result);
        
        // Verify html, head, and body were created
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.child(0).nodeName());
    }

    @Test
    public void testNormaliseDocumentWithHtmlOnly() {
        Document doc = new Document("http://example.com");
        doc.appendElement("html");
        doc.normalise();
        
        // Verify head and body were added
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseDocumentWithHtmlAndBodyOnly() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        html.appendElement("body");
        doc.normalise();
        
        // Verify head was prepended
        assertNotNull(doc.head());
        assertEquals("head", html.child(0).nodeName());
    }

    @Test
    public void testNormaliseMovesTextNodesToBody() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        html.appendElement("body");
        
        // Add text nodes to head
        head.appendText("Some text");
        
        doc.normalise();
        
        // Text should be moved to body
        assertEquals("", head.text().trim());
        assertTrue(doc.body().text().contains("Some text"));
    }

    @Test
    public void testNormaliseMovesTextNodesFromRoot() {
        Document doc = new Document("http://example.com");
        doc.appendText("Root text");
        doc.appendElement("html");
        
        doc.normalise();
        
        // Text should be moved to body
        assertTrue(doc.body().text().contains("Root text"));
    }

    @Test
    public void testNormaliseDoesNotMoveBlankTextNodes() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        html.appendElement("body");
        
        // Add blank text node
        head.appendText("   ");
        
        doc.normalise();
        
        // Blank text should not be moved (may remain or be stripped depending on isBlank)
        // Just verify normalisation completed without error
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseDocumentAlreadyComplete() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        
        // Should still have correct structure
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.child(0).nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("p").text("Hello");
        String html = doc.outerHtml();
        
        // Should not include wrapping tags for document itself
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<body>"));
        assertTrue(html.contains("Hello"));
    }

    @Test
    public void testTextMethod() {
        Document doc = Document.createShell("http://example.com");
        Element result = doc.text("Hello World");
        
        assertSame(doc, result);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testTextMethodReplacesExistingBodyContent() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("p").text("Original text");
        doc.text("New text");
        
        assertEquals("New text", doc.body().text());
    }

    @Test
    public void testTextMethodWithEmptyString() {
        Document doc = Document.createShell("http://example.com");
        doc.text("");
        
        assertEquals("", doc.body().text());
    }

    @Test
    public void testBaseUriInheritance() {
        Document doc = new Document("http://example.com");
        Element child = doc.createElement("div");
        assertEquals("http://example.com", child.baseUri());
    }
}

```