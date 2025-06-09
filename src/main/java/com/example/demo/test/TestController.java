package com.example.demo.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final RabbitMQSender sender;

    @GetMapping("/v1/test/{userId}")
    public TestEntity request(@PathVariable Long userId) {
        return testService.orderItem(userId);
    }

    @GetMapping("/mq-test")
    public String testMq() {
        sender.send("Hello, RabbitMQ!");
        return "Message sent to RabbitMQ!";
    }
}
