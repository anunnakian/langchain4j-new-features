package com.javapro.langchain4j.testing;

import com.javapro.langchain4j.guardrails.v5.CompletenessValidator;
import io.quarkiverse.langchain4j.scorer.junit5.AiScorer;
import io.quarkiverse.langchain4j.scorer.junit5.SampleLocation;
import io.quarkiverse.langchain4j.scorer.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationReport;
import io.quarkiverse.langchain4j.testing.scorer.Samples;
import io.quarkiverse.langchain4j.testing.scorer.Scorer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@AiScorer
@QuarkusTest
class CompletenessValidatorTest {

    @Inject
    CompletenessValidator completenessValidator;

    @Test
    void test_01_CompletenessValidator(
            @ScorerConfiguration(concurrency = 5) Scorer scorer,
            @SampleLocation("src/test/resources/prompt-samples.yaml") Samples<String> samples) {

        EvaluationReport<String> report = scorer.evaluate(
                samples,
                prompt -> completenessValidator.isRequestComplete(prompt.get(0)),
                (sample, verdict) -> sample.expectedOutput().equalsIgnoreCase(verdict)
        );

        assertThat(report.score()).isEqualTo(100);
    }
}