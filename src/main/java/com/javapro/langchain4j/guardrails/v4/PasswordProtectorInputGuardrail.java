package com.javapro.langchain4j.guardrails.v4;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordProtectorInputGuardrail implements InputGuardrail {

    @Override
    public InputGuardrailResult validate(UserMessage message) {
        String request = message.singleText();

        if (!PasswordDetector.detectPasswordsInText(request).isEmpty()) {
            return fatal("You have tried to send sensitive data to the LLM. Be careful !");
        }

        return InputGuardrailResult.success();
    }
}