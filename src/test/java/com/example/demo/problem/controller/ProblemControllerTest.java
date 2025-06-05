package com.example.demo.problem.controller;

import com.example.demo.global.common.ApiResponse;
import com.example.demo.global.enums.CodingLanguages;
import com.example.demo.problem.controller.request.SubmissionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ProblemControllerTest {

    RestClient restClient = RestClient.create("http://localhost:8080");

    @Test
    void getProblems() {
        ApiResponse response = restClient.get()
                .uri("/problems?start=3000&end=4000")
                .retrieve()
                .body(ApiResponse.class);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void getProblemById() {
        ApiResponse response = restClient.get()
                .uri("/problems/1")
                .retrieve()
                .body(ApiResponse.class);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());

        ApiResponse response2 = restClient.get()
                .uri("/problems/1")
                .retrieve()
                .body(ApiResponse.class);
        assertNotNull(response2);
        assertTrue(response2.isSuccess());
        assertNotNull(response2.getData());

        ApiResponse response3 = restClient.get()
                .uri("/problems/1")
                .retrieve()
                .body(ApiResponse.class);
        assertNotNull(response3);
        assertTrue(response3.isSuccess());
        assertNotNull(response3.getData());
    }


    @Test
    void submitProblem() {
        SubmissionRequest request = new SubmissionRequest(CodingLanguages.JAVA, "print('Hello, World!')");

        ApiResponse response = restClient.post()
                .uri("/problems/1/submission")
                .body(BodyInserters.fromValue(request)) // bodyValue → body(BodyInserters.fromValue(...))
                .retrieve()
                .body(ApiResponse.class);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }
}