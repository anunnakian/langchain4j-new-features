package com.javapro.langchain4j.guardrails.v1;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface JavaDeveloper {

    @SystemMessage("""
            You are a helpful code generator. Generate Java code and return it as raw code
            without any markdown or code fences or code block.
            """
    )
    String writeCode(String request);
}