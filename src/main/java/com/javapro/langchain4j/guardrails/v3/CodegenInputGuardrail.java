package com.javapro.langchain4j.guardrails.v3;

import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CodegenInputGuardrail implements InputGuardrail {

	private static final List<String> commands = List.of("ls", "ps", "top", "whoami");

	@Override
	public InputGuardrailResult validate(UserMessage message) {
		for (String cmd : commands) {
			if (message.singleText().contains(cmd)) {
				return fatal("Detected use of system command: " + cmd);
			}
		}
		return InputGuardrailResult.success();
	}
}