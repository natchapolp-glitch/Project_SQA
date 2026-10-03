## gson/src/main/java/com/google/gson/internal/bind/JsonAdapterAnnotationTypeAdapterFactory.java

```
/*
 * Copyright (C) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

/**
 * Given a type T, looks for the annotation {@link JsonAdapter} and uses an instance of the
 * specified class as the default type adapter.
 *
 * @since 2.3
 */
public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {

  private final ConstructorConstructor constructorConstructor;

  public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
    this.constructorConstructor = constructorConstructor;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
    JsonAdapter annotation = targetType.getRawType().getAnnotation(JsonAdapter.class);
    if (annotation == null) {
      return null;
    }
    return (TypeAdapter<T>) getTypeAdapter(constructorConstructor, gson, targetType, annotation);
  }

  @SuppressWarnings("unchecked") // Casts guarded by conditionals.
  static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson,
      TypeToken<?> fieldType, JsonAdapter annotation) {
    Class<?> value = annotation.value();
    TypeAdapter<?> typeAdapter;
    if (TypeAdapter.class.isAssignableFrom(value)) {
      Class<TypeAdapter<?>> typeAdapterClass = (Class<TypeAdapter<?>>) value;
      typeAdapter = constructorConstructor.get(TypeToken.get(typeAdapterClass)).construct();
    } else if (TypeAdapterFactory.class.isAssignableFrom(value)) {
      Class<TypeAdapterFactory> typeAdapterFactory = (Class<TypeAdapterFactory>) value;
      typeAdapter = constructorConstructor.get(TypeToken.get(typeAdapterFactory))
          .construct()
          .create(gson, fieldType);
    } else {
      throw new IllegalArgumentException(
          "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
    }
    if (typeAdapter != null) {
      typeAdapter = typeAdapter.nullSafe();
    }
    return typeAdapter;
  }
}

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.sonatype.oss</groupId>
    <artifactId>oss-parent</artifactId>
    <version>7</version>
  </parent>

  <groupId>com.google.code.gson</groupId>
  <artifactId>gson-parent</artifactId>
  <version>2.7-SNAPSHOT</version>
  <packaging>pom</packaging>

  <name>Gson Parent</name>
  <description>Gson JSON library</description>
  <url>https://github.com/google/gson</url>

  <modules>
    <module>gson</module>
  </modules>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.6</java.version>
  </properties>

  <scm>
    <url>https://github.com/google/gson/</url>
    <connection>scm:git:https://github.com/google/gson.git</connection>
    <developerConnection>scm:git:git@github.com:google/gson.git</developerConnection>
    <tag>HEAD</tag>
  </scm>

  <issueManagement>
    <system>GitHub Issues</system>
    <url>https://github.com/google/gson/issues</url>
  </issueManagement>

  <licenses>
    <license>
      <name>Apache 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>3.8.2</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.0</version>
          <configuration>
            <source>${java.version}</source>
            <target>${java.version}</target>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <version>2.10.3</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-jar-plugin</artifactId>
          <configuration>
            <useDefaultManifestFile>true</useDefaultManifestFile>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <version>2.4.2</version>
        <dependencies>
          <dependency>
            <groupId>org.apache.maven.scm</groupId>
            <artifactId>maven-scm-provider-gitexe</artifactId>
            <version>1.9</version>
          </dependency>
        </dependencies>
        <configuration>
          <autoVersionSubmodules>true</autoVersionSubmodules>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>

```
