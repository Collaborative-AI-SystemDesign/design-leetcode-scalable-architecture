package com.example.demo.application;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {


    RestClient restClient = RestClient.create("http://localhost:8080");


    @Test
    public void healthCheck() {
        String body = restClient.get()
                .uri("/health")
                .retrieve()
                .body(String.class);

        System.out.println(body);
    }
}