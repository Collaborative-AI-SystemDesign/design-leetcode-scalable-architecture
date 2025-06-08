package com.example.demo.submission.domain.api;

import com.example.demo.submission.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionApiRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByEncodedCode(String encodedCode);
}
