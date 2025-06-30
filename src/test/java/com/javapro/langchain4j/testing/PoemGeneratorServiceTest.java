package com.javapro.langchain4j.testing;

import com.javapro.langchain4j.guardrails.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.quarkiverse.langchain4j.scorer.junit5.AiScorer;
import io.quarkiverse.langchain4j.scorer.junit5.SampleLocation;
import io.quarkiverse.langchain4j.scorer.junit5.ScorerConfiguration;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationReport;
import io.quarkiverse.langchain4j.testing.scorer.Samples;
import io.quarkiverse.langchain4j.testing.scorer.Scorer;
import io.quarkiverse.langchain4j.testing.scorer.judge.AiJudgeStrategy;
import io.quarkiverse.langchain4j.testing.scorer.similarity.SemanticSimilarityStrategy;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@AiScorer
@QuarkusTest
class PoemGeneratorServiceTest {

	@Inject
	PoemGeneratorService poemGeneratorService;

	@Test
    void test_02_Semantic_Similarity_Strategy(
            @ScorerConfiguration(concurrency = 5) Scorer scorer,
            @SampleLocation("src/test/resources/poem-samples.yaml") Samples<String> samples) {

        EvaluationReport<String> report = scorer.evaluate(samples,
				topic -> poemGeneratorService.generatePoem(topic.get(0)),
				new SemanticSimilarityStrategy(0.8)
        );

        assertThat(report.score()).isGreaterThanOrEqualTo(70);
    }

	@Test
	void test_03_Ai_Judge_Strategy(
			@ScorerConfiguration(concurrency = 5) Scorer scorer,
			@SampleLocation("src/test/resources/poem-samples.yaml") Samples<String> samples
	) {
		var judgeModel = OpenAiChatModel.builder()
				.apiKey(System.getenv("OPENAI_API_KEY"))
				.modelName("gpt-4o")
				.build();

		EvaluationReport<String> report = scorer.evaluate(
				samples,
				topic -> poemGeneratorService.generatePoem(topic.get(0)),
				new AiJudgeStrategy(judgeModel, """
                You are an AI evaluating a poem and the expected poem.
                You need to evaluate whether the poems talk about the same topic or not.
                Return a true if they talk about the same topic, false if not.

                poem to evaluate: {response}
                Expected poem: {expected_output}

                Return just the result.
                """)
		);

		assertThat(report.score()).isGreaterThanOrEqualTo(70);
	}
}