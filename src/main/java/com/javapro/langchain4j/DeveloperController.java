package com.javapro.langchain4j;

import com.javapro.langchain4j.guardrails.v2.JavaDeveloper;
import dev.langchain4j.guardrail.InputGuardrailException;
import dev.langchain4j.guardrail.OutputGuardrailException;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class DeveloperController {

    Template codegen;
    JavaDeveloper ai;

    public DeveloperController(Template codegen, JavaDeveloper ai) {
        this.ai = ai;
        this.codegen = codegen;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance form() {
        return codegen.data("prompt", "", "result", null, "error", null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance generate(@FormParam("prompt") String prompt) {
        try {
            String result = ai.writeCode(prompt);
            return codegen.data("prompt", prompt, "result", result, "error", null);
        } catch (InputGuardrailException | OutputGuardrailException e) {
            return codegen.data("prompt", prompt, "error", extractUserMessage(e), "result", null);
        }
    }

    private String extractUserMessage(Throwable exception) {
        if (exception instanceof InputGuardrailException ||
                exception instanceof OutputGuardrailException) {
            String raw = exception.getMessage();
            // Strip the default prefix
            return raw.replaceAll(".*message: ", "").trim();
        }
        return exception.getMessage();
    }
}