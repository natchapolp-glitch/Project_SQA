## gson/src/main/java/com/google/gson/internal/bind/JsonTreeReader.java

```
/*
 * Copyright (C) 2011 Google Inc.
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

/**
 * This reader walks the elements of a JsonElement as if it was coming from a
 * character stream.
 *
 * @author Jesse Wilson
 */
public final class JsonTreeReader extends JsonReader {
  private static final Reader UNREADABLE_READER = new Reader() {
    @Override public int read(char[] buffer, int offset, int count) throws IOException {
      throw new AssertionError();
    }
    @Override public void close() throws IOException {
      throw new AssertionError();
    }
  };
  private static final Object SENTINEL_CLOSED = new Object();

  /*
   * The nesting stack. Using a manual array rather than an ArrayList saves 20%.
   */
  private Object[] stack = new Object[32];
  private int stackSize = 0;

  /*
   * The path members. It corresponds directly to stack: At indices where the
   * stack contains an object (EMPTY_OBJECT, DANGLING_NAME or NONEMPTY_OBJECT),
   * pathNames contains the name at this scope. Where it contains an array
   * (EMPTY_ARRAY, NONEMPTY_ARRAY) pathIndices contains the current index in
   * that array. Otherwise the value is undefined, and we take advantage of that
   * by incrementing pathIndices when doing so isn't useful.
   */
  private String[] pathNames = new String[32];
  private int[] pathIndices = new int[32];

  public JsonTreeReader(JsonElement element) {
    super(UNREADABLE_READER);
    push(element);
  }

  @Override public void beginArray() throws IOException {
    expect(JsonToken.BEGIN_ARRAY);
    JsonArray array = (JsonArray) peekStack();
    push(array.iterator());
    pathIndices[stackSize - 1] = 0;
  }

  @Override public void endArray() throws IOException {
    expect(JsonToken.END_ARRAY);
    popStack(); // empty iterator
    popStack(); // array
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
  }

  @Override public void beginObject() throws IOException {
    expect(JsonToken.BEGIN_OBJECT);
    JsonObject object = (JsonObject) peekStack();
    push(object.entrySet().iterator());
  }

  @Override public void endObject() throws IOException {
    expect(JsonToken.END_OBJECT);
    popStack(); // empty iterator
    popStack(); // object
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
  }

  @Override public boolean hasNext() throws IOException {
    JsonToken token = peek();
    return token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY;
  }

  @Override public JsonToken peek() throws IOException {
    if (stackSize == 0) {
      return JsonToken.END_DOCUMENT;
    }

    Object o = peekStack();
    if (o instanceof Iterator) {
      boolean isObject = stack[stackSize - 2] instanceof JsonObject;
      Iterator<?> iterator = (Iterator<?>) o;
      if (iterator.hasNext()) {
        if (isObject) {
          return JsonToken.NAME;
        } else {
          push(iterator.next());
          return peek();
        }
      } else {
        return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
      }
    } else if (o instanceof JsonObject) {
      return JsonToken.BEGIN_OBJECT;
    } else if (o instanceof JsonArray) {
      return JsonToken.BEGIN_ARRAY;
    } else if (o instanceof JsonPrimitive) {
      JsonPrimitive primitive = (JsonPrimitive) o;
      if (primitive.isString()) {
        return JsonToken.STRING;
      } else if (primitive.isBoolean()) {
        return JsonToken.BOOLEAN;
      } else if (primitive.isNumber()) {
        return JsonToken.NUMBER;
      } else {
        throw new AssertionError();
      }
    } else if (o instanceof JsonNull) {
      return JsonToken.NULL;
    } else if (o == SENTINEL_CLOSED) {
      throw new IllegalStateException("JsonReader is closed");
    } else {
      throw new AssertionError();
    }
  }

  private Object peekStack() {
    return stack[stackSize - 1];
  }

  private Object popStack() {
    Object result = stack[--stackSize];
    stack[stackSize] = null;
    return result;
  }

  private void expect(JsonToken expected) throws IOException {
    if (peek() != expected) {
      throw new IllegalStateException(
          "Expected " + expected + " but was " + peek() + locationString());
    }
  }

  @Override public String nextName() throws IOException {
    expect(JsonToken.NAME);
    Iterator<?> i = (Iterator<?>) peekStack();
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i.next();
    String result = (String) entry.getKey();
    pathNames[stackSize - 1] = result;
    push(entry.getValue());
    return result;
  }

  @Override public String nextString() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
      throw new IllegalStateException(
          "Expected " + JsonToken.STRING + " but was " + token + locationString());
    }
    String result = ((JsonPrimitive) popStack()).getAsString();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
    return result;
  }

  @Override public boolean nextBoolean() throws IOException {
    expect(JsonToken.BOOLEAN);
    boolean result = ((JsonPrimitive) popStack()).getAsBoolean();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
    return result;
  }

  @Override public void nextNull() throws IOException {
    expect(JsonToken.NULL);
    popStack();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
  }

  @Override public double nextDouble() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
      throw new IllegalStateException(
          "Expected " + JsonToken.NUMBER + " but was " + token + locationString());
    }
    double result = ((JsonPrimitive) peekStack()).getAsDouble();
    if (!isLenient() && (Double.isNaN(result) || Double.isInfinite(result))) {
      throw new NumberFormatException("JSON forbids NaN and infinities: " + result);
    }
    popStack();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
    return result;
  }

  @Override public long nextLong() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
      throw new IllegalStateException(
          "Expected " + JsonToken.NUMBER + " but was " + token + locationString());
    }
    long result = ((JsonPrimitive) peekStack()).getAsLong();
    popStack();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
    return result;
  }

  @Override public int nextInt() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
      throw new IllegalStateException(
          "Expected " + JsonToken.NUMBER + " but was " + token + locationString());
    }
    int result = ((JsonPrimitive) peekStack()).getAsInt();
    popStack();
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
    return result;
  }

  @Override public void close() throws IOException {
    stack = new Object[] { SENTINEL_CLOSED };
    stackSize = 1;
  }

  @Override public void skipValue() throws IOException {
    if (peek() == JsonToken.NAME) {
      nextName();
      pathNames[stackSize - 2] = "null";
    } else {
      popStack();
      if (stackSize > 0) {
        pathNames[stackSize - 1] = "null";
      }
    }
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }
  }

  @Override public String toString() {
    return getClass().getSimpleName();
  }

  public void promoteNameToValue() throws IOException {
    expect(JsonToken.NAME);
    Iterator<?> i = (Iterator<?>) peekStack();
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i.next();
    push(entry.getValue());
    push(new JsonPrimitive((String) entry.getKey()));
  }

  private void push(Object newTop) {
    if (stackSize == stack.length) {
      Object[] newStack = new Object[stackSize * 2];
      int[] newPathIndices = new int[stackSize * 2];
      String[] newPathNames = new String[stackSize * 2];
      System.arraycopy(stack, 0, newStack, 0, stackSize);
      System.arraycopy(pathIndices, 0, newPathIndices, 0, stackSize);
      System.arraycopy(pathNames, 0, newPathNames, 0, stackSize);
      stack = newStack;
      pathIndices = newPathIndices;
      pathNames = newPathNames;
    }
    stack[stackSize++] = newTop;
  }

  @Override public String getPath() {
    StringBuilder result = new StringBuilder().append('$');
    for (int i = 0; i < stackSize; i++) {
      if (stack[i] instanceof JsonArray) {
        if (stack[++i] instanceof Iterator) {
          result.append('[').append(pathIndices[i]).append(']');
        }
      } else if (stack[i] instanceof JsonObject) {
        if (stack[++i] instanceof Iterator) {
          result.append('.');
          if (pathNames[i] != null) {
            result.append(pathNames[i]);
          }
        }
      }
    }
    return result.toString();
  }

  private String locationString() {
    return " at path " + getPath();
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
  <version>2.8.1-SNAPSHOT</version>
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
        <version>4.12</version>
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
  <profiles>
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
</project>

```
