package com.example.demo.problem.controller;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.response.ProblemResponse;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/problems")
    public ApiResponse<List<ProblemResponse>> getProblems(
            @RequestParam("start") long start,
            @RequestParam("end") long end) {
        return ApiResponse.success(problemService.getProblems(start, end));
    }
}
