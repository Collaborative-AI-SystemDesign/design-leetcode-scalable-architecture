package com.example.demo.problem.service;

import com.example.demo.global.rabbitmq.RabbitMqService;
import com.example.demo.problem.application.ProblemService;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.domain.api.ProblemApiRepository;
import com.example.demo.submission.domain.api.SubmissionRepository;
import com.example.demo.user.domain.api.UserApiRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class ProblemListTest {
    @Mock
    SubmissionRepository submissionRepository;
    @Mock
    ProblemApiRepository problemRepository;
    @Mock
    UserApiRepository userRepository;
    @Mock
    RabbitMqService rabbitMqService;
    @InjectMocks
    ProblemService problemService;

    @Test
    @DisplayName("커서 기반 페이징 성능 테스트")
    void testCursorPagingPerformance() {
        long cursor = 999900L;
        int size = 100;
        long totalFetched = 0;
        long start = System.currentTimeMillis();

        List<ProblemResponse> problems = problemService.getProblemsByCursor(cursor, size);

        totalFetched += problems.size();

        // 다음 커서를 설정
        cursor = problems.get(problems.size() - 1).getId();
        System.out.printf("%d 번째에서 %d 번째까지 조회: %d건\n", cursor,cursor+size, problems.size());

        long end = System.currentTimeMillis();
        System.out.printf("✅ 커서 기반 페이지 전체 조회 시간: %dms, 총 조회된 개수: %d건\n", (end - start), totalFetched);
    }

    @Test
    @DisplayName("Offset 기반 페이징 성능 테스트")
    void testOffsetPagingPerformance() {
        int page = 9999;
        int size = 100;
        long totalFetched = 0;
        long start = System.currentTimeMillis();

        // 페이지 요청
        Pageable pageable = PageRequest.of(page, size);
        Page<ProblemResponse> problemPage = problemService.getProblemPage(pageable);


        totalFetched += problemPage.getNumberOfElements();
        System.out.printf("%d 번째에서 %d 번째까지 조회: %d건\n", page*size,(page+1)*size, problemPage.getNumberOfElements());

        long end = System.currentTimeMillis();
        System.out.printf("✅ 오프셋 기반 페이지 전체 조회 시간: %dms, 총 조회된 개수: %d건\n", (end - start), totalFetched);
    }



}

