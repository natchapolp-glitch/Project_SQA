Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Jsoup; fixed revision: 1f.
Modified target classes:
org.jsoup.nodes.Document

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "",
    "method": "createShell",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "body",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "createElement",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "head",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "nodeName",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "normalise",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "normalise",
    "parameter_types": "org.jsoup.nodes.Element"
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "outerHtml",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "text",
    "parameter_types": "java.lang.String"
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "title",
    "parameter_types": ""
  },
  {
    "class": "org.jsoup.nodes.Document",
    "constructor_types": "java.lang.String",
    "method": "title",
    "parameter_types": "java.lang.String"
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.jsoup.DataUtil",
  "org.jsoup.Jsoup",
  "org.jsoup.examples.ListLinks",
  "org.jsoup.nodes.Attribute",
  "org.jsoup.nodes.Attributes",
  "org.jsoup.nodes.Comment",
  "org.jsoup.nodes.DataNode",
  "org.jsoup.nodes.Document",
  "org.jsoup.nodes.Element",
  "org.jsoup.nodes.Evaluator",
  "org.jsoup.nodes.Node",
  "org.jsoup.nodes.TextNode",
  "org.jsoup.nodes.XmlDeclaration",
  "org.jsoup.parser.Parser",
  "org.jsoup.parser.Tag",
  "org.jsoup.parser.TokenQueue",
  "org.jsoup.safety.Cleaner",
  "org.jsoup.safety.Whitelist",
  "org.jsoup.select.Collector",
  "org.jsoup.select.Elements",
  "org.jsoup.select.Selector"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

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

## src/main/java/org/jsoup/nodes/Document.java

```
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

/**
 A HTML Document.

 @author Jonathan Hedley, jonathan@hedley.net */
public class Document extends Element {

    /**
     Create a new, empty Document.
     @param baseUri base URI of document
     @see org.jsoup.Jsoup#parse
     @see #createShell
     */
    public Document(String baseUri) {
        super(Tag.valueOf("#root"), baseUri);
    }

    /**
     Create a valid, empty shell of a document, suitable for adding more elements to.
     @param baseUri baseUri of document
     @return document with html, head, and body elements.
     */
    static public Document createShell(String baseUri) {
        Validate.notNull(baseUri);

        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        html.appendElement("head");
        html.appendElement("body");

        return doc;
    }

    /**
     Accessor to the document's {@code head} element.
     @return {@code head}
     */
    public Element head() {
        return getElementsByTag("head").first();
    }

    /**
     Accessor to the document's {@code body} element.
     @return {@code body}
     */
    public Element body() {
        return getElementsByTag("body").first();
    }

    /**
     Get the string contents of the document's {@code title} element.
     @return Trimed title, or empty string if none set.
     */
    public String title() {
        Element titleEl = getElementsByTag("title").first();
        return titleEl != null ? titleEl.text().trim() : "";
    }

    /**
     Set the document's {@code title} element. Updates the existing element, or adds {@code title} to {@code head} if
     not present
     @param title string to set as title
     */
    public void title(String title) {
        Validate.notNull(title);
        Element titleEl = getElementsByTag("title").first();
        if (titleEl == null) { // add to head
            head().appendElement("title").text(title);
        } else {
            titleEl.text(title);
        }
    }

    /**
     Create a new Element, with this document's base uri. Does not make the new element a child of this document.
     @param tagName element tag name (e.g. {@code a})
     @return new element
     */
    public Element createElement(String tagName) {
        return new Element(Tag.valueOf(tagName), this.baseUri());
    }

    /**
     Normalise the document. This happens after the parse phase so generally does not need to be called.
     Moves any text content that is not in the body element into the body.
     @return this document after normalisation
     */
    public Document normalise() {
        if (select("html").isEmpty())
            appendElement("html");
        if (head() == null)
            select("html").first().prependElement("head");
        if (body() == null)
            select("html").first().appendElement("body");

        // pull text nodes out of root, html, and head els, and push into body. non-text nodes are already taken care
        // of. do in inverse order to maintain text order.
        normalise(head());
        normalise(select("html").first());
        normalise(this);        

        return this;
    }

    // does not recurse.
    private void normalise(Element element) {
        List<Node> toMove = new ArrayList<Node>();
        for (Node node: element.childNodes) {
            if (node instanceof TextNode) {
                TextNode tn = (TextNode) node;
                if (!tn.isBlank())
                    toMove.add(tn);
            }
        }

        for (Node node: toMove) {
            element.removeChild(node);
            body().prependChild(node);
            body().prependChild(new TextNode(" ", ""));
        }
    }

    @Override
    public String outerHtml() {
        return super.html(); // no outer wrapper tag
    }

    /**
     Set the text of the {@code body} of this document. Any existing nodes within the body will be cleared.
     @param text unencoded text
     @return this document
     */
    @Override
    public Element text(String text) {
        body().text(text); // overridden to not nuke doc structure
        return this;
    }

    @Override
    public String nodeName() {
        return "#document";
    }
}


```
