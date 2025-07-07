package com.javapro.langchain4j.guardrails.v5;

import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import dev.langchain4j.data.message.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompletenessInputGuardrail implements InputGuardrail {

    CompletenessValidator completenessValidator;

    public CompletenessInputGuardrail(CompletenessValidator completenessValidator) {
        this.completenessValidator = completenessValidator;
    }

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        // Ask the model to verify prompt completeness
        var conversation = completenessValidator.isRequestComplete(userMessage.singleText());

        if (!"VALID".equalsIgnoreCase(conversation)) {
            return fatal(
                "Prompt is incomplete: please specify a class or method name and describe desired behavior."
            );
        }

        return InputGuardrailResult.success();
    }
}