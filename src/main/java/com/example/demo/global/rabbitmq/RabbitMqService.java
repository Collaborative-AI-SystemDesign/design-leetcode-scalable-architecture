package com.example.demo.global.rabbitmq;


import com.example.demo.global.common.ProblemDto;
import com.example.demo.global.common.SubmissionMessageDto;
import com.example.demo.global.common.TestcaseDto;
import com.example.demo.global.common.UserDto;
import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.domain.Problem;
import com.example.demo.submission.domain.Submission;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.api.UserApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Queue 로 메세지를 발행할 때에는 RabbitTemplate 의 ConvertAndSend 메소드를 사용하고
 * Queue 에서 메세지를 구독할때는 @RabbitListener 을 사용
 *
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitMqService {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final SubmissionRepository submissionRepository;
    private final UserApiRepository userApiRepository;

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
     * 3. RabbitTemplate 의 convertAndSend 메소드를 사용하여 메세지를 발행
     *
     * @return
     */
    public void sendMessage(SubmissionMessageDto messageDto) {
        log.info("************ messagge send: {}",messageDto.getProblemDto());
        this.rabbitTemplate.convertAndSend(exchangeName,routingKey,messageDto);
    }

    /**
     * 1. Queue 에서 메세지를 받도록 함.
     * 2. 임의로 messageDto를 Object 타입으로 받았지만, 실제로는 DTO 클래스를 사용하여 타입을 지정.
     **/
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    @Transactional
//    public void receiveMessage(SubmissionMessageDto messageDto) {
//        log.info("************ messagge receive: {}",messageDto.getProblemDto());
//        ProblemDto problemDto = messageDto.getProblemDto();
//        UserDto userDto = messageDto.getUserDto();
//        SubmissionRequest request = messageDto.getRequest();
//
//        List<TestcaseDto> testcases = problemDto.getTestcases();
//
//        String executableCode = generateExecutableCode(request.getCode(), testcases);
//        /* Todo: 실제로 샌드박스 환경에서 실행하는 코드로 변경하고 요청을 보내야합니다.
//        String stdout = sandboxApi.execute(executableCode);
//        List<Boolean> testResults = Arrays.stream(
//                        stdout.replaceAll("[\\[\\] ]", "").split(","))
//                .map(Boolean::parseBoolean)
//                .toList();
//        */
//
//        List<SubmissionStatus> testResults = new ArrayList<>();
//        for (int i=0; i < testcases.size(); i++) {
//            testResults.add(SubmissionStatus.SUCCESS);
//        }
//
//        double runtime = new Random().nextDouble(0.1, 2.0); // Simulate runtime in seconds
//        double memory = new Random().nextDouble(10, 100); // Simulate memory usage in MB
//        sleep((int)runtime*1000); // Simulate execution time
//
//        User user = User.toEntity(userDto.getId(), userDto.getNickname());
//        Problem problem = Problem.toEntity(problemDto);
//
//        // 결과 받아서 저장하기
//        Submission submission = Submission.toEntity(
//                request.getCode(),
//                request.getCodingLanguage(),
//                SubmissionStatus.SUCCESS, // 실제로는 testResults에 따라 다르게 설정해야 합니다.
//                runtime,
//                memory,
//                user,
//                problem
//        );
//
//        submissionRepository.save(submission);
//    }

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
