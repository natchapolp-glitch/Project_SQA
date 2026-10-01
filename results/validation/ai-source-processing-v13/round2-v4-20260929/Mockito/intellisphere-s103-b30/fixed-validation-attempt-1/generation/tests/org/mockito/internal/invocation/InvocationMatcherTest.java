package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Method sampleMethod1;
    private Method sampleMethod2;
    private Method varargsMethod;
    private Object dummyMock1;
    private Object dummyMock2;

    public void targetMethod1(String arg1, int arg2) {}
    public void targetMethod2(String arg1) {}
    public void varargsTargetMethod(String... args) {}

    @Before
    public void setUp() throws Exception {
        sampleMethod1 = InvocationMatcherTest.class.getMethod("targetMethod1", String.class, int.class);
        sampleMethod2 = InvocationMatcherTest.class.getMethod("targetMethod2", String.class);
        varargsMethod = InvocationMatcherTest.class.getMethod("varargsTargetMethod", String[].class);
        dummyMock1 = new Object();
        dummyMock2 = new Object();
    }

    private Invocation createMockInvocation(final Object mock, final Method method, final Object[] args, final Object[] rawArgs, final Location location, final boolean verified) {
        return (Invocation) Proxy.newProxyInstance(
                Invocation.class.getClassLoader(),
                new Class<?>[]{Invocation.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method m, Object[] methodArgs) throws Throwable {
                        String name = m.getName();
                        if ("getMock".equals(name)) return mock;
                        if ("getMethod".equals(name)) return method;
                        if ("getArguments".equals(name)) return args != null ? args : new Object[0];
                        if ("getRawArguments".equals(name)) return rawArgs != null ? rawArgs : (args != null ? args : new Object[0]);
                        if ("getLocation".equals(name)) return location;
                        if ("isVerified".equals(name)) return verified;
                        if ("getArgumentAt".equals(name)) {
                            int idx = (Integer) methodArgs[0];
                            return args[idx];
                        }
                        if ("toString".equals(name)) return "DummyInvocation";
                        if ("hashCode".equals(name)) return System.identityHashCode(proxy);
                        if ("equals".equals(name)) return proxy == methodArgs[0];
                        return null;
                    }
                }
        );
    }

    private Invocation createMockInvocation(Object mock, Method method, Object[] args) {
        return createMockInvocation(mock, method, args, args, null, false);
    }

    private static class CapturingMatcher extends org.hamcrest.BaseMatcher<Object> implements CapturesArguments {
        private final List<Object> captured = new ArrayList<Object>();

        @Override
        public void captureFrom(Object argument) {
            captured.add(argument);
        }

        @Override
        public boolean matches(Object item) {
            return true;
        }



        @Override
        public void describeTo(org.hamcrest.Description description) {
            description.appendText("CapturingMatcher");
        }
    }

    @Test
    public void testConstructorAndBasicGetters() {
        Invocation invocation = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"test", 123});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(sampleMethod1, matcher.getMethod());
        assertNotNull(matcher.getMatchers());
        assertEquals(2, matcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithExplicitMatchers() {
        Invocation invocation = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"test", 123});
        Matcher matcher1 = new IsEqual<String>("test");
        Matcher matcher2 = new IsEqual<Integer>(123);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToStringReturnsNonNullString() {
        Invocation invocation = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"test", 123});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String result = matcher.toString();
        assertNotNull(result);
        assertTrue(result.contains("targetMethod1"));
    }

    @Test
    public void testMatchesSameMockMethodAndArguments() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.matches(inv2));
    }

    @Test
    public void testMatchesDifferentMock() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock2, sampleMethod1, new Object[]{"hello", 1});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.matches(inv2));
    }

    @Test
    public void testMatchesDifferentArguments() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"world", 2});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.matches(inv2));
    }

    @Test
    public void testHasSameMethodTrue() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock2, sampleMethod1, new Object[]{"different", 2});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.hasSameMethod(inv2));
    }

    @Test
    public void testHasSameMethodDifferentNameOrParams() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod2, new Object[]{"hello"});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSameMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodTrue() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"different", 2});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodFalseWhenVerified() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1}, new Object[]{"hello", 1}, null, true);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodFalseWhenDifferentMock() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"hello", 1});
        Invocation inv2 = createMockInvocation(dummyMock2, sampleMethod1, new Object[]{"hello", 1});

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testGetLocation() {
        Location mockLocation = new Location() {
            @Override
            public String toString() {
                return "at test.Class.method(Class.java:10)";
            }
        };
        Invocation inv = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"a", 1}, new Object[]{"a", 1}, mockLocation, false);
        InvocationMatcher matcher = new InvocationMatcher(inv);

        assertEquals(mockLocation, matcher.getLocation());
    }

    @Test
    public void testCreateFromList() {
        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"a", 1});
        Invocation inv2 = createMockInvocation(dummyMock1, sampleMethod2, new Object[]{"b"});

        List<Invocation> invocations = Arrays.asList(inv1, inv2);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        assertEquals(inv1, matchers.get(0).getInvocation());
        assertEquals(inv2, matchers.get(1).getInvocation());
    }

    @Test
    public void testCreateFromEmptyList() {
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Collections.<Invocation>emptyList());
        assertNotNull(matchers);
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testCaptureArgumentsFromNonVarArgs() {
        CapturingMatcher cap1 = new CapturingMatcher();
        CapturingMatcher cap2 = new CapturingMatcher();
        List<Matcher> matchers = Arrays.<Matcher>asList(cap1, cap2);

        Invocation inv1 = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"val1", 100});
        InvocationMatcher matcher = new InvocationMatcher(inv1, matchers);

        Invocation actualInvocation = createMockInvocation(dummyMock1, sampleMethod1, new Object[]{"passedVal", 200});
        matcher.captureArgumentsFrom(actualInvocation);

        assertEquals(1, cap1.captured.size());
        assertEquals("passedVal", cap1.captured.get(0));
        assertEquals(1, cap2.captured.size());
        assertEquals(200, cap2.captured.get(0));
    }

    @Test
    public void testCaptureArgumentsFromVarArgs() {
        CapturingMatcher cap1 = new CapturingMatcher();
        List<Matcher> matchers = Collections.<Matcher>singletonList(cap1);

        Object[] args = new Object[]{new String[]{"a", "b"}};
        Object[] rawArgs = new Object[]{new String[]{"a", "b"}};

        Invocation inv1 = createMockInvocation(dummyMock1, varargsMethod, args, rawArgs, null, false);
        InvocationMatcher matcher = new InvocationMatcher(inv1, matchers);

        matcher.captureArgumentsFrom(inv1);

        assertEquals(1, cap1.captured.size());
        assertArrayEquals(new String[]{"a", "b"}, (String[]) cap1.captured.get(0));
    }
}
