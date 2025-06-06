package com.example.demo.user.domain;


import com.example.demo.submission.domain.Submission;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname; // 단순히 사용자 표시용 닉네임

    @OneToMany(mappedBy = "user")
    private List<Submission> submissions;

    public static User of(Long id, String nickname) {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }

}
