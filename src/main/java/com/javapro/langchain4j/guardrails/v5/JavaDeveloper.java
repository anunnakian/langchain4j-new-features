package com.javapro.langchain4j.guardrails.v5;

import com.javapro.langchain4j.guardrails.v2.CodegenOutputGuardrail;
import com.javapro.langchain4j.guardrails.v3.CodegenInputGuardrail;
import com.javapro.langchain4j.guardrails.v4.CodeStyleOutputGuardrail;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.InputGuardrails;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface JavaDeveloper {

	@InputGuardrails({CodegenInputGuardrail.class, CompletenessInputGuardrail.class})
	@OutputGuardrails({CodegenOutputGuardrail.class, CodeStyleOutputGuardrail.class})
	@SystemMessage("""
	You are a helpful code generator. Generate Java code and return it as raw code
	without any markdown or code fences or code block.
	"""
	)
	String writeCode(String request);
}