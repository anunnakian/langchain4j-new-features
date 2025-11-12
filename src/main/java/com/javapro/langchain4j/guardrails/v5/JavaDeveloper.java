package com.javapro.langchain4j.guardrails.v5;

import com.javapro.langchain4j.guardrails.v2.CodegenOutputGuardrail;
import com.javapro.langchain4j.guardrails.v3.CodegenInputGuardrail;
import com.javapro.langchain4j.guardrails.v4.PasswordProtectorInputGuardrail;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
@SystemMessage("""
    You are an expert Java developer.
    You Generate only Java code as a raw code without any markdown or code fences or code block.
    No text, only code
    """)
public interface JavaDeveloper {

	@InputGuardrails({CodegenInputGuardrail.class, PasswordProtectorInputGuardrail.class, CompletenessInputGuardrail.class})
	@OutputGuardrails(CodegenOutputGuardrail.class)
	String writeCode(String request);
}