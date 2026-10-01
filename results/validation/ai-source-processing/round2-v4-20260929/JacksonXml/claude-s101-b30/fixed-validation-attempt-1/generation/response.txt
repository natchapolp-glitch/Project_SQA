JUnit 4 is assumed, since the parent POM (`jackson-parent` 2.7) supplies JUnit 4.12 and the `Test.java` surefire include matches. Two limits apply to the tests below:

- `FromXmlParser.Feature` has no constants, so `enable`, `disable`, `isEnabled` and `configure` cannot be called with a real `Feature`. They are not tested, but `overrideFormatFeatures` and `getFormatFeatures` are.
- `_updateState` is `private`, so it is covered indirectly through `nextTextValue()`.

The tests sit in the production package so the `protected` helpers (`_isEmpty`, `_decodeBase64`, `_getByteArrayBuilder`, `_handleEOF`, `_releaseBuffers`) are callable. Woodstox is on the test classpath, so the tests use `XmlFactory`/`XmlMapper` for stream setup. There are 29 test methods.

com/fasterxml/jackson/dataformat/xml/deser/GeneratedFromXmlParserTest.java

```java
package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.dataformat.xml.PackageVersion;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class GeneratedFromXmlParserTest
{
    private static final String SIMPLE = "<root><a>hello</a></root>";

    private FromXmlParser parser(String xml) throws IOException {
        return (FromXmlParser) new XmlFactory().createParser(xml);
    }

    // ---- life-cycle / simple accessors ----

    @Test
    public void testDirectConstructor() throws Exception {
        XMLStreamReader sr = XMLInputFactory.newInstance()
                .createXMLStreamReader(new StringReader(SIMPLE));
        IOContext ctxt = new IOContext(new BufferRecycler(), "src", false);
        FromXmlParser p = new FromXmlParser(ctxt, 0, 0, null, sr);
        assertNull(p.getCodec());
        assertEquals(0, p.getFormatFeatures());
        assertFalse(p.isClosed());
        assertTrue(p.getParsingContext().inRoot());
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("a", p.getCurrentName());
        assertSame(sr, p.getStaxReader());
        p.close();
    }

    @Test
    public void testVersion() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertEquals(PackageVersion.VERSION, p.version());
        p.close();
    }

    @Test
    public void testRequiresCustomCodecAndHasTextCharacters() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertTrue(p.requiresCustomCodec());
        assertFalse(p.hasTextCharacters());
        p.close();
    }

    @Test
    public void testCodecGetAndSet() throws Exception {
        XmlMapper mapper = new XmlMapper();
        FromXmlParser p = (FromXmlParser) mapper.getFactory().createParser(SIMPLE);
        assertSame(mapper, p.getCodec());
        p.setCodec(null);
        assertNull(p.getCodec());
        ObjectCodec other = new XmlMapper();
        p.setCodec(other);
        assertSame(other, p.getCodec());
        p.close();
    }

    @Test
    public void testFormatFeatureOverride() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertEquals(0, p.getFormatFeatures());
        assertSame(p, p.overrideFormatFeatures(5, 7));
        assertEquals(5, p.getFormatFeatures());
        assertSame(p, p.overrideFormatFeatures(0, 1));
        assertEquals(4, p.getFormatFeatures());
        // bits outside mask are untouched
        p.overrideFormatFeatures(0xFF, 0x0);
        assertEquals(4, p.getFormatFeatures());
        p.close();
    }

    @Test
    public void testCloseAndIsClosed() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertFalse(p.isClosed());
        p.close();
        assertTrue(p.isClosed());
        p.close(); // idempotent
        assertTrue(p.isClosed());
    }

    @Test
    public void testStaxReaderAndLocations() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNotNull(p.getStaxReader());
        assertSame(p.getStaxReader(), p.getStaxReader());
        assertNotNull(p.getCurrentLocation());
        assertNotNull(p.getTokenLocation());
        p.close();
    }

    @Test
    public void testParsingContextInitialAndAfterStart() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertTrue(p.getParsingContext().inRoot());
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertTrue(p.getParsingContext().inObject());
        p.close();
    }

    // ---- nextToken ----

    @Test
    public void testNextTokenSimpleSequence() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNull(p.getText());
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals("{", p.getText());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("a", p.getCurrentName());
        assertEquals("a", p.getText());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("hello", p.getText());
        assertEquals(JsonToken.END_OBJECT, p.nextToken());
        assertEquals("}", p.getText());
        assertNull(p.nextToken());
        assertNull(p.getText());
        p.close();
    }

    @Test
    public void testNextTokenEmptyElementIsNull() throws Exception {
        FromXmlParser p = parser("<root><a/></root>");
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals(JsonToken.VALUE_NULL, p.nextToken());
        assertEquals("null", p.getValueAsString());
        assertEquals(JsonToken.END_OBJECT, p.nextToken());
        p.close();
    }

    @Test
    public void testNextTokenRepeatedElementsWithoutWrapping() throws Exception {
        FromXmlParser p = parser("<root><a>1</a><a>2</a></root>");
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("1", p.getText());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("a", p.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("2", p.getText());
        assertEquals(JsonToken.END_OBJECT, p.nextToken());
        p.close();
    }

    @Test
    public void testAttributeAndTextElementName() throws Exception {
        FromXmlParser p = parser("<root><a x=\"1\">text</a></root>");
        p.setXMLTextElementName("value");
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("a", p.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("x", p.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("1", p.getText());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("value", p.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("text", p.getText());
        p.close();
    }

    @Test
    public void testAddVirtualWrapping() throws Exception {
        FromXmlParser p = parser("<root><item>a</item><item>b</item></root>");
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        Set<String> names = new HashSet<String>(Arrays.asList("item"));
        p.addVirtualWrapping(names);
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("item", p.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("item", p.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("a", p.getText());
        p.close();
    }

    // ---- isExpectedStartArrayToken ----

    @Test
    public void testIsExpectedStartArrayTokenConvertsObject() throws Exception {
        FromXmlParser p = parser("<root><a>1</a><a>2</a></root>");
        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertTrue(p.isExpectedStartArrayToken());
        assertEquals(JsonToken.START_ARRAY, p.getCurrentToken());
        assertTrue(p.getParsingContext().inArray());
        assertTrue(p.isExpectedStartArrayToken()); // already START_ARRAY
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("1", p.getText());
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        assertEquals("2", p.getText());
        assertEquals(JsonToken.END_ARRAY, p.nextToken());
        p.close();
    }

    @Test
    public void testIsExpectedStartArrayTokenFalseForOtherTokens() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertFalse(p.isExpectedStartArrayToken()); // no token yet
        p.nextToken();
        p.nextToken(); // FIELD_NAME
        assertFalse(p.isExpectedStartArrayToken());
        p.close();
    }

    // ---- nextTextValue / _updateState ----

    @Test
    public void testNextTextValueSequence() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNull(p.nextTextValue());
        assertEquals(JsonToken.START_OBJECT, p.getCurrentToken());
        assertNull(p.nextTextValue());
        assertEquals(JsonToken.FIELD_NAME, p.getCurrentToken());
        assertEquals("hello", p.nextTextValue());
        assertEquals(JsonToken.VALUE_STRING, p.getCurrentToken());
        assertNull(p.nextTextValue());
        assertEquals(JsonToken.END_OBJECT, p.getCurrentToken());
        assertNull(p.nextTextValue());
        assertNull(p.getCurrentToken());
        p.close();
    }

    @Test
    public void testNextTextValueEmptyElementYieldsEmptyString() throws Exception {
        FromXmlParser p = parser("<root><a/></root>");
        p.nextTextValue();
        p.nextTextValue();
        assertEquals("", p.nextTextValue());
        assertEquals(JsonToken.VALUE_STRING, p.getCurrentToken());
        p.close();
    }

    @Test
    public void testNextTextValueUpdatesStateForPendingFieldName() throws Exception {
        FromXmlParser p = parser("<root><a x=\"1\"/></root>");
        assertNull(p.nextTextValue()); // START_OBJECT (root)
        assertNull(p.nextTextValue()); // FIELD_NAME a
        assertNull(p.nextTextValue()); // START_OBJECT (attributes)
        assertEquals(JsonToken.START_OBJECT, p.getCurrentToken());
        assertNull(p.nextTextValue()); // pending FIELD_NAME via _updateState
        assertEquals(JsonToken.FIELD_NAME, p.getCurrentToken());
        assertEquals("x", p.getCurrentName());
        p.close();
    }

    // ---- getCurrentName / overrideCurrentName ----

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentNameMissingThrows() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        p.nextToken(); // START_OBJECT with unnamed root parent
        p.getCurrentName();
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        p.nextToken(); // START_OBJECT
        p.overrideCurrentName("x");
        assertEquals("x", p.getCurrentName());
        p.nextToken(); // FIELD_NAME a
        assertEquals("a", p.getCurrentName());
        p.overrideCurrentName("b");
        assertEquals("b", p.getCurrentName());
        p.close();
    }

    // ---- text accessors ----

    @Test
    public void testTextCharactersLengthOffset() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNull(p.getTextCharacters());
        assertEquals(0, p.getTextLength());
        assertEquals(0, p.getTextOffset());
        p.nextToken();
        p.nextToken();
        p.nextToken(); // VALUE_STRING "hello"
        assertArrayEquals("hello".toCharArray(), p.getTextCharacters());
        assertEquals(5, p.getTextLength());
        assertEquals(0, p.getTextOffset());
        p.close();
    }

    @Test
    public void testGetValueAsString() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNull(p.getValueAsString());
        assertNull(p.getValueAsString("def"));
        p.nextToken();
        p.nextToken(); // FIELD_NAME
        assertEquals("a", p.getValueAsString());
        assertEquals("a", p.getValueAsString("def"));
        p.nextToken(); // VALUE_STRING
        assertEquals("hello", p.getValueAsString());
        assertEquals("hello", p.getValueAsString("def"));
        p.nextToken(); // END_OBJECT
        assertEquals("def", p.getValueAsString("def"));
        p.close();
    }

    @Test
    public void testEmbeddedObjectAndNumericStubs() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertNull(p.getEmbeddedObject());
        assertEquals(0, p.getIntValue());
        assertEquals(0L, p.getLongValue());
        assertEquals(0.0, p.getDoubleValue(), 0.0);
        assertEquals(0.0f, p.getFloatValue(), 0.0f);
        assertNull(p.getBigIntegerValue());
        assertNull(p.getDecimalValue());
        assertNull(p.getNumberType());
        assertNull(p.getNumberValue());
        p.close();
    }

    // ---- binary ----

    @Test
    public void testGetBinaryValueDecodesAndCaches() throws Exception {
        FromXmlParser p = parser("<root><a>SGVsbG8=</a></root>");
        p.nextToken();
        p.nextToken();
        assertEquals(JsonToken.VALUE_STRING, p.nextToken());
        byte[] b = p.getBinaryValue(Base64Variants.getDefaultVariant());
        assertArrayEquals("Hello".getBytes("UTF-8"), b);
        assertSame(b, p.getBinaryValue(Base64Variants.getDefaultVariant()));
        p.close();
    }

    @Test
    public void testGetBinaryValueInvalidBase64() throws Exception {
        FromXmlParser p = parser("<root><a>!!!!</a></root>");
        p.nextToken();
        p.nextToken();
        p.nextToken();
        try {
            p.getBinaryValue(Base64Variants.getDefaultVariant());
            fail("Should not pass");
        } catch (JsonParseException e) {
            assertNotNull(e.getMessage());
        }
        p.close();
    }

    @Test
    public void testGetBinaryValueWrongToken() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        p.nextToken();
        p.nextToken(); // FIELD_NAME
        try {
            p.getBinaryValue(Base64Variants.getDefaultVariant());
            fail("Should not pass");
        } catch (JsonParseException e) {
            assertTrue(e.getMessage().contains("VALUE_STRING"));
        }
        p.close();
    }

    @Test
    public void testDecodeBase64Internal() throws Exception {
        FromXmlParser p = parser("<root><a>AQID</a></root>");
        p.nextToken();
        p.nextToken();
        p.nextToken();
        assertArrayEquals(new byte[] { 1, 2, 3 },
                p._decodeBase64(Base64Variants.getDefaultVariant()));
        p.close();
    }

    // ---- internal helpers ----

    @Test
    public void testGetByteArrayBuilderReused() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        ByteArrayBuilder b1 = p._getByteArrayBuilder();
        assertNotNull(b1);
        b1.append(7);
        ByteArrayBuilder b2 = p._getByteArrayBuilder();
        assertSame(b1, b2);
        assertEquals(0, b2.toByteArray().length); // reset on reuse
        p.close();
    }

    @Test
    public void testIsEmpty() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        assertTrue(p._isEmpty(null));
        assertTrue(p._isEmpty(""));
        assertTrue(p._isEmpty(" \t\r\n"));
        assertFalse(p._isEmpty("a"));
        assertFalse(p._isEmpty("  a  "));
        p.close();
    }

    @Test
    public void testHandleEOF() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        p._handleEOF(); // root context: legitimate EOF, no exception
        p.nextToken(); // now inside an Object context
        try {
            p._handleEOF();
            fail("Should not pass");
        } catch (JsonParseException e) {
            assertTrue(e.getMessage().contains("end-of-input"));
        }
        p.close();
    }

    @Test
    public void testReleaseBuffersNoFailure() throws Exception {
        FromXmlParser p = parser(SIMPLE);
        p._releaseBuffers();
        assertFalse(p.isClosed());
        p.close();
    }
}
```

The riskiest assertions are the ones that depend on `XmlTokenStream`, which wasn't supplied:

- The first-token sequence assumes the root element is consumed at start-up, so `<root><a>x</a></root>` yields `START_OBJECT`, `FIELD_NAME a`, and so on.
- `testAddVirtualWrapping` assumes a specific token order for repeated start elements.
- The location tests only check for non-null values.

If any of these fail on revision 1f, check those assumptions first.