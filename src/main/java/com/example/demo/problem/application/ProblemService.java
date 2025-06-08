package com.example.demo.problem.application;

import com.example.demo.global.common.ProblemDto;
import com.example.demo.global.common.ProblemDtoMapper;
import com.example.demo.global.common.SubmissionMessageDto;
import com.example.demo.global.common.UserDto;
import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.global.rabbitmq.RabbitMqService;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.response.ProblemDetailResponse;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.controller.response.SubmissionResponse;
import com.example.demo.problem.domain.Problem;
import com.example.demo.problem.domain.api.ProblemApiRepository;
import com.example.demo.submission.domain.api.SubmissionApiRepository;
import com.example.demo.submission.domain.Submission;
import com.example.demo.testcases.domain.Testcase;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.api.UserApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final StringRedisTemplate redisTemplate;

    private final ProblemApiRepository problemRepository;
    private final UserApiRepository userRepository;
    private final SubmissionApiRepository submissionRepository;
    private final RabbitMqService rabbitMqService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<ProblemResponse> getProblems(long start, long end) {
        return problemRepository.findByIdBetween(start, end)
                .stream()
                .map(ProblemResponse::from)
                .toList();
    }

    // 문제 다 가져오기 offset 기반 페이징
    public Page<ProblemResponse> getProblemPage(Pageable pageable) {
        Page<Problem> problemPage = problemRepository.findAll(pageable);
        return problemPage.map(ProblemResponse::from);
    }

    // 문제 다 가져오기 cursor 기반 페이징
    public List<ProblemResponse> getProblemsByCursor(Long cursor, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "id"));
        List<Problem> problems;

        if (cursor == null) {
            problems = problemRepository.findAll(pageable).getContent();
        } else {
            problems = problemRepository.findByIdGreaterThan(cursor, pageable);
        }
        return problems.stream()
                .map(ProblemResponse::from)
                .toList();
    }

    public ProblemDetailResponse getDetailProblem(long problemId) {
        return ProblemDetailResponse.from(problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다.")));
    }

    public SubmissionResponse submitProblem(Long problemId, SubmissionRequest request, String idempotencyKey) {
        //check is duplicate request
        boolean duplicateRequest = redisTemplate.hasKey(idempotencyKey);
        if (duplicateRequest) throw new IllegalArgumentException("중복된 요청입니다.");
        redisTemplate.opsForValue().set(idempotencyKey, "true", 10, TimeUnit.SECONDS);

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다."));
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        //encode user code
        String encodedCode = DigestUtils.sha256Hex(request.getCode());
        if (encodedCode.length() > 80) {
            throw new IllegalArgumentException("코드가 너무 깁니다. 80자 이하로 작성해주세요.");
        }

        //check encodedCode exists
        Optional<Submission> submissionOpt = submissionRepository.findByEncodedCode(encodedCode);
        if (submissionOpt.isPresent()) {
            return SubmissionResponse.of(stringToTestResults(submissionOpt.get().getTestResults()));
        }

        List<Testcase> testcases = problem.getTestcases();
        String executableCode = generateExecutableCode(request.getCode(), testcases);
        //send executableCode to sandbox environment

        //Todo: 실제로 샌드박스 환경에서 실행하는 코드로 변경하고 요청을 보내야합니다.
        boolean testPassed = true;
        List<SubmissionStatus> testResults = new ArrayList<>();
        for (int i=0; i < testcases.size(); i++) {
            testResults.add(SubmissionStatus.SUCCESS);
            //if failed, set testPassed to false
        }
        double runtime = new Random().nextDouble(0.1, 2.0); // Simulate runtime in seconds
        double memory = new Random().nextDouble(10, 100); // Simulate memory usage in MB
        sleep((int)runtime*1000); // Simulate execution time


        // 결과 저장하기
        Submission submission = Submission.of(
                request.getCode(),
                encodedCode,
                request.getCodingLanguage(),
                SubmissionStatus.SUCCESS, // 실제로는 testResults에 따라 다르게 설정해야 합니다.
                runtime,
                memory,
                testPassed? SubmissionStatus.SUCCESS : SubmissionStatus.FAIL,
                strTestResults,
                user,
                problem
        );
        submissionRepository.save(submission);
        return SubmissionResponse.of(testResults);
    }


    public SubmissionResponse submitProblemWithMq(Long problemId, SubmissionRequest request) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다."));
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        ProblemDto problemDto = ProblemDtoMapper.fromEntity(problem);
        UserDto userDto = new UserDto(user.getId(), user.getNickname());
        SubmissionMessageDto submissionMessageDto = new SubmissionMessageDto(request, userDto, problemDto);
        rabbitMqService.sendMessage(submissionMessageDto);

        // MQ consumer에서 submission 저장을 한다.
        // client에서 1~2초 간격으로 db 저장데이터를 확인한다. 그 도중에는 로딩 중 표시를 사용자에게 노출

        // waiting response 필요
        List<SubmissionStatus> testResults = new ArrayList<>();
        return SubmissionResponse.of(testResults);
    }


    /**
     * 샌드박스 환경에서 실행할 수 있는 Java 프로그램 코드를 생성합니다.
     * List<Boolean> testResults가 샌드박스에서 출력되고, 서버에서 String으로 받아서 처리합니다.
     * Todo: 직접 테스트를 해봐야 합니다.
     */
    private String generateExecutableCode(String userCode, List<Testcase> testcases) {
        StringBuilder executableCode = new StringBuilder();
        executableCode.append("import java.util.*;\n");
        executableCode.append("public class Solution {\n");
        executableCode.append("    public static void main(String[] args) {\n");
        executableCode.append("        Scanner sc = new Scanner(System.in);\n");

        for (Testcase testcase : testcases) {
            executableCode.append("        // Test case: ").append(testcase.getInput()).append("\n");
            executableCode.append("        System.out.println(\"").append(testcase.getExpectedOutput()).append("\");\n");
        }

        executableCode.append(userCode);
        executableCode.append("    }\n");
        executableCode.append("}\n");

        return executableCode.toString();

        /* 보내지는 String 예시:
        public class Main {
            public static void main(String[] args) {
                Solution sol = new Solution();
                List<Boolean> testResults = new ArrayList<>();

                try {
                    String actual = String.valueOf(sol.getNum(1));
                    testResults.add(actual.equals("1"));
                } catch (Exception e) {
                    testResults.add(false);
                }

                try {
                    String actual = String.valueOf(sol.getNum(2));
                    testResults.add(actual.equals("2"));
                } catch (Exception e) {
                    testResults.add(false);
                }

                // 결과 출력
                System.out.println(testResults); <-- 이 결과가 샌드박스에서 출력되고, 서버에서 String으로 받아서 처리합니다.
            }
        }
         */
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String testResultsToString(List<SubmissionStatus> testResults) {
        try {
            return objectMapper.writeValueAsString(testResults);  // JSON으로 직렬화
        } catch (Exception e) {
            throw new RuntimeException("직렬화 실패", e);
        }
    }

    private List<SubmissionStatus> stringToTestResults(String str) {
        try {
            return objectMapper.readValue(str, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }
}
