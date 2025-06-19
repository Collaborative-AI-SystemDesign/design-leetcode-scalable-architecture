package com.example.demo.submission;

import com.example.demo.global.rabbitmq.RabbitMqService;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.ProblemController;
import com.example.demo.problem.domain.api.ProblemApiRepository;
import com.example.demo.submission.application.SubmissionService;
import com.example.demo.submission.controller.SubmissionController;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.api.UserApiRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SubmissionServiceTest {

    @Autowired
    ProblemApiRepository problemRepository;
    @Mock
    UserApiRepository userRepository;
    @Mock
    RabbitMqService rabbitMqService;
    @InjectMocks
    ProblemService problemService;
    @Autowired
    ProblemController problemController;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    SubmissionRepository submissionRepository;
    @Autowired
    SubmissionController  submissionController;
    /*
        * submit polling 성능 테스트
        * 반드시 제출된 submissionId를 기준으로 테스트해야함.
     */
    @Test
    @DisplayName("submit polling 성능 테스트")
    void testSubmissionPolling(){
        Long submissionId = 122352L; // 실제로 제출된 submissionId로 변경해야 함
        submissionController.getSubmissionStatus(submissionId);
    }
}
