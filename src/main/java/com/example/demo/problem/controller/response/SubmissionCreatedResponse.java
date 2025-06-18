package com.example.demo.problem.controller.response;

import com.example.demo.global.enums.SubmissionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionCreatedResponse {
    private Long submissionId;
    private SubmissionStatus status;  // 항상 PENDING

    public static SubmissionCreatedResponse of(Long submissionId) {
        return SubmissionCreatedResponse.builder()
                .submissionId(submissionId)
                .status(SubmissionStatus.PENDING)
                .build();
    }
}
