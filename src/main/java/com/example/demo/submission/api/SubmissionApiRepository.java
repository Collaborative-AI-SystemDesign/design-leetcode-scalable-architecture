package com.example.demo.submission.api;

import com.example.demo.submission.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionApiRepository extends JpaRepository<Submission, Long> {
}
