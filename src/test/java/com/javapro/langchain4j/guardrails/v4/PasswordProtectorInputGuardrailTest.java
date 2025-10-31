package com.javapro.langchain4j.guardrails.v4;

import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PasswordProtectorInputGuardrailTest {

    private PasswordProtectorInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new PasswordProtectorInputGuardrail();
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
        UserMessage request = UserMessage.from(code);

        var result = guardrail.validate(request);

        assertThat(result.isSuccess())
                .as("Valid Java class declaration should pass")
                .isTrue();
    }

    @Test
    void testFatal_MissingClassOrInterface() {
        String code = """
            void doSomething() { }
            """;
        UserMessage request = UserMessage.from(code);

        var result = guardrail.validate(request);

        assertThat(result.isSuccess())
                .as("Code without class or interface must be rejected")
                .isFalse();
        assertThat(result.failures().getFirst().message())
                .containsIgnoringCase("must include at least one class or interface declaration");
    }
}
