package com.javapro.langchain4j.guardrails.v3;

import com.javapro.langchain4j.guardrails.v2.CodegenOutputGuardrail;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.guardrail.OutputGuardrails;

@RegisterAiService
@SystemMessage("""
        You are an expert Java developer.
        You Generate Java code as a raw code without any markdown or code fences or code block.
        """)
public interface JavaDeveloper {

    @InputGuardrails(CodegenInputGuardrail.class)
    @OutputGuardrails(CodegenOutputGuardrail.class)
    String writeCode(String request);
}