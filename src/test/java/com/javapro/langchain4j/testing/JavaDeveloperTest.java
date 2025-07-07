package com.javapro.langchain4j.testing;

import com.javapro.langchain4j.guardrails.*;
import com.javapro.langchain4j.guardrails.v5.JavaDeveloper;
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
class JavaDeveloperTest {

	@Inject
	JavaDeveloper javaDeveloper;

	@Test
    void test_02_Semantic_Similarity_Strategy(
            @ScorerConfiguration(concurrency = 5) Scorer scorer,
            @SampleLocation("src/test/resources/codegen-samples.yaml") Samples<String> samples) {

		// Evaluate generateCode() outputs against our expected code samples
		EvaluationReport<String> report = scorer.evaluate(
				samples,
				prompt -> javaDeveloper.writeCode(prompt.get(0)),
				new SemanticSimilarityStrategy(0.85)  // 85% similarity threshold
		);

        assertThat(report.score()).isGreaterThanOrEqualTo(70);
    }

	@Test
	void test_03_Ai_Judge_Strategy(
			@ScorerConfiguration(concurrency = 5) Scorer scorer,
			@SampleLocation("src/test/resources/codegen-samples.yaml") Samples<String> samples
	) {
		var judgeModel = OpenAiChatModel.builder()
				.apiKey(System.getenv("OPENAI_API_KEY"))
				.modelName("gpt-4o")
				.build();

		String evaluationPrompt = """
            You are an AI assistant evaluating a generated Java code snippet against an expected implementation.
            Determine if the generated code follows similar java concept as expected code.
            Return true if it implements the pattern as expected, false otherwise.

            Generated code: {response}
            Expected code: {expected_output}

            Respond with only 'true' or 'false'.
            """;

		EvaluationReport<String> report = scorer.evaluate(
				samples,
				prompt -> javaDeveloper.writeCode(prompt.get(0)),
				new AiJudgeStrategy(judgeModel, evaluationPrompt)
		);

		// We expect at least 70% of samples to be judged correct
		assertThat(report.score())
				.as("At least 70% of generated code snippets should correctly implement the pattern")
				.isGreaterThanOrEqualTo(70);
	}

	@Test
	void test_04_Semantic_Similarity_Strategy_Per_Tag(
			@ScorerConfiguration(concurrency = 5) Scorer scorer,
			@SampleLocation("src/test/resources/codegen-samples.yaml") Samples<String> samples) {

		// Evaluate generateCode() outputs against our expected code samples
		EvaluationReport<String> report = scorer.evaluate(
				samples,
				prompt -> javaDeveloper.writeCode(prompt.get(0)),
				new SemanticSimilarityStrategy(0.85)  // 85% similarity threshold
		);

		// Overall quality should be high
		assertThat(report.score())
				.as("Overall code generation quality should be at least 80%")
				.isGreaterThanOrEqualTo(80);

		// Check per-pattern quality bars:
		assertThat(report.scoreForTag("singleton"))
				.as("Singleton implementations must stay consistent")
				.isGreaterThanOrEqualTo(80);

		assertThat(report.scoreForTag("builder"))
				.as("Builder patterns may have some variation")
				.isGreaterThanOrEqualTo(75);

		assertThat(report.scoreForTag("factory"))
				.as("Factory code should closely match expected structure")
				.isGreaterThanOrEqualTo(85);

		assertThat(report.scoreForTag("unittest"))
				.as("Unit-test skeletons must be nearly exact")
				.isGreaterThanOrEqualTo(90);
	}

	@Test
	void test_04_Custom_Evaluation_Strategy(
			@ScorerConfiguration(concurrency = 5) Scorer scorer,
			@SampleLocation("src/test/resources/codegen-samples.yaml") Samples<String> samples) {

		EditDistanceStrategy presenceStrategy = new EditDistanceStrategy(0.4);

		EvaluationReport<String> report = scorer.evaluate(
				samples,
				topic -> javaDeveloper.writeCode(topic.get(0)),
				presenceStrategy
		);

		assertThat(report.score()).isGreaterThanOrEqualTo(70);
	}

	@Test
	void test_04_Custom_Evaluation_Strategy_Not_Valid(
			@ScorerConfiguration(concurrency = 5) Scorer scorer,
			@SampleLocation("src/test/resources/invalid-codegen-samples.yaml") Samples<String> samples) {

		EditDistanceStrategy presenceStrategy = new EditDistanceStrategy(0.4);

		EvaluationReport<String> report = scorer.evaluate(
				samples,
				topic -> javaDeveloper.writeCode(topic.get(0)),
				presenceStrategy
		);

		assertThat(report.score()).isLessThan(30);
	}
}