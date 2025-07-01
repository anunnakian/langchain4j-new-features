package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodeStyleOutputGuardrail implements OutputGuardrail {

    // Maximum allowed lines of generated code to prevent runaway outputs
    private static final int MAX_LINES = 200;

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();
        String[] lines = code.split("\r?\n", -1);

        if (lines.length > MAX_LINES) {
            return failure("Generated code is over " + MAX_LINES + " lines. " +
                    "Consider splitting into smaller classes or methods."
            );
        }

        for (String line : lines) {
            String trimmed = line.trim().toLowerCase();
            if (trimmed.startsWith("// todo") || trimmed.contains("fixme")) {
                return fatal(
                        "Generated code must not contain TODO or FIXME comments."
                );
            }
        }

        return OutputGuardrailResult.success();
    }
}