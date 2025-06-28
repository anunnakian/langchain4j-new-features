package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContentModerationGuardrail implements OutputGuardrail {

	@Override
	public OutputGuardrailResult validate(AiMessage responseFromLLM) {
		String content = responseFromLLM.text().toLowerCase();
		if (content.contains("negative") || content.contains("harm")) {
			return reprompt("Negative content detected.", "Please provide positive and inspiring content.");
		}
		return OutputGuardrailResult.success();
	}
}