## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jsoup" default="package" basedir=".">

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

  <target name="compile" depends="jsoup-from-maven.compile"> </target>
  <target name="compile.tests" depends="jsoup-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <name>jsoup</name>

  <groupId>org.jsoup</groupId>
  <artifactId>jsoup</artifactId>
  <version>1.1.2-SNAPSHOT</version>
  <description>jsoup HTML parser</description>
  <url>http://jsoup.org/</url>
  <inceptionYear>2009</inceptionYear>
  <issueManagement>
  	<system>GitHub</system>
  	<url>http://github.com/jhy/jsoup/issues</url>
  </issueManagement>
  <licenses>
  	<license>
  		<name>The MIT License</name>
  		<url>http://jsoup.com/license</url>
  		<distribution>repo</distribution>
  	</license>  
  </licenses>
  <scm>
  	<url>http://github.com/jhy/jsoup</url>
    <connection>scm:git:git://github.com/jhy/jsoup.git</connection>
    <!-- <developerConnection>scm:git:git@github.com:jhy/jsoup.git</developerConnection> -->
  </scm>
  <organization>
  	<name>Jonathan Hedley</name>
  	<url>http://jonathanhedley.com/</url>
  </organization>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.0.2</version>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
          <encoding>UTF-8</encoding>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.6.1</version>
        <configuration>
        </configuration>
        <executions>
          <execution>
            <id>attach-javadoc</id>
            <phase>verify</phase>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>2.1.1</version>
        <configuration>
        </configuration>
        <executions>
          <execution>
            <id>attach-sources</id>
            <phase>verify</phase>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <distributionManagement>
    <snapshotRepository>
      <id>sonatype-nexus-snapshots</id>
      <name>Sonatype Nexus Snapshots</name>
      <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>
    <repository>
      <id>sonatype-nexus-staging</id>
      <name>Nexus Release Repository</name>
      <url>http://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
  </distributionManagement>

  <profiles>
    <profile>
      <id>release-sign-artifacts</id>
      <activation>
        <property>
          <name>performRelease</name>
          <value>true</value>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-gpg-plugin</artifactId>
            <executions>
              <execution>
                <id>sign-artifacts</id>
                <phase>verify</phase>
                <goals>
                  <goal>sign</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
 
  <dependencies>

    <dependency>
      <!-- junit -->
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.5</version>
      <scope>test</scope>
    </dependency>
    
    <dependency>
      <!-- Commons Lang, for method argument validation. -->
      <groupId>commons-lang</groupId>
      <artifactId>commons-lang</artifactId>
      <version>2.4</version>
    </dependency>
  </dependencies>
  <dependencyManagement>
  	<dependencies>
  	</dependencies>
  </dependencyManagement>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <developers>
    <developer>
      <id>jhy</id>
      <name>Jonathan Hedley</name>
      <email>jonathan@hedley.net</email>
      <roles>
        <role>Lead Developer</role>
      </roles>
      <timezone>+11</timezone>
    </developer>
  </developers>

</project>
```

## src/main/java/org/jsoup/parser/Parser.java

```
package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

/**
 Parses HTML into a {@link Document}. Generally best to use one of the  more convenient parse methods in {@link org.jsoup.Jsoup}.

 @author Jonathan Hedley, jonathan@hedley.net */
public class Parser {
    private static final String SQ = "'";
    private static final String DQ = "\"";

    private static final Tag htmlTag = Tag.valueOf("html");
    private static final Tag headTag = Tag.valueOf("head");
    private static final Tag bodyTag = Tag.valueOf("body");
    private static final Tag titleTag = Tag.valueOf("title");
    private static final Tag textareaTag = Tag.valueOf("textarea");

    private final LinkedList<Element> stack;
    private final TokenQueue tq;
    private final Document doc;
    private String baseUri;

