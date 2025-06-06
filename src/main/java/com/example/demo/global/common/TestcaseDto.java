package com.example.demo.global.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestcaseDto {
    public Long id;
    public String input;
    public String expectedOutput;
}
