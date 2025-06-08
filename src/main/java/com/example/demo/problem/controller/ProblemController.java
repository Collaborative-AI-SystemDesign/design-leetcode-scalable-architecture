package com.example.demo.problem.controller;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.request.PagingResponse;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.response.ProblemDetailResponse;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.controller.response.SubmissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/problems/offset")
    public ApiResponse<PagingResponse<ProblemResponse>> getProblemsByOffset(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProblemResponse> pageResult = problemService.getProblemPage(pageable);
        return ApiResponse.success(PagingResponse.from(pageResult));
    }

    @GetMapping("/problems/cursor")
    public ApiResponse<List<ProblemResponse>> getProblemsByCursor(
            @RequestParam("cursor") Long cursor,
            @RequestParam("limit") int limit) {
        return ApiResponse.success(problemService.getProblemsByCursor(cursor, limit));
    }

    @GetMapping("/problems/{problemId}")
    public ApiResponse<ProblemDetailResponse> getProblemById(
            @PathVariable("problemId") long problemId) {
        return ApiResponse.success(problemService.getDetailProblem(problemId));
    }

    @PostMapping("/problems/{problemId}/submission")
    public ApiResponse<SubmissionResponse> submitProblem(
            @PathVariable("problemId") long problemId,
            @RequestBody SubmissionRequest request,
            @RequestHeader(value = "idempotency-key") String idempotencyKey) {
        return ApiResponse.success(problemService.submitProblem(problemId, request, idempotencyKey));
    }
}
