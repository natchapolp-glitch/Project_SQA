package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;

import org.junit.Test;

public class InvocationMatcherTest {

    public interface SampleService {
        void simpleMethod(String arg);
        void varargsMethod(String... args);
    }

    private Invocation mockInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        Invocation invocation = mock(Invocation.class);
        try {
            Method method = SampleService.class.getMethod(methodName, parameterTypes);
            org.mockito.Mockito.when(invocation.getMethod()).thenReturn(method);
            org.mockito.Mockito.when(invocation.getArguments()).thenReturn(arguments);
            org.mockito.Mockito.when(invocation.getRawArguments()).thenReturn(arguments);
            org.mockito.Mockito.when(invocation.getMock()).thenReturn(new Object());
            org.mockito.Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return invocation;
    }

    @Test
    public void testGettersAndConstructors() {
        Invocation invocation = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "test" });
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new IsEqual<Object>("test"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertNotNull(invocationMatcher.getLocation());
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testDefaultMatcherCreation() {
        Invocation invocation = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "test" });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher.getMatchers());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "foo" });
        Invocation invocation2 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "foo" });
        Object sharedMock = new Object();
        try {
            org.mockito.Mockito.when(invocation1.getMock()).thenReturn(sharedMock);
            org.mockito.Mockito.when(invocation2.getMock()).thenReturn(sharedMock);
        } catch (Exception e) {
        }

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "foo" });
        Invocation invocation2 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "bar" });
        Invocation invocation3 = mockInvocation("varargsMethod", new Class<?>[] { String[].class }, new Object[] { new String[] { "foo" } });

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation2));
        assertFalse(matcher.hasSameMethod(invocation3));
    }

    @Test
    public void testCreateFromList() {
        Invocation invocation1 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "foo" });
        Invocation invocation2 = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "bar" });
        List<Invocation> list = new ArrayList<Invocation>();
        list.add(invocation1);
        list.add(invocation2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(list);

        assertEquals(2, matchers.size());
        assertEquals(invocation1, matchers.get(0).getInvocation());
        assertEquals(invocation2, matchers.get(1).getInvocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mockInvocation("simpleMethod", new Class<?>[] { String.class }, new Object[] { "capturedValue" });
        
        final List<Object> captured = new ArrayList<Object>();
        Matcher capturingMatcher = new BaseMatcher<Object>() implements CapturesArguments {
            public void captureFrom(Object value) {
                captured.add(value);
            }
            public boolean matches(Object item) {
                return true;
            }
            public void describeTo(Description description) {
            }
        };

        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(capturingMatcher);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals(1, captured.size());
        assertEquals("capturedValue", captured.get(0));
    }
}
