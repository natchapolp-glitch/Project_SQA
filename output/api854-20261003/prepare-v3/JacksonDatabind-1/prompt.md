Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: JacksonDatabind; fixed revision: 1f.
Modified target classes:
com.fasterxml.jackson.databind.ser.BeanPropertyWriter

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "_findAndAddDynamic",
    "parameter_types": "com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap,java.lang.Class,com.fasterxml.jackson.databind.SerializerProvider"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "_handleSelfReference",
    "parameter_types": "java.lang.Object,com.fasterxml.jackson.databind.JsonSerializer"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "assignNullSerializer",
    "parameter_types": "com.fasterxml.jackson.databind.JsonSerializer"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "assignSerializer",
    "parameter_types": "com.fasterxml.jackson.databind.JsonSerializer"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "depositSchemaProperty",
    "parameter_types": "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "depositSchemaProperty",
    "parameter_types": "com.fasterxml.jackson.databind.node.ObjectNode,com.fasterxml.jackson.databind.SerializerProvider"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "get",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getAnnotation",
    "parameter_types": "java.lang.Class"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getContextAnnotation",
    "parameter_types": "java.lang.Class"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getGenericPropertyType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getInternalSetting",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getMember",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getName",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getPropertyType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getRawSerializationType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getSerializationType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getSerializedName",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getSerializer",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getType",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getViews",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "getWrapperName",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "hasNullSerializer",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "hasSerializer",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "isRequired",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "isRequired",
    "parameter_types": "com.fasterxml.jackson.databind.AnnotationIntrospector"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "removeInternalSetting",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "rename",
    "parameter_types": "com.fasterxml.jackson.databind.util.NameTransformer"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "serializeAsColumn",
    "parameter_types": "java.lang.Object,com.fasterxml.jackson.core.JsonGenerator,com.fasterxml.jackson.databind.SerializerProvider"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "serializeAsField",
    "parameter_types": "java.lang.Object,com.fasterxml.jackson.core.JsonGenerator,com.fasterxml.jackson.databind.SerializerProvider"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "serializeAsPlaceholder",
    "parameter_types": "java.lang.Object,com.fasterxml.jackson.core.JsonGenerator,com.fasterxml.jackson.databind.SerializerProvider"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "setInternalSetting",
    "parameter_types": "java.lang.Object,java.lang.Object"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "setNonTrivialBaseType",
    "parameter_types": "com.fasterxml.jackson.databind.JavaType"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "toString",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "unwrappingWriter",
    "parameter_types": "com.fasterxml.jackson.databind.util.NameTransformer"
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "method": "willSuppressNulls",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
    "constructor_types": "com.fasterxml.jackson.databind.ser.BeanPropertyWriter,com.fasterxml.jackson.core.io.SerializedString",
    "method": "<init>",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "com.fasterxml.jackson.databind.AbstractTypeResolver",
  "com.fasterxml.jackson.databind.AnnotationIntrospector",
  "com.fasterxml.jackson.databind.BeanDescription",
  "com.fasterxml.jackson.databind.BeanProperty",
  "com.fasterxml.jackson.databind.DatabindContext",
  "com.fasterxml.jackson.databind.DeserializationConfig",
  "com.fasterxml.jackson.databind.DeserializationContext",
  "com.fasterxml.jackson.databind.DeserializationFeature",
  "com.fasterxml.jackson.databind.InjectableValues",
  "com.fasterxml.jackson.databind.JavaType",
  "com.fasterxml.jackson.databind.JsonDeserializer",
  "com.fasterxml.jackson.databind.JsonMappingException",
  "com.fasterxml.jackson.databind.JsonNode",
  "com.fasterxml.jackson.databind.JsonSerializable",
  "com.fasterxml.jackson.databind.JsonSerializer",
  "com.fasterxml.jackson.databind.KeyDeserializer",
  "com.fasterxml.jackson.databind.MapperFeature",
  "com.fasterxml.jackson.databind.MappingIterator",
  "com.fasterxml.jackson.databind.MappingJsonFactory",
  "com.fasterxml.jackson.databind.Module",
  "com.fasterxml.jackson.databind.ObjectMapper",
  "com.fasterxml.jackson.databind.ObjectReader",
  "com.fasterxml.jackson.databind.ObjectWriter",
  "com.fasterxml.jackson.databind.PropertyName",
  "com.fasterxml.jackson.databind.PropertyNamingStrategy",
  "com.fasterxml.jackson.databind.RuntimeJsonMappingException",
  "com.fasterxml.jackson.databind.SerializationConfig",
  "com.fasterxml.jackson.databind.SerializationFeature",
  "com.fasterxml.jackson.databind.SerializerProvider",
  "com.fasterxml.jackson.databind.annotation.JacksonStdImpl",
  "com.fasterxml.jackson.databind.annotation.JsonDeserialize",
  "com.fasterxml.jackson.databind.annotation.JsonNaming",
  "com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder",
  "com.fasterxml.jackson.databind.annotation.JsonSerialize",
  "com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver",
  "com.fasterxml.jackson.databind.annotation.JsonTypeResolver",
  "com.fasterxml.jackson.databind.annotation.JsonValueInstantiator",
  "com.fasterxml.jackson.databind.annotation.NoClass",
  "com.fasterxml.jackson.databind.cfg.BaseSettings",
  "com.fasterxml.jackson.databind.cfg.ConfigFeature",
  "com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig",
  "com.fasterxml.jackson.databind.cfg.HandlerInstantiator",
  "com.fasterxml.jackson.databind.cfg.MapperConfig",
  "com.fasterxml.jackson.databind.cfg.MapperConfigBase",
  "com.fasterxml.jackson.databind.cfg.PackageVersion",
  "com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig",
  "com.fasterxml.jackson.databind.deser.AbstractDeserializer",
  "com.fasterxml.jackson.databind.deser.BasicDeserializerFactory",
  "com.fasterxml.jackson.databind.deser.BeanDeserializer",
  "com.fasterxml.jackson.databind.deser.BeanDeserializerBase",
  "com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder",
  "com.fasterxml.jackson.databind.deser.BeanDeserializerFactory",
  "com.fasterxml.jackson.databind.deser.BeanDeserializerModifier",
  "com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer",
  "com.fasterxml.jackson.databind.deser.ContextualDeserializer",
  "com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer",
  "com.fasterxml.jackson.databind.deser.CreatorProperty",
  "com.fasterxml.jackson.databind.deser.DataFormatReaders",
  "com.fasterxml.jackson.databind.deser.DefaultDeserializationContext",
  "com.fasterxml.jackson.databind.deser.DeserializationProblemHandler",
  "com.fasterxml.jackson.databind.deser.DeserializerCache",
  "com.fasterxml.jackson.databind.deser.DeserializerFactory",
  "com.fasterxml.jackson.databind.deser.Deserializers",
  "com.fasterxml.jackson.databind.deser.KeyDeserializers",
  "com.fasterxml.jackson.databind.deser.ResolvableDeserializer",
  "com.fasterxml.jackson.databind.deser.SettableAnyProperty",
  "com.fasterxml.jackson.databind.deser.SettableBeanProperty",
  "com.fasterxml.jackson.databind.deser.ValueInstantiator",
  "com.fasterxml.jackson.databind.deser.ValueInstantiators",
  "com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap",
  "com.fasterxml.jackson.databind.deser.impl.CreatorCollector",
  "com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler",
  "com.fasterxml.jackson.databind.deser.impl.FieldProperty",
  "com.fasterxml.jackson.databind.deser.impl.InnerClassProperty",
  "com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty",
  "com.fasterxml.jackson.databind.deser.impl.MethodProperty",
  "com.fasterxml.jackson.databind.deser.impl.NullProvider",
  "com.fasterxml.jackson.databind.deser.impl.ObjectIdReader",
  "com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty",
  "com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator",
  "com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator",
  "com.fasterxml.jackson.databind.deser.impl.PropertyValue",
  "com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer",
  "com.fasterxml.jackson.databind.deser.impl.ReadableObjectId",
  "com.fasterxml.jackson.databind.deser.impl.SetterlessProperty",
  "com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler",
  "com.fasterxml.jackson.databind.deser.impl.ValueInjector",
  "com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer",
  "com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ClassDeserializer",
  "com.fasterxml.jackson.databind.deser.std.CollectionDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase",
  "com.fasterxml.jackson.databind.deser.std.DateDeserializers",
  "com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumMapDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumSetDeserializer",
  "com.fasterxml.jackson.databind.deser.std.FromStringDeserializer",
  "com.fasterxml.jackson.databind.deser.std.JacksonDeserializers",
  "com.fasterxml.jackson.databind.deser.std.JdkDeserializers",
  "com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer",
  "com.fasterxml.jackson.databind.deser.std.MapDeserializer",
  "com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.NumberDeserializers",
  "com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers",
  "com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers",
  "com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdValueInstantiator",
  "com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StringDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer",
  "com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer",
  "com.fasterxml.jackson.databind.exc.InvalidFormatException",
  "com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException",
  "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers",
  "com.fasterxml.jackson.databind.ext.CoreXMLSerializers",
  "com.fasterxml.jackson.databind.ext.DOMDeserializer",
  "com.fasterxml.jackson.databind.ext.DOMSerializer",
  "com.fasterxml.jackson.databind.ext.OptionalHandlerFactory",
  "com.fasterxml.jackson.databind.introspect.Annotated",
  "com.fasterxml.jackson.databind.introspect.AnnotatedClass",
  "com.fasterxml.jackson.databind.introspect.AnnotatedConstructor",
  "com.fasterxml.jackson.databind.introspect.AnnotatedField",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMember",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMethod",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap",
  "com.fasterxml.jackson.databind.introspect.AnnotatedParameter",
  "com.fasterxml.jackson.databind.introspect.AnnotatedWithParams",
  "com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair",
  "com.fasterxml.jackson.databind.introspect.AnnotationMap",
  "com.fasterxml.jackson.databind.introspect.BasicBeanDescription",
  "com.fasterxml.jackson.databind.introspect.BasicClassIntrospector",
  "com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition",
  "com.fasterxml.jackson.databind.introspect.ClassIntrospector",
  "com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector",
  "com.fasterxml.jackson.databind.introspect.MemberKey",
  "com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector",
  "com.fasterxml.jackson.databind.introspect.ObjectIdInfo",
  "com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector",
  "com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder",
  "com.fasterxml.jackson.databind.introspect.VisibilityChecker",
  "com.fasterxml.jackson.databind.introspect.WithMember",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWithSerializerProvider",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat",
  "com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormatVisitor",
  "com.fasterxml.jackson.databind.jsonschema.JsonSchema",
  "com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema",
  "com.fasterxml.jackson.databind.jsonschema.SchemaAware",
  "com.fasterxml.jackson.databind.jsontype.NamedType",
  "com.fasterxml.jackson.databind.jsontype.SubtypeResolver",
  "com.fasterxml.jackson.databind.jsontype.TypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.TypeIdResolver",
  "com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder",
  "com.fasterxml.jackson.databind.jsontype.TypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.FailingDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder",
  "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
  "com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase",
  "com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.TypeSerializerBase",
  "com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver",
  "com.fasterxml.jackson.databind.module.SimpleDeserializers",
  "com.fasterxml.jackson.databind.module.SimpleKeyDeserializers",
  "com.fasterxml.jackson.databind.module.SimpleModule",
  "com.fasterxml.jackson.databind.module.SimpleSerializers",
  "com.fasterxml.jackson.databind.module.SimpleValueInstantiators",
  "com.fasterxml.jackson.databind.node.ArrayNode",
  "com.fasterxml.jackson.databind.node.BaseJsonNode",
  "com.fasterxml.jackson.databind.node.BigIntegerNode",
  "com.fasterxml.jackson.databind.node.BinaryNode",
  "com.fasterxml.jackson.databind.node.BooleanNode",
  "com.fasterxml.jackson.databind.node.ContainerNode",
  "com.fasterxml.jackson.databind.node.DecimalNode",
  "com.fasterxml.jackson.databind.node.DoubleNode",
  "com.fasterxml.jackson.databind.node.FloatNode",
  "com.fasterxml.jackson.databind.node.IntNode",
  "com.fasterxml.jackson.databind.node.JsonNodeFactory",
  "com.fasterxml.jackson.databind.node.JsonNodeType",
  "com.fasterxml.jackson.databind.node.LongNode",
  "com.fasterxml.jackson.databind.node.MissingNode",
  "com.fasterxml.jackson.databind.node.NodeCursor",
  "com.fasterxml.jackson.databind.node.NullNode",
  "com.fasterxml.jackson.databind.node.NumericNode",
  "com.fasterxml.jackson.databind.node.ObjectNode",
  "com.fasterxml.jackson.databind.node.POJONode",
  "com.fasterxml.jackson.databind.node.ShortNode",
  "com.fasterxml.jackson.databind.node.TextNode",
  "com.fasterxml.jackson.databind.node.TreeTraversingParser",
  "com.fasterxml.jackson.databind.node.ValueNode",
  "com.fasterxml.jackson.databind.ser.AnyGetterWriter",
  "com.fasterxml.jackson.databind.ser.BasicSerializerFactory",
  "com.fasterxml.jackson.databind.ser.BeanPropertyFilter",
  "com.fasterxml.jackson.databind.ser.BeanPropertyWriter",
  "com.fasterxml.jackson.databind.ser.BeanSerializer",
  "com.fasterxml.jackson.databind.ser.BeanSerializerBuilder",
  "com.fasterxml.jackson.databind.ser.BeanSerializerFactory",
  "com.fasterxml.jackson.databind.ser.BeanSerializerModifier",
  "com.fasterxml.jackson.databind.ser.ContainerSerializer",
  "com.fasterxml.jackson.databind.ser.ContextualSerializer",
  "com.fasterxml.jackson.databind.ser.DefaultSerializerProvider",
  "com.fasterxml.jackson.databind.ser.FilterProvider",
  "com.fasterxml.jackson.databind.ser.PropertyBuilder",
  "com.fasterxml.jackson.databind.ser.ResolvableSerializer",
  "com.fasterxml.jackson.databind.ser.SerializerCache",
  "com.fasterxml.jackson.databind.ser.SerializerFactory",
  "com.fasterxml.jackson.databind.ser.Serializers",
  "com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer",
  "com.fasterxml.jackson.databind.ser.impl.FailingSerializer",
  "com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter",
  "com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer",
  "com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer",
  "com.fasterxml.jackson.databind.ser.impl.IteratorSerializer",
  "com.fasterxml.jackson.databind.ser.impl.JsonSerializerMap",
  "com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter",
  "com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator",
  "com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap",
  "com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap",
  "com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter",
  "com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider",
  "com.fasterxml.jackson.databind.ser.impl.StringArraySerializer",
  "com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer",
  "com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer",
  "com.fasterxml.jackson.databind.ser.impl.UnknownSerializer",
  "com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter",
  "com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer",
  "com.fasterxml.jackson.databind.ser.impl.WritableObjectId",
  "com.fasterxml.jackson.databind.ser.std.ArraySerializerBase",
  "com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase",
  "com.fasterxml.jackson.databind.ser.std.BeanSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.BooleanSerializer",
  "com.fasterxml.jackson.databind.ser.std.CalendarSerializer",
  "com.fasterxml.jackson.databind.ser.std.CollectionSerializer",
  "com.fasterxml.jackson.databind.ser.std.DateSerializer",
  "com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.EnumMapSerializer",
  "com.fasterxml.jackson.databind.ser.std.EnumSerializer",
  "com.fasterxml.jackson.databind.ser.std.EnumSetSerializer",
  "com.fasterxml.jackson.databind.ser.std.InetAddressSerializer",
  "com.fasterxml.jackson.databind.ser.std.IterableSerializer",
  "com.fasterxml.jackson.databind.ser.std.JsonValueSerializer",
  "com.fasterxml.jackson.databind.ser.std.MapSerializer",
  "com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.NullSerializer",
  "com.fasterxml.jackson.databind.ser.std.NumberSerializers",
  "com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer",
  "com.fasterxml.jackson.databind.ser.std.RawSerializer",
  "com.fasterxml.jackson.databind.ser.std.SerializableSerializer",
  "com.fasterxml.jackson.databind.ser.std.SqlDateSerializer",
  "com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer",
  "com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.StdArraySerializers",
  "com.fasterxml.jackson.databind.ser.std.StdContainerSerializers",
  "com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer",
  "com.fasterxml.jackson.databind.ser.std.StdJdkSerializers",
  "com.fasterxml.jackson.databind.ser.std.StdKeySerializer",
  "com.fasterxml.jackson.databind.ser.std.StdKeySerializers",
  "com.fasterxml.jackson.databind.ser.std.StdScalarSerializer",
  "com.fasterxml.jackson.databind.ser.std.StdSerializer",
  "com.fasterxml.jackson.databind.ser.std.StringSerializer",
  "com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer",
  "com.fasterxml.jackson.databind.ser.std.ToStringSerializer",
  "com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer",
  "com.fasterxml.jackson.databind.type.ArrayType",
  "com.fasterxml.jackson.databind.type.ClassKey",
  "com.fasterxml.jackson.databind.type.CollectionLikeType",
  "com.fasterxml.jackson.databind.type.CollectionType",
  "com.fasterxml.jackson.databind.type.HierarchicType",
  "com.fasterxml.jackson.databind.type.MapLikeType",
  "com.fasterxml.jackson.databind.type.MapType",
  "com.fasterxml.jackson.databind.type.SimpleType",
  "com.fasterxml.jackson.databind.type.TypeBase",
  "com.fasterxml.jackson.databind.type.TypeBindings",
  "com.fasterxml.jackson.databind.type.TypeFactory",
  "com.fasterxml.jackson.databind.type.TypeModifier",
  "com.fasterxml.jackson.databind.type.TypeParser",
  "com.fasterxml.jackson.databind.util.Annotations",
  "com.fasterxml.jackson.databind.util.ArrayBuilders",
  "com.fasterxml.jackson.databind.util.BeanUtil",
  "com.fasterxml.jackson.databind.util.ClassUtil",
  "com.fasterxml.jackson.databind.util.Converter",
  "com.fasterxml.jackson.databind.util.EmptyIterator",
  "com.fasterxml.jackson.databind.util.EnumResolver",
  "com.fasterxml.jackson.databind.util.EnumValues",
  "com.fasterxml.jackson.databind.util.ISO8601DateFormat",
  "com.fasterxml.jackson.databind.util.ISO8601Utils",
  "com.fasterxml.jackson.databind.util.JSONPObject",
  "com.fasterxml.jackson.databind.util.JSONWrappedObject",
  "com.fasterxml.jackson.databind.util.LRUMap",
  "com.fasterxml.jackson.databind.util.LinkedNode",
  "com.fasterxml.jackson.databind.util.NameTransformer",
  "com.fasterxml.jackson.databind.util.Named",
  "com.fasterxml.jackson.databind.util.ObjectBuffer",
  "com.fasterxml.jackson.databind.util.ObjectIdMap",
  "com.fasterxml.jackson.databind.util.PrimitiveArrayBuilder",
  "com.fasterxml.jackson.databind.util.Provider",
  "com.fasterxml.jackson.databind.util.RootNameLookup",
  "com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition",
  "com.fasterxml.jackson.databind.util.StdConverter",
  "com.fasterxml.jackson.databind.util.StdDateFormat",
  "com.fasterxml.jackson.databind.util.TokenBuffer",
  "com.fasterxml.jackson.databind.util.ViewMatcher"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jackson-databind" default="package" basedir=".">

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

  <target name="compile" depends="jackson-databind-from-maven.compile"> </target>
  <target name="compile.tests" depends="jackson-databind-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!--
 |  Copyright 2012 FasterXML.com
 |
 |  Licensed under the Apache License, Version 2.0 (the "License");
 |  you may not use this file except in compliance with the License.
 |  You may obtain a copy of the License at
 |
 |  http://www.apache.org/licenses/LICENSE-2.0
 |
 |  Unless required by applicable law or agreed to in writing, software
 |  distributed under the License is distributed on an "AS IS" BASIS,
 |  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 |  See the License for the specific language governing permissions and
 |  limitations under the License.
