package com.javapro.langchain4j.guardrails;

import static org.assertj.core.api.Assertions.*;

import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import dev.langchain4j.data.message.UserMessage;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CompletenessInputGuardrailTest {

    @Inject
    CompletenessValidator validator;

    private CompletenessInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new CompletenessInputGuardrail(validator);
    }

    @Test
    void testValidPrompt() {
        String prompt = "Generate a Java class UserService with methods createUser and deleteUser.";

        UserMessage msg = UserMessage.from(prompt);
        InputGuardrailResult result = guardrail.validate(msg);

        assertThat(result.isSuccess())
            .as("VALID verdict should result in success()")
            .isTrue();
        assertThat(result.failures()).isEmpty();
    }

    @Test
    void testInvalidPrompt() {
        String prompt = "Make a service.";

        UserMessage msg = UserMessage.from(prompt);
        InputGuardrailResult result = guardrail.validate(msg);

        assertThat(result.isSuccess())
            .as("INVALID verdict should result in a fatal error")
            .isFalse();
        assertThat(result.failures().getFirst().message())
            .containsIgnoringCase("Prompt is incomplete")
            .containsIgnoringCase("specify a class or method name");
    }
}