    private Parser(String html, String baseUri, boolean isBodyFragment) {
        Validate.notNull(html);
        Validate.notNull(baseUri);

        stack = new LinkedList<Element>();
        tq = new TokenQueue(html);
        this.baseUri = baseUri;

        if (isBodyFragment) {
            doc = Document.createShell(baseUri);
            stack.add(doc.body());
        } else {
            doc = new Document(baseUri);
            stack.add(doc);
        }
    }

    /**
     Parse HTML into a Document.
     @param html HTML to parse
     @param baseUri base URI of document (i.e. original fetch location), for resolving relative URLs.
     @return parsed Document
     */
    public static Document parse(String html, String baseUri) {
        Parser parser = new Parser(html, baseUri, false);
        return parser.parse();
    }

    /**
     Parse a fragment of HTML into the {@code body} of a Document.
     @param bodyHtml fragment of HTML
     @param baseUri base URI of document (i.e. original fetch location), for resolving relative URLs.
     @return Document, with empty head, and HTML parsed into body
     */
    public static Document parseBodyFragment(String bodyHtml, String baseUri) {
        Parser parser = new Parser(bodyHtml, baseUri, true);
        return parser.parse();
    }

    private Document parse() {
        while (!tq.isEmpty()) {
            if (tq.matches("<!--")) {
                parseComment();
            } else if (tq.matches("<![CDATA[")) {
                parseCdata();
            } else if (tq.matches("<?") || tq.matches("<!")) {
                parseXmlDecl();
            } else if (tq.matches("</")) {
                parseEndTag();
            } else if (tq.matches("<")) {
                parseStartTag();
            } else {
                parseTextNode();
            }
        }
        return doc.normalise();
    }

    private void parseComment() {
        tq.consume("<!--");
        String data = tq.chompTo("->");

        if (data.endsWith("-")) // i.e. was -->
            data = data.substring(0, data.length()-1);
        Comment comment = new Comment(data, baseUri);
        last().appendChild(comment);
    }

    private void parseXmlDecl() {
        tq.consume("<");
        Character firstChar = tq.consume(); // <? or <!, from initial match.
        boolean procInstr = firstChar.toString().equals("!");
        String data = tq.chompTo(">");

        XmlDeclaration decl = new XmlDeclaration(data, baseUri, procInstr);
        last().appendChild(decl);
    }

    private void parseEndTag() {
        tq.consume("</");
        String tagName = tq.consumeWord();
        tq.chompTo(">");

        if (tagName.length() != 0) {
            Tag tag = Tag.valueOf(tagName);
            popStackToClose(tag);
        }
    }

    private void parseStartTag() {
        tq.consume("<");
        String tagName = tq.consumeWord();

        if (tagName.length() == 0) { // doesn't look like a start tag after all; put < back on stack and handle as text
            tq.addFirst("&lt;");
            parseTextNode();
            return;
        }

        Attributes attributes = new Attributes();
        while (!tq.matchesAny("<", "/>", ">") && !tq.isEmpty()) {
            Attribute attribute = parseAttribute();
            if (attribute != null)
                attributes.put(attribute);
        }

        Tag tag = Tag.valueOf(tagName);
        Element child = new Element(tag, baseUri, attributes);

        boolean isEmptyElement = tag.isEmpty(); // empty element if empty tag (e.g. img) or self-closed el (<div/>
        if (tq.matchChomp("/>")) { // close empty element or tag
            isEmptyElement = true;
        } else {
            tq.matchChomp(">");
        }
        addChildToParent(child, isEmptyElement);

        // pc data only tags (textarea, script): chomp to end tag, add content as text node
        if (tag.isData()) {
            String data = tq.chompTo("</" + tagName);
            tq.chompTo(">");
            popStackToClose(tag);
            
            Node dataNode;
            if (tag.equals(titleTag) || tag.equals(textareaTag)) // want to show as text, but not contain inside tags (so not a data tag?)
                dataNode = TextNode.createFromEncoded(data, baseUri);
            else
                dataNode = new DataNode(data, baseUri); // data not encoded but raw (for " in script)
            child.appendChild(dataNode);   
        }

        // <base href>: update the base uri
        if (child.tagName().equals("base")) {
            String href = child.absUrl("href");
            if (href.length() != 0) { // ignore <base target> etc
                baseUri = href;
                doc.setBaseUri(href); // set on the doc so doc.createElement(Tag) will get updated base
            }
        }
    }

