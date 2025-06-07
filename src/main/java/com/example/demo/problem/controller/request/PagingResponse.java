package com.example.demo.problem.controller.request;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagingResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PagingResponse<T> from(Page<T> page) {
        return new PagingResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}