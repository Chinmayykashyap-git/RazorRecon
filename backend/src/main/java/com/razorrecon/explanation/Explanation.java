package com.razorrecon.explanation;

import java.util.List;

public record Explanation(String summary, String likelyCause, List<String> evidence,
                          String recommendedAction, String status) {
}