-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.fasterxml</groupId>
    <artifactId>oss-parent</artifactId>
    <version>10</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.2.2-SNAPSHOT</version>

  <name>jackson-databind</name>
  <description>General data-binding functionality for Jackson: works on core streaming API</description>
  <url>http://wiki.fasterxml.com/JacksonHome</url>

  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-databind.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-databind.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-databind</url>
    <tag>jackson-databind-2.2.1</tag>
  </scm>

  <properties>
    <osgi.export>
com.fasterxml.jackson.databind,
com.fasterxml.jackson.databind.annotation,
com.fasterxml.jackson.databind.cfg,
com.fasterxml.jackson.databind.deser,
com.fasterxml.jackson.databind.deser.impl,
com.fasterxml.jackson.databind.deser.std,
com.fasterxml.jackson.databind.exc,
com.fasterxml.jackson.databind.ext,
com.fasterxml.jackson.databind.introspect,
com.fasterxml.jackson.databind.jsonschema,
com.fasterxml.jackson.databind.jsonFormatVisitors,
com.fasterxml.jackson.databind.jsontype,
com.fasterxml.jackson.databind.jsontype.impl,
com.fasterxml.jackson.databind.module,
com.fasterxml.jackson.databind.node,
com.fasterxml.jackson.databind.ser,
com.fasterxml.jackson.databind.ser.impl,
com.fasterxml.jackson.databind.ser.std,
com.fasterxml.jackson.databind.type,
com.fasterxml.jackson.databind.util
    </osgi.export>
    <osgi.import>
