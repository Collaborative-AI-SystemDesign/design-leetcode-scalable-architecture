package com.example.demo.testcases.domain;

import com.example.demo.global.common.TestcaseDto;
import com.example.demo.problem.domain.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "testcases")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Testcase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    public static Testcase toEntity(TestcaseDto dto, Problem problem) {
        return Testcase.builder()
                .id(dto.id)
                .input(dto.input)
                .expectedOutput(dto.expectedOutput)
                .problem(problem) // 양방향 연관관계 설정
                .build();
    }
}
