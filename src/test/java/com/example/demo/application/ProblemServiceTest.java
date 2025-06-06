package com.example.demo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemServiceTest {

    @Autowired
    private ProblemTestDataInitializer problemTestDataInitializer;

    @BeforeEach
    public void setUp() {
        problemTestDataInitializer.initData();
    }

    @Test
    void testLeaderBoard() {
        // 테스트 코드 작성
    }
}
