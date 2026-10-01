```java
package com.google.gson;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 * Deterministic regression tests for {@link TypeInfoFactory}.
 *
 * <p>References:
 * <ul>
 *   <li>Defects4J Gson revision 1f</li>
 *   <li>Google Gson Project: https://github.com/google/gson</li>
 * </ul>
 */
public class TypeInfoFactoryTest extends TestCase {

  // Sample classes used as reflection fixtures

  private static class SimpleClass {
    int primitiveInt;
    String stringField;
    String[] stringArray;
    List<String> stringList;
  }

  private static class GenericClass<T, U> {
    T fieldT;
    U fieldU;
    List<T> listT;
    T[] arrayT;
    Map<T, List<U>> mapTListU;
    List<String>[] arrayGenericList;
  }

  private static class ConcreteSubClass extends GenericClass<String, Integer> {
    Double subFieldDouble;
  }

  private static class MidGenericClass<V> extends GenericClass<V, Boolean> {}

  private static class ConcreteMidSubClass extends MidGenericClass<Float> {}

  private static class MultiLevelLevel1<A> {
    A level1Field;
  }

  private static class MultiLevelLevel2<B> extends MultiLevelLevel1<B> {}

  private static class MultiLevelLevel3<C> extends MultiLevelLevel2