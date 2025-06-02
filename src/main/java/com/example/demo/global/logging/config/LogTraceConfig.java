package com.example.demo.global.logging.config;

import com.example.demo.global.logging.logtrace.LogTrace;
import com.example.demo.global.logging.logtrace.MdcLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new MdcLogTrace();
    }
}
