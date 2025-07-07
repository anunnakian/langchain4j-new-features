package com.javapro.langchain4j.guardrails.v4;

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
}
