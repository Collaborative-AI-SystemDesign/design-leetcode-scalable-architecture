package com.example.demo.domain.entity;


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
