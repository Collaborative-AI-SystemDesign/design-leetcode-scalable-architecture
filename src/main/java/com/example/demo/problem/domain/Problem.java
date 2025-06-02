package com.example.demo.problem.domain;


import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem {

    @Id
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

}
