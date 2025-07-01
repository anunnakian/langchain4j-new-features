package com.javapro.langchain4j.testing;

import io.quarkiverse.langchain4j.testing.scorer.EvaluationSample;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationStrategy;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class EditDistanceStrategy implements EvaluationStrategy<String> {

    private final double threshold;
    private final LevenshteinDistance levenshtein;

    /**
     * @param threshold similarity threshold in [0.0, 1.0], e.g. 0.8 for 80% similarity
     */
    public EditDistanceStrategy(double threshold) {
        if (threshold < 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0");
        }
        this.threshold = threshold;
        this.levenshtein = new LevenshteinDistance();
    }

    @Override
    public boolean evaluate(EvaluationSample<String> sample, String output) {
        String expected = sample.expectedOutput();

        // compute raw edit distance
        int dist = levenshtein.apply(expected, output);
        int maxLen = Math.max(expected.length(), output.length());
        // avoid division by zero
        if (maxLen == 0) {
            return true;
        }

        // normalized similarity: 1 - (distance / maxLen)
        double similarity = 1.0 - ((double) dist / maxLen);
        System.out.println("SIMI = " + similarity);
        return similarity >= threshold;
    }
}