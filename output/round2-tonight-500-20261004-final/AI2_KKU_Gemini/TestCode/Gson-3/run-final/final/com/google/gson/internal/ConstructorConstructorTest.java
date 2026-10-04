package com.google.gson.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {

  public static class SampleClass {
    public boolean initialized = true;
  }

  @Test
  public void testGetWithDefaultConstructor() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    ObjectConstructor<SampleClass> creator = constructorConstructor.get(TypeToken.get(SampleClass.class));
    assertNotNull(creator);
    SampleClass instance = creator.construct();
    assertNotNull(instance);
    assertTrue(instance.initialized);
  }

  @Test
  public void testGetWithInstanceCreator() {
    Map<Type, InstanceCreator<?>> map = new HashMap<Type, InstanceCreator<?>>();
    final SampleClass customInstance = new SampleClass();
    customInstance.initialized = false;
    map.put(SampleClass.class, new InstanceCreator<SampleClass>() {
      @Override
      public SampleClass createInstance(Type type) {
        return customInstance;
      }
    });

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(map);
    ObjectConstructor<SampleClass> creator = constructorConstructor.get(TypeToken.get(SampleClass.class));
    assertNotNull(creator);
    SampleClass instance = creator.construct();
    assertNotNull(instance);
    assertTrue(!instance.initialized);
  }

  @Test
  public void testGetWithCollectionInterface() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    ObjectConstructor<Collection> creator = constructorConstructor.get(TypeToken.get(Collection.class));
    assertNotNull(creator);
    Collection<?> instance = creator.construct();
    assertNotNull(instance);
    assertTrue(instance instanceof ArrayList);
  }

  @Test
  public void testGetWithListInterface() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    ObjectConstructor<List> creator = constructorConstructor.get(TypeToken.get(List.class));
    assertNotNull(creator);
    List<?> instance = creator.construct();
    assertNotNull(instance);
  }

  @Test
  public void testGetWithMapInterface() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    ObjectConstructor<Map> creator = constructorConstructor.get(TypeToken.get(Map.class));
    assertNotNull(creator);
    Map<?, ?> instance = creator.construct();
    assertNotNull(instance);
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> map = new HashMap<Type, InstanceCreator<?>>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(map);
    String result = constructorConstructor.toString();
    assertNotNull(result);
  }
}
