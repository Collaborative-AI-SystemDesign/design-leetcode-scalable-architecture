package com.example.demo.user.domain;


import com.example.demo.submission.domain.Submission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private Long id;

    private String nickname; // 단순히 사용자 표시용 닉네임

    @OneToMany(mappedBy = "user")
    private List<Submission> submissions;

}
