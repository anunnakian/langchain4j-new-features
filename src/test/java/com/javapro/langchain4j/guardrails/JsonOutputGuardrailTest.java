package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonOutputGuardrailTest {

    private final JsonOutputGuardrail guardrail = new JsonOutputGuardrail();

    @Test
    void testValidate_Success() {
        AiMessage message = new AiMessage("{\"poem\": \"Nature is beautiful\"}");
        OutputGuardrailResult result = guardrail.validate(message);
        assertTrue(result.isSuccess());
    }

    @Test
    void testValidate_InvalidJson() {
        AiMessage message = new AiMessage("Not a JSON");
        OutputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Invalid JSON format detected.", result.failures().getFirst().message());
    }
}