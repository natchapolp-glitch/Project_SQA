# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: Gson
Fixed reference revision: 1f
Target classes:
- com.google.gson.TypeInfoFactory

## Eligible shared API declarations

Target these declarations, which exist on both evaluation revisions. Only declaration signatures were checked; no buggy behavior was supplied. Generate at most 30 independent test methods per response.

```json
[
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getIndex",
    "parameter_types": "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable",
    "dimensions": 6
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getActualType",
    "parameter_types": "java.lang.reflect.Type,java.lang.reflect.Type,java.lang.Class",
    "dimensions": 9
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "extractRealTypes",
    "parameter_types": "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class",
    "dimensions": 9
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getTypeInfoForField",
    "parameter_types": "java.lang.reflect.Field,java.lang.reflect.Type",
    "dimensions": 6
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getTypeInfoForArray",
    "parameter_types": "java.lang.reflect.Type",
    "dimensions": 3
  }
]
```

## Production source gson/src/main/java/com/google/gson/TypeInfoFactory.java

```java
/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * A static factory class used to construct the "TypeInfo" objects.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
final class TypeInfoFactory {

  private TypeInfoFactory() {
    // Not instantiable since it provides factory methods only.
  }

  public static TypeInfoArray getTypeInfoForArray(Type type) {
    Preconditions.checkArgument(TypeUtils.isArray(type));
    return new TypeInfoArray(type);
  }

  /**
   * Evaluates the "actual" type for the field.  If the field is a "TypeVariable" or has a
   * "TypeVariable" in a parameterized type then it evaluates the real type.
   *
   * @param f the actual field object to retrieve the type from
   * @param typeDefiningF the type that contains the field {@code f}
   * @return the type information for the field
   */
  public static TypeInfo getTypeInfoForField(Field f, Type typeDefiningF) {
    Class<?> classDefiningF = TypeUtils.toRawClass(typeDefiningF);
    Type type = f.getGenericType();
    Type actualType = getActualType(type, typeDefiningF, classDefiningF);
    return new TypeInfo(actualType);
  }

  private static Type getActualType(
      Type typeToEvaluate, Type parentType, Class<?> rawParentClass) {
    if (typeToEvaluate instanceof Class<?>) {
      return typeToEvaluate;
    } else if (typeToEvaluate instanceof ParameterizedType) {
      ParameterizedType castedType = (ParameterizedType) typeToEvaluate;
      Type owner = castedType.getOwnerType();
      Type[] actualTypeParameters =
          extractRealTypes(castedType.getActualTypeArguments(), parentType, rawParentClass);
      Type rawType = castedType.getRawType();
      return new ParameterizedTypeImpl(rawType, actualTypeParameters, owner);
    } else if (typeToEvaluate instanceof GenericArrayType) {
      GenericArrayType castedType = (GenericArrayType) typeToEvaluate;
      Type componentType = castedType.getGenericComponentType();
      Type actualType = getActualType(componentType, parentType, rawParentClass);
      if (componentType.equals(actualType)) {
        return castedType;
      }
      return actualType instanceof Class<?> ?
          TypeUtils.wrapWithArray(TypeUtils.toRawClass(actualType))
          : new GenericArrayTypeImpl(actualType);
    } else if (typeToEvaluate instanceof TypeVariable<?>) {
      if (parentType instanceof ParameterizedType) {
        // The class definition has the actual types used for the type variables.
        // Find the matching actual type for the Type Variable used for the field.
        // For example, class Foo<A> { A a; }
        // new Foo<Integer>(); defines the actual type of A to be Integer.
        // So, to find the type of the field a, we will have to look at the class'
        // actual type arguments.
        TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) typeToEvaluate;
        TypeVariable<?>[] classTypeVariables = rawParentClass.getTypeParameters();
        ParameterizedType objParameterizedType = (ParameterizedType) parentType;
        int indexOfActualTypeArgument = getIndex(classTypeVariables, fieldTypeVariable);
        Type[] actualTypeArguments = objParameterizedType.getActualTypeArguments();
        return actualTypeArguments[indexOfActualTypeArgument];
      } else if (typeToEvaluate instanceof TypeVariable<?>) {
        Type theSearchedType = null;

        do {
          theSearchedType = extractTypeForHierarchy(parentType, (TypeVariable<?>) typeToEvaluate);
        } while ((theSearchedType != null) && (theSearchedType instanceof TypeVariable<?>));

        if (theSearchedType != null) {
          return theSearchedType;
        }
      }

      throw new UnsupportedOperationException("Expecting parameterized type, got " + parentType
          + ".\n Are you missing the use of TypeToken idiom?\n See "
          + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener");
    } else if (typeToEvaluate instanceof WildcardType) {
      WildcardType castedType = (WildcardType) typeToEvaluate;
      return getActualType(castedType.getUpperBounds()[0], parentType, rawParentClass);
    } else {
      throw new IllegalArgumentException("Type \'" + typeToEvaluate + "\' is not a Class, "
          + "ParameterizedType, GenericArrayType or TypeVariable. Can't extract type.");
    }
  }

  private static Type extractTypeForHierarchy(Type parentType, TypeVariable<?> typeToEvaluate) {
    Class<?> rawParentType = null;
    if (parentType instanceof Class<?>) {
      rawParentType = (Class<?>) parentType;
    } else if (parentType instanceof ParameterizedType) {
      ParameterizedType parentTypeAsPT = (ParameterizedType) parentType;
      rawParentType = (Class<?>) parentTypeAsPT.getRawType();
    } else {
      return null;
    }

    Type superClass = rawParentType.getGenericSuperclass();
    if (superClass instanceof ParameterizedType
        && ((ParameterizedType) superClass).getRawType() == typeToEvaluate.getGenericDeclaration()) {
      // Evaluate type on this type
      TypeVariable<?>[] classTypeVariables =
          ((Class<?>) ((ParameterizedType) superClass).getRawType()).getTypeParameters();
      int indexOfActualTypeArgument = getIndex(classTypeVariables, typeToEvaluate);

      Type[] actualTypeArguments = null;
      if (parentType instanceof Class<?>) {
        actualTypeArguments = ((ParameterizedType) superClass).getActualTypeArguments();
      } else if (parentType instanceof ParameterizedType) {
        actualTypeArguments = ((ParameterizedType) parentType).getActualTypeArguments();
      } else {
        return null;
      }

      return actualTypeArguments[indexOfActualTypeArgument];
    }

    Type searchedType = null;
    if (superClass != null) {
      searchedType = extractTypeForHierarchy(superClass, typeToEvaluate);
    }
    return searchedType;
  }

  private static Type[] extractRealTypes(
      Type[] actualTypeArguments, Type parentType, Class<?> rawParentClass) {
    Preconditions.checkNotNull(actualTypeArguments);

    Type[] retTypes = new Type[actualTypeArguments.length];
    for (int i = 0; i < actualTypeArguments.length; ++i) {
      retTypes[i] = getActualType(actualTypeArguments[i], parentType, rawParentClass);
    }
    return retTypes;
  }

  private static int getIndex(TypeVariable<?>[] types, TypeVariable<?> type) {
    for (int i = 0; i < types.length; ++i) {
      if (type.equals(types[i])) {
        return i;
      }
    }
    throw new IllegalStateException(
        "How can the type variable not be present in the class declaration!");
  }
}

```

