`com/fasterxml/jackson/dataformat/xml/deser/FromXmlParserTest.java`

```java
package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Assert;
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

public class FromXmlParserTest {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    private FromXmlParser createParser(String xml) throws Exception {
        return createParser(xml, null, 0, 0);
    }

    private FromXmlParser createParser(String xml, ObjectCodec codec, int genericFeatures, int xmlFeatures) throws Exception {
        XMLStreamReader xmlReader = XML_INPUT_FACTORY.createXMLStreamReader(new StringReader(xml));
        IOContext ioContext = new IOContext(new BufferRecycler(), xml, false);
        return new FromXmlParser(ioContext, genericFeatures, xmlFeatures, codec, xmlReader);
    }

    @Test
    public void testVersionAndCodec() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        Version v = parser.version();
        Assert.assertNotNull(v);
        Assert.assertFalse(v.isUnknownVersion());

        Assert.assertNull(parser.getCodec());
        ObjectCodec dummyCodec = null;
        parser.setCodec(dummyCodec);
        Assert.assertNull(parser.getCodec());

        Assert.assertTrue(parser.requiresCustomCodec());
        parser.close();
    }

    @Test
    public void testFormatFeaturesConfiguration() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        Assert.assertEquals(0, parser.getFormatFeatures());

        parser.overrideFormatFeatures(0x05, 0x07);
        Assert.assertEquals(0x05, parser.getFormatFeatures());

        parser.overrideFormatFeatures(0x02, 0x03);
        Assert.assertEquals(0x06, parser.getFormatFeatures());
        parser.close();
    }

    @Test
    public void testStaxReaderAccess() throws Exception {
        FromXmlParser parser = createParser("<root attr='val'>text</root>");
        XMLStreamReader reader = parser.getStaxReader();
        Assert.assertNotNull(reader);
        parser.close();
    }

    @Test
    public void testDefaultNumericValues() throws Exception {
        FromXmlParser parser = createParser("<root>123</root>");
        Assert.assertEquals(0, parser.getIntValue());
        Assert.assertEquals(0L, parser.getLongValue());
        Assert.assertEquals(0.0f, parser.getFloatValue(), 0.0001f);
        Assert.assertEquals(0.0, parser.getDoubleValue(), 0.0001);
        Assert.assertNull(parser.getBigIntegerValue());
        Assert.assertNull(parser.getDecimalValue());
        Assert.assertNull(parser.getNumberType());
        Assert.assertNull(parser.getNumberValue());
        parser.close();
    }

    @Test
    public void testTextPropertiesInitialState() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        Assert.assertFalse(parser.hasTextCharacters());
        Assert.assertNull(parser.getText());
        Assert.assertNull(parser.getTextCharacters());
        Assert.assertEquals(0, parser.getTextLength());
        Assert.assertEquals(0, parser.getTextOffset());
        Assert.assertNull(parser.getEmbeddedObject());
        parser.close();
    }

    @Test
    public void testCloseAndIsClosed() throws Exception {
        FromXmlParser parser = createParser("<root>data</root>");
        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
        // Duplicate close should be idempotent
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleParsingSequence() throws Exception {
        FromXmlParser parser = createParser("<root><item>Hello</item></root>");
        Assert.assertNull(parser.getCurrentToken());

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("item", parser.getCurrentName());
        Assert.assertEquals("item", parser.getText());

        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("Hello", parser.getText());
        Assert.assertEquals("Hello", parser.getValueAsString());
        Assert.assertEquals("Hello", new String(parser.getTextCharacters()));
        Assert.assertEquals(5, parser.getTextLength());

        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testGetLocations() throws Exception {
        FromXmlParser parser = createParser("<root>content</root>");
        JsonLocation tokenLoc = parser.getTokenLocation();
        JsonLocation currLoc = parser.getCurrentLocation();
        Assert.assertNotNull(tokenLoc);
        Assert.assertNotNull(currLoc);

        parser.nextToken(); // START_OBJECT
        Assert.assertNotNull(parser.getTokenLocation());
        Assert.assertNotNull(parser.getCurrentLocation());
        parser.close();
    }

    @Test
    public void testConvertExpectedStartArrayToken() throws Exception {
        FromXmlParser parser = createParser("<items><elem>1</elem></items>");
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        boolean isArray = parser.isExpectedStartArrayToken();
        Assert.assertTrue(isArray);
        Assert.assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());

        // Repeated check should still return true since current token is START_ARRAY
        Assert.assertTrue(parser.isExpectedStartArrayToken());
        parser.close();
    }

    @Test
    public void testIsExpectedStartArrayTokenFalseWhenNotStartObject() throws Exception {
        FromXmlParser parser = createParser("<root><val>abc</val></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        Assert.assertFalse(parser.isExpectedStartArrayToken());
        parser.close();
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        FromXmlParser parser = createParser("<root><first>value</first></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        Assert.assertEquals("first", parser.getCurrentName());

        parser.overrideCurrentName("overriddenName");
        Assert.assertEquals("overriddenName", parser.getCurrentName());
        Assert.assertEquals("overriddenName", parser.getText());
        parser.close();
    }

    @Test
    public void testOverrideCurrentNameOnStartObject() throws Exception {
        FromXmlParser parser = createParser("<root><child>value</child></root>");
        parser.nextToken(); // START_OBJECT
        parser.overrideCurrentName("newRoot");
        Assert.assertEquals("newRoot", parser.getCurrentName());
        parser.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentNameThrowsWhenNoName() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        parser.getCurrentName();
    }

    @Test
    public void testGetValueAsStringWithDefault() throws Exception {
        FromXmlParser parser = createParser("<root><num>text</num></root>");
        Assert.assertNull(parser.getValueAsString("default"));

        parser.nextToken(); // START_OBJECT
        Assert.assertEquals("default", parser.getValueAsString("default"));

        parser.nextToken(); // FIELD_NAME
        Assert.assertEquals("num", parser.getValueAsString("default"));

        parser.nextToken(); // VALUE_STRING
        Assert.assertEquals("text", parser.getValueAsString("default"));
        parser.close();
    }

    @Test
    public void testGetBinaryValueSuccess() throws Exception {
        // Base64 for "Hello World" is "SGVsbG8gV29ybGQ="
        FromXmlParser parser = createParser("<root><data>SGVsbG8gV29ybGQ=</data></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING

        byte[] binary = parser.getBinaryValue(Base64Variants.MIME);
        Assert.assertNotNull(binary);
        Assert.assertEquals("Hello World", new String(binary, "UTF-8"));

        // Subsequent call returns cached array
        byte[] cached = parser.getBinaryValue(Base64Variants.MIME);
        Assert.assertSame(binary, cached);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testGetBinaryValueNotOnValueStringThrows() throws Exception {
        FromXmlParser parser = createParser("<root><data>abc</data></root>");
        parser.nextToken(); // START_OBJECT
        parser.getBinaryValue(Base64Variants.MIME);
    }

    @Test(expected = JsonParseException.class)
    public void testGetBinaryValueCorruptedBase64Throws() throws Exception {
        FromXmlParser parser = createParser("<root><data>not valid base64!@#$</data></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        parser.getBinaryValue(Base64Variants.MIME);
    }

    @Test
    public void testNextTextValueDirect() throws Exception {
        FromXmlParser parser = createParser("<root><item>sample</item></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME

        String text = parser.nextTextValue();
        Assert.assertEquals("sample", text);
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        parser.close();
    }

    @Test
    public void testNextTextValueWithEmptyElement() throws Exception {
        FromXmlParser parser = createParser("<root><item/></root>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME

        String text = parser.nextTextValue();
        Assert.assertEquals("", text);
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        parser.close();
    }

    @Test
    public void testCustomXMLTextElementName() throws Exception {
        FromXmlParser parser = createParser("<root id='1'>TextInside</root>");
        parser.setXMLTextElementName("myCustomText");

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("id", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("1", parser.getText());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("myCustomText", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("TextInside", parser.getText());
        parser.close();
    }

    @Test
    public void testAddVirtualWrapping() throws Exception {
        FromXmlParser parser = createParser("<root><item>1</item><item>2</item></root>");
        Set<String> wrapNames = new HashSet<String>();
        wrapNames.add("item");
        parser.addVirtualWrapping(wrapNames);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("item", parser.getCurrentName());
        parser.close();
    }

    @Test
    public void testXmlParsingContext() throws Exception {
        FromXmlParser parser = createParser("<root><elem>val</elem></root>");
        XmlReadContext ctxt = parser.getParsingContext();
        Assert.assertNotNull(ctxt);
        Assert.assertTrue(ctxt.inRoot());

        parser.nextToken(); // START_OBJECT
        Assert.assertTrue(parser.getParsingContext().inObject());
        parser.close();
    }

    @Test
    public void testIsEmptyHelper() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        Assert.assertTrue(parser._isEmpty(null));
        Assert.assertTrue(parser._isEmpty(""));
        Assert.assertTrue(parser._isEmpty("   \t\r\n "));
        Assert.assertFalse(parser._isEmpty("a"));
        Assert.assertFalse(parser._isEmpty("  a  "));
        parser.close();
    }

    @Test
    public void testByteArrayBuilderLifecycle() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        Object b1 = parser._getByteArrayBuilder();
        Assert.assertNotNull(b1);
        Object b2 = parser._getByteArrayBuilder();
        Assert.assertSame(b1, b2);
        parser.close();
    }

    @Test
    public void testHandleEOFInRootDoesNotThrow() throws Exception {
        FromXmlParser parser = createParser("<root/>");
        // in root context, _handleEOF should not throw
        parser._handleEOF();
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testHandleEOFInNonRootThrows() throws Exception {
        FromXmlParser parser = createParser("<root><child>");
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        // in child object context, _handleEOF reports invalid EOF
        parser._handleEOF();
    }

    @Test
    public void testAttributesAsFieldAndValueTokens() throws Exception {
        FromXmlParser parser = createParser("<root attr=\"testValue\"/>");
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("attr", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("testValue", parser.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertNull(parser.nextToken());
        parser.close();
    }
}
```

### References
- Jackson XML format extension documentation: FasterXML `jackson-dataformat-xml` (https://github.com/FasterXML/jackson-dataformat-xml)
- Stax streaming API specifications (JSR 173): `javax.xml.stream.XMLStreamReader`
- Jackson Streaming parser API: `com.fasterxml.jackson.core.JsonParser`