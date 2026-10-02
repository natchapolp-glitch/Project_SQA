Generate a JUnit 4 Java test suite using only the supplied fixed source and build context.
Project: JacksonXml; fixed revision: 1f.
Candidate target classes:
com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser

Return Java source only, with explicit package declarations and JUnit 4 imports. Create at most 30 @Test methods in source order, with deterministic inputs and meaningful assertions derived from the fixed API behavior. Avoid external services, clock dependence, randomness, empty tests, and assertions that merely check non-null. Do not modify production code or create shadow production classes. Preserve all assertions; no feedback or repair loop.

Use the provided candidate classes; target eligibility and fixtures are specified by the shared adapter contract before generation.

## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jackson-dataformat-xml" default="package" basedir=".">

  <!-- ====================================================================== -->
  <!-- Import maven-build.xml into the current project                        -->
  <!-- ====================================================================== -->

  <import file="maven-build.xml"/>
  
  <!-- ====================================================================== -->
  <!-- Help target                                                            -->
  <!-- ====================================================================== -->

  <target name="help">
    <echo message="Please run: $ant -projecthelp"/>
  </target>

  <target name="compile" depends="jackson-dataformat-xml-from-maven.compile"> </target>
  <target name="compile.tests" depends="jackson-dataformat-xml-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion> 
  <parent>
    <groupId>com.fasterxml.jackson</groupId>
    <artifactId>jackson-parent</artifactId>
    <version>2.7</version>
  </parent>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.7.0-rc3</version>
  <name>Jackson-dataformat-XML</name>
  <packaging>bundle</packaging>
  <description>Data format extension for Jackson (http://jackson.codehaus.org) to offer
alternative support for serializing POJOs as XML and deserializing XML as pojos.
Support implemented on top of Stax API (javax.xml.stream), by implementing core Jackson Streaming API types like JsonGenerator, JsonParser and JsonFactory.
Some data-binding types overridden as well (ObjectMapper sub-classed as XmlMapper).
  </description>
  <url>http://wiki.fasterxml.com/JacksonExtensionXmlDataBinding</url>
  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-dataformat-xml.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-dataformat-xml.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-dataformat-xml</url>    
    <tag>HEAD</tag>
  </scm>
  <properties>
    <version.jackson.annotations>2.7.0-rc3</version.jackson.annotations>
    <version.jackson.core>2.7.0-rc3</version.jackson.core>
    <version.jackson.jaxb>2.7.0-rc3</version.jackson.jaxb>
    <packageVersion.dir>com/fasterxml/jackson/dataformat/xml</packageVersion.dir>
    <packageVersion.package>${project.groupId}.xml</packageVersion.package>

    <!-- Default export should work fine... -->
    <!-- And presumably import too? -->
  </properties>

  <dependencies>
    <!-- Extends Jackson (jackson-mapper); requires Stax API (and implementation on deploy time), Stax2 API.
         Also requires JAXB annotations module
      -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>${version.jackson.core}</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>${version.jackson.annotations}</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>${version.jackson.core}</version>
    </dependency>
    <!--  JAXB annotation introspector is needed too? -->
    <dependency>
      <groupId>com.fasterxml.jackson.module</groupId>
      <artifactId>jackson-module-jaxb-annotations</artifactId>
      <version>${version.jackson.jaxb}</version>
    </dependency>
    <!-- JDK 1.6 provides stax-api (javax.xml.stream), but let's add this for documentation
      -->
    <dependency>
      <groupId>javax.xml.stream</groupId>
      <artifactId>stax-api</artifactId>
      <version>1.0-2</version>
      <scope>provided</scope>
    </dependency>
    <!--  But Stax2 API must be included -->
    <dependency>
      <groupId>org.codehaus.woodstox</groupId>
      <artifactId>stax2-api</artifactId>
      <!-- 01-Jan-2010, Stupid Maven Release plug-in mandates a non-range
            version
        -->
<!--
      <version>[3.0.4, 3.5.0)</version>
-->
      <version>3.1.4</version>
    </dependency>
    <!--  and a Stax impl is needed: SJSXP (from JDK 1.6) might work, but always   
          has odd issues. Let's default to Woodstox: caller can upgrade to Aalto
         (needs to block this dep)
      -->
    <dependency>
      <groupId>com.fasterxml.woodstox</groupId>
      <artifactId>woodstox-core</artifactId>
      <version>5.0.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <!-- Inherited from oss-base. Generate PackageVersion.java.-->
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
        <executions>
          <execution>
            <id>process-packageVersion</id>
            <phase>generate-sources</phase>
          </execution>
        </executions>
      </plugin>
      <!--  Need to skip known-failing tests for build... -->
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>${version.plugin.surefire}</version>
          <configuration>
            <excludes>
              <exclude>com/fasterxml/jackson/dataformat/xml/failing/*.java</exclude>
            </excludes>
            <includes>
              <include>**/Test*.java</include>
              <include>**/*Test.java</include>
            </includes>
          </configuration>
        </plugin>
    </plugins>
  </build>

  <profiles>
    <!--  19-Feb-2012, tatu: Since we have some failing tests, may need to force release -->
    <profile>
      <id>force-release</id>
      <properties>
        <maven.test.skip>true</maven.test.skip>
        <skipTests>true</skipTests>
      </properties>
    </profile>
  </profiles>
  <!-- NOTE: repositories from parent POM -->

</project>

```

## src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java

```
package com.fasterxml.jackson.dataformat.xml.deser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.dataformat.xml.PackageVersion;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * {@link JsonParser} implementation that exposes XML structure as
 * set of JSON events that can be used for data binding.
 */
public class FromXmlParser
    extends ParserMinimalBase
{
    /**
     * The default name placeholder for XML text segments is empty
     * String ("").
     */
    public final static String DEFAULT_UNNAMED_TEXT_PROPERTY = "";

    /**
     * Enumeration that defines all togglable features for XML parsers.
     * None defined so far (2.6), so just a placeholder.
     */
    public enum Feature implements FormatFeature
    {
        ;

        final boolean _defaultState;
        final int _mask;
        
        /**
         * Method that calculates bit set (flags) of all features that
         * are enabled by default.
         */
        public static int collectDefaults()
        {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }
        
        private Feature(boolean defaultState) {
            _defaultState = defaultState;
            _mask = (1 << ordinal());
        }

        @Override public boolean enabledByDefault() { return _defaultState; }
        @Override public int getMask() { return _mask; }
        @Override public boolean enabledIn(int flags) { return (flags & getMask()) != 0; }
    }

    /**
     * In cases where a start element has both attributes and non-empty textual
     * value, we have to create a bogus property; we will use this as
     * the property name.
     *<p>
     * Name used for pseudo-property used for returning XML Text value (which does
     * not have actual element name to use). Defaults to empty String, but
     * may be changed for interoperability reasons: JAXB, for example, uses
     * "value" as name.
     * 
     * @since 2.1
     */
    protected String _cfgNameForTextElement = DEFAULT_UNNAMED_TEXT_PROPERTY;

    /*
    /**********************************************************
    /* Configuration
    /**********************************************************
     */

    /**
     * Bit flag composed of bits that indicate which
     * {@link FromXmlParser.Feature}s
     * are enabled.
     */
    protected int _formatFeatures;

    protected ObjectCodec _objectCodec;

    /*
    /**********************************************************
    /* I/O state
    /**********************************************************
     */

    /**
     * Flag that indicates whether parser is closed or not. Gets
     * set when parser is either closed by explicit call
     * ({@link #close}) or when end-of-input is reached.
     */
    protected boolean _closed;

    final protected IOContext _ioContext;

    /*
    /**********************************************************
    /* Parsing state
    /**********************************************************
     */

    /**
     * Information about parser context, context in which
     * the next token is to be parsed (root, array, object).
     */
    protected XmlReadContext _parsingContext;

    protected final XmlTokenStream _xmlTokens;
    /**
     * 
     * We need special handling to keep track of whether a value
     * may be exposed as simple leaf value.
     */
    protected boolean _mayBeLeaf;

    protected JsonToken _nextToken;

    protected String _currText;

    protected Set<String> _namesToWrap;

    /*
    /**********************************************************
    /* Parsing state, parsed values
    /**********************************************************
     */

    /**
     * ByteArrayBuilder is needed if 'getBinaryValue' is called. If so,
     * we better reuse it for remainder of content.
     */
    protected ByteArrayBuilder _byteArrayBuilder = null;

    /**
     * We will hold on to decoded binary data, for duration of
     * current event, so that multiple calls to
     * {@link #getBinaryValue} will not need to decode data more
     * than once.
     */
    protected byte[] _binaryValue;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    public FromXmlParser(IOContext ctxt, int genericParserFeatures, int xmlFeatures,
            ObjectCodec codec, XMLStreamReader xmlReader)
    {
        super(genericParserFeatures);
        _formatFeatures = xmlFeatures;
        _ioContext = ctxt;
        _objectCodec = codec;
        _parsingContext = XmlReadContext.createRootContext(-1, -1);
        // and thereby start a scope
        _nextToken = JsonToken.START_OBJECT;
        _xmlTokens = new XmlTokenStream(xmlReader, ctxt.getSourceReference());
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }
    
    @Override
    public ObjectCodec getCodec() {
        return _objectCodec;
    }

    @Override
    public void setCodec(ObjectCodec c) {
        _objectCodec = c;
    }

    /**
     * @since 2.1
     */
    public void setXMLTextElementName(String name) {
        _cfgNameForTextElement = name;
    }
    
    /**
     * XML format does require support from custom {@link ObjectCodec}
     * (that is, {@link XmlMapper}), so need to return true here.
     * 
     * @return True since XML format does require support from codec
     */
    @Override
    public boolean requiresCustomCodec() {
        return true;
    }
    
    /*
    /**********************************************************
    /* Extended API, configuration
    /**********************************************************
     */

    public FromXmlParser enable(Feature f) {
        _formatFeatures |= f.getMask();
        return this;
    }

    public FromXmlParser disable(Feature f) {
        _formatFeatures &= ~f.getMask();
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (_formatFeatures & f.getMask()) != 0;
    }

    public FromXmlParser configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    /*                                                                                       
    /**********************************************************                              
    /* FormatFeature support                                                                             
    /**********************************************************                              
     */

    @Override
    public int getFormatFeatures() {
        return _formatFeatures;
    }

    @Override
    public JsonParser overrideFormatFeatures(int values, int mask) {
        _formatFeatures = (_formatFeatures & ~mask) | (values & mask);
        return this;
    }

    /*
    /**********************************************************
    /* Extended API, access to some internal components
    /**********************************************************
     */

    /**
     * Method that allows application direct access to underlying
     * Stax {@link XMLStreamWriter}. Note that use of writer is
     * discouraged, and may interfere with processing of this writer;
     * however, occasionally it may be necessary.
     *<p>
     * Note: writer instance will always be of type
     * {@link org.codehaus.stax2.XMLStreamWriter2} (including
     * Typed Access API) so upcasts are safe.
     */
    public XMLStreamReader getStaxReader() {
        return _xmlTokens.getXmlReader();
    }

    /*
    /**********************************************************
    /* Internal API
    /**********************************************************
     */

    /**
     * Method that may be called to indicate that specified names
     * (only local parts retained currently: this may be changed in
     * future) should be considered "auto-wrapping", meaning that
     * they will be doubled to contain two opening elements, two
     * matching closing elements. This is needed for supporting
     * handling of so-called "unwrapped" array types, something
     * XML mappings like JAXB often use.
     *<p>
     * NOTE: this method is considered part of internal implementation
     * interface, and it is <b>NOT</b> guaranteed to remain unchanged
     * between minor versions (it is however expected not to change in
     * patch versions). So if you have to use it, be prepared for
     * possible additional work.
     * 
     * @since 2.1
     */
    public void addVirtualWrapping(Set<String> namesToWrap)
    {
        /* 17-Sep-2012, tatu: Not 100% sure why, but this is necessary to avoid
         *   problems with Lists-in-Lists properties
         */
        String name = _xmlTokens.getLocalName();
        if (name != null && namesToWrap.contains(name)) {
            _xmlTokens.repeatStartElement();
        }
        _namesToWrap = namesToWrap;
        _parsingContext.setNamesToWrap(namesToWrap);
    }

    /*
    /**********************************************************
    /* JsonParser impl
    /**********************************************************
     */
    
    /**
     * Method that can be called to get the name associated with
     * the current event.
     */
    @Override
    public String getCurrentName() throws IOException
    {
        // start markers require information from parent
        String name;
        if (_currToken == JsonToken.START_OBJECT || _currToken == JsonToken.START_ARRAY) {
            XmlReadContext parent = _parsingContext.getParent();
            name = parent.getCurrentName();
        } else {
            name = _parsingContext.getCurrentName();
        }
        // sanity check
        if (name == null) {
            throw new IllegalStateException("Missing name, in state: "+_currToken);
        }
        return name;
    }

    @Override
    public void overrideCurrentName(String name)
    {
        // Simple, but need to look for START_OBJECT/ARRAY's "off-by-one" thing:
        XmlReadContext ctxt = _parsingContext;
        if (_currToken == JsonToken.START_OBJECT || _currToken == JsonToken.START_ARRAY) {
            ctxt = ctxt.getParent();
        }
        ctxt.setCurrentName(name);
    }
    
    @Override
    public void close() throws IOException
    {
        if (!_closed) {
            _closed = true;
            try {
                if (_ioContext.isResourceManaged() || isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)) {
                    _xmlTokens.closeCompletely();
                } else {
                    _xmlTokens.close();
                }
            } finally {
                // as per [JACKSON-324], do in finally block
                // Also, internal buffer(s) can now be released as well
                _releaseBuffers();
            }
        }
    }

    @Override
    public boolean isClosed() { return _closed; }

    @Override
    public XmlReadContext getParsingContext() {
        return _parsingContext;
    }

    /**
     * Method that return the <b>starting</b> location of the current
     * token; that is, position of the first character from input
     * that starts the current token.
     */
    @Override
    public JsonLocation getTokenLocation() {
        return _xmlTokens.getTokenLocation();
    }

    /**
     * Method that returns location of the last processed character;
     * usually for error reporting purposes
     */
    @Override
    public JsonLocation getCurrentLocation() {
        return _xmlTokens.getCurrentLocation();
    }

    /**
     * Since xml representation can not really distinguish between array
     * and object starts (both are represented with elements), this method
     * is overridden and taken to mean that expecation is that the current
     * start element is to mean 'start array', instead of default of
     * 'start object'.
     */
    @Override
    public boolean isExpectedStartArrayToken()
    {
        JsonToken t = _currToken;
        if (t == JsonToken.START_OBJECT) {        	
            _currToken = JsonToken.START_ARRAY;
            // Ok: must replace current context with array as well
            _parsingContext.convertToArray();
//System.out.println(" isExpectedArrayStart: OBJ->Array, wraps now: "+_parsingContext.getNamesToWrap());
            // And just in case a field name was to be returned, wipe it
            _nextToken = null;
            // and last thing, [dataformat-xml#33], better ignore attributes
            _xmlTokens.skipAttributes();
            return true;
        }
//System.out.println(" isExpectedArrayStart?: t="+t);
        return (t == JsonToken.START_ARRAY);
    }

    // DEBUGGING
/*
    @Override
    public JsonToken nextToken() throws IOException
    {
        JsonToken t = nextToken0();
        if (t != null) {
            switch (t) {
            case FIELD_NAME:
                System.out.println("JsonToken: FIELD_NAME '"+_parsingContext.getCurrentName()+"'");
                break;
            case VALUE_STRING:
                System.out.println("JsonToken: VALUE_STRING '"+getText()+"'");
                break;
            default:
                System.out.println("JsonToken: "+t);
            }
        }
        return t;
    }
*/

    @Override
    public JsonToken nextToken() throws IOException
    {
        _binaryValue = null;
        if (_nextToken != null) {
            JsonToken t = _nextToken;
            _currToken = t;
            _nextToken = null;
            switch (t) {
            case START_OBJECT:
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                break;
            case START_ARRAY:
                _parsingContext = _parsingContext.createChildArrayContext(-1, -1);
                break;
            case END_OBJECT:
            case END_ARRAY:
                _parsingContext = _parsingContext.getParent();
                _namesToWrap = _parsingContext.getNamesToWrap();
                break;
            case FIELD_NAME:
                _parsingContext.setCurrentName(_xmlTokens.getLocalName());
                break;
            default: // VALUE_STRING, VALUE_NULL
                // should be fine as is?
            }
            return t;
        }
        int token = _xmlTokens.next();

        // Need to have a loop just because we may have to eat/convert
        // a start-element that indicates an array element.
        while (token == XmlTokenStream.XML_START_ELEMENT) {
            // If we thought we might get leaf, no such luck
            if (_mayBeLeaf) {
                // leave _mayBeLeaf set, as we start a new context
                _nextToken = JsonToken.FIELD_NAME;
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                return (_currToken = JsonToken.START_OBJECT);
            }
            if (_parsingContext.inArray()) {
                // Yup: in array, so this element could be verified; but it won't be
                // reported anyway, and we need to process following event.
                token = _xmlTokens.next();
                _mayBeLeaf = true;
                continue;
            }
            String name = _xmlTokens.getLocalName();
            _parsingContext.setCurrentName(name);

            // Ok: virtual wrapping can be done by simply repeating current START_ELEMENT.
            // Couple of ways to do it; but start by making _xmlTokens replay the thing...
            if (_namesToWrap != null && _namesToWrap.contains(name)) {
                _xmlTokens.repeatStartElement();
            }

            _mayBeLeaf = true;
            // Ok: in array context we need to skip reporting field names.
            // But what's the best way to find next token?
            return (_currToken = JsonToken.FIELD_NAME);
        }

        // Ok; beyond start element, what do we get?
        switch (token) {
        case XmlTokenStream.XML_END_ELEMENT:
            // Simple, except that if this is a leaf, need to suppress end:
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                if (_parsingContext.inArray()) {
                    // 06-Jan-2015, tatu: as per [dataformat-xml#180], need to
                    //    expose as empty Object, not null
                    _nextToken = JsonToken.END_OBJECT;
                    _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                    return (_currToken = JsonToken.START_OBJECT);
                }
                return (_currToken = JsonToken.VALUE_NULL);
            }
            _currToken = _parsingContext.inArray() ? JsonToken.END_ARRAY : JsonToken.END_OBJECT;
            _parsingContext = _parsingContext.getParent();
            _namesToWrap = _parsingContext.getNamesToWrap();
            return _currToken;
            
        case XmlTokenStream.XML_ATTRIBUTE_NAME:
            // If there was a chance of leaf node, no more...
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                _nextToken = JsonToken.FIELD_NAME;
                _currText = _xmlTokens.getText();
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                return (_currToken = JsonToken.START_OBJECT);
            }
            _parsingContext.setCurrentName(_xmlTokens.getLocalName());
            return (_currToken = JsonToken.FIELD_NAME);
        case XmlTokenStream.XML_ATTRIBUTE_VALUE:
            _currText = _xmlTokens.getText();
            return (_currToken = JsonToken.VALUE_STRING);
        case XmlTokenStream.XML_TEXT:
            _currText = _xmlTokens.getText();
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                /* One more refinement (pronunced like "hack") is that if
                 * we had an empty String (or all white space), and we are
                 * deserializing an array, we better hide the empty text.
                 */
                // Also: must skip following END_ELEMENT
                _xmlTokens.skipEndElement();
                if (_parsingContext.inArray()) {
                    if (_isEmpty(_currText)) {
                        // 06-Jan-2015, tatu: as per [dataformat-xml#180], need to
                        //    expose as empty Object, not null (or, worse, as used to
                        //    be done, by swallowing the token)
                        _nextToken = JsonToken.END_OBJECT;
                        _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                        return (_currToken = JsonToken.START_OBJECT);
                    }
                }
                return (_currToken = JsonToken.VALUE_STRING);
            } else {
                // [dataformat-xml#177]: empty text may also need to be skipped
                if (_parsingContext.inObject()
                        && (_currToken != JsonToken.FIELD_NAME) && _isEmpty(_currText)) {
                    _currToken = JsonToken.END_OBJECT;
                    _parsingContext = _parsingContext.getParent();
                    _namesToWrap = _parsingContext.getNamesToWrap();
                    return _currToken;
                }
            }
            // If not a leaf (or otherwise ignorable), need to transform into property...
            _parsingContext.setCurrentName(_cfgNameForTextElement);
            _nextToken = JsonToken.VALUE_STRING;
            return (_currToken = JsonToken.FIELD_NAME);
        case XmlTokenStream.XML_END:
            return (_currToken = null);
        }
        
        // should never get here
        _throwInternal();
        return null;
    }
    
    /*
    /**********************************************************
    /* Overrides of specialized nextXxx() methods
    /**********************************************************
     */

    /**
     * Method overridden to support more reliable deserialization of
     * String collections.
     */
    @Override
    public String nextTextValue() throws IOException
    {
        _binaryValue = null;
        if (_nextToken != null) {
            JsonToken t = _nextToken;
            _currToken = t;
            _nextToken = null;

            // expected case; yes, got a String
            if (t == JsonToken.VALUE_STRING) {
                return _currText;
            }
            _updateState(t);
            return null;
        }

        int token = _xmlTokens.next();

        // mostly copied from 'nextToken()'
        while (token == XmlTokenStream.XML_START_ELEMENT) {
            if (_mayBeLeaf) {
                _nextToken = JsonToken.FIELD_NAME;
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                _currToken = JsonToken.START_OBJECT;
                return null;
            }
            if (_parsingContext.inArray()) {
                token = _xmlTokens.next();
                _mayBeLeaf = true;
                continue;
            }
            String name = _xmlTokens.getLocalName();
            _parsingContext.setCurrentName(name);
            if (_namesToWrap != null && _namesToWrap.contains(name)) {
                _xmlTokens.repeatStartElement();
            }
            _mayBeLeaf = true;
            _currToken = JsonToken.FIELD_NAME;
            return null;
        }

        // Ok; beyond start element, what do we get?
        switch (token) {
        case XmlTokenStream.XML_END_ELEMENT:
            if (_mayBeLeaf) {
                // NOTE: this is different from nextToken() -- produce "", NOT null
                _mayBeLeaf = false;
                _currToken = JsonToken.VALUE_STRING;
                return (_currText = "");
            }
            _currToken = _parsingContext.inArray() ? JsonToken.END_ARRAY : JsonToken.END_OBJECT;
            _parsingContext = _parsingContext.getParent();
            _namesToWrap = _parsingContext.getNamesToWrap();
            break;
            
        case XmlTokenStream.XML_ATTRIBUTE_NAME:
            // If there was a chance of leaf node, no more...
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                _nextToken = JsonToken.FIELD_NAME;
                _currText = _xmlTokens.getText();
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                _currToken = JsonToken.START_OBJECT;
            } else {
                _parsingContext.setCurrentName(_xmlTokens.getLocalName());
                _currToken = JsonToken.FIELD_NAME;
            }
            break;
        case XmlTokenStream.XML_ATTRIBUTE_VALUE:
            _currText = _xmlTokens.getText();
            _currToken = JsonToken.VALUE_STRING;
            break;
        case XmlTokenStream.XML_TEXT:
            _currText = _xmlTokens.getText();
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                // Also: must skip following END_ELEMENT
                _xmlTokens.skipEndElement();

                // NOTE: this is different from nextToken() -- NO work-around
                // for otherwise empty List/array
                _currToken = JsonToken.VALUE_STRING;
                return _currText;
            }
            // If not a leaf, need to transform into property...
            _parsingContext.setCurrentName(_cfgNameForTextElement);
            _nextToken = JsonToken.VALUE_STRING;
            _currToken = JsonToken.FIELD_NAME;
            break;
        case XmlTokenStream.XML_END:
            _currToken = null;
        }
        return null;
    }


    private void _updateState(JsonToken t)
    {
        switch (t) {
        case START_OBJECT:
            _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
            break;
        case START_ARRAY:
            _parsingContext = _parsingContext.createChildArrayContext(-1, -1);
            break;
        case END_OBJECT:
        case END_ARRAY:
            _parsingContext = _parsingContext.getParent();
            _namesToWrap = _parsingContext.getNamesToWrap();
            break;
        case FIELD_NAME:
            _parsingContext.setCurrentName(_xmlTokens.getLocalName());
            break;
        default:
        }
    }

    /*
    /**********************************************************
    /* Public API, access to token information, text
    /**********************************************************
     */

    @Override
    public String getText() throws IOException
    {
        if (_currToken == null) {
            return null;
        }
        switch (_currToken) {
        case FIELD_NAME:
            return getCurrentName();
        case VALUE_STRING:
            return _currText;
        default:
            return _currToken.asString();
        }
    }

    // @since 2.1
    @Override
    public final String getValueAsString() throws IOException {
        return getValueAsString(null);
    }

    @Override
    public String getValueAsString(String defValue) throws IOException
    {
        JsonToken t = _currToken;
        if (t == null) {
            return null;
        }
        switch (t) {
        case FIELD_NAME:
            return getCurrentName();
        case VALUE_STRING:
            return _currText;
        case START_OBJECT:
            // the interesting case; may be able to convert certain kinds of
            // elements (specifically, ones with attributes, CDATA only content)
            // into VALUE_STRING
            {
                String str = _xmlTokens.convertToString();
                if (str != null) {
                    // need to convert token, as well as "undo" START_OBJECT
                    // note: Should NOT update context, because we will still be getting
                    // matching END_OBJECT, which will undo contexts properly
                    _parsingContext = _parsingContext.getParent();
                    _namesToWrap = _parsingContext.getNamesToWrap();
                    _currToken = JsonToken.VALUE_STRING;
                    _nextToken = null;
                    /* One more thing: must explicitly skip the END_OBJECT that
                     * would follow.
                     */
                    _xmlTokens.skipEndElement();
                    return (_currText = str);
                }
                
            }
            return null;
        default:
            if (_currToken.isScalarValue()) {
                return _currToken.asString();
            }
        }
        return defValue;
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        String text = getText();
        return (text == null)  ? null : text.toCharArray();
    }

    @Override
    public int getTextLength() throws IOException {
        String text = getText();
        return (text == null)  ? 0 : text.length();
    }

    @Override
    public int getTextOffset() throws IOException {
        return 0;
    }

    /**
     * XML input actually would offer access to character arrays; but since
     * we must coalesce things it cannot really be exposed.
     */
    @Override
    public boolean hasTextCharacters()
    {
        return false;
    }

    /*
    /**********************************************************
    /* Public API, access to token information, binary
    /**********************************************************
     */

    @Override
    public Object getEmbeddedObject() throws IOException {
        // no way to embed POJOs for now...
        return null;
    }

    @Override
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException
    {
        if (_currToken != JsonToken.VALUE_STRING &&
                (_currToken != JsonToken.VALUE_EMBEDDED_OBJECT || _binaryValue == null)) {
            _reportError("Current token ("+_currToken+") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        /* To ensure that we won't see inconsistent data, better clear up
         * state...
         */
        if (_binaryValue == null) {
            try {
                _binaryValue = _decodeBase64(b64variant);
            } catch (IllegalArgumentException iae) {
                throw _constructError("Failed to decode VALUE_STRING as base64 ("+b64variant+"): "+iae.getMessage());
            }
        }        
        return _binaryValue;
    }

    @SuppressWarnings("resource")
    protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException
    {
        ByteArrayBuilder builder = _getByteArrayBuilder();
        final String str = getText();
        _decodeBase64(str, builder, b64variant);
        return builder.toByteArray();
    }
    
    /*
    /**********************************************************
    /* Numeric accessors
    /**********************************************************
     */

    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getDecimalValue() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getDoubleValue() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFloatValue() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getIntValue() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLongValue() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public NumberType getNumberType() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getNumberValue() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
    /**********************************************************
    /* Abstract method impls for stuff from JsonParser
    /**********************************************************
     */

    /**
     * Method called when an EOF is encountered between tokens.
     * If so, it may be a legitimate EOF, but only iff there
     * is no open non-root context.
     */
    @Override
    protected void _handleEOF() throws JsonParseException
    {
        if (!_parsingContext.inRoot()) {
            _reportInvalidEOF(": expected close marker for "+_parsingContext.getTypeDesc()+" (from "+_parsingContext.getStartLocation(_ioContext.getSourceReference())+")");
        }
    }
    
    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    /**
     * Method called to release internal buffers owned by the base
     * parser.
     */
    protected void _releaseBuffers() throws IOException {
        // anything we can/must release? Underlying parser should do all of it, for now?
    }

    protected ByteArrayBuilder _getByteArrayBuilder()
    {
        if (_byteArrayBuilder == null) {
            _byteArrayBuilder = new ByteArrayBuilder();
        } else {
            _byteArrayBuilder.reset();
        }
        return _byteArrayBuilder;
    }

    protected boolean _isEmpty(String str)
    {
        int len = (str == null) ? 0 : str.length();
        if (len > 0) {
            for (int i = 0; i < len; ++i) {
                if (str.charAt(i) > ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}

```


## Preregistered shared declaration eligibility supplement

Generate tests only for the following eligible declarations. This list comes from fixed/buggy signature intersection, not execution behavior or triggering tests. The list does not certify fixture construction or semantic validity. Use meaningful non-null domain fixtures from the supplied fixed APIs. Do not substitute construction failures, null-only inputs, empty tests or non-null-only assertions for checks that reach a target. Preserve assertions; no compile/test feedback or repair loop.

[
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "_updateState",
    "parameter_types": "com.fasterxml.jackson.core.JsonToken"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "_isEmpty",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "_decodeBase64",
    "parameter_types": "com.fasterxml.jackson.core.Base64Variant"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "_getByteArrayBuilder",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "_handleEOF",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "_releaseBuffers",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "hasTextCharacters",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "isClosed",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "isExpectedStartArrayToken",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "requiresCustomCodec",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "getBinaryValue",
    "parameter_types": "com.fasterxml.jackson.core.Base64Variant"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getTextCharacters",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getCurrentLocation",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getTokenLocation",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 21,
    "method": "overrideFormatFeatures",
    "parameter_types": "int,int"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getNumberType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "nextToken",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getCodec",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "version",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 21,
    "method": "configure",
    "parameter_types": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature,boolean"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "disable",
    "parameter_types": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "enable",
    "parameter_types": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getParsingContext",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getDoubleValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "isEnabled",
    "parameter_types": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getValueAsString",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getFloatValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getFormatFeatures",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getIntValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getTextLength",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getTextOffset",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getNumberValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getEmbeddedObject",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getCurrentName",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getText",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "getValueAsString",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "nextTextValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getDecimalValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getBigIntegerValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getStaxReader",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "getLongValue",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "addVirtualWrapping",
    "parameter_types": "java.util.Set"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "close",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "overrideCurrentName",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "setCodec",
    "parameter_types": "com.fasterxml.jackson.core.ObjectCodec"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 18,
    "method": "setXMLTextElementName",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser",
    "constructor_types": "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader",
    "dimensions": 15,
    "method": "<init>",
    "parameter_types": ""
  }
]
