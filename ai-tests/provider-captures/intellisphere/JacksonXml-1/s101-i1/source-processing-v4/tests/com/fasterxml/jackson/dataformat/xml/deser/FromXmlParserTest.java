package com.fasterxml.jackson.dataformat.xml.deser;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Regression tests for {@link FromXmlParser}.
 */
public class FromXmlParserTest
{
    private XMLStreamReader createXmlReader(String xml) throws Exception {
        XMLInputFactory f = XMLInputFactory.newInstance();
        f.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        return f.createXMLStreamReader(new StringReader(xml));
    }

    private FromXmlParser newParser(String xml) throws Exception {
        return newParser(xml, null);
    }

    private FromXmlParser newParser(String xml, ObjectCodec codec) throws Exception {
        IOContext ctxt = new IOContext(new BufferRecycler(), xml, false);
        return new FromXmlParser(ctxt, 0, 0, codec, createXmlReader(xml));
    }

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    @Test
    public void testFeatureCollectDefaultsReturnsZero() {
        assertEquals(0, FromXmlParser.Feature.collectDefaults());
    }

    

    

    

    

    

    

    
}
