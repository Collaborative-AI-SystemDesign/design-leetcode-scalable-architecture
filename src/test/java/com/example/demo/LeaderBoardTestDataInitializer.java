package com.example.demo;

import com.example.demo.contest.domain.Contest;
import com.example.demo.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class LeaderBoardTestDataInitializer {
    @PersistenceContext
    private EntityManager em;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public void initData() {
        // 1️⃣ User 100000명
        for (long i = 1; i <= 100000; i++) {
            em.createNativeQuery("INSERT INTO user (id, nickname) VALUES (?, ?)")
                    .setParameter(1, i)
                    .setParameter(2, "user_" + i)
                    .executeUpdate();

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        // 2️⃣ Contest 520개
        for (long i = 1; i <= 520; i++) {
            em.createNativeQuery("INSERT INTO contests (id, name, start_time, end_time) VALUES (?, ?, ?, ?)")
                    .setParameter(1, i)
                    .setParameter(2, "Contest " + i)
                    .setParameter(3, LocalDateTime.now().minusDays(i))
                    .setParameter(4, LocalDateTime.now().plusDays(i))
                    .executeUpdate();

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        // 3️⃣ LeaderBoard 10,400,000개
        Random random = new Random();
        for (int i = 1; i <= 1_000_000; i++) {
            long contestId = 1 + random.nextInt(520);      // 1~520
            long userId = 1 + random.nextInt(100000);      // 1~100000
            int score = random.nextInt(1000);
            long timeTaken = random.nextInt(3600);

            em.createNativeQuery("INSERT INTO leaderboard (contest_id, user_id, score, time_taken) VALUES (?, ?, ?, ?)")
                    .setParameter(1, contestId)
                    .setParameter(2, userId)
                    .setParameter(3, score)
                    .setParameter(4, timeTaken)
                    .executeUpdate();

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
                System.out.printf("✅ LeaderBoard %d rows inserted...\n", i);
            }
        }

        System.out.println("✅ 데이터 생성 완료: User 100,000명, Contest 520개, LeaderBoard 1,000,000개");
    }
}
