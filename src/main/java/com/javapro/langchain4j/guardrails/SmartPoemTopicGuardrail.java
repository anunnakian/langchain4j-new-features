package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SmartPoemTopicGuardrail implements InputGuardrail {

    @Inject
    TopicValidationService topicValidationService;

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String input = userMessage.singleText();

        // Using injected AiService for advanced validation
        boolean isAppropriate = topicValidationService.isTopicAppropriate(input);

        if (!isAppropriate) {
            return fatal("Topic deemed inappropriate by validation service.");
        }

        return InputGuardrailResult.success();
    }
}