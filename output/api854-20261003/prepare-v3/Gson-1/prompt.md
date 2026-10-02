Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Gson; fixed revision: 1f.
Modified target classes:
com.google.gson.TypeInfoFactory

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "extractRealTypes",
    "parameter_types": "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class"
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getActualType",
    "parameter_types": "java.lang.reflect.Type,java.lang.reflect.Type,java.lang.Class"
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getIndex",
    "parameter_types": "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable"
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getTypeInfoForArray",
    "parameter_types": "java.lang.reflect.Type"
  },
  {
    "class": "com.google.gson.TypeInfoFactory",
    "constructor_types": "",
    "method": "getTypeInfoForField",
    "parameter_types": "java.lang.reflect.Field,java.lang.reflect.Type"
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "com.google.gson.AnonymousAndLocalClassExclusionStrategy",
  "com.google.gson.Cache",
  "com.google.gson.CamelCaseSeparatorNamingPolicy",
  "com.google.gson.CircularReferenceException",
  "com.google.gson.CompositionFieldNamingPolicy",
  "com.google.gson.DefaultTypeAdapters",
  "com.google.gson.DelegatingJsonElementVisitor",
  "com.google.gson.DisjunctionExclusionStrategy",
  "com.google.gson.Escaper",
  "com.google.gson.ExclusionStrategy",
  "com.google.gson.ExposeAnnotationDeserializationExclusionStrategy",
  "com.google.gson.ExposeAnnotationSerializationExclusionStrategy",
  "com.google.gson.FieldAttributes",
  "com.google.gson.FieldNamingPolicy",
  "com.google.gson.FieldNamingStrategy",
  "com.google.gson.FieldNamingStrategy2",
  "com.google.gson.FieldNamingStrategy2Adapter",
  "com.google.gson.GenericArrayTypeImpl",
  "com.google.gson.Gson",
  "com.google.gson.GsonBuilder",
  "com.google.gson.InnerClassExclusionStrategy",
  "com.google.gson.InstanceCreator",
  "com.google.gson.JavaFieldNamingPolicy",
  "com.google.gson.JsonArray",
  "com.google.gson.JsonArrayDeserializationVisitor",
  "com.google.gson.JsonDeserializationContext",
  "com.google.gson.JsonDeserializationContextDefault",
  "com.google.gson.JsonDeserializationVisitor",
  "com.google.gson.JsonDeserializer",
  "com.google.gson.JsonDeserializerExceptionWrapper",
  "com.google.gson.JsonElement",
  "com.google.gson.JsonElementVisitor",
  "com.google.gson.JsonFieldNameValidator",
  "com.google.gson.JsonIOException",
  "com.google.gson.JsonNull",
  "com.google.gson.JsonObject",
  "com.google.gson.JsonObjectDeserializationVisitor",
  "com.google.gson.JsonParseException",
  "com.google.gson.JsonParser",
  "com.google.gson.JsonPrimitive",
  "com.google.gson.JsonSerializationContext",
  "com.google.gson.JsonSerializationContextDefault",
  "com.google.gson.JsonSerializationVisitor",
  "com.google.gson.JsonSerializer",
  "com.google.gson.JsonStreamParser",
  "com.google.gson.JsonSyntaxException",
  "com.google.gson.JsonTreeNavigator",
  "com.google.gson.LongSerializationPolicy",
  "com.google.gson.LowerCamelCaseSeparatorNamingPolicy",
  "com.google.gson.LowerCaseNamingPolicy",
  "com.google.gson.LruCache",
  "com.google.gson.MappedObjectConstructor",
  "com.google.gson.MemoryRefStack",
  "com.google.gson.ModifierBasedExclusionStrategy",
  "com.google.gson.ModifyFirstLetterNamingPolicy",
  "com.google.gson.NullExclusionStrategy",
  "com.google.gson.ObjectConstructor",
  "com.google.gson.ObjectNavigator",
  "com.google.gson.ObjectNavigatorFactory",
  "com.google.gson.ObjectTypePair",
  "com.google.gson.Pair",
  "com.google.gson.ParameterizedTypeHandlerMap",
  "com.google.gson.ParameterizedTypeImpl",
  "com.google.gson.Preconditions",
  "com.google.gson.Primitives",
  "com.google.gson.RecursiveFieldNamingPolicy",
  "com.google.gson.SerializedNameAnnotationInterceptingNamingPolicy",
  "com.google.gson.Streams",
  "com.google.gson.SyntheticFieldExclusionStrategy",
  "com.google.gson.TypeAdapter",
  "com.google.gson.TypeInfo",
  "com.google.gson.TypeInfoArray",
  "com.google.gson.TypeInfoCollection",
  "com.google.gson.TypeInfoFactory",
  "com.google.gson.TypeInfoMap",
  "com.google.gson.TypeUtils",
  "com.google.gson.UpperCamelCaseSeparatorNamingPolicy",
  "com.google.gson.UpperCaseNamingPolicy",
  "com.google.gson.VersionConstants",
  "com.google.gson.VersionExclusionStrategy",
  "com.google.gson.annotations.Expose",
  "com.google.gson.annotations.SerializedName",
  "com.google.gson.annotations.Since",
  "com.google.gson.annotations.Until",
  "com.google.gson.reflect.TypeToken",
  "com.google.gson.stream.JsonReader",
  "com.google.gson.stream.JsonScope",
  "com.google.gson.stream.JsonToken",
  "com.google.gson.stream.JsonWriter",
  "com.google.gson.stream.MalformedJsonException"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

## gson/src/main/java/com/google/gson/TypeInfoFactory.java

```
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
