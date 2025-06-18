package com.example.demo.problem.controller.response;


import com.example.demo.global.enums.SubmissionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubmissionResultMessageDto {
    private Long submissionId;
    private Long contestId;
    private SubmissionStatus status; // "SUCCESS" or "FAIL"

    public static SubmissionResultMessageDto of(Long submissionId, Long contestId, SubmissionStatus status) {
        return SubmissionResultMessageDto.builder()
                .submissionId(submissionId)
                .contestId(contestId)
                .status(status)
                .build();
    }
}
