package com.example.demo.global.rabbitmq;


import com.example.demo.global.common.TestcaseDto;
import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.leaderboard.application.LeaderBoardService;
import com.example.demo.problem.controller.request.SubmissionRequestMessageDto;
import com.example.demo.problem.controller.response.SubmissionResultMessageDto;
import com.example.demo.submission.domain.Submission;
import com.example.demo.submission.domain.api.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Queue 로 메세지를 발행할 때에는 RabbitTemplate 의 ConvertAndSend 메소드를 사용하고
 * Queue 에서 메세지를 구독할때는 @RabbitListener 을 사용
 *
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMqService {


    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.request.routing.key}")
    private String requestRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    private final SubmissionRepository submissionRepository;
    private final LeaderBoardService leaderboardService;

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
     * 3. RabbitTemplate 의 convertAndSend 메소드를 사용하여 메세지를 발행
     *
     * @return
     */
    public void sendMessage(SubmissionRequestMessageDto submissionRequestMessageDto) {
        log.info("************ messagge send: {}", submissionRequestMessageDto.getSubmissionId());
        this.rabbitTemplate.convertAndSend(exchangeName,requestRoutingKey,submissionRequestMessageDto);
    }

    /**
     * 1. Queue 에서 메세지를 받도록 함.
     **/
    @Transactional
    @RabbitListener(
            queues = "${rabbitmq.result.queue.name}",
            ackMode = "NONE"
    )
    public void handleResult(SubmissionResultMessageDto submissionResultMessageDto) {
        // 1) 기존 Submission 엔티티 조회
        Long submissionId = submissionResultMessageDto.getSubmissionId();
        SubmissionStatus status = submissionResultMessageDto.getStatus();
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Submission not found: " + submissionId));

        // 2) 상태 업데이트
        submission.setStatus(status);// SUCCESS or FAILURE

        submissionRepository.save(submission);
        leaderboardService.saveLeaderboard( submissionResultMessageDto.getContestId(), submission.getUser());
    }

    /**
     * 샌드박스 환경에서 실행할 수 있는 Java 프로그램 코드를 생성합니다.
     * List<Boolean> testResults가 샌드박스에서 출력되고, 서버에서 String으로 받아서 처리합니다.
     * Todo: 직접 테스트를 해봐야 합니다.
     */
    private String generateExecutableCode(String userCode, List<TestcaseDto> testcases) {
        StringBuilder executableCode = new StringBuilder();
        executableCode.append("import java.util.*;\n");
        executableCode.append("public class Solution {\n");
        executableCode.append("    public static void main(String[] args) {\n");
        executableCode.append("        Scanner sc = new Scanner(System.in);\n");

        for (TestcaseDto testcase : testcases) {
            executableCode.append("        // Test case: ").append(testcase.getInput()).append("\n");
            executableCode.append("        System.out.println(\"").append(testcase.getExpectedOutput()).append("\");\n");
        }

        executableCode.append(userCode);
        executableCode.append("    }\n");
        executableCode.append("}\n");

        return executableCode.toString();
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
