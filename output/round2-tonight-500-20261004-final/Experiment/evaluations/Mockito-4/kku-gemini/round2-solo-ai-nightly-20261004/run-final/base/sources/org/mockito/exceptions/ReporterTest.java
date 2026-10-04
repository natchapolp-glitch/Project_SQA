package org.mockito.exceptions;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.verification.CannotVerifyStubOnlyMock;
import org.mockito.internal.matchers.LocalizedMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReporterTest {

    @Test(expected = MockitoException.class)
    public void testCheckedExceptionInvalid() {
        Reporter reporter = new Reporter();
        reporter.checkedExceptionInvalid(new Exception("test"));
    }

    @Test(expected = MockitoException.class)
    public void testCannotStubWithNullThrowable() {
        Reporter reporter = new Reporter();
        reporter.cannotStubWithNullThrowable();
    }

    @Test(expected = UnfinishedStubbingException.class)
    public void testUnfinishedStubbing() {
        Reporter reporter = new Reporter();
        reporter.unfinishedStubbing(null);
    }

    @Test(expected = MockitoException.class)
    public void testIncorrectUseOfApi() {
        Reporter reporter = new Reporter();
        reporter.incorrectUseOfApi();
    }

    @Test(expected = MissingMethodInvocationException.class)
    public void testMissingMethodInvocation() {
        Reporter reporter = new Reporter();
        reporter.missingMethodInvocation();
    }

    @Test(expected = UnfinishedVerificationException.class)
    public void testUnfinishedVerificationException() {
        Reporter reporter = new Reporter();
        reporter.unfinishedVerificationException(null);
    }

    @Test(expected = NotAMockException.class)
    public void testNotAMockPassedToVerify() {
        Reporter reporter = new Reporter();
        reporter.notAMockPassedToVerify(String.class);
    }

    @Test(expected = NullInsteadOfMockException.class)
    public void testNullPassedToVerify() {
        Reporter reporter = new Reporter();
        reporter.nullPassedToVerify();
    }

    @Test(expected = InvalidUseOfMatchersException.class)
    public void testInvalidUseOfMatchers() {
        Reporter reporter = new Reporter();
        List<LocalizedMatcher> matchers = new ArrayList<LocalizedMatcher>();
        reporter.invalidUseOfMatchers(2, matchers);
    }

    @Test(expected = CannotVerifyStubOnlyMock.class)
    public void testStubPassedToVerify() {
        Reporter reporter = new Reporter();
        reporter.stubPassedToVerify();
    }

    @Test(expected = MockitoException.class)
    public void testCannotMockFinalClass() {
        Reporter reporter = new Reporter();
        reporter.cannotMockFinalClass(String.class);
    }

    @Test(expected = MockitoException.class)
    public void testWantedAtMostX() {
        Reporter reporter = new Reporter();
        reporter.wantedAtMostX(5, 10);
    }
}
