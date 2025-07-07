package com.javapro.langchain4j.guardrails.v2;

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
    void test_02_write_code_with_output_guardrails() {
        String request = "Generate function that use ls command to display a list of files using Runtime class";

        assertThatThrownBy(() -> javaDeveloper.writeCode(request)).isInstanceOf(GuardrailException.class)
            .hasMessageContaining("Generated code must not call Runtime.exec().");
    }
}
