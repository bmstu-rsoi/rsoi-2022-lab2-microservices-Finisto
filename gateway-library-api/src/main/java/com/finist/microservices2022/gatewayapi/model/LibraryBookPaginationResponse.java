package com.finist.microservices2022.gatewayapi.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBookPaginationResponse {

    public int page;

    public int pageSize;

    public int totalElements;

    public List<LibraryBookResponse> items;

}
