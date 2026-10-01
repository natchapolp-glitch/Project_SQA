package org.mockito.internal.invocation;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherRegressionTest {

    private interface DummyService {
        void simpleMethod(String arg);
        void overloadedMethod(String arg);
        void overloadedMethod(Integer arg);
        void varargMethod(String prefix, String... items);
        void multipleArgs(String first, int second);
        String returnMethod();
    }

    private static class DummyCapturingMatcher extends BaseMatcher<Object> implements CapturesArguments {
        private final List<Object> captured = new ArrayList<Object>();

        public boolean matches(Object item) {
            return true;
        }

        public void describeTo(Description description) {
            description.appendText("dummy matcher");
        }

        public void captureFrom(Object argument) {
            captured.add(argument);
        }

        public List<Object> getCaptured() {
            return captured;
        }
    }

    private Object mockTarget;
    private Method simpleMethod;
    private Method overloadedStringMethod;
    private Method overloadedIntegerMethod;
    private Method varargMethod;
    private Method multipleArgsMethod;

    @Before
    public void setUp() throws Exception {
        mockTarget = new Object();
        simpleMethod = DummyService.class.getMethod("simpleMethod", String.class);
        overloadedStringMethod = DummyService.class.getMethod("overloadedMethod", String.class);
        overloadedIntegerMethod = DummyService.class.getMethod("overloadedMethod", Integer.class);
        varargMethod = DummyService.class.getMethod("varargMethod", String.class, String[].class);
        multipleArgsMethod = DummyService.class.getMethod("multipleArgs", String.class, int.class);
    }

    private Invocation createInvocation(final Object mock,
                                       final Method method,
                                       final Object[] args,
                                       final Object[] rawArgs,
                                       final boolean verified) {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method calledMethod, Object[] mArgs) throws Throwable {
                String name = calledMethod.getName();
                if ("getMock".equals(name)) {
                    return mock;
                } else if ("getMethod".equals(name)) {
                    return method;
                } else if ("getArguments".equals(name)) {
                    return args != null ? args : new Object[0];
                } else if ("getRawArguments".equals(name)) {
                    return rawArgs != null ? rawArgs : (args != null ? args : new Object[0]);
                } else if ("getArgumentAt".equals(name)) {
                    int index = (Integer) mArgs[0];
                    Class<?> targetType = (Class<?>) mArgs[1];
                    Object[] effectiveArgs = args != null ? args : new Object[0];
                    return targetType.cast(effectiveArgs[index]);
                } else if ("isVerified".equals(name)) {
                    return verified;
                } else if ("getLocation".equals(name)) {
                    return new Location() {
                        public String toString() {
                            return "dummy location";
                        }
                    };
                } else if ("toString".equals(name)) {
                    return "dummyInvocation";
                }
                return null;
            }
        };

        return (Invocation) Proxy.newProxyInstance(
                Invocation.class.getClassLoader(),
                new Class<?>[]{Invocation.class},
                handler
        );
    }

    @Test
    public void testConstructorWithEmptyMatchersPopulatesDefaultMatchers() {
        Invocation invocation = createInvocation(mockTarget, simpleMethod, new Object[]{"val"}, null, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertSame(invocation, matcher.getInvocation());
        assertSame(simpleMethod, matcher.getMethod());
        assertEquals(1, matcher.getMatchers().size());
        assertNotNull(matcher.getLocation());
    }

    @Test
    public void testConstructorWithExplicitMatchers() {
        Invocation invocation = createInvocation(mockTarget, simpleMethod, new Object[]{"val"}, null, false);
        DummyCapturingMatcher dummy = new DummyCapturingMatcher();
        List<Matcher> matchers = Collections.<Matcher>singletonList(dummy);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertSame(matchers, matcher.getMatchers());
        assertSame(invocation, matcher.getInvocation());
    }

    @Test
    public void testToStringProducesDescription() {
        Invocation invocation = createInvocation(mockTarget, simpleMethod, new Object[]{"val"}, null, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String result = matcher.toString();
        assertNotNull(result);
        assertTrue(result.contains("simpleMethod"));
    }

    @Test
    public void testMatchesReturnsTrueWhenSameMockMethodAndMatchingArguments() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"arg1"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"arg1"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.matches(inv2));
    }

    @Test
    public void testMatchesReturnsFalseWhenMockDiffers() {
        Object otherMock = new Object();
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"arg1"}, null, false);
        Invocation inv2 = createInvocation(otherMock, simpleMethod, new Object[]{"arg1"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.matches(inv2));
    }

    @Test
    public void testMatchesReturnsFalseWhenMethodDiffers() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"arg1"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, overloadedStringMethod, new Object[]{"arg1"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.matches(inv2));
    }

    @Test
    public void testMatchesReturnsFalseWhenArgumentsDoNotMatch() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"arg1"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"different"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.matches(inv2));
    }

    @Test
    public void testHasSameMethodIdentical() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"b"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.hasSameMethod(inv2));
    }

    @Test
    public void testHasSameMethodDifferentMethodName() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, overloadedStringMethod, new Object[]{"a"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSameMethod(inv2));
    }

    @Test
    public void testHasSameMethodDifferentParameterTypes() {
        Invocation inv1 = createInvocation(mockTarget, overloadedStringMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, overloadedIntegerMethod, new Object[]{1}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSameMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodWhenIdenticalAndUnverified() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertTrue(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodReturnsFalseWhenCandidateIsVerified() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, true);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodReturnsFalseWhenDifferentMock() {
        Object otherMock = new Object();
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(otherMock, simpleMethod, new Object[]{"a"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodReturnsFalseWhenDifferentMethodName() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"a"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, overloadedStringMethod, new Object[]{"a"}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        assertFalse(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testHasSimilarMethodWhenOverloadedWithDifferentArguments() {
        Invocation inv1 = createInvocation(mockTarget, overloadedStringMethod, new Object[]{"text"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, overloadedIntegerMethod, new Object[]{Integer.valueOf(10)}, null, false);

        InvocationMatcher matcher = new InvocationMatcher(inv1);
        // Method is overloaded (!methodEquals) and arguments don't match safely -> overloadedButSameArgs is false -> returns true
        assertTrue(matcher.hasSimilarMethod(inv2));
    }

    @Test
    public void testCaptureArgumentsFromNonVarargs() {
        Invocation invWanted = createInvocation(mockTarget, multipleArgsMethod, new Object[]{"hello", 42}, null, false);
        DummyCapturingMatcher matcher1 = new DummyCapturingMatcher();
        DummyCapturingMatcher matcher2 = new DummyCapturingMatcher();

        InvocationMatcher matcher = new InvocationMatcher(invWanted, Arrays.<Matcher>asList(matcher1, matcher2));

        Invocation invActual = createInvocation(mockTarget, multipleArgsMethod, new Object[]{"actualVal", 99}, null, false);
        matcher.captureArgumentsFrom(invActual);

        assertEquals(1, matcher1.getCaptured().size());
        assertEquals("actualVal", matcher1.getCaptured().get(0));

        assertEquals(1, matcher2.getCaptured().size());
        assertEquals(99, matcher2.getCaptured().get(0));
    }

    @Test
    public void testCaptureArgumentsFromVarargs() {
        Object[] actualArgs = new Object[]{"prefix", "item1", "item2"};
        Object[] actualRawArgs = new Object[]{"prefix", new String[]{"item1", "item2"}};

        Invocation invWanted = createInvocation(mockTarget, varargMethod, actualArgs, actualRawArgs, false);

        DummyCapturingMatcher prefixMatcher = new DummyCapturingMatcher();
        DummyCapturingMatcher varargMatcher = new DummyCapturingMatcher();

        InvocationMatcher matcher = new InvocationMatcher(
                invWanted,
                Arrays.<Matcher>asList(prefixMatcher, varargMatcher)
        );

        Invocation invActual = createInvocation(mockTarget, varargMethod, actualArgs, actualRawArgs, false);
        matcher.captureArgumentsFrom(invActual);

        assertEquals(1, prefixMatcher.getCaptured().size());
        assertEquals("prefix", prefixMatcher.getCaptured().get(0));

        assertEquals(1, varargMatcher.getCaptured().size());
        assertTrue(varargMatcher.getCaptured().get(0) instanceof String[]);
    }

    @Test
    public void testCreateFromMultipleInvocations() {
        Invocation inv1 = createInvocation(mockTarget, simpleMethod, new Object[]{"one"}, null, false);
        Invocation inv2 = createInvocation(mockTarget, simpleMethod, new Object[]{"two"}, null, false);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(Arrays.asList(inv1, inv2));

        assertEquals(2, result.size());
        assertSame(inv1, result.get(0).getInvocation());
        assertSame(inv2, result.get(1).getInvocation());
    }

    @Test
    public void testCreateFromEmptyList() {
        List<InvocationMatcher> result = InvocationMatcher.createFrom(Collections.<Invocation>emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
