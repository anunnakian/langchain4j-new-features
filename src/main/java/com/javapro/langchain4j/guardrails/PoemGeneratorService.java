package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.InputGuardrails;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
@SystemMessage("You are a helpful poem generator. " +
		"Generate inspiring and positive poems in raw JSON format (title and lines) without a code block in one line")
public interface PoemGeneratorService {

	@InputGuardrails(CodegenInputGuardrail.class)
	@OutputGuardrails(CodegenOutputGuardrail.class)
	String generatePoem(String topic);

	@InputGuardrails({CodegenInputGuardrail.class, LengthGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class, ContentModerationGuardrail.class})
	String generatePoemWithMultipleGuardrails(String topic);
}