package com.javapro.langchain4j.guardrails;

import dev.langchain4j.data.message.UserMessage;
import io.quarkiverse.langchain4j.guardrails.InputGuardrail;
import io.quarkiverse.langchain4j.guardrails.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodegenInputGuardrail implements InputGuardrail {

	@Override
	public InputGuardrailResult validate(UserMessage userMessage) {
		String prompt = userMessage.singleText();

		// Disallow dangerous or undesirable code patterns
		if (prompt.contains("System.exit")
				|| prompt.toLowerCase().contains("runtime.getruntime().exec")
				|| prompt.toLowerCase().contains("delete")
				|| prompt.toLowerCase().contains("reflection")) {
			return fatal("Prompt requests generation of unsafe or system-level code.");
		}

		// Disallow prompts that ask for sensitive credential handling
		if (prompt.toLowerCase().matches(".*(password|secret|privatekey).*")) {
			return fatal("Prompt includes requests for handling credentials; disallowed.");
		}

		return InputGuardrailResult.success();
	}
}