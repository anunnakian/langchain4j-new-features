package com.javapro.langchain4j.guardrails.v2;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodegenOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();

        if (code.contains("ProcessBuilder") || code.contains("exec(")) {
            return fatal("Generated code must not use ProcessBuilder or Runtime class.");
        }

        return OutputGuardrailResult.success();
    }
}