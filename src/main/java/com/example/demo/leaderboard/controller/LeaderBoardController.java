package com.example.demo.leaderboard.controller;

import com.example.demo.leaderboard.application.LeaderBoardService;
import com.example.demo.leaderboard.controller.response.LeaderBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    @GetMapping("/v1/contests/{contest_id}/leaderboard")
    public LeaderBoardResponse getLeaderBoardFromRdb(@PathVariable("contest_id") String contestId) {
        long start = System.nanoTime();
        LeaderBoardResponse response = leaderBoardService.getLeaderBoardInfo(contestId);
        long end = System.nanoTime();
        System.out.println("RDB 조회 시간: " + (end - start) / 1_000_000.0 + " ms");
        return response;
    }

    @GetMapping("/v1/redis/contests/{contest_id}/leaderboard")
    public LeaderBoardResponse getLeaderBoardFromRedis(@PathVariable("contest_id") String contestId) {
        long start = System.nanoTime();
        LeaderBoardResponse response = leaderBoardService.getLeaderBoardWithRedis(Long.valueOf(contestId));
        long end = System.nanoTime();
        System.out.println("Redis 조회 시간: " + (end - start) / 1_000_000.0 + " ms");
        return response;
    }
}
