package com.razorrecon.rules;

import org.springframework.stereotype.Component;

@Component
public class ReferenceSimilarityRule {

    public boolean matches(
            String referenceA,
            String referenceB) {

        if (referenceA == null || referenceB == null) {
            return false;
        }

        String a = normalize(referenceA);
        String b = normalize(referenceB);

        if (a.equals(b)) {
            return true;
        }

        int distance = levenshteinDistance(a, b);

        int maxLength = Math.max(a.length(), b.length());

        if (maxLength == 0) {
            return true;
        }

        double similarity =
                1.0 - ((double) distance / maxLength);

        return similarity >= 0.85;
    }

    private String normalize(String value) {

        return value
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");
    }

    private int levenshteinDistance(String a, String b) {

        int[][] dp =
                new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {

            for (int j = 1; j <= b.length(); j++) {

                int cost =
                        a.charAt(i - 1) == b.charAt(j - 1)
                                ? 0
                                : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[a.length()][b.length()];
    }
}