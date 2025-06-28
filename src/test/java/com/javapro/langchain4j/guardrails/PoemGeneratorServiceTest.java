package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PoemGeneratorServiceTest {

    @Inject
    PoemGeneratorService poemGeneratorService;

    @Test
    void testGeneratePoem_Success() {
        String topic = "nature";
        String result = poemGeneratorService.generatePoem(topic);
        assertNotNull(result);
        assertTrue(result.startsWith("{")); // Assuming JSON format
    }

    @Test
    void testGeneratePoem_InappropriateTopic() {
        String topic = "violence poem";
        assertThrows(GuardrailException.class,
                () -> poemGeneratorService.generatePoem(topic));
    }

    @Test
    void testGeneratePoem_TopicLengthViolation_Min() {
        String topic = "a"; // Too short
        assertThrows(GuardrailException.class,
                () -> poemGeneratorService.generatePoemWithMultipleGuardrails(topic));
    }

    @Test
    void testGeneratePoem_TopicLengthViolation_Max() {
        String topic = "A very long poem with many lines and complex topics that no body can understand"; // Too short
        assertThrows(GuardrailException.class,
                () -> poemGeneratorService.generatePoemWithMultipleGuardrails(topic));
    }
}