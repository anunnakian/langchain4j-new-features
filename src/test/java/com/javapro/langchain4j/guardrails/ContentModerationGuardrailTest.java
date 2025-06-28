package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentModerationGuardrailTest {

    private final ContentModerationGuardrail guardrail = new ContentModerationGuardrail();

    @Test
    void testValidate_Success() {
        AiMessage message = new AiMessage("This poem brings happiness and positivity.");
        OutputGuardrailResult result = guardrail.validate(message);
        assertTrue(result.isSuccess());
    }

    @Test
    void testValidate_NegativeContent() {
        AiMessage message = new AiMessage("This poem contains negative thoughts.");
        OutputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Negative content detected.", result.failures().getFirst().message());
    }

    @Test
    void testValidate_HarmfulContent() {
        AiMessage message = new AiMessage("This poem could cause harm.");
        OutputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Negative content detected.", result.failures().getFirst().message());
    }
}