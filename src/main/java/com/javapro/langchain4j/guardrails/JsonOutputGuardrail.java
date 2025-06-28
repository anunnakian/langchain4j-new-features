package com.javapro.langchain4j.guardrails;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JsonOutputGuardrail implements OutputGuardrail {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public OutputGuardrailResult validate(AiMessage responseFromLLM) {
		try {
			mapper.readTree(responseFromLLM.text());
		} catch (Exception e) {
			return reprompt("Invalid JSON format detected.", "Please ensure your response is a properly formatted JSON.");
		}
		return OutputGuardrailResult.success();
	}
}