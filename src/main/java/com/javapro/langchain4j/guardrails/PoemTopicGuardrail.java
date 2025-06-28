package com.javapro.langchain4j.guardrails;

import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PoemTopicGuardrail implements InputGuardrail {

	@Override
	public InputGuardrailResult validate(dev.langchain4j.data.message.UserMessage userMessage) {
		String input = userMessage.singleText().toLowerCase();
		
		if (input.contains("violence") 
			 || input.contains("hate") 
			 || input.contains("explicit")) {
			return fatal("Sensitive or inappropriate poem topic detected.");
		}
		
		return InputGuardrailResult.success();
	}
}