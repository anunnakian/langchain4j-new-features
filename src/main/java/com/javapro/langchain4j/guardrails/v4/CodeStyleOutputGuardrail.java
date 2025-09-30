package com.javapro.langchain4j.guardrails.v4;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodeStyleOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();

        if (!code.matches("(?s).*\\b(class|interface)\\s+\\w+.*\\{.*")) {
            return reprompt(
                    "Generated code must include at least one class or interface declaration.",
                    "Generate that code inside a class or interface");
        }

        return OutputGuardrailResult.success();
    }
}