package com.example.demo.problem.application;

import com.example.demo.problem.controller.response.ProblemDetailResponse;
import com.example.demo.problem.controller.response.ProblemResponse;
import com.example.demo.problem.domain.api.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public List<ProblemResponse> getProblems(long start, long end) {
        return problemRepository.findByIdBetween(start, end)
                .stream()
                .map(ProblemResponse::from)
                .toList();
    }

    public ProblemDetailResponse getDetailProblem(long problemId) {
        return ProblemDetailResponse.from(problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제가 존재하지 않습니다.")));
    }
}
