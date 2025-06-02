package com.example.demo.problem.controller.response;

import com.example.demo.problem.domain.Problem;
import com.example.demo.problem.dto.ExampleDto;
import com.example.demo.problem.dto.StartercodeDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProblemDetailResponse {
    Long id;
    String title;
    String difficulty;
    String description;
    String constraints;
    List<ExampleDto> examples;
    List<StartercodeDto> startercodes;

    public static ProblemDetailResponse from(Problem problem) {
        return ProblemDetailResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .difficulty(problem.getDifficulty().name())
                .description(problem.getDescription())
                .constraints(problem.getConstraints())
                .examples(problem.getExamples().stream()
                        .map(ExampleDto::from)
                        .toList())
                .startercodes(problem.getStartercodes().stream()
                        .map(StartercodeDto::from)
                        .toList())
                .build();
    }
}
