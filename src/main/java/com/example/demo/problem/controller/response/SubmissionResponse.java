package com.example.demo.problem.controller.response;

import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.submission.domain.Submission;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SubmissionResponse {
    
    private SubmissionStatus status;
    private List<SubmissionStatus> testCaseStatus;
    
    public static SubmissionResponse of(List<SubmissionStatus> status) {
        boolean allSuccess = status.stream()
                .allMatch(s -> s == SubmissionStatus.SUCCESS);

        return SubmissionResponse.builder()
                .status(allSuccess ? SubmissionStatus.SUCCESS : SubmissionStatus.FAIL)
                .testCaseStatus(status)
                .build();
    }
}
