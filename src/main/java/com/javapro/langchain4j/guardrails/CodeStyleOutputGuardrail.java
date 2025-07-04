package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodeStyleOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();

        if (!code.matches("(?s).*\\b(class|interface)\\s+\\w+.*\\{.*")) {
            return fatal("Generated code must include at least one class or interface declaration.");
        }

        return OutputGuardrailResult.success();
    }
}