package org.jsoup.nodes;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.assertEquals;

public class EntitiesTest {

    @Test
    public void testEscapeBasic() {
        Document doc = new Document("");
        Document.OutputSettings out = doc.outputSettings();
        out.escapeMode(Entities.EscapeMode.base);
        String input = "<div>&</div>";
        String escaped = Entities.escape(input, out);
        assertEquals("&lt;div&gt;&amp;&lt;/div&gt;", escaped);
    }

    @Test
    public void testEscapeExtended() {
        Document doc = new Document("");
        Document.OutputSettings out = doc.outputSettings();
        out.escapeMode(Entities.EscapeMode.extended);
        String input = "\u00A9"; // Copyright symbol
        String escaped = Entities.escape(input, out);
        assertEquals("&COPY;", escaped);
    }

    @Test
    public void testEscapeWithEncoderFallback() {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        String input = "H\u00E9llo"; // accented e
        String escaped = Entities.escape(input, asciiEncoder, Entities.EscapeMode.base);
        assertEquals("H&#233;llo", escaped);
    }

    @Test
    public void testUnescapeNamedEntities() {
        String input = "&lt;div&gt;&amp; &quot;test&quot;&lt;/div&gt;";
        String unescaped = Entities.unescape(input);
        assertEquals("<div>& \"test\"</div>", unescaped);
    }

    @Test
    public void testUnescapeNumericEntities() {
        String input = "&#38;&#x26;";
        String unescaped = Entities.unescape(input);
        assertEquals("&&", unescaped);
    }

    @Test
    public void testUnescapeNoEntities() {
        String input = "Hello World";
        String unescaped = Entities.unescape(input);
        assertEquals("Hello World", unescaped);
    }

    @Test
    public void testUnescapeMalformedEntities() {
        String input = "&invalidEntity; &";
        String unescaped = Entities.unescape(input);
        assertEquals("&invalidEntity; &", unescaped);
    }
}
