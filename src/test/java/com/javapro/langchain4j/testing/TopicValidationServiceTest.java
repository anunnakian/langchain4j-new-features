package com.javapro.langchain4j.testing;

import com.javapro.langchain4j.guardrails.TopicValidationService;
import io.quarkiverse.langchain4j.scorer.junit5.AiScorer;
import io.quarkiverse.langchain4j.scorer.junit5.SampleLocation;
import io.quarkiverse.langchain4j.scorer.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationReport;
import io.quarkiverse.langchain4j.testing.scorer.Samples;
import io.quarkiverse.langchain4j.testing.scorer.Scorer;
import io.quarkiverse.langchain4j.testing.scorer.similarity.SemanticSimilarityStrategy;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@AiScorer
@QuarkusTest
class TopicValidationServiceTest {

    @Inject
    TopicValidationService topicValidationService;

    @Test
    void simpleTest(
            @ScorerConfiguration(concurrency = 5) Scorer scorer,
            @SampleLocation("src/test/resources/topic-samples.yaml") Samples<String> samples) {
        EvaluationReport<String> report = scorer.evaluate(samples,
                topic -> Boolean.toString(topicValidationService.isTopicAppropriate(topic.get(0))),
                (sample, output) -> sample.expectedOutput().equals(output));

        assertThat(report.score()).isEqualTo(100);
    }

    @Test
    void semanticSimilarityTest(
            @ScorerConfiguration(concurrency = 5) Scorer scorer,
            @SampleLocation("src/test/resources/topic-samples.yaml") Samples<String> samples) {

        EvaluationReport<String> report = scorer.evaluate(samples,
                topic -> Boolean.toString(topicValidationService.isTopicAppropriate(topic.get(0))),
                new SemanticSimilarityStrategy(0.8)
        );

        assertThat(report.score()).isGreaterThanOrEqualTo(70);
    }
}