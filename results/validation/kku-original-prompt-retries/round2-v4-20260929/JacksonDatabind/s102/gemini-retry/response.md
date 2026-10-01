```java
package com.fasterxml.jackson.databind.ser;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Regression unit tests for {@link BeanPropertyWriter}.
 */
public class BeanPropertyWriterTest {

    // ----------------------------------------------------------------------
    // Test Beans
    // ----------------------------------------------------------------------

    static class SampleBean {
        @JsonProperty("customName")
        public String fieldProp = "fieldValue";

        private int methodProp = 42;

        public SampleBean cycle;

        public int getMethodProp() {
            return methodProp;
        }

        public void setMethodProp(int methodProp) {
            this.methodProp = methodProp;
        }
```