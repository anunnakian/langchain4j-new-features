package com.javapro.langchain4j.guardrails;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.InputGuardrails;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;

@RegisterAiService
public interface PoemGeneratorService {

	@InputGuardrails(PoemTopicGuardrail.class)
	@OutputGuardrails(JsonOutputGuardrail.class)
	@SystemMessage("You are a helpful poem generator. Generate inspiring and positive poems in raw JSON format without a code block in one line")
	String generatePoem(String topic);

	@InputGuardrails({PoemTopicGuardrail.class, LengthGuardrail.class})
	@OutputGuardrails({JsonOutputGuardrail.class, ContentModerationGuardrail.class})
	String generatePoemWithMultipleGuardrails(String topic);
}