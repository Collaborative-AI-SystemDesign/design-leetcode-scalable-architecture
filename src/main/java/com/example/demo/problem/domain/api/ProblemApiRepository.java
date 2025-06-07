package com.example.demo.problem.domain.api;

import com.example.demo.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemApiRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByIdBetween(long start, long end);
    List<Problem> findByIdGreaterThan(Long cursor, org.springframework.data.domain.Pageable pageable);
}
