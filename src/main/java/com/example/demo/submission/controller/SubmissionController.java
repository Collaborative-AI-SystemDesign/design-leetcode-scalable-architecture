package com.example.demo.submission.controller;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.problem.controller.response.SubmissionResponse;
import com.example.demo.submission.application.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;


    @GetMapping("/{submissionId}")
    public ApiResponse<SubmissionResponse> getSubmissionStatus(
            @PathVariable Long submissionId) {
        return ApiResponse.success(submissionService.getSubmissionStatus(submissionId));
    }
}
