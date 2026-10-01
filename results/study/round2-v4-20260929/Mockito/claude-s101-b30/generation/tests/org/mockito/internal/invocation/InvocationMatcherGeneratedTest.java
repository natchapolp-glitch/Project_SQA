package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class InvocationMatcherGeneratedTest {

    public interface Service {
        String one(String s);
        String two(int a, String b);
        void many(String... xs);
        void overloaded(String s);
        void overloaded(Object o);
    }

    private Service mockA;
    private Service mockB;
    private Method one;
    private Method two;
    private Method many;
    private Method overloadedString;
    private Method overloadedObject;

    @Before
    public void setUp() throws Exception {
        mockA = mock(Service.class);
        mockB = mock(Service.class);
        one = Service.class.getMethod("one", String.class);
        two = Service.class.getMethod("two", int.class, String.class);
        many = Service.class.getMethod("many", String[].class);
        overloadedString = Service.class.getMethod("overloaded", String.class);
        overloadedObject = Service.class.getMethod("overloaded", Object.class);
    }

    private Invocation inv(Object mock, Method method, Object[] args, Object[] raw, boolean verified) {
        Invocation i = mock(Invocation.class);
        when(i.getMock()).thenReturn(mock);
        when(i.getMethod()).thenReturn(method);
        when(i.getArguments()).thenReturn(args);
        when(i.getRawArguments()).thenReturn(raw);
        when(i.isVerified()).thenReturn(verified);
        for (int p = 0; p < raw.length; p++) {
            when(i.getArgumentAt(p, Object.class)).thenReturn(raw[p]);
        }
        return i;
    }

    private Invocation simple(Object mock, Method method, Object... args) {
        return inv(mock, method, args, args, false);
    }

    private static Matcher alwaysTrue() {
        return new BaseMatcher<Object>() {
            public boolean matches(Object item) { return true; }
            public void describeTo(Description d) { d.appendText("alwaysTrue"); }
        };
    }

    private static Matcher throwing() {
        return new BaseMatcher<Object>() {
            public boolean matches(Object item) { throw new IllegalStateException("boom"); }
            public void describeTo(Description d) { d.appendText("throwing"); }
        };
    }

    private static Matcher varargMatcher() {
        return new VarargTestMatcher();
    }

    private static class VarargTestMatcher extends BaseMatcher<Object> implements VarargMatcher {
        public boolean matches(Object item) { return true; }
        public void describeTo(Description d) { d.appendText("vararg"); }
    }

    private static class DecoratingTestMatcher extends BaseMatcher<Object> implements MatcherDecorator {
        private final Matcher inner;
        DecoratingTestMatcher(Matcher inner) { this.inner = inner; }
        public Matcher getActualMatcher() { return inner; }
        public boolean matches(Object item) { return inner.matches(item); }
        public void describeTo(Description d) { inner.describeTo(d); }
    }

    private boolean callBoolean(InvocationMatcher target, String name, Class[] types, Object... args) throws Exception {
        Method m = InvocationMatcher.class.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return (Boolean) m.invoke(target, args);
    }

    // ---- constructors / getters ----

    @Test
    public void singleArgConstructorBuildsMatchersFromArguments() {
        Invocation i = simple(mockA, two, 1, "x");
        InvocationMatcher im = new InvocationMatcher(i);
        assertSame(i, im.getInvocation());
        assertEquals(2, im.getMatchers().size());
        assertTrue(im.getMatchers().get(0).matches(1));
        assertTrue(im.getMatchers().get(1).matches("x"));
        assertFalse(im.getMatchers().get(1).matches("y"));
    }

    @Test
    public void twoArgConstructorWithEmptyMatchersFallsBackToArguments() {
        Invocation i = simple(mockA, one, "v");
        InvocationMatcher im = new InvocationMatcher(i, Collections.<Matcher>emptyList());
        assertEquals(1, im.getMatchers().size());
        assertTrue(im.getMatchers().get(0).matches("v"));
    }

    @Test
    public void twoArgConstructorKeepsSuppliedMatchers() {
        Invocation i = simple(mockA, one, "v");
        List<Matcher> supplied = new ArrayList<Matcher>();
        supplied.add(alwaysTrue());
        InvocationMatcher im = new InvocationMatcher(i, supplied);
        assertSame(supplied, im.getMatchers());
    }

    @Test
    public void getMethodDelegatesToInvocation() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertEquals(one, im.getMethod());
    }

    @Test
    public void getLocationDelegatesToInvocation() {
        Invocation i = simple(mockA, one, "v");
        Location loc = mock(Location.class);
        when(i.getLocation()).thenReturn(loc);
        assertSame(loc, new InvocationMatcher(i).getLocation());
    }

    @Test
    public void toStringContainsMethodName() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        String s = im.toString();
        assertNotNull(s);
        assertTrue(s, s.contains("one"));
    }

    // ---- matches ----

    @Test
    public void matchesSameMockMethodAndArguments() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(im.matches(simple(mockA, one, "v")));
    }

    @Test
    public void doesNotMatchDifferentMock() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.matches(simple(mockB, one, "v")));
    }

    @Test
    public void doesNotMatchDifferentMethod() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.matches(simple(mockA, overloadedString, "v")));
    }

    @Test
    public void doesNotMatchDifferentArguments() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.matches(simple(mockA, one, "other")));
    }

    // ---- hasSameMethod ----

    @Test
    public void hasSameMethodTrueForSameNameAndParameterTypes() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(im.hasSameMethod(simple(mockB, one, "z")));
    }

    @Test
    public void hasSameMethodFalseForDifferentName() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.hasSameMethod(simple(mockA, overloadedString, "v")));
    }

    @Test
    public void hasSameMethodFalseForDifferentParameterTypes() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, overloadedString, "v"));
        assertFalse(im.hasSameMethod(simple(mockA, overloadedObject, "v")));
    }

    @Test
    public void hasSameMethodFalseForDifferentParameterCount() throws Exception {
        Method oneArgTwoName = Service.class.getMethod("one", String.class);
        InvocationMatcher im = new InvocationMatcher(simple(mockA, two, 1, "x"));
        assertFalse(im.hasSameMethod(simple(mockA, oneArgTwoName, "x")));
    }

    // ---- hasSimilarMethod ----

    @Test
    public void hasSimilarMethodTrueForSameMethodUnverifiedSameMock() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(im.hasSimilarMethod(simple(mockA, one, "different")));
    }

    @Test
    public void hasSimilarMethodFalseForDifferentName() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.hasSimilarMethod(simple(mockA, two, 1, "x")));
    }

    @Test
    public void hasSimilarMethodFalseWhenCandidateVerified() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.hasSimilarMethod(inv(mockA, one, new Object[] { "v" }, new Object[] { "v" }, true)));
    }

    @Test
    public void hasSimilarMethodFalseForDifferentMock() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(im.hasSimilarMethod(simple(mockB, one, "v")));
    }

    @Test
    public void hasSimilarMethodFalseForOverloadedMethodWithSameArguments() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, overloadedString, "x"));
        assertFalse(im.hasSimilarMethod(simple(mockA, overloadedObject, "x")));
    }

    @Test
    public void hasSimilarMethodTrueForOverloadedMethodWithDifferentArguments() {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, overloadedString, "x"));
        assertTrue(im.hasSimilarMethod(simple(mockA, overloadedObject, "y")));
    }

    // ---- safelyArgumentsMatch (private) ----

    @Test
    public void safelyArgumentsMatchTrueWhenArgumentsMatch() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(callBoolean(im, "safelyArgumentsMatch", new Class[] { Object[].class },
                new Object[] { new Object[] { "v" } }));
    }

    @Test
    public void safelyArgumentsMatchFalseWhenArgumentsDiffer() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(callBoolean(im, "safelyArgumentsMatch", new Class[] { Object[].class },
                new Object[] { new Object[] { "nope" } }));
    }

    @Test
    public void safelyArgumentsMatchFalseWhenMatcherThrows() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"), Arrays.asList(throwing()));
        assertFalse(callBoolean(im, "safelyArgumentsMatch", new Class[] { Object[].class },
                new Object[] { new Object[] { "v" } }));
    }

    // ---- isVarargMatcher (private) ----

    @Test
    public void isVarargMatcherFalseForPlainMatcher() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertFalse(callBoolean(im, "isVarargMatcher", new Class[] { Matcher.class }, alwaysTrue()));
    }

    @Test
    public void isVarargMatcherTrueForVarargMatcher() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(callBoolean(im, "isVarargMatcher", new Class[] { Matcher.class }, varargMatcher()));
    }

    @Test
    public void isVarargMatcherUnwrapsDecorator() throws Exception {
        InvocationMatcher im = new InvocationMatcher(simple(mockA, one, "v"));
        assertTrue(callBoolean(im, "isVarargMatcher", new Class[] { Matcher.class },
                new DecoratingTestMatcher(varargMatcher())));
        assertFalse(callBoolean(im, "isVarargMatcher", new Class[] { Matcher.class },
                new DecoratingTestMatcher(alwaysTrue())));
    }

    // ---- isVariableArgument (private) ----

    @Test
    public void isVariableArgumentTrueForLastArrayArgOfVarargsMethod() throws Exception {
        Object[] raw = new Object[] { new String[] { "a", "b" } };
        Invocation i = inv(mockA, many, new Object[] { "a", "b" }, raw, false);
        InvocationMatcher im = new InvocationMatcher(i);
        assertTrue(callBoolean(im, "isVariableArgument", new Class[] { Invocation.class, int.class }, i, 0));
    }

    @Test
    public void isVariableArgumentFalseForNonVarargsMethodOrWrongPosition() throws Exception {
        Invocation nonVarargs = simple(mockA, one, "v");
        InvocationMatcher im = new InvocationMatcher(nonVarargs);
        assertFalse(callBoolean(im, "isVariableArgument", new Class[] { Invocation.class, int.class },
                nonVarargs, 0));

        Object[] raw = new Object[] { new String[] { "a" } };
        Invocation varargs = inv(mockA, many, new Object[] { "a" }, raw, false);
        assertFalse(callBoolean(im, "isVariableArgument", new Class[] { Invocation.class, int.class },
                varargs, 1));
    }

    // ---- captureArgumentsFrom / createFrom ----

    @Test
    public void captureArgumentsFromCapturesEachNonVarargArgument() {
        CapturingMatcher first = new CapturingMatcher();
        CapturingMatcher second = new CapturingMatcher();
        Invocation i = simple(mockA, two, 5, "s");
        InvocationMatcher im = new InvocationMatcher(i, Arrays.asList((Matcher) first, (Matcher) second));
        im.captureArgumentsFrom(i);
        assertEquals(Arrays.asList((Object) 5), first.getAllValues());
        assertEquals(Arrays.asList((Object) "s"), second.getAllValues());
    }

    @Test
    public void createFromWrapsEachInvocationInOrder() {
        Invocation i1 = simple(mockA, one, "a");
        Invocation i2 = simple(mockA, two, 1, "b");
        List<InvocationMatcher> out = InvocationMatcher.createFrom(Arrays.asList(i1, i2));
        assertEquals(2, out.size());
        assertSame(i1, out.get(0).getInvocation());
        assertSame(i2, out.get(1).getInvocation());
    }
}
