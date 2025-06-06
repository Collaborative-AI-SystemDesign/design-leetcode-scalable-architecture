package com.example.demo.problem.domain;


import com.example.demo.example.domain.Example;
import com.example.demo.global.common.ProblemDto;
import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import com.example.demo.startercode.domain.Startercode;
import com.example.demo.testcases.domain.Testcase;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "problem")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Diffculties difficulty; // "Easy", "Medium", "Hard"

    @Enumerated(EnumType.STRING)
    private Catagories category;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "problem")
    private List<Example> examples;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "problem")
    private List<Startercode> startercodes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "problem")
    private List<Testcase> testcases;

    public static Problem toEntity(ProblemDto dto) {
        // 엔티티 생성
        Problem problem = Problem.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .difficulty(dto.getDifficulty())
                .category(dto.getCategory())
                .constraints(dto.getConstraints())
                .build();


        // 2. testcases 변환 후 문제와 연관관계 설정
        if (dto.getTestcases() != null) {
            Problem finalProblem = problem;
            List<Testcase> testcases = dto.getTestcases().stream()
                    .map(tcDto -> Testcase.toEntity(tcDto, finalProblem))
                    .toList();

            // Builder로 새 Problem 객체 만들어서 testcases 세팅
            problem = Problem.builder()
                    .id(problem.getId())
                    .title(problem.getTitle())
                    .description(problem.getDescription())
                    .difficulty(problem.getDifficulty())
                    .category(problem.getCategory())
                    .constraints(problem.getConstraints())
                    .testcases(testcases)
                    .build();
        }

        return problem;
    }
}
