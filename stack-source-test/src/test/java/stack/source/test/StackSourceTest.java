package stack.source.test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.function.Function;

import static java.lang.String.join;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public final class StackSourceTest extends StackSourceTestBase {

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{

                {new MultilineThrow(), join(lineSeparator(),
                        "stack.source.test.TestException: hello world",
                        "\tat stack.source.test.MultilineThrow.run(MultilineThrow.java:7)",
                        "",
                        "\t-> 7          throw new TestException(",
                        "\t   8                  \"hello world\"",
                        "\t   9          );",
                        "",
                        ""
                ), null},

                {new Chained(), join(lineSeparator(),
                        "stack.source.test.TestException: what?",
                        "\tat stack.source.test.Chained.fail(Chained.java:18)",
                        "\tat stack.source.test.Chained.run(Chained.java:10)",
                        "",
                        "\t    7          new Chained()",
                        "\t    8                  .nothing1()",
                        "\t    9                  .nothing2()",
                        "\t-> 10                  .fail(\"what?\")",
                        "\t   11                  .nothing3()",
                        "\t   12                  .fail(\"more?\")",
                        "\t   13                  .fail(\"and more?\")",
                        "\t   14                  .fail(\"and more more?\");",
                        "",
                        ""
                ), null},

                {new ThrowExceptionCreatedElseWhere(), join(lineSeparator(),
                        "stack.source.test.TestException: testing",
                        "\tat stack.source.test.ThrowExceptionCreatedElseWhere.run(ThrowExceptionCreatedElseWhere.java:8)",
                        "",
                        "\t    5      @Override",
                        "\t    6      public void run() {",
                        "\t    7          @SuppressWarnings(\"UnnecessaryLocalVariable\")",
                        "\t->  8          TestException test = new TestException(\"testing\");",
                        "\t    9          throw test;",
                        "\t   10      }",
                        "",
                        ""
                ), null},

                {new ThrowNewException(), join(lineSeparator(),
                        "stack.source.test.TestException: testing",
                        "\tat stack.source.test.ThrowNewException.run(ThrowNewException.java:7)",
                        "",
                        "\t   5      @Override",
                        "\t   6      public void run() {",
                        "\t-> 7          throw new TestException(\"testing\");",
                        "\t   8      }",
                        "",
                        ""
                ), null},

                {new ReturnFailure(), join(lineSeparator(),
                        "stack.source.test.TestException: testing",
                        "\tat stack.source.test.ReturnFailure.bye(ReturnFailure.java:15)",
                        "\tat stack.source.test.ReturnFailure.hi(ReturnFailure.java:11)",
                        "\tat stack.source.test.ReturnFailure.run(ReturnFailure.java:7)",
                        "",
                        "\t   5      @Override",
                        "\t   6      public void run() {",
                        "\t-> 7          System.err.println(hi());",
                        "\t   8      }",
                        "",
                        ""
                ), null},
                {new MultiCalls(), join(lineSeparator(),
                        "stack.source.test.TestException: bob",
                        "\tat stack.source.test.MultiCalls.assertString(MultiCalls.java:12)",
                        "\tat stack.source.test.MultiCalls.lambda$null$0(MultiCalls.java:21)",
                        "\tat java.util.Optional.ifPresent(xxx)",
                        "\tat stack.source.test.MultiCalls.lambda$assertList$1(MultiCalls.java:17)",
                        "\tat java.util.Arrays$ArrayList.forEach(xxx)",
                        "\tat stack.source.test.MultiCalls.assertList(MultiCalls.java:17)",
                        "\tat stack.source.test.MultiCalls.run(MultiCalls.java:27)",
                        "",
                        "\t   25      @Override",
                        "\t   26      public void run() {",
                        "\t-> 27          assertList(asList(\"bob\", \"bob\"));",
                        "\t   28          assertList(asList(\"abc\", \"def\"));",
                        "\t   29      }",
                        "",
                        ""
                ), (Function<String, String>) s -> s
                        // Temporary hack to make this work on Java 9 too
                        .replaceAll("java.base/", "")
                        .replaceAll("Optional\\.java:\\d+", "xxx")
                        .replaceAll("Arrays\\.java:\\d+", "xxx")
                        .replace("lambda$assertList$0", "lambda$null$0")},

                {new DeepCalls(), join(lineSeparator(),
                        "stack.source.test.TestException: test",
                        "\tat stack.source.test.DeepCalls.call9(DeepCalls.java:43)",
                        "\tat stack.source.test.DeepCalls.call8(DeepCalls.java:39)",
                        "\tat stack.source.test.DeepCalls.call7(DeepCalls.java:35)",
                        "\tat stack.source.test.DeepCalls.call6(DeepCalls.java:31)",
                        "\tat stack.source.test.DeepCalls.call5(DeepCalls.java:27)",
                        "\tat stack.source.test.DeepCalls.call4(DeepCalls.java:23)",
                        "\tat stack.source.test.DeepCalls.call3(DeepCalls.java:19)",
                        "\tat stack.source.test.DeepCalls.call2(DeepCalls.java:15)",
                        "\tat stack.source.test.DeepCalls.call1(DeepCalls.java:11)",
                        "\tat stack.source.test.DeepCalls.run(DeepCalls.java:7)",
                        "",
                        "\t   5      @Override",
                        "\t   6      public void run() {",
                        "\t-> 7          call1();",
                        "\t   8      }",
                        "",
                        ""
                ), null},

                {new LongMethod(), join(lineSeparator(),
                        "stack.source.test.TestException: test",
                        "\tat stack.source.test.LongMethod.run(LongMethod.java:34)",
                        "",
                        "\t-> 34          throw new TestException(\"test\");",
                        "",
                        ""
                ), null},

                {new Lambda(), join(lineSeparator(),
                        "stack.source.test.TestException: hi",
                        "\tat stack.source.test.Lambda.lambda$run$0(Lambda.java:12)",
                        "",
                        "\t   10          lambda(() -> {",
                        "\t   11              assertTrue(true);",
                        "\t-> 12              throw new TestException(expected);",
                        "\t   13          });",
                        "",
                        "",
                        "\tat stack.source.test.Lambda.lambda(Lambda.java:17)",
                        "\tat stack.source.test.Lambda.run(Lambda.java:10)"
                ), null},

                {new MutipleLongChains(), join(lineSeparator(),
                        "stack.source.test.TestException: bob",
                        "\tat stack.source.test.MutipleLongChains$Helper.test(MutipleLongChains.java:57)",
                        "\tat stack.source.test.MutipleLongChains$Helper.access$100(MutipleLongChains.java:55)",
                        "\tat stack.source.test.MutipleLongChains.run(MutipleLongChains.java:20)",
                        "",
                        "\t   19          Helper helper2 = helper1",
                        "\t-> 20                  .test(\"x\")",
                        "\t   21                  .test(\"x\")",
                        "\t   22                  .test(\"x\");",
                        "",
                        ""
                ), null},
        });
    }

    public StackSourceTest(
            Runnable test,
            String expectedStackTraceBeginning,
            Function<String, String> processor) {
        super(test, expectedStackTraceBeginning, processor);
    }

}
