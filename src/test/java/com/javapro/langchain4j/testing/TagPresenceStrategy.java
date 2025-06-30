package com.javapro.langchain4j.testing;

import io.quarkiverse.langchain4j.testing.scorer.EvaluationSample;
import io.quarkiverse.langchain4j.testing.scorer.EvaluationStrategy;

public class TagPresenceStrategy implements EvaluationStrategy<String> {

    @Override
    public boolean evaluate(EvaluationSample<String> sample, String output) {
        // For each tag on the sample (e.g. "java", "devoxx"), require it appears in the poem.
        for (String tag : sample.tags()) {
            if (!output.toLowerCase().contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}