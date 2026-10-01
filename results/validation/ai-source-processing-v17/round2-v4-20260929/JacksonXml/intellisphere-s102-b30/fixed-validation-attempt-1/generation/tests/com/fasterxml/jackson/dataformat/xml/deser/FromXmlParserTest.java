package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.dataformat.xml.PackageVersion;

import static org.junit.Assert.*;

public class FromXmlParserTest {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    private FromXmlParser createParser(String xml) throws Exception {
        return createParser(xml, 0, 0, null);
    }

    private FromXmlParser createParser(String xml, int genericFeatures, int xmlFeatures, ObjectCodec codec)
            throws Exception {
        IOContext ctxt = new IOContext(new BufferRecycler(), xml, false);
        XMLStreamReader sr = XML_INPUT_FACTORY.createXMLStreamReader(new StringReader(xml));
        return new FromXmlParser(ctxt, genericFeatures, xmlFeatures, codec, sr);
    }

    @Test
    public void testRequiresCustomCodec() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertTrue(parser.requiresCustomCodec());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testVersion() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            Version v = parser.version();
            assertNotNull(v);
            assertEquals(PackageVersion.VERSION, v);
        } finally {
            parser.close();
        }
    }

    @Test
    public void testCodecAccessors() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertNull(parser.getCodec());
            parser.setCodec(null);
            assertNull(parser.getCodec());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testFormatFeaturesAndOverrides() throws Exception {
        FromXmlParser parser = createParser("<root/>", 0, 0b101, null);
        try {
            assertEquals(0b101, parser.getFormatFeatures());

            // override bits with mask: override bits 0b010 with 0b010 (setting it), mask 0b110
            parser.overrideFormatFeatures(0b010, 0b110);
            assertEquals(0b011, parser.getFormatFeatures());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testStaxReaderAccess() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertNotNull(parser.getStaxReader());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testHasTextCharactersAndTextOffset() throws Exception {
        FromXmlParser parser = createParser("<root>text</root>");
        try {
            assertFalse(parser.hasTextCharacters());
            assertEquals(0, parser.getTextOffset());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertNull(parser.getEmbeddedObject());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testNumericAccessorsDefaults() throws Exception {
        FromXmlParser parser = createParser("<root>123</root>");
        try {
            assertEquals(0, parser.getIntValue());
            assertEquals(0L, parser.getLongValue());
            assertEquals(0.0, parser.getDoubleValue(), 0.00001);
            assertEquals(0.0f, parser.getFloatValue(), 0.00001f);
            assertNull(parser.getNumberValue());
            assertNull(parser.getNumberType());
            assertNull(parser.getDecimalValue());
            assertNull(parser.getBigIntegerValue());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testIsEmptyHelper() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertTrue(parser._isEmpty(null));
            assertTrue(parser._isEmpty(""));
            assertTrue(parser._isEmpty("   \t\r\n "));
            assertFalse(parser._isEmpty("  a  "));
            assertFalse(parser._isEmpty("text"));
        } finally {
            parser.close();
        }
    }

    @Test
    public void testGetByteArrayBuilderReuse() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            ByteArrayBuilder builder1 = parser._getByteArrayBuilder();
            assertNotNull(builder1);
            builder1.append(1);

            ByteArrayBuilder builder2 = parser._getByteArrayBuilder();
            assertSame(builder1, builder2);
            assertEquals(0, builder2.toByteArray().length);
        } finally {
            parser.close();
        }
    }

    @Test
    public void testCloseIdempotency() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testLocations() throws Exception {
        FromXmlParser parser = createParser("<root>abc</root>");
        try {
            JsonLocation tokenLoc = parser.getTokenLocation();
            assertNotNull(tokenLoc);
            JsonLocation currLoc = parser.getCurrentLocation();
            assertNotNull(currLoc);
        } finally {
            parser.close();
        }
    }

    @Test
    public void testNextTokenBasicTraversal() throws Exception {
        FromXmlParser parser = createParser("<root><child>value</child></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals("child", parser.getCurrentName());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            assertEquals("value", parser.getText());
            assertEquals("value", parser.getValueAsString());
            assertEquals(5, parser.getTextLength());
            assertArrayEquals("value".toCharArray(), parser.getTextCharacters());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
            assertNull(parser.nextToken());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testGetValueAsStringWithDefault() throws Exception {
        FromXmlParser parser = createParser("<root><child>hello</child></root>");
        try {
            assertNull(parser.getValueAsString("fallback")); // before any token
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals("child", parser.getValueAsString("fallback"));
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            assertEquals("hello", parser.getValueAsString("fallback"));
        } finally {
            parser.close();
        }
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        FromXmlParser parser = createParser("<root><child>value</child></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals("child", parser.getCurrentName());
            parser.overrideCurrentName("renamedChild");
            assertEquals("renamedChild", parser.getCurrentName());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testGetCurrentNameMissingThrows() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            // currToken is null initially and parsingContext in root has no name
            parser.getCurrentName();
            fail("Expected IllegalStateException for missing name");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Missing name"));
        } finally {
            parser.close();
        }
    }

    @Test
    public void testIsExpectedStartArrayToken() throws Exception {
        FromXmlParser parser = createParser("<root><item>1</item></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertTrue(parser.isExpectedStartArrayToken());
            assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
            assertTrue(parser.isExpectedStartArrayToken());
            assertTrue(parser.getParsingContext().inArray());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testIsExpectedStartArrayTokenWhenNotStartObject() throws Exception {
        FromXmlParser parser = createParser("<root><item>1</item></root>");
        try {
            // before any token is fetched, _currToken is null
            assertFalse(parser.isExpectedStartArrayToken());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testBase64DecodingSuccess() throws Exception {
        // "SGVsbG8=" is base64 for "Hello"
        FromXmlParser parser = createParser("<root>SGVsbG8=</root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            byte[] bytes = parser.getBinaryValue(Base64Variants.MIME);
            assertNotNull(bytes);
            assertEquals("Hello", new String(bytes, "UTF-8"));

            // Repeated call returns cached binary value
            byte[] cached = parser.getBinaryValue(Base64Variants.MIME);
            assertSame(bytes, cached);
        } finally {
            parser.close();
        }
    }

    @Test(expected = IOException.class)
    public void testGetBinaryValueInvalidToken() throws Exception {
        FromXmlParser parser = createParser("<root><child>value</child></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            // Calling getBinaryValue on FIELD_NAME token should fail
            parser.getBinaryValue(Base64Variants.MIME);
        } finally {
            parser.close();
        }
    }

    @Test(expected = IOException.class)
    public void testGetBinaryValueMalformedBase64() throws Exception {
        FromXmlParser parser = createParser("<root>!Invalid-Base64!</root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            parser.getBinaryValue(Base64Variants.MIME);
        } finally {
            parser.close();
        }
    }

    @Test
    public void testNextTextValue() throws Exception {
        FromXmlParser parser = createParser("<root><name>Jackson</name></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            String text = parser.nextTextValue();
            assertNull(text);
            assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());

            text = parser.nextTextValue();
            assertEquals("Jackson", text);
            assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testNextTextValueEmptyLeaf() throws Exception {
        FromXmlParser parser = createParser("<root><empty/></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            // <empty/> without text: nextTextValue produces ""
            String text = parser.nextTextValue();
            assertEquals("", text);
            assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testSetXMLTextElementName() throws Exception {
        FromXmlParser parser = createParser("<root attr='val'>content</root>");
        try {
            parser.setXMLTextElementName("customTextProp");
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals("attr", parser.getCurrentName());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            assertEquals("val", parser.getText());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals("customTextProp", parser.getCurrentName());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            assertEquals("content", parser.getText());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testAddVirtualWrapping() throws Exception {
        FromXmlParser parser = createParser("<root><item>1</item></root>");
        try {
            Set<String> wrapping = new HashSet<String>(Collections.singletonList("item"));
            parser.addVirtualWrapping(wrapping);
            assertNotNull(parser.getParsingContext());
        } finally {
            parser.close();
        }
    }

    @Test
    public void testHandleEOFInRoot() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            assertTrue(parser.getParsingContext().inRoot());
            // In root context, _handleEOF should succeed cleanly
            parser._handleEOF();
        } finally {
            parser.close();
        }
    }

    @Test(expected = JsonParseException.class)
    public void testHandleEOFNotInRootThrows() throws Exception {
        FromXmlParser parser = createParser("<root><child/></root>");
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertFalse(parser.getParsingContext().inRoot());
            parser._handleEOF();
        } finally {
            parser.close();
        }
    }

    @Test
    public void testReleaseBuffers() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        try {
            parser._releaseBuffers();
        } finally {
            parser.close();
        }
    }
}
