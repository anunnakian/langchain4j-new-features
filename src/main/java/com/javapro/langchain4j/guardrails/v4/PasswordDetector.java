package com.javapro.langchain4j.guardrails.v4;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.regex.*;

/**
 * Please don't care about this class, it's just an exemple of Password detector.
 * In real project, you should use more advanced technic :)
 */
public class PasswordDetector {

    // Some context keywords often near passwords
    private static final Pattern CONTEXT_KEYWORD = Pattern.compile(
        "(?i)\\b(password|passwd|pwd|pass|secret|apikey|api_key|token|auth)\\b");

    // Simple "likely password" regex (alphanumeric + special, min 6 chars)
    private static final Pattern LIKELY_PWD_PATTERN = Pattern.compile("^[A-Za-z0-9@#$%^&*()_+=\\-\\[\\]{};:'\",.<>/?\\\\|`~]{6,}$");

    // Common weak patterns to downscore or still detect (optional)
    private static final Pattern WEAK_PATTERN = Pattern.compile("(?i)^(123456|password|qwerty|abc123|letmein|admin|welcome)$");

    // Split tokens (keeps alphanum and punctuation as tokens)
    private static List<String> tokenize(String text) {
        // Split on whitespace and some delimiters; keep likely contiguous strings
        return Arrays.stream(text.split("\\s+"))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toList();
    }

    // Shannon entropy per character * length -> approximate total entropy in bits
    private static double estimateEntropyBits(String token) {
        if (token == null || token.isEmpty()) return 0.0;
        int len = token.length();
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : token.toCharArray()) freq.put(c, freq.getOrDefault(c, 0) + 1);
        double entropyPerChar = 0.0;
        for (var count : freq.values()) {
            double p = (double) count / len;
            entropyPerChar += -p * (Math.log(p) / Math.log(2));
        }
        // total entropy (approx)
        return entropyPerChar * len;
    }

    // Returns a score and boolean flag if it's likely a password
    public static DetectionResult analyzeToken(String token, String surroundingText) {
        double score = 0.0;

        // length heuristics
        int len = token.length();
        if (len >= 6) score += 0.5;
        if (len >= 12) score += 0.5;

        // regex pattern
        if (LIKELY_PWD_PATTERN.matcher(token).matches()) score += 1.0;

        // presence of character classes
        boolean hasLower = token.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = token.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = token.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = token.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
        int classes = (hasLower ? 1 : 0) + (hasUpper ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);
        if (classes >= 2) score += 0.5;
        if (classes >= 3) score += 0.5;

        // entropy
        double bits = estimateEntropyBits(token);
        if (bits >= 30) score += 1.0;   // likely a complex/random password or API key
        else if (bits >= 20) score += 0.5;

        // common weak password detection (still a password, but weak)
        if (WEAK_PATTERN.matcher(token).matches()) score += 0.7;

        // context: check surrounding text (like "password:", "pwd=")
        if (surroundingText != null && CONTEXT_KEYWORD.matcher(surroundingText).find()) {
            score += 2.0; // strong signal
        }

        // tokens that look like base64 (often keys)
        if (token.matches("^[A-Za-z0-9+/]{16,}={0,2}$")) {
            // Base64-ish long token -> treat as likely secret
            score += 1.5;
        }

        boolean likely = score >= 1.5; // threshold you can tune
        return new DetectionResult(token, likely, score, bits);
    }

    // Analyze whole text and return suspicious tokens with context
    public static List<DetectionResult> detectPasswordsInText(String text) {
        List<String> tokens = tokenize(text);
        List<DetectionResult> results = new ArrayList<>();
        // We will use a sliding window (previous and next words) to form surrounding context
        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            // build small context area around token (previous 3 + next 3 tokens)
            int start = Math.max(0, i - 3);
            int end = Math.min(tokens.size(), i + 4);
            String context = String.join(" ", tokens.subList(start, end));
            DetectionResult r = analyzeToken(t, context);
            if (r.isLikely()) results.add(r);
        }
        // Sort by score descending
        results.sort(Comparator.comparingDouble(DetectionResult::getScore).reversed());
        return results;
    }

    public static void main(String[] args) {
        String sample = "Please keep this secret: password: AbcD1234! and also the apiKey=QmFzZTY0a2V5MTIzNDU2 and user=john.";
        List<DetectionResult> findings = detectPasswordsInText(sample);
        if (findings.isEmpty()) {
            IO.println("No likely passwords found.");
        } else {
            findings.forEach(IO::println);
        }
    }

    // Small DTO
    public static class DetectionResult {
        private final String token;
        private final boolean likely;
        private final double score;
        private final double entropyBits;

        public DetectionResult(String token, boolean likely, double score, double entropyBits) {
            this.token = token;
            this.likely = likely;
            this.score = score;
            this.entropyBits = entropyBits;
        }
        public boolean isLikely() { return likely; }
        public double getScore() { return score; }
        public String getToken() { return token; }
        public double getEntropyBits() { return entropyBits; }

        @Override
        public String toString() {
            return String.format("Token='%s' | likely=%s | score=%.2f | entropy=%.2f bits",
                    token, likely, score, entropyBits);
        }
    }
}