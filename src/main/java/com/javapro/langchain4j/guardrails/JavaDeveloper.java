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
public interface JavaDeveloper {

	String writeCode(String topic);

	@InputGuardrails({CodegenInputGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class})
	String writeCleanCode(String topic);

	@InputGuardrails({CodegenInputGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class, CodeStyleOutputGuardrail.class})
	String writeCleanCompliantCode(String topic);

	@InputGuardrails({CodegenInputGuardrail.class, CompletenessInputGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class, CodeStyleOutputGuardrail.class})
	String writeCleanCodeWithCleanRequest(String topic);
}