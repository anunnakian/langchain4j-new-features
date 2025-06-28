package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LengthGuardrailTest {

    private final LengthGuardrail guardrail = new LengthGuardrail();

    @Test
    void testValidate_Success() {
        UserMessage message = UserMessage.from("nature");
        InputGuardrailResult result = guardrail.validate(message);
        assertTrue(result.isSuccess());
    }

    @Test
    void testValidate_TooShort() {
        UserMessage message = UserMessage.from("hi");
        InputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Poem topic length is inappropriate.", result.failures().getFirst().message());
    }

    @Test
    void testValidate_TooLong() {
        UserMessage message = UserMessage.from("This is an exceptionally long poem topic that exceeds the maximum allowed length");
        InputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Poem topic length is inappropriate.", result.failures().getFirst().message());
    }
}