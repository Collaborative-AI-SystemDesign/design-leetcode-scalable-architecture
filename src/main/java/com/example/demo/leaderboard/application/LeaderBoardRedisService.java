package com.example.demo.leaderboard.application;


import com.example.demo.leaderboard.domain.RankingEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderBoardRedisService {

    private final StringRedisTemplate redisTemplate;

    public List<RankingEntry> getTopN(Long contestId, int n) {

        log.info("*****************************getTopN start******************************");
        String key = "leaderboard:" + contestId;
        Set<ZSetOperations.TypedTuple<String>> topN = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n - 1);

        List<RankingEntry> result = new ArrayList<>();
        if (topN != null) {
            for (ZSetOperations.TypedTuple<String> tuple : topN) {
                Long userId = Long.parseLong(tuple.getValue());
                int score = tuple.getScore().intValue();
                result.add(RankingEntry.from(userId, score));
            }
        }
        return result;
    }

    /**
     * 특정 contestId의 리더보드에 userId의 점수를 추가(또는 업데이트)합니다.
     *
     * @param contestId 리더보드 식별용 contestId
     * @param userId 사용자 식별용 userId
     * @param score 점수(Integer)
     */
    public void addScore(long contestId, long userId, int score) {
        String key = "leaderboard:" + contestId;
        redisTemplate.opsForZSet().add(key, String.valueOf(userId), score);
    }
}
