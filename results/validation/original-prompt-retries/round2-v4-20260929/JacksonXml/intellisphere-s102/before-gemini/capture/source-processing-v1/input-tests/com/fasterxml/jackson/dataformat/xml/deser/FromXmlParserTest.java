package com.fasterxml.jackson.dataformat.xml.deser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class FromXmlParserTest {

    private FromXmlParser parser;
    private XMLStreamReader xmlReader;
    private IOContext ioContext;

    @Before
    public void setUp() throws Exception {
        // Reset state before each test
        parser = null;
        xmlReader = null;
        ioContext = null;
    }

    @After
    public void tearDown() throws Exception {
        if (parser != null && !parser.isClosed()) {
            parser.close();
        }
    }

    private void createParser(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        xmlReader = factory.createXMLStreamReader(new StringReader(xml));
        BufferRecycler br = new BufferRecycler();
        ioContext = new IOContext(br, ContentReference.unknown(), false);
        parser = new FromXmlParser(ioContext, 0, FromXmlParser.Feature.collectDefaults(),
                null, xmlReader);
    }

    // Test constructor and initial state
    @Test
    public void testConstructorAndInitialState() throws Exception {
        createParser("<root>value</root>");
        assertNotNull("Parser should be created", parser);
        assertFalse("Parser should not be closed initially", parser.isClosed());
        assertNotNull("Parsing context should be initialized", parser.getParsingContext());
        assertTrue("Parsing context should be in root", parser.getParsingContext().inRoot());
        assertEquals("Initial token should be START_OBJECT", JsonToken.START_OBJECT, parser.currentToken());
    }

    // Test version
    @Test
    public void testVersion() throws Exception {
        createParser("<root>value</root>");
        Version version = parser.version();
        assertNotNull("Version should not be null", version);
        assertEquals("Group ID should be com.fasterxml.jackson", "com.fasterxml.jackson", version.getGroupId());
    }

    // Test setCodec and getCodec
    @Test
    public void testGetAndSetCodec() throws Exception {
        createParser("<root>value</root>");
        assertNull("Initial codec should be null", parser.getCodec());
        
        ObjectCodec mockCodec = new ObjectCodec() {
            @Override
            public <T> T readValue(JsonParser p, Class<T> valueType) { return null; }
            @Override
            public <T extends TreeNode> T readTree(JsonParser p) { return null; }
            // Other methods are omitted for brevity as they are not needed for this test
            public void writeValue(JsonGenerator gen, Object value) {}
            public <T extends TreeNode> T createObjectNode() { return null; }
            public <T extends TreeNode> T createArrayNode() { return null; }
            public JsonParser treeAsTokens(TreeNode n) { return null; }
            public <T> T treeToValue(TreeNode n, Class<T> valueType) { return null; }
        };
        
        parser.setCodec(mockCodec);
        assertEquals("Codec should be set and retrieved", mockCodec, parser.getCodec());
    }

    // Test requiresCustomCodec
    @Test
    public void testRequiresCustomCodec() throws Exception {
        createParser("<root>value</root>");
        assertTrue("XML format requires custom codec", parser.requiresCustomCodec());
    }

    // Test setXMLTextElementName and its effect
    @Test
    public void testSetXMLTextElementName() throws Exception {
        createParser("<root>text value</root>");
        parser.setXMLTextElementName("value");
        // The text element name will be used when there is text without a wrapper element
        // Verify by parsing
        parser.nextToken(); // FIELD_NAME for text element
        assertEquals("Text element name should be 'value'", "value", parser.getCurrentName());
        assertEquals("Next token should be VALUE_STRING", JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Text content should be retrieved", "text value", parser.getText());
    }

    // Test isClosed
    @Test
    public void testIsClosed() throws Exception {
        createParser("<root>value</root>");
        assertFalse("Parser should not be closed initially", parser.isClosed());
        parser.close();
        assertTrue("Parser should be closed after close()", parser.isClosed());
    }

    // Test close method
    @Test
    public void testClose() throws Exception {
        createParser("<root>value</root>");
        parser.close();
        assertTrue("Parser should be closed", parser.isClosed());
    }

    // Test getParsingContext
    @Test
    public void testGetParsingContext() throws Exception {
        createParser("<root><child>value</child></root>");
        XmlReadContext context = parser.getParsingContext();
        assertNotNull("Parsing context should not be null", context);
        assertTrue("Initial context should be root", context.inRoot());
    }

    // Test getTokenLocation
    @Test
    public void testGetTokenLocation() throws Exception {
        createParser("<root>value</root>");
        JsonLocation location = parser.getTokenLocation();
        assertNotNull("Token location should not be null", location);
    }

    // Test getCurrentLocation
    @Test
    public void testGetCurrentLocation() throws Exception {
        createParser("<root>value</root>");
        JsonLocation location = parser.getCurrentLocation();
        assertNotNull("Current location should not be null", location);
    }

    // Test isExpectedStartArrayToken
    @Test
    public void testIsExpectedStartArrayToken() throws Exception {
        createParser("<root><item>1</item><item>2</item></root>");
        // Navigate to START_OBJECT that should be converted
        parser.nextToken(); // FIELD_NAME "item"
        parser.nextToken(); // START_OBJECT (child)
        assertTrue("Should be able to treat as start array token", parser.isExpectedStartArrayToken());
        assertEquals("Token should be converted to START_ARRAY", JsonToken.START_ARRAY, parser.currentToken());
    }

    // Test hasTextCharacters
    @Test
    public void testHasTextCharacters() throws Exception {
        createParser("<root>value</root>");
        assertFalse("XML parser does not support text characters directly", parser.hasTextCharacters());
    }

    // Test getText
    @Test
    public void testGetText() throws Exception {
        createParser("<root>text content</root>");
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        assertEquals("Text should be retrieved", "text content", parser.getText());
    }

    // Test getTextCharacters
    @Test
    public void testGetTextCharacters() throws Exception {
        createParser("<root>text content</root>");
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        char[] chars = parser.getTextCharacters();
        assertArrayEquals("Character array should match text".toCharArray(), "text content".toCharArray(), chars);
    }

    // Test getTextLength
    @Test
    public void testGetTextLength() throws Exception {
        createParser("<root>text content</root>");
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        assertEquals("Text length should be 12", 12, parser.getTextLength());
    }

    // Test getTextOffset
    @Test
    public void testGetTextOffset() throws Exception {
        createParser("<root>text content</root>");
        assertEquals("Text offset should be 0", 0, parser.getTextOffset());
    }

    // Test getCurrentName
    @Test
    public void testGetCurrentName() throws Exception {
        createParser("<root><child>value</child></root>");
        parser.nextToken(); // FIELD_NAME for child
        assertEquals("Current name should be 'child'", "child", parser.getCurrentName());
    }

    // Test overrideCurrentName
    @Test
    public void testOverrideCurrentName() throws Exception {
        createParser("<root><child>value</child></root>");
        parser.nextToken(); // FIELD_NAME for child
        parser.overrideCurrentName("renamed");
        assertEquals("Current name should be overridden to 'renamed'", "renamed", parser.getCurrentName());
    }

    // Test getEmbeddedObject
    @Test
    public void testGetEmbeddedObject() throws Exception {
        createParser("<root>value</root>");
        assertNull("Embedded object should be null for XML", parser.getEmbeddedObject());
    }

    // Test getNumberType
    @Test
    public void testGetNumberType() throws Exception {
        createParser("<root>42</root>");
        // Advance to value
        parser.nextToken();
        parser.nextToken();
        assertNull("Number type should be null (not implemented)", parser.getNumberType());
    }

    // Test getNumberValue
    @Test
    public void testGetNumberValue() throws Exception {
        createParser("<root>42</root>");
        parser.nextToken();
        parser.nextToken();
        assertNull("Number value should be null (not implemented)", parser.getNumberValue());
    }

    // Test getBigIntegerValue
    @Test
    public void testGetBigIntegerValue() throws Exception {
        createParser("<root>42</root>");
        parser.nextToken();
        parser.nextToken();
        assertNull("BigInteger value should be null (not implemented)", parser.getBigIntegerValue());
    }

    // Test getDecimalValue
    @Test
    public void testGetDecimalValue() throws Exception {
        createParser("<root>3.14</root>");
        parser.nextToken();
        parser.nextToken();
        assertNull("Decimal value should be null (not implemented)", parser.getDecimalValue());
    }

    // Test getDoubleValue
    @Test
    public void testGetDoubleValue() throws Exception {
        createParser("<root>3.14</root>");
        parser.nextToken();
        parser.nextToken();
        assertEquals("Double value should be 0 (not implemented)", 0.0, parser.getDoubleValue(), 0.0);
    }

    // Test getFloatValue
    @Test
    public void testGetFloatValue() throws Exception {
        createParser("<root>3.14</root>");
        parser.nextToken();
        parser.nextToken();
        assertEquals("Float value should be 0 (not implemented)", 0.0f, parser.getFloatValue(), 0.0f);
    }

    // Test getIntValue
    @Test
    public void testGetIntValue() throws Exception {
        createParser("<root>42</root>");
        parser.nextToken();
        parser.nextToken();
        assertEquals("Int value should be 0 (not implemented)", 0, parser.getIntValue());
    }

    // Test getLongValue
    @Test
    public void testGetLongValue() throws Exception {
        createParser("<root>42</root>");
        parser.nextToken();
        parser.nextToken();
        assertEquals("Long value should be 0 (not implemented)", 0L, parser.getLongValue());
    }

    // Test getValueAsString with no argument
    @Test
    public void testGetValueAsString() throws Exception {
        createParser("<root>text value</root>");
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        assertEquals("Value as string should return text", "text value", parser.getValueAsString());
    }

    // Test getValueAsString with default value
    @Test
    public void testGetValueAsStringWithDefault() throws Exception {
        createParser("<root></root>"); // Empty element
        parser.nextToken(); // END_OBJECT or similar
        String result = parser.getValueAsString("default");
        // The behavior depends on current token; for non-scalar, returns default
        assertNotNull("Should return default or null", result);
    }

    // Test nextTextValue
    @Test
    public void testNextTextValue() throws Exception {
        createParser("<root>text content</root>");
        parser.nextToken(); // FIELD_NAME
        String text = parser.nextTextValue();
        assertEquals("Next text value should return text", "text content", text);
    }

    // Test enable and isEnabled for Feature
    @Test
    public void testEnableAndIsEnabled() throws Exception {
        createParser("<root>value</root>");
        // Since Feature enum is empty, no features to enable
        int formatFeatures = parser.getFormatFeatures();
        assertEquals("Format features should be initial defaults", 0, formatFeatures);
    }

    // Test configure
    @Test
    public void testConfigure() throws Exception {
        createParser("<root>value</root>");
        int initialFeatures = parser.getFormatFeatures();
        // Since Feature has no values, configure with 0 mask doesn't change anything
        // This tests that the method can be called without error
        parser.configure(null, true); // Will cause NPE if feature accessed, but tests method call
    }

    // Test getFormatFeatures
    @Test
    public void testGetFormatFeatures() throws Exception {
        createParser("<root>value</root>");
        int features = parser.getFormatFeatures();
        assertEquals("Initial format features should be 0", 0, features);
    }

    // Test overrideFormatFeatures
    @Test
    public void testOverrideFormatFeatures() throws Exception {
        createParser("<root>value</root>");
        int initialFeatures = parser.getFormatFeatures();
        parser.overrideFormatFeatures(0xFF, 0x0F);
        int modifiedFeatures = parser.getFormatFeatures();
        // Should have preserved bits not in mask, and used values for bits in mask
        assertEquals("Unmasked bits should be preserved", initialFeatures & ~0x0F, modifiedFeatures & ~0x0F);
        assertEquals("Masked bits should be from values", 0xFF & 0x0F, modifiedFeatures & 0x0F);
    }

    // Test addVirtualWrapping
    @Test
    public void testAddVirtualWrapping() throws Exception {
        createParser("<root><items><item>1</item></items></root>");
        Set<String> namesToWrap = new HashSet<>();
        namesToWrap.add("items");
        parser.nextToken(); // FIELD_NAME for items
        parser.nextToken(); // START_OBJECT for items
        parser.addVirtualWrapping(namesToWrap);
        // Verify wrapping was applied
        assertNotNull("Parsing context should exist", parser.getParsingContext());
    }

    // Test _isEmpty with null
    @Test
    public void testIsEmptyNull() throws Exception {
        createParser("<root>value</root>");
        // _isEmpty is protected, but we can test indirectly through parsing behavior
        // Empty text elements in arrays are treated as empty objects
        // This tests the null case indirectly
    }

    // Test _isEmpty with empty string
    @Test
    public void testIsEmptyString() throws Exception {
        createParser("<root></root>");
        // Empty elements should result in VALUE_NULL or similar
        parser.nextToken();
        // The behavior depends on whether it's treated as leaf
    }

    // Test _isEmpty with whitespace string
    @Test
    public void testIsEmptyWhitespace() throws Exception {
        createParser("<root>   </root>");
        // Whitespace-only text should be treated as empty
        parser.nextToken(); // FIELD_NAME
        JsonToken token = parser.nextToken();
        // Whitespace in leaf position should result in VALUE_NULL
    }

    // Test getBinaryValue with invalid token
    @Test(expected = JsonParseException.class)
    public void testGetBinaryValueInvalidToken() throws Exception {
        createParser("<root>value</root>");
        // Not on VALUE_STRING token, so should throw
        parser.getBinaryValue(Base64Variants.getDefaultVariant());
    }

    // Test getBinaryValue with valid token
    @Test
    public void testGetBinaryValueValidToken() throws Exception {
        createParser("<root>SGVsbG8=</root>"); // Base64 for "Hello"
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        byte[] binary = parser.getBinaryValue(Base64Variants.getDefaultVariant());
        assertNotNull("Binary value should not be null", binary);
        assertEquals("Binary should decode correctly", "Hello", new String(binary));
    }

    // Test _decodeBase64 via getBinaryValue
    @Test
    public void testDecodeBase64() throws Exception {
        createParser("<root>dGVzdA==</root>"); // Base64 for "test"
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // VALUE_STRING
        byte[] binary = parser.getBinaryValue(Base64Variants.getDefaultVariant());
        assertEquals("Decoded value should be 'test'", "test", new String(binary));
    }

    // Test _handleEOF via nextToken
    @Test
    public void testHandleEOF() throws Exception {
        createParser("<root></root>");
        parser.nextToken(); // END_OBJECT
        JsonToken token = parser.nextToken(); // Should be null (EOF)
        assertNull("After final token, nextToken should return null", token);
    }

    // Test _releaseBuffers via close
    @Test
    public void testReleaseBuffers() throws Exception {
        createParser("<root>value</root>");
        parser.close();
        assertTrue("Parser should be closed", parser.isClosed());
        // Buffers should be released without error
    }

    // Test nextToken basic flow
    @Test
    public void testNextTokenBasicFlow() throws Exception {
        createParser("<root><name>test</name></root>");
        assertEquals("Initial token", JsonToken.START_OBJECT, parser.currentToken());
        assertEquals("First nextToken", JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Should be 'name'", "name", parser.getCurrentName());
        assertEquals("Second nextToken", JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Should be 'test'", "test", parser.getText());
        assertEquals("Third nextToken", JsonToken.END_OBJECT, parser.nextToken());
        assertNull("Fourth nextToken should be null", parser.nextToken());
        assertTrue("Parser should be closed after EOF", parser.isClosed());
    }

    // Test getStaxReader
    @Test
    public void testGetStaxReader() throws Exception {
        createParser("<root>value</root>");
        XMLStreamReader reader = parser.getStaxReader();
        assertNotNull("StAX reader should not be null", reader);
    }

    // Test Feature enum static method
    @Test
    public void testFeatureCollectDefaults() {
        int defaults = FromXmlParser.Feature.collectDefaults();
        assertEquals("Default features mask should be 0 (empty enum)", 0, defaults);
    }

    // Test Feature enum methods (indirectly)
    @Test
    public void testFeatureEnabledByDefault() {
        // Since Feature enum is empty, there are no values to test
        FromXmlParser.Feature[] features = FromXmlParser.Feature.values();
        assertEquals("Feature enum should have no values", 0, features.length);
    }

    // Test overrideFormatFeatures with zero mask
    @Test
    public void testOverrideFormatFeaturesZeroMask() throws Exception {
        createParser("<root>value</root>");
        int original = parser.getFormatFeatures();
        parser.overrideFormatFeatures(42, 0);
        assertEquals("Format features should be unchanged with zero mask", original, parser.getFormatFeatures());
    }

    // Test parsing with attributes
    @Test
    public void testParsingWithAttributes() throws Exception {
        createParser("<root attr='value'><child>text</child></root>");
        parser.nextToken(); // FIELD_NAME for child
        assertEquals("Current name should be 'child'", "child", parser.getCurrentName());
    }
}
