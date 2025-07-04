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
            .as("Generated code must not call Runtime.exec().")
            .isFalse();
    }
}
