package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import dev.langchain4j.data.message.AiMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CodeStyleOutputGuardrailTest {

    private CodeStyleOutputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new CodeStyleOutputGuardrail();
    }

    @Test
    void testSuccess_WithPackageAndCleanCode() {
        String code = """
            package com.example;

            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("Hello");
                }
            }
            """;
        AiMessage ai = AiMessage.from(code);
        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Valid code with package, under line limit, no TODO/FIXME should pass")
            .isTrue();
    }

    @Test
    void testFailure_OnTooManyLines() {
        // Generate 201 empty lines to exceed MAX_LINES (200)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("class X").append(i).append(" {}\n");
        }
        AiMessage ai = AiMessage.from(sb.toString());
        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code exceeding max line count should not pass")
            .isFalse();
        assertThat(result.isFatal())
            .as("Exceeding lines is a failure, not a fatal error")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .contains("over 200 lines");
    }

    @Test
    void testFatal_OnTodoComment() {
        String code = """
            package com.example;

            public class TodoExample {
                // TODO: implement this method
                void doSomething() {}
            }
            """;
        AiMessage ai = AiMessage.from(code);
        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code containing TODO should be rejected fatally")
            .isFalse();
        assertThat(result.isFatal())
            .as("TODO comments trigger a fatal error")
            .isTrue();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must not contain TODO");
    }

    @Test
    void testFatal_OnFixmeComment() {
        String code = """
            package com.example;

            public class FixmeExample {
                // fixme: address null pointer edge case
                void doSomethingElse() {}
            }
            """;
        AiMessage ai = AiMessage.from(code);
        OutputGuardrailResult result = guardrail.validate(ai);

        assertThat(result.isSuccess())
            .as("Code containing FIXME should be rejected fatally")
            .isFalse();
        assertThat(result.isFatal())
            .as("FIXME comments trigger a fatal error")
            .isTrue();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("must not contain TODO or FIXME");
    }
}
