Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: JacksonDatabind; fixed revision: 112f.
Modified target classes:
com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "createContextual",
    "parameter_types": "com.fasterxml.jackson.databind.DeserializationContext,com.fasterxml.jackson.databind.BeanProperty"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "deserialize",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "deserialize",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "deserializeUsingCustom",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "deserializeWithType",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,com.fasterxml.jackson.databind.jsontype.TypeDeserializer"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "getContentDeserializer",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "getValueInstantiator",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "handleNonArray",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "isCachable",
    "parameter_types": ""
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator",
    "method": "withResolved",
    "parameter_types": "com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.NullValueProvider,java.lang.Boolean"
  },
  {
    "class": "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
    "constructor_types": "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.deser.ValueInstantiator,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.NullValueProvider,java.lang.Boolean",
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
  "com.fasterxml.jackson.databind.PropertyMetadata",
  "com.fasterxml.jackson.databind.PropertyName",
  "com.fasterxml.jackson.databind.PropertyNamingStrategy",
  "com.fasterxml.jackson.databind.RuntimeJsonMappingException",
  "com.fasterxml.jackson.databind.SequenceWriter",
  "com.fasterxml.jackson.databind.SerializationConfig",
  "com.fasterxml.jackson.databind.SerializationFeature",
  "com.fasterxml.jackson.databind.SerializerProvider",
  "com.fasterxml.jackson.databind.annotation.JacksonStdImpl",
  "com.fasterxml.jackson.databind.annotation.JsonAppend",
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
  "com.fasterxml.jackson.databind.cfg.ConfigOverride",
  "com.fasterxml.jackson.databind.cfg.ConfigOverrides",
  "com.fasterxml.jackson.databind.cfg.ContextAttributes",
  "com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig",
  "com.fasterxml.jackson.databind.cfg.HandlerInstantiator",
  "com.fasterxml.jackson.databind.cfg.MapperConfig",
  "com.fasterxml.jackson.databind.cfg.MapperConfigBase",
  "com.fasterxml.jackson.databind.cfg.MutableConfigOverride",
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
  "com.fasterxml.jackson.databind.deser.NullValueProvider",
  "com.fasterxml.jackson.databind.deser.ResolvableDeserializer",
  "com.fasterxml.jackson.databind.deser.SettableAnyProperty",
  "com.fasterxml.jackson.databind.deser.SettableBeanProperty",
  "com.fasterxml.jackson.databind.deser.UnresolvedForwardReference",
  "com.fasterxml.jackson.databind.deser.UnresolvedId",
  "com.fasterxml.jackson.databind.deser.ValueInstantiator",
  "com.fasterxml.jackson.databind.deser.ValueInstantiators",
  "com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap",
  "com.fasterxml.jackson.databind.deser.impl.CreatorCandidate",
  "com.fasterxml.jackson.databind.deser.impl.CreatorCollector",
  "com.fasterxml.jackson.databind.deser.impl.ErrorThrowingDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler",
  "com.fasterxml.jackson.databind.deser.impl.FailingDeserializer",
  "com.fasterxml.jackson.databind.deser.impl.FieldProperty",
  "com.fasterxml.jackson.databind.deser.impl.InnerClassProperty",
  "com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers",
  "com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty",
  "com.fasterxml.jackson.databind.deser.impl.MergingSettableBeanProperty",
  "com.fasterxml.jackson.databind.deser.impl.MethodProperty",
  "com.fasterxml.jackson.databind.deser.impl.NullsAsEmptyProvider",
  "com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider",
  "com.fasterxml.jackson.databind.deser.impl.NullsFailProvider",
  "com.fasterxml.jackson.databind.deser.impl.ObjectIdReader",
  "com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty",
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
  "com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer",
  "com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer",
  "com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer",
  "com.fasterxml.jackson.databind.deser.std.CollectionDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase",
  "com.fasterxml.jackson.databind.deser.std.DateDeserializers",
  "com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumMapDeserializer",
  "com.fasterxml.jackson.databind.deser.std.EnumSetDeserializer",
  "com.fasterxml.jackson.databind.deser.std.FactoryBasedEnumDeserializer",
  "com.fasterxml.jackson.databind.deser.std.FromStringDeserializer",
  "com.fasterxml.jackson.databind.deser.std.JdkDeserializers",
  "com.fasterxml.jackson.databind.deser.std.JsonLocationInstantiator",
  "com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer",
  "com.fasterxml.jackson.databind.deser.std.MapDeserializer",
  "com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer",
  "com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.NumberDeserializers",
  "com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers",
  "com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers",
  "com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StdValueInstantiator",
  "com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer",
  "com.fasterxml.jackson.databind.deser.std.StringDeserializer",
  "com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer",
  "com.fasterxml.jackson.databind.deser.std.TokenBufferDeserializer",
  "com.fasterxml.jackson.databind.deser.std.UUIDDeserializer",
  "com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer",
  "com.fasterxml.jackson.databind.exc.IgnoredPropertyException",
  "com.fasterxml.jackson.databind.exc.InvalidDefinitionException",
  "com.fasterxml.jackson.databind.exc.InvalidFormatException",
  "com.fasterxml.jackson.databind.exc.InvalidNullException",
  "com.fasterxml.jackson.databind.exc.InvalidTypeIdException",
  "com.fasterxml.jackson.databind.exc.MismatchedInputException",
  "com.fasterxml.jackson.databind.exc.PropertyBindingException",
  "com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException",
  "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers",
  "com.fasterxml.jackson.databind.ext.CoreXMLSerializers",
  "com.fasterxml.jackson.databind.ext.DOMDeserializer",
  "com.fasterxml.jackson.databind.ext.DOMSerializer",
  "com.fasterxml.jackson.databind.ext.Java7Support",
  "com.fasterxml.jackson.databind.ext.Java7SupportImpl",
  "com.fasterxml.jackson.databind.ext.NioPathDeserializer",
  "com.fasterxml.jackson.databind.ext.NioPathSerializer",
  "com.fasterxml.jackson.databind.ext.OptionalHandlerFactory",
  "com.fasterxml.jackson.databind.introspect.Annotated",
  "com.fasterxml.jackson.databind.introspect.AnnotatedClass",
  "com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver",
  "com.fasterxml.jackson.databind.introspect.AnnotatedConstructor",
  "com.fasterxml.jackson.databind.introspect.AnnotatedCreatorCollector",
  "com.fasterxml.jackson.databind.introspect.AnnotatedField",
  "com.fasterxml.jackson.databind.introspect.AnnotatedFieldCollector",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMember",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMethod",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector",
  "com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap",
  "com.fasterxml.jackson.databind.introspect.AnnotatedParameter",
  "com.fasterxml.jackson.databind.introspect.AnnotatedWithParams",
  "com.fasterxml.jackson.databind.introspect.AnnotationCollector",
  "com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair",
  "com.fasterxml.jackson.databind.introspect.AnnotationMap",
  "com.fasterxml.jackson.databind.introspect.BasicBeanDescription",
  "com.fasterxml.jackson.databind.introspect.BasicClassIntrospector",
  "com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition",
  "com.fasterxml.jackson.databind.introspect.ClassIntrospector",
  "com.fasterxml.jackson.databind.introspect.CollectorBase",
  "com.fasterxml.jackson.databind.introspect.ConcreteBeanPropertyBase",
  "com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector",
  "com.fasterxml.jackson.databind.introspect.MemberKey",
  "com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector",
  "com.fasterxml.jackson.databind.introspect.ObjectIdInfo",
  "com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector",
  "com.fasterxml.jackson.databind.introspect.POJOPropertyBuilder",
  "com.fasterxml.jackson.databind.introspect.SimpleMixInResolver",
  "com.fasterxml.jackson.databind.introspect.TypeResolutionContext",
  "com.fasterxml.jackson.databind.introspect.VirtualAnnotatedMember",
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
  "com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer",
  "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer",
  "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver",
  "com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder",
  "com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator",
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
  "com.fasterxml.jackson.databind.node.JsonNodeCreator",
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
  "com.fasterxml.jackson.databind.ser.PropertyFilter",
  "com.fasterxml.jackson.databind.ser.PropertyWriter",
  "com.fasterxml.jackson.databind.ser.ResolvableSerializer",
  "com.fasterxml.jackson.databind.ser.SerializerCache",
  "com.fasterxml.jackson.databind.ser.SerializerFactory",
  "com.fasterxml.jackson.databind.ser.Serializers",
  "com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter",
  "com.fasterxml.jackson.databind.ser.impl.AttributePropertyWriter",
  "com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer",
  "com.fasterxml.jackson.databind.ser.impl.FailingSerializer",
  "com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter",
  "com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer",
  "com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer",
  "com.fasterxml.jackson.databind.ser.impl.IteratorSerializer",
  "com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer",
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
  "com.fasterxml.jackson.databind.ser.std.AtomicReferenceSerializer",
  "com.fasterxml.jackson.databind.ser.std.BeanSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.BooleanSerializer",
  "com.fasterxml.jackson.databind.ser.std.ByteArraySerializer",
  "com.fasterxml.jackson.databind.ser.std.ByteBufferSerializer",
  "com.fasterxml.jackson.databind.ser.std.CalendarSerializer",
  "com.fasterxml.jackson.databind.ser.std.ClassSerializer",
  "com.fasterxml.jackson.databind.ser.std.CollectionSerializer",
  "com.fasterxml.jackson.databind.ser.std.DateSerializer",
  "com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.EnumSerializer",
  "com.fasterxml.jackson.databind.ser.std.EnumSetSerializer",
  "com.fasterxml.jackson.databind.ser.std.FileSerializer",
  "com.fasterxml.jackson.databind.ser.std.InetAddressSerializer",
  "com.fasterxml.jackson.databind.ser.std.InetSocketAddressSerializer",
  "com.fasterxml.jackson.databind.ser.std.IterableSerializer",
  "com.fasterxml.jackson.databind.ser.std.JsonValueSerializer",
  "com.fasterxml.jackson.databind.ser.std.MapProperty",
  "com.fasterxml.jackson.databind.ser.std.MapSerializer",
  "com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.NullSerializer",
  "com.fasterxml.jackson.databind.ser.std.NumberSerializer",
  "com.fasterxml.jackson.databind.ser.std.NumberSerializers",
  "com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer",
  "com.fasterxml.jackson.databind.ser.std.RawSerializer",
  "com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer",
  "com.fasterxml.jackson.databind.ser.std.SerializableSerializer",
  "com.fasterxml.jackson.databind.ser.std.SqlDateSerializer",
  "com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer",
  "com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase",
  "com.fasterxml.jackson.databind.ser.std.StdArraySerializers",
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
  "com.fasterxml.jackson.databind.ser.std.UUIDSerializer",
  "com.fasterxml.jackson.databind.type.ArrayType",
  "com.fasterxml.jackson.databind.type.ClassKey",
  "com.fasterxml.jackson.databind.type.ClassStack",
  "com.fasterxml.jackson.databind.type.CollectionLikeType",
  "com.fasterxml.jackson.databind.type.CollectionType",
  "com.fasterxml.jackson.databind.type.MapLikeType",
  "com.fasterxml.jackson.databind.type.MapType",
  "com.fasterxml.jackson.databind.type.PlaceholderForType",
  "com.fasterxml.jackson.databind.type.ReferenceType",
  "com.fasterxml.jackson.databind.type.ResolvedRecursiveType",
  "com.fasterxml.jackson.databind.type.SimpleType",
  "com.fasterxml.jackson.databind.type.TypeBase",
  "com.fasterxml.jackson.databind.type.TypeBindings",
  "com.fasterxml.jackson.databind.type.TypeFactory",
  "com.fasterxml.jackson.databind.type.TypeModifier",
  "com.fasterxml.jackson.databind.type.TypeParser",
  "com.fasterxml.jackson.databind.util.AccessPattern",
  "com.fasterxml.jackson.databind.util.Annotations",
  "com.fasterxml.jackson.databind.util.ArrayBuilders",
  "com.fasterxml.jackson.databind.util.ArrayIterator",
  "com.fasterxml.jackson.databind.util.BeanUtil",
  "com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream",
  "com.fasterxml.jackson.databind.util.ByteBufferBackedOutputStream",
  "com.fasterxml.jackson.databind.util.ClassUtil",
  "com.fasterxml.jackson.databind.util.CompactStringObjectMap",
  "com.fasterxml.jackson.databind.util.ConstantValueInstantiator",
  "com.fasterxml.jackson.databind.util.Converter",
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
  "com.fasterxml.jackson.databind.util.PrimitiveArrayBuilder",
  "com.fasterxml.jackson.databind.util.RawValue",
  "com.fasterxml.jackson.databind.util.RootNameLookup",
  "com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition",
  "com.fasterxml.jackson.databind.util.StdConverter",
  "com.fasterxml.jackson.databind.util.StdDateFormat",
  "com.fasterxml.jackson.databind.util.TokenBuffer",
  "com.fasterxml.jackson.databind.util.TokenBufferReadContext",
  "com.fasterxml.jackson.databind.util.TypeKey",
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
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.fasterxml.jackson</groupId>
    <artifactId>jackson-base</artifactId>
    <version>2.9.9-SNAPSHOT</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.9-SNAPSHOT</version>
  <name>jackson-databind</name>
  <packaging>bundle</packaging>
  <description>General data-binding functionality for Jackson: works on core streaming API</description>
  <url>http://github.com/FasterXML/jackson</url>
  <inceptionYear>2008</inceptionYear>

  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-databind.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-databind.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-databind</url>
    <tag>HEAD</tag>
  </scm>

  <properties>
    <!-- With Jackson 2.9 we will require JDK 7 (except for annotations/streaming),
         and new language features (diamond pattern) may be used.
         JDK classes are still loaded dynamically since there isn't much downside
         (small number of types); this allows use on JDK 6 platforms still (including
         Android)
      -->
    <javac.src.version>1.7</javac.src.version>
    <javac.target.version>1.7</javac.target.version>

    <!-- Can not use default, since group id != Java package name here -->
    <osgi.export>com.fasterxml.jackson.databind.*;version=${project.version}</osgi.export>
    <osgi.import> <!-- fix for databind#2299: using jackson-databind in an OSGi environment under Android --> 
        org.w3c.dom.bootstrap;resolution:=optional,
        *
    </osgi.import>

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/databind/cfg</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.databind.cfg</packageVersion.package>

    <!-- since 2.9.1: NOTE! can not use packageVersion.package as is -->
    <jdk.module.name>com.fasterxml.jackson.databind</jdk.module.name>
  </properties>

  <dependencies>
    <!-- Builds on core streaming API; also needs core annotations -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <!-- 06-Mar-2017, tatu: Although bom provides for dependencies, some legacy
             usage seems to benefit from actually specifying version here in case
             it is dependent on transitively
        -->
      <version>${jackson.version.annotations}</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>${jackson.version.core}</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->

    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>1.7.4</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>1.7.4</version>
      <scope>test</scope>
    </dependency>
    <!-- For testing TestNoClassDefFoundDeserializer -->
    <dependency>
      <groupId>javax.measure</groupId>
      <artifactId>jsr-275</artifactId>
      <version>1.0.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <!-- Alas, need to include snapshot reference since otherwise can not find
       snapshot of parent... -->
  <repositories>
    <repository>
      <id>sonatype-nexus-snapshots</id>
      <name>Sonatype Nexus Snapshots</name>
      <url>https://repo1.maven.org/maven2</url>
      <releases><enabled>false</enabled></releases>
      <snapshots><enabled>true</enabled></snapshots>
    </repository>
  </repositories>

  <build>
     <plugins>
      <!-- Important: enable enforcer plug-in: -->
      <plugin>
        <artifactId>maven-enforcer-plugin</artifactId>
        <executions> <!-- or?  combine.children="merge"> -->
          <execution>
            <id>enforce-properties</id>
	    <phase>validate</phase>
            <goals><goal>enforce</goal></goals>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <version>${version.plugin.surefire}</version>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <classpathDependencyExcludes>
            <exclude>javax.measure:jsr-275</exclude>
          </classpathDependencyExcludes>
          <excludes>
            <exclude>com/fasterxml/jackson/failing/*.java</exclude>
          </excludes>
        </configuration>
      </plugin>

      <!-- parent definitions should be ok, but need to add more links -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <links combine.children="append">
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.9</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.9</link>
          </links>
        </configuration>
      </plugin>

      <!-- settings are fine, but needed to trigger execution! -->
      <plugin>
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
      </plugin>

      <!-- 18-Oct-2016, tatu: Try to make coveralls work -->
      <plugin>
        <groupId>org.eluder.coveralls</groupId>
        <artifactId>coveralls-maven-plugin</artifactId>
        <version>4.3.0</version>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </reporting>

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

## src/main/java/com/fasterxml/jackson/databind/deser/std/StringCollectionDeserializer.java

```
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Specifically optimized version for {@link java.util.Collection}s
 * that contain String values; reason is that this is a very common
 * type and we can make use of the fact that Strings are final.
 */
@JacksonStdImpl
public final class StringCollectionDeserializer
    extends ContainerDeserializerBase<Collection<String>>
    implements ContextualDeserializer
{
    private static final long serialVersionUID = 1L;

    // // Configuration

    /**
     * Value deserializer to use, if NOT the standard one
     * (if it is, will be null).
     */
    protected final JsonDeserializer<String> _valueDeserializer;

    // // Instance construction settings:
    
    /**
     * Instantiator used in case custom handling is needed for creation.
     */
    protected final ValueInstantiator _valueInstantiator;

    /**
     * Deserializer that is used iff delegate-based creator is
     * to be used for deserializing from JSON Object.
     */
    protected final JsonDeserializer<Object> _delegateDeserializer;

    // NOTE: no PropertyBasedCreator, as JSON Arrays have no properties

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */
    
    public StringCollectionDeserializer(JavaType collectionType,
            JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator)
    {
        this(collectionType, valueInstantiator, null, valueDeser, valueDeser, null);
    }

    @SuppressWarnings("unchecked")
    protected StringCollectionDeserializer(JavaType collectionType,
            ValueInstantiator valueInstantiator, JsonDeserializer<?> delegateDeser,
            JsonDeserializer<?> valueDeser,
            NullValueProvider nuller, Boolean unwrapSingle)
    {
        super(collectionType, nuller, unwrapSingle);
        _valueDeserializer = (JsonDeserializer<String>) valueDeser;
        _valueInstantiator = valueInstantiator;
        _delegateDeserializer = (JsonDeserializer<Object>) delegateDeser;
    }

    protected StringCollectionDeserializer withResolved(JsonDeserializer<?> delegateDeser,
            JsonDeserializer<?> valueDeser,
            NullValueProvider nuller, Boolean unwrapSingle)
    {
        if ((_unwrapSingle == unwrapSingle) && (_nullProvider == nuller)
                && (_valueDeserializer == valueDeser) && (_delegateDeserializer == delegateDeser)) {
            return this;
        }
        return new StringCollectionDeserializer(_containerType, _valueInstantiator,
                delegateDeser, valueDeser, nuller, unwrapSingle);
    }

    @Override // since 2.5
    public boolean isCachable() {
        // 26-Mar-2015, tatu: Important: prevent caching if custom deserializers via annotations
        //    are involved
        return (_valueDeserializer == null) && (_delegateDeserializer == null);
    }
    
    /*
    /**********************************************************
    /* Validation, post-processing
    /**********************************************************
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException
    {
        // May need to resolve types for delegate-based creators:
        JsonDeserializer<Object> delegate = null;
        if (_valueInstantiator != null) {
            // [databind#2324]: check both array-delegating and delegating
            AnnotatedWithParams delegateCreator = _valueInstantiator.getArrayDelegateCreator();
            if (delegateCreator != null) {
                JavaType delegateType = _valueInstantiator.getArrayDelegateType(ctxt.getConfig());
                delegate = findDeserializer(ctxt, delegateType, property);
            } else if ((delegateCreator = _valueInstantiator.getDelegateCreator()) != null) {
                JavaType delegateType = _valueInstantiator.getDelegateType(ctxt.getConfig());
                delegate = findDeserializer(ctxt, delegateType, property);
            }
        }
        JsonDeserializer<?> valueDeser = _valueDeserializer;
        final JavaType valueType = _containerType.getContentType();
        if (valueDeser == null) {
            // [databind#125]: May have a content converter
            valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
            if (valueDeser == null) {
            // And we may also need to get deserializer for String
                valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
            }
        } else { // if directly assigned, probably not yet contextual, so:
            valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
        }
        // 11-Dec-2015, tatu: Should we pass basic `Collection.class`, or more refined? Mostly
        //   comes down to "List vs Collection" I suppose... for now, pass Collection
        Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class,
                JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
        if (isDefaultDeserializer(valueDeser)) {
            valueDeser = null;
        }
        return withResolved(delegate, valueDeser, nuller, unwrapSingle);
    }
    
    /*
    /**********************************************************
    /* ContainerDeserializerBase API
    /**********************************************************
     */

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        JsonDeserializer<?> deser = _valueDeserializer;
        return (JsonDeserializer<Object>) deser;
    }

    @Override
    public ValueInstantiator getValueInstantiator() {
        return _valueInstantiator;
    }

    /*
    /**********************************************************
    /* JsonDeserializer API
    /**********************************************************
     */
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException
    {
        if (_delegateDeserializer != null) {
            return (Collection<String>) _valueInstantiator.createUsingDelegate(ctxt,
                    _delegateDeserializer.deserialize(p, ctxt));
        }
        final Collection<String> result = (Collection<String>) _valueInstantiator.createUsingDefault(ctxt);
        return deserialize(p, ctxt, result);
    }

    @Override
    public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt,
            Collection<String> result)
        throws IOException
    {
        // Ok: must point to START_ARRAY
        if (!p.isExpectedStartArrayToken()) {
            return handleNonArray(p, ctxt, result);
        }

        if (_valueDeserializer != null) {
            return deserializeUsingCustom(p, ctxt, result, _valueDeserializer);
        }
        try {
            while (true) {
                // First the common case:
                String value = p.nextTextValue();
                if (value != null) {
                    result.add(value);
                    continue;
                }
                JsonToken t = p.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    break;
                }
                if (t == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = (String) _nullProvider.getNullValue(ctxt);
                } else {
                    value = _parseString(p, ctxt);
                }
                result.add(value);
            }
        } catch (Exception e) {
            throw JsonMappingException.wrapWithPath(e, result, result.size());
        }
        return result;
    }
    
    private Collection<String> deserializeUsingCustom(JsonParser p, DeserializationContext ctxt,
            Collection<String> result, final JsonDeserializer<String> deser) throws IOException
    {
        while (true) {
            /* 30-Dec-2014, tatu: This may look odd, but let's actually call method
             *   that suggest we are expecting a String; this helps with some formats,
             *   notably XML. Note, however, that while we can get String, we can't
             *   assume that's what we use due to custom deserializer
             */
            String value;
            if (p.nextTextValue() == null) {
                JsonToken t = p.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    break;
                }
                // Ok: no need to convert Strings, but must recognize nulls
                if (t == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = (String) _nullProvider.getNullValue(ctxt);
                } else {
                    value = deser.deserialize(p, ctxt);
                }
            } else {
                value = deser.deserialize(p, ctxt);
            }
            result.add(value);
        }
        return result;
    }
    
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
            TypeDeserializer typeDeserializer) throws IOException {
        // In future could check current token... for now this should be enough:
        return typeDeserializer.deserializeTypedFromArray(p, ctxt);
    }

    /**
     * Helper method called when current token is not START_ARRAY. Will either
     * throw an exception, or try to handle value as if member of implicit
     * array, depending on configuration.
     */
    @SuppressWarnings("unchecked")
    private final Collection<String> handleNonArray(JsonParser p, DeserializationContext ctxt,
            Collection<String> result) throws IOException
    {
        // implicit arrays from single values?
        boolean canWrap = (_unwrapSingle == Boolean.TRUE) ||
                ((_unwrapSingle == null) &&
                        ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
        if (!canWrap) {
            return (Collection<String>) ctxt.handleUnexpectedToken(_containerType.getRawClass(), p);
        }
        // Strings are one of "native" (intrinsic) types, so there's never type deserializer involved
        JsonDeserializer<String> valueDes = _valueDeserializer;
        JsonToken t = p.getCurrentToken();

        String value;
        
        if (t == JsonToken.VALUE_NULL) {
            // 03-Feb-2017, tatu: Does this work?
            if (_skipNullValues) {
                return result;
            }
            value = (String) _nullProvider.getNullValue(ctxt);
        } else {
            value = (valueDes == null) ? _parseString(p, ctxt) : valueDes.deserialize(p, ctxt);
        }
        result.add(value);
        return result;
    }
}

```
