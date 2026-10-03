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
    "method": "handleNonArray",
    "parameter_types": "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection"
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

Explicit fixture policy: aom-beam-champ-joint-fixtures-v10-development. Use the reviewed capability recipes below instead of legacy recursive/null construction.
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


Explicit fixture recipe definitions (generation support, separate from production source):
Use the same construction/projection knowledge across all four approaches. Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.
```json
{
  "fixture_policy_id": "aom-beam-champ-joint-fixtures-v10-development",
  "schema_version": 1,
  "scope": "Same fixture construction/projection knowledge for all four approaches; no execution feedback",
  "source_sha256": {
    "algorithms/java/SqaProbe.java": "b67701c4f7b37062cfe527556859f6d7cce9fa4998c70a06588abc71d32c36e9",
    "scripts/study/api854/fixture_policy.py": "11b801ff3dc305d3bc9be280ae16eba4b497bbf774dfd3b3e7f0b7340ef880ba"
  },
  "sources": {
    "algorithms/java/SqaProbe.java": "import java.lang.reflect.Array;\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.lang.reflect.Method;\nimport java.lang.reflect.Modifier;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.Paths;\nimport java.security.MessageDigest;\nimport java.security.NoSuchAlgorithmException;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.util.Base64;\nimport java.util.Comparator;\nimport java.util.List;\n\n/** Fixed-revision observations for explicitly supported, deterministic Java APIs.\n * No buggy source, patch, or triggering test is used during input generation.\n * The same source is packaged with the generated JUnit suite.\n */\npublic final class SqaProbe {\n    private static final String[] STRINGS = {\n        \"\", \"0\", \"1\", \"-1\", \"null\", \"true\", \"false\", \"abc\", \"ABC\", \" \",\n        \"0x0\", \"0x1\", \"0xFFFFFFFF\", \"1.0\", \"1e3\", \"NaN\", \"Infinity\",\n        \"{}\", \"[]\", \"[1]\", \"{\\\"a\\\":1}\", \"a=b\", \"--help\", \"-x\", \"a,b\",\n        \"1970-01-01\", \"a\\\\nb\", \"a\\nb\", \"a\\tb\", \"\\u0e17\\u0e14\\u0e2a\\u0e2d\\u0e1a\"\n    };\n    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,\n        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};\n\n    private SqaProbe() { }\n\n    /** Schema scaffolding carried in the suite; no benchmark test classes. */\n    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }\n    public static class StringBinding extends GenericFixture<String> { }\n    public static class IntegerBinding extends GenericFixture<Integer> { }\n    public static class FixtureBean { public String value = \"fixture-value\"; }\n    public interface FixtureMock { String accept(String value); }\n\n    public static final String EXPLICIT_FIXTURES = \"beam-explicit-fixtures-v3-proposal\";\n    public static final String SCALAR_FIXTURES = \"beam-explicit-fixtures-v4-proposal\";\n    public static final String PILOT_FIXTURES = \"beam-explicit-fixtures-v5-proposal\";\n    public static final String BUFFER_FIXTURES = \"beam-explicit-fixtures-v6-buffer-proposal\";\n    public static final String FRACTION_FIELD_FIXTURES = \"aom-beam-fraction-field-v6-development\";\n    public static final String LANG_HELPER_FIXTURES = \"beam-explicit-fixtures-v9-buffer-lang-development\";\n    public static final String JOINT_FIXTURES = \"aom-beam-champ-joint-fixtures-v10-development\";\n    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();\n    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();\n\n    /** A setup failure is never an observation of an uncalled target method. */\n    private static final class FixtureFailure extends RuntimeException {\n        FixtureFailure(String message, Throwable cause) { super(message, cause); }\n    }\n\n    // Production factories only: no dataset test classes, patches or buggy results.\n    // Reflection keeps the helper compilable without project-specific dependencies.\n    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();\n        while (declaring != null) {\n            try {\n                Method method = declaring.getDeclaredMethod(name, parameterTypes);\n                method.setAccessible(true);\n                return method.invoke(receiver instanceof Class ? null : receiver, values);\n            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n        }\n        throw new NoSuchMethodException(name);\n    }\n\n    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);\n        ctor.setAccessible(true);\n        return ctor.newInstance(values);\n    }\n\n    private static final class FixtureSession {\n        final String targetClass;\n        final String method;\n        final boolean pilot;\n        final boolean bufferSlices;\n        char[] outputBuffer;\n        final boolean fractionField;\n        final boolean langHelpers;\n        final boolean reviewed;\n        Object validationInput;\n        boolean constructing;\n        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;\n        org.w3c.dom.Element domRoot;\n        org.w3c.dom.Node domChild;\n        Object jdomRoot, jdomChild;\n        java.io.ByteArrayOutputStream archiveBytes;\n        Object mapper, parser, context, collectionType, collectionDeserializer;\n        Object mock, baseInvocation, actualInvocation;\n        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;\n        int cleanupNodeIndex;\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void unusedClosure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> ac = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            cleanupExterns = call(compiler, \"parseTestCode\", new Class<?>[]{String.class}, \"\");\n            cleanupScript = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                \"var unused = 1; function fixture(x) { var local = \" + (a < 0 ? \"2\" : \"3\") + \"; return x; } fixture(1);\");\n            // Normalize traverses sibling roots and requires their common parent.\n            int block = Class.forName(\"com.google.javascript.rhino.Token\").getField(\"BLOCK\").getInt(null);\n            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupExterns);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupScript);\n            Object normalize = construct(\"com.google.javascript.jscomp.Normalize\", new Class<?>[]{ac, boolean.class}, compiler, false);\n            call(normalize, \"process\", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);\n            Class<?> lifecycle = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage\");\n            call(compiler, \"setLifeCycleStage\", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, \"NORMALIZED\"));\n            closureNode = cleanupScript;\n        }\n\n        void chart(double a) throws ReflectiveOperationException {\n            if (chartDataset != null) return;\n            Class<?> dataset = Class.forName(\"org.jfree.data.category.CategoryDataset\");\n            Class<?> axis = Class.forName(\"org.jfree.chart.axis.CategoryAxis\");\n            Class<?> valueAxis = Class.forName(\"org.jfree.chart.axis.ValueAxis\");\n            Class<?> renderer = Class.forName(\"org.jfree.chart.renderer.category.CategoryItemRenderer\");\n            chartDataset = construct(\"org.jfree.data.category.DefaultCategoryDataset\", new Class<?>[]{});\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, \"row-a\", \"column-a\");\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, \"row-b\", \"column-a\");\n            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, \"Domain\");\n            Object rangeAxis = construct(\"org.jfree.chart.axis.NumberAxis\", new Class<?>[]{String.class}, \"Range\");\n            chartPlot = construct(\"org.jfree.chart.plot.CategoryPlot\", new Class<?>[]{dataset, axis, valueAxis, renderer},\n                chartDataset, chartAxis, rangeAxis, receiver);\n            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();\n            try {\n                call(receiver, \"initialise\", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,\n                    chartPlot.getClass(), dataset, Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")},\n                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);\n            } finally { graphics.dispose(); }\n        }\n\n        Object beanWriter() throws ReflectiveOperationException {\n            Object objectMapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Object provider = call(objectMapper, \"getSerializerProvider\", new Class<?>[]{});\n            provider = call(provider, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.SerializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.databind.ser.SerializerFactory\")},\n                call(objectMapper, \"getSerializationConfig\", new Class<?>[]{}), call(objectMapper, \"getSerializerFactory\", new Class<?>[]{}));\n            Object serializer = call(provider, \"findValueSerializer\", new Class<?>[]{Class.class, Class.forName(\"com.fasterxml.jackson.databind.BeanProperty\")}, FixtureBean.class, null);\n            return Array.get(field(serializer, \"_props\"), 0);\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void jacksonCollection(double a) throws ReflectiveOperationException {\n            if (mapper != null) return;\n            mapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Class<?> feature = Class.forName(\"com.fasterxml.jackson.databind.DeserializationFeature\");\n            call(mapper, \"configure\", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, \"ACCEPT_SINGLE_VALUE_AS_ARRAY\"), true);\n            Object typeFactory = call(mapper, \"getTypeFactory\", new Class<?>[]{});\n            collectionType = call(typeFactory, \"constructCollectionType\", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);\n            Object factory = call(mapper, \"getFactory\", new Class<?>[]{});\n            String input = method.equals(\"handleNonArray\") ? a < 0 ? \"\\\"alpha\\\"\" : \"\\\"beta\\\"\"\n                : a < 0 ? \"[\\\"alpha\\\",\\\"beta\\\"]\" : \"[\\\"left\\\",\\\"right\\\"]\";\n            parser = call(factory, \"createParser\", new Class<?>[]{String.class}, input);\n            call(parser, \"nextToken\", new Class<?>[]{});\n            Object blueprint = call(mapper, \"getDeserializationContext\", new Class<?>[]{});\n            context = call(blueprint, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.DeserializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.core.JsonParser\"), Class.forName(\"com.fasterxml.jackson.databind.InjectableValues\")},\n                call(mapper, \"getDeserializationConfig\", new Class<?>[]{}), parser, null);\n            collectionDeserializer = call(context, \"findRootValueDeserializer\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.JavaType\")}, collectionType);\n        }\n\n        void mockito(double a) throws ReflectiveOperationException {\n            if (mock != null) return;\n            mock = call(Class.forName(\"org.mockito.Mockito\"), \"mock\", new Class<?>[]{Class.class}, FixtureMock.class);\n            call(mock, \"accept\", new Class<?>[]{String.class}, \"alpha\");\n            call(mock, \"accept\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            Object util = construct(\"org.mockito.internal.util.MockUtil\", new Class<?>[]{});\n            Object handler = call(util, \"getMockHandler\", new Class<?>[]{Object.class}, mock);\n            Object container = call(handler, \"getInvocationContainer\", new Class<?>[]{});\n            List<?> invocations = (List<?>)call(container, \"getInvocations\", new Class<?>[]{});\n            baseInvocation = invocations.get(0);\n            actualInvocation = invocations.get(1);\n        }\n\n        FixtureSession(String targetClass, String method, String policy) {\n            this.targetClass = targetClass;\n            this.method = method;\n            this.reviewed = JOINT_FIXTURES.equals(policy);\n            this.langHelpers = LANG_HELPER_FIXTURES.equals(policy) || reviewed;\n            this.bufferSlices = BUFFER_FIXTURES.equals(policy) || langHelpers;\n            this.fractionField = FRACTION_FIELD_FIXTURES.equals(policy) || langHelpers;\n            this.pilot = PILOT_FIXTURES.equals(policy) || bufferSlices || fractionField;\n        }\n\n        Object[] langHelperArguments(Class<?>[] types, double[] vector) {\n            if (!langHelpers || constructing || !targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    || types.length != 1) return null;\n            double a = vector[0];\n            if (method.equals(\"isAllZeros\") && types[0] == String.class)\n                return new Object[]{new String[]{null, \"\", \"0\", \"000\", \"001\", \"12\", \"00 0\", \"-0\"}[bucket(a, 8)]};\n            if (method.equals(\"validateArray\") && types[0] == Object.class) {\n                Object[] arrays = {null, new int[0], new int[]{0}, new int[]{-1, 0, 7}};\n                validationInput = arrays[bucket(a, arrays.length)];\n                return new Object[]{validationInput};\n            }\n            return null;\n        }\n\n        Object[] boundedBufferArguments(Class<?>[] types, double[] vector) {\n            if (!bufferSlices || constructing || types.length == 0) return null;\n            double a = vector[0], b = vector[1 % vector.length];\n            if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && types[0] == char[].class) {\n                String text;\n                if (method.equals(\"parseLong\"))\n                    text = new String[]{\"1000000000\", \"1234567890123\", \"123456789012345678\"}[bucket(a, 3)];\n                else if (method.equals(\"parseInt\"))\n                    text = new String[]{\"0\", \"7\", \"12345\", \"999999999\"}[bucket(a, 4)];\n                else if (method.equals(\"inLongRange\"))\n                    text = new String[]{\"0\", \"9223372036854775807\", \"9223372036854775808\", \"9223372036854775809\"}[bucket(a, 4)];\n                else if (method.equals(\"parseBigDecimal\"))\n                    text = new String[]{\"0\", \"12.50\", \"-0.125\"}[bucket(a, 3)];\n                else return null;\n                if (types.length == 1) return new Object[]{text.toCharArray()};\n                char[] chars = (\"##\" + text + \"?\").toCharArray();\n                if (types.length == 4) return new Object[]{chars, 2, text.length(), b < 0};\n                return new Object[]{chars, 2, text.length()};\n            }\n            if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\") && method.equals(\"append\")\n                    && types.length == 3 && (types[0] == char[].class || types[0] == String.class)) {\n                String text = a < 0 ? \"xABCDy\" : \"p12345q\";\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, text.length() - offset - 1);\n                return new Object[]{types[0] == char[].class ? text.toCharArray() : text, offset, length};\n            }\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\") && method.equals(\"read\")\n                    && types.length == 3 && types[0] == char[].class) {\n                outputBuffer = new char[8];\n                Arrays.fill(outputBuffer, '~');\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, outputBuffer.length - offset - 1);\n                return new Object[]{outputBuffer, offset, length};\n            }\n            return null;\n        }\n\n        Object option(String name, String text) throws ReflectiveOperationException {\n            Object option = construct(\"org.apache.commons.cli.Option\",\n                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, \"fixture\");\n            call(option, \"setType\", new Class<?>[]{Object.class}, String.class);\n            call(option, \"addValue\", new Class<?>[]{String.class}, text);\n            return option;\n        }\n\n        Object archiveEntry(String name, long size) throws ReflectiveOperationException {\n            Object entry = construct(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\",\n                    new Class<?>[]{String.class}, name);\n            call(entry, \"setSize\", new Class<?>[]{long.class}, size);\n            call(entry, \"setTime\", new Class<?>[]{long.class}, 0L);\n            call(entry, \"setMode\", new Class<?>[]{long.class}, 0100644L);\n            return entry;\n        }\n\n        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {\n            if (!pilot) return value;\n            if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                call(value, \"addOption\", new Class<?>[]{Class.forName(\"org.apache.commons.cli.Option\")}, option(\"x\", a < 0 ? \"alpha\" : \"beta\"));\n                call(value, \"addArg\", new Class<?>[]{String.class}, \"positional\");\n            } else if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\")) {\n                char[] content = (a < 0 ? \"123\" : \"45.5\").toCharArray();\n                call(value, \"resetWithCopy\", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);\n            } else if (targetClass.equals(\"org.jsoup.nodes.Document\")) {\n                Object html = call(value, \"appendElement\", new Class<?>[]{String.class}, \"html\");\n                call(html, \"appendElement\", new Class<?>[]{String.class}, \"head\");\n                Object body = call(html, \"appendElement\", new Class<?>[]{String.class}, \"body\");\n                call(body, \"text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n                call(value, \"title\", new Class<?>[]{String.class}, \"Fixture\");\n            } else if (targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                call(value, \"putNextEntry\", new Class<?>[]{Class.forName(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\")},\n                        archiveEntry(\"fixture.txt\", method.equals(\"write\") ? 1 : 0));\n            } else if (targetClass.equals(\"org.joda.time.Partial\")) {\n                return call(value, \"with\", new Class<?>[]{Class.forName(\"org.joda.time.DateTimeFieldType\"), int.class},\n                        call(Class.forName(\"org.joda.time.DateTimeFieldType\"), \"hourOfDay\", new Class<?>[]{}), 10);\n            } else if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                receiver = value;\n                chart(a);\n            } else if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                // Real StAX input; getters start on a named leaf VALUE_STRING.\n                for (int i = 0; i < 8; i++) {\n                    Object token = call(value, \"nextToken\", new Class<?>[]{});\n                    if (token != null && token.toString().equals(\"VALUE_STRING\")) break;\n                }\n            }\n            return value;\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        Object nativeType(String name, boolean object) throws ReflectiveOperationException {\n            Class<?> nativeClass = Class.forName(\"com.google.javascript.rhino.jstype.JSTypeNative\");\n            Object key = Enum.valueOf((Class)nativeClass, name);\n            return call(registry, object ? \"getNativeObjectType\" : \"getNativeType\", new Class<?>[]{nativeClass}, key);\n        }\n\n        void closure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> scopeClass = Class.forName(\"com.google.javascript.jscomp.Scope\");\n            Class<?> abstractCompiler = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            registry = call(compiler, \"getTypeRegistry\", new Class<?>[]{});\n            String expression = a < 0 ? \"x + 1\" : \"x + 's'\";\n            if (method.contains(\"And\") || method.contains(\"ShortCircuit\")) expression = \"x && true\";\n            if (method.contains(\"Or\")) expression = \"x || false\";\n            if (method.equals(\"traverseArrayLiteral\")) expression = \"[x, 1]\";\n            if (method.equals(\"traverseObjectLiteral\")) expression = \"({p:x})\";\n            if (method.equals(\"traverseHook\")) expression = \"x ? 1 : 2\";\n            if (method.equals(\"traverseAssign\")) expression = \"x = 2\";\n            if (method.equals(\"traverseGetElem\")) expression = \"x['p']\";\n            if (method.equals(\"traverseGetProp\") || method.contains(\"Property\")) expression = \"x.p\";\n            if (method.equals(\"traverseName\") || method.equals(\"redeclareSimpleVar\")\n                    || method.equals(\"narrowScope\") || method.equals(\"updateScopeForTypeChange\")) expression = \"x\";\n            Object script = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                    \"function fixture(x) { return \" + expression + \"; }\");\n            Object function = call(script, \"getFirstChild\", new Class<?>[]{});\n            Object global = call(scopeClass, \"createGlobalScope\", new Class<?>[]{node}, script);\n            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);\n            Object astParameters = call(call(function, \"getFirstChild\", new Class<?>[]{}), \"getNext\", new Class<?>[]{});\n            Object name = call(astParameters, \"getFirstChild\", new Class<?>[]{});\n            call(scope, \"declare\", new Class<?>[]{String.class, node,\n                    Class.forName(\"com.google.javascript.rhino.jstype.JSType\"),\n                    Class.forName(\"com.google.javascript.jscomp.CompilerInput\")}, \"x\", name, nativeType(\"UNKNOWN_TYPE\", false), null);\n            Object body = call(function, \"getLastChild\", new Class<?>[]{});\n            Object returnNode = call(body, \"getFirstChild\", new Class<?>[]{});\n            closureNode = method.equals(\"traverseReturn\") || method.equals(\"branchedFlowThrough\")\n                    ? returnNode : call(returnNode, \"getFirstChild\", new Class<?>[]{});\n            if (method.equals(\"traverseObjectLiteral\"))\n                call(closureNode, \"setJSType\", new Class<?>[]{Class.forName(\"com.google.javascript.rhino.jstype.JSType\")}, nativeType(\"OBJECT_TYPE\", true));\n            Object analysis = construct(\"com.google.javascript.jscomp.ControlFlowAnalysis\",\n                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);\n            call(analysis, \"process\", new Class<?>[]{node, node}, null, function);\n            cfg = call(analysis, \"getCfg\", new Class<?>[]{});\n            Object convention = call(compiler, \"getCodingConvention\", new Class<?>[]{});\n            reverse = construct(\"com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter\",\n                    new Class<?>[]{Class.forName(\"com.google.javascript.jscomp.CodingConvention\"), registry.getClass()}, convention, registry);\n            flow = call(Class.forName(\"com.google.javascript.jscomp.LinkedFlowScope\"), \"createEntryLattice\",\n                    new Class<?>[]{scopeClass}, scope);\n            call(flow, \"inferSlotType\", new Class<?>[]{String.class, Class.forName(\"com.google.javascript.rhino.jstype.JSType\")},\n                    \"x\", nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false));\n        }\n\n        void dom(double a) throws Exception {\n            if (domRoot != null) return;\n            javax.xml.parsers.DocumentBuilderFactory factory = pilot\n                ? javax.xml.parsers.DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", SqaProbe.class.getClassLoader())\n                : javax.xml.parsers.DocumentBuilderFactory.newInstance();\n            factory.setNamespaceAware(true);\n            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();\n            domRoot = document.createElementNS(\"urn:sqa:root\", \"r:root\");\n            document.appendChild(domRoot);\n            domRoot.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:r\", \"urn:sqa:root\");\n            domRoot.setAttributeNS(\"http://www.w3.org/XML/1998/namespace\", \"xml:lang\", \"en\");\n            org.w3c.dom.Element element = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            domChild = element;\n            element.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:i\", \"urn:sqa:item\");\n            element.setAttribute(\"id\", a < 0 ? \"left\" : \"right\");\n            domChild.appendChild(document.createTextNode(a < 0 ? \"alpha\" : \"beta\"));\n            org.w3c.dom.Element grandchild = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            grandchild.appendChild(document.createTextNode(\"nested\"));\n            domChild.appendChild(grandchild);\n            org.w3c.dom.Element last = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            last.appendChild(document.createTextNode(\"nested-last\"));\n            domChild.appendChild(last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                domRoot.appendChild(document.createProcessingInstruction(\"fixture\", \"before\"));\n                domChild = document.createProcessingInstruction(\"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                domRoot.appendChild(document.createCDATASection(\"before\"));\n                domChild = document.createTextNode(a < 0 ? \"alpha\" : \"beta\");\n            }\n            domRoot.appendChild(domChild);\n        }\n\n        void jdom(double a) throws ReflectiveOperationException {\n            if (jdomRoot != null) return;\n            Class<?> element = Class.forName(\"org.jdom.Element\");\n            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, \"root\");\n            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(jdomChild, \"setText\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            call(jdomChild, \"setAttribute\", new Class<?>[]{String.class, String.class}, \"id\", a < 0 ? \"left\" : \"right\");\n            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(grandchild, \"setText\", new Class<?>[]{String.class}, \"nested\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, grandchild);\n            Object last = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(last, \"setText\", new Class<?>[]{String.class}, \"nested-last\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                Object before = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                Object before = construct(\"org.jdom.CDATA\", new Class<?>[]{String.class}, \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.Text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            }\n            call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, jdomChild);\n        }\n\n        void configurePointer(Object pointer) throws ReflectiveOperationException {\n            Class<?> resolverClass = Class.forName(\"org.apache.commons.jxpath.ri.NamespaceResolver\");\n            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"i\", \"urn:sqa:item\");\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"r\", \"urn:sqa:root\");\n            call(resolver, \"setNamespaceContextPointer\", new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\")}, pointer);\n            call(pointer, \"setNamespaceResolver\", new Class<?>[]{resolverClass}, resolver);\n        }\n\n        Object argument(Class<?> type, double a, double b, double c, int depth) {\n            try {\n                if (depth > 2) throw new FixtureFailure(\"Fixture recursion limit: \" + type.getName(), null);\n                String name = type.getName();\n                if (reviewed && !constructing && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                        && method.equals(\"setMaxCodeLen\") && type == int.class)\n                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;\n                if (pilot) {\n                    if (targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? \"value\" : \"items\");\n                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];\n                        if (type == java.lang.reflect.Field.class) return value;\n                        if (type == Class.class) return GenericFixture.class;\n                        if (type == java.lang.reflect.Type.class) {\n                            if (method.equals(\"getTypeInfoForArray\")) return a < 0 ? String[].class : Integer[].class;\n                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                        }\n                    }\n                    if (targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\")) {\n                        unusedClosure(a);\n                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.\n                        if (name.equals(\"com.google.javascript.jscomp.AbstractCompiler\")) return compiler;\n                        if (name.equals(\"com.google.javascript.rhino.Node\")) {\n                            if (method.equals(\"process\")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;\n                            if (method.equals(\"getFunctionArgList\")) {\n                                Object child = call(cleanupScript, \"getFirstChild\", new Class<?>[]{});\n                                while (child != null && !(Boolean)call(child, \"isFunction\", new Class<?>[]{}))\n                                    child = call(child, \"getNext\", new Class<?>[]{});\n                                if (child == null) throw new FixtureFailure(\"Missing parsed function\", null);\n                                return child;\n                            }\n                            return cleanupScript;\n                        }\n                    }\n                    if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                        chart(a);\n                        if (name.equals(\"org.jfree.data.category.CategoryDataset\")) return chartDataset;\n                        if (name.equals(\"org.jfree.chart.axis.CategoryAxis\")) return chartAxis;\n                        if (type == Comparable.class) return a < 0 ? \"row-a\" : \"column-a\";\n                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);\n                        if (name.equals(\"org.jfree.chart.util.RectangleEdge\")) return type.getField(\"BOTTOM\").get(null);\n                        if (type == int.class) return 0;\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\")) {\n                        if (name.equals(targetClass)) return beanWriter();\n                        if (name.equals(\"com.fasterxml.jackson.databind.util.NameTransformer\"))\n                            return call(type, \"simpleTransformer\", new Class<?>[]{String.class, String.class}, a < 0 ? \"left_\" : \"right_\", \"_suffix\");\n                        if (type == Object.class) return method.equals(\"get\") ? new FixtureBean() : a < 0 ? \"fixture-key\" : \"fixture-value\";\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\")) {\n                        jacksonCollection(a);\n                        if (name.equals(\"com.fasterxml.jackson.databind.JavaType\")) return collectionType;\n                        if (name.equals(\"com.fasterxml.jackson.core.JsonParser\")) return parser;\n                        if (name.equals(\"com.fasterxml.jackson.databind.DeserializationContext\")) return context;\n                        if (name.equals(\"com.fasterxml.jackson.databind.deser.ValueInstantiator\"))\n                            return call(collectionDeserializer, \"getValueInstantiator\", new Class<?>[]{});\n                        if (name.equals(\"com.fasterxml.jackson.databind.JsonDeserializer\"))\n                            return Class.forName(\"com.fasterxml.jackson.databind.deser.std.StringDeserializer\").getField(\"instance\").get(null);\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                        String xml = a < 0 ? \"<root><item>123</item><other>alpha</other></root>\" : \"<root><item>45</item><other>beta</other></root>\";\n                        if (type == int.class && constructing) return 0;\n                        if (name.equals(\"com.fasterxml.jackson.core.io.IOContext\"))\n                            return construct(name, new Class<?>[]{Class.forName(\"com.fasterxml.jackson.core.util.BufferRecycler\"), Object.class, boolean.class},\n                                construct(\"com.fasterxml.jackson.core.util.BufferRecycler\", new Class<?>[]{}), xml, false);\n                        if (name.equals(\"com.fasterxml.jackson.core.ObjectCodec\")) return construct(\"com.fasterxml.jackson.dataformat.xml.XmlMapper\", new Class<?>[]{});\n                        if (type == javax.xml.stream.XMLStreamReader.class) {\n                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));\n                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();\n                            return reader;\n                        }\n                    }\n                    if (targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")) {\n                        mockito(a);\n                        if (name.equals(\"org.mockito.invocation.Invocation\")) return constructing ? baseInvocation : actualInvocation;\n                    }\n                    if (targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) {\n                        int number = 1 + bucket(a, 8);\n                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;\n                        if (type == int.class) return number;\n                        if (type == long.class) return (long)number;\n                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);\n                        if (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\"))\n                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);\n                    }\n                    if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                        if (type == String.class) return constructing ? \"fixture\" : a < -0.33 ? \"x\" : a < 0.33 ? \"missing\" : \"extra\";\n                        if (type == char.class) return a < 0 ? 'x' : 'z';\n                        if (name.equals(\"org.apache.commons.cli.Option\")) return option(\"extra\", a < 0 ? \"left\" : \"right\");\n                    }\n                    if (targetClass.equals(\"org.jsoup.nodes.Document\") && type == String.class)\n                        return constructing ? \"https://fixture.invalid/\" : method.equals(\"createElement\") ? a < 0 ? \"span\" : \"section\"\n                            : STRINGS[bucket(a, STRINGS.length)];\n                    if (targetClass.equals(\"org.joda.time.Partial\")) {\n                        if (type == int.class) return bucket(a, 24);\n                        if (name.equals(\"org.joda.time.DateTimeFieldType\"))\n                            return call(type, \"hourOfDay\", new Class<?>[]{});\n                    }\n                    if (name.equals(\"org.joda.time.DurationFieldType\")) return call(type, a < 0 ? \"hours\" : \"days\", new Class<?>[]{});\n                    if (name.equals(\"org.joda.time.DurationField\")) return call(Class.forName(\"org.joda.time.field.UnsupportedDurationField\"),\n                        \"getInstance\", new Class<?>[]{Class.forName(\"org.joda.time.DurationFieldType\")},\n                        call(Class.forName(\"org.joda.time.DurationFieldType\"), \"hours\", new Class<?>[]{}));\n                    if (name.equals(\"com.fasterxml.jackson.core.util.BufferRecycler\")) return construct(name, new Class<?>[]{});\n                    if (type == java.io.OutputStream.class && targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                        archiveBytes = new java.io.ByteArrayOutputStream();\n                        return archiveBytes;\n                    }\n                    if (name.equals(\"org.apache.commons.compress.archivers.ArchiveEntry\") || name.equals(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\"))\n                        return archiveEntry(a < 0 ? \"next-left.txt\" : \"next-right.txt\", 0);\n                    if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && type == String.class)\n                        return new String[]{\"0\", \"1\", \"12\", \"2147483647\"}[bucket(a, 4)];\n                }\n                if (scalar(type)) {\n                    if (type == String.class && method.equals(\"getRelativePositionOfPI\")) return a < 0 ? \"fixture\" : \"other\";\n                    if (type == String.class && (method.equals(\"namespacePointer\") || method.equals(\"getNamespaceURI\")))\n                        return a < 0 ? \"r\" : \"i\";\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                }\n                if (type.isArray()) {\n                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith(\"NumberUtils\") || targetClass.endsWith(\"TypeInfoFactory\")) ? 1 + bucket(c, 4) : bucket(c, 5));\n                    for (int i = 0; i < Array.getLength(array); i++)\n                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));\n                    return array;\n                }\n                if (type == java.io.Reader.class && targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                    return new java.io.StringReader(bufferSlices ? (a < 0 ? \"A\\nBC\\nDE\" : \"12\\n345\\n\") : STRINGS[bucket(a, STRINGS.length)]);\n                if (name.startsWith(\"com.google.javascript.\")) {\n                    closure(a);\n                    if (name.endsWith(\".AbstractCompiler\")) return compiler;\n                    if (name.endsWith(\".ControlFlowGraph\")) return cfg;\n                    if (name.endsWith(\".ReverseAbstractInterpreter\")) return reverse;\n                    if (name.endsWith(\".Scope\")) return scope;\n                    if (name.endsWith(\".Scope$Var\")) return call(scope, \"getVar\", new Class<?>[]{String.class}, \"x\");\n                    if (name.endsWith(\".FlowScope\")) return flow;\n                    if (name.endsWith(\".Node\")) return closureNode;\n                    if (name.endsWith(\".JSType\")) return nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false);\n                    if (name.endsWith(\".ObjectType\")) return nativeType(\"OBJECT_TYPE\", true);\n                }\n                if (name.startsWith(\"org.w3c.dom.\")) {\n                    dom(a);\n                    if (type.isInstance(domChild)) return domChild;\n                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();\n                }\n                if (type == java.util.Locale.class) return java.util.Locale.ROOT;\n                if (name.equals(\"org.apache.commons.jxpath.ri.QName\"))\n                    return construct(name, new Class<?>[]{String.class}, method.equals(\"attributeIterator\") ? \"id\" : \"item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.compiler.NodeTest\"))\n                    return construct(\"org.apache.commons.jxpath.ri.compiler.NodeNameTest\",\n                            new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.QName\"), String.class},\n                            targetClass.contains(\".jdom.\")\n                                ? construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class}, \"item\")\n                                : construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class, String.class}, \"i\", \"item\"),\n                            targetClass.contains(\".jdom.\") ? null : \"urn:sqa:item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.model.NodePointer\")) {\n                    if (targetClass.contains(\".jdom.\")) {\n                        jdom(a);\n                        if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                            List<?> children = (List<?>)call(jdomChild, \"getContent\", new Class<?>[]{});\n                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);\n                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);\n                            configurePointer(pointer);\n                            return pointer;\n                        }\n                        Object pointer = construct(\"org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\",\n                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    dom(a);\n                    if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();\n                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    Object pointer = construct(\"org.apache.commons.jxpath.ri.model.dom.DOMNodePointer\",\n                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);\n                    configurePointer(pointer);\n                    return pointer;\n                }\n                if (type == Object.class && targetClass.contains(\".jdom.\")\n                        && (constructing || !method.equals(\"setValue\"))) { jdom(a); return jdomChild; }\n                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();\n                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n                    return new ArrayList<Object>();\n                if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n                if (type == Object.class || type == Number.class || type == java.util.Date.class)\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                throw new FixtureFailure(\"No explicit recipe: \" + name, null);\n            } catch (FixtureFailure failure) { throw failure; }\n            catch (Exception failure) { throw new FixtureFailure(\"Fixture recipe failed: \" + type.getName()\n                    + \":\" + failure.getClass().getName() + \":\" + failure.getMessage(), failure); }\n        }\n\n        String nodeSnapshot(org.w3c.dom.Node node, int depth) {\n            if (depth > 8) return \"depth-limit\";\n            StringBuilder out = new StringBuilder(\"node:\").append(node.getNodeType()).append(':')\n                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));\n            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();\n            List<String> attrs = new ArrayList<String>();\n            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)\n                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));\n            java.util.Collections.sort(attrs);\n            out.append(attrs.toString()).append('[');\n            org.w3c.dom.NodeList children = node.getChildNodes();\n            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));\n            return out.append(\"]children:\").append(children.getLength()).toString();\n        }\n\n        Object field(Object value, String name) throws ReflectiveOperationException {\n            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {\n                try {\n                    java.lang.reflect.Field field = type.getDeclaredField(name);\n                    field.setAccessible(true);\n                    return field.get(value);\n                } catch (NoSuchFieldException missing) { }\n            }\n            throw new NoSuchFieldException(name);\n        }\n\n        String projection(Object result, int depth) throws ReflectiveOperationException {\n            if (depth > 8) throw new FixtureFailure(\"Oracle projection depth exceeded\", null);\n            if (result == null) return \"null\";\n            String name = result.getClass().getName();\n            if (fractionField && (name.equals(\"org.apache.commons.math3.fraction.BigFractionField\")\n                    || name.equals(\"org.apache.commons.math3.fraction.FractionField\")))\n                return \"fraction-field:runtime=\" + projection(call(result, \"getRuntimeClass\", new Class<?>[]{}), depth + 1)\n                    + \":zero=\" + projection(call(result, \"getZero\", new Class<?>[]{}), depth + 1)\n                    + \":one=\" + projection(call(result, \"getOne\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.lang.reflect.Type) return \"type:\" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());\n            if (pilot && result instanceof Method) return \"method:\" + nestedTestName(((Method)result).toGenericString());\n            if (pilot && name.startsWith(\"com.google.gson.TypeInfo\"))\n                return \"type-info:\" + projection(call(result, \"getActualType\", new Class<?>[]{}), depth + 1);\n            if (pilot && name.equals(\"com.google.javascript.rhino.Node\")) return \"ast:\" + call(result, \"toStringTree\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.jxpath.ri.NamespaceResolver\"))\n                return \"namespaces:r=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"r\")\n                    + \":i=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"i\");\n            if (pilot && name.equals(\"org.jfree.data.Range\"))\n                return \"range:\" + call(result, \"getLowerBound\", new Class<?>[]{}) + ':' + call(result, \"getUpperBound\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItem\")) return \"legend:\" + call(result, \"getLabel\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItemCollection\")) {\n                StringBuilder out = new StringBuilder(\"legends[\");\n                int count = ((Number)call(result, \"getItemCount\", new Class<?>[]{})).intValue();\n                if (count > 256) throw new FixtureFailure(\"Legend limit exceeded\", null);\n                for (int i = 0; i < count; i++) out.append(projection(call(result, \"get\", new Class<?>[]{int.class}, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && name.startsWith(\"com.fasterxml.jackson.databind.type.\")) return \"java-type:\" + call(result, \"toCanonical\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.core.io.SerializedString\")) return \"serialized-name:\" + call(result, \"getValue\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return \"property:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + projection(call(result, \"getType\", new Class<?>[]{}), depth + 1);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")\n                    && Class.forName(\"org.mockito.invocation.Invocation\").isInstance(result))\n                return \"invocation:\" + projection(call(result, \"getMethod\", new Class<?>[]{}), depth + 1)\n                    + ':' + projection(call(result, \"getArguments\", new Class<?>[]{}), depth + 1)\n                    + \":verified=\" + call(result, \"isVerified\", new Class<?>[]{});\n            if (pilot && result.getClass().isArray()) {\n                int length = Array.getLength(result);\n                if (length > 100000) throw new FixtureFailure(\"Oracle array limit exceeded\", null);\n                StringBuilder out = new StringBuilder(\"array[\");\n                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.jsoup.nodes.Document\") || name.equals(\"org.jsoup.nodes.Element\")))\n                return \"html:\" + call(result, \"outerHtml\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.cli.Option\"))\n                return \"option:\" + call(result, \"getOpt\", new Class<?>[]{}) + ':' + projection(call(result, \"getValues\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.util.Iterator) {\n                StringBuilder out = new StringBuilder(\"iterator[\");\n                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;\n                int count = 0;\n                while (iterator.hasNext()) {\n                    if (++count > 256) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                    out.append(projection(iterator.next(), depth + 1)).append(';');\n                }\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\")))\n                return \"fraction:\" + call(result, \"getNumerator\", new Class<?>[]{}) + '/' + call(result, \"getDenominator\", new Class<?>[]{});\n            if (pilot && name.startsWith(\"org.joda.time.\")) {\n                if (name.equals(\"org.joda.time.Partial\")) return \"partial:\" + call(result, \"toStringList\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationFieldType\").isInstance(result)) return \"duration-type:\" + call(result, \"getName\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationField\").isInstance(result))\n                    return \"duration:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + call(result, \"isSupported\", new Class<?>[]{});\n            }\n            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);\n            if (reviewed && name.equals(\"org.jdom.Attribute\"))\n                return \"jdom-attribute:name=\" + projection(call(result, \"getName\", new Class<?>[]{}), depth + 1)\n                    + \":namespace=\" + projection(call(result, \"getNamespaceURI\", new Class<?>[]{}), depth + 1)\n                    + \":value=\" + projection(call(result, \"getValue\", new Class<?>[]{}), depth + 1);\n            if (name.equals(\"org.jdom.Element\") || name.equals(\"org.jdom.ProcessingInstruction\")\n                    || name.equals(\"org.jdom.Text\") || name.equals(\"org.jdom.CDATA\")) {\n                Object writer = construct(\"org.jdom.output.XMLOutputter\", new Class<?>[]{});\n                return \"xml:\" + call(writer, \"outputString\", new Class<?>[]{result.getClass()}, result);\n            }\n            if (name.equals(\"org.apache.commons.jxpath.ri.QName\")) return \"qname:\" + result.toString();\n            if (name.startsWith(\"com.google.javascript.rhino.jstype.\")) return \"js-type:\" + result.toString();\n            if (name.equals(\"com.google.javascript.jscomp.LinkedFlowScope\")) {\n                Object slot = call(result, \"getSlot\", new Class<?>[]{String.class}, \"x\");\n                return \"flow:x=\" + (slot == null ? \"absent\" : projection(call(slot, \"getType\", new Class<?>[]{}), depth + 1));\n            }\n            if (name.endsWith(\"TypeInference$BooleanOutcomePair\"))\n                return \"boolean-pair:\" + field(result, \"toBooleanOutcomes\") + ':' + field(result, \"booleanValues\")\n                    + \":left=\" + projection(field(result, \"leftScope\"), depth + 1)\n                    + \":right=\" + projection(field(result, \"rightScope\"), depth + 1);\n            if (result instanceof List) {\n                StringBuilder out = new StringBuilder(\"list[\");\n                if (((List<?>)result).size() > 256) throw new FixtureFailure(\"Oracle collection limit exceeded\", null);\n                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (result instanceof java.util.Map) {\n                java.util.Map<?,?> map = (java.util.Map<?,?>)result;\n                if (map.size() > 256) throw new FixtureFailure(\"Oracle map limit exceeded\", null);\n                List<String> entries = new ArrayList<String>();\n                for (java.util.Map.Entry<?,?> entry : map.entrySet())\n                    entries.add(projection(entry.getKey(), depth + 1) + \"=\" + projection(entry.getValue(), depth + 1));\n                java.util.Collections.sort(entries);\n                return \"map:\" + entries.toString();\n            }\n            if (name.startsWith(\"org.apache.commons.jxpath.ri.model.\")) {\n                Class<?> pointer = Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\");\n                if (pointer.isInstance(result))\n                    return \"pointer:\" + projection(call(result, \"getImmediateNode\", new Class<?>[]{}), depth + 1);\n                if (Class.forName(\"org.apache.commons.jxpath.ri.model.NodeIterator\").isInstance(result)) {\n                    StringBuilder out = new StringBuilder(\"iterator[\");\n                    for (int i = 1; i <= 9; i++) {\n                        boolean present = (Boolean)call(result, \"setPosition\", new Class<?>[]{int.class}, i);\n                        if (!present) return out.append(']').toString();\n                        if (i == 9) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                        out.append(projection(call(result, \"getNodePointer\", new Class<?>[]{}), depth + 1)).append(';');\n                    }\n                }\n            }\n            String simple = value(result);\n            if (simple.startsWith(\"object-type:\")) throw new FixtureFailure(\"No structural oracle: \" + name, null);\n            return simple;\n        }\n\n        String state() throws ReflectiveOperationException {\n            if (reviewed && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                    && method.equals(\"setMaxCodeLen\")) {\n                int limit = ((Number)call(receiver, \"getMaxCodeLen\", new Class<?>[]{})).intValue();\n                String encoded = (String)call(receiver, \"metaphone\", new Class<?>[]{String.class}, \"architecture\");\n                return \"metaphone:maxCodeLen=\" + limit + \":encoded=\" + encoded\n                    + \":maxCodeLenAfterEncoding=\" + call(receiver, \"getMaxCodeLen\", new Class<?>[]{});\n            }\n            if (langHelpers && targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && method.equals(\"validateArray\")) return \"validation-input:\" + value(validationInput);\n            if (pilot && targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\"))\n                return \"cleanup:\" + call(cleanupScript, \"toStringTree\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\"))\n                return \"chart:rows=\" + call(chartDataset, \"getRowCount\", new Class<?>[]{}) + \":columns=\" + call(chartDataset, \"getColumnCount\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return projection(receiver, 0) + \":setting=\" + projection(call(receiver, \"getInternalSetting\", new Class<?>[]{Object.class}, \"fixture-key\"), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\"))\n                return \"json-token:\" + call(parser, \"getCurrentToken\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\"))\n                return \"xml:closed=\" + call(receiver, \"isClosed\", new Class<?>[]{}) + \":token=\" + call(receiver, \"getCurrentToken\", new Class<?>[]{})\n                    + \":text=\" + projection(field(receiver, \"_currText\"), 0);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\"))\n                return projection(baseInvocation, 0) + \":candidate=\" + projection(actualInvocation, 0);\n            if (pilot && targetClass.equals(\"org.apache.commons.cli.CommandLine\"))\n                return \"cli:\" + projection(call(receiver, \"getOptions\", new Class<?>[]{}), 0) + ':' + projection(call(receiver, \"getArgs\", new Class<?>[]{}), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\"))\n                return \"text:\" + call(receiver, \"contentsAsString\", new Class<?>[]{}) + \":size=\" + call(receiver, \"size\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jsoup.nodes.Document\") && receiver != null) return projection(receiver, 0);\n            if (pilot && targetClass.endsWith(\"CpioArchiveOutputStream\")) return \"archive:\" + value(archiveBytes.toByteArray());\n            if (pilot && targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.Partial\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.field.UnsupportedDurationField\") && receiver != null) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.collections.map.Flat3Map\")) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                return \"reader:line=\" + call(receiver, \"getLineNumber\", new Class<?>[]{})\n                    + \":last=\" + call(receiver, \"readAgain\", new Class<?>[]{})\n                    + (bufferSlices && outputBuffer != null ? \":buffer=\" + value(outputBuffer) : \"\");\n            if (compiler != null) {\n                Object jsType = call(closureNode, \"getJSType\", new Class<?>[]{});\n                return \"ast:\" + call(closureNode, \"toStringTree\", new Class<?>[]{})\n                    + \":ast-type=\" + projection(jsType, 0) + ':' + projection(flow, 0);\n            }\n            if (domRoot != null) return nodeSnapshot(domRoot, 0) + \":child=\" + nodeSnapshot(domChild, 0)\n                    + \":attached=\" + (domChild.getParentNode() != null);\n            if (jdomRoot != null) return projection(jdomRoot, 0) + \":child=\" + projection(jdomChild, 0)\n                    + \":attached=\" + (call(jdomChild, \"getParent\", new Class<?>[]{}) != null);\n            return \"stateless-scalars\";\n        }\n    }\n\n    private static String quote(String value) {\n        StringBuilder out = new StringBuilder(\"\\\"\");\n        for (char c : value.toCharArray()) {\n            if (c == '\"' || c == '\\\\') out.append('\\\\').append(c);\n            else if (c < 32) out.append(String.format(\"\\\\u%04x\", (int)c));\n            else out.append(c);\n        }\n        return out.append('\"').toString();\n    }\n\n    private static String typeNames(Class<?>[] types) {\n        List<String> names = new ArrayList<String>();\n        for (Class<?> type : types) names.add(type.getName());\n        return String.join(\",\", names);\n    }\n\n    private static boolean scalar(Class<?> type) {\n        return type.isPrimitive() || type == String.class || type == Boolean.class\n            || type == Character.class || type == Byte.class || type == Short.class\n            || type == Integer.class || type == Long.class || type == Float.class\n            || type == Double.class || type.isEnum();\n    }\n\n    private static boolean supported(Class<?> type) {\n        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));\n    }\n\n    private static boolean supportedParameters(Class<?>[] types) {\n        if (types.length > 6) return false;\n        for (Class<?> type : types) if (type == void.class) return false;\n        return true;\n    }\n\n    private static Class<?> type(String name) throws ClassNotFoundException {\n        if (name.equals(\"boolean\")) return boolean.class;\n        if (name.equals(\"byte\")) return byte.class;\n        if (name.equals(\"short\")) return short.class;\n        if (name.equals(\"int\")) return int.class;\n        if (name.equals(\"long\")) return long.class;\n        if (name.equals(\"float\")) return float.class;\n        if (name.equals(\"double\")) return double.class;\n        if (name.equals(\"char\")) return char.class;\n        return Class.forName(name);\n    }\n\n    private static Class<?>[] types(String names) throws ClassNotFoundException {\n        if (names.length() == 0) return new Class<?>[0];\n        String[] split = names.split(\",\", -1);\n        Class<?>[] result = new Class<?>[split.length];\n        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);\n        return result;\n    }\n\n    private static int bucket(double coordinate, int size) {\n        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));\n        return Math.min(size - 1, (int)(unit * size));\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c) {\n        return argument(type, a, b, c, 0);\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c, int depth) {\n        FixtureSession session = FIXTURES.get();\n        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);\n    }\n\n    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {\n        if (depth > 2) return null;\n        if (type.isArray()) {\n            int length = bucket(c, 5);\n            Object array = Array.newInstance(type.getComponentType(), length);\n            for (int i = 0; i < length; i++) {\n                Array.set(array, i, argument(type.getComponentType(),\n                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));\n            }\n            return array;\n        }\n        if (!type.isPrimitive() && a < -0.96) return null;\n        if (type == String.class) {\n            int selection = bucket(a, STRINGS.length + 4);\n            if (selection < STRINGS.length) return STRINGS[selection];\n            int length = bucket(c, 33);\n            char character = \"0123456789abcdefXYZ +-_.\".charAt(bucket(b, 23));\n            char[] value = new char[length];\n            Arrays.fill(value, character);\n            return new String(value);\n        }\n        if (type == boolean.class || type == Boolean.class) return a >= 0;\n        if (type == char.class || type == Character.class) return (char)bucket(a, 128);\n        if (type.isEnum()) {\n            Object[] values = type.getEnumConstants();\n            return values.length == 0 ? null : values[bucket(a, values.length)];\n        }\n        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);\n        if (type == byte.class || type == Byte.class) return (byte)integer;\n        if (type == short.class || type == Short.class) return (short)integer;\n        if (type == int.class || type == Integer.class) return (int)integer;\n        if (type == long.class || type == Long.class) return integer;\n        double real = b < 0 ? integer : a * 1000;\n        if (type == float.class || type == Float.class) return (float)real;\n        if (type == double.class || type == Double.class) return real;\n        if (type == Number.class) return Double.valueOf(real);\n        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);\n        if (type == java.util.Date.class) return new java.util.Date(integer);\n        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n            return new java.util.ArrayList<Object>();\n        if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith(\"java.\")) {\n            Constructor<?>[] constructors = type.getDeclaredConstructors();\n            Arrays.sort(constructors, new Comparator<Constructor<?>>() {\n                public int compare(Constructor<?> left, Constructor<?> right) {\n                    int count = left.getParameterCount() - right.getParameterCount();\n                    return count != 0 ? count : left.toString().compareTo(right.toString());\n                }\n            });\n            for (Constructor<?> constructor : constructors) {\n                if (constructor.getParameterCount() > 3) continue;\n                try {\n                    constructor.setAccessible(true);\n                    Class<?>[] parameters = constructor.getParameterTypes();\n                    Object[] values = new Object[parameters.length];\n                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);\n                    return constructor.newInstance(values);\n                } catch (ReflectiveOperationException error) {\n                    // Failed fixture construction yields an explicit null boundary input.\n                } catch (RuntimeException error) {\n                    // Encapsulated/unconstructible fixture yields the same null boundary.\n                }\n            }\n        }\n        return null;\n    }\n\n    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {\n        FixtureSession explicitSession = FIXTURES.get();\n        if (explicitSession != null) {\n            Object[] helpers = explicitSession.langHelperArguments(types, vector);\n            if (helpers != null) return helpers;\n            Object[] bounded = explicitSession.boundedBufferArguments(types, vector);\n            if (bounded != null) return bounded;\n        }\n        Object[] values = new Object[types.length];\n        for (int i = 0; i < types.length; i++) {\n            int start = offset + 3 * i;\n            values[i] = argument(types[i], vector[start % vector.length],\n                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);\n        }\n        FixtureSession session = FIXTURES.get();\n        if (session != null && session.pilot && !session.constructing) {\n            if (session.targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                try {\n                    if (session.method.equals(\"getActualType\")) {\n                        values[0] = GenericFixture.class.getField(\"items\").getGenericType();\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    } else if (session.method.equals(\"extractRealTypes\")) {\n                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField(\"value\").getGenericType()};\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    }\n                } catch (NoSuchFieldException failure) { throw new FixtureFailure(\"Generic schema field missing\", failure); }\n            }\n            if (session.targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") && session.method.equals(\"getItemMiddle\")) {\n                values[0] = \"row-a\";\n                values[1] = \"column-a\";\n            }\n        }\n        return values;\n    }\n\n    private static String value(Object value) {\n        if (value == null) return \"null\";\n        Class<?> type = value.getClass();\n        if (type.isArray()) {\n            StringBuilder out = new StringBuilder(type.getName()).append('[');\n            int length = Array.getLength(value);\n            if (length > 100000) throw new IllegalStateException(\"SQA_HARNESS oversized outcome\");\n            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');\n            return out.append(']').toString();\n        }\n        if (value instanceof Class) return \"class:\" + nestedTestName(((Class<?>)value).getName());\n        if (!scalar(type) && !(value instanceof Number)) return \"object-type:\" + type.getName();\n        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);\n        return type.getName() + \":\" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));\n    }\n\n    private static String nestedTestName(String text) {\n        // GeneratedStudyTest nests a copy of this helper, so probe-time\n        // \"SqaProbe$FixtureMock\" renders at test runtime as\n        // \"GeneratedStudyTest$SqaProbe$FixtureMock\". Oracles must compare\n        // the probe-time spelling in both phases; never edit old suites.\n        return text.replace(\"GeneratedStudyTest$SqaProbe$\", \"SqaProbe$\");\n    }\n\n    private static String snapshot(String observed) {\n        // JVM string constants are limited to 65,535 encoded bytes. Long exact\n        // observations use a deterministic digest rather than enormous literals.\n        if (observed.length() <= 16000) return observed;\n        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);\n        try {\n            byte[] digest = MessageDigest.getInstance(\"SHA-256\").digest(bytes);\n            StringBuilder hex = new StringBuilder();\n            for (byte item : digest) hex.append(String.format(\"%02x\", item & 255));\n            return \"sha256:\" + hex + \":bytes:\" + bytes.length;\n        } catch (NoSuchAlgorithmException error) {\n            throw new IllegalStateException(\"SQA_HARNESS SHA-256 unavailable\", error);\n        }\n    }\n\n    public static String observe(String className, String constructorTypes, String methodName,\n                                 String methodTypes, double[] vector) {\n        INVOKED.set(false);\n        if (vector.length == 0) throw new IllegalArgumentException(\"SQA_HARNESS empty vector\");\n        try {\n            Class<?> target = Class.forName(className);\n            Class<?>[] ctorTypes = types(constructorTypes);\n            Class<?>[] parameterTypes = types(methodTypes);\n            Object receiver = null;\n            Method method = null;\n            if (!methodName.equals(\"<init>\")) {\n                Class<?> declaring = target;\n                while (declaring != null) {\n                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }\n                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n                }\n                if (method == null) throw new NoSuchMethodException(methodName);\n                method.setAccessible(true);\n            }\n            if (method == null || !Modifier.isStatic(method.getModifiers())) {\n                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);\n                ctor.setAccessible(true);\n                FixtureSession session = FIXTURES.get();\n                if (session != null) session.constructing = true;\n                try {\n                    Object[] values = arguments(ctorTypes, vector, 0);\n                    if (method == null) INVOKED.set(true);\n                    receiver = ctor.newInstance(values);\n                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);\n                    if (session != null) session.receiver = receiver;\n                    if (session != null && className.equals(\"org.apache.commons.collections.map.Flat3Map\")) {\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-a\", \"value-a\");\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-b\", \"value-b\");\n                    }\n                    if (session != null && className.startsWith(\"org.apache.commons.jxpath.ri.model.\")) session.configurePointer(receiver);\n                } catch (InvocationTargetException error) {\n                    if (session != null && method != null)\n                        throw new FixtureFailure(\"Receiver constructor failed before method invocation\", error.getCause());\n                    throw error;\n                } finally { if (session != null) session.constructing = false; }\n            }\n            if (method == null) {\n                if (FIXTURES.get() == null) return \"constructed:\" + target.getName();\n                try { return snapshot(\"constructed:\" + target.getName() + \":state=\" + FIXTURES.get().state()); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Constructor state oracle failed\", failure); }\n            }\n            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);\n            INVOKED.set(true);\n            Object result = method.invoke(receiver, values);\n            if (FIXTURES.get() != null) {\n                FixtureSession session = FIXTURES.get();\n                try {\n                    return snapshot((method.getReturnType() == void.class ? \"void\" : \"value:\" + session.projection(result, 0))\n                            + \"|state=\" + session.state());\n                } catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Structural oracle failed\", failure); }\n            }\n            return method.getReturnType() == void.class ? \"void\" : snapshot(\"value:\" + value(result));\n        } catch (InvocationTargetException error) {\n            Throwable cause = error.getCause();\n            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)\n                throw new IllegalStateException(\"SQA_HARNESS JVM failure\", cause);\n            FixtureSession session = FIXTURES.get();\n            if (session != null && session.langHelpers && session.targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && session.method.equals(\"validateArray\")) {\n                try { return \"exception:\" + cause.getClass().getName() + \"|message=\" + value(cause.getMessage())\n                        + \"|state=\" + session.state(); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Validation boundary oracle failed\", failure); }\n            }\n            return \"exception:\" + cause.getClass().getName();\n        } catch (ReflectiveOperationException error) {\n            throw new IllegalStateException(\"SQA_HARNESS reflection failure\", error);\n        } catch (LinkageError error) {\n            throw new IllegalStateException(\"SQA_HARNESS linkage failure\", error);\n        }\n    }\n\n    public static String observeWithPolicy(String className, String constructorTypes, String methodName,\n            String methodTypes, double[] vector, String policy) {\n        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)\n                && !PILOT_FIXTURES.equals(policy) && !BUFFER_FIXTURES.equals(policy)\n                && !FRACTION_FIELD_FIXTURES.equals(policy) && !LANG_HELPER_FIXTURES.equals(policy) && !JOINT_FIXTURES.equals(policy))\n            throw new IllegalArgumentException(\"Unknown explicit fixture policy\");\n        FIXTURES.set(new FixtureSession(className, methodName, policy));\n        try { return observe(className, constructorTypes, methodName, methodTypes, vector); }\n        finally { FIXTURES.remove(); }\n    }\n\n    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }\n\n    private static String descriptor(String className, String ctor, String method, String params, int count) {\n        return \"{\\\"class\\\":\" + quote(className) + \",\\\"constructor_types\\\":\" + quote(ctor)\n            + \",\\\"method\\\":\" + quote(method) + \",\\\"parameter_types\\\":\" + quote(params)\n            + \",\\\"dimensions\\\":\" + Math.max(3, count * 3) + \"}\";\n    }\n\n    private static void discover(String[] classes, List<String> fixtureClasses) {\n        List<String> targets = new ArrayList<String>();\n        List<String> errors = new ArrayList<String>();\n        for (String className : classes) {\n            try {\n                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());\n                Class<?> receiverType = target;\n                if (Modifier.isAbstract(target.getModifiers())) {\n                    for (String name : fixtureClasses) {\n                        try {\n                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());\n                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)\n                                    && candidate.getDeclaredConstructors().length > 0) {\n                                receiverType = candidate;\n                                break;\n                            }\n                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }\n                    }\n                }\n                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();\n                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {\n                    Constructor<?>[] all = receiverType.getDeclaredConstructors();\n                    Arrays.sort(all, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }\n                    });\n                    for (Constructor<?> ctor : all) {\n                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);\n                    }\n                    // Select a constructor before generating inputs; prefer the simplest fixture.\n                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }\n                    });\n                }\n                Method[] methods = target.getDeclaredMethods();\n                Arrays.sort(methods, new Comparator<Method>() {\n                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }\n                });\n                for (Method method : methods) {\n                    if (method.isSynthetic() || method.getName().equals(\"main\")\n                        || method.isBridge() || !supportedParameters(method.getParameterTypes())\n                        ) continue;\n                    if (Modifier.isStatic(method.getModifiers())) {\n                        targets.add(descriptor(className, \"\", method.getName(),\n                            typeNames(method.getParameterTypes()), method.getParameterCount()));\n                    } else if (!constructors.isEmpty()) {\n                        Constructor<?> ctor = constructors.get(0);\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),\n                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));\n                    }\n                }\n                for (Constructor<?> ctor : constructors) {\n                    if (ctor.getParameterCount() > 0)\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), \"<init>\", \"\", ctor.getParameterCount()));\n                }\n            } catch (Throwable error) {\n                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;\n                errors.add(quote(className + \":\" + error.getClass().getName()));\n            }\n        }\n        System.out.println(\"{\\\"targets\\\":[\" + String.join(\",\", targets) + \"],\\\"errors\\\":[\" + String.join(\",\", errors) + \"]}\");\n    }\n\n    public static void main(String[] args) throws Exception {\n        if (args.length > 0 && args[0].equals(\"discover\")) {\n            int start = 1;\n            List<String> fixtures = new ArrayList<String>();\n            if (args.length > 2 && args[1].equals(\"--fixtures\")) {\n                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);\n                start = 3;\n            }\n            discover(Arrays.copyOfRange(args, start, args.length), fixtures);\n            return;\n        }\n        if ((args.length != 6 && args.length != 7) || !args[0].equals(\"observe\"))\n            throw new IllegalArgumentException(\"SQA_HARNESS expected discover classes or observe class ctor method types vector\");\n        String[] pieces = args[5].split(\",\");\n        double[] vector = new double[pieces.length];\n        for (int i = 0; i < pieces.length; i++) {\n            vector[i] = Double.parseDouble(pieces[i]);\n            if (!Double.isFinite(vector[i]))\n                throw new IllegalArgumentException(\"SQA_HARNESS nonfinite vector\");\n        }\n        String outcome;\n        try {\n            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])\n                : observe(args[1], args[2], args[3], args[4], vector);\n        } catch (FixtureFailure failure) {\n            System.out.println(\"SQA_FIXTURE_FAILURE:\" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));\n            return;\n        }\n        System.out.println(\"SQA_TRACE:{\\\"target_invoked\\\":\" + Boolean.TRUE.equals(INVOKED.get()) + \"}\");\n        System.out.println(\"SQA_RESULT:\" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));\n    }\n}\n",
    "scripts/study/api854/fixture_policy.py": "\"\"\"Predeclared explicit fixture capability filter, never selected by buggy outcomes.\"\"\"\nPOLICY = 'beam-explicit-fixtures-v3-proposal'\nRECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')\n\n\ndef recipe_document(source_hashes, policy=POLICY):\n    from .common import ROOT, sha256\n    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}\n    if any(sha256(ROOT / name) != value for name, value in expected.items()):\n        raise ValueError('Explicit recipe source differs from protocol')\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10}:\n        raise ValueError('Unknown explicit fixture policy')\n    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,\n        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},\n        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}\n\n\ndef validate_recipe(recipe, source_hashes=None, policy=POLICY):\n    from .preparation import digest\n    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy\n            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)\n            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)\n            or any(not isinstance(recipe['sources'][name], str)\n                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):\n        raise ValueError('Explicit recipe source bytes/hash differ')\n    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):\n        raise ValueError('Explicit recipe source differs from frozen protocol')\n    return True\nPOLICY_V4 = 'beam-explicit-fixtures-v4-proposal'\nPOLICY_V5 = 'beam-explicit-fixtures-v5-proposal'\nPOLICY_V6 = 'aom-beam-fraction-field-v6-development'\nPOLICY_V10 = 'aom-beam-champ-joint-fixtures-v10-development'\nJOINT_SIGNATURES = {\n    ('org.apache.commons.codec.language.Metaphone', '', 'setMaxCodeLen', 'int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'inLongRange', '[C,int,int,boolean'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseInt', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseLong', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', 'java.lang.String,int,int'),\n    ('org.apache.commons.csv.ExtendedBufferedReader', 'java.io.Reader', 'read', '[C,int,int'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'isAllZeros', 'java.lang.String'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'validateArray', 'java.lang.Object'),\n}\n\n# Fixed-source recipes, declared before generation/evaluation. This development\n# version deliberately preserves unsupported declarations as explicit exclusions.\nPILOT_METHODS = {\n    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',\n        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',\n        'createNumber', 'createBigDecimal', 'createBigInteger'},\n    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',\n        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},\n    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',\n        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},\n    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},\n    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},\n    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},\n    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',\n        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',\n        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},\n    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',\n        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},\n    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},\n    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},\n    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},\n    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',\n        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',\n        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',\n        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},\n    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},\n    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',\n        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',\n        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},\n    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',\n        'getMethod', 'getInvocation', 'toString'},\n}\nPILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',\n    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',\n    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',\n    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',\n    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',\n    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',\n    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',\n    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',\n    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',\n    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',\n    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',\n    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}\n\n# Added capability recipes are fixed before any buggy evaluation. Mutators need\n# structural post-state; unsupported helpers/serialization hooks stay excluded.\nADDITIONAL_METHODS = {\n    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},\n    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},\n    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},\n    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',\n        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},\n    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},\n}\n\nSCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',\n           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',\n           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',\n           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',\n           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',\n           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}\nCLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',\n           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',\n           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',\n           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',\n           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',\n           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}\nJXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',\n          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',\n          'org.apache.commons.jxpath.ri.model.NodePointer'}\n# Methods requiring specialized AST parent/sibling/call metadata have no reviewed\n# recipe yet. This list is a structural restriction, not an outcome-based prune.\nCLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',\n    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',\n    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',\n    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',\n    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',\n    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',\n    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}\n\n\ndef select(targets, policy):\n    if policy is None:\n        return targets, []\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10}:\n        raise ValueError('Unknown explicit fixture policy')\n    if policy == POLICY_V10:\n        # Preserve v5+Math and add only exact peer-approved identities. JDOM is a repair.\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V6)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | JOINT_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V6:\n        # Keep every v5 decision, adding only the exact Champ-accepted signatures.\n        selected, excluded = select(targets, POLICY_V5)\n        accepted = lambda t: (t['class'] in {\n            'org.apache.commons.math3.fraction.BigFraction', 'org.apache.commons.math3.fraction.Fraction'}\n            and t['constructor_types'] == 'double' and t['method'] == 'getField' and t['parameter_types'] == '')\n        chosen = {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) for t in selected}\n        return ([t for t in targets if accepted(t) or tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) in chosen],\n                [row for row in excluded if not accepted(row['target'])])\n    selected, excluded = [], []\n    for target in targets:\n        name = target['class']\n        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {\n            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',\n            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()\n        extra = policy in {POLICY_V4, POLICY_V5} and name in ADDITIONAL_METHODS\n        pilot = policy == POLICY_V5 and name in PILOT_METHODS\n        if extra:\n            family = {'java.io.Reader'}\n        if pilot:\n            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}\n        reason = None\n        if not family:\n            reason = 'explicit_project_recipe_not_reviewed'\n        elif target['method'] in {'<init>', 'hashCode'}:\n            reason = 'constructor_or_identity_oracle_not_reviewed'\n        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:\n            reason = 'specialized_ast_recipe_not_reviewed'\n        elif extra and target['method'] not in ADDITIONAL_METHODS[name]:\n            reason = 'additional_method_preconditions_or_state_not_reviewed'\n        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:\n            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'\n        elif pilot and target['method'] not in PILOT_METHODS[name]:\n            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'\n        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:\n            reason = 'numeric_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':\n            reason = 'text_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':\n            reason = 'html_internal_normalise_recipe_not_reviewed'\n        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':\n            reason = 'archive_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:\n            reason = 'annotation_introspector_recipe_not_reviewed'\n        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':\n            reason = 'call_site_definition_finder_recipe_not_reviewed'\n        else:\n            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))\n            missing = required - SCALARS - family\n            if missing:\n                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))\n        if reason:\n            excluded.append({'target': target, 'reason': reason})\n        else:\n            selected.append(target)\n    return selected, excluded\n"
  }
}
```
