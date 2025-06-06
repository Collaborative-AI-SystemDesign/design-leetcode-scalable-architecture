package com.example.demo.global.common;

import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.domain.Problem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubmissionMessageDto {
    private SubmissionRequest request;
    private UserDto userDto;
    private ProblemDto problemDto;

    public SubmissionMessageDto(SubmissionRequest request, UserDto userDto,ProblemDto problemDto) {
        this.request = request;
        this.userDto = userDto;
        this.problemDto = problemDto;
    }
}