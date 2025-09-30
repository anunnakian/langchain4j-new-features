package com.javapro.langchain4j.guardrails.v3;

import com.javapro.langchain4j.guardrails.v2.CodegenOutputGuardrail;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface JavaDeveloper {

	@InputGuardrails(CodegenInputGuardrail.class)
	@OutputGuardrails(CodegenOutputGuardrail.class)
	@SystemMessage("""
		You are a helpful code generator. Generate Java code and return it as raw code
		without any markdown or code fences or code block.
		"""
	)
	String writeCode(String request);
}