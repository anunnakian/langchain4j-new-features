package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

// tribute to the Quarkus team for this guardrail example
// see https://docs.quarkiverse.io/quarkus-langchain4j/dev/guardrails.html
@ApplicationScoped
public class AdvancedOutputGuardrail implements OutputGuardrail {

    private final EvilUsageDetectionService service;

    public AdvancedOutputGuardrail(EvilUsageDetectionService service) {
        this.service = service;
    }

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        return OutputGuardrail.super.validate(responseFromLLM);
    }
}