Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Mockito; fixed revision: 1f.
Modified target classes:
org.mockito.internal.invocation.InvocationMatcher

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "",
    "method": "createFrom",
    "parameter_types": "java.util.List"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "captureArgumentsFrom",
    "parameter_types": "org.mockito.invocation.Invocation"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getInvocation",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getLocation",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getMatchers",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getMethod",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "hasSameMethod",
    "parameter_types": "org.mockito.invocation.Invocation"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "hasSimilarMethod",
    "parameter_types": "org.mockito.invocation.Invocation"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "isVarargMatcher",
    "parameter_types": "org.hamcrest.Matcher"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "isVariableArgument",
    "parameter_types": "org.mockito.invocation.Invocation,int"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "matches",
    "parameter_types": "org.mockito.invocation.Invocation"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "safelyArgumentsMatch",
    "parameter_types": "[Ljava.lang.Object;"
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "toString",
    "parameter_types": ""
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation,java.util.List",
    "method": "<init>",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.mockito.AdditionalAnswers",
  "org.mockito.AdditionalMatchers",
  "org.mockito.Answers",
  "org.mockito.ArgumentCaptor",
  "org.mockito.ArgumentMatcher",
  "org.mockito.BDDMockito",
  "org.mockito.Captor",
  "org.mockito.InOrder",
  "org.mockito.Incubating",
  "org.mockito.InjectMocks",
  "org.mockito.Matchers",
  "org.mockito.Mock",
  "org.mockito.MockSettings",
  "org.mockito.MockingDetails",
  "org.mockito.Mockito",
  "org.mockito.MockitoAnnotations",
  "org.mockito.MockitoDebugger",
  "org.mockito.ReturnValues",
  "org.mockito.Spy",
  "org.mockito.configuration.AnnotationEngine",
  "org.mockito.configuration.DefaultMockitoConfiguration",
  "org.mockito.configuration.IMockitoConfiguration",
  "org.mockito.exceptions.Discrepancy",
  "org.mockito.exceptions.Pluralizer",
  "org.mockito.exceptions.PrintableInvocation",
  "org.mockito.exceptions.Reporter",
  "org.mockito.exceptions.base.MockitoAssertionError",
  "org.mockito.exceptions.base.MockitoException",
  "org.mockito.exceptions.base.MockitoSerializationIssue",
  "org.mockito.exceptions.misusing.CannotStubVoidMethodWithReturnValue",
  "org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock",
  "org.mockito.exceptions.misusing.FriendlyReminderException",
  "org.mockito.exceptions.misusing.InvalidUseOfMatchersException",
  "org.mockito.exceptions.misusing.MissingMethodInvocationException",
  "org.mockito.exceptions.misusing.MockitoConfigurationException",
  "org.mockito.exceptions.misusing.NotAMockException",
  "org.mockito.exceptions.misusing.NullInsteadOfMockException",
  "org.mockito.exceptions.misusing.UnfinishedStubbingException",
  "org.mockito.exceptions.misusing.UnfinishedVerificationException",
  "org.mockito.exceptions.misusing.WrongTypeOfReturnValue",
  "org.mockito.exceptions.stacktrace.StackTraceCleaner",
  "org.mockito.exceptions.verification.ArgumentsAreDifferent",
  "org.mockito.exceptions.verification.NeverWantedButInvoked",
  "org.mockito.exceptions.verification.NoInteractionsWanted",
  "org.mockito.exceptions.verification.SmartNullPointerException",
  "org.mockito.exceptions.verification.TooLittleActualInvocations",
  "org.mockito.exceptions.verification.TooManyActualInvocations",
  "org.mockito.exceptions.verification.VerificationInOrderFailure",
  "org.mockito.exceptions.verification.WantedButNotInvoked",
  "org.mockito.exceptions.verification.junit.ArgumentsAreDifferent",
  "org.mockito.exceptions.verification.junit.JUnitTool",
  "org.mockito.internal.InOrderImpl",
  "org.mockito.internal.InternalMockHandler",
  "org.mockito.internal.MockitoCore",
  "org.mockito.internal.configuration.CaptorAnnotationProcessor",
  "org.mockito.internal.configuration.ClassPathLoader",
  "org.mockito.internal.configuration.DefaultAnnotationEngine",
  "org.mockito.internal.configuration.DefaultInjectionEngine",
  "org.mockito.internal.configuration.FieldAnnotationProcessor",
  "org.mockito.internal.configuration.GlobalConfiguration",
  "org.mockito.internal.configuration.InjectingAnnotationEngine",
  "org.mockito.internal.configuration.MockAnnotationProcessor",
  "org.mockito.internal.configuration.MockitoAnnotationsMockAnnotationProcessor",
  "org.mockito.internal.configuration.SpyAnnotationEngine",
  "org.mockito.internal.configuration.injection.ConstructorInjection",
  "org.mockito.internal.configuration.injection.MockInjection",
  "org.mockito.internal.configuration.injection.MockInjectionStrategy",
  "org.mockito.internal.configuration.injection.PropertyAndSetterInjection",
  "org.mockito.internal.configuration.injection.SpyOnInjectedFieldsHandler",
  "org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter",
  "org.mockito.internal.configuration.injection.filter.MockCandidateFilter",
  "org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter",
  "org.mockito.internal.configuration.injection.filter.OngoingInjecter",
  "org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter",
  "org.mockito.internal.configuration.injection.scanner.InjectMocksScanner",
  "org.mockito.internal.configuration.injection.scanner.MockScanner",
  "org.mockito.internal.configuration.plugins.DefaultPluginSwitch",
  "org.mockito.internal.configuration.plugins.PluginFileReader",
  "org.mockito.internal.configuration.plugins.PluginFinder",
  "org.mockito.internal.configuration.plugins.PluginLoader",
  "org.mockito.internal.configuration.plugins.PluginRegistry",
  "org.mockito.internal.configuration.plugins.Plugins",
  "org.mockito.internal.creation.DelegatingMethod",
  "org.mockito.internal.creation.MockSettingsImpl",
  "org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport",
  "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker",
  "org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator",
  "org.mockito.internal.creation.bytebuddy.InterceptedInvocation",
  "org.mockito.internal.creation.bytebuddy.MockBytecodeGenerator",
  "org.mockito.internal.creation.bytebuddy.MockFeatures",
  "org.mockito.internal.creation.bytebuddy.MockMethodInterceptor",
  "org.mockito.internal.creation.instance.ConstructorInstantiator",
  "org.mockito.internal.creation.instance.InstantiationException",
  "org.mockito.internal.creation.instance.Instantiator",
  "org.mockito.internal.creation.instance.InstantiatorProvider",
  "org.mockito.internal.creation.instance.ObjenesisInstantiator",
  "org.mockito.internal.creation.settings.CreationSettings",
  "org.mockito.internal.creation.util.MockitoMethodProxy",
  "org.mockito.internal.creation.util.SearchingClassLoader",
  "org.mockito.internal.debugging.FindingsListener",
  "org.mockito.internal.debugging.Localized",
  "org.mockito.internal.debugging.LocationImpl",
  "org.mockito.internal.debugging.LoggingListener",
  "org.mockito.internal.debugging.MockitoDebuggerImpl",
  "org.mockito.internal.debugging.VerboseMockInvocationLogger",
  "org.mockito.internal.debugging.WarningsCollector",
  "org.mockito.internal.debugging.WarningsFinder",
  "org.mockito.internal.debugging.WarningsPrinterImpl",
  "org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings",
  "org.mockito.internal.exceptions.MockitoLimitations",
  "org.mockito.internal.exceptions.VerificationAwareInvocation",
  "org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter",
  "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner",
  "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider",
  "org.mockito.internal.exceptions.stacktrace.StackTraceFilter",
  "org.mockito.internal.exceptions.util.ScenarioPrinter",
  "org.mockito.internal.handler.InvocationNotifierHandler",
  "org.mockito.internal.handler.MockHandlerFactory",
  "org.mockito.internal.handler.MockHandlerImpl",
  "org.mockito.internal.handler.NullResultGuardian",
  "org.mockito.internal.invocation.AbstractAwareMethod",
  "org.mockito.internal.invocation.ArgumentsComparator",
  "org.mockito.internal.invocation.ArgumentsProcessor",
  "org.mockito.internal.invocation.CapturesArgumensFromInvocation",
  "org.mockito.internal.invocation.InvocationImpl",
  "org.mockito.internal.invocation.InvocationMarker",
  "org.mockito.internal.invocation.InvocationMatcher",
  "org.mockito.internal.invocation.InvocationsFinder",
  "org.mockito.internal.invocation.MatchersBinder",
  "org.mockito.internal.invocation.MockitoMethod",
  "org.mockito.internal.invocation.SerializableMethod",
  "org.mockito.internal.invocation.StubInfoImpl",
  "org.mockito.internal.invocation.UnusedStubsFinder",
  "org.mockito.internal.invocation.finder.AllInvocationsFinder",
  "org.mockito.internal.invocation.finder.VerifiableInvocationsFinder",
  "org.mockito.internal.invocation.realmethod.CleanTraceRealMethod",
  "org.mockito.internal.invocation.realmethod.DefaultRealMethod",
  "org.mockito.internal.invocation.realmethod.RealMethod",
  "org.mockito.internal.junit.FriendlyExceptionMaker",
  "org.mockito.internal.junit.JUnitDetecter",
  "org.mockito.internal.junit.JUnitRule",
  "org.mockito.internal.junit.JUnitTool",
  "org.mockito.internal.listeners.CollectCreatedMocks",
  "org.mockito.internal.listeners.MockingProgressListener",
  "org.mockito.internal.listeners.MockingStartedListener",
  "org.mockito.internal.listeners.NotifiedMethodInvocationReport",
  "org.mockito.internal.matchers.And",
  "org.mockito.internal.matchers.Any",
  "org.mockito.internal.matchers.AnyVararg",
  "org.mockito.internal.matchers.ArrayEquals",
  "org.mockito.internal.matchers.CapturesArguments",
  "org.mockito.internal.matchers.CapturingMatcher",
  "org.mockito.internal.matchers.CompareEqual",
  "org.mockito.internal.matchers.CompareTo",
  "org.mockito.internal.matchers.Contains",
  "org.mockito.internal.matchers.ContainsExtraTypeInformation",
  "org.mockito.internal.matchers.EndsWith",
  "org.mockito.internal.matchers.Equality",
  "org.mockito.internal.matchers.Equals",
  "org.mockito.internal.matchers.EqualsWithDelta",
  "org.mockito.internal.matchers.Find",
  "org.mockito.internal.matchers.GreaterOrEqual",
  "org.mockito.internal.matchers.GreaterThan",
  "org.mockito.internal.matchers.InstanceOf",
  "org.mockito.internal.matchers.LessOrEqual",
  "org.mockito.internal.matchers.LessThan",
  "org.mockito.internal.matchers.LocalizedMatcher",
  "org.mockito.internal.matchers.MatcherDecorator",
  "org.mockito.internal.matchers.MatchersPrinter",
  "org.mockito.internal.matchers.Matches",
  "org.mockito.internal.matchers.Not",
  "org.mockito.internal.matchers.NotNull",
  "org.mockito.internal.matchers.Null",
  "org.mockito.internal.matchers.Or",
  "org.mockito.internal.matchers.Same",
  "org.mockito.internal.matchers.StartsWith",
  "org.mockito.internal.matchers.VarargCapturingMatcher",
  "org.mockito.internal.matchers.VarargMatcher",
  "org.mockito.internal.matchers.apachecommons.EqualsBuilder",
  "org.mockito.internal.matchers.apachecommons.ReflectionEquals",
  "org.mockito.internal.progress.ArgumentMatcherStorage",
  "org.mockito.internal.progress.ArgumentMatcherStorageImpl",
  "org.mockito.internal.progress.HandyReturnValues",
  "org.mockito.internal.progress.IOngoingStubbing",
  "org.mockito.internal.progress.MockingProgress",
  "org.mockito.internal.progress.MockingProgressImpl",
  "org.mockito.internal.progress.SequenceNumber",
  "org.mockito.internal.progress.ThreadSafeMockingProgress",
  "org.mockito.internal.reporting.Discrepancy",
  "org.mockito.internal.reporting.Pluralizer",
  "org.mockito.internal.reporting.PrintSettings",
  "org.mockito.internal.reporting.SmartPrinter",
  "org.mockito.internal.runners.JUnit44RunnerImpl",
  "org.mockito.internal.runners.JUnit45AndHigherRunnerImpl",
  "org.mockito.internal.runners.RunnerFactory",
  "org.mockito.internal.runners.RunnerImpl",
  "org.mockito.internal.runners.util.FrameworkUsageValidator",
  "org.mockito.internal.runners.util.RunnerProvider",
  "org.mockito.internal.runners.util.TestMethodsFinder",
  "org.mockito.internal.stubbing.BaseStubbing",
  "org.mockito.internal.stubbing.ConsecutiveStubbing",
  "org.mockito.internal.stubbing.InvocationContainer",
  "org.mockito.internal.stubbing.InvocationContainerImpl",
  "org.mockito.internal.stubbing.OngoingStubbingImpl",
  "org.mockito.internal.stubbing.StubbedInvocationMatcher",
  "org.mockito.internal.stubbing.StubberImpl",
  "org.mockito.internal.stubbing.VoidMethodStubbableImpl",
  "org.mockito.internal.stubbing.answers.AnswerReturnValuesAdapter",
  "org.mockito.internal.stubbing.answers.AnswersValidator",
  "org.mockito.internal.stubbing.answers.CallsRealMethods",
  "org.mockito.internal.stubbing.answers.ClonesArguments",
  "org.mockito.internal.stubbing.answers.DoesNothing",
  "org.mockito.internal.stubbing.answers.MethodInfo",
  "org.mockito.internal.stubbing.answers.Returns",
  "org.mockito.internal.stubbing.answers.ReturnsArgumentAt",
  "org.mockito.internal.stubbing.answers.ReturnsElementsOf",
  "org.mockito.internal.stubbing.answers.ThrowsException",
  "org.mockito.internal.stubbing.answers.ThrowsExceptionClass",
  "org.mockito.internal.stubbing.defaultanswers.Answers",
  "org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations",
  "org.mockito.internal.stubbing.defaultanswers.GloballyConfiguredAnswer",
  "org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs",
  "org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues",
  "org.mockito.internal.stubbing.defaultanswers.ReturnsMocks",
  "org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues",
  "org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls",
  "org.mockito.internal.util.Checks",
  "org.mockito.internal.util.ConsoleMockitoLogger",
  "org.mockito.internal.util.Decamelizer",
  "org.mockito.internal.util.DefaultMockingDetails",
  "org.mockito.internal.util.MockCreationValidator",
  "org.mockito.internal.util.MockNameImpl",
  "org.mockito.internal.util.MockUtil",
  "org.mockito.internal.util.MockitoLogger",
  "org.mockito.internal.util.ObjectMethodsGuru",
  "org.mockito.internal.util.Primitives",
  "org.mockito.internal.util.RemoveFirstLine",
  "org.mockito.internal.util.SimpleMockitoLogger",
  "org.mockito.internal.util.StringJoiner",
  "org.mockito.internal.util.Timer",
  "org.mockito.internal.util.collections.ArrayUtils",
  "org.mockito.internal.util.collections.HashCodeAndEqualsMockWrapper",
  "org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet",
  "org.mockito.internal.util.collections.IdentitySet",
  "org.mockito.internal.util.collections.Iterables",
  "org.mockito.internal.util.collections.ListUtil",
  "org.mockito.internal.util.collections.Sets",
  "org.mockito.internal.util.io.IOUtil",
  "org.mockito.internal.util.junit.JUnitFailureHacker",
  "org.mockito.internal.util.reflection.AccessibilityChanger",
  "org.mockito.internal.util.reflection.BeanPropertySetter",
  "org.mockito.internal.util.reflection.Constructors",
  "org.mockito.internal.util.reflection.FieldCopier",
  "org.mockito.internal.util.reflection.FieldInitializationReport",
  "org.mockito.internal.util.reflection.FieldInitializer",
  "org.mockito.internal.util.reflection.FieldReader",
  "org.mockito.internal.util.reflection.FieldSetter",
  "org.mockito.internal.util.reflection.Fields",
  "org.mockito.internal.util.reflection.GenericMaster",
  "org.mockito.internal.util.reflection.GenericMetadataSupport",
  "org.mockito.internal.util.reflection.InstanceField",
  "org.mockito.internal.util.reflection.LenientCopyTool",
  "org.mockito.internal.util.reflection.SuperTypesLastSorter",
  "org.mockito.internal.util.reflection.Whitebox",
  "org.mockito.internal.verification.AtLeast",
  "org.mockito.internal.verification.AtMost",
  "org.mockito.internal.verification.Calls",
  "org.mockito.internal.verification.DefaultRegisteredInvocations",
  "org.mockito.internal.verification.InOrderContextImpl",
  "org.mockito.internal.verification.InOrderWrapper",
  "org.mockito.internal.verification.MockAwareVerificationMode",
  "org.mockito.internal.verification.NoMoreInteractions",
  "org.mockito.internal.verification.Only",
  "org.mockito.internal.verification.RegisteredInvocations",
  "org.mockito.internal.verification.SingleRegisteredInvocation",
  "org.mockito.internal.verification.Times",
  "org.mockito.internal.verification.VerificationDataImpl",
  "org.mockito.internal.verification.VerificationModeFactory",
  "org.mockito.internal.verification.VerificationOverTimeImpl",
  "org.mockito.internal.verification.api.InOrderContext",
  "org.mockito.internal.verification.api.VerificationData",
  "org.mockito.internal.verification.api.VerificationDataInOrder",
  "org.mockito.internal.verification.api.VerificationDataInOrderImpl",
  "org.mockito.internal.verification.api.VerificationInOrderMode",
  "org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool",
  "org.mockito.internal.verification.checkers.AtLeastDiscrepancy",
  "org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker",
  "org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsInOrderChecker",
  "org.mockito.internal.verification.checkers.MissingInvocationChecker",
  "org.mockito.internal.verification.checkers.MissingInvocationInOrderChecker",
  "org.mockito.internal.verification.checkers.NonGreedyNumberOfInvocationsInOrderChecker",
  "org.mockito.internal.verification.checkers.NumberOfInvocationsChecker",
  "org.mockito.internal.verification.checkers.NumberOfInvocationsInOrderChecker",
  "org.mockito.invocation.DescribedInvocation",
  "org.mockito.invocation.Invocation",
  "org.mockito.invocation.InvocationOnMock",
  "org.mockito.invocation.Location",
  "org.mockito.invocation.MockHandler",
  "org.mockito.invocation.StubInfo",
  "org.mockito.junit.MockitoJUnit",
  "org.mockito.junit.MockitoJUnitRule",
  "org.mockito.junit.MockitoRule",
  "org.mockito.listeners.InvocationListener",
  "org.mockito.listeners.MethodInvocationReport",
  "org.mockito.mock.MockCreationSettings",
  "org.mockito.mock.MockName",
  "org.mockito.mock.SerializableMode",
  "org.mockito.plugins.MockMaker",
  "org.mockito.plugins.PluginSwitch",
  "org.mockito.plugins.StackTraceCleanerProvider",
  "org.mockito.runners.ConsoleSpammingMockitoJUnitRunner",
  "org.mockito.runners.MockitoJUnit44Runner",
  "org.mockito.runners.MockitoJUnitRunner",
  "org.mockito.runners.VerboseMockitoJUnitRunner",
  "org.mockito.stubbing.Answer",
  "org.mockito.stubbing.DeprecatedOngoingStubbing",
  "org.mockito.stubbing.OngoingStubbing",
  "org.mockito.stubbing.Stubber",
  "org.mockito.stubbing.VoidMethodStubbable",
  "org.mockito.stubbing.answers.ReturnsElementsOf",
  "org.mockito.verification.After",
  "org.mockito.verification.Timeout",
  "org.mockito.verification.VerificationAfterDelay",
  "org.mockito.verification.VerificationMode",
  "org.mockito.verification.VerificationWithTimeout",
  "org.mockito.verification.VerificationWrapper"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

