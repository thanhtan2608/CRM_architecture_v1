package org.example.template_architecture.domain.entity;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {
    private List<T> items;
    private int totalPages;
    private int currentPage;
    private long totalElements;

    public PageResult(List<T> items, int totalPages, int currentPage, long totalElements) {
        this.items = items;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
}
}
