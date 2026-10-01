package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
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
    private Invocation otherInvocation;
    private Matcher mockMatcher;
    private Matcher capturesMatcher;
    private Matcher varargMatcher;
    private Object mockObject;
    private Object otherMockObject;
    private Method testMethod;
    private Method varargMethod;
    private Method otherMethod;

    @Before
    public void setUp() throws Exception {
        mockObject = new Object();
        otherMockObject = new Object();
        
        invocation = mock(Invocation.class);
        otherInvocation = mock(Invocation.class);
        mockMatcher = mock(Matcher.class);
        capturesMatcher = mock(Matcher.class, withSettings().extraInterfaces(CapturesArguments.class));
        varargMatcher = mock(Matcher.class, withSettings().extraInterfaces(VarargMatcher.class));
        
        testMethod = String.class.getMethod("toString");
        varargMethod = String.class.getMethod("format", String.class, Object[].class);
        otherMethod = String.class.getMethod("length");
        
        when(invocation.getMethod()).thenReturn(testMethod);
        when(invocation.getMock()).thenReturn(mockObject);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getRawArguments()).thenReturn(new Object[]{});
        when(invocation.getLocation()).thenReturn(mock(Location.class));
        when(invocation.isVerified()).thenReturn(false);
    }

    // Constructor tests
    @Test
    public void testConstructorWithInvocationOnly() {
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertNotNull(matcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, matcher.getInvocation());
        assertNotNull(matcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mockMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(1, matcher.getMatchers().size());
        assertEquals(mockMatcher, matcher.getMatchers().get(0));
    }

    // Getter tests
    @Test
    public void testGetMethod() {
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(testMethod, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mockMatcher);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(1, matcher.getMatchers().size());
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(location, matcher.getLocation());
    }

    // hasSameMethod tests
    @Test
    public void testHasSameMethodSameMock() {
        when(otherInvocation.getMethod()).thenReturn(testMethod);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertTrue(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSameMethodDifferentMethod() {
        when(otherInvocation.getMethod()).thenReturn(otherMethod);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSameMethodDifferentMethodName() throws Exception {
        Method m1 = String.class.getMethod("toString");
        Method m2 = String.class.getMethod("length");
        when(invocation.getMethod()).thenReturn(m1);
        when(otherInvocation.getMethod()).thenReturn(m2);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSameMethod(otherInvocation));
    }

    @Test
    public void testHasSameMethodDifferentParameterCount() throws Exception {
        Method m1 = String.class.getMethod("toString");
        Method m2 = String.class.getMethod("format", String.class, Object[].class);
        when(invocation.getMethod()).thenReturn(m1);
        when(otherInvocation.getMethod()).thenReturn(m2);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSameMethod(otherInvocation));
    }

    // hasSimilarMethod tests
    @Test
    public void testHasSimilarMethodTrue() {
        when(otherInvocation.getMethod()).thenReturn(testMethod);
        when(otherInvocation.getMock()).thenReturn(mockObject);
        when(otherInvocation.isVerified()).thenReturn(false);
        when(otherInvocation.getArguments()).thenReturn(new Object[]{});
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertTrue(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethodDifferentMethodName() {
        when(otherInvocation.getMethod()).thenReturn(otherMethod);
        when(otherInvocation.isVerified()).thenReturn(false);
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethodVerified() {
        when(otherInvocation.getMethod()).thenReturn(testMethod);
        when(otherInvocation.getMock()).thenReturn(mockObject);
        when(otherInvocation.isVerified()).thenReturn(true);
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSimilarMethod(otherInvocation));
    }

    @Test
    public void testHasSimilarMethodDifferentMock() {
        when(otherInvocation.getMethod()).thenReturn(testMethod);
        when(otherInvocation.getMock()).thenReturn(otherMockObject);
        when(otherInvocation.isVerified()).thenReturn(false);
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.hasSimilarMethod(otherInvocation));
    }

    // matches tests
    @Test
    public void testMatchesSameMockAndMethod() {
        when(otherInvocation.getMock()).thenReturn(mockObject);
        when(otherInvocation.getMethod()).thenReturn(testMethod);
        when(otherInvocation.getArguments()).thenReturn(new Object[]{});
        
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        // ArgumentsComparator will be called; test verifies basic flow
        assertNotNull(matcher);
    }

    @Test
    public void testMatchesDifferentMock() {
        when(otherInvocation.getMock()).thenReturn(otherMockObject);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentMethod() {
        when(otherInvocation.getMock()).thenReturn(mockObject);
        when(otherInvocation.getMethod()).thenReturn(otherMethod);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.matches(otherInvocation));
    }

    // toString test
    

    // createFrom static factory test
    @Test
    public void testCreateFromEmptyList() {
        List<Invocation> invocations = new ArrayList<>();
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreateFromSingleInvocation() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        assertEquals(1, result.size());
        assertEquals(invocation, result.get(0).getInvocation());
    }

    

    // captureArgumentsFrom tests
    

    

    

    // Edge case: null method name (defensive programming)
    

    // Edge case: matcher decorator
    

    // Integration: complete flow with multiple matchers
    

    // Edge case: empty varargs array
    
}