## build.gradle

```
buildscript {
    repositories {
        maven { url "/home/aomsin/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }

    }

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // coveralls plugin depends on cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.6.1'
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.2' //publishing to bintray
        classpath 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2' //rest calls to bintray api
    }

    configurations.classpath.exclude group: 'com.android.tools.build', module: 'gradle'
}

apply plugin: 'maven-publish'

apply from: 'gradle/version.gradle'
apply from: "gradle/ide.gradle"
apply from: 'gradle/coverage.gradle'

allprojects {
    repositories {
        maven { url "/home/aomsin/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }

    }
}

group = 'org.mockito'
description = 'Core API and implementation.'
sourceCompatibility=1.6
targetCompatibility=1.6

configurations {
    provided
    testUtil //TODO move to separate project
}

sourceSets {
    main {
        java.srcDirs 'src', 'mockmaker/bytebuddy/main/java'
        compileClasspath = compileClasspath + configurations.provided
    }
    test {
        java.srcDirs 'test', 'mockmaker/bytebuddy/test/java'
        compileClasspath = compileClasspath + configurations.provided
    }
}

test {
    include "**/*Test.class"
    testLogging {
        exceptionFormat 'full'
        showCauses true
    }
}

tasks.withType(JavaCompile) {
    options.warnings = false
}

//TODO we should remove all dependencies to checked-in jars
dependencies {
    compile 'net.bytebuddy:byte-buddy:0.6.8'

    provided "junit:junit:4.10"
    compile "org.hamcrest:hamcrest-core:1.1", "org.objenesis:objenesis:2.1"

    testCompile 'org.ow2.asm:asm:5.0.4'
    testCompile fileTree("lib/test")
    testRuntime configurations.provided

    testUtil sourceSets.test.output
}

def licenseFiles = copySpec {
    //mockito license
    from(".") { include 'LICENSE', 'NOTICE' }
    //repackaged license
}

task sourcesJar(type: Jar) {
    jar {
        baseName = 'mockito-core'
        from(sourceSets.main.allSource)
        with licenseFiles

    }
    baseName = 'mockito-core'
    from(sourceSets.main.allSource)
    classifier = "sources"
    with licenseFiles
}

apply from: 'gradle/javadoc.gradle'
task javadocJar(type: Jar) {
    baseName = 'mockito-core'
    classifier = "javadoc"
    with licenseFiles
    from mockitoJavadoc
}

def antCommand = "ant"

if (System.getProperty("os.name").startsWith("Windows")) {
    antCommand += ".bat"
}

jar { task ->
    task.rootSpec.exclude "MANIFEST.MF" //hack to avoid problems with bnd
    doLast {
        project.exec {
            commandLine antCommand, '-f', 'build-ant.xml', "osgify.$task.baseName", "-Dversion=$project.version"
        }
    }
}

artifacts {
    archives sourcesJar
    archives javadocJar
}

publishing {
    publications {
        mockitoCore(MavenPublication) {
            from components.java
            artifactId 'mockito-core'
            artifact sourcesJar
            artifact javadocJar
        }
    }
}

apply from: 'gradle/release.gradle'
apply from: "gradle/pom.gradle"

task wrapper(type: Wrapper) {
    gradleVersion = '2.4'
}

task ciBuild {
    //validate the state of the project
    dependsOn build, publishToMavenLocal, tasks.idea, tasks.eclipse
}

```

