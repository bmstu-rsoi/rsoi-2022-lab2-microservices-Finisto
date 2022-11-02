package com.finist.microservices2022.gatewayapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibraryPaginationResponse {

    public Integer page;

    public Integer pageSize;

    public Integer totalElements;

    List<LibraryResponse> items;

//    public LibraryPaginationResponse(Integer page, Integer pageSize, Integer totalElements, List<LibraryResponse> items) {
//        this.page = page;
//        this.pageSize = pageSize;
//        this.totalElements = totalElements;
//        this.items = items;
//    }
}
