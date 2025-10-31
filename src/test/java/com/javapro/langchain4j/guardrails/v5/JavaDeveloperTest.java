package com.javapro.langchain4j.guardrails.v5;

import dev.langchain4j.guardrail.GuardrailException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
class JavaDeveloperTest {

    @Inject
    JavaDeveloper javaDeveloper;

    @Test
    void test_05_write_code_with_ai_input_guardrails() {
        String request = "Generate just the java method";

        assertThatThrownBy(() -> javaDeveloper.writeCode(request))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("please specify a class or method name and describe desired behavior.");
    }
}
