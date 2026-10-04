package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    public interface TestedObject {
        void simpleMethod(String arg);
        void noArgMethod();
        void varargsMethod(String... args);
        void overloaded(String arg);
        void overloaded(Integer arg);
    }

    private TestedObject mock;

    @Before
    public void setup() {
        mock = (TestedObject) java.lang.reflect.Proxy.newProxyInstance(
                TestedObject.class.getClassLoader(),
                new Class[]{TestedObject.class},
                (proxy, method, args) -> null);
    }

    private Invocation invocationOf(String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        java.lang.reflect.Method method = TestedObject.class.getMethod(methodName, paramTypes);
        return new InvocationImpl(
                mock,
                new org.mockito.internal.invocation.MockitoMethod() {
                    @Override
                    public String getName() { return method.getName(); }
                    @Override
                    public Class<?>[] getParameterTypes() { return method.getParameterTypes(); }
                    @Override
                    public Class<?> getReturnType() { return method.getReturnType(); }
                    @Override
                    public Class<?>[] getExceptionTypes() { return method.getExceptionTypes(); }
                    @Override
                    public boolean isVarArgs() { return method.isVarArgs(); }
                    @Override
                    public java.lang.reflect.Method getJavaMethod() { return method; }
                },
                args,
                1,
                null,
                new org.mockito.internal.invocation.RealMethod.FromMethod(method)
        );
    }

    @Test
    public void constructorWithoutMatchersDerivesEqualityMatchersFromArguments() throws Exception {
        Invocation invocation = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"hello"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(1, matcher.getMatchers().size());
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void matchesReturnsFalseForDifferentArguments() throws Exception {
        Invocation invocation1 = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"hello"});
        Invocation invocation2 = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"different"});
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void matchesNoArgMethodWithoutException() throws Exception {
        Invocation invocation = invocationOf("noArgMethod", new Class[]{}, new Object[]{});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void hasSameMethodTrueForSameMethodFalseForOverloaded() throws Exception {
        Invocation invocation1 = invocationOf("overloaded", new Class[]{String.class}, new Object[]{"a"});
        Invocation invocation2 = invocationOf("overloaded", new Class[]{Integer.class}, new Object[]{1});
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation1));
        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void createFromProducesSameSizeListInOrder() throws Exception {
        Invocation invocation1 = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"a"});
        Invocation invocation2 = invocationOf("noArgMethod", new Class[]{}, new Object[]{});
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        assertSame(invocation1, matchers.get(0).getInvocation());
        assertSame(invocation2, matchers.get(1).getInvocation());
    }

    @Test
    public void createFromEmptyListReturnsEmptyList() {
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Collections.<Invocation>emptyList());
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void toStringDoesNotThrowAndIsNonEmpty() throws Exception {
        Invocation invocation = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"hello"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String result = matcher.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getMethodAndInvocationAndMatchersDelegateExactly() throws Exception {
        Invocation invocation = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"hello"});
        List<Matcher> matchersList = new ArrayList<Matcher>();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchersList);

        assertSame(invocation, matcher.getInvocation());
        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertEquals(1, matcher.getMatchers().size());
    }

    @Test
    public void varargsInvocationMatchesVarargsInvocation() throws Exception {
        Invocation invocation1 = invocationOf("varargsMethod", new Class[]{String[].class}, new Object[]{new String[]{"a", "b"}});
        Invocation invocation2 = invocationOf("varargsMethod", new Class[]{String[].class}, new Object[]{new String[]{"a", "b"}});
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.matches(invocation2));
    }

    @Test
    public void captureArgumentsFromDoesNotThrowForNoArgMethod() throws Exception {
        Invocation invocation = invocationOf("noArgMethod", new Class[]{}, new Object[]{});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void getLocationReturnsInvocationLocation() throws Exception {
        Invocation invocation = invocationOf("simpleMethod", new Class[]{String.class}, new Object[]{"a"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getLocation(), matcher.getLocation());
    }
}
