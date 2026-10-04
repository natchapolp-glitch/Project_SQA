package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.Invocation;

interface IMSample {
    void simpleMethod(String arg);
    void simpleMethod(String arg, int other);
    void varargsMethod(String... args);
}

public class InvocationMatcherTest {

    private Invocation createInvocation(String methodName, Object... args) throws Exception {
        IMSample mock = mock(IMSample.class);
        if ("simpleMethod".equals(methodName)) {
            if (args.length == 1) {
                mock.simpleMethod((String) args[0]);
            } else {
                mock.simpleMethod((String) args[0], (Integer) args[1]);
            }
        } else if ("varargsMethod".equals(methodName)) {
            mock.varargsMethod((String[]) args);
        }
        return mockingDetails(mock).getInvocations().iterator().next();
    }

    @Test
    public void constructorWithoutMatchersCreatesEqualsMatchersFromArguments() throws Exception {
        Invocation invocation = createInvocation("simpleMethod", "foo");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(1, matcher.getMatchers().size());
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void matchesReturnsFalseForDifferentMock() throws Exception {
        Invocation invocation1 = createInvocation("simpleMethod", "foo");
        Invocation invocation2 = createInvocation("simpleMethod", "foo");
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void hasSameMethodTrueForSameSignature() throws Exception {
        IMSample mock = mock(IMSample.class);
        mock.simpleMethod("a");
        Invocation invocation1 = mockingDetails(mock).getInvocations().iterator().next();
        mock.simpleMethod("b");
        List<Invocation> all = new ArrayList<Invocation>(mockingDetails(mock).getInvocations());
        Invocation invocation2 = all.get(0);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void hasSameMethodFalseForOverloadedMethod() throws Exception {
        Invocation invocation1 = createInvocation("simpleMethod", "a");
        Invocation invocation2 = createInvocation("simpleMethod", "a", 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);
        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void createFromReturnsMatchersInSameOrderAndSize() throws Exception {
        Invocation invocation1 = createInvocation("simpleMethod", "a");
        Invocation invocation2 = createInvocation("simpleMethod", "b");
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        assertSame(invocation1, matchers.get(0).getInvocation());
        assertSame(invocation2, matchers.get(1).getInvocation());
    }

    @Test
    public void delegatesToInvocationForBasicAccessors() throws Exception {
        Invocation invocation = createInvocation("simpleMethod", "a");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertSame(invocation, matcher.getInvocation());
        assertSame(invocation.getMethod(), matcher.getMethod());
        assertNotNull(matcher.getLocation());
        assertNotNull(matcher.toString());
    }

    @Test
    public void captureArgumentsFromCapturesNonVarargArgument() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        IMSample mock = mock(IMSample.class);
        mock.simpleMethod("hello");
        Invocation invocation = mockingDetails(mock).getInvocations().iterator().next();

        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add((Matcher) captor.capture());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals("hello", captor.getValue());
    }
}
