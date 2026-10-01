package com.google.gson;

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

import junit.framework.TestCase;

/**
 * Regression tests for {@link TypeInfoFactory}. Private methods are exercised via reflection.
 */
public class TypeInfoFactoryTest extends TestCase {

  // ---------- Fixtures ----------

  static class Holder<T> {
    T value;
    List<T> list;
    T[] array;
    Map<String, T> map;
    List<? extends T> wildT;
    String str;
    List<String> strList;
    List<String>[] strListArray;
    List<T>[] listArray;
    List<? extends Number> wildNumber;
    List<?> unbounded;
  }

  static class Sub extends Holder<Integer> {
  }

  static class Mid<U> extends Holder<U> {
  }

  static class Leaf extends Mid<Long> {
  }

  static class Pair<A, B> {
    A a;
    B b;
  }

  static class Other<X> {
    X x;
  }

  static class Holders {
    Holder<Integer> ofInteger;
    Holder<String> ofString;
  }

  // ---------- Helpers ----------

  private static Field field(Class<?> c, String name) throws Exception {
    return c.getDeclaredField(name);
  }

  private static Type genericTypeOf(Class<?> c, String name) throws Exception {
    return field(c, name).getGenericType();
  }

  private static Type holderOfInteger() throws Exception {
    return genericTypeOf(Holders.class, "ofInteger");
  }

  private static Type holderOfString() throws Exception {
    return genericTypeOf(Holders.class, "ofString");
  }

