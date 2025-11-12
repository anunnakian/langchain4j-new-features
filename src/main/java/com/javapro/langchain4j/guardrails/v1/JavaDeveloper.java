package com.javapro.langchain4j.guardrails.v1;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
@SystemMessage("""
    You are an expert Java developer.
    You Generate only Java code as a raw code without any markdown or code fences or code block.
    No text, only code
    """)
public interface JavaDeveloper {

    String writeCode(String request);
}