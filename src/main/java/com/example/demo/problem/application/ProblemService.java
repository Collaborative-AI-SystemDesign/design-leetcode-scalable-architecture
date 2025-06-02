package com.example.demo.problem.application;

import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.response.ProblemDetailResponse;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.controller.response.SubmissionResponse;
import com.example.demo.problem.domain.Problem;
import com.example.demo.problem.domain.api.ProblemRepository;
import com.example.demo.testcases.domain.Testcase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public List<ProblemResponse> getProblems(long start, long end) {
        return problemRepository.findByIdBetween(start, end)
                .stream()
                .map(ProblemResponse::from)
                .toList();
    }

    public ProblemDetailResponse getDetailProblem(long problemId) {
        return ProblemDetailResponse.from(problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다.")));
    }

    public SubmissionResponse submitProblem(Long problemId, SubmissionRequest request) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다."));
        List<Testcase> testcases = problem.getTestcases();
        String executableCode = generateExecutableCode(request.getCode(), testcases);

        /* Todo: 실제로 샌드박스 환경에서 실행하는 코드로 변경하고 요청을 보내야합니다.
        String stdout = sandboxApi.execute(executableCode);
        List<Boolean> testResults = Arrays.stream(
                        stdout.replaceAll("[\\[\\] ]", "").split(","))
                .map(Boolean::parseBoolean)
                .toList();
        */

        List<SubmissionStatus> testResults = new ArrayList<>();
        for (int i=0; i < testcases.size(); i++) {
            testResults.add(SubmissionStatus.SUCCESS);
        }
        sleep(new Random().nextInt(1000, 3000)); // Simulate execution time
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
}
