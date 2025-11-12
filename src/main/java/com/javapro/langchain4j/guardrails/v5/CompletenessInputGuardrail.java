package com.javapro.langchain4j.guardrails.v5;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
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
                "Prompt is incomplete or not valid: please specify a class and/or method name and describe desired behavior."
            );
        }

        return InputGuardrailResult.success();
    }
}