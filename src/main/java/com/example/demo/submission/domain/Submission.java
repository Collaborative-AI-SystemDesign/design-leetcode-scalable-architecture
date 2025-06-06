package com.example.demo.submission.domain;

import com.example.demo.user.domain.User;
import com.example.demo.global.enums.CodingLanguages;
import com.example.demo.global.enums.SubmissionStatus;
import com.example.demo.problem.domain.Problem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submission")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private CodingLanguages language;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status; // e.g., "Accepted", "Wrong Answer", "Runtime Error"

    private double runtime; // ms

    private double memory;  // MB

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;


    public static Submission of(String code, CodingLanguages language, SubmissionStatus status,
                                double runtime, double memory, User user, Problem problem) {
        return Submission.builder()
                .code(code)
                .language(language)
                .status(status)
                .runtime(runtime)
                .memory(memory)
                .submittedAt(LocalDateTime.now())
                .user(user)
                .problem(problem)
                .build();
    }
}
