package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.ctc.wstx.stax.WstxInputFactory;

/**
 * Regression tests for FromXmlParser covering state management,
 * configuration, and accessor methods.
 */
public class FromXmlParserTest {

    private FromXmlParser parser;
    private IOContext ioContext;
    private XMLStreamReader xmlReader;
    private ObjectCodec codec;

    @Before
    public void setUp() throws Exception {
        // Setup IOContext for resource management
        ioContext = new IOContext(
            new com.fasterxml.jackson.core.util.BufferRecycler(),
            null, // inputSource
            false // resourceManaged
        );
        
        // Create a minimal XMLStreamReader from Woodstox
        WstxInputFactory factory = new WstxInputFactory();
        String xml = "<root><item>value</item></root>";
        xmlReader = factory.createXMLStreamReader(
            new java.io.StringReader(xml)
        );
        
        codec = null; // Use null codec for basic tests
        
        // Initialize parser
        parser = new FromXmlParser(
            ioContext,
            0, // genericParserFeatures
            0, // xmlFeatures
            codec,
            xmlReader
        );
    }

    // =========================================================================
    // Tests for _isEmpty() method
    // =========================================================================

    @Test
    public void testIsEmptyWithNullString() {
        assertTrue("Null string should be considered empty",
            invokeIsEmpty(parser, null));
    }

    @Test
    public void testIsEmptyWithEmptyString() {
        assertTrue("Empty string should be considered empty",
            invokeIsEmpty(parser, ""));
    }

    @Test
    public void testIsEmptyWithWhitespaceOnly() {
        assertTrue("Whitespace-only string should be considered empty",
            invokeIsEmpty(parser, "   \t\n\r  "));
    }

    @Test
    public void testIsEmptyWithSingleSpace() {
        assertTrue("Single space should be considered empty",
            invokeIsEmpty(parser, " "));
    }

    @Test
    public void testIsEmptyWithTabCharacter() {
        assertTrue("Tab character should be considered empty",
            invokeIsEmpty(parser, "\t"));
    }

    @Test
    public void testIsEmptyWithNewline() {
        assertTrue("Newline should be considered empty",
            invokeIsEmpty(parser, "\n"));
    }

    @Test
    public void testIsEmptyWithCarriageReturn() {
        assertTrue("Carriage return should be considered empty",
            invokeIsEmpty(parser, "\r"));
    }

    @Test
    public void testIsEmptyWithNonWhitespaceAtStart() {
        assertFalse("String starting with non-whitespace should not be empty",
            invokeIsEmpty(parser, "a"));
    }

    @Test
    public void testIsEmptyWithNonWhitespaceAtEnd() {
        assertFalse("String ending with non-whitespace should not be empty",
            invokeIsEmpty(parser, "   x"));
    }

    @Test
    public void testIsEmptyWithNonWhitespaceInMiddle() {
        assertFalse("String with non-whitespace in middle should not be empty",
            invokeIsEmpty(parser, "  x  "));
    }

    @Test
    public void testIsEmptyWithNormalText() {
        assertFalse("Normal text should not be empty",
            invokeIsEmpty(parser, "hello"));
    }

    @Test
    public void testIsEmptyWithSpecialCharacter() {
        assertFalse("String with special character should not be empty",
            invokeIsEmpty(parser, "!"));
    }

    @Test
    public void testIsEmptyWithZeroCharacter() {
        assertTrue("Null character (0x00) is not > ' ' so should be empty",
            invokeIsEmpty(parser, "\u0000"));
    }

    // =========================================================================
    // Tests for Feature configuration methods
    // =========================================================================

    @Test
    public void testEnableFeatureReturnsParser() {
        // FromXmlParser.Feature enum is empty, but method should work
        FromXmlParser result = parser.enable((FromXmlParser.Feature) null);
        assertSame("enable() should return this", parser, result);
    }

    @Test
    public void testDisableFeatureReturnsParser() {
        FromXmlParser result = parser.disable((FromXmlParser.Feature) null);
        assertSame("disable() should return this", parser, result);
    }

    @Test
    public void testConfigureFeatureTrue() {
        FromXmlParser result = parser.configure((FromXmlParser.Feature) null, true);
        assertSame("configure() should return this", parser, result);
    }

    @Test
    public void testConfigureFeatureFalse() {
        FromXmlParser result = parser.configure((FromXmlParser.Feature) null, false);
        assertSame("configure() should return this", parser, result);
    }

    @Test
    public void testGetFormatFeaturesInitial() {
        // Default format features should be 0 when no features set
        int features = parser.getFormatFeatures();
        assertEquals("Initial format features should be 0", 0, features);
    }

