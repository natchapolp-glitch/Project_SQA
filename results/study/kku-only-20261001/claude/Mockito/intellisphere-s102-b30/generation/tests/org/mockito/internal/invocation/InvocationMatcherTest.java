package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private Invocation anotherInvocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() throws Exception {
        // Create mock invocations for testing
        invocation = mock(Invocation.class);
        anotherInvocation = mock(Invocation.class);
    }

    // ============ Constructor Tests ============

    @Test
    public void testConstructorWithInvocationOnly() throws Exception {
        when(invocation.getArguments()).thenReturn(new Object[]{1, "test"});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertNotNull(matcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() throws Exception {
        List<Matcher> matchers = Collections.emptyList();
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() throws Exception {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{1});

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    // ============ Getter Tests ============

    

    @Test
    public void testGetInvocation() throws Exception {
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertSame(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() throws Exception {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.equalTo(1));
        when(invocation.getArguments()).thenReturn(new Object[]{1});

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testGetLocation() throws Exception {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(location, matcher.getLocation());
    }

    // ============ toString Tests ============

    

    

    // ============ hasSameMethod Tests ============

    @Test
    public void testHasSameMethodTrue() throws Exception {
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSameMethod(anotherInvocation));
    }

    @Test
    public void testHasSameMethodFalseByName() throws Exception {
        Method method1 = Object.class.getMethod("toString");
        Method method2 = Object.class.getMethod("hashCode");
        when(invocation.getMethod()).thenReturn(method1);
        when(anotherInvocation.getMethod()).thenReturn(method2);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSameMethod(anotherInvocation));
    }

    @Test
    public void testHasSameMethodFalseByParameterCount() throws Exception {
        Method method1 = String.class.getMethod("substring", int.class);
        Method method2 = String.class.getMethod("substring", int.class, int.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(anotherInvocation.getMethod()).thenReturn(method2);
        when(invocation.getArguments()).thenReturn(new Object[]{1});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSameMethod(anotherInvocation));
    }

    

    // ============ hasSimilarMethod Tests ============

    @Test
    public void testHasSimilarMethodTrue() throws Exception {
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn("mock");
        when(anotherInvocation.getMock()).thenReturn("mock");
        when(anotherInvocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.hasSimilarMethod(anotherInvocation));
    }

    @Test
    public void testHasSimilarMethodFalseWhenVerified() throws Exception {
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn("mock");
        when(anotherInvocation.getMock()).thenReturn("mock");
        when(anotherInvocation.isVerified()).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(anotherInvocation));
    }

    @Test
    public void testHasSimilarMethodFalseWhenDifferentMock() throws Exception {
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMethod()).thenReturn(method);
        Object mock1 = new Object();
        Object mock2 = new Object();
        when(invocation.getMock()).thenReturn(mock1);
        when(anotherInvocation.getMock()).thenReturn(mock2);
        when(anotherInvocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(anotherInvocation));
    }

    @Test
    public void testHasSimilarMethodFalseWhenDifferentMethodName() throws Exception {
        Method method1 = Object.class.getMethod("toString");
        Method method2 = Object.class.getMethod("hashCode");
        when(invocation.getMethod()).thenReturn(method1);
        when(anotherInvocation.getMethod()).thenReturn(method2);
        when(invocation.getMock()).thenReturn("mock");
        when(anotherInvocation.getMock()).thenReturn("mock");
        when(anotherInvocation.isVerified()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.hasSimilarMethod(anotherInvocation));
    }

    // ============ matches Tests ============

    @Test
    public void testMatchesTrueWithSameMockAndMethod() throws Exception {
        Object mock = new Object();
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        
        when(anotherInvocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMock()).thenReturn(mock);
        when(anotherInvocation.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesFalseWithDifferentMock() throws Exception {
        Object mock1 = new Object();
        Object mock2 = new Object();
        Method method = Object.class.getMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock1);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        
        when(anotherInvocation.getMethod()).thenReturn(method);
        when(anotherInvocation.getMock()).thenReturn(mock2);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test
    public void testMatchesFalseWithDifferentMethod() throws Exception {
        Object mock = new Object();
        Method method1 = Object.class.getMethod("toString");
        Method method2 = Object.class.getMethod("hashCode");
        when(invocation.getMethod()).thenReturn(method1);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        
        when(anotherInvocation.getMethod()).thenReturn(method2);
        when(anotherInvocation.getMock()).thenReturn(mock);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.matches(anotherInvocation));
    }

    // ============ createFrom Static Method Tests ============

    @Test
    public void testCreateFromEmptyList() throws Exception {
        List<Invocation> invocations = new ArrayList<>();

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateFromSingleInvocation() throws Exception {
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(1, result.size());
        assertEquals(invocation, result.get(0).getInvocation());
    }

    @Test
    public void testCreateFromMultipleInvocations() throws Exception {
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(anotherInvocation.getArguments()).thenReturn(new Object[]{});
        when(anotherInvocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        invocations.add(anotherInvocation);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(2, result.size());
        assertEquals(invocation, result.get(0).getInvocation());
        assertEquals(anotherInvocation, result.get(1).getInvocation());
    }

    // ============ isVarargMatcher Tests ============

    

    @Test
    public void testIsVarargMatcherWithNonVarargMatcher() throws Exception {
        Matcher nonVarargMatcher = Matchers.any(Object.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        java.lang.reflect.Method isVarargMethod = InvocationMatcher.class.getDeclaredMethod(
            "isVarargMatcher", Matcher.class);
        isVarargMethod.setAccessible(true);

        assertFalse((boolean) isVarargMethod.invoke(matcher, nonVarargMatcher));
    }

    

    // ============ isVariableArgument Tests ============

    

    @Test
    public void testIsVariableArgumentFalseWhenNotVarArgs() throws Exception {
        Method nonVarargMethod = Object.class.getMethod("toString");
        when(anotherInvocation.getMethod()).thenReturn(nonVarargMethod);
        when(anotherInvocation.getRawArguments()).thenReturn(new Object[]{"arg"});

        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        java.lang.reflect.Method isVariableMethod = InvocationMatcher.class.getDeclaredMethod(
            "isVariableArgument", Invocation.class, int.class);
        isVariableMethod.setAccessible(true);

        assertFalse((boolean) isVariableMethod.invoke(matcher, anotherInvocation, 0));
    }

    

    

    // ============ captureArgumentsFrom Tests ============

    

    

    // ============ safelyArgumentsMatch Tests ============

    

    

    // ============ Edge case Tests ============

    

    
}
