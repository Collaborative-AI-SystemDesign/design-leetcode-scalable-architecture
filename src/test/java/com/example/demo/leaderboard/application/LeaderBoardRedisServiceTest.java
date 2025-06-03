package com.example.demo.leaderboard.application;

import com.example.demo.leaderboard.domain.RankingEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class LeaderBoardRedisServiceTest {

    @Autowired
    private LeaderBoardRedisService leaderBoardRedisService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @BeforeEach
    void cleanRedis() {
        redisTemplate.delete("leaderboard:1001");
    }

    @Test
    void addScore_andGetTopN_Test() {
        long contestId = 1001L;
        long userId1 = 2001L;
        long userId2 = 2002L;
        int score1 = 500;
        int score2 = 800;

        // when
        leaderBoardRedisService.addScore(contestId, userId1, score1);
        leaderBoardRedisService.addScore(contestId, userId2, score2);

        List<RankingEntry> topN = leaderBoardRedisService.getTopN(contestId, 10);

        // then
        assertThat(topN).hasSize(2);
        assertThat(topN.get(0).getUserId()).isEqualTo(userId2); // 높은 점수가 먼저
        assertThat(topN.get(0).getScore()).isEqualTo(score2);

        assertThat(topN.get(1).getUserId()).isEqualTo(userId1);
        assertThat(topN.get(1).getScore()).isEqualTo(score1);


    }

}