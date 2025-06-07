package com.example.demo.problem.service;

import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.domain.Problem;
import com.example.demo.problem.domain.api.ProblemApiRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
@Transactional
public class ProblemDetailsTest {

    @Autowired
    ProblemApiRepository problemRepository;

    @Autowired
    ProblemService problemService;

    @Autowired
    CacheManager cacheManager;

    Long testProblemId;

    @AfterEach
    void cleanUp() {
        if (testProblemId != null) {
            problemRepository.deleteById(testProblemId);
        }
    }

    @BeforeEach
    void setUp() {
        final int WARM_UP_COUNT = 10;

        Problem testProblem = createTestProblem();
        Problem savedProblem = problemRepository.save(testProblem);
        testProblemId = savedProblem.getId();

        cacheManager.getCacheNames().forEach(name ->
                Objects.requireNonNull(cacheManager.getCache(name)).clear()
        );

        System.out.println("웜업 중...");
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            problemService.getDetailProblem(testProblemId);
        }
    }

    @Test
    @DisplayName("@Cacheable vs 일반 조회 성능 비교")
    void compareCacheableVsNormalQuery() {
        System.out.println("=== @Cacheable vs 일반 조회 성능 비교 ===\n");

        // 첫 번째: 캐시 미스 (DB 조회)
        double cacheMiss = measureExecutionTime("첫 번째 @Cacheable 조회 (Cache Miss)",
                () -> problemService.getDetailProblemWithCache(testProblemId));

        // 두 번째: 캐시 히트
        double cacheHit = measureExecutionTime("두 번째 @Cacheable 조회 (Cache Hit)",
                () -> problemService.getDetailProblemWithCache(testProblemId));

        // 일반 조회 (캐시 없음)
        double dbSelect = measureExecutionTime("세 번째 DB 조회 (Non-Cache)",
                () -> problemService.getDetailProblem(testProblemId));

        System.out.printf("\n--- result ---\n");
        System.out.printf("캐시 미스: %f ms\n", cacheMiss);
        System.out.printf("캐시 히트: %f ms\n", cacheHit);
        System.out.printf("DB 조회: %f ms\n", dbSelect);

        double cacheImprovement = ((dbSelect - cacheHit) / dbSelect) * 100;
        System.out.printf("캐시 성능 향상: %.1f%%\n\n", cacheImprovement);
    }

    @Test
    @DisplayName("반복 조회 성능 비교")
    void compareRepeatedQueriesWithWarmUp() {
        final int TEST_ITERATIONS = 100;
        System.out.println("=== 반복 조회 성능 비교 ===\n");

        double dbSelectRepeated = measureExecutionTime(
                String.format("일반 조회 %d회 반복", TEST_ITERATIONS),
                () -> {
                    for (int i = 0; i < TEST_ITERATIONS; i++) {
                        problemService.getDetailProblem(testProblemId);
                    }
                }
        );

        double cachedRepeated = measureExecutionTime(
                String.format("@Cacheable 조회 %d회 반복", TEST_ITERATIONS),
                () -> {
                    for (int i = 0; i < TEST_ITERATIONS; i++) {
                        problemService.getDetailProblemWithCache(testProblemId);
                    }
                }
        );

        System.out.printf("\n--- 반복 조회 결과 ---\n");
        System.out.printf("일반 조회 %d회: %f ms (평균 %.2f ms/회)\n",
                TEST_ITERATIONS, dbSelectRepeated, dbSelectRepeated / TEST_ITERATIONS);
        System.out.printf("캐시 조회 %d회: %f ms (평균 %.2f ms/회)\n",
                TEST_ITERATIONS, cachedRepeated, cachedRepeated / TEST_ITERATIONS);

        double cacheImprovement = ((dbSelectRepeated - cachedRepeated) / dbSelectRepeated) * 100;
        System.out.printf("전체 성능 향상: %.1f%%\n\n", cacheImprovement);
    }

    private double measureExecutionTime(String executionName, Runnable executionFunction) {
        long startTime = System.nanoTime();
        executionFunction.run();
        long endTime = System.nanoTime();
        double millis = (endTime - startTime) / 1_000_000.0;

        System.out.printf("%-40s: %.3f ms\n", executionName, millis);
        return millis;
    }



    private Problem createTestProblem() {
        return Problem.builder()
                .title("복잡한 성능 테스트 문제")
                .description("이 문제는 캐시 성능 테스트를 위한 복잡한 데이터를 포함합니다. " + "설명 ".repeat(100))
                .difficulty(Diffculties.HARD)
                .category(Catagories.STRING)
                .constraints("매우 복잡한 제약 조건들... " + "제약 ".repeat(50))
                .examples(java.util.Collections.emptyList())
                .startercodes(java.util.Collections.emptyList())
                .testcases(java.util.Collections.emptyList())
                .build();
    }
}
