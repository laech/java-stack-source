# Stack Source

Decorates test failure stack traces with source code snippets to make them more helpful:

```java
org.junit.ComparisonFailure: expected:<H[ello]!> but was:<H[i]!>
	at org.junit.Assert.assertEquals(Assert.java:115)
	at example.HelloTest.hello(HelloTest.java:16)

	   14      @Test
	   15      public void hello() {
	-> 16          assertEquals("Hello!", greet());
	   17      }

    ...
```

## Usage

Requires Java 8+.

### JUnit 5

[![Javadocs](https://www.javadoc.io/badge/com.gitlab.lae.stack.source/stack-source-junit5.svg)](https://www.javadoc.io/doc/com.gitlab.lae.stack.source/stack-source-junit5)

```java
/* Alternatively, run your tests with
 * -Djunit.jupiter.extensions.autodetection.enabled=true
 * then you don't need to use @ExtendWith(ErrorDecorator.class)
 */

@ExtendWith(ErrorDecorator.class)
class BaseTest {}

class MyTest extends BaseTest {
  @Test
  void myTest() {
    // ...
  }
}
```

### JUnit 4

[![Javadocs](https://www.javadoc.io/badge/com.gitlab.lae.stack.source/stack-source-junit4.svg)](https://www.javadoc.io/doc/com.gitlab.lae.stack.source/stack-source-junit4)

```java
public class BaseTest {
  @Rule
  public final ErrorDecorator errorDecorator = new ErrorDecorator();
}

public final class MyTest extends BaseTest {
  @Test
  public void myTest() {
    // ...
  }
}
```

### Maven `pom.xml`

```xml
<project>
  <dependencies>

    <!--
      If you use JUnit 5
      You also need: https://junit.org/junit5/docs/current/user-guide/#running-tests-build-maven
    -->
    <dependency>
      <groupId>com.gitlab.lae.stack.source</groupId>
      <artifactId>stack-source-junit5</artifactId>
      <version>0.4.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.gitlab.lae.stack.source</groupId>
      <artifactId>stack-source-processor</artifactId>
      <version>0.4.1</version>
      <scope>test</scope>
    </dependency>

    <!--
      If you use JUnit 4
    -->
    <dependency>
      <groupId>com.gitlab.lae.stack.source</groupId>
      <artifactId>stack-source-junit4</artifactId>
      <version>0.4.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.gitlab.lae.stack.source</groupId>
      <artifactId>stack-source-processor</artifactId>
      <version>0.4.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <pluginManagement>
      <plugins>

        <!--
          Set <trimStackTrace> to false to prevent trimming
          of stack traces, therefore allowing decorated
          stack traces to be shown under build servers
          such as Jenkins. 
        -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
          <configuration>
            <trimStackTrace>false</trimStackTrace>
          </configuration>
        </plugin>

        <!--
          If you distribute your tests as a jar file,
          exclude all local meta data files under
          stack-source/**
        -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.1.0</version>
          <configuration>
          <excludes>
            <exclude>stack-source/**</exclude>
          </excludes>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```

### Gradle `build.gradle`

Gradle is not currently fully supported, when using Gradle you can see
the decorated stack traces in your IDE, but not when running Gradle from
the command line, this is due to Gradle writes it's own stack trace instead of
calling `Throwable.printStackTrace`. [#5203](https://github.com/gradle/gradle/issues/5203)

```groovy
dependencies {

  // If you use JUnit 4
  testCompile 'com.gitlab.lae.stack.source:stack-source-junit4:0.4.1'
  testAnnotationProcessor 'com.gitlab.lae.stack.source:stack-source-processor:0.4.1'

  // If you use JUnit 5
  // You also need https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle
  testCompile 'com.gitlab.lae.stack.source:stack-source-junit5:0.4.1'
  testAnnotationProcessor 'com.gitlab.lae.stack.source:stack-source-processor:0.4.1'
}

// If you are creating a test jar similar to the following,
// exclude the 'stack-source' folder
task testJar(type: Jar) {
  classifier = 'tests'
  from sourceSets.test.output
  exclude 'stack-source'
}
```

### IntelliJ IDEA

* With Maven this works out of the box when running tests inside IntelliJ.
* With Gradle you need to enable: Preferences | Build, Execution, Deployment
  | Build Tools | Gradle | Runner | Delegate IDE build/run actions to gradle.
  Currently running Gradle on the command line doesn't show the decorated
  stack trace, but running the tests in IntelliJ still shows decorated
  stack trace.

### Eclipse

Running tests within Eclipse don't show decorated stack traces, because Eclipse
uses its own compiler which is not supported by the Java Compiler Tree API.
However if the project was built by Maven before running the tests then it
would work.
