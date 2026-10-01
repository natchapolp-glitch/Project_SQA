package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromXmlParserTest {

    private XMLInputFactory _xmlInputFactory;

    @Before
    public void setUp() {
        _xmlInputFactory = XMLInputFactory.newFactory();
    }

    @After
    public void tearDown() {
        _xmlInputFactory = null;
    }

    private FromXmlParser _createParser(String xml) throws XMLStreamException {
        return _createParser(xml, 0, 0, null);
    }

    private FromXmlParser _createParser(String xml, int genericFeatures, int xmlFeatures, ObjectCodec codec)
            throws XMLStreamException {
        XMLStreamReader sr = _xmlInputFactory.createXMLStreamReader(new StringReader(xml));
        sr.nextTag();
        IOContext ctxt = new IOContext(new BufferRecycler(), xml, false);
        return new FromXmlParser(ctxt, genericFeatures, xmlFeatures, codec, sr);
    }

    @Test
    public void testVersionAndCodec() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        Version v = parser.version();
        Assert.assertNotNull(v);
        Assert.assertFalse(v.isUnknownVersion());

        Assert.assertTrue(parser.requiresCustomCodec());
        Assert.assertNull(parser.getCodec());

        ObjectCodec codec = new ObjectMapper();
        parser.setCodec(codec);
        Assert.assertSame(codec, parser.getCodec());
        parser.close();
    }

    @Test
    public void testFormatFeatures() throws Exception {
        FromXmlParser parser = _createParser("<root/>", 0, 0b0101, null);
        Assert.assertEquals(0b0101, parser.getFormatFeatures());

        parser.overrideFormatFeatures(0b1010, 0b1111);
        Assert.assertEquals(0b1010, parser.getFormatFeatures());

        parser.overrideFormatFeatures(0b0001, 0b0001);
        Assert.assertEquals(0b1011, parser.getFormatFeatures());
        parser.close();
    }

    @Test
    public void testCloseAndIsClosed() throws Exception {
        FromXmlParser parser = _createParser("<root>data</root>");
        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
        // Redundant close must be safe
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    

    

    

    

    

    

    @Test
    public void testIsExpectedStartArrayTokenConversion() throws Exception {
        FromXmlParser parser = _createParser("<list><item>1</item></list>");

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertTrue(parser.isExpectedStartArrayToken());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
        Assert.assertTrue(parser.isExpectedStartArrayToken());

        parser.nextToken(); // item
        Assert.assertFalse(parser.isExpectedStartArrayToken());
        parser.close();
    }

    

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentNameThrowsWhenMissingName() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        parser.getCurrentName();
    }

    @Test
    public void testLocationsAndStaxReader() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        Assert.assertNotNull(parser.getStaxReader());
        Assert.assertNotNull(parser.getParsingContext());

        JsonLocation locCurrent = parser.getCurrentLocation();
        JsonLocation locToken = parser.getTokenLocation();
        Assert.assertNotNull(locCurrent);
        Assert.assertNotNull(locToken);
        parser.close();
    }

    @Test
    public void testTextPropertiesAndBuffers() throws Exception {
        FromXmlParser parser = _createParser("<root>some-text</root>");
        Assert.assertFalse(parser.hasTextCharacters());
        Assert.assertEquals(0, parser.getTextOffset());

        Assert.assertNull(parser.getText());
        Assert.assertNull(parser.getTextCharacters());
        Assert.assertEquals(0, parser.getTextLength());

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING

        Assert.assertArrayEquals("some-text".toCharArray(), parser.getTextCharacters());
        Assert.assertEquals("some-text".length(), parser.getTextLength());
        Assert.assertEquals(0, parser.getTextOffset());
        parser.close();
    }

    @Test
    public void testBase64DecodingSuccess() throws Exception {
        // "QWxhZGRpbjpvcGVuIHNlc2FtZQ==" is base64 for "Aladdin:open sesame"
        FromXmlParser parser = _createParser("<root>QWxhZGRpbjpvcGVuIHNlc2FtZQ==</root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING

        byte[] decoded = parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        Assert.assertEquals("Aladdin:open sesame", new String(decoded, "UTF-8"));

        // Repeated call reuses cached byte array
        byte[] cached = parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        Assert.assertSame(decoded, cached);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testBase64DecodingInvalidContent() throws Exception {
        FromXmlParser parser = _createParser("<root>not_valid_b64!@#$%</root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        try {
            parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        } finally {
            parser.close();
        }
    }

    @Test(expected = JsonParseException.class)
    public void testGetBinaryValueOnNonStringToken() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        parser.nextToken(); // START_OBJECT
        try {
            parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS);
        } finally {
            parser.close();
        }
    }

    @Test
    public void testNumericAccessorsDefaults() throws Exception {
        FromXmlParser parser = _createParser("<root>123</root>");
        Assert.assertEquals(0, parser.getIntValue());
        Assert.assertEquals(0L, parser.getLongValue());
        Assert.assertEquals(0.0, parser.getDoubleValue(), 0.0001);
        Assert.assertEquals(0.0f, parser.getFloatValue(), 0.0001f);
        Assert.assertNull(parser.getBigIntegerValue());
        Assert.assertNull(parser.getDecimalValue());
        Assert.assertNull(parser.getNumberValue());
        Assert.assertNull(parser.getNumberType());
        Assert.assertNull(parser.getEmbeddedObject());
        parser.close();
    }

    @Test
    public void testIsEmptyHelper() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        Assert.assertTrue(parser._isEmpty(null));
        Assert.assertTrue(parser._isEmpty(""));
        Assert.assertTrue(parser._isEmpty("    "));
        Assert.assertTrue(parser._isEmpty(" \t \r \n "));
        Assert.assertFalse(parser._isEmpty(" a "));
        Assert.assertFalse(parser._isEmpty("content"));
        parser.close();
    }

    @Test
    public void testByteArrayBuilderLifecycle() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        ByteArrayBuilder builder1 = parser._getByteArrayBuilder();
        Assert.assertNotNull(builder1);
        builder1.append(42);

        ByteArrayBuilder builder2 = parser._getByteArrayBuilder();
        Assert.assertSame(builder1, builder2);
        Assert.assertEquals(0, builder2.toByteArray().length);
        parser.close();
    }

    @Test
    public void testHandleEOF() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        // In root context, _handleEOF must succeed quietly
        parser._handleEOF();

        // Advance into non-root context
        parser.nextToken(); // START_OBJECT
        try {
            parser._handleEOF();
            Assert.fail("Expected JsonParseException for EOF in open non-root context");
        } catch (JsonParseException expected) {
            Assert.assertTrue(expected.getMessage().contains("expected close marker"));
        } finally {
            parser.close();
        }
    }

    @Test
    public void testReleaseBuffers() throws Exception {
        FromXmlParser parser = _createParser("<root/>");
        parser._releaseBuffers();
        parser.close();
    }

    

    
}
