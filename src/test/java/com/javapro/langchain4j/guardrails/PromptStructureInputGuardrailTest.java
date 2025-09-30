package com.javapro.langchain4j.guardrails;

import static org.assertj.core.api.Assertions.*;

import dev.langchain4j.guardrail.InputGuardrailResult;
import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromptStructureInputGuardrailTest {

    private PromptStructureInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new PromptStructureInputGuardrail();
    }

    @Test
    void testValidPrompts() {
        // Valid: starts with Generate and mentions Java
        UserMessage msg1 = UserMessage.from("Generate a Java service for user management.");
        InputGuardrailResult res1 = guardrail.validate(msg1);
        assertThat(res1.isSuccess())
            .as("Prompt starting with Generate and mentioning Java should pass")
            .isTrue();

        // Valid: starts with Create and mentions Java
        UserMessage msg2 = UserMessage.from("Create Java DTO classes for API responses.");
        InputGuardrailResult res2 = guardrail.validate(msg2);
        assertThat(res2.isSuccess())
            .as("Prompt starting with Create and mentioning Java should pass")
            .isTrue();

        // Valid: starts with Implement and mentions Java
        UserMessage msg3 = UserMessage.from("Implement builder pattern in Java for Person class.");
        InputGuardrailResult res3 = guardrail.validate(msg3);
        assertThat(res3.isSuccess())
            .as("Prompt starting with Implement and mentioning Java should pass")
            .isTrue();
    }

    @Test
    void testInvalidMissingActionVerb() {
        // Missing action verb at start
        UserMessage msg = UserMessage.from("Java service for order processing.");
        InputGuardrailResult res = guardrail.validate(msg);
        assertThat(res.isSuccess())
            .as("Prompt missing action verb should be rejected")
            .isFalse();
        assertThat(res.failures().getFirst().message())
            .containsIgnoringCase("Prompt must start with Generate/Create/Implement/Build")
            .containsIgnoringCase("mention 'Java'");
    }

    @Test
    void testInvalidMissingJavaKeyword() {
        // Has action verb but missing Java
        UserMessage msg = UserMessage.from("Generate a service for analytics.");
        InputGuardrailResult res = guardrail.validate(msg);
        assertThat(res.isSuccess())
            .as("Prompt missing 'Java' keyword should be rejected")
            .isFalse();
        assertThat(res.failures().getFirst().message())
            .containsIgnoringCase("Prompt must start with Generate/Create/Implement/Build")
            .containsIgnoringCase("mention 'Java'");
    }

    @Test
    void testInvalidCompletelyWrongFormat() {
        // Neither action verb nor Java
        UserMessage msg = UserMessage.from("Build awesome features.");
        InputGuardrailResult res = guardrail.validate(msg);
        assertThat(res.isSuccess())
            .as("Prompt without required structure should be rejected")
            .isFalse();
        assertThat(res.failures().getFirst().message())
            .containsIgnoringCase("Prompt must start with Generate/Create/Implement/Build")
            .containsIgnoringCase("mention 'Java'");
    }
}
