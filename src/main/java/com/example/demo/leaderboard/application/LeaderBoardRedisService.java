package com.example.demo.leaderboard.application;


import com.example.demo.leaderboard.domain.RankingEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderBoardRedisService {

    private final StringRedisTemplate redisTemplate;

    public List<RankingEntry> getTopN(Long contestId, int n) {

        String key = "leaderboard:" + contestId;
        Set<ZSetOperations.TypedTuple<String>> topN = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n - 1);

        List<RankingEntry> result = new ArrayList<>();
        if (topN == null) return result;

        for (ZSetOperations.TypedTuple<String> tuple : topN) {
            Long userId = Long.parseLong(tuple.getValue());
            int score = tuple.getScore().intValue();
            result.add(RankingEntry.of(userId, score));
        }
        return result;
    }

    /**
     * 특정 contestId의 리더보드에 userId의 점수를 추가(또는 업데이트)합니다.
     * 51번째 이후의 점수는 삭제합니다.
     *
     * @param contestId 리더보드 식별용 contestId
     * @param userId 사용자 식별용 userId
     * @param score 점수(Integer)
     */
    public void addScore(long contestId, long userId, int score) {
        String key = "leaderboard:" + contestId;
        redisTemplate.opsForZSet().add(key, String.valueOf(userId), score);
        //redisTemplate.opsForZSet().removeRange(key, 50, -1);

        //  마지막 제출자가 낸 시간으로 부터 1시간 후 자동으로 만료되도록 설정.
        redisTemplate.expire(key, Duration.ofHours(1));
    }
}
