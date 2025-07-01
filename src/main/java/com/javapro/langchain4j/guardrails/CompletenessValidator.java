package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface CompletenessValidator {

    @SystemMessage("You are a code generation prompt reviewer." +
            "Check if the user prompt specifies a valid Java class or method name, " +
            "and describes what it should do. Reply with 'VALID' or 'INVALID'.")
    String isRequestComplete(String request);
}