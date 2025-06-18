package com.example.demo.submission;

import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.global.rabbitmq.RabbitMqService;
import com.example.demo.leaderboard.application.LeaderBoardService;
import com.example.demo.problem.controller.response.SubmissionResultMessageDto;
import com.example.demo.submission.domain.Submission;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMqServiceTest {

    @Mock
    SubmissionRepository submissionRepository;
    @Mock
    LeaderBoardService leaderboardService;
    @InjectMocks
    RabbitMqService  rabbitMqService;

    @Test
    void handleResult_updatesStatusAndCallsLeaderboard() {
        // given
        long submissionId = 99L;
        Submission existing = new Submission();
        ReflectionTestUtils.setField(existing, "id", submissionId);
        existing.setStatus(SubmissionStatus.PENDING);
        User user = User.toEntity(1L, "alice");
        Long contestId = 7L;
        existing.setUser(user);
        when(submissionRepository.findById(submissionId))
                .thenReturn(Optional.of(existing));

        SubmissionResultMessageDto msg =
                SubmissionResultMessageDto.of(submissionId, contestId ,SubmissionStatus.SUCCESS);

        // when
        rabbitMqService.handleResult(msg);

        // then
        // 엔티티 상태가 SUCCESS로 바뀌었는지
        assertThat(existing.getStatus()).isEqualTo(SubmissionStatus.SUCCESS);
        // 저장 호출 검증
        verify(submissionRepository).save(existing);
        // 리더보드 서비스 호출 검증
        verify(leaderboardService).saveLeaderboard(contestId, user);
    }
}