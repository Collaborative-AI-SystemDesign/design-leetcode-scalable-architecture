package com.example.demo.problem.controller.request;

import com.example.demo.global.enums.CodingLanguages;
import lombok.Getter;

@Getter
public class SubmissionRequest {
    private CodingLanguages codingLanguage;
    private String code;
}
