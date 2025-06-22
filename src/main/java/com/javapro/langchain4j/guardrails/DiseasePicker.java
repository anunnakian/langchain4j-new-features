package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface DiseasePicker {

    @SystemMessage("""
    You are a helpful antibody drug research assistant. If the user asks, help them to choose the best disease for antibody research based on their questions.
    Only once the user chooses a disease to move forward with, you call storeDiseaseName with the disease name, and inform the user that their disease was stored and if they want to move on to the next step.
    """)
    String answer(@UserMessage String query);
}