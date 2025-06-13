package com.example.demo.submission.application;

import com.example.demo.contest.domain.Contest;
import com.example.demo.contest.domain.api.ContestRepository;
import com.example.demo.leaderboard.application.LeaderBoardRedisService;
import com.example.demo.leaderboard.domain.LeaderBoard;
import com.example.demo.leaderboard.domain.api.LeaderBoardRepository;
import com.example.demo.problem.controller.request.SubmissionRequest;
import com.example.demo.submission.domain.Submission;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ContestRepository contestRepository;
    private final LeaderBoardRedisService leaderBoardRedisService;
    private final LeaderBoardRepository leaderBoardRepository;

    @Transactional
    public void saveSubmissionAndLeaderboard(Submission submission, SubmissionRequest request, User user) {
        submissionRepository.save(submission);

        Long contestId = request.getContestId();
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
            LeaderBoard save = leaderBoardRepository.save(leaderBoard);
        }
    }
}
