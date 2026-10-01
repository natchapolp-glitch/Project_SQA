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

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Deterministic unit tests for {@link TypeInfoFactory}.
 */
public class TypeInfoFactoryTest extends TestCase {

  private static class SimpleClass {
    int intField;
    String stringField;
    List<String> listField;
    Object objectField;
  }

  private static class GenericHolder<T> {
    T tField;
    T[] arrayField;
    T[][] matrixField;
    List<T> genericListField;
  }

  private static class MultiGenericHolder<K, V> {
    Map<K, V> mapField;
  }

  private static class DirectSubClass extends GenericHolder<Double> {
  }

  private static class GenericSubClass<E> extends GenericHolder<E> {
  }

  private static class HierarchyLevel1<T> {
    T data;
  }

  private static class HierarchyLevel2<T> extends HierarchyLevel1<T> {
  }

  private static class HierarchyLevel3 extends HierarchyLevel2<Boolean> {
  }

  private static class WildcardHolder {
    List<? extends Number> wildcardField;
  }

  public void testGetTypeInfoForArray_primitiveArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(info);
    assertEquals(int[].class, info.getActualType());
    assertEquals(int[].class, info.getRawClass());
  }

  public void testGetTypeInfoForArray_objectArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(info);
    assertEquals(String[].class, info.getActualType());
    assertEquals(String[].class, info.getRawClass());
  }

  public void testGetTypeInfoForArray_multidimensionalArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(String[][].class);
    assertNotNull(info);
    assertEquals(String[][].class, info.getActualType());
    assertEquals(String[][].class, info.getRawClass());
  }

  public void testGetTypeInfoForArray_nonArrayThrowsIllegalArgumentException() {
    try {
      TypeInfoFactory.getTypeInfoForArray(String.class);
      fail("Expected IllegalArgumentException for non-array type");
    } catch (IllegalArgumentException expected) {
      // Expected
    }
  }

  public void testGetTypeInfoForField_primitiveType() throws Exception {
    Field field = SimpleClass.class.getDeclaredField("intField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, SimpleClass.class);

    assertNotNull(info);
    assertEquals(int.class, info.getActualType());
    assertEquals(int.class, info.getRawClass());
  }

  public void testGetTypeInfoForField_objectType() throws Exception {
    Field field = SimpleClass.class.getDeclaredField("objectField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, SimpleClass.class);

    assertNotNull(info);
    assertEquals(Object.class, info.getActualType());
    assertEquals(Object.class, info.getRawClass());
  }

  public void testGetTypeInfoForField_concreteParameterizedType() throws Exception {
    Field field = SimpleClass.class.getDeclaredField("listField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, SimpleClass.class);

    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(List.class, pt.getRawType());
    assertEquals(1, pt.getActualTypeArguments().length);
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
    assertEquals(List.class, info.getRawClass());
  }

  public void testGetTypeInfoForField_typeVariableResolvedViaParameterizedType() throws Exception {
    Field field = GenericHolder.class.getDeclaredField("tField");
    Type parentType = new TypeToken<GenericHolder<Integer>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertNotNull(info);
    assertEquals(Integer.class, info.getActualType());
    assertEquals(Integer.class, info.getRawClass());
  }

  public void testGetTypeInfoForField_typeVariableInNestedGenericResolved() throws Exception {
    Field field = GenericHolder.class.getDeclaredField("genericListField");
    Type parentType = new TypeToken<GenericHolder<String>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(List.class, pt.getRawType());
    assertEquals(1, pt.getActualTypeArguments().length);
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
  }

  public void testGetTypeInfoForField_genericArrayResolvedToClassArray() throws Exception {
    Field field = GenericHolder.class.getDeclaredField("arrayField");
    Type parentType = new TypeToken<GenericHolder<String>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertNotNull(info);
    assertEquals(String[].class, info.getActualType());
    assertEquals(String[].class, info.getRawClass());
  }

  public void testGetTypeInfoForField_multidimensionalGenericArrayResolved() throws Exception {
    Field field = GenericHolder.class.getDeclaredField("matrixField");
    Type parentType = new TypeToken<GenericHolder<Long>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertNotNull(info);
    assertEquals(Long[][].class, info.getActualType());
    assertEquals(Long[][].class, info.getRawClass());
  }

  public void testGetTypeInfoForField_multipleTypeVariablesResolved() throws Exception {
    Field field = MultiGenericHolder.class.getDeclaredField("mapField");
    Type parentType = new TypeToken<MultiGenericHolder<String, Integer>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(Map.class, pt.getRawType());
    assertEquals(2, pt.getActualTypeArguments().length);
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
    assertEquals(Integer.class, pt.getActualTypeArguments()[1]);
  }

  public void testGetTypeInfoForField_typeVariableResolvedViaClassHierarchy() throws Exception {
    Field field = GenericHolder.class.getDeclaredField("tField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, DirectSubClass.class);

    assertNotNull(info);
    assertEquals(Double.class, info.getActualType());
    assertEquals(Double.class, info.getRawClass());
  }

  

  public void testGetTypeInfoForField_deepHierarchyResolution() throws Exception {
    Field field = HierarchyLevel1.class.getDeclaredField("data");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, HierarchyLevel3.class);

    assertNotNull(info);
    assertEquals(Boolean.class, info.getActualType());
    assertEquals(Boolean.class, info.getRawClass());
  }

  public void testGetTypeInfoForField_wildcardTypeUpperBoundsExtracted() throws Exception {
    Field field = WildcardHolder.class.getDeclaredField("wildcardField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, WildcardHolder.class);

    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(List.class, pt.getRawType());
    assertEquals(1, pt.getActualTypeArguments().length);
    assertEquals(Number.class, pt.getActualTypeArguments()[0]);
  }

  public void testGetTypeInfoForField_unresolvedRawClassThrowsUnsupportedOperationException()
      throws Exception {
    Field field = GenericHolder.class.getDeclaredField("tField");
    try {
      TypeInfoFactory.getTypeInfoForField(field, GenericHolder.class);
      fail("Expected UnsupportedOperationException for raw type lacking type arguments");
    } catch (UnsupportedOperationException expected) {
      // Expected: Cannot resolve type variable on a raw type definition without TypeToken
    }
  }
}
