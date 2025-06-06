package com.example.demo.global.common;

import com.example.demo.problem.domain.Problem;

import java.util.List;

public class ProblemDtoMapper {
    public static ProblemDto fromEntity(Problem problem) {
        List<TestcaseDto> testcases = problem.getTestcases().stream()
                .map(t -> new TestcaseDto(t.getId(), t.getInput(), t.getExpectedOutput()))
                .toList();

        return new ProblemDto(
                problem.getId(),
                problem.getTitle(),
                problem.getDescription(),
                problem.getDifficulty(),
                problem.getCategory(),
                problem.getConstraints(),
                testcases
        );
    }
}
