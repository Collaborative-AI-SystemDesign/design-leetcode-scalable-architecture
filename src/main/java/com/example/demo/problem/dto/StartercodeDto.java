package com.example.demo.problem.dto;

import com.example.demo.global.enums.CodingLanguages;
import com.example.demo.startercode.domain.Startercode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StartercodeDto {
    CodingLanguages codingLanguage;
    String code;

    public static StartercodeDto from(Startercode startercode) {
        return StartercodeDto.builder()
                .codingLanguage(startercode.getCodingLanguage())
                .code(startercode.getCode())
                .build();
    }
}