  private static Object call(String name, Class<?>[] sig, Object[] args) throws Throwable {
    Method m = TypeInfoFactory.class.getDeclaredMethod(name, sig);
    m.setAccessible(true);
    try {
      return m.invoke(null, args);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private static Type actualType(Type toEvaluate, Type parent, Class<?> raw) throws Throwable {
    return (Type) call("getActualType",
        new Class<?>[] {Type.class, Type.class, Class.class},
        new Object[] {toEvaluate, parent, raw});
  }

  private static Type[] realTypes(Type[] args, Type parent, Class<?> raw) throws Throwable {
    return (Type[]) call("extractRealTypes",
        new Class<?>[] {Type[].class, Type.class, Class.class},
        new Object[] {args, parent, raw});
  }

  private static int index(TypeVariable<?>[] vars, TypeVariable<?> var) throws Throwable {
    return ((Integer) call("getIndex",
        new Class<?>[] {TypeVariable[].class, TypeVariable.class},
        new Object[] {vars, var})).intValue();
  }

  // ---------- getIndex ----------

  public void testGetIndexReturnsFirstPosition() throws Throwable {
    TypeVariable<?>[] vars = Pair.class.getTypeParameters();
    assertEquals(0, index(vars, vars[0]));
  }

  public void testGetIndexReturnsSecondPosition() throws Throwable {
    TypeVariable<?>[] vars = Pair.class.getTypeParameters();
    assertEquals(1, index(vars, vars[1]));
  }

  public void testGetIndexThrowsWhenVariableNotDeclared() throws Throwable {
    TypeVariable<?>[] vars = Pair.class.getTypeParameters();
    TypeVariable<?> foreign = Other.class.getTypeParameters()[0];
    try {
      index(vars, foreign);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException expected) {
    }
  }

  public void testGetIndexThrowsOnEmptyArray() throws Throwable {
    TypeVariable<?>[] empty = new TypeVariable<?>[0];
    TypeVariable<?> var = Pair.class.getTypeParameters()[0];
    try {
      index(empty, var);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException expected) {
    }
  }

  // ---------- extractRealTypes ----------

  public void testExtractRealTypesNullArrayThrows() throws Throwable {
    try {
      realTypes(null, holderOfInteger(), Holder.class);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
    }
  }

  public void testExtractRealTypesEmptyArrayReturnsEmpty() throws Throwable {
    Type[] result = realTypes(new Type[0], holderOfInteger(), Holder.class);
    assertNotNull(result);
    assertEquals(0, result.length);
  }

  public void testExtractRealTypesResolvesVariablesAndKeepsClasses() throws Throwable {
    Type tVar = Holder.class.getTypeParameters()[0];
    Type[] input = new Type[] {String.class, tVar, Long.class};
    Type[] result = realTypes(input, holderOfInteger(), Holder.class);
    assertEquals(3, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);
    assertEquals(Long.class, result[2]);
  }

  public void testExtractRealTypesInvalidElementThrows() throws Throwable {
    Type unknown = new Type() { };
    try {
      realTypes(new Type[] {String.class, unknown}, holderOfInteger(), Holder.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
  }

  // ---------- getActualType ----------

  public void testGetActualTypeReturnsClassUnchanged() throws Throwable {
    assertSame(String.class, actualType(String.class, String.class, String.class));
  }

  public void testGetActualTypeParameterizedWithoutVariables() throws Throwable {
    Type listOfString = genericTypeOf(Holder.class, "strList");
    Type result = actualType(listOfString, holderOfInteger(), Holder.class);
    assertTrue(result instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) result;
    assertEquals(List.class, pt.getRawType());
    assertEquals(1, pt.getActualTypeArguments().length);
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
    assertNull(pt.getOwnerType());
  }

  public void testGetActualTypeParameterizedResolvesTypeVariable() throws Throwable {
    Type listOfT = genericTypeOf(Holder.class, "list");
    Type result = actualType(listOfT, holderOfInteger(), Holder.class);
    assertTrue(result instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) result;
    assertEquals(List.class, pt.getRawType());
    assertEquals(Integer.class, pt.getActualTypeArguments()[0]);
  }

  public void testGetActualTypeMapWithMixedArguments() throws Throwable {
    Type mapType = genericTypeOf(Holder.class, "map");
    Type result = actualType(mapType, holderOfString(), Holder.class);
    ParameterizedType pt = (ParameterizedType) result;
    assertEquals(Map.class, pt.getRawType());
    assertEquals(2, pt.getActualTypeArguments().length);
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
    assertEquals(String.class, pt.getActualTypeArguments()[1]);
  }

  public void testGetActualTypeTypeVariableWithParameterizedParent() throws Throwable {
    Type tVar = genericTypeOf(Holder.class, "value");
    assertTrue(tVar instanceof TypeVariable<?>);
    assertEquals(Integer.class, actualType(tVar, holderOfInteger(), Holder.class));
    assertEquals(String.class, actualType(tVar, holderOfString(), Holder.class));
  }

  public void testGetActualTypeTypeVariableResolvedThroughSubclass() throws Throwable {
    Type tVar = genericTypeOf(Holder.class, "value");
    assertEquals(Integer.class, actualType(tVar, Sub.class, Sub.class));
  }

  public void testGetActualTypeTypeVariableResolvedThroughDeeperHierarchy() throws Throwable {
    Type tVar = genericTypeOf(Holder.class, "value");
    assertEquals(Long.class, actualType(tVar, Leaf.class, Leaf.class));
  }

  public void testGetActualTypeUnresolvableTypeVariableThrows() throws Throwable {
    Type tVar = genericTypeOf(Holder.class, "value");
    try {
      actualType(tVar, Holder.class, Holder.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
    }
    try {
      actualType(tVar, null, Holder.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
    }
  }

  public void testGetActualTypeForeignTypeVariableWithParameterizedParentThrows()
      throws Throwable {
    Type foreign = genericTypeOf(Other.class, "x");
    try {
      actualType(foreign, holderOfInteger(), Holder.class);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException expected) {
    }
  }

  public void testGetActualTypeGenericArrayOfTypeVariableBecomesClassArray() throws Throwable {
    Type tArray = genericTypeOf(Holder.class, "array");
    assertTrue(tArray instanceof GenericArrayType);
    Type result = actualType(tArray, holderOfInteger(), Holder.class);
    assertTrue(result instanceof Class<?>);
    assertTrue(((Class<?>) result).isArray());
    assertEquals(Integer.class, ((Class<?>) result).getComponentType());
  }

  public void testGetActualTypeGenericArrayWithoutVariablesStaysGenericArray()
      throws Throwable {
    Type arrayType = genericTypeOf(Holder.class, "strListArray");
    assertTrue(arrayType instanceof GenericArrayType);
    Type result = actualType(arrayType, holderOfInteger(), Holder.class);
    assertTrue(result instanceof GenericArrayType);
    Type component = ((GenericArrayType) result).getGenericComponentType();
    assertTrue(component instanceof ParameterizedType);
    assertEquals(List.class, ((ParameterizedType) component).getRawType());
    assertEquals(String.class, ((ParameterizedType) component).getActualTypeArguments()[0]);
  }

  public void testGetActualTypeGenericArrayOfParameterizedTypeWithVariable() throws Throwable {
    Type arrayType = genericTypeOf(Holder.class, "listArray");
    assertTrue(arrayType instanceof GenericArrayType);
    Type result = actualType(arrayType, holderOfInteger(), Holder.class);
    assertTrue(result instanceof GenericArrayType);
    Type component = ((GenericArrayType) result).getGenericComponentType();
    assertTrue(component instanceof ParameterizedType);
    assertEquals(Integer.class, ((ParameterizedType) component).getActualTypeArguments()[0]);
  }

  public void testGetActualTypeWildcardUsesFirstUpperBound() throws Throwable {
    ParameterizedType numberList =
        (ParameterizedType) genericTypeOf(Holder.class, "wildNumber");
    Type wildcard = numberList.getActualTypeArguments()[0];
    assertTrue(wildcard instanceof WildcardType);
    assertEquals(Number.class, actualType(wildcard, holderOfInteger(), Holder.class));

    ParameterizedType unboundedList = (ParameterizedType) genericTypeOf(Holder.class, "unbounded");
    Type unbounded = unboundedList.getActualTypeArguments()[0];
    assertEquals(Object.class, actualType(unbounded, holderOfInteger(), Holder.class));
  }

  public void testGetActualTypeUnknownTypeImplementationThrows() throws Throwable {
    Type unknown = new Type() { };
    try {
      actualType(unknown, holderOfInteger(), Holder.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
  }

  // ---------- getTypeInfoForField ----------

  public void testGetTypeInfoForFieldNonGenericFieldWithParameterizedParent() throws Exception {
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field(Holder.class, "str"), holderOfInteger());
    assertNotNull(info);
  }

  public void testGetTypeInfoForFieldTypeVariableFieldWithParameterizedParent()
      throws Exception {
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field(Holder.class, "value"), holderOfString());
    assertNotNull(info);
  }

  public void testGetTypeInfoForFieldUnresolvableTypeVariableThrows() throws Exception {
    try {
      TypeInfoFactory.getTypeInfoForField(field(Holder.class, "value"), Holder.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
    }
  }

  // ---------- getTypeInfoForArray ----------

  public void testGetTypeInfoForArrayWithPrimitiveArrayClass() {
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(int[].class));
  }

  public void testGetTypeInfoForArrayWithObjectArrayClass() {
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(String[].class));
  }

  public void testGetTypeInfoForArrayWithGenericArrayType() throws Exception {
    Type genericArray = genericTypeOf(Holder.class, "strListArray");
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(genericArray));
  }

  public void testGetTypeInfoForArrayRejectsNonArrayTypes() throws Exception {
    try {
      TypeInfoFactory.getTypeInfoForArray(String.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
    try {
      TypeInfoFactory.getTypeInfoForArray(genericTypeOf(Holder.class, "strList"));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
    }
  }
}
