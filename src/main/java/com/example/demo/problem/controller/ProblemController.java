package com.example.demo.problem.controller;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.response.ProblemDetailResponse;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.controller.response.SubmissionResponse;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/problems/{problemId}")
    public ApiResponse<ProblemDetailResponse> getProblemById(
            @PathVariable("problemId") long problemId) {
        return ApiResponse.success(problemService.getDetailProblem(problemId));
    }

    @PostMapping("/problems/{problemId}/submission")
    public ApiResponse<SubmissionResponse> submitProblem(
            @PathVariable("problemId") long problemId,
            @RequestBody SubmissionRequest request) {
        return ApiResponse.success(problemService.submitProblem(problemId, request));
    }
}
