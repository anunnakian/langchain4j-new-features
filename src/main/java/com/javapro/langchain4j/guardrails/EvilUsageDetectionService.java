package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface EvilUsageDetectionService {
    
    @UserMessage("""
            is the query : 
            '{userQuery}'
            A mix of many questions ?
            Return True or False as response
            """)
    Boolean isAttemptToBioWeapon(String userQuery);
}
