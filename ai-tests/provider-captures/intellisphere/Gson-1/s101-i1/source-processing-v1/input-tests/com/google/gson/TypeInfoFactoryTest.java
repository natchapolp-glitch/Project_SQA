package com.google.gson;

import junit.framework.TestCase;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class TypeInfoFactoryTest extends TestCase {

  @SuppressWarnings("unused")
  private static class RawTypesClass {
    private String stringField;
    private int intField;
    private String[] stringArrayField;
    private List<String> stringListField;
    private List<? extends String> wildcardListField;
  }

  @SuppressWarnings("unused")
  private static class GenericParent<T> {
    private T genericField;
    private T[] genericArrayField;
    private List<T> genericListField;
  }

  private static class ConcreteChild extends GenericParent<Integer> {
  }

  @SuppressWarnings("unused")
  private static class KeyValueParent<K, V> {
    private K keyField;
    private V valueField;
  }

  private static class ConcreteKeyValueChild extends KeyValueParent<String, Double> {
  }

  private static class MiddleChild<T> extends GenericParent<T> {
  }

  private static class GrandChild extends MiddleChild<Boolean> {
  }

  public void testGetTypeInfoForArrayValidArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(typeInfo);
    assertEquals(String[].class, typeInfo.getRawClass());
  }

  public void testGetTypeInfoForArrayPrimitiveArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(typeInfo);
    assertEquals(int[].class, typeInfo.getRawClass());
  }

  public void testGetTypeInfoForArrayInvalidNonArray() {
    try {
      TypeInfoFactory.getTypeInfoForArray(String.class);
      fail("Expected IllegalArgumentException for non-array type");
    } catch (IllegalArgumentException expected) {
      // Expected exception
    }
  }

  public void testGetTypeInfoForFieldSimpleClass() throws Exception {
    Field field = RawTypesClass.class.getDeclaredField("stringField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, RawTypesClass.class);
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldPrimitiveClass() throws Exception {
    Field field = RawTypesClass.class.getDeclaredField("intField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, RawTypesClass.class);
    assertNotNull(typeInfo);
    assertEquals(int.class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldObjectArray() throws Exception {
    Field field = RawTypesClass.class.getDeclaredField("stringArrayField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, RawTypesClass.class);
    assertNotNull(typeInfo);
    assertEquals(String[].class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldParameterizedType() throws Exception {
    Field field = RawTypesClass.class.getDeclaredField("stringListField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, RawTypesClass.class);
    assertNotNull(typeInfo);
    assertTrue(typeInfo.getActualType() instanceof ParameterizedType);
    ParameterizedType paramType = (ParameterizedType) typeInfo.getActualType();
    assertEquals(List.class, paramType.getRawType());
    assertEquals(1, paramType.getActualTypeArguments().length);
    assertEquals(String.class, paramType.getActualTypeArguments()[0](undefined));
  }

  public void testGetTypeInfoForFieldWildcardType() throws Exception {
    Field field = RawTypesClass.class.getDeclaredField("wildcardListField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, RawTypesClass.class);
    assertNotNull(typeInfo);
    assertTrue(typeInfo.getActualType() instanceof ParameterizedType);
    ParameterizedType paramType = (ParameterizedType) typeInfo.getActualType();
    assertEquals(List.class, paramType.getRawType());
    assertEquals(String.class, paramType.getActualTypeArguments()[0](undefined));
  }

  public void testGetTypeInfoForFieldInheritedGenericVariable() throws Exception {
    Field field = GenericParent.class.getDeclaredField("genericField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ConcreteChild.class);
    assertNotNull(typeInfo);
    assertEquals(Integer.class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldInheritedGenericArray() throws Exception {
    Field field = GenericParent.class.getDeclaredField("genericArrayField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ConcreteChild.class);
    assertNotNull(typeInfo);
    assertEquals(Integer[].class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldInheritedGenericList() throws Exception {
    Field field = GenericParent.class.getDeclaredField("genericListField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ConcreteChild.class);
    assertNotNull(typeInfo);
    assertTrue(typeInfo.getActualType() instanceof ParameterizedType);
    ParameterizedType paramType = (ParameterizedType) typeInfo.getActualType();
    assertEquals(List.class, paramType.getRawType());
    assertEquals(Integer.class, paramType.getActualTypeArguments()[0](undefined));
  }

  public void testGetTypeInfoForFieldMultipleGenericVariables() throws Exception {
    Field keyField = KeyValueParent.class.getDeclaredField("keyField");
    Field valueField = KeyValueParent.class.getDeclaredField("valueField");

    TypeInfo keyTypeInfo = TypeInfoFactory.getTypeInfoForField(keyField, ConcreteKeyValueChild.class);
    TypeInfo valueTypeInfo = TypeInfoFactory.getTypeInfoForField(valueField, ConcreteKeyValueChild.class);

    assertEquals(String.class, keyTypeInfo.getActualType());
    assertEquals(Double.class, valueTypeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldMultilevelInheritance() throws Exception {
    Field field = GenericParent.class.getDeclaredField("genericField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, GrandChild.class);
    assertNotNull(typeInfo);
    assertEquals(Boolean.class, typeInfo.getActualType());
  }

  public void testGetTypeInfoForFieldUnresolvedTypeVariableThrowsException() throws Exception {
    Field field = GenericParent.class.getDeclaredField("genericField");
    try {
      TypeInfoFactory.getTypeInfoForField(field, GenericParent.class);
      fail("Expected UnsupportedOperationException for unresolved type variable");
    } catch (UnsupportedOperationException expected) {
      // Expected exception
    }
  }
}
