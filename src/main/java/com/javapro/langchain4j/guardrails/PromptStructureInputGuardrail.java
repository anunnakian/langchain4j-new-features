package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PromptStructureInputGuardrail implements InputGuardrail {

    /**
     * Ensures prompts follow a clear, predictable format:
     *  - Start with an action verb (Generate/Create/Implement/Build)
     *  - Mention "Java" to scope the request
     */
    private static final String STRUCTURE_REGEX =
            "^(?i)(generate|create|implement|build)\\b.*\\bjava\\b.*";

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String prompt = userMessage.singleText().trim();

        // Enforce clear action + language specification
        if (!prompt.matches(STRUCTURE_REGEX)) {
            return fatal("Prompt must start with Generate/Create/Implement/Build and mention 'Java'."
            );
        }

        return InputGuardrailResult.success();
    }
}