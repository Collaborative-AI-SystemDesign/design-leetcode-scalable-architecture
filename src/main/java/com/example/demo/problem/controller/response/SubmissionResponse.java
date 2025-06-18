package com.example.demo.problem.controller.response;

import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.submission.domain.Submission;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SubmissionResponse {
    private Long submissionId;
    private SubmissionStatus status;
    private List<SubmissionStatus> testCaseStatus;
    
    public static SubmissionResponse of(Submission submission, List<SubmissionStatus> status) {
        // submission의 상태가 PENDING이면
        // PENDING 상태는 테스트 케이스가 아직 실행되지 않은 상태이므로, testCaseStatus는 null로 설정
        if (submission.getStatus() == SubmissionStatus.PENDING) {
            return SubmissionResponse.builder()
                    .submissionId(submission.getId())
                    .status(submission.getStatus())
                    .testCaseStatus(List.of(SubmissionStatus.PENDING)) // PENDING 상태로 초기화
                    .build();
        }

        //submission의 상태가 SUCCESS이면
        return SubmissionResponse.builder()
                .submissionId(submission.getId())
                .status(submission.getStatus())
                .testCaseStatus(status)
                .build();

        // submission의 상태가 SUCCESS 또는 FAIL인 경우
        // testCaseStatus는 각 테스트 케이스의 결과를 포함
//        boolean allSuccess = status.stream()
//                .allMatch(s -> s == SubmissionStatus.SUCCESS);

    }
}
