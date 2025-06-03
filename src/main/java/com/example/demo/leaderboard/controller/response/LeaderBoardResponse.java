package com.example.demo.leaderboard.controller.response;

import com.example.demo.leaderboard.domain.RankingEntry;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class LeaderBoardResponse {

    private List<RankingEntry> ranking;

    public static LeaderBoardResponse from(List<RankingEntry> ranking) {
        LeaderBoardResponse leaderBoardResponse = new LeaderBoardResponse();
        leaderBoardResponse.ranking = ranking;
        return leaderBoardResponse;
    }
}
