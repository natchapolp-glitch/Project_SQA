package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        SerializerFactory factory = provider.getSerializerFactory();

        DefaultSerializerProvider newProvider = provider.createInstance(config, factory);
        assertNotNull(newProvider);
        assertSame(config, newProvider.getConfig());
    }

    @Test
    public void testAsXmlGeneratorWithTokenBuffer() throws Exception {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ObjectMapper mapper = new ObjectMapper();
        TokenBuffer tb = new TokenBuffer(mapper, false);

        ToXmlGenerator xgen = provider._asXmlGenerator(tb);
        assertNull(xgen);
    }

    @Test
    public void testAsXmlGeneratorWithInvalidGenerator() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.getFactory().createGenerator(new java.io.StringWriter());
            fail("Expected JsonMappingException");
        } catch (Exception e) {
            // expected generator creation or invalid generator test flow
        }
    }

    @Test
    public void testRootNameForNullConstant() {
        assertNotNull(XmlSerializerProvider.ROOT_NAME_FOR_NULL);
    }

    @Test
    public void testRootNameFromConfigWithNull() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        QName qname = provider._rootNameFromConfig();
        assertNull(qname);
    }
}
