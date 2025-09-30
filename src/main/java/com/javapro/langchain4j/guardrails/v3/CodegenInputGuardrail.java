package com.javapro.langchain4j.guardrails.v3;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodegenInputGuardrail implements InputGuardrail {

	@Override
	public InputGuardrailResult validate(UserMessage userMessage) {
		String prompt = userMessage.singleText();

		// Disallow dangerous or undesirable code patterns
		if (prompt.toLowerCase().contains("processbuilder")) {
			return fatal("Prompt requests generation of unsafe or system-level code.");
		}

		return InputGuardrailResult.success();
	}
}