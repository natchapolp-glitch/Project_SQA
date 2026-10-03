## gson/src/main/java/com/google/gson/internal/ConstructorConstructor.java

```
/*
 * Copyright (C) 2011 Google Inc.
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

package com.google.gson.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

/**
 * Returns a function that can construct an instance of a requested type.
 */
public final class ConstructorConstructor {
  private final Map<Type, InstanceCreator<?>> instanceCreators;

  public ConstructorConstructor(Map<Type, InstanceCreator<?>> instanceCreators) {
    this.instanceCreators = instanceCreators;
  }

  public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) {
    final Type type = typeToken.getType();
    final Class<? super T> rawType = typeToken.getRawType();

    // first try an instance creator

    @SuppressWarnings("unchecked") // types must agree
    final InstanceCreator<T> typeCreator = (InstanceCreator<T>) instanceCreators.get(type);
    if (typeCreator != null) {
      return new ObjectConstructor<T>() {
        @Override public T construct() {
          return typeCreator.createInstance(type);
        }
      };
    }

    // Next try raw type match for instance creators
    @SuppressWarnings("unchecked") // types must agree
    final InstanceCreator<T> rawTypeCreator =
        (InstanceCreator<T>) instanceCreators.get(rawType);
    if (rawTypeCreator != null) {
      return new ObjectConstructor<T>() {
        @Override public T construct() {
          return rawTypeCreator.createInstance(type);
        }
      };
    }

    ObjectConstructor<T> defaultConstructor = newDefaultConstructor(rawType);
    if (defaultConstructor != null) {
      return defaultConstructor;
    }

    ObjectConstructor<T> defaultImplementation = newDefaultImplementationConstructor(type, rawType);
    if (defaultImplementation != null) {
      return defaultImplementation;
    }

    // finally try unsafe
    return newUnsafeAllocator(type, rawType);
  }

  private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> rawType) {
    try {
      final Constructor<? super T> constructor = rawType.getDeclaredConstructor();
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return new ObjectConstructor<T>() {
        @SuppressWarnings("unchecked") // T is the same raw type as is requested
        @Override public T construct() {
          try {
            Object[] args = null;
            return (T) constructor.newInstance(args);
          } catch (InstantiationException e) {
            // TODO: JsonParseException ?
            throw new RuntimeException("Failed to invoke " + constructor + " with no args", e);
          } catch (InvocationTargetException e) {
            // TODO: don't wrap if cause is unchecked!
            // TODO: JsonParseException ?
            throw new RuntimeException("Failed to invoke " + constructor + " with no args",
                e.getTargetException());
          } catch (IllegalAccessException e) {
            throw new AssertionError(e);
          }
        }
      };
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  /**
   * Constructors for common interface types like Map and List and their
   * subtypes.
   */
  @SuppressWarnings("unchecked") // use runtime checks to guarantee that 'T' is what it is
  private <T> ObjectConstructor<T> newDefaultImplementationConstructor(
      final Type type, Class<? super T> rawType) {
    if (Collection.class.isAssignableFrom(rawType)) {
      if (SortedSet.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new TreeSet<Object>();
          }
        };
      } else if (EnumSet.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @SuppressWarnings("rawtypes")
          @Override public T construct() {
            if (type instanceof ParameterizedType) {
              Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
              if (elementType instanceof Class) {
                return (T) EnumSet.noneOf((Class)elementType);
              } else {
                throw new JsonIOException("Invalid EnumSet type: " + type.toString());
              }
            } else {
              throw new JsonIOException("Invalid EnumSet type: " + type.toString());
            }
          }
        };
      } else if (Set.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new LinkedHashSet<Object>();
          }
        };
      } else if (Queue.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new LinkedList<Object>();
          }
        };
      } else {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new ArrayList<Object>();
          }
        };
      }
    }

    if (Map.class.isAssignableFrom(rawType)) {
      if (ConcurrentNavigableMap.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new ConcurrentSkipListMap<Object, Object>();
          }
        };
      } else if (ConcurrentMap.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new ConcurrentHashMap<Object, Object>();
          }
        };
      } else if (SortedMap.class.isAssignableFrom(rawType)) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new TreeMap<Object, Object>();
          }
        };
      } else if (type instanceof ParameterizedType && !(String.class.isAssignableFrom(
          TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType()))) {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new LinkedHashMap<Object, Object>();
          }
        };
      } else {
        return new ObjectConstructor<T>() {
          @Override public T construct() {
            return (T) new LinkedTreeMap<String, Object>();
          }
        };
      }
    }

    return null;
  }

  private <T> ObjectConstructor<T> newUnsafeAllocator(
      final Type type, final Class<? super T> rawType) {
    return new ObjectConstructor<T>() {
      private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
      @SuppressWarnings("unchecked")
      @Override public T construct() {
        try {
          Object newInstance = unsafeAllocator.newInstance(rawType);
          return (T) newInstance;
        } catch (Exception e) {
          throw new RuntimeException(("Unable to invoke no-args constructor for " + type + ". "
              + "Register an InstanceCreator with Gson for this type may fix this problem."), e);
        }
      }
    };
  }

  @Override public String toString() {
    return instanceCreators.toString();
  }
}

```

