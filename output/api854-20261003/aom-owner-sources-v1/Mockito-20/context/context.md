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
    compile 'net.bytebuddy:byte-buddy:0.2.1'

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

## src/org/mockito/internal/creation/bytebuddy/ByteBuddyMockMaker.java

```
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringJoiner.join;
import java.lang.reflect.Constructor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMaker implements MockMaker {

    private final ClassInstantiator classInstantiator;
    private final CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    public ByteBuddyMockMaker() {
        classInstantiator = initializeClassInstantiator();
        cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator();
    }

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
            throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
        }
        Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
                settings.getTypeToMock(),
                settings.getExtraInterfaces()
        );
        Instantiator instantiator = new InstantiatorProvider().getInstantiator(settings);
        T mockInstance = null;
        try {
            mockInstance = instantiator.newInstance(mockedProxyType);
            MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } catch (ClassCastException cce) {
            throw new MockitoException(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : " + describeClass(mockedProxyType),
                    "  created class : " + describeClass(settings.getTypeToMock()),
                    "  proxy instance class : " + describeClass(mockInstance),
                    "  instance creation by : " + instantiator.getClass().getSimpleName(),
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ),cce);
        } catch (org.mockito.internal.creation.instance.InstantiationException e) {
            throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
        }
    }

    private <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
        // Force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site.
        // This allows us to catch the ClassCastException earlier
        Class<T> typeToMock = settings.getTypeToMock();
        return typeToMock.cast(mock);
    }

    private static String describeClass(Class type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MockMethodInterceptor.MockAccess)) {
            return null;
        }
        return ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(newHandler), settings)
        );
    }

    private static ClassInstantiator initializeClassInstantiator() {
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
            return ClassInstantiator.class.cast(usingClassCacheConstructor.newInstance(new GlobalConfiguration().enableClassCache()));
        } catch (Throwable throwable) {
            // MockitoException cannot be used at this point as we are early in the classloading chain and necessary dependencies may not yet be loadable by the classloader
            throw new IllegalStateException(join(
                    "Mockito could not create mock: Objenesis is missing on the classpath.",
                    "Please add Objenesis on the classpath.",
                    ""
            ), throwable);
        }
    }

    private static InternalMockHandler asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please see the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler) handler;
    }
}

```
