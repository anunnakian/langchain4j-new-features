package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodegenOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String code = aiMessage.text();

        if (!code.matches("(?s).*\\b(class|interface)\\s+\\w+.*\\{.*")) {
            return fatal(
                    "Generated code must include at least one class or interface declaration."
            );
        }

        if (code.contains("System.exit")
                || code.contains("Runtime.getRuntime().exec")) {
            return fatal(
                    "Generated code must not call System.exit() or Runtime.exec()."
            );
        }

        if (code.contains("Class.forName")
                || code.contains("java.lang.reflect")) {
            return fatal(
                    "Generated code must not use reflection APIs."
            );
        }

        return OutputGuardrailResult.success();
    }
}