com.fasterxml.jackson.annotation,
com.fasterxml.jackson.core,
com.fasterxml.jackson.core.base,
com.fasterxml.jackson.core.format,
com.fasterxml.jackson.core.json,
com.fasterxml.jackson.core.io,
com.fasterxml.jackson.core.util,
com.fasterxml.jackson.core.type,
org.xml.sax,org.w3c.dom, org.w3c.dom.bootstrap, org.w3c.dom.ls,
javax.xml.datatype, javax.xml.namespace, javax.xml.parsers
</osgi.import>

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/databind/cfg</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.databind.cfg</packageVersion.package>
  </properties>

  <dependencies>
    <!-- Builds on core streaming API; also needs core annotations -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.2.1</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.2.1</version>
    </dependency>

    <!-- and for testing, JUnit is needed, as well as quite a few
         libs for which we use reflection for code, but direct dep for testing
      -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.10</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>cglib</groupId>
      <artifactId>cglib</artifactId>
      <version>2.2.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.codehaus.groovy</groupId>
      <artifactId>groovy</artifactId>
      <version>1.7.9</version>
      <scope>test</scope>
    </dependency>
    <dependency> <!--  from core we just test for repackaged cglib, not hibernate proper -->
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-cglib-repack</artifactId>
      <version>2.1_3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${surefire.version}</version>
        <configuration>
          <excludes>
            <exclude>com/fasterxml/jackson/failing/*.java</exclude>
          </excludes>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>${javadoc.version}</version>
        <configuration>
          <links>
            <link>http://docs.oracle.com/javase/6/docs/api/</link>
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.1.1/</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.1.1/</link>
          </links>
        </configuration>
      </plugin>
      <plugin>
        <!-- Inherited from oss-base. Generate PackageVersion.java.-->
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
        <executions>
          <execution>
            <id>process-packageVersion</id>
            <phase>process-sources</phase>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <profiles>
    <profile>
      <id>release</id>
      <properties>
        <maven.test.skip>true</maven.test.skip>
        <skipTests>true</skipTests>
      </properties>
    </profile>
  </profiles>

</project>

```

## src/main/java/com/fasterxml/jackson/databind/ser/BeanPropertyWriter.java

```
package com.fasterxml.jackson.databind.ser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Base bean property handler class, which implements common parts of
 * reflection-based functionality for accessing a property value
 * and serializing it.
 *<p> 
 * Note that current design tries to keep instances immutable (semi-functional
 * style); mostly because these instances are exposed to application
 * code and this is to reduce likelihood of data corruption and
 * synchronization issues.
 */
