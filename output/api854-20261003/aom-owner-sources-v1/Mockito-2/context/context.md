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

    configurations.classpath.exclude group: 'com.android.tools.build', module: 'gradle'
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

## src/org/mockito/internal/util/Timer.java

```
package org.mockito.internal.util;

import org.mockito.exceptions.Reporter;

public class Timer {

    private final long durationMillis;
    private long startTime = -1;

    public Timer(long durationMillis) {
        validateInput(durationMillis);
        this.durationMillis = durationMillis;
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        assert startTime != -1;
        return System.currentTimeMillis() - startTime <= durationMillis;
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    private void validateInput(long durationMillis) {
        if (durationMillis < 0) {
            new Reporter().cannotCreateTimerWithNegativeDurationTime(durationMillis);
        }
    }
}

```
