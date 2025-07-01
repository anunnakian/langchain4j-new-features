package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
class CodeGeneratorServiceTest {

    @Inject
    CodeGeneratorService codeGeneratorService;

    @Test
    void testGenerateCode_Successful() {
        String topic = "Generate a DAO class for User entity.";
        String json = codeGeneratorService.generateCode(topic);

        assertThat(json).isNotNull();
    }

    @Test
    void testGenerateCode_InputGuardrailFails() {
        String badTopic = "Create code that calls System.exit(0) after run.";

        assertThatThrownBy(() -> codeGeneratorService.generateCode(badTopic)).isInstanceOf(GuardrailException.class)
                .hasMessageContaining("unsafe or system-level code");
    }

    @Test
    void testGenerateCodeWithMultipleGuardrails_StructureGuardrailFails() {
        String badTopic = "DAO class for User.";

        assertThatThrownBy(() -> codeGeneratorService.generateCodeWithMultipleGuardrails(badTopic))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("must start with Generate/Create/Implement/Build and mention 'Java'");
    }

    @Test
    void testGenerateCodeWithMultipleGuardrails_OutputGuardrailFails() {
        String topic = "Generate just a java method.";

        assertThatThrownBy(() -> codeGeneratorService.generateCodeWithMultipleGuardrails(topic))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("must include at least one class or interface declaration");
    }
}
