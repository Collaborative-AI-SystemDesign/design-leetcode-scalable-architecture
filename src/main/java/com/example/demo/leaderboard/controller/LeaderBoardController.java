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
    public LeaderBoardResponse getLeaderBoardFromRdb(@PathVariable("contest_id") Long contestId) {
        LeaderBoardResponse response = leaderBoardService.getLeaderBoardInfo(contestId);
        return response;
    }

    @GetMapping("/v1/redis/contests/{contest_id}/leaderboard")
    public LeaderBoardResponse getLeaderBoardWithCache(@PathVariable("contest_id") String contestId) {
        LeaderBoardResponse response = leaderBoardService.getLeaderBoardWithCache(Long.valueOf(contestId));
        return response;
    }
}
