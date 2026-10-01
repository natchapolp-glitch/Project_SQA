The supplied build uses JUnit 4.10 and Java 1.6 source level, so the tests below use JUnit 4 annotations and no diamond operator, lambdas or try-with-resources. They also use Mockito itself as a test helper.

I could not see some supporting classes, so the tests rely on these Mockito 1.x APIs, which I recalled rather than checked against the supplied source:

CapturingMatcher (no-arg constructor, getAllValues())
Equals(Object)
MatcherDecorator.getActualMatcher()
VarargMatcher (marker interface)
the Invocation interface methods getMock, getMethod, getArguments,