    private Attribute parseAttribute() {
        tq.consumeWhitespace();
        String key = tq.consumeAttributeKey();
        String value = "";
        tq.consumeWhitespace();
        if (tq.matchChomp("=")) {
            tq.consumeWhitespace();

            if (tq.matchChomp(SQ)) {
                value = tq.chompTo(SQ);
            } else if (tq.matchChomp(DQ)) {
                value = tq.chompTo(DQ);
            } else {
                StringBuilder valueAccum = new StringBuilder();
                // no ' or " to look for, so scan to end tag or space (or end of stream)
                while (!tq.matchesAny("<", "/>", ">") && !tq.matchesWhitespace() && !tq.isEmpty()) {
                    valueAccum.append(tq.consume());
                }
                value = valueAccum.toString();
            }
            tq.consumeWhitespace();
        }
        if (key.length() != 0)
            return Attribute.createFromEncoded(key, value);
        else {
            tq.consume(); // unknown char, keep popping so not get stuck
            return null;
        }
    }

    private void parseTextNode() {
        String text = tq.consumeTo("<");
        TextNode textNode = TextNode.createFromEncoded(text, baseUri);
        last().appendChild(textNode);
    }

    private void parseCdata() {
        tq.consume("<![CDATA[");
        String rawText = tq.chompTo("]]>");
        TextNode textNode = new TextNode(rawText, baseUri); // constructor does not escape
        last().appendChild(textNode);
    }

    private Element addChildToParent(Element child, boolean isEmptyElement) {
        Element parent = popStackToSuitableContainer(child.tag());
        Tag childTag = child.tag();
        boolean validAncestor = stackHasValidParent(childTag);

        if (!validAncestor) {
            // create implicit parent around this child
            Tag parentTag = childTag.getImplicitParent();
            Element implicit = new Element(parentTag, baseUri);
            // special case: make sure there's a head before putting in body
            if (child.tag().equals(bodyTag)) {
                Element head = new Element(headTag, baseUri);
                implicit.appendChild(head);
            }
            implicit.appendChild(child);

            // recurse to ensure somewhere to put parent
            Element root = addChildToParent(implicit, false);
            if (!isEmptyElement)
                stack.addLast(child);
            return root;
        }

        parent.appendChild(child);

        if (!isEmptyElement)
            stack.addLast(child);
        return parent;
    }

    private boolean stackHasValidParent(Tag childTag) {
        if (stack.size() == 1 && childTag.equals(htmlTag))
            return true; // root is valid for html node
        
        for (int i = stack.size() -1; i >= 0; i--) {
            Element el = stack.get(i);
            Tag parent2 = el.tag();
            if (parent2.isValidParent(childTag)) {
                return true;
            }
        }
        return false;
    }

    private Element popStackToSuitableContainer(Tag tag) {
        while (!stack.isEmpty()) {
            if (last().tag().canContain(tag))
                return last();
            else
                stack.removeLast();
        }
        return null;
    }

    private Element popStackToClose(Tag tag) {
        // first check to see if stack contains this tag; if so pop to there, otherwise ignore
        int counter = 0;
        Element elToClose = null;
        for (int i = stack.size() -1; i > 0; i--) {
            counter++;
            Element el = stack.get(i);
            Tag elTag = el.tag();
            if (elTag.equals(bodyTag) || elTag.equals(htmlTag)) { // once in body, don't close past body
                break;
            } else if (elTag.equals(tag)) {
                elToClose = el;
                break;
            }
        }
        if (elToClose != null) {
            for (int i = 0; i < counter; i++) {
                stack.removeLast();
            }
        }
        return elToClose;
    }

    private Element last() {
        return stack.getLast();
    }
}

```
