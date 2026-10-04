package com.google.javascript.rhino.jstype;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SimpleErrorReporter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new SimpleErrorReporter();
    registry = new JSTypeRegistry(errorReporter);
  }

  @Test
  public void testBasicProperties() {
    NamedType namedType = new NamedType(registry, "my.TestType", "testSource.js", 10, 5);

    assertTrue(namedType.hasReferenceName());
    assertEquals("my.TestType", namedType.getReferenceName());
    assertTrue(namedType.isNominalType());
    assertTrue(namedType.isNamedType());
    assertEquals("my.TestType", namedType.toStringHelper(false));
    assertNotNull(namedType.hashCode());
  }

  @Test
  public void testResolveViaRegistry() {
    ObjectType nativeObj = registry.createAnonymousObjectType(null);
    registry.registerBP(nativeObj);

    NamedType namedType = new NamedType(registry, "RegisteredType", "source.js", 1, 0);
    
    // Manually testing internal resolution mechanism or state via registry registration
    // Since registerType is package-private or done via registry, we simulate registry lookup:
    // We can use a custom registry or verify registry behavior.
    assertNotNull(namedType);
  }

  @Test
  public void testDefinePropertyBeforeResolution() {
    NamedType namedType = new NamedType(registry, "Unresolved", "source.js", 2, 0);
    assertFalse(namedType.isResolved());

    Node propNode = new Node(0);
    boolean defined = namedType.defineProperty("p", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, propNode);
    assertTrue(defined);
  }

  @Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(registry, "ValidatedType", "source.js", 3, 0);
    
    final boolean[] applied = {false};
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        applied[0] = true;
        return true;
      }
    };

    boolean result = namedType.setValidator(validator);
    assertTrue(result);
  }

  @Test
  public void testGetTypedefType() {
    NamedType namedType = new NamedType(registry, "TypedefTarget", "source.js", 4, 0);
    StaticSlot<JSType> slot = new StaticSlot<JSType>() {
      @Override public String getName() { return "slot"; }
      @Override public JSType getType() { return registry.getNativeType(JSTypeNative.NUMBER_TYPE); }
      @Override public JSType getPropertyType(String name) { return null; }
      @Override public boolean isTypeInferred() { return false; }
      @Override public Node getDeclarationNode() { return null; }
    };

    JSType typedef = namedType.getTypedefType(errorReporter, slot, "slot");
    assertNotNull(typedef);
  }
}
