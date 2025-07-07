package com.javapro.langchain4j.guardrails.v4;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
class JavaDeveloperTest {

    @Inject
    JavaDeveloper javaDeveloper;

    @Test
    void test_04_write_code_with_multiple_output_guardrails() {
        String request = "Generate just the java method to multiply two numbers.";

        assertThatThrownBy(() -> javaDeveloper.writeCode(request))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("must include at least one class or interface declaration");
    }
}
