package com.example.demo.problem.service;

import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.global.rabbitmq.RabbitMqService;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.ProblemController;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.request.SubmissionRequestMessageDto;
import com.example.demo.problem.controller.response.SubmissionCreatedResponse;
import com.example.demo.problem.domain.Problem;
import com.example.demo.problem.domain.api.ProblemApiRepository;
import com.example.demo.submission.domain.Submission;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.api.UserApiRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.demo.global.enums.CodingLanguages.JAVA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProblemSubmissionTest {
    @Mock
    SubmissionRepository submissionRepository;
    @Mock
    ProblemApiRepository problemRepository;
    @Mock
    UserApiRepository userRepository;
    @Mock
    RabbitMqService rabbitMqService;
    @InjectMocks
    ProblemService problemService;
    @Autowired
    ProblemController problemController;


    @Test
    @DisplayName("코드 제출 테스트 및 메시지 발송 검증")
    void submitProblem_persistsAndSendsMessage() {
        // given
        long problemId = 42L;
        SubmissionRequest req = new SubmissionRequest(JAVA,7L, "JAVA", 7L);
        Problem problem = new Problem(
                1L,                                            // id
                "Sample Problem Title",                        // title
                "This is a sample problem description.",      // description
                Diffculties.EASY,                             // difficulty (enum)
                Catagories.ARRAY,                            // category (enum)
                "Time limit: 1s, Memory limit: 256MB",         // constraints
                new ArrayList<>(),                             // examples (빈 리스트로 시작)
                new ArrayList<>(),                             // startercodes (빈 리스트로 시작)
                new ArrayList<>()                              // testcases (빈 리스트로 시작)
        );
        User user = User.toEntity(1L, "alice");

        when(problemRepository.findById(problemId)).thenReturn(Optional.of(problem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // simulate save → id 채워진 객체 반환
        when(submissionRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> {
                    Submission s = invocation.getArgument(0);
                    ReflectionTestUtils.setField(s, "id", 123L);
                    return s;
                });

        // when
        SubmissionCreatedResponse resp = problemService.submitProblem(problemId, req);

        // then
        assertThat(resp.getSubmissionId()).isEqualTo(123L);
        assertThat(resp.getStatus()).isEqualTo(SubmissionStatus.PENDING);
        // 메시지 발송 검증
        ArgumentCaptor<SubmissionRequestMessageDto> captor =
                ArgumentCaptor.forClass(SubmissionRequestMessageDto.class);
        verify(rabbitMqService).sendMessage(captor.capture());
        assertThat(captor.getValue().getSubmissionId()).isEqualTo(123L);
        assertThat(captor.getValue().getContestId()).isEqualTo(7L);
    }

    // submitProblem 성능테스트
    @Test
    @DisplayName("문제 제출 성능 테스트")
    void submitProblemPerformanceTest() {
        long problemId = 42L;
        long userId = 7L;
        long contestId = 7L;
        SubmissionRequest req = new SubmissionRequest(JAVA, contestId, "print('Hello, World!')", userId);
        problemController.submitCode(problemId, req);
        // 문제와 유저를 미리 생성
    }
}
