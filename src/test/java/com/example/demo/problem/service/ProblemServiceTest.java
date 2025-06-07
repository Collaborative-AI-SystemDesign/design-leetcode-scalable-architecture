package com.example.demo.problem.service;

import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.response.ProblemResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProblemServiceTest {

    @Nested
    @SpringBootTest
    class ProblemPerformanceTest {

        @Autowired
        private ProblemService problemService;

        @Test
        void testFullQueryPerformance() {
            int size = 1_00_000;;

            // 전체 조회
            long fullStart = System.currentTimeMillis();
            List<ProblemResponse>  problemResponses =problemService.getProblems(0, size);
            long fullEnd = System.currentTimeMillis();
            System.out.println("전체 조회 시간: " + (fullEnd - fullStart) + "ms, 개수: " + problemResponses.size());
        }


        @Test
        void testCursorPagingPerformance() {
            long cursor = 0L;
            int size = 100;
            long totalFetched = 0;
            long start = System.currentTimeMillis();

            while (true) {
                List<ProblemResponse> problems = problemService.getProblemsByCursor(cursor, size);
                if (problems.isEmpty()) break;

                totalFetched += problems.size();

                // 다음 커서를 설정
                cursor = problems.get(problems.size() - 1).getId(); // id()는 ProblemResponse의 필드에 맞게 수정

                System.out.printf("Cursor %d까지 조회: %d건\n", cursor, problems.size());
            }

            long end = System.currentTimeMillis();
            System.out.printf("✅ 커서 기반 페이지 전체 조회 시간: %dms, 총 조회된 개수: %d건\n", (end - start), totalFetched);
        }

        @Test
        void testOffsetPagingPerformance() {
            int page = 0;
            int size = 100;
            long totalFetched = 0;
            long start = System.currentTimeMillis();

            while (true) {
                Page<ProblemResponse> problemPage = problemService.getProblemPage(page, size);

                if (problemPage.isEmpty()) break;

                totalFetched += problemPage.getNumberOfElements();
                System.out.printf("페이지 %d 조회: %d건\n", page, problemPage.getNumberOfElements());

                page++;
            }

            long end = System.currentTimeMillis();
            System.out.printf("✅ 오프셋 기반 페이지 전체 조회 시간: %dms, 총 조회된 개수: %d건\n", (end - start), totalFetched);
        }

    }
}
