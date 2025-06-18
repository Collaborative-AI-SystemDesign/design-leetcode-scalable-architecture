package com.example.demo.problem.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionRequestMessageDto {
    private Long submissionId;
    private Long contestId;

    public static SubmissionRequestMessageDto of(Long submissionId, Long contestId) {
        return SubmissionRequestMessageDto.builder()
                .submissionId(submissionId)
                .contestId(contestId)
                .build();
    }
}
