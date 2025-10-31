package com.javapro.langchain4j.guardrails.v4;

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
    void test_04_write_code_with_multiple_output_guardrails() {
        String request = "Generate just the java method that hash this password : javapro120049@d\\";

        assertThatThrownBy(() -> javaDeveloper.writeCode(request))
                .isInstanceOf(GuardrailException.class)
                .hasMessageContaining("ou have tried to send sensitive data to the LLM. Be careful !");
    }
}
