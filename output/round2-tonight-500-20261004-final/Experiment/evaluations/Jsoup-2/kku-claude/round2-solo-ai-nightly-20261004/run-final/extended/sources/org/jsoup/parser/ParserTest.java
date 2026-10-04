package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void parsesBasicHtmlStructure() {
        String html = "<html><head><title>Test</title></head><body><p>One</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        assertEquals("Test", doc.title());
        Element body = doc.body();
        assertNotNull(body);
        Elements_assertFirstText(body, "One");
    }

    @Test
    public void autoClosesUnclosedParagraphTags() {
        String html = "<body><p>1<p>2</body>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element body = doc.body();
        List<Element> paragraphs = body.children();

        int pCount = 0;
        for (Element el : paragraphs) {
            if (el.tagName().equals("p"))
                pCount++;
        }
        assertEquals(2, pCount);
    }

    @Test
    public void ignoresStrayEndTagWithoutThrowing() {
        String html = "<body><div>Hello</div></div></body>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element body = doc.body();
        assertEquals(1, body.children().size());
        assertEquals("div", body.children().get(0).tagName());
    }

    @Test
    public void parsesCommentAsCommentNode() {
        String html = "<html><body><!-- a comment --></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element body = doc.body();
        boolean foundComment = false;
        for (Node n : body.childNodes()) {
            if (n instanceof Comment) {
                foundComment = true;
                assertTrue(((Comment) n).getData().contains("a comment"));
            }
        }
        assertTrue(foundComment);
    }

    @Test
    public void parsesXmlDeclaration() {
        String html = "<?xml version=\"1.0\"?><html><body>Text</body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        boolean foundXmlDecl = false;
        for (Node n : doc.childNodes()) {
            if (n instanceof XmlDeclaration) {
                foundXmlDecl = true;
            }
        }
        assertTrue(foundXmlDecl);
    }

    @Test
    public void parsesAttributesWithDifferentQuoting() {
        String html = "<div id='single' class=\"double\" data-flag=unquoted></div>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element div = doc.select("div").first();
        assertNotNull(div);
        assertEquals("single", div.attr("id"));
        assertEquals("double", div.attr("class"));
        assertEquals("unquoted", div.attr("data-flag"));
    }

    @Test
    public void titleContentTreatedAsText() {
        String html = "<html><head><title>Hello <b>World</b></title></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        assertEquals("Hello <b>World</b>", doc.title());
    }

    @Test
    public void parseBodyFragmentWrapsContentInBody() {
        String fragment = "<p>Fragment content</p>";
        Document doc = Parser.parseBodyFragment(fragment, "http://example.com/");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(1, body.children().size());
        assertEquals("p", body.children().get(0).tagName());
        Elements_assertFirstText(body, "Fragment content");
    }

    @Test
    public void parseBodyFragmentWithEmptyStringProducesEmptyBody() {
        Document doc = Parser.parseBodyFragment("", "http://example.com/");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(0, body.children().size());
    }

    private void Elements_assertFirstText(Element parent, String expectedText) {
        boolean found = false;
        for (Node n : parent.childNodes()) {
            if (containsText(n, expectedText)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    private boolean containsText(Node node, String expectedText) {
        if (node instanceof TextNode) {
            return ((TextNode) node).getWholeText().contains(expectedText);
        }
        if (node instanceof Element) {
            for (Node child : node.childNodes()) {
                if (containsText(child, expectedText))
                    return true;
            }
        }
        return false;
    }
}