## Build configuration gson/build.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="gson" default="package" basedir=".">

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

  <target name="compile" depends="gson-from-maven.compile"> </target>
  <target name="compile.tests" depends="gson-from-maven.compile-tests"> </target>

</project>

```

## Build configuration gson/pom.xml

```text
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <packaging>jar</packaging>
  <version>1.6</version>
  <inceptionYear>2008</inceptionYear>
  <name>Gson</name>
  <url>http://code.google.com/p/google-gson/</url>
  <description>Google Gson library</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url> http://google-gson.googlecode.com/svn/mavenrepo/ </url>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/gson</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/gson</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/gson</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <defaultGoal>package</defaultGoal>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <archive>
            <manifestEntries>
              <Bundle-RequiredExecutionEnvironment>J2SE-1.5</Bundle-RequiredExecutionEnvironment>
              <Export-Package><![CDATA[com.google.gson;version=1.6, com.google.gson.annotations;version=1.6, com.google.gson.reflect;version=1.6]]></Export-Package>
              <Bundle-Version>1.6</Bundle-Version>
              <Bundle-ContactAddress>http://code.google.com/p/google-gson/</Bundle-ContactAddress>
              <Bundle-Vendor>Google Gson Project</Bundle-Vendor>
              <Bundle-Name>${project.name}</Bundle-Name>
              <Bundle-Description>${project.description}</Bundle-Description>
              <Bundle-ClassPath>.</Bundle-ClassPath>
              <Bundle-ManifestVersion>2</Bundle-ManifestVersion>
              <Bundle-SymbolicName>com.google.gson</Bundle-SymbolicName>
            </manifestEntries>
          </archive>
        </configuration>
      </plugin>
      <plugin>
       <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-source-plugin</artifactId>
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
       <plugin>
         <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-javadoc-plugin</artifactId>
         <executions>
           <execution>
             <id>attach-javadocs</id>
             <goals>
               <goal>jar</goal>
             </goals>
           </execution>
         </executions>
         <configuration>
           <links>
             <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
           </links>
           <version>true</version>
           <show>public</show>
         </configuration>
       </plugin>
       <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-eclipse-plugin</artifactId>
         <configuration>
           <downloadSources>true</downloadSources>
           <downloadJavadocs>true</downloadJavadocs>
           <workspace>
             ../eclipse-ws/
           </workspace>
           <workspaceCodeStylesURL>
             file:///${basedir}/../lib/gson-formatting-styles.xml
           </workspaceCodeStylesURL>
         </configuration>
       </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>google-gson-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
    <developer>
      <name>Joel Leitch</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration proto/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>proto</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Protobuf Support</name>
  <description>Gson support for Protobufs</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/proto</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/proto</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/proto</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>

    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>com.google.protobuf</groupId>
      <artifactId>protobuf-java</artifactId>
      <version>2.3.0</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>gson-proto</finalName>
    <plugins>
      <plugin>
        <artifactId>maven-antrun-plugin</artifactId>
        <executions>
          <execution>
            <id>compile-protoc</id>
            <phase>generate-sources</phase>
            <configuration>
              <tasks>
                <mkdir dir="target/generated" />
                <path id="proto.path">
                  <fileset dir="src/main/protobuf">
                    <include name="**/*.proto" />
                  </fileset>
                </path>
                <pathconvert pathsep=" " property="proto.files" refid="proto.path" />
                <exec executable="protoc" failonerror="true">
                  <arg value="--java_out=src/main/java" />
                  <arg value="-I${project.basedir}/src/main/protobuf" />
                  <arg line="${proto.files}" />
                </exec>
              </tasks>
            </configuration>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/proto-sources.jar</file>
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>proto-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsclient/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsclient</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Client Framework</name>
  <description>A Framework for Writing Clients for JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsf</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsf</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsf</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>
  
  <dependencies>
   
    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsdef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsclient</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsf-sources.jar</file>          
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsf-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsdef/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsdef</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Definition Framework</name>
  <description>A Framework for Defining JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsf</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsf</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsf</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>
  
  <dependencies>
   
    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsdef</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsf-sources.jar</file>          
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsf-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsexample/client/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsexampleclient</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Example Client</name>
  <description>An example for Writing Clients for JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsexample/client</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsexample/client</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsexample/client</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>

    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsdef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsclient</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsexampledef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsexampleclient</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsexampleclient-sources.jar</file>
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsexampledef-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsexample/definition/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsexampledef</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Example</name>
  <description>An example for Writing Clients for JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsexample/definition</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsexample/definition</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsexample/definition</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>

    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsdef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsexampledef</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsexampledef-sources.jar</file>
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsexampledef-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsexample/server/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsexampleserver</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Example Client</name>
  <description>An example for Writing Clients for JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsexample/server</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsexample/server</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsexample/server</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>

    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsdef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsf</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsexampledef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>

    <!-- Servlet & Jsp -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.4</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsexampleserver</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsexampleserver-sources.jar</file>
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsexampleserver-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```

## Build configuration wsf/pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>wsf</artifactId>
  <packaging>jar</packaging>
  <version>0.1</version>
  <name>Gson Web Service Framework</name>
  <description>A Framework for JSON based Web-services</description>
  <distributionManagement>
    <repository>
      <id>local.repo</id>
      <name>file repository to svn</name>
      <url>file://${basedir}/../../mavenrepo</url>
    </repository>
  </distributionManagement>
  <repositories>
    <repository>
      <id>gson</id>
      <url>http://google-gson.googlecode.com/svn/mavenrepo</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:svn:http://google-gson.googlecode.com/svn/trunk/wsf</connection>
    <developerConnection>scm:svn:https://google-gson.googlecode.com/svn/trunk/wsf</developerConnection>
    <url>http://google-gson.codegoogle.com/svn/trunk/wsf</url>
  </scm>
  <issueManagement>
    <system>Google Code Issue Tracking</system>
    <url>http://code.google.com/p/google-gson/issues/list</url>
  </issueManagement>

  <dependencies>

    <!-- Google collections -->
    <dependency>
      <groupId>com.google.guava</groupId>
      <artifactId>guava</artifactId>
      <version>r07</version>
      <scope>compile</scope>
    </dependency>

    <!--  Gson: Java to Json conversion -->
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>1.5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>wsdef</artifactId>
      <version>0.1</version>
      <scope>compile</scope>
    </dependency>

    <!-- Guice: Dependency injection -->
    <dependency>
      <groupId>com.google.inject</groupId>
      <artifactId>guice</artifactId>
      <version>2.0</version>
      <scope>compile</scope>
    </dependency>

    <!-- Servlet & Jsp -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.4</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>wsf</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.0.2</version>
        <configuration>
          <source>1.6</source>
          <target>1.6</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-eclipse-plugin</artifactId>
        <configuration>
          <downloadSources>true</downloadSources>
          <downloadJavadocs>true</downloadJavadocs>
          <workspace>../eclipse-ws</workspace>
          <workspaceCodeStylesURL>file:///${basedir}/../lib/gson-formatting-styles.xml</workspaceCodeStylesURL>
          <wtpversion>1.5</wtpversion>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <!-- configuration>
          <updateReleaseInfo>true</updateReleaseInfo>
          <createChecksum>true</createChecksum>
          <groupId>${groupId}</groupId>
          <artifactId>${artifactId}</artifactId>
          <version>${version}</version>
          <packaging>jar</packaging>
          <classifier>sources</classifier>
          <file>target/wsf-sources.jar</file>          
        </configuration -->
      </plugin>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>maven-jetty-plugin</artifactId>
        <configuration>
          <scanIntervalSeconds>10</scanIntervalSeconds>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
      </plugin>
      <plugin>
      <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
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
      <plugin>
        <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>	
          <archive>src/main/resources/javadoc-descriptor.xml</archive>
          <links>
            <link>http://java.sun.com/j2se/1.5.0/docs/api/</link>
          </links>
          <version>true</version>
          <useDefaultManifestFile>true</useDefaultManifestFile>
          <show>public</show>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <configuration>
          <targetJdk>1.5</targetJdk>
          <rulesets>
            <ruleset>/rulesets/basic.xml</ruleset>
            <ruleset>/rulesets/imports.xml</ruleset>
            <ruleset>/rulesets/unusedcode.xml</ruleset>
            <ruleset>/rulesets/finalizers.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <!-- configLocation>config/sun_checks.xml</configLocation -->
          <configLocation>config/maven_checks.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>@todo</tag>
            <tag>FIXME</tag>
            <tag>XXX</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>clean</id>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptor>src/main/resources/assembly-descriptor.xml</descriptor>
          <finalName>wsf-${version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
      <organization>Google Inc.</organization>
    </developer>
    <developer>
      <name>Joel Leitch</name>
      <organization>Google Inc.</organization>
    </developer>
  </developers>
</project>

```
