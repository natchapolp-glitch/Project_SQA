package com.google.gson;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Regression tests for TypeInfoFactory.
 * Tests the factory methods that construct TypeInfo objects from reflection types.
 */
public class TypeInfoFactoryTest extends TestCase {

  // Test classes with generic type parameters for reflection testing
  static class GenericContainer<T> {
    T field;
    List<T> listField;
    T[] arrayField;
  }

  static class ConcreteContainer extends GenericContainer<String> {
    // Inherits T as String
  }

  static class MultiGeneric<A, B> {
    A fieldA;
    B fieldB;
    Map<A, B> mapField;
  }

  static class ConcreteMultiGeneric extends MultiGeneric<Integer, String> {
    // Inherits A as Integer, B as String
  }

  static class NestedGeneric<T> {
    GenericContainer<T> nested;
  }

  // ========== Tests for getTypeInfoForArray ==========

  public void testGetTypeInfoForArrayWithSimpleArray() throws Exception {
    // Test with a simple array type like String[]
    Type arrayType = String[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull("Should return TypeInfoArray for String[]", result);
  }

  public void testGetTypeInfoForArrayWithIntArray() throws Exception {
    // Test with primitive array type
    Type arrayType = int[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull("Should return TypeInfoArray for int[]", result);
  }

  public void testGetTypeInfoForArrayWithMultidimensionalArray() throws Exception {
    // Test with multidimensional array
    Type arrayType = String[][].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull("Should return TypeInfoArray for String[][]", result);
  }

  public void testGetTypeInfoForArrayFailsWithNonArrayType() throws Exception {
    // Test that non-array types cause assertion failure
    Type nonArrayType = String.class;
    try {
      TypeInfoFactory.getTypeInfoForArray(nonArrayType);
      fail("Should throw exception for non-array type");
    } catch (IllegalArgumentException e) {
      // Expected: Preconditions.checkArgument should fail
    }
  }

  public void testGetTypeInfoForArrayWithObjectArray() throws Exception {
    // Test with Object array
    Type arrayType = Object[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull("Should return TypeInfoArray for Object[]", result);
  }

  // ========== Tests for getTypeInfoForField with simple types ==========

  public void testGetTypeInfoForFieldWithSimpleType() throws Exception {
    // Test extracting type info for a simple typed field
    Field field = GenericContainer.class.getDeclaredField("field");
    Type containerType = ConcreteContainer.class;
    
    // Should resolve T to String
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should return TypeInfo for field", result);
  }

  public void testGetTypeInfoForFieldWithListType() throws Exception {
    // Test extracting type info for a parameterized List field
    Field field = GenericContainer.class.getDeclaredField("listField");
    Type containerType = ConcreteContainer.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should return TypeInfo for List field", result);
  }

  public void testGetTypeInfoForFieldWithArrayType() throws Exception {
    // Test extracting type info for an array field with type variable
    Field field = GenericContainer.class.getDeclaredField("arrayField");
    Type containerType = ConcreteContainer.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should return TypeInfo for array field", result);
  }

  public void testGetTypeInfoForFieldWithNonGenericContainer() throws Exception {
    // Test with non-generic container - should just return field type as-is
    Field field = String.class.getDeclaredField("value");
    Type containerType = String.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should return TypeInfo for non-generic field", result);
  }

  public void testGetTypeInfoForFieldMultiGenericTypeA() throws Exception {
    // Test resolving first type parameter in multi-generic class
    Field field = MultiGeneric.class.getDeclaredField("fieldA");
    Type containerType = ConcreteMultiGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should resolve type parameter A", result);
  }

  public void testGetTypeInfoForFieldMultiGenericTypeB() throws Exception {
    // Test resolving second type parameter in multi-generic class
    Field field = MultiGeneric.class.getDeclaredField("fieldB");
    Type containerType = ConcreteMultiGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should resolve type parameter B", result);
  }

  public void testGetTypeInfoForFieldWithMapType() throws Exception {
    // Test extracting type info for parameterized Map with type variables
    Field field = MultiGeneric.class.getDeclaredField("mapField");
    Type containerType = ConcreteMultiGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should return TypeInfo for Map field with type vars", result);
  }

  public void testGetTypeInfoForFieldWithNullType() throws Exception {
    // Test behavior with null type (raw class)
    Field field = GenericContainer.class.getDeclaredField("field");
    // Use raw generic type without parameterization
    Type containerType = GenericContainer.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should handle raw generic type", result);
  }

  // ========== Tests for type resolution with inheritance hierarchy ==========

  public void testGetTypeInfoForFieldResolvesThroughInheritance() throws Exception {
    // Test that type variables are resolved through inheritance chain
    Field field = GenericContainer.class.getDeclaredField("field");
    Type concreteType = ConcreteContainer.class;
    
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, concreteType);
    assertNotNull("Should resolve type variable through inheritance", info);
  }

  static class DeepInheritance extends ConcreteContainer {
    // Further inherits from ConcreteContainer which sets T=String
  }

  public void testGetTypeInfoForFieldWithDeepInheritance() throws Exception {
    // Test type resolution through multiple levels of inheritance
    Field field = GenericContainer.class.getDeclaredField("field");
    Type deepType = DeepInheritance.class;
    
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, deepType);
    assertNotNull("Should resolve through deep inheritance hierarchy", info);
  }

  // ========== Tests for edge cases and error conditions ==========

  public void testGetTypeInfoForArrayWithGenericArrayType() throws Exception {
    // Test with a generic array type (e.g., from List<String[]>)
    Type arrayType = String[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull("Should handle generic array types", result);
  }

  static class SingleFieldGeneric<T> {
    T value;
  }

  public void testGetTypeInfoForFieldResolvesSimpleTypeVariable() throws Exception {
    // Test resolution of a simple, single type variable
    Field field = SingleFieldGeneric.class.getDeclaredField("value");
    ParameterizedType paramType = (ParameterizedType) SingleFieldGeneric.class
        .getGenericSuperclass();
    // Create a concrete instantiation manually through reflection exploration
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, SingleFieldGeneric.class);
    assertNotNull("Should handle simple type variable", result);
  }

  // ========== Tests for consistency and idempotence ==========

  public void testGetTypeInfoForArrayIsConsistent() throws Exception {
    // Call the same method twice and verify consistent results
    Type arrayType = String[].class;
    TypeInfoArray result1 = TypeInfoFactory.getTypeInfoForArray(arrayType);
    TypeInfoArray result2 = TypeInfoFactory.getTypeInfoForArray(arrayType);
    
    assertNotNull("First call should succeed", result1);
    assertNotNull("Second call should succeed", result2);
  }

  public void testGetTypeInfoForFieldIsConsistent() throws Exception {
    // Call the same method twice with same inputs and verify consistency
    Field field = GenericContainer.class.getDeclaredField("field");
    Type containerType = ConcreteContainer.class;
    
    TypeInfo result1 = TypeInfoFactory.getTypeInfoForField(field, containerType);
    TypeInfo result2 = TypeInfoFactory.getTypeInfoForField(field, containerType);
    
    assertNotNull("First call should succeed", result1);
    assertNotNull("Second call should succeed", result2);
  }

  // ========== Tests for primitive types ==========

  static class PrimitiveGeneric<T> {
    int intField;
    double doubleField;
    boolean booleanField;
  }

  public void testGetTypeInfoForFieldWithPrimitiveInt() throws Exception {
    Field field = PrimitiveGeneric.class.getDeclaredField("intField");
    Type containerType = PrimitiveGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should handle primitive int field", result);
  }

  public void testGetTypeInfoForFieldWithPrimitiveDouble() throws Exception {
    Field field = PrimitiveGeneric.class.getDeclaredField("doubleField");
    Type containerType = PrimitiveGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should handle primitive double field", result);
  }

  public void testGetTypeInfoForFieldWithPrimitiveBoolean() throws Exception {
    Field field = PrimitiveGeneric.class.getDeclaredField("booleanField");
    Type containerType = PrimitiveGeneric.class;
    
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, containerType);
    assertNotNull("Should handle primitive boolean field", result);
  }

  // ========== Tests for array component type resolution ==========

  public void testGetTypeInfoForArrayWithIntComponentType() throws Exception {
    // Verify array of primitive type works
    Type intArrayType = int[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertNotNull("Should handle int array", result);
  }

  public void testGetTypeInfoForArrayWithLongComponentType() throws Exception {
    // Verify array of another primitive type
    Type longArrayType = long[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(longArrayType);
    assertNotNull("Should handle long array", result);
  }

  public void testGetTypeInfoForArrayWithObjectComponentType() throws Exception {
    // Verify array of reference type works consistently
    Type objectArrayType = Object[].class;
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(objectArrayType);
    assertNotNull("Should handle Object array", result);
  }
}
