Rendered KKU IntelSphere response. Model label: Gemini - gemini-3.6-flash

```java
package org.mockito.internal.invocation;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private Method sampleMethod;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        sampleMethod = String.class.getMethod("substring", int.class);
        when(invocation.getMethod()).thenReturn(sampleMethod);
        when(invocation.getArguments()).thenReturn(new Object[]{1});
        when(invocation.getMock()).thenReturn("mockTarget");
    }

    @Test
    public void testConstructorWithExplicitMatchers() {
        Matcher matcher = new IsEqual<Integer>(1);
        List<Matcher> matchers = Collections.singletonList(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testSingleArgConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(1, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(sampleMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testHasSameMethod_TrueForSameMethodSignature() throws Exception {
        Invocation candidate = mock(Invocation.class);
        Method sameMethod = String.class.getMethod("substring", int.class);
        when(candidate.getMethod()).thenReturn(sameMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_FalseForDifferentMethodName() throws Exception {
        Invocation candidate = mock(Invocation.class);
        Method diffMethod = String.class.getMethod("length");
        when(candidate.getMethod()).thenReturn(diffMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_FalseForDifferentParamCount() throws Exception {
        Invocation candidate = mock(Invocation.class);
        Method diffMethod = String.class.getMethod("substring", int.class, int.class);
        when(candidate.getMethod()).thenReturn(diffMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_FalseForDifferentParamTypes() throws Exception {
        Invocation candidate = mock(Invocation.class);
        Method stringArgMethod = String.class.getMethod("indexOf", String.class);
        Method intArgMethod = String.class.getMethod("indexOf", int.class);

        when(invocation.getMethod()).thenReturn(stringArgMethod);
        when(candidate.getMethod()).thenReturn(intArgMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_FalseWhenDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(sampleMethod);
        when(candidate.getMock()).thenReturn("differentMock");
        when(candidate.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_FalseWhenVerified() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(sampleMethod);
        when(candidate.getMock()).thenReturn("mockTarget");
        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_TrueWhenUnverifiedAndSameMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(sampleMethod);
        when(candidate.getMock()).thenReturn("mockTarget");
        when(candidate.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testCreateFrom() {
        Invocation inv1 = mock(Invocation.class);
        Invocation inv2 = mock(Invocation.class);
        when(inv1.getArguments()).thenReturn(new Object[]{});
        when(inv2.getArguments()).thenReturn(new Object[]{});

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(inv1, inv2));
        assertEquals(2, matchers.size());
        assertEquals(inv1, matchers.get(0).getInvocation());
        assertEquals(inv2, matchers.get(1).getInvocation());
    }

    @Test
    public void testCaptureArgumentsFrom_NonVarArgs() {
        CapturingMatcher capturingMatcher = new CapturingMatcher();
        InvocationMatcher invocationMatcher = new InvocationMatcher(
                invocation,
                Collections.<Matcher>singletonList(capturingMatcher)
        );

        Invocation targetInvocation = mock(Invocation.class);
        when(targetInvocation.getMethod()).thenReturn(sampleMethod);
        when(targetInvocation.getArgumentAt(0, Object.class)).thenReturn("capturedValue");

        invocationMatcher.captureArgumentsFrom(targetInvocation);
        assertEquals("capturedValue", capturingMatcher.getLastCaptured());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher.toString());
    }

    private static class CapturingMatcher extends BaseMatcher<Object> implements CapturesArguments {
        private Object lastCaptured;

        @Override
        public boolean matches(Object item) {
            return true;
        }

        @Override
        public void describeTo(Description description) {
        }

        @Override
        public void captureFrom(Object argument) {
            this.lastCaptured = argument;
        }

        public Object getLastCaptured() {
            return lastCaptured;
        }
    }
}

```