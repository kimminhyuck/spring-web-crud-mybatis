package com.myproject.util;

public class Pagination {
    private int totalItems;
    private int currentPage;
    private int totalPages;
    private int pageSize;

    public Pagination(int totalItems, int currentPage, int pageSize) {
        this.totalItems = totalItems;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getTotalItems() { return totalItems; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
    public int getPageSize() { return pageSize; }
}
