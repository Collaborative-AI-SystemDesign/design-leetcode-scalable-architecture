package com.example.demo.problem.domain;


import com.example.demo.example.domain.Example;
import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import com.example.demo.startercode.domain.Startercode;
import com.example.demo.testcases.domain.Testcase;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
