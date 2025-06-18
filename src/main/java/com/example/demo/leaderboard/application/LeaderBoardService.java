package com.example.demo.leaderboard.application;


import com.example.demo.contest.domain.Contest;
import com.example.demo.contest.domain.api.ContestRepository;
import com.example.demo.leaderboard.controller.response.LeaderBoardResponse;
import com.example.demo.leaderboard.domain.LeaderBoard;
import com.example.demo.leaderboard.domain.RankingEntry;
import com.example.demo.leaderboard.domain.api.LeaderBoardRepository;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LeaderBoardService {
    private final LeaderBoardRepository leaderBoardRepository;
    private final LeaderBoardRedisService leaderBoardRedisService;
    private final ContestRepository contestRepository;

    public LeaderBoardResponse getLeaderBoardInfo(Long contestId) {

        return LeaderBoardResponse.from(leaderBoardRepository.getTop50LeaderBoardByContestId(contestId)
                .stream()
                .map(RankingEntry::from)
                .toList());
    }

    /***
     * 1. submission 때 rdb 뿐만 아니라 redis에도 데이터 저장
     * 2. 조회시 redis 확인 후 없으면 rdb에서 가져와서 처리 그 후에 redis 에 데이터 적재
     * 3. contest라면 1시간 마다 ttl로 캐시 삭제, 대회 종료 후 15분안에 전채 데이터 삭제
     * 4. 대회 종료 후 30분 후에는 redis 키 삭제
     */

    public LeaderBoardResponse getLeaderBoardWithCache(Long contestId) {
        List<RankingEntry> rankingEntries = leaderBoardRedisService.getTopN(contestId, 50);

        // 1. Redis에 데이터가 없다면
//        if (rankingEntries.isEmpty()) {
//            // 2. RDB에서 조회
//            List<RankingEntry> entriesFromRDB = leaderBoardRepository.getTop50LeaderBoardByContestId(contestId)
//                    .stream()
//                    .map(RankingEntry::from)
//                    .toList();
//
//            // 3. Redis에 적재
//            for (RankingEntry entry : entriesFromRDB) {
//                leaderBoardRedisService.addScore(contestId, entry.getUserId(), entry.getScore());
//            }
//
//            rankingEntries = entriesFromRDB;
//        }

        return LeaderBoardResponse.from(rankingEntries);
    }

    @Transactional
    public void saveLeaderboard(Long contestId, User user) {

        if (contestId != null) {
            Contest contest = contestRepository.findById(contestId).orElseThrow();
//            // 테스트용 코드로 score를 500 ~ 999점 사이로 넣는다.
//            int score = 500 + new Random().nextInt(999 - 500 + 1);
//            // 100~ 4000 사이로 랜덤값이 나오는데 score에 따라서 계산됨.
//            long timeTaken = (long) (4000 - (4000 - 100) * (new Random().nextInt(1000) / 999.0));

            int score = ThreadLocalRandom.current().nextInt(500, 999);
            long timeTaken = ThreadLocalRandom.current().nextLong(100, 4000);

            // save user score info to Redis
            leaderBoardRedisService.addScore(contestId,user.getId(), score);

            // save leaderboard info to the RDB
            LeaderBoard leaderBoard = LeaderBoard.toEntity(score, timeTaken, contest, user);
            leaderBoardRepository.save(leaderBoard);
        }
    }
}
