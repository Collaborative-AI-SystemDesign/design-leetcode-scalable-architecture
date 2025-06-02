package com.example.demo.problem.controller.response;

import com.example.demo.problem.domain.Problem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemResponse {
    Long id;
    String title;
    String difficulty;

    public static ProblemResponse from(Problem problem) {
        return ProblemResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .difficulty(problem.getDifficulty().name())
                .build();
    }
}
