package com.javapro.langchain4j.guardrails.v4;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.regex.*;

/**
 * Please don't care about this class, it's just an exemple of Password detector.
 * In real project, you should use more advanced technic :)
 */
public class PasswordDetector {

    // Returns a boolean flag if the string contains a password
    public static boolean detectPasswordsInText(String text) {
        // Please use proper way to check password presence in your text
        // This code is just for example
        return text.contains("javapro120049@d\\");
    }
}