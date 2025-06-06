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

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {
    RestClient restClient = RestClient.create("http://localhost:8080");

    @Test
    public void healthCheck() {
        String body = restClient.get()
                .uri("/health")
                .retrieve()
                .body(String.class);

        System.out.println(body);
    }

    @Test
    void getLeaderBoardInfoTest() {
        LeaderBoardResponse body = restClient.get()
                .uri("/v1/contests/{contest_id}/leaderboard", 1)
                .retrieve()
                .body(LeaderBoardResponse.class);
//
//        for(RankingEntry ranking : body.getRanking()) {
//            System.out.println("Id : " + ranking.getUserId());
//            System.out.println("score : " + ranking.getScore());
//        }

    }

    @Test
    void getLeaderBoardInfoWithRedisTest() {
        LeaderBoardResponse response = restClient.get()
                .uri("/v1/redis/contests/{contest_id}/leaderboard", 1)
                .retrieve()
                .body(LeaderBoardResponse.class);

        for(RankingEntry ranking : response.getRanking()) {
            System.out.println("Id : " + ranking.getUserId());
            System.out.println("score : " + ranking.getScore());
        }

        LeaderBoardResponse response2 = restClient.get()
                .uri("/v1/redis/contests/{contest_id}/leaderboard", 1)
                .retrieve()
                .body(LeaderBoardResponse.class);

        for(RankingEntry ranking : response2.getRanking()) {
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
        SubmissionRequest request = new SubmissionRequest(CodingLanguages.JAVA, userCode);

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