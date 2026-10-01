I could not compile or run these tests, so none of the expected values below have been executed.

Assumptions from the supplied build files

pom.xml declares JUnit 4.5 and Commons Lang 2.4, and the compiler source/target level is 1.5. So the tests use org.junit.Test and org.junit.Assert, with no diamond operator, lambdas, assertThrows or JUnit 5.
The tests sit in package org.jsoup.nodes so they can reach the package-visible childNodes field, which Document itself uses.
Only APIs visible in the supplied source are used: appendElement, prependElement, appendChild, getElementsByTag, select, Elements.first()/isEmpty(), TextNode(String,String), html(), baseUri(), nodeName() and text(). I did not use APIs that I could not confirm for this revision, such as Elements.size().
Exact pretty-printed HTML layout is not shown in the supplied source, so assertions on HTML use contains and indexOf rather than full-string equality.
The private normalise(Element) is covered only indirectly, through normalise().

org/jsoup/nodes/DocumentGeneratedTest.java

```java
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Regression tests for org.jsoup.nodes.Document (jsoup 1.1.2-SNAPSHOT reference revision).
 * Written for JUnit 4.5 / Java 1.5 source level.
 */
public class DocumentGeneratedTest {

    private static final String BASE = "http://example.com/";

    // ---------- constructor / nodeName ----------

    @Test
    public void constructorCreatesEmptyDocumentWithBaseUri() {
        Document doc = new Document(BASE);
        assertEquals(BASE, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertTrue(doc.select("html").isEmpty());
        assertEquals(0, doc.childNodes.size());
    }

    @Test
    public void nodeNameIsDocument() {
        assertEquals("#document", new Document("").nodeName());
        assertEquals("#document", Document.createShell(BASE).nodeName());
    }

    // ---------- createShell ----------

    @Test
    public void createShellBuildsHtmlHeadBody() {
        Document doc = Document.createShell(BASE);
        assertEquals(BASE, doc.baseUri());
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().nodeName());
        assertEquals("body", doc.body().nodeName());
    }

    @Test
    public void createShellAcceptsEmptyBaseUri() {
        Document doc = Document.createShell("");
        assertEquals("", doc.baseUri());
        assertNotNull(doc.body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createShellNullBaseUriThrows() {
        Document.createShell(null);
    }

    // ---------- head / body ----------

    @Test
    public void headAndBodyAreSameAsFirstByTag() {
        Document doc = Document.createShell(BASE);
        assertSame(doc.getElementsByTag("head").first(), doc.head());
        assertSame(doc.getElementsByTag("body").first(), doc.body());
    }

    @Test
    public void headAndBodyNullOnEmptyDocument() {
        Document doc = new Document(BASE);
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void bodyFoundWhenAddedManually() {
        Document doc = new Document(BASE);
        Element html = doc.appendElement("html");
        Element body = html.appendElement("body");
        assertSame(body, doc.body());
        assertNull(doc.head());
    }

    // ---------- title ----------

    @Test
    public void titleIsEmptyWhenNoTitleElement() {
        assertEquals("", Document.createShell(BASE).title());
        assertEquals("", new Document(BASE).title());
    }

    @Test
    public void titleSetterAddsTitleToHead() {
        Document doc = Document.createShell(BASE);
        doc.title("Hello");
        assertEquals("Hello", doc.title());
        Element titleEl = doc.head().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Hello", titleEl.text());
    }

    @Test
    public void titleSetterUpdatesExistingTitle() {
        Document doc = Document.createShell(BASE);
        doc.title("First");
        Element before = doc.getElementsByTag("title").first();
        doc.title("Second");
        Element after = doc.getElementsByTag("title").first();
        assertSame(before, after);
        assertEquals("Second", doc.title());
        assertFalse(doc.outerHtml().contains("First"));
    }

    @Test
    public void titleGetterTrimsWhitespace() {
        Document doc = Document.createShell(BASE);
        doc.title("   Padded Title   ");
        assertEquals("Padded Title", doc.title());
    }

    @Test
    public void titleReadsExistingTitleElementOutsideHead() {
        Document doc = new Document(BASE);
        doc.appendElement("title").text("Direct");
        assertEquals("Direct", doc.title());
        doc.title("Changed");
        assertEquals("Changed", doc.title());
    }

    @Test
    public void titleSetterAcceptsEmptyString() {
        Document doc = Document.createShell(BASE);
        doc.title("");
        assertEquals("", doc.title());
        assertNotNull(doc.getElementsByTag("title").first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void titleSetterNullThrows() {
        Document.createShell(BASE).title(null);
    }

    @Test(expected = NullPointerException.class)
    public void titleSetterWithoutHeadThrowsNpe() {
        // no title element and no head element: head() returns null
        new Document(BASE).title("x");
    }

    // ---------- createElement ----------

    @Test
    public void createElementUsesDocumentBaseUri() {
        Document doc = Document.createShell(BASE);
        Element a = doc.createElement("a");
        assertEquals("a", a.nodeName());
        assertEquals(BASE, a.baseUri());
    }

    @Test
    public void createElementDoesNotAttachToDocument() {
        Document doc = Document.createShell(BASE);
        doc.createElement("div");
        assertTrue(doc.getElementsByTag("div").isEmpty());
    }

    @Test
    public void createElementReturnsNewInstanceEachCall() {
        Document doc = new Document(BASE);
        Element one = doc.createElement("p");
        Element two = doc.createElement("p");
        assertTrue(one != two);
    }

    // ---------- text(String) ----------

    @Test
    public void textSetsBodyTextAndReturnsDocument() {
        Document doc = Document.createShell(BASE);
        Element result = doc.text("Hello there");
        assertSame(doc, result);
        assertEquals("Hello there", doc.body().text());
    }

    @Test
    public void textReplacesExistingBodyContent() {
        Document doc = Document.createShell(BASE);
        doc.body().appendElement("p").text("old");
        doc.text("new");
        assertTrue(doc.body().getElementsByTag("p").isEmpty());
        assertEquals("new", doc.body().text());
    }

    @Test
    public void textDoesNotDestroyDocumentStructure() {
        Document doc = Document.createShell(BASE);
        doc.text("content");
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test(expected = NullPointerException.class)
    public void textWithoutBodyThrowsNpe() {
        new Document(BASE).text("x");
    }

    // ---------- outerHtml ----------

    @Test
    public void outerHtmlHasNoWrapperTag() {
        Document doc = Document.createShell(BASE);
        String html = doc.outerHtml();
        assertEquals(doc.html(), html);
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("<body>"));
        assertFalse(html.contains("#root"));
        assertFalse(html.contains("#document"));
    }

    @Test
    public void outerHtmlIncludesTitleAndBodyText() {
        Document doc = Document.createShell(BASE);
        doc.title("T1");
        doc.text("Body text");
        String html = doc.outerHtml();
        assertTrue(html.contains("T1"));
        assertTrue(html.contains("Body text"));
    }

    // ---------- normalise ----------

    @Test
    public void normaliseOnEmptyDocumentCreatesHtmlHeadBody() {
        Document doc = new Document(BASE);
        Document result = doc.normalise();
        assertSame(doc, result);
        assertNotNull(doc.select("html").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        String html = doc.outerHtml();
        assertTrue(html.indexOf("<head>") < html.indexOf("<body>"));
    }

    @Test
    public void normaliseAddsMissingHeadAndBodyToExistingHtml() {
        Document doc = new Document(BASE);
        Element html = doc.appendElement("html");
        assertNull(doc.head());
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertSame(html, doc.select("html").first());
    }

    @Test
    public void normaliseAddsMissingBodyWhenHeadPresent() {
        Document doc = new Document(BASE);
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        doc.normalise();
        assertSame(head, doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void normaliseMovesRootTextIntoBody() {
        Document doc = Document.createShell(BASE);
        TextNode tn = new TextNode("Loose text", "");
        doc.appendChild(tn);
        doc.normalise();
        assertFalse(doc.childNodes.contains(tn));
        assertTrue(doc.body().text().contains("Loose text"));
    }

    @Test
    public void normaliseMovesHtmlAndHeadTextIntoBodyKeepingOrder() {
        Document doc = Document.createShell(BASE);
        doc.head().appendChild(new TextNode("inHead", ""));
        doc.appendChild(new TextNode("inRoot", ""));
        doc.normalise();
        String bodyText = doc.body().text();
        assertTrue(bodyText.contains("inHead"));
        assertTrue(bodyText.contains("inRoot"));
        assertTrue(bodyText.indexOf("inRoot") < bodyText.indexOf("inHead"));
        assertFalse(doc.head().text().contains("inHead"));
    }

    @Test
    public void normaliseLeavesBlankTextNodesInPlace() {
        Document doc = Document.createShell(BASE);
        TextNode blank = new TextNode("   ", "");
        doc.appendChild(blank);
        doc.normalise();
        assertTrue(doc.childNodes.contains(blank));
        assertEquals(0, doc.body().childNodes.size());
    }

    @Test
    public void normaliseKeepsExistingBodyElements() {
        Document doc = Document.createShell(BASE);
        Element p = doc.body().appendElement("p");
        p.text("para");
        doc.normalise();
        assertSame(p, doc.body().getElementsByTag("p").first());
        assertEquals("para", p.text());
    }

    @Test
    public void normaliseIsIdempotentOnStructure() {
        Document doc = new Document(BASE);
        doc.normalise();
        Element html = doc.select("html").first();
        Element head = doc.head();
        Element body = doc.body();
        doc.normalise();
        assertSame(html, doc.select("html").first());
        assertSame(head, doc.head());
        assertS
```
