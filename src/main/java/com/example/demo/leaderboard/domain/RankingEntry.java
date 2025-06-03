package com.example.demo.leaderboard.domain;


import lombok.Getter;

@Getter
public class RankingEntry {
    private Long userId;
    private int score;

    public static RankingEntry from(LeaderBoard leaderBoard) {
        RankingEntry rankingEntry = new RankingEntry();
        rankingEntry.userId = leaderBoard.getUser().getId();
        rankingEntry.score = leaderBoard.getScore();
        return rankingEntry;
    }

    public static RankingEntry of(Long userId, Integer score) {
        RankingEntry rankingEntry = new RankingEntry();
        rankingEntry.userId = userId;
        rankingEntry.score = score;
        return rankingEntry;
    }
}
