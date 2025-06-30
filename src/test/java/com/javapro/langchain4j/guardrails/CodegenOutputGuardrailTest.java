package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import dev.langchain4j.data.message.AiMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CodegenOutputGuardrailTest {

    private CodegenOutputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new CodegenOutputGuardrail();
    }

    @Test
    void testSuccess_WithValidClass() {
        String code = """
            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("Hello");
                }
            }
            """;
        AiMessage ai = AiMessage.from(code);

        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Valid Java class declaration should pass")
            .isTrue();
    }

    @Test
    void testFatal_MissingClassOrInterface() {
        String code = """
            void doSomething() { }
            """;
        AiMessage ai = AiMessage.from(code);

        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code without class or interface must be rejected")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must include at least one class or interface declaration");
    }

    @Test
    void testFatal_OnSystemExitCall() {
        String code = """
            public class Shutdown {
                void stop() {
                    System.exit(1);
                }
            }
            """;
        AiMessage ai = AiMessage.from(code);

        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code containing System.exit should be rejected")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must not call System.exit() or Runtime.exec()");
    }

    @Test
    void testFatal_OnRuntimeExecCall() {
        String code = """
            public class Executor {
                void run() throws Exception {
                    Runtime.getRuntime().exec("rm -rf /");
                }
            }
            """;
        AiMessage ai = AiMessage.from(code);

        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code containing Runtime.exec should be rejected")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must not call System.exit() or Runtime.exec()");
    }

    @Test
    void testFatal_OnReflectionUsage() {
        String code = """
            public class Reflector {
                void reflect() throws Exception {
                    Class.forName("com.example.Foo");
                }
            }
            """;
        AiMessage ai = AiMessage.from(code);

        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code using reflection APIs should be rejected")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must not use reflection APIs");
    }
}
