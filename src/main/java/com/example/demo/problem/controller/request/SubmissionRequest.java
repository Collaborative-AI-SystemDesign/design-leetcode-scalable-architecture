package com.example.demo.problem.controller.request;

import com.example.demo.global.enums.CodingLanguages;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionRequest {
    private CodingLanguages codingLanguage;
    private String code;
}
