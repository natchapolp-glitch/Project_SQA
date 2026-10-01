Here is the deterministic regression test suite for org.jsoup.nodes.Document, designed for JUnit 4.5 and Java 1.5+.

org/jsoup/nodes/DocumentTest.java

```java
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DocumentTest {

    @Test
    public void testDocumentConstructorAndNodeName() {
        Document doc = new Document("http://example.com/");
        assertEquals("#document", doc.nodeName());
        assertEquals("http://example.com/", doc.baseUri());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void testCreateShellStructure() {
        Document doc = Document.createShell("http://example.com/");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("http://example.com/", doc.baseUri());

        Element html = doc.select("html").first();
        assertNotNull(html);
        assertSame(html, doc.head().parent());
        assertSame(html, doc.body().parent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShellNullUriThrowsException() {
        Document.createShell(null);
    }

    @Test
    public void testHeadAndBodyWhenEmpty() {
        Document doc = new Document("http://example.com/");
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testTitleGetWhenNotPresent() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("", doc.title());
    }

    @Test
    public void testTitleSetAndGet() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Test Document Title");
        assertEquals("Test Document Title", doc.title());

        Element titleEl = doc.head().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Document Title", titleEl.text());
    }

    @Test
    public void testTitleUpdateExisting() {
        Document doc = Document.createShell("http://example.com/");
        doc.title("Initial Title");
        assertEquals("Initial Title", doc.title());

        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());
        assertEquals(1, doc.getElementsByTag("title").size());
    }

    @Test
    public void testTitleTrimsWhitespace() {
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
        Document doc = new Document("http://example.com/base/");
        Element div = doc.createElement("div");

        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com/base/", div.baseUri());
        assertNull(div.parent());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void testOuterHtmlEmptyDoc() {
        Document doc = new Document("http://example.com/");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testOuterHtmlMatchesHtmlContent() {
        Document doc = Document.createShell("http://example.com/");
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.outerHtml());
    }

    @Test
    public void testTextSetsBodyContent() {
        Document doc = Document.createShell("http://example.com/");
        doc.body().appendElement("p").text("Old paragraph");

        doc.text("New text body");

        assertEquals("New text body", doc.body().text());
        assertEquals(0, doc.body().getElementsByTag("p").size());
        assertNotNull(doc.head());
    }

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("http://example.com/");
        doc.normalise();

        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseMissingHead() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        html.appendElement("body");

        doc.normalise();

        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertTrue(doc.head().siblingIndex() < doc.body().siblingIndex());
    }

    @Test
    public void testNormaliseMissingBody() {
        Document doc = new Document("http://example.com/");
        Element html = doc.appendElement("html");
        html.appendElement("head");

        doc.normalise();

        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testNormaliseMovesTextFromDocumentRootToBody() {
        Document doc = Document.createShell("http://example.com/");
        TextNode rootText = new TextNode("Root Text", "http://example.com/");
        doc.appendChild(rootText);

        doc.normalise();

        assertTrue(doc.body().text().contains("Root Text"));
        assertEquals(1, doc.childNodes.size()); // only <html> remains directly under doc root
    }

    @Test
    public void testNormaliseMovesTextFromHeadToBody() {
        Document doc = Document.createShell("http://example.com/");
        TextNode headText = new TextNode("Accidental Head Text", "http://example.com/");
        doc.head().appendChild(headText);

        doc.normalise();

        assertTrue(doc.body().text().contains("Accidental Head Text"));
        assertEquals(0, doc.head().childNodes.size());
    }

    @Test
    public void testNormaliseMovesTextFromHtmlToBody() {
        Document doc = Document.createShell("http://example.com/");
        Element html = doc.select("html").first();
        TextNode htmlText = new TextNode("Html Scope Text", "http://example.com/");
        html.appendChild(htmlText);

        doc.normalise();

        assertTrue(doc.body().text().contains("Html Scope Text"));
    }

    @Test
    public void testNormaliseIgnoresBlankTextNodes() {
        Document doc = Document.createShell("http://example.com/");
        TextNode blankNode = new TextNode("   \n\t  ", "http://example.com/");
        doc.appendChild(blankNode);

        doc.normalise();

        // Blank node is not pushed into body text
        assertEquals("", doc.body().text());
    }
}

```



References

Jsoup HTML Parser (v1.1.2-SNAPSHOT / Revision 1f):
Hedley, J. (2009). Jsoup: Java HTML Parser. Source code repository: https://github.com/jhy/jsoup
Specification of org.jsoup.nodes.Document, org.jsoup.nodes.Element, and org.jsoup.nodes.TextNode.
Defects4J Benchmark:
Just, R., Jalali, D., & Ernst, M. D. (2014). Defects4J: A database of existing faults to enable controlled testing studies for Java. In Proceedings of the 2014 International Symposium on Software Testing and Analysis (ISSTA), pp. 437–440. https://doi.org/10.1145/2610384.2628055
JUnit 4 Documentation:
JUnit Team. (2009). JUnit 4.5 API and Assertions. https://junit.org/junit4/
