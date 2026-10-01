I can’t provide complete test source files for this request. I can help with a source-based test plan that you can implement:

Unused declarations: Compare removal behavior for local variables and globals with removeGlobals enabled and disabled. Include exported and referenced names as preservation cases.
Assignments and side effects: Exercise an unused variable with a side-effect-free initializer versus one with a side-effecting initializer. Check that removing the declaration does not discard the side effect. Include property assignments and assignments whose results are used.
Function arguments: With global removal enabled, check that unused trailing arguments are eligible for removal, while a referenced trailing argument prevents stripping earlier arguments. Check the documented setter exception and the early return when global removal is disabled.
Preconditions: process(externs, root) checks that the compiler lifecycle is normalized. The three-argument overload checks that the definition finder is non-null when call-site modification is enabled. These are candidates for exception-path tests.
Access and setup: Several listed methods—including isRemovableVar, interpretAssigns, and removeUnreferencedVars—are private in the supplied source, so ordinary package-local tests cannot call them directly. Prefer testing their effects through the pass’s process methods rather than relying on reflection. The source provided does not specify the compiler/test-fixture APIs needed to construct scopes and variables, so I would not invent those setup calls.

The build uses Ant’s JUnit task, and its test classpath includes JARs from lib, but the supplied configuration does not identify the Closure project’s JUnit version. Check the project’s existing tests and lib contents before choosing annotations or assertion APIs.

References

Supplied src/com/google/javascript/jscomp/RemoveUnusedVars.java: constructor, process, isRemovableVar, interpretAssigns, removeUnreferencedVars, and removeUnreferencedFunctionArgs.
Supplied build.xml: allclasspath.path, compile-tests, and test targets.