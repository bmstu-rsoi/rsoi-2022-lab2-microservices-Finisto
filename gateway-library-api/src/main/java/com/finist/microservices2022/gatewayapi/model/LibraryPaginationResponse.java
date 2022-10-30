package com.finist.microservices2022.gatewayapi.model;

import java.util.List;

public class LibraryPaginationResponse {

    public Integer page;

    public Integer pageSize;

    public Integer totalElements;

    List<LibraryResponse> items;

    public LibraryPaginationResponse(Integer page, Integer pageSize, Integer totalElements, List<LibraryResponse> items) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.items = items;
    }
}
