package com.javapro.langchain4j.guardrails.v3;

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
    void testFatalOnRuntimeExec() {
        UserMessage bad = UserMessage.from("Use Runtime.getRuntime().exec(\"rm -rf /\");");
        InputGuardrailResult result = guardrail.validate(bad);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.failures().getFirst().message())
                .containsIgnoringCase("unsafe or system-level code");
    }
}