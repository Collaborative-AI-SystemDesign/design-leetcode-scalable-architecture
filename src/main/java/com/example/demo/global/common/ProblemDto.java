package com.example.demo.global.common;


import com.example.demo.global.enums.Catagories;
import com.example.demo.global.enums.Diffculties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {

    private Long id;
    private String title;
    private String description;
    private Diffculties difficulty;
    private Catagories category;
    private String constraints;
    private List<TestcaseDto> testcases;
}
