package com.javapro.langchain4j.guardrails.v2;

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
    void test_02_write_code_with_output_guardrails() {
        String request = "Generate function that use ls command to display a list of files using Runtime class";

        assertThatThrownBy(() -> javaDeveloper.writeCode(request)).isInstanceOf(GuardrailException.class)
            .hasMessageContaining("Generated code must not use ProcessBuilder or Runtime class.");
    }
}
