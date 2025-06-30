package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
@SystemMessage("You validate if poem topics are positive and inspiring.")
public interface TopicValidationService {

    @UserMessage("Is the topic '{topic}' appropriate and inspiring for a poem? Answer only with true or false without bracket.")
    boolean isTopicAppropriate(String topic);
}