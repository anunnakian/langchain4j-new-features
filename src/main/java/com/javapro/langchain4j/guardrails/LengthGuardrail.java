package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LengthGuardrail implements InputGuardrail {

	@Override
	public InputGuardrailResult validate(UserMessage userMessage) {
		String input = userMessage.singleText();
		
		if (input.length() < 3 || input.length() > 50) {
			return fatal("Poem topic length is inappropriate.");
		}
		
		return InputGuardrailResult.success();
	}
}