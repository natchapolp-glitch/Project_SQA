# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: Mockito
Fixed reference revision: 1f
Target classes:
- org.mockito.internal.invocation.InvocationMatcher

## Eligible shared API declarations

Target these declarations, which exist on both evaluation revisions. Only declaration signatures were checked; no buggy behavior was supplied. Generate at most 30 independent test methods per response.

```json
[
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "isVarargMatcher",
    "parameter_types": "org.hamcrest.Matcher",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "isVariableArgument",
    "parameter_types": "org.mockito.invocation.Invocation,int",
    "dimensions": 9
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "safelyArgumentsMatch",
    "parameter_types": "[Ljava.lang.Object;",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "hasSameMethod",
    "parameter_types": "org.mockito.invocation.Invocation",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "hasSimilarMethod",
    "parameter_types": "org.mockito.invocation.Invocation",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "matches",
    "parameter_types": "org.mockito.invocation.Invocation",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "toString",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getMethod",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getMatchers",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getInvocation",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "getLocation",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "",
    "method": "createFrom",
    "parameter_types": "java.util.List",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "captureArgumentsFrom",
    "parameter_types": "org.mockito.invocation.Invocation",
    "dimensions": 6
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 3
  },
  {
    "class": "org.mockito.internal.invocation.InvocationMatcher",
    "constructor_types": "org.mockito.invocation.Invocation,java.util.List",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 6
  }
]
```

## Production source src/org/mockito/internal/invocation/InvocationMatcher.java

```java
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

## Build configuration build.gradle

```text
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

## Build configuration buildSrc/build.gradle

```text
apply plugin: 'idea'
apply plugin: 'groovy'

repositories { maven { url "/home/aomsin/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }
 }

dependencies {
    compile gradleApi()
    //TODO SF use jcabi to edit issues after the release so that they have the milestone attached
    //compile "com.jcabi:jcabi-github:0.17"
    compile "com.googlecode.json-simple:json-simple:1.1.1@jar"
    testCompile("org.spockframework:spock-core:0.7-groovy-2.0") {
        exclude module: "groovy-all"
    }
    testCompile "cglib:cglib-nodep:2.2.2"
}

test {
    testLogging {
        exceptionFormat = 'full'
    }
}

if (gradle.parent && gradle.parent.startParameter.taskNames.any { it in ["ideaModule", "idea"] }) {
    build.dependsOn ideaModule
}

```

## Build configuration cglib-and-asm/build.gradle

```text
apply plugin: 'java'

version = 1.1
sourceCompatibility = 1.5
targetCompatibility = 1.5

sourceSets.main.java.srcDir("src")

repositories {
  flatDir dirs: 'lib'
}

dependencies {
  compile(':ant:1.7.0')
}

jar {
  baseName = 'cglib-and-asm'
  from "src"
}

task jarSources(type: Zip) {
    classifier = 'sources'
    from 'src', 'licenses'
    extension = 'jar'
}

task copyToMockito {
  copy {
    from jar.archivePath
    into "../lib/repackaged"
  }
  copy {
    from jarSources.archivePath
    into "../lib/sources"
  }
}

defaultTasks "clean", "build", "jarSources", "copyToMockito"
```

## Build configuration gradle.properties

```text
org.gradle.daemon=false
org.gradle.parallel=true
```

## Build configuration settings.gradle

```text
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
