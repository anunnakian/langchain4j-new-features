package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodegenInputGuardrailTest {

    private CodegenInputGuardrail guardrail;

    @BeforeEach
    void setUp() {
        guardrail = new CodegenInputGuardrail();
    }

    @Test
    void testAllowedPrompt() {
        UserMessage safe = UserMessage.from("Generate a Java class to parse JSON into a POJO.");
        InputGuardrailResult result = guardrail.validate(safe);

        assertThat(result.isSuccess())
                .as("Safe prompts should pass the guardrail")
                .isTrue();
    }

    @Test
    void testFatalOnSystemExit() {
        UserMessage bad = UserMessage.from("Create code that calls System.exit(0) when done.");
        InputGuardrailResult result = guardrail.validate(bad);

        assertThat(result.isSuccess())
                .as("Prompts with System.exit should be rejected")
                .isFalse();
        assertThat(result.failures().getFirst().message())
                .containsIgnoringCase("unsafe or system-level code");
    }

    @Test
    void testFatalOnRuntimeExec() {
        UserMessage bad = UserMessage.from("Use Runtime.getRuntime().exec(\"rm -rf /\");");
        InputGuardrailResult result = guardrail.validate(bad);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.failures().getFirst().message())
                .containsIgnoringCase("unsafe or system-level code");
    }

    @Test
    void testFatalOnCredentialHandling() {
        UserMessage bad = UserMessage.from("Write code to decrypt the privateKey stored in memory.");
        InputGuardrailResult result = guardrail.validate(bad);

        assertThat(result.isSuccess())
                .as("Prompts touching credentials should be rejected")
                .isFalse();

        assertThat(result.failures().getFirst().message())
                .as("Failure message should mention credentials and disallowed")
                .containsIgnoringCase("credentials")
                .containsIgnoringCase("disallowed");
    }
}