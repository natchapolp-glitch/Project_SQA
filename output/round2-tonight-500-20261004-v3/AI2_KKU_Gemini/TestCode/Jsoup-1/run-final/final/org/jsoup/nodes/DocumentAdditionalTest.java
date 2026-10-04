package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentAdditionalTest {

    @Test
    public void testNormaliseWithTextNodes() {
        Document doc = new Document("http://example.com");
        doc.appendChild(new TextNode("Root Text", ""));
        Element html = doc.appendElement("html");
        html.appendChild(new TextNode("Html Text", ""));
        
        Document normalised = doc.normalise();
        assertNotNull(normalised.body());
        assertTrue(normalised.body().text().contains("Root Text"));
        assertTrue(normalised.body().text().contains("Html Text"));
    }

}
