package com.javapro.langchain4j.guardrails.v2;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface JavaDeveloper {

    @OutputGuardrails(CodegenOutputGuardrail.class)
    @SystemMessage("""
        You are a helpful code generator. Generate Java code and return it as raw code
        without any markdown or code fences or code block. Don't add comments in the result even if the block is empty.
        """
    )
    String writeCode(String request);
}