## gradle.properties

```
org.gradle.daemon=false
org.gradle.parallel=true
```

## settings.gradle

```
include 'testng'
include 'extTest'

rootProject.name = 'mockito'

rootProject.children.each { project ->
    String projectDirName = "subprojects/$project.name"
    project.projectDir = new File(settingsDir, projectDirName)
    project.buildFileName = "${project.name}.gradle"
    assert project.projectDir.isDirectory()
    assert project.buildFile.isFile()
}
```

## src/org/mockito/internal/invocation/InvocationMatcher.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcher implements DescribedInvocation, CapturesArgumensFromInvocation, Serializable {

    private static final long serialVersionUID = -3047126096857467610L;
    private final Invocation invocation;
    private final List<Matcher> matchers;

    public InvocationMatcher(Invocation invocation, List<Matcher> matchers) {
        this.invocation = invocation;
        if (matchers.isEmpty()) {
            this.matchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        } else {
            this.matchers = matchers;
        }
    }
    
    public InvocationMatcher(Invocation invocation) {
        this(invocation, Collections.<Matcher>emptyList());
    }

    public Method getMethod() {
        return invocation.getMethod();
    }
    
    public Invocation getInvocation() {
        return this.invocation;
    }
    
    public List<Matcher> getMatchers() {
        return this.matchers;
    }
    
    public String toString() {
        return new PrintSettings().print(matchers, invocation);
    }

    public boolean matches(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(this, actual);
    }

    private boolean safelyArgumentsMatch(Object[] actualArgs) {
        try {
            return new ArgumentsComparator().argumentsMatch(this, actualArgs);
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * similar means the same method name, same mock, unverified 
     * and: if arguments are the same cannot be overloaded
     */
    public boolean hasSimilarMethod(Invocation candidate) {
        String wantedMethodName = getMethod().getName();
        String currentMethodName = candidate.getMethod().getName();
        
        final boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
        final boolean isUnverified = !candidate.isVerified();
        final boolean mockIsTheSame = getInvocation().getMock() == candidate.getMock();
        final boolean methodEquals = hasSameMethod(candidate);

        if (!methodNameEquals || !isUnverified || !mockIsTheSame) {
            return false;
        }

        final boolean overloadedButSameArgs = !methodEquals && safelyArgumentsMatch(candidate.getArguments());

        return !overloadedButSameArgs;
    }

    public boolean hasSameMethod(Invocation candidate) {
        //not using method.equals() for 1 good reason:
        //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();
        
        if (m1.getName() != null && m1.getName().equals(m2.getName())) {
            /* Avoid unnecessary cloning */
            Class[] params1 = m1.getParameterTypes();
            Class[] params2 = m2.getParameterTypes();
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i])
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    public Location getLocation() {
        return invocation.getLocation();
    }

    public void captureArgumentsFrom(Invocation invocation) {
        if (invocation.getMethod().isVarArgs()) {
            int indexOfVararg = invocation.getRawArguments().length - 1;
            for (int position = 0; position < indexOfVararg; position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments) {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
            for (int position = indexOfVararg; position < matchers.size(); position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments) {
                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position - indexOfVararg]);
                }
            }

        } else {
            for (int position = 0; position < matchers.size(); position++) {
                Matcher m = matchers.get(position);
                if (m instanceof CapturesArguments) {
                    ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
                }
            }
        }

//        for (int position = 0; position < matchers.size(); position++) {
//            Matcher m = matchers.get(position);
//            if (m instanceof CapturesArguments && invocation.getRawArguments().length > position) {
//                //TODO SF - this whole lot can be moved captureFrom implementation
//                if(isVariableArgument(invocation, position) && isVarargMatcher(m)) {
//                    Object array = invocation.getRawArguments()[position];
//                    for (int i = 0; i < Array.getLength(array); i++) {
//                        ((CapturesArguments) m).captureFrom(Array.get(array, i));
//                    }
//                    //since we've captured all varargs already, it does not make sense to process other matchers.
//                    return;
//                } else {
//                    ((CapturesArguments) m).captureFrom(invocation.getRawArguments()[position]);
//                }
//            }
//        }
    }

    private boolean isVarargMatcher(Matcher matcher) {
        Matcher actualMatcher = matcher;
        if (actualMatcher instanceof MatcherDecorator) {
            actualMatcher = ((MatcherDecorator) actualMatcher).getActualMatcher();
        }
        return actualMatcher instanceof VarargMatcher;
    }

    private boolean isVariableArgument(Invocation invocation, int position) {
        return invocation.getRawArguments().length - 1 == position
                && invocation.getRawArguments()[position] != null
                && invocation.getRawArguments()[position].getClass().isArray()
                && invocation.getMethod().isVarArgs();
    }

    public static List<InvocationMatcher> createFrom(List<Invocation> invocations) {
        LinkedList<InvocationMatcher> out = new LinkedList<InvocationMatcher>();

        for (Invocation i : invocations) {
            out.add(new InvocationMatcher(i));
        }

        return out;
    }
}

```
