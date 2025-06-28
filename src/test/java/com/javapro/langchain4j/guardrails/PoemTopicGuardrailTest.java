package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoemTopicGuardrailTest {

    private final PoemTopicGuardrail guardrail = new PoemTopicGuardrail();

    @Test
    void testValidate_Success() {
        UserMessage message = UserMessage.from("nature");
        InputGuardrailResult result = guardrail.validate(message);
        assertTrue(result.isSuccess());
    }

    @Test
    void testValidate_InappropriateTopic_Violence() {
        UserMessage message = UserMessage.from("violence");
        InputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Sensitive or inappropriate poem topic detected.", result.failures().getFirst().message());
    }

    @Test
    void testValidate_InappropriateTopic_Hate() {
        UserMessage message = UserMessage.from("hate");
        InputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Sensitive or inappropriate poem topic detected.", result.failures().getFirst().message());
    }

    @Test
    void testValidate_InappropriateTopic_Explicit() {
        UserMessage message = UserMessage.from("explicit");
        InputGuardrailResult result = guardrail.validate(message);
        assertFalse(result.isSuccess());
        assertEquals("Sensitive or inappropriate poem topic detected.", result.failures().getFirst().message());
    }
}