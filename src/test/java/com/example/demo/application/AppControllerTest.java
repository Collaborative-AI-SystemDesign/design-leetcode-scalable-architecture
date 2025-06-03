package com.example.demo.application;

import com.example.demo.leaderboard.controller.response.LeaderBoardResponse;
import com.example.demo.leaderboard.domain.RankingEntry;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

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

        for(RankingEntry ranking : body.getRanking()) {
            System.out.println("Id : " + ranking.getUserId());
            System.out.println("score : " + ranking.getScore());
        }
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
}