package com.example.demo.leaderboard.domain.api;


import com.example.demo.leaderboard.domain.LeaderBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Long> {


    // Leaderboard

    // index 추가
    // contestId
    // score desc
    // CREATE INDEX idx_contest_score ON LeaderBoard (contest_id, score DESC);

    // redis
    // sortedset

    @Query(
            value = """
        select leaderBoard_id
             , contest_id
             , user_id
             , score
             , time_taken    
          from leaderboard
         where contest_id=:contestId 
        order by score desc
            limit 50
    """, nativeQuery = true
    )
    List<LeaderBoard> getTop50LeaderBoardByContestId(@Param("contestId") Long contestId);
}
