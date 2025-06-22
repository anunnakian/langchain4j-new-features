package com.javapro.langchain4j.guardrails;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import ma.devoxx.langchain4j.aiservices.DiseasePicker;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GuardrailsTest {

    @Inject
    DiseasePicker diseasePicker;

    @Test
    void simple() {
        var response = diseasePicker.answer(1, "Hello there");

        System.out.println(response);
    }
}