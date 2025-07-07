package com.javapro.langchain4j.guardrails.v1;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class JavaDeveloperTest {

    @Inject
    JavaDeveloper javaDeveloper;

    @Test
    void test_01_write_code_without_guardrails() {
        String request = "Generate function that use ls command to display a list of files";

        String code = javaDeveloper.writeCode(request);

        System.out.println(code);
        assertThat(code).isNotNull();
    }
}
