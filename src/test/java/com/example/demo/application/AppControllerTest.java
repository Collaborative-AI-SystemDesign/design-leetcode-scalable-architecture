package com.example.demo.application;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.global.enums.CodingLanguages;
import com.example.demo.leaderboard.controller.response.LeaderBoardResponse;
import com.example.demo.leaderboard.domain.RankingEntry;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.problem.controller.response.SubmissionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {
    RestClient restClient = RestClient.create("http://localhost:8080");

    @Test
    void getLeaderBoardInfoTest() {
        long contestId = 2L;
        long[] problemIds = {2L, 4L, 5L};
        String userCode = """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("[true]");
                }
            }
            """;
        // 1. 유저 1~100번 반복
        for (long userId = 1; userId <= 75; userId++) {
            SubmissionRequest request = new SubmissionRequest(CodingLanguages.JAVA, contestId, userCode, userId);

            restClient.post()
                    .uri("/problems/3/submission")
                    .body(request)
                    .retrieve()
                    .body(ApiResponse.class);

            // 2. 50번째 유저가 제출 끝났으면 leaderboard 조회
            if (userId == 50) {
                System.out.println("==== 50번째 유저 제출 완료 후 Leaderboard ====");
                LeaderBoardResponse leaderboard = restClient.get()
                        .uri("/v1/contests/{contest_id}/leaderboard", contestId)
                        .retrieve()
                        .body(LeaderBoardResponse.class);

                printLeaderboard(leaderboard);
            }

            // 3. 75번째 유저가 제출 끝났으면 leaderboard 조회
            if (userId == 75) {
                System.out.println("==== 75번째 유저 제출 완료 후 Leaderboard ====");
                LeaderBoardResponse leaderboard = restClient.get()
                        .uri("/v1/contests/{contest_id}/leaderboard", contestId)
                        .retrieve()
                        .body(LeaderBoardResponse.class);

                printLeaderboard(leaderboard);
            }
        }


    }

    private void printLeaderboard(LeaderBoardResponse leaderboard) {
        List<RankingEntry> rankings = leaderboard.getRanking();
        for (int i = 0; i < rankings.size(); i++) {
            RankingEntry entry = rankings.get(i);
            System.out.printf("%d위: UserId=%d, Score=%d%n",
                    i + 1, entry.getUserId(), entry.getScore());
        }
    }

    @Test
    void getLeaderBoardInfoWithRedisTest() {
        LeaderBoardResponse response = restClient.get()
                .uri("/v1/contests/{contest_id}/leaderboard", 2L)
                .retrieve()
                .body(LeaderBoardResponse.class);

        for(RankingEntry ranking : response.getRanking()) {
            System.out.println("Id : " + ranking.getUserId());
            System.out.println("score : " + ranking.getScore());
        }

    }

    @Test
    void submitProblemTest() {
        // 1️⃣ 테스트용 problemId와 userCode 준비
        long problemId = 1L;
        String userCode = """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("[true]");
                }
            }
            """;

        // 2️⃣ SubmissionRequest 생성
        SubmissionRequest request = new SubmissionRequest(CodingLanguages.JAVA, 2L, userCode, 102L);

        // 3️⃣ POST 요청 보내기
        ApiResponse<SubmissionResponse>  response = restClient.post()
                .uri("/problems/{problemId}/submission", problemId)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<SubmissionResponse>>() {});

        // 4️⃣ 결과 출력 (디버깅)
        System.out.println("API Response: " + response);

        // 5️⃣ SubmissionResponse 꺼내서 출력
        SubmissionResponse submissionResponse = response.getData();
        System.out.println("Submission Status: " + submissionResponse.getStatus());
        System.out.println("Test Case Statuses: " + submissionResponse.getTestCaseStatus());

        // 6️⃣ 검증 (예시)
        assertNotNull(submissionResponse);
        assertNotNull(submissionResponse.getStatus());
        assertNotNull(submissionResponse.getTestCaseStatus());


    }
}