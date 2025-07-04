package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.*;

@QuarkusTest
class JavaDeveloperTest {

    @Inject
    JavaDeveloper javaDeveloper;

    @Test
    void test_01_write_code_without_guardrails() {
        String request = "Generate function that use ls command to display a list of files";

        String code = javaDeveloper.writeCode(request);

        // it generates just the requested java method
        System.out.println(code);
        assertThat(code).isNotNull();
    }

    @Test
    void test_02_write_code_with_output_guardrails() {
        String request = "Generate function that use ls command to display a list of files";

        assertThatThrownBy(() -> javaDeveloper.writeCleanCompliantCode(request)).isInstanceOf(GuardrailException.class)
                .hasMessageContaining("Generated code must not call Runtime.exec()");
    }

    @Test
    void test_03_write_code_with_input_guardrails() {
        String request = "Generate function that use ls command to display a list of files " +
                "using Runtime.getRuntime().exec";

        assertThatThrownBy(() -> javaDeveloper.writeCleanCode(request)).isInstanceOf(GuardrailException.class)
                .hasMessageContaining("Prompt requests generation of unsafe or system-level code.");
    }

    @Test
    void test_04_write_code_with_multiple_output_guardrails() {
        String request = "Generate just the java method to multiply two numbers.";

        assertThatThrownBy(() -> javaDeveloper.writeCleanCompliantCode(request))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("must include at least one class or interface declaration");
    }

    @Test
    void test_05_write_code_with_ai_input_guardrails() {
        String request = "Generate just the java method";

        assertThatThrownBy(() -> javaDeveloper.writeCleanCodeWithCleanRequest(request))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("please specify a class or method name and describe desired behavior.");
    }
}
