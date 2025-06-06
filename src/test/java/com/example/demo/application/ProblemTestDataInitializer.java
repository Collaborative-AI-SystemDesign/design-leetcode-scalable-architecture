package com.example.demo.application;

import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import com.example.demo.problem.domain.Problem;
import com.example.demo.testcases.domain.Testcase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
public class ProblemTestDataInitializer {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void initData() {
        // 1️⃣ Problem 1개 생성
        em.createNativeQuery("""
            INSERT INTO problem (id, title, description, difficulty, category, constraints)
            VALUES (?, ?, ?, ?, ?, ?)
            """)
                .setParameter(1, 1L)
                .setParameter(2, "Sample Problem")
                .setParameter(3, "A simple addition problem.")
                .setParameter(4, Diffculties.EASY.name())           // Enum 값 (Diffculties)
                .setParameter(5, Catagories.ARRAY.name())           // Enum 값 (Categories)
                .setParameter(6, "No constraints.")
                .executeUpdate();

        // 2️⃣ Testcase 3개 생성
        em.createNativeQuery("""
            INSERT INTO testcases (problem_id, input, expected_output)
            VALUES (?, ?, ?)
            """)
                .setParameter(1, 1L)
                .setParameter(2, "1 2")
                .setParameter(3, "3")
                .executeUpdate();

        em.createNativeQuery("""
            INSERT INTO testcases (problem_id, input, expected_output)
            VALUES (?, ?, ?)
            """)
                .setParameter(1, 1L)
                .setParameter(2, "3 5")
                .setParameter(3, "8")
                .executeUpdate();

        em.createNativeQuery("""
            INSERT INTO testcases (problem_id, input, expected_output)
            VALUES (?, ?, ?)
            """)
                .setParameter(1, 1L)
                .setParameter(2, "10 20")
                .setParameter(3, "30")
                .executeUpdate();

        System.out.println("✅ 테스트 데이터 생성 완료: Problem 1개, Testcase 3개");
    }
}
