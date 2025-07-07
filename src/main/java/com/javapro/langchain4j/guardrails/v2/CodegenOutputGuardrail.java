package com.javapro.langchain4j.guardrails.v2;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodegenOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();

        if (code.contains("Runtime.getRuntime().exec")) {
            return fatal("Generated code must not call Runtime.exec().");
        }

        return OutputGuardrailResult.success();
    }
}