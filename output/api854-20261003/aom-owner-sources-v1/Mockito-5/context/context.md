## build.gradle

```
buildscript {
    repositories {
        maven { url "/home/team/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }

    }

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // coveralls plugin depends on cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.6.1'
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0' //publishing to bintray
        classpath 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2' //rest calls to bintray api
    }
}

apply plugin: 'maven-publish'

apply from: 'gradle/version.gradle'
apply from: "gradle/ide.gradle"
apply from: 'gradle/coverage.gradle'

allprojects {
    repositories {
        maven { url "/home/team/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
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
        java.srcDir 'src'
        compileClasspath = compileClasspath + configurations.provided
    }
    test {
        java.srcDir 'test'
        compileClasspath = compileClasspath + configurations.provided
    }
}

test.include "**/*Test.class"

tasks.withType(JavaCompile) {
    options.warnings = false
}

//TODO we should remove all dependencies to checked-in jars
dependencies {
    provided "junit:junit:4.10"
    compile "org.hamcrest:hamcrest-core:1.1", "org.objenesis:objenesis:2.1"
    compile fileTree('lib/repackaged') { exclude '*.txt'}

    testCompile fileTree("lib/test")
    testRuntime configurations.provided

    testUtil sourceSets.test.output
}

def licenseFiles = copySpec {
    //mockito license
    from(".") { include 'LICENSE', 'NOTICE' }
    //repackaged license
    from("lib/repackaged") { include '*.txt' }
}

jar {
    baseName = 'mockito-core'
    from(sourceSets.main.allSource)
    from(zipTree("lib/repackaged/cglib-and-asm-1.0.jar")) {
        exclude 'META-INF/MANIFEST.MF'
    }
    with licenseFiles
}

task sourcesJar(type: Jar) {
    baseName = 'mockito-core'
    from(sourceSets.main.allSource)
    classifier = "sources"
    from(zipTree("lib/sources/cglib-and-asm-1.0-sources.jar"))
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
    archives sourcesJar, javadocJar
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
    gradleVersion = '2.0'
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

## src/org/mockito/internal/verification/VerificationOverTimeImpl.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Verifies that another verification mode (the delegate) is satisfied within a certain timeframe
 * (before timeoutMillis has passed, measured from the call to verify()), and either returns immediately
 * once it does, or waits until it is definitely satisfied once the full time has passed.
 */
public class VerificationOverTimeImpl implements VerificationMode {

    private final long pollingPeriodMillis;
    private final long durationMillis;
    private final VerificationMode delegate;
    private final boolean returnOnSuccess;
    private final Timer timer;

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param pollingPeriodMillis The frequency to poll delegate.verify(), to check whether the delegate has been satisfied
     * @param durationMillis The max time to wait (in millis) for the delegate verification mode to be satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     */
    public VerificationOverTimeImpl(long pollingPeriodMillis, long durationMillis, VerificationMode delegate, boolean returnOnSuccess) {
        this(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, new Timer(durationMillis));
    }

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param pollingPeriodMillis The frequency to poll delegate.verify(), to check whether the delegate has been satisfied
     * @param durationMillis The max time to wait (in millis) for the delegate verification mode to be satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     * @param timer Checker of whether the duration of the verification is still acceptable
     */
    public VerificationOverTimeImpl(long pollingPeriodMillis, long durationMillis, VerificationMode delegate, boolean returnOnSuccess, Timer timer) {
        this.pollingPeriodMillis = pollingPeriodMillis;
        this.durationMillis = durationMillis;
        this.delegate = delegate;
        this.returnOnSuccess = returnOnSuccess;
        this.timer = timer;
    }

    /**
     * Verify the given ongoing verification data, and confirm that it satisfies the delegate verification mode
     * before the full duration has passed.
     *
     * In practice, this polls the delegate verification mode until it is satisfied. If it is not satisfied once
     * the full duration has passed, the last error returned by the delegate verification mode will be thrown
     * here in turn. This may be thrown early if the delegate is unsatisfied and the verification mode is known
     * to never recover from this situation (e.g. {@link AtMost}).
     *
     * If it is satisfied before the full duration has passed, behaviour is dependent on the returnOnSuccess parameter
     * given in the constructor. If true, this verification mode is immediately satisfied once the delegate is. If
     * false, this verification mode is not satisfied until the delegate is satisfied and the full time has passed.
     *
     * @throws MockitoAssertionError if the delegate verification mode does not succeed before the timeout
     */
    public void verify(VerificationData data) {
        AssertionError error = null;

        timer.start();
        while (timer.isCounting()) {
            try {
                delegate.verify(data);

                if (returnOnSuccess) {
                    return;
                } else {
                    error = null;
                }
            } catch (MockitoAssertionError e) {
                error = handleVerifyException(e);
            }
            catch (AssertionError e) {
                error = handleVerifyException(e);
            }
        }

        if (error != null) {
            throw error;
        }
    }

    private AssertionError handleVerifyException(AssertionError e) {
        if (canRecoverFromFailure(delegate)) {
            sleep(pollingPeriodMillis);
            return e;
        } else {
            throw e;
        }
    }

    protected boolean canRecoverFromFailure(VerificationMode verificationMode) {
        return !(verificationMode instanceof AtMost || verificationMode instanceof NoMoreInteractions);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }

    public long getPollingPeriod() {
        return pollingPeriodMillis;
    }

    public long getDuration() {
        return durationMillis;
    }

    public VerificationMode getDelegate() {
        return delegate;
    }

}

```
