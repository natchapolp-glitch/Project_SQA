Generate a JUnit 4 Java test suite using only the supplied fixed source and build context.
Project: Mockito; fixed revision: 1f.
Candidate target classes:
org.mockito.internal.invocation.InvocationMatcher

Return Java source only, with explicit package declarations and JUnit 4 imports. Create at most 30 @Test methods in source order, with deterministic inputs and meaningful assertions derived from the fixed API behavior. Avoid external services, clock dependence, randomness, empty tests, and assertions that merely check non-null. Do not modify production code or create shadow production classes. Preserve all assertions; no feedback or repair loop.

Use the provided candidate classes; target eligibility and fixtures are specified by the shared adapter contract before generation.

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
