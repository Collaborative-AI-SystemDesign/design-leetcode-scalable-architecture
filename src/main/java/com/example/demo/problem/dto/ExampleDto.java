package com.example.demo.problem.dto;

import com.example.demo.example.domain.Example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleDto {
    String input;
    String output;

    public static ExampleDto from(Example example) {
        return ExampleDto.builder()
                .input(example.getInput())
                .output(example.getOutput())
                .build();
    }
}