## pom.xml

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson-parent</artifactId>
  <packaging>pom</packaging>
  <version>2.5-SNAPSHOT</version>
  <inceptionYear>2008</inceptionYear>
  <name>Gson Parent</name>
  <parent>
    <groupId>org.sonatype.oss</groupId>
    <artifactId>oss-parent</artifactId>
    <version>9</version>
    <relativePath />
  </parent>
  <url>https://github.com/google/gson</url>
  <description>Google Gson library</description>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:https://github.com/google/gson.git</connection>
    <developerConnection>scm:git:https://github.com/google/gson.git</developerConnection>
    <url>https://github.com/google/gson</url>
    <tag>gson-${project.version}</tag>
  </scm>
  <issueManagement>
    <system>Github Issue Tracking</system>
    <url>https://github.com/google/gson/issues</url>
  </issueManagement>
  <distributionManagement>
    <snapshotRepository>
      <id>sonatype-nexus-snapshots</id>
      <name>Sonatype Nexus Snapshots</name>
      <url>https://repo1.maven.org/maven2/</url>
    </snapshotRepository>
    <repository>
      <id>sonatype-nexus-staging</id>
      <name>Nexus Release Repository</name>
      <url>http://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
  </distributionManagement>
  <organization>
    <name>Google, Inc.</name>
    <url>http://www.google.com</url>
  </organization>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <modules>
    <module>gson</module>
  </modules>
  <profiles>
    <!-- Activate PGP signing only when performing a release -->
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
            <version>1.5</version>
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
    <profile>
      <id>doclint-java8-disable</id>
      <activation>
        <jdk>[1.8,)</jdk>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <configuration>
              <additionalparam>-Xdoclint:none</additionalparam>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
  <build>
    <defaultGoal>package</defaultGoal>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.3</version>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.6</version>
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
            <addMavenDescriptor>false</addMavenDescriptor>
            <manifestEntries>
              <Bundle-RequiredExecutionEnvironment>J2SE-1.5, JavaSE-1.6, JavaSE-1.7, JavaSE-1.8</Bundle-RequiredExecutionEnvironment>
              <Export-Package><![CDATA[com.google.gson;version=${project.version}, com.google.gson.annotations;version=${project.version}, com.google.gson.reflect;version=${project.version}, com.google.gson.stream;version=${project.version}, com.google.gson.internal;version=${project.version}, com.google.gson.internal.bind;version=${project.version}]]></Export-Package>
              <Bundle-Version>${project.version}</Bundle-Version>
              <Bundle-ContactAddress>https://github.com/google/gson</Bundle-ContactAddress>
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
         <version>2.4</version>
         <executions>
           <execution>
             <id>attach-sources</id>
             <phase>verify</phase>
             <goals>
               <goal>jar</goal>
             </goals>
           </execution>
         </executions>
         <configuration>
           <archive>
             <manifestEntries>
               <Eclipse-SourceBundle>com.google.gson;version="${project.version}"</Eclipse-SourceBundle>
               <Bundle-SymbolicName>com.google.gson.source</Bundle-SymbolicName>
               <Bundle-ManifestVersion>2</Bundle-ManifestVersion>
               <Bundle-Version>${project.version}</Bundle-Version>
             </manifestEntries>
           </archive>
         </configuration>
       </plugin>
       <plugin>
         <!-- TODO(inder): add manifest entry for maven group, artifact, and classifier ids -->
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-javadoc-plugin</artifactId>
         <version>2.10.3</version>
         <executions>
           <execution>
             <id>attach-javadocs</id>
             <goals>
               <goal>jar</goal>
             </goals>
           </execution>
         </executions>
         <configuration>
           <includePackageNames>com.google.gson</includePackageNames>
           <excludePackageNames>com.google.gson.internal:com.google.gson.internal.bind</excludePackageNames>
            <links>
             <link>http://docs.oracle.com/javase/1.5.0/docs/api/</link>
           </links>
           <version>true</version>
           <show>protected</show>
         </configuration>
       </plugin>
       <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-eclipse-plugin</artifactId>
         <version>2.10</version>
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
        <artifactId>maven-release-plugin</artifactId>
        <!-- version>2.5</version -->
        <configuration>
          <arguments>-DenableCiProfile=true</arguments>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>2.5.5</version>
        <configuration>
          <descriptor>assembly-descriptor.xml</descriptor>
          <finalName>google-gson-${project.version}</finalName>
          <outputDirectory>target/dist</outputDirectory>
          <workDirectory>target/assembly/work</workDirectory>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <developers>
    <developer>
      <name>Inderjeet Singh</name>
    </developer>
    <developer>
      <name>Joel Leitch</name>
      <organization>Google Inc.</organization>
    </developer>
    <developer>
      <name>Jesse Wilson</name>
      <organization>Square Inc.</organization>
    </developer>
  </developers>
</project>

```