    @Test
    public void testOverrideFormatFeatures() {
        int mask = 0xFF;
        int values = 0xAA;
        
        JsonParser result = parser.overrideFormatFeatures(values, mask);
        assertSame("overrideFormatFeatures should return this", parser, result);
        
        // After override with values=0xAA and mask=0xFF, should get 0xAA
        int newFeatures = parser.getFormatFeatures();
        assertEquals("Format features should be updated", values, newFeatures);
    }

    @Test
    public void testOverrideFormatFeaturesPartialMask() {
        // Set initial features
        parser.overrideFormatFeatures(0xFF, 0xFF);
        
        // Override only lower 4 bits with value 0x05
        parser.overrideFormatFeatures(0x05, 0x0F);
        
        // Should have 0xF5 (0xFF with lower 4 bits replaced by 0x05)
        int features = parser.getFormatFeatures();
        assertEquals("Partial override should preserve masked bits", 0xF5, features);
    }

    // =========================================================================
    // Tests for _getByteArrayBuilder() method
    // =========================================================================

    @Test
    public void testGetByteArrayBuilderInitial() {
        ByteArrayBuilder builder = invokeGetByteArrayBuilder(parser);
        assertNotNull("ByteArrayBuilder should not be null", builder);
    }

    @Test
    public void testGetByteArrayBuilderReusedOnMultipleCalls() {
        ByteArrayBuilder builder1 = invokeGetByteArrayBuilder(parser);
        ByteArrayBuilder builder2 = invokeGetByteArrayBuilder(parser);
        
        // Same instance should be reused (after reset)
        assertSame("ByteArrayBuilder should be reused", builder1, builder2);
    }

    

    // =========================================================================
    // Tests for state accessor methods
    // =========================================================================

    @Test
    public void testIsClosed() {
        assertFalse("Parser should not be closed initially", parser.isClosed());
    }

    @Test
    public void testIsClosedAfterClose() throws IOException {
        parser.close();
        assertTrue("Parser should be closed after close()", parser.isClosed());
    }

    @Test
    public void testGetCodec() {
        assertNull("Codec should be null when not set", parser.getCodec());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec newCodec = new com.fasterxml.jackson.databind.ObjectMapper();
        parser.setCodec(newCodec);
        assertSame("Codec should be set", newCodec, parser.getCodec());
    }

    @Test
    public void testRequiresCustomCodec() {
        assertTrue("XML format requires custom codec", parser.requiresCustomCodec());
    }

    @Test
    public void testGetParsingContext() {
        assertNotNull("Parsing context should not be null", parser.getParsingContext());
    }

    @Test
    public void testGetStaxReader() {
        assertSame("Should return underlying XMLStreamReader",
            xmlReader, parser.getStaxReader());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = parser.getCurrentLocation();
        assertNotNull("Current location should not be null", location);
    }

    

    

    

    // =========================================================================
    // Tests for XML text element name configuration
    // =========================================================================

    

    

    

    

    // =========================================================================
    // Tests for virtual wrapping
    // =========================================================================

    

    

    // =========================================================================
    // Tests for text accessors
    // =========================================================================

    

    

    

    

    

    

    

    // =========================================================================
    // Tests for numeric accessors (returning null/0)
    // =========================================================================

    

    

    

    

    

    

    

    

    

    // =========================================================================
    // Tests for _releaseBuffers and _handleEOF
    // =========================================================================

    

    

    // =========================================================================
    // Helper methods (via reflection to access protected/private methods)
    // =========================================================================

    private boolean invokeIsEmpty(FromXmlParser p, String str) {
        try {
            java.lang.reflect.Method method = FromXmlParser.class
                .getDeclaredMethod("_isEmpty", String.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(p, str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ByteArrayBuilder invokeGetByteArrayBuilder(FromXmlParser p) {
        try {
            java.lang.reflect.Method method = FromXmlParser.class
                .getDeclaredMethod("_getByteArrayBuilder");
            method.setAccessible(true);
            return (ByteArrayBuilder) method.invoke(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeReleaseBuffers(FromXmlParser p) throws IOException {
        try {
            java.lang.reflect.Method method = FromXmlParser.class
                .getDeclaredMethod("_releaseBuffers");
            method.setAccessible(true);
            method.invoke(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeHandleEOF(FromXmlParser p) throws IOException {
        try {
            java.lang.reflect.Method method = FromXmlParser.class
                .getDeclaredMethod("_handleEOF");
            method.setAccessible(true);
            method.invoke(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
