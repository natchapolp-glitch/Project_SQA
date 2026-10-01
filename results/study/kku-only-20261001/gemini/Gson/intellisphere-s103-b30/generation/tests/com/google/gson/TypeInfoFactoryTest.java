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
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link TypeInfoFactory}.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
public class TypeInfoFactoryTest extends TestCase {

  // Test helper classes with various generic structures
  private static class SimpleClass {
    public int primitiveField;
    public String stringField;
    public String[] stringArrayField;
  }

  private static class GenericHolder<T> {
    public T genericField;
    public T[] genericArrayField;
    public List<T> genericListField;
  }

  private static class TwoParamHolder<K, V> {
    public K key;
    public V value;
    public Map<K, V> map;
  }

  private static class SubClassFixed extends GenericHolder<String> {
  }

  private static class SubClassGeneric<E> extends GenericHolder<E> {
  }

  private static class WildcardHolder {
    public List<? extends Number> upperBoundWildcard;
  }

  private static class MultiLevelSubClass extends SubClassGeneric<Long> {
  }

  // --- Tests for getTypeInfoForArray ---

  public void testGetTypeInfoForArrayValidClassArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(info);
    assertEquals(String[].class, info.getActualType());
  }

  public void testGetTypeInfoForArrayValidPrimitiveArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(info);
    assertEquals(int[].class, info.getActualType());
  }

  public void testGetTypeInfoForArrayNonArrayThrowsIllegalArgumentException() {
    try {
      TypeInfoFactory.getTypeInfoForArray(String.class);
      fail("Expected IllegalArgumentException for non-array type");
    } catch (IllegalArgumentException expected) {
      // expected
    }
  }

  // --- Tests for getTypeInfoForField with simple raw types ---

  public void testGetTypeInfoForFieldPrimitive() throws Exception {
    Field f = SimpleClass.class.getField("primitiveField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, SimpleClass.class);
    assertNotNull(info);
    assertEquals(int.class, info.getActualType());
  }

  public void testGetTypeInfoForFieldString() throws Exception {
    Field f = SimpleClass.class.getField("stringField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, SimpleClass.class);
    assertNotNull(info);
    assertEquals(String.class, info.getActualType());
  }

  public void testGetTypeInfoForFieldArray() throws Exception {
    Field f = SimpleClass.class.getField("stringArrayField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, SimpleClass.class);
    assertNotNull(info);
    assertEquals(String[].class, info.getActualType());
  }

  // --- Tests for getTypeInfoForField with ParameterizedType defining type ---

  public void testGetTypeInfoForFieldGenericVariableResolved() throws Exception {
    Field f = GenericHolder.class.getField("genericField");
    Type parentType = new TypeToken<GenericHolder<Integer>>() {}.getType();

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, parentType);
    assertNotNull(info);
    assertEquals(Integer.class, info.getActualType());
  }

  public void testGetTypeInfoForFieldGenericArrayResolved() throws Exception {
    Field f = GenericHolder.class.getField("genericArrayField");
    Type parentType = new TypeToken<GenericHolder<String>>() {}.getType();

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, parentType);
    assertNotNull(info);
    assertEquals(String[].class, info.getActualType());
  }

  public void testGetTypeInfoForFieldGenericListResolved() throws Exception {
    Field f = GenericHolder.class.getField("genericListField");
    Type parentType = new TypeToken<GenericHolder<Double>>() {}.getType();

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, parentType);
    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(List.class, pt.getRawType());
    assertEquals(Double.class, pt.getActualTypeArguments()[0]);
  }

  public void testGetTypeInfoForFieldTwoParamsResolved() throws Exception {
    Field keyField = TwoParamHolder.class.getField("key");
    Field valField = TwoParamHolder.class.getField("value");
    Field mapField = TwoParamHolder.class.getField("map");

    Type parentType = new TypeToken<TwoParamHolder<String, Integer>>() {}.getType();

    assertEquals(String.class, TypeInfoFactory.getTypeInfoForField(keyField, parentType).getActualType());
    assertEquals(Integer.class, TypeInfoFactory.getTypeInfoForField(valField, parentType).getActualType());

    Type mapType = TypeInfoFactory.getTypeInfoForField(mapField, parentType).getActualType();
    assertTrue(mapType instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) mapType;
    assertEquals(Map.class, pt.getRawType());
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
    assertEquals(Integer.class, pt.getActualTypeArguments()[1]);
  }

  // --- Tests for getTypeInfoForField resolving across inheritance hierarchy ---

  public void testGetTypeInfoForFieldInheritedFixedType() throws Exception {
    Field f = GenericHolder.class.getField("genericField");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, SubClassFixed.class);
    assertNotNull(info);
    assertEquals(String.class, info.getActualType());
  }

  

  public void testGetTypeInfoForFieldMultiLevelInheritance() throws Exception {
    Field f = GenericHolder.class.getField("genericField");
    Type parentType = MultiLevelSubClass.class;

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, parentType);
    assertNotNull(info);
    assertEquals(Long.class, info.getActualType());
  }

  // --- Tests for WildcardType in getTypeInfoForField ---

  public void testGetTypeInfoForFieldWildcardUpperBound() throws Exception {
    Field f = WildcardHolder.class.getField("upperBoundWildcard");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, WildcardHolder.class);
    assertNotNull(info);
    assertTrue(info.getActualType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) info.getActualType();
    assertEquals(Number.class, pt.getActualTypeArguments()[0]);
  }

  // --- Tests for error cases and unsupported operations ---

  public void testGetTypeInfoForFieldRawGenericThrowsUnsupportedOperationException() throws Exception {
    Field f = GenericHolder.class.getField("genericField");
    try {
      TypeInfoFactory.getTypeInfoForField(f, GenericHolder.class);
      fail("Expected UnsupportedOperationException when parentType is raw class lacking type arguments");
    } catch (UnsupportedOperationException expected) {
      // Expected: "Expecting parameterized type, got ..."
    }
  }

  // --- Reflection tests for private method getIndex ---

  public void testGetIndexFound() throws Exception {
    Method getIndexMethod = TypeInfoFactory.class.getDeclaredMethod(
        "getIndex", TypeVariable[].class, TypeVariable.class);
    getIndexMethod.setAccessible(true);

    TypeVariable<?>[] typeVariables = TwoParamHolder.class.getTypeParameters();
    assertEquals(2, typeVariables.length);

    Integer index0 = (Integer) getIndexMethod.invoke(null, typeVariables, typeVariables[0]);
    assertEquals(0, index0.intValue());

    Integer index1 = (Integer) getIndexMethod.invoke(null, typeVariables, typeVariables[1]);
    assertEquals(1, index1.intValue());
  }

  public void testGetIndexNotFoundThrowsIllegalStateException() throws Exception {
    Method getIndexMethod = TypeInfoFactory.class.getDeclaredMethod(
        "getIndex", TypeVariable[].class, TypeVariable.class);
    getIndexMethod.setAccessible(true);

    TypeVariable<?>[] holder1Params = GenericHolder.class.getTypeParameters();
    TypeVariable<?>[] holder2Params = TwoParamHolder.class.getTypeParameters();

    try {
      getIndexMethod.invoke(null, holder1Params, holder2Params[0]);
      fail("Expected IllegalStateException when TypeVariable is not found");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalStateException);
    }
  }

  // --- Reflection tests for private method extractRealTypes ---

  public void testExtractRealTypes() throws Exception {
    Method extractRealTypesMethod = TypeInfoFactory.class.getDeclaredMethod(
        "extractRealTypes", Type[].class, Type.class, Class.class);
    extractRealTypesMethod.setAccessible(true);

    Type parentType = new TypeToken<GenericHolder<String>>() {}.getType();
    Type[] typeArgs = new Type[] { GenericHolder.class.getTypeParameters()[0] };

    Type[] extracted = (Type[]) extractRealTypesMethod.invoke(
        null, typeArgs, parentType, GenericHolder.class);
    assertNotNull(extracted);
    assertEquals(1, extracted.length);
    assertEquals(String.class, extracted[0]);
  }

  // --- Reflection tests for private method getActualType ---

  public void testGetActualTypeForClass() throws Exception {
    Method getActualTypeMethod = TypeInfoFactory.class.getDeclaredMethod(
        "getActualType", Type.class, Type.class, Class.class);
    getActualTypeMethod.setAccessible(true);

    Type result = (Type) getActualTypeMethod.invoke(null, String.class, String.class, String.class);
    assertEquals(String.class, result);
  }

  public void testGetActualTypeForGenericArrayOfClass() throws Exception {
    Method getActualTypeMethod = TypeInfoFactory.class.getDeclaredMethod(
        "getActualType", Type.class, Type.class, Class.class);
    getActualTypeMethod.setAccessible(true);

    Field f = GenericHolder.class.getField("genericArrayField");
    GenericArrayType arrayType = (GenericArrayType) f.getGenericType();
    Type parentType = new TypeToken<GenericHolder<Integer>>() {}.getType();

    Type result = (Type) getActualTypeMethod.invoke(
        null, arrayType, parentType, GenericHolder.class);
    assertEquals(Integer[].class, result);
  }
}
