//package com.example.demo;
//
//
//import com.example.demo.contest.domain.Contest;
//import com.example.demo.leaderboard.domain.LeaderBoard;
//import com.example.demo.leaderboard.domain.api.LeaderBoardRepository;
//import com.example.demo.user.domain.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestComponent;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Random;
//
//@Component
//public class LeaderBoardTestDataInitializer {
//    @PersistenceContext
//    private EntityManager em;
//
//    @Transactional
//    public void initData() {
////        // 1️⃣ User 100명
////        for (long i = 1; i <= 100; i++) {
////            User user = new User();
////            em.createNativeQuery("INSERT INTO user (id, nickname) VALUES (?, ?)")
////                    .setParameter(1, i)
////                    .setParameter(2, "user_" + i)
////                    .executeUpdate();
////        }
////
////        // 2️⃣ Contest 5개
////        for (long i = 1; i <= 5; i++) {
////            Contest contest = new Contest();
////            em.createNativeQuery("INSERT INTO contests (id, name, start_time, end_time) VALUES (?, ?, ?, ?)")
////                    .setParameter(1, i)
////                    .setParameter(2, "Contest " + i)
////                    .setParameter(3, LocalDateTime.now().minusDays(i))
////                    .setParameter(4, LocalDateTime.now().plusDays(i))
////                    .executeUpdate();
////        }
//
//        // 3️⃣ LeaderBoard 1000개
//        Random random = new Random();
//        for (int i = 0; i < 1000; i++) {
//            long contestId = 1;   // 1~5
//            long userId = 1 + random.nextInt(100);    // 1~100
//            int score = random.nextInt(1000);
//            long timeTaken = random.nextInt(3600);
//
//            em.createNativeQuery("INSERT INTO leaderboard (contest_id, user_id, score, time_taken) VALUES (?, ?, ?, ?)")
//                    .setParameter(1, contestId)
//                    .setParameter(2, userId)
//                    .setParameter(3, score)
//                    .setParameter(4, timeTaken)
//                    .executeUpdate();
//        }
//
//        System.out.println("✅ 데이터 생성 완료: User 100명, Contest 5개, LeaderBoard 1000개");
//    }
//}
