package com.example.demo.problem.controller.request;

import com.example.demo.global.enums.CodingLanguages;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SubmissionRequest {
    private CodingLanguages codingLanguage;
    private String code;
    private Long contestId;
    private Long userId;

    public SubmissionRequest() {
    }

    public SubmissionRequest(CodingLanguages codingLanguage, String code, Long userId) {
        this.codingLanguage = codingLanguage;
        this.code = code;
        this.userId = userId;
    }

    public SubmissionRequest(CodingLanguages codingLanguage, Long contestId, String code, Long userId) {
        this.codingLanguage = codingLanguage;
        this.contestId = contestId;
        this.code = code;
        this.userId = userId;
    }
}