public class BeanPropertyWriter
    implements BeanProperty
{
    /**
     * Marker object used to indicate "do not serialize if empty"
     */
    public final static Object MARKER_FOR_EMPTY = new Object();
    
    /*
    /**********************************************************
    /* Settings for accessing property value to serialize
    /**********************************************************
     */

    /**
     * Member (field, method) that represents property and allows access
     * to associated annotations.
     */
    protected final AnnotatedMember _member;

    /**
     * Annotations from context (most often, class that declares property,
     * or in case of sub-class serializer, from that sub-class)
     */
    protected final Annotations _contextAnnotations;
    
    /**
     * Type property is declared to have, either in class definition 
     * or associated annotations.
     */
    protected final JavaType _declaredType;
    
    /**
     * Accessor method used to get property value, for
     * method-accessible properties.
     * Null if and only if {@link #_field} is null.
     */
    protected final Method _accessorMethod;
    
    /**
     * Field that contains the property value for field-accessible
     * properties.
     * Null if and only if {@link #_accessorMethod} is null.
     */
    protected final Field _field;
    
    /*
    /**********************************************************
    /* Opaque internal data that bean serializer factory and
    /* bean serializers can add.
    /**********************************************************
     */

    protected HashMap<Object,Object> _internalSettings;
    
    /*
    /**********************************************************
    /* Serialization settings
    /**********************************************************
     */
    
    /**
     * Logical name of the property; will be used as the field name
     * under which value for the property is written.
     */
    protected final SerializedString _name;

    /**
     * Wrapper name to use for this element, if any
     * 
     * @since 2.2
     */
    protected final PropertyName _wrapperName;
    
    /**
     * Type to use for locating serializer; normally same as return
     * type of the accessor method, but may be overridden by annotations.
     */
    protected final JavaType _cfgSerializationType;

    /**
     * Serializer to use for writing out the value: null if it can not
     * be known statically; non-null if it can.
     */
    protected JsonSerializer<Object> _serializer;

    /**
     * Serializer used for writing out null values, if any: if null,
     * null values are to be suppressed.
     */
    protected JsonSerializer<Object> _nullSerializer;
    
    /**
     * In case serializer is not known statically (i.e. <code>_serializer</code>
     * is null), we will use a lookup structure for storing dynamically
     * resolved mapping from type(s) to serializer(s).
     */
    protected PropertySerializerMap _dynamicSerializers;

    /**
     * Whether null values are to be suppressed (nothing written out if
     * value is null) or not.
     */
    protected final boolean _suppressNulls;
    
    /**
     * Value that is considered default value of the property; used for
     * default-value-suppression if enabled.
     */
    protected final Object _suppressableValue;

    /**
     * Alternate set of property writers used when view-based filtering
     * is available for the Bean.
     */
    protected final Class<?>[] _includeInViews;

    /**
     * If property being serialized needs type information to be
     * included this is the type serializer to use.
     * Declared type (possibly augmented with annotations) of property
     * is used for determining exact mechanism to use (compared to
     * actual runtime type used for serializing actual state).
     */
    protected TypeSerializer _typeSerializer;
    
    /**
     * Base type of the property, if the declared type is "non-trivial";
     * meaning it is either a structured type (collection, map, array),
     * or parameterized. Used to retain type information about contained
     * type, which is mostly necessary if type meta-data is to be
     * included.
     */
    protected JavaType _nonTrivialBaseType;

    /**
     * Whether value of this property has been marked as required.
     * Retained since it will be needed when traversing type hierarchy
     * for producing schemas (and other similar tasks); currently not
     * used for serialization.
     * 
     * @since 2.2
     */
    protected final boolean _isRequired;
    
    /*
    /**********************************************************
    /* Construction, configuration
    /**********************************************************
     */
    
    @SuppressWarnings("unchecked")
    public BeanPropertyWriter(BeanPropertyDefinition propDef,
            AnnotatedMember member, Annotations contextAnnotations,
            JavaType declaredType,
            JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType,
            boolean suppressNulls, Object suppressableValue)
    {
        
        _member = member;
        _contextAnnotations = contextAnnotations;
        _name = new SerializedString(propDef.getName());
        _wrapperName = propDef.getWrapperName();
        _declaredType = declaredType;
        _serializer = (JsonSerializer<Object>) ser;
        _dynamicSerializers = (ser == null) ? PropertySerializerMap.emptyMap() : null;
        _typeSerializer = typeSer;
        _cfgSerializationType = serType;
        _isRequired = propDef.isRequired();

        if (member instanceof AnnotatedField) {
            _accessorMethod = null;
            _field = (Field) member.getMember();
        } else if (member instanceof AnnotatedMethod) {
            _accessorMethod = (Method) member.getMember();
            _field = null;
        } else {
            throw new IllegalArgumentException("Can not pass member of type "+member.getClass().getName());
        }
        _suppressNulls = suppressNulls;
        _suppressableValue = suppressableValue;
        _includeInViews = propDef.findViews();

        // this will be resolved later on, unless nulls are to be suppressed
        _nullSerializer = null;
    }

    /**
     * "Copy constructor" to be used by filtering sub-classes
     */
    protected BeanPropertyWriter(BeanPropertyWriter base) {
        this(base, base._name);
    }

    protected BeanPropertyWriter(BeanPropertyWriter base, SerializedString name)
    {
        _name = name;
        _wrapperName = base._wrapperName;

        _member = base._member;
        _contextAnnotations = base._contextAnnotations;
        _declaredType = base._declaredType;
        _accessorMethod = base._accessorMethod;
        _field = base._field;
        _serializer = base._serializer;
        _nullSerializer = base._nullSerializer;
        // one more thing: copy internal settings, if any (since 1.7)
        if (base._internalSettings != null) {
            _internalSettings = new HashMap<Object,Object>(base._internalSettings);
        }
        _cfgSerializationType = base._cfgSerializationType;
        _dynamicSerializers = base._dynamicSerializers;
        _suppressNulls = base._suppressNulls;
        _suppressableValue = base._suppressableValue;
        _includeInViews = base._includeInViews;
        _typeSerializer = base._typeSerializer;
        _nonTrivialBaseType = base._nonTrivialBaseType;
        _isRequired = base._isRequired;
    }

    public BeanPropertyWriter rename(NameTransformer transformer) {
        String newName = transformer.transform(_name.getValue());
        if (newName.equals(_name.toString())) {
            return this;
        }
        return new BeanPropertyWriter(this, new SerializedString(newName));
    }
    
    /**
     * Method called to assign value serializer for property
     * 
     * @since 2.0
     */
    public void assignSerializer(JsonSerializer<Object> ser)
    {
        // may need to disable check in future?
        if (_serializer != null && _serializer != ser) {
            throw new IllegalStateException("Can not override serializer");
        }
        _serializer = ser;
    }

    /**
     * Method called to assign null value serializer for property
     * 
     * @since 2.0
     */
    public void assignNullSerializer(JsonSerializer<Object> nullSer)
    {
        // may need to disable check in future?
        if (_nullSerializer != null && _nullSerializer != nullSer) {
            throw new IllegalStateException("Can not override null serializer");
        }
        _nullSerializer = nullSer;
    }
    
    /**
     * Method called create an instance that handles details of unwrapping
     * contained value.
     */
    public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
        return new UnwrappingBeanPropertyWriter(this, unwrapper);
    }

    /**
     * Method called to define type to consider as "non-trivial" basetype,
     * needed for dynamic serialization resolution for complex (usually container)
     * types
     */
    public void setNonTrivialBaseType(JavaType t) {
        _nonTrivialBaseType = t;
    }

    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public String getName() {
        return _name.getValue();
    }

    @Override
    public JavaType getType() {
        return _declaredType;
    }

    @Override
    public PropertyName getWrapperName() {
        return _wrapperName;
    }

    @Override
    public boolean isRequired() {
        return _isRequired;
    }
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return _member.getAnnotation(acls);
    }

    @Override
    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return _contextAnnotations.get(acls);
    }

    @Override
    public AnnotatedMember getMember() {
        return _member;
    }


    @Override
    public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor)
        throws JsonMappingException
    {
        if (objectVisitor != null) {
            if (isRequired()) {
                objectVisitor.property(this); 
            } else {
                objectVisitor.optionalProperty(this);
            }
        }
    }

    /*
    /**********************************************************
    /* Managing and accessing of opaque internal settings
    /* (used by extensions)
    /**********************************************************
     */
    
    /**
     * Method for accessing value of specified internal setting.
     * 
     * @return Value of the setting, if any; null if none.
     */
    public Object getInternalSetting(Object key)
    {
        if (_internalSettings == null) {
            return null;
        }
        return _internalSettings.get(key);
    }
    
    /**
     * Method for setting specific internal setting to given value
     * 
     * @return Old value of the setting, if any (null if none)
     */
    public Object setInternalSetting(Object key, Object value)
    {
        if (_internalSettings == null) {
            _internalSettings = new HashMap<Object,Object>();
        }
        return _internalSettings.put(key, value);
    }

    /**
     * Method for removing entry for specified internal setting.
     * 
     * @return Existing value of the setting, if any (null if none)
     */
    public Object removeInternalSetting(Object key)
    {
        Object removed = null;
        if (_internalSettings != null) {
            removed = _internalSettings.remove(key);
            // to reduce memory usage, let's also drop the Map itself, if empty
            if (_internalSettings.size() == 0) {
                _internalSettings = null;
            }
        }
        return removed;
    }
    
    /*
    /**********************************************************
    /* Accessors
    /**********************************************************
     */

    public SerializedString getSerializedName() { return _name; }
    
    public boolean hasSerializer() { return _serializer != null; }
    public boolean hasNullSerializer() { return _nullSerializer != null; }

    public boolean willSuppressNulls() { return _suppressNulls; }
    
    // Needed by BeanSerializer#getSchema
    public JsonSerializer<Object> getSerializer() {
        return _serializer;
    }

    public JavaType getSerializationType() {
        return _cfgSerializationType;
    }

    public Class<?> getRawSerializationType() {
        return (_cfgSerializationType == null) ? null : _cfgSerializationType.getRawClass();
    }
    
    public Class<?> getPropertyType() 
    {
        if (_accessorMethod != null) {
            return _accessorMethod.getReturnType();
        }
        return _field.getType();
    }

    /**
     * Get the generic property type of this property writer.
     *
     * @return The property type, or null if not found.
     */
    public Type getGenericPropertyType()
    {
        if (_accessorMethod != null) {
            return _accessorMethod.getGenericReturnType();
        }
        return _field.getGenericType();
    }

    public Class<?>[] getViews() { return _includeInViews; }

    /**
     *<p>
     * NOTE: due to introspection, this is a <b>slow</b> method to call
     * and should never be called during actual serialization or filtering
     * of the property. Rather it is needed for traversal needed for things
     * like constructing JSON Schema instances.
     * 
     * @since 2.1
     * 
     * @deprecated since 2.2, use {@link #isRequired()} instead.
     */
    @Deprecated
    protected boolean isRequired(AnnotationIntrospector intr) {
        return _isRequired;
    }

    /*
    /**********************************************************
    /* Legacy support for JsonFormatVisitable
    /**********************************************************
     */

    /**
     * Attempt to add the output of the given {@link BeanPropertyWriter} in the given {@link ObjectNode}.
     * Otherwise, add the default schema {@link JsonNode} in place of the writer's output
     * 
     * @param propertiesNode Node which the given property would exist within
     * @param provider Provider that can be used for accessing dynamic aspects of serialization
     *  processing
     *  
     *  {@link BeanPropertyFilter#depositSchemaProperty(BeanPropertyWriter, ObjectNode, SerializerProvider)}
     * 
     * @since 2.1
     */
    @SuppressWarnings("deprecation")
    public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider)
        throws JsonMappingException
    {
        JavaType propType = getSerializationType();
        // 03-Dec-2010, tatu: SchemaAware REALLY should use JavaType, but alas it doesn't...
        Type hint = (propType == null) ? getGenericPropertyType() : propType.getRawClass();
        JsonNode schemaNode;
        // Maybe it already has annotated/statically configured serializer?
        JsonSerializer<Object> ser = getSerializer();
        if (ser == null) { // nope
            Class<?> serType = getRawSerializationType();
            if (serType == null) {
                serType = getPropertyType();
            }
            ser = provider.findValueSerializer(serType, this);
        }
        boolean isOptional = !isRequired();
        if (ser instanceof SchemaAware) {
            schemaNode =  ((SchemaAware) ser).getSchema(provider, hint, isOptional) ;
        } else {  
            schemaNode = com.fasterxml.jackson.databind.jsonschema.JsonSchema.getDefaultSchemaNode(); 
        }
        propertiesNode.put(getName(), schemaNode);
    }
    
    /*
    /**********************************************************
    /* Serialization functionality
    /**********************************************************
     */

    /**
     * Method called to access property that this bean stands for, from
     * within given bean, and to serialize it as a JSON Object field
     * using appropriate serializer.
     */
    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
        throws Exception
    {
        Object value = get(bean);
        // Null handling is bit different, check that first
        if (value == null) {
            if (_nullSerializer != null) {
                jgen.writeFieldName(_name);
                _nullSerializer.serialize(null, jgen, prov);
            }
            return;
        }
        // then find serializer to use
        JsonSerializer<Object> ser = _serializer;
        if (ser == null) {
            Class<?> cls = value.getClass();
            PropertySerializerMap map = _dynamicSerializers;
            ser = map.serializerFor(cls);
            if (ser == null) {
                ser = _findAndAddDynamic(map, cls, prov);
            }
        }
        // and then see if we must suppress certain values (default, empty)
        if (_suppressableValue != null) {
            if (MARKER_FOR_EMPTY == _suppressableValue) {
                if (ser.isEmpty(value)) {
                    return;
                }
            } else if (_suppressableValue.equals(value)) {
                return;
            }
        }
        // For non-nulls: simple check for direct cycles
        if (value == bean) {
            _handleSelfReference(bean, ser);
        }
        jgen.writeFieldName(_name);
        if (_typeSerializer == null) {
            ser.serialize(value, jgen, prov);
        } else {
            ser.serializeWithType(value, jgen, prov, _typeSerializer);
        }
    }

    /**
     * Alternative to {@link #serializeAsField} that is used when a POJO
     * is serialized as JSON Array; the difference is that no field names
     * are written.
     * 
     * @since 2.1
     */
    public void serializeAsColumn(Object bean, JsonGenerator jgen, SerializerProvider prov)
        throws Exception
    {
        Object value = get(bean);
        if (value == null) { // nulls need specialized handling
            if (_nullSerializer != null) {
                _nullSerializer.serialize(null, jgen, prov);
            } else { // can NOT suppress entries in tabular output
                jgen.writeNull();
            }
            return;
        }
        // otherwise find serializer to use
        JsonSerializer<Object> ser = _serializer;
        if (ser == null) {
            Class<?> cls = value.getClass();
            PropertySerializerMap map = _dynamicSerializers;
            ser = map.serializerFor(cls);
            if (ser == null) {
                ser = _findAndAddDynamic(map, cls, prov);
            }
        }
        // and then see if we must suppress certain values (default, empty)
        if (_suppressableValue != null) {
            if (MARKER_FOR_EMPTY == _suppressableValue) {
                if (ser.isEmpty(value)) { // can NOT suppress entries in tabular output
                    serializeAsPlaceholder(bean, jgen, prov);
                    return;
                }
            } else if (_suppressableValue.equals(value)) { // can NOT suppress entries in tabular output
                serializeAsPlaceholder(bean, jgen, prov);
                return;
            }
        }
        // For non-nulls: simple check for direct cycles
        if (value == bean) {
            _handleSelfReference(bean, ser);
        }
        if (_typeSerializer == null) {
            ser.serialize(value, jgen, prov);
        } else {
            ser.serializeWithType(value, jgen, prov, _typeSerializer);
        }
    }

    /**
     * Method called to serialize a placeholder used in tabular output when
     * real value is not to be included (is filtered out), but when we need
     * an entry so that field indexes will not be off. Typically this should
     * output null or empty String, depending on datatype.
     * 
     * @since 2.1
     */
    public void serializeAsPlaceholder(Object bean, JsonGenerator jgen, SerializerProvider prov)
        throws Exception
    {
        if (_nullSerializer != null) {
            _nullSerializer.serialize(null, jgen, prov);
        } else {
            jgen.writeNull();
        }
    }
    
    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */
    
    protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map,
            Class<?> type, SerializerProvider provider) throws JsonMappingException
    {
        PropertySerializerMap.SerializerAndMapResult result;
        if (_nonTrivialBaseType != null) {
            JavaType t = provider.constructSpecializedType(_nonTrivialBaseType, type);
            result = map.findAndAddSerializer(t, provider, this);
        } else {
            result = map.findAndAddSerializer(type, provider, this);
        }
        // did we get a new map of serializers? If so, start using it
        if (map != result.map) {
            _dynamicSerializers = result.map;
        }
        return result.serializer;
    }
    
    /**
     * Method that can be used to access value of the property this
     * Object describes, from given bean instance.
     *<p>
     * Note: method is final as it should not need to be overridden -- rather,
     * calling method(s) ({@link #serializeAsField}) should be overridden
     * to change the behavior
     */
    public final Object get(Object bean) throws Exception
    {
        if (_accessorMethod != null) {
            return _accessorMethod.invoke(bean);
        }
        return _field.get(bean);
    }

    protected void _handleSelfReference(Object bean, JsonSerializer<?> ser)
        throws JsonMappingException
    {
        /* 05-Feb-2012, tatu: Usually a problem, but NOT if we are handling
         *    object id; this may be the case for BeanSerializers at least.
         */
        if (ser.usesObjectId()) {
            return;
        }
        throw new JsonMappingException("Direct self-reference leading to cycle");
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(40);
        sb.append("property '").append(getName()).append("' (");
        if (_accessorMethod != null) {
            sb.append("via method ").append(_accessorMethod.getDeclaringClass().getName()).append("#").append(_accessorMethod.getName());
        } else {
            sb.append("field \"").append(_field.getDeclaringClass().getName()).append("#").append(_field.getName());
        }
        if (_serializer == null) {
            sb.append(", no static serializer");
        } else {
            sb.append(", static serializer of type "+_serializer.getClass().getName());
        }
        sb.append(')');
        return sb.toString();
    }
}

```
