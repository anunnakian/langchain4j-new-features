package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.InputGuardrails;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
@SystemMessage("""
	You are a helpful code generator. Generate Java code and return it as raw code 
	without any markdown or code fences or code block.
	"""
)
public interface CodeGeneratorService {

	@InputGuardrails(CodegenInputGuardrail.class)
	@OutputGuardrails(CodegenOutputGuardrail.class)
	String generateCode(String topic);

	@InputGuardrails({CodegenInputGuardrail.class, PromptStructureInputGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class, CodeStyleOutputGuardrail.class})
	String generateCodeWithMultipleGuardrails(String topic);
}